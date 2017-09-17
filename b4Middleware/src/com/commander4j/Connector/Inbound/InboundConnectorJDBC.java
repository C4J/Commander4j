package com.commander4j.Connector.Inbound;

import com.commander4j.Interface.Inbound.InboundInterface;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;

public class InboundConnectorJDBC extends InboundConnectorABSTRACT
{

	public InboundConnectorJDBC(InboundInterface inter)
	{
		super(Connector_JDBC, inter);
	}

	@Override
	public boolean connectorLoad(String filename)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
