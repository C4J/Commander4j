package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBDDL.java
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

public class JDBDDL
{

	private int version = -1;

	private int sequence = -1;

	private String text = "";

	private String error = "";
	
	private int errorCount = 0;

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public JDBDDL()
	{

	}


	public JDBDDL(int ver, int seq, String desc, String txt, String err)
	{
		version = ver;
		sequence = seq;
		// description = desc;
		text = txt;
		error = err;
	}


	public int getVersion() {
		return version;
	}


	public int getSequence() {
		return sequence;
	}


	public String getText() {
		return text;

	}


	public String getError() {
		return error;

	}


	public void setVersion(int value) {
		version = value;
	}


	public void setSequence(int value) {
		sequence = value;
	}


	public void setText(String value) {
		text = value;

	}


	public void setError(String value) {
		error = value;

	}

}
