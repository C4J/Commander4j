package com.commander4j.db;

import java.math.BigDecimal;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWasteLocations.java
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.commander4j.util.JUtility;

/**
 * The JDBViewWasteLog class updates the table VIEW_WASTE_LOG. This table contains a
 * single row for each unique Waste Location as stored in the VIEW_WASTE_LOG
 * table.
 * <p>
 * <img alt="" src="./doc-files/VIEW_WASTE_LOG.jpg" >
 * 
 * @see com.commander4j.db.JDBWasteReasons JDBWasteReasons
 * @see com.commander4j.db.JDBWasteReportingIDS JDBWasteReportingIDS
 * @see com.commander4j.db.JDBWasteTransactionType JDBWasteTransactionType
 * @see com.commander4j.db.JDBWasteLocationReporting JDBWasteLocationReporting
 * @see com.commander4j.db.JDBWasteReasons JDBWasteTypes
 * @see com.commander4j.db.JDBWasteReasons JDBWasteMaterial
 * @see com.commander4j.db.JDBWasteReasons JDBWasteLog
 * 
 */

public class JDBViewWasteReporting
{
	public static int field_TransactionTypeID_id = 25;
	public static int field_LocationID = 25;
	public static int field_MaterialID = 25;
	public static int field_ProcessOrder = 10;
	public static int field_ReasonID = 20;
	public static int field_UserID = 20;

	private String 			dbErrorMessage;
	private long 			dbReportingGroup;
	private String			dbReportingID;
	private long 			dbTransactionRef;
	private Timestamp 		dbWasteReportTime;
	private String 			dbTransactionType;
	private String 			dbLocationID;
	private String 			dbContainerID;
	private String			dbMaterialID;
	private String			dbMaterialType;
	private String 			dbReasonID;
	private String 			dbProcessOrder;
	private BigDecimal 		dbWeightKG;
	private BigDecimal 		dbTareWeight;
	private BigDecimal 		dbNetWeight;
	private BigDecimal 		dbCostPerKg;	
	private BigDecimal 		dbCost;
	
	private final Logger logger = Logger.getLogger(JDBViewWasteReporting.class);
	private String hostID;
	private String sessionID;


	public JDBViewWasteReporting(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}
	
	@SuppressWarnings("unused")
	private String getSessionID()
	{
		return sessionID;
	}
	
	private void setHostID(String host)
	{
		hostID = host;
	}
	
	@SuppressWarnings("unused")
	private String getHostID()
	{
		return hostID;
	}
	
	private void setSessionID(String session)
	{
		sessionID = session;
	}
	
	public void clear()
	{
		setReportingGroup(0);
		setTransactionRef(0);
		setReportTime(JUtility.getSQLDateTime());
		setTransactionType("");
		setLocationID("");
		setContainerID("");
		setMaterialID("");
		setMaterialType("");
		setReasonID("");
		setProcessOrder("");
		setTareWeight(new BigDecimal("0.000"));
		setNetWeight(new BigDecimal("0"));
		setCostPerKg(new BigDecimal("0"));
		setWeightKG(new BigDecimal("0"));
		setCostTotal(new BigDecimal("0"));
	}
	
	public void setReportingID(String var)
	{
		dbReportingID = var;
	}

	public String getReportingID()
	{
		return dbReportingID;
	}
	
	public long getReportingGroup()
	{
		return dbReportingGroup;
	}

	public void setReportingGroup(long dbReportingGroup)
	{
		this.dbReportingGroup = dbReportingGroup;
	}

	public String getMaterialType()
	{
		return dbMaterialType;
	}

	public void setMaterialType(String dbMaterialType)
	{
		this.dbMaterialType = dbMaterialType;
	}

	public BigDecimal getCostPerUom()
	{
		return dbCostPerKg;
	}

	public void setCostPerKg(BigDecimal dbCostPerUom)
	{
		this.dbCostPerKg = dbCostPerUom;
	}


	public long getTransactionRef()
	{
		return dbTransactionRef;
	}

	public void setTransactionRef(long transactionRef)
	{
		dbTransactionRef = transactionRef;
	}

	
	public BigDecimal getTareWeight()
	{
		return dbTareWeight;
	}

	public void setTareWeight(BigDecimal dbTare)
	{
		this.dbTareWeight = dbTare;
	}

	public BigDecimal getNetWeight()
	{
		return dbNetWeight;
	}

	public void setNetWeight(BigDecimal dbNet)
	{
		this.dbNetWeight = dbNet;
	}
	
	public BigDecimal getWeightKG()
	{
		return dbWeightKG;
	}

	public void setWeightKG(BigDecimal val)
	{
		this.dbWeightKG = val;
	}	
	
	public BigDecimal getCostTotal()
	{
		return dbCost;
	}

	public void setCostTotal(BigDecimal val)
	{
		this.dbCost = val;
	}	
	
	public String getLocationID()
	{
		return JUtility.replaceNullStringwithBlank(dbLocationID).trim();
	}
	
	public String getContainerID()
	{
		return JUtility.replaceNullStringwithBlank(dbContainerID).trim();
	}

	public String getMaterialID()
	{
		return JUtility.replaceNullStringwithBlank(dbMaterialID).trim();
	}
	

	public String getProcessOrder()
	{
		return JUtility.replaceNullStringwithBlank(dbProcessOrder).trim();
	}

	public String getReasonID()
	{
		return JUtility.replaceNullStringwithBlank(dbReasonID).trim();
	}

	public void setReasonID(String user)
	{
		dbReasonID = JUtility.replaceNullStringwithBlank(user).trim();
	}

	public String getTransactionType()
	{
		return JUtility.replaceNullStringwithBlank(dbTransactionType);
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public ResultSet getWasteLogResultSet(PreparedStatement criteria)
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

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setReportingID(rs.getString("waste_reporting_id"));
			setReportingGroup(rs.getLong("reporting_group"));
			setTransactionRef(rs.getLong("transaction_ref"));
			setReportTime(rs.getTimestamp("report_time"));
			setTransactionType(rs.getString("waste_transaction_type"));
			setLocationID(rs.getString("waste_location_id"));
			setContainerID(rs.getString("waste_container_id"));
			setMaterialID(rs.getString("waste_material_id"));
			setMaterialType(rs.getString("waste_type_id"));
			setReasonID(rs.getString("waste_reason_id"));
			setProcessOrder(rs.getString("process_order"));
			setCostPerKg(rs.getBigDecimal("cost_per_kg"));
			setWeightKG(rs.getBigDecimal("weight_kg"));
			setTareWeight(rs.getBigDecimal("tare_weight"));
			setNetWeight(rs.getBigDecimal("net_weight"));
			setCostTotal(rs.getBigDecimal("cost"));

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public Timestamp getWasteReportTime()
	{
		return dbWasteReportTime;
	}

	public void setLocationID(String str)
	{
		dbLocationID = str;
	}
	
	public void setContainerID(String str)
	{
		dbContainerID = str;
	}

	public void setMaterialID(String str)
	{
		dbMaterialID = str;
	}
	
	public void setProcessOrder(String str)
	{
		dbProcessOrder = str;
	}

	public void setTransactionType(String desc)
	{
		dbTransactionType = desc;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setReportTime(Timestamp res)
	{
		dbWasteReportTime = res;
	}


	public String toString()
	{
		return String.valueOf(getTransactionRef());
	}

}
