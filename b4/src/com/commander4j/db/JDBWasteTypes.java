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
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.commander4j.gui.JCheckListItem;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBWasteTypes class updates the table
 * APP_WASTE_TYPES. This table contains a single row for each unique
 * Waste Location as stored in the APP_WASTE_TYPES table. 
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_TYPES.jpg" >
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

public class JDBWasteTypes
{
	public static int field_WasteTypeID = 25;
	public static int field_Description = 80;
	public static int field_Recyclable = 1;
	public static int field_Hazardous = 1;
	public static int field_PPERequired = 1;
	public static int field_Enabled = 1;
	
	private String dbErrorMessage;
	private String dbWasteTypeID;  /* PK */
	private String dbDescription;
	private String dbRecyclable;
	private String dbHazardous;
	private String dbPPERequired;
	private String dbEnabled;
	
	private final Logger logger = Logger.getLogger(JDBWasteTypes.class);
	private String hostID;
	private String sessionID;
	private JDBWasteLocationTypes wlt;

	public JDBWasteTypes(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		wlt = new JDBWasteLocationTypes(getHostID(),getSessionID());
	}

	public void clear()
	{
		setDescription("");
		setRecylable("");
		setHazardous("");
		setPPERequired("");
		setEnabled(true);
	}
	
	public boolean create(String res)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setWasteTypeID(res);
			clear();

