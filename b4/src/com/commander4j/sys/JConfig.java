package com.commander4j.sys;

import java.io.File;

import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

public class JConfig
{
	private static JXMLDocument xmlMessage;
	
	public static void loadConfig(String filename)
	{
		getConfigFile(filename);
	}
	
	private static boolean getConfigFile(String filename)
	{
		boolean result = false;
		
		try
		{
			xmlMessage = new JXMLDocument();
			result = xmlMessage.setDocument("." + File.separator + "xml" + File.separator + "config" + File.separator + filename);
			if (result)
			{
				Common.appDisplayName = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath("//config/appDisplayName").trim());
				Common.appWebsite = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath("//config/appWebsite").trim());
				Common.appAuthor = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath("//config/appAuthor").trim());
				Common.appSupportEmail = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath("//config/appSupportEmail").trim());
				Common.image_splash = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath("//config/appSplashScreenImage").trim());
			}
		}
		catch (Exception ex)
		{
			result = false;
		}
		return result;
	}
	
}
