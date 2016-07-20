package com.commander4j.messages;

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
