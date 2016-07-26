package com.commander4j.mw;

import com.commander4j.util.JWait;

public class StartService {

	StartMW smw= new StartMW();

	public StartService()
	{
	}

	public Integer start(String[] args)
	{

		AddShutdownHookSample sample = new AddShutdownHookSample();
		sample.attachShutDownHook();

		smw.StartMiddleware();

		return null;
	}

	public int stop(int exitCode)
	{

		smw.StopMiddleware();
		return exitCode;
	}

	public class AddShutdownHookSample {
		public void attachShutDownHook()
		{
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run()
				{
					System.out.println("Shutdown detected - requesting threads stop.");

					System.out.println("Waiting for threads to stop.");
					smw.StopMiddleware();
					System.out.println("All threads have stopped.");
				}
			});
			System.out.println("Shut Down Hook Active.");
		}
	}

	public static void main(String[] args)
	{
		System.out.println("Starting");
		StartService ss = new StartService();
		
		ss.smw.StartMiddleware();
		
		while (ss.smw.isRunning())
		{
			JWait.oneSec();
		}
		
		System.out.println("Stopped");
	}
}
