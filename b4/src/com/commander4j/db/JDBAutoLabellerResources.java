package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBAutoLabellerResources.java
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

public class JDBAutoLabellerResources {
	private String dbErrorMessage;
	private String dbRequiredResource;
	private String dbLine;
	private String dbGroup;
	public static int field_Line = 20;
	public static int field_RequiredResource = 50;
	private final Logger logger = Logger.getLogger(JDBAutoLabellerResources.class);
	private String hostID;
	private String sessionID;

	public JDBAutoLabellerResources(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setErrorMessage("");
	}

	public boolean create(String Line, String Group,String RequiredResource)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			if (isValidLineResource(Line, Group,RequiredResource) == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabellerResources.create"));
				stmtupdate.setString(1, Line);
				stmtupdate.setString(2,Group);
				stmtupdate.setString(3, RequiredResource);
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Line / Group / Resource already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String Line, String group, String RequiredResource)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidLineResource(Line,group, RequiredResource) == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabellerResources.delete"));
				stmtupdate.setString(1, Line);
				stmtupdate.setString(2, group);
				stmtupdate.setString(3, RequiredResource);
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

	public boolean renameLine(String fromLine, String toLine)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabellerResources.renameLine"));
			stmtupdate.setString(1, toLine);
			stmtupdate.setString(2, fromLine);
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

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getLine()
	{
		String result = "";
		if (dbLine != null)
			result = dbLine;
		return result;
	}
	
	public String getGroup()
	{
		String result = "";
		if (dbGroup != null)
			result = dbGroup;
		return result;
	}

	public String getRequiredResource()
	{
		return dbRequiredResource;
	}

	public LinkedList<String> getRequiredResourceList(String line,String group)
	{
		LinkedList<String> resourceList = new LinkedList<String>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setLine(line);
		setGroup(group);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabellerResources.getResources"));
			stmt.setFetchSize(50);
			stmt.setString(1, getLine());
			stmt.setString(2, getGroup());
			rs = stmt.executeQuery();

			while (rs.next())
			{
				String resource = new String();
				resource = JUtility.replaceNullStringwithBlank(rs.getString("required_resource"));
				resourceList.add(resource);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return resourceList;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean isValidLineResource(String line,String group, String resource)
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabellerResources.isValid"));
			stmt.setString(1, line);
			stmt.setString(2,group);
			stmt.setString(3, resource);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Line / Group / Resource");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
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

	public void setLine(String Line)
	{
		dbLine = Line;
	}
	
	public void setGroup(String group)
	{
		dbGroup = group;
	}

	public void setRequiredResource(String RequiredResource)
	{
		dbRequiredResource = RequiredResource;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

}
