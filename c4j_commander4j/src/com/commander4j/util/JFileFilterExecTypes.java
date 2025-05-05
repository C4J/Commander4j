package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JFileFilterExecTypes.java
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

import java.io.File;

/**
 */
public class JFileFilterExecTypes
{
	/**
	 * Field EXE. (value is ""exe"") Value: {@value EXE}
	 */
	public final static String EXE = "exe";
	/**
	 * Field COM. (value is ""com"") Value: {@value COM}
	 */
	public final static String COM = "com";
	/**
	 * Field CMD. (value is ""cmd"") Value: {@value CMD}
	 */
	public final static String CMD = "cmd";
	/**
	 * Field BAT. (value is ""bat"") Value: {@value BAT}
	 */
	public final static String BAT = "bat";
	/**
	 * Field SH. (value is ""sh"") Value: {@value SH}
	 */
	public final static String SH = "sh";
	/**
	 * Field BIN. (value is ""bin"") Value: {@value BIN}
	 */
	public final static String BIN = "bin";

	/*
	 * Get the extension of a file.
	 */
	/**
	 * Method getExtension.
	 * 
	 * @param f
	 *            File
	 * @return String
	 */
	public static String getExtension(File f) {
		String ext = null;
		String filename = f.getName();
		int i = filename.lastIndexOf('.');

		if (i > 0 && i < filename.length() - 1)
		{
			ext = filename.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}
