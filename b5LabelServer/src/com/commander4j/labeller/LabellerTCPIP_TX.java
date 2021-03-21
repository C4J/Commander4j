package com.commander4j.labeller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.logging.log4j.Logger;

public class LabellerTCPIP_TX extends Thread
{
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((LabellerTCPIP_TX.class));
	private LabellerProperties prop;
	private OutputStream outputStream;
	public volatile boolean shutdown = false;
	private Socket socket;
	private LabellerUtility utils = new LabellerUtility();

	private String[] statusNames = new String[] {"RUNNING", "SHUTDOWN REQUESTED", "ERROR", "IDLE" };
	public static int status_RUNNING = 0;
	public static int status_SHUTDOWN_REQUESTED = 1;
	public static int status_ERROR = 2;
	public static int status_IDLE = 3;
	private int status = status_IDLE;

	public String getStatusName()
	{
		return statusNames[status];
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	@Override
	public void run()
	{
		setStatus(status_IDLE);
		
		logger.info("TX Status started.");
		try
		{
			outputStream = socket.getOutputStream();
			setStatus(status_RUNNING);
			while (shutdown == false)
			{
				utils.pause(100);
			}
			
		} catch (IOException e)
		{
			logger.error(e.getMessage());
			setStatus(status_ERROR);
		}

		logger.info("TX Status stopped. ("+String.valueOf(getStatus()+")"));
	}

	public void shutdown()
	{
		logger.debug("TX Status Shutdown requested.");
		setStatus(status_SHUTDOWN_REQUESTED);
		shutdown = true;
		utils.pause(100);
		
		try
		{
			outputStream.close();
		} catch (Exception e)
		{
			logger.error("Shutdown :"+e.getMessage());
		}
	}

	public void setSocketConnection(Socket socket)
	{
		this.socket = socket;
	}

	public void setLabellerProperties(LabellerProperties prop)
	{
		this.prop = prop;
		logger.debug("Labeller properties stored for "+this.prop.getId());

	}

	public Boolean send(String data)
	{

		Boolean result = false;
		
		if (data.equals("") == false)
		{
			String sendData = utils.encodeControlChars(data);
			
			try
			{
				logger.info("TX------->{"+utils.decodeControlChars(data)+"}");
				outputStream.write(sendData.getBytes());
				outputStream.flush();
				
				data = "";
				result = true;

			} catch (IOException e)
			{
				logger.error("TX ERROR-->{"+e.getMessage()+"}");
				setStatus(status_ERROR);
			}
		}
		return result;
	}

}
