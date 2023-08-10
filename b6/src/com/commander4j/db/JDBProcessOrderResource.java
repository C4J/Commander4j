package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBProcessOrderResource.java
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

/**
 * The JDBProcessOrderResource class updates the table
 * APP_PROCESS_ORDER_RESOURCE. This table contains a single row for each unique
 * Process Order Resource as stored in the APP_PROCESS_ORDER table. This table
 * holds information which can be linked directly to the Resource name. This
 * table is automatically updated with resource names when a new Process is
 * imported via the interfaces.
 * <p>
 * <img alt="" src="./doc-files/APP_PROCESS_ORDER_RESOURCE.jpg" >
 * 
 * @see com.commander4j.db.JDBProcessOrder JDBProcessOrder
 *
 */
public class JDBProcessOrderResource
{
	public static int field_Resource_id = 50;
	public static int field_Description_id = 80;
	public static int field_Suffix_id = 15;
	public static int field_Enabled = 1;
	public static int field_Plant_id = 20;
	private String dbErrorMessage;
	private String dbResource;
	private String dbDescription;
	private String dbBatchSuffix;
	private String dbEnabled;
	private String dbPlant;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBProcessOrderResource.class);
	private String hostID;
	private String sessionID;

	public JDBProcessOrderResource(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setBatchSuffix("");
		setDescription("");
		setEnabled(false);
		setPlant("");
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
			setPlant("");

			if (isValidResource() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.create"));
				stmtupdate.setString(1, getResource());
				stmtupdate.setString(2, getEnabled());
				stmtupdate.setString(3, getDescription());
				stmtupdate.setString(4, getBatchSuffix());
				stmtupdate.setString(5, getPlant());
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

	public String getPlant()
	{
		return JUtility.replaceNullStringwithBlank(dbPlant).trim();
	}
	
	public String getBatchSuffix()
	{
		return JUtility.replaceNullStringwithBlank(dbBatchSuffix).trim();
	}

	public String getBatchSuffixForResource(String res)
	{
		String result = "";

		if (getResourceProperties(res))
		{
			if (isEnabled())
			{
				result = getBatchSuffix();
			}
		}

		return result;
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
			dbEnabled = "N";
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

	public ResultSet getProcessOrderResourceResultSet(PreparedStatement criteria)
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
			setPlant(rs.getString("plant_id"));
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
				setPlant(rs.getString("plant_id"));
				result = true;
			} else
			{
				setErrorMessage("Invalid Resource " + getResource());
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

	public LinkedList<JDBProcessOrderResource> getResources() {
		LinkedList<JDBProcessOrderResource> sampList = new LinkedList<JDBProcessOrderResource>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.getResources"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBProcessOrderResource samp = new JDBProcessOrderResource(getHostID(), getSessionID());

				samp.setResource(rs.getString("required_resource"));
				samp.setDescription(rs.getString("description"));
				samp.setBatchSuffix(rs.getString("batch_suffix"));
				samp.setEnabled(rs.getString("enabled"));
				samp.setPlant(rs.getString("plant_id"));
				sampList.add(samp);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return sampList;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public Boolean isEnabled()
	{
		Boolean result = false;
		if (getEnabled().equals("Y"))
		{
			result = true;
		} else
		{
			result = false;
		}
		return result;
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
	
	public void setPlant(String plant)
	{
		dbPlant = plant;
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

	public String toString()
	{
		return getResource();
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
				stmtupdate.setString(4, getPlant());
				stmtupdate.setString(5, getResource());
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
