package com.commander4j.db;

import java.io.File;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBPrinters.java
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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * JDBPrinters class inserts/deletes/updates record from the table SYS_PRINTERS.
 * This table will become more important during later releases of the
 * application.
 *
 * @see com.commander4j.db.JDBAutoLabeller JDBAutoLabeller
 * @see com.commander4j.db.JDBAutoLabellerResources JDBAutoLabellerResources
 */
public class JDBPrinters
{

	private String dbPrinterID;
	private String dbPrinterType;
	private String dbEnabled;
	private String dbIPAddress;
	private String dbPort;
	private String dbDescription;
	private String dbLanguage;
	private String dbGroupID;
	private String dbErrorMessage;
	private String hostID;
	private String sessionID;
	private String dbPrinterDPI;
	private String dbPaperSize;	
	private String dbEnableExport = "N";
	private String dbExportPath = "";
	private String dbExportFormat = "CSV";
	private String dbEnableDirectPrint = "N";
	private final Logger logger = Logger.getLogger(JDBPrinters.class);

	public static int field_printer_id = 20;
	public static int field_printer_type = 20;
	public static int field_ip_address = 25;
	public static int field_printer_port = 10;
	public static int field_description = 50;
	public static int field_group = 15;
	public static int field_language = 15;
	public static int field_dpi = 10;
	public static int field_export_path = 250;
	public static int field_export_format = 15;
	public static int field_direct_print = 1;

