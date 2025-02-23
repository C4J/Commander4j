package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBModule.java
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

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.sys.JMenuOption;
import com.commander4j.util.JUtility;

/**
 * The JDBModule is used to insert/update/delete records in the SYS_MODULES
 * table. The modules database contains a definition of all screens/reports and
 * functions available within the application. These modules are assigned to
 * groups and groups are assigned to users.
 *
 * <p>
 * <img alt="" src="./doc-files/SYS_MODULES.jpg" >
 * 
 * @see com.commander4j.db.JDBGroupPermissions JDBGroupPermissions
 */
public class JDBModule
{

	private String dbResourceKey;
	private String dbMnemonicStr;
	private char dbMnemonicChar;
	private String dbDescription;
	private String dbDKActive;
	private String dbErrorMessage;
	private String dbHelpsetId;
	private String dbIconFilename;
	private String dbModuleId;
	private String dbModuleType;
	private String dbRFActive;
	private String dbReportFilename;
	private String dbAutoLabelCommandFile;
	private String dbAutoLabelLabelFile;
	private String dbReportType;
	private String dbExecFilename;
	private String dbExecDir;
	private String dbPrintPreview;
	private String dbPrintDialog;
	private int dbPrintCopies;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBModule.class);
	public static int field_module_id = 35;
	public static int field_rf_active = 1;
	public static int field_dk_active = 1;
	public static int field_module_type = 10;
	public static int field_hint = 80;
	public static int field_mneumonic = 1;
	public static int field_icon_filename = 40;
	public static int field_helpset_id = 250;
	public static int field_report_filename = 80;
	public static int field_exec_filename = 80;
	public static int field_exec_dir = 80;
	public static int field_report_type = 50;
	public static int field_resource_key = 50;
	public static int field_autolabeller_command_file = 80;
	public static int field_autolabeller_label_file = 80;

	private JDBLanguage lang;

	private String hostID;

	private String sessionID;

	public static Icon getModuleIcon16x16(String filename, String moduleType)
	{

		Icon icon = new ImageIcon();

		try
		{
			if (filename == null)
			{
				filename = "";
			}

			if (filename.compareTo("") == 0)
			{

				icon = new ImageIcon(Common.image_blank_icon);

				if (moduleType.equals("MENU"))
				{
					icon = Common.imageIconloader.getImageIcon16x16(Common.image_menu);
				}
				if (moduleType.equals("FORM"))
				{
					icon = Common.imageIconloader.getImageIcon16x16(Common.image_form);
				}
				if (moduleType.equals("SCANNER"))
				{
					icon = Common.imageIconloader.getImageIcon16x16(Common.image_scanner);
				}
				if (moduleType.equals("REPORT"))
				{
					icon = Common.imageIconloader.getImageIcon16x16(Common.image_report);
				}
				if (moduleType.equals("FUNCTION"))
				{
					icon = Common.imageIconloader.getImageIcon16x16(Common.image_function);
				}
				if (moduleType.equals("EXEC"))
				{
					icon = Common.imageIconloader.getImageIcon16x16(Common.image_execute);
				}
				if (moduleType.equals("USER"))
				{
					icon = Common.imageIconloader.getImageIcon16x16(Common.image_user_report);
				}
			} else
			{
				File file;
				file = new File(Common.image_path_16x16 + filename);
				if (file.exists())
				{
					icon = Common.imageIconloader.getImageIcon16x16(filename);
				} else
				{
					icon = new ImageIcon(Common.image_error);
				}
			}

		} catch (Exception e)
		{
		}
		return icon;
	}
	
	public static Icon getModuleIcon24x24(String filename, String moduleType)
	{

		Icon icon = new ImageIcon();

		try
		{
			if (filename == null)
			{
				filename = "";
			}

			if (filename.compareTo("") == 0)
			{

				icon = new ImageIcon(Common.image_blank_icon);

				if (moduleType.equals("MENU"))
				{
					icon = Common.imageIconloader.getImageIcon24x24(Common.image_menu);
				}
				if (moduleType.equals("FORM"))
				{
					icon = Common.imageIconloader.getImageIcon24x24(Common.image_form);
				}
				if (moduleType.equals("SCANNER"))
				{
					icon = Common.imageIconloader.getImageIcon24x24(Common.image_scanner);
				}
				if (moduleType.equals("REPORT"))
				{
					icon = Common.imageIconloader.getImageIcon24x24(Common.image_report);
				}
				if (moduleType.equals("FUNCTION"))
				{
					icon = Common.imageIconloader.getImageIcon24x24(Common.image_function);
				}
				if (moduleType.equals("EXEC"))
				{
					icon = Common.imageIconloader.getImageIcon24x24(Common.image_execute);
				}
				if (moduleType.equals("USER"))
				{
					icon = Common.imageIconloader.getImageIcon24x24(Common.image_user_report);
				}
			} else
			{
				File file;
				file = new File(Common.image_path_24x24 + filename);
				if (file.exists())
				{
					icon = Common.imageIconloader.getImageIcon24x24(filename);
				} else
				{
					icon = new ImageIcon(Common.image_error);
				}
			}

		} catch (Exception e)
		{
		}
		return icon;
	}
	
	public static Icon getModuleIcon32x32(String filename, String moduleType)
	{

		Icon icon = new ImageIcon();

		try
		{
			if (filename == null)
			{
				filename = "";
			}

			if (filename.compareTo("") == 0)
			{

				icon = new ImageIcon(Common.image_blank_icon);

				if (moduleType.equals("MENU"))
				{
					icon = Common.imageIconloader.getImageIcon32x32(Common.image_menu);
				}
				if (moduleType.equals("FORM"))
				{
					icon = Common.imageIconloader.getImageIcon32x32(Common.image_form);
				}
				if (moduleType.equals("SCANNER"))
				{
					icon = Common.imageIconloader.getImageIcon32x32(Common.image_scanner);
				}
				if (moduleType.equals("REPORT"))
				{
					icon = Common.imageIconloader.getImageIcon32x32(Common.image_report);
				}
				if (moduleType.equals("FUNCTION"))
				{
					icon = Common.imageIconloader.getImageIcon32x32(Common.image_function);
				}
				if (moduleType.equals("EXEC"))
				{
					icon = Common.imageIconloader.getImageIcon32x32(Common.image_execute);
				}
				if (moduleType.equals("USER"))
				{
					icon = Common.imageIconloader.getImageIcon32x32(Common.image_user_report);
				}
			} else
			{
				File file;
				file = new File(Common.image_path_32x32 + filename);
				if (file.exists())
				{
					icon = Common.imageIconloader.getImageIcon32x32(filename);
				} else
				{
					icon = new ImageIcon(Common.image_error);
				}
			}

		} catch (Exception e)
		{
		}
		return icon;
	}

	public static LinkedList<JDBListData> moveElementDown(LinkedList<JDBListData> list, JDBListData element)
	{
		int position;
		int size;

		size = list.size();

		if (size > 0)
		{
			position = list.indexOf(element);
			if (position < (size - 1))
			{
				list.remove(position);
				list.add(position + 1, element);

			}
		}
		return list;
	}

	public static LinkedList<JDBListData> moveElementUp(LinkedList<JDBListData> list, JDBListData element)
	{
		int position;
		int size;

		size = list.size();

		if (size > 0)
		{
			position = list.indexOf(element);
			if (position > 0)
			{
				list.remove(position);
				list.add(position - 1, element);
			}
		}
		return list;
	}

	public JDBModule(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		lang = new JDBLanguage(host, session);
	}

	public boolean addGroup(String lGroupId)
	{
		boolean result = false;
		try
		{
			if (isValidModuleId() == true)
			{
				PreparedStatement stmtupdate;

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.addGroup"));
				stmtupdate.setString(1, lGroupId);
				stmtupdate.setString(2, getModuleId());
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

	public void clear()
	{
		setResourceKey("");
		setDKActive("");
		setHelpSetID("");
		setIconFilename("");
		setReportFilename("");
		setAutoLabelCommandFilename("");
		setAutoLabelLabelFilename("");
		setReportType("");
		setExecFilename("");
		setExecDir("");
		setType("");
		setRFActive("");
	}

	public boolean create(String lModuleId, String lresourceKey, String lDKActive, String lRFActive, String ltype, String lIconFilename, String lhelpsetid)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setModuleId(lModuleId);
			setResourceKey(lresourceKey);
			setDKActive(lDKActive);
			setRFActive(lRFActive);
			setType(ltype);

			if (isValidModuleId() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.create"));
				stmtupdate.setString(1, getModuleId());
				stmtupdate.setString(2, getResourceKey());
				stmtupdate.setString(3, getDKActive());
				stmtupdate.setString(4, getRFActive());
				stmtupdate.setString(5, getType());
				stmtupdate.setString(6, getHelpSetID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Module Id ["+lModuleId+"] already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	/**
	 * Method delete.
	 * 
	 * @return boolean
	 */
	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidModuleId() == true)
			{

				JDBGroupPermissions gp = new JDBGroupPermissions(getHostID(), getSessionID());
				gp.deletePermissionsForModule(getModuleId());

				JDBMenus men = new JDBMenus(getHostID(), getSessionID());
				men.deleteMenusForModuleId(getModuleId());

				men.deleteMenusForMenuId(getModuleId());

				JDBToolbar tb = new JDBToolbar(getHostID(), getSessionID());
				tb.setModuleId(getModuleId());
				tb.delete();

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.delete"));
				stmtupdate.setString(1, getModuleId());
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

	public String getAutoLabelLabelFilename()
	{
		return dbAutoLabelLabelFile.toLowerCase();
	}

	public String getDescription()
	{
		return dbDescription;
	}

	public String getDKActive()
	{
		return dbDKActive;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getExecDir()
	{
		return dbExecDir;
	}

	public String getExecFilename()
	{
		return dbExecFilename;
	}

	public LinkedList<String> getFormIds()
	{
		LinkedList<String> moduleList = new LinkedList<String>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getFormIds"));
			rs = stmt.executeQuery();
			while (rs.next())
			{
				moduleList.addLast(rs.getString("module_id"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public ResultSet getGroupsAssignedDataResultSet(String module)
	{

		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");
		setModuleId(module);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getGroupsAssigned"));
			stmt.setString(1, getModuleId());
			rs = stmt.executeQuery();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public LinkedList<String> getGroupsAssigned(String module)
	{
		setModuleId(module);
		return getGroupsAssigned();
	}

	public LinkedList<String> getGroupsAssigned()
	{
		LinkedList<String> moduleList = new LinkedList<String>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getGroupsAssigned"));
			stmt.setString(1, getModuleId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				moduleList.addLast(rs.getString("group_id"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public LinkedList<String> getGroupsUnAssigned()
	{
		LinkedList<String> moduleList = new LinkedList<String>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getGroupsUnAssigned"));
			stmt.setString(1, getModuleId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				moduleList.addLast(rs.getString("group_id"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public String getHelpSetID()
	{
		return dbHelpsetId;
	}

	public String getHint()
	{
		return getDescription();
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getIconFilename()
	{
		return dbIconFilename.toLowerCase();
	}

	public String getLabelCommandFilename()
	{
		return dbAutoLabelCommandFile.toLowerCase();
	}

	public LinkedList<JDBListData> getMenuIds()
	{
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();
		Icon icon;
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getMenuIds"));
			rs = stmt.executeQuery();
			while (rs.next())
			{
				icon = getModuleIcon16x16(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, 0, true, rs.getString("module_id"));
				moduleList.addLast(mld);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public char getMnemonicChar()
	{
		return dbMnemonicChar;
	}

	public String getMnemonicStr()
	{
		return dbMnemonicStr;
	}

	public ResultSet getModuleData()
	{
		Statement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
			stmt.setFetchSize(250);
			rs = stmt.executeQuery(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModuleIds"));

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public ResultSet getModuleDataByType(String module_type)
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModuleIdsByType"));
			stmt.setString(1, module_type);
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public String getModuleId()
	{
		return dbModuleId;
	}

	public LinkedList<JDBListData> getModuleIds()
	{
		LinkedList<JDBListData> modList = new LinkedList<JDBListData>();
		Statement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
			stmt.setFetchSize(250);
			rs = stmt.executeQuery(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModuleIds"));

			while (rs.next())
			{
				icon = getModuleIcon16x16(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, index, true, rs.getString("module_id"));
				modList.addLast(mld);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return modList;
	}

	public LinkedList<JDBListData> getModuleIdsByType(String module_type)
	{
		LinkedList<JDBListData> groupList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModuleIdsByType"));
			stmt.setString(1, module_type);
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				icon = getModuleIcon16x16(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, index, true, rs.getString("module_id"));
				groupList.addLast(mld);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return groupList;
	}

	public JDBListData getModuleJDBListData()
	{

		Icon icon;
		icon = getModuleIcon16x16(getIconFilename(), getType());
		JDBListData result = new JDBListData(icon, 0, true, getModuleId());
		return result;
	}

	public JDBListData getModuleJDBListData(String module_id)
	{
		setModuleId(module_id);
		getModuleProperties();
		return getModuleJDBListData();
	}

	public boolean getModuleProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModuleProperties"));
			stmt.setString(1, getModuleId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setResourceKey(rs.getString("resource_key"));
				setDKActive(rs.getString("dk_active"));
				setRFActive(rs.getString("rf_active"));
				setType(rs.getString("module_type"));
				setHelpSetID(rs.getString("helpset_id"));
				setIconFilename(rs.getString("icon_filename"));
				setReportFilename(rs.getString("report_filename"));
				setAutoLabelCommandFilename(rs.getString("auto_labeller_command_file"));
				setAutoLabelLabelFilename(rs.getString("auto_labeller_label_file"));
				setReportType(rs.getString("report_type"));
				setExecFilename(rs.getString("exec_filename"));
				setExecDir(rs.getString("exec_dir"));
				setPrintPreview(rs.getString("print_preview"));
				setPrintDialog(rs.getString("print_dialog"));
				setPrintCopies(rs.getInt("print_copies"));
				result = true;
			} else
			{
				setErrorMessage("Invalid ModuleId [" + getModuleId() + "]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getModuleProperties(String mod)
	{
		setModuleId(mod);
		return getModuleProperties();
	}

	public LinkedList<JDBListData> getModulesAssignedtoMenu()
	{
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();
		Icon icon;
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModulesAssignedtoMenu"));
			stmt.setString(1, getModuleId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				icon = getModuleIcon16x16(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, 0, true, rs.getString("module_id"));
				moduleList.addLast(mld);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public LinkedList<JDBListData> getModulesAssignedtoRFMenu()
	{
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();
		Icon icon;
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModulesAssignedtoRFMenu"));
			rs = stmt.executeQuery();
			while (rs.next())
			{
				icon = getModuleIcon16x16(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, 0, true, rs.getString("module_id"));
				moduleList.addLast(mld);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public LinkedList<JDBListData> getModulesAssignedtoToolbar()
	{
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();
		Icon icon;
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModulesAssignedtoToolbar"));
			rs = stmt.executeQuery();
			while (rs.next())
			{
				icon = getModuleIcon16x16(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, 0, true, rs.getString("module_id"));
				moduleList.addLast(mld);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public LinkedList<JDBListData> getModulesofTypeforUser(String host, String session, String type)
	{
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();
		Icon icon;
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setHostID(host);
		setSessionID(session);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModulesofTypeforUser"));
			stmt.setString(1, type);
			stmt.setString(2, Common.userList.getUser(getSessionID()).getUserId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				JMenuOption mo = new JMenuOption(getHostID(), getSessionID());
				mo.load(rs);
				icon = getModuleIcon16x16(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, 0, true, mo);
				moduleList.addLast(mld);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public LinkedList<JDBListData> getModulesUnAssignedtoMenu()
	{
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();
		Icon icon;
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModulesUnAssignedtoMenu"));
			stmt.setString(1, getModuleId());
			stmt.setString(2, getModuleId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				icon = getModuleIcon16x16(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, 0, true, rs.getString("module_id"));
				moduleList.addLast(mld);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public LinkedList<JDBListData> getModulesUnAssignedtoRFMenu()
	{
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();
		Icon icon;
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModulesUnAssignedtoRFMenu"));
			rs = stmt.executeQuery();
			while (rs.next())
			{
				icon = getModuleIcon16x16(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, 0, true, rs.getString("module_id"));
				moduleList.addLast(mld);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public LinkedList<JDBListData> getModulesUnAssignedtoToolbar()
	{
		LinkedList<JDBListData> moduleList = new LinkedList<JDBListData>();
		Icon icon;
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.getModulesUnAssignedtoToolbar"));
			rs = stmt.executeQuery();
			while (rs.next())
			{
				icon = getModuleIcon16x16(rs.getString("icon_filename"), rs.getString("module_type"));
				JDBListData mld = new JDBListData(icon, 0, true, rs.getString("module_id"));
				moduleList.addLast(mld);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return moduleList;
	}

	public Integer getPrintCopies()
	{
		if (dbPrintCopies == 0)
		{
			dbPrintCopies = 1;
		}
		return dbPrintCopies;
	}

	public String getPrintDialog()
	{
		if (dbPrintDialog == null)
		{
			dbPrintDialog = "N";
		}
		return dbPrintDialog;
	}

	public String getPrintPreview()
	{
		if (dbPrintPreview == null)
		{
			dbPrintPreview = "Y";
		}
		return dbPrintPreview;
	}

	public String getReportFilename()
	{
		return dbReportFilename.toLowerCase();
	}

	public String getReportType()
	{
		if (JUtility.replaceNullStringwithBlank(dbReportType).equals(""))
		{
			if (getType().equals("REPORT"))
			{
				dbReportType = "Standard";
			} else
			{
				dbReportType = "";
			}
		}
		return dbReportType;
	}

	public String getResourceKey()
	{
		return JUtility.replaceNullStringwithBlank(dbResourceKey);
	}

	public String getRFActive()
	{
		return dbRFActive;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getType()
	{
		return dbModuleType;
	}

	public boolean isDKModule()
	{
		boolean result = false;

		if (getDKActive().equals("Y"))
			result = true;
		else
			result = false;

		return result;
	}

	public boolean isPrintDialog()
	{
		boolean result = false;

		if (getPrintDialog().equals("Y"))
			result = true;
		else
			result = false;

		return result;
	}

	public boolean isPrintPreview()
	{
		boolean result = false;

		if (getPrintPreview().equals("Y"))
			result = true;
		else
			result = false;

		return result;
	}

	public boolean isRFModule()
	{
		boolean result = false;

		if (getRFActive().equals("Y"))
			result = true;
		else
			result = false;

		return result;
	}

	public boolean isValidModuleId()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.isValidModuleId"));
			stmt.setFetchSize(1);
			stmt.setString(1, getModuleId());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid ModuleId");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public boolean removeGroup(String lgroup_id)
	{
		boolean result = false;
		try
		{
			if (isValidModuleId() == true)
			{
				PreparedStatement stmtupdate;

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.removeGroup"));
				stmtupdate.setString(1, getModuleId());
				stmtupdate.setString(2, lgroup_id);
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

	public boolean renameTo(String newModuleId)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidModuleId() == true)
			{
				JDBModule newmod = new JDBModule(getHostID(), getSessionID());
				newmod.setModuleId(newModuleId);
				if (newmod.isValidModuleId() == false)
				{
					JDBGroupPermissions gp = new JDBGroupPermissions(getHostID(), getSessionID());
					gp.setModuleId(getModuleId());
					gp.renameModuleTo(newModuleId);

					JDBToolbar tb = new JDBToolbar(getHostID(), getSessionID());
					tb.setModuleId(getModuleId());
					tb.renameModuleTo(newModuleId);

					JDBRFMenu rf = new JDBRFMenu(getHostID(), getSessionID());
					rf.setModuleId(getModuleId());
					rf.renameModuleTo(newModuleId);

					JDBMenus men = new JDBMenus(getHostID(), getSessionID());
					men.setModuleId(getModuleId());
					men.renameModuleTo(newModuleId);

					men.setMenuId(getModuleId());
					men.renameMenuTo(newModuleId);

					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.renameTo"));
					stmtupdate.setString(1, newModuleId);
					stmtupdate.setString(2, getModuleId());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setModuleId(newModuleId);
					result = true;
				} else
				{
					setErrorMessage("New module_id is already in use.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public void setAutoLabelCommandFilename(String CommandFilename)
	{
		if (CommandFilename == null)
		{
			CommandFilename = "";
		}
		dbAutoLabelCommandFile = CommandFilename;
	}

	public void setAutoLabelLabelFilename(String LabelFilename)
	{
		if (LabelFilename == null)
		{
			LabelFilename = "";
		}
		dbAutoLabelLabelFile = LabelFilename;
	}

	public void setDKActive(String Active)
	{
		dbDKActive = Active;
	}

	private void setErrorMessage(String ErrorMsg)
	{
		if (ErrorMsg.isEmpty() == false)
		{
			logger.error(ErrorMsg);
		}
		dbErrorMessage = ErrorMsg;
	}

	public void setExecDir(String ExecDir)
	{
		if (ExecDir == null)
		{
			ExecDir = "";
		}
		dbExecDir = ExecDir;
	}

	public void setExecFilename(String ExecFilename)
	{
		if (ExecFilename == null)
		{
			ExecFilename = "";
		}
		dbExecFilename = ExecFilename;
	}

	public void setHelpSetID(String helpsetid)
	{
		dbHelpsetId = helpsetid;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setIconFilename(String IconFilename)
	{
		if (IconFilename == null)
		{
			IconFilename = "";
		}
		dbIconFilename = IconFilename;
	}

	public void setModuleId(String GroupId)
	{
		dbModuleId = GroupId;
	}

	public void setPrintCopies(Integer Copies)
	{
		dbPrintCopies = Copies;
	}

	public void setPrintDialog(String Dialog)
	{
		dbPrintDialog = Dialog;
	}

	public void setPrintPreview(String Preview)
	{
		dbPrintPreview = Preview;
	}

	public void setReportFilename(String ReportFilename)
	{
		if (ReportFilename == null)
		{
			ReportFilename = "";
		}
		dbReportFilename = ReportFilename;
	}

	public void setReportType(String ReportType)
	{
		if (ReportType == null)
		{
			ReportType = "";
		}
		dbReportType = ReportType;
	}

	public void setResourceKey(String key)
	{
		dbResourceKey = key;
		dbDescription = lang.get(key);
		dbMnemonicStr = lang.getMnemonicString();
		dbMnemonicChar = lang.getMnemonicChar();
	}

	public void setRFActive(String Active)
	{
		dbRFActive = Active;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setType(String Type)
	{
		dbModuleType = Type;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidModuleId() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBModule.update"));
				stmtupdate.setString(1, getResourceKey());
				stmtupdate.setString(2, getDKActive());
				stmtupdate.setString(3, getRFActive());
				stmtupdate.setString(4, getType());
				stmtupdate.setString(5, getHint());
				stmtupdate.setString(6, getIconFilename());
				stmtupdate.setString(7, getHelpSetID());
				stmtupdate.setString(8, getReportFilename());
				stmtupdate.setString(9, getExecFilename());
				stmtupdate.setString(10, getExecDir());
				stmtupdate.setString(11, getPrintPreview());
				stmtupdate.setString(12, getPrintDialog());
				stmtupdate.setInt(13, getPrintCopies());
				stmtupdate.setString(14, getReportType());
				stmtupdate.setString(15, getLabelCommandFilename());
				stmtupdate.setString(16, getAutoLabelLabelFilename());
				stmtupdate.setString(17, getModuleId());
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
