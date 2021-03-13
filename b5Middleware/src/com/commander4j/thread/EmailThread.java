package com.commander4j.thread;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JWait;

public class EmailThread extends Thread
{
	public boolean allDone = false;
	Integer counter = 0;

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((EmailThread.class));

	public EmailThread()
	{
		super();

	}

	public void run()
	{
		logger.debug("EmailThread started.");
		while (true)
		{
			
			JWait.oneSec();

			Common.emailqueue.processQueue();

			if (allDone)
			{
				Common.emailqueue.processQueue();
				logger.debug("EmailThread closed.");
				return;
			}

		}
	}
}
