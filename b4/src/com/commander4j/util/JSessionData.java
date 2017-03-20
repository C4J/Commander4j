package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JSessionData.java
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

import java.util.LinkedList;

public class JSessionData
{
	private LinkedList<String> sessionKeys = new LinkedList<String>();
	private LinkedList<String> sessionData = new LinkedList<String>();


	public Boolean setData(String sessionID, String key, String value, boolean allowBlank) {
		Boolean updated = false;
		String lookup = sessionID + "-" + key;
		String data = JUtility.replaceNullObjectwithBlank(value);
		if (data.isEmpty() == true)
			data = "<null>";

		if ((allowBlank == true) | (data.equals("<null>") == false))
		{

			int index = sessionKeys.indexOf(lookup);
			if (index >= 0)
			{

				sessionData.set(index, data);
			}
			else
			{
				sessionKeys.addLast(lookup);
				sessionData.addLast(data);

			}
			updated = true;
		}
		return updated;

	}

	public String getData(String sessionID, String key) {

		String lookup = sessionID + "-" + key;
		String result = "";
		int index = sessionKeys.indexOf(lookup);
		if (index >= 0)
		{
			result = sessionData.get(index);
		}
		if (result.equals("<null>"))
			result = "";

		return result;
	}

	public void deleteData(String sessionID, String key) {

		String lookup = sessionID + "-" + key;
		int index = sessionKeys.indexOf(lookup);
		if (index >= 0)
		{
			sessionKeys.remove(index);
			sessionData.remove(index);
		}

	}

	public void clear() {
		sessionKeys.clear();
		sessionData.clear();
	}

}
