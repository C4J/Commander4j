package com.commander4j.bom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBListData;
import com.commander4j.sys.Common;

public class JDBBomStructure
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBBomStructure.class);
	JDBBomStructureRecord structureRecord = new JDBBomStructureRecord();
	JDBBomElement bomElement;

	private String hostID;
	private String sessionID;
	private String dbErrorMessage;

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private void setErrorMessage(String ErrorMsg)
	{
		dbErrorMessage = ErrorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private String getHostID()
	{
		return hostID;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public JDBBomStructure(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		bomElement = new JDBBomElement(getHostID(), getSessionID());

	}

	public JDBBomStructureRecord getProperties(JDBBomStructureRecord rec)
	{
		JDBBomStructureRecord currentRecord = new JDBBomStructureRecord();

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomStructure.getProperties"));

			stmt.setString(1, rec.getDataId());
			stmt.setString(2, rec.getDataIDParent());

			rs = stmt.executeQuery();

			if (rs.next())
			{
				currentRecord.getPropertiesFromResultSet(rs);

			}

			stmt.clearParameters();
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		return currentRecord;
	}

	public boolean isValid(JDBBomStructureRecord rec)
	{
		boolean result = false;

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomStructure.getProperties"));

			stmt.setString(1, rec.getDataId());
			stmt.setString(2, rec.getDataIDParent());

			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;

			}

			stmt.clearParameters();
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		return result;
	}

	public LinkedList<JDBBomStructureRecord> getAllowedChildren(JDBBomRecord rec)
	{
		LinkedList<JDBBomStructureRecord> result = new LinkedList<JDBBomStructureRecord>();

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomStructure.getAllowedChildren"));

			stmt.setString(1, rec.getDataId());

			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBBomStructureRecord newrec = new JDBBomStructureRecord();

				newrec.getPropertiesFromResultSet(rs);
				result.add(newrec);
			}

			stmt.clearParameters();
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		return result;
	}

	public boolean delete(JDBBomStructureRecord rec)
	{
		boolean result = true;

		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomStructure.delete"));

			stmtupdate.setString(1, rec.getDataId());
			stmtupdate.setString(2, rec.getDataIDParent());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}

		return result;
	}

	private boolean create(JDBBomStructureRecord rec)
	{
		boolean result = true;
		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomStructure.create"));

			stmtupdate.setString(1, rec.getDataId());
			stmtupdate.setString(2, rec.getDataIDParent());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}
		return result;

	}
	
	public boolean isBlankRecord(JDBBomStructureRecord rec)
	{
		boolean result = true;
		
		if (rec.getDataId().equals("")==false)
		{
			result = false;
		}
		
		if (rec.getDataIDParent().equals("")==false)
		{
			result = false;
		}
		
		
		return result;
	}

	public boolean update(JDBBomStructureRecord fromrec, JDBBomStructureRecord torec)
	{
		boolean result = false;

		logger.debug("fromrec data_id = ["+fromrec.getDataId()+"] parent_data_id = ["+fromrec.getDataIDParent()+"]");
		logger.debug("torec   data_id = ["+torec.getDataId()+"] parent_data_id = ["+torec.getDataIDParent()+"]");
		
		// compare old and new to see if a change has been made

		// if old <> new and new does not exist

		boolean changed = true;

		if (fromrec.getDataId().equals(torec.getDataId()))
		{
			if (fromrec.getDataIDParent().equals(torec.getDataIDParent()))
			{

					changed = false;

			}
		}

		if (changed)
		{
			logger.debug("fromrec and torec are DIFFERENT");
			
			if (isValid(torec))
			{
				// New record already exists

				logger.debug("torec already exists - abort update");
				result = false;
				setErrorMessage("Duplicate record cannot be created.");
			}
			else
			{
				if ((isValid(fromrec) || !isBlankRecord(fromrec)))
				{
					// Rename - UPDATE
					logger.debug("fromrec already exists - apply update sql");
					
					try
					{
						PreparedStatement stmtupdate;

						stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomStructure.update"));

						stmtupdate.setString(1, torec.getDataId());
						stmtupdate.setString(2, torec.getDataIDParent());

						stmtupdate.setString(3, fromrec.getDataId());
						stmtupdate.setString(4, fromrec.getDataIDParent());

						logger.debug(stmtupdate);
						
						stmtupdate.execute();
						stmtupdate.clearParameters();
						Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

						stmtupdate.close();
						
						result = true;
					}
					catch (Exception e)
					{
						logger.error(e.getMessage());
						result = false;
					}
				}
				else
				{
					// CREATE
					logger.debug("fromrec does not exist - apply create sql");
					result = create(torec);
				}
			}
		}
		else
		{
			// No changes made
			logger.debug("from and to are SAME");
			result = true;
		}

		return result;
	}

	public LinkedList<JDBListData> getStructureIds()
	{
		LinkedList<JDBListData> structureList = new LinkedList<JDBListData>();

		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomStructure.getList"));

			rs = stmt.executeQuery();

			while (rs.next())

			{
				JDBBomStructureRecord currentRecord = new JDBBomStructureRecord();
				currentRecord.getPropertiesFromResultSet(rs);
				
				bomElement.getProperties(currentRecord.getDataIDParent());
				icon = bomElement.getElementRecord().getIcon();
				
				JDBListData mld = new JDBListData(icon, index, true, currentRecord);
				structureList.addLast(mld);
			}

			stmt.clearParameters();
			rs.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
		}

		return structureList;
	}

}
