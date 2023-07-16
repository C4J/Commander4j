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

public class JeMailOutGoingMessageManual
{

	private String emailaddresses;
	private JeMail mail;
	private String[] emailList;
	private String subject;
	private String message;
	private String shortFilename;
	private String longFilename;

	
	public JeMailOutGoingMessageManual(String host,String session,String[] addresses,String subj,String msg,String shortname,String longname) {
		
		this.emailList = addresses;
		
		mail = new JeMail(host, session);
		
		this.subject = subj;
		this.message = msg;
		this.shortFilename=shortname;
		this.longFilename=longname;


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
						      subject, 
						      message, 
						      shortFilename, 
						      longFilename);

			}
			catch (MessagingException e) {

			}
		}		
		return result;
	}
}
