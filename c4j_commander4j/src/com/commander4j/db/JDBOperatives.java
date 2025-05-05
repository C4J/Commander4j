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

public class JDBOperatives {

	private String db_id;
	private String db_surname;
	private String db_forename;
	private String db_error_message;
	private String db_enabled;
	public static int field_surname = 45;
	public static int field_forename = 45;
	public static int field_enabled = 1;
	public static int field_id = 10;
	private String hostID;
	private String sessionID;
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBOperatives.class);

	public JDBOperatives(String host, String session) {
		setHostID(host);
		setSessionID(session);
	}

	public void clear() {
		setSurname("");
		setForename("");
		setEnabled("Y");
		setErrorMessage("");
	}

	public boolean create(String id,String surname,String forename,String enabled) {
		boolean result = false;
		setErrorMessage("");

		try {
			setID(id);
			setSurname(surname);
			setForename(forename);
			setEnabled(enabled);

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBOperatives.create"));
			stmtupdate.setString(1, getID());
			stmtupdate.setString(2, getSurname());
			stmtupdate.setString(3, getForename());
			stmtupdate.setString(4, getEnabled());

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

	public boolean delete(String id)
	{
		setID(id);
		return delete();
	}
	
	public boolean delete() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try {

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBOperatives.delete"));
			stmtupdate.setString(1, getID());
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
	
	public boolean renameTo(String newID)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{

					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBOperatives.renameTo"));
					stmtupdate.setString(1, newID);
					stmtupdate.setString(2, getID());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	public String getForename() {
		db_forename = JUtility.replaceNullStringwithBlank(db_forename);
		return db_forename;
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

	public boolean getProperties(String id)
	{
		setID(id);
		return getProperties();
	}
	
	public boolean isValid(String id) {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setID(id);
		setErrorMessage("");

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBOperatives.isValid"));
			stmt.setFetchSize(1);
			stmt.setString(1, getSurname());
			rs = stmt.executeQuery();

			if (rs.next()) {
				result = true;
			} else {
				setErrorMessage("Invalid ID ["+getID()+"]");
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
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBOperatives.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getID());
			rs = stmt.executeQuery();

			if (rs.next()) {
				setID(rs.getString("id"));
				setSurname(rs.getString("surname"));
				setForename(rs.getString("forename"));
				setEnabled(rs.getString("enabled"));
				result = true;
			} else {
				setErrorMessage("Invalid ID ["+getID()+"]");
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

	public LinkedList<JDBOperatives> getOperatives() {
		LinkedList<JDBOperatives> opList = new LinkedList<JDBOperatives>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBOperatives.getOperatives"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next()) {
				JDBOperatives op = new JDBOperatives(getHostID(), getSessionID());
				op.setID(rs.getString("id"));
				op.setSurname(rs.getString("surname"));
				op.setForename(rs.getString("forename"));
				op.setEnabled(rs.getString("enabled"));
				opList.add(op);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return opList;
	}
	
	public Vector<JDBOperatives> getOperatives(boolean enabled)
	{
		Vector<JDBOperatives> opList = new Vector<JDBOperatives>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBOperatives.getOperatives"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBOperatives op = new JDBOperatives(getHostID(), getSessionID());
				op.setID(rs.getString("id"));
				op.setSurname(rs.getString("surname"));
				op.setForename(rs.getString("forename"));
				op.setEnabled(rs.getString("enabled"));
				
				if (op.isEnabled() == enabled)
				{
					opList.add(op);
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return opList;
	}

	public ResultSet getOperativeResultSet() {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBOperatives.getOperatives"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public String getSurname()
	{
		return JUtility.replaceNullStringwithBlank(db_surname);
	}
	
	public void setForename(String forename) {
		db_forename = forename;
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
	
	public void setSurname(String si)
	{
		db_surname = si;
	}
	
	public void setID(String id)
	{
		db_id = id;
	}

	public String getID()
	{
		return db_id;
	}
	
	public String toString() {
		String result = "";

			result = JUtility.padString(getID(), true, field_id, " ") + " - " +getSurname() + ", " + getForename();


		return result;
	}

	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try {

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBOperatives.update"));

			stmtupdate.setString(1, getSurname());
			stmtupdate.setString(2, getForename());
			stmtupdate.setString(3, getEnabled());
			stmtupdate.setString(4, getID());

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
