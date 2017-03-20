package com.commander4j.email;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JeMailHelper.java
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.FileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.core.io.InputStreamSource;

public class JeMailHelper
{

	private final MimeMessage mimeMessage;

	private MimeMultipart mimeMultipart = null;

	public JeMailHelper(MimeMessage mimeMessage)
	{
		this.mimeMessage = mimeMessage;
	}

	public JeMailHelper(MimeMessage mimeMessage, boolean multipart) throws MessagingException
	{
		this.mimeMessage = mimeMessage;
		if (multipart)
		{
			this.mimeMultipart = new MimeMultipart();
			this.mimeMessage.setContent(this.mimeMultipart);
		}
	}

	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}

	public void setFrom(String from) throws MessagingException {
		this.mimeMessage.setFrom(new InternetAddress(from));
	}

	public void setTo(String to) throws MessagingException {
		this.mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
	}

	public void setTo(String[] to) throws MessagingException {
		for (int i = 0; i < to.length; i++)
		{
			this.mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
		}
	}

	public void setCc(String cc) throws MessagingException {
		this.mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
	}

	public void setCc(String[] cc) throws MessagingException {
		for (int i = 0; i < cc.length; i++)
		{
			this.mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
		}
	}

	public void setBcc(String bcc) throws MessagingException {
		this.mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
	}

	public void setBcc(String[] bcc) throws MessagingException {
		for (int i = 0; i < bcc.length; i++)
		{
			this.mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
		}
	}

	public void setSubject(String subject) throws MessagingException {
		this.mimeMessage.setSubject(subject);
	}

	public void setText(String text) throws MessagingException {
		setText(text, false);
	}

	public void setText(final String text, boolean html) throws MessagingException {
		Part partToUse = null;
		if (this.mimeMultipart != null)
		{
			BodyPart bodyPart = null;
			for (int i = 0; i < this.mimeMultipart.getCount(); i++)
			{
				BodyPart bp = this.mimeMultipart.getBodyPart(i);
				if (bp.getFileName() == null)
				{
					bodyPart = bp;
				}
			}
			if (bodyPart == null)
			{
				MimeBodyPart mimeBodyPart = new MimeBodyPart();
				this.mimeMultipart.addBodyPart(mimeBodyPart);
				bodyPart = mimeBodyPart;
			}
			partToUse = bodyPart;
		}
		else
		{
			partToUse = this.mimeMessage;
		}
		if (html)
		{
			// need to use a javax.activation.DataSource (!) to set a text
			// with content type "text/html"
			partToUse.setDataHandler(new DataHandler(new DataSource() {
				public InputStream getInputStream() throws IOException {
					return new ByteArrayInputStream(text.getBytes());
				}

				public OutputStream getOutputStream() throws IOException {
					throw new UnsupportedOperationException("Read-only javax.activation.DataSource");
				}

				public String getContentType() {
					return "text/html";
				}

				public String getName() {
					return "text";
				}
			}));
		}
		else
		{
			partToUse.setText(text);
		}
	}

	public void addAttachment(String attachmentFilename, File file) throws MessagingException {
		addAttachment(attachmentFilename, new FileDataSource(file));
	}

	public void addAttachment(final String attachmentFilename, final InputStreamSource inputStreamSource) throws MessagingException {
		addAttachment(attachmentFilename, new DataSource() {
			public InputStream getInputStream() throws IOException {
				return inputStreamSource.getInputStream();
			}

			public OutputStream getOutputStream() {
				throw new UnsupportedOperationException("Read-only javax.activation.DataSource");
			}

			public String getContentType() {
				return FileTypeMap.getDefaultFileTypeMap().getContentType(attachmentFilename);
			}

			public String getName() {
				return attachmentFilename;
			}
		});
	}

	public void addAttachment(String attachmentFilename, DataSource dataSource) throws MessagingException {
		if (this.mimeMultipart == null)
		{
			throw new IllegalStateException("Cannot add attachment - not in multipart mode");
		}
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setFileName(attachmentFilename);
		bodyPart.setDataHandler(new DataHandler(dataSource));
		this.mimeMultipart.addBodyPart(bodyPart);
	}

}
