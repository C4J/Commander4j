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

public class OutgoingPalletDelete
{
	private String hostID;
	private String sessionID;
	final Logger logger = Logger.getLogger(OutgoingPalletDelete.class);
	private JeMailOutGoingMessage ogm;

	private String errorMessage;
	private JFileIO fio = new JFileIO();

	public OutgoingPalletDelete(String host, String session)
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

		inter.getInterfaceProperties("Pallet Delete", "Output");
		String device = inter.getDevice();

		JDBPalletHistory palhistfrom = new JDBPalletHistory(getHostID(), getSessionID());

		ResultSet rsFrom = palhistfrom.getInterfacingData(transactionRef, "DELETE", "MANUAL", Long.valueOf(1), "SSCC", "asc");

		try
		{
			if (rsFrom.next())
			{
				palhistfrom.getPropertiesfromResultSet(rsFrom);

				String locn = "";

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

					Element messageType = addElement(document, "interfaceType", "Pallet Delete");
					message.appendChild(messageType);

					Element messageInformation = addElement(document, "messageInformation", "SSCC=" + palhistfrom.getPallet().getSSCC());
					message.appendChild(messageInformation);

					Element messageDirection = addElement(document, "interfaceDirection", "Output");
					message.appendChild(messageDirection);

					Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
					message.appendChild(messageDate);

					Element palletDelete = (Element) document.createElement("palletDelete");

					Element sscc = addElement(document, "SSCC", palhistfrom.getPallet().getSSCC());
					palletDelete.appendChild(sscc);

					Element material = addElement(document, "material", palhistfrom.getPallet().getMaterial());
					palletDelete.appendChild(material);

					Element batch = addElement(document, "batch", palhistfrom.getPallet().getBatchNumber());
					palletDelete.appendChild(batch);

					locn = palhistfrom.getPallet().getLocationID();
					Element location = addElement(document, "location", palhistfrom.getPallet().getLocationID());
					palletDelete.appendChild(location);

					Element quantity = addElement(document, "quantity", palhistfrom.getPallet().getQuantity().toString());
					palletDelete.appendChild(quantity);

					Element uom = addElement(document, "uom", palhistfrom.getPallet().getUom().toString());
					palletDelete.appendChild(uom);

					Element statusFrom = addElement(document, "status", palhistfrom.getPallet().getStatus());
					palletDelete.appendChild(statusFrom);

					Element confirmed = addElement(document, "confirmed", palhistfrom.getPallet().getConfirmed());
					palletDelete.appendChild(confirmed);
					
					Element messageData = (Element) document.createElement("messageData");
					messageData.appendChild(palletDelete);

					message.appendChild(messageData);

					document.appendChild(message);

					JXMLDocument xmld = new JXMLDocument();
					xmld.setDocument(document);
					gmh.decodeHeader(xmld);

					if (device.equals("Disk") | device.equals("Email"))
					{

						path = inter.getRealPath();
						if (fio.writeToDisk(path, document, transactionRef, "_" + locn.replace(" ", "_") + "_PalletDelete.xml") == true)
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

			} else
			{
				logger.debug("Could not find Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef));
			}
			rsFrom.close();
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
		inter.getInterfaceProperties("Pallet Delete", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(dbTransactionRef, "Pallet Delete");
		} else
		{
			logger.debug("Interface Pallet Delete - Output is DISABLED");
		}

	}

}
