package com.commander4j.email;

public class Email
{
	public String distributionID = "";
	public String subject = "";
	public String messageText = "";
	public String filename = "";
	
	public Email(String distributionID,String subject,String messageText,String filename)
	{
		this.distributionID=distributionID;
		this.subject=subject;
		this.messageText=messageText;
		this.filename=filename;
	}
}
