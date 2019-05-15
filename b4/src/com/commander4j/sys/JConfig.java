package com.commander4j.sys;

import java.io.File;
import java.util.Locale;

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
				Common.locale_language = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath("//config/appLanguage").trim());
				Common.locale_region = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath("//config/appRegion").trim());
				if ((Common.locale_language.equals("")==false) && (Common.locale_region.equals("")==false))
				{
					Locale.setDefault(new Locale(Common.locale_language, Common.locale_region));
				}
			}
		}
		catch (Exception ex)
		{
			result = false;
		}
		return result;
	}
	
}
