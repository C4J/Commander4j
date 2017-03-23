package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBLabelData.java
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
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.opencsv.ResultSetHelperService;

import com.commander4j.messages.OutgoingLabelData;
import com.commander4j.sys.Common;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;

/**
 * JDBLabelData is used to insert/update/delete record from the APP_LABEL_DATA
 * table. When printing case labels you have the option of sending a report
 * directly to a printer or using the "Assign to Labeller" option. If the assign
 * option is selected all of the data required to print the label is written to
 * a record in the table. A background thread is then used to write the required
 * data to a file for each labeller which needs to print the data. It should be
 * noted that the operator selects a production line when printing and each
 * production line can have one or more physical labellers.
 * <p>
 * <img alt="" src="./doc-files/APP_LABEL_DATA.jpg" >
 */
public class JDBLabelData
{
	private String dbUniqueID;
	private Timestamp dbPrintDate;
	private String dbUserID;
	private String dbWorkstationID;
	private String dbMaterial;
	private String dbMaterialType;
	private String dbBatchNumber;
	private String dbProcessOrder;
	private String dbRequiredResource;
	private String dbLocationID;
	private BigDecimal dbProdQuantity;
	private String dbProdUom;
	private BigDecimal dbBaseQuantity;
	private String dbBaseUom;
	private Timestamp dbDateofManufacture;
	private Timestamp dbExpiryDate;
	private String dbExpiryMode;
	private String dbProdEAN;
	private String dbProdVariant;
	private String dbBaseEAN;
	private String dbBaseVariant;
	private String dbCustomer;
	private String dbPrintQueue;
	private Long dbPrintCopies;
	private String dbErrorMessage;
	private String dbModuleID;
	private String dbOverideDateofManufacture;
	private String dbOverideExpiryDate;
	private String dbOverrideBatchPrefix;
	private String dbBatchPrefix;
	private String dbBatchSuffix;
	private String dbLabelType;
	private String dbLine;

	private final Logger logger = Logger.getLogger(JDBLabelData.class);

	private String hostID;

	private String sessionID;

