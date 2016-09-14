package com.commander4j.thread;

import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

public class LogArchiveThread extends Thread
{
	public boolean allDone = false;

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((LogArchiveThread.class));
	public LogArchiveThread(LinkedList<String> fromPathList, String toPath)
	{
		super();

	}

	public void run()
	{
		while (true)
		{
			try
			{
				sleep(1000);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}

			if (allDone)
			{
				logger.debug("LogArchiveThread closed.");
				return;
			}

	
		}
	}
}
