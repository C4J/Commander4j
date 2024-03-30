package com.commander4j.email;

public class Email
{
	public String[] recipients;
	public String subject = "";
	public String messageText = "";
	public String filename = "";
	
	public Email(String[] recipients,String subject,String messageText,String filename)
	{
		this.recipients=recipients;
		this.subject=subject;
		this.messageText=messageText;
		this.filename=filename;
	}
}
