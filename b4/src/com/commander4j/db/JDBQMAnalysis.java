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
	public static int field_data_1 = 20;
	public static int field_data_2 = 20;
	private String 	dbAnalysisID;
	private String 	dbDescription;
	private int 	dbDisplaySequence;
	private String 	dbModuleID;
	private String 	dbBatchSuffixReqd;
	private String 	dbBatchSuffixParam;
	private String 	dbResourceReqd;
	private String 	dbResourceParam;
	private String 	dbProcessOrderReqd;
	private String 	dbProcessOrderParam;
	private String 	dbMaterialReqd;
	private String 	dbMaterialParam;
	private String 	dbSampleDateStartReqd;
	private String 	dbSampleDateStartParam;
	private String 	dbSampleDateEndReqd;
	private String 	dbSampleDateEndParam;	
	private String 	dbUserData1Reqd;
	private String 	dbUserData1Param;	
	private String 	dbUserData2Reqd;
	private String 	dbUserData2Param;	
	private String 	dbUserData3Reqd;
	private String 	dbUserData3Param;	
	
	private String 	dbUserData4Reqd;

	private String 	dbUserData4Param;
	private String dbErrorMessage;
	private final Logger logger = Logger.getLogger(JDBQMAnalysis.class);
	private String hostID;
	private String sessionID;

	public JDBQMAnalysis(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{
		setModuleID("");
		setDescription("");
		setDisplaySequence(0);
		setBatchSuffixReqd("N");
		setBatchSuffixParam("P_BATCH_SUFFIX");
		setProcessOrderReqd("N");
		setProcessOrderParam("P_PROCESS_ORDER");
		setMaterialReqd("N");
		setMaterialParam("P_MATERIAL");
		setSampleDateStartReqd("N");
		setSampleDateStartParam("P_SAMPLE_DATE_START");
		setSampleDateEndReqd("N");
		setSampleDateEndParam("P_SAMPLE_DATE_END");
		setUserData1Reqd("N");
		setUserData1Param("P_USER_DATA_1");
		setUserData2Reqd("N");
		setUserData2Param("P_USER_DATA_2");
		setUserData3Reqd("N");
		setUserData4Param("P_USER_DATA_3");
		setUserData4Reqd("N");
		setUserData4Param("P_USER_DATA_4");				
		setResourceReqd("N");
		setResourceParam("P_RESOURCE");
	}

	public boolean create(String analysisid)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setAnalysisID(analysisid);

			if (isValidAnalysis() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMAnalysis.create"));
				stmtupdate.setString(1, getAnalysisID());			
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
				mt.setDescription(rs.getString("description"));
				mt.setDisplaySequence(rs.getInt("display_sequence"));
				mt.setModuleID(rs.getString("module_id"));
				mt.setProcessOrderReqd(rs.getString("process_order_reqd"));
				mt.setProcessOrderParam(rs.getString("process_order_param"));
				mt.setMaterialReqd(rs.getString("material_reqd"));
				mt.setMaterialParam(rs.getString("material_param"));
				mt.setSampleDateStartReqd(rs.getString("sample_date_start_reqd"));
				mt.setSampleDateStartParam(rs.getString("sample_date_start_param"));
				mt.setSampleDateEndReqd(rs.getString("sample_date_end_reqd"));
				mt.setSampleDateEndParam(rs.getString("sample_date_end_param"));
				mt.setUserData1Reqd(rs.getString("user_data_1_reqd"));
				mt.setUserData1Param(rs.getString("user_data_1_param"));
				mt.setUserData2Reqd(rs.getString("user_data_2_reqd"));
				mt.setUserData2Param(rs.getString("user_data_2_param"));
				mt.setUserData3Reqd(rs.getString("user_data_3_reqd"));
				mt.setUserData3Param(rs.getString("user_data_3_param"));
				mt.setUserData4Reqd(rs.getString("user_data_4_reqd"));
				mt.setUserData4Param(rs.getString("user_data_4_param"));
				mt.setBatchSuffixReqd(rs.getString("batch_suffix_reqd"));
				mt.setBatchSuffixParam(rs.getString("batch_suffix_param"));
				mt.setResourceReqd(rs.getString("resource_reqd"));
				mt.setResourceParam(rs.getString("resource_param"));				
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

	public String getAnalysisID()
	{
		return dbAnalysisID;
	}

	public String getSampleDateEndParam()
	{
		return dbSampleDateEndParam;
	}

	public String getSampleDateEndReqd()
	{
		return dbSampleDateEndReqd;
	}

	public String getSampleDateStartParam()
	{
		return dbSampleDateStartParam;
	}

	public String getSampleDateStartReqd()
	{
		return dbSampleDateStartReqd;
	}

	public String getUserData1Param()
	{
		return dbUserData1Param;
	}

	public String getUserData1Reqd()
	{
		return dbUserData1Reqd;
	}

	public String getUserData2Param()
	{
		return dbUserData2Param;
	}

	public String getUserData2Reqd()
	{
		return dbUserData2Reqd;
	}

	public String getUserData3Param()
	{
		return dbUserData3Param;
	}

	public String getUserData3Reqd()
	{
		return dbUserData3Reqd;
	}

	public String getUserData4Param()
	{
		return dbUserData4Param;
	}

	public String getUserData4Reqd()
	{
		return dbUserData4Reqd;
	}

	public String getDescription()
	{
		String result = "";
		if (dbDescription != null)
			result = dbDescription;
		return result;
	}

	public int getDisplaySequence()
	{
		return dbDisplaySequence;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getMaterialParam()
	{
		String result = "";
		if (dbMaterialParam != null)
			result = dbMaterialParam;
		return result;
	}

	public String getMaterialReqd()
	{
		String result = "";
		if (dbMaterialReqd != null)
			result = dbMaterialReqd;
		return result;
	}

	public String getModuleID()
	{
		String result = "";
		if (dbModuleID != null)
			result = dbModuleID;
		return result;
	}


	public String getProcessOrderParam()
	{
		String result = "";
		if (dbProcessOrderParam != null)
			result = dbProcessOrderParam;
		return result;
	}

	public String getProcessOrderReqd()
	{
		String result = "";
		if (dbProcessOrderReqd != null)
			result = dbProcessOrderReqd;
		return result;
	}
	
	public String getResourceParam()
	{
		String result = "";
		if (dbResourceParam != null)
			result = dbResourceParam;
		return result;
	}

	public String getResourceReqd()
	{
		String result = "";
		if (dbResourceReqd != null)
			result = dbResourceReqd;
		return result;
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
				setAnalysisID(rs.getString("analysis_id"));
				setDescription(rs.getString("description"));
				setDisplaySequence(rs.getInt("display_sequence"));
				setModuleID(rs.getString("module_id"));
				setProcessOrderReqd(rs.getString("process_order_reqd"));
				setProcessOrderParam(rs.getString("process_order_param"));
				setMaterialReqd(rs.getString("material_reqd"));
				setMaterialParam(rs.getString("material_param"));
				setSampleDateStartReqd(rs.getString("sample_date_start_reqd"));
				setSampleDateStartParam(rs.getString("sample_date_start_param"));
				setSampleDateEndReqd(rs.getString("sample_date_end_reqd"));
				setSampleDateEndParam(rs.getString("sample_date_end_param"));
				setUserData1Reqd(rs.getString("user_data_1_reqd"));
				setUserData1Param(rs.getString("user_data_1_param"));
				setUserData2Reqd(rs.getString("user_data_2_reqd"));
				setUserData2Param(rs.getString("user_data_2_param"));
				setUserData3Reqd(rs.getString("user_data_3_reqd"));
				setUserData3Param(rs.getString("user_data_3_param"));
				setUserData4Reqd(rs.getString("user_data_4_reqd"));
				setUserData4Param(rs.getString("user_data_4_param"));
				setBatchSuffixReqd(rs.getString("batch_suffix_reqd"));
				setBatchSuffixParam(rs.getString("batch_suffix_param"));
				setResourceReqd(rs.getString("resource_reqd"));
				setResourceParam(rs.getString("resource_param"));				
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

	public boolean getResultsProperties(String analysisid)
	{
		setAnalysisID(analysisid);
		return getProperties();
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean isAscending()
	{
		boolean result = false;
		
		if (getProcessOrderReqd().equals("Y"))
		{
			result = true;
		}
		
		return result;
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

	public boolean isValidAnalysis(String analysisid)
	{
		setAnalysisID(analysisid);
		return isValidAnalysis();
	}

	public void setAnalysisID(String analysisid)
	{
		dbAnalysisID = analysisid;
	}

	public void setSampleDateEndParam(String dbSampleDateEndParam)
	{
		this.dbSampleDateEndParam = dbSampleDateEndParam;
	}

	public void setSampleDateEndReqd(String dbSampleDateEndReqd)
	{
		this.dbSampleDateEndReqd = dbSampleDateEndReqd;
	}

	public void setSampleDateStartParam(String dbSampleDateStartParam)
	{
		this.dbSampleDateStartParam = dbSampleDateStartParam;
	}

	public void setSampleDateStartReqd(String dbSampleDateStartReqd)
	{
		this.dbSampleDateStartReqd = dbSampleDateStartReqd;
	}

	public void setUserData1Param(String dbUserData1Param)
	{
		this.dbUserData1Param = dbUserData1Param;
	}

	public void setUserData1Reqd(String dbUserData1Reqd)
	{
		this.dbUserData1Reqd = dbUserData1Reqd;
	}
	
	public void setUserData2Param(String dbUserData2Param)
	{
		this.dbUserData2Param = dbUserData2Param;
	}
	
	public void setUserData2Reqd(String dbUserData2Reqd)
	{
		this.dbUserData2Reqd = dbUserData2Reqd;
	}

	public void setUserData3Param(String dbUserData3Param)
	{
		this.dbUserData3Param = dbUserData3Param;
	}

	public void setUserData3Reqd(String dbUserData3Reqd)
	{
		this.dbUserData3Reqd = dbUserData3Reqd;
	}

	public void setUserData4Param(String dbUserData4Param)
	{
		this.dbUserData4Param = dbUserData4Param;
	}

	public void setUserData4Reqd(String dbUserData4Reqd)
	{
		this.dbUserData4Reqd = dbUserData4Reqd;
	}

	public void setDescription(String po)
	{
		dbDescription = po;
	}

	public void setDisplaySequence(int ud4)
	{
		dbDisplaySequence = ud4;
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

	public void setMaterialParam(String ud3)
	{
		dbMaterialParam = ud3;
	}
	
	public void setBatchSuffixReqd(String ud3)
	{
		dbBatchSuffixReqd = ud3;
	}
	
	public String getBatchSuffixReqd()
	{
		return dbBatchSuffixReqd;
	}
	
	public void setBatchSuffixParam(String ud3)
	{
		dbBatchSuffixParam = ud3;
	}
	
	public String getBatchSuffixParam()
	{
		return dbBatchSuffixParam;
	}	

	public void setMaterialReqd(String inspectid)
	{
		dbMaterialReqd = inspectid;
	}

	public void setModuleID(String value)
	{
		dbModuleID = value;
	}

	public void setProcessOrderParam(String ud2)
	{
		dbProcessOrderParam = ud2;
	}

	public void setProcessOrderReqd(String ud1)
	{
		dbProcessOrderReqd = ud1;
	}

	public void setResourceParam(String ud2)
	{
		dbResourceParam = ud2;
	}

	public void setResourceReqd(String ud1)
	{
		dbResourceReqd = ud1;
	}	
	
	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";
		if (getDescription().equals("") == false)
		{
			result = getDescription() + " ("+getAnalysisID()+")";
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

	
				stmtupdate.setString(1, getDescription());
				stmtupdate.setInt(2, getDisplaySequence());
				stmtupdate.setString(3, getModuleID());
				stmtupdate.setString(4, getProcessOrderReqd());
				stmtupdate.setString(5, getProcessOrderParam());
				stmtupdate.setString(6, getMaterialReqd());
				stmtupdate.setString(7, getMaterialParam());
				stmtupdate.setString(8, getSampleDateStartReqd());
				stmtupdate.setString(9, getSampleDateStartParam());
				stmtupdate.setString(10, getSampleDateEndReqd());
				stmtupdate.setString(11, getSampleDateEndParam());
				stmtupdate.setString(12, getUserData1Reqd());
				stmtupdate.setString(13, getUserData1Param());
				stmtupdate.setString(14, getUserData2Reqd());
				stmtupdate.setString(15, getUserData2Param());
				stmtupdate.setString(16, getUserData3Reqd());
				stmtupdate.setString(17, getUserData3Param());
				stmtupdate.setString(18, getUserData4Reqd());
				stmtupdate.setString(19, getUserData4Param());
				stmtupdate.setString(20, getBatchSuffixReqd());
				stmtupdate.setString(21, getBatchSuffixParam());	
				stmtupdate.setString(21, getResourceReqd());
				stmtupdate.setString(22, getResourceParam());				
				stmtupdate.setString(23, getAnalysisID());

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
