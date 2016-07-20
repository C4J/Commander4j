package com.commander4j.sys;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.commander4j.db.JDBUser;

public class JUserList
{

	private Hashtable<String, JDBUser> users = new Hashtable<String, JDBUser>();
	private final Logger logger = Logger.getLogger(JUserList.class);

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
