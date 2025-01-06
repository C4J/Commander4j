package com.commander4j.email;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;

import com.commander4j.db.JDBInterface;
import com.commander4j.exception.ExceptionHTML;
import com.commander4j.exception.ExceptionMsg;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;

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

			ExceptionHTML ept = new ExceptionHTML(interx.getInterfaceType(), "Description", "10%", "Detail", "30%");
			ept.clear();
			ept.addRow(new ExceptionMsg("Site Name", Common.hostList.getHost(interx.getHostID()).getSiteDescription()));
			ept.addRow(new ExceptionMsg("Computer Name", JUtility.getClientName()));
			ept.addRow(new ExceptionMsg("Interface Type", interx.getInterfaceType()));
			ept.addRow(new ExceptionMsg("Interface Direction", interx.getInterfaceDirection()));
			ept.addRow(new ExceptionMsg("Transaction Ref", String.valueOf(txnRef)));
			ept.addRow(new ExceptionMsg("Processing Date", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime())));
			ept.addRow(new ExceptionMsg("Attached File", xfio.getShortFilename()));
			ept.addRow(new ExceptionMsg("Comment", "See attached report"));
			
			Common.sendmail.Send(emailList, interx.getInterfaceType(), ept.getHTML(), xfio.getFilename());

		}
		return result;
	}
}
