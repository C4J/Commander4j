package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : IncommingProcessOrderStatusChange.java
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

import com.commander4j.db.JDBProcessOrder;

/**
 * IncommingProcessOrderStatusChange updates records in the APP_PROCESS_ORDER
 * table. Each Process Order can have a number of different states depending on
 * if the order is active or finished. This message allows an external system to
 * change the status of a Process Order.
 *
 * @see com.commander4j.db.JDBProcessOrder JDBProcessOrder
 */
public class IncommingProcessOrderStatusChange
{

	private String hostID;
	private String sessionID;
	private String errorMessage;
	private String orderNo;
	private String status;
	private String receipeId;

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

	public IncommingProcessOrderStatusChange(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh)
	{
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
			} else
			{
				result = false;
				setErrorMessage("Process Order [" + orderNo + "] updated failed. [" + po.getErrorMessage() + "]");
			}
		} else
		{
			result = false;
			setErrorMessage("Process Order [" + orderNo + "] not found.");
		}

		po = null;

		return result;
	}
}
