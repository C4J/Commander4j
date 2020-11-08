package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : OutgoingPalletStatusChange.java
 * 
 * Package Name : com.commander4j.messages
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

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBMHN;
import com.commander4j.db.JDBMHNReasons;
import com.commander4j.db.JDBPalletHistory;
import com.commander4j.email.JeMailOutGoingMessage;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

public class OutgoingPalletStatusChange
{
	private String hostID;
	private String sessionID;
	final Logger logger = Logger.getLogger(OutgoingPalletStatusChange.class);
	private JeMailOutGoingMessage ogm;

	private String errorMessage;
	private JFileIO fio = new JFileIO();

	public OutgoingPalletStatusChange(String host, String session) {
		setHostID(host);
		setSessionID(session);
	}

	public Element addElement(Document doc, String name, String value)
	{
		Element temp = (Element) doc.createElement(name);
		Text temp_value = doc.createTextNode(value);
		temp.appendChild(temp_value);
		return temp;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public String getHostID()
	{
		return hostID;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public Boolean processMessage(Long transactionRef)

	{
		Boolean result = false;
		String path = "";
		JDBInterfaceLog il = new JDBInterfaceLog(getHostID(), getSessionID());
		GenericMessageHeader gmh = new GenericMessageHeader();
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());

		inter.getInterfaceProperties("Pallet Status Change", "Output");
		String device = inter.getDevice();

		JDBPalletHistory palhistfrom = new JDBPalletHistory(getHostID(), getSessionID());
		JDBPalletHistory palhistto = new JDBPalletHistory(getHostID(), getSessionID());
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		
		String origin = ctrl.getKeyValue("DEFAULT_LOCATION");
		JDBMHN mhn = new JDBMHN(getHostID(), getSessionID());
		JDBMHNReasons mhnReasons = new JDBMHNReasons(getHostID(), getSessionID());

		ResultSet rsFrom = palhistfrom.getInterfacingData(transactionRef, "STATUS CHANGE", "FROM", Long.valueOf(1), "SSCC", "asc");

		ResultSet rsTo = palhistfrom.getInterfacingData(transactionRef, "STATUS CHANGE", "TO", Long.valueOf(1), "SSCC", "asc");

		try
		{
			if (rsFrom.next())
			{
				if (rsTo.next())
				{
					palhistfrom.getPropertiesfromResultSet(rsFrom);
					palhistto.getPropertiesfromResultSet(rsTo);

					String mhnNumber = palhistto.getPallet().getMHNNumber();
					String initiator = "";
					String recorder = "";
					String authorisor = "";
					String decision = "";
					String reason1 = "";
					String reason2 = "";
					String reason3 = "";
					String dateCreated = "";
					String dateExpected = "";
					String dateResolved = "";
					String comment = "";
					String status = "";
					String locn = "";
					if (palhistto.getPallet().getMHNNumber().equals("") == false)
					{
						if (mhn.getMHNProperties(palhistto.getPallet().getMHNNumber()))
						{
							initiator = mhn.getInitiator();
							recorder = mhn.getRecorder();
							authorisor = mhn.getAuthorisor();
							reason1 = mhn.getReason1();
							reason2 = mhn.getReason2();
							reason3 = mhn.getReason3();
							dateCreated = JUtility.getISOTimeStampStringFormat(mhn.getDateCreated());
							if (dateCreated.equals("Error"))
								dateCreated = "";
							dateExpected = JUtility.getISOTimeStampStringFormat(mhn.getDateExpected());
							if (dateExpected.equals("Error"))
								dateExpected = "";
							dateResolved = JUtility.getISOTimeStampStringFormat(mhn.getDateResolved());
							if (dateResolved.equals("Error"))
								dateResolved = "";
							comment = mhn.getComments();
							status = mhn.getStatus();
						}
					}

					try
					{
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						DocumentBuilder builder = factory.newDocumentBuilder();

						Document document = builder.newDocument();

						Element message = (Element) document.createElement("message");

						Element hostUniqueID = addElement(document, "hostRef", Common.hostList.getHost(getHostID()).getUniqueID());
						message.appendChild(hostUniqueID);

						Element messageRef = addElement(document, "messageRef", String.valueOf(transactionRef));
						message.appendChild(messageRef);

						Element messageType = addElement(document, "interfaceType", "Pallet Status Change");
						message.appendChild(messageType);

						Element messageInformation = addElement(document, "messageInformation", "SSCC=" + palhistto.getPallet().getSSCC());
						message.appendChild(messageInformation);

						Element messageDirection = addElement(document, "interfaceDirection", "Output");
						message.appendChild(messageDirection);

						Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
						message.appendChild(messageDate);

						Element palletStatusChange = (Element) document.createElement("palletStatusChange");

						Element sscc = addElement(document, "SSCC", palhistto.getPallet().getSSCC());
						palletStatusChange.appendChild(sscc);
						
						Element material = addElement(document, "material", palhistto.getPallet().getMaterial());
						palletStatusChange.appendChild(material);

						Element batch = addElement(document, "batch", palhistto.getPallet().getBatchNumber());
						palletStatusChange.appendChild(batch);

						Element source = addElement(document,"origin",origin);
						palletStatusChange.appendChild(source);
						
						locn = palhistfrom.getPallet().getLocationID();
						Element location = addElement(document, "location", palhistto.getPallet().getLocationID());
						palletStatusChange.appendChild(location);
						
						Element quantity = addElement(document, "quantity", palhistto.getPallet().getQuantity().toString());
						palletStatusChange.appendChild(quantity);
						
						Element uom = addElement(document, "uom", palhistto.getPallet().getUom().toString());
						palletStatusChange.appendChild(uom);						
						
						Element despNo = addElement(document, "despatchNumber", palhistto.getPallet().getDespatchNo());
						palletStatusChange.appendChild(despNo);	

						Element statusFrom = addElement(document, "statusFrom", palhistfrom.getPallet().getStatus());
						palletStatusChange.appendChild(statusFrom);

						Element statusTo = addElement(document, "statusTo", palhistto.getPallet().getStatus());
						palletStatusChange.appendChild(statusTo);
						
						decision =  palhistto.getPallet().getDecision();

						Element masterHoldNoticeData = (Element) document.createElement("masterHoldNoticeData");

						Element mhnNo = addElement(document, "MHN", mhnNumber);
						masterHoldNoticeData.appendChild(mhnNo);

						Element mhnRecorder = addElement(document, "recorder", recorder);
						masterHoldNoticeData.appendChild(mhnRecorder);

						Element mhnInitiator = addElement(document, "initiator", initiator);
						masterHoldNoticeData.appendChild(mhnInitiator);

						Element mhnAuthorisor = addElement(document, "authorisor", authorisor);
						masterHoldNoticeData.appendChild(mhnAuthorisor);
						
						Element mhnDecision = addElement(document, "decision", decision);
						masterHoldNoticeData.appendChild(mhnDecision);

						Element masterHoldReasons = (Element) document.createElement("reasons");

						if (reason1.equals("") == false)
						{
							Element masterHoldReason = (Element) document.createElement("reason");
							Element mhnReason1 = addElement(document, "code", reason1);
							masterHoldReason.appendChild(mhnReason1);
							
							mhnReasons.getReasonProperties(reason1);
							String reason1desc = mhnReasons.getDescription();
							Element mhnReason1desc = addElement(document, "description", reason1desc);
							masterHoldReason.appendChild(mhnReason1desc);
							masterHoldReasons.appendChild(masterHoldReason);
						}

						if (reason2.equals("") == false)
						{
							Element masterHoldReason = (Element) document.createElement("reason");
							Element mhnReason2 = addElement(document, "code", reason2);
							masterHoldReason.appendChild(mhnReason2);
							
							mhnReasons.getReasonProperties(reason2);
							String reason2desc = mhnReasons.getDescription();
							Element mhnReason2desc = addElement(document, "description", reason2desc);
							masterHoldReason.appendChild(mhnReason2desc);
							masterHoldReasons.appendChild(masterHoldReason);
						}

						if (reason3.equals("") == false)
						{
							Element masterHoldReason = (Element) document.createElement("reason");
							Element mhnReason3 = addElement(document, "code", reason3);
							masterHoldReason.appendChild(mhnReason3);
							
							mhnReasons.getReasonProperties(reason3);
							String reason3desc = mhnReasons.getDescription();
							Element mhnReason3desc = addElement(document, "description", reason3desc);
							masterHoldReason.appendChild(mhnReason3desc);
							masterHoldReasons.appendChild(masterHoldReason);
						}

						masterHoldNoticeData.appendChild(masterHoldReasons);

						Element mhnCreated = addElement(document, "created", dateCreated);
						masterHoldNoticeData.appendChild(mhnCreated);

						Element mhnExpected = addElement(document, "expected", dateExpected);
						masterHoldNoticeData.appendChild(mhnExpected);

						Element mhnResolved = addElement(document, "resolved", dateResolved);
						masterHoldNoticeData.appendChild(mhnResolved);

						Element mhnComments = addElement(document, "comments", comment);
						masterHoldNoticeData.appendChild(mhnComments);

						Element mhnStatus = addElement(document, "status", status);
						masterHoldNoticeData.appendChild(mhnStatus);

						palletStatusChange.appendChild(masterHoldNoticeData);

						Element messageData = (Element) document.createElement("messageData");
						messageData.appendChild(palletStatusChange);

						message.appendChild(messageData);

						document.appendChild(message);

						JXMLDocument xmld = new JXMLDocument();
						xmld.setDocument(document);
						gmh.decodeHeader(xmld);

						if (device.equals("Disk") | device.equals("Email"))
						{

							path = inter.getRealPath();
							if (fio.writeToDisk(path, document, transactionRef, "_"+locn.replace(" ", "_")+"_PalletStatusChange.xml") == true)
							{
								result = true;
								il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", fio.getFilename());
								setErrorMessage("");

								if (device.equals("Email"))
								{
									ogm = new JeMailOutGoingMessage(inter, transactionRef, fio);
									ogm.sendEmail();
								}
							}
							else
							{
								result = false;
								il.write(gmh, GenericMessageHeader.msgStatusError, fio.getErrorMessage(), "File Write", fio.getFilename());
								setErrorMessage(fio.getErrorMessage());
							}
						}

					}

					catch (Exception ex)
					{
						logger.error("Error sending message. " + ex.getMessage());
						ex.printStackTrace();

					}
				}
				else
				{
					logger.debug("Could not find TO Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef));
				}
				rsTo.close();
			}
			else
			{
				logger.debug("Could not find FROM Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef));
			}
			rsFrom.close();
		}
		catch (SQLException e)
		{
			logger.debug("Error finding Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef) + " " + e.getMessage());
		}

		return result;
	}

	private void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public void setHostID(String host)
	{
		hostID = host;
	}

	public void setSessionID(String session)
	{
		sessionID = session;
	}

	public void submit(long dbTransactionRef)
	{
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		inter.getInterfaceProperties("Pallet Status Change", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(dbTransactionRef, "Pallet Status Change");
		}
		else
		{
			logger.debug("Interface Pallet Status Change - Output is DISABLED");
		}

	}

}
