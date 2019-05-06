package com.commander4j.messageSplitter;


public class Splitter4j
{

	ExtractXml2 xmlSplitterThread;


	public Integer startService()
	{

		xmlSplitterThread = new ExtractXml2();

		xmlSplitterThread.start();

		AddShutdownHookSample sample = new AddShutdownHookSample();
		sample.attachShutDownHook();

		try
		{
			while (xmlSplitterThread.isAlive())
			{
				com.commander4j.util.JWait.milliSec(100);
			}
		}
		catch (Exception ex)
		{
		}

		return 0;
	}
	
	public Integer startGUI()
	{

		xmlSplitterThread = new ExtractXml2();

		xmlSplitterThread.startupInterface();

		return 0;
	}

	public int stop(int exitCode)
	{

		try
		{
			while (xmlSplitterThread.isAlive())
			{
				xmlSplitterThread.requestStop();
				com.commander4j.util.JWait.milliSec(100);
			}
		}
		catch (Exception ex)
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

					xmlSplitterThread.requestStop();
					System.out.println("Waiting for threads to stop.");
					while (xmlSplitterThread.isAlive())
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
		String param = "";
		
		if (args.length == 1)
		{
			param=args[0];
		}
		
		if (param.equals(""))
		{
			param="GUI";
		}
		
		if (param.equals("GUI"))
		{
			Splitter4j int4j = new Splitter4j();
			System.out.println("Starting");
			int4j.startGUI();
			System.out.println("Stopped");
		}
		
		if (param.equals("Service"))
		{
			Splitter4j int4j = new Splitter4j();
			System.out.println("Starting");
			int4j.startService();
			System.out.println("Stopped");
		}
		
	}
	
}
