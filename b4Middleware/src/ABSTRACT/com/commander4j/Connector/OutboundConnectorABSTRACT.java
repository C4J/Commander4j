package ABSTRACT.com.commander4j.Connector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;

import org.apache.logging.log4j.Logger;

import INTERFACE.com.commander4j.Connector.OutboundConnectorINTERFACE;

import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

public abstract class OutboundConnectorABSTRACT implements OutboundConnectorINTERFACE
{
	private boolean enabled = true;
	private String type = "";
	private String filename = "";
	private String path = "";

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

	public void processOutboundData(String path, String filename, Document data)
	{
		outboundConnectorCount++;

		setData(data);
		setPath(path);
		setFilename(filename);

		// ==================

		boolean saveFile = true;

		String comparitor = getOutboundInterface().getComparator();

		if (comparitor.equals("") == false)
		{
			// We need to do a comparison
			System.out.println("Comparitor =" + comparitor);

			JXMLDocument document = new JXMLDocument();
			document.setDocument(getData());

			// Determine parm1 value
			String parm1value = "";
			String parm1type = getOutboundInterface().getCompareParam1_Type();
			System.out.println("parm1type =" + parm1type);

			if (parm1type.equals("xquery"))
			{
				parm1value = Utility.replaceNullStringwithBlank(document.findXPath(getOutboundInterface().getCompareParam1()).trim());

			}

			if (parm1type.equals("literal"))
			{
				parm1value = getOutboundInterface().getCompareParam1();
			}
			System.out.println("parm1value =" + parm1value);

			// Determine parm2 value
			String parm2value = "";
			String parm2type = getOutboundInterface().getCompareParam2_Type();
			System.out.println("parm2type =" + parm2type);

			if (parm2type.equals("xquery"))
			{
				parm2value = Utility.replaceNullStringwithBlank(document.findXPath(getOutboundInterface().getCompareParam2()).trim());

			}

			if (parm2type.equals("literal"))
			{
				parm2value = getOutboundInterface().getCompareParam2();
			}
			System.out.println("parm2value =" + parm2value);

			if (comparitor.equals("NOT EQUAL"))
			{
				logger.debug("Comparing parm1value [" + parm1value + "] with parm2value [" + parm2value + "]");
				if (parm1value.equals(parm2value))
				{
					System.out.println("SAME");
					saveFile = false;
				}
				else
				{
					System.out.println("DIFFERENT");
				}
			}

			if (comparitor.equals("EQUAL"))
			{
				if (parm1value.equals(parm2value) == false)
				{
					saveFile = false;
				}
			}
		}

		if (saveFile == true)
		{

			while (connectorSave(getPath(), outint.getPrefix() + getFilename()) == false)
			{
				logger.error("processOutboundData - remote path unavailable [" + getPath(), outint.getPrefix() + getFilename() + "] - waiting 20 seconds before retry");
				com.commander4j.util.JWait.milliSec(20000);
			}

		}

	}

	public OutboundConnectorABSTRACT(String type, OutboundInterface outer)
	{
		this.type = type;
		this.outint = outer;
	}

	public String getPathFilename()
	{
		return getPath() + File.separator + getFilename();
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

	private void setPath(String path)
	{
		this.path = path;
	}

	public String getPath()
	{
		return this.path;
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
