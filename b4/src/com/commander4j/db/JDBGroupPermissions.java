package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBGroupPermissions.java
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

import javax.swing.Icon;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;


public class JDBGroupPermissions
{

	private String dbErrorMessage;
	private String dbGroupId;
	private String dbModuleId;
	private Logger logger = Logger.getLogger(JDBGroupPermissions.class);
	private String hostID;
	private String sessionID;
	private JDBAuditPermissions auditPerm;

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

	public JDBGroupPermissions(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		auditPerm = new JDBAuditPermissions(getHostID(), getSessionID());
	}

	public boolean create(String lGroupId, String lModuleId, String actionedBy) {
		boolean result = false;
		setErrorMessage("");

		try
		{
			setGroupId(lGroupId);
			setModuleId(lModuleId);

			if (isValidGroupPermission() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroupPermissions.create"));
				stmtupdate.setString(1, getGroupId());
				stmtupdate.setString(2, getModuleId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
				
				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "GROUP_MODULE", "ADD", getGroupId(), getModuleId());
			}
			else
			{
				setErrorMessage("Group Permission already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String groupid, String moduleid,String actionedBy) {
		setGroupId(groupid);
		setModuleId(moduleid);
		return delete(actionedBy);
	}

	public boolean deletePermissionsForModule(String moduleid) {
		setModuleId(moduleid);
		return deletePermissionsForModule();
	}

	public boolean deletePermissionsForModule() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroupPermissions.deletePermissionsForModule"));
			stmtupdate.setString(1, getModuleId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String actionedBy) {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidGroupPermission() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroupPermissions.delete"));
				stmtupdate.setString(1, getGroupId());
				stmtupdate.setString(2, getModuleId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "GROUP_MODULE", "REMOVE", getGroupId(), getModuleId());
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public String getErrorMessage() {
		return dbErrorMessage;
	}

	public String getGroupId() {
		return dbGroupId;
	}


	public String getModuleId() {
		return dbModuleId;
	}


	public LinkedList<JDBListData> getModulesAssigned(String host, String session) {
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();
		Icon icon;

		PreparedStatement stmt;
		ResultSet rs;
		setHostID(host);
		setSessionID(session);
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroupPermissions.getModulesAssigned"));
			stmt.setString(1, getGroupId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				icon = JDBModule.getModuleIcon(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, 0, true, rs.getString("module_id"));
				moduleList.addLast(mld);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}


	public LinkedList<JDBListData> getModulesUnAssigned(String host, String session) {
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();
		Icon icon;
		PreparedStatement stmt;
		ResultSet rs;
		setHostID(host);
		setSessionID(session);
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroupPermissions.getModulesUnAssigned"));
			stmt.setString(1, getGroupId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				icon = JDBModule.getModuleIcon(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, 0, true, rs.getString("module_id"));
				moduleList.addLast(mld);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return moduleList;
	}


	public boolean isValidGroupPermission() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroupPermissions.isValidGroupPermission"));
			stmt.setString(1, getGroupId());
			stmt.setString(2, getModuleId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Group Permission");
			}
			stmt.close();
			rs.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}


	public boolean removeModulesfromGroup() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroupPermissions.removeModulesfromGroup"));
			stmtupdate.setString(1, getGroupId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	public boolean renameGroupTo(String lGroupId) {
		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroupPermissions.renameGroupTo"));
			stmtupdate.setString(1, lGroupId);
			stmtupdate.setString(2, getGroupId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	public boolean renameModuleTo(String lModuleId) {
		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroupPermissions.renameModuleTo"));
			stmtupdate.setString(1, lModuleId);
			stmtupdate.setString(2, getModuleId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	private void setErrorMessage(String errorMsg) {
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}


	public void setGroupId(String groupId) {
		dbGroupId = groupId;
	}


	public void setModuleId(String moduleId) {
		dbModuleId = moduleId;
	}
}
