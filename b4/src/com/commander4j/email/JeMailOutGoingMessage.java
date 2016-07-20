package com.commander4j.email;

import javax.mail.MessagingException;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;

import com.commander4j.db.JDBInterface;
import com.commander4j.util.JFileIO;

public class JeMailOutGoingMessage
{

	private String emailaddresses;
	private JeMail mail;
	private JFileIO xfio;
	private String[] emailList;
	private JDBInterface interx;
	private Long txnRef;

	
	public JeMailOutGoingMessage(JDBInterface inter, Long transactionRef,JFileIO fio) {
		
		interx = inter;
		txnRef = transactionRef;
		emailaddresses = inter.getEmailAddresses();
		mail = new JeMail(inter.getHostID(), inter.getSessionID());
		xfio = fio;

		StringConverter stringConverter = new StringConverter();
		ArrayConverter arrayConverter = new ArrayConverter(String[].class, stringConverter);
		arrayConverter.setDelimiter(';');
		arrayConverter.setAllowedChars(new char[] { '@' });

		emailList = (String[]) arrayConverter.convert(String[].class, emailaddresses);
	}
	
	public boolean sendEmail()
	{
		boolean result = true;
		if (emailList.length > 0) {
			try {

				mail.postMail(emailList, 
						      interx.getInterfaceType() + " (" + String.valueOf(txnRef) + ")", 
						      interx.getInterfaceType() + " message attached.", 
						      xfio.getShortFilename(), 
						      xfio.getFilename());

			}
			catch (MessagingException e) {

			}
		}		
		return result;
	}
}
