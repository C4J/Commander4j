package com.commander4j.mw;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.sys.Config;
import com.commander4j.thread.LogArchiveThread;
import com.commander4j.util.Utility;

public class StartMain
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((StartMain.class));
	public Config cfg;
	public static String version = "1.05";
	Boolean running = false;
	LogArchiveThread archiveLog;

	public Boolean isRunning()
	{
		return running;
	}

	public Boolean StartMiddleware()
	{
		Boolean result = true;

		logger.debug("Application starting");
		Utility.initLogging("");

		logger.debug("*************************");
		logger.debug("**     STARTING        **");
		logger.debug("*************************");

		cfg = new Config();

		cfg.loadMaps(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config"
				+ File.separator + "config.xml");

		logger.debug("*************************");
		logger.debug("**     MAPS LOADED     **");
		logger.debug("*************************");

		if ((cfg.getMapDirectoryErrorCount() == 0) || (Common.runMode.equals("Service")))
		{

			archiveLog = new LogArchiveThread();
			archiveLog.setName("Log Archiver");
			archiveLog.start();

			cfg.startMaps();

			logger.debug("*************************");
			logger.debug("**      STARTED        **");
			logger.debug("*************************");

			running = true;

		} else
		{
			logger.debug("*************************");
			logger.debug("**      ERRORS         **");
			logger.debug("*************************");

			for (int x = 0; x < cfg.getMapDirectoryErrorCount(); x++)
			{
				logger.error(cfg.getMapDirectoryErrors().get(x));
			}

			result = false;
		}

		return result;
	}

	public Boolean StopMiddleware()
	{
		Boolean result = true;

		logger.debug("*************************");
		logger.debug("**      STOPPING       **");
		logger.debug("*************************");

		try
		{
			logger.debug("Shutting down Log File Archiver");
			while (archiveLog.isAlive())
			{
				archiveLog.allDone = true;
				com.commander4j.util.JWait.milliSec(100);
			}
			logger.debug("Log File Archiver terminated");
		} catch (Exception ex1)
		{

		}

		logger.debug("Shutting down Maps");
		cfg.stopMaps();
		logger.debug("Maps Terminated");

		logger.info(cfg.getInterfaceStatistics());

		logger.debug("*************************");
		logger.debug("**      STOPPED        **");
		logger.debug("*************************");

		running = false;

		return result;
	}

	public static void main(String[] args)
	{

		String parameter = "";

		if (args.length > 0)
		{
			parameter = args[0];
		}

		if (parameter.equals(""))
		{
			parameter = "GUI";
		}

		if (parameter.equals("GUI"))
		{
			Common.runMode = "GUI";
			StartGUI.main(args);
		}

		if (parameter.equals("Service"))
		{
			Common.runMode = "Service";
			StartService.main(args);
		}

	}
}
