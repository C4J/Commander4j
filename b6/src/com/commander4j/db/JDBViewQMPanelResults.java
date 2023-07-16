package com.commander4j.db;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.commander4j.util.JUtility;

public class JDBViewQMPanelResults
{
	public static int field_PanelID = 10;
	public static int field_TrayID = 10;
	public static int field_SampleID = 10;
	
	public static int field_MaterialID = 25;
	public static int field_ProcessOrder = 10;
	public static int field_Result = 20;
	public static int field_UserID = 20;
	public static int field_Plant = 20;

	private String dbErrorMessage;
	
	private long dbPanelID; /* PK */
	private long dbTrayID;
	private long dbSampleID;
	
	private Timestamp dbSampleDate;
	private Timestamp dbPanelDate;
	
	private String dbStatus;

	private String dbTrayDescription;
	
	private String dbPlant;
	private String dbUserID;
	
	private String dbResult;
	private String dbResultDescription;
	
	private String dbProcessOrder;

	private String dbMaterial;
	private String dbUserData1;
	private String dbUserData2;
	private String dbUserData3;
	private String dbUserData4;
	
	private String dbFirstname;
	private String dbSurname;
	
	private final Logger logger = Logger.getLogger(JDBWasteLog.class);
	private String hostID;
	private String sessionID;


	public JDBViewQMPanelResults(String host, String session)
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
		setPanelID((long) 0);
		setTrayID((long) 0);
		setSampleID((long) 0);
		setSampleDate(JUtility.getSQLDateTime());
		setPanelDate(JUtility.getSQLDateTime());
		setStatus("");
		setTrayDescription("");
		setPlant("");
		setUserID("");
		setResult("");
		setResultDescription("");
		setProcessOrder("");
		setMaterial("");
		setUserData1("");
		setUserData2("");
		setUserData3("");
		setUserData4("");
		setFirstName("");
		setSurname("");

	}

	public long getPanelID()
	{
		return dbPanelID;
	}

	public void setPanelID(long panelid)
	{
		dbPanelID = panelid;
	}

	public String getMaterial()
	{
		return dbMaterial;
	}

	public void setMaterial(String dbQuantity)
	{
		this.dbMaterial = dbQuantity;
	}

	public String getUserData1()
	{
		return dbUserData1;
	}

	public void setUserData1(String val)
	{
		this.dbUserData1 = val;
	}	
	
	public String getUserData2()
	{
		return dbUserData2;
	}

	public void setUserData2(String val)
	{
		this.dbUserData2 = val;
	}
	
	public String getUserData3()
	{
		return dbUserData3;
	}

	public void setUserData3(String val)
	{
		this.dbUserData3 = val;
	}
	
	public String getUserData4()
	{
		return dbUserData4;
	}

	public void setUserData4(String val)
	{
		this.dbUserData4 = val;
	}
	
	public String getFirstName()
	{
		return dbFirstname;
	}

	public void setFirstName(String val)
	{
		this.dbFirstname = val;
	}
	
	public String getSurname()
	{
		return dbSurname;
	}

	public void setSurname(String val)
	{
		this.dbSurname = val;
	}
	
	public Long getTrayID()
	{
		return dbTrayID;
	}
	
	public String getStatus()
	{
		return JUtility.replaceNullStringwithBlank(dbStatus).trim();
	}
	
	public Long getSampleID()
	{
		return dbSampleID;
	}

	public String getTrayDescription()
	{
		return JUtility.replaceNullStringwithBlank(dbTrayDescription).trim();
	}
	
	public String getPlant()
	{
		return JUtility.replaceNullStringwithBlank(dbPlant).trim();
	}
	
	public String getResult()
	{
		return JUtility.replaceNullStringwithBlank(dbResult).trim();
	}

	public String getProcessOrder()
	{
		return JUtility.replaceNullStringwithBlank(dbProcessOrder).trim();
	}

	public String getResultDescription()
	{
		return JUtility.replaceNullStringwithBlank(dbResultDescription).trim();
	}

	public void setResultDescription(String user)
	{
		dbResultDescription = JUtility.replaceNullStringwithBlank(user).trim();
	}

	public void setUserID(String user)
	{
		dbUserID = JUtility.replaceNullStringwithBlank(user).trim();
	}

	public Timestamp getPanelDate()
	{
		return dbPanelDate;
	}

	public String getUserID()
	{
		return JUtility.replaceNullStringwithBlank(dbUserID);
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public ResultSet getPanelResultSet(PreparedStatement criteria)
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

			setPanelID(rs.getLong("panel_id"));
			setTrayID(rs.getLong("tray_id"));
			setSampleID(rs.getLong("sample_id"));
			setSampleDate(rs.getTimestamp("sample_date"));
			setPanelDate(rs.getTimestamp("panel_date"));
			setStatus(rs.getString("status"));
			setTrayDescription(rs.getString("tray_description"));
			setPlant(rs.getString("plant"));
			setUserID(rs.getString("user_id"));
			setResult(rs.getString("value"));
			setResultDescription(rs.getString("result_description"));
			setProcessOrder(rs.getString("process_order"));
			setMaterial(rs.getString("material"));
			setUserData1(rs.getString("user_data_1"));
			setUserData2(rs.getString("user_data_2"));
			setUserData3(rs.getString("user_data_3"));
			setUserData4(rs.getString("user_data_4"));
			setFirstName(rs.getString("first_name"));
			setSurname(rs.getString("surname"));

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public Timestamp getSampleDate()
	{
		return dbSampleDate;
	}

	public void setTrayID(Long str)
	{
		dbTrayID = str;
	}
	
	public void setStatus(String str)
	{
		dbStatus = str;
	}

	public void setSampleID(Long str)
	{
		dbSampleID = str;
	}
	
	public void setTrayDescription(String str)
	{
		dbTrayDescription = str;
	}
	
	public void setPlant(String str)
	{
		dbPlant = str;
	}

	public void setResult(String str)
	{
		dbResult = str;
	}
	
	public void setProcessOrder(String str)
	{
		dbProcessOrder = str;
	}

	public void setPanelDate(Timestamp desc)
	{
		dbPanelDate = desc;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setSampleDate(Timestamp res)
	{
		dbSampleDate = res;
	}


	public String toString()
	{
		return String.valueOf(getPanelID());
	}

}