	public JDBLabelData(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBLabelData(String uid, Timestamp printTime, String user, Long copies, String workstation, String mat, String matType, String batch, String order, String resource, String locn, BigDecimal prodqty, String produom, BigDecimal baseqty,
			String baseuom, Timestamp dom, Timestamp exp, String prodean, String prodvar, String baseean, String basevar, String cust, String q, String expmode, String module, String ov_dom, String ov_exp, String ov_bat, String batch_pre,
			String batch_suf, String labelType, String line)
	{

		setUniqueID(uid);
		setPrintDate(printTime);
		setUserID(user);
		setPrintCopies(copies);
		setWorkstationID(workstation);
		setMaterial(mat);
		setMaterialType(matType);
		setBatchNumber(batch);
		setProcessOrder(order);
		setRequiredResource(resource);
		setLocationID(locn);
		setProdQuantity(prodqty);
		setProdUom(produom);
		setBaseQuantity(baseqty);
		setBaseUom(baseuom);
		setDateofManufacture(dom);
		setExpiryDate(dom);
		setProdEAN(prodean);
		setProdVariant(prodvar);
		setBaseEAN(baseean);
		setBaseVariant(basevar);
		setCustomer(cust);
		setPrintQueue(q);
		setExpiryMode(expmode);
		setModuleID(module);
		setOverrideDateofManufacture(ov_dom);
		setOverrideExpiryDate(ov_exp);
		setOverrideBatchPrefix(batch_pre);
		setBatchPrefix(batch_pre);
		setBatchSuffix(batch_suf);
		setLabelType(labelType);
		setLine(line);
		create();
	}

	public void clear()
	{
		setPrintDate(null);
		setUserID("");
		setPrintCopies(null);
		setWorkstationID("");
		setMaterial("");
		setMaterialType("");
		setBatchNumber("");
		setProcessOrder("");
		setRequiredResource("");
		setLocationID("");
		setProdQuantity(new BigDecimal("0"));
		setProdUom("");
		setBaseQuantity(new BigDecimal("0"));
		setBaseUom("");
		setDateofManufacture(null);
		setExpiryDate(null);
		setProdEAN("");
		setProdVariant("");
		setBaseEAN("");
		setBaseVariant("");
		setCustomer("");
		setPrintQueue("");
		setExpiryMode("");
		setModuleID("");
		setOverrideDateofManufacture("N");
		setOverrideExpiryDate("N");
		setOverrideBatchPrefix("N");
		setBatchPrefix("");
		setBatchSuffix("");
		setLabelType("");
		setLine("");
	}

	public void sendMessage()
	{
		OutgoingLabelData old = new OutgoingLabelData(getHostID(), getSessionID());
		old.submit(getUniqueID());
	}

	public boolean create()
	{

		logger.debug("create [" + getUniqueID() + "]");

		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLabelData.create"));

			stmtupdate.setString(1, getUniqueID());
			stmtupdate.setTimestamp(2, getPrintDate());
			stmtupdate.setString(3, getUserID());
			stmtupdate.setString(4, getWorkstationID());
			stmtupdate.setString(5, getMaterial());
			stmtupdate.setString(6, getMaterialType());
			stmtupdate.setString(7, getBatchNumber());
			stmtupdate.setString(8, getProcessOrder());
			stmtupdate.setString(9, getRequiredResource());
			stmtupdate.setString(10, getLocationID());
			stmtupdate.setBigDecimal(11, getProdQuantity());
			stmtupdate.setString(12, getProdUom());
			stmtupdate.setBigDecimal(13, getBaseQuantity());
			stmtupdate.setString(14, getBaseUom());
			stmtupdate.setTimestamp(15, getDateofManufacture());
			stmtupdate.setTimestamp(16, getExpirtDate());
			stmtupdate.setString(17, getProdEAN());
			stmtupdate.setString(18, getProdVariant());
			stmtupdate.setString(19, getBaseEAN());
			stmtupdate.setString(20, getBaseVariant());
			stmtupdate.setString(21, getCustomer());
			stmtupdate.setLong(22, getPrintCopies());
			stmtupdate.setString(23, getPrintQueue());
			stmtupdate.setString(24, getExpiryMode());
			stmtupdate.setString(25, getModuleID());
			stmtupdate.setString(26, getOverrideDateofManufacture());
			stmtupdate.setString(27, getOverrideExpiryDate());
			stmtupdate.setString(28, getOverrideBatchPrefix());
			stmtupdate.setString(29, getBatchPrefix());
			stmtupdate.setString(30, getBatchSuffix());
			stmtupdate.setString(31, getLabelType());
			stmtupdate.setString(32, getLine());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLabelData.delete"));
			stmtupdate.setString(1, getUniqueID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public String generateUniqueID()
	{
		String result = JUnique.getUniqueID();
		setUniqueID(result);
		return result;
	}

	public String getBaseEAN()
	{
		return dbBaseEAN;
	}

	public BigDecimal getBaseQuantity()
	{
		return dbBaseQuantity;
	}

	public String getBaseUom()
	{
		return dbBaseUom;
	}

	public String getBaseVariant()
	{
		return dbBaseVariant;
	}

	public String getBatchNumber()
	{
		return dbBatchNumber;
	}

	public String getBatchPrefix()
	{
		return dbBatchPrefix;
	}

	public String getBatchSuffix()
	{
		return dbBatchSuffix;
	}

	public String getCustomer()
	{
		return dbCustomer;
	}

	public Timestamp getDateofManufacture()
	{
		return dbDateofManufacture;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public Timestamp getExpirtDate()
	{
		return dbExpiryDate;
	}

	public String getExpiryMode()
	{
		return dbExpiryMode;
	}

	private String getHostID()
	{
		return hostID;
	}

	public boolean getProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLabelData.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getUniqueID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid UniqueID");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public boolean getProperties(String unique)
	{
		setUniqueID(unique);
		return getProperties();
	}

	public ResultSet getLabelDataResultSet(PreparedStatement criteria)
	{

		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();

		} catch (Exception e)
		{
			rs = null;
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public String getLabelType()
	{
		return dbLabelType;
	}

	public String getLine()
	{
		return JUtility.replaceNullStringwithBlank(dbLine);
	}

	public String getLocationID()
	{
		return dbLocationID;
	}

	public String getMaterial()
	{
		return dbMaterial;
	}

	public String getMaterialType()
	{
		return dbMaterialType;
	}

	public String getMessageInformation()
	{
		return dbMaterial;
	}

	public String getModuleID()
	{
		return dbModuleID;
	}

	public String getOverrideBatchPrefix()
	{
		return dbOverrideBatchPrefix;
	}

	public String getOverrideDateofManufacture()
	{
		return dbOverideDateofManufacture;
	}

	public String getOverrideExpiryDate()
	{
		return dbOverideExpiryDate;
	}

	public Long getPrintCopies()
	{
		return dbPrintCopies;
	}

	public Timestamp getPrintDate()
	{
		return dbPrintDate;
	}

	public String getPrintQueue()
	{
		return dbPrintQueue;
	}

	public String getProcessOrder()
	{
		return dbProcessOrder;
	}

	public String getProdEAN()
	{
		return dbProdEAN;
	}

	public BigDecimal getProdQuantity()
	{
		return dbProdQuantity;
	}

	public String getProdUom()
	{
		return dbProdUom;
	}

	public String getProdVariant()
	{
		return dbProdVariant;
	}

	public String[] getDataArray(String unique, String mode)
	{
		String[] result =
		{ "" };

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getLabelDataProperties [" + getUniqueID() + "]");
		ResultSetHelperService rsh = new ResultSetHelperService();

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLabelData.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getUniqueID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				if (mode.equals("heading"))
				{
					result = rsh.getColumnNames(rs);
				} else
				{
					result = rsh.getColumnValues(rs);
				}

			} else
			{
				setErrorMessage("Invalid UniqueID");
			}
			rs.close();
			stmt.close();
		} catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;

	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();

			setUniqueID(rs.getString("UNIQUE_ID"));
			setPrintDate(rs.getTimestamp("PRINT_DATE"));
			setUserID(rs.getString("USER_ID"));
			setPrintCopies(rs.getLong("PRINT_COPIES"));
			setWorkstationID(rs.getString("WORKSTATION_ID"));
			setMaterial(rs.getString("MATERIAL"));
			setMaterialType(rs.getString("MATERIAL_TYPE"));
			setBatchNumber(rs.getString("BATCH_NUMBER"));
			setProcessOrder(rs.getString("PROCESS_ORDER"));
			setRequiredResource(rs.getString("REQUIRED_RESOURCE"));
			setLocationID(rs.getString("LOCATION_ID"));
			setProdQuantity(rs.getBigDecimal("PROD_QUANTITY"));
			setProdUom(rs.getString("PROD_UOM"));
			setBaseQuantity(rs.getBigDecimal("BASE_QUANTITY"));
			setBaseUom(rs.getString("BASE_UOM"));
			setDateofManufacture(rs.getTimestamp("DATE_OF_MANUFACTURE"));
			setExpiryDate(rs.getTimestamp("EXPIRY_DATE"));
			setProdEAN(rs.getString("PROD_EAN"));
			setProdVariant(rs.getString("PROD_VARIANT"));
			setBaseEAN(rs.getString("BASE_EAN"));
			setBaseVariant(rs.getString("BASE_VARIANT"));
			setCustomer(rs.getString("CUSTOMER_ID"));
			setPrintQueue(rs.getString("PRINT_QUEUE_NAME"));
			setExpiryMode(rs.getString("EXPIRY_MODE"));
			setModuleID(rs.getString("MODULE_ID"));
			setOverrideDateofManufacture(rs.getString("OVERRIDE_MANUFACTURE_DATE"));
			setOverrideExpiryDate(rs.getString("OVERRIDE_EXPIRY_DATE"));
			setOverrideBatchPrefix(rs.getString("OVERRIDE_BATCH_PREFIX"));
			setBatchPrefix(rs.getString("BATCH_PREFIX"));
			setBatchSuffix(rs.getString("BATCH_SUFFIX"));
			setLabelType(rs.getString("LABEL_TYPE"));
			setLine(rs.getString("LINE"));

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getRequiredResource()
	{
		return JUtility.replaceNullStringwithBlank(dbRequiredResource);
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getUniqueID()
	{
		return dbUniqueID;
	}

	public String getUserID()
	{
		return dbUserID;
	}

	public String getWorkstationID()
	{
		return dbWorkstationID;
	}

	public void setBaseEAN(String ean)
	{
		dbBaseEAN = ean;
	}

	public void setBaseQuantity(BigDecimal qty)
	{
		dbBaseQuantity = qty;
	}

	public void setBaseUom(String uom)
	{
		dbBaseUom = uom;
	}

	public void setBaseVariant(String var)
	{
		dbBaseVariant = var;
	}

	public void setBatchNumber(String batch)
	{
		dbBatchNumber = batch;
	}

	public void setBatchPrefix(String batch)
	{
		dbBatchPrefix = batch;
		dbBatchNumber = dbBatchPrefix + dbBatchSuffix;
	}

	public void setBatchSuffix(String batch)
	{
		dbBatchSuffix = batch;
		dbBatchNumber = dbBatchPrefix + dbBatchSuffix;
	}

	public void setCustomer(String cust)
	{
		dbCustomer = cust;
	}

	public void setDateofManufacture(Timestamp prodDate)
	{
		dbDateofManufacture = prodDate;
	}

	private void setErrorMessage(String msg)
	{
		dbErrorMessage = msg;
	}

	public void setExpiryDate(Timestamp expTime)
	{
		dbExpiryDate = expTime;
	}

	public void setExpiryMode(String expmod)
	{
		dbExpiryMode = expmod;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setLabelType(String ltype)
	{
		dbLabelType = ltype;
	}

	public void setLine(String line)
	{
		dbLine = line;
	}

	public void setLocationID(String locn)
	{
		this.dbLocationID = locn;
	}

	public void setMaterial(String material)
	{
		dbMaterial = material;
	}

	public void setMaterialType(String type)
	{
		dbMaterialType = type;
	}

	public void setModuleID(String module)
	{
		dbModuleID = module;
	}

	public void setOverrideBatchPrefix(Boolean override)
	{
		if (override == true)
		{
			dbOverrideBatchPrefix = "Y";
		} else
		{
			dbOverrideBatchPrefix = "N";
		}
	}

	public void setOverrideBatchPrefix(String override)
	{
		dbOverrideBatchPrefix = override;
	}

	public void setOverrideDateofManufacture(Boolean override)
	{
		if (override == true)
		{
			dbOverideDateofManufacture = "Y";
		} else
		{
			dbOverideDateofManufacture = "N";
		}
	}

	public void setOverrideDateofManufacture(String override)
	{
		dbOverideDateofManufacture = override;
	}

	public void setOverrideExpiryDate(Boolean override)
	{
		if (override == true)
		{
			dbOverideExpiryDate = "Y";
		} else
		{
			dbOverideExpiryDate = "N";
		}
	}

	public void setOverrideExpiryDate(String override)
	{
		dbOverideExpiryDate = override;
	}

	public void setPrintCopies(Long copies)
	{
		dbPrintCopies = copies;
	}

	public void setPrintDate(Timestamp printTime)
	{
		dbPrintDate = printTime;
	}

	public void setPrintQueue(String q)
	{
		dbPrintQueue = q;
	}

	public void setProcessOrder(String processOrder)
	{
		dbProcessOrder = processOrder;
	}

	public void setProdEAN(String ean)
	{
		dbProdEAN = ean;
	}

	public void setProdQuantity(BigDecimal qty)
	{
		dbProdQuantity = qty;
	}

	public void setProdUom(String uom)
	{
		dbProdUom = uom;
	}

	public void setProdVariant(String var)
	{
		dbProdVariant = var;
	}

	public void setRequiredResource(String resource)
	{
		dbRequiredResource = resource;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setUniqueID(String uniqueID)
	{
		dbUniqueID = uniqueID;
	}

	public void setUserID(String user)
	{
		dbUserID = user;
	}

	public void setWorkstationID(String workstationID)
	{
		dbWorkstationID = workstationID;
	}

	public boolean updateLine(String uniqueId, String line)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		setUniqueID(uniqueId);
		setLine(line);

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLabelData.updateLine"));
			stmtupdate.setString(1, getLine());
			stmtupdate.setString(2, getUniqueID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean renameLine(String oldLine, String newLine)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLabelData.renameLine"));
			stmtupdate.setString(1, newLine);
			stmtupdate.setString(2, oldLine);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
