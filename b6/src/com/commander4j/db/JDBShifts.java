package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBShifts.java
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

public class JDBShifts {

	private String db_shift_id;
	private String db_description;
	private String db_error_message;
	private String db_end_time;
	private String db_start_time;
	public static int field_shift_id = 10;
	public static int field_start_time = 10;
	public static int field_end_time = 10;
	public static int field_description = 45;
	public static int field_time = 8;
	private String hostID;
	private String sessionID;
	final Logger logger = Logger.getLogger(JDBShifts.class);

	public JDBShifts(String host, String session) {
		setHostID(host);
		setSessionID(session);
	}

	public void clear() {
		setDescription("");
		setEndTime("");
		setStartTime("");
		setErrorMessage("");
	}

	public boolean create(String si,String starttime, String endtime, String desciption) {
		boolean result = false;
		setErrorMessage("");

		try {
			setShiftID(si);
			setStartTime(starttime);
			setDescription(desciption);
			setEndTime(endtime);

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShifts.create"));
			stmtupdate.setString(1, getShiftID());
			stmtupdate.setString(2, getStartTime());
			stmtupdate.setString(3, getEndTime());
			stmtupdate.setString(4, getDescription());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String si)
	{
		setShiftID(si);
		return delete();
	}
	
	public boolean delete() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try {

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShifts.delete"));
			stmtupdate.setString(1, getShiftID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	public String getDescription() {
		String result = "";
		if (db_description != null)
			result = db_description;
		return result;
	}

	public String getEndTime() {
		return db_end_time;
	}

	public int getEndTimeHours()
	{
		// hh:mm:ss
		int result = 0;
		result = Integer.valueOf(getEndTime().substring(0, 2));
		return result;
	}
	
	public int getEndTimeMins()
	{
		// hh:mm:ss
		int result = 0;
		result = Integer.valueOf(getEndTime().substring(3, 5));
		return result;
	}

	public int getEndTimeSecs()
	{
		// hh:mm:ss
		int result = 0;
		result = Integer.valueOf(getEndTime().substring(6,8));
		return result;
	}
	
	public int getStartTimeHours()
	{
		// hh:mm:ss
		int result = 0;
		result = Integer.valueOf(getStartTime().substring(0, 2));
		return result;
	}
	
	public int getStartTimeMins()
	{
		// hh:mm:ss
		int result = 0;
		result = Integer.valueOf(getStartTime().substring(3, 5));
		return result;
	}

	public int getStartTimeSecs()
	{
		// hh:mm:ss
		int result = 0;
		result = Integer.valueOf(getStartTime().substring(6, 8));
		return result;
	}
	
	public String getErrorMessage() {
		return db_error_message;
	}


	private String getHostID() {
		return hostID;
	}

	public boolean getProperties(String si)
	{
		setShiftID(si);
		return getProperties();
	}
	
	public boolean isValid(String shft) {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setShiftID(shft);
		setErrorMessage("");

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShifts.isValid"));
			stmt.setFetchSize(1);
			stmt.setString(1, getShiftID());
			rs = stmt.executeQuery();

			if (rs.next()) {
				result = true;
			} else {
				setErrorMessage("Invalid Shift ID ["+getShiftID()+"]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public boolean getProperties() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShifts.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getShiftID());
			rs = stmt.executeQuery();

			if (rs.next()) {
				setShiftID(rs.getString("SHIFT_ID"));
				setStartTime(rs.getString("START_TIME"));
				setEndTime(rs.getString("END_TIME"));
				setDescription(rs.getString("DESCRIPTION"));
				result = true;
			} else {
				setErrorMessage("Invalid Shift ID ["+getShiftID()+"]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	private String getSessionID() {
		return sessionID;
	}

	public LinkedList<JDBShifts> getShifts() {
		LinkedList<JDBShifts> uomList = new LinkedList<JDBShifts>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShifts.getShifts"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next()) {
				JDBShifts shift = new JDBShifts(getHostID(), getSessionID());
				shift.setShiftID(rs.getString("SHIFT_ID"));
				shift.setStartTime(rs.getString("START_TIME"));
				shift.setEndTime(rs.getString("END_TIME"));
				shift.setDescription(rs.getString("DESCRIPTION"));
				uomList.add(shift);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return uomList;
	}

	public ResultSet getShiftsResultSet() {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShifts.getShifts"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public String getShiftID()
	{
		return JUtility.replaceNullStringwithBlank(db_shift_id);
	}
	
	public String getStartTime() {
		return db_start_time;
	}

	public void setDescription(String Description) {
		db_description = Description;
	}

	public void setEndTime(String et) {
		db_end_time = et;
	}

	private void setErrorMessage(String ErrorMsg) {
		if (ErrorMsg.isEmpty() == false) {
			logger.debug(ErrorMsg);
		}
		db_error_message = ErrorMsg;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	public void setStartTime(String st) {
		db_start_time = st;
	}
	
	public void setShiftID(String si)
	{
		db_shift_id = si;
	}

	public String toString() {
		String result = "";
		if (getStartTime().equals("") == false)
			result = JUtility.padString(getStartTime(), true, field_time, " ").substring(0,8) + " - " + JUtility.padString(getEndTime(), true, field_time, " ").substring(0,8)+" - "+getDescription();
		else
			result = "";

		return result;
	}

	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try {

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShifts.update"));

			stmtupdate.setString(1, getDescription());
			stmtupdate.setString(2, getStartTime());
			stmtupdate.setString(3, getEndTime());
			stmtupdate.setString(4, getShiftID());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
}
