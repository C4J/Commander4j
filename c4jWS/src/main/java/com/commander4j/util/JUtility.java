/*
 * Created on 14-Aug-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JUtility.java
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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

public class JUtility
{

	public static int field_timestamp = 20;

	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	
	public static String getDateTimeString(String fmt)
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	public static void errorBeep()
	{
		JPlaySound s = new JPlaySound(System.getProperty("user.dir") + File.separator + "audio" + File.separator + "beep.wav");

		if (s.equals(s))
		{
			s = null;
		}
	}
	
	public static String removePathSeparators(String path)
	{
		String result = path;

		result = StringUtils.replaceChars(result, String.valueOf(":"), "_");
		result = StringUtils.replaceChars(result, String.valueOf("\\"), "_");
		result = StringUtils.replaceChars(result, String.valueOf("/"), "_");

		return result;
	}

	
	public static String yesNoToTrueFalse(String input)
	{
		String result = replaceNullStringwithBlank(input);

		if (result.equals("Y"))
		{
			result = "True";
		}

		if (result.toUpperCase().equals("YES"))
		{
			result = "True";
		}

		if (result.equals("N"))
		{
			result = "False";
		}

		if (result.toUpperCase().equals("NO"))
		{
			result = "False";
		}

		return result;
	}

	public static String diskFree()
	{
		String result = "";
		long free = 0;
		File f = new File(System.getProperty("user.dir"));
		String root = "";

		while (f.getParent() != null)
		{
			root = f.getParent();
			f = new File(root);
		}

		try
		{
			URI rootURI = new URI("file:///");
			Path rootPath = Paths.get(rootURI);
			Path dirPath = rootPath.resolve(root);
			FileStore dirFileStore = Files.getFileStore(dirPath);

			free = dirFileStore.getUsableSpace() / 1048576;
			result = String.valueOf(free) + " mb on " + root;

		}
		catch (Exception e)
		{
			result = "Error trying to determine free disk space " + e.getLocalizedMessage();
		}

		return result;
	}

	public static String getFilenameFromPath(String path)
	{
		String result = "";
		String temp = JUtility.replaceNullStringwithBlank(path);
		int size = temp.length();

		if (size > 0)
		{
			for (int x = size; x > 0; x--)
			{
				if (temp.substring(x - 1, x).equals("\\"))
				{
					break;
				}
				if (temp.substring(x - 1, x).equals("/"))
				{
					break;
				}
				result = temp.substring(x - 1, x) + result;
			}
		}

		return result;
	}

	public static BigDecimal stringToBigDecimal(String str)
	{
		BigDecimal result;

		NumberFormat nf;
		nf = NumberFormat.getNumberInstance(Locale.getDefault());

		try
		{
			Number myNumber = nf.parse(str);
			Double dbl = myNumber.doubleValue();
			result = BigDecimal.valueOf(dbl);
			str = String.valueOf(dbl);
		}
		catch (ParseException e)
		{
			final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JUtility.class);
			logger.error(e.getMessage());
			str = "0";
			result = new BigDecimal("0");
		}

		// result = new BigDecimal(str);
		return result;
	}

	public static String removeChar(String s, char c)
	{
		StringBuffer r = new StringBuffer(s.length());
		r.setLength(s.length());
		int current = 0;
		for (int i = 0; i < s.length(); i++)
		{
			char cur = s.charAt(i);
			if (cur != c)
				r.setCharAt(current++, cur);
		}
		return r.toString();
	}

	public static String bigDecimaltoString(BigDecimal bd)
	{
		String result = "";
		NumberFormat nf1 = NumberFormat.getInstance();

		nf1 = NumberFormat.getInstance();
		nf1.setMinimumFractionDigits(3);
		nf1.setMaximumFractionDigits(3);
		result = nf1.format(bd);

		return result;
	}

	public static boolean isStringPatternValid(String regex, String input)
	{
		boolean result = true;
		String regex2 = JUtility.replaceNullStringwithBlank(regex);
		String input2 = JUtility.replaceNullStringwithBlank(input);

		if ((regex2.equals("") == false) && (input2.equals("") == false))
		{
			Pattern pat = Pattern.compile(regex);

			Matcher mat = pat.matcher(input);

			result = mat.matches();
		}

		return result;
	}

	public static int countOccurrences(String arg1, String arg2)
	{
		int count = 0;
		int index = 0;

		while ((index = arg1.indexOf(arg2, index)) != -1)
		{
			++index;
			++count;
		}

		return count;
	}

	public static String addtoSQL(String sql, String field, String delimiter, String comparator, String value)
	{
		String result = sql;
		boolean first = false;

		if ((field != null) && (value != null))
		{
			if ((field.equals("") == false) && (value.equals("") == false))
			{
				if (sql.equals(""))
				{
					sql = "where ";
					first = true;
				}
				else
				{
					first = false;
				}

				if (first == false)
				{
					sql = sql + " and ";
				}

				sql = sql + field + " " + comparator + " " + delimiter + value + delimiter;
				result = sql;
			}
		}

		return result;
	}

	/**
	 * Method capitaliseAll.
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String capitaliseAll(String str)
	{
		String result = "";
		char ch; // One of the characters in str.
		char prevCh; // The character that comes before ch in the string.
		int i; // A position in str, from 0 to str.length()-1.

		if (str != null)
		{
			str = str.toLowerCase();
			prevCh = '.'; // Prime the loop with any non-letter character.

			for (i = 0; i < str.length(); i++)
			{
				ch = str.charAt(i);

				if (Character.isLetter(ch) && !Character.isLetter(prevCh))
				{
					result = result + Character.toUpperCase(ch);
				}
				else
				{
					result = result + ch;
				}

				prevCh = ch; // prevCh for next iteration is ch.
			}
		}

		return result;
	}

	static String capitalize(String input)
	{
		String str = input.toLowerCase();
		String result = "";
		char ch;
		char prevCh;
		int i;
		prevCh = '.';

		for (i = 0; i < str.length(); i++)
		{
			ch = str.charAt(i);

			if (Character.isLetter(ch) && !Character.isLetter(prevCh))
			{
				result = result + Character.toUpperCase(ch);
			}
			else
			{
				result = result + ch;
			}

			prevCh = ch;
		}

		return result;
	}

	/**
	 * Method differenceInDays.
	 * 
	 * @param date1
	 *            Calendar
	 * @param date2
	 *            Calendar
	 * @return long
	 */
	public static long differenceInDays(Calendar date1, Calendar date2)
	{
		final long msPerDay = 1000 * 60 * 60 * 24;

		final long date1Milliseconds = date1.getTime().getTime();
		final long date2Milliseconds = date2.getTime().getTime();
		final long result = (date1Milliseconds - date2Milliseconds) / msPerDay;

		return result;
	}

	public static String formatNumber(String No, String fmt)
	{
		String result = "error";

		Long SeqNumber = (long) 0;
		int SeqStart = 0;
		int SeqEnd = 0;
		int SeqLength = 0;
		boolean retry = true;

		SeqStart = fmt.indexOf("{");
		SeqEnd = fmt.indexOf("}");
		SeqLength = SeqEnd - SeqStart;
		SeqLength--;

		do
		{
			SeqNumber = Long.parseLong(No);
			SeqNumber++;

			No = JUtility.padString(No, false, SeqLength, "0");

			if (SeqStart > 0)
			{
				No = fmt.substring(0, SeqStart) + No;
			}

			if ((SeqEnd + 1) < fmt.length())
			{
				No = No + fmt.substring(SeqEnd + 1, fmt.length());
			}

			retry = false;
			result = No;
		}
		while (retry);

		return result;
	}

	public static String getBasePath()
	{
		String basepath = "";

		try
		{
			basepath = new File("").getCanonicalPath();
		}
		catch (Exception ex)
		{
			basepath = "error";
		}

		return basepath;
	}

	/**
	 * Method getClientName.
	 * 
	 * @return String
	 */
	public static String getClientName()
	{
		String result = "";
		String clientname = "";

		try
		{
			clientname = System.getenv("Clientname").toString();

			if (clientname.equals("Console"))
			{
				clientname = "unknown";
			}
		}
		catch (Exception e)
		{
			clientname = "unknown";
		}

		if (clientname.equals("unknown"))
		{
			try
			{
				clientname = InetAddress.getLocalHost().getHostName().toLowerCase();
			}
			catch (Exception e)
			{
				clientname = "unknown";
			}
		}

		if (clientname.contains("."))
		{
			String[] bits = clientname.split("\\.");
			clientname=bits[0];
		}
		
		result = left(clientname, 40);

		return result;
	}

	public static String left(String inputstr, int size)
	{
		String result = JUtility.replaceNullStringwithBlank(inputstr);

		if (size > inputstr.length())
		{
			size = inputstr.length();
		}

		if (size >= 0)
		{
			result = inputstr.substring(0, size);
		}
		else
		{
			result = "";
		}

		return result;
	}

	public static String getDefaultValue(String newValue, String oldValue, String defaultValue)
	{
		String result = replaceNullStringwithBlank(newValue);

		if (newValue.length() == 0)
		{
			if (replaceNullStringwithBlank(oldValue).length() > 0)
			{
				result = replaceNullStringwithBlank(oldValue);
			}
			else
			{
				result = replaceNullStringwithBlank(defaultValue);
			}
		}

		return result;
	}


	/**
	 * Method getFormattedQuantity.
	 * 
	 * @param qty
	 *            String
	 * @param len
	 *            int
	 * @param pad
	 *            String
	 * @return String
	 */
	public static String getFormattedQuantity(String qty, int len, String pad)
	{
		String result = replaceNullObjectwithBlank(qty);

		char decimalseparator = java.text.DecimalFormatSymbols.getInstance().getDecimalSeparator();
		char thousandseparator = java.text.DecimalFormatSymbols.getInstance().getGroupingSeparator();

		if (result.equals(""))
		{
			result = "0";
		}

		result = result.trim();

		if (result.length() > 0)
		{
			String temp = "";

			Boolean intbit = false;
			if (result.indexOf(decimalseparator) < 0)
			{
				intbit = true;
			}

			for (int i = result.length() - 1; i >= 0; i--)
			{
				if ((result.charAt(i) == decimalseparator))
				{
					intbit = true;
				}

				if (intbit)
				{
					if ((result.charAt(i) != decimalseparator) & (result.charAt(i) != thousandseparator))
						temp = result.charAt(i) + temp;
				}
			}

			result = temp;
		}

		result = padString(result, false, len, pad);

		return result;
	}

	/**
	 * Method getFormattedSSCC.
	 * 
	 * @param sscc
	 *            String
	 * @return String
	 */
	public static String getFormattedSSCC(String sscc)
	{
		String result = replaceNullObjectwithBlank(sscc);

		if (result.length() == 18)
		{
			result = result.substring(0, 3) + " " + result.substring(3, 8) + " " + result.substring(8, 13) + " " + result.substring(13, 18);
		}

		return result;
	}


	public static String getISOTimeStampStringFormat(Timestamp ts)
	{
		String result = "";

		try
		{
			String temp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts);
			// String temp = ts.toString(); 0123456789012345678
			result = temp.substring(0, 4);
			result = result + "-";
			result = result + temp.substring(5, 7);
			result = result + "-";
			result = result + temp.substring(8, 10);
			result = result + "T";
			result = result + temp.substring(11, 13);
			result = result + ":";
			result = result + temp.substring(14, 16);
			result = result + ":";
			result = result + temp.substring(17, 19);
		}
		catch (Exception ex)
		{
			result = "Error";
		}

		return result;
	}

	public static String getISODateStringFormat(Date ts)
	{
		String result = "";

		try
		{
			String temp = new java.text.SimpleDateFormat("yyyy-MM-dd").format(ts);
			// String temp = ts.toString(); 0123456789012345678
			result = temp.substring(0, 4);
			result = result + "-";
			result = result + temp.substring(5, 7);
			result = result + "-";
			result = result + temp.substring(8, 10);

		}
		catch (Exception ex)
		{
			result = "Error";
		}

		return result;
	}

	/**
	 * Method getJulianDay.
	 * 
	 * @param currentDate
	 *            Calendar
	 * @return long
	 */
	public static long getJulianDay(Calendar currentDate)
	{
		long result = 0;

		result = currentDate.get(Calendar.DAY_OF_YEAR);

		return result;
	}




	/**
	 * Method getDayOfWeek.
	 * 
	 * @param currentDate
	 *            Calendar
	 * @return int
	 */
	public static int getDayOfWeek(Calendar currentDate)
	{
		int result = 0;

		result = currentDate.get(Calendar.DAY_OF_WEEK);

		return result;
	}

	/**
	 * Method getDayOfWeekString1.
	 * 
	 * @param currentDate
	 *            Calendar
	 * @return int
	 */
	public static String getDayOfWeekString1(Calendar currentDate)
	{
		int temp = getDayOfWeek(currentDate);
		String result = "";

		switch (temp)
		{
		case Calendar.MONDAY:
			result = "A";
			break;
		case Calendar.TUESDAY:
			result = "B";
			break;
		case Calendar.WEDNESDAY:
			result = "C";
			break;
		case Calendar.THURSDAY:
			result = "D";
			break;
		case Calendar.FRIDAY:
			result = "E";
			break;
		case Calendar.SATURDAY:
			result = "F";
			break;
		case Calendar.SUNDAY:
			result = "G";
			break;
		}

		return result;
	}

	/**
	 * Method getSQLDate.
	 * 
	 * @return java.sql.Date
	 */
	public static java.sql.Date getSQLDate()
	{
		Calendar caldate = Calendar.getInstance();
		int day = caldate.get(Calendar.DATE);
		int month = caldate.get(Calendar.MONTH);
		int year = caldate.get(Calendar.YEAR);
		java.sql.Date sqldate = getSQLDate(year, month, day);

		return sqldate;
	}

	/**
	 * Method getSQLDate.
	 * 
	 * @param caldate
	 *            Calendar
	 * @return java.sql.Date
	 */
	public static java.sql.Date getSQLDate(Calendar caldate)
	{
		java.sql.Date sqldate = new java.sql.Date(caldate.getTimeInMillis());

		return sqldate;
	}

	/**
	 * Method getSQLDate.
	 * 
	 * @param yyyy
	 *            int
	 * @param mm
	 *            int
	 * @param dd
	 *            int
	 * @return java.sql.Date
	 */
	public static java.sql.Date getSQLDate(int yyyy, int mm, int dd)
	{
		Calendar caldate = Calendar.getInstance();
		caldate.set(yyyy, mm, dd, 0, 0, 0);

		java.sql.Date sqldate = new java.sql.Date(caldate.getTimeInMillis());

		return sqldate;
	}

	/**
	 * Method getSQLDateTime.
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getSQLDateTime()
	{
		Calendar caldate = Calendar.getInstance();
		Timestamp t = new Timestamp(caldate.getTimeInMillis());
		t.setTime(caldate.getTimeInMillis());
		t.setNanos(0);
		return t;
	}

	/**
	 * Method getTimestampFromDate.
	 * 
	 * @param d
	 *            Date
	 * @return Timestamp
	 */
	public static Timestamp getTimestampFromDate(Date d)
	{
		Calendar caldate = Calendar.getInstance();
		caldate.setTime(d);

		Timestamp t = new Timestamp(caldate.getTimeInMillis());

		t.setTime(caldate.getTimeInMillis());
		t.setNanos(0);
		
		return t;
	}

	public static Timestamp getTimeStampFromISOString(String isoString)
	{
		Timestamp result;

		// 2010-05-31T10:14:49
		// 0123456789111111111
		// 012345678
		try
		{
			Calendar caldate = Calendar.getInstance();

			int year = 0;
			int month = 0;
			int day = 0;
			int hour = 0;
			int min = 0;
			int second = 0;

			year = Integer.valueOf(isoString.substring(0, 4));
			month = Integer.valueOf(isoString.substring(5, 7));
			day = Integer.valueOf(isoString.substring(8, 10));
			hour = Integer.valueOf(isoString.substring(11, 13));
			min = Integer.valueOf(isoString.substring(14, 16));
			second = Integer.valueOf(isoString.substring(17, 19));

			if ((month < 1) | (month > 12))
			{
				throw new Exception("Invalid month " + String.valueOf(month));
			}

			if ((day < 1) | (day > 31))
			{
				throw new Exception("Invalid day " + String.valueOf(day));
			}

			caldate.set(year, month - 1, day, hour, min, second);

			result = new Timestamp(caldate.getTimeInMillis());

			result.setNanos(0); // or other value
		}
		catch (Exception ex)
		{
			result = null;
		}

		return result;
	}

	public static String getTimeStampStringFormat(Timestamp ts, String fmt)
	{
		String result = "";
		LinkedList<String> fmtList = new LinkedList<String>();
		LinkedList<String> valList = new LinkedList<String>();
		fmtList.clear();
		valList.clear();

		result = ts.toString();

		fmtList.add("yyyy");
		valList.add(result.substring(0, 4));

		fmtList.add("yy");
		valList.add(result.substring(2, 4));

		fmtList.add("mm");
		valList.add(result.substring(5, 7));

		fmtList.add("dd");
		valList.add(result.substring(8, 10));

		fmtList.add("hh");
		valList.add(result.substring(11, 13));

		fmtList.add("mi");
		valList.add(result.substring(14, 16));

		fmtList.add("ss");
		valList.add(result.substring(17, 19));

		fmtList.add("yymmdd");
		valList.add(result.substring(2, 4) + result.substring(5, 7) + result.substring(8, 10));

		int pos = fmtList.indexOf(fmt);

		if (pos >= 0)
		{
			result = valList.get(pos);
		}
		else
		{
			result = "";
		}

		return result;
	}

	public static String formatPath(String path)
	{
		String result = path;

		result = result.replace("\\", File.separator);
		result = result.replace("/", File.separator);

		return result;
	}

	/**
	 * Method initLogging.
	 * 
	 * @param filename
	 *            String
	 */
	public static void initLogging(String filename)
	{
		if (filename.isEmpty())
		{
			filename = "xml" + File.separator + "log" + File.separator + "log4j2.xml";
		}

		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		File file = new File(filename);

		// this will force a reconfiguration
		context.setConfigLocation(file.toURI());

	}

	/**
	 * Method isNullORBlank.
	 * 
	 * @param value
	 *            String
	 * @return boolean
	 */
	public static boolean isNullORBlank(String value)
	{
		boolean result = false;

		if (value == null)
		{
			result = true;
		}
		else
		{
			if (value.equals("") == true)
			{
				result = true;
			}
		}

		return result;
	}

	/**
	 * Method isValidJavaVersion.
	 * 
	 * @param minVersion
	 *            String
	 * @return boolean
	 */
	public static boolean isValidJavaVersion(String minVersion)
	{
		boolean result = false;

		String current = System.getProperty("java.version");

		int comp = current.compareTo(minVersion);

		if (comp < 0)
		{
			result = false;
		}
		else
		{
			result = true;
		}

		return result;
	}

	/**
	 * Method loadListFromTextFile.
	 * 
	 * @param filename
	 *            String
	 * @param defaultitem
	 *            String
	 * @param linePrefix
	 *            String
	 * @param lineSuffix
	 *            String
	 * @return String
	 */
	public synchronized static String loadListFromTextFile(String filename, String defaultitem, String linePrefix, String lineSuffix)
	{
		String result = "";

		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String str;

			if (defaultitem != null)
			{
				result = result + linePrefix + defaultitem + lineSuffix;
			}

			while ((str = in.readLine()) != null)
			{
				if (str.equalsIgnoreCase(defaultitem) == false)
				{
					result = result + linePrefix + str + lineSuffix;
				}
			}

			in.close();
		}
		catch (IOException e)
		{
		}

		return result;
	}

	public static String now()
	{
		final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

		return sdf.format(cal.getTime());
	}

	/**
	 * Method padSpace.
	 * 
	 * @param size
	 *            int
	 * @return String
	 */
	public static String padSpace(int size)
	{
		String s = "";

		for (int i = 0; i < size; i++)
		{
			s = s + " ";
		}

		return s;
	}

	/**
	 * Method padString.
	 * 
	 * @param size
	 *            int
	 * @param character
	 *            String
	 * @return String
	 */
	public static String padString(int size, String character)
	{
		String s = "";

		for (int i = 0; i < size; i++)
		{
			s = s + character;
		}

		return s;
	}

	/**
	 * Method padString.
	 * 
	 * @param input
	 *            String
	 * @param right
	 *            boolean
	 * @param size
	 *            int
	 * @param character
	 *            String
	 * @return String
	 */
	public static String padString(String input, boolean right, int size, String character)
	{
		int inputlength = 0;
		String result = replaceNullStringwithBlank(input);

		inputlength = result.length();

		if (inputlength > size)
		{
			// result = result.substring(0,size-1);
			result = result.substring(0, size);
		}
		else
		{
			if (inputlength < size)
			{
				if (right == true)
				{
					result = result + padString(size - inputlength, character);
				}
				else
				{
					result = padString(size - inputlength, character) + result;
				}
			}
		}

		return result;
	}



	/**
	 * Method removeExtensionFromFilename.
	 * 
	 * @param filename
	 *            String
	 * @param extension
	 *            String
	 * @return String
	 */
	public static String removeExtensionFromFilename(String filename, String extension)
	{
		String result = "";

		if (filename == null)
		{
			filename = "";
		}

		if (extension == null)
		{
			extension = "";
		}

		result = filename;

		if (filename.indexOf(extension) > 0)
		{
			if (filename.length() > extension.length())
			{
				int s1 = filename.length() - extension.length();

				if (s1 > 0)
				{
					result = filename.substring(0, s1);
				}
			}
		}

		return result;
	}

	/**
	 * Method removeTimefromDate.
	 * 
	 * @param inputDate
	 *            Date
	 * @return java.sql.Date
	 */
	public static java.sql.Date removeTimefromDate(Date inputDate)
	{
		Calendar caldate = Calendar.getInstance();
		caldate.setTime(inputDate);

		int day = caldate.get(Calendar.DATE);
		int month = caldate.get(Calendar.MONTH);
		int year = caldate.get(Calendar.YEAR);
		java.sql.Date sqldate = getSQLDate(year, month, day);

		return sqldate;
	}

	/**
	 * Method replaceNullObjectwithBlank.
	 * 
	 * @param value
	 *            Object
	 * @return String
	 */
	public static String replaceNullObjectwithBlank(Object value)
	{
		String result = "";

		if (value != null)
		{
			result = value.toString();
		}

		return result;
	}

	public static String replaceNullStringwithBlank(String value)
	{
		if (value == null)
		{
			value = "";
		}

		return value;
	}


	public static void setLookandFeel()
	{

		try
		{
			SetLookAndFeel("Metal", "Ocean");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void SetLookAndFeel(String LOOKANDFEEL, String THEME)
	{
		try
		{
			if (LOOKANDFEEL.equals("Metal"))
			{
				if (THEME.equals("DefaultMetal"))
					MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
				else if (THEME.equals("Ocean"))
					MetalLookAndFeel.setCurrentTheme(new OceanTheme());

				UIManager.setLookAndFeel(new MetalLookAndFeel());

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void setResultRecordCountColour(JLabel label, boolean limitSet, Integer limitRecords, Integer ActualRecords)
	{
		String warning = "";

		if (ActualRecords > 0)
		{
			if (limitSet)
			{
				if (ActualRecords >= limitRecords)
				{
					label.setForeground(Color.RED);
					warning = " Number of records returned constrained by user defined limit.";
				}
				else
				{
					label.setForeground(Color.BLACK);
				}
			}
			else
			{
				label.setForeground(Color.BLACK);
			}

			label.setText(String.valueOf(ActualRecords) + " record(s) retrieved." + warning);
		}
		else
		{
			label.setForeground(Color.BLACK);
			label.setText("0 records shown.");
		}
	}

	/**
	 * Method sqlSelectNull.
	 * 
	 * @param value
	 *            String
	 * @return String
	 */
	public static String sqlSelectNull(String value)
	{
		if (value.equals(""))
		{
			return "is null";
		}
		else
		{
			return " = '" + value + "'";
		}
	}

	public static String substSchemaName(String schemaName, String sql)
	{
		String result = "";

		result = sql.replace("{schema}", schemaName);

		return result;
	}

	public synchronized static void writeToTextFile(String filename, String text)
	{
		BufferedWriter bw = null;

		try
		{
			bw = new BufferedWriter(new FileWriter(filename, true));
			bw.write(text);
			bw.newLine();
			bw.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{ // always close the file

			if (bw != null)
			{
				try
				{
					bw.close();
				}
				catch (IOException ioe2)
				{
				}
			}
		}
	}
}
