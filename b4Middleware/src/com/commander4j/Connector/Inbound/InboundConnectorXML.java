package com.commander4j.Connector.Inbound;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.Logger;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.sys.Common;
import com.commander4j.util.Utility;

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

		try
		{
			String destination = Common.logDir + java.io.File.separator + Utility.getCurrentTimeStampString() + " INPUT_BACKUP_" + getType() + " " + (new File(fullFilename)).getName();
			logger.debug("connectorLoad Backup [" + fullFilename + "] to [" + destination + "]");
			Path from = Paths.get(fullFilename);
			Path to = Paths.get(destination);
			Files.copy(from, to);
		} catch (Exception ex)
		{
			logger.error("connectorLoad unable to backup [" + fullFilename + "]");
			logger.error("Error message [" + ex.getMessage() + "]");
		}

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
