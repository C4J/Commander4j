package com.commander4j.messages;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBPalletHistory;
import com.commander4j.email.OutGoingMessage;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;

public class OutgoingSortNotify
{
	private String hostID;
	private String sessionID;
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(OutgoingSortNotify.class);
	private String errorMessage;
	private JFileIO fio = new JFileIO();
	private OutGoingMessage ogm;

	public String getErrorMessage()
	{
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public OutgoingSortNotify(String host, String session)
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

		inter.getInterfaceProperties("Sort Notification", "Output");
		String device = inter.getDevice();
		String format = inter.getFormat();

		setErrorMessage("");

		if (device.equals("Email") || device.equals("Disk"))
		{
			if (format.equals("PDF"))
			{

				path = inter.getRealPath();

				JDBPalletHistory palhist = new JDBPalletHistory(getHostID(), getSessionID());
				ResultSet rs = palhist.getInterfacingData(transactionRef, "SORT", "NOTIFY", Long.valueOf(1), "SSCC", "asc");

				try
				{
					if (rs.next())
					{
						palhist.getPropertiesfromResultSet(rs);

						try
						{
							String sscc = palhist.getPallet().getSSCC();
							HashMap<String, Object> parameters = new HashMap<String, Object>();

							parameters.put("P_SSCC", sscc);

							gmh.setMessageRef(transactionRef.toString());
							gmh.setInterfaceType(inter.getInterfaceType());
							gmh.setMessageInformation("SSCC=" + sscc);
							gmh.setInterfaceDirection(inter.getInterfaceDirection());
							gmh.setMessageDate(JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));

							String filename = path;

							if (filename.endsWith(File.separator) == false)
							{
								filename = filename + File.separator;
							}

							String tempFilename = filename + sscc + ".tmp";

							String finalFilename = filename + sscc + ".pdf";

							JLaunchReport.runReportToPDF("RPT_NOTIFY_SORTING", parameters, "", null, tempFilename);

							try
							{
								FileUtils.deleteQuietly(new File(finalFilename));

								FileUtils.moveFile(new File(tempFilename), new File(finalFilename));

								if (device.equals("Email"))
								{
									fio.setFilename(finalFilename);
									fio.setShortFilename(sscc + ".pdf");

									if (inter.getEmailSuccess())
									{

										ogm = new OutGoingMessage(inter, transactionRef, fio);

										if (ogm.sendEmail())
										{
											il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "Email", finalFilename);
											FileUtils.deleteQuietly(new File(finalFilename));
										}
										else
										{
											result = false;
											setErrorMessage("Error sending email");
											il.write(gmh, GenericMessageHeader.msgStatusError, "Error sending email", "File Write", finalFilename);
										}
									}
									else
									{
										result = false;
										setErrorMessage("Email on Success not selected");

										il.write(gmh, GenericMessageHeader.msgStatusError, "Email on Success not selected", "Config", finalFilename);
									}
								}

								if (device.equals("Disk"))
								{
									il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", finalFilename);
								}

							}
							catch (IOException e)
							{
								il.write(gmh, GenericMessageHeader.msgStatusError, e.getMessage(), "File Rename", tempFilename);
								setErrorMessage("Error renaming file " + tempFilename);
								result = false;
							}

						}
						catch (Exception ex)
						{
							logger.error("Error sending message. " + ex.getMessage());
							ex.printStackTrace();

						}
					}
					else
					{
						logger.debug("Could not find Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef));
					}
					rs.close();
				}
				catch (SQLException e)
				{
					logger.debug("Error finding Pallet History Interfacing Data for Transaction Ref  " + String.valueOf(transactionRef) + " " + e.getMessage());
				}
			}
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
		inter.getInterfaceProperties("Sort Notification", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(dbTransactionRef, "Sort Notification");
		}
		else
		{
			logger.debug("Interface Sorting Notify - Output is DISABLED");
		}
	}
}
