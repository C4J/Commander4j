package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBUom.java
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
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBUom
{

	private String db_description;
	private String db_error_message;
	private String db_iso_uom;
	private String db_local_uom;
	private String db_uom;

	final Logger logger = Logger.getLogger(JDBUom.class);

	public static int field_uom = 3;
	public static int field_description = 50;
	private String hostID;
	private String sessionID;

	public JDBUom(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBUom(String host, String session, String uom, String description)
	{
		setInternalUom(uom);
		setDescription(description);
	}

	public void clear() {
		setDescription("");
		setIsoUom("");
		setLocalUom("");
		setInternalUom("");
		setErrorMessage("");
	}

	public boolean create(String luom, String liso_uom, String loc_uom, String ldescription) {
		boolean result = false;
		setErrorMessage("");

		try
		{
			setInternalUom(luom);
			setDescription(ldescription);
			setLocalUom(loc_uom);
			setIsoUom(liso_uom);

			if (isValidInternalUom() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUom.create"));
				stmtupdate.setString(1, getInternalUom());
				stmtupdate.setString(2, getIsoUom());
				stmtupdate.setString(3, getDescription());
				stmtupdate.setString(4, getLocalUom());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("UOM already exists");
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
			if (isValidInternalUom() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUom.delete"));
				stmtupdate.setString(1, getInternalUom());
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
	 * Method getDescription.
	 * 
	 * @return String
	 */
	public String getDescription() {
		String result = "";
		if (db_description != null)
			result = db_description;
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

	private String getHostID() {
		return hostID;
	}

	/**
	 * Method getIsoUom.
	 * 
	 * @return String
	 */
	public String getIsoUom() {
		return db_iso_uom;
	}

	// ===============================================================

	public String convertUom(String conversion, String uom) {
		String result = "";

		uom = JUtility.replaceNullStringwithBlank(uom.trim());

		if (uom.length() > 0)
		{

			if (conversion.equals(Common.UOM_Convert_Internal_to_ISO))
			{
				result = getISOUomfromInternalUom(uom);
			}
			if (conversion.equals(Common.UOM_Convert_Internal_to_Local))
			{
				result = getLocalfromInternalUom(uom);
			}
			if (conversion.equals(Common.UOM_Convert_None))
			{
				result = uom;
			}
			if (conversion.equals(Common.UOM_Convert_ISO_to_INTERNAL))
			{
				result = getInternalfromISOUom(uom);
			}
			if (conversion.equals(Common.UOM_Convert_ISO_to_Local))
			{
				result = getLocalUomfromISOUom(uom);
			}
			if (conversion.equals(Common.UOM_Convert_Local_to_ISO))
			{
				result = getISOUomfromLocalUom(uom);
			}
			if (conversion.equals(Common.UOM_Convert_Local_to_Internal))
			{
				result = getInternalUomfromLocalUom(uom);
			}
			if (result.equals(""))
			{
				result = uom;
				logger.debug("Unable to convert [" + uom + "] using [" + conversion + "]");
			}
		}

		return result;
	}

	// "INTERNAL to ISO"

	public String getISOUomfromInternalUom(String uom) {
		String result = "";

		if (getInternalUomProperties(uom))
		{
			result = getIsoUom();
		}
		return result;
	}

	// "INTERNAL to Local"

	public String getLocalfromInternalUom(String uom) {
		String result = "";

		if (getInternalUomProperties(uom))
		{
			result = getLocalUom();
		}
		return result;
	}

	// "None"

	// "ISO to INTERNAL"

	public String getInternalfromISOUom(String uom) {
		String result = "";

		if (getISOUomProperties(uom))
		{
			result = getInternalUom();
		}
		return result;
	}

	// "ISO to Local"

	public String getLocalUomfromISOUom(String uom) {
		String result = "";

		if (getISOUomProperties(uom))
		{
			result = getLocalUom();
		}
		return result;
	}

	// "Local to ISO"

	public String getISOUomfromLocalUom(String uom) {
		String result = "";

		if (getLocalUomProperties(uom))
		{
			result = getIsoUom();
		}
		return result;
	}

	// "Local to INTERNAL"

	public String getInternalUomfromLocalUom(String uom) {
		String result = "";

		if (getLocalUomProperties(uom))
		{
			result = getInternalUom();
		}
		return result;
	}

	// ===============================================================

	public boolean getISOUomProperties() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUom.getISOUomProperties"));
			stmt.setString(1, getIsoUom());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setInternalUom(rs.getString("uom"));
				setIsoUom(rs.getString("iso_uom"));
				setLocalUom(rs.getString("local_uom"));
				setDescription(rs.getString("description"));
				result = true;
			}
			else
			{
				setErrorMessage("Invalid ISO UOM");
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

	public boolean getISOUomProperties(String uom) {
		boolean result = false;
		setIsoUom(uom);
		result = getISOUomProperties();
		return result;
	}

	public boolean getLocalUomProperties(String uom) {
		boolean result = false;
		setLocalUom(uom);
		result = getLocalUomProperties();
		return result;
	}

	public String getLocalUom() {
		return db_local_uom;
	}

	public boolean getLocalUomProperties() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUom.getLocalUomProperties"));
			stmt.setString(1, getLocalUom());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setInternalUom(rs.getString("uom"));
				setIsoUom(rs.getString("iso_uom"));
				setLocalUom(rs.getString("local_uom"));
				setDescription(rs.getString("description"));
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Local UOM");
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

	private String getSessionID() {
		return sessionID;
	}

	/**
	 * Method getUom.
	 * 
	 * @return String
	 */
	public String getInternalUom() {
		String result = "";
		if (db_uom != null)
			result = db_uom;
		return result;
	}

	public ResultSet getUomDataResultSet() {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUom.getUoms"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public String getUomfromISOUom(String uom) {
		String result = "";

		if (getISOUomProperties(uom))
		{

			result = getInternalUom();
			logger.debug("Standard Uom is [" + result + "] for ISO Uom [" + uom + "]");
		}
		return result;
	}

	/**
	 * Method getUomProperties.
	 * 
	 * @return boolean
	 */
	public boolean getInternalUomProperties() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUom.getUomProperties"));
			stmt.setString(1, getInternalUom());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setInternalUom(rs.getString("uom"));
				setIsoUom(rs.getString("iso_uom"));
				setLocalUom(rs.getString("local_uom"));
				setDescription(rs.getString("description"));
				result = true;
			}
			else
			{
				setErrorMessage("Invalid UOM");
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
	 * Method getUomProperties.
	 * 
	 * @param uom
	 *            String
	 * @return boolean
	 */
	public boolean getInternalUomProperties(String uom) {
		boolean result = false;
		setInternalUom(uom);
		result = getInternalUomProperties();
		return result;
	}


	public LinkedList<JDBUom> getInternalUoms() {
		LinkedList<JDBUom> uomList = new LinkedList<JDBUom>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUom.getUoms"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBUom uom = new JDBUom(getHostID(), getSessionID());
				uom.setInternalUom(rs.getString("uom"));
				uom.setDescription(rs.getString("description"));
				uomList.add(uom);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return uomList;
	}

	/**
	 * Method isValidUom.
	 * 
	 * @return boolean
	 */
	public boolean isValidInternalUom() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUom.isValidUom"));
			stmt.setString(1, getInternalUom());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid UOM [" + getInternalUom() + "]");
			}
			stmt.close();
			rs.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	/**
	 * Method isValidUom.
	 * 
	 * @param uom
	 *            String
	 * @return boolean
	 */
	public boolean isValidInternalUom(String uom) {
		setInternalUom(uom);
		return isValidInternalUom();
	}

	/**
	 * Method renameTo.
	 * 
	 * @param newUom
	 *            String
	 * @return boolean
	 */
	public boolean renameInternalUomTo(String newUom) {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidInternalUom() == true)
			{
				JDBUom newuom = new JDBUom(getHostID(), getSessionID());
				newuom.setInternalUom(newUom);
				if (newuom.isValidInternalUom() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUom.renameTo"));
					stmtupdate.setString(1, newUom);
					stmtupdate.setString(2, getInternalUom());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setInternalUom(newUom);
					result = true;
				}
				else
				{
					setErrorMessage("New UOM is already in use.");
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
	 * Method setDescription.
	 * 
	 * @param Description
	 *            String
	 */
	public void setDescription(String Description) {
		db_description = Description;
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
			logger.debug(ErrorMsg);
		}
		db_error_message = ErrorMsg;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	/**
	 * Method setIsoUom.
	 * 
	 * @param iso_uom
	 *            String
	 */
	public void setIsoUom(String iso_uom) {
		db_iso_uom = iso_uom;
	}

	public void setLocalUom(String loc_uom) {
		db_local_uom = loc_uom;
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	/**
	 * Method setUom.
	 * 
	 * @param uom
	 *            String
	 */
	public void setInternalUom(String uom) {
		db_uom = uom;
	}

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	public String toString() {
		String result = "";
		if (getInternalUom().equals("") == false)
			result = JUtility.padString(getInternalUom(), true, field_uom, " ") + " - " + getDescription();
		else
			result = "";

		return result;
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
			if (isValidInternalUom() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUom.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getIsoUom());
				stmtupdate.setString(3, getLocalUom());
				stmtupdate.setString(4, getInternalUom());
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
