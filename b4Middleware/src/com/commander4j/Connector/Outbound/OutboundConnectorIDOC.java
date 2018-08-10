package com.commander4j.Connector.Outbound;

import com.commander4j.Interface.Outbound.OutboundInterface;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorIDOC extends OutboundConnectorABSTRACT
{

	public OutboundConnectorIDOC(OutboundInterface inter)
	{
		super(Connector_IDOC, inter);
	}

	@Override
	public boolean connectorSave(String path,String filename)
	{
		boolean result = false;

		return result;
	}

}
