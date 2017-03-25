package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMDictionaryGroup.java
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


public class JDBQMDictionaryGroup
{
	private String dbTestID;
	private String dbGroup;
	private String dbAccess;

	private String dbErrorMessage;
	public static int field_activity_id = 20;
	private final Logger logger = Logger.getLogger(JDBQMDictionaryGroup.class);
	private String hostID;
	private String sessionID;

	public JDBQMDictionaryGroup(String host, String session) {
		setHostID(host);
		setSessionID(session);
	}

	public JDBQMDictionaryGroup(String host, String session, String testid, String group, String access) {
		setHostID(host);
		setSessionID(session);
		setTestID(testid);
		setGroup(group);
		setAccess(access);
	}

	public void clear() {
		setAccess("");
	}

	public ResultSet getQMDictionaryDataResultSet(String testid) {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");
		setTestID(testid);
		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionaryGroup.getDictionary"));
			stmt.setString(1, getTestID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public boolean create(String testid, String group, String access) {
		boolean result = false;
		setErrorMessage("");

		try {

			setTestID(testid);
			setGroup(group);
			setAccess(access);

			if (isValidDictionaryItemGroup() == false) {
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionaryGroup.create"));
				stmtupdate.setString(1, getTestID());
				stmtupdate.setString(2, getGroup());
				stmtupdate.setString(3, getAccess());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else {
				setErrorMessage("QMDictionary item/group already exists");
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
				if (isValidDictionaryItemGroup() == true) {
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionaryGroup.delete"));
					stmtupdate.setString(1, getTestID());
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

	public boolean getActivityProperties(String testid) {
		setTestID(testid);
		return getDictionaryItemProperties();
	}

	public boolean getDictionaryItemProperties() {
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getDictionaryItemProperties");

		clear();

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQDictionaryGroup.getDictionaryProperties"));
			stmt.setString(1,getTestID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next()) {
				setAccess(rs.getString("access"));
				result = true;
				rs.close();
				stmt.close();
			}
			else {
				setErrorMessage("Invalid QM Dictionary Test ID");
			}
		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public LinkedList<JDBQMDictionaryGroup> getActivities(String inspectionid) {
		LinkedList<JDBQMDictionaryGroup> typeList = new LinkedList<JDBQMDictionaryGroup>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setTestID(inspectionid);
		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionaryGroup.getDictionaryItem"));
			stmt.setString(1, getTestID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next()) {
				JDBQMDictionaryGroup mt = new JDBQMDictionaryGroup(getHostID(), getSessionID());
				mt.setTestID(rs.getString("test_id"));
				mt.setGroup(rs.getString("group_id"));
				mt.setAccess(rs.getString("access"));
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

	public String getTestID() {
		String result = "";
		if (dbTestID != null)
			result = dbTestID;
		return result;
	}

	
	public String getGroup() {
		String result = "";
		if (dbGroup != null)
			result = dbGroup;
		return result;
	}
	
	
	public String getAccess() {
		String result = "";
		if (dbAccess != null)
			result = dbAccess;
		return result;
	}	
		

	private String getSessionID() {
		return sessionID;
	}

	public boolean isValidDictionaryItemGroup(String testid) {
		setTestID(testid);
		return isValidDictionaryItemGroup();
	}

	public boolean isValidDictionaryItemGroup() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionaryGroup.isValidDictionaryGroup"));
			stmt.setString(1, getTestID());
			stmt.setString(2, getGroup());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next()) {
				result = true;
			}
			else {
				setErrorMessage("Invalid Activity [" + getTestID().toString() + "/" + getGroup().toString() + "]");
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

	public void setTestID(String testid) {
		dbTestID = testid;
	}
	
	public void setGroup(String group) {
		dbGroup = group;
	}

	public void setAccess(String access) {
		dbAccess = access;
	}

	
	private void setSessionID(String session) {
		sessionID = session;
	}

	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try {
			if (isValidDictionaryItemGroup() == true) {
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionaryGroup.update"));
				stmtupdate.setString(1, getAccess());
				stmtupdate.setString(2, getTestID());
				stmtupdate.setString(3, getGroup());
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
