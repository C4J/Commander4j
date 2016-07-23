package com.commander4j.Connector.Inbound;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import INTERFACE.com.commander4j.Connector.InboundConnectorINTERFACE;

import com.commander4j.Interface.Inbound.InboundInterface;

public abstract class InboundConnectorABSTRACT implements InboundConnectorINTERFACE {
	private boolean enabled = true;
	private String type = "";
	private String filename = "";
	Logger logger = Logger.getLogger(InboundConnectorABSTRACT.class);

	protected Document data;

	private InboundInterface inint;

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

		setFilename(filename);
		if (connectorLoad(inint.getInputPath() + File.separator + filename))
		{
			connectorBackup(filename);
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

	public boolean connectorBackup(String filename)
	{
		Boolean result = false;
		File source = new File(inint.getInputPath() + File.separator + filename);
		File destination = new File(inint.getBackupPath() + File.separator + filename);
		logger.debug(inint.getDescription()+" Backup input file :"+source.getAbsolutePath()+" to : "+destination.getAbsolutePath());
		try
		{
			FileUtils.copyFile(source, destination);
			result = true;
		} catch (IOException e)
		{
			result = false;
		}

		return result;
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
