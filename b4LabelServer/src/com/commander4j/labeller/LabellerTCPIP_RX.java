package com.commander4j.labeller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Logger;

public class LabellerTCPIP_RX extends Thread
{
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((LabellerTCPIP_RX.class));
	private LabellerProperties prop;
	public volatile boolean shutdown = false;
	private BufferedInputStream bufferedInputStream;
	private InputStream inputStream = null;
	private Socket socket;
	private String responseBuffer = "";
	public volatile ConcurrentLinkedQueue<String> responseQueue = new ConcurrentLinkedQueue<String>();
	public volatile ConcurrentLinkedQueue<String> ioQueue = new ConcurrentLinkedQueue<String>();
	
	public volatile ConcurrentLinkedQueue<String> ignoredResponses = new ConcurrentLinkedQueue<String>();
	public volatile ConcurrentLinkedQueue<String> successResponses = new ConcurrentLinkedQueue<String>();
	public ConcurrentLinkedQueue<String> failedResponses = new ConcurrentLinkedQueue<String>();
	private String responseEOL = "<CR>";
	LabellerUtility utils = new LabellerUtility();
	private String[] statusNames = new String[]
	{ "RUNNING", "SHUTDOWN REQUESTED", "ERROR", "IDLE" };
	public static int status_RUNNING = 0;
	public static int status_SHUTDOWN_REQUESTED = 1;
	public static int status_ERROR = 2;
	public static int status_IDLE = 3;
	private int status = status_IDLE;
	
	public void clearQueue()
	{

		try
		{
		for (int x=0;x<=100;x++)
		{
			utils.pause(50);
			responseQueue.remove();
		}
		}
		catch (Exception ex)
		{
			
		}
	}
	
	public void setResponseEOL(String eol)
	{
		responseEOL=eol;
	}
	
	public String getResponseEOL()
	{
		return responseEOL;
	}
	
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
		setStatus(status_RUNNING);
		logger.info("RX Status started.");

		try
		{
			inputStream = socket.getInputStream();
			bufferedInputStream = new BufferedInputStream(inputStream);
		} catch (IOException e)
		{
			setStatus(status_ERROR);
			shutdown = true;
		}

		while (shutdown == false)
		{
			try
			{
				String got = readInputStream(bufferedInputStream); // in.readLine();
				if (got == null)
				{
					setStatus(status_ERROR);
					shutdown = true;
				} else
				{

					String encodedEOL = utils.encodeControlChars(responseEOL);
					int encodedEOLLength = encodedEOL.length();
					String beforeEOL = "";
					String afterEOL = "";
					
					while ((responseBuffer.contains(encodedEOL)) && (responseBuffer.length() > encodedEOLLength) && (responseBuffer.indexOf(encodedEOL) <= (responseBuffer.length() - encodedEOLLength)))
					{

						beforeEOL = responseBuffer.substring(0, responseBuffer.indexOf(encodedEOL));
						afterEOL = responseBuffer.substring(beforeEOL.length() + encodedEOL.length(), responseBuffer.length());
						if (beforeEOL.equals("") == false)
						{
							logger.info("RX<---{"+utils.decodeControlChars(beforeEOL)+"}");
							
							if (ignoredResponses.contains(beforeEOL) == false)
							{
								if ((successResponses.contains(beforeEOL) == true))
								{	
									beforeEOL = "SUCCESS";
								}
								else
								{
									if ((failedResponses.contains(beforeEOL) == true))
									//if (beforeEOL.equals("FAILED")==true)
									{
										beforeEOL = "FAILED";
										responseQueue.add(beforeEOL);
										break;
									}
								}

							} else
							{
								beforeEOL="";
							}
						}

						responseBuffer = afterEOL;

						if (beforeEOL.equals("")==false)
						{
							while ((beforeEOL.indexOf("{")>=0) && (beforeEOL.indexOf("}\r")>=0) ) 
							{
								String found = beforeEOL.substring(beforeEOL.indexOf("{"), beforeEOL.indexOf("}\r"));
								ioQueue.add(found);
								beforeEOL = beforeEOL.replaceFirst(found, "");
							}
							responseQueue.add(beforeEOL);
							//System.out.println("["+prop.getId()+"]"+" RX<-------{"+utils.decodeControlChars(beforeEOL)+"}");
						}
					}

				}

			} catch (IOException e)
			{

				if (shutdown == true)
				{
					setStatus(status_SHUTDOWN_REQUESTED);
				} else
				{
					shutdown = true;
					setStatus(status_ERROR);
				}

			}

			utils.pause(10);
		}
		logger.info("RX Status stopped.");
	}

	private String readInputStream(BufferedInputStream _in) throws IOException
	{
		String data = "";
		String returnData = "";
		int s = _in.read();
		if (s == -1)
			return null;
		data += "" + (char) s;
		int len = _in.available();
		if (len > 0)
		{
			byte[] byteData = new byte[len];
			_in.read(byteData);
			data += new String(byteData);
		}

		returnData = utils.encodeControlChars(data);
		responseBuffer = responseBuffer + returnData;

		return returnData;
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

	public void shutdown()
	{
		logger.debug("RX Status " + prop + " Shutdown requested.");
		setStatus(status_SHUTDOWN_REQUESTED);
		shutdown = true;
		utils.pause(10);
		try
		{
			inputStream.close();
		} catch (IOException e)
		{
		}
	}
}
