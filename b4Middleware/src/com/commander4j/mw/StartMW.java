package com.commander4j.mw;

import java.io.File;

import org.apache.log4j.Logger;

import com.commander4j.gui.StartGUI;
import com.commander4j.util.Utility;

public class StartMW {

	
	Logger logger = Logger.getLogger(StartMW.class);
	Config cfg;
	
	
	public Boolean StartMiddleware()
	{
		Boolean result = true;
		
		logger.debug("Application starting");
		Utility.initLogging("");
		
		logger.debug("*************************");
		logger.debug("**      START          **");
		logger.debug("*************************");
		
		cfg = new Config();
		
		cfg.loadMaps(System.getProperty("user.dir")+File.separator+"xml"+File.separator+"config"+File.separator+"config.xml");
		
		
		cfg.startMaps();
		
		return result;
	}
	
	
	public Boolean StopMiddleware()
	{
		Boolean result = true;
		
		cfg.stopMaps();
		

		logger.debug("*************************");
		logger.debug("**      COMPLETED      **");
		logger.debug("*************************");
		
		return result;
	}
	
	
	public static void main(String[] args)
	{
		
		String parameter = "";
		
		if (args.length>0)
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
			StartMW smw = new StartMW();

			smw.StartMiddleware();
		}

	}
}
