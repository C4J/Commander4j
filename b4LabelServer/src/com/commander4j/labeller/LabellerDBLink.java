package com.commander4j.labeller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;

public class LabellerDBLink
{
	private String jdbcDriver = "";
	private String jdbcUsername = "";
	private String jdbcPassword = "";
	private String jdbcServer = "";
	private String jdbcPort = "";
	private String jdbcSID = "";
	private String jdbcDatabase = "";
	private HashMap<String, String> sqlSelect = new HashMap<String, String>();
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((LabellerDBLink.class));
	Connection jdbcConnection;

	public String queryExecute(String queryID, String param,String fieldname)
	{
		String result = "";

		PreparedStatement stmt;
		ResultSet rs;

		try
		{
			stmt = jdbcConnection.prepareStatement(sqlSelect.get(queryID));
			stmt.setFetchSize(1);
			stmt.setString(1, param);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = rs.getString(fieldname);
			} else
			{
				result = "row not found";
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			logger.error(e.getMessage());
		}

		return result;
	}

	public boolean connect()
	{
		boolean result = false;

		try
		{
			Class.forName(getJdbcDriver()).newInstance();
			try
			{
				logger.error(getjdbcConnectString());
				jdbcConnection = DriverManager.getConnection(getjdbcConnectString(), getJdbcUsername(), getJdbcPassword());
				jdbcConnection.setAutoCommit(false);
				jdbcConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

				logger.info("Connected successfully");
				result = true;
			} catch (Exception ex)
			{
				logger.error(ex.getMessage());
				result = false;
			}
		} catch (Exception ex)
		{
			logger.error(ex.getMessage());
			result = false;
		}

		return result;
	}

	public synchronized void disconnect()
	{
		try
		{
			jdbcConnection.close();
			logger.info("Disconnected successfully");
		} catch (Exception ex)
		{
			logger.info("SQLException: " + ex.getMessage());
		}
	}

	public String getjdbcConnectString()
	{

		String value = "";

		if (getJdbcDriver().equals("com.ibm.db2.jcc.DB2Driver"))
		{
			value = "jdbc:db2://jdbcServer:jdbcPort/jdbcDatabase";
		}

		if (getJdbcDriver().equals("com.mysql.jdbc.Driver"))
		{
			value = "jdbc:mysql://jdbcServer/jdbcDatabase";
		}

		if (getJdbcDriver().equals("oracle.jdbc.driver.OracleDriver"))
		{
			value = "jdbc:oracle:thin:@jdbcServer:jdbcPort:jdbcSID";
		}

		if (getJdbcDriver().equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
		{
			if (getJdbcSID().equals("") == true)
			{
				value = "jdbc:sqlserver://jdbcServer";
			} else
			{
				value = "jdbc:sqlserver://jdbcServer\\jdbcSID";
			}
			if (getJdbcPort().equals("") == false)
			{
				if (getJdbcPort().equals("1433") == false)
				{
					value = value + ":" + "jdbcPort";
				}
			}
		}

		value = value.replaceAll("jdbcServer", getJdbcServer());
		value = value.replaceAll("jdbcPort", getJdbcPort());
		value = value.replaceAll("jdbcSID", getJdbcSID());
		value = value.replaceAll("jdbcDatabase", getJdbcDatabase());

		return value;
	}

	public void addSQL(String id, String sql)
	{
		sqlSelect.put(id, sql);
	}

	public String getSQL(String id)
	{
		return sqlSelect.get(id);
	}

	public String getJdbcDriver()
	{
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver)
	{
		this.jdbcDriver = jdbcDriver;
	}

	public String getJdbcUsername()
	{
		return jdbcUsername;
	}

	public void setJdbcUsername(String jdbcUsername)
	{
		this.jdbcUsername = jdbcUsername;
	}

	public String getJdbcPassword()
	{
		return jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword)
	{
		this.jdbcPassword = jdbcPassword;
	}

	public String getJdbcServer()
	{
		return jdbcServer;
	}

	public void setJdbcServer(String jdbcServer)
	{
		this.jdbcServer = jdbcServer;
	}

	public String getJdbcPort()
	{
		return jdbcPort;
	}

	public void setJdbcPort(String jdbcPort)
	{
		this.jdbcPort = jdbcPort;
	}

	public String getJdbcSID()
	{
		return jdbcSID;
	}

	public void setJdbcSID(String jdbcSID)
	{
		this.jdbcSID = jdbcSID;
	}

	public String getJdbcDatabase()
	{
		return jdbcDatabase;
	}

	public void setJdbcDatabase(String jdbcDatabase)
	{
		this.jdbcDatabase = jdbcDatabase;
	}

}
