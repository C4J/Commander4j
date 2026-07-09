package com.commander4j.util;


/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JWait.java
 * 
 * Package Name : com.commander4j.util
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

public class JWait
{
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JWait.class);

	public static void milliSec(long ms) {
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{
			logger.error(e);
		}
	}

	public static void oneSec() {
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			logger.error(e);
		}
	}

	public static void manySec(long s) {
		try
		{
			Thread.sleep(s * 1000);
		}
		catch (InterruptedException e)
		{
			logger.error(e);
		}
	}

}
