package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBUserReport.java
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

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.log4j.Logger;

import com.opencsv.CSVWriter;

import com.commander4j.email.JeMail;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JExcel;
import com.commander4j.util.JFileFilterCSV;
import com.commander4j.util.JFileFilterMDB;
import com.commander4j.util.JFileFilterPDF;
import com.commander4j.util.JFileFilterXLS;
import com.commander4j.util.JUtility;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.util.ImportUtil;

public class JDBUserReport {
	
	private String 	dbReportID;
	private Integer dbSequence;
	private String 	dbDescription;
	private String 	dbSQL;
	private String 	dbModuleID;
	private String 	dbDestination;
	private String 	dbEnabled;
	private String 	dbPreview;
	private String 	dbPromptSaveAs;
	private String 	dbEmailEnabled;
	private String 	dbPromptEmailEnabled;
	private String 	dbEmailAddresses;
	private String 	dbSavePath;
	private String 	dbUserID;
	private String 	dbPrivate;
	private String 	dbGroupID;
	private String 	dbErrorMessage;
	private final Logger logger = Logger.getLogger(JDBUserReport.class);
	private String 	hostID;
	private String 	sessionID;
	private JDBUserGroupMembership ugm;
	private String 	exportFilename = "";
	private boolean adminUser = false;
	private String 	dbParamDateRange = "";
	private Timestamp 	paramDateFrom;
	private JeMail 	mail;
	private JDBLanguage 	lang ;
	private String systemResultData = ""; 
	private Timestamp paramDateTo;
	private LinkedList<JUserReportParameter> systemParams = new LinkedList<JUserReportParameter>();
	
	public String getResultData()
	{
		return JUtility.replaceNullStringwithBlank(systemResultData);
	}
	
	private void setResultData(String data)
	{
		systemResultData = JUtility.replaceNullStringwithBlank(data);
	}
	
	public void setSYSTEMparameters(LinkedList<JUserReportParameter> params)
	{
		systemParams = params;
	}
	
	private boolean generateSYSTEM(ResultSet temp)
	{
		boolean result = true;

		try
		{			
			if (temp.next())
			{
				ResultSetMetaData rsmd = temp.getMetaData();
				int colcount = rsmd.getColumnCount();
				boolean dataColumnFound = false;
				for (int x=0;x<colcount;x++)
				{
					if (rsmd.getColumnName(x).toLowerCase().equals("data"))
					{
						dataColumnFound=true;
					}
				}
				if (dataColumnFound)
				{
					setResultData(temp.getString("data"));
					result = true;
				}
				else
				{
					setErrorMessage("No field called 'data' returned by query.");
				}

			} else
			{
				setErrorMessage("No record returned by query.");
			}

		} catch (Exception e)
		{
			result = false;
			setErrorMessage(e.getMessage());
		}
		return result;
	}
	
	public static Icon getUserReportIcon(String enabled, String destination)
	{

		Icon icon = new ImageIcon();

		try
		{
			icon = new ImageIcon(Common.image_blank_icon);

			if (enabled.equals("N"))
			{
				icon = Common.imageIconloader.getImageIcon(Common.image_user_locked);
			} else
			{
				if (destination.equals("JASPER_REPORTS"))
				{
					icon = Common.imageIconloader.getImageIcon(Common.image_jasperreport);
				}
				if (destination.equals("PDF"))
				{
					icon = Common.imageIconloader.getImageIcon(Common.image_pdf);
				}
				if (destination.equals("EXCEL"))
				{
					icon = Common.imageIconloader.getImageIcon(Common.image_XLS);
				}
				if (destination.equals("ACCESS"))
				{
					icon = Common.imageIconloader.getImageIcon(Common.image_msaccess);
				}
				if (destination.equals("CSV"))
				{
					icon = Common.imageIconloader.getImageIcon(Common.image_csv);
				}
				if (destination.equals("SYSTEM"))
				{
					icon = Common.imageIconloader.getImageIcon(Common.image_execute);
				}				
			}
		} catch (Exception e)
		{
		}
		return icon;
	}
	
