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
 * Filename     : JEANdef.java
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

public class JEANdef
{
	private String applicationID;

	private String description;

	private String dataType;

	private String decimalIndicator;

	private int maxLength;

	private String fixedVariable;

	private String checkDigit;

	public String toString() {
		return this.applicationID;
	}

	JEANdef(String appid, String desc, String type, String decInd, int maxlen, String fixedvar, String cd)
	{
		applicationID = appid;
		description = desc;
		dataType = type;
		decimalIndicator = decInd;
		maxLength = maxlen;
		fixedVariable = fixedvar;
		checkDigit = cd;
	}


	public String getApplicationID() {
		return applicationID;
	}


	public void setApplicationID(String value) {
		applicationID = value;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String value) {
		description = value;
	}


	public String getDataType() {
		return dataType;
	}


	public void setDataType(String value) {
		dataType = value;
	}


	public String getDecimalIndicator() {
		return decimalIndicator;
	}


	public void setDecimalIndicator(String value) {
		decimalIndicator = value;
	}


	public int getMaxLength() {
		return maxLength;
	}


	public void setMaxLength(int value) {
		maxLength = value;
	}


	public String getFixedVariable() {
		return fixedVariable;
	}


	public void setFixedVariable(String value) {
		fixedVariable = value;
	}


	public String getCheckDigit() {
		return checkDigit;
	}


	public void setCheckDigit(String value) {
		checkDigit = value;
	}

}
