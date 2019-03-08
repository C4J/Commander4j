package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBMaterial.java
 * 
 * Package Name : com.commander4j.db
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * JDBMaterial class is used to insert/update/delete records from the
 * APP_MATERIAL table.
 * <p>
 * <img alt="" src="./doc-files/APP_MATERIAL.jpg" >
 * 
 * @see com.commander4j.db.JDBMaterialBatch JDBMaterialBatch
 * @see com.commander4j.db.JDBMaterialLocation JDBMaterialLocation
 * @see com.commander4j.db.JDBMaterialUom JDBMaterialUom
 * @see com.commander4j.db.JDBMaterialCustomerData JDBMaterialCustomerData
 * @see com.commander4j.db.JDBMaterialType JDBMaterialType
 */
public class JDBMaterial
{

	private String dbEnabled = "Y";
	private String dbBaseUom;
	private String dbDescription;
	private String dbErrorMessage;
	private BigDecimal dbGrossWeight;
	private String dbMaterial;
	private String dbMaterialType;
	private BigDecimal dbNetWeight;
	private String dbOldMaterial;
	private String dbInspectionID;
	private String dbEquipmentType;
	private Integer dbShelflife;
	private String dbShelflifeRule;
	private String dbShelflifeUom;
	private String dbWeightUom;
	private String dbDefaultBatchStatus;
	public static int field_material = 20;
	public static int field_description = 80;
	public static int field_shelf_life_uom = 1;
	public static int field_old_material = 20;
	public static int field_equipment_type = 15;
	private final Logger logger = Logger.getLogger(JDBMaterial.class);
	private String hostID;
	private String sessionID;
	private JDBUom uom;
	private JDBControl ctrl;
	private JDBMaterialUom muom;
	private JDBLocation moveLocation;
	private String BatchExpiryTimeRoudingMode = "";

	private String dbOverride_Pack_Label;
	private String dbOverride_Pallet_Label;
	private String dbPack_Label_ModuleID;
	private String dbPallet_Label_ModuleID;

	private String dbValidateScanPallet = "N";
	private String dbValidateScanCase = "N";
	private String dbValidateScanEach = "N";
	private String dbMoveAfterMakeEnabled = "N";
	private String dbMoveAfterMakeLocationID = "";

	public void setEnabled(boolean yesno)
	{
		if (yesno)
		{
			setEnabled("Y");
		}
		else
		{
			setEnabled("N");
		}
	}

	public void setEnabled(String yesno)
	{
		dbEnabled = JUtility.replaceNullStringwithBlank(yesno);
	}

