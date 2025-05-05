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

public class JDBViewPalletSample
{

	private String dbErrorMessage;
	private String dbSSCC;
	private long dbSampleSequence;
	private Timestamp dbSampleDate;
	private Timestamp dbProductionDate;
	private String dbSampleReasonID;
	private String dbSampleDefectType;
	private String dbSampleDefectID;
	private String dbLeaking;
	private String dbComment;
	private String dbOperative;
	private BigDecimal dbStartQuantity;
	private BigDecimal dbSampleQuantity;
	private String dbLocation;
	private String dbSamplePoint;
	private String dbMaterialID;
	private String dbProcessOrder;
	private String dbMHNNumber;
	private BigDecimal dbLane1Quantity = new BigDecimal("0.00");
	private BigDecimal dbLane2Quantity = new BigDecimal("0.00");
	private BigDecimal dbLane3Quantity = new BigDecimal("0.00");
	private BigDecimal dbLane4Quantity = new BigDecimal("0.00");
	private BigDecimal dbLane5Quantity = new BigDecimal("0.00");
	private BigDecimal zero = new BigDecimal("0.00");

	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBViewPalletSample.class);
	private String hostID;
	private String sessionID;

	public JDBViewPalletSample(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setSSCC("");
		setSampleSequence(0);
		setSampleDate(JUtility.getSQLDateTime());
		setProductionDate(JUtility.getSQLDateTime());
		setSampleReason("");
		setSampleDefectType("");
		setSampleDefectID("");
		setLeaking("");
		setComment("");
		setOperative("");
		setStartQuantity(new BigDecimal("0"));
		setSampleQuantity(new BigDecimal("0"));
		setLocation("");
		setSamplePoint("");
		setMaterialID("");
		setProcessOrder("");
		setMHNNumber("");
		setLane1Quantity(new BigDecimal("0"));
		setLane2Quantity(new BigDecimal("0"));
		setLane3Quantity(new BigDecimal("0"));
		setLane4Quantity(new BigDecimal("0"));
		setLane5Quantity(new BigDecimal("0"));
	}

	public void setLane1Quantity(BigDecimal removed)
	{
		if (removed == null)
		{
			removed = zero;
		}	
		dbLane1Quantity = removed;
	}
	
	public void setLane2Quantity(BigDecimal removed)
	{
		if (removed == null)
		{
			removed = zero;
		}		
		dbLane2Quantity = removed;
	}
	
	public void setLane3Quantity(BigDecimal removed)
	{
		if (removed == null)
		{
			removed = zero;
		}
		dbLane3Quantity = removed;
	}
	
	public void setLane4Quantity(BigDecimal removed)
	{
		if (removed == null)
		{
			removed = zero;
		}
		dbLane4Quantity = removed;
	}
	
	public void setLane5Quantity(BigDecimal removed)
	{
		if (removed == null)
		{
			removed = zero;
		}
		dbLane5Quantity = removed;
	}
	
	public BigDecimal getLane1Quantity()
	{
		if (dbLane1Quantity == null)
		{
			dbLane1Quantity =  zero;
		}
		return dbLane1Quantity;
	}
	
	public BigDecimal getLane2Quantity()
	{
		if (dbLane2Quantity == null)
		{
			dbLane2Quantity =  zero;
		}
		return dbLane2Quantity;
	}
	
	public BigDecimal getLane3Quantity()
	{
		if (dbLane3Quantity == null)
		{
			dbLane3Quantity =  zero;
		}
		return dbLane3Quantity;
	}
	
	public BigDecimal getLane4Quantity()
	{
		if (dbLane4Quantity == null)
		{
			dbLane4Quantity =  zero;
		}
		return dbLane4Quantity;
	}
	
	public BigDecimal getLane5Quantity()
	{
		if (dbLane5Quantity == null)
		{
			dbLane5Quantity =  zero;
		}
		return dbLane5Quantity;
	}
	
	public String getComment()
	{
		return JUtility.replaceNullStringwithBlank(dbComment).trim();
	}
	
	public String getOperative()
	{
		return JUtility.replaceNullStringwithBlank(dbOperative).trim();
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	@SuppressWarnings("unused")
	private String getHostID()
	{
		return hostID;
	}

	public String getLeaking()
	{
		return JUtility.replaceNullStringwithBlank(dbLeaking);
	}

	public boolean isLeaking()
	{
		boolean result = false;
		if (JUtility.replaceNullStringwithBlank(dbLeaking).equals("Y"))
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
	
	public String getLocation()
	{
		return JUtility.replaceNullStringwithBlank(dbLocation).trim();
	}
	
	public String getSamplePoint()
	{
		return JUtility.replaceNullStringwithBlank(dbSamplePoint).trim();
	}

	public String getMaterialID()
	{
		return JUtility.replaceNullStringwithBlank(dbMaterialID).trim();
	}

	public String getMHNNumber()
	{
		return JUtility.replaceNullStringwithBlank(dbMHNNumber);
	}

	public String getProcessOrder()
	{
		return JUtility.replaceNullStringwithBlank(dbProcessOrder).trim();
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setSSCC(rs.getString("sscc"));
			setSampleSequence(rs.getLong("sample_sequence"));
			setProductionDate(rs.getTimestamp("date_of_manufacture"));
			setSampleDate(rs.getTimestamp("sample_date"));
			setSampleReason(rs.getString("sample_reason"));
			setSampleDefectType(rs.getString("defect_type"));
			setSampleDefectID(rs.getString("defect_id"));
			setLeaking(rs.getString("leaking"));
			setComment(rs.getString("sample_comment"));
			setStartQuantity(rs.getBigDecimal("start_quantity"));
			setSampleQuantity(rs.getBigDecimal("sample_quantity"));
			setLocation(rs.getString("location"));
			setSamplePoint(rs.getString("sample_point"));
			setMaterialID(rs.getString("material"));
			setProcessOrder(rs.getString("process_order"));
			setMHNNumber(rs.getString("mhn_number"));
			setOperative(rs.getString("id"));
			setLane1Quantity(rs.getBigDecimal("lane_1"));
			setLane2Quantity(rs.getBigDecimal("lane_2"));
			setLane3Quantity(rs.getBigDecimal("lane_3"));
			setLane4Quantity(rs.getBigDecimal("lane_4"));
			setLane5Quantity(rs.getBigDecimal("lane_5"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getSampleDefectID()
	{
		return JUtility.replaceNullStringwithBlank(dbSampleDefectID).trim();
	}

	public String getSampleDefectType()
	{
		return JUtility.replaceNullStringwithBlank(dbSampleDefectType).trim();
	}

	public BigDecimal getSampleQuantityt()
	{
		return dbSampleQuantity;
	}

	public String getSampleReasonID()
	{
		return JUtility.replaceNullStringwithBlank(dbSampleReasonID).trim();
	}

	public long getSampleSequence()
	{
		return dbSampleSequence;
	}

	@SuppressWarnings("unused")
	private String getSessionID()
	{
		return sessionID;
	}

	public String getSSCC()
	{
		return JUtility.replaceNullStringwithBlank(dbSSCC).trim();
	}

	public BigDecimal getStartQuantity()
	{
		return dbStartQuantity;
	}

	public ResultSet getPalletSampleResultSet(PreparedStatement criteria)
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

	public Timestamp getSampleDate()
	{
		return dbSampleDate;
	}

	public Timestamp getProductionDate()
	{
		return dbProductionDate;
	}

	
	public void setComment(String str)
	{
		dbComment = str;
	}
	
	public void setOperative(String str)
	{
		dbOperative = str;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setLeaking(String desc)
	{
		dbLeaking = desc;
	}

	public void setLocation(String str)
	{
		dbLocation = str;
	}
	
	public void setSamplePoint(String str)
	{
		dbSamplePoint = str;
	}

	public void setMaterialID(String str)
	{
		dbMaterialID = str;
	}

	public void setMHNNumber(String user)
	{
		dbMHNNumber = JUtility.replaceNullStringwithBlank(user).trim();
	}

	public void setProcessOrder(String str)
	{
		dbProcessOrder = str;
	}

	public void setSampleDate(Timestamp res)
	{
		dbSampleDate = res;
	}

	public void setProductionDate(Timestamp res)
	{
		dbProductionDate = res;
	}
	
	public void setSampleDefectID(String str)
	{
		dbSampleDefectID = str;
	}

	public void setSampleDefectType(String str)
	{
		dbSampleDefectType = str;
	}

	public void setSampleQuantity(BigDecimal val)
	{
		this.dbSampleQuantity = val;
	}

	public void setSampleReason(String user)
	{
		dbSampleReasonID = JUtility.replaceNullStringwithBlank(user).trim();
	}

	public void setSampleSequence(long transactionRef)
	{
		dbSampleSequence = transactionRef;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setSSCC(String str)
	{
		dbSSCC = str;
	}

	public void setStartQuantity(BigDecimal dbQuantity)
	{
		this.dbStartQuantity = dbQuantity;
	}

	public String toString()
	{
		return String.valueOf(getSampleSequence());
	}

}
