package com.commander4j.Interface.Mapping;

import java.util.LinkedList;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.util.Utility;

public class Map {
	boolean enabled = true;
	InboundInterface inboundInterface;
	LinkedList<OutboundInterface> outboundInterface = new LinkedList<OutboundInterface>();
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((Map.class));

	private String id="";
	private String description="";
	private Long inboundMapMsgCount = (long) 0;
	private Long outboundMapMsgCount = (long) 0;
	
	public void run()
	{

	}
	
	public Long getInboundMapMessageCount()
	{
		return inboundMapMsgCount;
	}
	
	public Long getOutboundMapMessageCount()
	{
		return outboundMapMsgCount;
	}	
	
	public String toString()
	{
		int OutboundIntCount = getNumberofOutboundInterfaces();
		String outboundTypeList="";
		
		if (OutboundIntCount>0)
		{
			for (int x=0;x<OutboundIntCount;x++)
			{
				getOutBoundInterface(x).getType();
				if (x>0)
				{
					outboundTypeList = outboundTypeList + "\\";
				}
				outboundTypeList=getOutBoundInterface(x).getType();
				
			}
		}
		
		return Utility.padString(getId(),true,10," ") + "  " + Utility.padString(getDescription(),true,40," ") + "  "+Utility.padString(getInboundInterface().getType(),true,5," ") + "  "+Utility.padString(outboundTypeList,true,12," ") +Utility.padString(getInboundMapMessageCount().toString(),false,8," ")+" "+Utility.padString(getOutboundMapMessageCount().toString(),false,8," ") +"  "+getInboundInterface().getXSLTFilename();
	}
	
	
	public void setId(String ID)
	{
		id = ID;
	}
	
	public void setDescription(String desc)
	{
		description = desc;
	}

	public String getId()
	{
		return id;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setEnabled(boolean yesno)
	{
		logger.debug("setEnabled "+String.valueOf(yesno));
		if (yesno==true)
		{
			for (int x = 0; x < outboundInterface.size(); x++)
			{
				outboundInterface.get(x).setEnabled(yesno);
			}
			this.inboundInterface.setEnabled(yesno);
		} else
		{
			this.inboundInterface.setEnabled(yesno);
			for (int x = 0; x < outboundInterface.size(); x++)
			{
				outboundInterface.get(x).setEnabled(yesno);
			}
		}

	}
	
	public InboundInterface getInboundInterface()
	{
		return this.inboundInterface;
	}

	public int getNumberofOutboundInterfaces()
	{
		int result = outboundInterface.size();
		return result;
	}
	
	public OutboundInterface getOutBoundInterface(int index)
	{
		return outboundInterface.get(index);
	}
	
	public void setInboundInterface(InboundInterface inint)
	{
		this.inboundInterface = inint;
	}

	public void addOutboundInterface(OutboundInterface outint)
	{
		outboundInterface.addLast(outint);
	}

	public void processMapToOutboundInterface(String filename, OutboundInterface outint,Document data)
	{
		logger.debug(">> processMapToOutboundInterface [" + filename + " - " +  Utility.getStringFromDocument(data)+"]");
		outboundMapMsgCount++;
		logger.debug(">> outboundMapMessages count [" + getOutboundMapMessageCount().toString()+"]");
		outint.processInterfaceToConnector(filename,data);
	}
	
	public void processInboundInterfaceToMap(String filename,Document data)
	{

		logger.debug("<< processInboundInterfaceToMap  [" + filename + " - " + Utility.getStringFromDocument(data)+"]");
		inboundMapMsgCount++;
		logger.debug(">> inboundMapMessages count [" + getInboundMapMessageCount().toString()+"]");
		for (int x = 0; x < outboundInterface.size(); x++)
		{
			processMapToOutboundInterface(filename,outboundInterface.get(x),data);

		}

	}

}
