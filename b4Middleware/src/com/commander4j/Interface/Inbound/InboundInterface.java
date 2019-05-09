package com.commander4j.Interface.Inbound;

import java.io.File;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.commander4j.Interface.Mapping.Map;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Interface.InboundInterfaceABSTRACT;

public class InboundInterface extends InboundInterfaceABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((InboundInterface.class));
	JFileIO jfileio = new JFileIO();
	TransformerFactory fact = new net.sf.saxon.TransformerFactoryImpl();
	StreamSource source;
	StreamSource xslt;
	StreamResult destination;
	Transformer transformer;
	Boolean writeSuccess;
	Boolean loadFileResult;
	String filename_imported;
	String filename_transformed;

	public InboundInterface(Map map, String description)
	{
		super(map);
		setDescription(description);
	}

	boolean enabled = false;

	@Override
	public void run()
	{
		try
		{
			File dir = new File(getInputPath());
			String[] extensions = getInputFileMask();
			String prefix = getPrefix();

			if (extensions.length > 0)
			{
				if (extensions[0].equals("*"))
				{
					extensions = null;
				}
			}

			List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, false);
			if (files.size() > 0)
			{
				for (File file : files)
				{
					if (file.length() > 0)
					{

						if (file.isHidden() == false)
						{

							if (file.getName().toLowerCase().startsWith(prefix.toLowerCase()))
							{

								logger.debug("Processing [" + file.getName() + "]");
								loadFileResult = true;
								if (connector.processInboundFile(file.getName()))
								{
									data = connector.getData();

									filename_imported = Utility.getCurrentTimeStampString() + " INPUT_IMPORTED_" + connector.getType() + "_" + getId() + "_" + file.getName();

									if (filename_imported.endsWith(".xml") == false)
									{
										filename_imported = filename_imported + ".xml";
									}

									writeSuccess = jfileio.writeToDisk(Common.logDir, data, filename_imported);

									if (getXSLTFilename().equals("") == false)
									{

										filename_transformed = Utility.getCurrentTimeStampString() + " INPUT_TRANSFORMED_" + connector.getType() + "_" + getId() + "_" + file.getName();

										if (filename_transformed.endsWith(".xml") == false)
										{
											filename_transformed = filename_transformed + ".xml";
										}

										source = new StreamSource(new File(Common.logDir + File.separator + filename_imported));
										destination = new StreamResult(new File(Common.logDir + File.separator + filename_transformed));
										xslt = new StreamSource(new File(getXSLTPath() + getXSLTFilename()));

										try
										{
											transformer = fact.newTransformer(xslt);
											transformer.transform(source, destination);
											JXMLDocument doc = new JXMLDocument();
											loadFileResult = doc.setDocument(Common.logDir + File.separator + filename_transformed);
											data = doc.getDocument();
										}
										catch (TransformerConfigurationException e)
										{
											logger.error("Error Map [" + map.getId() + "] " + e.getMessage() + "\n\n" + file.getAbsolutePath());
											Common.emailqueue.addToQueue("Error", "Error Map [" + map.getId() + "]", e.getMessage() + "\n\n", file.getAbsolutePath());
										}
										catch (TransformerException e)
										{
											logger.error("Error Map [" + map.getId() + "] " + e.getMessage() + "\n\n" + file.getAbsolutePath());
											Common.emailqueue.addToQueue("Error", "Error Map [" + map.getId() + "]", e.getMessage() + "\n\n", file.getAbsolutePath());
										}
									}

									if (writeSuccess && loadFileResult)
									{
										processConnectorToInterfaceData(connector.getFilename(), data);
									}
									else
									{
										if (writeSuccess == false)
										{
											logger.error("Error Map [" + map.getId() + "] Unable to save inbound xml" + " " + filename_imported);
											Common.emailqueue.addToQueue("Error", "Error Map [" + map.getId() + "]", "Unable to save inbound xml" + "\n\n", filename_imported);
										}
										else
										{
											logger.error("Error Map [" + map.getId() + "] Unable to load inbound xml" + " " + filename_imported);
											Common.emailqueue.addToQueue("Error", "Error Map [" + map.getId() + "]", "Unable to load inbound xml" + "\n\n", filename_imported);
										}
									}
								}
							}
						}
					}
				}
			}
			files = null;
			dir = null;

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			
			logger.error("InboundInterface Map [" + map.getId() + "] error :" + ex.getMessage());
			Common.emailqueue.addToQueue("Error", "InboundInterface Map [" + map.getId() + "]", " error :" + ex.getMessage(), "");
		}

	}

	public void processConnectorToInterfaceData(String filename, Document data)
	{
		this.data = data;

		logger.debug("processConnectorToInterfaceData [" + Utility.getStringFromDocument(data) + "]");
		map.processInboundInterfaceToMap(filename, data);

	}

}
