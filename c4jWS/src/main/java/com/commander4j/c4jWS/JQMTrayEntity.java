package com.commander4j.c4jWS;

import java.sql.Timestamp;

import com.commander4j.util.JUtility;

import jakarta.persistence.Entity;

@Entity
public class JQMTrayEntity
{
	private Long panelID;
	private Long trayID;
	private int traySequence;
	private String description;
	private Timestamp updated;
	private Timestamp created;
	
	public Long getPanelID()
	{
		return panelID;
	}
	public void setPanelID(Long panelID)
	{
		this.panelID = panelID;
	}
	public Long getTrayID()
	{
		return trayID;
	}
	public void setTrayID(Long trayID)
	{
		this.trayID = trayID;
	}
	public int getTraySequence()
	{
		return traySequence;
	}
	public void setTraySequence(int traySequence)
	{
		this.traySequence = traySequence;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Timestamp getUpdated()
	{
		return updated;
	}
	public void setUpdated(Timestamp updated)
	{
		this.updated = updated;
	}
	public Timestamp getCreated()
	{
		return created;
	}
	public void setCreated(Timestamp created)
	{
		this.created = created;
	}

		
	@Override
	public String toString()
	{
		return "panelID="+
	JUtility.replaceNullObjectwithBlank(getPanelID())
	+"trayID="+JUtility.replaceNullObjectwithBlank(getTrayID())+ " description="+getDescription()+" created "+JUtility.replaceNullObjectwithBlank(getCreated())+" updated "+JUtility.replaceNullObjectwithBlank(getUpdated());
	}
}
