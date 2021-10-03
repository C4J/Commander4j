package com.commander4j.scales;

import org.apache.log4j.Logger;

import com.commander4j.db.JDBWTScale;
import com.commander4j.db.JDBWTWorkstation;
import com.commander4j.util.JUtility;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

public class Scale extends Thread
{
	private boolean run = false;
	private JDBWTWorkstation workdb;
	private JDBWTScale scaledb;
	private String workstation = "";
	private String hostID = "";
	private String sessionID = "";
	private boolean waitForResponse = false;
	private String previousValue = "";
	private ScaleCallbackInteface cb;

	private final Logger logger = Logger.getLogger(Scale.class);

	public Scale(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void setCallbackInterface(ScaleCallbackInteface scb)
	{
		this.cb = scb;
	}

	public void shutdown(boolean stop)
	{
		run = stop;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private String getHostID()
	{
		return hostID;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	private void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}

	public void run()
	{

		workstation = JUtility.getClientName().toUpperCase();
		scaledb = new JDBWTScale(getHostID(), getSessionID());
		workdb = new JDBWTWorkstation(getHostID(), getSessionID());

		logger.debug("Host ID identified for service is  [" + hostID + "]");

		if (workdb.getProperties(workstation))
		{
			if (scaledb.connect(workdb.getScaleID(), workdb.getScalePort()))
			{

				// *******************************************************************************************
				// ********************************** METTLER TOLEDO
				// *******************************************************************************************

				if (scaledb.getMake().equals("METTLER TOLEDO"))
				{

					scaledb.comPort.addDataListener(new SerialPortMessageListener()
					{
						@Override
						public int getListeningEvents()
						{
							return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
						}

						@Override
						public void serialEvent(SerialPortEvent event)
						{
							String data = JUtility.removeASCII(new String(event.getReceivedData()));

							try
							{

								if (data.startsWith("S S "))
								{
									String value = data.substring(5).trim().replace("g", "").trim();

									if (value.equals(previousValue) == false)
									{
										System.out.println("Weight = " + value);

										if ((cb == null) == false)
										{
											cb.setWeight(value);
										}
										previousValue = value;

									}

								}
								else
								{
									previousValue = "";
								}

							}
							catch (Exception ex)
							{
								System.out.println("Error [" + ex.getMessage() + "]");
							}

							waitForResponse = false;

						}

						@Override
						public boolean delimiterIndicatesEndOfMessage()
						{
							// TODO Auto-generated method stub
							return true;
						}

						@Override
						public byte[] getMessageDelimiter()
						{
							byte[] eol = JUtility.getASCIIfromTokens(scaledb.getEndOfLine()).getBytes();

							return eol;
						}
					});

					scaleReset();

					// scaleRequestWeightonChange();

				}

				// *******************************************************************************************
				// ********************************** AVERY W-TRONIX
				// *******************************************************************************************

				if (scaledb.getMake().equals("AVERY W-TRONIX"))
				{

					scaledb.comPort.addDataListener(new SerialPortMessageListener()
					{
						@Override
						public int getListeningEvents()
						{
							return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
						}

						@Override
						public void serialEvent(SerialPortEvent event)
						{
							String data = JUtility.removeASCII(new String(event.getReceivedData()));

							try
							{

								if (data.startsWith(" 1G "))
								{
									String value = data.substring(5).trim().replace("g", "").trim();

									if (value.equals(previousValue) == false)
									{
										System.out.println("Weight = " + value);
										if ((cb == null) == false)
										{
											cb.setWeight(value);
										}
										previousValue = value;

									}
								}
								else
								{
									previousValue = "";
								}

							}
							catch (Exception ex)
							{
								System.out.println("Error [" + ex.getMessage() + "]");
							}

							waitForResponse = false;

						}

						@Override
						public boolean delimiterIndicatesEndOfMessage()
						{
							// TODO Auto-generated method stub
							return true;
						}

						@Override
						public byte[] getMessageDelimiter()
						{
							byte[] eol = JUtility.getASCIIfromTokens(scaledb.getEndOfLine()).getBytes();

							return eol;
						}
					});

					scaleReset();
				}

				run = true;

				// Connect

				while (run == true)
				{

					// Poll

					try
					{
						Thread.sleep(10);
					}
					catch (InterruptedException e)
					{
						run = false;
						// break;
					}

					scaleRequestDisplayedWeight();

				}
				
				cb=null;
			}
		}

	}

	private boolean waitForResponse(int timeout)
	{
		boolean result = false;

		while (waitForResponse)
		{
			try
			{
				logger.debug("waitForResponse " + timeout);
				Thread.sleep(1000);
				timeout--;
				if (timeout == 0)
				{
					logger.debug("waitForResponse timeout");
					waitForResponse = false;
				}
			}
			catch (InterruptedException e)
			{
				run = false;
				waitForResponse = false;
				// break;
			}
		}

		return result;
	}

	public void scaleReset()
	{
		if (scaledb.getMake().equals("METTLER TOLEDO"))
		{
			waitForResponse = true;
			scaledb.scaleTX("@");
			waitForResponse(2);
		}
		if (scaledb.getMake().equals("AVERY W-TRONIX"))
		{
			waitForResponse = true;
			scaledb.scaleTX("C");
			waitForResponse(2);
		}
	}

	public void scaleRequestWeightonChange()
	{
		if (scaledb.getMake().equals("METTLER TOLEDO"))
		{
			waitForResponse = true;
			scaledb.scaleTX("SR");
			waitForResponse(2);
		}
	}

	public void scaleRequestStableWeight()
	{
		if (scaledb.getMake().equals("METTLER TOLEDO"))
		{
			waitForResponse = true;
			scaledb.scaleTX("S");
			waitForResponse(2);
		}
	}

	public void scaleRequestDisplayedWeight()
	{
		if (scaledb.getMake().equals("METTLER TOLEDO"))
		{
			waitForResponse = true;
			scaledb.scaleTX("SI");
			waitForResponse(2);
		}
		if (scaledb.getMake().equals("AVERY W-TRONIX"))
		{
			waitForResponse = true;
			scaledb.scaleTX("W");
			waitForResponse(2);
		}
	}

}
