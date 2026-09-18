/* Copied AS-IS from c4j_web_WS (2026-09-12, step 8 port): only the package changed, the decorative @Entity annotation was dropped (jakarta.persistence is not in this war; serialisation is Gson) and the sample check uses core com.commander4j.db.JDBQMSample. */
package com.commander4j.web.entity;

import java.sql.Timestamp;


public class JQMTrayResultEntity
{
	private Long trayID;
	private Long sampleID;
	private Long sequenceID;
	private String sequenceLetter;
	private String userID;
	private String testID;
	private String value;
	private Timestamp updated;
	private Timestamp created;


	public Long getTrayID()
	{
		return trayID;
	}


	public void setTrayID(Long trayID)
	{
		this.trayID = trayID;
	}


	public Long getSampleID()
	{
		return sampleID;
	}

	public Long getSequenceID()
	{
		return sequenceID;
	}

	public String getSequenceLetter()
	{
		return sequenceLetter;
	}

	public void setSampleID(Long sampleID)
	{
		this.sampleID = sampleID;
	}

	public void setSequenceID(Long sequenceID)
	{
		this.sequenceID = sequenceID;
	}

	public void setSequenceLetter(String sequenceLetter)
	{
		this.sequenceLetter = sequenceLetter;
	}

	public String getUserID()
	{
		return userID;
	}


	public void setUserID(String userID)
	{
		this.userID = userID;
	}


	public String getTestID()
	{
		return testID;
	}


	public void setTestID(String testId)
	{
		this.testID = testId;
	}


	public String getValue()
	{
		return value;
	}


	public void setValue(String value)
	{
		this.value = value;
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
		return "trayID="+getTrayID().toString()+ " sampleID="+getSampleID() + " userID="+getUserID()+ " testID="+getTestID()+ " value="+getValue()+" created "+getCreated()+" updated "+getUpdated();
	}
}
