package com.commander4j.entity;

import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.Entity;

@JsonbPropertyOrder({ "action", "userID", "userPassword", "commandStatus"})

@Entity
public class JQMUserEntity
{
	@JsonbProperty("action")
	@SerializedName("action")
	private String action;
	
	@JsonbProperty("userID")
	@SerializedName("userID")
	private String userID;
	
	@JsonbProperty("userPassword")
	@SerializedName("userPassword")
	private String userPassword;
	
	@JsonbProperty("commandStatus")
	@SerializedName("commandStatus")
	private String commandStatus;

	@JsonbProperty("action")
	public String getAction()
	{
		return JUtility.replaceNullStringwithBlank(action);
	}
	
	@JsonbProperty("action")
	public void setAction(String action)
	{
		this.action = JUtility.replaceNullStringwithBlank(action);
	}
	
	@JsonbProperty("userID")
	public String getUserID()
	{
		return JUtility.replaceNullStringwithBlank(userID).toUpperCase();
	}
	
	@JsonbProperty("userID")
	public void setUserID(String userID)
	{
		this.userID = JUtility.replaceNullStringwithBlank(userID);
	}
	
	@JsonbProperty("userPassword")
	public String getUserPassword()
	{
		return JUtility.replaceNullStringwithBlank(userPassword);
	}
	
	@JsonbProperty("userPassword")
	public void setUserPassword(String pass)
	{
		this.userPassword = JUtility.replaceNullStringwithBlank(pass);
	}
	
	@JsonbProperty("commandStatus")
	public String getCommandStatus()
	{
		return JUtility.replaceNullStringwithBlank(commandStatus);
	}
	
	@JsonbProperty("commandStatus")
	public void setCommandStatus(String commandStatus)
	{
		this.commandStatus = JUtility.replaceNullStringwithBlank(commandStatus);
	}

	@Override
	public String toString()
	{
		return "user="+getUserID().toString()+" userPassword="+getUserPassword().toString();
	}
}
