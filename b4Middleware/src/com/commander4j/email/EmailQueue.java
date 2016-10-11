package com.commander4j.email;

import java.util.LinkedList;

public class EmailQueue
{
	public LinkedList<Email> queue = new LinkedList<Email>();
	public SendEmail sendmail = new SendEmail();
	
	public void addToQueue(String distributionID,String subject,String messageText,String filename)
	{
		Email email = new Email(distributionID,subject,messageText,filename);
		queue.addLast(email);
	}	
	
	public void addToQueue(Email email)
	{
		queue.addLast(email);
	}
	
	public Email getFromQueue()
	{
		Email result = 	queue.getFirst();
		queue.removeFirst();
		return result;
	}
	
	public void processQueue()
	{
		while (queue.size()>0)
		{
			Email email = getFromQueue();
			sendmail.Send(email.distributionID,email.subject,email.messageText,email.filename);
			
		}
	}
	
}
