package com.commander4j.entity;

import java.math.BigDecimal;

import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.Entity;

@JsonbPropertyOrder({ "action",	"processOrdeID", "quantity", "uom"})

@Entity
public class JQMReturnableEntity
{

	@JsonbProperty("action")
	@SerializedName("action")
	private String action;
	
	@JsonbProperty("processOrderID")
	@SerializedName("processOrderID")
	private String processOrderID;
	
	@JsonbProperty("locationID")
	@SerializedName("locationID")
	private String locationID;
	
	@JsonbProperty("quantity")
	@SerializedName("quantity")
	private BigDecimal quantity = new BigDecimal(0.000);
	
	@JsonbProperty("uom")
	@SerializedName("uom")
	private String uom;
	

	@JsonbProperty("action")
	public String getAction()
	{
		return JUtility.replaceNullStringwithBlank(action);
	}
	
	@JsonbProperty("processOrderID")
	public String getProcessOrderID()
	{
		return JUtility.replaceNullStringwithBlank(processOrderID);
	}
	
	@JsonbProperty("locationID")
	public String getLocationID()
	{
		return JUtility.replaceNullStringwithBlank(locationID);
	}
	
	@JsonbProperty("quantity")
	public BigDecimal getQuantity()
	{
		return quantity;
	}
	
	@JsonbProperty("uom")
	public String getUom()
	{
		return JUtility.replaceNullStringwithBlank(uom);
	}
	
	@JsonbProperty("action")
	public void setAction(String action)
	{
		this.action = JUtility.replaceNullStringwithBlank(action);
	}
	
	@JsonbProperty("processOrderID")
	public void setProcessOrderID(String processOrder)
	{
		this.processOrderID = JUtility.replaceNullStringwithBlank(processOrder);
	}
	
	@JsonbProperty("locationID")
	public void setLocationID(String location)
	{
		this.locationID = JUtility.replaceNullStringwithBlank(location);
	}
	
	@JsonbProperty("quantity")
	public void setQuantity(BigDecimal quantity)
	{
		this.quantity  = quantity;
	}
	
	@JsonbProperty("uom")
	public void setUom(String uom)
	{
		this.uom = JUtility.replaceNullStringwithBlank(uom);
	}
		
}
