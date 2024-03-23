package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWTSamplePoint.java
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

import com.commander4j.gui.JCheckListItem;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBWTSamplePoint
{
	public static int field_SamplePoint = 25;
	public static int field_Description = 35;
	public static int field_Location = 35;
	public static int field_Required_Resource = 50;

	public static final int displayType_LIST = 1;
	public static final int displayType_COMBO = 2;
	public static final int displayType_SHORT = 3;
	private String dbSamplePoint = "";
	private String dbDescription = "";
	private String dbLocation = "";
	private String dbErrorMessage = "";
	private String hostID;
	private String sessionID;
	private String dbRequiredResource = "";
	private int displayType = 1;

	public String getRequiredResource()
	{
		return dbRequiredResource;
	}

	public void setRequiredResource(String requiredResource)
	{
		dbRequiredResource = requiredResource;
	}

	public JDBWTSamplePoint(String host, String session)
	{
		super();
		setHostID(host);
		setSessionID(session);
		setDisplayType(displayType_LIST);
	}

	private void clear()
	{
		setDescription("");
		setLocation("");
		setRequiredResource("");

	}

	public boolean create()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			if (isValidSamplePoint() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSamplePoint.create"));
				stmtupdate.setString(1, getSamplePoint());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Sample Point already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(String samplepoint)
	{
		setSamplePoint(samplepoint);
		return create();
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		clear();

		try
		{
			if (isValidSamplePoint() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSamplePoint.delete"));
				stmtupdate.setString(1, getSamplePoint());
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

	public boolean delete(String samplepoint)
	{

		setSamplePoint(samplepoint);

		return delete();
	}

	public String getDescription()
	{
		return dbDescription;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getLocation()
	{
		return dbLocation;
	}

	public boolean getProperties(String samplepoint)
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		setSamplePoint(samplepoint);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSamplePoint.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getSamplePoint());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Sample Point");
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

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();

			setDescription(rs.getString("description"));
			setLocation(rs.getString("location"));
			setRequiredResource(rs.getString("required_resource"));

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getSamplePoint()
	{
		return dbSamplePoint;
	}

	public ResultSet getSamplePointDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSamplePoint.getSamplePoints"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public LinkedList<JDBWTSamplePoint> getSamplePoints(int displayType)
	{
		LinkedList<JDBWTSamplePoint> sampList = new LinkedList<JDBWTSamplePoint>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSamplePoint.getSamplePoints"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWTSamplePoint samp = new JDBWTSamplePoint(getHostID(), getSessionID());
				samp.setDisplayType(displayType);
				samp.setSamplePoint(rs.getString("sample_point"));
				samp.setDescription(rs.getString("description"));
				samp.setLocation(rs.getString("location"));
				samp.setRequiredResource(rs.getString("required_resource"));
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

	public Vector<JCheckListItem> getSamplePointCheckList(LinkedList<String> defaultselected)
	{
		Vector<JCheckListItem> spList = new Vector<JCheckListItem>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSamplePoint.getSamplePoints"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWTSamplePoint samp = new JDBWTSamplePoint(getHostID(), getSessionID());
				samp.getValuesFromResultSet(rs);
				samp.setDisplayType(displayType_SHORT);

				JCheckListItem ci = new JCheckListItem(samp);
				
				if (defaultselected.indexOf(samp.getSamplePoint()) >= 0)
				{
					ci.setSelected(true);
				}
				else
				{
					ci.setSelected(false);
				}
				
				spList.add(ci);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return spList;
	}

	public void getValuesFromResultSet(ResultSet rs)
	{
		try
		{
			setDescription(rs.getString("description"));
			setSamplePoint(rs.getString("sample_point"));
			setDescription(rs.getString("description"));
			setLocation(rs.getString("location"));
			setRequiredResource(rs.getString("required_resource"));

		}
		catch (Exception ex)
		{

		}
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean isValidSamplePoint()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSamplePoint.isValidSamplePoint"));
			stmt.setString(1, getSamplePoint());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Sample Point [" + getSamplePoint() + "]");
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

	public boolean isValidSamplePoint(String samplepoint)
	{
		setSamplePoint(samplepoint);
		return isValidSamplePoint();
	}

	public boolean renameTo(String newname)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidSamplePoint() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSamplePoint.rename"));
				stmtupdate.setString(1, newname);
				stmtupdate.setString(2, getSamplePoint());
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

	public void setDescription(String dbDescription)
	{
		this.dbDescription = JUtility.replaceNullStringwithBlank(dbDescription);
	}

	public void setDisplayType(int type)
	{
		this.displayType = type;
	}

	private void setErrorMessage(String ErrorMsg)
	{
		dbErrorMessage = ErrorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setLocation(String loc)
	{
		this.dbLocation = JUtility.replaceNullStringwithBlank(loc);
	}

	public void setSamplePoint(String spoint)
	{
		this.dbSamplePoint = JUtility.replaceNullStringwithBlank(spoint);
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{

		String result = "";
		
		switch (displayType) {
			case displayType_LIST:
				result = JUtility.padString(getSamplePoint(), true, field_SamplePoint, " ") + JUtility.padString(getDescription(), true, field_Description, " ") + getRequiredResource();
				break;
			case displayType_COMBO:
				result = JUtility.padString(getSamplePoint(), true, field_SamplePoint, " ") + " - " + JUtility.padString(getDescription(), true, field_Description, " ");
				break;
			case displayType_SHORT:
				result = getSamplePoint();
				break;
			default:
				result = "displayType undifined";
		}
		
		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidSamplePoint() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSamplePoint.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getLocation());
				stmtupdate.setString(3, getRequiredResource());
				stmtupdate.setString(4, getSamplePoint());
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

}
