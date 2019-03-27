package com.commander4j.thread;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : InboundMessageThread.java
 * 
 * Package Name : com.commander4j.thread
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.log4j.Logger;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;

import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBUser;
import com.commander4j.email.JeMail;
import com.commander4j.messages.GenericMessageHeader;
import com.commander4j.messages.IncommingBatchStatusChange;
import com.commander4j.messages.IncommingDespatchConfirmation;
import com.commander4j.messages.IncommingInspectionResult;
import com.commander4j.messages.IncommingJourney;
import com.commander4j.messages.IncommingLocation;
import com.commander4j.messages.IncommingMaterialAutoMove;
import com.commander4j.messages.IncommingMaterialDefinition;
import com.commander4j.messages.IncommingPalletMove;
import com.commander4j.messages.IncommingPalletStatusChange;
import com.commander4j.messages.IncommingProcessOrder;
import com.commander4j.messages.IncommingProcessOrderStatusChange;
import com.commander4j.messages.IncommingProductionDeclarationConfirmation;
import com.commander4j.messages.IncommingQMInspectionRequest;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;

public class InboundMessageThread extends Thread {
	public boolean allDone = false;
	private String fromFile;
	private String inputPath;
	private String errorPath;
	private String backupPath;
	private File dir;
	private File[] chld;
	private String fileName;
	private JFileIO reader = new JFileIO();
	private String sessionID;
	private String hostID;
	private Boolean messageProcessedOK = false;
	private String errorMessage = "";
	private final Logger logger = Logger.getLogger(InboundMessageThread.class);
	private int maxfiles = 10000;

	public InboundMessageThread(String host, String path, String errorPath, String backupPath)
	{
		super();
		inputPath = path;
		this.errorPath = errorPath;
		this.backupPath = backupPath;
		setHostID(host);
		setSessionID(JUnique.getUniqueID());
		JDBUser user = new JDBUser(getHostID(), getSessionID());
		user.setUserId("interface");
		user.setPassword("interface");
		user.setLoginPassword("interface");
		Common.userList.addUser(getSessionID(), user);
		Common.sd.setData(getSessionID(), "silentExceptions", "Yes", true);
	}

