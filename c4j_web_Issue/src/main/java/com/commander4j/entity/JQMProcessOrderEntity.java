package com.commander4j.entity;

import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.Entity;

@JsonbPropertyOrder({ "processOrderID","material","description","status","bom_id","bom_version"})

@Entity
public class JQMProcessOrderEntity
{
	@JsonbProperty("processOrderID")
	@SerializedName("processOrderID")
	private String processOrderID;
	
	@JsonbProperty("material")
	@SerializedName("material")
	private String material;
	
	@JsonbProperty("description")
	@SerializedName("description")
	private String description;
	
	@JsonbProperty("status")
	@SerializedName("status")
	private String status;
	
	@JsonbProperty("bom_id")
	@SerializedName("bom_id")
	private String bom_id;
	
	@JsonbProperty("bom_version")
	@SerializedName("bom_version")
	private String bom_version;

	@JsonbProperty("processOrderID")
	public String getProcessOrderID()
	{
		return JUtility.replaceNullStringwithBlank(processOrderID);
	}
	
	@JsonbProperty("processOrderID")
	public void setUserProcessOrderID(String var)
	{
		this.processOrderID = JUtility.replaceNullStringwithBlank(var);
	}
	
	@JsonbProperty("material")
	public String getMaterial()
	{
		return JUtility.replaceNullStringwithBlank(material);
	}
	
	@JsonbProperty("material")
	public void setMaterial(String var)
	{
		this.material = JUtility.replaceNullStringwithBlank(var);
	}
	
	@JsonbProperty("description")
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(description);
	}
	
	@JsonbProperty("description")
	public void setDescription(String var)
	{
		this.description = JUtility.replaceNullStringwithBlank(var);
	}
	
	@JsonbProperty("status")
	public String getStatus()
	{
		return JUtility.replaceNullStringwithBlank(status);
	}
	
	@JsonbProperty("status")
	public void setStatus(String var)
	{
		this.status = JUtility.replaceNullStringwithBlank(var);
	}
	
	@JsonbProperty("bom_id")
	public String getBomID()
	{
		return JUtility.replaceNullStringwithBlank(bom_id);
	}
	
	@JsonbProperty("bom_id")
	public void setBomID(String var)
	{
		this.bom_id = JUtility.replaceNullStringwithBlank(var);
	}
	
	@JsonbProperty("bom_version")
	public String getBomVersion()
	{
		return JUtility.replaceNullStringwithBlank(bom_version);
	}
	
	@JsonbProperty("bom_version")
	public void setBomVersion(String var)
	{
		this.bom_version = JUtility.replaceNullStringwithBlank(var);
	}
		
	@Override
	public String toString()
	{
		return "process order="+getProcessOrderID().toString()+"material="+getMaterial().toString()+ " description="+getDescription()+ " bom_id="+getBomID()+ " bom_version="+getBomVersion();
	}
}
