package com.commander4j.messages;

import com.commander4j.db.JDBProcessOrder;

public class IncommingProcessOrderStatusChange
{

	private String hostID;
	private String sessionID;
	private String errorMessage;
	private String orderNo;
	private String status;
	private String receipeId;

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

	public IncommingProcessOrderStatusChange(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh) {
		Boolean result = true;

		JDBProcessOrder po = new JDBProcessOrder(getHostID(), getSessionID());

		orderNo = gmh.getXMLDocument().findXPath("//message/messageData/processOrderStatusChange/orderNo").trim();
		status = gmh.getXMLDocument().findXPath("//message/messageData/processOrderStatusChange/status").trim();
		receipeId = gmh.getXMLDocument().findXPath("//message/messageData/processOrderStatusChange/receipeId").trim();

		if (po.getProcessOrderProperties(orderNo) == true)
		{
			po.setStatus(status);
			po.setRecipe(receipeId);
			if (po.update() == true)
			{
				setErrorMessage("Process Order " + orderNo + " status updated.");
			}
			else
			{
				result = false;
				setErrorMessage("Process Order [" + orderNo + "] updated failed. [" + po.getErrorMessage() + "]");
			}
		}
		else
		{
			result = false;
			setErrorMessage("Process Order [" + orderNo + "] not found.");
		}

		po = null;

		return result;
	}
}
