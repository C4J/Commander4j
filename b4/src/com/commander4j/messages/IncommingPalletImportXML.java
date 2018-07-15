package com.commander4j.messages;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : IncommingJourney.java
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

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import com.commander4j.gui.JLabel4j_std;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileFilterXML;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

public class IncommingPalletImportXML
{

	private String hostID;
	private String sessionID;
	private String errorMessage;
	final Logger logger = Logger.getLogger(IncommingPalletImportXML.class);

	public String getErrorMessage()
	{
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getHostID()
	{
		return hostID;
	}

	public void setHostID(String hostID)
	{
		this.hostID = hostID;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}

	public IncommingPalletImportXML(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	private void updateStatus(JLabel4j_std jStatusText, String msg)
	{
		jStatusText.setText(msg);
		Rectangle progressRect = jStatusText.getBounds();
		progressRect = jStatusText.getBounds();
		progressRect.x = 0;
		progressRect.y = 0;
		jStatusText.paintImmediately(progressRect);
	}

	public Boolean loadFrom(String filename, Component parent, JLabel4j_std jStatusText)
	{

		jStatusText.setForeground(Color.BLACK);
		updateStatus(jStatusText, "Choose input filename");
		Boolean result = false;
		JFileChooser loadXML = new JFileChooser();

		try
		{
			File f = new File(new File(System.getProperty("user.home")).getCanonicalPath());
			loadXML.setCurrentDirectory(f);
			loadXML.addChoosableFileFilter(new JFileFilterXML());
			loadXML.setSelectedFile(new File(filename));

			int r = loadXML.showOpenDialog(parent);

			if (r == 0)
			{
				File selectedFile;
				selectedFile = loadXML.getSelectedFile();
				if (selectedFile != null)
				{
					String importtFilename = selectedFile.getAbsolutePath();
					processMessage(importtFilename, jStatusText);
					result = true;
					updateStatus(jStatusText, "Import completed from " + importtFilename);
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

	public Boolean processMessage(String filename, JLabel4j_std jStatusText)
	{
		Boolean result = false;

		JXMLDocument xmlDoc = new JXMLDocument(filename);

		int tableInstance = 1;
		String tableName = "";

		while ((xmlDoc.findXPath("/message/messageData/table[" + String.valueOf(tableInstance) + "]/@name").equals("") == false))
		{
			tableName = xmlDoc.findXPath("/message/messageData/table[" + String.valueOf(tableInstance) + "]/@name");
			logger.debug("Tablename = " + tableName);

			int recordInstance = 1;
			String recordNo = "";

			while ((xmlDoc.findXPath("/message/messageData/table[" + String.valueOf(tableInstance) + "]/record[" + String.valueOf(recordInstance) + "]/@no").equals("") == false))
			{
				String insertSQLinsert = "insert into " + tableName;
				String insertSQLfieldNames = "";
				String insertSQLfieldValues = "";

				recordNo = xmlDoc.findXPath("/message/messageData/table[" + String.valueOf(tableInstance) + "]/record[" + String.valueOf(recordInstance) + "]/@no");
				logger.debug("  Record No = " + recordNo);

				int fieldInstance = 1;
				String fieldName = "";
				String fieldType = "";
				String fieldValue = "";
				String fieldPrimaryKey = "";

				LinkedList<String> fieldNames = new LinkedList<String>();
				LinkedList<String> fieldTypes = new LinkedList<String>();
				LinkedList<String> fieldValues = new LinkedList<String>();
				LinkedList<String> fieldPrimaryKeys = new LinkedList<String>();

				while ((xmlDoc.findXPath("/message/messageData/table[" + String.valueOf(tableInstance) + "]/record[" + String.valueOf(recordInstance) + "]/field[" + String.valueOf(fieldInstance) + "]/@name").equals("") == false))
				{
					fieldName = xmlDoc.findXPath("/message/messageData/table[" + String.valueOf(tableInstance) + "]/record[" + String.valueOf(recordInstance) + "]/field[" + String.valueOf(fieldInstance) + "]/@name");
					fieldType = xmlDoc.findXPath("/message/messageData/table[" + String.valueOf(tableInstance) + "]/record[" + String.valueOf(recordInstance) + "]/field[" + String.valueOf(fieldInstance) + "]/@type");
					fieldValue = xmlDoc.findXPath("/message/messageData/table[" + String.valueOf(tableInstance) + "]/record[" + String.valueOf(recordInstance) + "]/field[" + String.valueOf(fieldInstance) + "]/@value");
					fieldPrimaryKey = xmlDoc.findXPath("/message/messageData/table[" + String.valueOf(tableInstance) + "]/record[" + String.valueOf(recordInstance) + "]/field[" + String.valueOf(fieldInstance) + "]/@primaryKey");

					fieldNames.addLast(fieldName);
					fieldTypes.addLast(fieldType);
					fieldValues.addLast(fieldValue);
					fieldPrimaryKeys.addLast(fieldPrimaryKey);

					//logger.debug("    Field name=" + fieldName + ", type=" + fieldType + ", value=" + fieldValue + ", primaryKey" + fieldPrimaryKey);

					if (insertSQLfieldNames.equals(""))
					{
						insertSQLfieldNames = fieldName;
						insertSQLfieldValues = "?";
					}
					else
					{
						insertSQLfieldNames = insertSQLfieldNames + "," + fieldName;
						insertSQLfieldValues = insertSQLfieldValues + ",?";
					}

					fieldInstance++;
				}

				insertSQLinsert = insertSQLinsert + " (" + insertSQLfieldNames + ") values (" + insertSQLfieldValues + ")";
				//logger.debug(insertSQLinsert);
				try
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(insertSQLinsert);
					
					for (int x = 0; x < fieldNames.size(); x++)
					{

						switch (fieldTypes.get(x))
						{
						case "VARCHAR":
							stmtupdate.setString(x + 1, fieldValues.get(x));
							break;
						case "VARCHAR2":
							stmtupdate.setString(x + 1, fieldValues.get(x));
							break;
						case "DATETIME":
							stmtupdate.setTimestamp(x + 1, JUtility.getTimeStampFromISOString(fieldValues.get(x)));
							break;
						case "DATE":
							stmtupdate.setTimestamp(x + 1, JUtility.getTimeStampFromISOString(fieldValues.get(x)));
							break;
						case "INT":
							stmtupdate.setInt(x + 1, Integer.valueOf(fieldValues.get(x)));
							break;
						case "DECIMAL":
							BigDecimal temp1 = new BigDecimal(fieldValues.get(x));
							stmtupdate.setBigDecimal(x + 1, temp1);
							break;
						case "NUMBER":
							BigDecimal temp2 = new BigDecimal(fieldValues.get(x));
							stmtupdate.setBigDecimal(x + 1, temp2);
							break;
						default:
							logger.debug("Unhandled type " + fieldTypes.get(x));
						}

					}
					logger.debug(stmtupdate.toString());

					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					updateStatus(jStatusText, "Table ["+tableName+"] rows inserted ["+String.valueOf(recordNo)+"]");
					result = true;
				}
				catch (SQLException e)
				{
					setErrorMessage(e.getMessage());
					logger.error(e.getMessage());
				}
				recordInstance++;
			}

			tableInstance++;
		}

		return result;

	}
}
