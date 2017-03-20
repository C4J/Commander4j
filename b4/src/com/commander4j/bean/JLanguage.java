package com.commander4j.bean;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JLanguage.java
 * 
 * Package Name : com.commander4j.bean
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

import org.apache.log4j.Logger;

import com.commander4j.db.JDBLanguage;

public class JLanguage
{
	private String hostID;
	private String sessionID;
	private String languageID;
	private JDBLanguage lang;
	private Boolean initialised=false;
	private Logger logger = Logger.getLogger(JLanguage.class);
	
	public String getHostID()
	{
		return hostID;
	}

	public String getLanguageID()
	{
		return languageID;
	}
	
	public String getSessionID()
	{
		return sessionID;
	}
	
	public String getText(String id,String Lang)
	{
		String result="";
		
		setLanguageID(Lang);
		
		result = lang.get(id);
		return result;
	}
	
	public String getText(String id)
	{
		String result="";
		
		if (initialised==false)
		{
			init();
		}
		
		result = lang.get(id, getLanguageID());
		logger.debug("getText ["+id+"] ("+getLanguageID()+")="+result);
		return result;
	}
	
	public void preload(String mask)
	{
		if (initialised==false)
		{
			init();
		}
		lang.preLoad(mask);
	}
	
	public void init()
	{
		lang = new JDBLanguage(getHostID(),getSessionID());
	}
	
	public void setHostID(String hostID)
	{
		logger.debug("hostID="+hostID);
		this.hostID = hostID;
	}
	public void setLanguageID(String languageID)
	{
		logger.debug("languageID="+languageID);
		this.languageID = languageID;
	}

	public void setSessionID(String sessionID)
	{
		logger.debug("sessionID="+sessionID);
		this.sessionID = sessionID;
	}
	
}
