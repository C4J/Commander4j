package com.commander4j.spool;

public class PrintJob
{
	public String filename="";
	public String ip = "";
	public int port=0;
	//private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(PrintJob.class);
	
	public PrintJob(String ip,int port,String filename)
	{
		this.ip= ip;
		this.port = port;
		this.filename = filename;
	}
	
	public String toString()
	{
		return "PrintJob Filename=["+filename+ "] IP=["+ip+"] Port =["+String.valueOf(port)+"]";
	}
}
