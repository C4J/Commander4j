package com.commander4j.email;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.commander4j.util.Utility;

public class SendEmail
{
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((SendEmail.class));

	private Properties smtpProperties;
	private HashMap<String, distributionList> distList = new HashMap<String, distributionList>();
	private HashMap<String, Timestamp> emailLog = new HashMap<String, Timestamp>();
	private Calendar cal;

	public void init(String distributionID)
	{

		if (distList.containsKey(distributionID) == false)
		{
			String filename = System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config" + File.separator + "email.xml";

			JXMLDocument doc = new JXMLDocument(filename);

			smtpProperties = System.getProperties();
			Boolean cont = true;
			int seq = 1;
			while (cont)
			{
				String prop = doc.findXPath("//configuration/property[" + String.valueOf(seq) + "]/@name").trim();
				String val = doc.findXPath("//configuration/property[" + String.valueOf(seq) + "]/@value").trim();
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

			String addressList = "";
			addressList = Utility.replaceNullStringwithBlank(doc.findXPath("//emailSettings/distributionList[@id='" + distributionID + "'][@enabled='Y']/toAddressList").trim());
			String temp = Utility.replaceNullStringwithBlank(doc.findXPath("//emailSettings/distributionList[@id='" + distributionID + "'][@enabled='Y']/@maxFrequencyMins").trim());

			if (temp.equals(""))
				temp = "0";

			distributionList newItem = new distributionList();

			newItem.listId = distributionID;
			newItem.addressList = addressList;
			newItem.maxFrequencyMins = Long.valueOf(temp);

			distList.put(distributionID, newItem);
		}
	}

	public synchronized boolean Send(String distributionID, String subject, String messageText, String filename)
	{
		boolean result = true;

		if (Common.emailEnabled == false)
		{
			return result;
		}

		if (distList.containsKey(distributionID) == false)
		{
			init(distributionID);
		}

		if (distList.containsKey(distributionID) == true)
		{

			String emailKey = "[" + distributionID + "] - [" + subject + "]";
			logger.debug(emailKey);
			Session session;
			Timestamp lastSent;
			Boolean okToSend;

			if (emailLog.containsKey(emailKey))
			{
				lastSent = new Timestamp(emailLog.get(emailKey).getTime());
			}
			else
			{
				// Enter dummmy last email sent date so that first email will be
				// sent
				cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_YEAR, -30);
				lastSent = new Timestamp(cal.getTime().getTime());
				emailLog.put(emailKey, lastSent);
			}

			long ageInMins = Utility.compareTwoTimeStamps(Utility.getSQLDateTime(), lastSent);
			logger.debug("Last email to " + emailKey + " was at " + lastSent);
			logger.debug("Minutes since last email to " + emailKey + " is " + String.valueOf(ageInMins));

			if (ageInMins >= distList.get(distributionID).maxFrequencyMins)
			{
				okToSend = true;
				logger.debug("Email allowed");
			}
			else
			{
				okToSend = false;
				logger.debug("Email suppressed - too fequent");
			}

			if (okToSend)
			{

				if (smtpProperties.get("mail.smtp.auth").toString().toLowerCase().equals("true"))
				{
					session = Session.getInstance(smtpProperties, new javax.mail.Authenticator()
					{
						protected PasswordAuthentication getPasswordAuthentication()
						{
							return new PasswordAuthentication(smtpProperties.get("mail.smtp.user").toString(), smtpProperties.get("mail.smtp.password").toString());
						}
					});
				}
				else
				{
					session = Session.getInstance(smtpProperties);
				}

				try
				{

					MimeMessage message = new MimeMessage(session);
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(distList.get(distributionID).addressList));
					message.setSubject(subject);
					// message.setText(messageText);

					MimeBodyPart mbp1 = new MimeBodyPart();
					MimeBodyPart mbp2 = new MimeBodyPart();

					mbp1.setText(messageText);

					Multipart mp = new MimeMultipart();
					mp.addBodyPart(mbp1);

					if (filename.equals("") == false)
					{
						FileDataSource fds = new FileDataSource(filename);
						mbp2.setDataHandler(new DataHandler(fds));

						mbp2.setFileName(fds.getName());
						mp.addBodyPart(mbp2);
					}
					message.setContent(mp);
					message.setSentDate(new Date());

					Transport.send(message);
					emailLog.get(emailKey).setTime(Utility.getSQLDateTime().getTime());
					
					message = null;
					mbp1 = null;
					mbp2 = null;
					mp = null;

					logger.debug("Email sent successfully..");
				}
				catch (MessagingException mex)
				{
					logger.error("Error sending email : " + mex.getMessage());
					result = false;
				}
			}
			
			session=null;

		}
		else
		{
			logger.debug("Disabled or empty email distribution list. No email sent.");
		}

		return result;
	}
}