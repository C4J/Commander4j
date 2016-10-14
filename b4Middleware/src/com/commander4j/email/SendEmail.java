package com.commander4j.email;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JXMLDocument;

public class SendEmail
{
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((SendEmail.class));
	private String addressList = "";
	Properties properties;

	public void init(String distributionID)
	{

		String filename = System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config" + File.separator + "email.xml";

		JXMLDocument doc = new JXMLDocument(filename);

		properties = System.getProperties();
		Boolean cont = true;
		int seq = 1;
		while (cont)
		{
			String prop = doc.findXPath("//configuration/property[" + String.valueOf(seq) + "]/@name").trim();
			String val = doc.findXPath("//configuration/property[" + String.valueOf(seq) + "]/@value").trim();
			if (prop.equals(""))
			{
				cont=false;
			}
			else
			{
				properties.setProperty(prop, val);
				seq++;
			}
		}
		//select="/DataSet/Data/[@Value1='2']/@Value2"
		addressList = doc.findXPath("//emailSettings/distributionList[@id='" + distributionID + "']/toAddressList").trim();

	}

	public synchronized boolean Send(String distributionID, String subject, String messageText, String filename)
	{
		boolean result = true;
		
		if (Common.emailEnabled==false)
		{
			return result;
		}

		init(distributionID);

		Session session;
		
		if (properties.get("mail.smtp.auth").toString().toLowerCase().equals("true"))
		{
			session = Session.getInstance(properties, new javax.mail.Authenticator()
			{
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(properties.get("mail.smtp.user").toString(), properties.get("mail.smtp.password").toString());
				}
			});
		} else
		{
			session = Session.getInstance(properties);
		}

		try
		{

			MimeMessage message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addressList));
			message.setSubject(subject);
			//message.setText(messageText);
			
			MimeBodyPart mbp1 = new MimeBodyPart();
			MimeBodyPart mbp2 = new MimeBodyPart();
			
		    mbp1.setText(messageText);
		    
		    Multipart mp = new MimeMultipart();
		    mp.addBodyPart(mbp1);
			
			if (filename.equals("")==false)
			{
		         FileDataSource fds = new FileDataSource(filename);
		         mbp2.setDataHandler(new DataHandler(fds));
		        
		         mbp2.setFileName(fds.getName());
		         mp.addBodyPart(mbp2);
			}
			message.setContent(mp);
			message.setSentDate(new Date());
			
			Transport.send(message);
			logger.debug("Email sent successfully..");
		} catch (MessagingException mex)
		{
			logger.error("Error sending email : "+mex.getMessage());
			result = false;
		}

		return result;
	}
}