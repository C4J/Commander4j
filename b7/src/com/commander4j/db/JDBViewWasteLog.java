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

import org.apache.logging.log4j.Logger;

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

public class JDBViewWasteLog
{
	public static int field_TransactionTypeID_id = 25;
	public static int field_LocationID = 25;
	public static int field_ContainerID = 25;
	public static int field_MaterialID = 25;
	public static int field_ProcessOrder = 10;
	public static int field_ReasonID = 20;
	public static int field_UserID = 20;

	private String dbErrorMessage;
	private long dbTransactionRef; /* PK */
	private Timestamp dbWasteReportTime;
	private String dbTransactionType;
	private String dbLocationID;
	private String dbComment;
	private String dbContainerID;
	private String dbMaterialID;
	private String dbMaterialType;
	private String dbUOM;
	private String dbProcessOrder;
	private String dbReasonID;
	private String dbUserID;
	private BigDecimal dbTareWeight;
	private BigDecimal dbWeightKG;
	private BigDecimal dbNetWeight;
	private BigDecimal dbCostPerKg;
	private BigDecimal dbCostTotal;
	
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBWasteLog.class);
	private String hostID;
	private String sessionID;


	public JDBViewWasteLog(String host, String session)
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
		setWasteReportTime(JUtility.getSQLDateTime());
		setTransactionType("");
		setLocationID("");
		setContainerID("");
		setProcessOrder("");
		setReasonID("");
		setUserID("");
		setMaterialID("");
		setMaterialType("");
		setTareWeight(new BigDecimal("0"));
		setWeightKG(new BigDecimal("0"));
		setCostTotal(new BigDecimal("0"));
		setComment("");
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

	public void setTareWeight(BigDecimal dbQuantity)
	{
		this.dbTareWeight = dbQuantity;
	}

	public BigDecimal getWeightKG()
	{
		return dbWeightKG;
	}

	public void setWeightKG(BigDecimal val)
	{
		this.dbWeightKG = val;
	}	
	
	public BigDecimal getNetWeightKG()
	{
		return dbNetWeight;
	}

	public void setNetWeightKG(BigDecimal val)
	{
		this.dbNetWeight = val;
	}
	
	public BigDecimal getCostTotal()
	{
		return dbCostTotal;
	}

	public void setCostTotal(BigDecimal val)
	{
		this.dbCostTotal = val;
	}	
	
	public BigDecimal getCostPerKg()
	{
		return dbCostPerKg;
	}

	public void setCostPerKg(BigDecimal val)
	{
		this.dbCostPerKg = val;
	}
	
	public String getLocationID()
	{
		return JUtility.replaceNullStringwithBlank(dbLocationID).trim();
	}
	
	public String getComment()
	{
		return JUtility.replaceNullStringwithBlank(dbComment).trim();
	}
	
	public String getContainerID()
	{
		return JUtility.replaceNullStringwithBlank(dbContainerID).trim();
	}

	public String getMaterialID()
	{
		return JUtility.replaceNullStringwithBlank(dbMaterialID).trim();
	}
	
	public String getMaterialType()
	{
		return JUtility.replaceNullStringwithBlank(dbMaterialType).trim();
	}
	
	public String getUOM()
	{
		return JUtility.replaceNullStringwithBlank(dbUOM).trim();
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

	public void setUserID(String user)
	{
		dbUserID = JUtility.replaceNullStringwithBlank(user).trim();
	}

	public String getTransactionType()
	{
		return JUtility.replaceNullStringwithBlank(dbTransactionType);
	}

	public String getUserID()
	{
		return JUtility.replaceNullStringwithBlank(dbUserID);
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

			setTransactionRef(rs.getLong("transaction_ref"));
			setWasteReportTime(rs.getTimestamp("report_time"));
			setTransactionType(rs.getString("waste_transaction_type"));
			setLocationID(rs.getString("waste_location_id"));
			setContainerID(rs.getString("waste_container_id"));
			setMaterialID(rs.getString("waste_material_id"));
			setMaterialType(rs.getString("waste_type_id"));
			setProcessOrder(rs.getString("process_order"));
			setReasonID(rs.getString("waste_reason_id"));
			setUserID(rs.getString("user_id"));
			setWeightKG(rs.getBigDecimal("weight_kg"));
			setTareWeight(rs.getBigDecimal("tare_weight"));
			setCostTotal(rs.getBigDecimal("cost"));
			setNetWeightKG(rs.getBigDecimal("net_weight"));
			setCostPerKg(rs.getBigDecimal("cost_per_kg"));
			setComment(rs.getString("waste_comment"));
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
	
	public void setComment(String str)
	{
		dbComment = str;
	}

	public void setContainerID(String str)
	{
		dbContainerID = str;
	}
	
	public void setMaterialID(String str)
	{
		dbMaterialID = str;
	}
	
	public void setMaterialType(String str)
	{
		dbMaterialType = str;
	}

	public void setUOM(String str)
	{
		dbUOM = str;
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

	public void setWasteReportTime(Timestamp res)
	{
		dbWasteReportTime = res;
	}


	public String toString()
	{
		return String.valueOf(getTransactionRef());
	}

}
