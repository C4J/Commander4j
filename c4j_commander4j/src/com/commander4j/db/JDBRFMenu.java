package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBRFMenu.java
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
import com.commander4j.util.JUtility;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;

/**
 * JDBRFMenu class is used to insert/update/delete records from the
 * SYS_RF_MENU table. 
 * 
 * <p>
 * <img alt="" src="./doc-files/SYS_RF_MENU.jpg" >
 */
public class JDBRFMenu
{

	private String dbErrorMessage;

	private String dbModuleId;

	/** parent menu (SYS_RF_MENU.MENU_ID, schema 217): "root" for the top level, else a MENU module id */
	private String dbMenuId;

	private int dbSequenceId;

	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBRFMenu.class);
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

	public JDBRFMenu(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}


	public boolean create(String lModuleId, int lSequenceId) {
		boolean result = false;
		setErrorMessage("");

		try
		{
			setModuleId(lModuleId);
			setSequenceId(lSequenceId);

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.create"));
			stmtupdate.setString(1, getModuleId());
			stmtupdate.setInt(2, getSequenceId());
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
	 * Insert a row under a given parent menu (schema 217 - SYS_RF_MENU is a tree like SYS_MENUS).
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
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.createInMenu"));
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


	public boolean delete() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.delete"));
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
	 * Remove every row under a menu (a MENU module being deleted, as JDBMenus.deleteMenusForMenuId).
	 */
	public boolean deleteForMenuId(String menuId) {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			setMenuId(menuId);
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.deleteForMenuId"));
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


	public String getErrorMessage() {
		return dbErrorMessage;
	}


	public String getMenuId() {
		return JUtility.replaceNullStringwithBlank(dbMenuId);
	}


	public String getModuleId() {
		return JUtility.replaceNullStringwithBlank(dbModuleId);
	}


	public int getSequenceId() {
		return dbSequenceId;
	}


	public boolean renameModuleTo(String newModuleId) {
		boolean result = false;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.renameModuleTo"));
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
	 * Re-point the rows of a menu that is being renamed (as JDBMenus.renameMenuTo).
	 */
	public boolean renameMenuTo(String newMenuId) {
		boolean result = false;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.renameMenuTo"));
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


	/**
	 * Replace the rows of ONE menu with the given modules in order 0..n, as
	 * JDBMenus.rewriteMenu does for SYS_MENUS. Rows of other menus are untouched.
	 * (Schema 217 - the former whole-table rewriteRFMenu(list) is gone: it would
	 * have flattened the tree.)
	 */
	public boolean rewriteRFMenu(String lMenuId, LinkedList<JDBListData> modules) {
		boolean result = false;
		String lModuleId;
		int lSequenceId = 0;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.rewriteRFMenuForMenu"));
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


	private void setErrorMessage(String errorMsg) {
		logger.error(errorMsg);
		dbErrorMessage = errorMsg;
	}


	public void setMenuId(String menuId) {
		dbMenuId = menuId;
	}


	public void setModuleId(String moduleId) {
		dbModuleId = moduleId;
	}


	public void setSequenceId(int sequenceId) {
		dbSequenceId = sequenceId;
	}


	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.update"));
			stmtupdate.setString(1, getModuleId());
			stmtupdate.setInt(2, getSequenceId());
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
