package com.commander4j.notifier;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.commander4j.autolab.AutoLab;
import com.commander4j.utils.JUtility;

public class EmailLogs
{
	public static Logger logger = org.apache.logging.log4j.LogManager.getLogger((EmailLogs.class));

	public void sendLogEmail()
	{
		JUtility utils = new JUtility();
		
		String pathToLog = System.getProperty("user.dir") + File.separator + "logs" + File.separator + "AutoLab.log";
		String pathToZip = System.getProperty("user.dir") + File.separator + "logs" + File.separator + "AutoLab.zip";

		if (utils.zipFile(pathToLog, pathToZip))
		{

			logger.debug("File to email [" + pathToZip + "]");

			AutoLab.emailqueue.addToQueue("Config", "AutoLab Log (zip) from [" + utils.getClientName() + "]", "AutoLab version " + AutoLab.version + "\n\n", pathToZip);
		}

	}
}