	public boolean isEnabled()
	{
		if (getEnabled().equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getEnabled()
	{
		dbEnabled = JUtility.replaceNullStringwithBlank(dbEnabled);
		if (dbEnabled.equals(""))
		{
			dbEnabled = "Y";
		}
		return dbEnabled;

	}

	public void setValidateScanPallet(boolean yesno)
	{
		if (yesno)
		{
			setValidateScanPallet("Y");
		}
		else
		{
			setValidateScanPallet("N");
		}
	}

	public void setValidateScanPallet(String yesno)
	{
		dbValidateScanPallet = JUtility.replaceNullStringwithBlank(yesno);
	}

	public boolean isValidateScanPallet()
	{
		if (getValidateScanPallet().equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getValidateScanPallet()
	{
		dbValidateScanPallet = JUtility.replaceNullStringwithBlank(dbValidateScanPallet);
		if (dbValidateScanPallet.equals(""))
		{
			dbValidateScanPallet = "N";
		}
		return dbValidateScanPallet;

	}

	public void setValidateScanCase(boolean yesno)
	{
		if (yesno)
		{
			setValidateScanCase("Y");
		}
		else
		{
			setValidateScanCase("N");
		}
	}

	public void setValidateScanCase(String yesno)
	{
		dbValidateScanCase = JUtility.replaceNullStringwithBlank(yesno);
	}

	public boolean isValidateScanCase()
	{
		if (getValidateScanCase().equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getValidateScanCase()
	{
		dbValidateScanCase = JUtility.replaceNullStringwithBlank(dbValidateScanCase);
		if (dbValidateScanCase.equals(""))
		{
			dbValidateScanCase = "N";
		}
		return dbValidateScanCase;

	}

	public void setValidateScanEach(boolean yesno)
	{
		if (yesno)
		{
			setValidateScanEach("Y");
		}
		else
		{
			setValidateScanEach("N");
		}
	}

	public void setValidateScanEach(String yesno)
	{
		dbValidateScanEach = JUtility.replaceNullStringwithBlank(yesno);
	}

	public boolean isValidateScanEach()
	{
		if (getValidateScanEach().equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getValidateScanEach()
	{
		dbValidateScanEach = JUtility.replaceNullStringwithBlank(dbValidateScanEach);
		if (dbValidateScanEach.equals(""))
		{
			dbValidateScanEach = "N";
		}
		return dbValidateScanEach;

	}

	public void setOverridePackLabel(boolean yesno)
	{
		if (yesno)
		{
			setOverridePackLabel("Y");
		}
		else
		{
			setOverridePackLabel("N");
		}
	}

	public void setOverridePackLabel(String yesno)
	{
		dbOverride_Pack_Label = JUtility.replaceNullStringwithBlank(yesno);
	}

	// ===============

	public void setMoveAfterMakeLocationID(String val)
	{
		dbMoveAfterMakeLocationID = JUtility.replaceNullStringwithBlank(val);
	}

	public String getMoveAfterMakeLocationID()
	{
		return JUtility.replaceNullStringwithBlank(dbMoveAfterMakeLocationID);
	}

	public void setMoveAfterMakeEnabled(String yesno)
	{
		dbMoveAfterMakeEnabled = JUtility.replaceNullStringwithBlank(yesno);
	}

	public String getMoveAfterMakeEnabled()
	{
		dbMoveAfterMakeEnabled = JUtility.replaceNullStringwithBlank(dbMoveAfterMakeEnabled);
		if (dbMoveAfterMakeEnabled.equals(""))
		{
			dbMoveAfterMakeEnabled = "N";
		}
		return dbMoveAfterMakeEnabled;
	}

	public boolean isMoveAfterMakeEnabled()
	{
		if (getMoveAfterMakeEnabled().equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// ================

	public boolean isOverridePackLabel()
	{
		if (getOverridePackLabel().equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getOverridePackLabel()
	{
		dbOverride_Pack_Label = JUtility.replaceNullStringwithBlank(dbOverride_Pack_Label);
		if (dbOverride_Pack_Label.equals(""))
		{
			dbOverride_Pack_Label = "N";
		}
		return dbOverride_Pack_Label;

	}

	public void setPackLabelModuleID(String id)
	{
		dbPack_Label_ModuleID = id;
	}

	public String getPackLabelModuleID()
	{
		return JUtility.replaceNullStringwithBlank(dbPack_Label_ModuleID);
	}

	public void setOverridePalletLabel(boolean yesno)
	{
		if (yesno)
		{
			setOverridePalletLabel("Y");
		}
		else
		{
			setOverridePalletLabel("N");
		}
	}

	public void setOverridePalletLabel(String yesno)
	{
		dbOverride_Pallet_Label = JUtility.replaceNullStringwithBlank(yesno);
	}

	public boolean isOverridePalletLabel()
	{
		if (getOverridePalletLabel().equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getOverridePalletLabel()
	{
		dbOverride_Pallet_Label = JUtility.replaceNullStringwithBlank(dbOverride_Pallet_Label);
		if (dbOverride_Pallet_Label.equals(""))
		{
			dbOverride_Pallet_Label = "N";
		}
		return dbOverride_Pallet_Label;

	}

	public void setPalletLabelModuleID(String id)
	{
		dbPallet_Label_ModuleID = id;
	}

	public String getPalletLabelModuleID()
	{
		return JUtility.replaceNullStringwithBlank(dbPallet_Label_ModuleID);
	}

	public JDBMaterial(ResultSet rs)
	{
		getPropertiesfromResultSet(rs);
	}

	public JDBMaterial(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		ctrl = new JDBControl(getHostID(), getSessionID());
		muom = new JDBMaterialUom(getHostID(), getSessionID());
		uom = new JDBUom(getHostID(), getSessionID());
		moveLocation = new JDBLocation(getHostID(), getSessionID());
	}

	public JDBMaterial(String material, String description, String type, String baseUom, Integer shelflife, String shelflifeuom, String shelfliferule, String equipmentType, String overridePackLabel, String packLabelModule, String overridePalletLabel,
			String palletLabelModule, String validateScanPallet, String validateScanCase, String validateScanEach, String enabled, String moveEnabled, String moveLocation)
	{
		setMaterial(material);
		setDescription(description);
		setMaterialType(type);
		setBaseUom(baseUom);
		setShelfLife(shelflife);
		setShelfLifeUom(shelflifeuom);
		setShelfLifeRule(shelfliferule);
		setEquipmentType(equipmentType);
		setOverridePackLabel(overridePackLabel);
		setPackLabelModuleID(packLabelModule);
		setOverridePalletLabel(overridePalletLabel);
		setPalletLabelModuleID(palletLabelModule);
		setValidateScanPallet(validateScanPallet);
		setValidateScanCase(validateScanCase);
		setValidateScanEach(validateScanEach);
		setEnabled(enabled);
		setMoveAfterMakeEnabled(moveEnabled);
		setMoveAfterMakeLocationID(moveLocation);
	}

	public Date calcBBE(Date dateOfManufacture, Integer shelfLife, String shelfLifeUom, String shelfLifeRule)
	{
		Date result;
		Calendar caldate = Calendar.getInstance();
		caldate.setTime(dateOfManufacture);

		result = dateOfManufacture;

		if (shelfLifeUom.equals("H"))
		{
			caldate.add(Calendar.HOUR_OF_DAY, shelfLife);
		}

		if (shelfLifeUom.equals("D"))
		{
			caldate.add(Calendar.DAY_OF_YEAR, shelfLife);
		}

		if (shelfLifeUom.equals("W"))
		{
			caldate.add(Calendar.DAY_OF_YEAR, (7 * shelfLife));
		}

		if (shelfLifeUom.equals("M"))
		{
			caldate.add(Calendar.MONTH, shelfLife);
		}

		if (shelfLifeUom.equals("Y"))
		{
			caldate.add(Calendar.YEAR, shelfLife);
		}

		result = getRoundedExpiryDate(caldate.getTime());

		return result;
	}

	public void clear()
	{
		// setMaterial("");
		setBaseUom("");
		setDescription("");
		setGrossWeight((new BigDecimal(0)));
		setMaterialType("");
		setOldMaterial("");
		setInspectionID("");
		setNetWeight((new BigDecimal(0)));
		setBaseUom("");
		setWeightUom("");
		setShelfLife(0);
		setShelfLifeRule("");
		setShelfLifeUom("");
		setDefaultBatchStatus("");
		setEquipmentType("");
		setOverridePackLabel("");
		setPackLabelModuleID("");
		setOverridePalletLabel("");
		setPalletLabelModuleID("");
		setValidateScanPallet("");
		setValidateScanCase("");
		setValidateScanEach("");
		setMoveAfterMakeEnabled("N");
		setMoveAfterMakeLocationID("");
		setEnabled("Y");
	}

	public BigDecimal convertQuantitytoBaseUOM(BigDecimal quantity, String uom)
	{
		BigDecimal result = new BigDecimal(0);
		BigDecimal numerator = new BigDecimal("0");
		BigDecimal denominator = new BigDecimal("0");

		if (muom.getMaterialUomProperties(getMaterial(), uom) == true)
		{
			numerator = BigDecimal.valueOf(muom.getNumerator());
			denominator = BigDecimal.valueOf(muom.getDenominator());
			result = quantity;
			result = result.multiply(numerator);
			result = result.divide(denominator);
		}

		return result;
	}

	/**
	 * Method create.
	 * 
	 * @return boolean
	 */
	public boolean create()
	{

		logger.debug("create [" + getMaterial() + "]");

		boolean result = false;

		if (isValid() == true)
		{

			if (isValidMaterial(getMaterial()) == true)
			{
				setErrorMessage("Key violation - material [" + getMaterial() + "] already exists !");
			}
			else
			{
				try
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterial.create"));
					stmtupdate.setString(1, getMaterial());
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					update();
					result = true;
				}
				catch (SQLException e)
				{
					setErrorMessage(e.getMessage());
				}
			}
		}

		return result;
	}

	/**
	 * Method delete.
	 * 
	 * @return boolean
	 */
	public boolean delete()
	{

		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("delete");

		try
		{
			if (isValidMaterial() == true)
			{
				Integer noofbatches = getMaterialBatchCount();
				if (noofbatches == 0)
				{
					Integer noofuoms = getMaterialUomCount();
					if (noofuoms == 0)
					{
						stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterial.delete"));
						stmtupdate.setString(1, getMaterial());
						stmtupdate.execute();
						stmtupdate.clearParameters();
						Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

						stmtupdate.close();
						result = true;
					}
					else
					{
						setErrorMessage(noofuoms.toString() + " uom conversions exist for Material " + getMaterial());
					}
				}
				else
				{
					setErrorMessage(noofbatches.toString() + " batches exist for Material " + getMaterial());
				}
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	/**
	 * Method getBaseUom.
	 * 
	 * @return String
	 */
	public String getBaseUom()
	{
		return dbBaseUom;
	}

	/**
	 * Method getDefaultBatchStatus.
	 * 
	 * @return String
	 */
	public String getDefaultBatchStatus()
	{
		String result = "";
		result = JUtility.replaceNullStringwithBlank(dbDefaultBatchStatus);
		if (result.equals("") == true)
		{
			ctrl.getProperties("DEFAULT BATCH STATUS");
			result = ctrl.getKeyValue();
		}
		return result;
	}

	/**
	 * Method getDescription.
	 * 
	 * @return String
	 */
	public String getDescription()
	{
		if (dbDescription == null)
		{
			dbDescription = "";
		}
		return dbDescription;
	}

	public String getEquipmentType()
	{
		return dbEquipmentType;
	}

	/**
	 * Method getErrorMessage.
	 * 
	 * @return String
	 */
	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	/**
	 * Method getGrossWeight.
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getGrossWeight()
	{
		return dbGrossWeight;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getInspectionID()
	{
		return dbInspectionID;
	}

	/**
	 * Method getMaterial.
	 * 
	 * @return String
	 */
	public String getMaterial()
	{
		return dbMaterial;
	}

	/**
	 * Method getMaterialBatchCount.
	 * 
	 * @return Integer
	 */
	public Integer getMaterialBatchCount()
	{
		Integer result = 0;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getMaterialBatchCount for material " + getMaterial());

		try
		{

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterial.getMaterialBatchCount"));
			stmt.setFetchSize(1);
			stmt.setString(1, getMaterial());
			rs = stmt.executeQuery();

			while (rs.next())
			{
				result = rs.getInt("batch_number");
			}

			rs.close();

			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public LinkedList<JDBMaterialBatch> getMaterialBatches()
	{
		LinkedList<JDBMaterialBatch> batchList = new LinkedList<JDBMaterialBatch>();

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getMaterialBatches for material " + getMaterial());

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterial.getMaterialBatches"));
			stmt.setFetchSize(1);
			stmt.setString(1, getMaterial());
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBMaterialBatch mb = new JDBMaterialBatch(rs.getString("material"), rs.getString("batch"), rs.getTimestamp("expiry_date"), rs.getString("status"));
				batchList.addLast(mb);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return batchList;
	}

	public Vector<JDBMaterial> getMaterialData(PreparedStatement criteria)
	{

		ResultSet rs;
		Vector<JDBMaterial> result = new Vector<JDBMaterial>();
		// logger.debug("getControlData");

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBMaterial("material", "description", "type", "base_uom", 0, "shelf life uom", "rounding rule", "equipmentType", "overridePack", "packModule", "overridePallet", "palletModule", "validateScanPallet", "validateScanCase",
					"validateScanEach", "Y", "N", ""));
		}
		else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					result.addElement(new JDBMaterial(rs.getString("material"), rs.getString("description"), rs.getString("material_type"), rs.getString("base_uom"), rs.getInt("shelf_life"), rs.getString("shelf_life_uom"), rs.getString("shelf_life_rule"),
							rs.getString("equipment_type"), rs.getString("override_pack_label"), rs.getString("pack_label_module_id"), rs.getString("override_pallet_label"), rs.getString("pallet_label_module_id"), rs.getString("validate_scan_pallet"),
							rs.getString("validate_scan_case"), rs.getString("validate_scan_each"), rs.getString("enabled"), rs.getString("move_after_make_enabled"), rs.getString("move_after_make_location_id")));
				}
				rs.close();

			}
			catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public ResultSet getMaterialDataResultSet(PreparedStatement criteria)
	{

		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();

		}
		catch (Exception e)
		{
			rs = null;
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public boolean getMaterialProperties(String lmaterial)
	{
		boolean result = false;

		clear();

		try
		{
			setMaterial(lmaterial);

			if (getMaterial().equals("") == false)
			{
				logger.debug("find [" + lmaterial + "]");
				PreparedStatement stmt;
				ResultSet rs;
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterial.getMaterialProperties"));
				stmt.setString(1, lmaterial);
				stmt.setFetchSize(1);
				rs = stmt.executeQuery();

				if (rs.next())
				{
					if (rs.getString("material").equals(lmaterial) == true)
					{
						getPropertiesfromResultSet(rs);
						result = true;
					}
				}
				else
				{
					setErrorMessage("Invalid Material [" + lmaterial + "]");
				}
				rs.close();
				stmt.close();
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public String getMaterialType()
	{
		return dbMaterialType;
	}

	public Integer getMaterialUomCount()
	{
		Integer result = 0;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getMaterialUomCount for Material " + getMaterial());

		try
		{

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterial.getMaterialUomCount"));
			stmt.setFetchSize(1);
			stmt.setString(1, getMaterial());
			rs = stmt.executeQuery();

			while (rs.next())
			{
				result = rs.getInt("uom");
			}

			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public LinkedList<JDBMaterialUom> getMaterialUoms()
	{
		LinkedList<JDBMaterialUom> uomList = new LinkedList<JDBMaterialUom>();

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getMaterialUoms for Material " + getMaterial());

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterial.getMaterialUoms"));
			stmt.setFetchSize(1);
			stmt.setString(1, getMaterial());
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBMaterialUom mu = new JDBMaterialUom(getHostID(), getSessionID(), rs.getString("material"), rs.getString("uom"), rs.getString("ean"), rs.getString("variant"), rs.getInt("numerator"), rs.getInt("denominator"), rs.getString("override"));
				uomList.addLast(mu);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return uomList;
	}

	public BigDecimal getNetWeight()
	{
		return dbNetWeight;
	}

	public String getOldMaterial()
	{
		return dbOldMaterial;
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();

			setMaterial(rs.getString("material"));
			setBaseUom(rs.getString("base_uom"));
			setDescription(rs.getString("description"));
			setGrossWeight(rs.getBigDecimal("gross_weight"));
			setMaterialType(rs.getString("material_type"));
			setOldMaterial(rs.getString("old_material"));
			setInspectionID(rs.getString("inspection_id"));
			setNetWeight(rs.getBigDecimal("net_weight"));
			setBaseUom(rs.getString("base_uom"));
			setWeightUom(rs.getString("weight_uom"));
			setShelfLife(rs.getInt("shelf_life"));
			setShelfLifeRule(rs.getString("shelf_life_rule"));
			setShelfLifeUom(rs.getString("shelf_life_uom"));
			setDefaultBatchStatus(JUtility.replaceNullStringwithBlank(rs.getString("default_batch_status")));
			setEquipmentType(rs.getString("equipment_type"));
			setOverridePackLabel(rs.getString("override_pack_label"));
			setPackLabelModuleID(rs.getString("pack_label_module_id"));
			setOverridePalletLabel(rs.getString("override_pallet_label"));
			setPalletLabelModuleID(rs.getString("pallet_label_module_id"));
			setValidateScanPallet(rs.getString("validate_scan_pallet"));
			setValidateScanCase(rs.getString("validate_scan_case"));
			setValidateScanEach(rs.getString("validate_scan_each"));
			setEnabled(rs.getString("enabled"));
			setMoveAfterMakeEnabled(rs.getString("move_after_make_enabled"));
			setMoveAfterMakeLocationID(rs.getString("move_after_make_location_id"));

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public Date getRoundedExpiryDate(Date inputDate)
	{

		Calendar caldate = Calendar.getInstance();
		caldate.setTime(inputDate);
		Date result;

		if (getShelfLifeRule().equals("-"))
		{
			caldate.set(Calendar.DAY_OF_MONTH, 1);
		}

		if (getShelfLifeRule().equals("+"))
		{
			caldate.set(Calendar.DAY_OF_MONTH, 1);
			caldate.add(Calendar.MONTH, 1);
			caldate.add(Calendar.DAY_OF_YEAR, -1);
		}

		result = getRoundedExpiryTime(caldate.getTime());

		return result;
	}

	public Date getRoundedExpiryTime(Date inputDate)
	{
		Date result;
		Calendar caldate = Calendar.getInstance();
		caldate.setTime(inputDate);

		if (BatchExpiryTimeRoudingMode.equals(""))
		{
			BatchExpiryTimeRoudingMode = ctrl.getKeyValueWithDefault("BATCH EXPIRY TIME", "-", "Round Batch Expiry Time");
		}

		if (BatchExpiryTimeRoudingMode.equals("-"))
		{
			caldate.set(Calendar.HOUR_OF_DAY, 0);
			caldate.set(Calendar.MINUTE, 0);
			caldate.set(Calendar.SECOND, 0);
			caldate.set(Calendar.MILLISECOND, 0);
		}

		if (BatchExpiryTimeRoudingMode.equals("+"))
		{
			caldate.set(Calendar.HOUR_OF_DAY, 23);
			caldate.set(Calendar.MINUTE, 59);
			caldate.set(Calendar.SECOND, 59);
			caldate.set(Calendar.MILLISECOND, 0);
		}

		result = caldate.getTime();

		return result;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public int getShelfLife()
	{
		return dbShelflife;
	}

	public String getShelfLifeRule()
	{
		return dbShelflifeRule;
	}

	public String getShelfLifeUom()
	{
		return dbShelflifeUom;
	}

	public String getWeightUom()
	{
		return dbWeightUom;
	}

	public boolean isValid()
	{
		boolean result = true;

		/* Check Material */
		if (JUtility.isNullORBlank(dbMaterial) == true)
		{
			setErrorMessage("MATERIAL code cannot be null");
			result = false;
		}

		/* Check Base UOM */
		if (result == true)
		{
			if (JUtility.isNullORBlank(dbBaseUom) == false)
			{
				uom.setInternalUom(dbBaseUom);
				result = uom.isValidInternalUom();
				if (result == false)
				{
					setErrorMessage(uom.getErrorMessage());
				}
			}
		}

		/* Check Weight UOM */
		if (result == true)
		{
			if (JUtility.isNullORBlank(dbWeightUom) == false)
			{
				uom.setInternalUom(dbWeightUom);
				result = uom.isValidInternalUom();
				if (result == false)
					setErrorMessage(uom.getErrorMessage());
			}
		}

		if (result == true)
		{
			if (getMoveAfterMakeLocationID().equals("") == false)
			{
				if (moveLocation.isValidLocation(getMoveAfterMakeLocationID()) == false)
				{
					result = false;
					setErrorMessage(moveLocation.getErrorMessage());
				}
			}
		}

		if (result == false)
		{
			logger.debug("isValid [" + getMaterial() + "] " + getErrorMessage());
		}

		return result;
	}

	public boolean isValidMaterial()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterial.isValidMaterial"));
			stmt.setString(1, getMaterial());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Material [" + getMaterial() + "]");
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public boolean isValidMaterial(String mat)
	{
		setMaterial(mat);
		return isValidMaterial();
	}

	public void setBaseUom(String uom)
	{
		if (uom == null)
		{
			uom = "";
		}
		dbBaseUom = uom;
	}

	public void setDefaultBatchStatus(String batchStatus)
	{
		dbDefaultBatchStatus = batchStatus;
	}

	public void setDescription(String description)
	{
		dbDescription = description;
	}

	public void setEquipmentType(String equipmentType)
	{
		dbEquipmentType = equipmentType;
	}

	private void setErrorMessage(String ErrorMsg)
	{
		dbErrorMessage = ErrorMsg;
	}

	public void setGrossWeight(BigDecimal weight)
	{
		dbGrossWeight = weight;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setInspectionID(String insp)
	{
		dbInspectionID = insp;
	}

	public void setMaterial(String material)
	{
		dbMaterial = material;
	}

	public void setMaterialType(String materialType)
	{
		dbMaterialType = materialType;
	}

	public void setNetWeight(BigDecimal weight)
	{
		dbNetWeight = weight;
	}

	public void setOldMaterial(String material)
	{
		dbOldMaterial = material;
	}

	public void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setShelfLife(int shelflife)
	{
		dbShelflife = shelflife;
	}

	public void setShelfLifeRule(String rule)
	{
		dbShelflifeRule = rule;
	}

	public void setShelfLifeUom(String uom)
	{
		dbShelflifeUom = uom;
	}

	public void setWeightUom(String uom)
	{
		dbWeightUom = uom;
	}

	public boolean update()
	{
		boolean result = false;

		logger.debug("update [" + getMaterial() + "]");

		if (isValid() == true)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterial.update"));

				stmtupdate.setString(1, getBaseUom());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setBigDecimal(3, getGrossWeight());
				stmtupdate.setString(4, getMaterialType());
				stmtupdate.setString(5, getOldMaterial());
				stmtupdate.setBigDecimal(6, getNetWeight());
				stmtupdate.setString(7, getWeightUom());
				stmtupdate.setInt(8, getShelfLife());
				stmtupdate.setString(9, getShelfLifeRule());
				stmtupdate.setString(10, getShelfLifeUom());
				stmtupdate.setString(11, getDefaultBatchStatus());
				stmtupdate.setString(12, getEquipmentType());
				stmtupdate.setTimestamp(13, JUtility.getSQLDateTime());
				stmtupdate.setString(14, getInspectionID());
				stmtupdate.setString(15, getOverridePackLabel());
				stmtupdate.setString(16, getPackLabelModuleID());
				stmtupdate.setString(17, getOverridePalletLabel());
				stmtupdate.setString(18, getPalletLabelModuleID());
				stmtupdate.setString(19, getValidateScanPallet());
				stmtupdate.setString(20, getValidateScanCase());
				stmtupdate.setString(21, getValidateScanEach());
				stmtupdate.setString(22, getEnabled());
				stmtupdate.setString(23, getMoveAfterMakeEnabled());
				stmtupdate.setString(24, getMoveAfterMakeLocationID());
				stmtupdate.setString(25, getMaterial());

				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

}
