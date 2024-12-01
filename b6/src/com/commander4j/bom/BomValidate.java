package com.commander4j.bom;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;

public class BomValidate
{
	UUID uuid = UUID.randomUUID();
	JDBBom bomDB = new JDBBom(Common.selectedHostID,Common.sessionID);
	public static Integer mode_firstAvailable = 0;
	public static Integer mode_adjacent = 1;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(BomValidate.class);

	public BigDecimal getRequiredQuantity(String bom_id, String bom_version,String stage)
	{
		BigDecimal result = new BigDecimal(0);

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(Common.selectedHostID).getConnection(Common.sessionID).prepareStatement("select * from VIEW_BOM where bom_id = ? and bom_version = ? and input_output = ? and stage = ?");

			stmt.setString(1, bom_id);
			stmt.setString(2, bom_version);
			stmt.setString(3, "output");
			stmt.setString(4, stage);

			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = rs.getBigDecimal("quantity");
			}

			stmt.clearParameters();
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			logger.debug(e.getMessage());
		}

		return result;
	}

	public void allocateLocations(String bom_id, String bom_version, String inputoutput,String stage, int mode)
	{

		BigDecimal outputQuantity = getRequiredQuantity(bom_id, bom_version,stage);

		LinkedList<BomMaterials> material = new LinkedList<BomMaterials>();

		JDBViewBomRecord vbr = new JDBViewBomRecord(Common.selectedHostID,Common.sessionID);

		JDBBomDefaults def = new JDBBomDefaults(Common.selectedHostID,Common.sessionID);

		LinkedList<JDBViewBomRecord> vblist = vbr.getBomMaterials(bom_id, bom_version, inputoutput,stage);

		int locationsRequired = 0;

		logger.debug("");

		for (int x = 0; x < vblist.size(); x++)
		{
			BigDecimal materialRequiredQuantity = vblist.get(x).getQuantity();
			
			locationsRequired = 0;

			do 
			{
				locationsRequired++;
				materialRequiredQuantity = materialRequiredQuantity.subtract(outputQuantity);
			}
			while (materialRequiredQuantity.compareTo(outputQuantity) >= 0);
			
			BomMaterials matLocation = new BomMaterials();

			matLocation.setMaterial(vblist.get(x).getMaterial());
			matLocation.setMaterial_uuid(vblist.get(x).getMaterial_uuid());
			matLocation.setDefault_location(def.defaultValue("material", vblist.get(x).getMaterial(), "location_id"));
			matLocation.setLocationsRequired(locationsRequired);

			boolean found = false;
			for (int y = 0; y < material.size(); y++)
			{
				if (material.get(y).getMaterial_uuid().equals(matLocation.getMaterial_uuid()))
				{
					found = true;
				}
			}

			if (found == false)
			{
				material.addLast(matLocation);
				logger.debug(matLocation);
			}
		}

		logger.debug("");

		if (mode == mode_firstAvailable)
		{
			logger.debug("<<<<<< Mode = firstFree >>>>>>");
			Collections.sort(material, Comparator.comparingInt(BomMaterials::getSortKey));

			for (int x = 0; x < material.size(); x++)
			{
				logger.debug(material.get(x));
			}
		}
		else
		{
			logger.debug("<<<<<< Mode = adjacent >>>>>>");
		}

		JDBBomList ll = new JDBBomList(Common.selectedHostID,Common.sessionID);

		LinkedList<String> availableLocations = ll.getValues(Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBBomList.getLocationIDs"), "ITEM");
		LinkedList<String> assignedLocations = new LinkedList<String>();

		HashMap<String, JDBBomRecord> materialNewlyAssignedLocations = new HashMap<String, JDBBomRecord>();
		HashMap<String, JDBBomRecord> materialCurrentlyAssignedLocations = new HashMap<String, JDBBomRecord>();

		logger.debug("");

		logger.debug("Available Locations " + availableLocations);

		boolean newLocationRequired = false;

		String defaultAssignment = "";
		assignedLocations.clear();

		for (int x = 0; x < material.size(); x++)
		{
			logger.debug("\n***********************************");
			logger.debug("Processing " + material.get(x).material);

			logger.debug("Locations Required " + material.get(x).getLocationsRequired());

			materialCurrentlyAssignedLocations.clear();
			materialNewlyAssignedLocations.clear();

			for (int y = 1; y <= material.get(x).getLocationsRequired(); y++)
			{
				System.out.print("\n====Location " + y + " of " + material.get(x).getLocationsRequired() + "====\n");

				logger.debug("Available Locations = " + availableLocations);

				defaultAssignment = (material.get(x).default_location);

				if (defaultAssignment.equals(""))
				{
					newLocationRequired = true;
				}
				else
				{
					if (availableLocations.contains(defaultAssignment) == false)
					{
						newLocationRequired = true;
					}
				}

				if (newLocationRequired)
				{
					if (availableLocations.size() > 0)
					{
						defaultAssignment = availableLocations.getFirst();
					}
				}

				// logger.debug("Before assignedLocations = "+assignedLocations);
				// logger.debug("Before availableLocations =
				// "+availableLocations);

				logger.debug("Preferred Location  = [" + defaultAssignment + "]");

				assignedLocations.add(defaultAssignment);

				
				JDBBomRecord newLocation = new JDBBomRecord(Common.selectedHostID,Common.sessionID);
				newLocation.setBOMId(bom_id);
				newLocation.setBOMVersion(bom_version);
				newLocation.setDataId("location_id");
				newLocation.setDataType("string");
				newLocation.setDataString(defaultAssignment);
				newLocation.setParent_uuid(material.get(x).getMaterial_uuid());
				newLocation.setUuid(UUID.randomUUID().toString());
				newLocation.setBOMSequence(bomDB.getSequence("location_id", material.get(x).getMaterial_uuid()));
				
				materialNewlyAssignedLocations.put(defaultAssignment, newLocation);

				availableLocations.remove(defaultAssignment);

				// logger.debug("Assigned Locations = "+assignedLocations);
				logger.debug("Assign " + materialNewlyAssignedLocations + " to [" + material.get(x).material + "]");

			}

			// Get locations currently assigned.

			JDBBom bom = new JDBBom(Common.selectedHostID,Common.sessionID);

			JDBBomRecord rec = bom.getProperties(material.get(x).getMaterial_uuid());

			LinkedList<JDBBomRecord> child = bom.getChildUUIDs(rec);

			for (int z = child.size() - 1; z >= 0; z--)
			{
				if (child.get(z).getDataId().equals("location_id") == false)
				{
					child.remove(z);
				}
			}

			for (int z = 0; z < child.size(); z++)
			{
				// logger.debug("current location assigned " + child.get(z));
				materialCurrentlyAssignedLocations.put(child.get(z).getDataString(), child.get(z));
			}

			// Collections.sort(currentLocations);
			logger.debug("Currently Assigned = " + materialCurrentlyAssignedLocations);


			String checkCreate = "";
			int sequenceIncrement = 0;

			for (HashMap.Entry<String, JDBBomRecord> set : materialNewlyAssignedLocations.entrySet())
			{
				checkCreate = set.getKey();


				if (materialCurrentlyAssignedLocations.containsKey(checkCreate) == false)
				{
					sequenceIncrement = sequenceIncrement + 10;
					
					set.getValue().setBOMSequence(set.getValue().getBOMSequence()+sequenceIncrement);
					
					bomDB.writeRecord( set.getValue());
				}
			}

			String checkDelete = "";

			for (HashMap.Entry<String, JDBBomRecord> set : materialCurrentlyAssignedLocations.entrySet())
			{
				checkDelete = set.getKey();
				
				if (materialNewlyAssignedLocations.containsKey(checkDelete) == false)
				{
					bomDB.deleteUUID(set.getValue().getUuid());
				}
			}


			logger.debug("***********************************");
		}

		logger.debug("\n");
		logger.debug("Final assignedLocations  = " + assignedLocations);
		logger.debug("Final availableLocations = " + availableLocations);
	}
}
