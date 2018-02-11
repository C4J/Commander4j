package com.commander4j.Connector.Inbound;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.sys.Common;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;

public class InboundConnectorEmail extends InboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((InboundConnectorEmail.class));

	public InboundConnectorEmail(InboundInterface inter)
	{
		super(Connector_EMAIL, inter);
	}

	@Override
	public boolean connectorLoad(String fullFilename)
	{

		logger.debug("connectorLoad [" + fullFilename + "]");
		boolean result = false;

		if (backupInboundFile(fullFilename))
		{

			FileInputStream fis = null;

			try
			{
				fis = new FileInputStream(fullFilename);

				byte[] byteArray = IOUtils.toByteArray(fis); // convert to bytes
				String byteArray64String = Base64.encodeBase64String(byteArray); // convert to base64

				/* DOCUMENT */
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				data = builder.newDocument();

				Element message = (Element) data.createElement("data");
				message.setAttribute("type", Connector_EMAIL);
				
				message.setAttribute("bytes", String.valueOf(String.valueOf(byteArray.length)));
				message.setAttribute("format", "base64");

				Element content = (Element) data.createElement("content");

				content.setTextContent(byteArray64String);

				message.appendChild(content);

				data.appendChild(message);

				byteArray = null;
				byteArray64String = null;

				result = true;

			} catch (Exception e)
			{
				logger.error("connectorLoad " + getType() + " " + e.getMessage());
				Common.emailqueue.addToQueue("Error", "Error reading " + getType(), "connectorLoad " + getType() + " " + e.getMessage() + "\n\n" + fullFilename, "");
			} finally
			{
				try
				{
					if (fis != null)
						fis.close();
				} catch (IOException e)
				{
				}
			}
		}
		return result;
	}
}
