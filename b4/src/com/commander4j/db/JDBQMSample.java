package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMSample.java
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
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBQMSample inserts rows into the APP_QM_SAMPLE table when Sample Labels
 * are printed. Each sample is allocated a unique sample id. When results are
 * entered into the database they are linked back to the original sample using
 * this sample id.
 * <p>
 * <img alt="" src="./doc-files/APP_SAMPLE_ID.jpg" >
 * 
 * @see com.commander4j.db.JDBQMInspection JDBQMInspection
 * @see com.commander4j.db.JDBQMActivity JDBQMActivity
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary
  * @see com.commander4j.db.JDBQMTest JDBQMTest
 */
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
	private final Logger logger = Logger.getLogger(JDBQMSample.class);
	private String hostID;
	private String sessionID;

	public Long generateSampleID()
	{
		Long result = (long) 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String extensionID = "1";
		Long SeqNumber = (long) 0;

		if (ctrl.lockRecord("QM SAMPLE ID") == true)
		{
			if (ctrl.getProperties("QM SAMPLE ID") == true)
			{
				extensionID = ctrl.getKeyValue();
				SeqNumber = Long.parseLong(extensionID);

				SeqNumber++;
				extensionID = String.valueOf(SeqNumber);
				ctrl.setKeyValue(extensionID);
				ctrl.update();
				result = SeqNumber;
			} else
			{
				result = (long) 1;
				ctrl.create("QM SAMPLE ID", "1", "Unique Sample Record ID");
			}
		} else
		{
			result = (long) -1;
			setErrorMessage(ctrl.getErrorMessage());
		}

		return result;
	}

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

	public boolean create(Long sampleid, String inspectionid, String activityid, String processOrder, String material, String userdata1, String userdata2, String userdata3, String userdata4, Timestamp sampleTime)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setSampleID(sampleid);
			setInspectionID(inspectionid);
			setActivityID(activityid);
			setProcessOrder(processOrder);
			setMaterial(material);
			setUserData1(userdata1);
			setUserData2(userdata2);
			setUserData3(userdata3);
			setUserData4(userdata4);
			setSampleDate(sampleTime);

			if (isValidSample() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSample.create"));
				stmtupdate.setLong(1, getSampleID());
				stmtupdate.setString(2, getInspectionID());
				stmtupdate.setString(3, getActivityID());
				stmtupdate.setString(4, getProcessOrder());
				stmtupdate.setString(5, getMaterial());
				stmtupdate.setTimestamp(6, getSampleDate());
				stmtupdate.setString(7, Common.userList.getUser(getSessionID()).getUserId());
				stmtupdate.setString(8, getUserData1());
				stmtupdate.setString(9, getUserData2());
				stmtupdate.setString(10, getUserData3());
				stmtupdate.setString(11, getUserData4());				
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("QMSample already exists");
			}
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
			if (isValidSample() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSample.delete"));
				stmtupdate.setLong(1, getSampleID());
				stmtupdate.setString(2, getInspectionID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
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

	public LinkedList<JDBQMSample> getSamples(String processOrder, String inspectionid, String activityid)
	{
		LinkedList<JDBQMSample> typeList = new LinkedList<JDBQMSample>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setProcessOrder(processOrder);
		setInspectionID(inspectionid);
		setActivityID(activityid);
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSample.getSamples"));
			stmt.setString(1, getProcessOrder());
			stmt.setString(2, getInspectionID());
			stmt.setString(3, getActivityID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBQMSample mt = new JDBQMSample(getHostID(), getSessionID());
				mt.setSampleID(rs.getLong("sample_id"));
				mt.setInspectionID(rs.getString("inspection_id"));
				mt.setActivityID(rs.getString("activity_id"));
				mt.setMaterial(rs.getString("material"));
				mt.setProcessOrder(rs.getString("process_order"));
				mt.setUserID(rs.getString("user_id"));
				mt.setSampleDate(rs.getTimestamp("sample_date"));
				mt.setUserData1(rs.getString("user_data_1"));
				mt.setUserData2(rs.getString("user_data_2"));
				mt.setUserData3(rs.getString("user_data_3"));
				mt.setUserData4(rs.getString("user_data_4"));
				typeList.add(mt);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return typeList;
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

	public String toString()
	{
		String result = "";
		if (getInspectionID().equals("") == false)
		{
			result = JUtility.padString(getInspectionID(), true, JDBQMInspection.field_inspection_id, " ") + " - " + getSampleDate();
		} else
		{
			result = "";
		}

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidSample() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSample.update"));
				stmtupdate.setString(1, getInspectionID());
				stmtupdate.setString(2, getActivityID());
				stmtupdate.setString(3, getProcessOrder());
				stmtupdate.setString(4, getMaterial());
				stmtupdate.setTimestamp(5, getSampleDate());
				stmtupdate.setString(6, Common.userList.getUser(getSessionID()).getUserId());
				stmtupdate.setString(7, getUserData1());
				stmtupdate.setString(8, getUserData2());
				stmtupdate.setString(9, getUserData3());
				stmtupdate.setString(10, getUserData4());
				stmtupdate.setLong(11, getSampleID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
}
