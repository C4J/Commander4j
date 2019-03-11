package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : IncommingPalletStatusChange.java
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

import com.commander4j.db.JDBMaterial;
import com.commander4j.util.JUtility;

/**
 * IncommingMaterialAutoMove is used to update 2 fields of the Material Master
 * MoveAfterMakeEnabled and MoveAfterMakeLocationID
 *
 */

public class IncommingMaterialAutoMove
{

	private String hostID;
	private String sessionID;
	private String errorMessage;

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

	public IncommingMaterialAutoMove(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh)
	{
		Boolean result = false;

		JDBMaterial mat = new JDBMaterial(getHostID(), getSessionID());
		String material = "";
		String moveAfterMake = "";
		String locationID = "";


		material = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialAutoMove/material").trim());
		moveAfterMake = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialAutoMove/moveAfterMake").trim());
		locationID = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/materialAutoMove/moveLocationID").trim());

		if (moveAfterMake.equals("Y") || moveAfterMake.equals("N"))
		{
			if (mat.getMaterialProperties(material))
			{
				if (mat.updateAutoMove(material, moveAfterMake, locationID))
				{
					result = true;
				}
				else
				{
					setErrorMessage(mat.getErrorMessage());
				}
			}
		}

		return result;

	}
}
