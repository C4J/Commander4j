package com.commander4j.Connector.Inbound;

import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.sys.Common;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
//import com.commander4j.util.JFileIO;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;

public class InboundConnectorCSV extends InboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((InboundConnectorCSV.class));
	private LinkedList<String> parseOptions = new LinkedList<String>();
	boolean disableQuotes = false;
	char seperator = ',';
	char quote = '"';

	public InboundConnectorCSV(InboundInterface inter)
	{
		super(Connector_CSV, inter);
	}

	private void getCSVOptions()
	{
		String options = getInboundInterface().getCSVOptions();
		String delimeter = getInboundInterface().getOptionDelimeter();
		parsePattern(options, delimeter);
	}

	private void parsePattern(String pattern, String delim)
	{
		parseOptions.clear();
		delim = "\\" + delim;
		String[] opts = pattern.split(delim);
		for (int x = 0; x < opts.length; x++)
		{
			String two = opts[x];
			String[] three = two.split("=");

			String opt = three[0];
			String val = three[1];

			switch (opt)
			{
			case "separator":
				seperator = val.charAt(0);
				break;
			case "quote":
				if (val.equals("none"))
				{
					disableQuotes = true;
				}
				else
				{
					quote = val.charAt(0);
					disableQuotes = false;
				}
				break;
			default:
				opt = "";
				break;
			}
		}
	}

	@Override
	public boolean connectorLoad(String fullFilename)
	{
		long row = 0;
		long col = 0;
		long maxcol = 0;

		logger.debug("connectorLoad [" + fullFilename + "]");
		boolean result = false;

		getCSVOptions();

		if (backupInboundFile(fullFilename))
		{

			try
			{
				result = true;
				CSVParser csvParser;
				CSVReader reader;
				if (disableQuotes)
				{
					csvParser = new CSVParserBuilder().withSeparator(seperator).withIgnoreQuotations(true).build();
					reader = new CSVReaderBuilder(new FileReader(fullFilename)).withCSVParser(csvParser).build();
				}
				else
				{
					csvParser = new CSVParserBuilder().withSeparator(seperator).withIgnoreQuotations(false).withQuoteChar(quote).build();
					reader = new CSVReaderBuilder(new FileReader(fullFilename)).withCSVParser(csvParser).build();
				}
				String[] nextLine;

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();

				data = builder.newDocument();

				Element message = (Element) data.createElement("data");
				message.setAttribute("type", Connector_CSV);

				while ((nextLine = reader.readNext()) != null)
				{
					row++;
					Element xmlrow = (Element) data.createElement("row");
					xmlrow.setAttribute("id", String.valueOf(row));
					xmlrow.setNodeValue(String.valueOf(row));

					for (int x = 0; x < nextLine.length; x++)
					{
						col = x + 1;
						if (col > maxcol)
							maxcol = col;

						Element xmlcol = addElement(data, "col", nextLine[x].trim());
						xmlcol.setAttribute("id", String.valueOf(col));
						xmlcol.setNodeValue(nextLine[x].trim());
						xmlrow.appendChild(xmlcol);

					}
					message.appendChild(xmlrow);
				}
				reader.close();

				message.setAttribute("type", Connector_CSV);
				message.setAttribute("cols", String.valueOf(maxcol));
				message.setAttribute("rows", String.valueOf(row));
				message.setAttribute("filename", (new File(fullFilename)).getName());

				data.appendChild(message);

				result = true;

			}
			catch (Exception ex)
			{
				result = false;
				logger.error("connectorLoad " + getType() + " " + ex.getMessage());
				Common.emailqueue.addToQueue("Error", "Error reading " + getType(), "connectorLoad " + getType() + " " + ex.getMessage() + "\n\n" + fullFilename, "");
			}
		}
		return result;
	}

}
