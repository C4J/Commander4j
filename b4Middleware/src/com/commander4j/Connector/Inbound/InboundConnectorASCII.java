package com.commander4j.Connector.Inbound;

import com.commander4j.Interface.Inbound.InboundInterface;

public class InboundConnectorASCII extends InboundConnectorABSTRACT {
    
	public InboundConnectorASCII(InboundInterface inter)
	{
		super(Connector_ASCII,inter);
	}
	
	@Override
	public boolean connectorLoad(String fullFilename)
	{
		// TODO Auto-generated method stub
		return false;
	}

}


