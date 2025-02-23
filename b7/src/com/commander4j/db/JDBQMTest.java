package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMTest.java
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
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBQMTest inserts/updates/deletes rows into the APP_QM_TEST table. This
 * table defines the tests which are required for the associated Inspection /
 * Activity.
 * <p>
 * <img alt="" src="./doc-files/APP_SAMPLE_ID.jpg" >
 * 
 * @see com.commander4j.db.JDBQMInspection JDBQMInspection
 * @see com.commander4j.db.JDBQMActivity JDBQMActivity
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary
 * @see com.commander4j.db.JDBQMSample JDBQMSample
 */
public class JDBQMTest
{
	private String dbActivityID;
	private String dbInspectionID;
	private String dbTestID;
	private String dbErrorMessage;
	private Long dbSequence;
	public static int field_test_id = 35;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBQMTest.class);
	private JDBQMDictionary dict;
	private String hostID;
	private String sessionID;
	private Long dbExtensionID;

	/*
	 * 
	 * Table: APP_QM_TEST
	 * 
	 * Columns: INSPECTION_ID varchar(20) PK ACTIVITY_ID varchar(10) PK
	 * SEQUENCE_ID int(11) PK TEST_ID varchar(50)
	 */

	public Long getExtensionID()
	{
		Long result = (long) -1;
		if (dbExtensionID != null)
			result = dbExtensionID;
		return result;
	}

	public void setExtensionID(Long extensionid)
	{
		dbExtensionID = extensionid;
	}

	public JDBQMTest(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		dict = new JDBQMDictionary(getHostID(), getSessionID());
	}

	public JDBQMTest(String host, String session, String inspectionid, String activityid, String testid)
	{
		setHostID(host);
		setSessionID(session);
		setInspectionID(inspectionid);
		setActivityID(activityid);
		setTestID(testid);
		dict = new JDBQMDictionary(getHostID(), getSessionID());
	}

	public String getDescription()
	{
		String result = "";
		if (dict.getProperties(getTestID()))
		{
			result = dict.getDescription();
		}
		return result;
	}

	public void clear()
	{
		setSequence((long) -1);
	}

	public ResultSet getQMTestDataResultSet(String inspectionid, String activityid)
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");
		setInspectionID(inspectionid);
		setActivityID(activityid);
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTest.getTests"));
			stmt.setString(1, getInspectionID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public boolean create(String inspectionid, String activityid, String testid, Long sequence)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setInspectionID(inspectionid);
			setActivityID(activityid);
			setSequence(sequence);
			setTestID(testid);

			if (isValidTest() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTest.create"));
				stmtupdate.setString(1, getInspectionID());
				stmtupdate.setString(2, getActivityID());
				stmtupdate.setString(3, getTestID());
				stmtupdate.setLong(4, getSequence());
				JDBQMExtension qmExtension = new JDBQMExtension(getHostID(), getSessionID());
				setExtensionID(qmExtension.generateExtensionID());
				qmExtension = null;
				stmtupdate.setLong(5, getExtensionID());

				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("QMTest already exists");
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
			if (isValidTest() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTest.delete"));
				stmtupdate.setString(1, getInspectionID());
				stmtupdate.setString(2, getActivityID());
				stmtupdate.setString(3, getTestID());
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

	public boolean getProperties(String inspectionid, String activityid, String testid)
	{
		setInspectionID(inspectionid);
		setActivityID(activityid);
		setTestID(testid);
		return getProperties();
	}

	public boolean getProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("JDBQMTest getProperties Inspection [" + getInspectionID() + "] Activity [" + getActivityID() + "] Test [" + getTestID() + "]");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTest.getProperties"));
			stmt.setString(1, getInspectionID());
			stmt.setString(2, getActivityID());
			stmt.setString(3, getTestID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setSequence(rs.getLong("sequence_id"));
				setExtensionID(rs.getLong("extension_id"));
				result = true;
				rs.close();
				stmt.close();
			} else
			{
				setErrorMessage("Invalid Test [" + getInspectionID().toString() + "/" + getActivityID().toString() + "/" + getTestID().toString() + "]");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public LinkedList<JDBQMTest> getTests(String inspectionid, String activity)
	{
		LinkedList<JDBQMTest> testList = new LinkedList<JDBQMTest>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setInspectionID(inspectionid);
		setActivityID(activity);
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTest.getTests"));
			stmt.setString(1, getInspectionID());
			stmt.setString(2, getActivityID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBQMDictionary dict = new JDBQMDictionary(getHostID(), getSessionID());
				if (dict.getProperties(rs.getString("test_id")))
				{
					if (dict.getVisible().equals("Y"))
					{
						JDBQMTest mt = new JDBQMTest(getHostID(), getSessionID());
						mt.setInspectionID(rs.getString("inspection_id"));
						mt.setActivityID(rs.getString("activity_id"));
						mt.setTestID(rs.getString("test_id"));
						mt.setSequence(rs.getLong("sequence_id"));
						mt.setExtensionID(rs.getLong("extension_id"));
						testList.add(mt);
					}
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return testList;
	}

	public LinkedList<JDBQMDictionary> getTestsPropertiesList(String inspectionid, String activity)
	{
		LinkedList<JDBQMDictionary> testList = new LinkedList<JDBQMDictionary>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setInspectionID(inspectionid);
		setActivityID(activity);
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTest.getTests"));
			stmt.setString(1, getInspectionID());
			stmt.setString(2, getActivityID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{

				JDBQMDictionary mt = new JDBQMDictionary(getHostID(), getSessionID());
				if (mt.getProperties(rs.getString("test_id")))
				{
					if (mt.getVisible().equals("Y"))
					{
						testList.add(mt);
					}
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return testList;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getInspectionID()
	{
		String result = "";
		if (dbInspectionID != null)
			result = dbInspectionID;
		return result;
	}

	public String getActivityID()
	{
		String result = "";
		if (dbActivityID != null)
			result = dbActivityID;
		return result;
	}

	public String getTestID()
	{
		String result = "";
		if (dbTestID != null)
			result = dbTestID;
		return result;
	}

	public Long getSequence()
	{
		Long result = (long) -1;
		if (dbSequence != null)
			result = dbSequence;
		return result;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean isValid(String inspectionid, String activityid, String testid)
	{
		setInspectionID(inspectionid);
		setActivityID(activityid);
		setTestID(testid);
		return isValidTest();
	}

	public boolean isValidTest()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTest.isValid"));
			stmt.setString(1, getInspectionID());
			stmt.setString(2, getActivityID());
			stmt.setString(3, getTestID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Test [" + getInspectionID().toString() + "/" + getActivityID().toString() + "/" + getTestID().toString() + "]");
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

	public void setInspectionID(String inspectionid)
	{
		dbInspectionID = inspectionid;
	}

	public void setActivityID(String activityid)
	{
		dbActivityID = activityid;
	}

	public void setTestID(String description)
	{
		dbTestID = description;
	}

	public void setSequence(Long extensionid)
	{
		dbSequence = extensionid;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";
		if (getActivityID().equals("") == false)
		{
			result = JUtility.padString(String.valueOf(getSequence()), false, 4, "0") + " - " + JUtility.padString(getTestID(), true, field_test_id, " ") + " - " + getDescription();
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
			if (isValidTest() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTest.update"));
				stmtupdate.setLong(1, getSequence());
				stmtupdate.setString(2, getInspectionID());
				stmtupdate.setString(3, getActivityID());
				stmtupdate.setString(4, getTestID());
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
