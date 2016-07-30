package com.commander4j.mw;

import java.io.File;

import org.apache.log4j.Logger;

import com.commander4j.sys.Config;
import com.commander4j.util.Utility;

public class StartMain
{

	private Logger logger = Logger.getLogger(StartMain.class);
	public Config cfg;
	public static String version = "1.03";
	Boolean running = false;

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

		if (cfg.getMapDirectoryErrorCount() == 0)
		{

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
	
		cfg.stopMaps();
	
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
			StartGUI.main(args);
		}

		if (parameter.equals("Service"))
		{
			StartService.main(args);
		}

	}
}
