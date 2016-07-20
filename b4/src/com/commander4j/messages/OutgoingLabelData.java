package com.commander4j.messages;

import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.opencsv.CSVWriter;

import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBLabelData;
import com.commander4j.db.JDBMaterial;
import com.commander4j.email.JeMailOutGoingMessage;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

public class OutgoingLabelData {
	private String hostID;
	private String sessionID;
	final Logger logger = Logger.getLogger(OutgoingLabelData.class);
	private JeMailOutGoingMessage ogm;

	private String errorMessage;
	private JFileIO fio = new JFileIO();

	public OutgoingLabelData(String host, String session)
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

	public Boolean processMessage(String unique)

	{
		Boolean result = false;
		String path = "";
		JDBInterfaceLog il = new JDBInterfaceLog(getHostID(), getSessionID());
		GenericMessageHeader gmh = new GenericMessageHeader();
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		JDBMaterial mat = new JDBMaterial(getHostID(), getSessionID());
		JDBCustomer cust = new JDBCustomer(getHostID(), getSessionID());
		inter.getInterfaceProperties("Label Data", "Output");
		String device = inter.getDevice();
		String format = inter.getFormat();
		JDBLabelData labdata = new JDBLabelData(getHostID(), getSessionID());

		if (labdata.getProperties(unique))
		{

			try
			{
				if (format.equals("CSV"))
				{

					if (device.equals("Disk"))
					{
						String heading[] = labdata.getDataArray(unique, "heading");
						String data[] = labdata.getDataArray(unique, "data");

						path = inter.getRealPath();

						path = path.replace("\\", java.io.File.separator);
						path = path.replace("/", java.io.File.separator);

						if (path.length() > 0)
						{
							if (path.substring(path.length() - 1).equals(java.io.File.separator) == false)
							{
								path = path + java.io.File.separator;
							}
						}
						String filename = path + "LabelData_"+labdata.getLine() + "_" + unique + ".csv";

						CSVWriter writer = new CSVWriter(new FileWriter(filename), ',');
						writer.writeNext(heading);
						writer.writeNext(data);
						writer.close();
						result = true;
						il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", filename);
						setErrorMessage("");
					}
				}

				if (format.equals("XML"))
				{

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();

					Document document = builder.newDocument();

					Element message = (Element) document.createElement("message");

					Element hostUniqueID = addElement(document, "hostRef", Common.hostList.getHost(getHostID()).getUniqueID());
					message.appendChild(hostUniqueID);

					Element messageRef = addElement(document, "messageRef", unique);
					message.appendChild(messageRef);

					Element messageType = addElement(document, "interfaceType", "Label Data");
					message.appendChild(messageType);

					Element messageInformation = addElement(document, "messageInformation", "UniqueID=" + unique);
					message.appendChild(messageInformation);

					Element messageDirection = addElement(document, "interfaceDirection", "Output");
					message.appendChild(messageDirection);

					Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
					message.appendChild(messageDate);

					Element labelData = (Element) document.createElement("labelData");

					Element unq = addElement(document, "uniqueID", unique);
					labelData.appendChild(unq);

					Element lt = addElement(document, "labelType", labdata.getLabelType());
					labelData.appendChild(lt);

					Element line = addElement(document, "line", labdata.getLine());
					labelData.appendChild(line);

					Element printDate = addElement(document, "printDate", JUtility.getISOTimeStampStringFormat(labdata.getPrintDate()));
					labelData.appendChild(printDate);

					Element user = addElement(document, "userID", labdata.getUserID());
					labelData.appendChild(user);

					Element workstation = addElement(document, "workstation", labdata.getWorkstationID());
					labelData.appendChild(workstation);

					Element material = addElement(document, "material", labdata.getMaterial());
					labelData.appendChild(material);

					if (mat.getMaterialProperties(labdata.getMaterial()))
					{
						Element materialDesc = addElement(document, "materialDescription", mat.getDescription());
						labelData.appendChild(materialDesc);
					}

					Element materialType = addElement(document, "materialType", labdata.getMaterialType());
					labelData.appendChild(materialType);

					Element po = addElement(document, "processOrder", labdata.getProcessOrder());
					labelData.appendChild(po);

					Element rr = addElement(document, "requiredResource", labdata.getRequiredResource());
					labelData.appendChild(rr);

					Element location = addElement(document, "location", labdata.getLocationID());
					labelData.appendChild(location);

					Element batch = addElement(document, "batch", labdata.getBatchNumber());
					labelData.appendChild(batch);

					Element batchPrefix = addElement(document, "batchPrefix", labdata.getBatchPrefix());
					labelData.appendChild(batchPrefix);

					Element ovbatchPrefix = addElement(document, "overrideBatchPrefix", labdata.getOverrideBatchPrefix());
					labelData.appendChild(ovbatchPrefix);

					Element batchSuffix = addElement(document, "batchSuffix", labdata.getBatchSuffix());
					labelData.appendChild(batchSuffix);

					Element prodquantity = addElement(document, "prodQuantity", labdata.getProdQuantity().toString());
					labelData.appendChild(prodquantity);

					Element produom = addElement(document, "prodUom", labdata.getProdUom());
					labelData.appendChild(produom);

					Element prodEAN = addElement(document, "prodEAN", labdata.getProdEAN());
					labelData.appendChild(prodEAN);

					Element prodVar = addElement(document, "prodVariant", labdata.getProdVariant());
					labelData.appendChild(prodVar);

					Element basequantity = addElement(document, "baseQuantity", labdata.getBaseQuantity().toString());
					labelData.appendChild(basequantity);

					Element baseuom = addElement(document, "baseUom", labdata.getBaseUom());
					labelData.appendChild(baseuom);

					Element baseEAN = addElement(document, "baseEAN", labdata.getBaseEAN());
					labelData.appendChild(baseEAN);

					Element baseVar = addElement(document, "baseVariant", labdata.getBaseVariant());
					labelData.appendChild(baseVar);

					Element dom = addElement(document, "dateOfManufacture", JUtility.getISOTimeStampStringFormat(labdata.getDateofManufacture()));
					labelData.appendChild(dom);

					Element ovdom = addElement(document, "overrideDateofManufacture", labdata.getOverrideDateofManufacture());
					labelData.appendChild(ovdom);

					Element expiry = addElement(document, "expiryDate", JUtility.getISOTimeStampStringFormat(labdata.getExpirtDate()));
					labelData.appendChild(expiry);

					Element ovexpiry = addElement(document, "overrideExpiryDate", labdata.getOverrideExpiryDate());
					labelData.appendChild(ovexpiry);

					Element expiryMode = addElement(document, "expiryMode", labdata.getExpiryMode());
					labelData.appendChild(expiryMode);

					Element custid = addElement(document, "customer", labdata.getCustomer());
					labelData.appendChild(custid);

					if (cust.getCustomerProperties(labdata.getCustomer()))
					{
						Element custName = addElement(document, "customerName", cust.getName());
						labelData.appendChild(custName);
					}

					Element copies = addElement(document, "copies", labdata.getPrintCopies().toString());
					labelData.appendChild(copies);

					Element queueName = addElement(document, "queueName", labdata.getPrintQueue());
					labelData.appendChild(queueName);

					Element module = addElement(document, "moduleID", labdata.getModuleID());
					labelData.appendChild(module);

					Element messageData = (Element) document.createElement("messageData");
					messageData.appendChild(labelData);

					message.appendChild(messageData);

					document.appendChild(message);

					JXMLDocument xmld = new JXMLDocument();
					xmld.setDocument(document);
					gmh.decodeHeader(xmld);

					if (device.equals("Disk") | device.equals("Email"))
					{

						path = inter.getRealPath();
						if (fio.writeToDisk(path, document, -1, labdata.getLine() + "_" + unique + "_LabelData.xml") == true)
						{
							result = true;
							il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", fio.getFilename());
							setErrorMessage("");

							if (device.equals("Email"))
							{
								ogm = new JeMailOutGoingMessage(inter, (long) 0, fio);
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
			}

			catch (Exception ex)
			{
				logger.error("Error sending message. " + ex.getMessage());
				ex.printStackTrace();

			}

		} else
		{
			logger.debug("Could not find Label Data for Unique ID  " + unique);
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

	public void submit(String unique)
	{
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		inter.getInterfaceProperties("Label Data", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(unique, "Label Data");
		} else
		{
			logger.debug("Interface Label Data - Output is DISABLED");
		}

	}

}
