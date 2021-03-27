package com.commander4j.email;

import org.apache.logging.log4j.Logger;


import com.commander4j.autolab.AutoLab;
import com.commander4j.utils.JWait;


public class EmailThread extends Thread
{
	Integer counter = 0;
	JWait wait = new JWait();
	boolean run=true;

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((EmailThread.class));

	public EmailThread()
	{
		super();

	}
	
	public void shutdown()
	{
		// logger.debug(getName() + " Thread Shutdown Requested.");

		run = false;
	}


	public void run()
	{
		logger.debug("EmailThread started.");
		while (run)
		{
			
			wait.oneSec();

			AutoLab.emailqueue.processQueue();

		}
		logger.debug("EmailThread stopped.");
	}
}
