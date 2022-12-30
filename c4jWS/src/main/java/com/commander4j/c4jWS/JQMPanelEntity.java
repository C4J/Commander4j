package com.commander4j.c4jWS;

import java.sql.Timestamp;

import com.commander4j.util.JUtility;

import jakarta.persistence.Entity;

@Entity
public class JQMPanelEntity
{
	private Long panelID;
	private Timestamp panelDate;
	private String plant;
	private String description;
	private String status;
	private Timestamp updated;
	private Timestamp created;

	public Long getPanelID()
	{
		if (panelID == null)
		{
			panelID = (long) -1;
		}
		return panelID;
	}

	public void setPanelID(Long panelID)
	{
		if (panelID == null)
		{
			panelID = (long) -1;
		}
		this.panelID = panelID;
	}

	public Timestamp getPanelDate()
	{
		if (panelDate == null)
		{
			panelDate = JUtility.getSQLDateTime();
		}
		return panelDate;
	}

	public void setPanelDate(Timestamp panelDate)
	{
		if (panelDate == null)
		{
			panelDate = JUtility.getSQLDateTime();
		}
		this.panelDate = panelDate;
	}

	public String getPlant()
	{
		return plant;
	}

	public void setPlant(String plant)
	{
		this.plant = plant;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		if (status == null)
		{
			status = "Panel";
		}
		this.status = status;
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
		System.out.print("panelID=" + getPanelID().toString());
		System.out.print("panelDate=" + getPanelDate().toString());
		return "panelID=" + getPanelID().toString() + "panelDate=" + getPanelDate().toString() + " status=" + getStatus() + " created " + getCreated() + " updated " + getUpdated();
	}
}
