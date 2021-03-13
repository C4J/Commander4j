package com.commander4j.Connector.Inbound;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.sys.Common;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;

public class InboundConnectorPDF_PRINT extends InboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((InboundConnectorPDF_PRINT.class));

	public InboundConnectorPDF_PRINT(InboundInterface inter)
	{
		super(Connector_PDF_PRINT, inter);
	}

	@Override
	public boolean connectorLoad(String fullFilename)
	{

		logger.debug("connectorLoad [" + fullFilename + "]");
		boolean result = false;

		if (backupInboundFile(fullFilename))
		{
			try
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				data = builder.newDocument();

				Element message = (Element) data.createElement("pdf_print");


				Element content = (Element) data.createElement("inputFilename");

				content.setTextContent(fullFilename);

				message.appendChild(content);

				data.appendChild(message);

				result = true;

			} catch (Exception e)
			{
				logger.error("connectorLoad " + getType() + " " + e.getMessage());
				Common.emailqueue.addToQueue("Error", "Error reading " + getType(), "connectorLoad " + getType() + " " + e.getMessage() + "\n\n" + fullFilename, "");
			}
		}
		return result;
	}
}
