package com.commander4j.c4jWS;

import jakarta.persistence.Entity;

@Entity
public class JDBQMSelectListEntity
{
	private String selectListID;
	private String value;
	private String description;
	private Integer sequence;
	private String visible;
	
	public String getSelectListID()
	{
		return selectListID;
	}
	public void setSelectListID(String selectListID)
	{
		this.selectListID = selectListID;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Integer getSequence()
	{
		return sequence;
	}
	public void setSequence(Integer sequence)
	{
		this.sequence = sequence;
	}
	public String getVisible()
	{
		return visible;
	}
	public void setVisible(String visible)
	{
		this.visible = visible;
	}
	
}
