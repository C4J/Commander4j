package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBPrinterLineMembership.java
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
 * The JDBPrinterLineMembership class associates physical printers with a
 * production line and updates the table SYS_PRINTER_LINE_MEMBERSHIP
 * <p>
 * <img alt="" src="./doc-files/SYS_PRINTER_LINE_MEMBERSHIP.jpg" >
 * 
 * @see com.commander4j.db.JDBPrinters JDBPrinters
 * @see com.commander4j.db.JDBAutoLabeller JDBAutoLabeller
 */
public class JDBPrinterLineMembership
{

	private String db_error_message;
	private Exception db_exception;
	private String db_printer_id;
	private String db_line_id;
	private String db_group_id;
	final Logger logger = Logger.getLogger(JDBPrinterLineMembership.class);
	private String hostID;
	private String sessionID;

	public JDBPrinterLineMembership(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public boolean addPrintertoLine(String line, String group, String printer)
	{
		boolean result = false;

		result = create(line, group, printer);
		return result;
	}

	public boolean create()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			if (isPrinterAssignedToLine() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLinePrinterMembership.create"));
				stmtupdate.setString(1, getLineId());
				stmtupdate.setString(2, getGroupId());
				stmtupdate.setString(3, getPrinterId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Line / Printer already exists");
			}
		} catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	public boolean renameLine(String oldLine, String newLine)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLinePrinterMembership.renameLine"));
			stmtupdate.setString(1, newLine);
			stmtupdate.setString(2, oldLine);
			stmtupdate.execute();
			stmtupdate.clearParameters();

			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();

			setLineId(newLine);
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(String line, String group, String printer)
	{
		setPrinterId(printer);
		setLineId(line);
		setGroupId(group);
		return create();
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isPrinterAssignedToLine() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLinePrinterMembership.delete"));
				stmtupdate.setString(1, getLineId());
				stmtupdate.setString(2, getGroupId());
				stmtupdate.setString(3, getPrinterId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	public boolean deleteForPrinterID(String printer)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		setPrinterId(printer);

		try
		{
			if (isPrinterAssignedToLine() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLinePrinterMembership.deleteForPrinterID"));
				stmtupdate.setString(1, getPrinterId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	public String getErrorMessage()
	{
		return db_error_message;
	}

	public Exception getException()
	{
		return db_exception;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getLineId()
	{
		return db_line_id;
	}

	public String getGroupId()
	{
		return db_group_id;
	}

	public String getPrinterId()
	{
		return db_printer_id;
	}

	public LinkedList<JDBListData> getPrintersAssignedtoLine(String line, String group)
	{

		LinkedList<JDBListData> printerList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		int index = 0;
		setLineId(line);
		setGroupId(group);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLinePrinterMembership.getPrintersAssignedtoLine"));
			stmt.setString(1, getLineId());
			stmt.setString(2, getGroupId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				if (rs.getString("group_id").equals(group))
				{
					JDBListData mld = new JDBListData(null, index, true, rs.getString("printer_id"));
					printerList.addLast(mld);
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setError(e);
		}

		return printerList;
	}

	public LinkedList<JDBListData> getPrintersNotAssignedtoLine(String line, String group)
	{
		LinkedList<JDBListData> printerList = new LinkedList<JDBListData>();

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		int index = 0;
		setLineId(line);
		setGroupId(group);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLinePrinterMembership.getPrintersNotAssignedtoLine"));
			stmt.setString(1, getLineId());
			stmt.setString(2, getGroupId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				if (rs.getString("group_id").equals(group))
				{
					JDBListData mld = new JDBListData(null, index, true, rs.getString("printer_id"));
					printerList.addLast(mld);
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setError(e);
		}

		return printerList;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean isPrinterAssignedToLine()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLinePrinterMembership.isPrinterAssignedToLine"));
			stmt.setString(1, getLineId());
			stmt.setString(2, getGroupId());
			stmt.setString(3, getPrinterId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Line Group Printer Membership");
			}
			stmt.close();

		} catch (SQLException e)
		{
			setError(e);
		}

		return result;

	}

	public boolean removeAllPrintersfromLine(String line, String group)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("removeAllPrintersfromLine Line [" + getLineId() + " / " + getGroupId() + "]");
		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLinePrinterMembership.removeAllPrintersfromLine"));
			stmtupdate.setString(1, line);
			stmtupdate.setString(2, group);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		} catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	public boolean removePrinterfromLine(String line, String group, String printer)
	{
		boolean result = false;

		setLineId(line);
		setGroupId(group);
		setPrinterId(printer);
		result = delete();

		return result;
	}

	public boolean removePrinterfromAllLines(String printer, String group)
	{
		boolean result = false;
		JDBAutoLabeller al = new JDBAutoLabeller(getHostID(), getSessionID());
		LinkedList<JDBListData> lineList = new LinkedList<JDBListData>();
		String line = "";

		lineList = al.getLabellerIDsforGroup(group);

		for (int j = 0; j < lineList.size(); j++)
		{
			line = ((JDBAutoLabeller) lineList.get(j).getObject()).getLine();
			group = ((JDBAutoLabeller) lineList.get(j).getObject()).getGroup();
			removePrinterfromLine(line, group, printer);
		}

		return result;
	}

	private void setError(Exception e)
	{
		logger.error(e);
		db_exception = e;
		setErrorMessage(e.getMessage());
	}

	private void setErrorMessage(String ErrorMsg)
	{
		db_error_message = ErrorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setLineId(String UserId)
	{
		db_line_id = UserId;
	}

	public void setGroupId(String group)
	{
		db_group_id = group;
	}

	public void setPrinterId(String GroupId)
	{
		db_printer_id = GroupId;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		return JUtility.replaceNullStringwithBlank(getPrinterId());
	}
}
