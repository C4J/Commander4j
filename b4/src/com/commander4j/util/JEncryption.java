package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JEncryption.java
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


public class JEncryption
{


	public static String from = "abcdefghijklmnopqrstuvwxyz1234567890";

	public static String to = "mnopqrstuvwxyz1234567890abcdefghijkl";


	public static String encrypt(String s) {
		String result = "";

		if (s.equals(null) == false)
		{
			int temp = s.length();
			for (int p = 0; p < temp; p++)
			{
				char chr = s.charAt(p);
				int i = (int) chr;
				String hexstr = Integer.toString(i, 16);
				result = result + hexstr;
			}
		}

		result = substitute(result, from, to);
		return result;
	}

	public static String decrypt(String s) {
		String result = "";

		try
		{
			s = substitute(s, to, from);

			if (s.equals(null) == false)
			{
				int len = s.length();
				for (int p = 0; p < len; p = p + 2)
				{
					String hexstr = s.substring(p, p + 2);
					int i = Integer.valueOf(hexstr, 16).intValue();
					String chr = new Character((char) i).toString();
					result = result + chr;
				}
			}
		}
		catch (Exception e)
		{
			result = "error";
		}

		return result;
	}

	private static String substitute(String s, String from, String to) {
		String result = "";

		for (int i = 0; i < s.length(); i = i + 1)
		{ // for as many letters
			// as there are
			char ch = s.charAt(i); // access each letter in message
			int index = from.indexOf(ch); // find its position in alphabet
			if (index == -1)
			{ // if it's not a capital letter,
				result = result + ch; // then leave it as is & add
			} // otherwise,
			else
			{ // find the corresponding
				result = result + to.charAt(index); // letter in the key & add
			}
		}
		return result;
	}

}
