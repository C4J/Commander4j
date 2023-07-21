package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBSampleReasonss.java
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
import java.util.Vector;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBSampleReasonss class is used to insert/update/delete records in the table
 * APP_SAMPLE_REASONS. This table contains a list of valid reasons which can be
 * assigned to the Pallet Sample.
 */

public class JDBSampleReasons
{
	private String dbErrorMessage;
	private String dbDescription;
	private String dbSampleReason;
	private String dbEnabled;
	public static int field_sample_reason = 15;
	public static int field_description = 80;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBSampleReasons.class);
	private String hostID;
	private String sessionID;

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	private String getHostID()
	{
		return hostID;
	}

	public JDBSampleReasons(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

	}

	public boolean create(String reason, String description)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setSampleReason(reason);

			setDescription(description);
			
			setEnabled(true);

			if (isValidReason() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleReasons.create"));
				stmtupdate.setString(1, getSampleReason());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getEnabled());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Reason already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidReason() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleReasons.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getEnabled());
				stmtupdate.setString(3, getSampleReason());
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

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidReason() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleReasons.delete"));
				stmtupdate.setString(1, getSampleReason());
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

	public boolean renameTo(String newReason)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidReason() == true)
			{
				JDBSampleReasons mattype = new JDBSampleReasons(getHostID(), getSessionID());
				mattype.setSampleReason(newReason);
				if (mattype.isValidReason() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleReasons.renameTo1"));
					stmtupdate.setString(1, newReason);
					stmtupdate.setString(2, getSampleReason());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleReasons.renameTo2"));
					stmtupdate.setString(1, newReason);
					stmtupdate.setString(2, getSampleReason());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setSampleReason(newReason);
					result = true;
				} else
				{
					setErrorMessage("New Reason Code is already in use.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidReason()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleReasons.isValidReason"));
			stmt.setString(1, getSampleReason());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				if (getSampleReason().equals("") == false)
				{
					setErrorMessage("Invalid Reason [" + getSampleReason() + "]");
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public JDBSampleReasons(String host, String session, String reason, String description,String enabled)
	{
		setHostID(host);
		setSessionID(session);
		setSampleReason(reason);
		setDescription(description);
		setEnabled(enabled);
	}

	public String getDescription()
	{
		String result = "";
		if (dbDescription != null)
			result = dbDescription;
		return result;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}
	
	public void setEnabled(String enabled)
	{
		dbEnabled = JUtility.replaceNullStringwithBlank(enabled);
		if (dbEnabled.equals(""))
		{
			dbEnabled = "N";
		}
	}
	
	public void setEnabled(boolean enabled)
	{
		if (enabled)
		{
			dbEnabled = "Y";
		}
		else
		{
			dbEnabled = "N";
		}
	}
	
	public String getEnabled()
	{
		dbEnabled = JUtility.replaceNullStringwithBlank(dbEnabled);
		
		if (dbEnabled.equals(""))
		{
			dbEnabled = "N";
		}
		
		return dbEnabled;
	}
	
	public boolean isEnabled()
	{
		boolean result = false;
		
		if (getEnabled().equals("Y"))
		{
			result = true;
		}
		
		return result;
	}
	
	public void clear()
	{
		// setType("");
		setDescription("");
		setErrorMessage("");
	}

	public boolean getSampleReasonProperties(String reason)
	{
		setSampleReason(reason);
		return getSampleReasonProperties();
	}

	public boolean getSampleReasonProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getReasonProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleReasons.getReasonProperties"));
			stmt.setString(1, getSampleReason());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDescription(rs.getString("description"));
				setEnabled(rs.getString("enabled"));
				result = true;
			} else
			{
				if (getSampleReason().equals("") == false)
				{
					setErrorMessage("Invalid Reason");
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public Vector<JDBSampleReasons> getSampleReasons(boolean enabled)
	{
		Vector<JDBSampleReasons> typeList = new Vector<JDBSampleReasons>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleReasons.getReasons"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBSampleReasons mt = new JDBSampleReasons(getHostID(), getSessionID());
				mt.setSampleReason(rs.getString("sample_reason"));
				mt.setDescription(rs.getString("description"));
				mt.setEnabled(rs.getString("enabled"));
				
				if (mt.isEnabled() == enabled)
				{
					typeList.add(mt);
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

	public String getSampleReason()
	{
		String result = "";
		if (dbSampleReason != null)
			result = dbSampleReason;
		return result;
	}

	public void setDescription(String description)
	{
		dbDescription = description;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setSampleReason(String reason)
	{
		dbSampleReason = reason;
	}

	public String toString()
	{
		String result = "";
		if (getSampleReason().equals("") == false)
		{
			result = JUtility.padString(getSampleReason(), true, field_sample_reason, " ") + " " + getDescription();
		} else
		{
			result = "";
		}

		return result;
	}
}
