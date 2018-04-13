package com.commander4j.thread;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : AutoLabellerThread.java
 * 
 * Package Name : com.commander4j.thread
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.opencsv.CSVWriter;

import com.commander4j.db.JDBUser;
import com.commander4j.db.JDBViewAutoLabellerPrinter;
import com.commander4j.sys.Common;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;
import com.commander4j.util.JWait;
import com.commander4j.xml.JXMLDocument;

public class AutoLabellerThread extends Thread {

	public boolean allDone = false;
	private String sessionID;
	private String hostID;
	private Boolean messageProcessedOK = false;
	private String messageError = "";
	private final Logger logger = Logger.getLogger(AutoLabellerThread.class);

	public String getHostID()
	{
		return hostID;
	}

	public void setHostID(String host)
	{
		hostID = host;
	}

	public AutoLabellerThread(String host)
	{
		super();
		setHostID(host);
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}

	public void run()
	{
		logger.debug("AutoLabeller Thread running");
		setSessionID(JUnique.getUniqueID());
		JDBUser user = new JDBUser(getHostID(), getSessionID());
		user.setUserId("interface");
		user.setPassword("interface");
		user.setLoginPassword("interface");
		Common.userList.addUser(getSessionID(), user);
		Common.sd.setData(getSessionID(), "silentExceptions", "Yes", true);

		Boolean dbconnected = false;

		if (Common.hostList.getHost(hostID).isConnected(sessionID) == false)
		{

			dbconnected = Common.hostList.getHost(hostID).connect(sessionID, hostID);

		} else
		{
			dbconnected = true;
		}

		if (dbconnected)
		{

			JDBViewAutoLabellerPrinter alp = new JDBViewAutoLabellerPrinter(getHostID(), getSessionID());
			LinkedList<JDBViewAutoLabellerPrinter> autolabellerList = new LinkedList<JDBViewAutoLabellerPrinter>();

			int noOfMessages = 0;

			while (true)
			{

				JWait.milliSec(500);

				if (allDone)
				{
					if (dbconnected)
					{
						Common.hostList.getHost(hostID).disconnect(getSessionID());
					}
					return;
				}

				autolabellerList.clear();
				autolabellerList = alp.getModifiedPrinterLines();
				noOfMessages = autolabellerList.size();

				if (noOfMessages > 0)
				{
					for (int x = 0; x < noOfMessages; x++)
					{
						JWait.milliSec(100);

						JDBViewAutoLabellerPrinter autolabview = autolabellerList.get(x);

						messageProcessedOK = true;
						messageError = "";

						if (autolabview.getPrinterObj().isEnabled())
						{
							logger.debug("Line             =" + autolabview.getAutoLabellerObj().getLine());
							logger.debug("Line Description =" + autolabview.getAutoLabellerObj().getDescription());
							logger.debug("Printer ID       =" + autolabview.getPrinterObj().getPrinterID());
							logger.debug("Printer Enabled  =" + autolabview.getPrinterObj().isEnabled());
							logger.debug("Export Path      =" + autolabview.getPrinterObj().getExportRealPath());
							logger.debug("Export Enabled   =" + autolabview.getPrinterObj().isExportEnabled());
							logger.debug("Export Format    =" + autolabview.getPrinterObj().getExportFormat());
							logger.debug("Direct Print     =" + autolabview.getPrinterObj().isDirectPrintEnabled());
							logger.debug("Printer Type     =" + autolabview.getPrinterObj().getPrinterType());
							logger.debug("Printer IP       =" + autolabview.getPrinterObj().getIPAddress());
							logger.debug("Printer Port     =" + autolabview.getPrinterObj().getPort());
							logger.debug("Process Order    =" + autolabview.getLabelDataObj().getProcessOrder());
							logger.debug("Material         =" + autolabview.getLabelDataObj().getMaterial());
							logger.debug("Module ID        =" + autolabview.getModuleObj().getModuleId());
							logger.debug("Module Type      =" + autolabview.getModuleObj().getType());


							if (autolabview.getPrinterObj().isExportEnabled())
							{
								String exportPath = JUtility.replaceNullStringwithBlank(JUtility.formatPath(autolabview.getPrinterObj().getExportRealPath()));
								if (exportPath.equals("") == false)
								{
									if (exportPath.substring(exportPath.length() - 1).equals(File.separator) == false)
									{
										exportPath = exportPath + File.separator;
									}
								} else
								{
									exportPath = Common.interface_output_path + "Auto Labeller" + File.separator;
								}

								String exportFilename = exportPath + JUtility.removePathSeparators(autolabview.getAutoLabellerObj().getLine()) + "_" + JUtility.removePathSeparators(autolabview.getPrinterObj().getPrinterID()) + "."
										+ autolabview.getPrinterObj().getExportFormat();
								
								logger.debug("Export Filename  =" + exportFilename);

								/* ================CSV================ */

								if (autolabview.getPrinterObj().getExportFormat().equals("CSV"))
								{
									try
									{
										PreparedStatement stmt = null;
										ResultSet rs;

										stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID())
												.prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("DBVIEW_AUTO_LABELLER_PRINTER.getProperties"));
										stmt.setString(1, autolabview.getAutoLabellerObj().getLine());
										stmt.setString(2, autolabview.getPrinterObj().getPrinterID());
										stmt.setFetchSize(50);

										rs = stmt.executeQuery();

										logger.debug("Writing CSV");
										
										CSVWriter writer = new CSVWriter(new FileWriter(exportFilename), CSVWriter.DEFAULT_SEPARATOR,CSVWriter.DEFAULT_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
										
										writer.writeAll(rs, true);

										rs.close();

										stmt.close();

										writer.close();
									} catch (Exception e)
									{
										messageProcessedOK = false;
										messageError = e.getMessage();
									}
								}

								/* ================XML================ */

								if (autolabview.getPrinterObj().getExportFormat().equals("XML"))
								{
									try
									{
										PreparedStatement stmt = null;
										ResultSet rs;

										stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID())
												.prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("DBVIEW_AUTO_LABELLER_PRINTER.getProperties"));
										stmt.setString(1, autolabview.getAutoLabellerObj().getLine());
										stmt.setString(2, autolabview.getPrinterObj().getPrinterID());
										stmt.setFetchSize(50);

										rs = stmt.executeQuery();
										ResultSetMetaData rsmd = rs.getMetaData();
										int colCount = rsmd.getColumnCount();

										DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
										DocumentBuilder builder = factory.newDocumentBuilder();
										Document document = builder.newDocument();

										Element message = (Element) document.createElement("message");

										Element hostUniqueID = addElement(document, "hostRef", Common.hostList.getHost(getHostID()).getUniqueID());
										message.appendChild(hostUniqueID);

										Element messageRef = addElement(document, "messageRef", autolabview.getAutoLabellerObj().getUniqueID());
										message.appendChild(messageRef);

										Element messageType = addElement(document, "interfaceType", "Auto Labeller Data");
										message.appendChild(messageType);

										Element messageInformation = addElement(document, "messageInformation", "Unique ID=" + autolabview.getAutoLabellerObj().getUniqueID());
										message.appendChild(messageInformation);

										Element messageDirection = addElement(document, "interfaceDirection", "Output");
										message.appendChild(messageDirection);

										Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
										message.appendChild(messageDate);

										if (rs.first())
										{

											Element labelData = (Element) document.createElement("LabelData");

											Element row = document.createElement("Row");
											labelData.appendChild(row);
											for (int i = 1; i <= colCount; i++)
											{
												String columnName = rsmd.getColumnName(i);
												Object value = rs.getObject(i);
												Element node = document.createElement(columnName);
												node.appendChild(document.createTextNode(value.toString()));
												row.appendChild(node);
											}

											message.appendChild(labelData);

											document.appendChild(message);

											JXMLDocument xmld = new JXMLDocument();
											xmld.setDocument(document);

											// ===============================

											DOMImplementationLS DOMiLS = null;
											FileOutputStream FOS = null;

											// testing the support for DOM
											// Load and Save
											if ((document.getFeature("Core", "3.0") != null) && (document.getFeature("LS", "3.0") != null))
											{
												DOMiLS = (DOMImplementationLS) (document.getImplementation()).getFeature("LS", "3.0");

												// get a LSOutput object
												LSOutput LSO = DOMiLS.createLSOutput();

												FOS = new FileOutputStream(exportFilename);
												LSO.setByteStream((OutputStream) FOS);

												// get a LSSerializer object
												LSSerializer LSS = DOMiLS.createLSSerializer();

												// do the serialization
												LSS.write(document, LSO);

												FOS.close();
											}

											// ===============================

										}
										rs.close();
										stmt.close();

									} catch (Exception e)
									{
										messageError = e.getMessage();
									}

								}

								if (autolabview.getPrinterObj().getExportFormat().equals("LQF"))
								{

								}
							}

							if (autolabview.getPrinterObj().isDirectPrintEnabled())
							{

							}

						}

						if (messageProcessedOK == true)
						{
							autolabview.getAutoLabellerObj().setModified(false);
							autolabview.getAutoLabellerObj().update();
						} else
						{
							logger.error(messageError);
						}

						autolabview = null;
					}
				}
			}
		}
	}

	public Element addElement(Document doc, String name, String value)
	{
		Element temp = (Element) doc.createElement(name);
		Text temp_value = doc.createTextNode(value);
		temp.appendChild(temp_value);
		return temp;
	}
}
