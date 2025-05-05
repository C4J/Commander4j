package com.commander4j.c4jWS;

import com.commander4j.util.JUtility;

import jakarta.persistence.Entity;

@Entity
public class JDBControlEntity
{
	private String systemKey;
	private String keyValue;
	private String description;

	
	public String getSystemKey()
	{
		return JUtility.replaceNullStringwithBlank(systemKey);
	}
	public void setSystemKey(String systemKey)
	{
		this.systemKey = JUtility.replaceNullStringwithBlank(systemKey);
	}
	public String getKeyValue()
	{
		return JUtility.replaceNullStringwithBlank(keyValue);
	}
	public void setKeyValue(String keyValue)
	{
		this.keyValue = JUtility.replaceNullStringwithBlank(keyValue);
	}
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(description);
	}
	public void setDescription(String description)
	{
		this.description = JUtility.replaceNullStringwithBlank(description);
	}
		
	@Override
	public String toString()
	{
		return "systemKey="+getSystemKey().toString()+"keyValue="+getKeyValue().toString()+ " description="+getDescription();
	}
}
