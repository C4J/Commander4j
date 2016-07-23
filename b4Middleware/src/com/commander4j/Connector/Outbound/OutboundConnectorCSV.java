package com.commander4j.Connector.Outbound;

import org.apache.log4j.Logger;

import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

public class OutboundConnectorCSV extends OutboundConnectorABSTRACT {

	Logger logger = Logger.getLogger(OutboundConnectorCSV.class);

	public OutboundConnectorCSV(OutboundInterface inter)
	{
		super(Connector_CSV, inter);
	}

	@Override
	public boolean connectorSave(String filename)
	{
		boolean result = false;

		logger.debug("connectorSave [" + filename + "." + getType().toLowerCase() + "]");

		JXMLDocument document = new JXMLDocument();

		String cl = Utility.replaceNullStringwithBlank(document.findXPath("//data/@cols").trim());
		if (cl.equals(""))
		{
			cl = "0";
		}
		int columns = Integer.valueOf(cl);

		String rw = Utility.replaceNullStringwithBlank(document.findXPath("//data/@rows").trim());
		if (rw.equals(""))
		{
			rw = "0";
		}
		int rows = Integer.valueOf(rw);

		if (rows > 0)
		{
			if (columns > 0)
			{

			}
		}

		document.setDocument(getData());

		return result;
	}

}
