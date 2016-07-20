package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBReportRequest
{

	private String dbInvocationMode;
	private String dbModuleID;
	private Long dbReportRequestID;
	private Integer dbParameterCount;
	private String dbParameterDelimiter;
	private String dbParameterNames;
	private String dbParameterTypes;
	private String dbParameterValues;
	private String dbSQL;
	private String dbPrintQueue;
	private Integer dbCopies;
	private String dbErrorMessage;
	private final Logger logger = Logger.getLogger(JDBReportRequest.class);
	private String hostID;
	private String sessionID;

	public JDBReportRequest(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		clear();
	}

	public void clearParameters() {
		setParameterNames("");
		setParameterTypes("");
		setParameterValues("");
		setParameterCount(0);
	}

	public void defineReport(String module, String mode, String delim, String sql, String queue, Integer copies) {
		setParameterDelimiter(delim);
		setModuleID(module);
		setInvocationMode(mode);
		setParameterCount(0);
		setSQL(sql);
		setPrintQueue(queue);
		setCopies(copies);
	}

	public void addParameter(String name, String type, String value) {
		String delim = getParameterDelimiter();

		if (getParameterCount() == 0)
		{
			delim = "";
		}

		dbParameterNames = dbParameterNames + delim + name;
		dbParameterTypes = dbParameterTypes + delim + type;
		dbParameterValues = dbParameterValues + delim + value;

		dbParameterCount++;

	}

	public void clear() {
		setInvocationMode("");
		setModuleID("");
		setParameterCount(0);
		setParameterDelimiter("");
		setParameterNames("");
		setParameterTypes("");
		setParameterValues("");
		setSQL("");
		setPrintQueue("");
		setCopies(1);
	}

	public boolean create() {
		final Logger logger = Logger.getLogger(JDBReportRequest.class);

		logger.debug("Create Report Request for [" + getModuleID() + "]");

		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBReportRequest.create"));

			stmtupdate.setLong(1, generateNewReportRequestID());
			stmtupdate.setString(2, getModuleID());
			stmtupdate.setString(3, getInvocationMode());
			stmtupdate.setInt(4, getParameterCount());
			stmtupdate.setString(5, getParameterDelimiter());
			stmtupdate.setString(6, getParameterNames());
			stmtupdate.setString(7, getParameterTypes());
			stmtupdate.setString(8, getParameterValues());
			stmtupdate.setString(9, getSQL());
			stmtupdate.setString(10, getPrintQueue());
			stmtupdate.setInt(11, getCopies());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBReportRequest.delete"));
			stmtupdate.setLong(1, getReportRequestID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(Long intReq) {
		setReportRequestID(intReq);
		return delete();
	}

	public long generateNewReportRequestID() {
		long result = 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		String reportRequestID_str = "1";
		long reportRequestID = 1;

		boolean retry = true;
		@SuppressWarnings("unused")
		int counter = 0;

		if (ctrl.getProperties("REPORT REQUEST ID") == true)
		{
			do
			{
				if (ctrl.lockRecord("REPORT REQUEST ID") == true)
				{
					if (ctrl.getProperties("REPORT REQUEST ID") == true)
					{
						reportRequestID_str = ctrl.getKeyValue();
						reportRequestID = Long.parseLong(reportRequestID_str);
						reportRequestID++;
						temp = String.valueOf(reportRequestID);
						ctrl.setKeyValue(temp);

						if (ctrl.update())
						{
							retry = false;
						}
					}
				}
				else
				{
					logger.debug("Record Locked !");
					retry = true;
					counter++;
				}
			}
			while (retry);
		}
		else
		{
			ctrl.getKeyValueWithDefault("REPORT REQUEST ID", "1", "Unique Report ID");
			reportRequestID = 1;
		}

		setReportRequestID(reportRequestID);
		result = reportRequestID;

		logger.debug("ReportRequestID :" + result);
		return result;
	}

	public Integer getCopies() {
		return dbCopies;
	}

	public String getErrorMessage() {
		return dbErrorMessage;
	}

	private String getHostID() {
		return hostID;
	}

	public String getInvocationMode() {
		return dbInvocationMode;
	}

	public String getModuleID() {
		return dbModuleID;
	}

	public Integer getParameterCount() {
		return Integer.valueOf(dbParameterCount);
	}

	public String getParameterDelimiter() {
		return dbParameterDelimiter;
	}

	public String getParameterNames() {
		return dbParameterNames;
	}

	public String getParameterTypes() {
		return dbParameterTypes;
	}

	public String getParameterValues() {
		return dbParameterValues;
	}

	public String getPrintQueue() {
		return JUtility.replaceNullStringwithBlank(dbPrintQueue);
	}

	public Long getReportRequestID() {
		return dbReportRequestID;
	}

	public LinkedList<Long> getReportRequestIDs() {
		LinkedList<Long> intList = new LinkedList<Long>();
		Statement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
			stmt.setFetchSize(25);
			rs = stmt.executeQuery(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBReportRequest.getReportRequestIDs"));

			while (rs.next())
			{
				intList.addLast(rs.getLong("report_request_id"));
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return intList;
	}

	public HashMap<String, Object> getParameters() {
		HashMap<String, Object> result = new HashMap<String, Object>();

		String p_name = "";
		String p_value = "";
		StringTokenizer st_names = new StringTokenizer(getParameterNames(), getParameterDelimiter());
		StringTokenizer st_values = new StringTokenizer(getParameterValues(), getParameterDelimiter());

		result.clear();

		while (st_names.hasMoreTokens())
		{

			p_name = st_names.nextToken();

			if (st_values.hasMoreTokens() == true)
			{
				p_value = st_values.nextToken();

			}
			else
			{
				p_value = "";
			}
			result.put(p_name, p_value);
		}

		return result;
	}

	public boolean getReportRequestProperties() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBReportRequest.getReportRequestProperties"));
			stmt.setLong(1, getReportRequestID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{

				setReportRequestID(rs.getLong("report_request_id"));
				setModuleID(rs.getString("module_id"));
				setInvocationMode(rs.getString("invocation_mode"));
				setParameterCount(rs.getInt("parameter_count"));
				setParameterDelimiter(rs.getString("parameter_delimiter"));
				setParameterNames(rs.getString("parameter_names"));
				setParameterTypes(rs.getString("parameter_types"));
				setParameterValues(rs.getString("parameter_values"));
				setSQL(rs.getString("SQL"));
				setPrintQueue(rs.getString("print_queue_name"));
				setCopies(rs.getInt("print_copies"));

				result = true;
			}
			else
			{
				setErrorMessage("Invalid Report Request ID [" + getReportRequestID() + "]");
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public ResultSet getReportRequestResultSet(PreparedStatement criteria) {

		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();

		}
		catch (Exception e)
		{
			rs = null;
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	private String getSessionID() {
		return sessionID;
	}

	public String getSQL() {
		return dbSQL;
	}

	public void setCopies(Integer copies) {
		dbCopies = copies;
	}

	private void setErrorMessage(String err) {
		dbErrorMessage = err;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	public void setInvocationMode(String type) {
		dbInvocationMode = type;
	}

	public void setModuleID(String moduleid) {
		dbModuleID = moduleid;
	}

	public void setParameterCount(Integer count) {
		dbParameterCount = count;
	}

	public void setParameterDelimiter(String delimiter) {
		dbParameterDelimiter = delimiter;
	}

	public void setParameterNames(String names) {
		dbParameterNames = names;
	}

	public void setParameterTypes(String types) {
		dbParameterTypes = types;
	}

	public void setParameterValues(String values) {
		dbParameterValues = values;
	}

	public void setPrintQueue(String queue) {
		dbPrintQueue = queue;
	}

	public void setReportRequestID(Long ReportLogID) {
		this.dbReportRequestID = ReportLogID;
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	public void setSQL(String sql) {
		dbSQL = sql;
	}

}
