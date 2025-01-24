package com.commander4j.entity;

import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.Entity;

@JsonbPropertyOrder({ "hostID",	"description"})

@Entity
public class JQMHostEntity
{

	@JsonbProperty("hostID")
	@SerializedName("hostID")
	private String hostID;
	
	@JsonbProperty("description")
	@SerializedName("description")
	private String description;
	

	@JsonbProperty("hostID")
	public String getHostID()
	{
		return JUtility.replaceNullStringwithBlank(hostID);
	}
	
	@JsonbProperty("description")
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(description);
	}

	@JsonbProperty("hostID")
	public void setHostID(String hostID)
	{
		this.hostID = JUtility.replaceNullStringwithBlank(hostID);
	}
	
	@JsonbProperty("description")
	public void setDescription(String description)
	{
		this.description = JUtility.replaceNullStringwithBlank(description);
	}
			
}
