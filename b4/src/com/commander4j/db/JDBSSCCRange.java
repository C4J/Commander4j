/*
 * Created on 12-Jun-2005
 *
 */
package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * @author David
 * 
 * @version $Revision: 1.0 $
 */
public class JDBSSCCRange
{
	/**
	 * @uml.property name="db_workstation"
	 */
	private String db_workstation;
	/**
	 * @uml.property name="db_error_message"
	 */
	private String db_error_message;
	/**
	 * @uml.property name="db_prefix"
	 */
	private String db_prefix;
	/**
	 * @uml.property name="db_min"
	 */
	private String db_min;
	/**
	 * @uml.property name="db_max"
	 */
	private String db_max;
	/**
	 * @uml.property name="db_next"
	 */
	private String db_next;

	/**
	 * @uml.property name="logger"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	final Logger logger = Logger.getLogger(JDBSSCCRange.class);

	/* Uoms */
	/**
	 * Field field_prefix. Value: {@value field_prefix}
	 */
	public static int field_prefix = 18;
	/**
	 * Field field_min. Value: {@value field_min}
	 */
	public static int field_min = 18;
	/**
	 * Field field_max. Value: {@value field_max}
	 */
	public static int field_max = 18;
	/**
	 * Field field_next. Value: {@value field_next}
	 */
	public static int field_next = 18;
	/**
	 * Field field_workstation. Value: {@value field_workstation}
	 */
	public static int field_workstation = 40;
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

