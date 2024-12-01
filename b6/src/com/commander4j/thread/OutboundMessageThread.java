package com.commander4j.thread;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : OutboundMessageThread.java
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

import java.util.LinkedList;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBUser;
import com.commander4j.messages.GenericMessageHeader;
import com.commander4j.messages.OutgoingDespatchConfirmation;
import com.commander4j.messages.OutgoingDespatchEmail;
import com.commander4j.messages.OutgoingDespatchPreAdvice;
import com.commander4j.messages.OutgoingEquipmentTracking;
import com.commander4j.messages.OutgoingPalletDelete;
import com.commander4j.messages.OutgoingPalletIssue;
import com.commander4j.messages.OutgoingPalletReturn;
import com.commander4j.messages.OutgoingPalletSplit;
import com.commander4j.messages.OutgoingPalletStatusChange;
import com.commander4j.messages.OutgoingProductionDeclarationConfirmation;
import com.commander4j.messages.OutgoingProductionUnConfirm;
import com.commander4j.messages.OutgoingSortNotify;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;
import com.commander4j.util.JWait;

public class OutboundMessageThread extends Thread
{

	public boolean allDone = false;
	private String sessionID;
	private String hostID;
	private String errorMessage = "";
	private Boolean messageProcessedOK = false;
	private JFileIO mover = new JFileIO();
	private String destinationFile = "";
	private String renamedDestinationFile = "";
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(OutboundMessageThread.class);

	public String getHostID() {
		return hostID;
	}

	public void setHostID(String host) {
		hostID = host;
	}