	public JDBPrinters(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{

		setPrinterType("");
		setPrinterType("");
		setIPAddress("127.0.0.1");
		setPort("8000");
		setDescription("");
		setLanguage("");
		setEnableExport(false);
		setExportPath("");
		setExportFormat("");
		setEnableDirectPrint(false);
		setPrinterEnabled(false);
	}

	public void setEnableExport(String enabled)
	{
		dbEnableExport = enabled;
	}

	public String getEnableExport()
	{
		return dbEnableExport;
	}

	public void setEnableExport(boolean enabled)
	{
		if (enabled)
		{
			dbEnableExport = "Y";
		} else
		{
			dbEnableExport = "N";
		}
	}

	public void setExportPath(String path)
	{
		dbExportPath = path;
	}

	public void setExportFormat(String format)
	{
		if (format.equals(""))
		{
			format = "CSV";
		}
		dbExportFormat = format;
	}

	public String getExportPath()
	{
		return dbExportPath;
	}

	public String getExportRealPath()
	{
		String result = getExportPath();
		result = result.replace("{base_dir}", Common.base_dir);
		result = StringUtils.replaceChars(result, "\\", String.valueOf(File.separatorChar));
		result = StringUtils.replaceChars(result, "/", String.valueOf(File.separatorChar));
		logger.debug(result);
		return result;
	}
	
	public String getExportFormat()
	{
		if (dbExportFormat.equals(""))
		{
			dbExportFormat = "CSV";
		}
		return dbExportFormat;
	}

	public void setEnableDirectPrint(String enabled)
	{
		dbEnableDirectPrint = enabled;
	}

	public String getEnableDirectPrint()
	{
		return dbEnableDirectPrint;
	}

	public void setEnableDirectPrint(boolean enabled)
	{
		if (enabled)
		{
			dbEnableDirectPrint = "Y";
		} else
		{
			dbEnableDirectPrint = "N";
		}
	}

	public boolean isExportEnabled()
	{
		boolean result = false;
		if (dbEnableExport.equals("Y"))
		{
			result = true;
		}
		return result;
	}

	public boolean isDirectPrintEnabled()
	{
		boolean result = false;
		if (dbEnableDirectPrint.equals("Y"))
		{
			result = true;
		}
		return result;
	}

	public boolean create()
	{

		logger.debug("create [" + getPrinterID() + " - " + getGroupID() + "]");

		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPrinters.create"));

			stmtupdate.setString(1, getPrinterID());
			stmtupdate.setString(2, getGroupID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			update();
			result = true;
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(String printerId, String groupId)
	{
		setPrinterID(printerId);
		setGroupID(groupId);
		return create();
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPrinters.delete"));
			stmtupdate.setString(1, getPrinterID());
			stmtupdate.setString(2, getGroupID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

			/* Remove all links to printer */
			JDBPrinterLineMembership plm = new JDBPrinterLineMembership(getHostID(), getSessionID());
			plm.deleteForPrinterID(getPrinterID());

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String printer, String group)
	{
		setPrinterID(printer);
		setGroupID(group);
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

	public String getGroupID()
	{

		dbGroupID = JUtility.replaceNullStringwithBlank(dbGroupID);
		if (dbGroupID.equals(""))
		{
			dbGroupID = Common.printerGroup[0];
		}
		return dbGroupID;

	}

	public void setDPI(String dpi)
	{
		dbPrinterDPI = JUtility.replaceNullStringwithBlank(dpi);
	}
	
	public void setPaperSize(String size)
	{
		String result = JUtility.replaceNullStringwithBlank(size).toLowerCase();
		dbPaperSize = StringUtils.remove(result, " ");
	}

	public String getDPI()
	{
		return JUtility.replaceNullStringwithBlank(dbPrinterDPI);
	}

	public String getPaperSize()
	{
		String result = JUtility.replaceNullStringwithBlank(dbPaperSize).toLowerCase();
		result = StringUtils.remove(result, " ");
		return result;
	}
	
	private String getHostID()
	{
		return hostID;
	}

	public String getIPAddress()
	{
		dbIPAddress = JUtility.replaceNullStringwithBlank(dbIPAddress);
		if (dbIPAddress.equals(""))
		{
			dbIPAddress = "127.0.0.1";
		}
		return dbIPAddress;
	}

	public String getLanguage()
	{
		dbLanguage = JUtility.replaceNullStringwithBlank(dbLanguage);
		if (dbLanguage.equals(""))
		{
			dbLanguage = "";
		}
		return dbLanguage;
	}

	public String getPort()
	{
		dbPort = JUtility.replaceNullStringwithBlank(dbPort);
		if (dbPort.equals(""))
		{
			dbPort = "9100";
		}
		return dbPort;
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

	public Icon getPrinterIcon()
	{
		Icon icon = new ImageIcon();

		if (isEnabled() == true)
		{
			icon = Common.imageIconloader.getImageIcon16x16(Common.image_printer_enabled);
		} else
		{
			icon = Common.imageIconloader.getImageIcon16x16(Common.image_user_expired);

		}

		return icon;
	}

	public String getPrinterID()
	{
		return dbPrinterID;
	}

	public LinkedList<JDBListData> getPrinterIDs()
	{
		LinkedList<JDBListData> intList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPrinters.getPrinterIDs"));
			stmt.setFetchSize(250);
			stmt.setString(1, "Y");
			rs = stmt.executeQuery();

			while (rs.next())
			{
				getPropertiesfromResultSet(rs);
				icon = getPrinterIcon();
				JDBListData mld = new JDBListData(icon, index, true, rs.getString("printer_id"));
				intList.addLast(mld);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return intList;
	}

	public boolean getPrinterProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPrinters.getPrinterProperties"));
			stmt.setString(1, getPrinterID());
			stmt.setString(2, getGroupID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Printer ID [" + getPrinterID() + "]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getPrinterProperties(String pid, String grp)
	{
		setPrinterID(pid);
		setGroupID(grp);
		boolean result = false;
		result = getPrinterProperties();
		return result;
	}

	public ResultSet getPrintersResultSet(PreparedStatement criteria)
	{

		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();

		} catch (Exception e)
		{
			rs = null;
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public String getPrinterType()
	{
		dbPrinterType = JUtility.replaceNullStringwithBlank(dbPrinterType);
		if (dbPrinterType.equals(""))
		{
			dbPrinterType = Common.printerTypes[0];
		}
		return dbPrinterType;
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setEnabled(rs.getString("enabled"));
			setPrinterType(rs.getString("printer_type"));
			setIPAddress(rs.getString("ip_address"));
			setPrinterID(rs.getString("printer_id"));
			setPort(rs.getString("port"));
			setDescription(rs.getString("description"));
			setLanguage(rs.getString("language"));
			setGroupID(rs.getString("group_id"));
			setDPI(rs.getString("printer_dpi"));
			setPaperSize(rs.getString("paper_size"));
			setEnableExport(rs.getString("enable_export"));
			setExportPath(rs.getString("export_path"));
			setEnableDirectPrint(rs.getString("enable_direct_print"));
			setExportFormat(rs.getString("export_format"));

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
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

	public void setDescription(String desc)
	{
		dbDescription = desc;
	}

	private void setErrorMessage(String err)
	{
		dbErrorMessage = err;
	}

	public void setGroupID(String grp)
	{
		dbGroupID = grp;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setIPAddress(String ip)
	{
		dbIPAddress = ip;
	}

	public void setLanguage(String lang)
	{
		dbLanguage = lang;
	}

	public void setPort(String port)
	{
		dbPort = port;
	}

	public void setEnabled(String enabled)
	{
		this.dbEnabled = enabled;
	}

	public void setPrinterEnabled(boolean enabled)
	{
		if (enabled)
		{
			this.dbEnabled = "Y";
		} else
		{
			this.dbEnabled = "N";
		}
	}

	public void setPrinterID(String pid)
	{
		dbPrinterID = pid;
	}

	public void setPrinterType(String type)
	{
		dbPrinterType = type;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = getPrinterID();
		return result;
	}

	public boolean update()
	{
		boolean result = false;
		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPrinters.update"));

			stmtupdate.setString(1, getPrinterType());
			stmtupdate.setString(2, getEnabled());
			stmtupdate.setString(3, getIPAddress());
			stmtupdate.setString(4, getPort());
			stmtupdate.setString(5, getDescription());
			stmtupdate.setString(6, getLanguage());
			stmtupdate.setString(7, getDPI());
			stmtupdate.setString(8, getEnableExport());
			stmtupdate.setString(9, getExportPath());
			stmtupdate.setString(10, getEnableDirectPrint());
			stmtupdate.setString(11, getExportFormat());
			stmtupdate.setString(12, getPaperSize());
			stmtupdate.setString(13, getPrinterID());
			stmtupdate.setString(14, getGroupID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
			{
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			}
			stmtupdate.close();
			result = true;
		} catch (Exception e)
		{
			setErrorMessage(e.getMessage());
			result = false;
		}
		return result;
	}

	public boolean update(String printid, String groupid)
	{
		Boolean result = false;
		setPrinterID(printid);
		setGroupID(groupid);
		result = update();
		return result;
	}

}
