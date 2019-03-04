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

import com.commander4j.db.JDBPallet;
import com.commander4j.util.JUtility;

/**
 * IncommingPalletMove as its name suggests updates the Pallet Location.
 * Only the destination is required. However if the source is also provided then
 * this will be checked before the update is performed.
 */
public class IncommingPalletMove
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

	public IncommingPalletMove(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh)
	{
		Boolean result = true;

		JDBPallet pal = new JDBPallet(getHostID(), getSessionID());
		String sscc = "123456789012345678";
		String fromLocation = "";
		String toLocation = "";
		int occur = 1;
		int notfound = 0;
		int updated = 0;
		int notupdated = 0;
		Long txn = (long) 0;

		while (sscc.length() > 0)

		{

			sscc = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/palletMove/pallet[" + String.valueOf(occur) + "]/sscc").trim());
			fromLocation = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/palletMove/pallet[" + String.valueOf(occur) + "]/fromLocation").trim());
			toLocation = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/palletMove/pallet[" + String.valueOf(occur) + "]/toLocation").trim());

			if (sscc.length() > 0)
			{

				if (pal.getPalletProperties(sscc) == true)
				{
					txn = pal.updateLocation(txn,fromLocation, toLocation);
					if (txn > 0)
					{
						updated++;
					} else
					{
						notupdated++;
					}
				} else
				{

					notfound++;

				}
				occur++;
			}
		}
		pal = null;

		if ((notfound > 0) || (notupdated > 0))
		{
			result = false;
			String error = "Some SSCC's were not found in the Commander4j database. \n\n";

			error = error + String.valueOf(updated) + " Updated.\n";
			error = error + String.valueOf(notupdated) + " Ignored (same).\n";
			error = error + String.valueOf(notfound) + " Not found.\n\n";
			setErrorMessage(error);
		} else
		{
			setErrorMessage(String.valueOf(updated) + " Updated.");
		}

		return result;

	}
}
