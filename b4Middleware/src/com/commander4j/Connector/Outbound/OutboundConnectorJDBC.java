package com.commander4j.Connector.Outbound;

import com.commander4j.Interface.Outbound.OutboundInterface;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorJDBC extends OutboundConnectorABSTRACT
{

	public OutboundConnectorJDBC(OutboundInterface inter)
	{
		super(Connector_DB, inter);
	}

	@Override
	public boolean connectorSave(String path,String filename)
	{
		boolean result = false;

		return result;
	}

}
