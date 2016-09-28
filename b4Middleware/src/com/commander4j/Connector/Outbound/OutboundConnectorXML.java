package com.commander4j.Connector.Outbound;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.commander4j.Interface.Outbound.OutboundInterface;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorXML extends OutboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundConnectorXML.class));

	public OutboundConnectorXML(OutboundInterface inter)
	{
		super(Connector_XML, inter);
	}

	@Override
	public boolean connectorSave(String filename)
	{
		boolean result = false;

		logger.debug("connectorSave [" + filename + "." + getOutboundInterface().getOutputFileExtension().toLowerCase()
				+ "]");
		try
		{
			DOMImplementationLS DOMiLS = null;
			FileOutputStream FOS = null;
			if ((data.getFeature("Core", "3.0") != null) && (data.getFeature("LS", "3.0") != null))
			{
				DOMiLS = (DOMImplementationLS) (data.getImplementation()).getFeature("LS", "3.0");

				LSOutput LSO = DOMiLS.createLSOutput();

				if (filename.endsWith("." + getType().toLowerCase()) == false)
				{
					filename = filename + "." + getType().toLowerCase();
				}
				FOS = new FileOutputStream(filename);
				LSO.setByteStream((OutputStream) FOS);

				LSSerializer LSS = DOMiLS.createLSSerializer();

				LSS.write(getData(), LSO);

				FOS.close();
				result = true;
			}
		} catch (Exception ex)
		{
			result = false;
			logger.error(ex.getMessage());
		}

		return result;
	}

}
