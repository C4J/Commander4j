package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JHost.java
 * 
 * Package Name : com.commander4j.sys
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

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import org.apache.commons.text.WordUtils;
import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBSQLStatement;
import com.commander4j.db.JDatabaseParameters;
import com.commander4j.util.JSplashScreenUtils;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;

public class JHost
{

	public static int instanceCount = 0;
	private Hashtable<String, Connection> connections = new Hashtable<String, Connection>();
	private Hashtable<String, Boolean> connected2 = new Hashtable<String, Boolean>();
	private JDatabaseParameters databaseParameters = new JDatabaseParameters();
	private JDBSQLStatement sqlstatements = new JDBSQLStatement();
	private JDBSQLStatement viewstatements = new JDBSQLStatement();
	private String siteNumber = "";
	private String siteDescription = "";
	private String siteURL = "http://";
	private String enabled = "Y";
	private String uniqueid = JUnique.getUniqueID();
	private int schemaVersion = -1;
	private int schemaVersionRequired = -1;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JHost.class);

	public int getConnectionCount()
	{
		return instanceCount;
	}

	private void addConnection(String sessionID, Connection conn)
	{
		connections.put(sessionID, conn);
	}

	public synchronized boolean connect(String sessionID, String host)
	{
		String sqlPath = "";
		String viewPath = "";
		String basePath = "";
		if (Common.applicationMode.equals("Servlet"))
		{
			sqlPath = Common.paths.get("sql." + getDatabaseParameters().getjdbcDriver() + ".xml").toString();
			viewPath = Common.paths.get("view." + getDatabaseParameters().getjdbcDriver() + ".xml").toString();
		}
		else
		{
			basePath = System.getProperty("user.dir");
			sqlPath = basePath + "/xml/sql/sql." + getDatabaseParameters().getjdbcDriver() + ".xml";
			viewPath = basePath + "/xml/view/view." + getDatabaseParameters().getjdbcDriver() + ".xml";
		}

		return connect(sessionID, host, sqlPath, viewPath);
	}

	public synchronized boolean connect(String sessionID, String host, String sqlPath, String viewPath)
	{
		Connection tempConn;
		boolean result = false;

		if (getSqlstatements().IsInitialised() == false)
		{

			getSqlstatements().setXMLFilename(sqlPath);
			getSqlstatements().setjdbcDriver(getDatabaseParameters().getjdbcDriver());
			getSqlstatements().loadSQLStatements(host);

			getViewstatements().setXMLFilename(viewPath);
			getViewstatements().setjdbcDriver(getDatabaseParameters().getjdbcDriver());
			getViewstatements().loadSQLStatements(host);

			getSqlstatements().setSubstitutions(getViewstatements().getSQLStatements());
			getSqlstatements().setInitialised();
		}

		if (connections.containsKey(sessionID) == false)
		{
			
			 try
			{
				if (getDatabaseParameters().getjdbcDriver().equals("com.mysql.cj.jdbc.Driver"))
				{
					DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
				}
				
				if (getDatabaseParameters().getjdbcDriver().equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
				{
					DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
				}
				
				if (getDatabaseParameters().getjdbcDriver().equals("oracle.jdbc.driver.OracleDriver"))
				{
					DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				}
				
				
				try
				{

					try
					{
						logger.debug(getDatabaseParameters().getjdbcConnectString());
						DriverManager.setLoginTimeout(5);
						tempConn = DriverManager.getConnection(getDatabaseParameters().getjdbcConnectString(), getDatabaseParameters().getjdbcUsername(), getDatabaseParameters().getjdbcPassword());
						tempConn.setAutoCommit(false);
						tempConn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
						++instanceCount;
						addConnection(sessionID, tempConn);
						result = true;
						setConnected(sessionID, true);
						logger.debug("Opened connection for session ID : " + sessionID);
					}
					catch (Exception ex)
					{
						String err = "";
						try
						{
							err = ex.getMessage().substring(0, ex.getMessage().indexOf("\n"));
						}
						catch (Exception ex2)
						{
							err = ex.getMessage();
						}

						logger.fatal(ex);
						if (Common.sd.getData(sessionID, "silentExceptions").equals("Yes") == false)
						{
							JUtility.errorBeep();
							JSplashScreenUtils.hide();
							JOptionPane.showMessageDialog(null, WordUtils.wrap(err, 100), "Database Connection Error (" + getSiteDescription() + ")", JOptionPane.ERROR_MESSAGE);
						}
						result = false;
					}
				}
				catch (Exception ex)
				{
					logger.fatal(ex);
					if (Common.sd.getData(sessionID, "silentExceptions").equals("Yes") == false)
					{
						JUtility.errorBeep();
						JSplashScreenUtils.hide();
						JOptionPane.showMessageDialog(null, "Invalid jdbc driver [" + ex.getMessage() + "]", "Login Error (" + getSiteDescription() + ")", JOptionPane.ERROR_MESSAGE);
					}
					result = false;
				}				
				
				
				
			}
			catch (SQLException e)
			{
				logger.fatal(e);
				result = false;
			}
		}

		return result;
	}

	public synchronized void disconnect(String sessionID)
	{
		try
		{
			// logger.debug("disconnect sessionID="+sessionID);
			if (connections.containsKey(sessionID))
			{
				if (connections.get(sessionID).isClosed() == false)
				{
					connections.get(sessionID).close();
					connections.remove(sessionID);
					logger.debug("Closed connection for to Host ID " + getSiteNumber() + " session ID : " + sessionID);
					--instanceCount;
				}
				else
				{
					logger.debug("sessionID [" + sessionID + "] is closed");
				}
			}
			else
			{
				// logger.debug("disconnect sessionID ["+sessionID+"] not found");
			}
		}
		catch (Exception ex)
		{
			System.out.println("SQLException: " + ex.getMessage());
		}
	}

	public synchronized void disconnectAll()
	{
		Enumeration<String> k = connections.keys();
		if (connections.size() > 0)
		{
			while (k.hasMoreElements())
			{
				String key = (String) k.nextElement();
				try
				{

					connections.get(key).close();
					logger.debug("Closed connection for session ID : " + key);
				}
				catch (Exception ex)
				{
					logger.error("Error closing connection for session ID : " + key);
				}
				connections.remove(key);
			}
		}
	}

	public static void deRegisterDrivers()
	{
		Enumeration<Driver> e = java.sql.DriverManager.getDrivers();
		while (e.hasMoreElements())
		{
			Object driverAsObject = e.nextElement();
			System.out.println("JDBC Driver=" + driverAsObject);
			try
			{
				DriverManager.deregisterDriver((java.sql.Driver) driverAsObject);
			}
			catch (SQLException e1)
			{

			}
		}
	}

	public Connection getConnection(String sessionID)
	{

		if (connections.containsKey(sessionID) == false)
		{
			logger.error("JHost error during getConnection - cannot find Session ID: " + sessionID);
		}
		return connections.get(sessionID);

	}

	/**
	 * Method getDatabaseParameters.
	 * 
	 * @return DatabaseParameters
	 */
	public JDatabaseParameters getDatabaseParameters()
	{
		return databaseParameters;
	}

	/**
	 * Method getEnabled.
	 * 
	 * @return String
	 */
	public String getEnabled()
	{
		return enabled;
	}

	public String getUniqueID()
	{
		return uniqueid;
	}

	/**
	 * Method getSchemaVersion.
	 * 
	 * @return int
	 */
	public int getSchemaVersion()
	{
		return schemaVersion;
	}

	/**
	 * Method getSchemaVersionRequired.
	 * 
	 * @return int
	 */
	public int getSchemaVersionRequired()
	{
		return schemaVersionRequired;
	}

	/**
	 * Method getSiteDescription.
	 * 
	 * @return String
	 */
	public String getSiteDescription()
	{
		return siteDescription;
	}

	/**
	 * Method getSiteNumber.
	 * 
	 * @return String
	 */
	public String getSiteNumber()
	{
		return siteNumber;
	}

	/**
	 * Method getSiteURL.
	 * 
	 * @return String
	 */
	public String getSiteURL()
	{
		return siteURL;
	}

	/**
	 * Method getSqlstatements.
	 * 
	 * @return JDBSQLStatement
	 */
	public JDBSQLStatement getSqlstatements()
	{
		return sqlstatements;
	}

	/**
	 * Method getViewstatements.
	 * 
	 * @return JDBSQLStatement
	 */
	public JDBSQLStatement getViewstatements()
	{
		return viewstatements;
	}

	public boolean isConnected(String sessionID)
	{
		boolean result = false;

		if (connections.containsKey(sessionID))
		{
			try
			{
				if (connections.get(sessionID).isClosed() == false)
				{
					result = true;
				}
			}
			catch (Exception ex)
			{
				System.out.println("SQLException: " + ex.getMessage());
			}

		}

		return result;
	}

	private void setConnected(String sessionID, boolean isConnected)
	{
		connected2.put(sessionID, isConnected);

	}

	/**
	 * Method setDatabaseParameters.
	 * 
	 * @param value
	 *            DatabaseParameters
	 */
	public void setDatabaseParameters(JDatabaseParameters value)
	{
		databaseParameters = value;
	}

	/**
	 * Method setEnabled.
	 * 
	 * @param value
	 *            String
	 */
	public void setEnabled(String value)
	{
		enabled = value;
	}

	public void setUniqueID(String value)
	{
		uniqueid = value;
	}

	/**
	 * Method setSchemaVersion.
	 * 
	 * @param value
	 *            int
	 */
	public void setSchemaVersion(int value)
	{
		schemaVersion = value;
	}

	/**
	 * Method setSchemaVersionRequired.
	 * 
	 * @param value
	 *            int
	 */
	public void setSchemaVersionRequired(int value)
	{
		schemaVersionRequired = value;
	}

	/**
	 * Method setSiteDescription.
	 * 
	 * @param value
	 *            String
	 */
	public void setSiteDescription(String value)
	{
		siteDescription = value;
	}

	/**
	 * Method setSiteNumber.
	 * 
	 * @param value
	 *            String
	 */
	public void setSiteNumber(String value)
	{
		siteNumber = value;
	}

	/**
	 * Method setSiteURL.
	 * 
	 * @param value
	 *            String
	 */
	public void setSiteURL(String value)
	{
		siteURL = value;
	}

	/**
	 * Method setSqlstatements.
	 * 
	 * @param value
	 *            JDBSQLStatement
	 */
	public void setSqlstatements(JDBSQLStatement value)
	{
		sqlstatements = value;
	}

	/**
	 * Method setViewstatements.
	 * 
	 * @param value
	 *            JDBSQLStatement
	 */
	public void setViewstatements(JDBSQLStatement value)
	{
		viewstatements = value;
	}

	public String toString()
	{
		return this.siteDescription;
	}

}
