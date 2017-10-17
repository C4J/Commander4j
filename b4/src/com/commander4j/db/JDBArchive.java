package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBArchive.java
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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * JDBArchive class is used to insert/update/delete the SYS_ARCHIVE table. The
 * SYS_ARCHIVE table is a user definable auto archiving system. In essence a SQL
 * delete statement with date parameter can be inserted into this table. At a
 * predetermined interval the Archiving Thread executes each statement in this
 * table to remove old/unneeded records from tables.
 * <p>
 * <img alt="" src="./doc-files/SYS_ARCHIVE.jpg" >
 * 
 * @see com.commander4j.sys.JInternalFrameArchiveAdmin JInternalFrameArchiveAdmin
 * @see com.commander4j.sys.JDialogArchiveProperties JDialogArchiveProperties
 */
public class JDBArchive
{

	private final Logger logger = Logger.getLogger(JDBArchive.class);
	private String dbErrorMessage;
	private String hostID;
	private String sessionID;

	private String dbArchiveID;
	private String dbDescription;
	private String dbEnabled;
	private String dbBackgroundTask;
	private String dbSQLTable;
	private String dbSQLCriteria;
	private String dbSQLResult;
	private int dbRetentionDays;
	private int dbSequence;
	private Timestamp dbRunStart;
	private Timestamp dbRunEnd;
	private Long dbRecordsDeleted;
	private Long dbMaxDelete;
	public static int Action_Select = 0;
	public static int Action_Delete = 1;
	private JDBLanguage lang;
	private Long recordsDeleted = (long) 0;
	private String dbEmailReport = "";

	public static int field_archive_id = 45;
	public static int field_description = 40;
	public static int field_sql_table = 32;
	public static int field_sql_criteria = 255;

	private void reportNew()
	{
		dbEmailReport = "";
	}

	private void reportAddLine(String line)
	{
		dbEmailReport = dbEmailReport + line + "\n";
	}

	private void reportAddBlankLine()
	{
		dbEmailReport = dbEmailReport + "\n";
	}

	private void reportHeading()
	{
		reportAddLine("Database Archiving Report");
		reportAddLine("=========================");
		reportAddBlankLine();
	}

	public String reportData()
	{
		return dbEmailReport + "\n\n";
	}

	public JDBArchive(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		lang = new JDBLanguage(getHostID(), getSessionID());
	}

	public JDBArchive(String host, String session, String id, String name, String label)
	{
		setHostID(host);
		setSessionID(session);
		setArchiveID(id);
		setDescription(name);
		lang = new JDBLanguage(getHostID(), getSessionID());
	}

	public void clear()
	{
		setDescription("");
		setEnabled("N");
		setBackgroundTask("N");
		setSQLTable("");
		setSQLCriterial("");
		setSequence(10);
		setRetentionDays(30);
		setErrorMessage("");
		setMaxDelete((long) 0);
	}

	public boolean create(String archID)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setArchiveID(archID);

