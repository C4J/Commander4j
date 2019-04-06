package com.commander4j.messages;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.email.JeMailOutGoingMessage;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;

public class OutgoingDespatchEmail
{
	private String hostID;
	private String sessionID;
	final Logger logger = Logger.getLogger(OutgoingDespatchEmail.class);
	private String errorMessage;
	private JFileIO fio = new JFileIO();
	private JeMailOutGoingMessage ogm;

	public String getErrorMessage()
	{
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public OutgoingDespatchEmail(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public Element addElement(Document doc, String name, String value)
	{
		Element temp = (Element) doc.createElement(name);
		Text temp_value = doc.createTextNode(value);
		temp.appendChild(temp_value);
		return temp;
	}

	public String getHostID()
	{
		return hostID;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public Boolean processMessage(Long transactionRef)
	{
		Boolean result = true;
		String path = "";

		JDBInterfaceLog il = new JDBInterfaceLog(getHostID(), getSessionID());
		GenericMessageHeader gmh = new GenericMessageHeader();
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());

		inter.getInterfaceProperties("Despatch Email", "Output");
		String device = inter.getDevice();

		JDBDespatch desp = new JDBDespatch(getHostID(), getSessionID());
		desp.setTransactionRef(transactionRef);
		desp.getDespatchPropertiesFromTransactionRef();
		setErrorMessage("");
		
		gmh.setMessageRef(desp.getTransactionRef().toString());
		gmh.setInterfaceType(inter.getInterfaceType());
		gmh.setMessageInformation("Despatch=" + desp.getDespatchNo());
		gmh.setInterfaceDirection(inter.getInterfaceDirection());
		gmh.setMessageDate(JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));

		if (device.equals("Email") || device.equals("Disk"))
		{
			path = inter.getRealPath();

			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("p_despatch_no", desp.getDespatchNo());

			String filename = path;

			if (filename.endsWith(File.separator) == false)
			{
				filename = filename + File.separator;
			}

			String tempFilename = filename + desp.getLocationIDFrom().replace(" ", "_") + "_" + desp.getLocationIDTo().replace(" ", "_") + "_" + desp.getDespatchNo() + ".tmp";

			String finalFilename = filename + desp.getLocationIDFrom().replace(" ", "_") + "_" + desp.getLocationIDTo().replace(" ", "_") + "_" + desp.getDespatchNo() + ".pdf";

			JLaunchReport.runReportToPDF("RPT_DESPATCH", parameters, "", null, tempFilename);

			try
			{
				FileUtils.deleteQuietly(new File(finalFilename));
				
				FileUtils.moveFile(new File(tempFilename), new File(finalFilename));

				il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", finalFilename);

				if (device.equals("Email"))
				{
					fio.setFilename(finalFilename);
					
					ogm = new JeMailOutGoingMessage(inter, transactionRef, fio);
					
					if (ogm.sendEmail())
					{
						il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "Email", finalFilename);
					}
					else
					{
						il.write(gmh, GenericMessageHeader.msgStatusError, "Error sending email", "File Write", finalFilename);
					}

					FileUtils.deleteQuietly(new File(finalFilename));
				}
				
				if (device.equals("Disk"))
				{
					il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", finalFilename);
				}

			}
			catch (IOException e)
			{
				setErrorMessage("Error renaming file " + tempFilename);
				result = false;
			}
		}
		else
		{
			setErrorMessage("Device type needs to be DISK or EMAIL");
			result = false;
		}

		return result;
	}

	public void setHostID(String host)
	{
		hostID = host;
	}

	public void setSessionID(String session)
	{
		sessionID = session;
	}

	public void submit(long dbTransactionRef)
	{
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		inter.getInterfaceProperties("Despatch Email", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(dbTransactionRef, "Despatch Email");
		}
		else
		{
			logger.debug("Interface Despatch Email - Output is DISABLED");
		}
	}
}
