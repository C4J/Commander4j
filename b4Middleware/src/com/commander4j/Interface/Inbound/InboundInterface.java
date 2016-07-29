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
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.commander4j.Interface.Mapping.Map;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Interface.InboundInterfaceABSTRACT;

public class InboundInterface extends InboundInterfaceABSTRACT {

	Logger logger = Logger.getLogger(InboundInterface.class);
    JFileIO jfileio = new JFileIO();
	TransformerFactory fact = new net.sf.saxon.TransformerFactoryImpl();
	StreamSource source;
	StreamSource xslt;
	StreamResult destination;
	Transformer transformer;
    
	public InboundInterface(Map map,String description)
	{
		super(map);
		setDescription(description);

	}

	boolean enabled = false;
	
	@Override
	public void run()
	{

		File dir = new File(getInputPath());
		String[] extensions = new String[] { getInputFileMask() };
		
		List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
		for (File file : files) {
			logger.debug("Processing [" + file.getName()+"]");
			if (connector.processInboundFile(file.getName()))
			{
				data = connector.getData();
				
				String filename_imported = "Inbound "+connector.getType()+" Formatted to XML ("+map.getId()+" - "+getId()+ ")" + file.getName()+".xml";
				
				String filename_transformed = "Inbound "+connector.getType()+" XSLT Transformed ("+map.getId()+" - "+getId()+ ")"+ file.getName()+".xml";
				
				//String filename_transformed = "Inbound_XSLT_"+connector.getType()+"_"+map.getId()+"_"+getId()+ file.getName()+".xml";
				
				jfileio.writeToDisk(Common.logDir, data, filename_imported);
				
				source = new StreamSource(new File(Common.logDir+File.separator+filename_imported));
				destination = new StreamResult(new File(Common.logDir+File.separator+filename_transformed));
				xslt = new StreamSource(new File(getXSLTPath()+getXSLTFilename()));

				try
				{
					transformer =  fact.newTransformer(xslt);
					transformer.transform(source,destination);
					JXMLDocument doc = new JXMLDocument(Common.logDir+File.separator+filename_transformed);
					data = doc.getDocument();
				} catch (TransformerConfigurationException e)
				{
					logger.error(e.getMessage());
					e.printStackTrace();
				} catch (TransformerException e)
				{
					logger.error(e.getMessage());
					e.printStackTrace();
				}
	
				processConnectorToInterfaceData(connector.getFilename(), data);
			}
		}
	}

	public void processConnectorToInterfaceData(String filename,Document data)
	{
		this.data = data;

		logger.debug("processConnectorToInterfaceData [" + Utility.getStringFromDocument(data)+"]");
		map.processInboundInterfaceToMap(filename,data);

	}

}
