package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : IncommingQMInspectionRequest.java
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

import javax.swing.JLabel;

import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMActivity;
import com.commander4j.db.JDBQMDictionary;
import com.commander4j.db.JDBQMExtension;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBQMTest;
import com.commander4j.util.JUtility;

public class IncommingQMInspectionRequest
{

	private String hostID;
	private String sessionID;
	private String errorMessage;
	private String inspectionID;
	private String inspectionDescription;
	private String processOrder;
	public String getErrorMessage() {
		return errorMessage;
	}

	@SuppressWarnings("unused")
	private void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getHostID() {
		return hostID;
	}

	public void setHostID(String hostID) {
		this.hostID = hostID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public IncommingQMInspectionRequest(String host, String session) {
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh) {
		Boolean result = false;

		JDBQMInspection qmInspection = new JDBQMInspection(getHostID(), getSessionID());
		JDBQMExtension qmExtension = new JDBQMExtension(getHostID(), getSessionID());
		JDBQMActivity qmActivity = new JDBQMActivity(getHostID(), getSessionID());
		JDBQMTest qmTest = new JDBQMTest(getHostID(), getSessionID());
		JDBQMDictionary qmDictionary = new JDBQMDictionary(getHostID(), getSessionID());
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		JDBProcessOrder po = new JDBProcessOrder(getHostID(),getSessionID());

		inter.getInterfaceProperties("QM Inspection Request", "Input");

		// INSPECTION //
		// Decode the Inspection Top Level ID //
		
		inspectionID = gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/ID").trim();
		inspectionDescription = gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/description").trim();
		processOrder = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/processOrder").trim());
		
		
		if (processOrder.equals("")==false)
		{
			processOrder = processOrder.replaceAll("^0+", "");
		    po.setProcessOrder(processOrder);
		    if (po.isValidProcessOrder())
		    {
		    	po.getProcessOrderProperties();
		    	po.setInspectionID(inspectionID);
		    	po.update();
		    }
		}

		qmInspection.setInspectionID(inspectionID);

		// Create or update the Inspection record.
		if (qmInspection.isValid()==false)
		{
			// New inspection record to be created.
			qmInspection.create(inspectionID, inspectionDescription);
		}
		else
		{
			// Amend existing inspection record.
			qmInspection.getProperties();
			qmInspection.setDescription(inspectionDescription);
			qmInspection.update();
		}

		Long inspectionExtensionID = (long) 0;
		inspectionExtensionID=qmInspection.getExtensionID();
		
		// INSPECTION - extended data //
		// Scan through XML to process any 'extension' data which needs be stored against the top level Inspection record.
		String extensionName = " ";
		String extensionValue = "";
		int extensionOccurence = 1;
		while (extensionName.length() > 0)

		{
			extensionName = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/extendedData/item[" + String.valueOf(extensionOccurence) + "]/name").trim());

			if (extensionName.length() > 0)
			{
				extensionValue = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/extendedData/item[" + String.valueOf(extensionOccurence) + "]/value").trim());

				qmExtension.setExtensionID(inspectionExtensionID);
				qmExtension.setFieldName(extensionName);
				qmExtension.setTableName("APP_QM_INSPECTION");
				if (qmExtension.isValid())
				{
					qmExtension.getProperties();
					qmExtension.setValue(extensionValue);
					qmExtension.update();
				}
				else
				{
					qmExtension.create(inspectionExtensionID,"APP_QM_INSPECTION", extensionName, extensionValue);
				}
	
			}
			extensionOccurence++;
		}
		
		// ACTIVITY //
		// Scan through XML to process any 'activity' records which needs be stored against the top level Inspection record.		
		String activityID = " ";
		String activityDescription = "";

		Long activityExtensionID = (long) 0;
		int ActivityOccurence = 1;
		while (activityID.length() > 0)
		{
			activityID = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/ID").trim());
			
			if (activityID.length() > 0)
			{	
				activityDescription = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/description").trim());

				qmActivity.setInspectionID(inspectionID);
				qmActivity.setActivityID(activityID);

				
				if(qmActivity.isValid()==false)
				{
					// Create new Activity Record.
					qmActivity.create(inspectionID, activityID, activityDescription);
				}
				else
				{
					// Update Activity Record.
					qmActivity.getProperties();
					qmActivity.setDescription(activityDescription);
					qmActivity.update();
				}
				activityExtensionID = qmActivity.getExtensionID();

			}
				
			// ACTIVITY - extended data //
			// Scan through XML to process any 'extension' data which needs be stored against the activity record.
			extensionName = " ";
			extensionValue = "";
			extensionOccurence = 1;
			while (extensionName.length() > 0)

			{

				extensionName = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/extendedData/item[" + String.valueOf(extensionOccurence) + "]/name").trim());

				if (extensionName.length() > 0)
				{
					extensionValue = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/extendedData/item[" + String.valueOf(extensionOccurence) + "]/value").trim());

					qmExtension.setExtensionID(activityExtensionID);
					qmExtension.setFieldName(extensionName);
					qmExtension.setTableName("APP_QM_ACTIVITY");
					if (qmExtension.isValid())
					{
						qmExtension.getProperties();
						qmExtension.setValue(extensionValue);
						qmExtension.update();
					}
					else
					{
						qmExtension.create(activityExtensionID,"APP_QM_ACTIVITY", extensionName, extensionValue);
					}
					
				}
				extensionOccurence++;
			}
			
			//////////////   TESTS			
			
			// TEST //
			// Scan through XML to process any 'test' records which needs be stored against the  Activity record.		
			Long testSequence = (long) 0;
			String testID = " ";
			String testDescription = "";
			@SuppressWarnings("unused")
			String testFrequency = "";
			@SuppressWarnings("unused")
			String singleResultIndicator = "";
			String testDataType = "";
			String testSelectListID = "";
			String testRequired = "";
			String testVisible = "";
			String testUOM = "";
			String testDefaultValue="";
			
			int testOccurence = 1;
			while (testID.length() > 0)
			{
				testID = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/ID").trim());

				
				if (testID.length() > 0)
				{	
					testSequence = Long.parseLong(JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/sequence").trim()));
					testDescription = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/description").trim());
					singleResultIndicator = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/singleResultIndicator").trim());
					testFrequency = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/frequency").trim());
					testDataType = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/dataType").trim());
					testSelectListID = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/selectListID").trim());
					testRequired = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/required").trim());
					testVisible = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/visible").trim());
					testUOM = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/uom").trim());
					testDefaultValue = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/defaultValue").trim());				
					// DICTIONARY //

		
					qmDictionary.setTestID(testID);
					if (qmDictionary.isValid()==false)
					{
						qmDictionary.create(testID, JLabel.LEADING, testDataType, testUOM, testRequired, testDescription, testVisible,50,testDefaultValue);
						
					}
					else
					{
						qmDictionary.getProperties();
						qmDictionary.setDataType(testDataType);
						qmDictionary.setDescription(testDescription);
						qmDictionary.setRequired(testRequired);
						qmDictionary.setSelectListID(testSelectListID);
						qmDictionary.setUOM(testUOM);
						qmDictionary.setVisible(testVisible);
						qmDictionary.update();
					}
					
										
					//////
									
					qmTest.setInspectionID(inspectionID);
					qmTest.setActivityID(activityID);
					qmTest.setSequence(testSequence);
					qmTest.setTestID(testID);
					
					if(qmTest.isValidTest()==false)
					{
						// Create new Test Record.
						qmTest.create(inspectionID, activityID, testID,testSequence);

					}
					else
					{
						// Update Test Record.
						qmTest.getProperties();
						qmTest.setSequence(testSequence);
						qmTest.update();
						
					}
					
					// TEST EXTENSION //
					Long testExtensionID = (long) 0;
					testExtensionID = qmTest.getExtensionID();
					
					extensionName = " ";
					extensionValue = "";
					extensionOccurence = 1;
					while (extensionName.length() > 0)

					{

						extensionName = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/extendedData/item[" + String.valueOf(extensionOccurence) + "]/name").trim());

						if (extensionName.length() > 0)
						{
							extensionValue = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/inspectionDefinition/activity[" + String.valueOf(ActivityOccurence) + "]/test[" + String.valueOf(testOccurence) + "]/extendedData/item[" + String.valueOf(extensionOccurence) + "]/value").trim());

							qmExtension.setExtensionID(testExtensionID);
							qmExtension.setFieldName(extensionName);
							qmExtension.setTableName("APP_QM_TEST");
							if (qmExtension.isValid())
							{
								qmExtension.getProperties();
								qmExtension.setValue(extensionValue);
								qmExtension.update();
							}
							else
							{
								qmExtension.create(testExtensionID,"APP_QM_TEST", extensionName, extensionValue);
							}
							
						}
						extensionOccurence++;
					}

				}			

				testOccurence++;
			}
			
			//////////////// END OF TESTS
			ActivityOccurence++;
		}	
	
		qmInspection = null;
		qmActivity = null;
		qmExtension = null;
		
		result = true;

		return result;
	}
}
