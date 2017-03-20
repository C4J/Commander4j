package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JUnique.java
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

public class JUnique
{

	/**
	 * Field NUM_CHARS. (value is 26) Value: {@value NUM_CHARS}
	 */
	private static final int NUM_CHARS = 26;
	/**
	 * Field lastString. Value: {@value lastString}
	 */
	private static int[] lastString = new int[NUM_CHARS];
	/**
	 * Field CHARS. (value is
	 * ""0123456789abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"")
	 * Value: {@value CHARS}
	 */
	private static final String CHARS = "0123456789abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * Method getUnique.
	 * 
	 * @return String
	 */
	public static String getUniqueID() {
		return java.util.UUID.randomUUID().toString();
	}

	static String getUnique() {

		char[] buf = new char[NUM_CHARS];

		carry(lastString, buf.length - 1);
		for (int i = 0; i < buf.length; i++)
		{
			buf[i] = CHARS.charAt(lastString[i]);
		}
		return new String(buf);
	}

	/**
	 * Method carry.
	 * 
	 * @param ca
	 *            int[]
	 * @param index
	 *            int
	 */
	private static void carry(int[] ca, int index) {
		if (ca[index] == (CHARS.length() - 1))
		{
			ca[index] = 0;
			carry(ca, --index);
		}
		else
		{
			ca[index] = ca[index] + 1;
		}
	}

}
