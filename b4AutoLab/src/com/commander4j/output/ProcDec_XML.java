package com.commander4j.output;

import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.autolab.AutoLab;
import com.commander4j.prodLine.ProdLine;
import com.commander4j.utils.JFileIO;
import com.commander4j.utils.JUtility;

public class ProcDec_XML
{

	private String filename = "";
	private String path = "";
	private String uuid = "";
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document document;
	private JFileIO fio = new JFileIO();
	private JUtility utils = new JUtility();
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((ProcDec_XML.class));
	private Calendar caldate;

	public void appendNotification(String message)
	{
		((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).appendNotification(message);
	}
	
	public void setNotification(String message)
	{
		((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).setNotification(message);
	}
	
	public String getUuid()
	{
		return uuid;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public boolean processMessage()
	{
		boolean result = false;

		if (AutoLab.config.isSuppressOutputMessage())
		{
			result = true;
			appendNotification("OUTPUT SUPPRESSED - see config.xml");
			logger.debug("[" + getUuid()+"] OUTPUT SUPPRESSED - see config.xml ");
		}
		else
		{
			try
			{
				factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();
				document = builder.newDocument();

				setFilename(AutoLab.getDataSet_Field(uuid, "SSCC") + ".xml");
				setPath(AutoLab.config.getOutputPath());
				
				appendNotification("Creating Production Declaration ["+getFilename()+"]");

				Element element_message = (Element) document.createElement("message");

				Element element_hostRef = addElement(document, "hostRef", AutoLab.config.getSystemKey("HOST REF"));

				element_message.appendChild(element_hostRef);

				Element element_interfaceType = addElement(document, "interfaceType", "Production Declaration");

				element_message.appendChild(element_interfaceType);

				Element element_messageInformation = addElement(document, "messageInformation", "SSCC=" + AutoLab.getDataSet_Field(uuid, "SSCC"));

				element_message.appendChild(element_messageInformation);

				Element element_interfaceDirection = addElement(document, "interfaceDirection", "Input");

				element_message.appendChild(element_interfaceDirection);

				caldate = Calendar.getInstance();
				Element element_messageDate = addElement(document, "messageDate", utils.getFormattedISOCalendarString(caldate));

				element_message.appendChild(element_messageDate);

				Element element_messageData = (Element) document.createElement("messageData");

				Element element_productionDeclaration = (Element) document.createElement("productionDeclaration");

				Element element_SSCC = addElement(document, "SSCC", AutoLab.getDataSet_Field(uuid, "SSCC"));

				element_productionDeclaration.appendChild(element_SSCC);

				Element element_productionQuantity = addElement(document, "productionQuantity", AutoLab.getDataSet_Field(uuid, "QUANTITY_DECIMALS"));

				element_productionDeclaration.appendChild(element_productionQuantity);

				Element element_UOM = addElement(document, "productionUOM", AutoLab.getDataSet_Field(uuid, "PROD_UOM"));

				element_productionDeclaration.appendChild(element_UOM);

				caldate = utils.convertStringToCalendar("dd-MMM-yyyy HH:mm:ss", AutoLab.getDataSet_Field(uuid, "EXPIRY_DATE"));

				Element element_expiryDate = addElement(document, "expiryDate", utils.getFormattedISOCalendarString(caldate));

				element_productionDeclaration.appendChild(element_expiryDate);

				caldate = utils.convertStringToCalendar("dd-MMM-yyyy HH:mm:ss", AutoLab.getDataSet_Field(uuid, "DATE_OF_MANUFACTURE"));

				Element element_productionDate = addElement(document, "productionDate", utils.getFormattedISOCalendarString(caldate));

				element_productionDeclaration.appendChild(element_productionDate);

				Element element_batch = addElement(document, "batch", AutoLab.getDataSet_Field(uuid, "BATCH_NUMBER"));

				element_productionDeclaration.appendChild(element_batch);

				Element element_processOrder = addElement(document, "processOrder", AutoLab.getDataSet_Field(uuid, "PROCESS_ORDER"));

				element_productionDeclaration.appendChild(element_processOrder);

				Element element_confirmed = addElement(document, "confirmed", AutoLab.config.getSystemKey("SSCC AUTO CONFIRM"));

				element_productionDeclaration.appendChild(element_confirmed);

				element_messageData.appendChild(element_productionDeclaration);

				element_message.appendChild(element_messageData);

				document.appendChild(element_message);

				fio.writeToDisk(getPath(), document, getFilename());
				
				appendNotification("File ["+getFilename()+"] created successfully ");

				//AutoLab.emailqueue.addToQueue("Info", utils.getClientName() + " ProdDec created.", getPath() + File.separator + getFilename(), "");

			}
			catch (ParserConfigurationException e)
			{
				logger.debug("Error writing output file :" + e.getLocalizedMessage());
				appendNotification("Error writing output file :" + e.getLocalizedMessage());
			}
		}

		return result;
	}

	public Element addElement(Document doc, String name, String value)
	{
		Element temp = (Element) doc.createElement(name);
		Text temp_value = doc.createTextNode(value);
		temp.appendChild(temp_value);
		return temp;
	}

}
