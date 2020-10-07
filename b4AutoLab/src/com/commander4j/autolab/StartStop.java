package com.commander4j.autolab;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.core.util.DefaultShutdownCallbackRegistry;
import org.apache.logging.log4j.spi.LoggerContextFactory;

import com.commander4j.utils.JUtility;
import com.commander4j.utils.JWait;

public class StartStop
{

	public static JWait wait = new JWait();
	public static JUtility utils = new JUtility();
	public static AutoLab autolab;
	public static boolean run = true;
	public static Logger logger = org.apache.logging.log4j.LogManager.getLogger((AutoLab.class));
	public static LoggerContextFactory factory = LogManager.getFactory();

	public static void main(String[] args)
	{
		initLogging("");

		utils.setLookandFeel();

		ShutdownHook shutdownHook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		wait = new JWait();
		autolab = new AutoLab();

		autolab.start();

		int counter = 0;

		while (autolab.isAlive())
		{
			wait.milliSec(1000);
			counter++;

			if (args.length > 0)
			{
				if (args[0].toUpperCase().contentEquals("DEBUG"))
				{
					if (counter == 15)
					{
						System.exit(0);
					}
				}
				else
				{
					counter = 0;
				}
			}
			else
			{
				counter = 0;
			}

		}

		System.exit(0);
	}

	public static void initLogging(String filename)
	{
		if (filename.isEmpty())
		{
			filename = System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config" + File.separator + "log4j2.xml";
		}

		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		File file = new File(filename);

		context.setConfigLocation(file.toURI());

		if (factory instanceof Log4jContextFactory)
		{
			// LOG.info("register shutdown hook");
			Log4jContextFactory contextFactory = (Log4jContextFactory) factory;

			((DefaultShutdownCallbackRegistry) contextFactory.getShutdownCallbackRegistry()).stop();
		}

	}

}
