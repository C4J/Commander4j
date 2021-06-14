package com.commander4j.db;

import java.math.BigDecimal;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWasteContainers.java
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
 * The JDBWasteContainer class updates the table
 * APP_WASTE_CONTAINER. This table contains a single row for each unique
 * Waste Container as stored in the APP_WASTE_CONTAINER table. 
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_CONTAINER.jpg" >
 * 
 * 
 */

public class JDBWasteContainer
{
	public static int field_WasteContainerID = 25;
	public static int field_Description = 80;
	public static int field_Enabled = 1;
	
	public static int displayModeFull = 0;
	public static int displayModeShort = 1;
	
	private String 		dbErrorMessage;
	private String 		dbWasteContainerID;  /* PK */
	private String 		dbDescription;
	private String 		dbEnabled;
	private BigDecimal 	dbTareWeight;
	
	private final Logger logger = Logger.getLogger(JDBWasteContainer.class);
	private String hostID;
	private String sessionID;
	private Integer displayMode=displayModeFull;

	public JDBWasteContainer(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setDescription("");
		setEnabled(true);
		setTareWeight(new BigDecimal("0.000"));
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
			setWasteContainerID(res);
			clear();


			if (isValidWasteContainer() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteContainer.create"));
				stmtupdate.setString(1, getWasteContainerID());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setBigDecimal(3, getTareWeight());
				stmtupdate.setString(4, getEnabled());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Waste Container " + getWasteContainerID() + " already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public Icon getContainerIcon()
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
			if (isValidWasteContainer() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteContainer.delete"));
				stmtupdate.setString(1, getWasteContainerID());
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
	
	public BigDecimal getTareWeight()
	{
		return dbTareWeight;
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

	public ResultSet getWasteContainerResultSet(PreparedStatement criteria)
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
			setWasteContainerID(rs.getString("waste_container_id"));
			setDescription(rs.getString("description"));
			setEnabled(rs.getString("enabled"));
			setTareWeight(rs.getBigDecimal("tare_weight"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getWasteContainerID()
	{
		return JUtility.replaceNullStringwithBlank(dbWasteContainerID);
	}

	public boolean getWasteContainerProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteContainer.getProperties"));
			stmt.setString(1, getWasteContainerID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Waste Container [" + getWasteContainerID()+"]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getWasteContainerProperties(String res)
	{
		setWasteContainerID(res);
		return getWasteContainerProperties();
	}

	
	public ResultSet getWasteContainersDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteContainer.getWasteContainers"));
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
		LinkedList<JDBWasteContainer> containerList = new LinkedList<JDBWasteContainer>();
				
		containerList.addAll(getWasteContainerList(true,displayModeShort));
		result = "<SELECT width=\"100%\" style=\"width: 100%\" ID=\"" + itemName + "\" NAME=\"" + itemName + "\">";
		result = result + "<OPTION></OPTION>";
		
		if (containerList.size() > 0)
		{
			for (int x = 0; x < containerList.size(); x++)
			{
				if (containerList.get(x).getWasteContainerID().equals(defaultValue))
				{
					selected = " SELECTED";
				} else
				{
					selected = "";
				}
				result = result + "<OPTION" + selected + ">" + containerList.get(x).getWasteContainerID()+"</OPTION>";
			}
		}
		result = result + "</SELECT>";

		return result;
	}
	
	public LinkedList<JDBWasteContainer> getWasteContainerList(Boolean enabled,int mode) {
		
		LinkedList<JDBWasteContainer> wasteContainerList = new LinkedList<JDBWasteContainer>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteContainer.getWasteContainers"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteContainer samp = new JDBWasteContainer(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				
				if (samp.isEnabled().equals(enabled))
				{	
					wasteContainerList.addLast(samp);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteContainerList;
	}
	
	public LinkedList<JDBListData> getWasteContainers(Boolean enabled,int mode) {
		
		LinkedList<JDBListData> wasteContainerList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteContainer.getWasteContainers"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteContainer samp = new JDBWasteContainer(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				icon = samp.getContainerIcon();
				
				if (samp.isEnabled().equals(enabled))
				{
				
					JDBListData mld = new JDBListData(icon, index, true, samp);
				
					wasteContainerList.addLast(mld);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteContainerList;
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
	
	
	public boolean isValidWasteContainer()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteContainer.isValidWasteContainer"));
			stmt.setFetchSize(1);
			stmt.setString(1, getWasteContainerID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Waste Container ID " + getWasteContainerID() + " not found.");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidContainer(String res)
	{
		setWasteContainerID(res);

		return isValidWasteContainer();
	}
	
	public void setTareWeight(BigDecimal tareWeight)
	{
		dbTareWeight = tareWeight;
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

	public void setWasteContainerID(String res)
	{
		dbWasteContainerID = res;
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
			result = JUtility.padString(getWasteContainerID().toString(), true, field_WasteContainerID, " ")+	getDescription().trim(); 
		}
		if (displayMode==displayModeShort)
		{
			result = getWasteContainerID().toString();
		}

		return result;
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteContainer() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteContainer.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setBigDecimal(2, getTareWeight());
				stmtupdate.setString(3, getEnabled());
				stmtupdate.setString(4, getWasteContainerID());
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
	
	public boolean rename(String oldContainerID,String newContainerID)
	{
		boolean result = false;
		setWasteContainerID(oldContainerID);
		setErrorMessage("");

		try
		{
			if (isValidContainer(oldContainerID) == true)
			{
				if (isValidContainer(newContainerID) == false)
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteContainer.renameContainerID"));
					stmtupdate.setString(1, newContainerID);
					stmtupdate.setString(2, oldContainerID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.renameContainerID"));
					stmtupdate.setString(1, newContainerID);
					stmtupdate.setString(2, oldContainerID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					
					setWasteContainerID(newContainerID);
					
					result = true;
				}
				else
				{
					setErrorMessage(newContainerID + " already exists.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


}
