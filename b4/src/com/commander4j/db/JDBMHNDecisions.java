package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBMHNDecisions.java
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

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JColorPair;
import com.commander4j.util.JUtility;

/**
 * The JDBMHNDecisions class is used to insert/update/delete records in the
 * table APP_MHN_DECISIONS. This table contains a list of valid decisions which
 * can be applied to the individual pallets which have been assigned to the
 * Master Hold Notice.
 * <p>
 * <img alt="" src="./doc-files/APP_MHN_DECISIONS.jpg" >
 */
public class JDBMHNDecisions
{
	private String dbErrorMessage;
	private String dbDescription;
	private String dbStatus;
	private String dbDecision;
	private Integer dbForeground;
	private Integer dbBackground;
	public static int field_decision = 10;
	public static int field_description = 50;
	public static int field_status = 20;
	private final Logger logger = Logger.getLogger(JDBMHNDecisions.class);
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

	public JDBMHNDecisions(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

	}

	public LinkedList<JDBMHNDecisions> getMHNDecisions()
	{
		LinkedList<JDBMHNDecisions> uomList = new LinkedList<JDBMHNDecisions>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHN.getDecisions"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBMHNDecisions descision = new JDBMHNDecisions(getHostID(), getSessionID());
				descision.setDecision(rs.getString("decision"));
				descision.setDescription(rs.getString("description"));
				descision.setStatus(rs.getString("status"));
				uomList.add(descision);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return uomList;
	}

	public boolean create(String decision, String description, String status)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setDecision(decision);

			setDescription(description);
			setStatus(status);

			if (isValidDecision() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNDecision.create"));
				stmtupdate.setString(1, getDecision());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setInt(3, Color.white.getRGB());
				stmtupdate.setInt(4, Color.black.getRGB());
				stmtupdate.setString(5, getStatus());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Decision already exists");
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
			if (isValidDecision() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNDecision.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setInt(2, getBackground().getRGB());
				stmtupdate.setInt(3, getForeground().getRGB());
				stmtupdate.setString(4, getStatus());
				stmtupdate.setString(5, getDecision());
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
			if (isValidDecision() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNDecision.delete"));
				stmtupdate.setString(1, getDecision());
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

	public boolean renameTo(String newDecision)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidDecision() == true)
			{
				JDBMHNDecisions mattype = new JDBMHNDecisions(getHostID(), getSessionID());
				mattype.setDecision(newDecision);
				if (mattype.isValidDecision() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNDecision.renameTo"));
					stmtupdate.setString(1, newDecision);
					stmtupdate.setString(2, getDecision());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setDecision(newDecision);
					result = true;
				} else
				{
					setErrorMessage("New Decision Code is already in use.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidDecision()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNDecision.isValidDecision"));
			stmt.setString(1, getDecision());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Decision [" + getDecision() + "]");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public JDBMHNDecisions(String host, String session, String decision, String description, String status)
	{
		setHostID(host);
		setSessionID(session);
		setDecision(decision);
		setDescription(description);
		setStatus(status);
	}

	public String getDescription()
	{
		String result = "";
		if (dbDescription != null)
			result = dbDescription;
		return result;
	}

	public String getStatus()
	{
		String result = "";
		if (dbStatus != null)
			result = dbStatus;
		return result;
	}

	public Color getForeground()
	{
		Color result = Color.black;
		try
		{
			result = new Color(dbForeground);
		} catch (Exception ex)
		{
			result = Color.black;
		}
		return result;
	}

	public Color getBackground()
	{
		Color result = Color.black;
		try
		{
			result = new Color(dbBackground);
		} catch (Exception ex)
		{
			result = Color.red;
		}
		return result;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public void clear()
	{
		// setType("");
		setDescription("");
		setErrorMessage("");
		setStatus("");
	}

	public boolean getDecisionProperties(String decis)
	{
		setDecision(decis);
		return getDecisionProperties();
	}

	public boolean getDecisionProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getDecisionProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNDecision.getDecisionProperties"));
			stmt.setString(1, getDecision());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDescription(rs.getString("description"));
				setForeground(rs.getInt("color_foreground"));
				setBackground(rs.getInt("color_background"));
				setStatus(rs.getString("status"));
				result = true;
			} else
			{
				setErrorMessage("Invalid Decision");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public HashMap<String, JColorPair> getDecisionColors()
	{
		HashMap<String, JColorPair> colorList = new HashMap<String, JColorPair>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNDecision.getDecisions"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				Color f = new Color(rs.getInt("color_foreground"));
				Color b = new Color(rs.getInt("color_background"));
				JColorPair cp = new JColorPair(f, b);
				colorList.put(rs.getString("decision"), cp);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return colorList;
	}

	public Vector<JDBMHNDecisions> getDecisions()
	{
		Vector<JDBMHNDecisions> typeList = new Vector<JDBMHNDecisions>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNDecision.getDecisions"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBMHNDecisions mt = new JDBMHNDecisions(getHostID(), getSessionID());
				mt.setDecision(rs.getString("decision"));
				mt.setDescription(rs.getString("description"));
				mt.setStatus(rs.getString("status"));
				mt.setForeground(rs.getInt("color_foreground"));
				mt.setBackground(rs.getInt("color_background"));
				typeList.add(mt);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return typeList;
	}

	public String getDecision()
	{
		String result = "";
		if (dbDecision != null)
			result = dbDecision;
		return result;
	}

	public void setDescription(String description)
	{
		dbDescription = description;
	}

	public void setStatus(String status)
	{
		dbStatus = status;
	}

	public void setForeground(Color foreground)
	{
		dbForeground = foreground.getRGB();
	}

	public void setBackground(Color background)
	{
		if (background != null)
		{
			dbBackground = background.getRGB();
		}
	}

	public void setForeground(Integer forground)
	{
		if (forground != null)
		{
			dbForeground = forground;
		}
	}

	public void setBackground(Integer background)
	{
		dbBackground = background;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setDecision(String decision)
	{
		dbDecision = decision;
	}

	public String toString()
	{
		String result = "";
		if (getDecision().equals("") == false)
		{
			result = JUtility.padString(getDecision(), true, field_decision, " ") + " - " + JUtility.padString(getStatus(), true, field_status, " ") + " - " + getDescription();
		} else
		{
			result = "";
		}

		return result;
	}
}
