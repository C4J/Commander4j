package com.commander4j.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

		if (queryString.isEmpty() == false)
		{
			String[] varArray = queryString.split("[&]");

			for (int x = 0; x < varArray.length; x++)
			{
				String expression = varArray[x];
				// Split on the first '=' only so values that themselves contain
				// '=' (e.g. base64) are preserved.
				int eq = expression.indexOf('=');

				if (eq > 0)
				{
					String var = urlDecode(expression.substring(0, eq));
					String val = urlDecode(expression.substring(eq + 1));
					resultMap.put(var, val);
					logger.debug("Found var [" + var + "] with value [" + val + "]");
				}
			}
		}

		return resultMap;
	}

	private String urlDecode(String value)
	{
		try
		{
			return URLDecoder.decode(value, StandardCharsets.UTF_8);
		}
		catch (Exception e)
		{
			// Malformed escape sequence - fall back to the raw value.
			return value;
		}
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

	public int getParameterVariableInt(HttpServletRequest request, String variable)
	{
		int result =  -1;

		if (paramMap == null)
		{
			paramMap = getParameters(request);
		}

		String temp = JUtility.replaceNullStringwithBlank(paramMap.get(variable));

		try
		{
			result = Integer.valueOf(temp);
		}
		catch (Exception ex)
		{
			result = -1;
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
