package com.commander4j.email;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JeMail.java
 * 
 * Package Name : com.commander4j.email
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import javax.mail.MessagingException;
import org.apache.commons.mail.*;
import org.apache.log4j.Logger;
import com.commander4j.db.JDBControl;
import com.commander4j.util.JUtility;

public class JeMail
{
	private static String SMTP_HOST_LOADED = "N/A";
	private static String SMTP_AUTH_REQD;
	private static String SMTP_HOST_NAME;
	private static String SMTP_AUTH_USER;
	private static String SMTP_AUTH_PWD;
	private static String SMTP_FROM_ADRESS;
	private static String MAIL_SMTP_PORT;
	private static String MAIL_SMTP_SSL_PORT;
	private static String SMTP_USE_SSL;
	private static String SMTP_USE_TLS;
	private final Logger logger = Logger.getLogger(JeMail.class);
	private String hostID;
	private String sessionID;

	public JeMail(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());

		if (SMTP_HOST_LOADED.equals("N/A") == true)
		{
			SMTP_AUTH_REQD = ctrl.getKeyValueWithDefault("MAIL_SMTP_AUTH_REQD", "true", "SMTP Authentication Reqd");
			SMTP_HOST_NAME = ctrl.getKeyValueWithDefault("MAIL_SMTP_HOST_NAME", "mail.testsmtp.com", "SMTP Server for sending emails");
			SMTP_AUTH_USER = ctrl.getKeyValueWithDefault("MAIL_SMTP_AUTH_USER", "mailUser", "SMTP Username");
			SMTP_AUTH_PWD = ctrl.getKeyValueWithDefault("MAIL_SMTP_AUTH_PWD", "mailPassword", "SMTP Password");
			MAIL_SMTP_PORT = ctrl.getKeyValueWithDefault("MAIL_SMTP_PORT", "25", "SMTP Port");
			MAIL_SMTP_SSL_PORT = ctrl.getKeyValueWithDefault("MAIL_SMTP_SSL_PORT", "465", "SMTP SSL Port");
			SMTP_FROM_ADRESS = ctrl.getKeyValueWithDefault("MAIL_SMTP_FROM_ADRESS", "Commander4j", "From Email");
			SMTP_USE_SSL = ctrl.getKeyValueWithDefault("MAIL_SMTP_USE_SSL", "False", "Use SSL");
			SMTP_USE_TLS = ctrl.getKeyValueWithDefault("MAIL_SMTP_USE_TLS", "False", "Use TLS");
			SMTP_HOST_LOADED = host;
		}

	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	private String getHostID()
	{
		return hostID;
	}

	public void setSMTPServer(String hostName, String userName, String password)
	{
		SMTP_HOST_NAME = hostName;
		SMTP_AUTH_USER = userName;
		SMTP_AUTH_PWD = password;
	}

	public void postMail(String recipientsTO[],   String subject, String message, String attachmentFilename, String attachmentLongFilename) throws MessagingException
	{
		
		logger.debug("SMTP_AUTH_REQD="+SMTP_AUTH_REQD);
		logger.debug("MAIL_SMTP_HOST_NAME="+SMTP_HOST_NAME);
		logger.debug("MAIL_SMTP_AUTH_USER="+SMTP_AUTH_USER);
		logger.debug("MAIL_SMTP_AUTH_PWD=********");
		logger.debug("MAIL_SMTP_PORT="+MAIL_SMTP_PORT);
		logger.debug("MAIL_SMTP_SSL_PORT="+MAIL_SMTP_SSL_PORT);
		logger.debug("MAIL_SMTP_FROM_ADRESS="+SMTP_FROM_ADRESS);
		logger.debug("MAIL_SMTP_USE_SSL="+SMTP_USE_SSL);
		
		//Email email = new SimpleEmail();
		logger.debug("Creating MultiPart Email");
		MultiPartEmail email = new MultiPartEmail();

		logger.debug("Setting Host Name to "+SMTP_HOST_NAME);
		email.setHostName(SMTP_HOST_NAME);
		logger.debug("Setting SMTP Port to "+MAIL_SMTP_PORT);
		email.setSmtpPort(Integer.valueOf(MAIL_SMTP_PORT));
		
		logger.debug("Setting SMTP SSL Port to "+MAIL_SMTP_SSL_PORT);
		email.setSslSmtpPort(MAIL_SMTP_SSL_PORT);
		
		logger.debug("Setting Use SSL on Connect to  "+SMTP_USE_SSL);
		email.setSSLOnConnect(Boolean.valueOf(SMTP_USE_SSL));	
		
		logger.debug("Authentication Required =  "+SMTP_AUTH_REQD);
		
		if (SMTP_AUTH_REQD.toUpperCase().equals("TRUE"))
		{
			email.setAuthenticator(new DefaultAuthenticator(SMTP_AUTH_USER, SMTP_AUTH_PWD));

		}

		logger.debug("Setting SMTP USE SSL =  "+SMTP_USE_SSL);
		email.setSSLOnConnect(Boolean.valueOf(SMTP_USE_SSL));

		logger.debug("Setting SMTP USE TLS =  "+SMTP_USE_TLS);
		email.setStartTLSEnabled(Boolean.valueOf(SMTP_USE_TLS));
		email.setStartTLSRequired(Boolean.valueOf(SMTP_USE_TLS));
		
		try
		{
			logger.debug("From Address =  "+SMTP_FROM_ADRESS);
			email.setFrom(SMTP_FROM_ADRESS);
			email.setSubject(subject);
			email.setMsg(message+"\n\n");
			for (int x = 1; x <= recipientsTO.length; x++)
			{
				logger.debug("Add To Address =  "+recipientsTO[x - 1]);
				email.addTo(recipientsTO[x - 1]);
			}
			
			if (JUtility.replaceNullStringwithBlank(attachmentFilename).equals("")==false)
			{
				  // Create the attachment
				 EmailAttachment attachment = new EmailAttachment();
				  attachment.setPath(attachmentLongFilename);
				  attachment.setDisposition(EmailAttachment.ATTACHMENT);
				  attachment.setDescription(attachmentFilename);
				  attachment.setName(attachmentFilename);

				  // add the attachment
				  logger.debug("Add Attachment");
				  email.attach(attachment);
			}

			logger.debug("Sending");
			email.send();
			logger.debug("Sent successfully");
		}
		catch (EmailException e)
		{
			logger.error("Unable to send email : "+e.getCause().getMessage());
		}

	}

}
