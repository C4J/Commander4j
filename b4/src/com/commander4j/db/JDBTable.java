package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.Vector;

import com.commander4j.sys.Common;

/**
 */
public class JDBTable
{

	/**
	 * @uml.property name="dbNoOfColumns"
	 */
	private int dbNoOfColumns = 0;
	/**
	 * @uml.property name="dbTableName"
	 */
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

	/**
	 * Constructor for JDBTable.
	 * 
	 * @param tableName
	 *            String
	 */
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

	/**
	 * Method isValidTable.
	 * 
	 * @return boolean
	 */
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

	/**
	 * Method enumFields.
	 * 
	 * @param tableName
	 *            String
	 */
	public void enumFields(String tableName) {

		setTableName(tableName);
		enumFields();
	}

	/**
	 * Method getColumnNameForField.
	 * 
	 * @param pos
	 *            int
	 * @return String
	 */
	public String getColumnNameForField(int pos) {
		String result = "";

		result = fieldList.get(pos).getfieldName();

		return result;
	}

	/**
	 * Method getColumnSizeForField.
	 * 
	 * @param pos
	 *            int
	 * @return int
	 */
	public int getColumnSizeForField(int pos) {
		int result = 0;

		result = fieldList.get(pos).getfieldSize();

		return result;
	}

	/**
	 * Method getColumnSizeForField.
	 * 
	 * @param fieldName
	 *            String
	 * @return int
	 */
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

	/**
	 * Method getColumnTypeForField.
	 * 
	 * @param pos
	 *            int
	 * @return String
	 */
	public String getColumnTypeForField(int pos) {
		String result = "";

		result = fieldList.get(pos).getfieldType();

		return result;
	}

	/**
	 * Method getColumnTypeForField.
	 * 
	 * @param fieldName
	 *            String
	 * @return String
	 */
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

	/**
	 * Method getFieldNames.
	 * 
	 * @return Vector<String>
	 */
	public Vector<String> getFieldNames() {
		Vector<String> result = new Vector<String>();
		result.clear();

		for (int l = 1; l <= getNumberOfColumns(); l++)
		{
			result.add(fieldList.get(l - 1).getfieldName());
		}

		return result;
	}

	/**
	 * Method getFields.
	 * 
	 * @return LinkedList<JDBField>
	 */
	public LinkedList<JDBField> getFields() {
		return fieldList;
	}

	/**
	 * Method getNumberOfColumns.
	 * 
	 * @return int
	 */
	public int getNumberOfColumns() {
		return dbNoOfColumns;
	}

	/**
	 * Method getTableName.
	 * 
	 * @return String
	 */
	public String getTableName() {
		return dbTableName;
	}

	/**
	 * Method setNumberOfColumns.
	 * 
	 * @param cols
	 *            int
	 */
	public void setNumberOfColumns(int cols) {
		dbNoOfColumns = cols;
	}

	/**
	 * Method setTableName.
	 * 
	 * @param tableName
	 *            String
	 */
	public void setTableName(String tableName) {
		dbTableName = tableName;
	}

}
