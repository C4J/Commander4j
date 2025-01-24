package com.commander4j.entity;

import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.Entity;

@JsonbPropertyOrder({ "action", "bom_id", "bom_version", "stage","input_output","stage", "material","description", "location_id","commandStatus"})

@Entity
public class JQMViewBOMEntity
{

	@JsonbProperty("action")
	@SerializedName("action")
	private String action;
	
	@JsonbProperty("bom_id")
	@SerializedName("bom_id")
	private String bom_id;
	
	@JsonbProperty("bom_version")
	@SerializedName("bom_version")
	private String bom_version;
	
	@JsonbProperty("stage")
	@SerializedName("stage")
	private String stage;
	
	@JsonbProperty("input_output")
	@SerializedName("input_output")
	private String input_Output;
	
	@JsonbProperty("material")
	@SerializedName("material")
	private String material;

	@JsonbProperty("description")
	@SerializedName("description")
	private String description;
	
	@JsonbProperty("location_id")
	@SerializedName("location_id")
	private String location_id;

	@JsonbProperty("commandStatus")
	@SerializedName("commandStatus")
	private String commandStatus;

	@JsonbProperty("action")
	public String getAction()
	{
		return this.action;
	}
	
	@JsonbProperty("action")
	public void setAction(String val)
	{
		this.action = val;
	}

	@JsonbProperty("location_id")
	public String getLocation_id()
	{
		return this.location_id;
	}
	
	@JsonbProperty("location_id")
	public void setLocation_id(String val)
	{
		this.location_id = val;
	}

	@JsonbProperty("input_output")
	public String getInputOutput()
	{
		return JUtility.replaceNullStringwithBlank(this.input_Output);
	}
	
	@JsonbProperty("input_output")
	public void setInputOutput(String val)
	{
		this.input_Output = JUtility.replaceNullStringwithBlank(val);
	}
	
	@JsonbProperty("material")
	public String getMaterial()
	{
		return JUtility.replaceNullStringwithBlank(this.material);
	}
	
	@JsonbProperty("material")
	public void setMaterial(String val)
	{
		this.material = JUtility.replaceNullStringwithBlank(val);
	}
	
	@JsonbProperty("description")
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(this.description);
	}
	
	@JsonbProperty("description")
	public void setDescription(String val)
	{
		this.description = JUtility.replaceNullStringwithBlank(val);
	}
	
	@JsonbProperty("stage")
	public String getStage()
	{
		return JUtility.replaceNullStringwithBlank(this.stage);
	}
	
	@JsonbProperty("stage")
	public void setStage(String val)
	{
		this.stage = JUtility.replaceNullStringwithBlank(val);
	}
	
	@JsonbProperty("bom_id")
	public String getBomID()
	{
		return JUtility.replaceNullStringwithBlank(this.bom_id);
	}
	
	@JsonbProperty("bom_id")
	public void setBomID(String val)
	{
		this.bom_id = JUtility.replaceNullStringwithBlank(val);
	}
	
	@JsonbProperty("bom_version")
	public String getBomVersion()
	{
		return JUtility.replaceNullStringwithBlank(this.bom_version);
	}
	
	@JsonbProperty("bom_version")
	public void setBomVersion(String val)
	{
		this.bom_version = JUtility.replaceNullStringwithBlank(val);
	}
	
	@JsonbProperty("commandStatus")
	public void setCommandStatus(String val)
	{
		this.commandStatus = JUtility.replaceNullStringwithBlank(val);
	}
	
	@JsonbProperty("commandStatus")
	public String getCommandStatus()
	{
		return JUtility.replaceNullStringwithBlank(commandStatus);
	}
	
}
