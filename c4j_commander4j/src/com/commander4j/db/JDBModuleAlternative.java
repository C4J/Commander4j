package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBModuleAlternative.java
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
 * JDBModuleAlternative class is used to insert/update/delete records from the SYS_MODULES_ALTERNATIVE table.
 * <p>
 * <img alt="" src="./doc-files/SYS_MODULES_ALTERNATIVE.jpg" >
 * 
 */
public class JDBModuleAlternative
{

	private String dbErrorMessage;
	private String dbModuleId;
	private String dbWorkstationId;
	private String dbAlternativeModuleId;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBModuleAlternative.class);
	private String hostID;
	private String sessionID;

	public JDBModuleAlternative(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public boolean create(String lModuleId, String lWorkstationId, String lAltModuleID)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setModuleId(lModuleId);
			setWorkstationId(lWorkstationId);
			setAternativeModuleId(lAltModuleID);

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModuleAlternative.create"));
			stmtupdate.setString(1, getModuleId());
			stmtupdate.setString(2, getWorkstationId());
			stmtupdate.setString(3, getAlternativeModuleId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String lModuleId, String lWorkstationId)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			setModuleId(lModuleId);
			setWorkstationId(lWorkstationId);
			
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModuleAlternative.delete"));
			stmtupdate.setString(1, getModuleId());
			stmtupdate.setString(2, getWorkstationId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public String getAlternativeModuleId()
	{
		return dbAlternativeModuleId;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getModuleId()
	{
		return dbModuleId;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getWorkstationId()
	{
		return dbWorkstationId;
	}

	public void setAternativeModuleId(String sequenceId)
	{
		dbAlternativeModuleId = sequenceId;
	}

	private void setErrorMessage(String errorMsg)
	{
		logger.error(errorMsg);
		dbErrorMessage = errorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setModuleId(String menuId)
	{
		dbModuleId = menuId;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setWorkstationId(String moduleId)
	{
		dbWorkstationId = moduleId;
	}

	public boolean update()
	{
		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModuleAlternative.update"));
			stmtupdate.setString(1, getAlternativeModuleId());
			stmtupdate.setString(2, getModuleId());
			stmtupdate.setString(3, getWorkstationId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public LinkedList<String> getWorkstations(String mod)
	{

		LinkedList<String> workstationList = new LinkedList<String>();
		PreparedStatement stmt;
		ResultSet rs;
		setModuleId(mod);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModuleAlternative.getWorkstations"));
			stmt.setString(1, getModuleId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				String resource = new String();
				resource = rs.getString("workstation_id");
				workstationList.add(resource);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return workstationList;
	}
	
	public void clear()
	{
		setAternativeModuleId("");
	}
	
	
	public ResultSet getDataResultSet(PreparedStatement criteria)
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
	
	public String substituteAlternative(String moduleid)
	{
		String workstation = JUtility.getClientName().toUpperCase();
		String alt = "";
		
		if (getProperties(moduleid,workstation))
		{
			alt = getAlternativeModuleId();
		}
		else
		{
			alt = moduleid;
		}
		
		return alt;
	}
	
	public boolean getProperties(String module,String workstation)
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		clear();
		setModuleId(module);
		setWorkstationId(workstation);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModuleAlternative.getProperties"));
			stmt.setString(1, getModuleId());
			stmt.setString(2, getWorkstationId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setAternativeModuleId(rs.getString("alt_module_id"));
				result = true;
			} else
			{
				setErrorMessage("Invalid Module ID / Workstation ID ["+getModuleId()+"/"+getWorkstationId()+"]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
}
