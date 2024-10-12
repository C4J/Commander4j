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

import java.math.BigDecimal;

import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBUom;
import com.commander4j.util.JUtility;

/**
 * IncommingProcessOrder creates or updates records in the APP_PROCESS_ORDER
 * table. Some verification is performed before the table is updated. If errors
 * are found the insert/update does not occur and an error is written to the
 * SYS_INTERFACE_LOG table with a description of the failure reason.
 *
 * @see com.commander4j.db.JDBProcessOrder JDBProcessOrder
 */
public class IncommingProcessOrder
{

	private String hostID;
	private String sessionID;
	private String errorMessage;
	private String orderNo;
	private String material;
	private String description;
	private String status;
	private String dueDate;
	private String location;
	private String receipeId;
	private String receipeVersion;
	private String requiredQuantity;
	private String requiredUom;
	private String defaultPalletStatus;
	private String requiredResource;
	private String customerID;
	private String customerName;
	private String inspectionID;

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

	public IncommingProcessOrder(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh)
	{
		Boolean result = false;

		JDBProcessOrder po = new JDBProcessOrder(getHostID(), getSessionID());
		JDBCustomer cst = new JDBCustomer(getHostID(), getSessionID());
		JDBUom uomdb = new JDBUom(getHostID(), getSessionID());
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());

		inter.getInterfaceProperties("Process Order", "Input");

		orderNo = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/orderNo").trim();
		material = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/material").trim();
		description = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/description").trim();
		status = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/status").trim();
		location = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/location").trim();
		dueDate = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/dueDate").trim();
		receipeId = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/receipeId").trim();
		receipeVersion = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/receipeVersion").trim();
		requiredQuantity = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/requiredQuantity").trim();

		requiredUom = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/requiredUom").trim();
		requiredUom = uomdb.convertUom(inter.getUOMConversion(), requiredUom);

		defaultPalletStatus = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/defaultPalletStatus").trim();
		requiredResource = gmh.getXMLDocument().findXPath("//message/messageData/processOrder/requiredResource").trim();

		inspectionID = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/processOrder/inspectionID").trim());
		customerID = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/processOrder/customerID").trim());
		customerName = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/processOrder/customerName").trim());

		boolean create = false;
		if (po.getProcessOrderProperties(orderNo) == false)
		{
			create = true;
		} else
		{
			create = false;
		}

		po.setLocation(location);
		po.setMaterial(material);
		po.setDescription(description);
		po.setStatus(status);
		po.setDefaultPalletStatus(defaultPalletStatus);
		po.setRecipe(receipeId);
		po.setRecipeVersion(receipeVersion);
		po.setRequiredQuantity(BigDecimal.valueOf(Double.valueOf(requiredQuantity)));
		po.setRequiredUom(requiredUom);
		po.setRequiredResource(requiredResource);
		po.setInspectionID(inspectionID);

		if (customerID.equals("") == false)
		{
			if (cst.getCustomerProperties(customerID) == true)
			{
				if (customerName.equals("") == false)
				{
					cst.setName(customerName);
					cst.update();
				}
			} else
			{
				cst.clear();
				cst.create(customerID, customerName, "Y");
				cst.update();
			}
		} else
		{
			customerID = "SELF";
		}

		po.setCustomerID(customerID);

		String temp = dueDate.replace("T", " ");
		
		try
		{
			java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf(temp);
			
			po.setDueDate(ts2);

			if (create == true)
			{
				if (po.create() == false)
				{
					setErrorMessage(po.getErrorMessage());
				} else
				{
					result = true;
					setErrorMessage("Process Order " + po.getProcessOrder() + " created.");
				}
			} else
			{
				if (po.update() == false)
				{
					setErrorMessage("Process Order " + orderNo + " : " + po.getErrorMessage());
				} else
				{
					result = true;
					setErrorMessage("Process Order " + po.getProcessOrder() + " updated.");
				}
			}
		}
		catch (Exception ex)
		{
			setErrorMessage(ex.getMessage());
			result = false;
		}
		
		po = null;

		return result;
	}
}
