package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBInterfaceLog.java
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
import java.util.Vector;

import org.apache.logging.log4j.Logger;

import com.commander4j.messages.GenericMessageHeader;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * JDBInterfaceLog is used to record all activities of the interface module. The
 * data is stored in the table SYS_INTERFACE_LOG When a messages is processed,
 * whether it be inbound or outbound its recorded in this log along with the
 * status, Success or Fail. A screen within the application allows messages to
 * be re-processed if needed.
 * <p>
 * <img alt="" src="./doc-files/SYS_INTERFACE_LOG.jpg" >
 * 
 * @see com.commander4j.db.JDBInterfaceRequest JDBInterfaceRequest
 */
public class JDBInterfaceLog
{

	private Timestamp dbEventTime;
	private String dbMessageRef;
	private String dbInterfaceType;
	private String dbMessageInformation;
	private String dbInterfaceDirection;
	private Timestamp dbMessageDate;
	private String dbMessageStatus;
	private String dbMessageError;
	private String dbFilename;
	private String dbAction;
	private Long dbInterfaceLogID;
	private String dbWorkstationID;
	public static int field_message_ref = 45;
	public static int field_message_information = 45;
	public static int field_message_error = 150;
	public static int field_action = 45;
	public static int field_workstation_id = 45;
	public static int field_filename = 150;

	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBInterfaceLog.class);

	private String hostID;

	private String sessionID;

	public JDBInterfaceLog(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBInterfaceLog(Timestamp eventTime, Long logID, String messageRef, String interfaceType, String messageInfo, String interfaceDirection, Timestamp messageDateTime, String messageStatus, String messageError, String action)
	{
		setEventTime(eventTime);
		setInterfaceLogID(logID);
		setMessageRef(messageRef);
		setInterfaceType(interfaceType);
		setMessageInformation(messageInfo);
		setInterfaceDirection(interfaceDirection);
		setMessageDate(messageDateTime);
		setMessageStatus(messageStatus);
		setMessageError(messageError);
		setAction(action);
		create();
	}

	public void clear()
	{
		setEventTime(null);
		setMessageRef("");
		setInterfaceType("");
		setMessageInformation("");
		setInterfaceDirection("");
		setMessageDate(null);
		setMessageStatus("");
	}

	public boolean create()
	{

		logger.debug("create [" + getInterfaceType() + "][" + getInterfaceDirection() + "]");

		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBInterfaceLog.create"));
			stmtupdate.setTimestamp(1, getEventTime());
			stmtupdate.setLong(2, generateNewInterfaceLogID());
			stmtupdate.setString(3, getMessageRef());
			stmtupdate.setString(4, getInterfaceType());
			stmtupdate.setString(5, getMessageInformation());
			stmtupdate.setString(6, getInterfaceDirection());
			stmtupdate.setString(7, getAction());
			stmtupdate.setTimestamp(8, getMessageDate());
			stmtupdate.setString(9, getMessageStatus());
			stmtupdate.setString(10, getMessageError());
			stmtupdate.setString(11, JUtility.getClientName());
			stmtupdate.setString(12, getFilename());

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

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBInterfaceLog.delete"));
			stmtupdate.setLong(1, getInterfaceLogID());
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

	public boolean archive()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBInterfaceLog.archive1"));
			stmtupdate.execute();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBInterfaceLog.archive2"));
			stmtupdate.execute();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public ResultSet getInterfaceDataResultSet(PreparedStatement criteria)
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

	public long generateNewInterfaceLogID()
	{
		long result = 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		String interfaceLogID_str = "1";
		long interfaceLogID = 1;

		boolean retry = true;
		@SuppressWarnings("unused")
		int counter = 0;

		if (ctrl.getProperties("INTERFACE LOG ID") == true)
		{
			do
			{
				if (ctrl.lockRecord("INTERFACE LOG ID") == true)
				{
					if (ctrl.getProperties("INTERFACE LOG ID") == true)
					{
						interfaceLogID_str = ctrl.getKeyValue();
						interfaceLogID = Long.parseLong(interfaceLogID_str);
						interfaceLogID++;
						temp = String.valueOf(interfaceLogID);
						ctrl.setKeyValue(temp);

						if (ctrl.update())
						{
							retry = false;
						}
					}
				} else
				{
					logger.debug("Record Locked !");
					retry = true;
					counter++;
				}
			} while (retry);
		} else
		{
			ctrl.getKeyValueWithDefault("INTERFACE LOG ID", "1", "Unique Interface Log ID");
			interfaceLogID = 1;
		}

		result = interfaceLogID;
		setInterfaceLogID(result);
		logger.debug("InterfaceLogID :" + result);
		return result;
	}

	public String getAction()
	{
		return dbAction;
	}

	public String getErrorMessage()
	{
		return JUtility.replaceNullStringwithBlank(dbMessageError);
	}

	public String getFilename()
	{
		return JUtility.replaceNullStringwithBlank(dbFilename);
	}

	public Timestamp getEventTime()
	{
		return dbEventTime;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getInterfaceDirection()
	{
		return dbInterfaceDirection;
	}

	public Vector<JDBInterfaceLog> getInterfaceLogData(PreparedStatement criteria)
	{

		ResultSet rs;
		Vector<JDBInterfaceLog> result = new Vector<JDBInterfaceLog>();

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBInterfaceLog(JUtility.getSQLDateTime(), Long.valueOf("0"), "messageRef", "interfaceType", "messageInfo", "interfaceDirection", JUtility.getSQLDateTime(), "messageStatus", "messageError", "action"));
		} else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					getPropertiesfromResultSet(rs);
					result.addElement(
							new JDBInterfaceLog(getEventTime(), getInterfaceLogID(), getMessageRef(), getInterfaceType(), getMessageInformation(), getInterfaceDirection(), getMessageDate(), getMessageStatus(), getMessageError(), getAction()));
				}
				rs.close();

			} catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public ResultSet getInterfaceLogDataResultSet(PreparedStatement criteria)
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

	public Long getInterfaceLogID()
	{
		return dbInterfaceLogID;
	}

	public boolean getInterfaceLogProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getInterfaceLogProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBInterface.getInterfaceLogProperties"));
			stmt.setFetchSize(1);
			stmt.setLong(1, getInterfaceLogID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid InterfaceLogID");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public boolean getInterfaceLogProperties(Long logID)
	{
		setInterfaceLogID(logID);
		return getInterfaceLogProperties();
	}

	public ResultSet getInterfaceLogResultSet(PreparedStatement criteria)
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

	public String getInterfaceType()
	{
		return dbInterfaceType;
	}

	public Timestamp getMessageDate()
	{
		return dbMessageDate;
	}

	private String getMessageError()
	{
		return dbMessageError;
	}

	public String getMessageInformation()
	{
		return dbMessageInformation;
	}

	public String getMessageRef()
	{
		return dbMessageRef;
	}

	public String getMessageStatus()
	{
		return dbMessageStatus;
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setEventTime(rs.getTimestamp("event_time"));
			setInterfaceLogID(rs.getLong("Interface_Log_ID"));
			setMessageRef(rs.getString("message_ref"));
			setInterfaceType(rs.getString("interface_type"));
			setMessageInformation(rs.getString("message_information"));
			setInterfaceDirection(rs.getString("interface_direction"));
			setAction(rs.getString("action"));
			setMessageDate(rs.getTimestamp("message_date"));
			setMessageStatus(rs.getString("message_status"));
			setMessageError(rs.getString("message_error"));
			setWorkstationID(rs.getString("workstation_id"));
			setFilename(rs.getString("filename"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getWorkstationID()
	{
		return dbWorkstationID;
	}

	public void setAction(String Action)
	{
		this.dbAction = Action;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbMessageStatus = errorMsg;
	}

	public void setEventTime(Timestamp eventTime)
	{
		dbEventTime = eventTime;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setInterfaceDirection(String direction)
	{
		dbInterfaceDirection = direction;
	}

	public void setInterfaceLogID(Long logID)
	{
		dbInterfaceLogID = logID;
	}

	public void setInterfaceType(String type)
	{
		dbInterfaceType = type;
	}

	public void setMessageDate(Timestamp messagedate)
	{
		dbMessageDate = messagedate;
	}

	private void setMessageError(String messageError)
	{
		dbMessageError = messageError;
	}

	private void setFilename(String file)
	{
		dbFilename = file;
	}

	public void setMessageInformation(String messageinformation)
	{
		dbMessageInformation = messageinformation.replaceAll("\\n", "");
		dbMessageInformation = dbMessageInformation.replaceAll("  ", " ");
		dbMessageInformation = JUtility.left(dbMessageInformation, field_message_information);
	}

	public void setMessageRef(String messageref)
	{
		messageref = JUtility.replaceNullStringwithBlank(messageref);
		dbMessageRef = messageref.replaceAll("\\n", "");
		dbMessageRef = dbMessageRef.replaceAll("  ", " ");
		dbMessageRef = JUtility.left(messageref, field_message_ref);
	}

	public void setMessageStatus(String messagestatus)
	{
		dbMessageStatus = messagestatus;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setWorkstationID(String workstationID)
	{
		dbWorkstationID = workstationID;
	}

	public void write(GenericMessageHeader gmh, String messageStatus, String messageError, String action, String filename)
	{
		setEventTime(JUtility.getSQLDateTime());
		setMessageRef(gmh.getMessageRef());
		setInterfaceType(gmh.getInterfaceType());
		setMessageInformation(gmh.getMessageInformation());
		setInterfaceDirection(gmh.getInterfaceDirection());
		setMessageDate(gmh.getMessageDateTimeStamp());
		setMessageStatus(messageStatus);
		setMessageError(messageError);
		setAction(action);
		setFilename(filename);
		create();
	}

	public void write(String messageRef, String interfaceType, String messageInfo, String interfaceDirection, Timestamp messageDateTime, String messageStatus, String messageError, String action)
	{
		setEventTime(JUtility.getSQLDateTime());
		setMessageRef(messageRef);
		setInterfaceType(interfaceType);
		setMessageInformation(messageInfo);
		setInterfaceDirection(interfaceDirection);
		setMessageDate(messageDateTime);
		setMessageStatus(messageStatus);
		setMessageError(messageError);
		setAction(action);
		create();
	}

}
