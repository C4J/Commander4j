package com.commander4j.email;

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

/**
 * Helper class for easy population of a
 * <code>javax.mail.internet.MimeMessage</code>.
 * 
 * <p>
 * Mirrors the simple setters of SimpleMailMessage, directly applying the values
 * to the underlying MimeMessage. Also offers support for typical mail
 * attachments. Advanced settings can still be applied directly to underlying
 * MimeMessage!
 * 
 * <p>
 * Typically used in MimeMessagePreparator implementations or JavaMailSender
 * client code: simply instantiating it as a facade to a MimeMessage, invoking
 * setters on the facade, using the underlying MimeMessage for mail sending.
 * Also used internally by JavaMailSenderImpl.
 * 
 * <p>
 * Sample code:
 * <p>
 * <code>
 * mailSender.send(new MimeMessagePreparator() {<br>
 * &nbsp;&nbsp;public void prepare(MimeMessage mimeMessage) throws MessagingException {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;message.setFrom("me@mail.com");<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;message.setTo("you@mail.com");<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;message.setSubject("my subject");<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;message.setText("my text");<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;message.addAttachment("logo.gif", new ClassPathResource("images/mylogo.gif"));<br>
 * &nbsp;&nbsp;}<br>
 * });
 * </code>
 * 
 * @author Juergen Hoeller
 * @since 19.01.2004
 * @see javax.mail.internet.MimeMessage
 * @see #getMimeMessage
 * @see MimeMessagePreparator
 * @see JavaMailSender
 * @see JavaMailSenderImpl
 * @see org.springframework.mail.SimpleMailMessage
 */
public class JeMailHelper
{

	private final MimeMessage mimeMessage;

	private MimeMultipart mimeMultipart = null;

	/**
	 * Create new MimeMessageHelper for the given MimeMessage, assuming a simple
	 * text message (no multipart content).
	 * 
	 * @param mimeMessage
	 *            MimeMessage to work on
	 * @see #MimeMessageHelper(javax.mail.internet.MimeMessage, boolean)
	 */
	public JeMailHelper(MimeMessage mimeMessage)
	{
		this.mimeMessage = mimeMessage;
	}

	/**
	 * Create new MimeMessageHelper for the given MimeMessage, in multipart mode
	 * (supporting attachments) if requested.
	 * 
	 * @param mimeMessage
	 *            MimeMessage to work on
	 * @param multipart
	 *            whether to create a multipart message that supports
	 *            attachments
	 */
	public JeMailHelper(MimeMessage mimeMessage, boolean multipart) throws MessagingException
	{
		this.mimeMessage = mimeMessage;
		if (multipart)
		{
			this.mimeMultipart = new MimeMultipart();
			this.mimeMessage.setContent(this.mimeMultipart);
		}
	}

	/**
	 * Return the underlying MimeMessage.
	 */
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

	/**
	 * Sets the given text directly as content in non-multipart mode
	 * respectively as default body part in multipart mode.
	 * 
	 * @param text
	 *            text to set
	 * @param html
	 *            whether to apply content type "text/html" for an HTML mail,
	 *            using default content type ("text/plain") else
	 */
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

	/**
	 * Add an attachment to the given MimeMessage, taking the content from a
	 * java.io.File.
	 * <p>
	 * The content type will be determined by the name of the given content
	 * file. Do not use this for temporary files with arbitrary filenames
	 * (possibly ending in ".tmp" or the like)!
	 * 
	 * @param attachmentFilename
	 *            the name of the attachment as it will appear in the mail
	 * @param file
	 *            the File resource to take the content from
	 * @throws MessagingException
	 * @see #addAttachment(String,
	 *      org.springframework.core.io.InputStreamSource)
	 * @see #addAttachment(String, javax.activation.DataSource)
	 */
	public void addAttachment(String attachmentFilename, File file) throws MessagingException {
		addAttachment(attachmentFilename, new FileDataSource(file));
	}

	/**
	 * Add an attachment to the given MimeMessage, taking the content from an
	 * org.springframework.core.InputStreamResource.
	 * <p>
	 * The content type will be determined by the given filename for the
	 * attachment. Thus, any content source will be fine, including temporary
	 * files with arbitrary filenames.
	 * 
	 * @param attachmentFilename
	 *            the name of the attachment as it will appear in the mail
	 * @param inputStreamSource
	 *            the resource to take the content from
	 * @see #addAttachment(String, File)
	 * @see #addAttachment(String, javax.activation.DataSource)
	 */
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

	/**
	 * Add an attachment to the given MimeMessage, taking the content from a
	 * <code>javax.activation.DataSource</code>.
	 * <p>
	 * Note that the InputStream returned by the DataSource implementation needs
	 * to be a <i>fresh one on each call</i>, as JavaMail will invoke
	 * getInputStream() multiple times.
	 * 
	 * @param attachmentFilename
	 *            the name of the attachment as it will appear in the mail (the
	 *            content type will be determined by this)
	 * @param dataSource
	 *            the <code>javax.activation.DataSource</code> to take the
	 *            content from, determining the InputStream and the content type
	 * @throws MessagingException
	 *             in case of errors
	 * @see #addAttachment(String, File)
	 * @see #addAttachment(String,
	 *      org.springframework.core.io.InputStreamSource)
	 */
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
