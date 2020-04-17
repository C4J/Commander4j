package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : IncommingProductionDeclarationConfirmation.java
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

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBPallet;
import com.commander4j.util.JUtility;

/**
 * IncommingProductionDeclarationConfirmation allows an external system
 * (typically an auto-labeller) to declare a pallet (SSCC) as being created -
 * successfully labelled.
 * 
 * From a transaction perspective this is exactly the same as printing and
 * confirming a pallet from within the desktop application and subject to the
 * exact same validation rules.
 *
 *
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.db.JDBPalletHistory JDBPalletHistory
 * @see com.commander4j.app.JInternalFrameProductionDeclaration JInternalFrameProductionDeclaration
 * @see com.commander4j.app.JInternalFrameProductionConfirmation JInternalFrameProductionConfirmation
 */
public class IncommingProductionDeclarationConfirmation
{

	private String hostID;
	private String sessionID;
	private String errorMessage;
	private String confirmed;

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

	public IncommingProductionDeclarationConfirmation(String host, String session)
	{
		setSessionID(session);
		setHostID(host);

	}

	public Boolean processMessage(GenericMessageHeader gmh)
	{
		Boolean result = false;

		JDBPallet pal = new JDBPallet(getHostID(), getSessionID());
		JDBMaterial mat = new JDBMaterial(getHostID(), getSessionID());

		String sscc = gmh.getXMLDocument().findXPath("//message/messageData/productionDeclaration/SSCC").trim();
		confirmed = gmh.getXMLDocument().findXPath("//message/messageData/productionDeclaration/confirmed").trim().toUpperCase();

		if (pal.getPalletProperties(sscc) == false)
		{
			pal.setSSCC(sscc);
			pal.setProcessOrder(gmh.getXMLDocument().findXPath("//message/messageData/productionDeclaration/processOrder").trim());
			pal.setUom(gmh.getXMLDocument().findXPath("//message/messageData/productionDeclaration/productionUOM").trim());
			if (pal.populateFromProcessOrder() == true)
			{

				pal.setBatchNumber(gmh.getXMLDocument().findXPath("//message/messageData/productionDeclaration/batch").trim());
				
				if (pal.getBatchNumber().length()>JDBMaterialBatch.field_batch_number)
				{
					result = false;
					setErrorMessage("SSCC " + pal.getSSCC() + " Batch Number (" + pal.getBatchNumber() + ") is too long. Max length is "+String.valueOf(JDBMaterialBatch.field_batch_number)+" characters.");
				}
				
				
				pal.setQuantity(new BigDecimal(gmh.getXMLDocument().findXPath("//message/messageData/productionDeclaration/productionQuantity").trim()));

				String prodDateString = gmh.getXMLDocument().findXPath("//message/messageData/productionDeclaration/productionDate").trim();
				Timestamp prodDateTime = JUtility.getTimeStampFromISOString(prodDateString);

				String expireString = gmh.getXMLDocument().findXPath("//message/messageData/productionDeclaration/expiryDate").trim();
				Timestamp expireTime = JUtility.getTimeStampFromISOString(expireString);

				if (expireTime == null)
				{
					result = false;
					setErrorMessage("SSCC " + pal.getSSCC() + " Expiry Date in wrong format (" + expireString + ") yyyy-mm-ddThh:mm:ss");
				} else
				{

					pal.setBatchExpiry(JUtility.getTimestampFromDate(mat.getRoundedExpiryTime(expireTime)));

					if (pal.getBatchExpiry().before(prodDateTime))
					{
						result = false;
						setErrorMessage("SSCC " + pal.getSSCC() + " has Expiry Date before Production Date.");
					} else
					{

						if (pal.create("PROD DEC", "CREATE") == false)
						{
							result = false;
							setErrorMessage("SSCC " + pal.getSSCC() + " " + pal.getErrorMessage());
						} else
						{
							result = true;
							setErrorMessage("SSCC " + pal.getSSCC() + " created.");
						}
					}
				}

			} else
			{
				result = false;
				setErrorMessage("SSCC " + pal.getSSCC() + " " + pal.getErrorMessage());
			}

		} else
		{
			result = true;
			// setErrorMessage("SSCC " + pal.getSSCC() + " already exists.");
		}

		if (result == true)
		{
			if (confirmed.equals("Y"))
			{
				if (pal.isConfirmed() == false)
				{

					String prodDateString = gmh.getXMLDocument().findXPath("//message/messageData/productionDeclaration/productionDate").trim();
					Timestamp prodDateTime = JUtility.getTimeStampFromISOString(prodDateString);

					pal.setDateOfManufacture(prodDateTime);

					if (pal.confirm() == false)
					{
						result = false;
						setErrorMessage(pal.getErrorMessage());
					} else
					{
						setErrorMessage("SSCC " + pal.getSSCC() + " confirmed.");
					}
				} else
				{
					setErrorMessage("SSCC " + pal.getSSCC() + " already confirmed.");
					result = false;
				}
			}
		}
		pal = null;

		return result;
	}
}
