package com.commander4j.messages;

import com.commander4j.bom.BomImport;
import com.commander4j.bom.JDBBomRecord;

/**
 * IncommingBillOfMaterials class allows an external system to create a Bill of Materials (Recipe).
 * 
 */

public class IncommingBillOfMaterials
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

	public IncommingBillOfMaterials(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh)
	{
		Boolean result = false;

		int errors = 0;

		setErrorMessage("");
				
		BomImport bomImport = new BomImport(getHostID(),getSessionID());
		
		JDBBomRecord importresult = bomImport.importBom(gmh.getFilename());

		String bom_id = importresult.getBOMId();
		String bom_version = importresult.getBOMVersion();
		
		if (errors == 0)
		{
			result = true;
		}
		
		setErrorMessage("Bill of Materials - Recipe : ["+bom_id+"] Version : ["+bom_version+"] imported.");
		
		bomImport = null;

		return result;

	}
}
