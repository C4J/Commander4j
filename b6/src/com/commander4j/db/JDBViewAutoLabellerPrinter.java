package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBViewAutoLabellerPrinter.java
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

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBViewAutoLabellerPrinter {

	private String dbLine = "";
	private String dbPrinterID = "";
	private String dbGroupID = "";
	private String dbModuleID = "";
	private String dbError = "";
	private String dbUnique = "";
	private String hostID;
	private String sessionID;
	private JDBAutoLabeller autolab;
	private JDBLabelData labdat;
	private JDBModule modules;
	private JDBPrinters prints;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBViewAutoLabellerPrinter.class);

	public JDBViewAutoLabellerPrinter(String host, String session) {
		setHostID(host);
		setSessionID(session);
		autolab = new JDBAutoLabeller(getHostID(), getSessionID());
		labdat = new JDBLabelData(getHostID(), getSessionID());
		modules = new JDBModule(getHostID(), getSessionID());
		prints = new JDBPrinters(getHostID(), getSessionID());
	}

	public String getLineID()
	{
		return JUtility.replaceNullStringwithBlank(dbLine);
	}

	public String getModuleID()
	{
		return dbModuleID;
	}

	public String getPrinterID()
	{
		return dbPrinterID;
	}

	public String getUniqiueID()
	{
		return dbUnique;
	}

	public String getErrorMessage()
	{
		return dbError;
	}

	public String getHostID()
	{
		return hostID;
	}

	public JDBLabelData getLabelDataObj()
	{
		return labdat;
	}

	public JDBPrinters getPrinterObj()
	{
		return prints;
	}

	public JDBAutoLabeller getAutoLabellerObj()
	{
		return autolab;
	}

	public void setGroupID(String grp)
	{
		this.dbGroupID = grp;
		reloadAutoLabPrinter();
	}

	public String getGroupID()
	{
		return JUtility.replaceNullStringwithBlank(dbGroupID);
	}

	public boolean getProperties(String line, String printer)
	{
		boolean result = false;
		PreparedStatement stmt = null;
		ResultSet rs;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("DBview_auto_labeller_printer.getProperties"));

			stmt.setString(1, line);
			stmt.setString(2, printer);
			stmt.setFetchSize(50);

			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid line/printer combination [" + line + "/" + printer + "]");
			}
			rs.close();

			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public LinkedList<JDBViewAutoLabellerPrinter> getModifiedPrinterLines()
	{
		PreparedStatement stmt = null;
		LinkedList<JDBViewAutoLabellerPrinter> result = new LinkedList<JDBViewAutoLabellerPrinter>();
		ResultSet rs;

		result.clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("DBview_auto_labeller_printer.getModified"));
			stmt.setFetchSize(50);

			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBViewAutoLabellerPrinter newitem = new JDBViewAutoLabellerPrinter(getHostID(), getSessionID());
				newitem.getPropertiesfromResultSet(rs);
				result.addLast(newitem);
			}

			rs.close();

			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	private void clear()
	{
		dbError = "";
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setLineID(rs.getString("LINE"));
			setPrinterID(rs.getString("PRINTER_ID"));
			setModuleID(rs.getString("MODULE_ID"));
			setUniqueID(rs.getString("UNIQUE_ID"));
			setGroupID(rs.getString("GROUP_ID"));

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public JDBModule getModuleObj()
	{
		return modules;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public void setLineID(String dbLINE)
	{
		this.dbLine = dbLINE;

		reloadAutoLabPrinter();
	}

	private void reloadAutoLabPrinter()
	{

		if (getGroupID().equals("") == false)
		{
			if (getLineID().equals("") == false)
			{
				if (autolab.getProperties(getLineID(), getGroupID()) == false)
				{
					logger.error(autolab.getErrorMessage());
				}
			}
			
			if (getPrinterID().equals("")==false)
			{
				if (prints.getPrinterProperties(getPrinterID(), getGroupID()) == false)
				{
					logger.error(prints.getErrorMessage());
				}
			}
		}
	}

	public void setModuleID(String dbMODULE_ID)
	{
		this.dbModuleID = dbMODULE_ID;

		if (modules.getModuleProperties(this.dbModuleID) == false)
		{
			logger.error(modules.getErrorMessage());
		}
	}

	public void setPrinterID(String dbPRINTER_ID)
	{
		this.dbPrinterID = dbPRINTER_ID;

		reloadAutoLabPrinter();

	}

	public void setUniqueID(String unqueID)
	{
		this.dbUnique = unqueID;

		if (labdat.getProperties(this.dbUnique) == false)
		{
			logger.error(labdat.getErrorMessage());
		} else
		{
			setModuleID(labdat.getModuleID());
		}
	}

	private void setErrorMessage(String err)
	{
		dbError = err;
	}

	public void setHostID(String hostID)
	{
		this.hostID = hostID;
	}

	public void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}

}
