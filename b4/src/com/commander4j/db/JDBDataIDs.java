package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBDataIDs.java
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
import com.commander4j.util.JUtility;

/**
 * JDBDataIDs class is used to insert/update/delete the APP_MATERIAL_DATA_IDS
 * table. The default APP_MATERIAL table holds the basic material master data.
 * However in order to extend the type of data which can be stored and also link
 * it so a customer there is another table called APP_MATERIAL_CUSTOMER_DATA.
 * This second table is keyed on MATERIAL, CUSTOMER_ID and DATA_ID. The DATA_ID
 * can be anything meaningful name to describe the new data item being stored.
 * <p>
 * <img alt="" src="./doc-files/APP_MATERIAL_DATA_IDS.jpg" > 
 */

public class JDBDataIDs
{

	private String dbErrorMessage;
	private String dbDataIDDescription;
	private String dbDataID;
	private final Logger logger = Logger.getLogger(JDBDataIDs.class);
	private String hostID;
	private String sessionID;
	public static int field_data_id = 20;
	public static int field_description = 80;

	public JDBDataIDs(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBDataIDs(String host, String session, String type, String description)
	{
		setHostID(host);
		setSessionID(session);
		setID(type);
		setDescription(description);
	}

	public void clear()
	{
		// setType("");
		setDescription("");
		setErrorMessage("");
	}

	public boolean create(String lid)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setID(lid);

			if (isValidDataID() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDataID.create"));
				stmtupdate.setString(1, getID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Data ID [" + lid + "] already exists");
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
			if (isValidDataID() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDataID.delete"));
				stmtupdate.setString(1, getID());
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

	public String getDescription()
	{
		String result = "";
		if (dbDataIDDescription != null)
			result = dbDataIDDescription;
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

	public boolean getProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("JDBMaterialCustomerData.getProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDataID.getProperties"));
			stmt.setString(1, getID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDescription(rs.getString("description"));
				result = true;
			} else
			{
				setErrorMessage("Invalid Data ID [" + getID() + "]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public boolean geProperties(String materialType)
	{
		setID(materialType);
		return getProperties();
	}

	public LinkedList<JDBDataIDs> getDataIDs()
	{
		LinkedList<JDBDataIDs> typeList = new LinkedList<JDBDataIDs>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDataID.getIDs"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBDataIDs mt = new JDBDataIDs(getHostID(), getSessionID());
				mt.setID(rs.getString("data_id"));
				mt.setDescription(rs.getString("description"));
				typeList.add(mt);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return typeList;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getID()
	{
		String result = "";
		if (dbDataID != null)
			result = dbDataID;
		return result;
	}

	public boolean isValidDataID()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDataID.isValidID"));
			stmt.setString(1, getID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Data ID [" + getID() + "]");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public boolean isValidDataID(String type)
	{
		setID(type);
		return isValidDataID();
	}

	public void setDescription(String description)
	{
		dbDataIDDescription = description;
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

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setID(String id)
	{
		dbDataID = id;
	}

	public String toString()
	{
		String result = "";
		if (getID().equals("") == false)
		{
			result = JUtility.padString(getID(), true, 20, " ") + " - " + getDescription();
		} else
		{
			result = "";
		}

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidDataID() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDataID.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getID());
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
