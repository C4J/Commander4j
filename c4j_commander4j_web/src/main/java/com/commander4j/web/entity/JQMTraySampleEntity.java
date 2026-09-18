/* Copied AS-IS from c4j_web_WS (2026-09-12, step 8 port): only the package changed, the decorative @Entity annotation was dropped (jakarta.persistence is not in this war; serialisation is Gson) and the sample check uses core com.commander4j.db.JDBQMSample. */
package com.commander4j.web.entity;

import java.sql.Timestamp;

import com.google.gson.annotations.Expose;


public class JQMTraySampleEntity
{
	@Expose
	private Long trayID;
	@Expose
	private Long sequenceID;
	private String sequenceLetter;
	@Expose
	private Long sampleID;
	private Timestamp updated;
	private Timestamp created;

	public Long getTrayID()
	{
		if (trayID == null)
		{
			trayID = (long) 0;
		}
		return trayID;
	}

	public void setTrayID(Long trayID)
	{
		this.trayID = trayID;
	}

	public Long getSequenceID()
	{
		if (sequenceID == null)
		{
			sequenceID = (long) 0;
		}
		return sequenceID;
	}

	public String getSequenceLetter()
	{
		return sequenceLetter;
	}

	public void setSequenceID(Long sequenceID)
	{
		this.sequenceID = sequenceID;
	}

	public void setSequenceLetter(String sequenceLetter)
	{
		this.sequenceLetter = sequenceLetter;
	}

	public Long getSampleID()
	{
		if (sampleID == null)
		{
			sampleID = (long) 0;
		}
		return sampleID;
	}

	public void setSampleID(Long sampleID)
	{
		this.sampleID = sampleID;
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
		return "trayID="+getTrayID().toString()+ " sequenceID="+getSequenceID()+" sampleID="+getSampleID() +" created "+getCreated()+" updated "+getUpdated();
	}
}
