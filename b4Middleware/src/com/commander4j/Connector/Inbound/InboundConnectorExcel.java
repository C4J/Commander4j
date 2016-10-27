package com.commander4j.Connector.Inbound;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.w3c.dom.Element;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;

public class InboundConnectorExcel extends InboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((InboundConnectorExcel.class));
	JFileIO jfileio = new JFileIO();

	public InboundConnectorExcel(InboundInterface inter)
	{
		super(Connector_Excel, inter);
	}

	@Override
	public boolean connectorLoad(String fullFilename)
	{

		logger.debug("connectorLoad [" + fullFilename + "]");
		boolean result = false;

		//backupInboundFile(fullFilename);

		if (backupInboundFile(fullFilename))
		{
			
			   try {
				    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fullFilename));
				    HSSFWorkbook wb = new HSSFWorkbook(fs);
				    HSSFSheet sheet = wb.getSheetAt(0);
				    HSSFRow excelRow;
				    HSSFCell cell;

				    //Create XML Document
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					data = builder.newDocument();
					Element message = (Element) data.createElement("data");
					message.setAttribute("type", Connector_Excel);
				    
					//Determine number of rows and columns.
				    int rows = sheet.getPhysicalNumberOfRows();
				    int cols = 0;
				    int tmp = 0;
				    
				    for(int i = 0; i < 10 || i < rows; i++) {
				        excelRow = sheet.getRow(i);
				        if(excelRow != null) {
				            tmp = sheet.getRow(i).getPhysicalNumberOfCells();
				            if(tmp > cols) cols = tmp;
				        }
				    }
				    
				    logger.debug("Spreadsheet rows = "+String.valueOf(rows));
				    logger.debug("Spreadsheet cols = "+String.valueOf(cols));

				    for(int currentRow = 0; currentRow < rows; currentRow++) {
				        excelRow = sheet.getRow(currentRow);
				        if(excelRow != null) {
				        	
							Element xmlrow = (Element) data.createElement("row");
							xmlrow.setAttribute("id", String.valueOf(currentRow+1));
							xmlrow.setNodeValue(String.valueOf(currentRow));
							System.out.println("currentRow="+String.valueOf(currentRow));
							
				            for(int currentColumn = 0; currentColumn < cols; currentColumn++) {
				                cell = excelRow.getCell((short)currentColumn);
				                if(cell != null) {
				                	
				                	
									Element xmlcol = addElement(data, "col", cell.toString());
									xmlcol.setAttribute("id", String.valueOf(currentColumn+1));
									xmlcol.setNodeValue(cell.toString());
									
									xmlrow.appendChild(xmlcol);
									System.out.println("row="+String.valueOf(currentRow+1)+","+"column="+String.valueOf(currentColumn+1)+","+"value="+cell.toString());
				                }
				            }
				            message.appendChild(xmlrow);
				        }
				    }
				    
				    wb.close();
				    
					message.setAttribute("type", Connector_Excel);
					message.setAttribute("cols", String.valueOf(cols));
					message.setAttribute("rows", String.valueOf(rows));
					message.setAttribute("filename", (new File(fullFilename)).getName());

					data.appendChild(message);
					
					result = true;
					
				} catch(Exception ex) {
					result = false;
					logger.error("connectorLoad " + getType() + " " + ex.getMessage());
					Common.emailqueue.addToQueue("Error", "Error reading "+getType(), "connectorLoad " + getType() + " " + ex.getMessage()+"\n\n"+fullFilename, "");
				}

		}
		return result;
	}

}
