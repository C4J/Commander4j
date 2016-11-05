package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBProcessOrderResource
{
	public static int field_Resource_id = 50;
	public static int field_Description_id = 80;
	public static int field_Suffix_id = 15;
	public static int field_Enabled = 1;
	private String dbErrorMessage;
	private String dbResource;
	private String dbDescription;
	private String dbBatchSuffix;
	private String dbEnabled;
	private final Logger logger = Logger.getLogger(JDBProcessOrderResource.class);
	private String hostID;
	private String sessionID;

	public JDBProcessOrderResource(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setResource("");
		setBatchSuffix("");
		setDescription("");
		setEnabled(false);
	}

	public boolean create(String res)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setResource(res);
			setEnabled("Y");
			setDescription("");
			setBatchSuffix("");

			if (isValidResource() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.create"));
				stmtupdate.setString(1, getResource());
				stmtupdate.setString(2, getEnabled());
				stmtupdate.setString(3, getDescription());
				stmtupdate.setString(4, getBatchSuffix());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Resource " + getResource() + " already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidResource() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.delete"));
				stmtupdate.setString(1, getResource());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public String getBatchSuffix()
	{
		return JUtility.replaceNullStringwithBlank(dbBatchSuffix);
	}

	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(dbDescription);
	}

	public String getEnabled()
	{
		String result = JUtility.replaceNullStringwithBlank(dbEnabled);
		if (result.equals(""))
		{
			dbEnabled="N";
			result = "N";
		}
		return result;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public ResultSet getProcessOrderResourceDataResultSet(PreparedStatement criteria)
	{
		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();
		} catch (Exception e)
		{
			rs = null;
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setResource(rs.getString("required_resource"));
			setDescription(rs.getString("description"));
			setBatchSuffix(rs.getString("batch_suffix"));
			setEnabled(rs.getString("enabled")); 
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getResource()
	{
		return JUtility.replaceNullStringwithBlank(dbResource);
	}

	public boolean getResourceProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.getProperties"));
			stmt.setString(1, getResource());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setEnabled(rs.getString("enabled"));
				setBatchSuffix(rs.getString("batch_suffix"));
				setDescription(rs.getString("description"));
				result = true;
			} else
			{
				setErrorMessage("Invalid Resource "+getResource());
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getResourceProperties(String res)
	{
		setResource(res);
		return getResourceProperties();
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean isValidResource()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.isValidResource"));
			stmt.setFetchSize(1);
			stmt.setString(1, getResource());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Resource " + getResource() + " not found.");
			}
			stmt.close();
			rs.close(); 

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}
	
	public boolean isValidResource(String res)
	{
		setResource(res);

		return isValidResource();
	}

	public void setBatchSuffix(String suffix)
	{
		dbBatchSuffix = suffix;
	}

	public void setDescription(String desc)
	{
		dbDescription = desc;
	}

	public void setEnabled(boolean enabled)
	{
		if (enabled)
		{
			setEnabled("Y");
		} else
		{
			setEnabled("N");
		}
	}

	public void setEnabled(String enabled)
	{
		dbEnabled = enabled;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setResource(String res)
	{
		dbResource = res;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidResource() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.update"));
				stmtupdate.setString(1, getEnabled());
				stmtupdate.setString(2, getBatchSuffix());
				stmtupdate.setString(3, getDescription());
				stmtupdate.setString(4, getResource());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
