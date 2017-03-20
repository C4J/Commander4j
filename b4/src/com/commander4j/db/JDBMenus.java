package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBMenus.java
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
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;


public class JDBMenus
{

	private String dbErrorMessage;

	private String dbMenuId;

	private String dbModuleId;

	private int dbSequenceId;

	private final Logger logger = Logger.getLogger(JDBMenus.class);
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

	public JDBMenus(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	/**
	 * Method create.
	 * 
	 * @param lMenuId
	 *            String
	 * @param lModuleId
	 *            String
	 * @param lSequenceId
	 *            int
	 * @return boolean
	 */
	public boolean create(String lMenuId, String lModuleId, int lSequenceId) {
		boolean result = false;
		setErrorMessage("");

		try
		{
			setMenuId(lMenuId);
			setModuleId(lModuleId);
			setSequenceId(lSequenceId);

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.create"));
			stmtupdate.setString(1, getMenuId());
			stmtupdate.setString(2, getModuleId());
			stmtupdate.setInt(3, getSequenceId());
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

	/**
	 * Method deleteMenusForMenuId.
	 * 
	 * @param menuid
	 *            String
	 * @return boolean
	 */
	public boolean deleteMenusForMenuId(String menuid) {
		setMenuId(menuid);
		return deleteMenusForMenuId();
	}

	/**
	 * Method deleteMenusForMenuId.
	 * 
	 * @return boolean
	 */
	public boolean deleteMenusForMenuId() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.deleteMenusForMenuId"));
			stmtupdate.setString(1, getMenuId());
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

	/**
	 * Method deleteMenusForModuleId.
	 * 
	 * @param moduleid
	 *            String
	 * @return boolean
	 */
	public boolean deleteMenusForModuleId(String moduleid) {
		setModuleId(moduleid);
		return deleteMenusForModuleId();
	}

	/**
	 * Method deleteMenusForModuleId.
	 * 
	 * @return boolean
	 */
	public boolean deleteMenusForModuleId() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.deleteMenusForModuleId"));
			stmtupdate.setString(1, getModuleId());
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
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.delete"));
			stmtupdate.setString(1, getMenuId());
			stmtupdate.setString(2, getModuleId());
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

	/**
	 * Method getErrorMessage.
	 * 
	 * @return String
	 */
	public String getErrorMessage() {
		return dbErrorMessage;
	}

	/**
	 * Method getMenuId.
	 * 
	 * @return String
	 */
	public String getMenuId() {
		return dbMenuId;
	}

	/**
	 * Method getModuleId.
	 * 
	 * @return String
	 */
	public String getModuleId() {
		return dbModuleId;
	}

	/**
	 * Method getSequenceId.
	 * 
	 * @return int
	 */
	public int getSequenceId() {
		return dbSequenceId;
	}

	/**
	 * Method renameModuleTo.
	 * 
	 * @param newModuleId
	 *            String
	 * @return boolean
	 */
	public boolean renameModuleTo(String newModuleId) {
		boolean result = false;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.renameModuleTo"));
			stmtupdate.setString(1, newModuleId);
			stmtupdate.setString(2, getModuleId());
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

	/**
	 * Method renameMenuTo.
	 * 
	 * @param newMenuId
	 *            String
	 * @return boolean
	 */
	public boolean renameMenuTo(String newMenuId) {
		boolean result = false;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.renameMenuTo"));
			stmtupdate.setString(1, newMenuId);
			stmtupdate.setString(2, getMenuId());
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


	public boolean rewriteMenu(String lMenuId, LinkedList<JDBListData> modules) {
		boolean result = false;
		String lModuleId;
		int lSequenceId = 0;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.rewriteMenu"));
			stmtupdate.setString(1, lMenuId);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();

			for (int j = 0; j < modules.size(); j++)
			{
				lModuleId = modules.get(j).toString();
				create(lMenuId, lModuleId, lSequenceId++);
			}
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	/**
	 * Method setErrorMessage.
	 * 
	 * @param errorMsg
	 *            String
	 */
	private void setErrorMessage(String errorMsg) {
		logger.error(errorMsg);
		dbErrorMessage = errorMsg;
	}

	/**
	 * Method setMenuId.
	 * 
	 * @param menuId
	 *            String
	 */
	public void setMenuId(String menuId) {
		dbMenuId = menuId;
	}

	/**
	 * Method setModuleId.
	 * 
	 * @param moduleId
	 *            String
	 */
	public void setModuleId(String moduleId) {
		dbModuleId = moduleId;
	}

	/**
	 * Method setSequenceId.
	 * 
	 * @param sequenceId
	 *            int
	 */
	public void setSequenceId(int sequenceId) {
		dbSequenceId = sequenceId;
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
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.update"));
			stmtupdate.setString(1, getMenuId());
			stmtupdate.setString(2, getModuleId());
			stmtupdate.setInt(3, getSequenceId());
			stmtupdate.setString(4, getMenuId());
			stmtupdate.setString(5, getModuleId());
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
}
