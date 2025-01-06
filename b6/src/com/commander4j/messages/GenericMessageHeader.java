package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : GenericMessageHeader.java
 * 
 * Package Name : com.commander4j.messages
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

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Document;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

/**
 * All Commander4j xml messages have a standard format. The header section
 * describes the content (data) component of the message.
 *
 */
public class GenericMessageHeader
{
	private String hostRef = "";
	private String hostID = "";
	private String messageRef = "";
	private String messageInformation = "";
	private String messageDate = "";
	private String interfaceType = "";
	private String interfaceDirection = "";
	private Boolean validHostRef = false;
	private Boolean dbconnected = true;
	private String xmlfilename = "";
	private JXMLDocument xmlMessage;
	public static String msgStatusSuccess = "Success";
	public static String msgStatusWarning = "Warning";
	public static String msgStatusError = "Error";

	public static ConcurrentHashMap<String, Integer> interfaceStats = new ConcurrentHashMap<String, Integer>();

	public static synchronized void updateStats(String direction, String type, String result)
	{
		String key = com.commander4j.util.JUtility.padString(direction, true, 10, " ");
		key = key + com.commander4j.util.JUtility.padString(type, true, 45, " ");
		key = key + com.commander4j.util.JUtility.padString(result, true, 10, " ");

		if (interfaceStats.containsKey(key) == false)
		{
			interfaceStats.put(key, 0);
		}

		int counter = interfaceStats.get(key);
		counter++;

		interfaceStats.replace(key, counter);
	}

	public static synchronized void clearStats()
	{
		interfaceStats.clear();
	}

	public static synchronized String getStats()
	{
		String results = "";

		Iterator<String> iterator = interfaceStats.keySet().iterator();
		String key = "";
		int counter = 0;

		results = "<br>\n" + 
		"<div id=\"garbage\" >\n" + 
		"<table border=\"3\">\n" + 
		"<thead>\n" + 
		"<caption>Interface Statistics</caption>\n" + 
		"<tr>\n" + 
		"<th>Interface</th>\n" + 
		"<th>Status</th>\n" + 
		"<th>Count</th>\n" + 
		"</tr>\n" + 
		"</thead>\n" + 
		"<tbody>\n";

		String status = "";
		String description = "";
		
		while (iterator.hasNext())
		{
			key = iterator.next().toString();
			description = key;
			
			if (description.contains(Boolean.TRUE.toString()))
			{
				status= "Success";
				description = description.replace(Boolean.TRUE.toString(), "");
			}
			
			if (description.contains(Boolean.FALSE.toString()))
			{
				status= "Fail";
				description = description.replace(Boolean.FALSE.toString(), "");
			}
			
			//description = description.replace("  ", " ");
			
			results = results + 
					"<tr>\n" + 
					"<td>" + description + "</td>\n" +
					"<td>" + status + "</td>\n" +
					"<td style=\"width:20%; text-align: right\">" + interfaceStats.get(key).toString() + "</td>\n" +
					"</tr>\n";
			counter++;
		}

		if (counter == 0)
			results = results + "<tr>\n" + "<td>No Messages Processed</td><td></td><td></td></tr>\n";

		results = results + "</tbody>\n" + "</table> \n";

		return results;
	}

	public void decodeHeader(JXMLDocument xmltest)
	{
		dbconnected = false;

		setHostRef(xmltest.findXPath("//message/hostRef"));
		setMessageRef(xmltest.findXPath("//message/messageRef"));
		setMessageInformation(xmltest.findXPath("//message/messageInformation"));
		setInterfaceType(xmltest.findXPath("//message/interfaceType").trim());
		setMessageDate(xmltest.findXPath("//message/messageDate").trim());
		setInterfaceDirection(xmltest.findXPath("//message/interfaceDirection").trim());
		setHostID(Common.hostList.getHostIDforUniqueId(getHostRef()));

	}

	public String getHostID()
	{
		return hostID;
	}

	public Document getDocument()
	{
		return xmlMessage.getDocument();
	}

	public String getInterfaceDirection()
	{
		return interfaceDirection;
	}

	public String getInterfaceType()
	{
		return interfaceType;
	}

	public String getMessageDate()
	{
		return messageDate;
	}

	public Timestamp getMessageDateTimeStamp()
	{
		return JUtility.getTimeStampFromISOString(getMessageDate());
	}

	public String getMessageInformation()
	{
		return messageInformation;
	}

	public String getMessageRef()
	{
		return messageRef;
	}

	public JXMLDocument getXMLDocument()
	{
		return xmlMessage;
	}

	public Boolean isConnected()
	{
		return dbconnected;
	}

	public Boolean isValidHostRef()
	{
		return validHostRef;
	}

	public void setFilename(String filename)
	{
		xmlfilename = filename;
	}

	public String getFilename()
	{
		return xmlfilename;
	}

	public Boolean readAddressInfo(String filename, String sessionID)
	{
		Boolean result;
		try
		{
			xmlMessage = new JXMLDocument();
			result = xmlMessage.setDocument(filename);
			if (result)
			{
				decodeHeader(xmlMessage);
				setFilename(filename);
			}
		}
		catch (Exception ex)
		{
			result = false;
		}
		return result;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private void setHostRef(String hRef)
	{
		hostRef = hRef;
	}

	private String getHostRef()
	{
		return hostRef;
	}

	public void setInterfaceDirection(String direction)
	{
		interfaceDirection = direction;
	}

	public void setInterfaceType(String type)
	{
		interfaceType = type;
	}

	public void setMessageDate(String mDate)
	{
		messageDate = mDate;
	}

	public void setMessageInformation(String messageInfo)
	{
		messageInformation = messageInfo;
	}

	public void setMessageRef(String mRef)
	{
		messageRef = mRef;
	}

}
