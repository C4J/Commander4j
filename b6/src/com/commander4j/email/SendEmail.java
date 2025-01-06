package com.commander4j.email;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;
import com.commander4j.util.EncryptData;
import com.commander4j.util.JCipher;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class SendEmail
{
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((SendEmail.class));

	private Properties smtpProperties;
	private boolean initialised = false;
	private boolean enabled = false;
	private String configID="";
	private JCipher cipher = new JCipher(EncryptData.key);

	public void init()
	{

		String filename = System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config" + File.separator + "email.xml";

		JXMLDocument doc = new JXMLDocument(filename);

		try
		{
			enabled = Boolean.valueOf(doc.findXPath("/emailSettings/email/@enabled").trim());
			configID = doc.findXPath("/emailSettings/email/@configID");
		}
		catch (Exception ex)
		{
			enabled = false;
		}

		smtpProperties = System.getProperties();
		Boolean cont = true;
		int seq = 1;
		while (cont)
		{
			String prop = doc.findXPath("/emailSettings/"+configID+"/property[" + String.valueOf(seq) + "]/@name").trim();
			String val  = doc.findXPath("/emailSettings/"+configID+"/property[" + String.valueOf(seq) + "]/@value").trim();		
			String encrypted = doc.findXPath("//configuration/property[" + String.valueOf(seq) + "]/@encrypted").trim().toLowerCase();
			
			if (encrypted.equals(""))
			{
				encrypted = "no";
			}
			
			if (encrypted.equals("yes"))
			{
				val = cipher.decode(val);
			}			
			if (prop.equals(""))
			{
				cont = false;
			}
			else
			{
				smtpProperties.setProperty(prop, val);
				seq++;
			}
		}

	}

	public synchronized boolean Send(String[] recipients, String subject, String messageText, String filename)
	{
		boolean result = true;
		
		if (initialised == false)
		{
			init();
			initialised = true;
		}

		if (enabled)
		{

			if (recipients != null)
			{

				if (recipients.length > 0)
				{
					
					try
					{

						Properties propAuth = new Properties();
						Properties propNoAuth = new Properties();

						propAuth.putAll(smtpProperties);
						propNoAuth.putAll(smtpProperties);

						Session authenticatedSession = Session.getInstance(propAuth, new Authenticator()
						{
							@Override
							protected PasswordAuthentication getPasswordAuthentication()
							{
								return new PasswordAuthentication(smtpProperties.get("mail.smtp.user").toString(), smtpProperties.get("mail.smtp.password").toString());
							}
						});

						propNoAuth.put("mail.smtp.user", "");
						propNoAuth.put("mail.smtp.password", "");

						Session unauthenticatedSession = Session.getInstance(propAuth, null);

						MimeMessage message;

						if (smtpProperties.get("mail.smtp.auth").toString().toLowerCase().equals("true"))
						{
							logger.debug("Email authentication required");
							message = new MimeMessage(authenticatedSession);
						}
						else
						{
							logger.debug("Email no authentication required");
							message = new MimeMessage(unauthenticatedSession);
						}

						String emails = StringUtils.join(recipients, ",");

						logger.debug("Email To: " + emails);
						message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(emails));

						message.setFrom(new InternetAddress(smtpProperties.get("mail.smtp.from").toString()));

						message.setSubject(subject);

						MimeBodyPart mimeBodyPart = new MimeBodyPart();

						mimeBodyPart.setContent(messageText, "text/html");

						Multipart multipart = new MimeMultipart();
						multipart.addBodyPart(mimeBodyPart);

						if (filename.equals("") == false)
						{
							logger.debug("Email add attachment [" + JUtility.getFilenameFromPath(filename) + "]");

							MimeBodyPart attachmentBodyPart = new MimeBodyPart();
							attachmentBodyPart.attachFile(new File(filename));
							attachmentBodyPart.setDescription(filename);

							multipart.addBodyPart(attachmentBodyPart);

						}
						message.setContent(multipart);

						logger.debug("Sending email");
						Transport.send(message);
						logger.debug("Email sent");

						message = null;
					}
					catch (Exception ex)
					{
						logger.error("Error encountered sending email [" + ex.getMessage() + "]");
					}
				}
			}
		}

		return result;
	}
}