package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBJourney.java
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
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * JDBJourney is a table associated with the despatch transaction. When
 * Dispatching pallets to a location it is an option to capture an external
 * tracking reference (Journey Ref). The option to determine the requirement of
 * Journey Ref is determined by a property of the receiving location. See
 * JDBLocation class and APP_LOCATION table. The Journey Refs which can be used
 * as either inserted manually using the appropriate screen within the
 * application or read in from a XML message using the application interfacing
 * module.
 * <p>
 * <img alt="" src="./doc-files/APP_JOURNEY.jpg" >
 * 
 * @see com.commander4j.db.JDBDespatch JDBDespatch
 * @see com.commander4j.app.JInternalFrameJourneyAdmin JInternalFrameJourneyAdmin
 * @see com.commander4j.app.JInternalFrameJourneyProperties JInternalFrameJourneyProperties
 */
public class JDBJourney
{
	public static int field_journey_ref = 25;
	public static int field_despatch_no = 18;
	public static int field_status = 20;
	private String dbErrorMessage;
	private String dbJourneyRef;
	private String dbStatus;
	private String dbDespatchNo;
	private String dbLocationTo;
	private String dbloadType = "";
	private String dbloadTypeDesc = "";
	private String dbhaulier = "";
	private Timestamp dbDateUpdated;
	private Timestamp dbTimeslot;
	private final Logger logger = Logger.getLogger(JDBJourney.class);
	private String hostID;
	private String sessionID;
	private JDBDespatch desp;
	private JDBLocation loc;

	public String getLoadType()
	{
		return JUtility.replaceNullStringwithBlank(dbloadType);
	}

	public String getLoadTypeDesc()
	{
		return JUtility.replaceNullStringwithBlank(dbloadTypeDesc);
	}

	public String getHaulier()
	{
		return JUtility.replaceNullStringwithBlank(dbhaulier);
	}

	public void setLoadType(String type)
	{
		dbloadType = type;
	}

	public void setLoadTypeDesc(String desc)
	{
		dbloadTypeDesc = desc;
	}

	public void setHaulier(String haulier)
	{
		dbhaulier = haulier;
	}

	public JDBJourney(ResultSet rs)
	{
		getPropertiesfromResultSet(rs);
	}

	public JDBJourney(String host, String session)
	{

		setHostID(host);
		setSessionID(session);

	}

	public JDBJourney(String journey, String status, String desp, Timestamp updated, String loadType, String loadTypeDesc, String haulier)
	{
		setJourneyRef(journey);
		setStatus(status);
		setDespatchNo(desp);
		setDateUpdated(updated);
		setLoadType(loadType);
		setLoadTypeDesc(loadTypeDesc);
		setHaulier(haulier);
	}

	public void clear()
	{
		setDespatchNo(null);
		setStatus("");
		setDateUpdated(null);
		setLoadType("");
		setLoadTypeDesc("");
		setHaulier("");
	}

	public boolean create(String journey, String status)
	{
		setJourneyRef(journey);
		setStatus(status);
		return create();
	}

