package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBSSCCRange.java
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

public class JDBSSCCRange
{

	private String db_workstation;

	private String db_error_message;

	private String db_prefix;

	private String db_min;

	private String db_max;

	private String db_next;

	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBSSCCRange.class);

	public static int field_prefix = 18;

	public static int field_min = 18;

	public static int field_max = 18;

	public static int field_next = 18;

	public static int field_workstation = 40;
	private String hostID;
	private String sessionID;

	private void setSessionID(String session) {
		sessionID = session;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private String getSessionID() {
		return sessionID;
	}

	private String getHostID() {
		return hostID;
	}

	public JDBSSCCRange(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}


	public JDBSSCCRange(String workstation, String prefix, String min, String max, String next)
	{
		setWorkstation(workstation);
		setPrefix(prefix);
		setMin(min);
		setMax(max);
		setNext(next);
	}


	public boolean create(String workstation, String prefix, String min, String max, String next) {
		boolean result = false;
		setErrorMessage("");

		try
		{

			setWorkstation(workstation);
			setPrefix(prefix);
			setMin(min);
			setMax(max);
			setNext(next);

			if (isValidWorkstation() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.create"));
				stmtupdate.setString(1, getWorkstation());
				stmtupdate.setString(2, getPrefix());
				stmtupdate.setString(3, getMin());
				stmtupdate.setString(4, getMax());
				stmtupdate.setString(5, getNext());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Workstation already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	public boolean delete() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWorkstation() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.delete"));
				stmtupdate.setString(1, getWorkstation());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	public String getWorkstation() {
		String result = "";
		if (db_workstation != null)
			result = db_workstation;
		return result;
	}


	public String getErrorMessage() {
		return db_error_message;
	}


	public boolean getWorkstationProperties() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.getSSCCRangeProperties"));
			stmt.setString(1, getWorkstation());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setPrefix(rs.getString("prefix"));
				setMin(rs.getString("min"));
				setMax(rs.getString("max"));
				setNext(rs.getString("next"));
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Workstation");
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


	public String getPrefix() {
		return db_prefix;
	}


	public String getMin() {
		String result = "";
		if (db_min != null)
			result = db_min;
		return result;
	}


	public String getMax() {
		String result = "";
		if (db_max != null)
			result = db_max;
		return result;
	}


	public String getNext() {
		String result = "";
		if (db_next != null)
			result = db_next;
		return result;
	}


	public LinkedList<JDBSSCCRange> getWorkstations() {
		LinkedList<JDBSSCCRange> SSCCRangeList = new LinkedList<JDBSSCCRange>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.getWorkstations"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBSSCCRange SSCCRange = new JDBSSCCRange(getHostID(), getSessionID());
				SSCCRange.setWorkstation(rs.getString("workstation"));
				SSCCRange.setPrefix(rs.getString("prefix"));
				SSCCRange.setMin(rs.getString("min"));
				SSCCRange.setMax(rs.getString("max"));
				SSCCRange.setNext(rs.getString("next"));
				SSCCRangeList.add(SSCCRange);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return SSCCRangeList;
	}


	public String toString() {
		String result = "";
		if (getMin().equals("") == false)
			result = JUtility.padString(getWorkstation(), true, field_workstation, " ");
		else
			result = "";

		return result;
	}


	public boolean isValidWorkstation() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.isValidWorkstation"));
			stmt.setString(1, getWorkstation());
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Workstation [" + getWorkstation() + "]");
			}
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}


	public boolean isValidWorkstation(String workstation) {
		setWorkstation(workstation);
		return isValidWorkstation();
	}


	public boolean renameTo(String newWorkstation) {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidWorkstation() == true)
			{
				JDBSSCCRange newworkstation = new JDBSSCCRange(getHostID(), getSessionID());
				newworkstation.setWorkstation(newWorkstation);
				if (newworkstation.isValidWorkstation() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.renameTo"));
					stmtupdate.setString(1, newWorkstation);
					stmtupdate.setString(2, getWorkstation());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setWorkstation(newWorkstation);
					result = true;
				}
				else
				{
					setErrorMessage("New Workstation is already in use.");
				}
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	public void setWorkstation(String workstation) {
		db_workstation = workstation;
	}


	private void setErrorMessage(String ErrorMsg) {
		if (ErrorMsg.isEmpty() == false)
		{
			logger.error(ErrorMsg);
		}
		db_error_message = ErrorMsg;
	}


	public void setPrefix(String prefix) {
		db_prefix = prefix;
	}


	public void setMin(String min) {
		db_min = min;
	}


	public void setMax(String max) {
		db_max = max;
	}


	public void setNext(String next) {
		db_next = next;
	}


	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWorkstation() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.update"));
				stmtupdate.setString(1, getPrefix());
				stmtupdate.setString(2, getMin());
				stmtupdate.setString(3, getMax());
				stmtupdate.setString(4, getNext());
				stmtupdate.setString(5, getWorkstation());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
