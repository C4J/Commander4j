package com.commander4j.Connector.Outbound;

import com.commander4j.Interface.Outbound.OutboundInterface;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorDB extends OutboundConnectorABSTRACT
{

	public OutboundConnectorDB(OutboundInterface inter)
	{
		super(Connector_DB, inter);
	}

	@Override
	public boolean connectorSave(String fullFilename)
	{
		boolean result = false;

		return result;
	}

}
