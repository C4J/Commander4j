package com.commander4j.Connector.Inbound;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;

import com.commander4j.Interface.Inbound.InboundInterface;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;

public class InboundConnectorXML extends InboundConnectorABSTRACT {

	Logger logger = Logger.getLogger(InboundConnectorXML.class);

	public InboundConnectorXML(InboundInterface inter)
	{
		super(Connector_XML, inter);
	}

	@Override
	public boolean connectorLoad(String fullFilename)
	{

		logger.debug("connectorLoad [" + fullFilename + "]");

		boolean result;
		try
		{
			result = true;

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			data = builder.parse(new File(fullFilename));
			result = true;

		} catch (Exception ex)
		{
			result = false;
		}
		return result;
	}

}
