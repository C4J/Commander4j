package com.commander4j.mw;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JWait;

public class StartService
{

	public Boolean shutdown = false;
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((StartService.class));

	public StartService()
	{
	}

	public Integer start(String[] args)
	{
		Integer result = 0;
		AddShutdownHookSample sample = new AddShutdownHookSample();
		sample.attachShutDownHook();

		if (Common.smw.StartMiddleware())
		{

			while (shutdown.equals(false))
			{
				while (Common.smw.isRunning())
				{
					JWait.oneSec();
				}

			}
		}

		return result;
	}

	public static void main(String[] args)
	{
		Common.sservice = new StartService();
		Common.sservice.start(args);
		System.exit(0);
	}

	public int stop(int exitCode)
	{

		shutdown = true;
		Common.smw.StopMiddleware();
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

					logger.info("Waiting for threads to stop.");
					
					Common.smw.StopMiddleware();
					
					logger.info("All threads have stopped.");;
				}
			});
			
			logger.info("Shut Down Hook Active.");

		}
	}
}
