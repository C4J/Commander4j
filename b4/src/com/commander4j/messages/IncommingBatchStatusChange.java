package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : IncommingBatchStatusChange.java
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

import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.util.JUtility;

public class IncommingBatchStatusChange
{

	private String hostID;
	private String sessionID;
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

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

	public IncommingBatchStatusChange(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh) {
		Boolean result = false;

		JDBMaterialBatch batch = new JDBMaterialBatch(getHostID(), getSessionID());
		String material = "12345";
		String batchStr = "";
		String status = "";
		int occur = 1;

		while (material.length() > 0)

		{
			material = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/batchStatusChangel[" + String.valueOf(occur) + "]/materia").trim());
			batchStr = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/batchStatusChange[" + String.valueOf(occur) + "]/batch").trim());
			status = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/batchStatusChange[" + String.valueOf(occur) + "]/status").trim());

			if (material.length() > 0)
			{

				if (batch.getMaterialBatchProperties(material, batchStr) == true)
				{
					batch.setStatus(status);
					batch.update();
					result = true;
					setErrorMessage(String.valueOf(occur) + " Batch(s) updated");
				}
				else
				{
					result = false;
					setErrorMessage("Material " + material + " Batch " + batchStr + " not found.");
				}
				occur++;
			}
		}
		batch = null;
		return result;

	}
}
