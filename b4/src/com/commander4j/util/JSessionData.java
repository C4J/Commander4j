package com.commander4j.util;

import java.util.LinkedList;

//import org.apache.log4j.Logger;

public class JSessionData
{
	private LinkedList<String> sessionKeys = new LinkedList<String>();
	private LinkedList<String> sessionData = new LinkedList<String>();

	// private Logger logger = Logger.getLogger(JSessionData.class);

	public Boolean setData(String sessionID, String key, String value, boolean allowBlank) {
		Boolean updated = false;
		String lookup = sessionID + "-" + key;
		String data = JUtility.replaceNullObjectwithBlank(value);
		if (data.isEmpty() == true)
			data = "<null>";

		if ((allowBlank == true) | (data.equals("<null>") == false))
		{

			int index = sessionKeys.indexOf(lookup);
			if (index >= 0)
			{
				// logger.debug("JSessionData - setData (update) sessionID=>" +
				// sessionID + "< key = <" + key + "< value = >" + data + "<");
				sessionData.set(index, data);
			}
			else
			{
				sessionKeys.addLast(lookup);
				sessionData.addLast(data);
				// logger.debug("JSessionData - setData (add) sessionID=>" +
				// sessionID + "< key = <" + key + "< value = >" + data + "<");
			}
			updated = true;
		}
		return updated;

	}

	public String getData(String sessionID, String key) {

		String lookup = sessionID + "-" + key;
		String result = "";
		int index = sessionKeys.indexOf(lookup);
		if (index >= 0)
		{
			result = sessionData.get(index);
		}
		if (result.equals("<null>"))
			result = "";
		// logger.debug("JSessionData - getData sessionID=>" + sessionID +
		// "< key = <" + key + "< result = >" + result + "<");
		return result;
	}

	public void deleteData(String sessionID, String key) {

		String lookup = sessionID + "-" + key;
		int index = sessionKeys.indexOf(lookup);
		if (index >= 0)
		{
			sessionKeys.remove(index);
			sessionData.remove(index);
		}
		// logger.debug("JSessionData - getData sessionID=>" + sessionID +
		// "< key = <" + key + "<");
	}

	public void clear() {
		sessionKeys.clear();
		sessionData.clear();
	}

}
