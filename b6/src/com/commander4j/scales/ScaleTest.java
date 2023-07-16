package com.commander4j.scales;

import org.apache.log4j.Logger;

import com.commander4j.db.JDBUser;

import com.commander4j.sys.Common;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : Interface4j.java
 * 
 * Package Name : com.commander4j.service
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

import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;

public class ScaleTest {
	private String hostID = "";
	private String sessionID = "";
	private final Logger logger = Logger.getLogger(ScaleTest.class);

	private JDBUser userdb;
	
	public ScaleTest()
	{
		
	}
	
	private void setHostID(String host)
	{
		hostID = host;
	}

	private String getHostID()
	{
		return hostID;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	private void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}

	public static void main(String[] args)
	{
		ScaleTest scaleTest = new ScaleTest();
		
		scaleTest.initTestOnly();

	}
	
	private boolean initTestOnly()
	{
		boolean result = false;

		JUtility.initLogging("");

		Common.base_dir = System.getProperty("user.dir");
		String uniqueID = "";

		uniqueID = "service";

		logger.debug("Checking if parameter [" + uniqueID + "] is a Unique Host ID");

		Common.hostList.loadHosts();

		setHostID(Common.hostList.getHostIDforUniqueId(uniqueID));
		Common.selectedHostID = getHostID();
		setSessionID(JUnique.getUniqueID());


		logger.debug("Connecting to database.");
		while ((Common.hostList.getHost(getHostID()).isConnected(getSessionID()) == false))
		{
			Common.hostList.getHost(getHostID()).connect(getSessionID(), getHostID());
		}



		userdb = new JDBUser(getHostID(), getSessionID());

		userdb.setUserId("interface");
		userdb.setPassword("interface");
		userdb.setLoginPassword("interface");

		Common.userList.addUser(getSessionID(), userdb);

		if (hostID.length() > 0)
		{
			result = true;
		}
		else
		{
			logger.debug("No Host has been identified to be target for service - check hosts file unique id.");

			logger.debug("Host with unique id  [" + uniqueID + "] not found - Interface thread aborting.");

			result = false;
		}

		class test implements ScaleCallbackInteface
		{

			@Override
			public void setWeight(String weight)
			{
				System.out.println("Parent method : "+weight);
				
			}
			
		}
		test cb = new test();
		
		Scale scale = new Scale(getHostID(),getSessionID());
		scale.setCallbackInterface(cb);
		
		System.out.println("Starting");
		scale.start();
		System.out.println("Stopped");
		
		return result;

	}

}
