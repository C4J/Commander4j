package com.commander4j.Connector.Outbound;

import com.commander4j.Interface.Outbound.OutboundInterface;

public class OutboundConnectorASCII extends OutboundConnectorABSTRACT {

	public OutboundConnectorASCII(OutboundInterface inter)
	{
		super(Connector_ASCII,inter);
	}

	@Override
	public boolean connectorSave(String fullFilename)
	{
		boolean result = false;
		
		return result;
	}

}


