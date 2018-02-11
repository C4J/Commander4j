package com.commander4j.Connector.Outbound;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
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
	public boolean connectorSave(String filename)
	{
		boolean result = false;

		//parsePattern(getOutboundInterface().getOutputPattern());

		JXMLDocument document = new JXMLDocument();
		document.setDocument(getData());

		String byteArray64String = Utility.replaceNullStringwithBlank(document.findXPath("//data/content").trim());
		byte[] returnedBytes = Base64.decodeBase64(byteArray64String);

		FileOutputStream output;
		try
		{
			output = new FileOutputStream(new File(filename));
			IOUtils.write(returnedBytes, output);
			result=true;
			
			String addresses = getOutboundInterface().getEmailListID();
			String subject = getOutboundInterface().getEmailSubject();
			String message = getOutboundInterface().getEmailMessage()+"\n\n";
			
			Common.emailqueue.addToQueue(addresses, subject,message,filename);

		} catch (Exception e)
		{
			logger.error("connectorLoad " + getType() + " " + e.getMessage());
			Common.emailqueue.addToQueue("Error", "Error writing " + getType(), "connectorSave " + getType() + " " + e.getMessage() + "\n\n" + filename, "");
		}

		document = null;

		return result;
	}

}
