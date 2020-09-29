package com.commander4j.resources;

import java.util.Locale;
import java.util.ResourceBundle;

public class JRes
{
	private static Locale locale = Locale.getDefault();
	private static ResourceBundle language = ResourceBundle.getBundle("com.commander4j.resources.b4AutoLab", locale);
	
	
	
	public static String getText(String textID)
	{
		String result = "";
		try
		{
			result = language.getString(textID);

		}
		catch (Exception ex)
		{
			result = "["+textID+"]";
		}
		
		return result;
	}
	
}
