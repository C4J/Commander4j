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
 * The JDBWasteReportingIDS class updates the table
 * APP_WASTE_REPORTING_IDS. This table contains a single row for each unique
 * Waste Transaction Type as stored in the APP_WASTE_REPORTING_IDS table. 
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_REPORTING_IDS.jpg" >
 * 
 * @see com.commander4j.db.JDBWasteReasons JDBWasteReasons
 * @see com.commander4j.db.JDBWasteReportingIDS JDBWasteReportingIDS
 * @see com.commander4j.db.JDBWasteTransactionType JDBWasteTransactionType
 * @see com.commander4j.db.JDBWasteLocationReporting JDBWasteLocationReporting
 * @see com.commander4j.db.JDBWasteReasons JDBWasteTypes
 * @see com.commander4j.db.JDBWasteReasons JDBWasteMaterial
 * @see com.commander4j.db.JDBWasteReasons JDBWasteLog
 * 
 */

public class JDBWasteReportingIDS
{
	public static int field_WasteReportingID = 25;
	public static int field_Description = 80;
	public static int field_ReportingGroup = 10;
	public static int field_Enabled = 1;
	
	public static int displayModeFull = 0;
	public static int displayModeShort = 1;
	
	private String dbErrorMessage;
	private String dbWasteReportingID;   /* PK */
	private String dbDescription;
	private Integer dbReportingGroup;
	private String dbEnabled;
	
	private final Logger logger = Logger.getLogger(JDBWasteReportingIDS.class);
	private String hostID;
	private String sessionID;
	private Integer displayMode=displayModeFull;

	public JDBWasteReportingIDS(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setDescription("");
		setReportingGroup(0);
		setEnabled(true);
	}
	
	public void setDisplayMode(int mode)
	{
		displayMode = mode;
	}
	
	public boolean create(String res)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setWasteReportingID(res);
			clear();

			if (isValidWasteReportingID() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.create"));
				stmtupdate.setString(1, getWasteReportingID());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setInt(3, getReportingGroup());
				stmtupdate.setString(4, getEnabled());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Waste Reporting ID " + getWasteReportingID() + " already exists");
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
			if (isValidWasteReportingID() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.delete"));
				stmtupdate.setString(1, getWasteReportingID());
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

	public Integer getReportingGroup()
	{
		return dbReportingGroup;
	}
	

	public Integer getReportingGroup(String res)
	{
		Integer result = 0;

		if (getWasteReportingIDProperties(res))
		{

			result = getReportingGroup();

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

	public ResultSet getWasteReportingIDSResultSet(PreparedStatement criteria)
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

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setWasteReportingID(rs.getString("waste_reporting_id"));
			setDescription(rs.getString("description"));
			setReportingGroup(rs.getInt("reporting_group"));
			setEnabled(rs.getString("enabled"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getWasteReportingID()
	{
		return JUtility.replaceNullStringwithBlank(dbWasteReportingID);
	}

	public boolean getReportingIDProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.getProperties"));
			stmt.setString(1, getWasteReportingID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Waste Reporting ID " + getWasteReportingID());
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getWasteReportingIDProperties(String res)
	{
		setWasteReportingID(res);
		return getReportingIDProperties();
	}
	
	public ResultSet getWasteReportIDSDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.getWasteLocations"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public LinkedList<JDBListData> getWasteReportingIDs(Boolean enabled,int mode) {
		
		LinkedList<JDBListData> sampList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.getWasteReportingIDs"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteReportingIDS samp = new JDBWasteReportingIDS(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				icon = samp.geReportingIDIcon();
				
				if (samp.isEnabled().equals(enabled))
				{
					JDBListData mld = new JDBListData(icon, index, true, samp);
					sampList.add(mld);
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
	
	public LinkedList<JDBListData> getWasteReportingIDsAssignedtoLocation(String locn) {
		
		LinkedList<JDBListData> sampList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.getReportingIDsAssignedtoLocation"));
			stmt.setFetchSize(250);
			stmt.setString(1, locn);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteReportingIDS samp = new JDBWasteReportingIDS(getHostID(), getSessionID());
				samp.setDisplayMode(displayModeShort);
				samp.getPropertiesfromResultSet(rs);
				icon = samp.geReportingIDIcon();
				JDBListData mld = new JDBListData(icon, index, true, samp);
				sampList.add(mld);
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
	
	public LinkedList<JDBListData> getReportingIDsUnassignedtoLocation(String locn) {
		
		LinkedList<JDBListData> sampList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.getReportingIDsUnassignedtoLocation"));
			stmt.setFetchSize(250);
			stmt.setString(1, locn);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteReportingIDS samp = new JDBWasteReportingIDS(getHostID(), getSessionID());
				samp.setDisplayMode(displayModeShort);
				samp.getPropertiesfromResultSet(rs);
				icon = samp.geReportingIDIcon();
				JDBListData mld = new JDBListData(icon, index, true, samp);
				sampList.add(mld);
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
	
	public Icon geReportingIDIcon()
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
		} else
		{
			result = false;
		}
		return result;
	}

	public boolean isValidWasteReportingID()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.isValidWasteReportingID"));
			stmt.setFetchSize(1);
			stmt.setString(1, getWasteReportingID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Waste Reporting ID " + getWasteReportingID() + " not found.");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidWasteReportingID(String res)
	{
		setWasteReportingID(res);

		return isValidWasteReportingID();
	}

	public void setReportingGroup(Integer suffix)
	{
		dbReportingGroup = suffix;
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
		} else
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

	public void setWasteReportingID(String res)
	{
		dbWasteReportingID = res;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";
		String grp = "";
		
		if (getReportingGroup()==-1)
		{
			grp="";
		}
		else
		{
			grp=JUtility.padString(getReportingGroup().toString(), false, field_ReportingGroup, " ");
		}
		
		if (displayMode==displayModeFull)
		{
			result = JUtility.padString(getWasteReportingID().toString(), true, field_WasteReportingID, " ")+	
			        JUtility.padString(getDescription().toString(), true, field_Description-55, " ")+"       "+ grp;
		}
		
		if (displayMode==displayModeShort)
		{
			result = getWasteReportingID().toString();
		}

		return result;
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteReportingID() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setInt(2, getReportingGroup());
				stmtupdate.setString(3, getEnabled());
				stmtupdate.setString(4, getWasteReportingID());
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
	
	public boolean rename(String oldReportingID,String newReportingID)
	{
		boolean result = false;
		setWasteReportingID(oldReportingID);
		setErrorMessage("");

		try
		{
			if (isValidWasteReportingID(oldReportingID) == true)
			{
				if (isValidWasteReportingID(newReportingID) == false)
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReportingIDS.renameReportingID"));
					stmtupdate.setString(1, newReportingID);
					stmtupdate.setString(2, oldReportingID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationReporting.renameReportingID"));
					stmtupdate.setString(1, newReportingID);
					stmtupdate.setString(2, oldReportingID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					
					setWasteReportingID(newReportingID);
					
					result = true;
				}
				else
				{
					setErrorMessage(newReportingID + " already exists.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
