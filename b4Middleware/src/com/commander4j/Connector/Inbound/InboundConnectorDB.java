package com.commander4j.Connector.Inbound;

import com.commander4j.Interface.Inbound.InboundInterface;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;

public class InboundConnectorDB  extends InboundConnectorABSTRACT {
	
	
	public InboundConnectorDB(InboundInterface inter)
	{
		super(Connector_DB,inter);
	}

	@Override
	public boolean connectorLoad(String filename)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
