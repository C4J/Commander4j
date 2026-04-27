package com.commander4j.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.HttpServletRequest;

public final class JURL
{
	private static final Logger logger = LogManager.getLogger(JURL.class);

	private JURL() {}

	public static String getParameter(HttpServletRequest request, String name)
	{
		String value = JUtility.replaceNullStringwithBlank(request.getParameter(name));
		logger.debug("Returning var {" + name + "} with value [" + value + "]");
		return value;
	}
}
