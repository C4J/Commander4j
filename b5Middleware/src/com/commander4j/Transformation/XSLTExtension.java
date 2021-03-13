package com.commander4j.Transformation;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.commander4j.util.Utility;

public class XSLTExtension
{
	public static Timestamp getSQLDateTime()
	{
		Calendar caldate = Calendar.getInstance();
		Timestamp t = new Timestamp(caldate.getTimeInMillis());
		t.setTime(caldate.getTimeInMillis());
		return t;
	}
	public static String getISODateTimeString()
	{
		String result = "";
		Timestamp ts = getSQLDateTime();
		try
		{
			String temp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK).format(ts);
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

	public static String getISODateTimeFilenameString()
	{
		String result = getISODateTimeString();
		result = result.replace(":", "_");
		result = result.replace("-", "_");
		result = result.replace(" ", "_");
		result = result.replace("T", "_");

		return result;
	}

	public static String removeLeadingZeros(String param)
	{
		String result = StringUtils.stripStart(param, "0");
		return result;
	}

	public static String padStringLeft(String value, int size, String character)
	{
		String s = Utility.replaceNullStringwithBlank(value);

		while (s.length() < size)
		{
			s = character + s;
		}

		return s;
	}

	public static String padStringRight(String value, int size, String character)
	{
		String s = Utility.replaceNullStringwithBlank(value);

		while (s.length() < size)
		{
			s = s + character;
		}

		return s;
	}

	public static synchronized String trim(String value)
	{
		String result = Utility.replaceNullStringwithBlank(value);

		result = result.trim();

		return result;
	}

	public static synchronized String nvl(String value, String defaultValue)
	{
		String result = "";

		if (value == null)
		{
			value = "";
		}

		result = value;

		if (result.equals(""))
		{
			result = defaultValue;
		}

		return result;
	}

	public static synchronized String padVariant(String value)
	{
		String result = Utility.replaceNullStringwithBlank(value);

		if (result.equals("") == false)
		{

			result = Utility.padString(value, false, 2, "0");
		}

		return result;
	}

	public static synchronized String padEAN(String value)
	{
		String result = Utility.replaceNullStringwithBlank(value);

		if (result.equals("") == false)
		{

			result = Utility.padString(value, false, 14, "0");
		}

		return result;
	}

	public static synchronized String date_DD_MMM_YY_to_ISO_Date(String inputString)
	{
		String result = inputString;

		SimpleDateFormat fromUser = new SimpleDateFormat("dd-MMM-yy", Locale.UK);
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

		try
		{

			result = myFormat.format(fromUser.parse(inputString));
			result = result + "T00:00:00";

		}
		catch (ParseException e)
		{

			result = "Input date DD-MMM-YY format error [" + inputString + "]";

		}

		return result;
	}

	public static synchronized String date_DDMMYYYY_to_ISO_Date(String inputString)
	{
		String result = inputString;

		SimpleDateFormat fromUser = new SimpleDateFormat("ddMMyyyy", Locale.UK);
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

		try
		{

			result = myFormat.format(fromUser.parse(inputString));
			result = result + "T00:00:00";

		}
		catch (ParseException e)
		{

			result = "Input date DD-MMM-YY format error [" + inputString + "]";

		}

		return result;
	}

	public static synchronized String date_DD_MM_YY_HH_MM_SS_to_ISO_Date(String inputString)
	{
		String result = inputString;

		SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.UK);
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

		try
		{

			result = myFormat.format(fromUser.parse(inputString));

		}
		catch (ParseException e)
		{

			result = "Input date DD-MM-YY HH:mm:ss format error [" + inputString + "]";

		}

		return result;
	}

	public static synchronized String ISO_Date_to_date_DD_MM_YYYY_HH_MM_SS(String inputString)
	{
		String result = inputString.replace("T", " ");

		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
		SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.UK);

		try
		{

			result = myFormat.format(fromUser.parse(result));

		}
		catch (ParseException e)
		{

			result = "Input date yyyy-MM-dd HH:mm:ss format error [" + result + "]";

		}

		return result;
	}

	public static synchronized String ISO_Date_to_date_MMYYYY(String inputString)
	{
		String result = inputString.replace("T", " ");

		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
		SimpleDateFormat myFormat = new SimpleDateFormat("MMyyyy", Locale.UK);

		try
		{

			result = myFormat.format(fromUser.parse(result));

		}
		catch (ParseException e)
		{

			result = "Input date yyyy-MM-dd HH:mm:ss format error [" + result + "]";

		}

		return result;
	}

	public static synchronized String ISO_Date_to_date_DDMMYYYY(String inputString)
	{
		String result = inputString.replace("T", " ");

		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
		SimpleDateFormat myFormat = new SimpleDateFormat("ddMMyyyy", Locale.UK);

		try
		{

			result = myFormat.format(fromUser.parse(result));

		}
		catch (ParseException e)
		{

			result = "Input date yyyy-MM-dd HH:mm:ss format error [" + result + "]";

		}

		return result;
	}

	public static synchronized String formatDate(String inputString, String formatFrom, String formatTo)
	{
		String result = inputString;

		SimpleDateFormat fromUser = new SimpleDateFormat(formatFrom);
		SimpleDateFormat myFormat = new SimpleDateFormat(formatTo);

		try
		{

			result = myFormat.format(fromUser.parse(inputString));

		}
		catch (Exception e)
		{

			result = e.getMessage() + "[" + inputString + "],[" + formatFrom + "],[" + formatTo + "]";

		}

		return result;
	}

	public static synchronized String removeCommas(String inputString)
	{
		String result = inputString;

		result = result.replace(",", " ");

		return result;
	}

	public static synchronized String removeSpaces(String inputString)
	{
		String result = inputString;

		result = result.replace(" ", "");

		return result;
	}

	public static synchronized String subString(String inputString, int startpos, int length)
	{
		String result = inputString;

		result = result.substring(startpos, startpos + length);

		return result;
	}

	public static synchronized String concat(String inputString1, String inputString2)
	{
		String result = Utility.replaceNullStringwithBlank(inputString1) + Utility.replaceNullStringwithBlank(inputString2);

		return result;
	}

}
