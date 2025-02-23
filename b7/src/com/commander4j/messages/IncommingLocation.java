package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : IncommingProcessOrder.java
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

import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBLocation;
import com.commander4j.util.JUtility;

/**
 * IncommingLocation creates or updates records in the APP_LOCATION
 * table. Some verification is performed before the table is updated. If errors
 * are found the insert/update does not occur and an error is written to the
 * SYS_INTERFACE_LOG table with a description of the failure reason.
 *
 * @see com.commander4j.db.JDBLocation JDBLocation
 */
public class IncommingLocation
{

	
	private String hostID;
	private String sessionID;
	private String errorMessage;
	
	private String enabled;
	private String location;
	private String plant;
	private String warehouse;
	private String gln;	
	private String description;
	private String storageLocation;
	private String storageType;
	private String storageSection;
	private String storageBin;
	private String equipmentTrackingID;
	
	private String msgDespatchConfirm;
	private String msgDespatchPreAdvice;
	private String msgEquipmentTracking;
	private String msgProductionConfirmation;
	private String msgPalletStatus;
	private String msgPalletSplit;
	private String msgPalletDelete;
	private String msgDespatchJourneyRef;
	
	private String allowedStatusPallet;
	private String allowedStatusBatch;
	
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

	public IncommingLocation(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh)
	{
		Boolean result = false;

		JDBLocation locn = new JDBLocation(getHostID(), getSessionID());

		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());

		inter.getInterfaceProperties("Process Order", "Input");

		enabled = gmh.getXMLDocument().findXPath("//message/messageData/location/enabled").trim();
		location = gmh.getXMLDocument().findXPath("//message/messageData/location/locationID").trim();
		plant = gmh.getXMLDocument().findXPath("//message/messageData/location/plant").trim();
		warehouse = gmh.getXMLDocument().findXPath("//message/messageData/location/warehouse").trim();
		gln = gmh.getXMLDocument().findXPath("//message/messageData/location/GLN").trim();
		description = gmh.getXMLDocument().findXPath("//message/messageData/location/description").trim();
		storageLocation = gmh.getXMLDocument().findXPath("//message/messageData/location/storageLocation").trim();
		storageType = gmh.getXMLDocument().findXPath("//message/messageData/location/storageType").trim();

		storageSection = gmh.getXMLDocument().findXPath("//message/messageData/location/storageSection").trim();


		storageBin = gmh.getXMLDocument().findXPath("//message/messageData/location/storageBin").trim();
		equipmentTrackingID = gmh.getXMLDocument().findXPath("//message/messageData/location/equipmentTrackingID").trim();

		msgDespatchConfirm = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/location/messageOptions/despatchConfirm").trim());
		msgDespatchConfirm = JUtility.yesNoToTrueFalse(msgDespatchConfirm);
		
		msgDespatchPreAdvice = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/location/messageOptions/despatchPreAdvice").trim());
		msgDespatchPreAdvice = JUtility.yesNoToTrueFalse(msgDespatchPreAdvice);
		
		msgEquipmentTracking = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/location/messageOptions/equipmentTracking").trim());
		msgEquipmentTracking = JUtility.yesNoToTrueFalse(msgEquipmentTracking);
		
		msgProductionConfirmation = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/location/messageOptions/productionConfirmation").trim());
		msgProductionConfirmation = JUtility.yesNoToTrueFalse(msgProductionConfirmation);
		
		msgPalletStatus = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/location/messageOptions/palletStatus").trim());
		msgPalletStatus = JUtility.yesNoToTrueFalse(msgPalletStatus);
		
		msgPalletSplit = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/location/messageOptions/palletSplit").trim());
		msgPalletSplit = JUtility.yesNoToTrueFalse(msgPalletSplit);
		
		msgPalletDelete = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/location/messageOptions/palletDelete").trim());
		msgPalletDelete = JUtility.yesNoToTrueFalse(msgPalletDelete);
		
		msgDespatchJourneyRef = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/location/messageOptions/despatchJourneyRef").trim());
		msgDespatchJourneyRef = JUtility.yesNoToTrueFalse(msgDespatchJourneyRef);
		
		allowedStatusPallet = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/location/allowedStatus/pallet").trim());
		allowedStatusBatch = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/location/allowedStatus/batch").trim());

		
		boolean create = false;
		if (locn.getLocationProperties(location) == false)
		{
			create = true;
		} else
		{
			create = false;
		}

		locn.setEnabled(enabled);
		locn.setLocationID(location);
		locn.setPlant(plant);
		locn.setWarehouse(warehouse);
		locn.setGLN(gln);
		locn.setDescription(description);
		locn.setStorageLocation(storageLocation);
		locn.setStorageType(storageType);
		locn.setStorageBin(storageBin);
		locn.setStorageSection(storageSection);
		locn.setEquipmentTrackingID(equipmentTrackingID);
		locn.setMsgDespatchConfirm(Boolean.valueOf(msgDespatchConfirm));
		locn.setMsgDespatchPreadvice(Boolean.valueOf(msgDespatchPreAdvice));
		locn.setMsgDespatchEquipTrack(Boolean.valueOf(msgEquipmentTracking));
		locn.setMsgProdConfirm(Boolean.valueOf(msgProductionConfirmation));
		locn.setMsgStatusChange(Boolean.valueOf(msgPalletStatus));
		locn.setMsgPalletSplit(Boolean.valueOf(msgPalletSplit));
		locn.setMsgDelete(Boolean.valueOf(msgPalletDelete));
		locn.setMsgJourneyRef(Boolean.valueOf(msgDespatchJourneyRef));
		
		locn.setPermittedPalletStatus(allowedStatusPallet);
		locn.setPermittedBatchStatus(allowedStatusBatch);
		
		if (create == true)
		{
			if (locn.create() == false)
			{
				setErrorMessage(locn.getErrorMessage());
			} else
			{
				result = true;
				setErrorMessage("Location " + locn.getLocationID() + " created.");
			}
		} else
		{
			if (locn.update() == false)
			{
				setErrorMessage("Location " + location + " : " + locn.getErrorMessage());
			} else
			{
				result = true;
				setErrorMessage("Location " + locn.getLocationID() + " updated.");
			}
		}

		location = null;

		return result;
	}
}
