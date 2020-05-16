package com.commander4j.email;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.mail.*;

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

			Timestamp lastSent;
			Boolean okToSend;

			if (emailLog.containsKey(emailKey))
			{
				lastSent = new Timestamp(emailLog.get(emailKey).getTime());
			}
			else
			{
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
				EmailAttachment attachment = new EmailAttachment();
				MultiPartEmail email = new MultiPartEmail();
				try
				{
					if (smtpProperties.get("mail.smtp.auth").toString().toLowerCase().equals("true"))
					{
						logger.debug("Email authentication required");
						email.setAuthenticator(new DefaultAuthenticator(smtpProperties.get("mail.smtp.user").toString(), smtpProperties.get("mail.smtp.password").toString()));
						email.setStartTLSEnabled(true);
					}
					else
					{
						logger.debug("Email No Authentication specified");
					}

					email.getMailSession().getProperties().putAll(smtpProperties);

					String emails = distList.get(distributionID).addressList;
					String[] emailArray = emails.split(",");
					emails = null;

					if (emailArray.length > 0)
					{

						for (int x = 0; x < emailArray.length; x++)
						{
							email.addTo(emailArray[x].toLowerCase(), "");
							logger.debug("Email To: " + emailArray[x].toLowerCase());
						}

						emailArray = null;

						try
						{

							email.setFrom(smtpProperties.get("mail.smtp.from").toString(), "");
							email.setSubject(subject);
							email.setMsg(messageText);

							// add the attachment
							if (filename.equals("") == false)
							{
								logger.debug("Email add attachment [" + Utility.getFilenameFromPath(filename) + "]");

								attachment.setPath(filename);
								attachment.setDisposition(EmailAttachment.ATTACHMENT);
								attachment.setDescription(filename);
								attachment.setName(Utility.getFilenameFromPath(filename));
								email.attach(attachment);
							}

							// send the email
							logger.debug("Email begin send...");

							email.send();

							logger.debug("Email sent successfully");

						}
						catch (Exception mex)
						{
							logger.error("Error sending email : " + mex.getMessage());
							result = false;
						}

					}

				}
				catch (Exception mex)
				{
					logger.error("Error sending email : " + mex.getMessage());
					result = false;
				}

				attachment = null;
				email = null;
			}

		}
		else
		{
			logger.debug("Disabled or empty email distribution list. No email sent.");
		}

		return result;
	}
}