package com.commander4j.util;

import java.io.File;
import java.io.StringWriter;
import java.sql.Timestamp;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;

public class Utility {

	public static synchronized String getStringFromDocument(Document doc)  {
		
		try{
	    DOMSource domSource = new DOMSource(doc);
	    StringWriter writer = new StringWriter();
	    StreamResult result = new StreamResult(writer);
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.transform(domSource, result);
	    return writer.toString();
		}
		catch (Exception ex)
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
	
	public static  void pause(int value)
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
			filename = "." + File.separator + "xml" + File.separator + "config" + File.separator + "log4j.xml";
		}

		DOMConfigurator.configure(filename);

	}
	
	public static String getLogFilename(String filename)
	{
		return System.getProperty("user.dir")+File.separator+"interface"+File.separator+"log"+File.separator+filename;
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
	
}
