package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBShiftNames.java
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
import java.util.Vector;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBShiftNames {

	private String db_shift_id;
	private String db_description;
	private String db_error_message;
	private String db_enabled;
	public static int field_shift_id = 10;
	public static int field_enabled = 10;
	public static int field_description = 45;
	private String hostID;
	private String sessionID;
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBShiftNames.class);

	public JDBShiftNames(String host, String session) {
		setHostID(host);
		setSessionID(session);
	}

	public void clear() {
		setDescription("");
		setEnabled("N");
		setErrorMessage("");
	}

	public boolean create(String si,String desciption,String enabled) {
		boolean result = false;
		setErrorMessage("");

		try {
			setShiftID(si);
			setDescription(desciption);
			setEnabled(enabled);

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShiftNames.create"));
			stmtupdate.setString(1, getShiftID());
			stmtupdate.setString(2, getDescription());
			stmtupdate.setString(3, getEnabled());

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

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShiftNames.delete"));
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
	
	public boolean renameTo(String newShiftID)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValid(getShiftID()) == true)
			{
				JDBMHNDecisions mattype = new JDBMHNDecisions(getHostID(), getSessionID());
				mattype.setDecision(newShiftID);
				if (mattype.isValidDecision() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShiftNames.renameTo1"));
					stmtupdate.setString(1, newShiftID);
					stmtupdate.setString(2, getShiftID());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShiftNames.renameTo2"));
					stmtupdate.setString(1, newShiftID);
					stmtupdate.setString(2, getShiftID());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setShiftID(newShiftID);
					result = true;
				} else
				{
					setErrorMessage("New Shift ID is already in use.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	public String getDescription() {
		db_description = JUtility.replaceNullStringwithBlank(db_description);
		return db_description;
	}

	public String getEnabled() {
		db_enabled = JUtility.replaceNullStringwithBlank(db_enabled);
		if (db_enabled.equals(""))
		{
			db_enabled="N";
		}
		return db_enabled;
	}

	public boolean isEnabled()
	{
		boolean result = false;
		
		if (getEnabled().equals("Y"))
		{
			result = true;
		}
		
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
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShiftNames.isValid"));
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
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShiftNames.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getShiftID());
			rs = stmt.executeQuery();

			if (rs.next()) {
				setShiftID(rs.getString("shift_id"));
				setDescription(rs.getString("description"));
				setEnabled(rs.getString("enabled"));
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

	public LinkedList<JDBShiftNames> getShiftNamess() {
		LinkedList<JDBShiftNames> uomList = new LinkedList<JDBShiftNames>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShiftNames.getShiftNames"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next()) {
				JDBShiftNames shift = new JDBShiftNames(getHostID(), getSessionID());
				shift.setShiftID(rs.getString("shift_id"));
				shift.setDescription(rs.getString("description"));
				shift.setEnabled(rs.getString("enabled"));
				uomList.add(shift);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return uomList;
	}
	
	public Vector<JDBShiftNames> getShiftNames(boolean enabled)
	{
		Vector<JDBShiftNames> typeList = new Vector<JDBShiftNames>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShiftNames.getShiftNames"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBShiftNames shift = new JDBShiftNames(getHostID(), getSessionID());
				shift.setShiftID(rs.getString("shift_id"));
				shift.setDescription(rs.getString("description"));
				shift.setEnabled(rs.getString("enabled"));
				
				if (shift.isEnabled() == enabled)
				{
					typeList.add(shift);
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return typeList;
	}

	public ResultSet getShiftsResultSet() {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShiftNames.getShiftNames"));
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
	
	public void setDescription(String Description) {
		db_description = Description;
	}

	public void setEnabled(String et) {
		db_enabled = JUtility.replaceNullStringwithBlank(et);
		if (db_enabled.equals(""))
		{
			db_enabled = "N";
		}
	}
	
	public void setEnabled(boolean et) {
		if (et)
		{
			setEnabled("Y");
		}
		else
		{
			setEnabled("N");
		}
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
	
	public void setShiftID(String si)
	{
		db_shift_id = si;
	}

	public String toString() {
		String result = "";

			result = JUtility.padString(getShiftID(), true, field_shift_id, " ") + " " + getDescription();


		return result;
	}

	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try {

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBShiftNames.update"));

			stmtupdate.setString(1, getDescription());
			stmtupdate.setString(2, getEnabled());
			stmtupdate.setString(3, getShiftID());

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
