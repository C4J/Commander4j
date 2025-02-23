package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;

public class JDBViewProductGroups
{
	private String dbErrorMessage;

	private String dbProductGroup;

	public static int field_group = 30 ;

	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBViewProductGroups.class);
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

	public JDBViewProductGroups(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

	}

	public LinkedList<JDBViewProductGroups> getProductGroups()
	{
		LinkedList<JDBViewProductGroups> groupList = new LinkedList<JDBViewProductGroups>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewProductGroups.getProductGroups"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBViewProductGroups prodgroup = new JDBViewProductGroups(getHostID(), getSessionID());
				prodgroup.setProductGroup(rs.getString("product_group"));
				groupList.add(prodgroup);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return groupList;
	}


	public JDBViewProductGroups(String host, String session, String group)
	{
		setHostID(host);
		setSessionID(session);
		setProductGroup(group);
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public void clear()
	{

		setErrorMessage("");
	}


	public Vector<JDBViewProductGroups> getGroups()
	{
		Vector<JDBViewProductGroups> typeList = new Vector<JDBViewProductGroups>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewProductGroups.getProductGroups"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBViewProductGroups mt = new JDBViewProductGroups(getHostID(), getSessionID());
				mt.setProductGroup(rs.getString("product_group"));
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

	public String getProductGroup()
	{
		String result = "";
		if (dbProductGroup != null)
			result = dbProductGroup;
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

	public void setProductGroup(String group)
	{
		dbProductGroup = group;
	}

	public String toString()
	{
		String result = "";
		if (getProductGroup().equals("") == false)
		{
			result = getProductGroup();
		} else
		{
			result = "";
		}

		return result;
	}
}
