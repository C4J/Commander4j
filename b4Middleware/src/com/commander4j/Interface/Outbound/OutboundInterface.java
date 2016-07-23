package com.commander4j.Interface.Outbound;

import java.io.File;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.commander4j.Interface.Mapping.Map;
import com.commander4j.util.Utility;


public class OutboundInterface extends OutboundInterfaceABSTRACT  {
	
	Logger logger = Logger.getLogger(OutboundInterface.class);

	Document data;
	boolean enabled = false;
	
	public OutboundInterface(Map map,String description)
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

		logger.debug("processInterfaceToConnector [" + Utility.getStringFromDocument(data)+"]");
		connector.processOutboundData(getOutputPath()+File.separator+filename,data);

	}

}