	private String getHostID()
	{
		return hostID;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public void run()
	{
		logger.debug("InboundMessageThread running");
		Boolean dbconnected = false;

		if (Common.hostList.getHost(hostID).isConnected(sessionID) == false)
		{
			dbconnected = Common.hostList.getHost(hostID).connect(sessionID, hostID);
		} else
		{
			dbconnected = true;
		}

		if (dbconnected)
		{
			JDBInterfaceLog il = new JDBInterfaceLog(getHostID(), getSessionID());
			JeMail mail = new JeMail(getHostID(), getSessionID());
			JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
			IncommingMaterialDefinition imd = new IncommingMaterialDefinition(getHostID(), getSessionID());
			IncommingProcessOrderStatusChange iposc = new IncommingProcessOrderStatusChange(getHostID(), getSessionID());
			IncommingProductionDeclarationConfirmation ipd = new IncommingProductionDeclarationConfirmation(getHostID(), getSessionID());
			IncommingProcessOrder ipo = new IncommingProcessOrder(getHostID(), getSessionID());
			IncommingLocation ilocn = new IncommingLocation(getHostID(), getSessionID());
			IncommingPalletStatusChange ipsc = new IncommingPalletStatusChange(getHostID(), getSessionID());
			IncommingPalletMove ipmv = new IncommingPalletMove(getHostID(), getSessionID());
			IncommingBatchStatusChange bsc = new IncommingBatchStatusChange(getHostID(), getSessionID());
			IncommingJourney ij = new IncommingJourney(getHostID(), getSessionID());
			IncommingInspectionResult iirslt = new IncommingInspectionResult(getHostID(), getSessionID());
			IncommingDespatchConfirmation idc = new IncommingDespatchConfirmation(getHostID(), getSessionID());
			IncommingQMInspectionRequest iireq = new IncommingQMInspectionRequest(getHostID(), getSessionID());
			IncommingMaterialAutoMove imam = new IncommingMaterialAutoMove(getHostID(), getSessionID());
			GenericMessageHeader gmh = new GenericMessageHeader();
			LinkedList<String> filenames = new LinkedList<String>();
			BasicFileAttributes attrs;
			
			while (true)
			{

				com.commander4j.util.JWait.milliSec(100);

				if (allDone)
				{
					if (dbconnected)
					{
						Common.hostList.getHost(hostID).disconnect(getSessionID());
					}
					return;
				}

				if (InboundMessageCollectionThread.recoveringFiles == false)
				{

					dir = new File(inputPath);

					chld = dir.listFiles((FileFilter) FileFileFilter.FILE);

					if (chld == null)
					{
						allDone = true;
					} else
					{
						Arrays.sort(chld, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
						filenames.clear();

						for (int i = 0; (i < chld.length) & (i < maxfiles); i++)
						{
							fileName = chld[i].getName();
							try
							{
								attrs = Files.readAttributes(chld[i].getAbsoluteFile().toPath(), BasicFileAttributes.class);

								if (attrs.size() > 0)
								{
									if (fileName.endsWith(".xml"))
									{
										filenames.addFirst(fileName);
										com.commander4j.util.JWait.milliSec(50);
									}
								} else
								{
									try
									{
										chld[i].delete();
									}
									catch (Exception ex)
									{
										
									}
								}
							} catch (IOException e)
							{

							}

						}

						if (filenames.size() > 0)
						{
							logger.debug("Begin processing " + String.valueOf(filenames.size()) + " files.");
							for (int i = filenames.size() - 1; i >= 0; i--)
							{
								if (allDone)
								{
									if (dbconnected)
									{
										Common.hostList.getHost(hostID).disconnect(getSessionID());
									}
									return;
								}

								fromFile = filenames.get(i);

								try
								{
									logger.debug("<---  START OF PROCESSING " + fromFile + "  ---->");
									logger.debug("Reading message header : " + inputPath + fromFile);

									if (gmh.readAddressInfo(inputPath + fromFile, getSessionID()) == true)
									{

										messageProcessedOK = true;
										errorMessage = "";

										if (gmh.getInterfaceType().length() == 0)
										{
											messageProcessedOK = false;
											errorMessage = "Unrecognised Commander4j XML message format in file " + fromFile;
											logger.debug(errorMessage);
											String datetime = "";
											datetime = JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime());
											gmh.setMessageDate(datetime);
											gmh.setInterfaceDirection("Unknown");
											gmh.setMessageInformation(fromFile);
											gmh.setInterfaceType("Unknown");
											gmh.setMessageRef("Unknown");
										} else
										{
											if (gmh.getInterfaceDirection().equals("Input") == false)
											{
												messageProcessedOK = false;
												errorMessage = "Inbound message ignored - Interface Direction = " + gmh.getInterfaceDirection();
											} else
											{
												String interfaceType = gmh.getInterfaceType();
												logger.debug("Processing " + interfaceType + " started.");
												if (interfaceType.equals("Despatch Confirmation") == true)
												{
													messageProcessedOK = idc.processMessage(gmh);
													errorMessage = idc.getErrorMessage();
												}

												if (interfaceType.equals("Material Definition") == true)
												{
													messageProcessedOK = imd.processMessage(gmh);
													errorMessage = imd.getErrorMessage();
												}

												if (interfaceType.equals("Process Order") == true)
												{
													messageProcessedOK = ipo.processMessage(gmh);
													errorMessage = ipo.getErrorMessage();
												}
												
												if (interfaceType.equals("Location") == true)
												{
													messageProcessedOK = ilocn.processMessage(gmh);
													errorMessage = ilocn.getErrorMessage();
												}

												if (interfaceType.equals("Pallet Status Change") == true)
												{
													messageProcessedOK = ipsc.processMessage(gmh);
													errorMessage = ipsc.getErrorMessage();
												}
												
												if (interfaceType.equals("Pallet Move") == true)
												{
													messageProcessedOK = ipmv.processMessage(gmh);
													errorMessage = ipmv.getErrorMessage();
												}

												if (interfaceType.equals("Batch Status Change") == true)
												{
													messageProcessedOK = bsc.processMessage(gmh);
													errorMessage = bsc.getErrorMessage();
												}

												if (interfaceType.equals("Journey Definition") == true)
												{
													messageProcessedOK = ij.processMessage(gmh);
													errorMessage = ij.getErrorMessage();
												}
												
												if (interfaceType.equals("Process Order Status Change") == true)
												{
													messageProcessedOK = iposc.processMessage(gmh);
													errorMessage = iposc.getErrorMessage();
												}

												if (interfaceType.equals("Production Declaration") == true)
												{
													messageProcessedOK = ipd.processMessage(gmh);
													errorMessage = ipd.getErrorMessage();
												}

												if (interfaceType.equals("QM Inspection Request") == true)
												{
													messageProcessedOK = iireq.processMessage(gmh);
													errorMessage = iireq.getErrorMessage();
												}

												if (interfaceType.equals("QM Inspection Result") == true)
												{
													messageProcessedOK = iirslt.processMessage(gmh);
													errorMessage = iirslt.getErrorMessage();
												}
												
												if (interfaceType.equals("Material Auto Move") == true)
												{
													messageProcessedOK = imam.processMessage(gmh);
													errorMessage = imam.getErrorMessage();
												}

												GenericMessageHeader.updateStats("Input", interfaceType, messageProcessedOK.toString());
												
												logger.debug("Processing " + interfaceType + " finished.");
											}
										}

										logger.debug("		===  RESULT " + messageProcessedOK.toString() + "  ===");

										if (messageProcessedOK)
										{

											il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "DB Update", fromFile);
											reader.deleteFile(backupPath + gmh.getInterfaceType() + File.separator + fromFile);
											reader.move_FileToDirectory(inputPath + fromFile, backupPath + gmh.getInterfaceType(), true);
										} else
										{
											il.write(gmh, GenericMessageHeader.msgStatusError, errorMessage, "DB Update", fromFile);
											if (inter.getInterfaceProperties(gmh.getInterfaceType(), "Input") == true)
											{
												if (inter.getEmailError() == true)
												{
													String emailaddresses = inter.getEmailAddresses();

													StringConverter stringConverter = new StringConverter();
													ArrayConverter arrayConverter = new ArrayConverter(String[].class, stringConverter);
													arrayConverter.setDelimiter(';');
													arrayConverter.setAllowedChars(new char[]
													{ '@', '_' });

													String[] emailList = (String[]) arrayConverter.convert(String[].class, emailaddresses);

													if (emailList.length > 0)
													{
														String siteName = Common.hostList.getHost(getHostID()).getSiteDescription();
														String attachedFilename = Common.base_dir + java.io.File.separator + inputPath + fromFile;
														logger.debug("Attaching file  " + Common.base_dir + java.io.File.separator + inputPath + fromFile);
														mail.postMail(emailList, "Error Processing Incoming " + gmh.getInterfaceType() + " for [" + siteName + "] on " + JUtility.getClientName(), errorMessage, fromFile, attachedFilename);
														com.commander4j.util.JWait.milliSec(2000);
													}
												}

											}
											reader.deleteFile(errorPath + gmh.getInterfaceType() + File.separator + fromFile);
											reader.move_FileToDirectory(inputPath + fromFile, errorPath + gmh.getInterfaceType(), true);
										}
									}
									logger.debug("<---  END OF PROCESSING " + fromFile + "  ---->");
								} catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	private void setHostID(String hostID)
	{
		this.hostID = hostID;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

}
