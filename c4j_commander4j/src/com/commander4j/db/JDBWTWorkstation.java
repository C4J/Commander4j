package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWTWorkstation.java
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

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBWTWorkstation
{
	public static int field_WorkstationID = 25;
	public static int field_Description =35;
	public static int field_Location = 35;
	public static int field_SamplePoint = 25;
	public static int field_ScaleID = 15;
	public static int field_ScalePort = 45;
	public static int field_Override_Sample_Size = 5;
	public static int field_Sample_Size = 5;

	private String dbWorkstationID="";
	private String dbDescription="";
	private String dbLocation="";
	private String dbSamplePoint="";
	private String dbScaleID="";
	private String dbScalePort="";
	private String dbOverrideSampleSize="";
	private int dbSampleSize = 5;

	private String dbErrorMessage="";
	private String hostID;
	private String sessionID;
	
	
	public JDBWTWorkstation(String host, String session)
	{
		super();
		setHostID(host);
		setSessionID(session);
	}
	
	private void clear()
	{
		setDescription("");
		setLocation("");
		setScaleID("");
		setScalePort("");
		setOverrideSampleSize("N");
		setSampleSize(5);
	}

	public boolean create()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			if (isValidWorkstationID() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTWorkstation.create"));
				stmtupdate.setString(1, getWorkstationID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Workstation already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public boolean create(String workstationId)
	{
		setWorkstationID(workstationId);
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
			if (isValidWorkstationID() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTWorkstation.delete"));
				stmtupdate.setString(1, getWorkstationID());
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
	
	public boolean delete(String workstationId)	
	{

		setWorkstationID(workstationId);
		
		return delete();
	}
	
	public void setOverrideSampleSize(String override)
	{
		dbOverrideSampleSize = override;
	}
	
	public void setOverrideSampleSize(boolean override)
	{
		if (override)
		{
			dbOverrideSampleSize = "Y";
		}
		else
		{
			dbOverrideSampleSize = "N";
		}
	}
	
	public boolean isOverrideSampleSize()
	{
		boolean result = false;
		
		if (getOverrideSampleSize().equals("Y"))
		{			
			result =  true;
		}
		
		return result;
	}
	
	public void setSampleSize(int samplesize)
	{
		dbSampleSize = samplesize;
	}
	
	public int getSampleSize()
	{
		return dbSampleSize;
	}
	
	public String getOverrideSampleSize()
	{
		dbOverrideSampleSize = JUtility.replaceNullStringwithBlank(dbOverrideSampleSize);
		
		return dbOverrideSampleSize;
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
	
	public boolean getProperties(String workstationId)
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		setWorkstationID(workstationId);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTWorkstation.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getWorkstationID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Workstation ID");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
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
			setSamplePoint(rs.getString("sample_point"));
			setScaleID(rs.getString("scale_id"));
			setScalePort(rs.getString("scale_port_id"));
			setOverrideSampleSize(rs.getString("override_sample_size"));
			setSampleSize(rs.getInt("sample_size"));

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}
	
	public String getSamplePoint()
	{
		return dbSamplePoint;
	}
	
	public String getScaleID()
	{
		return dbScaleID;
	}
	
	public String getScalePort()
	{
		return dbScalePort;
	}
	
	private String getSessionID()
	{
		return sessionID;
	}
	
	public ResultSet getWorkstationDataResultSet() {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTWorkstation.getWorkstations"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}
	
	public String getWorkstationID()
	{
		return dbWorkstationID;
	}
	
	public LinkedList<JDBWTWorkstation> getWorkstations() {
		LinkedList<JDBWTWorkstation> sampList = new LinkedList<JDBWTWorkstation>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTWorkstation.getWorkstations"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWTWorkstation samp = new JDBWTWorkstation(getHostID(), getSessionID());
				samp.setWorkstationID(rs.getString("workstation_id"));
				samp.setSamplePoint(rs.getString("sample_point"));
				samp.setDescription(rs.getString("description"));
				samp.setLocation(rs.getString("location"));
				samp.setScaleID(rs.getString("scale_id"));
				samp.setOverrideSampleSize(rs.getString("override_sample_size"));
				samp.setSampleSize(rs.getInt("sample_size"));

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
	
	public boolean isValidWorkstationID()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTWorkstation.isValidWorkstation"));
			stmt.setString(1, getWorkstationID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Workstation [" + getWorkstationID() + "]");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}
	
	public boolean isValidWorkstationID(String workstationID)
	{
		setWorkstationID(workstationID);
		return isValidWorkstationID();
	}
	
	public boolean renameTo(String newname)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWorkstationID() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTWorkstation.rename"));
				stmtupdate.setString(1, newname);
				stmtupdate.setString(2, getWorkstationID());
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
	
	private void setErrorMessage(String ErrorMsg)
	{
		dbErrorMessage = ErrorMsg;
	}
	
	private void setHostID(String host)
	{
		hostID = host;
	}
	
	public void setLocation(String dbLocation)
	{
		this.dbLocation = JUtility.replaceNullStringwithBlank(dbLocation);
	}	
	
	public void setSamplePoint(String dbSamplePoint)
	{
		this.dbSamplePoint = JUtility.replaceNullStringwithBlank(dbSamplePoint);
	}
	
	public void setScaleID(String dbScaleID)
	{
		this.dbScaleID = JUtility.replaceNullStringwithBlank(dbScaleID);
	}
	
	public void setScalePort(String dbScalePort)
	{
		this.dbScalePort = JUtility.replaceNullStringwithBlank(dbScalePort);
	}
	
	private void setSessionID(String session)
	{
		sessionID = session;
	}
	
	public void setWorkstationID(String dbWorkstationID)
	{
		this.dbWorkstationID = JUtility.replaceNullStringwithBlank(dbWorkstationID);
	}

	public String toString() {
		String result = JUtility.padString(getWorkstationID(), true, field_WorkstationID, " ")+ JUtility.padString(getSamplePoint(), true, field_SamplePoint, " ")+JUtility.padString(getScaleID(), true, field_ScaleID, " ")+getLocation();

		return result;
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWorkstationID() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTWorkstation.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getLocation());
				stmtupdate.setString(3, getSamplePoint());
				stmtupdate.setString(4, getScaleID());	
				stmtupdate.setString(5, getScalePort());
				stmtupdate.setString(6, getOverrideSampleSize());
				stmtupdate.setInt(7, getSampleSize());
				stmtupdate.setString(8, getWorkstationID());
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
