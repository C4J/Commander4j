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

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBWasteReasons class updates the table APP_WASTE_REASONS. This table
 * contains a single row for each unique Waste Transaction Type as stored in the
 * APP_WASTE_REASONS table.
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_REASONS.jpg" >
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

public class JDBWasteReasons
{
	public static int field_WasteReason = 25;
	public static int field_Description = 80;
	public static int field_Enabled = 1;

	private String dbErrorMessage;
	private String dbWasteReasonID; /* PK */
	private String dbDescription;
	private String dbEnabled;
	
	public static int displayModeFull = 0;
	public static int displayModeShort = 1;

	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBWasteReasons.class);
	private String hostID;
	private String sessionID;
	private Integer displayMode=displayModeFull;

	public JDBWasteReasons(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setDescription("");
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
			setWasteReasonID(res);
			clear();

			if (isValidWasteReason() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReasons.create"));
				stmtupdate.setString(1, getWasteReasonID());
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
				setErrorMessage("Waste Reason ID" + getWasteReasonID() + " already exists");
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
			if (isValidWasteReason() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReasons.delete"));
				stmtupdate.setString(1, getWasteReasonID());
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

	public ResultSet getWasteReasonsResultSet(PreparedStatement criteria)
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
			setWasteReasonID(rs.getString("waste_reason_id"));
			setDescription(rs.getString("description"));
			setEnabled(rs.getString("enabled"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getWasteReasonID()
	{
		return JUtility.replaceNullStringwithBlank(dbWasteReasonID);
	}

	public boolean getWasteReasonProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReasons.getProperties"));
			stmt.setString(1, getWasteReasonID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Waste Reason ID [" + getWasteReasonID()+"]");
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

	public ResultSet getWasteReasonsDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReasons.getWasteReasons"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public boolean getWasteReasonProperties(String res)
	{
		setWasteReasonID(res);
		return getWasteReasonProperties();
	}
	
	public String getHTMLPullDownCombo(String itemName, String defaultValue, String onchange)
	{
		String result = "";
		String selected = "";
		LinkedList<JDBWasteReasons> reasonList = new LinkedList<JDBWasteReasons>();
				
		reasonList.addAll(getWasteReasonssList(true,displayModeShort));
		result = "<SELECT width=\"100%\" style=\"width: 100%\" ID=\"" + itemName + "\" NAME=\"" + itemName + "\" " +onchange + "\">";
		result = result + "<OPTION></OPTION>";
		
		if (reasonList.size() > 0)
		{
			for (int x = 0; x < reasonList.size(); x++)
			{
				if (reasonList.get(x).getWasteReasonID().equals(defaultValue))
				{
					selected = " SELECTED";
				} else
				{
					selected = "";
				}
				result = result + "<OPTION" + selected + ">" + reasonList.get(x).getWasteReasonID()+"</OPTION>";
			}
		}
		result = result + "</SELECT>";

		return result;
	}

	public LinkedList<JDBWasteReasons> getWasteReasonssList(Boolean enabled,int mode) {

		LinkedList<JDBWasteReasons> wasteReasonList = new LinkedList<JDBWasteReasons>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReasons.getWasteReasons"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteReasons samp = new JDBWasteReasons(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				
				if (samp.isEnabled().equals(enabled))
				{	
					wasteReasonList.addLast(samp);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteReasonList;
	}
	
	public LinkedList<JDBListData> getWasteReasonIDs(Boolean enabled,int mode)
	{
		LinkedList<JDBListData> sampList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReasons.getWasteReasons"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteReasons samp = new JDBWasteReasons(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
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

	public boolean isValidWasteReason()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReasons.isValidWasteReasonID"));
			stmt.setFetchSize(1);
			stmt.setString(1, getWasteReasonID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Waste Reason ID " + getWasteReasonID() + " not found.");
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

	public boolean isValidWasteReasonID(String res)
	{
		setWasteReasonID(res);

		return isValidWasteReason();
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

	public void setWasteReasonID(String res)
	{
		dbWasteReasonID = res;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";

		if (displayMode==displayModeFull)
		{
			result = JUtility.padString(getWasteReasonID().toString(), true, field_WasteReason, " ") + getDescription();
		}
		
		if (displayMode==displayModeShort)
		{
			result = getWasteReasonID();
		}
		
		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteReason() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReasons.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getEnabled());
				stmtupdate.setString(3, getWasteReasonID());
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
	
	public boolean rename(String oldReasonID,String newReasonID)
	{
		boolean result = false;
		setWasteReasonID(oldReasonID);
		setErrorMessage("");

		try
		{
			if (isValidWasteReasonID(oldReasonID) == true)
			{
				if (isValidWasteReasonID(newReasonID) == false)
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteReasons.renameReasonID"));
					stmtupdate.setString(1, newReasonID);
					stmtupdate.setString(2, oldReasonID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.renameReasonID"));
					stmtupdate.setString(1, newReasonID);
					stmtupdate.setString(2, oldReasonID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					
					setWasteReasonID(newReasonID);
					
					result = true;
				}
				else
				{
					setErrorMessage(newReasonID + " already exists.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
