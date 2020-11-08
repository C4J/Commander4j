package com.commander4j.email;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JeMailOutGoingMessage.java
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
