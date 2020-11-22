package com.commander4j.sftpSend;

import org.apache.logging.log4j.LogManager;


public class ShutdownHook extends Thread
{

	@Override
	public void run()
	{
		
		JWait wait = new JWait();
		Transfer.sftpsend.requestStop();
		Transfer.sftpsend.interrupt();

		while (Transfer.sftpsend.isAlive())
		{

			wait.oneSec();
		}
	
		LogManager.shutdown();
	}

}
