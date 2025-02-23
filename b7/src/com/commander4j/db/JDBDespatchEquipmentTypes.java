package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBDespatchEquipmentTypes.java
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

public class JDBDespatchEquipmentTypes
{
	public static int field_Despatch = 18;
	public static int field_EquipmentType = 15;
	public static int field_Quantity = 5;

	public static int displayModeFull = 0;
	public static int displayModeShort = 1;

	private String dbErrorMessage;
	private String dbEquipmentType; /* PK */
	private String dbDespatchNo;
	private String dbEnabled;
	private int dbQuantity;

	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBDespatchEquipmentTypes.class);
	private String hostID;
	private String sessionID;
	private Integer displayMode = displayModeFull;

	public JDBDespatchEquipmentTypes(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setDespatchNo("");
		setEnabled(true);
		setQuantity(0);
	}

	public void setDisplayMode(int mode)
	{
		displayMode = mode;
	}

	public boolean create()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			if (isValidDespatchEquipmentType() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDespatchEquipmentType.create"));
				stmtupdate.setString(1, getDespatchNo());
				stmtupdate.setString(2, getEquipmentType());
				stmtupdate.setInt(3, getQuantity());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Despetch " + getDespatchNo() + " Equipment Type " + getEquipmentType() + " already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public Icon getEquipmentTypeIcon()
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
			if (isValidDespatchEquipmentType() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDespatchEquipmentType.delete"));
				stmtupdate.setString(1, getDespatchNo());
				stmtupdate.setString(2, getEquipmentType());
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

	public boolean deleteAll()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDespatchEquipmentType.deleteAll"));
			stmtupdate.setString(1, getDespatchNo());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public int getQuantity()
	{
		return dbQuantity;
	}

	public String getDespatchNo()
	{
		return JUtility.replaceNullStringwithBlank(dbDespatchNo);
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

	public ResultSet getDespatchEquipmentResultSet(PreparedStatement criteria)
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
			setDespatchNo(rs.getString("despatch_no"));
			setEquipmentType(rs.getString("equipment_type"));
			setQuantity(rs.getInt("quantity"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getEquipmentType()
	{
		return JUtility.replaceNullStringwithBlank(dbEquipmentType).toUpperCase();
	}

	public boolean getDespatchEquipmentTypeProperties(String desp, String equip)
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();
		setDespatchNo(desp);
		setEquipmentType(equip);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDespatchEquipmentType.getProperties"));
			stmt.setString(1, getDespatchNo());
			stmt.setString(2, getEquipmentType());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Equipment Type [" + getEquipmentType() + "]");
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

	public ResultSet getDespatchEquipmentTypeDataResultSet(String desp)
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDespatchEquipmentType.getEquipmentTypes"));
			stmt.setString(1, desp);
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public LinkedList<JDBDespatchEquipmentTypes> getDespatchEquipmentTypeList(String desp, int mode)
	{

		LinkedList<JDBDespatchEquipmentTypes> equipList = new LinkedList<JDBDespatchEquipmentTypes>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDespatchEquipmentType.getEquipmentTypes"));
			stmt.setString(1, desp);
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBDespatchEquipmentTypes samp = new JDBDespatchEquipmentTypes(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);

				equipList.addLast(samp);

			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return equipList;
	}

	public LinkedList<JDBListData> getDespatchEquipmentTypes(String desp, int mode)
	{

		LinkedList<JDBListData> wasteContainerList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDespatchEquipmentType.getEquipmentTypes"));
			stmt.setString(1, desp);
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBDespatchEquipmentTypes samp = new JDBDespatchEquipmentTypes(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				icon = samp.getEquipmentTypeIcon();

				JDBListData mld = new JDBListData(icon, index, true, samp);

				wasteContainerList.addLast(mld);

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
		}
		else
		{
			result = false;
		}
		return result;
	}

	public boolean isValidDespatchEquipmentType()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDespatchEquipmentType.isValidEquipmentType"));
			stmt.setFetchSize(1);
			stmt.setString(1, getDespatchNo());
			stmt.setString(2, getEquipmentType());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Equipment ID " + getEquipmentType() + " not found.");
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

	public boolean isValidDespatchEquipment(String res)
	{
		setEquipmentType(res);

		return isValidDespatchEquipmentType();
	}

	public void setQuantity(int weight)
	{
		dbQuantity = weight;
	}

	public void setDespatchNo(String desc)
	{
		dbDespatchNo = desc;
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

	public void setEquipmentType(String res)
	{
		dbEquipmentType = JUtility.replaceNullStringwithBlank(res).toUpperCase();
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";

		if (displayMode == displayModeFull)
		{
			result = JUtility.padString(getEquipmentType().toString(), true, field_EquipmentType, " ") + JUtility.padString(String.valueOf(getQuantity()), false, JDBDespatchEquipmentTypes.field_Quantity, " ");
		}
		if (displayMode == displayModeShort)
		{
			result = getEquipmentType().toString();
		}

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidDespatchEquipmentType() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDespatchEquipmentType.update"));
				stmtupdate.setInt(1, getQuantity());
				stmtupdate.setString(2, getDespatchNo());
				stmtupdate.setString(3, getEquipmentType());
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
	
	public LinkedList<JDBEquipmentList> getEquipment()
	{
		LinkedList<JDBEquipmentList> result = new LinkedList<JDBEquipmentList>();
		PreparedStatement stmt = null;
		ResultSet rs;
		String temp = "";

		try
		{
			temp = Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBDespatchEquipmentType.getEquipmentTypes");

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(temp);
			stmt.setFetchSize(25);
			stmt.setString(1, getDespatchNo());

			rs = stmt.executeQuery();
			result.clear();

			while (rs.next())
			{
				result.addLast(new JDBEquipmentList(rs.getString("equipment_type"), rs.getInt("quantity")));
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

}
