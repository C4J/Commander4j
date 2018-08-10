package com.commander4j.Connector.Outbound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.sys.Common;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorXML extends OutboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundConnectorXML.class));

	public OutboundConnectorXML(OutboundInterface inter)
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
			DOMImplementationLS DOMiLS = null;
			FileOutputStream FOS = null;
			if ((data.getFeature("Core", "3.0") != null) && (data.getFeature("LS", "3.0") != null))
			{
				DOMiLS = (DOMImplementationLS) (data.getImplementation()).getFeature("LS", "3.0");

				LSOutput LSO = DOMiLS.createLSOutput();

				if (fullPath.endsWith("." + getType().toLowerCase()) == false)
				{
					fullPath = fullPath + "." + getType().toLowerCase();
				}

				String tempFilename = fullPath + ".tmp";
				String finalFilename = fullPath;

				FOS = new FileOutputStream(tempFilename);
				LSO.setByteStream((OutputStream) FOS);

				LSSerializer LSS = DOMiLS.createLSSerializer();

				LSS.write(getData(), LSO);

				FOS.close();

				FileUtils.deleteQuietly( new File(finalFilename));
				FileUtils.moveFile(new File(tempFilename), new File(finalFilename));

				DOMiLS = null;
				LSO = null;
				LSS = null;
				FOS = null;
				tempFilename = null;
				finalFilename = null;
				
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
