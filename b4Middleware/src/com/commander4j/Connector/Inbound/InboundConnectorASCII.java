package com.commander4j.Connector.Inbound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.sys.Common;
import com.commander4j.sys.FixedASCIIColumns;
import com.commander4j.sys.FixedASCIIData;
import com.commander4j.util.JFileIO;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;

public class InboundConnectorASCII extends InboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((InboundConnectorCSV.class));
	JFileIO jfileio = new JFileIO();
    private LinkedList<FixedASCIIColumns> parseCols = new LinkedList<FixedASCIIColumns>();

	public InboundConnectorASCII(InboundInterface inter)
	{
		super(Connector_ASCII, inter);
	}

	private int getPatternColumnCount()
	{
		return parseCols.size();
	}
	
	private void parsePattern(String pattern)
	{
		parseCols.clear();
		String[] one = pattern.split(",");
		for (int x=0;x<one.length;x++)
		{
			String two = one[x];
			String[] three = two.split("-");
			int start = Integer.valueOf(three[0]);
			int end = Integer.valueOf(three[1]);
			FixedASCIIColumns col = new FixedASCIIColumns();
			col.position = x;
			col.start=start;
			col.end=end;
			parseCols.add(col);
		}
	}
	
	private LinkedList<FixedASCIIData> getASCIIColumnData(String line)
	{
		LinkedList<FixedASCIIData> result = new LinkedList<FixedASCIIData>();
		
		if (parseCols.size()>0)
		{
			if (line.length()>0)
			{
				for (int x=0;x<parseCols.size();x++)
				{
					int firstcol = parseCols.get(x).start;
					int lastcol = parseCols.get(x).end;
					
					FixedASCIIData data = new FixedASCIIData();
					
					data.columnId=x+1;
					data.columnData=line.substring(firstcol-1, lastcol);
					result.addLast(data);
				}
			}
		}
		
		return result;
	}
	
	@Override
	public boolean connectorLoad(String fullFilename)
	{
		long row = 0;
		
		parsePattern(getInboundInterface().getInputPattern());

		logger.debug("connectorLoad [" + fullFilename + "]");

		try
		{
			
			String filename_BACKUP = Common.logDir + java.io.File.separator + "Inbound Backup of " + getType() + " " + (new File(fullFilename)).getName();
			logger.debug("connectorLoad Backup [" + fullFilename + "] to [" + filename_BACKUP + "]");
			FileUtils.copyFile(new File(fullFilename), new File(filename_BACKUP));
		} catch (Exception ex)
		{
			logger.error("connectorLoad unable to backup [" + fullFilename + "]");
		}

		boolean result = true;

		String line = null;

		try
		{

			FileReader fileReader = new FileReader(fullFilename);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			data = builder.newDocument();

			Element message = (Element) data.createElement("data");
			message.setAttribute("type", Connector_ASCII);

			row=0;
			while ((line = bufferedReader.readLine()) != null)
			{
				row++;
				Element xmlrow = (Element) data.createElement("row");
				xmlrow.setAttribute("id", String.valueOf(row));
				xmlrow.setNodeValue(String.valueOf(row));
				
				System.out.println(line);
				
				LinkedList<FixedASCIIData>  nextLine = getASCIIColumnData(line);
				
				for (int x = 0; x < getPatternColumnCount(); x++)
				{
					
					System.out.println(nextLine.get(x).columnId);
					System.out.println(nextLine.get(x).columnData);
					
					Element xmlcol = addElement(data, "col", nextLine.get(x).columnData);
					xmlcol.setAttribute("id", String.valueOf(nextLine.get(x).columnId));
					xmlcol.setNodeValue(nextLine.get(x).columnData);
					xmlrow.appendChild(xmlcol);

				}
				message.appendChild(xmlrow);
			}

			bufferedReader.close();
			
			message.setAttribute("type", Connector_ASCII);
			message.setAttribute("cols", String.valueOf(getPatternColumnCount()));
			message.setAttribute("rows", String.valueOf(row));
			message.setAttribute("filename", (new File(fullFilename)).getName());
			
			data.appendChild(message);
						
		} catch (FileNotFoundException ex)
		{
			System.out.println("Unable to open file '" + fullFilename + "'");
		} catch (IOException ex)
		{
			System.out.println("Error reading file '" + fullFilename + "'");

		} catch (ParserConfigurationException ex)
		{
			
		}


		return result;
	}

}
