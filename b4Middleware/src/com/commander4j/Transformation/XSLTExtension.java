package com.commander4j.Transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.commander4j.util.Utility;

public class XSLTExtension
{
	public static synchronized String nvl(String value,String defaultValue)
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

		
		if (result.equals("")==false)
		{
			
			result = Utility.padString(value, false, 2, "0");
		}

		return result;
	}
		
	public static synchronized String padEAN(String value)
	{
		String result = Utility.replaceNullStringwithBlank(value);

		
		if (result.equals("")==false)
		{
			
			result = Utility.padString(value, false, 14, "0");
		}

		return result;
	}
	
	public static  synchronized String date_DD_MMM_YY_to_ISO_Date(String inputString)
	{
		String result = inputString;
		
		SimpleDateFormat fromUser = new SimpleDateFormat("dd-MMM-yy");
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {

		    result = myFormat.format(fromUser.parse(inputString));
		    result = result + "T00:00:00";
		    
		} catch (ParseException e) {
			
			result = "Input date DD-MMM-YYY format error ["+inputString+"]";
			
		}
		
		return result;
	}
	
}
