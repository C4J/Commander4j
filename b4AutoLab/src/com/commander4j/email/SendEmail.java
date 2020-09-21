package com.commander4j.email;

import java.io.File;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.mail.*;

import org.apache.logging.log4j.Logger;

import com.commander4j.autolab.AutoLab;
import com.commander4j.utils.JUtility;
import com.commander4j.xml.JXMLDocument;

public class SendEmail
{
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((SendEmail.class));

	private Properties smtpProperties;
	private HashMap<String, distributionList> distList = new HashMap<String, distributionList>();
	private HashMap<String, Calendar> emailLog = new HashMap<String, Calendar>();
	private JUtility utils = new JUtility();

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
			String enabled = "";
			addressList = utils.replaceNullStringwithBlank(doc.findXPath("//emailSettings/distributionList[@id='" + distributionID + "'][@enabled='Y']/toAddressList").trim());
			String temp = utils.replaceNullStringwithBlank(doc.findXPath("//emailSettings/distributionList[@id='" + distributionID + "'][@enabled='Y']/@maxFrequencyMins").trim());
			enabled = utils.replaceNullStringwithBlank(doc.findXPath("//emailSettings/distributionList[@id='" + distributionID + "'][@enabled='Y']/@enabled").trim());

			if (temp.equals(""))
				temp = "0";

			distributionList newItem = new distributionList();

			newItem.listId = distributionID;
			newItem.addressList = addressList;
			newItem.maxFrequencyMins = Long.valueOf(temp);
			newItem.enabled = enabled.toUpperCase();

			distList.put(distributionID, newItem);
		}
	}

	public synchronized boolean Send(String distributionID, String subject, String messageText, String filename)
	{
		boolean result = true;

		if (AutoLab.config.isEmailEnabled() == false)
		{
			logger.debug("Email disabled in config.xml");
			return result;
		}

		if (distList.containsKey(distributionID) == false)
		{
			init(distributionID);
		}

		if (distList.containsKey(distributionID) == true)
		{

			if (distList.get(distributionID).enabled.equals("Y"))
			{

				String emailKey = "[" + distributionID + "] - [" + subject + "]";
				logger.debug(emailKey);

				Calendar lastSent;
				Calendar now = Calendar.getInstance();

				Boolean okToSend = false;

				if (emailLog.containsKey(emailKey))
				{
					lastSent = emailLog.get(emailKey);
				}
				else
				{
					okToSend = true;
					lastSent = now;
					emailLog.put(emailKey, lastSent);
				}

				long seconds = (now.getTimeInMillis() - lastSent.getTimeInMillis()) / 1000;

				long ageInMins = seconds / 60;

				logger.debug("Last email to " + emailKey + " was at " + utils.getISODateStringFromCalendar(lastSent));
				logger.debug("Current time is " + utils.getISODateStringFromCalendar(now));

				logger.debug("Minutes since last email to " + emailKey + " is " + String.valueOf(ageInMins));

				if (ageInMins >= distList.get(distributionID).maxFrequencyMins)
				{
					okToSend = true;
					emailLog.put(emailKey, now);
					logger.debug("Email allowed");
				}
				else
				{
					// okToSend = false;
					logger.debug("Email suppressed - too frequent");
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
									logger.debug("Email add attachment [" + utils.getFilenameFromPath(filename) + "]");

									attachment.setPath(filename);
									attachment.setDisposition(EmailAttachment.ATTACHMENT);
									attachment.setDescription(filename);
									attachment.setName(utils.getFilenameFromPath(filename));
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
				logger.debug("Email Distribution list ["+distributionID+"] is disabled.");
			}
		}
		else
		{
			logger.debug("Disabled or empty email distribution list ["+distributionID+"]. No email sent.");
		}

		return result;
	}
}