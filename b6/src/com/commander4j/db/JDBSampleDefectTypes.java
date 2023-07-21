package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBSampleDefects.java
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
 * The JDBSampleDefectTypes class is used to insert/update/delete records in the table
 * APP_SAMPLE_DEFECTS. This table contains a list of valid reasons which can be
 * assigned to the Pallet Sample.
 */

public class JDBSampleDefectTypes
{
	public static int field_sample_defect_type = 15;
	public static int field_description = 40;
	public static int field_long_description = 255;
	private String 	dbErrorMessage;
	private String 	dbSampleDefectType;
	private String 	dbDefectDescription;
	private String 	dbDefectLongDescription;
	private int		dbOutputColumn;
	private String 	dbEnabled;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBSampleDefectTypes.class);
	private String hostID;
	private String sessionID;
	private String dbLeakingData;
	private String dbNonLeakingData;
	
	public JDBSampleDefectTypes(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

	}
	
	public JDBSampleDefectTypes(String host, String session, String defect, String description,String LongDescription,int col,String enabled,String leakyes,String leakno)
	{
		setHostID(host);
		setSessionID(session);
		setSampleDefectType(defect);
		setDescription(description);
		setLongDescription(LongDescription);
		setOutputColumn(col);
		setEnabled(enabled);
		setLeakingData(leakyes);
		setNonLeakingData(leakno);
	}

	public void clear()
	{
		// setType("");
		setDescription("");
		setLongDescription("");
		setErrorMessage("");
		setEnabled(true);
		setLeakingData(true);
		setNonLeakingData(true);
	}

	public boolean create(String defect, String description)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setSampleDefectType(defect);

			setDescription(description);
			
			setEnabled(true);
			setLeakingData(true);
			setNonLeakingData(true);

			if (isValidDefect() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectType.create"));
				stmtupdate.setString(1, getSampleDefectType());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getLongDescription());
				stmtupdate.setInt(4, getOutputColumn());
				stmtupdate.setString(5, getLeakData());
				stmtupdate.setString(6, getNonLeakData());
				stmtupdate.setString(7, getEnabled());
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

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidDefect() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectType.delete"));
				stmtupdate.setString(1, getSampleDefectType());
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

	public String getDescription()
	{
		String result = "";
		if (dbDefectDescription != null)
			result = dbDefectDescription;
		return result;
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
	
	public String getLeakData()
	{
		dbLeakingData = JUtility.replaceNullStringwithBlank(dbLeakingData);
		
		if (dbLeakingData.equals(""))
		{
			dbLeakingData = "N";
		}
		
		return dbLeakingData;
	}
	
	public String getNonLeakData()
	{
		dbNonLeakingData = JUtility.replaceNullStringwithBlank(dbNonLeakingData);
		
		if (dbNonLeakingData.equals(""))
		{
			dbNonLeakingData = "N";
		}
		
		return dbNonLeakingData;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getLongDescription()
	{
		String result = "";
		if (dbDefectLongDescription != null)
			result = dbDefectLongDescription;
		return result;
	}

	public int getOutputColumn()
	{
		return dbOutputColumn;
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
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectType.getDefectProperties"));
			stmt.setString(1, getSampleDefectType());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDescription(rs.getString("description"));
				setLongDescription(rs.getString("long_description"));
				setOutputColumn(rs.getInt("output_column"));
				setLeakingData(rs.getString("leaking_data"));
				setNonLeakingData(rs.getString("non_leaking_data"));
				setEnabled(rs.getString("enabled"));
				result = true;
			} else
			{
				if (getSampleDefectType().equals("") == false)
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

	public boolean getSampleDefectProperties(String reason)
	{
		setSampleDefectType(reason);
		return getSampleDefectProperties();
	}

	public Vector<JDBSampleDefectTypes> getSampleDefects(boolean enabled)
	{
		Vector<JDBSampleDefectTypes> typeList = new Vector<JDBSampleDefectTypes>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectType.getDefects"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBSampleDefectTypes mt = new JDBSampleDefectTypes(getHostID(), getSessionID());
				mt.setSampleDefectType(rs.getString("defect_type"));
				mt.setDescription(rs.getString("description"));
				mt.setLongDescription(rs.getString("long_description"));
				mt.setOutputColumn(rs.getInt("output_column"));
				mt.setLeakingData(rs.getString("leaking_data"));
				mt.setNonLeakingData(rs.getString("non_leaking_data"));
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

	public Vector<JDBSampleDefectTypes> getSampleDefectsByOutputColumn(boolean enabled)
	{
		Vector<JDBSampleDefectTypes> typeList = new Vector<JDBSampleDefectTypes>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectType.getDefectsByOutputColumn"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBSampleDefectTypes mt = new JDBSampleDefectTypes(getHostID(), getSessionID());
				mt.setSampleDefectType(rs.getString("defect_type"));
				mt.setDescription(rs.getString("description"));
				mt.setLongDescription(rs.getString("long_description"));
				mt.setOutputColumn(rs.getInt("output_column"));
				mt.setLeakingData(rs.getString("leaking_data"));
				mt.setNonLeakingData(rs.getString("non_leaking_data"));
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
	
	public String getSampleDefectType()
	{
		String result = "";
		if (dbSampleDefectType != null)
			result = dbSampleDefectType;
		return result;
	}
	
	private String getSessionID()
	{
		return sessionID;
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
	
	public boolean isLeakingData()
	{
		boolean result = false;
		
		if (getLeakData().equals("Y"))
		{
			result = true;
		}
		
		return result;
	}
	
	public boolean isNonLeakingData()
	{
		boolean result = false;
		
		if (getNonLeakData().equals("Y"))
		{
			result = true;
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
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectType.isValidDefect"));
			stmt.setString(1, getSampleDefectType());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				if (getSampleDefectType().equals("") == false)
				{
					setErrorMessage("Invalid Defect Type [" + getSampleDefectType() + "]");
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
	
	public boolean renameTo(String newDefect)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidDefect() == true)
			{
				JDBSampleDefectTypes mattype = new JDBSampleDefectTypes(getHostID(), getSessionID());
				mattype.setSampleDefectType(newDefect);
				if (mattype.isValidDefect() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectType.renameTo1"));
					stmtupdate.setString(1, newDefect);
					stmtupdate.setString(2, getSampleDefectType());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectType.renameTo2"));
					stmtupdate.setString(1, newDefect);
					stmtupdate.setString(2, getSampleDefectType());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectType.renameTo3"));
					stmtupdate.setString(1, newDefect);
					stmtupdate.setString(2, getSampleDefectType());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setSampleDefectType(newDefect);
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
	
	public void setDescription(String description)
	{
		dbDefectDescription = description;
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
	
	public void setLeakingData(boolean enabled)
	{
		if (enabled)
		{
			dbLeakingData = "Y";
		}
		else
		{
			dbLeakingData = "N";
		}
	}

	
	public void setNonLeakingData(boolean enabled)
	{
		if (enabled)
		{
			dbNonLeakingData = "Y";
		}
		else
		{
			dbNonLeakingData = "N";
		}
	}
	
	public void setEnabled(String enabled)
	{
		dbEnabled = JUtility.replaceNullStringwithBlank(enabled);
		if (dbEnabled.equals(""))
		{
			dbEnabled = "N";
		}
	}
	
	public void setLeakingData(String enabled)
	{
		dbLeakingData = JUtility.replaceNullStringwithBlank(enabled);
		if (dbLeakingData.equals(""))
		{
			dbLeakingData = "N";
		}
	}
	
	public void setNonLeakingData(String enabled)
	{
		dbNonLeakingData = JUtility.replaceNullStringwithBlank(enabled);
		if (dbNonLeakingData.equals(""))
		{
			dbNonLeakingData = "N";
		}
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

	public void setLongDescription(String description)
	{
		dbDefectLongDescription = description;
	}

	public void setOutputColumn(int col)
	{
		dbOutputColumn =  col;
	}
	
	public void setSampleDefectType(String reason)
	{
		dbSampleDefectType = reason;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";
		if (getSampleDefectType().equals("") == false)
		{
			String NL = "";
			
			String L="";
			
			if (isNonLeakingData())
			{
				NL="NL ";
			}
			else
			{
				NL="   ";
			}
			if (isLeakingData())
			{
				L = "L  ";
			}
			else
			{
				L = "   ";
			}
			result = JUtility.padString(getSampleDefectType(), true, field_sample_defect_type, " ") + " " + JUtility.padString(getDescription(),true,40," ")+" "+L+NL+JUtility.padString(String.valueOf(getOutputColumn()),false,3,"0");
		} else
		{
			result = "";
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
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSampleDefectType.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getLongDescription());
				stmtupdate.setInt(3, getOutputColumn());
				stmtupdate.setString(4, getLeakData());
				stmtupdate.setString(5, getNonLeakData());
				stmtupdate.setString(6, getEnabled());
				stmtupdate.setString(7, getSampleDefectType());
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
