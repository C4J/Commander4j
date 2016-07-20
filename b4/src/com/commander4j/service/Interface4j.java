package com.commander4j.service;

import com.commander4j.thread.InterfaceThread;

public class Interface4j {
	InterfaceThread interfaceThread;

	public Interface4j()
	{
	}

	public Integer start(String[] args)
	{

		interfaceThread = new InterfaceThread(args);

		interfaceThread.start();

		AddShutdownHookSample sample = new AddShutdownHookSample();
		sample.attachShutDownHook();

		try
		{
			while (interfaceThread.isAlive())
			{
				com.commander4j.util.JWait.milliSec(100);
			}
		} catch (Exception ex)
		{
		}

		return null;
	}

	public int stop(int exitCode)
	{

		try
		{
			while (interfaceThread.isAlive())
			{
				interfaceThread.requestStop();
				com.commander4j.util.JWait.milliSec(100);
			}
		} catch (Exception ex)
		{
		}

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

					interfaceThread.requestStop();
					System.out.println("Waiting for threads to stop.");
					while (interfaceThread.isAlive())
					{
						com.commander4j.util.JWait.milliSec(100);
					}
					System.out.println("All threads have stopped.");
				}
			});
			System.out.println("Shut Down Hook Active.");
		}
	}

	public static void main(String[] args)
	{
		Interface4j int4j = new Interface4j();
		System.out.println("Starting");
		int4j.start(args);
		System.out.println("Stopped");
	}
}
