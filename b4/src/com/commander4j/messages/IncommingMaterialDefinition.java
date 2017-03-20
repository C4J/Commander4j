package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : IncommingMaterialDefinition.java
 * 
 * Package Name : com.commander4j.messages
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import java.math.BigDecimal;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBDataIDs;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialCustomerData;
import com.commander4j.db.JDBMaterialLocation;
import com.commander4j.db.JDBMaterialType;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBUom;
import com.commander4j.util.JUtility;

public class IncommingMaterialDefinition
{

	private String hostID;
	private String sessionID;
	private String errorMessage;
	private String material;
	private String materialType;
	private String description;
	private String base_uom;
	private String production_uom;
	private String issue_uom;
	private String shelf_life_uom;
	private String shelf_life;
	private String shelf_life_rule;
	private String equipment_type;
	private String gross_weight;
	private String net_weight;
	private String weight_uom;
	private String old_material;
	private String inspection_id;
	private String default_batch_status;
	private Boolean repeat = true;
	private Boolean repeat2 = true;
	private String key;
	private int seq;
	private int seq2;
	private String uom;
	private String location;
	private String numerator;
	private String override;
	private String denominator;
	private String ean;
	private String variant;
	private String status;
	private String customerID;
	private String dataID;
	private String dataValue;
	private String customer_name;

	public String getErrorMessage()
	{
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getHostID()
	{
		return hostID;
	}

	public void setHostID(String hostID)
	{
		this.hostID = hostID;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}

	public IncommingMaterialDefinition(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh)
	{
		Boolean result = true;

		JDBMaterial mat = new JDBMaterial(getHostID(), getSessionID());
		JDBMaterialType mattype = new JDBMaterialType(getHostID(), getSessionID());
		JDBMaterialLocation matlocn = new JDBMaterialLocation(getHostID(), getSessionID());
		JDBLocation locn = new JDBLocation(getHostID(), getSessionID());
		JDBMaterialUom matuom = new JDBMaterialUom(getHostID(), getSessionID());
		JDBUom uomdb = new JDBUom(getHostID(), getSessionID());
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		JDBMaterialCustomerData matcustdata = new JDBMaterialCustomerData(getHostID(), getSessionID());
		JDBDataIDs dataIds = new JDBDataIDs(getHostID(), getSessionID());
		JDBCustomer cust = new JDBCustomer(getHostID(), getSessionID());

		String defaultBatchStatus = ctrl.getKeyValue("DEFAULT BATCH STATUS");

		inter.getInterfaceProperties("Material Definition", "Input");

		material = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/material").trim());
		setErrorMessage("Material " + material + " updated.");
		materialType = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/materialType").trim());

		if (mattype.isValidMaterialType(materialType) == false)
		{
			mattype.create(materialType, materialType);
		}

		description = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/description").trim());
		base_uom = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/base_uom").trim());
		base_uom = uomdb.convertUom(inter.getUOMConversion(), base_uom);

		production_uom = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/production_uom").trim());
		production_uom = uomdb.convertUom(inter.getUOMConversion(), production_uom);

		issue_uom = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/issue_uom").trim());
		issue_uom = uomdb.convertUom(inter.getUOMConversion(), issue_uom);

		shelf_life_uom = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/shelf_life_uom").trim());
		equipment_type = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/equipment_Type").trim());
		shelf_life = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/shelf_life").trim());
		shelf_life_rule = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/shelf_life_rule").trim());
		gross_weight = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/gross_weight").trim());
		net_weight = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/net_weight").trim());

		weight_uom = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/weight_uom").trim());
		weight_uom = uomdb.convertUom(inter.getUOMConversion(), weight_uom);

		old_material = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/old_material").trim());
		inspection_id = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/inspection_id").trim());
		default_batch_status = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialDefinition/default_batch_status").trim());

		/* Material Customer Data */

		repeat = true;
		seq = 1;

		do
		{
			key = "//message/messageData/materialDefinition/materialCustomerData/customer[" + String.valueOf(seq) + "]/@ID";
			customerID = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath(key).trim());

			if (customerID.length() > 0)
			{

				repeat2 = true;
				seq2 = 1;
				do
				{
					key = "//message/messageData/materialDefinition/materialCustomerData/customer[" + String.valueOf(seq) + "]/data[" + String.valueOf(seq2) + "]/@dataType";
					dataID = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath(key).trim());
					key = "//message/messageData/materialDefinition/materialCustomerData/customer[" + String.valueOf(seq) + "]/data[" + String.valueOf(seq2) + "]/@value";
					dataValue = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath(key).trim());

					if (dataID.length() > 0)
					{

						if (dataIds.isValidDataID(dataID) == false)
						{
							dataIds.setDescription("Auto Created");
							dataIds.create(dataID);
							dataIds.update();
						}

						if (matcustdata.isValidMaterialCustomerData(material, customerID, dataID) == false)
						{
							matcustdata.create(material, customerID, dataID);
						}
						matcustdata.setData(dataValue);
						matcustdata.update();

						if (dataID.equals("NAME"))
						{
							customer_name = dataValue;
						}

					} else
					{
						repeat2 = false;
					}

					seq2++;

				} while (repeat2);

				if (cust.isValidCustomer(customerID) == false)
				{
					cust.clear();
					cust.create(customerID, customer_name, "Y");
					cust.update();
				}
			} else
			{
				repeat = false;
			}

