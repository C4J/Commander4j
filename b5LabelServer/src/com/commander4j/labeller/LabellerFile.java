package com.commander4j.labeller;

import java.sql.Timestamp;

import org.apache.logging.log4j.Logger;

public class LabellerFile
{
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((LabellerFile.class));
	private volatile String filename;
	private volatile Long size;
	private volatile Timestamp datetime;
	private String date;
	private String time;
	private LabellerUtility utils = new LabellerUtility();
	
	public Timestamp getDatetime()
	{
		return datetime;
	}
	public String getFilename()
	{
		return filename;
	}
	public Long getSize()
	{
		return size;
	}
	
	public void setDateTime(String date,String time)
	{
		this.date = date;
		this.time = time;
		datetime = utils.getTimeStampFromlogopakDIRString(date, time);
	}
	
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	public void setSize(Long size)
	{
		this.size = size;
	}
	
	public String toString()
	{
		return utils.padString(filename, true, 30, " ")+utils.padString(String.valueOf(this.size), false, 12, " ")+utils.padString(6, " ")+datetime.toString();
	}
	
	
}
