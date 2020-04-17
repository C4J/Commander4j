package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMSelectList.java
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
 * The APP_QM_SELECTLIST table contains the lists which appear within the
 * Quality Module results entry table. This table can contain multiple lists by
 * use of a filter on a field called SELECT_LIST_ID
 * 
 * <p>
 * <img alt="" src="./doc-files/APP_QM_SELECTLIST.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameQMSelectListAdmin
 *      JInternalFrameQMSelectListAdmin
 * @see com.commander4j.app.JDialogQMSelectListProperties
 *      JDialogQMSelectListProperties
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary
 * 
 *
 */
public class JDBQMSelectList
{
	private String dbValueID;
	private String dbSelectListID;
	private String dbDescription;
	private String dbVisible;
	private String dbErrorMessage;
	private Long dbSequence;
	public static int field_list_id = 20;
	public static int field_value_id = 20;
	public static int field_description = 50;
	private final Logger logger = Logger.getLogger(JDBQMSelectList.class);
	private String hostID;
	private String sessionID;
	private Boolean displayModeLong = false;

	public JDBQMSelectList()
	{

	}

	public PreparedStatement getSelectListPreparedStatement()
	{

		PreparedStatement stmt;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSelectList.getLists"));
			stmt.setFetchSize(100);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			stmt = null;
		}

		return stmt;
	}

	public ResultSet getQMSelectListResultSet(PreparedStatement stmt)
	{

		ResultSet rs;

		try
		{
			rs = stmt.executeQuery();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			rs = null;
		}

		return rs;
	}

	public Boolean isDisplayModeLong()
	{
		return displayModeLong;
	}

	public void setDisplayModeLong(Boolean full)
	{
		displayModeLong = full;
	}

	public JDBQMSelectList(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBQMSelectList(String host, String session, String selectlistid, String value, String description, Long sequence)
	{
		setHostID(host);
		setSessionID(session);
		setSelectListID(selectlistid);
		setValue(value);
		setDescription(description);
		setSequence(sequence);
	}

	public void clear()
	{
		setDescription("");
		setSequence((long) -1);
	}

	public boolean create(String sequenceid, String value, String description, Long sequence,String vis)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setSelectListID(sequenceid);
			setValue(value);
			setDescription(description);
			setSequence(sequence);
			setVisible(vis);

			if (isValid() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSelectList.create"));
				stmtupdate.setString(1, getSelectListID());
				stmtupdate.setString(2, getValue());
				stmtupdate.setString(3, getDescription());
				stmtupdate.setLong(4,getSequence());
				stmtupdate.setString(5, getVisible());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("QMSelectList List/Value already exists");
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
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSelectList.delete"));
				stmtupdate.setString(1, getSelectListID());
				stmtupdate.setString(2, getValue());
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

	public String getDescription()
	{
		String result = "";
		if (dbDescription != null)
			result = dbDescription;
		return result;
	}
	
	public String getVisible()
	{
		String result = JUtility.replaceNullStringwithBlank(dbVisible);
		if (result.equals(""))
		{
			result = "Y";
		}
		return result;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public boolean getProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		// logger.debug("JDBQMSequenceList getProperties SelectList
		// ["+getSelectListID()+"] Value ["+getValue()+"]");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSelectList.getProperties"));
			stmt.setString(1, getSelectListID());
			stmt.setString(2, getValue());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDescription(rs.getString("description"));
				setSequence(rs.getLong("sequence"));
				setVisible(rs.getString("visible"));
				result = true;
				rs.close();
				stmt.close();
			} else
			{
				setErrorMessage("Invalid SequenceID/Value [" + getSelectListID().toString() + "/" + getValue().toString() + "]");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public boolean getProperties(String sequenceid, String value)
	{
		setSelectListID(sequenceid);
		setValue(value);
		return getProperties();
	}

	public ResultSet getQMSelectListResultSet(String sequenceid)
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");
		setSelectListID(sequenceid);
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSelectList.getList"));
			stmt.setString(1, getSelectListID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public LinkedList<JDBQMSelectList> getSelectLists()
	{
		LinkedList<JDBQMSelectList> typeList = new LinkedList<JDBQMSelectList>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSelectList.getLists"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBQMSelectList mt = new JDBQMSelectList();
				mt.setSelectListID(rs.getString("select_list_id"));
				mt.setValue(rs.getString("value"));
				mt.setDescription(rs.getString("description"));
				mt.setVisible(rs.getString("visible"));
				mt.setDisplayModeLong(isDisplayModeLong());
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

	public LinkedList<String> getSelectListSummary()
	{
		LinkedList<String> typeList = new LinkedList<String>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSelectList.getSummary"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				String mt = new String();
				mt = rs.getString("select_list_id");
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

	public LinkedList<JDBQMSelectList> getSelectList(String selectlistid)
	{
		LinkedList<JDBQMSelectList> typeList = new LinkedList<JDBQMSelectList>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setSelectListID(selectlistid);
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSelectList.getList"));
			stmt.setString(1, getSelectListID());
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				if (rs.getString("visible").equals("Y"))
				{
					JDBQMSelectList mt = new JDBQMSelectList();
					mt.setSelectListID(rs.getString("select_list_id"));
					mt.setValue(rs.getString("value"));
					mt.setDescription(rs.getString("description"));
					mt.setVisible(rs.getString("visible"));
					mt.setDisplayModeLong(isDisplayModeLong());
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

	public String getSelectListID()
	{
		String result = "";
		if (dbSelectListID != null)
			result = dbSelectListID;
		return result;
	}

	public Long getSequence()
	{
		Long result = (long) -1;
		if (dbSequence != null)
			result = dbSequence;
		return result;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getValue()
	{
		String result = "";
		if (dbValueID != null)
			result = dbValueID;
		return result;
	}

	public boolean isValid()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSelectList.isValid"));
			stmt.setString(1, getSelectListID());
			stmt.setString(2, getValue());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Sequence/Value [" + getSelectListID().toString() + "/" + getValue().toString() + "]");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public boolean isValid(String sequenceid, String value)
	{
		setSelectListID(sequenceid);
		setValue(value);
		return isValid();
	}

	public void setDescription(String description)
	{
		dbDescription = description;
	}

	public void setVisible(String vis)
	{
		vis = JUtility.replaceNullStringwithBlank(vis);
		if (vis.equals(""))
		{
			vis = "Y";
		}
		dbVisible = vis;
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

	public void setSelectListID(String selectlistid)
	{
		dbSelectListID = selectlistid;
	}

	public void setSequence(Long sequence)
	{
		dbSequence = sequence;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setValue(String value)
	{
		dbValueID = value;
	}

	public String toString()
	{
		String result = "";
		if (getValue().equals("") == false)
		{
			if (displayModeLong)
			{
				result = JUtility.padString(getSelectListID(), true, field_list_id, " ") + " - " + JUtility.padString(getValue(), true, field_value_id, " ") + " - " + getDescription();
			} else
			{
				// result = JUtility.padString(getValue(), true, field_value_id,
				// " ") + " - " + getDescription();
				result = getValue() + " - " + getDescription();
			}
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
			if (isValid() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSelectList.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setLong(2, getSequence());
				stmtupdate.setString(3, getVisible());
				stmtupdate.setString(4, getSelectListID());
				stmtupdate.setString(5, getValue());
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
