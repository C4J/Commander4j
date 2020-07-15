
package com.commander4j.utils;

import java.io.File;
import org.apache.logging.log4j.Logger;

import com.commander4j.autolab.AutoLab;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

public class JUtility
{
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((JUtility.class));
	
	public  String getFilenameFromPath(String path)
	{
		String result = "";
		String temp = replaceNullStringwithBlank(path);
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
	
	public  long compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime)
	{
	  long milliseconds1 = oldTime.getTime();
	  long milliseconds2 = currentTime.getTime();

	  long diff = milliseconds2 - milliseconds1;
	  //long diffSeconds = diff / 1000;
	  long diffMinutes = diff / (60 * 1000);
	  //long diffHours = diff / (60 * 60 * 1000);
	  //long diffDays = diff / (24 * 60 * 60 * 1000);

	   return diffMinutes;
	}

	public String replaceNullObjectwithBlank(Object value)
	{
		String result = "";

		if (value != null)
		{
			result = value.toString();
		}

		return result;
	}

	public String replaceNullStringwithBlank(String value)
	{
		if (value == null)
		{
			value = "";
		}

		return value;
	}

	public boolean copyNewerFile(String name, File src, File dst)
	{
		boolean result = false;
		try
		{
			if (dst.exists())
			{
				if (FileUtils.isFileNewer(src, dst))
				{
					logger.debug(name + " Copying updated file " + dst.getName());
					FileUtils.copyFile(src, dst, true);
					result = true;
				}

				if (FileUtils.isFileOlder(src, dst))
				{
					logger.debug(name + " Restoring older file " + dst.getName());
					FileUtils.copyFile(src, dst, true);
					result = true;
				}
			}
			else
			{
				logger.debug(name + " Copying new File " + dst.getName());
				FileUtils.copyFile(src, dst, true);
				result = true;
			}
		}
		catch (IOException e)
		{
			logger.debug(name + " Error Copying  File " + e.getLocalizedMessage());
			result = false;

		}
		return result;
	}

	public String padString(int size, String character)
	{
		String s = "";

		for (int i = 0; i < size; i++)
		{
			s = s + character;
		}

		return s;
	}

	public String padString(String input, boolean right, int size, String character)
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

	public String getISOTimeStampStringFormat(Timestamp ts)
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

	public String getISODateStringFormat(Date ts)
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
	public long getJulianDay(Calendar currentDate)
	{
		long result = 0;

		result = currentDate.get(Calendar.DAY_OF_YEAR);

		return result;
	}

	/**
	 * Method getWeekOfYear.
	 * 
	 * @param currentDate
	 *            Calendar
	 * @return int
	 */
	public String getWeekOfYear(Calendar currentDate)
	{
		int temp = 0;
		String result = "";

		temp = currentDate.get(Calendar.WEEK_OF_YEAR);
		result = String.valueOf(temp).trim();
		result = padString(result, false, 2, "0");

		return result;
	}

	/**
	 * Method getDayOfWeek.
	 * 
	 * @param currentDate
	 *            Calendar
	 * @return int
	 */
	public int getDayOfWeek(Calendar currentDate)
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
	public String getDayOfWeekString1(Calendar currentDate)
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
	public java.sql.Date getSQLDate()
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
	public java.sql.Date getSQLDate(Calendar caldate)
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
	public java.sql.Date getSQLDate(int yyyy, int mm, int dd)
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
	public Timestamp getSQLDateTime()
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
	public Timestamp getTimestampFromDate(Date d)
	{
		Calendar caldate = Calendar.getInstance();
		caldate.setTime(d);

		Timestamp t = new Timestamp(caldate.getTimeInMillis());

		t.setTime(caldate.getTimeInMillis());
		t.setNanos(0);

		return t;
	}

	public Timestamp getTimeStampFromISOString(String isoString)
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

	public String getTimeStampStringFormat(Timestamp ts, String fmt)
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

