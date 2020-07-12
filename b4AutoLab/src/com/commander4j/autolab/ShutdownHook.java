package com.commander4j.autolab;

import com.commander4j.utils.JWait;

public class ShutdownHook extends Thread
{
	JWait wait = new JWait();

			@Override
			public void run()
			{
				StartStop.debugToFile("Shutdown detected - requesting threads stop.");
			
				StartStop.autolab.requestStop();
				
				while (StartStop.autolab.isAlive())
				{
					StartStop.debugToFile("Waiting for threads to stop.");
					wait.oneSec();
				}
				
				StartStop.debugToFile("Stopped.");
			}

	
	
}
