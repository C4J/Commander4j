package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWTScale.java
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

public class JDBWTScale
{
	private String dbScaleID = "";
	private String dbDescription = "";
	private String dbMake = "";
	private String dbModel = "";
	private String dbParity = "";
	private String dbEndOfLine="";
	private int dbBaudRate = 0;

	private int dbDataBits = 0;
	private int dbStopBits = 0;
	private String dbConnection = "";
	
	private String dbFlowControl = "";
	private String dbErrorMessage = "";
	private String hostID;
	private String sessionID;
	public static int field_ScaleID = 15;
	public static int field_Description = 25;
	public static int field_Make = 15;
	public static int field_Model = 15;
	public static int field_Parity = 10;
	public static int field_EndOfLine = 15;

	public JDBWTScale(String host, String session)
	{
		super();
		setHostID(host);
		setSessionID(session);
	}

	private void clear()
	{
		setDescription("");
		setMake("");
		setModel("");
		setBaudRate(0);
		setDataBits(0);
		setStopBits(0);
		setFlowControl("");
		setConnection("");
		setParity("");
		setEndOfLine("");
	}

	public boolean create()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			if (isValidScaleID() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTScale.create"));
				stmtupdate.setString(1, getScaleID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Scale already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public void setParity(String parity)
	{
		this.dbParity = JUtility.replaceNullStringwithBlank(parity);
	}
	
	public void setEndOfLine(String endOfLine)
	{
		this.dbEndOfLine = JUtility.replaceNullStringwithBlank(endOfLine);
	}

	public String getParity()
	{
		return dbParity;
	}
	
	public String getEndOfLine()
	{
		return dbEndOfLine;
	}
	
	public boolean create(String scaleId)
	{
		setScaleID(scaleId);
		return create();
	}
	
	public boolean renameTo(String newname)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidScaleID() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTScale.rename"));
				stmtupdate.setString(1, newname);
				stmtupdate.setString(2, getScaleID());
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

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		clear();

		try
		{
			if (isValidScaleID() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTScale.delete"));
				stmtupdate.setString(1, getScaleID());
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

	public boolean delete(String scaleId)
	{

		setScaleID(scaleId);

		return delete();
	}

	public int getBaudRate()
	{
		return dbBaudRate;
	}

	public int getDataBits()
	{
		return dbDataBits;
	}

	public String getDescription()
	{
		return dbDescription;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getFlowControl()
	{
		return dbFlowControl;
	}
	
	public String getConnection()
	{
		return dbConnection;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getMake()
	{
		return dbMake;
	}

	public String getModel()
	{
		return dbModel;
	}

	public boolean getProperties(String scaleID)
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		setScaleID(scaleID);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTScale.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getScaleID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Scale ID");
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
			setMake(rs.getString("make"));
			setModel(rs.getString("model"));
			setBaudRate(rs.getInt("baud_rate"));
			setDataBits(rs.getInt("data_bits"));
			setStopBits(rs.getInt("stop_bits"));
			setFlowControl(rs.getString("flow_control"));
			setConnection(rs.getString("connection"));
			setParity(rs.getString("parity"));
			setEndOfLine(rs.getString("end_of_line"));

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getScaleID()
	{
		return dbScaleID;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public int getStopBits()
	{
		return dbStopBits;
	}

	public boolean isValidScaleID()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTScale.isValidScale"));
			stmt.setString(1, getScaleID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Scale [" + getScaleID() + "]");
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

	public boolean isValidScaleID(String dbScaleID)
	{
		setScaleID(dbScaleID);
		return isValidScaleID();
	}

	public void setBaudRate(int dbBaud)
	{
		this.dbBaudRate = dbBaud;
	}

	public void setDataBits(int dbDataBits)
	{
		this.dbDataBits = dbDataBits;
	}

	public void setDescription(String dbDescription)
	{
		this.dbDescription = JUtility.replaceNullStringwithBlank(dbDescription);
	}

	private void setErrorMessage(String ErrorMsg)
	{
		dbErrorMessage = ErrorMsg;
	}

	public void setFlowControl(String dbFlowControl)
	{
		this.dbFlowControl = dbFlowControl;
	}

	public void setConnection(String dbConnection)
	{
		this.dbConnection = dbConnection;
	}
	
	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setMake(String dbMake)
	{
		this.dbMake = JUtility.replaceNullStringwithBlank(dbMake);
	}

	public void setModel(String dbModel)
	{
		this.dbModel = JUtility.replaceNullStringwithBlank(dbModel);
	}

	public void setSamplePoint(int dbDataBits)
	{
		this.dbDataBits = dbDataBits;
	}

	public void setScaleID(String dbScaleID)
	{
		this.dbScaleID = JUtility.replaceNullStringwithBlank(dbScaleID);
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setStopBits(int dbStopBits)
	{
		this.dbStopBits = dbStopBits;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidScaleID() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTScale.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getMake());
				stmtupdate.setString(3, getModel());
				stmtupdate.setInt(4, getBaudRate());
				stmtupdate.setInt(5, getDataBits());
				stmtupdate.setInt(6, getStopBits());
				stmtupdate.setString(7, getFlowControl());
				stmtupdate.setString(8, getConnection());
				stmtupdate.setString(9, getParity());
				stmtupdate.setString(10, getEndOfLine());
				stmtupdate.setString(11, getScaleID());
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
	
	public LinkedList<JDBWTScale> getScales() {
		LinkedList<JDBWTScale> sampList = new LinkedList<JDBWTScale>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTScale.getScales"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWTScale scale = new JDBWTScale(getHostID(), getSessionID());
				scale.setScaleID(rs.getString("scale_id"));
				scale.setDescription(rs.getString("description"));
				scale.setMake(rs.getString("make"));
				scale.setModel(rs.getString("model"));
				scale.setBaudRate(rs.getInt("baud_rate"));
				scale.setDataBits(rs.getInt("data_bits"));
				scale.setStopBits(rs.getInt("stop_bits"));
				scale.setFlowControl(rs.getString("flow_control"));
				scale.setConnection(rs.getString("connection"));
				scale.setParity(rs.getString("parity"));
				scale.setEndOfLine(rs.getString("end_of_line"));
				sampList.add(scale);
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
	
	public ResultSet getScalesDataResultSet() {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTScale.getScales"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}
	
	public String toString() {
		String result = JUtility.padString(getScaleID(), true, field_ScaleID, " ")+ JUtility.padString(getDescription(), true, field_Description, " ")+JUtility.padString(getMake(), true, field_Make, " ")+getModel();


		return result;
	}	

}