	public OutboundMessageThread(String host)
	{
		super();
		setHostID(host);
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public void run() {
		logger.debug("OutboundMessageThread running");
		setSessionID(JUnique.getUniqueID());
		JDBUser user = new JDBUser(getHostID(), getSessionID());
		user.setUserId("interface");
		user.setPassword("interface");
		user.setLoginPassword("interface");
		Common.userList.addUser(getSessionID(), user);
		Common.sd.setData(getSessionID(), "silentExceptions", "Yes", true);

		Boolean dbconnected = false;

		if (Common.hostList.getHost(hostID).isConnected(sessionID) == false)
		{

			dbconnected = Common.hostList.getHost(hostID).connect(sessionID, hostID);

		}
		else
		{
			dbconnected = true;
		}

		if (dbconnected)
		{

			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
			OutgoingProductionDeclarationConfirmation opdc = new OutgoingProductionDeclarationConfirmation(getHostID(), getSessionID());
			OutgoingProductionUnConfirm opuc = new OutgoingProductionUnConfirm(getHostID(), getSessionID());
			OutgoingDespatchConfirmation odc = new OutgoingDespatchConfirmation(getHostID(), getSessionID());
			OutgoingDespatchPreAdvice opa = new OutgoingDespatchPreAdvice(getHostID(), getSessionID());
			OutgoingDespatchEmail ode = new OutgoingDespatchEmail(getHostID(), getSessionID());
			OutgoingSortNotify osn = new OutgoingSortNotify(getHostID(), getSessionID());
			OutgoingEquipmentTracking oet = new OutgoingEquipmentTracking(getHostID(), getSessionID());
			OutgoingPalletStatusChange psc = new OutgoingPalletStatusChange(getHostID(), getSessionID());
			OutgoingPalletSplit ops = new OutgoingPalletSplit(getHostID(), getSessionID());
			OutgoingPalletDelete opd = new OutgoingPalletDelete(getHostID(), getSessionID());
			OutgoingPalletIssue opi = new OutgoingPalletIssue(getHostID(), getSessionID());
			OutgoingPalletReturn opr = new OutgoingPalletReturn(getHostID(), getSessionID());
			LinkedList<Long> irqList = new LinkedList<Long>();
			int noOfMessages = 0;

			while (true)
			{

				JWait.milliSec(500);

				if (allDone)
				{
					if (dbconnected)
					{
						Common.hostList.getHost(hostID).disconnect(getSessionID());
					}
					return;
				}

				irqList.clear();
				irqList = ir.getInterfaceRequestIDs();
				noOfMessages = irqList.size();

				if (noOfMessages > 0)
				{
					for (int x = 0; x < noOfMessages; x++)
					{
						JWait.milliSec(100);
						ir.setInterfaceRequestID(irqList.get(x));
						ir.getInterfaceRequestProperties();

						if (ir.getMode().equals("Inbound File Re-Submit"))
						{
							if (inter.getInterfaceProperties(ir.getInterfaceType(), "Input") == true)
							{
								String sourceFile = Common.base_dir + java.io.File.separator + "xml" + java.io.File.separator + "interface" + java.io.File.separator + "error" + java.io.File.separator + ir.getInterfaceType() + java.io.File.separator
										+ ir.getFilename();

								destinationFile = inter.getRealPath() + java.io.File.separator + ir.getFilename();
								renamedDestinationFile = inter.getRealPath() + java.io.File.separator + ir.getFilename().replaceAll(".xml", ".lmx");

								mover.move_File(sourceFile, renamedDestinationFile);
								mover.move_File(renamedDestinationFile, destinationFile);
								ir.delete();

							}
						}

						if (ir.getMode().equals("Normal"))
						{

							errorMessage = "Unknown Outbound Interface Type :" + ir.getInterfaceType();
							messageProcessedOK = false;

							if (ir.getInterfaceType().equals("Production Declaration"))
							{
								messageProcessedOK = opdc.processMessage(ir.getTransactionRef());
								errorMessage = opdc.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Production Declaration", messageProcessedOK.toString());
							}
							
							if (ir.getInterfaceType().equals("Production Unconfirm"))
							{
								messageProcessedOK = opuc.processMessage(ir.getTransactionRef());
								errorMessage = opuc.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Production Unconfirm", messageProcessedOK.toString());
							}

							if (ir.getInterfaceType().equals("Pallet Status Change"))
							{
								messageProcessedOK = psc.processMessage(ir.getTransactionRef());
								errorMessage = psc.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Pallet Status Change", messageProcessedOK.toString());
							}							

							if (ir.getInterfaceType().equals("Pallet Split"))
							{
								messageProcessedOK = ops.processMessage(ir.getTransactionRef());
								errorMessage = ops.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Pallet Split", messageProcessedOK.toString());
							}	
							
							if (ir.getInterfaceType().equals("Pallet Delete"))
							{
								messageProcessedOK = opd.processMessage(ir.getTransactionRef());
								errorMessage = opd.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Pallet Delete", messageProcessedOK.toString());
							}	
							
							if (ir.getInterfaceType().equals("Despatch Confirmation"))
							{
								messageProcessedOK = odc.processMessage(ir.getTransactionRef());
								errorMessage = odc.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Despatch Confirmation", messageProcessedOK.toString());
							}

							if (ir.getInterfaceType().equals("Despatch Pre Advice"))
							{
								messageProcessedOK = opa.processMessage(ir.getTransactionRef());
								errorMessage = opa.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Despatch Pre Advice", messageProcessedOK.toString());
							}
							
							if (ir.getInterfaceType().equals("Despatch Email"))
							{
								messageProcessedOK = ode.processMessage(ir.getTransactionRef());
								errorMessage = ode.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Despatch Email", messageProcessedOK.toString());
							}
							
							if (ir.getInterfaceType().equals("Sort Notification"))
							{
								messageProcessedOK = osn.processMessage(ir.getTransactionRef());
								errorMessage = osn.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Sort Notification", messageProcessedOK.toString());
							}

							if (ir.getInterfaceType().equals("Equipment Tracking"))
							{
								messageProcessedOK = oet.processMessage(ir.getTransactionRef());
								errorMessage = oet.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Equipment Tracking", messageProcessedOK.toString());
							}
							
							if (ir.getInterfaceType().equals("Pallet Issue"))
							{
								messageProcessedOK = opi.processMessage(ir.getTransactionRef());
								errorMessage = opi.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Pallet Issue", messageProcessedOK.toString());
							}
							
							if (ir.getInterfaceType().equals("Pallet Return"))
							{
								messageProcessedOK = opr.processMessage(ir.getTransactionRef());
								errorMessage = opr.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Pallet Return", messageProcessedOK.toString());
							}
							
							if (messageProcessedOK == true)
							{
								ir.delete();
							}
							else
							{
								ir.update(irqList.get(x), "Error");
								if (inter.getInterfaceProperties(ir.getInterfaceType(), "Output") == true)
								{
									if (inter.getEmailError() == true)
									{
										String emailaddresses = inter.getEmailAddresses();

										StringConverter stringConverter = new StringConverter();
										ArrayConverter arrayConverter = new ArrayConverter(String[].class, stringConverter);
										arrayConverter.setDelimiter(';');
										arrayConverter.setAllowedChars(new char[] { '@' });

										String[] emailList = (String[]) arrayConverter.convert(String[].class, emailaddresses);

										if (emailList.length > 0)
										{
												String siteName = Common.hostList.getHost(getHostID()).getSiteDescription();

												Common.sendmail.Send(emailList, "Error Processing Outgoing " + ir.getInterfaceType()+" for ["+siteName+"] on "+JUtility.getClientName(), errorMessage,  "");

										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
