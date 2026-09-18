/*
 * Created on 05-Apr-2005
 *
 */
package com.commander4j.html;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JMenuRFMenu.java
 * 
 * Package Name : com.commander4j.html
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
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.sys.JMenuOption;

/**
 * @author David
 * 
 */
public class JMenuRFMenu
{

	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JMenuRFMenu.class);
	private String hostID;
	private String sessionID;
	private int checkedIndex;
	private String CRLF = "\n";

	public JMenuRFMenu(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private String getHostID() {
		return hostID;
	}

	private String getSessionID() {
		return sessionID;
	}
	
	public int getCheckedIndex() {
		return checkedIndex;
	}

	public String getCheckedIndexString() {
		return String.valueOf(checkedIndex);
	}	

	/** The pseudo-parent of the top-level RF menu rows, as in SYS_MENUS. */
	public static final String ROOT_MENU_ID = "root";

	/**
	 * The RF menu options for the session's user as data, in menu order - the
	 * same rows buildMenu() renders as radio buttons, without the HTML. Added
	 * 2026-09-11 for c4j_commander4j_web; buildMenu() is unchanged.
	 * Since schema 217 SYS_RF_MENU is a tree like SYS_MENUS; this returns the
	 * top level (MENU_ID = root).
	 */
	public LinkedList<JMenuOption> getMenuOptions() {
		return getMenuOptions(ROOT_MENU_ID);
	}

	/**
	 * The RF menu options directly under one menu (SYS_RF_MENU.MENU_ID, schema
	 * 217): root for the top level, or the module id of a MENU-type row. Same
	 * rf_active + group permission filter as buildMenu(). Added 2026-09-17 for
	 * the nested menu in c4j_commander4j_web. A failure (schema 217 not applied,
	 * sql key missing) is logged at warn - it would otherwise look like an
	 * empty menu.
	 */
	public LinkedList<JMenuOption> getMenuOptions(String menuID) {

		LinkedList<JMenuOption> result = new LinkedList<JMenuOption>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JMenuRFMenu.getMenuOptions"));
			stmt.setString(1, menuID);
			stmt.setString(2, Common.userList.getUser(getSessionID()).getUserId());
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				JMenuOption menuOption = new JMenuOption(getHostID(), getSessionID());
				menuOption.load(rs);
				result.add(menuOption);
			}
		}
		catch (Exception ex)
		{
			logger.warn("Error in JMenuRFMenu.getMenuOptions(" + menuID + ") " + ex.getMessage());
		}
		finally
		{
			try
			{
				if (rs != null)
				{
					rs.close();
				}
				if (stmt != null)
				{
					stmt.close();
				}
			}
			catch (Exception ex)
			{
			}
		}
		return result;
	}

	public String buildMenu(String defaultItem) {

		String result = "";
		String selected = "";
		
		checkedIndex = 0;
		
		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JMenuRFMenu.buildMenu"));
			stmt.setString(1, Common.userList.getUser(getSessionID()).getUserId());
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();
			JMenuOption menuOption = new JMenuOption(getHostID(), getSessionID());
			int x = 0;
			while (rs.next())
			{

				menuOption.load(rs);
				
				if (defaultItem.equals("")==true)
				{
					defaultItem = menuOption.moduleID;
				}
				
				if (menuOption.moduleID.equals(defaultItem))
				{
					selected = "checked=\"checked\"";
					checkedIndex = x;
				}
				else
				{
					selected = "";
				}				
				
				logger.debug(Common.userList.getUser(getSessionID()).getUserId() + " " + menuOption.description);
				result = result + "<label><input type=\"radio\" name=\"selectedMenuOption\" value=\"" + menuOption.moduleID + "\" " + selected + "/>" + menuOption.description + "</label><br/>"+CRLF;
				x++;
			}
		}
		catch (Exception ex)
		{
			logger.debug("Error in JMenuRFMenu " + ex.getMessage());
		}
		return result;

	}

}