	public JDBSSCCRange(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	/**
	 * Constructor for JDBSSCCRange.
	 * 
	 * @param workstation
	 *            String
	 * @param prefix
	 *            String
	 * @param min
	 *            String
	 * @param max
	 *            String
	 * @param next
	 *            String
	 */
	public JDBSSCCRange(String workstation, String prefix, String min, String max, String next)
	{
		setWorkstation(workstation);
		setPrefix(prefix);
		setMin(min);
		setMax(max);
		setNext(next);
	}

	/**
	 * Method create.
	 * 
	 * @param workstation
	 *            String
	 * @param prefix
	 *            String
	 * @param min
	 *            String
	 * @param max
	 *            String
	 * @param next
	 *            String
	 * @return boolean
	 */
	public boolean create(String workstation, String prefix, String min, String max, String next) {
		boolean result = false;
		setErrorMessage("");

		try
		{

			setWorkstation(workstation);
			setPrefix(prefix);
			setMin(min);
			setMax(max);
			setNext(next);

			if (isValidWorkstation() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.create"));
				stmtupdate.setString(1, getWorkstation());
				stmtupdate.setString(2, getPrefix());
				stmtupdate.setString(3, getMin());
				stmtupdate.setString(4, getMax());
				stmtupdate.setString(5, getNext());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Workstation already exists");
			}
		}
		catch (SQLException e)
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
	public boolean delete() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWorkstation() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.delete"));
				stmtupdate.setString(1, getWorkstation());
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

	/**
	 * Method getWorkstation.
	 * 
	 * @return String
	 */
	public String getWorkstation() {
		String result = "";
		if (db_workstation != null)
			result = db_workstation;
		return result;
	}

	/**
	 * Method getErrorMessage.
	 * 
	 * @return String
	 */
	public String getErrorMessage() {
		return db_error_message;
	}

	/**
	 * Method getWorkstationProperties.
	 * 
	 * @return boolean
	 */
	public boolean getWorkstationProperties() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.getSSCCRangeProperties"));
			stmt.setString(1, getWorkstation());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setPrefix(rs.getString("prefix"));
				setMin(rs.getString("min"));
				setMax(rs.getString("max"));
				setNext(rs.getString("next"));
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Workstation");
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

	/**
	 * Method getPrefix.
	 * 
	 * @return String
	 */
	public String getPrefix() {
		return db_prefix;
	}

	/**
	 * Method getMin.
	 * 
	 * @return String
	 */
	public String getMin() {
		String result = "";
		if (db_min != null)
			result = db_min;
		return result;
	}

	/**
	 * Method getMax.
	 * 
	 * @return String
	 */
	public String getMax() {
		String result = "";
		if (db_max != null)
			result = db_max;
		return result;
	}

	/**
	 * Method getNext.
	 * 
	 * @return String
	 */
	public String getNext() {
		String result = "";
		if (db_next != null)
			result = db_next;
		return result;
	}

	/**
	 * Method getWorkstations.
	 * 
	 * @return LinkedList<JDBSSCCRange>
	 */
	public LinkedList<JDBSSCCRange> getWorkstations() {
		LinkedList<JDBSSCCRange> SSCCRangeList = new LinkedList<JDBSSCCRange>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.getWorkstations"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBSSCCRange SSCCRange = new JDBSSCCRange(getHostID(), getSessionID());
				SSCCRange.setWorkstation(rs.getString("workstation"));
				SSCCRange.setPrefix(rs.getString("prefix"));
				SSCCRange.setMin(rs.getString("min"));
				SSCCRange.setMax(rs.getString("max"));
				SSCCRange.setNext(rs.getString("next"));
				SSCCRangeList.add(SSCCRange);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return SSCCRangeList;
	}

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	public String toString() {
		String result = "";
		if (getMin().equals("") == false)
			result = JUtility.padString(getWorkstation(), true, field_workstation, " ");
		else
			result = "";

		return result;
	}

	/**
	 * Method isValidWorkstation.
	 * 
	 * @return boolean
	 */
	public boolean isValidWorkstation() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.isValidWorkstation"));
			stmt.setString(1, getWorkstation());
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Workstation [" + getWorkstation() + "]");
			}
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	/**
	 * Method isValidWorkstation.
	 * 
	 * @param workstation
	 *            String
	 * @return boolean
	 */
	public boolean isValidWorkstation(String workstation) {
		setWorkstation(workstation);
		return isValidWorkstation();
	}

	/**
	 * Method renameTo.
	 * 
	 * @param newWorkstation
	 *            String
	 * @return boolean
	 */
	public boolean renameTo(String newWorkstation) {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidWorkstation() == true)
			{
				JDBSSCCRange newworkstation = new JDBSSCCRange(getHostID(), getSessionID());
				newworkstation.setWorkstation(newWorkstation);
				if (newworkstation.isValidWorkstation() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.renameTo"));
					stmtupdate.setString(1, newWorkstation);
					stmtupdate.setString(2, getWorkstation());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setWorkstation(newWorkstation);
					result = true;
				}
				else
				{
					setErrorMessage("New Workstation is already in use.");
				}
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	/**
	 * Method setWorkstation.
	 * 
	 * @param workstation
	 *            String
	 */
	public void setWorkstation(String workstation) {
		db_workstation = workstation;
	}

	/**
	 * Method setErrorMessage.
	 * 
	 * @param ErrorMsg
	 *            String
	 */
	private void setErrorMessage(String ErrorMsg) {
		if (ErrorMsg.isEmpty() == false)
		{
			logger.error(ErrorMsg);
		}
		db_error_message = ErrorMsg;
	}

	/**
	 * Method setPrefix.
	 * 
	 * @param prefix
	 *            String
	 */
	public void setPrefix(String prefix) {
		db_prefix = prefix;
	}

	/**
	 * Method setMin.
	 * 
	 * @param min
	 *            String
	 */
	public void setMin(String min) {
		db_min = min;
	}

	/**
	 * Method setMax.
	 * 
	 * @param max
	 *            String
	 */
	public void setMax(String max) {
		db_max = max;
	}

	/**
	 * Method setNext.
	 * 
	 * @param next
	 *            String
	 */
	public void setNext(String next) {
		db_next = next;
	}

	/**
	 * Method update.
	 * 
	 * @return boolean
	 */
	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWorkstation() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBSSCCRange.update"));
				stmtupdate.setString(1, getPrefix());
				stmtupdate.setString(2, getMin());
				stmtupdate.setString(3, getMax());
				stmtupdate.setString(4, getNext());
				stmtupdate.setString(5, getWorkstation());
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

}
