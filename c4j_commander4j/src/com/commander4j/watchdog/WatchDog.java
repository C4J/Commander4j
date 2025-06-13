package com.commander4j.watchdog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

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
             	logger.debug("WatchDog listening on port " + " : " + String.valueOf(port));

				while (true)
				{

					logger.debug("WatchDog listening on port "+ " : "+String.valueOf(port));
					
					clientSocket = serverSocket.accept();

					String clientIP = clientSocket.getInetAddress().getHostAddress();

					try
					{
						clientSocket.setSoTimeout(5000);

						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						String message = in.readLine();

						if ("shutdown requested".equalsIgnoreCase(message))
						{


							if (run)
							{

								logger.debug("WatchDog shutdown request detected.");

								System.exit(0);
					

								run = false;
							}
						}
						else
						{
							System.out.println("Unexpected message: " + message);
						}
					}
					catch (SocketTimeoutException e)
					{

					}
					catch (IOException e)
					{
						System.out.println("I/O error with " + clientIP + ": " + e.getMessage());
					}
					finally
					{
						try
						{
							clientSocket.close();
						}
						catch (IOException e)
						{
							// Ignore
						}
					}
				}
			}
			catch (IOException e1)
			{

			}

		}

		logger.debug("WatchDog Instance Stopped.");
	}

}
