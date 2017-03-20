package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMExtension.java
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

public class JDBQMExtension
{
	private Long dbExtensionID;
	private String dbLinkedTable;
	private String dbFieldName;
	private String dbValue;
	
	private String dbErrorMessage;
	private final Logger logger = Logger.getLogger(JDBQMExtension.class);
	private String hostID;
	private String sessionID;
	
	/*
	 * 
		Table APP_QM_EXTENSION
		======================
		EXTENSION_ID, LINKED_TABLE, FIELDNAME, VALUE
		----------------------
		EXTENSION_ID     int(11) PK
		LINKED_TABLE     varchar(50)
		FIELDNAME        varchar(50)
		VALUE            varchar(50)
	 * 
	 */

	public JDBQMExtension(String host, String session) {
		setHostID(host);
		setSessionID(session);
	}

	public JDBQMExtension(String host, String session, Long extensionid, String tablename, String fieldname,String value) {
		setHostID(host);
		setSessionID(session);
		setExtensionID(extensionid);
		setTableName(tablename);
		setFieldName(fieldname);
		setValue(value);
	}

	public void clear() {
		setValue("");
	}

	public ResultSet getQMExtensionResultSet(Long extensionid,String Table,String field) {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");
		setExtensionID(extensionid);
		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMExtension.getProperties"));
			stmt.setLong(1, getExtensionID());
			stmt.setString(2, Table);
			stmt.setString(3, field);
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public Long generateExtensionID()
	{
		Long result = (long) 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String extensionID = "1";
		Long SeqNumber = (long) 0;

		if (ctrl.lockRecord("QM EXTENSION ID") == true)
		{
			if (ctrl.getProperties("QM EXTENSION ID") == true)
			{
				extensionID = ctrl.getKeyValue();
				SeqNumber = Long.parseLong(extensionID);

				SeqNumber++;
				extensionID = String.valueOf(SeqNumber);
				ctrl.setKeyValue(extensionID);
				ctrl.update();
				result = SeqNumber;
			}
			else
			{
				result = (long) 1;
				ctrl.create("QM EXTENSION ID","1","Unique Extension Record ID");
			}
		}
		else
		{
			result = (long) -1;
			setErrorMessage(ctrl.getErrorMessage());
		}

		return result;
	}
	
	public Boolean create(Long extensionID,String tablename, String fieldname,String value) {
		Boolean result = false;
		setErrorMessage("");

		try {

			setExtensionID(extensionID);
			setTableName(tablename);
			setFieldName(fieldname);
			setValue(value);

			if (isValid() == false) {
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMExtension.create"));
				stmtupdate.setLong(1, getExtensionID());
				stmtupdate.setString(2, getLinkedTable());
				stmtupdate.setString(3, getFieldName());
				stmtupdate.setString(4,getValue());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else {
				setErrorMessage("QMExtension item already exists");
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
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMExtension.delete"));
					stmtupdate.setLong(1, getExtensionID());
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

	public boolean getProperties(Long extensionid,String table,String field) {
		setExtensionID(extensionid);
		setTableName(table);
		setFieldName(field);
		return getProperties();
	}

	public boolean getProperties() {
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("JDBQMExtension getProperties Extension ["+getExtensionID().toString()+"] Table ["+getLinkedTable()+"] Field ["+getFieldName()+"]");

		clear();

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMExtension.getProperties"));
			stmt.setLong(1,getExtensionID());
			stmt.setString(2, getLinkedTable());
			stmt.setString(3, getFieldName());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next()) {
				setValue(rs.getString("value"));
				result = true;
				rs.close();
				stmt.close();
			}
			else {
				setErrorMessage("Invalid Extension [" + getExtensionID().toString() + "]");
			}
		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public LinkedList<JDBQMExtension> getExtensions(Long extensionid) {
		LinkedList<JDBQMExtension> extensionList = new LinkedList<JDBQMExtension>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setExtensionID(extensionid);
		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMExtension.getExtension"));
			stmt.setLong(1, getExtensionID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next()) {
				JDBQMExtension mt = new JDBQMExtension(getHostID(), getSessionID());
				mt.setExtensionID(rs.getLong("extension_id"));
				mt.setTableName(rs.getString("linked_table"));
				mt.setFieldName(rs.getString("fieldname"));
				mt.setValue(rs.getString("value"));
				extensionList.add(mt);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return extensionList;
	}

	public String getErrorMessage() {
		return dbErrorMessage;
	}

	private String getHostID() {
		return hostID;
	}

	public Long getExtensionID() {
		Long result = (long) -1;
		if (dbExtensionID != null)
			result = dbExtensionID;
		return result;
	}

	
	public String getLinkedTable() {
		String result = "";
		if (dbLinkedTable != null)
			result = dbLinkedTable;
		return result;
	}
	
	
	public String getFieldName() {
		String result = "";
		if (dbFieldName != null)
			result = dbFieldName;
		return result;
	}	
	
	public String getValue() {
		String result = "";
		if (dbValue != null)
			result = dbValue;
		return result;
	}	

	private String getSessionID() {
		return sessionID;
	}

	public boolean isValid(Long extensionid,String table,String field) {
		setExtensionID(extensionid);
		setTableName(table);
		setFieldName(field);
		return isValid();
	}

	public boolean isValid() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMExtension.isValid"));
			stmt.setLong(1, getExtensionID());
			stmt.setString(2,getLinkedTable());
			stmt.setString(3,getFieldName());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next()) {
				result = true;
			}
			else {
				logger.debug("JDBQMExtension isValid Extension ["+getExtensionID().toString()+"] Table ["+getLinkedTable()+"] Field ["+getFieldName()+"]");
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

	public void setExtensionID(Long extensionid) {
		dbExtensionID = extensionid;
	}
	
	public void setTableName(String linkedtable) {
		dbLinkedTable = linkedtable;
	}

	public void setFieldName(String filename) {
		dbFieldName = filename;
	}

	public void setValue(String value) {
		dbValue = value;
	}
	
	private void setSessionID(String session) {
		sessionID = session;
	}

	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try {
			if (isValid() == true) {
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMExtension.update"));
				stmtupdate.setString(1, getValue());
				stmtupdate.setLong(2, getExtensionID());
				stmtupdate.setString(3, getLinkedTable());
				stmtupdate.setString(4, getFieldName());
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
