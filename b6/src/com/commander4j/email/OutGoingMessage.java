package com.commander4j.email;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;

import com.commander4j.db.JDBInterface;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;

public class OutGoingMessage
{

	private String emailaddresses;
	private JFileIO xfio;
	private String[] emailList;
	private JDBInterface interx;
	private Long txnRef;

	public OutGoingMessage(JDBInterface inter, Long transactionRef, JFileIO fio)
	{

		interx = inter;
		txnRef = transactionRef;
		emailaddresses = inter.getEmailAddresses();
		xfio = fio;

		StringConverter stringConverter = new StringConverter();
		ArrayConverter arrayConverter = new ArrayConverter(String[].class, stringConverter);
		arrayConverter.setDelimiter(';');
		arrayConverter.setAllowedChars(new char[]
		{ '@' });

		emailList = (String[]) arrayConverter.convert(String[].class, emailaddresses);
	}

	public boolean sendEmail()
	{
		boolean result = true;
		if (emailList.length > 0)
		{

			Common.sendmail.Send(emailList, interx.getInterfaceType() + " (" + String.valueOf(txnRef) + ")", interx.getInterfaceType() + " message attached.", xfio.getFilename());

		}
		return result;
	}
}
