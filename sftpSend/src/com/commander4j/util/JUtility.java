package com.commander4j.util;

import java.io.File;
import java.net.InetAddress;

public class JUtility
{

	public  String replaceNullObjectwithBlank(Object value)
	{
		String result = "";

		if (value != null)
		{
			result = value.toString();
		}

		return result;
	}

	public  String replaceNullStringwithBlank(String value)
	{
		if (value == null)
		{
			value = "";
		}

		return value;
	}
	
	public  String replaceNullStringwithDefault(String value,String newval)
	{
		if (value == null)
		{
			value = newval;
		}

		return value;
	}
	
	public  String formatPath(String path)
	{
		String result = path;

		result = result.replace("\\", File.separator);
		result = result.replace("/", File.separator);

		return result;
	}
	
	public String formatPathTerminator(String path)
	{
		String result = "";
		
		result = replaceNullStringwithBlank(path);
		
		result = formatPath(result);
		
		if (result.length()>0)
		{
			if (result.endsWith(File.separator)==false)
			{
				result = result + File.separator;
			}
		}
		
		
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
			clientname = bits[0];
		}

		result = left(clientname, 40);

		return result;
	}
	
	public String left(String inputstr, int size)
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

	public String getFilenameFromPath(String path)
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

}
