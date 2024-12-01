package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : OutgoingProductionDeclarationConfirmation.java
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

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBPalletHistory;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBUom;
import com.commander4j.email.OutGoingMessage;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

/**
 * The OutgoingPalletIssue message is designed to output a
 * message to an external system (typically an ERP system) to inform it that a
 * pallet (SSCC) has been issued. All the core data from
 * the APP_PALLET table is exported along with linked information from
 * APP_LOCATION, APP_MATERIAL and APP_PROCESS_ORDER
 * 
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.db.JDBProcessOrder JDBProcessOrder
 * @see com.commander4j.db.JDBMaterial JDBMaterial
 * @see com.commander4j.db.JDBLocation JDBLocation
 */
public class OutgoingPalletIssue
{
	private String hostID;
	private String sessionID;
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(OutgoingPalletIssue.class);
	private OutGoingMessage ogm;

	private String errorMessage;
	private JFileIO fio = new JFileIO();

	public OutgoingPalletIssue(String host, String session)
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
		JDBUom uom = new JDBUom(getHostID(), getSessionID());
		JDBMaterial mat = new JDBMaterial(getHostID(), getSessionID());
		JDBProcessOrder order = new JDBProcessOrder(getHostID(), getSessionID());
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		JDBCustomer cust = new JDBCustomer(getHostID(), getSessionID());

		String expiryMode;
		expiryMode = ctrl.getKeyValue("EXPIRY DATE MODE");
		
		String defaultBatchFormat;
		defaultBatchFormat = ctrl.getKeyValue("BATCH FORMAT");

		inter.getInterfaceProperties("Pallet Issue", "Output");
		String device = inter.getDevice();

