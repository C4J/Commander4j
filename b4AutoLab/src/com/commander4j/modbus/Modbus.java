package com.commander4j.modbus;

import java.io.IOException;
import java.util.Calendar;

import com.commander4j.autolab.AutoLab;
import com.commander4j.batch.Batch;
import com.commander4j.label.Label;
import com.commander4j.output.ProcDec_XML;
import com.commander4j.prodLine.ProdLine;
import com.commander4j.resources.JRes;
import com.commander4j.utils.JUtility;
import com.commander4j.utils.JWait;
import org.apache.logging.log4j.Logger;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;

public class Modbus extends Thread
{

	private ModbusClient modbusClient;
	private boolean run = true;
	private Boolean currentValue = false;
	private Boolean previousValue = false;
	private String ipAddress;
	private int portNumber;
	private int timeOut;
	private int address;
	private boolean printOnValue;
	private JWait wait = new JWait();
	private String uuid = "";
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((Modbus.class));
	private int labelCount = 0;
	boolean inProgress = false;
	private String ssccSequenceFilename = "";
	private String sscc = "";
	private String batchNo = "";
	private JUtility utils = new JUtility();
	private Batch batchNumber = new Batch();
	private Calendar caldate;
	private Calendar expiryDate;
	private String expiryString;
	private ProcDec_XML prodDec = new ProcDec_XML();
	private String lastMessage = "";

	public Modbus(String uuid, String name, String ipAddress, int portNumber, int timeOut, int address, boolean printOnValue, String ssccSequenceFilename)
	{
		this.uuid = uuid;
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		this.timeOut = timeOut;
		this.address = address;
		this.ssccSequenceFilename = ssccSequenceFilename;
		this.printOnValue = printOnValue;
		setName("Modbus " + name + " (" + ipAddress + ") [Coil " + address + "]");
		this.modbusClient = new ModbusClient();
		logger.debug("[" + getUuid() + "] {" + getName() + "} Instance Created.");
	}

