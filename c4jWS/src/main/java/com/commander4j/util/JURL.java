package com.commander4j.util;

import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.HttpServletRequest;

public class JURL
{

	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JURL.class);
	private HashMap<String, String> paramMap;

	public JURL(HttpServletRequest request)
	{
		super();
		paramMap = getParameters(request);

	}

	public HashMap<String, String> getParameters(HttpServletRequest request)
	{
		HashMap<String, String> resultMap = new HashMap<String, String>();
		String queryString = JUtility.replaceNullStringwithBlank(request.getQueryString());
		
		logger.debug("Reading request parameters for session "+request.getSession().getId());

		String[] varArray = queryString.split("[&]");

		for (int x = 0; x < varArray.length; x++)
		{
			String expression = varArray[x];
			String[] parts = expression.split("[=]");

			if (parts.length == 2)
			{
				String var = parts[0];
				var = var.replace("%20", " ");
				String val = parts[1];
				val = val.replace("%20", " ");
				resultMap.put(var, val);
				logger.debug("Found var [" + var + "] with value [" + val + "]");
			}
		}

		return resultMap;
	}

	public String getParameterVariable(HttpServletRequest request, String variable)
	{
		String result = "";

		if (paramMap == null)
		{
			paramMap = getParameters(request);
		}

		result = JUtility.replaceNullStringwithBlank(paramMap.get(variable));

		logger.debug("Returning var {"+variable +"} with value [" + result+"]");

		return result;
	}
	
	public Long getParameterVariableLong(HttpServletRequest request, String variable)
	{
		Long result = (long) -1;

		if (paramMap == null)
		{
			paramMap = getParameters(request);
		}

		String temp = JUtility.replaceNullStringwithBlank(paramMap.get(variable));
		
		try
		{
			result = Long.valueOf(temp);
		}
		catch (Exception ex)
		{
			result = (long) -1;
		}

		logger.debug("Returning var {"+variable +"} with value [" + result+"]");

		return result;
	}
	
	public String getPathInfoValue(HttpServletRequest request)
	{
		String result = "";
		
		result = JUtility.replaceNullStringwithBlank(request.getPathInfo());
		result = result.replace("/", "");
		
		return result;
	}

}
