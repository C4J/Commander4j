package com.commander4j.watchdog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.Logger;

public class WatchDog extends Thread
{

	private boolean run = true;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((WatchDog.class));
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private Integer port = 8000;


	public WatchDog(int port)
	{
		this.port = port;
		logger.debug("WatchDog Instance Created.");
	}

	public void shutdown()
	{
		try
		{
			clientSocket.close();
		}
		catch (Exception e)
		{

		}
		this.run = false;
	}



	public void run()
	{

		logger.debug("WatchDog Instance Started.");

		run = true;


		while (run == true)
		{

			try
			{
				serverSocket = new ServerSocket(port, 1);

				while (true)
				{

					logger.debug("WatchDog listening on port "+ " : "+String.valueOf(port));
					
					clientSocket = serverSocket.accept();

					clientSocket.close();

					logger.debug("WatchDog shutdown request detected.");

					System.exit(0);

					run = false;
				}
			}
			catch (IOException e1)
			{

			}

		}

		logger.debug("WatchDog Instance Stopped.");
	}

}