		JDBPalletHistory palhist = new JDBPalletHistory(getHostID(), getSessionID());
		ResultSet rs = palhist.getInterfacingData(transactionRef, "ISSUE", "TO", Long.valueOf(1), "SSCC", "asc");
		try
		{
			if (rs.next())
			{
				palhist.getPropertiesfromResultSet(rs);

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

					Element messageType = addElement(document, "interfaceType", "Pallet Issue");
					message.appendChild(messageType);

					Element messageInformation = addElement(document, "messageInformation", "SSCC=" + palhist.getPallet().getSSCC());
					message.appendChild(messageInformation);

					Element messageDirection = addElement(document, "interfaceDirection", "Output");
					message.appendChild(messageDirection);

					Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
					message.appendChild(messageDate);

					Element productionDeclaration = (Element) document.createElement("palletIssue");

					Element sscc = addElement(document, "SSCC", palhist.getPallet().getSSCC());
					productionDeclaration.appendChild(sscc);

					Element processOrder = addElement(document, "processOrder", palhist.getPallet().getProcessOrder());
					productionDeclaration.appendChild(processOrder);

					Element recipe = addElement(document, "recipe", palhist.getPallet().getProcessOrderObj(false).getRecipe());
					productionDeclaration.appendChild(recipe);
					
					Element recipeVersion = addElement(document, "recipeVersion", palhist.getPallet().getProcessOrderObj(false).getRecipeVersion());
					productionDeclaration.appendChild(recipeVersion);

					Element required_resource = addElement(document, "requiredResource", palhist.getPallet().getProcessOrderObj(false).getRequiredResource());
					productionDeclaration.appendChild(required_resource);

					Element material = addElement(document, "material", palhist.getPallet().getMaterial());
					productionDeclaration.appendChild(material);

					if (mat.getMaterialProperties(palhist.getPallet().getMaterial()) == true)
					{
						Element description = addElement(document, "description", mat.getDescription());
						productionDeclaration.appendChild(description);
						Element old_code = addElement(document, "old_code", mat.getOldMaterial());
						productionDeclaration.appendChild(old_code);
					} else
					{
						Element description = addElement(document, "description", "");
						productionDeclaration.appendChild(description);
						Element old_code = addElement(document, "old_code", "");
						productionDeclaration.appendChild(old_code);
					}
					
					if (order.getProcessOrderProperties(palhist.getPallet().getProcessOrder()) == true)
					{
						Element customer = addElement(document, "customerID", order.getCustomerID());
						productionDeclaration.appendChild(customer);
						
						if (cust.getCustomerProperties(order.getCustomerID())==true)
						{
							Element customerName = addElement(document, "customerName",cust.getName());
							productionDeclaration.appendChild(customerName);
						}
					}
					else
					{
						Element customer = addElement(document, "customerID", "");
						productionDeclaration.appendChild(customer);
					}

					Element ean = addElement(document, "ean", palhist.getPallet().getEAN());
					productionDeclaration.appendChild(ean);

					Element variant = addElement(document, "variant", palhist.getPallet().getVariant());
					productionDeclaration.appendChild(variant);

					Element status = addElement(document, "status", palhist.getPallet().getStatus());
					productionDeclaration.appendChild(status);

					Element batchDefault = addElement(document, "batchDefaultFormat", defaultBatchFormat);
					productionDeclaration.appendChild(batchDefault);
					
					Element batch = addElement(document, "batch", palhist.getPallet().getBatchNumber());
					productionDeclaration.appendChild(batch);

					Element batchStatus = addElement(document, "batchStatus", palhist.getPallet().getMaterialBatchStatus());
					productionDeclaration.appendChild(batchStatus);

					Element expiryDateMode = addElement(document, "expiry_Mode", expiryMode);
					productionDeclaration.appendChild(expiryDateMode);

					if (expiryMode.equals("BATCH") == true)
					{
						Element expiryDate = addElement(document, "expiryDate", JUtility.getISOTimeStampStringFormat(palhist.getPallet().getMaterialBatchExpiryDate()));
						productionDeclaration.appendChild(expiryDate);
					} else
					{
						Element expiryDate = addElement(document, "expiryDate", JUtility.getISOTimeStampStringFormat(palhist.getPallet().getBatchExpiry()));
						productionDeclaration.appendChild(expiryDate);
					}

					Element location = addElement(document, "location", palhist.getPallet().getLocationID());
					productionDeclaration.appendChild(location);

					Element name = addElement(document, "name", palhist.getPallet().getLocationObj().getDescription());
					productionDeclaration.appendChild(name);

					Element gln = addElement(document, "gln", palhist.getPallet().getLocationObj().getGLN());
					productionDeclaration.appendChild(gln);

					Element plant = addElement(document, "plant", palhist.getPallet().getLocationObj().getPlant());
					productionDeclaration.appendChild(plant);

					Element warehouse = addElement(document, "warehouse", palhist.getPallet().getLocationObj().getWarehouse());
					productionDeclaration.appendChild(warehouse);

					Element storageLocation = addElement(document, "storageLocation", palhist.getPallet().getLocationObj().getStorageLocation());
					productionDeclaration.appendChild(storageLocation);

					Element storageSection = addElement(document, "storageSection", palhist.getPallet().getLocationObj().getStorageSection());
					productionDeclaration.appendChild(storageSection);

					Element storageBin = addElement(document, "storageBin", palhist.getPallet().getLocationObj().getStorageBin());
					productionDeclaration.appendChild(storageBin);

					Element storageType = addElement(document, "storageType", palhist.getPallet().getLocationObj().getStorageType());
					productionDeclaration.appendChild(storageType);
					
					Element locationBarcodeId = addElement(document, "barcodeId", palhist.getPallet().getLocationObj().getBarcodeId());
					productionDeclaration.appendChild(locationBarcodeId);

					Element productionQuantity = addElement(document, "quantity", String.valueOf(palhist.getPallet().getQuantity()));
					productionDeclaration.appendChild(productionQuantity);

					String paluom = palhist.getPallet().getUom();
					paluom = uom.convertUom(inter.getUOMConversion(), paluom);

					Element productionUOM = addElement(document, "uom", paluom);
					productionDeclaration.appendChild(productionUOM);

					Element productionConfirmed = addElement(document, "confirmed", palhist.getPallet().getConfirmed());
					productionDeclaration.appendChild(productionConfirmed);

					Element productionDate = addElement(document, "productionDate", JUtility.getISOTimeStampStringFormat(palhist.getPallet().getDateOfManufacture()));
					productionDeclaration.appendChild(productionDate);

					Element messageData = (Element) document.createElement("messageData");
					messageData.appendChild(productionDeclaration);

					message.appendChild(messageData);

					document.appendChild(message);

					JXMLDocument xmld = new JXMLDocument();
					xmld.setDocument(document);
					gmh.decodeHeader(xmld);

					if (device.equals("Disk") | device.equals("Email"))
					{

						path = inter.getRealPath();
						if (fio.writeToDisk(path, document, transactionRef, "_" + palhist.getPallet().getLocationID().replace(" ", "_") + "_PalletIssue.xml") == true)
						{
							result = true;
							il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", fio.getFilename());
							setErrorMessage("");

							if (device.equals("Email"))
							{
								ogm = new OutGoingMessage(inter, transactionRef, fio);
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
			} else
			{
				logger.debug("Could not find Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef));
			}
			rs.close();
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
		inter.getInterfaceProperties("Pallet Issue", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(dbTransactionRef, "Pallet Issue");
		} else
		{
			logger.debug("Interface Pallet Issue - Output is DISABLED");
		}

	}

}
