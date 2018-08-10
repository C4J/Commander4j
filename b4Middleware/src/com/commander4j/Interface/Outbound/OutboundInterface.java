package com.commander4j.Interface.Outbound;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.Logger;

import org.w3c.dom.Document;

import com.commander4j.Interface.Mapping.Map;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Interface.OutboundInterfaceABSTRACT;

public class OutboundInterface extends OutboundInterfaceABSTRACT
{

	JFileIO jfileio = new JFileIO();
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundInterface.class));
	TransformerFactory fact = new net.sf.saxon.TransformerFactoryImpl();	
	StreamSource source;
	StreamSource xslt;
	StreamResult destination;
	Transformer transformer;
	Boolean writeSuccess;
	String filename_outputImported;
	String filename_outputTransformed;

	Document data;
	boolean enabled = false;

	public OutboundInterface(Map map, String description)
	{
		super(map);
		setDescription(description);
	}

	@Override
	public void run()
	{

	}

	public void processInterfaceToConnector(String filename, Document data)
	{
		this.data = data;

		logger.debug("processInterfaceToConnector [" + Utility.getStringFromDocument(data) + "]");

		if (getXSLTFilename().equals("") == false)
		{
			getDescription();
			 
			filename_outputImported = Utility.getCurrentTimeStampString() + " OUTPUT_IMPORTED_" + connector.getType()+ "_"+getId() + "_" + filename;
			if (filename_outputImported.endsWith(".xml") == false)
			{
				filename_outputImported = filename_outputImported + ".xml";
			}

			writeSuccess = jfileio.writeToDisk(Common.logDir, data, filename_outputImported);

			if (writeSuccess)
			{

				filename_outputTransformed = Utility.getCurrentTimeStampString() + " OUTPUT_TRANSFORMED_" + connector.getType()+ "_"+getId() + "_" + filename;
				if (filename_outputTransformed.endsWith(".xml") == false)
				{
					filename_outputTransformed = filename_outputTransformed + ".xml";
				}
				
				source = new StreamSource(new File(Common.logDir + File.separator + filename_outputImported));
				destination = new StreamResult(new File(Common.logDir + File.separator + filename_outputTransformed));
				xslt = new StreamSource(new File(getXSLTPath() + getXSLTFilename()));
				
				try
				{
					transformer = fact.newTransformer(xslt);
					transformer.transform(source, destination);
					JXMLDocument doc = new JXMLDocument(Common.logDir + File.separator + filename_outputTransformed);
					data = doc.getDocument();
				}
				catch (TransformerConfigurationException e)
				{
					logger.error(e.getMessage());
					Common.emailqueue.addToQueue("Error", "Error Map [" + map.getId() + "]", e.getMessage() + "\n\n", filename_outputImported);
				}
				catch (TransformerException e)
				{
					logger.error(e.getMessage());
					Common.emailqueue.addToQueue("Error", "Error Map [" + map.getId() + "]", e.getMessage() + "\n\n", filename_outputImported);
				}

			}
		}

		connector.processOutboundData(getOutputPath(), filename, data);

	}

}
