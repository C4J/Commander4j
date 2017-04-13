package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMSample.java
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

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;

/**
 * The JDBQMAnalysis inserts rows into the APP_QM_ANALYSIS table.
 * Each row in this table is used by the QM Analysis screen to allow the user to generate reports on Samples/Tests and Results.
 * <p>
 * <img alt="" src="./doc-files/APP_QM_ANALYSIS.jpg" >
 * 
  * @see com.commander4j.db.JDBQMResult JDBQMResult
 * @see com.commander4j.db.JDBQMInspection JDBQMInspection
 * @see com.commander4j.db.JDBQMActivity JDBQMActivity
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary
  * @see com.commander4j.db.JDBQMTest JDBQMTest
 */
public class JDBQMAnalysis
{
	private String dbViewName;
	private String dbSortBy;
	private String dbAnalysisID;
	private String dbDescription;
	private String dbErrorMessage;
	private String dbAscDesc;
	private String dbFieldList;
	private String dbMergeSpreadsheet;
	private int dbDisplaySequence;
	public static int field_data_1 = 20;
	public static int field_data_2 = 20;
	private final Logger logger = Logger.getLogger(JDBQMAnalysis.class);
	private String hostID;
	private String sessionID;

	public JDBQMAnalysis(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBQMAnalysis(String host, String session, String analysisID, String sortBy, String description, String viewName, String ascDesc, String fieldList, String mergeSpreadsheet, int displaySequence)
	{
		setHostID(host);
		setSessionID(session);
		setAnalysisID(analysisID);
		setDescription(description);
		setViewName(viewName);
		setSortBy(sortBy);
		setAscDesc(ascDesc);
		setFieldList(fieldList);
		setMergeSpreadsheet(mergeSpreadsheet);
		setDisplaySequence(displaySequence);		
	}

	public void clear()
	{
		setViewName("");
		setDescription("");
		setSortBy("");
		setAscDesc("");
		setFieldList("");
		setMergeSpreadsheet("");
		setDisplaySequence(0);
	}

	public ResultSet getQMAnalysisResultSet(String analysisID)
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");
		setAnalysisID(analysisID);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMAnalysis.getAnalysis"));
			stmt.setString(1, getAnalysisID());
			stmt.setFetchSize(200);
			rs = stmt.executeQuery();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public boolean isAscending()
	{
		boolean result = false;
		
		if (getAscDesc().equals("Y"))
		{
			result = true;
		}
		
		return result;
	}
	
	public boolean create(String analysisid, String sortby, String desc, String view, String ascdesc, String fields, String mergespreadsheet, int dispseq)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setAnalysisID(analysisid);
			setSortBy(sortby);
			setDescription(desc);
			setViewName(view);
			setAscDesc(ascdesc);
			setFieldList(fields);
			setMergeSpreadsheet(mergespreadsheet);
			setDisplaySequence(dispseq);

			if (isValidAnalysis() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMAnalysis.create"));
				stmtupdate.setString(1, getAnalysisID());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getViewName());
				stmtupdate.setString(4, getSortBy());
				stmtupdate.setString(5, getAscDesc());
				stmtupdate.setString(6, getFieldList());
				stmtupdate.setString(7, getMergeSpreadsheet());
				stmtupdate.setInt(8, getDisplaySequence());				
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("QMAnalysis ID already exists");
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
			if (isValidAnalysis() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMAnalysis.delete"));
				stmtupdate.setString(1, getAnalysisID());
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

	public boolean getResultsProperties(String analysisid)
	{
		setAnalysisID(analysisid);
		return getProperties();
	}

	public boolean getProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("Analysis.getProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMAnalysis.getProperties"));
			stmt.setString(1, getAnalysisID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setSortBy(rs.getString("sort_by"));
				setDescription(rs.getString("description"));
				setViewName(rs.getString("view_name"));
				setAscDesc(rs.getString("asc_desc"));
				setFieldList(rs.getString("field_list"));
				setMergeSpreadsheet(rs.getString("merge_spreadsheet"));
				setDisplaySequence(rs.getInt("display_sequence"));
				result = true;
				rs.close();
				stmt.close();
			} else
			{
				setErrorMessage("Invalid Analysis ID");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public Vector<JDBQMAnalysis> getAnalysisData()
	{
		Vector<JDBQMAnalysis> typeList = new Vector<JDBQMAnalysis>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMAnalysis.getAnalysisData"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBQMAnalysis mt = new JDBQMAnalysis(getHostID(), getSessionID());
				mt.setAnalysisID(rs.getString("analysis_id"));
				mt.setSortBy(rs.getString("sort_by"));
				mt.setViewName(rs.getString("view_name"));
				mt.setDescription(rs.getString("description"));
				mt.setAscDesc(rs.getString("asc_desc"));
				mt.setFieldList(rs.getString("field_list"));
				mt.setMergeSpreadsheet(rs.getString("merge_spreadsheet"));
				mt.setDisplaySequence(rs.getInt("display_sequence"));
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

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getAnalysisID()
	{
		return dbAnalysisID;
	}

	public String getSortBy()
	{
		String result = "";
		if (dbSortBy != null)
			result = dbSortBy;
		return result;
	}

	public String getViewName()
	{
		String result = "";
		if (dbViewName != null)
			result = dbViewName;
		return result;
	}

	public String getDescription()
	{
		String result = "";
		if (dbDescription != null)
			result = dbDescription;
		return result;
	}

	public String getAscDesc()
	{
		String result = "";
		if (dbAscDesc != null)
			result = dbAscDesc;
		return result;
	}

	public String getFieldList()
	{
		String result = "";
		if (dbFieldList != null)
			result = dbFieldList;
		return result;
	}
	
	public String getMergeSpreadsheet()
	{
		String result = "";
		if (dbMergeSpreadsheet != null)
			result = dbMergeSpreadsheet;
		return result;
	}
	
	public int getDisplaySequence()
	{
		return dbDisplaySequence;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean isValidAnalysis(String analysisid)
	{
		setAnalysisID(analysisid);
		return isValidAnalysis();
	}

	public boolean isValidAnalysis()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMAnalysis.isValid"));
			stmt.setString(1, getAnalysisID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Analysis ID [" + String.valueOf(getAnalysisID()) + "]");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

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

	public void setAnalysisID(String analysisid)
	{
		dbAnalysisID = analysisid;
	}

	public void setSortBy(String inspectid)
	{
		dbSortBy = inspectid;
	}

	public void setViewName(String value)
	{
		dbViewName = value;
	}

	public void setDescription(String po)
	{
		dbDescription = po;
	}

	public void setAscDesc(String ud1)
	{
		dbAscDesc = ud1;
	}

	public void setFieldList(String ud2)
	{
		dbFieldList = ud2;
	}

	public void setMergeSpreadsheet(String ud3)
	{
		dbMergeSpreadsheet = ud3;
	}

	public void setDisplaySequence(int ud4)
	{
		dbDisplaySequence = ud4;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";
		if (getSortBy().equals("") == false)
		{
			result = getDescription();
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
			if (isValidAnalysis() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMSample.update"));
				stmtupdate.setString(1, getSortBy());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getViewName());
				stmtupdate.setString(4, getAscDesc());
				stmtupdate.setString(5, getFieldList());
				stmtupdate.setString(6, getMergeSpreadsheet());
				stmtupdate.setInt(7, getDisplaySequence());
				stmtupdate.setString(8, getAnalysisID());
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