	public String padSpace(int size)
	{
		String s = "";

		for (int i = 0; i < size; i++)
		{
			s = s + " ";
		}

		return s;
	}

	public Timestamp convertStringToTimestamp(String format, String strDate)
	{
		try
		{
			// "dd MMM yyyy HH:mm:ss"
			DateFormat formatter = new SimpleDateFormat(format);

			Date date = formatter.parse(strDate);
			Timestamp timeStampDate = new Timestamp(date.getTime());

			return timeStampDate;
		}
		catch (ParseException e)
		{
			System.out.println("Exception :" + e);
			return null;
		}
	}
	
	public Calendar convertStringToCalendar(String format, String strDate)
	{
		try
		{

			DateFormat formatter = new SimpleDateFormat(format);

			Calendar cal  = Calendar.getInstance();
			cal.setTime(formatter.parse(strDate));

			return cal;
		}
		catch (ParseException e)
		{
			System.out.println("Exception :" + e);
			return null;
		}
	}
	
	public String getFormattedCSVCalendarString(Calendar ts)
	{
		String result = "";

		try
		{
			result = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(ts.getTimeInMillis());

		}
		catch (Exception ex)
		{
			result = "Error";
		}

		return result;
	}
	
	public String getFormattedISOCalendarString(Calendar ts)
	{
		String result = "";
		String temp = "";
		try
		{
			temp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts.getTimeInMillis());
			
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


	public String formatNumber(String No, String fmt)
	{
		String result = "error";

		Double dbl = (double) 0;
		
		dbl=Double.valueOf(No);
		
		DecimalFormat df = new DecimalFormat(fmt); 
		
		result = df.format(dbl); 
		
		return result;
	}
	
	public Calendar getRoundedExpiryDate(Calendar caldate,String rule)
	{
		String mode = AutoLab.config.getSystemKey("BATCH EXPIRY TIME");
		
		Calendar result = caldate;

		if (rule.equals("-"))
		{
			caldate.set(Calendar.DAY_OF_MONTH, 1);
		}

		if (rule.equals("+"))
		{
			caldate.set(Calendar.DAY_OF_MONTH, 1);
			caldate.add(Calendar.MONTH, 1);
			caldate.add(Calendar.DAY_OF_YEAR, -1);
		}
		
		if (mode.equals("-"))
		{
			caldate.set(Calendar.HOUR_OF_DAY, 0);
			caldate.set(Calendar.MINUTE, 0);
			caldate.set(Calendar.SECOND, 0);
			caldate.set(Calendar.MILLISECOND, 0);
		}

		if (mode.equals("+"))
		{
			caldate.set(Calendar.HOUR_OF_DAY, 23);
			caldate.set(Calendar.MINUTE, 59);
			caldate.set(Calendar.SECOND, 59);
			caldate.set(Calendar.MILLISECOND, 0);
		}

		return result;
	}
	
	public Calendar calcBBE(Calendar dateOfManufacture, Integer shelfLife, String shelfLifeUom, String shelfLifeRule)
	{
		Calendar result  = dateOfManufacture;

		if (shelfLifeUom.equals("H"))
		{
			result.add(Calendar.HOUR_OF_DAY, shelfLife);
		}

		if (shelfLifeUom.equals("D"))
		{
			result.add(Calendar.DAY_OF_YEAR, shelfLife);
		}

		if (shelfLifeUom.equals("W"))
		{
			result.add(Calendar.DAY_OF_YEAR, (7 * shelfLife));
		}

		if (shelfLifeUom.equals("M"))
		{
			result.add(Calendar.MONTH, shelfLife);
		}

		if (shelfLifeUom.equals("Y"))
		{
			result.add(Calendar.YEAR, shelfLife);
		}

		result = getRoundedExpiryDate(result,shelfLifeRule);

		return result;
	}
	
	public String getClientName()
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

	public  String left(String inputstr, int size)
	{
		String result = replaceNullStringwithBlank(inputstr);

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
	
}
