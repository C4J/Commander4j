package com.commander4j.zebra;

import java.io.*;
import java.net.*;
import org.apache.logging.log4j.Logger;

import com.commander4j.autolab.AutoLab;


/**
 * @author dave
 *
 */
public class Print extends Thread
{
	private String ipAddress = "0.0.0.0";
	private int portNumber = 9100;
	private String printerName = "";
	private boolean run = true;
	private boolean dataReady = false;
	private String data = "";
	private Socket clientSocket;
	private String errorMessage = "";
	private String uuid = "";
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((Print.class));

	public Print(String uuid, String name)
	{
		this.uuid = uuid;
		setPrinterName(name);
		setThreadName();
		logger.debug("[" + getUuid()+"] {"+getName()+"} Print Instance Created.");
	}

	public void shutdown()
	{
		logger.debug("[" + getUuid()+"] {"+getName()+"} Print Thread Shutdown Requested.");
		run = false;
	}

	public void setPrinterName(String name)
	{
		this.printerName = name;
		setThreadName();
	}

	public void setThreadName()
	{
		setName("Print " + getPrinterName() + " (" + ipAddress + ":" + String.valueOf(getPortNumber()) + ")");
	}

	public String getPrinterName()
	{
		return printerName;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
		setThreadName();
		logger.debug("[" + getUuid()+"] {"+getName()+"} IP Address set to " + getIpAddress());
	}

	public int getPortNumber()
	{
		return portNumber;
	}

	public void setPortNumber(int portNumber)
	{
		this.portNumber = portNumber;
		setThreadName();
		logger.debug("[" + getUuid()+"] {"+getName()+"} Port set to " + getPortNumber());
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public void setDataReady(boolean dataReady)
	{
		this.dataReady = dataReady;
	}

	public boolean isDataReady()
	{
		return dataReady;
	}

	public boolean setData(String data)
	{
		boolean result = false;

		if (isDataReady() == false)
		{
			this.data = data;
			result = true;
		}

		return result;
	}

	public String getData()
	{
		return this.data;
	}

	public void run()
	{

		run = true;
		logger.debug("[" + getUuid()+"] {"+getName()+"} Thread Started...");

		while (run == true)
		{

			try
			{

				if (isDataReady())
				{

					if (AutoLab.config.isSuppressLabelPrint())
					{
						setDataReady(false);
					}
					else
					{

						try
						{
							this.clientSocket = new Socket(this.ipAddress, this.portNumber);

							DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

							outToServer.writeBytes(this.getData());

							clientSocket.close();

							setErrorMessage("");

							setDataReady(false);
						}
						catch (UnknownHostException e)
						{
							setErrorMessage(e.getLocalizedMessage());
						}
						catch (IOException e)
						{
							setErrorMessage(e.getLocalizedMessage());
						}
					}
				}

				Thread.sleep(250);
			}
			catch (InterruptedException e)
			{
				logger.debug("[" + getUuid()+"] {"+getName()+"} Interrupted Exception " + e.getLocalizedMessage());
				run = false;
			}
		}

		logger.debug("[" + getUuid()+"] {"+getName()+"} Thread Stopped...");
	}
}
