package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBSuppliers.java
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
import java.util.Vector;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBSuppliers class is used to insert/update/delete records in the table
 * APP_SUPPLIERS. This table contains a list of valid suppliers which can be
 * assigned to the Pallet Sample.
 */

public class JDBSuppliers
{
	private String dbErrorMessage;
	private String dbDescription;
	private String dbSupplierID;
	private String dbType;
	private String dbEnabled;
	public static int field_supplier_id = 15;
	public static int field_supplier_type = 12;
	public static int field_description = 80;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBSuppliers.class);
	private String hostID;
	private String sessionID;

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	private String getHostID()
	{
		return hostID;
	}

	public JDBSuppliers(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public boolean create(String supplier, String type, String description)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setSupplierID(supplier);
			setType(type);
			setDescription(description);
			
			setEnabled(true);

			if (isValidSupplier() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSuppliers.create"));
				stmtupdate.setString(1, getSupplierID());
				stmtupdate.setString(2, getType());
				stmtupdate.setString(3, getDescription());
				stmtupdate.setString(4, getEnabled());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Supplier ID already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidSupplier() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSuppliers.update"));
				stmtupdate.setString(1, getType());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getEnabled());
				stmtupdate.setString(4, getSupplierID());
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

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidSupplier() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSuppliers.delete"));
				stmtupdate.setString(1, getSupplierID());
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

	public boolean renameTo(String newType)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidSupplier() == true)
			{
				JDBSuppliers mattype = new JDBSuppliers(getHostID(), getSessionID());
				mattype.setSupplierID(newType);
				if (mattype.isValidSupplier() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSuppliers.renameTo1"));
					stmtupdate.setString(1, newType);
					stmtupdate.setString(2, getSupplierID());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSuppliers.renameTo2"));
					stmtupdate.setString(1, newType);
					stmtupdate.setString(2, getSupplierID());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setSupplierID(newType);
					result = true;
				} else
				{
					setErrorMessage("New Supplier ID is already in use.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidSupplier()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSuppliers.isValidSupplier"));
			stmt.setString(1, getSupplierID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				if (getSupplierID().equals("") == false)
				{
					setErrorMessage("Invalid Supplier [" + getSupplierID() + "]");
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public JDBSuppliers(String host, String session, String id,String type, String description,String enabled)
	{
		setHostID(host);
		setSessionID(session);
		setSupplierID(id);
		setType(type);
		setDescription(description);
		setEnabled(enabled);
	}

	public String getDescription()
	{
		String result = "";
		result = JUtility.replaceNullStringwithBlank(dbDescription);
		return result;
	}

	
	public String getType()
	{
		String result = "";
		result = JUtility.replaceNullStringwithBlank(dbType);
		return result;
	}
	
	public String getErrorMessage()
	{
		return dbErrorMessage;
	}
	
	public void setEnabled(String enabled)
	{
		dbEnabled = JUtility.replaceNullStringwithBlank(enabled);
		if (dbEnabled.equals(""))
		{
			dbEnabled = "N";
		}
	}
	
	public void setEnabled(boolean enabled)
	{
		if (enabled)
		{
			dbEnabled = "Y";
		}
		else
		{
			dbEnabled = "N";
		}
	}
	
	public String getEnabled()
	{
		dbEnabled = JUtility.replaceNullStringwithBlank(dbEnabled);
		
		if (dbEnabled.equals(""))
		{
			dbEnabled = "N";
		}
		
		return dbEnabled;
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
	
	public void clear()
	{
		setType("");
		setDescription("");
		setErrorMessage("");
	}

	public boolean getSupplierProperties(String id)
	{
		setSupplierID(id);
		return getSupplierProperties();
	}

	public boolean getSupplierProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getSupplierProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSuppliers.getSupplierProperties"));
			stmt.setString(1, getSupplierID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setType(rs.getString("supplier_type"));
				setDescription(rs.getString("description"));
				setEnabled(rs.getString("enabled"));
				result = true;
			} else
			{
				if (getSupplierID().equals("") == false)
				{
					setErrorMessage("Invalid Supplier ID");
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public Vector<JDBSuppliers> getSuppliers(boolean enabled)
	{
		Vector<JDBSuppliers> typeList = new Vector<JDBSuppliers>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSuppliers.getSuppliers"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBSuppliers mt = new JDBSuppliers(getHostID(), getSessionID());
				mt.setSupplierID(rs.getString("supplier_id"));
				mt.setType(rs.getString("supplier_type"));
				mt.setDescription(rs.getString("description"));
				mt.setEnabled(rs.getString("enabled"));
				
				if (mt.isEnabled() == enabled)
				{
					typeList.add(mt);
				}
				
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return typeList;
	}

	public String getSupplierID()
	{
		String result = "";
		if (dbSupplierID != null)
			result = dbSupplierID;
		return result;
	}

	public void setDescription(String description)
	{
		dbDescription = JUtility.replaceNullStringwithBlank(description);
	}
	
	public void setType(String type)
	{
		dbType = JUtility.replaceNullStringwithBlank(type);
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setSupplierID(String id)
	{
		dbSupplierID = id;
	}

	public String toString()
	{
		String result = "";
		if (getSupplierID().equals("") == false)
		{
			result = JUtility.padString(getSupplierID(), true, field_supplier_id, " ") + " " + getDescription();
		} else
		{
			result = "";
		}

		return result;
	}
}
