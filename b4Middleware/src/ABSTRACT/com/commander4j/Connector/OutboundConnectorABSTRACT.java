package ABSTRACT.com.commander4j.Connector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.apache.logging.log4j.Logger;

import INTERFACE.com.commander4j.Connector.OutboundConnectorINTERFACE;

import com.commander4j.Interface.Outbound.OutboundInterface;

public abstract class OutboundConnectorABSTRACT implements OutboundConnectorINTERFACE
{
	private boolean enabled = true;
	private String type = "";
	private String filename = "";

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundConnectorABSTRACT.class));

	private Long outboundConnectorCount = (long) 0;

	protected Document data;

	private OutboundInterface outint;

	public Long getOutboundConnectorCount()
	{
		return outboundConnectorCount;
	}

	public OutboundInterface getOutboundInterface()
	{
		return outint;
	}

	public void processOutboundData(String filename, Document data)
	{
		outboundConnectorCount++;

		setData(data);
		setFilename(filename);
		while (connectorSave(filename)==false)
		{
			logger.error("processOutboundData - remote path unavailable ["+filename+"] - waiting 20 seconds before retry");
			com.commander4j.util.JWait.milliSec(20000);
		}
	}

	public OutboundConnectorABSTRACT(String type, OutboundInterface outer)
	{
		this.type = type;
		this.outint = outer;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public boolean getEnabled()
	{
		return this.enabled;
	}

	private void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getFilename()
	{
		return this.filename;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setData(Document data)
	{
		this.data = data;
	}

	public Document getData()
	{
		return data;
	}

	public Element addElement(Document doc, String name, String value)
	{
		Element temp = (Element) doc.createElement(name);
		Text temp_value = doc.createTextNode(value);
		temp.appendChild(temp_value);
		return temp;
	}

}
