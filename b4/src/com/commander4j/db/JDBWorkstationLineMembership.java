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
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBWorkstationLineMembership {

	private String db_error_message;
	private Exception db_exception;
	private String db_workstation_id;
	private String db_line_id;
	private String db_group_id;
	final Logger logger = Logger.getLogger(JDBWorkstationLineMembership.class);
	private String hostID;
	private String sessionID;

	public JDBWorkstationLineMembership(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public boolean addWorkstationtoLine(String line,String group, String workstation)
	{
		boolean result = false;

		result = create(line,group, workstation);
		return result;
	}

	public boolean create()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			if (isWorkstationAssignedToLine() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLineWorkstationMembership.create"));
				stmtupdate.setString(1, getLineId());
				stmtupdate.setString(2, getGroupId());
				stmtupdate.setString(3, getWorkstationId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Line / Workstation already exists");
			}
		} catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	public boolean renameLine(String oldLine,String newLine)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLineWorkstationMembership.renameLine"));
			stmtupdate.setString(1, newLine);
			stmtupdate.setString(2, oldLine);
			stmtupdate.execute();
			stmtupdate.clearParameters();

			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();

			setLineId(newLine);
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(String line, String group,String workstation)
	{
		setWorkstationId(workstation);
		setLineId(line);
		setGroupId(group);
		return create();
	}
	
	
	public boolean delete(String line,String group,String workstation)
	{
		setLineId(line);
		setGroupId(group);
		setWorkstationId(workstation);
		return delete();
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isWorkstationAssignedToLine() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLineWorkstationMembership.delete"));
				stmtupdate.setString(1, getLineId());
				stmtupdate.setString(2, getGroupId());
				stmtupdate.setString(3, getWorkstationId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	public boolean deleteForWorkstationID(String workstation)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		setWorkstationId(workstation);

		try
		{
			if (isWorkstationAssignedToLine() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLineWorkstationMembership.deleteForWorkstationID"));
				stmtupdate.setString(1, getWorkstationId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
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

	public String getLineId()
	{
		return db_line_id;
	}

	public String getGroupId()
	{
		return db_group_id;
	}	
	
	public String getWorkstationId()
	{
		return db_workstation_id;
	}

	public LinkedList<String> getWorkstationsAssignedtoLine(String line,String group)
	{

		LinkedList<String> workstationList = new LinkedList<String>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setLineId(line);
		setGroupId(group);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLineWorkstationMembership.getWorkstationsAssignedtoLine"));
			stmt.setString(1, getLineId());
			stmt.setString(2, getGroupId());
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
			setError(e);
		}

		return workstationList;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean isWorkstationAssignedToLine()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLineWorkstationMembership.isWorkstationAssignedToLine"));
			stmt.setString(1, getLineId());
			stmt.setString(2, getGroupId());
			stmt.setString(3, getWorkstationId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Line Group Workstation Membership");
			}
			stmt.close();

		} catch (SQLException e)
		{
			setError(e);
		}

		return result;

	}

	public boolean removeAllWorkstationsfromLine(String line,String group)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("removeAllWorkstationsfromLine Line [" + getLineId()  + " / " + getGroupId()+ "]");
		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLineWorkstationMembership.removeAllWorkstationsfromLine"));
			stmtupdate.setString(1, line);
			stmtupdate.setString(2, group);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		} catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	public boolean removeWorkstationfromLine(String line, String group,String workstation)
	{
		boolean result = false;

		setLineId(line);
		setGroupId(group);
		setWorkstationId(workstation);
		result = delete();

		return result;
	}

	public boolean removeWorkstationfromAllLines(String workstation,String group)
	{
		boolean result = false;
		JDBAutoLabeller al = new JDBAutoLabeller(getHostID(), getSessionID());
		LinkedList<JDBListData> lineList = new LinkedList<JDBListData>();
		String line = "";

		lineList = al.getLabellerIDsforGroup(group);

		for (int j = 0; j < lineList.size(); j++)
		{
			line = ((JDBAutoLabeller) lineList.get(j).getObject()).getLine();
			group = ((JDBAutoLabeller) lineList.get(j).getObject()).getGroup();
			removeWorkstationfromLine(line, group,workstation);
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

	public void setLineId(String UserId)
	{
		db_line_id = UserId;
	}

	public void setGroupId(String group)
	{
		db_group_id = group;
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
