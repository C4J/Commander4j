package com.commander4j.Connector.Outbound;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorEmail extends OutboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundConnectorEmail.class));
	JFileIO jfileio = new JFileIO();

	public OutboundConnectorEmail(OutboundInterface inter)
	{
		super(Connector_EMAIL, inter);
	}

	@Override
	public boolean connectorSave(String path,String prefix,String filename)
	{
		boolean result = false;
		String fullPath = path+File.separator+filename;

		JXMLDocument document = new JXMLDocument();
		document.setDocument(getData());

		String inputFilename = Utility.replaceNullStringwithBlank(document.findXPath("//email/inputFilename").trim());
		
		String outputFilename = path;
		
		if (outputFilename.endsWith(File.separator))
		{
			outputFilename = outputFilename + filename;
		}
		else
		{
			outputFilename = outputFilename +File.separator+filename;
		}
		
		logger.error("connectorLoad " + getType() + " inputFilename" + inputFilename);
		logger.error("connectorLoad " + getType() + " outputFilename" + outputFilename);

		try
		{
			FileUtils.deleteQuietly(new File(outputFilename));
			FileUtils.moveFileToDirectory(new File(inputFilename), new File(path), false);
			
			result=true;
			
			String addresses = getOutboundInterface().getEmailListID();
			String subject = getOutboundInterface().getEmailSubject();
			String message = getOutboundInterface().getEmailMessage()+"\n\n";
			
			Common.emailqueue.addToQueue(addresses, subject,message,outputFilename);

		} catch (Exception e)
		{
			logger.error("connectorLoad " + getType() + " " + e.getMessage());
			Common.emailqueue.addToQueue("Error", "Error writing " + getType(), "connectorSave " + getType() + " " + e.getMessage() + "\n\n" + fullPath, "");
		}

		document = null;

		return result;
	}

}
