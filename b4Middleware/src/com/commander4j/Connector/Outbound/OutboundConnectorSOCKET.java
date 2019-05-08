package com.commander4j.Connector.Outbound;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.sys.Common;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorSOCKET extends OutboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundConnectorSOCKET.class));

	public OutboundConnectorSOCKET(OutboundInterface inter)
	{
		super(Connector_SOCKET, inter);
	}

	@Override
	public boolean connectorSave(String path, String prefix, String filename)
	{
		boolean result = false;
		String errorDescription = "";

		filename = getOutboundInterface().get83GUIDFilename(prefix, filename);

		String fullPath = path + File.separator + filename;

		logger.debug("connectorSave [" + fullPath + "." + getOutboundInterface().getOutputFileExtension().toLowerCase() + "]");

		try
		{

			if (fullPath.endsWith("." + getType().toLowerCase()) == false)
			{
				fullPath = fullPath + "." + getType().toLowerCase();
			}

			String tempFilename = fullPath + ".tmp";
			String finalFilename = fullPath;

			Socket socket;
			String send = "";
			String eol = "";
			PrintWriter out;

			FileWriter fw = new FileWriter(tempFilename);
			try
			{
				JXMLDocument document = new JXMLDocument();
				document.setDocument(getData());

				if (document.findXPath("//data/line[contains(text(),'*EOF*')]").equals("*EOF*"))
				{

					socket = new Socket(getOutboundInterface().getHostIP(), getOutboundInterface().getHostPort());
					out = new PrintWriter(socket.getOutputStream(), true);

					for (int rep = 0; rep < getOutboundInterface().getHostRepeat(); rep++)
					{

						int line = 1;
						while (Utility.replaceNullStringwithBlank(document.findXPath("/data/line[" + String.valueOf(line) + "]")).equals("*EOF*") == false)
						{
							send = Utility.replaceNullStringwithBlank(document.findXPath("/data/line[" + String.valueOf(line) + "]"));
							eol = Utility.replaceNullStringwithBlank(document.findXPath("/data/line[" + String.valueOf(line) + "]/@eol"));

							if (eol.equals("") == false)
							{
								eol = eol.replace("cr", "\r");
								eol = eol.replace("lf", "\n");
							}

							if (send.equals("*EOF*") == false)
							{
								out.print(send + eol);
								out.flush();
								fw.write(send + "\n");
								fw.flush();
								logger.debug(send);
							}
							line++;
						}
					}

					out.close();
					socket.close();

					result = true;
				}
				else
				{

					errorDescription = "Missing *EOF* in input file";
				}

			}
			catch (Exception e)
			{
				errorDescription = e.getMessage();
				fw.write("*ERROR*\n");
				fw.write(e.getMessage());

			}
			finally
			{
				fw.flush();
				fw.close();
				send = null;
			}

			FileUtils.deleteQuietly(new File(finalFilename));
			FileUtils.moveFile(new File(tempFilename), new File(finalFilename));

			tempFilename = null;
			finalFilename = null;

		}
		catch (Exception ex)
		{
			errorDescription = ex.getMessage();
		}

		if (result == false)
		{
			logger.error(errorDescription);
			Common.emailqueue.addToQueue("Error", "Error writing to socket [" + fullPath + "]", errorDescription + "\n\n", "");
		}

		return result;
	}

}
