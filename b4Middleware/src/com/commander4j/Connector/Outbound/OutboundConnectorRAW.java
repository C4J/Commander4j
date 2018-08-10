package com.commander4j.Connector.Outbound;

import java.io.File;
import java.io.FileWriter;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.sys.Common;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorRAW extends OutboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundConnectorRAW.class));

	public OutboundConnectorRAW(OutboundInterface inter)
	{
		super(Connector_XML, inter);
	}

	@Override
	public boolean connectorSave(String path,String filename)
	{
		boolean result = false;
		String fullPath = path+File.separator+filename;

		logger.debug("connectorSave [" + fullPath + "." + getOutboundInterface().getOutputFileExtension().toLowerCase() + "]");
		try
		{

			if ((data.getFeature("Core", "3.0") != null) && (data.getFeature("LS", "3.0") != null))
			{
			    XPath xpath = XPathFactory.newInstance().newXPath();
			    String value;
				try
				{
					Node widgetNode = (Node) xpath.evaluate("/raw", data, XPathConstants.NODE);
					value = widgetNode.getFirstChild().getNodeValue().toString();
				}
				catch (Exception ex)
				{
					value = "";
				}
				

				if (fullPath.endsWith("." + getOutboundInterface().getOutputFileExtension().toLowerCase()) == false)
				{
					fullPath = fullPath + "." + getOutboundInterface().getOutputFileExtension().toLowerCase();
				}

				String tempFilename = fullPath + ".tmp";
				String finalFilename = fullPath;

				FileWriter fw = new FileWriter(tempFilename);
				fw.write(value);
				fw.flush();
				fw.close();

				FileUtils.deleteQuietly( new File(finalFilename));
				FileUtils.moveFile(new File(tempFilename), new File(finalFilename));

				tempFilename = null;
				finalFilename = null;
				fw = null;
				
				result = true;
			}
		} catch (Exception ex)
		{
			result = false;
			logger.error(ex.getMessage());
			Common.emailqueue.addToQueue("Error", "Error Writing File [" + fullPath + "]", ex.getMessage() + "\n\n", "");

		}

		return result;
	}

}
