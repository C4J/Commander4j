package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBTable.java
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
import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.Vector;

import com.commander4j.sys.Common;


public class JDBTable
{


	private int dbNoOfColumns = 0;

	private String dbTableName = "";
	private LinkedList<JDBField> fieldList = new LinkedList<JDBField>();

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

	public JDBTable(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}


	public JDBTable(String host, String session, String tableName)
	{
		setHostID(host);
		setSessionID(session);
		setTableName(tableName);
		enumFields();
	}

	public void enumFields() {

		ResultSet rs;
		PreparedStatement stmt;

		ResultSetMetaData rsmd;

		String column;
		String type;
		int size;

		setNumberOfColumns(0);
		fieldList.clear();

		try
		{
			if (dbTableName.toUpperCase().endsWith("SYS_USERS"))
			{
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement("select user_id,user_comment from " + getTableName() + " where 1 = 2");
			}
			else
			{
			   stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement("select * from " + getTableName() + " where 1 = 2");
			}
			rs = stmt.executeQuery();
			rsmd = rs.getMetaData();
			setNumberOfColumns(rsmd.getColumnCount());

			for (int l = 1; l <= getNumberOfColumns(); l++)
			{
				column = rsmd.getColumnName(l);
				type = rsmd.getColumnClassName(l);
				size = rsmd.getColumnDisplaySize(l);
				JDBField field = new JDBField(column, type, size);
				fieldList.add(field);
			}

			rs.close();
			stmt.close();

		}
		catch (Exception ex)
		{

		}

	}


	public boolean isValidTable() {
		boolean result = true;
		PreparedStatement stmt = null;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement("select * from " + getTableName() + " where 1 = 2");
			stmt.executeQuery();

		}
		catch (Exception ex)
		{
			result = false;
		}

		try
		{
			stmt.close();
		}
		catch (Exception ex)
		{

		}

		return result;
	}


	public void enumFields(String tableName) {

		setTableName(tableName);
		enumFields();
	}


	public String getColumnNameForField(int pos) {
		String result = "";

		result = fieldList.get(pos).getfieldName();

		return result;
	}


	public int getColumnSizeForField(int pos) {
		int result = 0;

		result = fieldList.get(pos).getfieldSize();

		return result;
	}


	public int getColumnSizeForField(String fieldName) {
		int result = 0;
		if (getColumnTypeForField(fieldName).equals("java.sql.Timestamp"))
		{
			result = 16;
		}
		else
		{
		for (int l = 1; l <= getNumberOfColumns(); l++)
		{
			if (fieldList.get(l - 1).getfieldName().toUpperCase().equals(fieldName.toUpperCase()) == true)
			{
				result = fieldList.get(l - 1).getfieldSize();
				
				break;
			}
		}
		}

		return result;
	}


	public String getColumnTypeForField(int pos) {
		String result = "";

		result = fieldList.get(pos).getfieldType();

		return result;
	}


	public String getColumnTypeForField(String fieldName) {
		String result = "";

		for (int l = 1; l <= getNumberOfColumns(); l++)
		{
			if (fieldList.get(l - 1).getfieldName().toUpperCase().equals(fieldName.toUpperCase()) == true)
			{
				result = fieldList.get(l - 1).getfieldType();
				break;
			}
		}

		return result;
	}


	public Vector<String> getFieldNames() {
		Vector<String> result = new Vector<String>();
		result.clear();

		for (int l = 1; l <= getNumberOfColumns(); l++)
		{
			result.add(fieldList.get(l - 1).getfieldName());
		}

		return result;
	}


	public LinkedList<JDBField> getFields() {
		return fieldList;
	}


	public int getNumberOfColumns() {
		return dbNoOfColumns;
	}


	public String getTableName() {
		return dbTableName;
	}


	public void setNumberOfColumns(int cols) {
		dbNoOfColumns = cols;
	}


	public void setTableName(String tableName) {
		dbTableName = tableName;
	}

}
