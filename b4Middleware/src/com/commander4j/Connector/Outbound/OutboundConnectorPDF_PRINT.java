package com.commander4j.Connector.Outbound;

import java.awt.print.PrinterJob;
import java.io.File;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorPDF_PRINT extends OutboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundConnectorPDF_PRINT.class));
	JFileIO jfileio = new JFileIO();

	public OutboundConnectorPDF_PRINT(OutboundInterface inter)
	{
		super(Connector_PDF_PRINT, inter);
	}

	@Override
	public boolean connectorSave(String path, String prefix, String filename)
	{
		boolean result = false;
		String errorMessage = "";
		String fullPath = path + File.separator + filename;

		JXMLDocument document = new JXMLDocument();
		document.setDocument(getData());

		String inputFilename = Utility.replaceNullStringwithBlank(document.findXPath("//pdf_print/inputFilename").trim());

		String outputFilename = path;

		if (outputFilename.endsWith(File.separator))
		{
			outputFilename = outputFilename + filename;
		}
		else
		{
			outputFilename = outputFilename + File.separator + filename;
		}

		logger.error("connectorLoad " + getType() + " inputFilename" + inputFilename);
		logger.error("connectorLoad " + getType() + " outputFilename" + outputFilename);

		try
		{
			FileUtils.deleteQuietly(new File(outputFilename));
			FileUtils.moveFileToDirectory(new File(inputFilename), new File(path), false);

			logger.debug("Printing PDF file called :" + outputFilename);

			if (getOutboundInterface().getQueueName().equals(""))
			{
				logger.debug("No print queue specified - getting default queue.");

				PrintService service = PrintServiceLookup.lookupDefaultPrintService();

				if (service != null)
				{
					getOutboundInterface().setQueueName(service.getName());
					logger.debug("Using default print queue : "+getOutboundInterface().getQueueName());

				}
				else
				{
					logger.debug("No default print service found");
				}
			}
			else
			{
				logger.debug(("Requested Queue Name    : " + getOutboundInterface().getQueueName()));
			}

			if (getOutboundInterface().getQueueName().equals("") == false)
			{

				PDDocument pdfdocument = PDDocument.load(new File(outputFilename));

				PrinterJob printerjob = PrinterJob.getPrinterJob();
				
				String validQueueNames="";

				for (PrintService printService : PrinterJob.lookupPrintServices())
				{
					logger.debug("Found printer " + printService);
					
					validQueueNames = validQueueNames + "["+printService.getName()+"]\n";
					
					if (getOutboundInterface().getQueueName().equalsIgnoreCase(printService.getName()))
					{
						logger.debug("Found specified printer queue. "+getOutboundInterface().getQueueName());
						printerjob.setPrintService(printService);
						printerjob.setPageable(new PDFPageable(pdfdocument));
						printerjob.print();
						FileUtils.deleteQuietly(new File(outputFilename));
						
						result = true;
					}
				}

				if (result==false)
				{
					errorMessage = "\n\nValid queues are \n\n"+validQueueNames;
				}

			}
			else
			{
				logger.debug("Unable to find print queue ");
			}
			
			if (result==false)
			{
				Common.emailqueue.addToQueue("Error", "Error printing " + getType(), "Unable to print "+ filename + " to ["+getOutboundInterface().getQueueName()+"]"+errorMessage, "");
			}
			
		}
		catch (Exception e)
		{
			logger.error("connectorLoad " + getType() + " " + e.getMessage());
			Common.emailqueue.addToQueue("Error", "Error printing " + getType(), "connectorSave " + getType() + " " + e.getMessage() + "\n\n" + fullPath, "");
		}

		document = null;

		return result;
	}

}
