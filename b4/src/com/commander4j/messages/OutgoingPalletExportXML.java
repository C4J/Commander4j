package com.commander4j.messages;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.io.File;
import java.sql.PreparedStatement;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : OutgoingPalletSplit.java
 * 
 * Package Name : com.commander4j.messages
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBField;
import com.commander4j.db.JDBStructure;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileFilterXML;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;

public class OutgoingPalletExportXML
{
	private String hostID;
	private String sessionID;
	final Logger logger = Logger.getLogger(OutgoingPalletExportXML.class);

	private String errorMessage;
	private JFileIO fio = new JFileIO();

	public OutgoingPalletExportXML(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public Element addElement(Document doc, String name, String value)
	{
		Element temp = (Element) doc.createElement(name);
		Text temp_value = doc.createTextNode(value);
		temp.appendChild(temp_value);
		return temp;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public String getHostID()
	{
		return hostID;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public ResultSet getTableResultSet(PreparedStatement stmt)
	{
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public Boolean saveAs(String filename, PreparedStatement stmt, Component parent,JLabel4j_std jStatusText)
	{

		jStatusText.setForeground(Color.BLACK);
		updateStatus(jStatusText,"Choose outout path and filename");
		Boolean result = false;
		JFileChooser saveXML = new JFileChooser();

		try
		{
			File f = new File(new File(System.getProperty("user.home")).getCanonicalPath());
			saveXML.setCurrentDirectory(f);
			saveXML.addChoosableFileFilter(new JFileFilterXML());
			saveXML.setSelectedFile(new File(filename));

			int r = saveXML.showSaveDialog(parent);

			if (r == 0)
			{
				File selectedFile;
				selectedFile = saveXML.getSelectedFile();
				if (selectedFile != null)
				{
					String exportFilename = selectedFile.getAbsolutePath();
					processMessage(exportFilename, stmt,jStatusText);
					result = true;
					updateStatus(jStatusText,"Export completed to "+exportFilename);
				}
			}
			else
			{
				updateStatus(jStatusText,"");
			}
		}
		catch (Exception ex)

		{
			setErrorMessage(ex.getMessage());
		}

		return result;
	}
	
	private void updateStatus(JLabel4j_std jStatusText,String msg)
	{
		jStatusText.setText(msg);
		Rectangle progressRect = jStatusText.getBounds();
		progressRect = jStatusText.getBounds();
		progressRect.x = 0;
		progressRect.y = 0;
		jStatusText.paintImmediately(progressRect);
	}

	public Boolean processMessage(String filename, PreparedStatement stmtPallet,JLabel4j_std jStatusText)
	{
		Boolean result = false;
		
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			// Message Header Section //
			JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
			Element message = (Element) document.createElement("message");
			Element hostUniqueID = addElement(document, "hostRef", ctrl.getKeyValue("DEFAULT_LOCATION"));
			message.appendChild(hostUniqueID);
			Element messageRef = addElement(document, "messageRef", Common.userList.getUser(Common.sessionID).getUserId());
			message.appendChild(messageRef);
			Element messageType = addElement(document, "interfaceType", "Pallet Data XML");
			message.appendChild(messageType);
			Element messageInformation = addElement(document, "messageInformation",ctrl.getKeyValue("SCHEMA VERSION"));
			message.appendChild(messageInformation);
			Element messageDirection = addElement(document, "interfaceDirection", "Output");
			message.appendChild(messageDirection);
			Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
			message.appendChild(messageDate);
			Element messageData = (Element) document.createElement("messageData");

			updateStatus(jStatusText,"Loading Pallet Data...");

			LinkedList<String> processOrders = new LinkedList<String>();
			LinkedList<String> materialBatches = new LinkedList<String>();
			LinkedList<String> materialUoms = new LinkedList<String>();
			LinkedList<String> materials = new LinkedList<String>();
			LinkedList<String> uoms = new LinkedList<String>();
			LinkedList<String> SSCCs = new LinkedList<String>();
			try
			{
				ResultSet rs = getTableResultSet(stmtPallet);
				
				String material = "";
				String batch = "";
				String uom = "";
				String order = "";
				String sscc="";
				while (rs.next())
				{

					// Build list of Materials //
					material = rs.getString("MATERIAL");

					if (materials.contains(material) == false)
					{
						materials.add(material);
					}
					
					// Build list of Batches //
					material = rs.getString("MATERIAL");
					batch = rs.getString("BATCH_NUMBER");

					if (materialBatches.contains(material + " " + batch) == false)
					{
						materialBatches.add(material + " " + batch);
					}
					
					// Build list of Material Uoms //
					material = rs.getString("MATERIAL");
					uom = rs.getString("UOM");

					if (materialUoms.contains(material + " " + uom) == false)
					{
						materialUoms.add(material + " " + uom);
					}
					
					if (uoms.contains(uom) == false)
					{
						uoms.add(uom);
					}					

					// Build list of Orders //
					order = rs.getString("PROCESS_ORDER");

					if (processOrders.contains(order) == false)
					{
						processOrders.add(order);
					}
					
					//Build list of SSCCs //
					sscc = rs.getString("SSCC");

					if (SSCCs.contains(sscc) == false)
					{
						SSCCs.add(sscc);
					}					
					updateStatus(jStatusText,"Found "+String.valueOf(materials.size())+"Materials. "+String.valueOf(materialBatches.size())+"Material Batches. "+String.valueOf(processOrders.size())+"Process Order. "+String.valueOf(SSCCs.size())+"SSCCs. ");

				}
				rs.close();
			}
			catch (Exception ex)
			{
				setErrorMessage(ex.getMessage());
			}

			// Generate SQL Select for orders //
			int noOfOrders = processOrders.size();
			if (noOfOrders > 0)
			{
				String sqlSelect = "SELECT * FROM APP_PROCESS_ORDER WHERE PROCESS_ORDER IN (";

				for (int x = 0; x < noOfOrders; x++)
				{
					if (x == 0)
					{
						sqlSelect = sqlSelect + "?";
					}
					else
					{
						sqlSelect = sqlSelect + ",?";
					}
				}
				sqlSelect = sqlSelect + ")";

				PreparedStatement stmtOrders;

				stmtOrders = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(sqlSelect);
				stmtOrders.setFetchSize(100);
				
				for (int x = 0; x < noOfOrders; x++)
				{
					stmtOrders.setString(x+1, processOrders.get(x));
				}

				messageData.appendChild(getTableXML(document, "APP_PROCESS_ORDER", stmtOrders));
			}
			
			// Generate SQL Select for UOMs //
			int noUOMs = uoms.size();
			if (noUOMs > 0)
			{
				String sqlSelect = "SELECT * FROM APP_UOM WHERE UOM IN (";

				for (int x = 0; x < noUOMs; x++)
				{
					if (x == 0)
					{
						sqlSelect = sqlSelect + "?";
					}
					else
					{
						sqlSelect = sqlSelect + ",?";
					}
				}
				sqlSelect = sqlSelect + ")";

				PreparedStatement stmtUOMs;

				stmtUOMs = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(sqlSelect);
				stmtUOMs.setFetchSize(100);
				
				for (int x = 0; x < noUOMs; x++)
				{
					stmtUOMs.setString(x+1, uoms.get(x));
				}

				messageData.appendChild(getTableXML(document, "APP_UOM", stmtUOMs));
			}			
			
			// Generate SQL Select for Materials //
			int noOfMaterials = materials.size();
			if (noOfMaterials > 0)
			{
				String sqlSelect = "SELECT * FROM APP_MATERIAL WHERE MATERIAL IN (";

				for (int x = 0; x < noOfMaterials; x++)
				{
					if (x == 0)
					{
						sqlSelect = sqlSelect + "?";
					}
					else
					{
						sqlSelect = sqlSelect + ",?";
					}
				}
				sqlSelect = sqlSelect + ")";

				PreparedStatement stmtMaterials;

				stmtMaterials = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(sqlSelect);
				stmtMaterials.setFetchSize(100);
				
				for (int x = 0; x < noOfMaterials; x++)
				{
					stmtMaterials.setString(x+1, materials.get(x));
				}

				messageData.appendChild(getTableXML(document, "APP_MATERIAL", stmtMaterials));
			}

			// Generate SQL Select for Material Uoms //

			if (noOfMaterials > 0)
			{
				String sqlSelect = "SELECT * FROM APP_MATERIAL_UOM WHERE MATERIAL IN (";

				for (int x = 0; x < noOfMaterials; x++)
				{
					if (x == 0)
					{
						sqlSelect = sqlSelect + "?";
					}
					else
					{
						sqlSelect = sqlSelect + ",?";
					}
				}
				sqlSelect = sqlSelect + ")";

				PreparedStatement stmtMaterialUoms;

				stmtMaterialUoms = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(sqlSelect);
				stmtMaterialUoms.setFetchSize(100);
				
				for (int x = 0; x < noOfMaterials; x++)
				{
					stmtMaterialUoms.setString(x+1, materials.get(x));
				}

				messageData.appendChild(getTableXML(document, "APP_MATERIAL_UOM", stmtMaterialUoms));
			}				
			
			// Generate SQL Select for batches //
			int noOfBatches = materialBatches.size();
			if (noOfBatches > 0)
			{
				String sqlSelect = "SELECT * FROM APP_MATERIAL_BATCH WHERE ";

				for (int x = 0; x < noOfBatches; x++)
				{
					if (x > 0)
					{
						sqlSelect = sqlSelect + " OR ";
					}
					sqlSelect = sqlSelect + " (MATERIAL = ? AND BATCH_NUMBER = ?)";
				}

				PreparedStatement stmtBatches;

				stmtBatches = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(sqlSelect);
				stmtBatches.setFetchSize(100);
				
				int varPointer = 0;
				for (int x = 0; x < noOfBatches; x++)
				{
					String[] pair = materialBatches.get(x).split(" ");
					stmtBatches.setString(varPointer+1, pair[0]);
					stmtBatches.setString(varPointer+2, pair[1]);
					varPointer = varPointer + 2;
				}

				messageData.appendChild(getTableXML(document, "APP_MATERIAL_BATCH", stmtBatches));
			}	
			
			// Generate SQL Select for orders //
			int noOfSSCCs = SSCCs.size();
			if (noOfSSCCs > 0)
			{
				String sqlSelect = "SELECT * FROM APP_PALLET WHERE SSCC IN (";

				for (int x = 0; x < noOfSSCCs; x++)
				{
					if (x == 0)
					{
						sqlSelect = sqlSelect + "?";
					}
					else
					{
						sqlSelect = sqlSelect + ",?";
					}
				}
				sqlSelect = sqlSelect + ")";

				PreparedStatement stmtSSCCs;

				stmtSSCCs = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(sqlSelect);
				stmtSSCCs.setFetchSize(100);
				
				for (int x = 0; x < noOfSSCCs; x++)
				{
					stmtSSCCs.setString(x+1, SSCCs.get(x));
				}

				messageData.appendChild(getTableXML(document, "APP_PALLET", stmtSSCCs));
			}
			
			// Table Section //
			message.appendChild(messageData);

			// End of Document //
			document.appendChild(message);

			// JXMLDocument xmld = new JXMLDocument();
			// xmld.setDocument(document);

			if (fio.writeToDisk(filename, document) == true)
			{
				result = true;
				setErrorMessage("");
			}
			else
			{
				result = false;
				setErrorMessage(fio.getErrorMessage());
			}
		}

		catch (Exception ex)
		{
			logger.error("Error exporting pallet data. " + ex.getMessage());
			ex.printStackTrace();
		}

		return result;
	}

	private Element getTableXML(Document document, String tableName, PreparedStatement stmt)
	{

		Element tableElement = (Element) document.createElement("table");
		tableElement.setAttribute("name", tableName.toUpperCase());

		JDBStructure structure = new JDBStructure(getHostID(), getSessionID());
		LinkedList<String> primaryKeys = structure.getPrimaryKey(tableName);
		LinkedList<String> uniqueIndexes = structure.getUniqueIndexes(tableName);

		Long recordCount = (long) 0;
		try
		{
			ResultSet rs = getTableResultSet(stmt);
			while (rs.next())
			{
				// Export Pallet records to xml
				Element recordElement = (Element) document.createElement("record");
				recordCount++;
				recordElement.setAttribute("no", String.valueOf(recordCount));

				LinkedList<JDBField> fieldNames = structure.getFieldNames(tableName.toUpperCase());
				int palletColumns = fieldNames.size();
				for (int x = 0; x < palletColumns; x++)
				{

					String col_name = fieldNames.get(x).getfieldName();
					String col_type = fieldNames.get(x).getfieldType();

					String value = "";

					try
					{
						switch (col_type)
						{
						case "VARCHAR":
							value = rs.getString(col_name);
							break;
						case "VARCHAR2":
							value = rs.getString(col_name);
							break;							
						case "DATETIME":
							value = JUtility.getISOTimeStampStringFormat(rs.getTimestamp(col_name));
							break;
						case "DATE":
							value = JUtility.getISOTimeStampStringFormat(rs.getTimestamp(col_name));
							break;							
						case "INT":
							value = String.valueOf(rs.getInt(col_name));
							break;
						case "DECIMAL":
							value = rs.getBigDecimal(col_name).toString();
							break;
						case "NUMBER":
							value = rs.getBigDecimal(col_name).toString();
							break;							
						default:
							value = "Unhandled type " + col_type;
						}
					}
					catch (Exception ex)
					{
						value = col_name + " exception " + ex.getMessage();
					}

					Element fieldElement = (Element) document.createElement("field");

					fieldElement.setAttribute("name", col_name);
					fieldElement.setAttribute("type", col_type);
					fieldElement.setAttribute("value", value);
					if (primaryKeys.contains(col_name))
					{
						fieldElement.setAttribute("primaryKey", "*");
						
					}
					else
					{
						if (uniqueIndexes.contains(col_name))
						{
							fieldElement.setAttribute("primaryKey", "*");
						}
					}
					
					recordElement.appendChild(fieldElement);
				}
				tableElement.appendChild(recordElement);
			}
			rs.close();
		}
		catch (Exception ex)
		{
			setErrorMessage(ex.getMessage());
		}
		return tableElement;
	}

	private void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public void setHostID(String host)
	{
		hostID = host;
	}

	public void setSessionID(String session)
	{
		sessionID = session;
	}

}