	public void setEmailPromptEnabled(String enabled)
	{
		dbPromptEmailEnabled = JUtility.replaceNullStringwithBlank(enabled);
	}
	
	public JDBUserReport(String host, String session)
	{

		setHostID(host);
		setSessionID(session);
		ugm = new JDBUserGroupMembership(host, session);
		mail = new JeMail(getHostID(), getSessionID());
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_REPORT_EDIT"))
		{
			adminUser = true;
		}
	}
	
	public JDBUserReport(String reportID, String desc, String dest, String Enabled)
	{
		setReportID(reportID);
		setDescription(desc);
		setDestination(dest);
		setSequence(0);
		setEnabled(Enabled);

	}
	
	public void clear()
	{
		setSequence(0);
		setEnabled("");
		setDescription("");
		setDestination("");
	}
	
	public boolean create()
	{

		boolean result = false;

		if (isValidUserReport() == false)
		{
			logger.debug("create [" + getReportID() + "]");

			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID())
						.prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserReport.create"));
				stmtupdate.setString(1, getReportID());
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
		}

		return result;
	}
	
	public boolean create(String id)
	{
		setReportID(id);
		return create();
	}
	
	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("delete");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserReport.delete"));
			stmtupdate.setString(1, getReportID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}	
	
	public boolean delete(String id)
	{
		boolean result = false;
		setReportID(id);
		result = delete();
		return result;
	}
	
	private boolean generateAccess(ResultSet temp)
	{
		boolean result = true;
		setExportFilename(getExportPath() + getReportID() + "_" + JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()).replace("T", "_").replace("-", "_").replace(":", "_") + ".mdb");
		setExportFilename(promptUserforSave());

		try
		{
			ResultSetMetaData rsmd = temp.getMetaData();
			String tablename = rsmd.getTableName(1);
			Database db = DatabaseBuilder.create(Database.FileFormat.V2007, new File(getExportFilename()));
			db = DatabaseBuilder.open(new File(getExportFilename()));
			new ImportUtil.Builder(db, tablename).importResultSet(temp);
			db.close();

		} catch (Exception e)
		{
			result = false;
			setErrorMessage(e.getMessage());
		}
		return result;
	}
	
	private boolean generateCSV(ResultSet temp)
	{
		boolean result = true;
		setExportFilename(getExportPath() + getReportID() + "_" + JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()).replace("T", "_").replace("-", "_").replace(":", "_") + ".csv");

		setExportFilename(promptUserforSave());

		try
		{
			CSVWriter writer = new CSVWriter(new FileWriter(getExportFilename()), ',');
			writer.writeAll(temp, true);
			writer.close();
		} catch (Exception e)
		{
			result = false;
			setErrorMessage(e.getMessage());
		}
		return result;
	}
	
	private boolean generateExcel(ResultSet temp)
	{
		boolean result = true;
		JExcel export = new JExcel();
		setExportFilename(getExportPath() + getReportID() + "_" + JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()).replace("T", "_").replace("-", "_").replace(":", "_") + ".xls");
		setExportFilename(promptUserforSave());
		export.exportToExcel(getExportFilename(), temp);
		return result;
	}
	
	private boolean generateJasper(PreparedStatement temp)
	{
		boolean result = true;

		// JLaunchReport.runReport(getModuleID(), null, "", temp, "");

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("p_title", getDescription());
		parameters.put("p_from_date", getParamFromDate());
		parameters.put("p_to_date", getParamToDate());
		JLaunchReport.runReport(getModuleID(), parameters, "", temp, "");

		return result;
	}

	private boolean generatePDF(PreparedStatement temp)
	{
		boolean result = true;

		setExportFilename(getExportPath() + getReportID() + "_" + JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()).replace("T", "_").replace("-", "_").replace(":", "_") + ".pdf");
		setExportFilename(promptUserforSave());

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("p_title", getDescription());
		parameters.put("p_from_date", getParamFromDate());
		parameters.put("p_to_date", getParamToDate());
		JLaunchReport.runReportToPDF(getModuleID(), parameters, "", temp, getExportFilename());

		return result;
	}

	public String getDescription()
	{
		dbDescription = JUtility.replaceNullStringwithBlank(dbDescription);
		if (dbDescription.equals(""))
		{
			dbDescription = "New Report " + getReportID();
		}
		return dbDescription;
	}

	public String getDestination()
	{
		dbDestination = JUtility.replaceNullStringwithBlank(dbDestination);
		if (dbDestination.equals(""))
		{
			dbDestination = "EXCEL";
		}
		return dbDestination;
	}

	public String getEmailAddress()
	{
		return JUtility.replaceNullStringwithBlank(dbEmailAddresses);
	}

	public String getEmailAddresses()
	{
		return JUtility.replaceNullStringwithBlank(dbEmailAddresses);
	}

	public String getEmailEnabled()
	{
		return  JUtility.replaceNullStringwithBlank(dbEmailEnabled);
	}

	public String getEmailPromptEnabled()
	{
		return  JUtility.replaceNullStringwithBlank(dbPromptEmailEnabled);
	}

	public String getEnabled()
	{
		dbEnabled = JUtility.replaceNullStringwithBlank(dbEnabled);
		if (dbEnabled.equals(""))
			dbEnabled = "Y";
		return dbEnabled;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getExportFilename()
	{
		return exportFilename;
	}

	private String getExportPath()
	{
		String path = "";
		if (getSavePath().equals(""))
		{
			path = System.getProperty("user.home");
		} else
		{
			path = getSavePath();
		}
		path = path + File.separator;
		return path;
	}

	public String getGroupID()
	{
		return JUtility.replaceNullStringwithBlank(dbGroupID);
	}

	public String getHostID()
	{
		return hostID;
	}

	public String getModuleID()
	{
		dbModuleID = JUtility.replaceNullObjectwithBlank(dbModuleID);
		return dbModuleID;
	}

	public String getParamDateRangeRequired()
	{
		dbParamDateRange = JUtility.replaceNullStringwithBlank(dbParamDateRange);
		if (dbParamDateRange.equals(""))
			dbParamDateRange = "Y";
		return dbParamDateRange;
	}

	private Timestamp getParamFromDate()
	{
		return paramDateFrom;
	}

	private Timestamp getParamToDate()
	{
		return paramDateTo;
	}

	public String getPreview()
	{
		dbPreview = JUtility.replaceNullStringwithBlank(dbPreview);
		if (dbPreview.equals(""))
			dbPreview = "Y";
		return dbPreview;
	}

	private String getPrivacy()
	{
		String result = "";
		if (adminUser)
		{
			if (isPrivate())
			{
				if (getUserID().equals("") == false)
				{
					result = "(user=" + getUserID() + ")";
				}
				if (getGroupID().equals("") == false)
				{
					result = result + " (group=" + getGroupID() + ")";
				}
			}
			if (isEnabled() == false)
			{
				result = result + " (disabled)";
			}
		}
		return result;
	}

	public String getPrivate()
	{
		dbPrivate = JUtility.replaceNullStringwithBlank(dbPrivate);
		if (dbPrivate.equals(""))
			dbPrivate = "Y";
		return dbPrivate;
	}

	public String getPromptSaveAs()
	{
		dbPromptSaveAs = JUtility.replaceNullStringwithBlank(dbPromptSaveAs);
		if (dbPromptSaveAs.equals(""))
			dbPromptSaveAs = "Y";
		return JUtility.replaceNullStringwithBlank(dbPromptSaveAs);
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setReportID(rs.getString("USER_REPORT_ID"));
			setSequence(rs.getInt("DISPLAY_SEQUENCE"));
			setDescription(rs.getString("DESCRIPTION"));
			setSQL(rs.getString("SQL_SELECT"));
			setModuleID(rs.getString("MODULE_ID"));
			setDestination(rs.getString("DESTINATION"));
			setEnabled(rs.getString("ENABLED"));
			setPrivate(rs.getString("PRIVATE"));
			setUserID(rs.getString("USER_ID"));
			setGroupID(rs.getString("GROUP_ID"));
			setSavePath(rs.getString("SAVE_PATH"));
			setPreview(rs.getString("PREVIEW"));
			setPromptSaveAs(rs.getString("PROMPT_SAVE_AS"));
			setParamDateRangeRequired(rs.getString("PARAM_DATE_RANGE"));
			setEmailEnabled(rs.getString("EMAIL_ENABLED"));
			setEmailPromptEnabled(rs.getString("EMAIL_PROMPT_ENABLED"));
			setEmailAddresses(rs.getString("EMAIL_ADDRESSES"));			
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getReportID()
	{
		return dbReportID;
	}

	public String getSavePath()
	{
		return JUtility.replaceNullStringwithBlank(dbSavePath);
	}

	public Integer getSequence()
	{
		if (dbSequence == null)
			dbSequence = 0;
		return dbSequence;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public String getSQL()
	{
		dbSQL = JUtility.replaceNullStringwithBlank(dbSQL);
		if (dbSQL.equals(""))
		{
			dbSQL = "SELECT FIELD1,FIELD2\n   FROM MYTABLE\n   WHERE FIELD1 >= ? AND FIELD1 <= ?\n   ORDER BY FIELD3 DESC";
		}
		return dbSQL;
	}

	public String getUserID()
	{
		dbUserID = JUtility.replaceNullStringwithBlank(dbUserID);
		if (dbUserID.equals("") && getPrivate().equals("Y"))
		{
			dbUserID = Common.userList.getUser(Common.sessionID).getUserId();
		}
		return dbUserID;
	}

	public Vector<JDBUserReport> getUserReportData(PreparedStatement criteria)
	{

		ResultSet rs;
		Vector<JDBUserReport> result = new Vector<JDBUserReport>();

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBUserReport("Report ID", "Description", "Destination", "Enabled"));
		} else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					result.addElement(new JDBUserReport(rs.getString("USER_REPORT_ID"), rs.getString("DESCRIPTION"), rs.getString("DESTINATION"), rs.getString("ENABLED")));
				}
				rs.close();

			} catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public LinkedList<JDBListData> getUserReportIds()
	{
		LinkedList<JDBListData> groupUserReportList = new LinkedList<JDBListData>();
		Statement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		boolean show = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
			stmt.setFetchSize(250);
			rs = stmt.executeQuery(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserReport.getUserReportIDs"));

			while (rs.next())
			{
				// TO DO
				// IF user is private check username and group membership.
				if (adminUser)
				{
					show = true;
				} else
				{
					show = false;
					if (rs.getString("PRIVATE").equals("N"))
					{
						show = true;
					} else
					{
						if (rs.getString("USER_ID").equals(Common.userList.getUser(getSessionID()).getUserId()))
						{
							show = true;
						}

						ugm.setUserId(Common.userList.getUser(getSessionID()).getUserId());
						ugm.setGroupId(rs.getString("GROUP_ID"));

						if (ugm.isValidUserGroupMembership())
						{
							show = true;
						}

					}
				}

				if (show == true)
				{
					JDBUserReport ur = new JDBUserReport(getHostID(), getSessionID());
					ur.getPropertiesfromResultSet(rs);
					icon = getUserReportIcon(rs.getString("ENABLED"), rs.getString("DESTINATION"));
					JDBListData mld = new JDBListData(icon, index, true, ur);

					groupUserReportList.addLast(mld);
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return groupUserReportList;
	}

	public boolean getUserReportProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserReport.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getReportID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid User Report ID");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public boolean getUserReportProperties(String id)
	{
		setReportID(id);
		return getUserReportProperties();
	}

	public boolean isEmailEnabled()
	{
		boolean result = false;
		if (getEmailEnabled().equals("Y"))
		{
			result = true;
		}
		return result;
	}

	public boolean isEmailPromptEnabled()
	{
		boolean result = false;
		if (getEmailPromptEnabled().equals("Y"))
		{
			result = true;
		}
		return result;
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

	public boolean isParamDateRequired()
	{
		boolean result = false;
		if (getParamDateRangeRequired().equals("Y"))
		{
			result = true;
		}
		return result;
	}

	public boolean isPreviewRequired()
	{
		boolean result = false;
		if (getPreview().equals("Y") == true)
		{
			result = true;
		}
		return result;
	}

	public boolean isPrivate()
	{
		boolean result = false;
		if (getPrivate().equals("Y"))
		{
			result = true;
		}
		return result;
	}

	public boolean isPromptSaveAsRequired()
	{
		boolean result = false;

		if (getPromptSaveAs().equals("Y") == true)
		{
			result = true;
		}

		return result;
	}

	public boolean isValidUserReport()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserReport.isValid"));
			stmt.setString(1, getReportID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
				setErrorMessage("Report ID [" + getReportID() + "] exists");
			} else
			{
				setErrorMessage("Invalid User Report ID");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		logger.debug("isValidUserReport :" + result);

		return result;
	}

	public boolean isValidUserReport(String id)
	{
		setReportID(id);
		return isValidUserReport();
	}

	private String promptUserforSave()
	{
		JFileChooser saveTYPE = new JFileChooser();
		String filename = getExportFilename();
		if (isPromptSaveAsRequired())
		{

			try
			{
				File f = new File(new File(getExportFilename()).getCanonicalPath());
				saveTYPE.setCurrentDirectory(f);
				if (getDestination().equals("EXCEL"))
					saveTYPE.addChoosableFileFilter(new JFileFilterXLS());
				if (getDestination().equals("JASPER_REPORT"))
					saveTYPE.addChoosableFileFilter(new JFileFilterPDF());
				if (getDestination().equals("PDF"))
					saveTYPE.addChoosableFileFilter(new JFileFilterPDF());
				if (getDestination().equals("CSV"))
					saveTYPE.addChoosableFileFilter(new JFileFilterCSV());
				if (getDestination().equals("ACCESS"))
					saveTYPE.addChoosableFileFilter(new JFileFilterMDB());
				saveTYPE.setSelectedFile(new File(getExportFilename()));

				int result = saveTYPE.showSaveDialog(Common.mainForm);
				if (result == 0)
				{
					File selectedFile;
					selectedFile = saveTYPE.getSelectedFile();
					if (selectedFile != null)
					{
						filename = selectedFile.getAbsolutePath();
					}
				}
			} catch (Exception ex)
			{
			}
		}
		return filename;
	}

	public boolean runReport()
	{
		PreparedStatement prepStatement;
		boolean result = true;

		try
		{
			prepStatement = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(getSQL(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			prepStatement.setFetchSize(25);

			if (isParamDateRequired())
			{
				prepStatement.setTimestamp(1, getParamFromDate());
				prepStatement.setTimestamp(2, getParamToDate());
			}

			if (getDestination().equals("SYSTEM"))
			{
				for (int x=0;x<systemParams.size();x++)
				{
					String type = systemParams.get(x).parameterType;
					
					if (type.toLowerCase().equals("string"))
					{
						prepStatement.setString(systemParams.get(x).parameterPosition,(String) systemParams.get(x).parameterValue);
					}
					if (type.toLowerCase().equals("integer"))
					{
						prepStatement.setInt(systemParams.get(x).parameterPosition,(Integer) systemParams.get(x).parameterValue);
					}
					if (type.toLowerCase().equals("timestamp"))
					{
						prepStatement.setTimestamp(systemParams.get(x).parameterPosition,(Timestamp) systemParams.get(x).parameterValue);
					}
				}
			}
			
			ResultSet tempResult = prepStatement.executeQuery();

			boolean dataReturned = true;
			if (!tempResult.next())
			{
				dataReturned = false;
			}
			tempResult.beforeFirst();
			if (dataReturned)
			{
				if (getDestination().equals("EXCEL"))
				{
					generateExcel(tempResult);
				}

				if (getDestination().equals("JASPER_REPORTS"))
				{
					generateJasper(prepStatement);
				}
				if (getDestination().equals("PDF"))
				{
					generatePDF(prepStatement);
				}
				if (getDestination().equals("ACCESS"))
				{
					generateAccess(tempResult);
				}

				if (getDestination().equals("CSV"))
				{
					generateCSV(tempResult);
				}
				
				if (getDestination().equals("SYSTEM"))
				{
					generateSYSTEM(tempResult);
				}

				if (isPreviewRequired())
				{
					try
					{
						Desktop.getDesktop().open(new File(getExportFilename()));
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				
				if (isEmailEnabled())
				{
					
					String emailaddresses = getEmailAddresses();
					
					if (isEmailPromptEnabled())
					{
						emailaddresses = JUtility.replaceNullStringwithBlank(JOptionPane.showInputDialog(lang.get("lbl_Email_Addresses") ));
					}
					
					StringConverter stringConverter = new StringConverter();
					ArrayConverter arrayConverter = new ArrayConverter(String[].class, stringConverter);
					arrayConverter.setDelimiter(';');
					arrayConverter.setAllowedChars(new char[]{ '@','_' });

					String[] emailList = (String[]) arrayConverter.convert(String[].class, emailaddresses);

					if (emailList.length > 0)
					{
						String shortFilename = JUtility.getFilenameFromPath(getExportFilename());
						mail.postMail(emailList, "Commande4j User Report requested by "+Common.userList.getUser(Common.sessionID).getUserId()+" from [" + Common.hostList.getHost(getHostID()).getSiteDescription() + "] on " + JUtility.getClientName(), "See attached report.\n", shortFilename, getExportFilename());
						com.commander4j.util.JWait.milliSec(2000);
					}
				}

			} else
			{
				result = false;
				setErrorMessage("No data returned by query.");
			}

		} catch (Exception ex)
		{
			setErrorMessage(ex.getMessage());
			result = false;
		}

		return result;
	}

	public void setDescription(String desc)
	{
		dbDescription = desc;
	}

	public void setDestination(String destination)
	{
		dbDestination = destination;
	}

	public void setEmailAddresses(String address)
	{
		dbEmailAddresses=JUtility.replaceNullStringwithBlank(address);
	}

	public void setEmailEnabled(Boolean enabled)
	{
		if (enabled == true)
		{
			dbEmailEnabled = "Y";
		} else
		{
			dbEmailEnabled = "N";
		}
	}

	public void setEmailEnabled(String enable)
	{
		dbEmailEnabled = JUtility.replaceNullStringwithBlank(enable);
	}

	public void setEmailPromptEnabled(Boolean enabled)
	{
		if (enabled == true)
		{
			dbPromptEmailEnabled = "Y";
		} else
		{
			dbPromptEmailEnabled = "N";
		}
	}

	public void setEnabled(Boolean enabled)
	{
		if (enabled == true)
		{
			dbEnabled = "Y";
		} else
		{
			dbEnabled = "N";
		}
	}

	public void setEnabled(String enabled)
	{
		if (enabled == null)
		{
			dbEnabled = "N";
		} else
		{
			dbEnabled = enabled.toUpperCase();
		}
	}

	private void setErrorMessage(String errorMsg)
	{
		dbErrorMessage = errorMsg;
	}

	private void setExportFilename(String fn)
	{
		exportFilename = fn;
	}

	public void setGroupID(String dbGroupID)
	{
		this.dbGroupID = dbGroupID;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setModuleID(String id)
	{
		dbModuleID = id;
	}

	public void setParamDateRangeRequired(Boolean enabled)
	{
		if (enabled == true)
		{
			dbParamDateRange = "Y";
		} else
		{
			dbParamDateRange = "N";
		}
	}

	public void setParamDateRangeRequired(String reqd)
	{
		dbParamDateRange = reqd;
	}

	public void setParamFromDate(Timestamp from)
	{
		paramDateFrom = from;
	}

	public void setParamToDate(Timestamp to)
	{
		paramDateTo = to;
	}

	public void setPreview(Boolean enabled)
	{
		if (enabled == true)
		{
			dbPreview = "Y";
		} else
		{
			dbPreview = "N";
		}
	}

	public void setPreview(String dbPreview)
	{
		this.dbPreview = JUtility.replaceNullStringwithBlank(dbPreview);
	}

	public void setPrivate(Boolean enabled)
	{
		if (enabled == true)
		{
			dbPrivate = "Y";
		} else
		{
			dbPrivate = "N";
		}
	}

	public void setPrivate(String dbPrivate)
	{
		this.dbPrivate = dbPrivate;
	}

	public void setPromptEmail(String enable)
	{
		dbPromptEmailEnabled = JUtility.replaceNullStringwithBlank(enable);
	}

	public void setPromptSaveAs(Boolean enabled)
	{
		if (enabled == true)
		{
			dbPromptSaveAs = "Y";
		} else
		{
			dbPromptSaveAs = "N";
		}
	}

	public void setPromptSaveAs(String dbPromptSaveAs)
	{
		this.dbPromptSaveAs = JUtility.replaceNullStringwithBlank(dbPromptSaveAs);
	}

	public void setReportID(String id)
	{
		id = JUtility.replaceNullStringwithBlank(id);
		id = JUtility.left(id, 12);
		dbReportID = id;
	}

	public void setSavePath(String sp)
	{
		this.dbSavePath = JUtility.replaceNullStringwithBlank(sp);
	}

	public void setSequence(Integer seq)
	{
		dbSequence = seq;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setSQL(String sql)
	{
		dbSQL = sql;
	}

	public void setUserID(String dbUserID)
	{
		dbUserID = JUtility.replaceNullStringwithBlank(dbUserID);
		this.dbUserID = dbUserID;
	}

	public String toString()
	{
		return JUtility.padString(getReportID(), true, 12, " ") + " - " + getDescription() + " " + getPrivacy();
	}

	public boolean update()
	{
		boolean result = false;

		logger.debug("update [" + getReportID() + "]");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserReport.update"));

			stmtupdate.setInt(1, getSequence());
			stmtupdate.setString(2, getDescription());
			stmtupdate.setString(3, getSQL());
			stmtupdate.setString(4, getModuleID());
			stmtupdate.setString(5, getDestination());
			stmtupdate.setString(6, getEnabled());
			stmtupdate.setString(7, getPrivate());
			stmtupdate.setString(8, getUserID());
			stmtupdate.setString(9, getGroupID());
			stmtupdate.setString(10, getParamDateRangeRequired());
			stmtupdate.setString(11, getSavePath());
			stmtupdate.setString(12, getPreview());
			stmtupdate.setString(13, getPromptSaveAs());
			stmtupdate.setString(14, getEmailEnabled());
			stmtupdate.setString(15, getEmailPromptEnabled());
			stmtupdate.setString(16, getEmailAddresses());
			stmtupdate.setString(17, getReportID());

			stmtupdate.execute();
			stmtupdate.clearParameters();

			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean updateUserID(String oldUserID,String newUserID)
	{
		boolean result = false;

		logger.debug("updateUserID oldUserID=[" + oldUserID + "] newUserId=["+ newUserID + "]");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserReport.updateUserID"));

			stmtupdate.setString(1, newUserID);
			stmtupdate.setString(2, oldUserID);

			stmtupdate.execute();
			stmtupdate.clearParameters();

			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
}
