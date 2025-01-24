package com.commander4j.entity;

import com.commander4j.util.JUtility;

import jakarta.persistence.Entity;

@Entity
public class JQMResourceEntity
{
	private String required_resource;
	private String description;

	public String getRequiredResource()
	{
		return JUtility.replaceNullStringwithBlank(required_resource).toUpperCase();
	}
	public void setRequiredResource(String resource)
	{
		this.required_resource = JUtility.replaceNullStringwithBlank(resource);
	}
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(description);
	}
	public void setDescription(String desc)
	{
		this.description = JUtility.replaceNullStringwithBlank(desc);
	}

	@Override
	public String toString()
	{
		return "resource="+getRequiredResource().toString()+" description="+getDescription().toString();
	}
}
