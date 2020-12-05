package com.commander4j.email;

import org.apache.logging.log4j.Logger;

import com.commander4j.sftp.Transfer;


public class EmailThread extends Thread
{
	Integer counter = 0;
	com.commander4j.util.JWait wait = new com.commander4j.util.JWait();
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

			Transfer.emailqueue.processQueue();

		}
		
		Transfer.emailqueue.processQueue();
		
		logger.debug("EmailThread stopped.");
	}
}
