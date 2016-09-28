package com.commander4j.mw;

import com.commander4j.sys.Common;
import com.commander4j.util.JWait;

public class StartService
{

	// StartMW smw= new StartMW();
	public Boolean shutdown = false;

	public StartService()
	{
	}

	public Integer start(String[] args)
	{
		Integer result = 0;
		AddShutdownHookSample sample = new AddShutdownHookSample();
		sample.attachShutDownHook();

		Common.sgui = new StartGUI();
		Common.sgui.setVisible(false);

		Common.smw.StartMiddleware();

		while (shutdown.equals(false))
		{
			if (Common.smw.isRunning())
			{
				System.out.println("shutdown =" + String.valueOf(shutdown));
				if (Common.smw.cfg.getMapDirectoryErrorCount() > 0)
				{
					System.out.println("Aborting due to directories not found.");
					result = 1;
				} else
				{
					System.out.println("isRunning() =" + String.valueOf(Common.smw.isRunning()));
					while (Common.smw.isRunning())
					{
						JWait.oneSec();
					}
					result = 0;
				}
			}

			if (shutdown.equals(false))
			{
				JWait.oneSec();
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
					System.out.println("Shutdown detected - requesting threads stop.");

					System.out.println("Waiting for threads to stop.");
					Common.smw.StopMiddleware();
					System.out.println("All threads have stopped.");
				}
			});
			System.out.println("Shut Down Hook Active.");
		}
	}
}
