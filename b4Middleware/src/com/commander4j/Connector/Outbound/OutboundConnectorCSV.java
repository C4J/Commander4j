package com.commander4j.Connector.Outbound;

import java.io.FileWriter;

import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.Logger;

import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;
import com.opencsv.CSVWriter;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorCSV extends OutboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundConnectorCSV.class));

	public OutboundConnectorCSV(OutboundInterface inter)
	{
		super(Connector_CSV, inter);
	}

	@Override
	public boolean connectorSave(String filename)
	{
		boolean result = false;

		logger.debug("connectorSave [" + filename + "." + getType().toLowerCase() + "]");

		JXMLDocument document = new JXMLDocument();
		document.setDocument(getData());

		String cl = Utility.replaceNullStringwithBlank(document.findXPath("//data/@cols").trim());
		if (cl.equals(""))
		{
			cl = "0";
		}
		int columns = Integer.valueOf(cl);

		String rw = Utility.replaceNullStringwithBlank(document.findXPath("//data/@rows").trim());
		if (rw.equals(""))
		{
			rw = "0";
		}
		int rows = Integer.valueOf(rw);

		String separator = Utility.replaceNullStringwithBlank(document.findXPath("//data/@separator").trim());
		char sep = separator.charAt(0);
		
		boolean disableQuotes = Boolean.valueOf(Utility.replaceNullStringwithBlank(document.findXPath("//data/@disableQuotes").trim()));
		
		try
		{
			System.out.println(document.documentToString(getData()));
		} catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (rows > 0)
		{
			if (columns > 0)
			{
				try
				{
					CSVWriter writer;
					if (disableQuotes)
					{
						writer =  new CSVWriter(new FileWriter(filename+"."+getType().toLowerCase() ), sep, CSVWriter.NO_QUOTE_CHARACTER);
					}
					else
					{
						writer = new CSVWriter(new FileWriter(filename+"."+getType().toLowerCase() ), sep);
					}

					String[] csvrow = new String[columns];
					for (int r = 1; r <= rows; r++)
					{
						String.valueOf(r);
						for (int c = 1; c <= columns; c++)
						{
							String xpath = "//data/row[@id='" + String.valueOf(r) + "']/col[@id='" + String.valueOf(c)
									+ "']";
							String data = Utility.replaceNullStringwithBlank(document.findXPath(xpath).trim());
							csvrow[c - 1] = data;
							logger.debug("row=["+ String.valueOf(r) +"] col=["+ String.valueOf(c) +"] data=["+data+"]");
						}

						writer.writeNext(csvrow);

					}
					writer.close();
				} catch (Exception ex)
				{

				}

			}
		}

		document.setDocument(getData());

		return result;
	}

}
