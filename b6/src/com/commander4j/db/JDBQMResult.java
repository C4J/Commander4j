package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMResult.java
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

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBQMResult
{
	private String 		dbUserID;
	private String 		dbValue;
	private String 		dbTestID;
	private Long 		dbSampleID;
	private Timestamp 	dbProcessed;
	private String 		dbStatus;
	private String 		dbErrorMessage;
	private Timestamp 	dbUpdatedID;
	public static int 	field_activity_id = 20;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBQMResult.class);
	private String 		hostID;
	private String 		sessionID;
	
	public JDBQMResult(String host, String session) {
		setHostID(host);
		setSessionID(session);
	}

	public JDBQMResult(String host, String session, Long sampleid, String testid, String value, String userid,String status,Timestamp processed,Timestamp updated) {
		setHostID(host);
		setSessionID(session);
		setSampleID(sampleid);
		setTestID(testid);
		setUserID(userid);
		setStatus(status);
		setValue(value);
		setProcessed(processed);
		setUpdated(updated);
	}

	public void clear() {
		setStatus("");
		setUserID("");
		setValue("");
		setUpdated(null);
		setProcessed(null);
	}

	public ResultSet getQMResultDataResultSet(Long sampleid,String testid) {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");
		setSampleID(sampleid);
		setTestID(testid);
		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMResult.getResult"));
			stmt.setLong(1, getSampleID());
			stmt.setString(2, getTestID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public boolean create(Long sampleid, String testid, String value,String status,String userid) {
		boolean result = false;
		setErrorMessage("");

		try {
			setSampleID(sampleid);
			setTestID(testid);
			setValue(value);
			setStatus(status);
			setUserID(userid);


			if (isValidResult() == false) {
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMResult.create"));
				stmtupdate.setLong(1, getSampleID());
				stmtupdate.setString(2, getTestID());
				stmtupdate.setString(3, getValue());
				stmtupdate.setTimestamp(4, JUtility.getSQLDateTime());
				stmtupdate.setString(5, Common.userList.getUser(getSessionID()).getUserId());
				stmtupdate.setString(6, getStatus());
				stmtupdate.setTimestamp(7, null);
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else {
				setErrorMessage("QMResult already exists");
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
				if (isValidResult() == true) {
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMResult.delete"));
					stmtupdate.setLong(1, getSampleID());
					stmtupdate.setString(2, getTestID());
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

	public boolean getResultsProperties(Long sampleid,String testid) {
		setSampleID(sampleid);
		setTestID(testid);
		return getResultProperties();
	}

	public boolean getResultProperties() {
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		//logger.debug("getResultsProperties ["+String.valueOf(getSampleID())+"] ["+getTestID()+"]");

		clear();

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMResult.getProperties"));
			stmt.setLong(1,getSampleID());
			stmt.setString(2,getTestID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next()) {
				setValue(rs.getString("value"));
				setUpdated(rs.getTimestamp("updated"));
				setUserID(rs.getString("user_id"));
				setStatus(rs.getString("status"));
				setProcessed(rs.getTimestamp("processed"));
				result = true;
				rs.close();
				stmt.close();
			}
			else {
				rs.close();
				stmt.close();
				//setErrorMessage("Result not found.");
			}
		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public LinkedList<JDBQMResult> getResults(Long sampleid) {
		LinkedList<JDBQMResult> typeList = new LinkedList<JDBQMResult>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setSampleID(sampleid);
		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMResult.getResults"));
			stmt.setLong(1, getSampleID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next()) {
				JDBQMResult mt = new JDBQMResult(getHostID(), getSessionID());
				mt.setSampleID(rs.getLong("sample_id"));
				mt.setTestID(rs.getString("test_id"));
				mt.setValue(rs.getString("value"));
				mt.setUpdated(rs.getTimestamp("updated"));
				mt.setUserID(rs.getString("user_id"));
				mt.setStatus(rs.getString("status"));
				mt.setProcessed(rs.getTimestamp("processed"));
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

	public Long getSampleID() {
		return dbSampleID;
	}

	public String getTestID() {
		String result = "";
		if (dbTestID != null)
			result = dbTestID;
		return result;
	}
	
	public String getValue() {
		String result = "";
		if (dbValue != null)
			result = dbValue;
		return result;
	}
	
	public String getStatus() {
		String result = "";
		if (dbStatus != null)
			result = dbStatus;
		return result;
	}

	public String getUserID() {
		String result = "";
		if (dbUserID != null)
			result = dbUserID;
		return result;
	}

	public Timestamp getProcessed() {
		return dbProcessed;
	}	
	
	public Timestamp getUpdated() {
		return dbUpdatedID;
	}		

	private String getSessionID() {
		return sessionID;
	}

	public boolean isValidResult(Long sampleid,String testid) {
		setSampleID(sampleid);
		setTestID(testid);
		return isValidResult();
	}

	public boolean isValidResult() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMResult.isValid"));
			stmt.setLong(1, getSampleID());
			stmt.setString(2, getTestID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next()) {
				result = true;
			}
			else {
				setErrorMessage("Invalid Result [" + getSampleID().toString() + "/" + getTestID().toString() + "]");
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

	public void setSampleID(Long sampleid) {
		dbSampleID = sampleid;
	}

	public void setTestID(String testid) {
		dbTestID = testid;
	}
	
	public void setUserID(String userid) {
		dbUserID = userid;
	}

	public void setValue(String value) {
		dbValue = value;
	}

	public void setStatus(String status) {
		dbStatus = status;
	}
	
	public void setUpdated(Timestamp updated)
	{
		dbUpdatedID = updated;
	}
	
	public void setProcessed(Timestamp processed)
	{
		dbProcessed = processed;
	}
	
	private void setSessionID(String session) {
		sessionID = session;
	}


	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try {
			if (isValidResult() == true) {
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMResult.update"));

				stmtupdate.setString(1, getValue());
				stmtupdate.setTimestamp(2, getUpdated());
				stmtupdate.setString(3, Common.userList.getUser(getSessionID()).getUserId());
				stmtupdate.setString(4, getStatus());
				stmtupdate.setTimestamp(5, getProcessed());
				stmtupdate.setLong(6, getSampleID());
				stmtupdate.setString(7, getTestID());
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
