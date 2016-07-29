package ABSTRACT.com.commander4j.Connector;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.Interface.Inbound.InboundInterface;

import INTERFACE.com.commander4j.Connector.InboundConnectorINTERFACE;

public abstract class InboundConnectorABSTRACT implements InboundConnectorINTERFACE {
	private boolean enabled = true;
	private String type = "";
	private String filename = "";
	Logger logger = Logger.getLogger(InboundConnectorABSTRACT.class);
	private Long inboundConnectorMessageCount = (long) 0;

	protected Document data;

	private InboundInterface inint;

	public Long getInboundConnectorMessageCount()
	{
		return inboundConnectorMessageCount;
	}
	
	public InboundInterface getInboundInterface()
	{
		return inint;
	}

	public InboundConnectorABSTRACT(String type, InboundInterface inter)
	{
		this.type = type;
		this.inint = inter;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public boolean getEnabled()
	{
		return this.enabled;
	}

	public Boolean processInboundFile(String filename)
	{
		Boolean result = false;
		
		inboundConnectorMessageCount++;
		
		setFilename(filename);
		if (connectorLoad(inint.getInputPath() + File.separator + filename))
		{
			result = true;
			connectorDelete(filename);
		}

		return result;

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


	public boolean connectorDelete(String filename)
	{
		Boolean result = false;
		File source = new File(inint.getInputPath() + File.separator + filename);

		try
		{
			FileUtils.deleteQuietly(source);
			logger.debug(inint.getDescription()+" Delete input file :"+source.getAbsolutePath());
			result = true;
		} catch (Exception e)
		{
			result = false;
		}

		return result;
	}

}
