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
	private String previousValue = "0.00";
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
							String rawdata = new String(event.getReceivedData());

							String data = JUtility.removeASCII(rawdata);
							logger.debug("Debug RX [" + data + "]");

							try
							{

								if (data.startsWith("S S "))
								{
									String value = data.substring(5).trim().replace("g", "").trim();

									logger.debug(" Previous Weight = " + previousValue);
									logger.debug(" Weight          = " + value);

									if (value.equals(previousValue) == false)
									{

										if (Double.valueOf(previousValue).intValue() == 0)
										{
											if ((cb == null) == false)
											{
												logger.debug("**RECORD WEIGHT** [" + value + "]");
												cb.setWeight(value);
											}
											previousValue = value;

										}
										else
										{
											previousValue = value;
										}

									}
								}


							}
							catch (Exception ex)
							{
								logger.debug("Error [" + ex.getMessage() + "]");
							}

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

							String rawdata = new String(event.getReceivedData());

							String data = JUtility.removeASCII(rawdata);
							logger.debug("Debug RX [" + data + "]");

							try
							{

								if (data.startsWith(" 1G ") || data.startsWith("Z1G "))
								{
									String value = data.substring(5).trim().replace("g", "").trim();

									logger.debug(" Previous Weight = " + previousValue);
									logger.debug(" Weight          = " + value);

									if (value.equals(previousValue) == false)
									{

										if (Double.valueOf(previousValue).intValue() == 0)
										{
											if ((cb == null) == false)
											{
												logger.debug("**RECORD WEIGHT** [" + value + "]");
												cb.setWeight(value);
											}
											previousValue = value;

										}
										else
										{
											previousValue = value;
										}

									}
								}


							}
							catch (Exception ex)
							{
								logger.debug("Error [" + ex.getMessage() + "]");
							}

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
					if (waitForResponse == false)
					{
						scaleRequestDisplayedWeight();
					}

				}

				cb = null;

				if (scaledb.isConnected())
				{
					scaledb.disconnect();
				}
			}
		}

	}


	public void scaleReset()
	{

		if (scaledb.getMake().equals("METTLER TOLEDO"))
		{

			scaleTX("@");

		}
		if (scaledb.getMake().equals("AVERY W-TRONIX"))
		{

			scaleTX("C");

		}
	}

	public void scaleRequestWeightonChange()
	{

		if (scaledb.getMake().equals("METTLER TOLEDO"))
		{

			scaleTX("SR");

		}
	}

	public void scaleRequestStableWeight()
	{

		if (scaledb.getMake().equals("METTLER TOLEDO"))
		{

			scaleTX("S");

		}

	}

	public void scaleRequestDisplayedWeight()
	{

		if (scaledb.getMake().equals("METTLER TOLEDO"))
		{

			scaleTX("SI");

		}
		if (scaledb.getMake().equals("AVERY W-TRONIX"))
		{

			scaleTX("W");

		}

	}

	private void scaleTX(String data)
	{

		scaledb.scaleTX(data);

	}

}
