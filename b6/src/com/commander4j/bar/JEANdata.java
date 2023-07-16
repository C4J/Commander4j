/*
 * Created on 27-May-2005
 *
 */
package com.commander4j.bar;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JEANdata.java
 * 
 * Package Name : com.commander4j.bar
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

public class JEANdata
{
	private String applicationID;

	private String data;

	JEANdata()
	{
		applicationID = "";
		data = "";
	}

	JEANdata(String id, String dat)
	{
		applicationID = id;
		data = dat;
	}

	public String getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(String value) {
		applicationID = value;
	}

	public String getData() {
		return data;
	}

	public void setData(String value) {
		data = value;
	}
}
