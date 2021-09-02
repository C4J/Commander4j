package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : OutgoingEquipmentTracking.java
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

import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBDespatchEquipmentTypes;
import com.commander4j.db.JDBEquipmentList;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.email.JeMailOutGoingMessage;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

public class OutgoingEquipmentTracking
{
	private String hostID;
	private String sessionID;
	final Logger logger = Logger.getLogger(OutgoingEquipmentTracking.class);
	private String errorMessage;
	private JFileIO fio = new JFileIO();
	private JeMailOutGoingMessage ogm;

	public String getErrorMessage()
	{
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public OutgoingEquipmentTracking(String host, String session) {
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

		inter.getInterfaceProperties("Equipment Tracking", "Output");
		String device = inter.getDevice();

		JDBDespatch desp = new JDBDespatch(getHostID(), getSessionID());
		JDBDespatchEquipmentTypes despequip = new JDBDespatchEquipmentTypes(getHostID(), getSessionID());
		desp.setTransactionRef(transactionRef);

		Boolean suppressMessage = false;
		if (desp.getDespatchPropertiesFromTransactionRef() == true)
		{

			String sourceEquipmentLocnEquipTrackingID = JUtility.replaceNullStringwithBlank(desp.getLocationDBFrom().getEquipmentTrackingID());
			String destinationEquipmentLocnEquipTrackingID = JUtility.replaceNullStringwithBlank(desp.getLocationDBTo().getEquipmentTrackingID());


			gmh.setMessageRef(desp.getTransactionRef().toString());
			gmh.setInterfaceType(inter.getInterfaceType());
			gmh.setMessageInformation("Despatch=" + desp.getDespatchNo());
			gmh.setInterfaceDirection(inter.getInterfaceDirection());
			gmh.setMessageDate(JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));

			if (desp.getDespatchPalletCount()==0)
			{
				setErrorMessage("Message Suppressed - 0 pallets assigned to despatch ["+desp.getDespatchNo()+"]");
				suppressMessage = true;			
			}
			
			if (sourceEquipmentLocnEquipTrackingID.length() == 0)
			{
				setErrorMessage("Message Suppressed - No Equipment Destination (From) for Location [" + desp.getLocationIDFrom() + "] despatch ["+desp.getDespatchNo()+"]");
				suppressMessage = true;
			}

			if (destinationEquipmentLocnEquipTrackingID.length() == 0)
			{
				setErrorMessage("Message Suppressed - No Equipment Destination (To) for Location [" + desp.getLocationIDTo() + "] despatch ["+desp.getDespatchNo()+"]");
				suppressMessage = true;
			}
		}
		else
		{
			setErrorMessage("Message Suppressed - Unable to find Despatch with Transaction Ref [" + String.valueOf(transactionRef) + "] despatch ["+desp.getDespatchNo()+"]");
			suppressMessage = true;

		}

		if (suppressMessage == true)
		{
			result = false;
			il.write(gmh, GenericMessageHeader.msgStatusWarning, getErrorMessage(), "File Write", "");

		}
		else
		{
			if (inter.getFormat().equals("XML"))
			{

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

					Element messageType = addElement(document, "interfaceType", "Equipment Tracking");
					message.appendChild(messageType);

					Element messageInformation = addElement(document, "messageInformation", "Despatch=" + desp.getDespatchNo());
					message.appendChild(messageInformation);

					Element messageDirection = addElement(document, "interfaceDirection", "Output");
					message.appendChild(messageDirection);

					Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
					message.appendChild(messageDate);

					Element equipmentTracking = (Element) document.createElement("equipmentTracking");

					Element noofpallets = addElement(document, "numberOfPallets", String.valueOf(desp.getTotalPallets()));
					equipmentTracking.appendChild(noofpallets);

					Element haulier = addElement(document, "haulier", String.valueOf(JUtility.replaceNullStringwithBlank(desp.getHaulier())));
					equipmentTracking.appendChild(haulier);

					Element trailer = addElement(document, "trailer", String.valueOf(JUtility.replaceNullStringwithBlank(desp.getTrailer())));
					equipmentTracking.appendChild(trailer);

					Element despatch = addElement(document, "despatchNo", desp.getDespatchNo());
					equipmentTracking.appendChild(despatch);

					Element despatchDate = addElement(document, "despatchDate", JUtility.getISOTimeStampStringFormat(desp.getDespatchDate()));
					equipmentTracking.appendChild(despatchDate);

					Element locationFrom = addElement(document, "fromLocation", desp.getLocationIDFrom());
					equipmentTracking.appendChild(locationFrom);

					Element locationFromETID = addElement(document, "fromEquipmentTrackingID", desp.getLocationDBFrom().getEquipmentTrackingID());
					equipmentTracking.appendChild(locationFromETID);
					
					Element locationFromETIDGLN = addElement(document, "fromEquipmentLocnGLN", desp.getLocationDBFrom().getGLN());
					equipmentTracking.appendChild(locationFromETIDGLN);

					Element locationTo = addElement(document, "toLocation", desp.getLocationIDTo());
					equipmentTracking.appendChild(locationTo);

					Element locationToETID = addElement(document, "toEquipmentTrackingID", desp.getLocationDBTo().getEquipmentTrackingID());
					equipmentTracking.appendChild(locationToETID);
					
					Element locationToETIDGLN = addElement(document, "toEquipmentLocnGLN", desp.getLocationDBTo().getGLN());
					equipmentTracking.appendChild(locationToETIDGLN);

					Element contents = (Element) document.createElement("contents");
					equipmentTracking.appendChild(contents);

					LinkedList<JDBEquipmentList> equipList = new LinkedList<JDBEquipmentList>();
					LinkedList<JDBEquipmentList> equipListEmpty = new LinkedList<JDBEquipmentList>();
					
					//Get list of regular equipment assigned to pallets with product on them
					equipList = desp.getEquipment();
					
					//Get a list of emply equipment (no product just pallets)
					despequip.setDespatchNo(desp.getDespatchNo());
					equipListEmpty = despequip.getEquipment() ;
					
					//Loop through the empty pallet list
					for (int x = 0;x < equipListEmpty.size();x++)
					{
						String findType = equipListEmpty.get(x).getEquipmentID();
						int countType  = equipListEmpty.get(x).getCount();
						
						boolean foundType = false;
						
						//Check if empty pallet type is present in the regular (full pallet type list)
						
						for (int y = 0; y < equipList.size(); y++)
						{
							if (equipList.get(y).getEquipmentID().equals(findType))
							{
								//If in both lists add the quantity of empty pallets into the quantity for normal pallets
								foundType = true;
								equipList.get(y).setCount(equipList.get(y).getCount()+countType);
							}
						}
						
						if (foundType==false)
						{
							//if Empty pallet type does not exist in regular pallet type list then add the empty pallet type into the regular list.
							equipList.add(equipListEmpty.get(x));
						}
					}

					//Write the pallet types and quantities to the xml file
					for (int x = 0; x < equipList.size(); x++)
					{
						Element equipment = (Element) document.createElement("Equipment");

						Element equipmentID = addElement(document, "equipmentID", equipList.get(x).getEquipmentID());
						equipment.appendChild(equipmentID);

						Element count = addElement(document, "count", String.valueOf(equipList.get(x).getCount()));
						equipment.appendChild(count);

						contents.appendChild(equipment);

						if (equipList.get(x).getEquipmentID().equals(""))
						{
							setErrorMessage("Message Suppressed - No Equipment Type defined for one or more Materials in Despatch [" + desp.getDespatchNo() + "]");
							suppressMessage = true;
						}
					}

					Element messageData = (Element) document.createElement("messageData");
					messageData.appendChild(equipmentTracking);

					message.appendChild(messageData);

					document.appendChild(message);

					JXMLDocument xmld = new JXMLDocument();
					xmld.setDocument(document);
					gmh.decodeHeader(xmld);

					if (suppressMessage == true)
					{
						result = false;
						il.write(gmh, GenericMessageHeader.msgStatusWarning, getErrorMessage(), "File Write", "");
					}
					else
					{
						if (device.equals("Disk") | device.equals("Email"))
						{
							path = inter.getRealPath();
							if (fio.writeToDisk(path, document, transactionRef, "_"+desp.getLocationIDTo().replace(" ", "_")+"_EquipmentTracking.xml") == true)
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
				}

				catch (Exception ex)
				{
					logger.error("Error sending message. " + ex.getMessage());
					ex.printStackTrace();

				}
			}
		}

		return result;
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
		inter.getInterfaceProperties("Equipment Tracking", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(dbTransactionRef, "Equipment Tracking");
		}
		else
		{
			logger.debug("Interface Equipment Tracking - Output is DISABLED");
		}
	}

}
