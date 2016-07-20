package com.commander4j.messages;

import com.commander4j.db.JDBPallet;
import com.commander4j.util.JUtility;

public class IncommingPalletStatusChange
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

	public IncommingPalletStatusChange(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh) {
		Boolean result = true;

		JDBPallet pal = new JDBPallet(getHostID(), getSessionID());
		String sscc = "123456789012345678";
		String status = "";
		int occur = 1;
		int notfound = 0;
		int updated = 0;
		int notupdated = 0;

		while (sscc.length() > 0)

		{
			sscc = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/palletStatusChange/sscc[" + String.valueOf(occur) + "]").trim());
			status = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/palletStatusChange/status[" + String.valueOf(occur) + "]").trim());

			if (sscc.length() > 0)
			{

				if (pal.getPalletProperties(sscc) == true)
				{
					Long txn = pal.updateStatus(status);
					if (txn>0)
					{
						// if there has been a change to the status create an outbound message here
						OutgoingPalletStatusChange opsc = new OutgoingPalletStatusChange(getHostID(), getSessionID());
						opsc.submit(txn);
						updated++;
					}
					else
					{
						// false in this case means that no update was required as the new status was the same as the old one.
						notupdated++;
					}
				}
				else
				{

					notfound++;

				}
				occur++;
			}
		}
		pal = null;
		
		if (notfound>0)
		{
			result = false;
			String error = "Some SSCC's were not found in the Commander4j database. \n\n";
					
			error = error +String.valueOf(updated)+" Updated.\n";
			error = error +String.valueOf(notupdated)+" Ignored (same).\n";
			error = error +String.valueOf(notfound)+" Not found.\n\n";
			setErrorMessage(error);
		}
		else
		{
			setErrorMessage(String.valueOf(updated)+" Updated.");
		}
		
		
		return result;

	}
}
