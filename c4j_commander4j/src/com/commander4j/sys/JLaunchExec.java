package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JLaunchExec.java
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

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBModule;
import com.commander4j.util.JUtility;

public class JLaunchExec
{

	public static JDBModule mod;

	public static void runExec(String host, String session, String optionName) {
		final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JLaunchMenu.class);
		mod = new JDBModule(host, session);

		logger.debug("runForm :" + optionName);

		mod.setModuleId(optionName);
		if (mod.getModuleProperties() == true)
		{
			String command = mod.getExecFilename();
			String dir = mod.getExecDir();
			JUtility.exec(command, dir);

		}

	}
}
