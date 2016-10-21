package com.commander4j.Connector.Outbound;

import java.io.FileWriter;
import java.util.LinkedList;

import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.Logger;

import com.commander4j.Connector.Inbound.InboundConnectorCSV;
import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.sys.Common;
import com.commander4j.sys.FixedASCIIColumns;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorASCII extends OutboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((InboundConnectorCSV.class));
	JFileIO jfileio = new JFileIO();
	private LinkedList<FixedASCIIColumns> parseCols = new LinkedList<FixedASCIIColumns>();
	private int maxColumn = 0;

	public OutboundConnectorASCII(OutboundInterface inter)
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
		for (int x = 0; x < one.length; x++)
		{
			String two = one[x];
			String[] three = two.split("-");
			int start = Integer.valueOf(three[0]);
			int end = Integer.valueOf(three[1]);
			if (end > maxColumn)
			{
				maxColumn = end;
			}
			FixedASCIIColumns col = new FixedASCIIColumns();
			col.position = x;
			col.start = start;
			col.end = end;
			parseCols.add(col);
		}
	}

	@Override
	public boolean connectorSave(String filename)
	{
		boolean result = false;

		parsePattern(getOutboundInterface().getOutputPattern());

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
					FileWriter fw = new FileWriter(filename + "." + getOutboundInterface().getOutputFileExtension().toLowerCase());

					// Read new row value from XML

					for (int r = 1; r <= rows; r++)
					{
						String rowdata = Utility.padSpace(maxColumn);
						char[] rowdataArray = rowdata.toCharArray();

						// Read new col value from XML

						for (int c = 1; c <= columns; c++)
						{
							if (c <= getPatternColumnCount())
							{
								// Get the data from the XML input
								String xpath = "//data/row[@id='" + String.valueOf(r) + "']/col[@id='" + String.valueOf(c) + "']";
								String dataString = Utility.replaceNullStringwithBlank(document.findXPath(xpath).trim());

								// Get the position of the data within the ASCII
								// file for this column.
								FixedASCIIColumns coldef = parseCols.get(c - 1);

								// Do we have data to put into columns
								if (dataString.isEmpty() == false)
								{
									// For each column in the data field
									for (int x = 0; x < dataString.length(); x++)
									{
										// Make sure that the data string fits
										// within output column range
										if ((coldef.start + x) <= coldef.end)
										{
											// Update character in output row
											rowdataArray[coldef.start + x - 1] = dataString.charAt(x);
										}
									}
								}
								logger.debug("row=[" + String.valueOf(r) + "] col=[" + String.valueOf(c) + "] data=[" + dataString + "]");
							} else
							{
								logger.debug("Igored row=[" + String.valueOf(r) + "] col=[" + String.valueOf(c) + "] - no column defined in config.xml");
							}
						}

						// Convert char array back into string
						String joinedString = String.copyValueOf(rowdataArray);
						joinedString = joinedString + "\n";
						// Write to output file
						fw.write(joinedString);
						fw.flush();
					}

					// Close output file
					fw.close();
					result=true;

				} catch (Exception ex)
				{
					logger.error(ex.getMessage());
					Common.emailqueue.addToQueue("Error", "Error Writing File ["+filename+"]", ex.getMessage()+"\n\n", "");

				}
			}
		}

		document = null;

		return result;
	}

}
