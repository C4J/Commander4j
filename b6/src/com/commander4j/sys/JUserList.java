package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JUserList.java
 * 
 * Package Name : com.commander4j.sys
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

import java.util.Hashtable;

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBUser;

public class JUserList
{

	private Hashtable<String, JDBUser> users = new Hashtable<String, JDBUser>();
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JUserList.class);

	public void addUser(String sessionID, JDBUser usr) {
		if (users.containsKey(sessionID) == true)
		{
			users.remove(sessionID);
		}
		users.put(sessionID, usr);
	}

	public void removeUser(String sessionID) {
		if (users.containsKey(sessionID) == true)
		{
			users.remove(sessionID);
		}
	}

	public void clear() {
		users.clear();
	}

	public int size() {
		return users.size();
	}

	public JDBUser getUser(String sessionID) {
		JDBUser usr = new JDBUser("", "");
		if (users.containsKey(sessionID) == true)
		{
			usr = users.get(sessionID);
		}
		else
		{
			logger.error("JUserList error during getUser - cannot find Session ID: " + sessionID);
		}
		return usr;
	}

}
