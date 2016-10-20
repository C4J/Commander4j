package com.commander4j.Connector.Inbound;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.Logger;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.sys.Common;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;

public class InboundConnectorXML extends InboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((InboundConnectorABSTRACT.class));

	public InboundConnectorXML(InboundInterface inter)
	{
		super(Connector_XML, inter);
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
				data = builder.parse(new File(fullFilename));
				result = true;

			} catch (Exception ex)
			{
				result = false;
				logger.error("connectorLoad " + getType() + " " + ex.getMessage());
				Common.emailqueue.addToQueue("Error", "Error reading "+getType(), "connectorLoad " + getType() + " " + ex.getMessage()+"\n\n"+fullFilename, "");
			}

		}

		return result;
	}

}
