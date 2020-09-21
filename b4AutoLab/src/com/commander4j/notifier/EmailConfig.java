package com.commander4j.notifier;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.commander4j.autolab.AutoLab;
import com.commander4j.utils.JUtility;

public class EmailConfig
{
	public static Logger logger = org.apache.logging.log4j.LogManager.getLogger((EmailConfig.class));
	
	public void sendConfigEmail()
	{
		JUtility utils = new JUtility();
		String pathToConfig = System.getProperty("user.dir")+File.separator+"xml"+File.separator+"config"+File.separator+"config.xml";
		logger.debug("File to email ["+pathToConfig+"]");
		AutoLab.emailqueue.addToQueue("Config", "AutoLab config.xml from [" + utils.getClientName()+"]", "AutoLab version " + AutoLab.version+"\n\n", pathToConfig);
		
	}
}
