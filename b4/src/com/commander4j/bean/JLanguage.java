package com.commander4j.bean;

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
