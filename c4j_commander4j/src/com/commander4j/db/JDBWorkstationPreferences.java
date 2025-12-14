package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWorkstationLineMembership.java
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

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBWorkstationPreferences
{

	private String db_error_message = "";
	private Exception db_exception;
	private String db_workstation_id = "";
	private String db_module_id = "";
	private String db_device_type = "";
	private String db_printer_id = "";
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBWorkstationPreferences.class);
	private String hostID = "";
	private String sessionID = "";

	public JDBWorkstationPreferences(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public boolean create()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWorkstationPreferences.create"));
			stmtupdate.setString(1, getWorkstationId());
			stmtupdate.setString(2, getModuleId());
			stmtupdate.setString(3, getDeviceType());
			stmtupdate.setString(4, getPrinterId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWorkstationPreferences.update"));
			stmtupdate.setString(1, getDeviceType());
			stmtupdate.setString(2, getPrinterId());
			stmtupdate.setString(3, getWorkstationId());
			stmtupdate.setString(4, getModuleId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	public boolean save(String workstation, String module, String type, String printer)
	{
		boolean result = false;

		setWorkstationId(workstation);
		setModuleId(module);
		setDeviceType(type);
		
		setPrinterId(printer);

		if (isValid(workstation,module) == false)
		{
			result = create();
		}
		else
		{
			result = update();
		}

		return result;
	}

	public boolean update(String workstation, String module, String type, String printer)
	{
		setWorkstationId(workstation);
		setModuleId(module);
		setDeviceType(type);
		setPrinterId(printer);

		return update();
	}

	public boolean create(String workstation, String module, String type, String printer)
	{
		setWorkstationId(workstation);
		setModuleId(module);
		setDeviceType(type);
		setPrinterId(printer);

		return create();
	}

	public boolean delete(String workstation, String module)
	{
		setWorkstationId(workstation);
		setModuleId(module);

		return delete();
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWorkstationPreferences.delete"));
			stmtupdate.setString(1, getWorkstationId());
			stmtupdate.setString(2, getModuleId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	public String getErrorMessage()
	{
		return db_error_message;
	}

	public Exception getException()
	{
		return db_exception;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getModuleId()
	{
		return db_module_id;
	}

	public String getDeviceType()
	{
		return db_device_type;
	}

	public void setDeviceType(String val)
	{
		db_device_type = val;
	}

	public String getPrinterId()
	{
		return db_printer_id;
	}

	public String getWorkstationId()
	{
		return db_workstation_id;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean getProperties(String workstation,String module)
	{
		setWorkstationId(workstation);
		setModuleId(module);
		
		return getProperties();
	}
	
	public boolean getProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWorkstationPreferences.getProperties"));
			stmt.setString(1, getWorkstationId());
			stmt.setString(2, getModuleId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDeviceType(rs.getString("device_type"));
				setPrinterId(rs.getString("printer_id"));
				result = true;
			}
			else
			{
				setErrorMessage("No Workstation Preferences");
			}
			stmt.close();

		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;

	}
	
	public boolean isValid(String workstation,String module)
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWorkstationPreferences.getProperties"));
			stmt.setString(1, workstation);
			stmt.setString(2, module);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("No Workstation Preferences");
			}
			stmt.close();

		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;

	}

	private void setError(Exception e)
	{
		logger.error(e);
		db_exception = e;
		setErrorMessage(e.getMessage());
	}

	private void setErrorMessage(String ErrorMsg)
	{
		db_error_message = ErrorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setModuleId(String UserId)
	{
		db_module_id = UserId;
	}

	public void setPrinterId(String group)
	{
		db_printer_id = group;
	}

	public void setWorkstationId(String GroupId)
	{
		db_workstation_id = GroupId;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		return JUtility.replaceNullStringwithBlank(getWorkstationId());
	}
}
