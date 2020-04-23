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

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBWasteLocationTypes class updates the table
 * APP_WASTE_LOCATION_TYPES. 
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_LOCATION_TYPES.jpg" >
 * 
 * @see com.commander4j.db.JDBWasteReasons JDBWasteReasons
 * @see com.commander4j.db.JDBWasteReportingIDS JDBWasteReportingIDS
 * @see com.commander4j.db.JDBWasteTransactionType JDBWasteTransactionType
 * @see com.commander4j.db.JDBWasteLocationTypes JDBWasteLocationReporting
 * @see com.commander4j.db.JDBWasteReasons JDBWasteTypes
 * @see com.commander4j.db.JDBWasteReasons JDBWasteMaterial
 * @see com.commander4j.db.JDBWasteReasons JDBWasteLog
 * 
 */

public class JDBWasteLocationTypes
{
	public static int field_LocationID = 25;
	public static int field_Type = 25;
	public static int field_Enabled = 1;
	
	private String dbErrorMessage;
	private String dbWasteLocationID;  /* PK */
	private String dbType;      /* PK */
	
	private final Logger logger = Logger.getLogger(JDBWasteLocationTypes.class);
	private String hostID;
	private String sessionID;

	public JDBWasteLocationTypes(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{

	}
	
	public boolean rewriteTypesAssignedToLocation(String locationID, LinkedList<String> types)
	{
		boolean result = false;
		String ltype;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationTypes.deleteByLocation"));
			stmtupdate.setString(1, locationID);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();

			for (int j = 0; j < types.size(); j++)
			{
				ltype = types.get(j).toString();
				create(locationID, ltype);
			}
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	
	public boolean create(String res1,String res2)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setWasteLocationID(res1);
			setWasteType(res2);
			clear();

			if (isValidWasteLocationType() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationTypes.create"));
				stmtupdate.setString(1, getWasteLocationID());
				stmtupdate.setString(2, getWasteType());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Waste Location / Type"  + getWasteLocationID() +"/"+getWasteType()+ " already exists");
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
			if (isValidWasteLocationType() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationTypes.delete"));
				stmtupdate.setString(1, getWasteLocationID());
				stmtupdate.setString(2, getWasteType());
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

	
	public String getWasteType()
	{
		return JUtility.replaceNullStringwithBlank(dbType);
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public ResultSet getWasteLocationTypeResultSet(PreparedStatement criteria)
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
			setWasteType(rs.getString("waste_type_id"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getWasteLocationID()
	{
		return JUtility.replaceNullStringwithBlank(dbWasteLocationID);
	}

	public boolean getWasteLocationTypeProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationTypes.getProperties"));
			stmt.setString(1, getWasteLocationID());
			stmt.setString(2, getWasteType());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Location/Type " + getWasteLocationID()+"/"+getWasteType());
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getWasteLocationTypeProperties(String loc,String rep)
	{
		setWasteLocationID(loc);
		setWasteType(rep);
		return getWasteLocationTypeProperties();
	}

	public LinkedList<JDBWasteLocationTypes> getWasteLocationTypes() {
		LinkedList<JDBWasteLocationTypes> sampList = new LinkedList<JDBWasteLocationTypes>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationTypes.getWasteLocationTypes"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteLocationTypes samp = new JDBWasteLocationTypes(getHostID(), getSessionID());
				samp.getPropertiesfromResultSet(rs);
				sampList.add(samp);
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

	public boolean isValidWasteLocationType()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationTypes.isValidWasteLocationType"));
			stmt.setFetchSize(1);
			stmt.setString(1, getWasteLocationID());
			stmt.setString(2, getWasteType());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Waste Location/Reporting ID " + getWasteLocationID() +"/"+getWasteType()+ " not found.");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidWasteLocationID(String res1,String res2)
	{
		setWasteLocationID(res1);
		setWasteType(res2);

		return isValidWasteLocationType();
	}

	public void setWasteType(String desc)
	{
		dbType = desc;
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
		return getWasteLocationID()+"-"+getWasteType();
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteLocationType() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationTypes.update"));
				stmtupdate.setString(1, getWasteLocationID());
				stmtupdate.setString(2, getWasteType());
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

}
