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
 * The JDBWasteLocationReporting class updates the table
 * APP_WASTE_LOCATION_REPORTING. This table contains a single row for each unique
 * Waste Transaction Type as stored in the APP_WASTE_LOCATION_REPORTING table. 
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_LOCATION_REPORTING.jpg" >
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

public class JDBWasteLocationReporting
{
	public static int field_LocationID = 25;
	public static int field_ReportingID = 25;
	public static int field_Enabled = 1;
	
	private String dbErrorMessage;
	private String dbWasteLocationID;  /* PK */
	private String dbReportingID;      /* PK */
	
	private final Logger logger = Logger.getLogger(JDBWasteLocationReporting.class);
	private String hostID;
	private String sessionID;

	public JDBWasteLocationReporting(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{

	}
	
	public boolean rewriteReportIDSAssignedToLocation(String locationID, LinkedList<JDBListData> reportingIDs)
	{
		boolean result = false;
		String lreportingId;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationReporting.deleteByLocation"));
			stmtupdate.setString(1, locationID);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();

			for (int j = 0; j < reportingIDs.size(); j++)
			{
				lreportingId = reportingIDs.get(j).toString();
				create(locationID, lreportingId);
			}
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public boolean rewriteLocationsAssignedToReportID(String reportID, LinkedList<JDBListData> locations)
	{
		boolean result = false;
		String llocation;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationReporting.deleteByReportID"));
			stmtupdate.setString(1, reportID);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();

			for (int j = 0; j < locations.size(); j++)
			{
				llocation = locations.get(j).toString();
				create(llocation,reportID);
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
			setWasteReportingID(res2);
			clear();

			if (isValidWasteLocationReportingID() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationReporting.create"));
				stmtupdate.setString(1, getWasteLocationID());
				stmtupdate.setString(2, getWasteReportingID());;
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Waste Location / Reporting ID"  + getWasteLocationID() +"/"+getWasteReportingID()+ " already exists");
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
			if (isValidWasteLocationReportingID() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationReporting.delete"));
				stmtupdate.setString(1, getWasteLocationID());
				stmtupdate.setString(2, getWasteReportingID());
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

	
	public String getWasteReportingID()
	{
		return JUtility.replaceNullStringwithBlank(dbReportingID);
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public ResultSet getWasteLocationReportingIDResultSet(PreparedStatement criteria)
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
			setWasteReportingID(rs.getString("waste_reporting_id"));

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getWasteLocationID()
	{
		return JUtility.replaceNullStringwithBlank(dbWasteLocationID);
	}

	public boolean getWasteLocationReportingIDProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationReporting.getProperties"));
			stmt.setString(1, getWasteLocationID());
			stmt.setString(2, getWasteReportingID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Location/Reporting ID " + getWasteLocationID());
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getWasteLocationReportingIDProperties(String loc,String rep)
	{
		setWasteLocationID(loc);
		setWasteReportingID(rep);
		return getWasteLocationReportingIDProperties();
	}

	public LinkedList<JDBWasteLocationReporting> getWasteLocationReportingIDs() {
		LinkedList<JDBWasteLocationReporting> sampList = new LinkedList<JDBWasteLocationReporting>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationReporting.getWasteLocationReportingIDs"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteLocationReporting samp = new JDBWasteLocationReporting(getHostID(), getSessionID());
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

	public boolean isValidWasteLocationReportingID()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationReporting.isValidWasteLocationReportingID"));
			stmt.setFetchSize(1);
			stmt.setString(1, getWasteLocationID());
			stmt.setString(2, getWasteReportingID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Waste Location/Reporting ID " + getWasteLocationID() +"/"+getWasteReportingID()+ " not found.");
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
		setWasteReportingID(res2);

		return isValidWasteLocationReportingID();
	}

	public void setWasteReportingID(String desc)
	{
		dbReportingID = desc;
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
		return getWasteLocationID()+"-"+getWasteReportingID();
	}
	

}
