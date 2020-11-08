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
 * The JDBWasteLocation class updates the table
 * APP_WASTE_LOCATION. This table contains a single row for each unique
 * Waste Location as stored in the APP_WASTE_LOCATION table. 
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_LOCATION.jpg" >
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

public class JDBWasteLocation
{
	public static int field_WasteLocationID = 25;
	public static int field_Description = 80;
	public static int field_Process_Order_Required = 1;
	public static int field_Reason_ID_Required = 1;
	public static int field_Enabled = 1;
	
	public static int displayModeFull = 0;
	public static int displayModeShort = 1;
	
	private String dbErrorMessage;
	private String dbWasteLocationID;  /* PK */
	private String dbDescription;
	private String dbProcessOrderRequired;
	private String dbReasonIDRequired;
	private String dbEnabled;
	
	private final Logger logger = Logger.getLogger(JDBWasteLocation.class);
	private String hostID;
	private String sessionID;
	private Integer displayMode=displayModeFull;

	public JDBWasteLocation(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setDescription("");
		setProcessOrderRequired("");
		setReasonIDRequired("");
		setEnabled(true);
	}
	
	public void setDisplayMode(int mode)
	{
		displayMode = mode;
	}
	
	public LinkedList<JDBListData> getWasteLocationsAssignedtoReportingID(String locn) {
		
		LinkedList<JDBListData> sampList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.getLocationsAssignedtoReportingID"));
			stmt.setFetchSize(250);
			stmt.setString(1, locn);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteLocation samp = new JDBWasteLocation(getHostID(), getSessionID());
				samp.setDisplayMode(displayModeShort);
				samp.getPropertiesfromResultSet(rs);
				icon = samp.getLocationIcon();
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
	
	public LinkedList<JDBListData> getLocationsUnassignedtoReportidID(String locn) {
		
		LinkedList<JDBListData> sampList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.getLocationsUnAssignedtoReportingID"));
			stmt.setFetchSize(250);
			stmt.setString(1, locn);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteLocation samp = new JDBWasteLocation(getHostID(), getSessionID());
				samp.setDisplayMode(displayModeShort);
				samp.getPropertiesfromResultSet(rs);
				icon = samp.getLocationIcon();
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
	
	public boolean create(String res)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setWasteLocationID(res);
			clear();


			if (isValidWasteLocation() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.create"));
				stmtupdate.setString(1, getWasteLocationID());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getProcessOrderRequired());
				stmtupdate.setString(4, getReasonIDRequired());
				stmtupdate.setString(5, getEnabled());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Waste Location " + getWasteLocationID() + " already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public Icon getLocationIcon()
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
			if (isValidWasteLocation() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.delete"));
				stmtupdate.setString(1, getWasteLocationID());
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

	public String getProcessOrderRequired()
	{
		return JUtility.replaceNullStringwithBlank(dbProcessOrderRequired).trim();
	}
	
	public String getReasonIDRequired()
	{
		return JUtility.replaceNullStringwithBlank(dbReasonIDRequired).trim();
	}

	public String getProcessOrderRequired(String res)
	{
		String result = "";

		if (getWasteLocationProperties(res))
		{
			result = getProcessOrderRequired();
		}

		return result;
	}
	
	public String getReasonIDRequired(String res)
	{
		String result = "";

		if (getWasteLocationProperties(res))
		{
			result = getReasonIDRequired();
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

	public ResultSet getWasteLocationResultSet(PreparedStatement criteria)
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
			setWasteLocationID(rs.getString("waste_location_id"));
			setDescription(rs.getString("description"));
			setProcessOrderRequired(rs.getString("process_order_required"));
			setReasonIDRequired(rs.getString("reason_id_required"));
			setEnabled(rs.getString("enabled"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getWasteLocationID()
	{
		return JUtility.replaceNullStringwithBlank(dbWasteLocationID);
	}

	public boolean getWasteLocationProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.getProperties"));
			stmt.setString(1, getWasteLocationID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Waste Location [" + getWasteLocationID()+"]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getWasteLocationProperties(String res)
	{
		setWasteLocationID(res);
		return getWasteLocationProperties();
	}

	
	public ResultSet getWasteLocationsDataResultSet()
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
	
	public String getHTMLPullDownCombo(String itemName, String defaultValue)
	{
		String result = "";
		String selected = "";
		LinkedList<JDBWasteLocation> locationList = new LinkedList<JDBWasteLocation>();
				
		locationList.addAll(getWasteLocationsList(true,displayModeShort));
		result = "<SELECT width=\"100%\" style=\"width: 100%\" ID=\"" + itemName + "\" NAME=\"" + itemName + "\">";
		result = result + "<OPTION></OPTION>";
		
		if (locationList.size() > 0)
		{
			for (int x = 0; x < locationList.size(); x++)
			{
				if (locationList.get(x).getWasteLocationID().equals(defaultValue))
				{
					selected = " SELECTED";
				} else
				{
					selected = "";
				}
				result = result + "<OPTION" + selected + ">" + locationList.get(x).getWasteLocationID()+"</OPTION>";
			}
		}
		result = result + "</SELECT>";

		return result;
	}
	
	public LinkedList<JDBWasteLocation> getWasteLocationsList(Boolean enabled,int mode) {
		
		LinkedList<JDBWasteLocation> wasteLocationList = new LinkedList<JDBWasteLocation>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.getWasteLocations"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteLocation samp = new JDBWasteLocation(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				
				if (samp.isEnabled().equals(enabled))
				{	
					wasteLocationList.addLast(samp);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteLocationList;
	}
	
	public LinkedList<JDBListData> getWasteLocations(Boolean enabled,int mode) {
		
		LinkedList<JDBListData> wasteLocationList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.getWasteLocations"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteLocation samp = new JDBWasteLocation(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				icon = samp.getLocationIcon();
				
				if (samp.isEnabled().equals(enabled))
				{
				
					JDBListData mld = new JDBListData(icon, index, true, samp);
				
					wasteLocationList.addLast(mld);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteLocationList;
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
	
	public Boolean isProcessOrderRequired()
	{
		Boolean result = false;
		if (getProcessOrderRequired().equals("Y"))
		{
			result = true;
		} else
		{
			result = false;
		}
		return result;
	}

	public Boolean isReasonIDRequired()
	{
		Boolean result = false;
		if (getReasonIDRequired().equals("Y"))
		{
			result = true;
		} else
		{
			result = false;
		}
		return result;
	}
	
	public boolean isValidWasteLocation()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.isValidWasteLocation"));
			stmt.setFetchSize(1);
			stmt.setString(1, getWasteLocationID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Waste Location ID " + getWasteLocationID() + " not found.");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidWasteLocation(String res)
	{
		setWasteLocationID(res);

		return isValidWasteLocation();
	}

	public void setProcessOrderRequired(String str)
	{
		dbProcessOrderRequired = str;
	}
	
	public void setReasonIDRequired(String str)
	{
		dbReasonIDRequired = str;
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
	
	public void setProcessOrderRequired(boolean enabled)
	{
		if (enabled)
		{
			setProcessOrderRequired("Y");
		} else
		{
			setProcessOrderRequired("N");
		}
	}
	
	public void setReasonIDRequired(boolean enabled)
	{
		if (enabled)
		{
			setReasonIDRequired("Y");
		} else
		{
			setReasonIDRequired("N");
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

	public void setWasteLocationID(String res)
	{
		dbWasteLocationID = res;
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
			result = JUtility.padString(getWasteLocationID().toString(), true, field_WasteLocationID, " ")+	getDescription().trim(); 
		}
		if (displayMode==displayModeShort)
		{
			result = getWasteLocationID().toString();
		}

		return result;
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteLocation() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getProcessOrderRequired());
				stmtupdate.setString(3, getReasonIDRequired());
				stmtupdate.setString(4, getEnabled());
				stmtupdate.setString(5, getWasteLocationID());
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
	
	public boolean rename(String oldLocationID,String newLocationID)
	{
		boolean result = false;
		setWasteLocationID(oldLocationID);
		setErrorMessage("");

		try
		{
			if (isValidWasteLocation(oldLocationID) == true)
			{
				if (isValidWasteLocation(newLocationID) == false)
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocation.renameLocationID"));
					stmtupdate.setString(1, newLocationID);
					stmtupdate.setString(2, oldLocationID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationReporting.renameLocationID"));
					stmtupdate.setString(1, newLocationID);
					stmtupdate.setString(2, oldLocationID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.renameLocationID"));
					stmtupdate.setString(1, newLocationID);
					stmtupdate.setString(2, oldLocationID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationTypes.renameLocationID"));
					stmtupdate.setString(1, newLocationID);
					stmtupdate.setString(2, oldLocationID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					setWasteLocationID(newLocationID);
					
					result = true;
				}
				else
				{
					setErrorMessage(newLocationID + " already exists.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


}
