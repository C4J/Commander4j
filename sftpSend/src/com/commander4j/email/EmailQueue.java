package com.commander4j.email;

import java.util.LinkedList;

public class EmailQueue
{
	public LinkedList<Email> queue = new LinkedList<Email>();
	public SendEmail sendmail = new SendEmail();
	
	public void setEnabled(boolean value)
	{
		sendmail.setEnabled(value);
	}

	public boolean getlEnabled()
	{
		return sendmail.getlEnabled();
	}

	public int getQueueSize()
	{
		return queue.size();
	}
	
	public synchronized void addToQueue(String distributionID, String subject, String messageText, String filename)
	{
		//AutoLab.systemNotify.appendToMessage("Requesting email to distribution list ["+distributionID+"].");
		Email email = new Email(distributionID, subject, messageText, filename);
		queue.addLast(email);
	}

	public synchronized void addToQueue(Email email)
	{
		if (queue.size() < 10)
		{
			queue.addLast(email);
		}
	}

	public synchronized Email getFromQueue()
	{
		Email result = queue.getFirst();
		queue.removeFirst();
		return result;
	}

	public synchronized void processQueue()
	{
		while (queue.size() > 0)
		{
			Email email = getFromQueue();
			sendmail.Send(email.distributionID, email.subject, email.messageText, email.filename);

		}
	}

}
