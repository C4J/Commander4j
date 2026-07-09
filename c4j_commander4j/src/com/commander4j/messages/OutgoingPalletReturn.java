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
 * The OutgoingPalletReturn message is designed to output a
 * message to an external system (typically an ERP system) to inform it that a
 * pallet (SSCC) has been returned. All the core data from
 * the APP_PALLET table is exported along with linked information from
 * APP_LOCATION, APP_MATERIAL and APP_PROCESS_ORDER
 *
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.db.JDBProcessOrder JDBProcessOrder
 * @see com.commander4j.db.JDBMaterial JDBMaterial
 * @see com.commander4j.db.JDBLocation JDBLocation
 */
public class OutgoingPalletReturn
{
	private String hostID;
	private String sessionID;
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(OutgoingPalletReturn.class);
	private OutGoingMessage ogm;

	private String errorMessage;
	private JFileIO fio = new JFileIO();

	public OutgoingPalletReturn(String host, String session)
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

	/**
	 * Builds the field block for a single pallet history record. The block is
	 * nested under an element whose name is supplied by the caller (the
	 * transaction subtype - "from" or "to"), so the return message carries both
	 * the origin (FROM) and destination (TO) records of the move.
	 */
	private Element buildDetail(Document document, String elementName, JDBPalletHistory palhist, JDBMaterial mat, JDBProcessOrder order, JDBCustomer cust, JDBUom uom, JDBInterface inter, String expiryMode, String defaultBatchFormat)
	{
		Element detail = (Element) document.createElement(elementName);

		Element sscc = addElement(document, "SSCC", palhist.getPallet().getSSCC());
		detail.appendChild(sscc);

		Element processOrder = addElement(document, "processOrder", palhist.getPallet().getProcessOrder());
		detail.appendChild(processOrder);

		Element recipe = addElement(document, "recipe", palhist.getPallet().getProcessOrderObj(false).getRecipe());
		detail.appendChild(recipe);

		Element recipeVersion = addElement(document, "recipeVersion", palhist.getPallet().getProcessOrderObj(false).getRecipeVersion());
		detail.appendChild(recipeVersion);

		Element required_resource = addElement(document, "requiredResource", palhist.getPallet().getProcessOrderObj(false).getRequiredResource());
		detail.appendChild(required_resource);

		Element material = addElement(document, "material", palhist.getPallet().getMaterial());
		detail.appendChild(material);

		if (mat.getMaterialProperties(palhist.getPallet().getMaterial()) == true)
		{
			Element description = addElement(document, "description", mat.getDescription());
			detail.appendChild(description);
			Element old_code = addElement(document, "old_code", mat.getOldMaterial());
			detail.appendChild(old_code);
		} else
		{
			Element description = addElement(document, "description", "");
			detail.appendChild(description);
			Element old_code = addElement(document, "old_code", "");
			detail.appendChild(old_code);
		}

		if (order.getProcessOrderProperties(palhist.getPallet().getProcessOrder()) == true)
		{
			Element customer = addElement(document, "customerID", order.getCustomerID());
			detail.appendChild(customer);

			if (cust.getCustomerProperties(order.getCustomerID()) == true)
			{
				Element customerName = addElement(document, "customerName", cust.getName());
				detail.appendChild(customerName);
			}
		} else
		{
			Element customer = addElement(document, "customerID", "");
			detail.appendChild(customer);
		}

		Element transactionDate = addElement(document, "transactionDate", JUtility.getISOTimeStampStringFormat(palhist.getTransactionDate()));
		detail.appendChild(transactionDate);

		Element ean = addElement(document, "ean", palhist.getPallet().getEAN());
		detail.appendChild(ean);

		Element variant = addElement(document, "variant", palhist.getPallet().getVariant());
		detail.appendChild(variant);

		Element status = addElement(document, "status", palhist.getPallet().getStatus());
		detail.appendChild(status);

		Element batchDefault = addElement(document, "batchDefaultFormat", defaultBatchFormat);
		detail.appendChild(batchDefault);

		Element batch = addElement(document, "batch", palhist.getPallet().getBatchNumber());
		detail.appendChild(batch);

		Element batchStatus = addElement(document, "batchStatus", palhist.getPallet().getMaterialBatchStatus());
		detail.appendChild(batchStatus);

		Element expiryDateMode = addElement(document, "expiry_Mode", expiryMode);
		detail.appendChild(expiryDateMode);

		if (expiryMode.equals("BATCH") == true)
		{
			Element expiryDate = addElement(document, "expiryDate", JUtility.getISOTimeStampStringFormat(palhist.getPallet().getMaterialBatchExpiryDate()));
			detail.appendChild(expiryDate);
		} else
		{
			Element expiryDate = addElement(document, "expiryDate", JUtility.getISOTimeStampStringFormat(palhist.getPallet().getBatchExpiry()));
			detail.appendChild(expiryDate);
		}

		Element location = addElement(document, "location", palhist.getPallet().getLocationID());
		detail.appendChild(location);

		Element name = addElement(document, "name", palhist.getPallet().getLocationObj().getDescription());
		detail.appendChild(name);

		Element gln = addElement(document, "gln", palhist.getPallet().getLocationObj().getGLN());
		detail.appendChild(gln);

		Element plant = addElement(document, "plant", palhist.getPallet().getLocationObj().getPlant());
		detail.appendChild(plant);

		Element warehouse = addElement(document, "warehouse", palhist.getPallet().getLocationObj().getWarehouse());
		detail.appendChild(warehouse);

		Element storageLocation = addElement(document, "storageLocation", palhist.getPallet().getLocationObj().getStorageLocation());
		detail.appendChild(storageLocation);

		Element storageSection = addElement(document, "storageSection", palhist.getPallet().getLocationObj().getStorageSection());
		detail.appendChild(storageSection);

		Element storageBin = addElement(document, "storageBin", palhist.getPallet().getLocationObj().getStorageBin());
		detail.appendChild(storageBin);

		Element storageType = addElement(document, "storageType", palhist.getPallet().getLocationObj().getStorageType());
		detail.appendChild(storageType);

		Element locationBarcodeId = addElement(document, "barcodeId", palhist.getPallet().getLocationObj().getBarcodeId());
		detail.appendChild(locationBarcodeId);

		Element productionQuantity = addElement(document, "quantity", String.valueOf(palhist.getPallet().getQuantity()));
		detail.appendChild(productionQuantity);

		String paluom = palhist.getPallet().getUom();
		paluom = uom.convertUom(inter.getUOMConversion(), paluom);

		Element productionUOM = addElement(document, "uom", paluom);
		detail.appendChild(productionUOM);

		Element productionConfirmed = addElement(document, "confirmed", palhist.getPallet().getConfirmed());
		detail.appendChild(productionConfirmed);

		Element productionDate = addElement(document, "productionDate", JUtility.getISOTimeStampStringFormat(palhist.getPallet().getDateOfManufacture()));
		detail.appendChild(productionDate);

		return detail;
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

		inter.getInterfaceProperties("Pallet Return", "Output");
		String device = inter.getDevice();

		JDBPalletHistory palhist = new JDBPalletHistory(getHostID(), getSessionID());

		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.newDocument();

			Element palletReturn = (Element) document.createElement("palletReturn");

			String headerSSCC = "";
			String fileLocation = "";
			boolean foundAny = false;

			// The return transaction now writes two pallet history records sharing one
			// transaction ref - a FROM (origin) record and a TO (destination) record.
			// Loop through both and nest each field block under an element named after
			// the transaction subtype (from / to).
			String[] subtypes = { "FROM", "TO" };
			for (String subtype : subtypes)
			{
				ResultSet rs = palhist.getInterfacingData(transactionRef, "RETURN", subtype, Long.valueOf(1), "SSCC", "asc");
				if (rs != null)
				{
					if (rs.next())
					{
						palhist.getPropertiesfromResultSet(rs);
						palletReturn.appendChild(buildDetail(document, palhist.getTransactionSubtype().toLowerCase(), palhist, mat, order, cust, uom, inter, expiryMode, defaultBatchFormat));
						headerSSCC = palhist.getPallet().getSSCC();
						// Preserve the historical filename (return used the FROM / origin location).
						if (subtype.equals("FROM"))
						{
							fileLocation = palhist.getPallet().getLocationID();
						}
						foundAny = true;
					}
					rs.close();
				}
			}

			if (foundAny)
			{
				Element message = (Element) document.createElement("message");

				Element hostUniqueID = addElement(document, "hostRef", Common.hostList.getHost(getHostID()).getUniqueID());
				message.appendChild(hostUniqueID);

				Element messageRef = addElement(document, "messageRef", String.valueOf(transactionRef));
				message.appendChild(messageRef);

				Element messageType = addElement(document, "interfaceType", "Pallet Return");
				message.appendChild(messageType);

				Element messageInformation = addElement(document, "messageInformation", "SSCC=" + headerSSCC);
				message.appendChild(messageInformation);

				Element messageDirection = addElement(document, "interfaceDirection", "Output");
				message.appendChild(messageDirection);

				Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
				message.appendChild(messageDate);

				Element messageData = (Element) document.createElement("messageData");
				messageData.appendChild(palletReturn);

				message.appendChild(messageData);

				document.appendChild(message);

				JXMLDocument xmld = new JXMLDocument();
				xmld.setDocument(document);
				gmh.decodeHeader(xmld);

				if (device.equals("Disk") | device.equals("Email"))
				{

					path = inter.getRealPath();
					if (fio.writeToDisk(path, document, transactionRef, "_" + fileLocation.replace(" ", "_") + "_PalletReturn.xml") == true)
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
			} else
			{
				logger.debug("Could not find Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef));
			}
		}

		catch (Exception ex)
		{
			logger.error("Error sending message. " + ex.getMessage());
			ex.printStackTrace();

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
		inter.getInterfaceProperties("Pallet Return", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(dbTransactionRef, "Pallet Return");
		} else
		{
			logger.debug("Interface Pallet Return - Output is DISABLED");
		}

	}

}
