package com.commander4j.util;

import java.io.File;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.w3c.dom.Document;

public class Utility
{

	public static String getCurrentTimeStampString()
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
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

	public static synchronized String getStringFromDocument(Document doc)
	{

		try
		{
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (Exception ex)
		{
			return " Error displaying document(data)";
		}
	}

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

	public static void pause(int value)
	{
		for (int x = 0; x < value; x++)
		{
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{

				e.printStackTrace();
			}
		}
	}

	public static void initLogging(String filename)
	{
		if (filename.isEmpty())
		{
			filename = "xml" + File.separator + "config" + File.separator + "log4j2.xml";
		}

		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		File file = new File(filename);

		// this will force a reconfiguration
		context.setConfigLocation(file.toURI());

	}

	public static String getLogFilename(String filename)
	{
		return System.getProperty("user.dir") + File.separator + "interface" + File.separator + "log" + File.separator + filename;
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
		} catch (Exception ex)
		{
			result = "Error";
		}

		return result;
	}

	public static String padSpace(int size)
	{
		String s = "";

		for (int i = 0; i < size; i++)
		{
			s = s + " ";
		}

		return s;
	}

	public static String padString(int size, String character)
	{
		String s = "";

		for (int i = 0; i < size; i++)
		{
			s = s + character;
		}

		return s;
	}

	public static String padString(String input, boolean right, int size, String character)
	{
		int inputlength = 0;
		String result = replaceNullStringwithBlank(input);

		inputlength = result.length();

		if (inputlength > size)
		{
			// result = result.substring(0,size-1);
			result = result.substring(0, size);
		} else
		{
			if (inputlength < size)
			{
				if (right == true)
				{
					result = result + padString(size - inputlength, character);
				} else
				{
					result = padString(size - inputlength, character) + result;
				}
			}
		}

		return result;
	}

}
