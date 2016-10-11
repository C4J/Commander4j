package com.commander4j.email;

public class testemail
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

		//SendEmail se = new SendEmail();
		EmailQueue eq = new EmailQueue();
		
		eq.addToQueue("System", "Subject of Email", "This is the message content\nLine 2\nLine 3\n\n", "/Users/dave/Commander4j/workspace.osx/b4Middleware/xml/config/config.xml");
		eq.addToQueue("Error", "Subject of Email", "This is the message content", "");
		
		//se.Send("System", "Subject of Email", "This is the message content\nLine 2\nLine 3\n\n", "/Users/dave/Commander4j/workspace.osx/b4Middleware/xml/config/config.xml");
		//se.Send("Error", "Subject of Email", "This is the message content", "");
		eq.processQueue();
	}

}
