package com.commander4j.c4jWS;

import java.sql.Timestamp;

import jakarta.persistence.Entity;

@Entity
public class JQMTrayResultEntity
{
	private Long trayID;
	private Long sampleID;
	private Long sequenceID;
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


	public void setSampleID(Long sampleID)
	{
		this.sampleID = sampleID;
	}
	
	public void setSequenceID(Long sequenceID)
	{
		this.sequenceID = sequenceID;
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
