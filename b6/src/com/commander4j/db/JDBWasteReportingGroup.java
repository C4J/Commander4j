package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWasteLocations.java
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
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBWasteReportingGroup class updates the table APP_WASTE_REPORTING_GROUP. This table
 * contains a single row for each unique Waste Transaction Type as stored in the
 * APP_WASTE_REPORTING_GROUP table.
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_REPORTING_GROUP.jpg" >
 * 
 * 
 */

public class JDBWasteReportingGroup
{
	public static int field_WasteReportingGroup = 5;
	public static int field_Description = 80;
	public static int field_Enabled = 1;

	private String dbErrorMessage;
	private int    dbWasteGroupID; /* PK */
	private String dbDescription;
	private String dbEnabled;

	private final Logger logger = Logger.getLogger(JDBWasteReportingGroup.class);
	private String hostID;
	private String sessionID;

	public JDBWasteReportingGroup(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setDescription("");
		setEnabled(true);
	}

	public boolean create(int res)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setWasteReportingGroup(res);
			clear();

			if (isValidWasteReportingGroup() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingGroup.create"));
				stmtupdate.setInt(1, getWasteReportGroup());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getEnabled());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Reporting Group " + getWasteReportGroup() + " already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public Icon getTypeIcon()
	{
		Icon icon = new ImageIcon();

		try
		{
			if (isEnabled() == false)
			{
				icon = Common.icon_cancel_16x16;
			}
			else
			{
				icon = Common.icon_ok_16x16;

			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

		return icon;
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteReportingGroup() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingGroup.delete"));
				stmtupdate.setInt(1, getWasteReportGroup());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.renameReportingGroup"));
				stmtupdate.setInt(1, -1);
				stmtupdate.setInt(2, getWasteReportGroup());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
				
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
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

	public ResultSet getWasteReportingGroupsResultSet(PreparedStatement criteria)
	{
		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();
		}
		catch (Exception e)
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
			setWasteReportingGroup(rs.getInt("reporting_group"));
			setDescription(rs.getString("description"));
			setEnabled(rs.getString("enabled"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public int getWasteReportGroup()
	{
		return dbWasteGroupID;
	}

	public boolean getWasteReportingGroupProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingGroup.getProperties"));
			stmt.setInt(1, getWasteReportGroup());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Waste Reporting Group [" + getWasteReportGroup()+"]");
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public ResultSet getWasteReportingGroupDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingGroup.getWasteReportingGroups"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public boolean getWasteReportingGroupProperties(int res)
	{
		setWasteReportingGroup(res);
		return getWasteReportingGroupProperties();
	}

	public LinkedList<JDBListData> getWasteReportingGroups(boolean enabled)
	{
		LinkedList<JDBListData> sampList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingGroup.getWasteReportingGroups"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteReportingGroup samp = new JDBWasteReportingGroup(getHostID(), getSessionID());
				samp.getPropertiesfromResultSet(rs);
				icon = samp.getTypeIcon();

				if (samp.isEnabled().equals(enabled))
				{

					JDBListData mld = new JDBListData(icon, index, true, samp);

					sampList.addLast(mld);
				}
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
	
	public LinkedList<JDBWasteReportingGroup> getWasteGroupList() {
		
		LinkedList<JDBWasteReportingGroup> wasteGroupList = new LinkedList<JDBWasteReportingGroup>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingGroup.getWasteReportingGroups"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteReportingGroup samp = new JDBWasteReportingGroup(getHostID(), getSessionID());
				samp.getPropertiesfromResultSet(rs);

				wasteGroupList.addLast(samp);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteGroupList;
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
		}
		else
		{
			result = false;
		}
		return result;
	}

	public boolean isValidWasteReportingGroup()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingGroup.isValidWasteReportingGroup"));
			stmt.setFetchSize(1);
			stmt.setInt(1, getWasteReportGroup());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Waste Reporting Group " + getWasteReportGroup() + " not found.");
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

	public boolean isValidWasteReportingGroup(int res)
	{
		setWasteReportingGroup(res);

		return isValidWasteReportingGroup();
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
		}
		else
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

	public void setWasteReportingGroup(int res)
	{
		dbWasteGroupID = res;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";
		
		if (getWasteReportGroup()==-1)
		{
			result = "";
		}
		else
		{
			result = JUtility.padString(String.valueOf(getWasteReportGroup()), false, field_WasteReportingGroup, " ") + "   " +getDescription();
		}
		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteReportingGroup() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingGroup.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getEnabled());
				stmtupdate.setInt(3, getWasteReportGroup());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public boolean rename(int oldGroup,int newGroup)
	{
		boolean result = false;
		setWasteReportingGroup(oldGroup);
		setErrorMessage("");

		try
		{
			if (isValidWasteReportingGroup(oldGroup) == true)
			{
				if (isValidWasteReportingGroup(newGroup) == false)
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingGroup.renameReportingGroup"));
					stmtupdate.setInt(1, newGroup);
					stmtupdate.setInt(2, oldGroup);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.renameReportingGroup"));
					stmtupdate.setInt(1, newGroup);
					stmtupdate.setInt(2, oldGroup);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					
					setWasteReportingGroup(newGroup);
					
					result = true;
				}
				else
				{
					setErrorMessage(newGroup + " already exists.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
