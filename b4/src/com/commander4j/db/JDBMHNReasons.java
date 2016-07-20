
package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;


public class JDBMHNReasons
{
	private String dbErrorMessage;
	private String dbDescription;
	private String dbReason;
	public static int field_reason = 10;
	public static int field_description = 50;
	private final Logger logger = Logger.getLogger(JDBMHNReasons.class);
	private String hostID;
	private String sessionID;

	private void setSessionID(String session) {
		sessionID = session;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private String getSessionID() {
		return sessionID;
	}

	private String getHostID() {
		return hostID;
	}

	public JDBMHNReasons(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

	}

	public boolean create(String reason, String description) {
		boolean result = false;
		setErrorMessage("");

		try
		{
			setReason(reason);

			setDescription(description);

			if (isValidReason() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNReason.create"));
				stmtupdate.setString(1, getReason());
				stmtupdate.setString(2, getDescription());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Reason already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidReason() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNReason.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getReason());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
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
			if (isValidReason() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNReason.delete"));
				stmtupdate.setString(1, getReason());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean renameTo(String newReason) {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidReason() == true)
			{
				JDBMHNReasons mattype = new JDBMHNReasons(getHostID(), getSessionID());
				mattype.setReason(newReason);
				if (mattype.isValidReason() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNReason.renameTo"));
					stmtupdate.setString(1, newReason);
					stmtupdate.setString(2, getReason());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setReason(newReason);
					result = true;
				}
				else
				{
					setErrorMessage("New Reason Code is already in use.");
				}
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidReason() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNReason.isValidReason"));
			stmt.setString(1, getReason());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				if (getReason().equals("")==false)
				{
					setErrorMessage("Invalid Reason [" + getReason() + "]");
				}
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

	public JDBMHNReasons(String host, String session, String reason, String description)
	{
		setHostID(host);
		setSessionID(session);
		setReason(reason);
		setDescription(description);
	}

	public String getDescription() {
		String result = "";
		if (dbDescription != null)
			result = dbDescription;
		return result;
	}

	public String getErrorMessage() {
		return dbErrorMessage;
	}

	public void clear() {
		// setType("");
		setDescription("");
		setErrorMessage("");
	}
	
	public boolean getReasonProperties(String reason) {
		setReason(reason);
		return getReasonProperties();
	}

	public boolean getReasonProperties() {
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getReasonProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNReason.getReasonProperties"));
			stmt.setString(1, getReason());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDescription(rs.getString("description"));
				result = true;
			}
			else
			{
				if (getReason().equals("")==false)
				{
					setErrorMessage("Invalid Reason");
				}
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

	public Vector<JDBMHNReasons> getReasons() {
		Vector<JDBMHNReasons> typeList = new Vector<JDBMHNReasons>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHNReason.getReasons"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBMHNReasons mt = new JDBMHNReasons(getHostID(), getSessionID());
				mt.setReason(rs.getString("reason"));
				mt.setDescription(rs.getString("description"));
				typeList.add(mt);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return typeList;
	}

	public String getReason() {
		String result = "";
		if (dbReason != null)
			result = dbReason;
		return result;
	}

	public void setDescription(String description) {
		dbDescription = description;
	}

	private void setErrorMessage(String errorMsg) {
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setReason(String reason) {
		dbReason = reason;
	}

	public String toString() {
		String result = "";
		if (getReason().equals("") == false)
		{
			result = JUtility.padString(getReason(), true, field_reason, " ") + " - " + getDescription();
		}
		else
		{
			result = "";
		}

		return result;
	}
}
