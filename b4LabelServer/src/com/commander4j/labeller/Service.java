package com.commander4j.labeller;

public class Service
{

	public Server server;
	LabellerUtility utils = new LabellerUtility();
	public Boolean shutdown = false;

	public Service()
	{

	}

	public Integer start(String[] args)
	{

		AddShutdownHookSample shutdownHook = new AddShutdownHookSample();
		shutdownHook.attachShutDownHook();

		System.out.println("Service - start");

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
		System.out.println("Service - stop");
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
					System.out.println("Shutdown detected - requesting threads stop.");

					server.stop(0);
					System.out.println("Waiting for threads to stop.");
					while (server.isAlive())
					{
						utils.pause(100);
					}
					System.out.println("All threads have stopped.");
				}
			});
			System.out.println("Shut Down Hook Active.");
		}
	}

	public static void main(String[] args)
	{
		
		LabellerUtility utils = new LabellerUtility();
		System.out.println("Starting");

		Server server = new Server();
		server.start();

		while (server.started==false)
		{
			utils.pause(100);
			System.out.print(".");
		}
		System.out.println(".");
		
		while (server.isAlive())
		{
			utils.pause(100);
		}
		
		server.stop(0);
		
		System.exit(0);
	}
}
