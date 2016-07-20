package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBAuditPermissions
{

	private Long dbAuditLogID;
	private Timestamp dbEventTime;
	private String dbUserID;
	private String dbTargetType;
	private String dbTargetAction;
	private String dbTargetID;
	private String dbTargetData;
	private String dbTargetWorkstation;
	private String dbErrorMessage;
	public static int field_workstation_id = 45;
	public static int field_target = 45;
	public static int field_data = 40;
	
	private final Logger logger = Logger.getLogger(JDBAuditPermissions.class);

	private String hostID;

	private String sessionID;

	public JDBAuditPermissions( Long logID, Timestamp eventTime,String userID, String targetType, String targetAction, String targetID, String targetData,String workstation)
	{
		setAuditLogID(logID);
		setEventTime(eventTime);
		setUserID(userID);
		setTargetType(targetType);
		setTargetAction(targetAction);
		setTargetID(targetID);
		setTargetData(targetData);
		setTargetWorkstation(workstation);
		create();
	}

	public JDBAuditPermissions(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear() {
		setEventTime(null);
		setUserID("");
		setTargetType("");
		setTargetAction("");
		setTargetID("");
		setTargetData("");
		setTargetWorkstation("");
		setErrorMessage("");
	}
	
	
	public boolean create() {

		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAuditPermissions.create"));
			stmtupdate.setLong(1, getAuditLogID());
			stmtupdate.setTimestamp(2, getEventTime());
			stmtupdate.setString(3, getUserID());
			stmtupdate.setString(4, getTargetType());
			stmtupdate.setString(5, getTargetAction());
			stmtupdate.setString(6, getTargetID());
			stmtupdate.setString(7, getTargetData());
			stmtupdate.setString(8, getTargetWorkstation());
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

	
	public String getTargetWorkstationID()
	{
		dbTargetWorkstation = JUtility.replaceNullStringwithBlank(dbTargetWorkstation);
		if (dbTargetWorkstation.equals(""))
		{
			dbTargetWorkstation = JUtility.getClientName();
		}
		return dbTargetWorkstation;
	}

	public long generateNewAuditLogID() {
		long result = 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		String auditLogID_str = "1";
		long auditLogID = 1;

		boolean retry = true;
		@SuppressWarnings("unused")
		int counter = 0;

		if (ctrl.getProperties("AUDIT PERMISSIONS LOG ID") == true)
		{
			do
			{
				if (ctrl.lockRecord("AUDIT PERMISSIONS LOG ID") == true)
				{
					if (ctrl.getProperties("AUDIT PERMISSIONS LOG ID") == true)
					{
						auditLogID_str = ctrl.getKeyValue();
						auditLogID = Long.parseLong(auditLogID_str);
						auditLogID++;
						temp = String.valueOf(auditLogID);
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
			ctrl.getKeyValueWithDefault("AUDIT PERMISSIONS LOG ID", "1", "Unique Audit Log ID");
			auditLogID = 1;
		}

		result = auditLogID;
		setAuditLogID(result);

		return result;
	}

	public Vector<JDBAuditPermissions> getAuditLogData(PreparedStatement criteria) {

		ResultSet rs;
		Vector<JDBAuditPermissions> result = new Vector<JDBAuditPermissions>();

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBAuditPermissions(Long.valueOf("0"),JUtility.getSQLDateTime(),  "user_id", "event_type", "event_action", "target", "data", "workstation_id"));
		}
		else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					getPropertiesfromResultSet(rs);
					result.addElement(new JDBAuditPermissions(getAuditLogID(),getEventTime(),  getUserID(), getTargetType(), getTargetAction(), getTargetID(), getTargetData(), getTargetWorkstation()));
				}
				rs.close();

			}
			catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public ResultSet getAuditLogDataResultSet(PreparedStatement criteria) {

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

	public Long getAuditLogID() {
		return dbAuditLogID;
	}


	public boolean getAuditLogProperties() {
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAuditPermissions.getAuditLogProperties"));
			stmt.setFetchSize(1);
			stmt.setLong(1, getAuditLogID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Audit Log ID");
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

	public boolean getAuditLogProperties(Long logID) {
		setAuditLogID(logID);
		return getAuditLogProperties();
	}

	public ResultSet getAuditLogResultSet(PreparedStatement criteria) {

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

	public String getTargetAction() {
		return dbTargetAction;
	}

	public Timestamp getEventTime() {
		return dbEventTime;
	}

	public String getTargetType() {
		return dbTargetType;
	}

	private String getHostID() {
		return hostID;
	}

	public ResultSet getAuditDataResultSet(PreparedStatement criteria) {

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

	public void getPropertiesfromResultSet(ResultSet rs) {
		try
		{
			clear();
			setAuditLogID(rs.getLong("sys_audit_log_id"));
			setEventTime(rs.getTimestamp("event_time"));
			setUserID(rs.getString("user_id"));
			setTargetType(rs.getString("event_type"));
			setTargetAction(rs.getString("event_action"));
			setTargetID(rs.getString("target"));
			setTargetData(rs.getString("data"));
			setTargetWorkstation(rs.getString("workstation_id"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}
	
	public void setTargetWorkstation(String wk)
	{
		dbTargetWorkstation = wk;
	}
	

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}
	
	private String getSessionID() {
		return sessionID;
	}


	public String getTargetData() {
		return dbTargetData;
	}

	public String getTargetWorkstation() {
		return JUtility.getClientName();
	}

	public String getTargetID() {
		return dbTargetID;
	}

	public String getUserID() {
		return dbUserID;
	}

	public void setAuditLogID(Long logID) {
		dbAuditLogID = logID;
	}

	private void setErrorMessage(String errorMsg) {
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setTargetAction(String messageinformation) {
		dbTargetAction = messageinformation;
	}

	public void setEventTime(Timestamp eventTime) {
		dbEventTime = eventTime;
	}

	public void setTargetType(String type) {
		dbTargetType = type;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	public void setTargetData(String messagestatus) {
		dbTargetData = messagestatus;
	}

	public void setTargetID(String direction) {
		dbTargetID = direction;
	}

	public void setUserID(String messageref) {
		dbUserID = messageref;
	}

	public void write(String userID,String eventType, String targetAction, String targetID, String targetdata) {
		setUserID(userID);
		setEventTime(JUtility.getSQLDateTime());
		setTargetType(eventType);
		setTargetAction(targetAction);
		setTargetID(targetID);
		setTargetData(targetdata);
		setTargetWorkstation(JUtility.getClientName());
		create();
	}

}
