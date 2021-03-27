package com.commander4j.autolab;

import org.apache.logging.log4j.LogManager;

import com.commander4j.utils.JWait;

public class ShutdownHook extends Thread
{
	JWait wait = new JWait();

	@Override
	public void run()
	{

		StartStop.autolab.requestStop();
		StartStop.autolab.interrupt();

		while (StartStop.autolab.isAlive())
		{

			wait.oneSec();
		}
	
		LogManager.shutdown();
	}

}