			seq++;

		} while (repeat);

		/* Material Locations */

		repeat = true;
		seq = 1;

		do
		{
			key = "//message/messageData/materialDefinition/validLocations/location[" + String.valueOf(seq) + "]/id";
			location = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath(key).trim());
			key = "//message/messageData/materialDefinition/validLocations/location[" + String.valueOf(seq) + "]/status";
			status = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath(key).trim());

			if (location.length() > 0)
			{
				if (locn.isValidLocation(location) == true)
				{
					if (matlocn.isValidMaterialLocation(material, location) == false)
					{
						matlocn.create(material, location);
					}
					matlocn.setStatus(status);
					matlocn.update();
				}

			} else
			{
				repeat = false;
			}
			seq++;

		} while (repeat);

		/* Material Units of Measure */

		repeat = true;
		seq = 1;
		String matUOMError = "";

		do
		{
			key = "//message/messageData/materialDefinition/materialUOMDefinition[" + String.valueOf(seq) + "]/uom";
			uom = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath(key).trim());
			uom = uomdb.convertUom(inter.getUOMConversion(), uom);

			if (uom.length() > 0)
			{

				key = "//message/messageData/materialDefinition/materialUOMDefinition[" + String.valueOf(seq) + "]/numerator";
				numerator = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath(key).trim());

				key = "//message/messageData/materialDefinition/materialUOMDefinition[" + String.valueOf(seq) + "]/denominator";
				denominator = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath(key).trim());

				key = "//message/messageData/materialDefinition/materialUOMDefinition[" + String.valueOf(seq) + "]/ean";
				ean = gmh.getXMLDocument().findXPath(key).trim();

				key = "//message/messageData/materialDefinition/materialUOMDefinition[" + String.valueOf(seq) + "]/variant";
				variant = gmh.getXMLDocument().findXPath(key).trim();

				key = "//message/messageData/materialDefinition/materialUOMDefinition[" + String.valueOf(seq) + "]/override";
				override = gmh.getXMLDocument().findXPath(key).trim();

				if (uomdb.isValidInternalUom(uom) == false)
				{
					uomdb.create(uom, uom, uom, "Auto created UOM");
				}

				Boolean foundMatUom = matuom.getMaterialUomProperties(material, uom);
				String locked = matuom.getOverride();

				if (foundMatUom == false)
				{
					matuom.setMaterial(material);
					matuom.setUom(uom);
					if (matuom.create() == false)
					{
						result = false;
						matUOMError = "Material " + material + " UOM [" + uom + "] create error. " + matuom.getErrorMessage();
						setErrorMessage(matUOMError);
					} else
					{
						setErrorMessage("Material " + material + " created.");
					}
				}

				try
				{
					if ((locked.equals("") || override.equals("X")))
					{

						matuom.setNumerator(Integer.valueOf(JUtility.getDefaultValue(numerator, String.valueOf(matuom.getNumerator()), "0")));
						matuom.setDenominator(Integer.valueOf(JUtility.getDefaultValue(denominator, String.valueOf(matuom.getDenominator()), "0")));

						matuom.setEan(ean);
						matuom.setVariant(variant);
						matuom.setOverride(override);

						if (matuom.getNumerator() == 0)
						{
							result = false;
							setErrorMessage("Material " + material + " UOM [" + uom + "]. Numerator cannot be zero.");
						} else
						{
							if (matuom.getDenominator() == 0)
							{
								result = false;
								setErrorMessage("Material " + material + " UOM [" + uom + "]. Denominator cannot be zero.");
							} else
							{
								if (matuom.update() == false)
								{
									result = false;
									setErrorMessage("Material " + material + " UOM [" + uom + "] update error. " + matuom.getErrorMessage());
								}
							}
						}

					}
				} catch (Exception ex)
				{
					result = false;
					setErrorMessage("Material " + material + " UOM [" + uom + "]. Invalid Quantity");
				}

			} else
			{
				repeat = false;
			}
			seq++;
			if (matUOMError.equals("") == false)
			{
				setErrorMessage(matUOMError);
			}

		} while (repeat);

		if (mat.getMaterialProperties(material) == false)
		{
			mat.setBaseUom(JUtility.getDefaultValue(base_uom, mat.getBaseUom(), ""));
			if (mat.create() == false)
			{
				result = false;
				setErrorMessage("Material " + material + " create error. " + mat.getErrorMessage());
			} else
			{
				setErrorMessage("Material " + material + " created.");
			}
		}

		if (uomdb.isValidInternalUom(uom) == false)
		{
			uomdb.create(weight_uom, weight_uom, weight_uom, "Auto created UOM");
		}

		mat.setMaterialType(JUtility.getDefaultValue(materialType, mat.getMaterialType(), ""));
		mat.setDescription(JUtility.getDefaultValue(description, mat.getDescription(), "No description"));
		mat.setBaseUom(JUtility.getDefaultValue(base_uom, mat.getBaseUom(), ""));
		mat.setShelfLifeUom(JUtility.getDefaultValue(shelf_life_uom, mat.getShelfLifeUom(), "D"));
		mat.setEquipmentType(JUtility.getDefaultValue(equipment_type, mat.getEquipmentType(), ""));
		mat.setShelfLife(Integer.valueOf(JUtility.getDefaultValue(shelf_life, String.valueOf(mat.getShelfLife()), "0")));
		mat.setShelfLifeRule(JUtility.getDefaultValue(shelf_life_rule, mat.getShelfLifeRule(), "+"));
		mat.setGrossWeight(BigDecimal.valueOf(Double.valueOf(JUtility.getDefaultValue(gross_weight, mat.getGrossWeight().toString(), "0.0"))));
		mat.setNetWeight(BigDecimal.valueOf(Double.valueOf(JUtility.getDefaultValue(net_weight, mat.getNetWeight().toString(), "0.0"))));
		mat.setWeightUom(JUtility.getDefaultValue(weight_uom, mat.getWeightUom(), ""));
		mat.setOldMaterial(JUtility.getDefaultValue(old_material, mat.getOldMaterial(), ""));
		mat.setInspectionID(JUtility.getDefaultValue(inspection_id, mat.getInspectionID(), ""));
		mat.setDefaultBatchStatus(JUtility.getDefaultValue(default_batch_status, mat.getDefaultBatchStatus(), defaultBatchStatus));

		if (mat.update() == false)
		{
			result = false;
			setErrorMessage("Material " + material + " update error. " + mat.getErrorMessage());
		}

		mat = null;

		return result;
	}
}
