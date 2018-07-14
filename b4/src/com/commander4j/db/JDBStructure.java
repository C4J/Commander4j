package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBStructure.java
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

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JFileFilterTXT;
import com.commander4j.util.JUtility;

public class JDBStructure
{
	private String hostID;
	private String sessionID;
	private String db_error_message;
	private LinkedList<String> output = new LinkedList<String>();
	final Logger logger = Logger.getLogger(JDBStructure.class);
	ResultSetMetaData md;

	public JDBStructure(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	private String getHostID()
	{
		return hostID;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public LinkedList<String> getTableNames()
	{
		LinkedList<String> tableNames = new LinkedList<String>();
		Boolean includeTable = false;
		String driver = Common.hostList.getHost(getHostID()).getDatabaseParameters().getjdbcDriver();
		String actual_schema = "";
		String required_schema = Common.hostList.getHost(getHostID()).getDatabaseParameters().getjdbcDatabaseSchema();

		if (required_schema.length() == 0)
		{
			if (driver.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
			{
				required_schema = "dbo";
			}

			if (driver.equals("oracle.jdbc.driver.OracleDriver"))
			{
				required_schema = Common.hostList.getHost(getHostID()).getDatabaseParameters().getjdbcUsername();
			}

			if (driver.equals("com.mysql.cj.jdbc.Driver"))
			{
				required_schema = Common.hostList.getHost(getHostID()).getDatabaseParameters().getjdbcDatabase();
			}
		}

		required_schema = required_schema.replace(".", "");

		tableNames.clear();
		output.clear();

		try
		{
			DatabaseMetaData dbm = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getMetaData();
			String[] types =
			{ "TABLE" };
			ResultSet rs = dbm.getTables(null, null, "%", types);
			while (rs.next())
			{
				includeTable = false;

				try
				{
					actual_schema = rs.getString("TABLE_SCHEM");
					if (driver.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
					{

						if (actual_schema.equals(required_schema) == true)
						{
							includeTable = true;
						}
					}

					if (driver.equals("oracle.jdbc.driver.OracleDriver"))
					{
						if (actual_schema.toLowerCase().equals(required_schema.toLowerCase()) == true)
						{
							if (rs.getString("TABLE_NAME").toUpperCase().startsWith("APP_"))
							{
								includeTable = true;
							}
							if (rs.getString("TABLE_NAME").toUpperCase().startsWith("BAR_"))
							{
								includeTable = true;
							}
							if (rs.getString("TABLE_NAME").toUpperCase().startsWith("SYS_"))
							{
								includeTable = true;
							}
						}
					}
				}
				catch (Exception ex)
				{
					actual_schema = "";
				}

				try
				{
					actual_schema = rs.getString("TABLE_CAT");
					if (driver.equals("com.mysql.cj.jdbc.Driver"))
					{
						if (actual_schema.equals(required_schema) == true)
						{
							includeTable = true;
						}
					}
				}
				catch (Exception ex)
				{
					actual_schema = "";
				}

				if (includeTable)
				{
					if (rs.getString("TABLE_NAME").equals("sysdiagrams") == false)
					{
						tableNames.addLast(rs.getString("TABLE_NAME").toUpperCase());
					}
				}
			}
		}
		catch (SQLException s)
		{
			setErrorMessage(s.getMessage());
			logger.debug("No tables found in the database");

		}

		Collections.sort(tableNames);

		return tableNames;
	}

	public LinkedList<String> getPrimaryKey(String tablename)
	{
		LinkedList<String> result = new LinkedList<String>();
		try
		{
			DatabaseMetaData dmd = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getMetaData();
			ResultSet rs = dmd.getPrimaryKeys(Common.hostList.getHost(getHostID()).getDatabaseParameters().getjdbcDatabase(), null, tablename);

			while (rs.next())
			{

				logger.debug("COLUMN_NAME = " + rs.getString("COLUMN_NAME"));
				logger.debug("TABLE_CAT   = " + rs.getString("TABLE_CAT"));
				logger.debug("TABLE_SCHEM = " + rs.getString("TABLE_SCHEM"));
				logger.debug("TABLE_NAME  = " + rs.getString("TABLE_NAME"));
				logger.debug("KEY_SEQ     = " + rs.getString("KEY_SEQ"));
				logger.debug("PK_NAME     = " + rs.getString("PK_NAME"));

				if (result.contains(rs.getString("COLUMN_NAME")) == false)
				{
					result.add(rs.getString("COLUMN_NAME"));
					logger.debug("PRIMARY KEY FIELD FOUND = " + rs.getString("COLUMN_NAME"));
				}
			}

			rs.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public LinkedList<String> getUniqueIndexes(String tablename)
	{
		LinkedList<String> result = new LinkedList<String>();
		try
		{
			DatabaseMetaData dmd = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getMetaData();
			ResultSet rs = dmd.getIndexInfo(Common.hostList.getHost(getHostID()).getDatabaseParameters().getjdbcDatabase(), null, tablename, true, false);

			while (rs.next())
			{

				logger.debug("TABLE_CAT         = " + rs.getString("TABLE_CAT"));
				logger.debug("TABLE_SCHEM       = " + rs.getString("TABLE_SCHEM"));
				logger.debug("TABLE_NAME        = " + rs.getString("TABLE_NAME"));
				logger.debug("NON_UNIQUE        = " + rs.getBoolean("NON_UNIQUE"));
				logger.debug("INDEX_QUALIFIER   = " + rs.getString("INDEX_QUALIFIER"));
				logger.debug("INDEX_NAME	    = " + rs.getString("INDEX_NAME"));
				logger.debug("TYPE 				= " + rs.getShort("TYPE"));
				logger.debug("ORDINAL_POSITION 	= " + rs.getShort("ORDINAL_POSITION"));
				logger.debug("COLUMN_NAME       = " + rs.getString("COLUMN_NAME"));
				logger.debug("ASC_OR_DESC       = " + rs.getString("ASC_OR_DESC"));
				logger.debug("CARDINALITY       = " + rs.getLong("CARDINALITY"));
				logger.debug("PAGES             = " + rs.getLong("PAGES"));
				logger.debug("FILTER_CONDITION  = " + rs.getString("FILTER_CONDITION"));

				if (result.contains(rs.getString("COLUMN_NAME")) == false)
				{
					result.add(rs.getString("COLUMN_NAME"));
					logger.debug("PRIMARY KEY FIELD FOUND = " + rs.getString("COLUMN_NAME"));
				}
			}

			rs.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public LinkedList<JDBField> getFieldNames(String table_name)
	{
		LinkedList<JDBField> fieldNames = new LinkedList<JDBField>();

		String col_name = "";
		String col_type = "";
		int col_size = 0;
		String select_prefix = "";
		int col = 0;
		String required_schema = Common.hostList.getHost(getHostID()).getDatabaseParameters().getjdbcDatabaseSchema();
		if (required_schema.equals("") == false)
		{
			select_prefix = required_schema + ".";
		}

		try
		{
			Statement st = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
			ResultSet rs = st.executeQuery("select * from " + select_prefix + table_name + " where 1 = 2");
			md = rs.getMetaData();
			col = md.getColumnCount();

			for (int i = 1; i <= col; i++)
			{
				col_name = md.getColumnName(i);
				col_type = md.getColumnTypeName(i);
				col_size = md.getColumnDisplaySize(i);
				JDBField field = new JDBField(col_name, col_type, col_size);
				fieldNames.addLast(field);
			}

		}
		catch (Exception ex)
		{
			col = 0;
		}

		return fieldNames;
	}

	public void exportSchema()
	{

		LinkedList<String> tableNames = getTableNames();
		String table_name = "";
		String col_name = "";
		String col_type = "";
		String col_size = "";
		String col_precision = "";
		String col_scale = "";
		String select_prefix = "";
		int col = 0;
		String required_schema = Common.hostList.getHost(getHostID()).getDatabaseParameters().getjdbcDatabaseSchema();
		select_prefix = required_schema + ".";

		Collections.sort(tableNames);
		int tableCount = tableNames.size();

		if (tableCount > 0)
		{
			try
			{

				for (int x = 0; x < tableCount; x++)
				{
					table_name = tableNames.get(x);

					try
					{
						Statement st = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
						ResultSet rs = st.executeQuery("select * from " + select_prefix + table_name + " where 1 = 2");
						md = rs.getMetaData();
						col = md.getColumnCount();
						output.addLast("");
						output.addLast(table_name);
						output.addLast(JUtility.padString(table_name.length(), "="));
						output.addLast("");
						output.addLast("Column Name                      Type               Size  Precision     Scale");
						output.addLast("-------------------------------  ------------ ----------  ---------  --------");
					}
					catch (Exception ex)
					{
						col = 0;
					}

					for (int i = 1; i <= col; i++)
					{
						table_name = JUtility.padString(table_name, true, 32, " ");
						col_name = JUtility.padString(md.getColumnName(i), true, 32, " ");
						col_type = JUtility.padString(md.getColumnTypeName(i), true, 20, " ");
						col_size = JUtility.padString(String.valueOf(md.getColumnDisplaySize(i)), true, 10, " ");
						if ((col_type.contains("char") == true) || (col_type.contains("date") == true))
						{
							col_precision = "";
							col_scale = "";
						}
						else
						{
							col_precision = JUtility.padString(String.valueOf(md.getPrecision(i)), true, 10, " ");
							col_scale = JUtility.padString(String.valueOf(md.getScale(i)), true, 10, " ");
							if ((col_scale.contains("-") == true))
							{
								col_scale = "";
							}
							if ((col_precision.trim().equals("0") == true))
							{
								col_precision = "";
							}
						}
						output.addLast(col_name + " " + col_type + " " + String.valueOf(col_size) + " " + String.valueOf(col_precision) + " " + String.valueOf(col_scale));
					}
				}
			}
			catch (SQLException s)
			{
				setErrorMessage(s.getMessage());
				System.out.println("SQL statement is not executed!");
			}
		}

	}

	public String getErrorMessage()
	{
		return db_error_message;
	}

	private void setErrorMessage(String ErrorMsg)
	{
		db_error_message = ErrorMsg;
	}

	public void saveAs(String defaultFilename, Component parent)
	{

		if (output.size() > 0)
		{
			JFileChooser saveTXT = new JFileChooser();

			try
			{
				File f = new File(new File(System.getProperty("user.home")).getCanonicalPath());
				saveTXT.setCurrentDirectory(f);
				saveTXT.addChoosableFileFilter(new JFileFilterTXT());
				saveTXT.setSelectedFile(new File(defaultFilename));
			}
			catch (Exception ex)
			{
			}

			int result = saveTXT.showSaveDialog(parent);
			if (result == 0)
			{
				File selectedFile;
				selectedFile = saveTXT.getSelectedFile();
				if (selectedFile != null)
				{
					String filename = selectedFile.getAbsolutePath();
					BufferedWriter fw = null;

					try
					{
						fw = new BufferedWriter(new FileWriter(filename));

						fw.write("Schema Report");
						fw.newLine();
						for (int x = 0; x < output.size(); x++)
						{
							fw.write(output.get(x));
							fw.newLine();
						}
						fw.newLine();
						fw.append("End of Report.");
						fw.newLine();
						fw.flush();
						fw.close();
					}
					catch (IOException e)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		else
		{
			JUtility.errorBeep();
			JOptionPane.showMessageDialog(Common.mainForm, "No tables in selected schema", "Export Error", JOptionPane.WARNING_MESSAGE);
		}
	}
}