			if (isValidWasteType() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTypes.create"));
				stmtupdate.setString(1, getWasteTypeID());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getRecyclable());
				stmtupdate.setString(4, getHazardous());
				stmtupdate.setString(5, getPPERequired());
				stmtupdate.setString(6, getEnabled());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Waste Type " + getWasteTypeID() + " already exists");
			}
		} catch (SQLException e)
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
			if (isValidWasteType() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTypes.delete"));
				stmtupdate.setString(1, getWasteTypeID());
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

	public String getRecyclable()
	{
		return JUtility.replaceNullStringwithBlank(dbRecyclable).trim();
	}
	
	public String getHazardous()
	{
		return JUtility.replaceNullStringwithBlank(dbHazardous).trim();
	}

	public String getPPERequired()
	{
		return JUtility.replaceNullStringwithBlank(dbPPERequired).trim();
	}
	
	public String getRecyclable(String res)
	{
		String result = "";

		if (getWasteTypeProperties(res))
		{
			result = getRecyclable();
		}

		return result;
	}
	
	public String getHazardous(String res)
	{
		String result = "";

		if (getWasteTypeProperties(res))
		{
			result = getHazardous();
		}

		return result;
	}
	
	public String getPPERequired(String res)
	{
		String result = "";

		if (getWasteTypeProperties(res))
		{
			result = getPPERequired();
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
			setWasteTypeID(rs.getString("waste_type_id"));
			setDescription(rs.getString("description"));
			setRecylable(rs.getString("recyclable"));
			setHazardous(rs.getString("hazardous"));
			setPPERequired(rs.getString("ppe_required"));
			setEnabled(rs.getString("enabled"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getWasteTypeID()
	{
		return JUtility.replaceNullStringwithBlank(dbWasteTypeID);
	}

	public boolean getWasteTypeProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTypes.getProperties"));
			stmt.setString(1, getWasteTypeID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Waste Type [" + getWasteTypeID()+"]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getWasteTypeProperties(String res)
	{
		setWasteTypeID(res);
		return getWasteTypeProperties();
	}

	public ResultSet getWasteTypesDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTypes.getWasteTypes"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}
	
	public LinkedList<JDBListData> getWasteTypes(Boolean enabled) {
		
		LinkedList<JDBListData> wasteTypeList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTypes.getWasteTypes"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteTypes samp = new JDBWasteTypes(getHostID(), getSessionID());
				samp.getPropertiesfromResultSet(rs);
				icon = samp.getTypeIcon();
				
				
				if (samp.isEnabled().equals(enabled))
				{
				
					JDBListData mld = new JDBListData(icon, index, true, samp);
					
					wasteTypeList.addLast(mld);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteTypeList;
	}
	
	public LinkedList<JDBWasteTypes> getWasteTypesList() {
		
		LinkedList<JDBWasteTypes> wasteTypeList = new LinkedList<JDBWasteTypes>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTypes.getWasteTypes"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteTypes samp = new JDBWasteTypes(getHostID(), getSessionID());
				samp.getPropertiesfromResultSet(rs);

				wasteTypeList.addLast(samp);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteTypeList;
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

	public Boolean isRecyclable()
	{
		Boolean result = false;
		if (getRecyclable().equals("Y"))
		{
			result = true;
		} else
		{
			result = false;
		}
		return result;
	}
	
	public Boolean isHazardous()
	{
		Boolean result = false;
		if (getHazardous().equals("Y"))
		{
			result = true;
		} else
		{
			result = false;
		}
		return result;
	}
	
	public Boolean isPPEReqd()
	{
		Boolean result = false;
		if (getPPERequired().equals("Y"))
		{
			result = true;
		} else
		{
			result = false;
		}
		return result;
	}
	
	public boolean isValidWasteType()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTypes.isValidWasteType"));
			stmt.setFetchSize(1);
			stmt.setString(1, getWasteTypeID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Waste Type ID " + getWasteTypeID() + " not found.");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidWasteType(String res)
	{
		setWasteTypeID(res);

		return isValidWasteType();
	}

	public void setRecylable(String str)
	{
		dbRecyclable = str;
	}
	
	public void setHazardous(String str)
	{
		dbHazardous = str;
	}
	
	public void setPPERequired(String str)
	{
		dbPPERequired = str;
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
	
	public void setHazardous(boolean enabled)
	{
		if (enabled)
		{
			setHazardous("Y");
		} else
		{
			setHazardous("N");
		}
	}
	
	public void setRecylable(boolean enabled)
	{
		if (enabled)
		{
			setRecylable("Y");
		} else
		{
			setRecylable("N");
		}
	}
	
	public void setPPERequired(boolean enabled)
	{
		if (enabled)
		{
			setPPERequired("Y");
		} else
		{
			setPPERequired("N");
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

	public void setWasteTypeID(String res)
	{
		dbWasteTypeID = res;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";

		
		result = JUtility.padString(getWasteTypeID().toString(), true, field_WasteTypeID, " ")+	getDescription().toString();
		return result;
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteType() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTypes.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getRecyclable());
				stmtupdate.setString(3, getHazardous());
				stmtupdate.setString(4, getPPERequired());
				stmtupdate.setString(5, getEnabled());
				stmtupdate.setString(6, getWasteTypeID());
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
	
	public Vector<JCheckListItem> getTypesCheckListForLocation(String loc)
	{

		Vector<JCheckListItem> typeList = new Vector<JCheckListItem>();
		PreparedStatement stmt;
		ResultSet rs;
		boolean found = false;
		setErrorMessage("");
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTypes.getWasteTypes"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteTypes mt = new JDBWasteTypes(getHostID(), getSessionID());
				mt.getPropertiesfromResultSet(rs);
				if (mt.isEnabled())
				{
					if (wlt.getWasteLocationTypeProperties(loc, mt.getWasteTypeID()))
					{
						found = true;
					}
					else
					{
						found = false;
					}
						
					JCheckListItem ci = new JCheckListItem(mt);
					ci.setSelected(found);
					typeList.add(ci);

				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return typeList;
	}
	
	public boolean rename(String oldTypeID,String newTypeID)
	{
		boolean result = false;
		setWasteTypeID(oldTypeID);
		setErrorMessage("");

		try
		{
			if (isValidWasteType(oldTypeID) == true)
			{
				if (isValidWasteType(newTypeID) == false)
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTypes.renameTypeID"));
					stmtupdate.setString(1, newTypeID);
					stmtupdate.setString(2, oldTypeID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterial.renameTypeID"));
					stmtupdate.setString(1, newTypeID);
					stmtupdate.setString(2, oldTypeID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					
					setWasteTypeID(newTypeID);
					
					result = true;
				}
				else
				{
					setErrorMessage(newTypeID + " already exists.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
