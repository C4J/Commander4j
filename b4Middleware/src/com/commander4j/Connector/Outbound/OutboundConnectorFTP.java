package com.commander4j.Connector.Outbound;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.ftp.FTP_Send;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorFTP extends OutboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundConnectorFTP.class));
	JFileIO jfileio = new JFileIO();

	public OutboundConnectorFTP(OutboundInterface inter)
	{
		super(Connector_FTP, inter);
	}

	@Override
	public boolean connectorSave(String path,String filename)
	{
		boolean result = false;
		String fullPath = path+File.separator+filename;

		Path p = Paths.get(fullPath);
		String shortFilename = p.getFileName().toString();
		p = null;

		JXMLDocument document = new JXMLDocument();
		document.setDocument(getData());

		String byteArray64String = Utility.replaceNullStringwithBlank(document.findXPath("//data/content").trim());
		byte[] returnedBytes = Base64.decodeBase64(byteArray64String);

		FileOutputStream output;
		try
		{
			output = new FileOutputStream(new File(fullPath));
			IOUtils.write(returnedBytes, output);
			result=true;
			
			String server = getOutboundInterface().getFtpServer();
			int port = getOutboundInterface().getFtpPort();
			String username = getOutboundInterface().getFtpUsername();
			String password = getOutboundInterface().getFtpPassword();
			String remotePath = getOutboundInterface().getFtpRemotePath();

			FTP_Send sender = new FTP_Send();

			sender.send(server, port, remotePath, shortFilename, fullPath,username, password);
			
			File f = new File(fullPath);
			FileUtils.deleteQuietly(f);
			f = null;
			

		} catch (Exception e)
		{
			logger.error("connectorLoad " + getType() + " " + e.getMessage());
			Common.emailqueue.addToQueue("Error", "Error writing " + getType(), "connectorSave " + getType() + " " + e.getMessage() + "\n\n" + fullPath, "");
		}

		document = null;

		return result;
	}

}
