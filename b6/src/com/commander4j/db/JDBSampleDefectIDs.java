package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBSampleDefectID.java
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
 * The JDBSampleDefects class is used to insert/update/delete records in the table
 * APP_SAMPLE_DEFECT_ID. This table contains a list of valid reasons which can be
 * assigned to the Pallet Sample.
 */

public class JDBSampleDefectIDs
{
	private String dbErrorMessage;
	private String dbSampleDefectID;
	private String dbDefectDescription;
	private String dbDefectLongDescription;
	private String dbSampleDefectType;
	private String dbEnabled;
	public static int field_sample_defect_type = 15;
	public static int field_description = 40;
	public static int field_long_description = 255;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBSampleDefectIDs.class);
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

	public JDBSampleDefectIDs(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

	}

	public boolean create(String defect, String description)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setSampleDefectID(defect);

			setDescription(description);
			
			setEnabled(true);

			if (isValidDefect() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectID.create"));
				stmtupdate.setString(1, getSampleDefectID());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getLongDescription());
				stmtupdate.setString(4, getSampleDefectType());
				stmtupdate.setString(5, getEnabled());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Defect already exists");
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
			if (isValidDefect() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectID.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getLongDescription());
				stmtupdate.setString(3, getSampleDefectType());
				stmtupdate.setString(4, getEnabled());
				stmtupdate.setString(5, getSampleDefectID());
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
			if (isValidDefect() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectID.delete"));
				stmtupdate.setString(1, getSampleDefectID());
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

	public boolean renameTo(String newDefect)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidDefect() == true)
			{
				JDBSampleDefectIDs mattype = new JDBSampleDefectIDs(getHostID(), getSessionID());
				mattype.setSampleDefectID(newDefect);
				if (mattype.isValidDefect() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectID.renameTo1"));
					stmtupdate.setString(1, newDefect);
					stmtupdate.setString(2, getSampleDefectID());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectID.renameTo2"));
					stmtupdate.setString(1, newDefect);
					stmtupdate.setString(2, getSampleDefectID());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setSampleDefectID(newDefect);
					result = true;
				} else
				{
					setErrorMessage("New Defect Code is already in use.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidDefect()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectID.isValidDefect"));
			stmt.setString(1, getSampleDefectID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				if (getSampleDefectID().equals("") == false)
				{
					setErrorMessage("Invalid Defect [" + getSampleDefectID() + "]");
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

	public JDBSampleDefectIDs(String host, String session, String defect, String description,String LongDescription,String type,String enabled)
	{
		setHostID(host);
		setSessionID(session);
		setSampleDefectID(defect);
		setDescription(description);
		setLongDescription(LongDescription);
		setSampleDefectType(type);
		setEnabled(enabled);
	}

	public String getDescription()
	{
		String result = "";
		if (dbDefectDescription != null)
			result = dbDefectDescription;
		return result;
	}

	public String getLongDescription()
	{
		String result = "";
		if (dbDefectLongDescription != null)
			result = dbDefectLongDescription;
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
		setLongDescription("");
		setEnabled("Y");
		setSampleDefectType("");
		setErrorMessage("");
	}

	public boolean getSampleDefectProperties(String reason)
	{
		setSampleDefectID(reason);
		return getSampleDefectProperties();
	}

	public boolean getSampleDefectProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getDefectnProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectID.getDefectProperties"));
			stmt.setString(1, getSampleDefectID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDescription(rs.getString("description"));
				setLongDescription(rs.getString("long_description"));
				setSampleDefectType(rs.getString("defect_type"));
				setEnabled(rs.getString("enabled"));
				result = true;
			} else
			{
				if (getSampleDefectID().equals("") == false)
				{
					setErrorMessage("Invalid Defect");
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

	public Vector<JDBSampleDefectIDs> getSampleDefects(boolean enabled)
	{
		Vector<JDBSampleDefectIDs> typeList = new Vector<JDBSampleDefectIDs>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectID.getDefects"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBSampleDefectIDs mt = new JDBSampleDefectIDs(getHostID(), getSessionID());
				mt.setSampleDefectID(rs.getString("defect_id"));
				mt.setDescription(rs.getString("description"));
				mt.setLongDescription(rs.getString("long_description"));
				mt.setSampleDefectType(rs.getString("defect_type"));
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

	public String getSampleDefectID()
	{
		String result = "";
		if (dbSampleDefectID != null)
			result = dbSampleDefectID;
		return result;
	}
	
	public String getSampleDefectType()
	{
		String result = "";
		if (dbSampleDefectType != null)
			result = dbSampleDefectType;
		return result;
	}

	public void setDescription(String description)
	{
		dbDefectDescription = description;
	}
	
	public void setLongDescription(String description)
	{
		dbDefectLongDescription = description;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setSampleDefectID(String reason)
	{
		dbSampleDefectID = reason;
	}
	
	public void setSampleDefectType(String reason)
	{
		dbSampleDefectType = reason;
	}

	public String toString()
	{
		String result = "";
		if (getSampleDefectID().equals("") == false)
		{
			result = JUtility.padString(getSampleDefectID(), true, field_sample_defect_type, " ") + " " + getDescription();
		} else
		{
			result = "";
		}

		return result;
	}
}
