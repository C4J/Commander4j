package com.commander4j.bar;

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