	public boolean create()
	{

		logger.debug("create [" + getJourneyRef() + "][" + getStatus() + "]");
		setDateUpdated(JUtility.getSQLDateTime());
		boolean result = false;

		if (isValid() == true)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBJourney.create"));
				stmtupdate.setString(1, getJourneyRef());
				stmtupdate.setString(2, getStatus());
				stmtupdate.setTimestamp(3, JUtility.getSQLDateTime());
				stmtupdate.setString(4, getLocationTo());
				stmtupdate.setTimestamp(5, getTimeslot());
				stmtupdate.setString(6, getLoadType());
				stmtupdate.setString(7, getLoadTypeDesc());
				stmtupdate.setString(8, getHaulier());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				update();
				result = true;
			} catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
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
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBJourney.delete"));
			stmtupdate.setString(1, getJourneyRef());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String journey)
	{
		boolean result = false;
		setJourneyRef(journey);
		result = delete();
		return result;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getDespatchNo()
	{
		return JUtility.replaceNullStringwithBlank(dbDespatchNo);
	}

	public Timestamp getDateUpdated()
	{
		return dbDateUpdated;
	}

	public Timestamp getTimeslot()
	{

		return dbTimeslot;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getJourneyRef()
	{
		return dbJourneyRef.toUpperCase();
	}

	public String getLocationTo()
	{
		return dbLocationTo;
	}

	public LinkedList<JDBJourney> getJourneyList(String defaultItem)
	{
		LinkedList<JDBJourney> journeyList = new LinkedList<JDBJourney>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		String test = JUtility.replaceNullStringwithBlank(defaultItem);

		JDBJourney journey1 = new JDBJourney(getHostID(), getSessionID());
		journey1.setJourneyRef(test);
		journeyList.add(journey1);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBJourney.getJourneyList"));

			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBJourney journey2 = new JDBJourney(getHostID(), getSessionID());
				journey2.getPropertiesfromResultSet(rs);
				journeyList.add(journey2);
			}

			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return journeyList;
	}

	public Vector<JDBJourney> getJourneyRefData(PreparedStatement criteria)
	{
		ResultSet rs;
		Vector<JDBJourney> result = new Vector<JDBJourney>();

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBJourney("journey", "status", "desp", JUtility.getSQLDateTime(), "loadType", "loadTypeDesc", "haulier"));
		} else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					result.addElement(
							new JDBJourney(rs.getString("journey_ref"), rs.getString("status"), rs.getString("despatch_no"), rs.getTimestamp("date_updated"), rs.getString("load_type"), rs.getString("load_type_desc"), rs.getString("haulier")));
				}

				rs.close();
			} catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public ResultSet getJourneyRefDataResultSet(PreparedStatement criteria)
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

	public boolean getJourneyRefProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBJourney.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getJourneyRef());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Unknown JourneyRef [" + getJourneyRef() + "]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getJourneyRefProperties(String journey)
	{
		setJourneyRef(journey);

		return getJourneyRefProperties();
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setJourneyRef(rs.getString("Journey_Ref"));
			setStatus(rs.getString("status"));
			setDespatchNo(rs.getString("despatch_no"));
			setDateUpdated(rs.getTimestamp("date_updated"));
			setTimeslot(rs.getTimestamp("timeslot"));
			setLocationTo(rs.getString("location_id_to"));
			setLoadType(rs.getString("load_type"));
			setLoadTypeDesc(rs.getString("load_type_desc"));
			setHaulier(rs.getString("haulier"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getStatus()
	{
		return JUtility.replaceNullStringwithBlank(dbStatus);
	}

	public boolean isValid()
	{
		boolean result = true;

		if (JUtility.isNullORBlank(getJourneyRef()) == true)
		{
			setErrorMessage("JourneyRef cannot be null");
			result = false;
		}

		if (result == true)
		{
			if (JUtility.isNullORBlank(getStatus()) == true)
			{
				setErrorMessage("Status cannot be null");
				result = false;
			}
		}

		if (result == true)
		{
			if (JUtility.isNullORBlank(getDespatchNo()) == false)
			{
				desp = new JDBDespatch(getHostID(), getSessionID());
				if (desp.isValidDespatchNo(getDespatchNo()) == false)
				{
					setErrorMessage("Invalid Despatch No");
					result = false;
				}
			}
		}

		if (result == true)
		{
			if (JUtility.isNullORBlank(getLocationTo()) == false)
			{
				loc = new JDBLocation(getHostID(), getSessionID());
				if (loc.isValidLocation(getLocationTo()) == false)
				{
					setErrorMessage("Invalid Location ID");
					result = false;
				}
			}
		}

		return result;
	}

	public boolean isValidJourneyRef()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBJourney.isValid"));
			stmt.setString(1, getJourneyRef());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid JourneyRef");
			}

			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidJourneyRef(String journey)
	{
		setJourneyRef(journey);

		return isValidJourneyRef();
	}

	private void setErrorMessage(String errorMsg)
	{
		dbErrorMessage = errorMsg;
	}

	public void setDespatchNo(String despNo)
	{
		dbDespatchNo = JUtility.replaceNullStringwithBlank(despNo);
		if (dbDespatchNo.equals("") == true)
		{
			dbStatus = "Unassigned";
		} else
		{
			dbStatus = "Assigned";
		}

	}

	public void setDateUpdated(Timestamp updated)
	{
		dbDateUpdated = updated;
	}

	public void setTimeslot(Timestamp timeslot)
	{
		dbTimeslot = timeslot;
	}

	public void setLocationTo(String location)
	{
		dbLocationTo = location;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setJourneyRef(String journey)
	{
		dbJourneyRef = journey.toUpperCase();
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setStatus(String status)
	{
		dbStatus = status;
	}

	public boolean update()
	{
		boolean result = false;
		setDateUpdated(JUtility.getSQLDateTime());
		if (isValid() == true)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBJourney.update"));

				stmtupdate.setString(1, getStatus());
				stmtupdate.setString(2, getDespatchNo());
				stmtupdate.setTimestamp(3, JUtility.getSQLDateTime());
				stmtupdate.setString(4, getLocationTo());
				stmtupdate.setTimestamp(5, getTimeslot());
				stmtupdate.setString(6, getLoadType());
				stmtupdate.setString(7, getLoadTypeDesc());
				stmtupdate.setString(8, getHaulier());
				stmtupdate.setString(9, getJourneyRef());
				stmtupdate.execute();
				stmtupdate.clearParameters();

				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}
}
