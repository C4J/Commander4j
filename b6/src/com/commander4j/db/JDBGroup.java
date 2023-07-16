package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBGroup.java
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
import java.sql.Statement;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;

/**
 * The JDBGroup class is used to insert/update and delete records from the
 * SYS_GROUPS table. The table is part of the security / permissions within the
 * application.
 * <p>
 * <img alt="" src="./doc-files/SYS_GROUPS.jpg" >
 * 
 * @see com.commander4j.db.JDBGroupPermissions JDBGroupPermissions
 */
public class JDBGroup
{

	private String dbDescription;
	private String dbErrorMessage;
	private String dbGroupId;
	public static int field_group_id = 20;
	public static int field_description = 80;
	private JDBGroupPermissions groupPermissions;
	private JDBUserGroupMembership userGroupMembership;
	private final Logger logger = Logger.getLogger(JDBGroup.class);
	private String hostID;
	private String sessionID;
	private JDBAuditPermissions auditPerm;

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

	public JDBGroup(String host, String session)
	{
		// Get number of days that a password lasts before it needs changing.
		setHostID(host);
		setSessionID(session);
		groupPermissions = new JDBGroupPermissions(getHostID(), getSessionID());
		userGroupMembership = new JDBUserGroupMembership(getHostID(), getSessionID());
		auditPerm = new JDBAuditPermissions(getHostID(), getSessionID());
	}

	public void clear()
	{
		setDescription("");
	}

	public boolean addModule(String lModuleId, String actionedBy)
	{
		boolean result = false;

		if (isValidGroupId() == true)
		{
			result = groupPermissions.create(getGroupId(), lModuleId, actionedBy);
			setErrorMessage(groupPermissions.getErrorMessage());
		}

		return result;
	}

	public boolean create(String lGroupId, String ldescription, String actionedBy)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setGroupId(lGroupId);
			setDescription(ldescription);

			if (isValidGroupId() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroup.create"));
				stmtupdate.setString(1, getGroupId());
				stmtupdate.setString(2, getDescription());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;

				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "GROUP", "CREATE", getGroupId(), ldescription);

			} else
			{
				setErrorMessage("Group Id already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String group, String actionedBy)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			setGroupId(group);
			if (isValidGroupId() == true)
			{

				userGroupMembership.setGroupId(getGroupId());
				userGroupMembership.removeAllUsersfromGroup();

				groupPermissions.setGroupId(getGroupId());
				groupPermissions.removeModulesfromGroup();

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroup.delete"));
				stmtupdate.setString(1, getGroupId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;

				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "GROUP", "DELETE", getGroupId(), "");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public String getDescription()
	{
		return dbDescription;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getGroupId()
	{
		return dbGroupId;
	}

	public LinkedList<String> getGroupIds()
	{
		LinkedList<String> groupList = new LinkedList<String>();

		Statement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
			stmt.setFetchSize(50);
			rs = stmt.executeQuery(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroup.getGroupIds"));

			while (rs.next())
			{
				groupList.addLast(rs.getString("group_id"));
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return groupList;
	}

	public ResultSet getGroupIdResultset()
	{

		Statement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
			stmt.setFetchSize(50);
			rs = stmt.executeQuery(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroup.getGroups"));

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public boolean getGroupProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroup.getGroupProperties"));
			stmt.setString(1, getGroupId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDescription(rs.getString("description"));
				result = true;
			} else
			{
				setErrorMessage("Invalid GroupId");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public LinkedList<JDBListData> getModulesAssigned()
	{
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();

		groupPermissions.setGroupId(getGroupId());
		moduleList = groupPermissions.getModulesAssigned(getHostID(), getSessionID());

		return moduleList;
	}

	public LinkedList<JDBListData> getModulesUnAssigned()
	{
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();

		groupPermissions.setGroupId(getGroupId());
		moduleList = groupPermissions.getModulesUnAssigned(getHostID(), getSessionID());

		return moduleList;
	}

	public boolean isValidGroupId()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroup.isValidGroupId"));
			stmt.setFetchSize(1);
			stmt.setString(1, getGroupId());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid GroupId");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public boolean removeModule(String lModuleId, String actionedBy)
	{
		boolean result = false;

		if (isValidGroupId() == true)
		{
			groupPermissions.setGroupId(getGroupId());
			groupPermissions.setModuleId(lModuleId);
			result = groupPermissions.delete(actionedBy);
			setErrorMessage(groupPermissions.getErrorMessage());
		}

		return result;
	}

	public boolean renameTo(String oldGroupId, String newGroupId, String actionedBy)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			setGroupId(oldGroupId);
			if (isValidGroupId() == true)
			{
				JDBGroup newgroup = new JDBGroup(getHostID(), getSessionID());
				newgroup.setGroupId(newGroupId);
				if (newgroup.isValidGroupId() == false)
				{
					groupPermissions.setGroupId(getGroupId());
					groupPermissions.renameGroupTo(newGroupId);

					userGroupMembership.setGroupId(getGroupId());
					userGroupMembership.renameGroupTo(newGroupId);

					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroup.renameTo"));
					stmtupdate.setString(1, newGroupId);
					stmtupdate.setString(2, getGroupId());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setGroupId(newGroupId);
					result = true;

					auditPerm.generateNewAuditLogID();
					auditPerm.write(actionedBy, "GROUP", "RENAME", oldGroupId, newGroupId);
				} else
				{
					setErrorMessage("New group_id is already in use.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public void setDescription(String description)
	{
		dbDescription = description;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setGroupId(String groupId)
	{
		dbGroupId = groupId;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidGroupId() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBGroup.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getGroupId());
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
