package com.commander4j.bar;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JEANUtility.java
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

import java.util.Formatter;

import com.commander4j.util.JUtility;

public class JEANUtility
{

	public static String escapeUnicode(String input)
	{
		StringBuilder b = new StringBuilder(input.length());
		Formatter f = new Formatter(b);
		for (char c : input.toCharArray())
		{
			if (c < 128)
			{
				b.append(c);
			} else
			{
				f.format("%02X", (int) c);
			}
		}
		f.close();
		return b.toString();
	}
	
	public static String calcCheckDigit(int baseLen,String value)
	{
		String result = "";
		int checkdigit = 0;
		String data = JUtility.replaceNullStringwithBlank(value);
		int datalength = data.length();

		if (datalength >= baseLen)
		{
			int odd = 0;
			int x = 0;
			while ((x + 1) <= baseLen)
			{
				odd = odd + Integer.valueOf(data.substring(x, x + 1));
				x = x + 2;
			}
			odd = odd * 3;
			int even = 0;
			x = 1;
			while ((x + 1) <= baseLen)
			{
				even = even + Integer.valueOf(data.substring(x, x + 1));
				x = x + 2;
			}
			int total = odd + even;
			checkdigit = 10 - (total % 10);
			if (checkdigit == 10)
			{
				checkdigit = 0;

			}
		}
		result = String.valueOf(checkdigit);
		return result;
	}

	public static boolean isValidCheckDigit(int baseLen,String value)
	{
		Boolean result = true;
		String data = JUtility.replaceNullStringwithBlank(value);
		int datalength = data.length();

		if (datalength > 3)
		{
			String currentCheckDigit = value.substring(datalength - 1, datalength);
			String calculatedCheckDigit = calcCheckDigit(baseLen,value.substring(0, datalength ));
			if (calculatedCheckDigit.equals(currentCheckDigit))
			{
				result = true;
			}
			else
			{
				result = false;
			}
		}

		return result;
	}

}
