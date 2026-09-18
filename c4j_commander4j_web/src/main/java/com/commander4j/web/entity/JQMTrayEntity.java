/* Copied AS-IS from c4j_web_WS (2026-09-12, step 8 port): only the package changed, the decorative @Entity annotation was dropped (jakarta.persistence is not in this war; serialisation is Gson) and the sample check uses core com.commander4j.db.JDBQMSample. */
package com.commander4j.web.entity;

import java.sql.Timestamp;

import com.commander4j.util.JUtility;


public class JQMTrayEntity
{
	private Long panelID = (long) 0;
	private Long trayID = (long) 0;
	private Long traySequence = (long) 0;
	private String description = "";
	private Timestamp updated;
	private Timestamp created;
	private String queryType = "";

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
		if (trayID == null)
		{
		//	trayID = (long) -1;
		}
		return trayID;
	}
	public void setTrayID(Long trayID)
	{
		this.trayID = trayID;
	}
	public Long getTraySequence()
	{
		return traySequence;
	}
	public void setTraySequence(Long traySequence)
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
	public String getqueryType()
	{
		return queryType;
	}
	public void setqueryType(String queryType)
	{
		this.queryType = queryType;
	}

	@Override
	public String toString()
	{
		return "panelID="+
	JUtility.replaceNullObjectwithBlank(getPanelID())+
	"trayID="+JUtility.replaceNullObjectwithBlank(getTrayID())+
	" description="+getDescription()+
	" created "+
	JUtility.replaceNullObjectwithBlank(getCreated())+
	" updated "+JUtility.replaceNullObjectwithBlank(getUpdated())+
	" queryType "+JUtility.replaceNullObjectwithBlank(getqueryType());
	}
}
