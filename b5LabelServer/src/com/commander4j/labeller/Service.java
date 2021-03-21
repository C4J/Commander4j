package com.commander4j.labeller;

import org.apache.logging.log4j.Logger;

import com.commander4j.util.JUtility;

public class Service
{

	public Server server;
	LabellerUtility utils = new LabellerUtility();
	public Boolean shutdown = false;
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((Service.class));

	public Service()
	{

	}

	public Integer start(String[] args)
	{

		AddShutdownHookSample shutdownHook = new AddShutdownHookSample();
		shutdownHook.attachShutDownHook();

		logger.info("Service - start");

		server = new Server();

		server.start();

		while (shutdown.equals(false))
		{
			utils.pause(1000);
		}

		stop(0);

		return 0;
	}

	public int stop(int exitCode)
	{
		logger.info("Service - stop");

		try
		{
			server.stop(0);
			utils.pause(100);

		} catch (Exception ex)
		{
		}

		return exitCode;
	}

	public class AddShutdownHookSample
	{
		public void attachShutDownHook()
		{
			Runtime.getRuntime().addShutdownHook(new Thread()
			{
				@Override
				public void run()
				{
					logger.info("Shutdown detected - requesting threads stop.");

					server.stop(0);
					
					logger.info("Waiting for threads to stop.");
					
					while (server.isAlive())
					{
						utils.pause(100);
					}
					
					logger.info("Service - stop");
				}
			});
			
			logger.info("Shut Down Hook Active.");
		}
	}

	public static void main(String[] args)
	{
		
		LabellerUtility utils = new LabellerUtility();
		
		JUtility.initLogging("");
		Logger logger = org.apache.logging.log4j.LogManager.getLogger((Service.class));
		
		logger.info("Starting");

		Server server = new Server();
		server.start();

		while (server.started==false)
		{
			utils.pause(100);

		}

		
		while (server.isAlive())
		{
			utils.pause(100);
		}
		
		server.stop(0);
		
		System.exit(0);
	}
}
