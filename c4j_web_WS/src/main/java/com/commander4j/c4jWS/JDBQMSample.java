package com.commander4j.c4jWS;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.Logger;


public class JDBQMSample
{
	private String dbUserID;
	private String dbMaterial;
	private String dbInspectionID;
	private Long dbSampleID;
	private Timestamp dbSampleDate;
	private String dbProcessOrder;
	private String dbErrorMessage;
	private String dbUserData1;
	private String dbUserData2;
	private String dbUserData3;
	private String dbUserData4;
	private String dbActivityID;
	public static int field_data_1 = 20;
	public static int field_data_2 = 20;
	public static int field_data_3 = 20;
	public static int field_data_4 = 20;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBQMSample.class);
	private String hostID;
	private String sessionID;


	public JDBQMSample(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBQMSample(String host, String session, Long sampleid, String inspectionid, String activityid, String processorder, String material, String userid, Timestamp sampledate, String userData1, String userData2, String userData3, String userData4)
	{
		setHostID(host);
		setSessionID(session);
		setSampleID(sampleid);
		setProcessOrder(processorder);
		setMaterial(material);
		setInspectionID(inspectionid);
		setActivityID(activityid);
		setUserID(userid);
		setSampleDate(sampledate);
		setUserData1(userData1);
		setUserData2(userData2);
		setUserData3(userData3);
		setUserData4(userData4);		
	}

	public void clear()
	{
		setMaterial("");
		setProcessOrder("");
		setUserID("");
		setActivityID("");
		setSampleDate(null);
		setInspectionID("");
		setUserData1("");
		setUserData2("");
		setUserData3("");
		setUserData4("");
	}

	public ResultSet getQMSampleDataResultSet(String inspectionid, String activityid)
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");
		setInspectionID(inspectionid);
		setActivityID(activityid);
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSample.getSamples"));
			stmt.setString(1, getInspectionID());
			stmt.setString(2, getActivityID());
			stmt.setFetchSize(200);
			rs = stmt.executeQuery();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}


	public boolean getResultsProperties(String inspectionid, String activityid)
	{
		setInspectionID(inspectionid);
		setActivityID(activityid);
		return getProperties();
	}

	public boolean getProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("Sample.getProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSample.getProperties"));
			stmt.setLong(1, getSampleID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setInspectionID(rs.getString("inspection_id"));
				setActivityID(rs.getString("activity_id"));
				setProcessOrder(rs.getString("process_order"));
				setMaterial(rs.getString("material"));
				setSampleDate(rs.getTimestamp("sample_date"));
				setUserID(rs.getString("user_id"));
				setUserData1(rs.getString("user_data_1"));
				setUserData2(rs.getString("user_data_2"));
				setUserData3(rs.getString("user_data_3"));
				setUserData4(rs.getString("user_data_4"));
				result = true;
				rs.close();
				stmt.close();
			} else
			{
				setErrorMessage("Invalid Sample ID");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public Long getSampleID()
	{
		return dbSampleID;
	}

	public String getInspectionID()
	{
		String result = "";
		if (dbInspectionID != null)
			result = dbInspectionID;
		return result;
	}

	public String getMaterial()
	{
		String result = "";
		if (dbMaterial != null)
			result = dbMaterial;
		return result;
	}

	public String getProcessOrder()
	{
		String result = "";
		if (dbProcessOrder != null)
			result = dbProcessOrder;
		return result;
	}

	public String getUserData1()
	{
		String result = "";
		if (dbUserData1 != null)
			result = dbUserData1;
		return result;
	}

	public String getUserData2()
	{
		String result = "";
		if (dbUserData2 != null)
			result = dbUserData2;
		return result;
	}
	
	public String getUserData3()
	{
		String result = "";
		if (dbUserData3 != null)
			result = dbUserData3;
		return result;
	}
	
	public String getUserData4()
	{
		String result = "";
		if (dbUserData4 != null)
			result = dbUserData4;
		return result;
	}

	public String getUserID()
	{
		String result = "";
		if (dbUserID != null)
			result = dbUserID;
		return result;
	}

	public Timestamp getSampleDate()
	{
		return dbSampleDate;
	}

	public String getActivityID()
	{
		return dbActivityID;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean isValidSample(Long sampleid)
	{
		setSampleID(sampleid);
		return isValidSample();
	}

	public boolean isValidSample()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSample.isValid"));
			stmt.setLong(1, getSampleID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Sample [" + String.valueOf(getSampleID()) + "]");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

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

	public void setSampleID(Long sampleid)
	{
		dbSampleID = sampleid;
	}

	public void setInspectionID(String inspectid)
	{
		dbInspectionID = inspectid;
	}

	public void setUserID(String userid)
	{
		dbUserID = userid;
	}

	public void setMaterial(String value)
	{
		dbMaterial = value;
	}

	public void setProcessOrder(String po)
	{
		dbProcessOrder = po;
	}

	public void setUserData1(String ud1)
	{
		dbUserData1 = ud1;
	}

	public void setUserData2(String ud2)
	{
		dbUserData2 = ud2;
	}

	public void setUserData3(String ud3)
	{
		dbUserData3 = ud3;
	}

	public void setUserData4(String ud4)
	{
		dbUserData4 = ud4;
	}
	
	public void setActivityID(String actid)
	{
		dbActivityID = actid;
	}

	public void setSampleDate(Timestamp sampledate)
	{
		dbSampleDate = sampledate;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

}
