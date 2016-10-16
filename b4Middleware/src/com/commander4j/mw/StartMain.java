package com.commander4j.mw;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.sys.MiddlewareConfig;
import com.commander4j.thread.EmailThread;
import com.commander4j.thread.LogArchiveThread;
import com.commander4j.util.Utility;

public class StartMain
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((StartMain.class));
	public MiddlewareConfig cfg;
	public static String version = "1.10";
	Boolean running = false;
	LogArchiveThread archiveLog;
	EmailThread emailthread;

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
		

		cfg = new MiddlewareConfig();

		cfg.loadMaps(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config" + File.separator + "config.xml");

		logger.debug("*************************");
		logger.debug("**     MAPS LOADED     **");
		logger.debug("*************************");

		if ((cfg.getMapDirectoryErrorCount() == 0) || (Common.runMode.equals("Service")))
		{

			archiveLog = new LogArchiveThread();
			archiveLog.setName("Log Archiver");
			archiveLog.start();
			
			emailthread = new EmailThread();
			emailthread.setName("Email Thread");
			emailthread.start();			

			cfg.startMaps();

			logger.debug("*************************");
			logger.debug("**      STARTED        **");
			logger.debug("*************************");
			
			Common.emailqueue.addToQueue("System", "Starting "+Common.configName, "Program started", "");
			running = true;

		} else
		{
			logger.debug("*************************");
			logger.debug("**      ERRORS         **");
			logger.debug("*************************");

			String errorMsg="";
			for (int x = 0; x < cfg.getMapDirectoryErrorCount(); x++)
			{
				logger.error(cfg.getMapDirectoryErrors().get(x));
				errorMsg=errorMsg+cfg.getMapDirectoryErrors().get(x)+"\n";
			}
			
			Common.emailqueue.addToQueue("System", "Error Starting "+Common.configName, "Errors :-\n\n"+errorMsg, "");
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
		
		
		Common.emailqueue.addToQueue("System", "Stopping "+Common.configName, "Program stopped", "");
		
		
		try
		{
			Common.emailqueue.processQueue();
			logger.debug("Shutting Email Thread");
			while (emailthread.isAlive())
			{
				emailthread.allDone = true;
				com.commander4j.util.JWait.milliSec(100);
			}
			logger.debug("Emailer terminated");
		} catch (Exception ex1)
		{

		}
		
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