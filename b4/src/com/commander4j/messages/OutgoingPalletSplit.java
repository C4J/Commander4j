package com.commander4j.messages;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBPalletHistory;
import com.commander4j.email.JeMailOutGoingMessage;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

public class OutgoingPalletSplit
{
	private String hostID;
	private String sessionID;
	final Logger logger = Logger.getLogger(OutgoingPalletSplit.class);
	private JeMailOutGoingMessage ogm;

	private String errorMessage;
	private JFileIO fio = new JFileIO();

	public OutgoingPalletSplit(String host, String session)
	{
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

		inter.getInterfaceProperties("Pallet Split", "Output");
		String device = inter.getDevice();

		JDBPalletHistory palhistbefore = new JDBPalletHistory(getHostID(), getSessionID());
		JDBPalletHistory palhistafter = new JDBPalletHistory(getHostID(), getSessionID());
		JDBPalletHistory palhistcreate = new JDBPalletHistory(getHostID(), getSessionID());
 		
		ResultSet rsBefore = palhistbefore.getInterfacingData(transactionRef, "SPLIT", "BEFORE", Long.valueOf(1), "SSCC", "asc");

		ResultSet rsAfter = palhistafter.getInterfacingData(transactionRef, "SPLIT", "AFTER", Long.valueOf(1), "SSCC", "asc");

		ResultSet rsCreate = palhistcreate.getInterfacingData(transactionRef, "SPLIT", "CREATE", Long.valueOf(1), "SSCC", "asc");

		try
		{
			if (rsBefore.next())
			{
				if (rsAfter.next())
				{
					if (rsCreate.next())
					{
						palhistbefore.getPropertiesfromResultSet(rsBefore);
						palhistafter.getPropertiesfromResultSet(rsAfter);
						palhistcreate.getPropertiesfromResultSet(rsCreate);
						
						String locn =palhistbefore.getPallet().getLocationID();

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

							Element messageType = addElement(document, "interfaceType", "Pallet Split");
							message.appendChild(messageType);

							Element messageInformation = addElement(document, "messageInformation", "SSCC=" + palhistbefore.getPallet().getSSCC());
							message.appendChild(messageInformation);

							Element messageDirection = addElement(document, "interfaceDirection", "Output");
							message.appendChild(messageDirection);

							Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
							message.appendChild(messageDate);

							Element messageData = (Element) document.createElement("messageData");
							
							//-----------------------INPUT PALLET - BEFORE ------------------------//
							
							Element palletSplitBefore = (Element) document.createElement("palletSplitBefore");

							Element ssccBefore = addElement(document, "SSCC", palhistbefore.getPallet().getSSCC());
							palletSplitBefore.appendChild(ssccBefore);

							Element materialBefore = addElement(document, "material", palhistbefore.getPallet().getMaterial());
							palletSplitBefore.appendChild(materialBefore);

							Element batchBefore = addElement(document, "batch", palhistbefore.getPallet().getBatchNumber());
							palletSplitBefore.appendChild(batchBefore);

							Element locationBefore = addElement(document, "location", palhistbefore.getPallet().getLocationID());
							palletSplitBefore.appendChild(locationBefore);

							//---
						
							Element processOrderBefore = addElement(document, "processOrder", palhistbefore.getPallet().getProcessOrder());
							palletSplitBefore.appendChild(processOrderBefore);						

							Element statusBefore = addElement(document, "status", palhistbefore.getPallet().getStatus());
							palletSplitBefore.appendChild(statusBefore);	
							
							Element plantBefore = addElement(document, "plant", palhistbefore.getPallet().getLocationObj().getPlant());
							palletSplitBefore.appendChild(plantBefore);

							Element warehouseBefore= addElement(document, "warehouse", palhistbefore.getPallet().getLocationObj().getWarehouse());
							palletSplitBefore.appendChild(warehouseBefore);

							Element binBefore = addElement(document, "bin", palhistbefore.getPallet().getLocationObj().getStorageBin());
							palletSplitBefore.appendChild(binBefore);
							
							Element storageLocationBefore = addElement(document, "storageLocation", palhistbefore.getPallet().getLocationObj().getStorageLocation());
							palletSplitBefore.appendChild(storageLocationBefore);

							Element storageTypeBefore = addElement(document, "storageType", palhistbefore.getPallet().getLocationObj().getStorageType());
							palletSplitBefore.appendChild(storageTypeBefore);
							//----
							
							Element quantityBefore = addElement(document, "quantity", palhistbefore.getPallet().getQuantity().toString());
							palletSplitBefore.appendChild(quantityBefore);

							Element uomBefore = addElement(document, "uom", palhistbefore.getPallet().getUom().toString());
							palletSplitBefore.appendChild(uomBefore);

							messageData.appendChild(palletSplitBefore);

							//-----------------------INPUT PALLET - AFTER ------------------------//
							
							Element palletSplitAfter = (Element) document.createElement("palletSplitAfter");

							Element ssccAfter = addElement(document, "SSCC", palhistafter.getPallet().getSSCC());
							palletSplitAfter.appendChild(ssccAfter);

							Element materialAfter = addElement(document, "material", palhistafter.getPallet().getMaterial());
							palletSplitAfter.appendChild(materialAfter);

							Element batchAfter = addElement(document, "batch", palhistafter.getPallet().getBatchNumber());
							palletSplitAfter.appendChild(batchAfter);

							Element locationAfter = addElement(document, "location", palhistafter.getPallet().getLocationID());
							palletSplitAfter.appendChild(locationAfter);
							
							//---
							
							Element processOrderAfter = addElement(document, "processOrder", palhistafter.getPallet().getProcessOrder());
							palletSplitAfter.appendChild(processOrderAfter);						

							Element statusAfter = addElement(document, "status", palhistafter.getPallet().getStatus());
							palletSplitAfter.appendChild(statusAfter);	
							
							Element plantAfter = addElement(document, "plant", palhistafter.getPallet().getLocationObj().getPlant());
							palletSplitAfter.appendChild(plantAfter);

							Element warehouseAfter = addElement(document, "warehouse", palhistafter.getPallet().getLocationObj().getWarehouse());
							palletSplitAfter.appendChild(warehouseAfter);

							Element binAfter = addElement(document, "bin", palhistafter.getPallet().getLocationObj().getStorageBin());
							palletSplitAfter.appendChild(binAfter);
							
							Element storageLocationAfter = addElement(document, "storageLocation", palhistafter.getPallet().getLocationObj().getStorageLocation());
							palletSplitAfter.appendChild(storageLocationAfter);

							Element storageTypeAfter = addElement(document, "storageType", palhistafter.getPallet().getLocationObj().getStorageType());
							palletSplitAfter.appendChild(storageTypeAfter);
							//----
							
							Element quantityAfter = addElement(document, "quantity", palhistafter.getPallet().getQuantity().toString());
							palletSplitAfter.appendChild(quantityAfter);

							Element uomAfter = addElement(document, "uom", palhistafter.getPallet().getUom().toString());
							palletSplitAfter.appendChild(uomAfter);

							messageData.appendChild(palletSplitAfter);
							
							//-----------------------OUTPUT PALLET - NEW ------------------------//						
							
							Element palletSplitCreated = (Element) document.createElement("palletSplitCreated");

							Element ssccCreated = addElement(document, "SSCC", palhistcreate.getPallet().getSSCC());
							palletSplitCreated.appendChild(ssccCreated);

							Element materialCreated = addElement(document, "material", palhistcreate.getPallet().getMaterial());
							palletSplitCreated.appendChild(materialCreated);

							Element batchCreated = addElement(document, "batch", palhistcreate.getPallet().getBatchNumber());
							palletSplitCreated.appendChild(batchCreated);

							Element locationCreated = addElement(document, "location", palhistcreate.getPallet().getLocationID());
							palletSplitCreated.appendChild(locationCreated);
							
							//---
							
							Element processOrderCreated = addElement(document, "processOrder", palhistcreate.getPallet().getProcessOrder());
							palletSplitCreated.appendChild(processOrderCreated);						

							Element statusCreated = addElement(document, "status", palhistcreate.getPallet().getStatus());
							palletSplitCreated.appendChild(statusCreated);	
							
							Element plantCreated = addElement(document, "plant", palhistcreate.getPallet().getLocationObj().getPlant());
							palletSplitCreated.appendChild(plantCreated);

							Element warehouseCreated = addElement(document, "warehouse", palhistcreate.getPallet().getLocationObj().getWarehouse());
							palletSplitCreated.appendChild(warehouseCreated);

							Element binCreated = addElement(document, "bin", palhistcreate.getPallet().getLocationObj().getStorageBin());
							palletSplitCreated.appendChild(binCreated);
							
							Element storageLocationCreated = addElement(document, "storageLocation", palhistcreate.getPallet().getLocationObj().getStorageLocation());
							palletSplitCreated.appendChild(storageLocationCreated);

							Element storageTypeCreated = addElement(document, "storageType", palhistcreate.getPallet().getLocationObj().getStorageType());
							palletSplitCreated.appendChild(storageTypeCreated);
							//----							

							Element quantityCreated = addElement(document, "quantity", palhistcreate.getPallet().getQuantity().toString());
							palletSplitCreated.appendChild(quantityCreated);

							Element uomCreated = addElement(document, "uom", palhistcreate.getPallet().getUom().toString());
							palletSplitCreated.appendChild(uomCreated);

							messageData.appendChild(palletSplitCreated);
							
							//--------------------------------------------------------------//		
							
							message.appendChild(messageData);

							document.appendChild(message);

							JXMLDocument xmld = new JXMLDocument();
							xmld.setDocument(document);
							gmh.decodeHeader(xmld);

							if (device.equals("Disk") | device.equals("Email"))
							{

								path = inter.getRealPath();
								if (fio.writeToDisk(path, document, transactionRef, "_" + locn.replace(" ", "_") + "_PalletSplit.xml") == true)
								{
									result = true;
									il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", fio.getFilename());
									setErrorMessage("");

									if (device.equals("Email"))
									{
										ogm = new JeMailOutGoingMessage(inter, transactionRef, fio);
										ogm.sendEmail();
									}
								} else
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
						rsCreate.close();
					} else
					{
						logger.debug("Could not find Split CREATE Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef));
					}
					rsAfter.close();
				} else
				{
					logger.debug("Could not find Split AFTER Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef));
				}
				rsBefore.close();
			} else
			{
				logger.debug("Could not find Split BEFORE Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef));
			}

		} catch (SQLException e)
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
		inter.getInterfaceProperties("Pallet Split", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(dbTransactionRef, "Pallet Split");
		} else
		{
			logger.debug("Interface Pallet Split - Output is DISABLED");
		}

	}

}
