package com.commander4j.db;

import java.math.BigDecimal;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBPalletExtension.java
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

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBPalletSamples
{
	public static int field_sample_reason = 15;
	public static int field_sample_defect_id = 12;
	public static int field_sample_leaking = 1;
	public static int field_sample_comment = 80;

	private String dbErrorMessage;
	private String dbSSCC;
	private Long dbSampleSequence;
	private Timestamp dbSampleDateTime;
	private String dbSampleReason;
	private String dbSampleDefectType;
	private String dbSampleDefectID;
	private BigDecimal dbSampleQuantity = new BigDecimal("0.00");
	private String dbSampleLeaking = "";
	private String dbSampleComment;
	private String dbUserID;
	private String dbOperative;
	private String dbSamplePoint;
	private long sample_sequence = 1;

	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBPalletSamples.class);
	private String hostID;
	private String sessionID;

	public JDBPalletSamples(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBPalletSamples(String sscc, Long sampleSequence,Timestamp sampledate, String reason, String defecttype, String defectid, BigDecimal sampleQuantity, String leaking, String comment, String user, String samplepoint, String operative)
	{
		setSSCC(sscc);
		setSampleSequence(sampleSequence);
		setSampleDateTime(sampledate);
		setSampleReason(reason);
		setSampleDefectType(defecttype);
		setSampleDefectID(defectid);
		setSampleQuantity(sampleQuantity);
		setSampleLeaking(leaking);
		setSampleComment(comment);
		setUserID(user);
		setSamplePoint(samplepoint);
		setOperative(operative);
	}

	public void clear()
	{
		setSampleDateTime(JUtility.getSQLDateTime());
		setSampleReason("");
		setSampleDefectType("");
		setSampleDefectID("");
		setSampleQuantity(new BigDecimal("0.00"));
		setSampleLeaking("");
		setSampleComment("");
		setUserID("");
		setSamplePoint("");
		setOperative("");
	}

	public boolean create()
	{

		logger.debug("create [" + getSSCC() + "] DateTime [" + JUtility.getISODateStringFormat(getSampleDateTime()));

		boolean result = false;

		if (isValid() == false)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletSamples.create"));
				stmtupdate.setString(1, getSSCC());
				stmtupdate.setLong(2, getSampleSequence());
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

		return result;
	}

	public boolean create(String sscc, Long ps)
	{
		setSSCC(sscc);
		setSampleSequence(ps);

		return create();
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletSamples.delete"));
			stmtupdate.setString(1, getSSCC());
			stmtupdate.setLong(2, getSampleSequence());
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

		return result;
	}

	public boolean delete(String sscc, Long seq)
	{
		boolean result = false;
		setSSCC(sscc);
		setSampleSequence(seq);
		result = delete();
		return result;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getSampleReason()
	{
		return JUtility.replaceNullObjectwithBlank(dbSampleReason);
	}
	
	public String getSampleDefectType()
	{
		return JUtility.replaceNullObjectwithBlank(dbSampleDefectType);
	}

	public String getSampleDefectID()
	{
		return JUtility.replaceNullObjectwithBlank(dbSampleDefectID);
	}

	public Timestamp getSampleDateTime()
	{

		return dbSampleDateTime;
	}

	private String getHostID()
	{
		return hostID;
	}

	public BigDecimal getSampleQuantity()
	{

		return dbSampleQuantity;
	}
	

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setSSCC(rs.getString("sscc"));
			setSampleSequence(rs.getLong("sample_sequence"));
			setSampleDateTime(rs.getTimestamp("sample_date"));
			setSampleReason(rs.getString("sample_reason"));
			setSampleDefectType(rs.getString("defect_type"));
			setSampleDefectID(rs.getString("defect_id"));
			setSampleLeaking(rs.getString("leaking"));
			setSampleComment(rs.getString("sample_comment"));
			setSampleQuantity(rs.getBigDecimal("sample_quantity"));
			setUserID(rs.getString("user_id"));
			setSamplePoint(rs.getString("sample_point"));
			setOperative(rs.getString("id"));

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public boolean getPalletSampleProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletSamples.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getSSCC());
			stmt.setLong(2, getSampleSequence());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Unknown SSCC [" + getSSCC() + "] Sequence [" + String.valueOf(getSampleSequence()+"]"));
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

	public boolean getPalletSampleProperties(String sscc, Long ps)
	{
		setSSCC(sscc);
		setSampleSequence(ps);

		return getPalletSampleProperties();
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getSampleLeaking()
	{
		return JUtility.replaceNullStringwithBlank(dbSampleLeaking);
	}
	
	public boolean isSampleLeaking()
	{
		boolean result = false;
		
		if (getSampleLeaking().equals("Y"))
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}

	public String getSSCC()
	{
		return dbSSCC;
	}

	public String getSampleComment()
	{
		return JUtility.replaceNullStringwithBlank(dbSampleComment);
	}

	public String getUserID()
	{
		return JUtility.replaceNullStringwithBlank(dbUserID);
	}
	
	public String getOperative()
	{
		return JUtility.replaceNullStringwithBlank(dbOperative);
	}

	public boolean isValid()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletSamples.isValid"));
			stmt.setString(1, getSSCC());
			stmt.setLong(2, getSampleSequence());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Unknown SSCC [" + getSSCC() + "] Sequence [" + String.valueOf(getSampleSequence()+"]"));
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

	public boolean isValid(String sscc)
	{
		setSSCC(sscc);

		return isValid();
	}

	private void setErrorMessage(String errorMsg)
	{
		dbErrorMessage = errorMsg;
	}

	public void setSampleReason(String yes)
	{
		dbSampleReason = JUtility.replaceNullStringwithBlank(yes);
	}

	public void setSampleDateTime(Timestamp dt)
	{
		try
		{
			dbSampleDateTime = dt;
		}
		catch (Exception ex)
		{

		}

	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setSampleDefectType(String yes)
	{

		dbSampleDefectType = JUtility.replaceNullStringwithBlank(yes);

	}
	
	public void setSampleDefectID(String yes)
	{

		dbSampleDefectID = JUtility.replaceNullStringwithBlank(yes);

	}

	public void setSampleQuantity(BigDecimal removed)
	{
		dbSampleQuantity = removed;
	}
	
	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setSampleLeaking(String leaking)
	{
		dbSampleLeaking = leaking;
	}

	public void setSampleLeaking(boolean leaking)
	{
		if (leaking)
		{
			dbSampleLeaking = "Y";
		}
		else
		{
			dbSampleLeaking = "N";
		}
	}

	public void setSSCC(String sscc)
	{
		dbSSCC = sscc;
	}

	public void setSampleComment(String comm)
	{
		dbSampleComment = comm;
	}

	public void setSamplePoint(String sp)
	{
		dbSamplePoint = JUtility.replaceNullStringwithBlank(sp);
	}
	
	public String getSamplePoint()
	{
		return JUtility.replaceNullStringwithBlank(dbSamplePoint);
	}
	
	public void setUserID(String user)
	{
		dbUserID = user;
	}

	public void setOperative(String operative)
	{
		dbOperative = operative;
	}
	
	public ResultSet getPalletDataResultSet(PreparedStatement criteria)
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
	
	public Long getSampleSequence()
	{
		return dbSampleSequence;
	}
	
	public void setSampleSequence(Long ps)
	{
		dbSampleSequence = ps;
	}

	public boolean update()
	{
		boolean result = false;

		if (isValid() == true)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletSamples.update"));

				stmtupdate.setTimestamp(1, getSampleDateTime());
				stmtupdate.setString(2, getSampleReason());
				stmtupdate.setString(3, getSampleDefectType());
				stmtupdate.setString(4, getSampleDefectID());
				stmtupdate.setString(5, getSampleLeaking());
				stmtupdate.setString(6, getSampleComment());
				stmtupdate.setBigDecimal(7, getSampleQuantity());
				stmtupdate.setString(8, getUserID());
				stmtupdate.setString(9, getSamplePoint());
				stmtupdate.setString(10, getOperative());
				stmtupdate.setString(11, getSSCC());
				stmtupdate.setLong(12, getSampleSequence());
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
	
	public long generateNewSampleSequence()
	{
		long result = 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		String sample_sequence_str = "1";
		
		ctrl.getKeyValueWithDefault("PALLET SAMPLE SEQ", sample_sequence_str, "Unique Sequence Number used by Pallet Sampling/Sorting");

		boolean retry = true;
		@SuppressWarnings("unused")
		int counter = 0;

		if (ctrl.getProperties("PALLET SAMPLE SEQ") == true)
		{
			do
			{
				if (ctrl.lockRecord("PALLET SAMPLE SEQ") == true)
				{
					if (ctrl.getProperties("PALLET SAMPLE SEQ") == true)
					{
						sample_sequence_str = ctrl.getKeyValue();
						sample_sequence = Long.parseLong(sample_sequence_str);
						sample_sequence++;
						temp = String.valueOf(sample_sequence);
						ctrl.setKeyValue(temp);

						if (ctrl.update())
						{
							retry = false;
						}
					}
				} else
				{
					retry = true;
					counter++;
				}
			} while (retry);
		}

		result = sample_sequence;

		logger.debug("Pallet Sample Sequence :" + result);
		return result;
	}
}
