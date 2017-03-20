package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMActivity.java
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

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 */
public class JDBQMActivity
{
	private String dbActivityID;
	private String dbInspectionID;
	private String dbDescription;
	private String dbErrorMessage;
	private Long dbExtensionID;
	public static int field_activity_id = 10;
	public static int field_description = 50;
	private final Logger logger = Logger.getLogger(JDBQMActivity.class);
	private String hostID;
	private String sessionID;
	private JDBQMExtension extension;
	
	/*
	 * 
	 * 
	 * Table APP_QM_ACTIVITY
		=====================
	INSPECTION_ID, ACTIVITY_ID, LOCATION_ID, MATERIAL, REQUIRED_RESOURCE, DESCRIPTION, EXTENSION_ID
	---------------------
	INSPECTION_ID    varchar(20) PK
	ACTIVITY_ID      varchar(10) PK
	DESCRIPTION      varchar(50)
	EXTENSION_ID     int(11)
	 * 
	 */

	public JDBQMActivity(String host, String session) {
		setHostID(host);
		setSessionID(session);
		extension = new JDBQMExtension(host,session);
	}

	public JDBQMActivity(String host, String session, String inspectionid, String activityid,String description) {
		setHostID(host);
		setSessionID(session);
		setInspectionID(inspectionid);
		setActivityID(activityid);
		setDescription(description);
	}

	public void clear() {
		setDescription("");
		setExtensionID((long) -1);
	}

	public ResultSet getQMActivityDataResultSet(String inspectionid) {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");
		setInspectionID(inspectionid);
		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMActivity.getActivities"));
			stmt.setString(1, getInspectionID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();
		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public boolean create(String inspectionid, String activityid, String description) {
		boolean result = false;
		setErrorMessage("");

		try {
			setInspectionID(inspectionid);
			setActivityID(activityid);
			setDescription(description);

			if (isValid() == false) {
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMActivity.create"));
				stmtupdate.setString(1, getInspectionID());
				stmtupdate.setString(2, getActivityID());
				stmtupdate.setString(3, getDescription());
				setExtensionID(extension.generateExtensionID());
				stmtupdate.setLong(4, getExtensionID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else {
				setErrorMessage("QMActivity already exists");
			}
		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try {
				if (isValid() == true) {
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMActivity.delete"));
					stmtupdate.setString(1, getInspectionID());
					stmtupdate.setString(2, getActivityID());
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					result = true;
				}
		}
		catch (Exception e) {
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getProperties(String inspectionid,String activityid) {
		setInspectionID(inspectionid);
		setActivityID(activityid);
		return getProperties();
	}

	public boolean getProperties() {
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("JDBQMActivity getProperties Inspection ["+getInspectionID()+"] Activity ["+getActivityID()+"]");

		clear();

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMActivity.getProperties"));
			stmt.setString(1,getInspectionID());
			stmt.setString(2,getActivityID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next()) {
				setDescription(rs.getString("description"));
				setExtensionID(rs.getLong("extension_id"));
				result = true;
				rs.close();
				stmt.close();
			}
			else {
				setErrorMessage("Invalid Activity [" + getInspectionID().toString() + "/" + getActivityID().toString() + "]");
			}
		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public LinkedList<JDBQMActivity> getActivities(String inspectionid) {
		LinkedList<JDBQMActivity> typeList = new LinkedList<JDBQMActivity>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setInspectionID(inspectionid);
		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMActivity.getActivities"));
			stmt.setString(1, getInspectionID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next()) {
				JDBQMActivity mt = new JDBQMActivity(getHostID(), getSessionID());
				mt.setInspectionID(rs.getString("inspection_id"));
				mt.setActivityID(rs.getString("activity_id"));
				mt.setDescription(rs.getString("description"));
				mt.setExtensionID(rs.getLong("extension_id"));
				typeList.add(mt);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return typeList;
	}

	public String getErrorMessage() {
		return dbErrorMessage;
	}

	private String getHostID() {
		return hostID;
	}

	public String getInspectionID() {
		String result = "";
		if (dbInspectionID != null)
			result = dbInspectionID;
		return result;
	}

	public String getActivityID() {
		String result = "";
		if (dbActivityID != null)
			result = dbActivityID;
		return result;
	}
	

	public String getDescription() {
		String result = "";
		if (dbDescription != null)
			result = dbDescription;
		return result;
	}	
	
	public Long getExtensionID() {
		Long result = (long) -1;
		if (dbExtensionID != null)
			result = dbExtensionID;
		return result;
	}		

	private String getSessionID() {
		return sessionID;
	}

	public boolean isValid(String inspectionid,String activityid) {
		setInspectionID(inspectionid);
		setActivityID(activityid);
		return isValid();
	}

	public boolean isValid() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMActivity.isValid"));
			stmt.setString(1, getInspectionID());
			stmt.setString(2, getActivityID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next()) {
				result = true;
			}
			else {
				setErrorMessage("Invalid Activity [" + getInspectionID().toString() + "/" + getActivityID().toString() + "]");
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return result;

	}


	private void setErrorMessage(String errorMsg) {
		if (errorMsg.isEmpty() == false) {
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	public void setInspectionID(String inspectionid) {
		dbInspectionID = inspectionid;
	}

	public void setActivityID(String activityid) {
		dbActivityID = activityid;
	}
	

	public void setDescription(String description) {
		dbDescription = description;
	}

	public void setExtensionID(Long extensionid)
	{
		dbExtensionID = extensionid;
	}
	
	private void setSessionID(String session) {
		sessionID = session;
	}

	public String toString() {
		String result = "";
		if (getActivityID().equals("") == false) {
			result = JUtility.padString(getActivityID(), true, field_activity_id, " ") + " - " + getDescription();
		}
		else {
			result = "";
		}

		return result;
	}

	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try {
			if (isValid() == true) {
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMActivity.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getInspectionID());
				stmtupdate.setString(3, getActivityID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return result;
	}
}
