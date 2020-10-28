package com.commander4j.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.Logger;
import com.commander4j.autolab.AutoLab;
import com.commander4j.xml.JXMLDocument;
import com.install4j.api.launcher.ApplicationLauncher;

public class JUpdate
{
	public  String updatePath = "";
	public  String updateMode = "";
	public  Logger logger = org.apache.logging.log4j.LogManager.getLogger((AutoLab.class));
	private JXMLDocument xmlDoc;

	public  void updateCheck()
	{
		if (new File(System.getProperty("user.dir") + java.io.File.separator + ".install4j" + java.io.File.separator + "i4jparams.conf").isFile())
		{
			
			xmlDoc = new JXMLDocument(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config" + File.separator + "config.xml");
			
			updatePath = xmlDoc.findXPath("/config/updateURL[1]");
			logger.debug("updatePath = ["+updatePath+"]");
			updateMode = xmlDoc.findXPath("/config/updateMode[1]");
			logger.debug("updateMode = ["+updateMode+"]");
			
			xmlDoc = null;

			if (updatePath.equals("") == false)
			{

				
				String paramsAll[] =
				{ "-VC4JUpdaterUrl=" + updatePath, "-VC4JUpdateMode=" + updateMode, "-VC4JUpdateDirectory=" + System.getProperty("user.dir") };
				
				logger.debug("Setting params = ["+Arrays.toString(paramsAll)+"]");

				try
				{

					logger.debug("launchApplication");
					
					ApplicationLauncher.launchApplication("831", paramsAll, true, new ApplicationLauncher.Callback()
					{
						public void exited(int exitValue)
						{
							logger.debug("exited");
						}

						public void prepareShutdown()
						{
							logger.debug("prepareShutdown");
						}

					});
				}
				catch (IOException e)
				{
					logger.debug(e.getMessage());
				}

			}

		}
		else
		{
			logger.debug("Update check suppressed when running in IDE");
		}
	}
}
