package com.commander4j.thread;

import java.util.LinkedList;

import javax.mail.MessagingException;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.log4j.Logger;

import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBUser;
import com.commander4j.email.JeMail;
import com.commander4j.messages.GenericMessageHeader;
import com.commander4j.messages.OutgoingDespatchConfirmation;
import com.commander4j.messages.OutgoingDespatchPreAdvice;
import com.commander4j.messages.OutgoingEquipmentTracking;
import com.commander4j.messages.OutgoingLabelData;
import com.commander4j.messages.OutgoingPalletDelete;
import com.commander4j.messages.OutgoingPalletSplit;
import com.commander4j.messages.OutgoingPalletStatusChange;
import com.commander4j.messages.OutgoingProductionDeclarationConfirmation;
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
	private final Logger logger = Logger.getLogger(OutboundMessageThread.class);

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

			JeMail mail = new JeMail(getHostID(), getSessionID());
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
			OutgoingProductionDeclarationConfirmation opdc = new OutgoingProductionDeclarationConfirmation(getHostID(), getSessionID());
			OutgoingDespatchConfirmation odc = new OutgoingDespatchConfirmation(getHostID(), getSessionID());
			OutgoingDespatchPreAdvice opa = new OutgoingDespatchPreAdvice(getHostID(), getSessionID());
			OutgoingEquipmentTracking oet = new OutgoingEquipmentTracking(getHostID(), getSessionID());
			OutgoingPalletStatusChange psc = new OutgoingPalletStatusChange(getHostID(), getSessionID());
			OutgoingPalletSplit ops = new OutgoingPalletSplit(getHostID(), getSessionID());
			OutgoingPalletDelete opd = new OutgoingPalletDelete(getHostID(), getSessionID());
			OutgoingLabelData old = new OutgoingLabelData(getHostID(), getSessionID());
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

							if (ir.getInterfaceType().equals("Equipment Tracking"))
							{
								messageProcessedOK = oet.processMessage(ir.getTransactionRef());
								errorMessage = oet.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Equipment Tracking", messageProcessedOK.toString());
							}
							
							if (ir.getInterfaceType().equals("Label Data"))
							{
								messageProcessedOK = old.processMessage(ir.getUniqueID());
								errorMessage = oet.getErrorMessage();
								GenericMessageHeader.updateStats("Output","Label Data", messageProcessedOK.toString());
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
											try
											{
												String siteName = Common.hostList.getHost(getHostID()).getSiteDescription();
												mail.postMail(emailList, "Error Processing Outgoing " + ir.getInterfaceType()+" for ["+siteName+"] on "+JUtility.getClientName(), errorMessage, "", "");
											}
											catch (MessagingException e)
											{

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
}
