package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : OutgoingDespatchConfirmation.java
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
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBPalletHistory;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBUom;
import com.commander4j.email.OutGoingMessage;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;


public class OutgoingDespatchConfirmation
{
	private String hostID;
	private String sessionID;
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(OutgoingDespatchConfirmation.class);
	private String errorMessage;
	private JFileIO fio = new JFileIO();
	private OutGoingMessage ogm;

	public String getErrorMessage() {
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public OutgoingDespatchConfirmation(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public Element addElement(Document doc, String name, String value) {
		Element temp = (Element) doc.createElement(name);
		Text temp_value = doc.createTextNode(value);
		temp.appendChild(temp_value);
		return temp;
	}

	public String getHostID() {
		return hostID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public Boolean processMessage(Long transactionRef) {
		Boolean result = false;
		String path = "";
		String defaultBatchStatus = "";

		JDBInterfaceLog il = new JDBInterfaceLog(getHostID(), getSessionID());
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		GenericMessageHeader gmh = new GenericMessageHeader();
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		JDBUom uoml = new JDBUom(getHostID(), getSessionID());
		JDBProcessOrder order = new JDBProcessOrder(getHostID(), getSessionID());
		JDBCustomer cust = new JDBCustomer(getHostID(), getSessionID());
		
		String batchDateMode = ctrl.getKeyValue("EXPIRY DATE MODE");
		
		inter.getInterfaceProperties("Despatch Confirmation", "Output");
		String device = inter.getDevice();
		
		JDBDespatch desp = new JDBDespatch(getHostID(), getSessionID());
		desp.setTransactionRef(transactionRef);
		desp.getDespatchPropertiesFromTransactionRef();

		String sourceGLN = JUtility.replaceNullStringwithBlank(desp.getLocationDBFrom().getGLN());
		String destinationGLN = JUtility.replaceNullStringwithBlank(desp.getLocationDBTo().getGLN());
		Boolean suppressMessage = false;

		gmh.setMessageRef(desp.getTransactionRef().toString());
		gmh.setInterfaceType(inter.getInterfaceType());
		gmh.setMessageInformation("Despatch=" + desp.getDespatchNo());
		gmh.setInterfaceDirection(inter.getInterfaceDirection());
		gmh.setMessageDate(JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
		
		String noJourneyPrefix = ctrl.getKeyValueWithDefault("NO JOURNEY PREFIX", "NJ_", "Prefix for No Journey Messages");
		
		if (desp.getDespatchPalletCount()==0)
		{
			setErrorMessage("Message Suppressed - 0 pallets assigned to despatch");
			suppressMessage = true;			
		}

		if (sourceGLN.length() == 0)
		{
			setErrorMessage("Message Suppressed - No GLN Source (From) GLN for Location [" + desp.getLocationIDFrom() + "]");
			suppressMessage = true;
		}

		if (destinationGLN.length() == 0)
		{
			setErrorMessage("Message Suppressed - No GLN Destination (To) GLN for Location [" + desp.getLocationIDTo() + "]");
			suppressMessage = true;
		}

		if (suppressMessage == true)
		{
			result = true;
			il.write(gmh, GenericMessageHeader.msgStatusWarning, getErrorMessage(), "File Write", "");

		}
		else
		{
			if (inter.getFormat().equals("EANCOM"))
			{

				if (sourceGLN.endsWith(""))
				{

				}

				int segments = 0;
				int optional = 0;
				String document = "";
				String despdateShort = new java.text.SimpleDateFormat("yyMMdd:HHmm").format(desp.getDespatchDate());
				String despdateLong = new java.text.SimpleDateFormat("yyyyMMddHHmm").format(desp.getDespatchDate());

				document = document + "UNA:+.? 'UNB+UNOA:3+" + desp.getLocationDBFrom().getGLN() + ":14+" + desp.getLocationDBTo().getGLN() + ":14+";
				document = document + despdateShort + "+" + desp.getDespatchNo() + "'";
				document = document + "UNH+" + desp.getDespatchNo() + "+DESADV:D:96A:EN:EAN005'";
				document = document + "BGM+351+" + desp.getDespatchNo() + "+9'";
				document = document + "DTM+11:" + despdateLong + ":203'";
				document = document + "RFF+LO:" + desp.getDespatchNo() + "'";
				document = document + "RFF+ZCO:'";
				
				if (desp.getLocationDBTo().getMsgJourneyRef().equals("Y"))
				{
					if (desp.getJourneyRef().equals("NO JOURNEY"))
					{
						document = document + "RFF+SRN:" + noJourneyPrefix+desp.getDespatchNo() + "'";
					}
					else
					{
						document = document + "RFF+SRN:" + desp.getJourneyRef() + "'";
					}
					optional++;
				}
				
				document = document + "RFF+ZAF:'";
				document = document + "RFF+ZPI:1'";
				document = document + "RFF+ZCH:'";
				document = document + "NAD+SF+" + desp.getLocationDBFrom().getGLN() + "::9'";

				if (desp.getLocationDBFrom().getStorageLocation().equals("") == false)
				{
					if (desp.getLocationDBTo().getStorageLocation().equals("") == false)
					{
						document = document + "LOC+198+" + desp.getLocationDBFrom().getStorageLocation() + "::91'";
						optional++;
					}
				}

				document = document + "NAD+ST+" + desp.getLocationDBTo().getGLN() + "::9'";

				if (desp.getLocationDBFrom().getStorageLocation().equals("") == false)
				{
					if (desp.getLocationDBTo().getStorageLocation().equals("") == false)
					{
						document = document + "LOC+195+" + desp.getLocationDBTo().getStorageLocation() + "::91'";
						optional++;
					}
				}

				document = document + "TDT+20++30+31+::9:" + JUtility.stripEANCOMSpecialCharacters(JUtility.replaceNullStringwithBlank(desp.getHaulier())) + "+++:::" + JUtility.stripEANCOMSpecialCharacters(JUtility.replaceNullStringwithBlank(desp.getTrailer())) + "'";
	
				if (desp.getLoadNo().equals(""))
				{
					desp.setLoadNo("123");
				}
					    
				document = document + "EQD+CN+"+JUtility.replaceNullStringwithBlank(StringUtils.left(desp.getTrailer(), 10))+"'";
	    
				// NEXT 2 LINES COMMENTS NEED TO BE RESTORED FOR SAP EWM
				
				// Next line needs commenting pre SAP EWM
			    document = document + "SEL+"+JUtility.replaceNullStringwithBlank(StringUtils.left(desp.getLoadNo(), 10))+"+CA'";
			    document = document + "SEL+"+desp.getDespatchNo()+"+CU'";

			    // Next line needs amending pre SAP EWM
				segments = 14 + optional;
				//segments = 13 + optional;

				JDBPalletHistory palhist = new JDBPalletHistory(getHostID(), getSessionID());
				ResultSet rs = palhist.getInterfacingData(transactionRef, "DESPATCH", "TO", Long.valueOf(0), "SSCC", "asc");

				int x = 1;
				try
				{
					rs.beforeFirst();
					while (rs.next())
					{
						palhist.getPropertiesfromResultSet(rs);

						document = document + "CPS+" + JUtility.padString(String.valueOf(x).trim(), false, 4, "0") + "'";
						document = document + "PAC+1++202'";
						document = document + "PCI+33E'";
						document = document + "GIN+BJ+" + palhist.getPallet().getSSCC() + "'";
						document = document + "LIN+1++" + palhist.getPallet().getEAN() + ":EN'";
						document = document + "PIA+1+" + palhist.getPallet().getVariant() + ":PV+" + palhist.getPallet().getMaterial() + ":SA'";

						NumberFormat formatter = new DecimalFormat("#.#");
						String outqty = formatter.format(palhist.getPallet().getQuantity()); // -1234.567000

						document = document + "QTY+12:" + outqty + ":" + palhist.getPallet().getUom() + "'";
						document = document + "DLM+++0::9'";

//						String batchExpiryLong = new java.text.SimpleDateFormat("yyyyMMdd").format(palhist.getPallet().getMaterialBatchExpiryDate());
						
						String batchExpiryLong  = "";
						if (batchDateMode.equals("BATCH"))
						{				
									batchExpiryLong = new java.text.SimpleDateFormat("yyyyMMdd").format(palhist.getPallet().getMaterialBatchExpiryDate());
						}
						
						if (batchDateMode.equals("SSCC"))
						{
									batchExpiryLong = new java.text.SimpleDateFormat("yyyyMMdd").format(palhist.getPallet().getBatchExpiry());
						}	
						
						
						String dateOfManufactureLong = new java.text.SimpleDateFormat("yyyyMMdd").format(palhist.getPallet().getDateOfManufacture());

						document = document + "DTM+361:" + batchExpiryLong + ":102'";
						document = document + "DTM+94:" + dateOfManufactureLong + ":102'";
						document = document + "RFF+AAJ:" + palhist.getPallet().getDespatchNo() + ":1'";
						
						defaultBatchStatus = palhist.getPallet().getMaterialBatchStatus();
						
						if (defaultBatchStatus.equals("Unrestricted"))
						{
							document = document + "RFF+ZBR:'";
						}
						else
						{
							document = document + "RFF+ZBR:B'";
						}

						document = document + "RFF+ZRB:'";
						document = document + "RFF+ZSR:'";
						document = document + "RFF+ZRC:'";
						document = document + "RFF+ZRT:'";
						document = document + "PCI+36E'";
						document = document + "GIN+BX+" + palhist.getPallet().getBatchNumber() + "'";

						segments = segments + 18;

						x++;
					}
					rs.close();

					segments = segments + 1;

					document = document + "UNT+" + String.valueOf(segments).trim() + "+" + desp.getDespatchNo() + "'";
					document = document + "UNZ+1+" + desp.getDespatchNo() + "'";

					if (device.equals("Disk") | device.equals("Email"))
					{
						path = inter.getRealPath();
						if (fio.writeToDisk(path, document, transactionRef, "_"+desp.getLocationIDTo().replace(" ", "_")+"_DespatchConfirmation.txt") == true)
						{
							result = true;
							il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", fio.getFilename());
							setErrorMessage("");
							if (device.equals("Email"))
							{
								ogm = new OutGoingMessage(inter,transactionRef,fio);
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
				catch (SQLException e)
				{

				}

			}

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

					Element messageType = addElement(document, "interfaceType", "Despatch Confirmation");
					message.appendChild(messageType);

					Element messageInformation = addElement(document, "messageInformation", "Despatch=" + desp.getDespatchNo());
					message.appendChild(messageInformation);

					Element messageDirection = addElement(document, "interfaceDirection", "Output");
					message.appendChild(messageDirection);

					Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
					message.appendChild(messageDate);

					Element despatchConfirmation = (Element) document.createElement("despatchConfirmation");

					Element noofpallets = addElement(document, "numberOfPallets", String.valueOf(desp.getTotalPallets()));
					despatchConfirmation.appendChild(noofpallets);

					Element haulier = addElement(document, "haulier", String.valueOf(JUtility.replaceNullStringwithBlank(desp.getHaulier())));
					despatchConfirmation.appendChild(haulier);

					Element trailer = addElement(document, "trailer", String.valueOf(JUtility.replaceNullStringwithBlank(desp.getTrailer())));
					despatchConfirmation.appendChild(trailer);

					Element load = addElement(document, "loadNo", String.valueOf(JUtility.replaceNullStringwithBlank(desp.getLoadNo())));
					despatchConfirmation.appendChild(load);
					
					Element journey = addElement(document, "journeyRef", String.valueOf(JUtility.replaceNullStringwithBlank(desp.getJourneyRef())));
					despatchConfirmation.appendChild(journey);
					
					Element despatch = addElement(document, "despatchNo", desp.getDespatchNo());
					despatchConfirmation.appendChild(despatch);

					Element despatchDate = addElement(document, "despatchDate", JUtility.getISOTimeStampStringFormat(desp.getDespatchDate()));
					despatchConfirmation.appendChild(despatchDate);

					Element locationFrom = addElement(document, "fromLocation", desp.getLocationIDFrom());
					despatchConfirmation.appendChild(locationFrom);

					Element locationFromPlant = addElement(document, "fromPlant", desp.getLocationDBFrom().getPlant());
					despatchConfirmation.appendChild(locationFromPlant);
					
					Element locationFromWarehouse = addElement(document, "fromWarehouse", desp.getLocationDBFrom().getWarehouse());
					despatchConfirmation.appendChild(locationFromWarehouse);
					
					Element locationFromStorageSection = addElement(document, "fromStorageSection", desp.getLocationDBFrom().getStorageSection());
					despatchConfirmation.appendChild(locationFromStorageSection);
					
					Element locationFromStorageType = addElement(document, "fromStorageType", desp.getLocationDBFrom().getStorageType());
					despatchConfirmation.appendChild(locationFromStorageType);
					
					Element locationFromStorageBin = addElement(document, "fromStorageBin", desp.getLocationDBFrom().getStorageBin());
					despatchConfirmation.appendChild(locationFromStorageBin);		
					
					
					
					Element locationFromGLN = addElement(document, "fromGLN", desp.getLocationDBFrom().getGLN());
					despatchConfirmation.appendChild(locationFromGLN);

					Element locationFromStorageLocation = addElement(document, "fromStorageLocation", desp.getLocationDBFrom().getStorageLocation());
					despatchConfirmation.appendChild(locationFromStorageLocation);

					Element locationTo = addElement(document, "toLocation", desp.getLocationIDTo());
					despatchConfirmation.appendChild(locationTo);

					Element locationToPlant = addElement(document, "toPlant", desp.getLocationDBTo().getPlant());
					despatchConfirmation.appendChild(locationToPlant);
					
					Element locationToWarehouse = addElement(document, "toWarehouse", desp.getLocationDBTo().getWarehouse());
					despatchConfirmation.appendChild(locationToWarehouse);	
					
					Element locationToStorageSection = addElement(document, "toStorageSection", desp.getLocationDBTo().getStorageSection());
					despatchConfirmation.appendChild(locationToStorageSection);
					
					Element locationToStorageType = addElement(document, "toStorageType", desp.getLocationDBTo().getStorageType());
					despatchConfirmation.appendChild(locationToStorageType);
					
					Element locationToStorageBin = addElement(document, "toStorageBin", desp.getLocationDBTo().getStorageBin());
					despatchConfirmation.appendChild(locationToStorageBin);						
					
					Element locationToGLN = addElement(document, "toGLN", desp.getLocationDBTo().getGLN());
					despatchConfirmation.appendChild(locationToGLN);

					Element locationToStorageLocation = addElement(document, "toStorageLocation", desp.getLocationDBTo().getStorageLocation());
					despatchConfirmation.appendChild(locationToStorageLocation);

					Element contents = (Element) document.createElement("contents");
					despatchConfirmation.appendChild(contents);

					JDBPalletHistory palhist = new JDBPalletHistory(getHostID(), getSessionID());
					ResultSet rs = palhist.getInterfacingData(transactionRef, "DESPATCH", "TO", Long.valueOf(0), "SSCC", "asc");

					int x = 1;
					try
					{
						rs.beforeFirst();
						while (rs.next())
						{
							palhist.getPropertiesfromResultSet(rs);
							Element pallet = (Element) document.createElement("pallet");

							Element item = addElement(document, "item", String.valueOf(x));
							pallet.appendChild(item);
							x++;

							Element sscc = addElement(document, "SSCC", palhist.getPallet().getSSCC());
							pallet.appendChild(sscc);

							Element processOrder = addElement(document, "processOrder", palhist.getPallet().getProcessOrder());
							pallet.appendChild(processOrder);
							
							Element material = addElement(document, "material", palhist.getPallet().getMaterial());
							pallet.appendChild(material);
							
							Element materialDescription = addElement(document, "materialDescription", palhist.getPallet().getMaterialObj().getDescription());
							pallet.appendChild(materialDescription);

							Element ean = addElement(document, "ean", palhist.getPallet().getEAN());
							pallet.appendChild(ean);

							Element variant = addElement(document, "variant", palhist.getPallet().getVariant());
							pallet.appendChild(variant);

							Element qty = addElement(document, "quantity", palhist.getPallet().getQuantity().toString());
							pallet.appendChild(qty);

							String paluom = palhist.getPallet().getUom();
							paluom = uoml.convertUom(inter.getUOMConversion(), paluom);

							Element uom = addElement(document, "UOM", paluom);
							pallet.appendChild(uom);

							Element status = addElement(document, "status", palhist.getPallet().getStatus());
							pallet.appendChild(status);

							String expiryDateStr = "";
							if (batchDateMode.equals("BATCH"))
							{
								expiryDateStr=JUtility.getISOTimeStampStringFormat(palhist.getPallet().getMaterialBatchExpiryDate());
							}
							
							if (batchDateMode.equals("SSCC"))
							{
								expiryDateStr=JUtility.getISOTimeStampStringFormat(palhist.getPallet().getBatchExpiry());
							}			
							
							Element expiryDate = addElement(document, "bestBefore", expiryDateStr);
							pallet.appendChild(expiryDate);

							Element dom = addElement(document, "productionDate", JUtility.getISOTimeStampStringFormat(palhist.getPallet().getDateOfManufacture()));
							pallet.appendChild(dom);

							Element batch = addElement(document, "batch", palhist.getPallet().getBatchNumber());
							pallet.appendChild(batch);

							Element batchStatus = addElement(document, "batchStatus", palhist.getPallet().getMaterialBatchStatus());
							pallet.appendChild(batchStatus);
							
							
							if (order.getProcessOrderProperties(palhist.getPallet().getProcessOrder()) == true)
							{
								Element customer = addElement(document, "customerID", order.getCustomerID());
								pallet.appendChild(customer);
								
								if (cust.getCustomerProperties(order.getCustomerID())==true)
								{

									Element batchOverride = addElement(document, "customerBatchOverride",cust.getCustomerBatchOverride());
									pallet.appendChild(batchOverride);
									
									Element batchFormat = addElement(document, "customerBatchFormat",cust.getCustomerBatchFormat());
									pallet.appendChild(batchFormat);
									
								}
							}
							else
							{
								Element customer = addElement(document, "customerID", "");
								pallet.appendChild(customer);
							}
							

							Element equipmentType = addElement(document, "equipmentType", palhist.getPallet().getEquipmentType());
							pallet.appendChild(equipmentType);
							
							contents.appendChild(pallet);

						}
						rs.close();
					}
					catch (SQLException e)
					{

					}

					Element messageData = (Element) document.createElement("messageData");
					messageData.appendChild(despatchConfirmation);

					message.appendChild(messageData);

					document.appendChild(message);

	

					JXMLDocument xmld = new JXMLDocument();
					xmld.setDocument(document);
					gmh.decodeHeader(xmld);

					if (device.equals("Disk") | device.equals("Email"))
					{
						path = inter.getRealPath();
						if (fio.writeToDisk(path, document, transactionRef, "_"+desp.getLocationIDTo().replace(" ", "_")+"_DespatchConfirmation.xml") == true)
						{
							result = true;
							il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", fio.getFilename());
							setErrorMessage("");
							
							if (device.equals("Email"))
							{
								ogm = new OutGoingMessage(inter,transactionRef,fio);
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
					result=false;
				}
			}
		}

		return result;
	}

	public void setHostID(String host) {
		hostID = host;
	}

	public void setSessionID(String session) {
		sessionID = session;
	}

	public void submit(long dbTransactionRef) {
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		inter.getInterfaceProperties("Despatch Confirmation", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(dbTransactionRef, "Despatch Confirmation");
		}
		else
		{
			logger.debug("Interface Despatch Confirmation - Output is DISABLED");
		}
	}

}
