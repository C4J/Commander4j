package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMInspection.java
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

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBQMInspection class is part of a structure within the database which
 * represents the SAP Inspection Lot hierarchy. This is the top level. Each
 * Inspection has one or more activities associated with it.. The data is stored
 * in a table called APP_QM_INSPECTION
 *
 * <p>
 * <img alt="" src="./doc-files/APP_QM_INSPECTION.jpg" >
 * 
 * @see com.commander4j.db.JDBQMActivity JDBQMActivity
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary
 */
public class JDBQMInspection
{
	private Long dbExtensionID;
	private String dbDescription;
	private String dbInspectionID;
	private String dbErrorMessage;
	public static int field_inspection_id = 20;
	public static int field_description = 50;
	private final Logger logger = Logger.getLogger(JDBQMInspection.class);
	private String hostID;
	private String sessionID;
	private JDBQMExtension extension;

	public String toString()
	{
		String result = "";
		if (getInspectionID().equals("") == false)
		{
			result = JUtility.padString(getInspectionID(), true, field_inspection_id, " ") + " - " + getDescription();
		} else
		{
			result = "";
		}

		return result;
	}

	public JDBQMInspection(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		extension = new JDBQMExtension(host, session);
	}

	public JDBQMInspection(String host, String session, String inpectionid, String description, Long extensionid)
	{
		setHostID(host);
		setSessionID(session);
		setInspectionID(inpectionid);
		setDescription(description);
		setExtensionID(extensionid);
	}

	public void clear()
	{
		setDescription("");
		setExtensionID((long) -1);
	}

	public boolean create(String inspectionid, String description)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			setInspectionID(inspectionid);
			setDescription(description);

			if (isValid() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMInspection.create"));
				stmtupdate.setString(1, getInspectionID());
				stmtupdate.setString(2, getDescription());
				setExtensionID(extension.generateExtensionID());
				stmtupdate.setLong(3, getExtensionID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("QMInspection item already exists");
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
			if (isValid() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMInspection.delete"));
				stmtupdate.setString(1, getInspectionID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getProperties(String inspectionID)
	{
		setInspectionID(inspectionID);
		return getProperties();
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getDescription()
	{
		String result = "";
		if (dbDescription != null)
			result = dbDescription;
		return result;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getInspectionID()
	{
		String result = "";
		if (dbInspectionID != null)
			result = dbInspectionID;
		return result;
	}

	public boolean getProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("JDBQMInspection getProperties Inspection [" + getInspectionID() + "]");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMInspection.getProperties"));
			stmt.setString(1, getInspectionID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDescription(rs.getString("description"));
				setExtensionID(rs.getLong("extension_id"));
				result = true;
				rs.close();
				stmt.close();
			} else
			{
				setErrorMessage("Invalid Inspection ID [" + getInspectionID() + "]");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public Long getExtensionID()
	{
		Long result = (long) -1;
		if (dbExtensionID != null)
			result = dbExtensionID;
		return result;
	}

	public boolean isValid(String inspect)
	{
		setInspectionID(inspect);
		return isValid();
	}

	public boolean isValid()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMInspection.isValid"));
			stmt.setString(1, getInspectionID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Inspection [" + getInspectionID().toString() + "]");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setDescription(String description)
	{
		dbDescription = description;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setInspectionID(String inpectionid)
	{
		dbInspectionID = inpectionid;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setExtensionID(Long extensionid)
	{
		dbExtensionID = extensionid;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValid() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMInspection.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getInspectionID());
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