			if (isValidArchive() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBArchive.create"));
				stmtupdate.setString(1, getArchiveID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Archive ID [" + getArchiveID() + "] already exists");
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
			if (isValidArchive() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBArchive.delete"));
				stmtupdate.setString(1, getArchiveID());
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

	public ResultSet getArchiveDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBArchive.getArchives"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public ResultSet getArchiveDataResultSet(PreparedStatement criteria)
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

	public String getArchiveID()
	{
		return JUtility.replaceNullStringwithBlank(dbArchiveID);
	}

	public boolean getArchiveProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getArchiveProperties [" + getArchiveID() + "]");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBArchive.getProperties"));
			stmt.setString(1, getArchiveID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
				rs.close();
				stmt.close();
			} else
			{
				setErrorMessage("Invalid Archive ID [" + getArchiveID() + "]");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public boolean getArchiveProperties(String archid)
	{
		setArchiveID(archid);
		return getArchiveProperties();
	}

	public LinkedList<JDBArchive> getArchives()
	{
		LinkedList<JDBArchive> typeList = new LinkedList<JDBArchive>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBArchive.getArchives"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBArchive mt = new JDBArchive(getHostID(), getSessionID());
				mt.getPropertiesfromResultSet(rs);
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

	public String getBackgroundTask()
	{
		dbBackgroundTask = JUtility.replaceNullStringwithBlank(dbBackgroundTask);
		if (dbBackgroundTask.equals(""))
		{
			dbBackgroundTask = "N";
		}
		return dbBackgroundTask;

	}

	public String getDescription()
	{

		return JUtility.replaceNullStringwithBlank(dbDescription);
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

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public LinkedList<String> getJobList()
	{
		LinkedList<String> typeList = new LinkedList<String>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBArchive.jobList"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				getPropertiesfromResultSet(rs);
				typeList.add(getArchiveID());
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return typeList;
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();

			setArchiveID(rs.getString("archive_id"));
			setDescription(rs.getString("description"));
			setEnabled(rs.getString("enabled"));
			setBackgroundTask(rs.getString("background_task"));
			setSQLResult(rs.getString("sql_result"));
			setSQLTable(rs.getString("sql_table"));
			setSQLCriterial(rs.getString("sql_criteria"));
			setRetentionDays(rs.getInt("retention_days"));
			setSequence(rs.getInt("sequence"));
			setRunStart(rs.getTimestamp("run_start_time"));
			setRunEnd(rs.getTimestamp("run_end_time"));
			setRecordsDeleted(rs.getLong("records_deleted"));
			setMaxDelete(rs.getLong("max_delete"));

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public Long getRecordsDeleted()
	{
		return dbRecordsDeleted;
	}

	public Long getMaxDelete()
	{
		return dbMaxDelete;
	}
	
	public void setMaxDelete(Long max)
	{
		dbMaxDelete = max;
	}
	
	public int getRetentionDays()
	{
		return dbRetentionDays;

	}

	public Timestamp getRunEnd()
	{
		return dbRunEnd;
	}

	public Timestamp getRunStart()
	{
		return dbRunStart;
	}

	public int getSequence()
	{
		return dbSequence;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public Timestamp getSQLArchiveDate()
	{
		Timestamp result;

		Calendar caldate = Calendar.getInstance();
		caldate.add(Calendar.DATE, (-1 * getRetentionDays()));
		result = new Timestamp(caldate.getTimeInMillis());
		logger.debug("[" + getArchiveID() + "] getSQLArchiveDate = " + result.toString());
		return result;
	}

	public String getSQLCriteria()
	{
		return JUtility.replaceNullStringwithBlank(dbSQLCriteria);
	}

	public String getSQLResult()
	{
		return JUtility.replaceNullStringwithBlank(dbSQLResult);
	}

	public String getSQLTable()
	{
		return JUtility.replaceNullStringwithBlank(dbSQLTable);
	}

	public boolean isBackgroundTask()
	{
		if (dbBackgroundTask.equals("Y"))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public boolean isEnabled()
	{
		if (dbEnabled.equals("Y"))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public boolean isRunable()
	{
		boolean result = true;

		if (isEnabled() == false)
		{
			result = false;
			setErrorMessage("Archive job [" + getArchiveID() + "] is DISABLED");
		} else
		{

			if (getSQLTable().equals(""))
			{
				result = false;
				setErrorMessage("Archive job [" + getArchiveID() + "] - Table name cannot be empty");
			} else
			{
				if (getSQLCriteria().equals(""))
				{
					result = false;
					setErrorMessage("Archive job [" + getArchiveID() + "] - Criteria cannot be empty");
				} else
				{
					if (getSQLCriteria().contains("?") == false)
					{
						result = false;
						setErrorMessage("Archive job [" + getArchiveID() + "] - Criteria must contain place marker [?] for parameter");
					}
				}
			}
		}

		return result;
	}

	public boolean isValidArchive()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBArchive.isValidArchive"));
			stmt.setString(1, getArchiveID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Archive ID [" + getArchiveID() + "]");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public boolean isValidArchive(String archid)
	{
		setArchiveID(archid);
		return isValidArchive();
	}

	public boolean renameTo(String newID)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidArchive() == true)
			{
				JDBArchive mattype = new JDBArchive(getHostID(), getSessionID());
				mattype.setArchiveID(newID);
				if (mattype.isValidArchive() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBArchive.renameTo"));
					stmtupdate.setString(1, newID);
					stmtupdate.setString(2, getArchiveID());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setArchiveID(newID);
					result = true;
				} else
				{
					setErrorMessage("New Archive ID [" + newID + "] is already in use.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public void runManual(String archid)
	{
		setArchiveID(archid);
		if (getArchiveProperties())
		{
			if (isRunable())
			{
				Long toDelete = runSQLSelect(getArchiveID());
				if (toDelete > 0)
				{
					int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Delete") + " " + toDelete.toString() + " " + lang.get("lbl_Rows") + ".", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
					if (n == 0)
					{
						runSQLDelete(getArchiveID());
						JOptionPane.showMessageDialog(Common.mainForm, lang.get("dlg_Archive_Complete"), lang.get("dlg_Information"), JOptionPane.INFORMATION_MESSAGE);
					}
				} else
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, getDescription() + "\n" + toDelete.toString() + " " + lang.get("lbl_Rows_Found") + ".", lang.get("dlg_Information"), JOptionPane.INFORMATION_MESSAGE);
				}

			} else
			{
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(Common.mainForm, getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public Long runSQL(int action)
	{
		Long result = (long) 0;

		setErrorMessage("");
		if (isRunable())
		{
			String limit = Common.hostList.getHost(getHostID()).getDatabaseParameters().getjdbcDatabaseSelectLimit();
			
			String sql = "";
			
			if (limit.equals("limit"))
			{
				if (action == Action_Select)
				{
					sql = "SELECT COUNT(*) AS ROWS_FOUND FROM (SELECT *  FROM  " + getSQLTable() +  " WHERE " + getSQLCriteria()+" LIMIT "+getMaxDelete().toString()+") AS A";
					logger.debug(sql);
				}

				if (action == Action_Delete)
				{
					sql = "DELETE FROM " + getSQLTable() + " WHERE " + getSQLCriteria()+ " LIMIT "+getMaxDelete().toString();
					logger.debug(sql);
				}				
			}
			
			if (limit.equals("top"))
			{
				if (action == Action_Select)
				{
					sql = "SELECT COUNT(*) AS ROWS_FOUND FROM (SELECT TOP ("+getMaxDelete().toString()+") * FROM  " + getSQLTable() +  " WHERE " + getSQLCriteria()+") AS A";
					logger.debug(sql);
				}

				if (action == Action_Delete)
				{
					sql = "DELETE TOP ("+getMaxDelete().toString()+") FROM " + getSQLTable() + " WHERE " + getSQLCriteria();
					logger.debug(sql);
				}
			}
			
			if (limit.equals("rownum"))
			{
				if (action == Action_Select)
				{
					sql = "SELECT COUNT(*) AS ROWS_FOUND FROM " + getSQLTable() +  " WHERE " + getSQLCriteria()+" AND ROWNUM <= "+getMaxDelete().toString();
					//sql = "SELECT COUNT(*) AS ROWS_FOUND FROM " + getSQLTable() + " WHERE " + getSQLCriteria()+ " {?}";
					logger.debug(sql);
				}

				if (action == Action_Delete)
				{
					sql = "DELETE FROM " + getSQLTable() + " WHERE " + getSQLCriteria()+ " AND ROWNUM <= "+getMaxDelete().toString();
					logger.debug(sql);
				}	
			}
			
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(sql);
				stmtupdate.setTimestamp(1, getSQLArchiveDate());
				stmtupdate.setFetchSize(1);

				if (action == Action_Select)
				{
					ResultSet rs;
					rs = stmtupdate.executeQuery();
					if (rs.next())
					{
						result = rs.getLong("ROWS_FOUND");
						recordsDeleted = result;
						setErrorMessage("[" + getArchiveID() + "] " + result.toString() + " row(s) returned by query.");
						setSQLResult(getErrorMessage());
					} else
					{
						result = (long) -1;
						recordsDeleted = (long) 0;
						setErrorMessage("[" + getArchiveID() + "] No results returned by query");
						setSQLResult(getErrorMessage());
					}
					rs.close();
				} else
				{
					Timestamp start = JUtility.getSQLDateTime();
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					Timestamp end = JUtility.getSQLDateTime();
					setRunStart(start);
					setRunEnd(end);
					setSQLResult(recordsDeleted.toString() + " records deleted.");
					setRecordsDeleted(recordsDeleted);
					reportAddLine(JUtility.padString("[" + getArchiveID() + "]", true, 20, " ") + JUtility.padString(getDescription(), true, 41, " ") + JUtility.padString(recordsDeleted.toString() + " records deleted.", false, 30, " "));

					updateRunStats();
				}

				stmtupdate.close();

			} catch (SQLException e)
			{
				result = (long) -1;
				setErrorMessage(e.getMessage());
				setSQLResult(getErrorMessage());
			}
		}

		return result;
	}

	public Long runSQLDelete(String archid)
	{
		Long result = (long) 0;
		if (getArchiveProperties(archid))
		{
			result = runSQL(Action_Delete);

		} else
		{
			result = (long) -1;
		}

		return result;
	}

	public void runSQLJobList()
	{
		LinkedList<String> SQLjobs = new LinkedList<String>();

		SQLjobs = getJobList();
		reportNew();

		if (SQLjobs.size() > 0)
		{
			reportHeading();
			String archid = "";
			Long result = (long) 0;
			for (int x = 0; x < SQLjobs.size(); x++)
			{
				archid = SQLjobs.get(x);
				result = runSQLSelect(archid);
				if (result >= 0)
				{
					result = runSQLDelete(archid);
				}
			}
		}

	}

	public Long runSQLSelect(String archid)
	{
		Long result = (long) 0;
		if (getArchiveProperties(archid))
		{
			result = runSQL(Action_Select);
		} else
		{
			result = (long) -1;
		}
		return result;
	}

	public void setArchiveID(String type)
	{
		dbArchiveID = type;
	}

	public void setBackgroundTask(boolean yesno)
	{
		if (yesno)
		{
			dbBackgroundTask = "Y";
		} else
		{
			dbBackgroundTask = "N";
		}
	}

	public void setBackgroundTask(String yesno)
	{
		dbBackgroundTask = JUtility.replaceNullStringwithBlank(yesno);
	}

	public void setDescription(String description)
	{
		dbDescription = description;
	}

	public void setEnabled(boolean yesno)
	{
		if (yesno)
		{
			dbEnabled = "Y";
		} else
		{
			dbEnabled = "N";
		}
	}

	public void setEnabled(String yesno)
	{
		dbEnabled = JUtility.replaceNullStringwithBlank(yesno);
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

	public void setRecordsDeleted(Long recs)
	{
		dbRecordsDeleted = recs;
	}

	public void setRetentionDays(int ret)
	{
		dbRetentionDays = ret;
	}

	public void setRunEnd(Timestamp end)
	{
		dbRunEnd = end;
	}

	public void setRunStart(Timestamp start)
	{
		dbRunStart = start;
	}

	public void setSequence(int seq)
	{
		dbSequence = seq;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setSQLCriterial(String id)
	{
		dbSQLCriteria = id;
	}

	public void setSQLResult(String id)
	{
		dbSQLResult = id;
	}

	public void setSQLTable(String id)
	{
		dbSQLTable = id;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidArchive() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBArchive.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getEnabled());
				stmtupdate.setString(3, getBackgroundTask());
				stmtupdate.setString(4, getSQLTable());
				stmtupdate.setString(5, getSQLCriteria());
				stmtupdate.setInt(6, getRetentionDays());
				stmtupdate.setInt(7, getSequence());
				stmtupdate.setLong(8, getMaxDelete());
				stmtupdate.setString(9, getArchiveID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			result = false;
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean updateRunStats()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidArchive() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBArchive.updateRunStats"));
				stmtupdate.setTimestamp(1, getRunStart());
				stmtupdate.setTimestamp(2, getRunEnd());
				stmtupdate.setLong(3, getRecordsDeleted());
				stmtupdate.setString(4, getSQLResult());
				stmtupdate.setString(5, getArchiveID());
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