	public void appendNotification(String message)
	{
		if (message.equals(lastMessage) == false)
		{
			((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).appendNotification(message);
			lastMessage = message;
		}
	}

	public void setNotification(String message)
	{
		((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).setNotification(message);
	}

	public String getSsccSequenceFilename()
	{
		return ssccSequenceFilename;
	}

	public void setSsccSequenceFilename(String ssccSequenceFilename)
	{
		this.ssccSequenceFilename = ssccSequenceFilename;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	public int getPortNumber()
	{
		return portNumber;
	}

	public void setPortNumber(int portNumber)
	{
		this.portNumber = portNumber;
	}

	public int getTimeOut()
	{
		return timeOut;
	}

	public void setTimeOut(int timeOut)
	{
		this.timeOut = timeOut;
	}

	public void shutdown()
	{
		if (inProgress)
		{
			logger.debug("[" + getUuid() + "] {" + getName() + "} " + "<<<<<<<Abort shutdown>>>>>>> until print run complete**");
		}
		else
		{
			run = false;
			if (modbusClient.isConnected())
			{
				try
				{
					appendNotification(JRes.getText("modbus_disconnecting_from") + " [" + getIpAddress() + ":" + getPortNumber() + "] Coil " + address);
					modbusClient.Disconnect();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				modbusClient = null;
			}
			logger.debug("[" + getUuid() + "] {" + getName() + "} " + "Thread Shutdown Requested.");
		}
	}

	public void run()
	{

		run = true;
		logger.debug("[" + getUuid() + "] {" + getName() + "} " + "Thread Started...");

		while (run == true)
		{

			try
			{
				if (modbusClient.isConnected())
				{
					try
					{
						appendNotification(JRes.getText("modbus_disconnecting_from") + " [" + getIpAddress() + ":" + getPortNumber() + "] Coil " + address);
						modbusClient.Disconnect();
					}
					catch (IOException e1)
					{
						logger.debug("[" + getUuid() + "] {" + getName() + "} IOException " + e1.getLocalizedMessage());
					}
				}

				appendNotification(JRes.getText("modbus_attempting_connection_to_device") + " [" + getIpAddress() + ":" + getPortNumber() + "] Coil " + address);

				modbusClient.setipAddress(getIpAddress());
				modbusClient.setPort(getPortNumber());
				modbusClient.setConnectionTimeout(getTimeOut());
				modbusClient.Connect();

				currentValue = false;
				previousValue = false;

				if (modbusClient.isConnected())
				{
					appendNotification(JRes.getText("modbus_connected_to_device") + " [" + getIpAddress() + ":" + getPortNumber() + "] Coil " + address);
				}

				while (modbusClient.isConnected() && (run == true))
				{

					try
					{
						currentValue = modbusClient.ReadCoils(address, 1)[0];

						if (currentValue != previousValue)
						{

							previousValue = currentValue;

							if (currentValue == printOnValue)
							{
								if (run)
								{
									logger.debug("[" + getUuid() + "] {" + getName() + "} " + "[PRINT REQUEST DETECTED].....");

									appendNotification(JRes.getText("modbus_print_request_detected"));

									if (AutoLab.isDataSet_DataReady(getUuid()))
									{
										AutoLab.setPrintProperties(getUuid(), AutoLab.getDataSet_Field(getUuid(), "IP_ADDRESS"), Integer.valueOf(AutoLab.getDataSet_Field(getUuid(), "PORT")));

										if (labelCount == 0)
										{
											sscc = AutoLab.get_SSCC_Sequence(getSsccSequenceFilename());
											AutoLab.setDataSet_FieldValue(uuid, "SSCC", sscc);

											// Get current date and time
											caldate = Calendar.getInstance();

											batchNo = batchNumber.getDefaultBatchNumber(uuid, caldate);
											AutoLab.setDataSet_FieldValue(uuid, "BATCH_NUMBER", batchNo);

											// Convert current date time to
											// String in correct format and
											// store back into HashMap
											AutoLab.setDataSet_FieldValue(uuid, "DATE_OF_MANUFACTURE", utils.getFormattedCSVCalendarString(caldate));

											expiryDate = utils.calcBBE(caldate, Integer.valueOf(AutoLab.getDataSet_Field(getUuid(), "SHELF_LIFE")), AutoLab.getDataSet_Field(getUuid(), "SHELF_LIFE_UOM"), AutoLab.getDataSet_Field(getUuid(), "SHELF_LIFE_RULE"));
											expiryString = utils.getFormattedCSVCalendarString(expiryDate);
											AutoLab.setDataSet_FieldValue(uuid, "EXPIRY_DATE", expiryString);

											logger.debug("[" + getUuid() + "] {" + getName() + "} " + "--------------------------------------------------------------------------------------------------------------");
											logger.debug("[" + getUuid() + "] {" + getName() + "} " + "SSCC                 = " + sscc + " <<NEW>>");
										}
										else
										{
											logger.debug("[" + getUuid() + "] {" + getName() + "} " + "--------------------------------------------------------------------------------------------------------------");
											logger.debug("[" + getUuid() + "] {" + getName() + "} " + "SSCC                 = " + sscc + " <<REPEAT>>");
										}

										// logger.debug("[" + getUuid() + "] {"
										// + getName() + "} " +
										// "--------------------------------------------------------------------------------------------------------------");
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "PROCESS_ORDER        = " + AutoLab.getDataSet_Field(getUuid(), "PROCESS_ORDER"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "MATERIAL             = " + AutoLab.getDataSet_Field(getUuid(), "MATERIAL"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "MATERIAL_DESCRIPTION = " + AutoLab.getDataSet_Field(getUuid(), "DESCRIPTION"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "CUSTOMER_ID          = " + AutoLab.getDataSet_Field(getUuid(), "CUSTOMER_ID"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "DATE_OF_MANUFACTURE  = " + AutoLab.getDataSet_Field(getUuid(), "DATE_OF_MANUFACTURE"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "SHELF_LIFE           = " + AutoLab.getDataSet_Field(getUuid(), "SHELF_LIFE"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "SHELF_LIFE_UOM       = " + AutoLab.getDataSet_Field(getUuid(), "SHELF_LIFE_UOM"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "SHELF_LIFE_RULE      = " + AutoLab.getDataSet_Field(getUuid(), "SHELF_LIFE_RULE"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "EXPIRY_DATE          = " + AutoLab.getDataSet_Field(getUuid(), "EXPIRY_DATE"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "BATCH_FORMAT         = " + AutoLab.getDataSet_Field(getUuid(), "BATCH_FORMAT"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "BATCH_NUMBER         = " + AutoLab.getDataSet_Field(getUuid(), "BATCH_NUMBER"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "REPORT_FILENAME      = " + AutoLab.getDataSet_Field(getUuid(), "REPORT_FILENAME"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "IP_ADDRESS (Printer) = " + AutoLab.getDataSet_Field(getUuid(), "IP_ADDRESS"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "PORT       (Printer) = " + AutoLab.getDataSet_Field(getUuid(), "PORT"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "LANGUAGE   (Printer) = " + AutoLab.getDataSet_Field(getUuid(), "LANGUAGE"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "LABELS_PER_PRINT     = " + AutoLab.getDataSet_Field(getUuid(), "LABELS_PER_PRINT"));
										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "LABELS_PER_PALLET    = " + AutoLab.getDataSet_Field(getUuid(), "LABELS_PER_PALLET"));

										logger.debug("[" + getUuid() + "] {" + getName() + "} " + "--------------------------------------------------------------------------------------------------------------");

										int labelsPerPrint = Integer.valueOf(AutoLab.getDataSet_Field(getUuid(), "LABELS_PER_PRINT"));
										int labelsPerPallet = Integer.valueOf(AutoLab.getDataSet_Field(getUuid(), "LABELS_PER_PALLET"));

										if (labelCount < labelsPerPallet)
										{
											for (int x = 0; x < labelsPerPrint; x++)
											{
												while (AutoLab.isDataReady(getUuid()))
												{
													wait.manySec(1);
													logger.debug("[" + getUuid() + "] {" + getName() + "} " + "Waiting for printer to be ready");

												}

												labelCount++;

												AutoLab.setDataSet_FieldValue(uuid, "LABEL_NO", String.valueOf(labelCount).trim());
												logger.debug("**********************************************************************************************************************************************************************");
												logger.debug("[" + getUuid() + "] {" + getName() + "} " + "Printing " + labelCount + " of " + labelsPerPallet + " for Process Order " + AutoLab.getDataSet_Field(getUuid(), "PROCESS_ORDER") + " - SSCC " + sscc);
												logger.debug("**********************************************************************************************************************************************************************");

												appendNotification(JRes.getText("preparing_label") + " " + (x + 1) + " " + JRes.getText("of") + " " + labelsPerPrint);

												Label label = new Label();
												String zpl = label.process(uuid);

												AutoLab.set_PrintData(getUuid(), zpl);
												
												AutoLab.request_Print(getUuid());
												
												AutoLab.set_PreviewData(getUuid(), zpl);

												//* Wait for print thread to confirm its 
												
												while (AutoLab.isDataReady(getUuid()))
												{
													try
													{
														Thread.sleep(1000);
														logger.debug("Waiting for confirmation that print is complete.");
														appendNotification(JRes.getText("wait_for_print_confirmation"));
													}
													catch (Exception ex)
													{
														break;
													}
												}

												if (labelCount == labelsPerPallet)
												{
													labelCount = 0;
													inProgress = false;

													if (AutoLab.isDataReady(getUuid()) == false)
													{
														appendNotification(JRes.getText("print_confirmation_received"));
														logger.debug("Print confirmation received");
														logger.debug("[" + getUuid() + "] {" + getName() + "} " + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
														logger.debug("[" + getUuid() + "] {" + getName() + "} " + "+++Generate Production Declaration " + sscc + " +++");
														logger.debug("[" + getUuid() + "] {" + getName() + "} " + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
														prodDec.setUuid(uuid);
														prodDec.processMessage();
													}
												}
												else
												{
													inProgress = true;
												}
											}
										}

									}
								}
							}
						}
						Thread.sleep(250);
					}
					catch (IOException e1)
					{
						logger.debug("[" + getUuid() + "] {" + getName() + "} IOException during read " + e1.getLocalizedMessage());
						appendNotification(JRes.getText("modbus_cannot_connect_to_device") + " [" + ":" + getPortNumber() + "] Coil " + address);
						modbusClient.Disconnect();
					}

				}

				if (modbusClient.isConnected())
				{
					try
					{
						modbusClient.Disconnect();
						appendNotification(JRes.getText("modbus_disconnected_from_device") + " [" + ":" + getPortNumber() + "] Coil " + address);
					}
					catch (IOException e1)
					{
						logger.debug("[" + getUuid() + "] {" + getName() + "} IOException " + e1.getLocalizedMessage());
					}
				}
			}

			catch (ModbusException e)
			{
				logger.debug("[" + getUuid() + "] {" + getName() + "} " + "ModbusException " + e.getLocalizedMessage());
			}

			catch (java.net.UnknownHostException e)
			{
				logger.debug("[" + getUuid() + "] {" + getName() + "} " + "UnknownHostException " + e.getLocalizedMessage());
			}

			catch (java.net.SocketException e)
			{
				logger.debug("[" + getUuid() + "] {" + getName() + "} " + "SocketException " + e.getLocalizedMessage());
				if (run == false)
				{
					break;
				}

			}

			catch (java.io.IOException e)
			{
				logger.debug("[" + getUuid() + "] {" + getName() + "} " + "IOException " + e.getLocalizedMessage());
			}
			catch (InterruptedException e)
			{
				logger.debug("[" + getUuid() + "] {" + getName() + "} " + "Interrupted Exception " + e.getLocalizedMessage());
				if (inProgress)
				{
					logger.debug("[" + getUuid() + "] {" + getName() + "} " + "<<<<<<<Abort shutdown>>>>>>> until print run complete**");
				}
				else
				{
					run = false;
					logger.debug("[" + getUuid() + "] {" + getName() + "} " + "Thread Shutdown Requested.");
				}
			}

		}
		logger.debug("[" + getUuid() + "] {" + getName() + "} " + "Thread Stopped...");
	}
}
