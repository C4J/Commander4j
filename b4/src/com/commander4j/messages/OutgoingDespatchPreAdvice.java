package com.commander4j.messages;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBPalletHistory;
import com.commander4j.db.JDBUom;
import com.commander4j.email.JeMailOutGoingMessage;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

public class OutgoingDespatchPreAdvice
{
	private String hostID;
	private String sessionID;
	final Logger logger = Logger.getLogger(OutgoingDespatchPreAdvice.class);
	private String errorMessage;
	private JFileIO fio = new JFileIO();
	private JeMailOutGoingMessage ogm;

	public String getErrorMessage() {
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public OutgoingDespatchPreAdvice(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public Element addElement(Document doc, String name, String value) {
		Element temp = (Element) doc.createElement(name);
		Text temp_value = doc.createTextNode(value);
		temp.appendChild(temp_value);
		return temp;
	}

	public String getHostID() {
		return hostID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public Boolean processMessage(Long transactionRef) {
		Boolean result = false;
		String path = "";

		JDBInterfaceLog il = new JDBInterfaceLog(getHostID(), getSessionID());
		GenericMessageHeader gmh = new GenericMessageHeader();
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		JDBUom uoml = new JDBUom(getHostID(), getSessionID());
		inter.getInterfaceProperties("Despatch Pre Advice", "Output");
		String device = inter.getDevice();

		JDBDespatch desp = new JDBDespatch(getHostID(), getSessionID());
		desp.setTransactionRef(transactionRef);
		desp.getDespatchPropertiesFromTransactionRef();

		String sourceGLN = JUtility.replaceNullStringwithBlank(desp.getLocationDBFrom().getGLN());
		String destinationGLN = JUtility.replaceNullStringwithBlank(desp.getLocationDBTo().getGLN());
		Boolean suppressMessage = false;

		gmh.setMessageRef(desp.getTransactionRef().toString());
		gmh.setInterfaceType(inter.getInterfaceType());
		gmh.setMessageInformation("Despatch=" + desp.getDespatchNo());
		gmh.setInterfaceDirection(inter.getInterfaceDirection());
		gmh.setMessageDate(JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));

		if (sourceGLN.length() == 0)
		{
			setErrorMessage("Message Suppressed - No GLN Source (From) GLN for Location [" + desp.getLocationIDFrom() + "]");
			suppressMessage = true;
		}

		if (destinationGLN.length() == 0)
		{
			setErrorMessage("Message Suppressed - No GLN Destination (To) GLN for Location [" + desp.getLocationIDTo() + "]");
			suppressMessage = true;
		}

		if (suppressMessage == true)
		{
			result = true;
			il.write(gmh, GenericMessageHeader.msgStatusWarning, getErrorMessage(), "File Write", "");

		}
		else
		{

			if (inter.getFormat().equals("XML"))
			{

				try
				{
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();

					Document document = builder.newDocument();

					Element message = (Element) document.createElement("message");

					Element hostUniqueID = addElement(document, "hostRef", Common.hostList.getHost(getHostID()).getUniqueID());
					message.appendChild(hostUniqueID);

					Element messageRef = addElement(document, "messageRef", String.valueOf(transactionRef));
					message.appendChild(messageRef);

					Element messageType = addElement(document, "interfaceType", "Despatch Pre Advice");
					message.appendChild(messageType);

					Element messageInformation = addElement(document, "messageInformation", "Despatch=" + desp.getDespatchNo());
					message.appendChild(messageInformation);

					Element messageDirection = addElement(document, "interfaceDirection", "Output");
					message.appendChild(messageDirection);

					Element messageDate = addElement(document, "messageDate", JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()));
					message.appendChild(messageDate);

					Element despatchConfirmation = (Element) document.createElement("despatchPreAdvice");

					Element noofpallets = addElement(document, "numberOfPallets", String.valueOf(desp.getTotalPallets()));
					despatchConfirmation.appendChild(noofpallets);

					Element haulier = addElement(document, "haulier", String.valueOf(JUtility.replaceNullStringwithBlank(desp.getHaulier())));
					despatchConfirmation.appendChild(haulier);

					Element trailer = addElement(document, "trailer", String.valueOf(JUtility.replaceNullStringwithBlank(desp.getTrailer())));
					despatchConfirmation.appendChild(trailer);
					
					Element load = addElement(document, "loadNo", String.valueOf(JUtility.replaceNullStringwithBlank(desp.getLoadNo())));
					despatchConfirmation.appendChild(load);
					
					Element journey = addElement(document, "journeyRef", String.valueOf(JUtility.replaceNullStringwithBlank(desp.getJourneyRef())));
					despatchConfirmation.appendChild(journey);
					
					Element despatch = addElement(document, "despatchNo", desp.getDespatchNo());
					despatchConfirmation.appendChild(despatch);

					Element despatchDate = addElement(document, "despatchDate", JUtility.getISOTimeStampStringFormat(desp.getDespatchDate()));
					despatchConfirmation.appendChild(despatchDate);

					Element locationFrom = addElement(document, "fromLocation", desp.getLocationIDFrom());
					despatchConfirmation.appendChild(locationFrom);

					Element locationFromGLN = addElement(document, "fromGLN", desp.getLocationDBFrom().getGLN());
					despatchConfirmation.appendChild(locationFromGLN);

					Element locationTo = addElement(document, "toLocation", desp.getLocationIDTo());
					despatchConfirmation.appendChild(locationTo);

					Element locationToGLN = addElement(document, "toGLN", desp.getLocationDBTo().getGLN());
					despatchConfirmation.appendChild(locationToGLN);

					Element contents = (Element) document.createElement("contents");
					despatchConfirmation.appendChild(contents);

					JDBPalletHistory palhist = new JDBPalletHistory(getHostID(), getSessionID());
					ResultSet rs = palhist.getInterfacingData(transactionRef, "DESPATCH", "TO", Long.valueOf(0), "SSCC", "asc");

					int x = 1;
					try
					{
						rs.beforeFirst();
						while (rs.next())
						{
							palhist.getPropertiesfromResultSet(rs);
							Element pallet = (Element) document.createElement("pallet");

							Element item = addElement(document, "item", String.valueOf(x));
							pallet.appendChild(item);
							x++;

							Element sscc = addElement(document, "SSCC", palhist.getPallet().getSSCC());
							pallet.appendChild(sscc);

							Element material = addElement(document, "material", palhist.getPallet().getMaterial());
							pallet.appendChild(material);

							Element ean = addElement(document, "ean", palhist.getPallet().getEAN());
							pallet.appendChild(ean);

							Element variant = addElement(document, "variant", palhist.getPallet().getVariant());
							pallet.appendChild(variant);

							Element qty = addElement(document, "quantity", palhist.getPallet().getQuantity().toString());
							pallet.appendChild(qty);

							String paluom = palhist.getPallet().getUom();
							paluom = uoml.convertUom(inter.getUOMConversion(), paluom);

							Element uom = addElement(document, "UOM", paluom);
							pallet.appendChild(uom);

							Element status = addElement(document, "status", palhist.getPallet().getStatus());
							pallet.appendChild(status);

							Element bbe = addElement(document, "bestBefore", JUtility.getISOTimeStampStringFormat(palhist.getPallet().getMaterialBatchExpiryDate()));
							pallet.appendChild(bbe);

							Element dom = addElement(document, "productionDate", JUtility.getISOTimeStampStringFormat(palhist.getPallet().getDateOfManufacture()));
							pallet.appendChild(dom);

							Element batch = addElement(document, "batch", palhist.getPallet().getBatchNumber());
							pallet.appendChild(batch);

							Element batchStatus = addElement(document, "batchStatus", palhist.getPallet().getMaterialBatchStatus());
							pallet.appendChild(batchStatus);

							contents.appendChild(pallet);

						}
						rs.close();
					}
					catch (SQLException e)
					{

					}

					Element messageData = (Element) document.createElement("messageData");
					messageData.appendChild(despatchConfirmation);

					message.appendChild(messageData);

					document.appendChild(message);

					JXMLDocument xmld = new JXMLDocument();
					xmld.setDocument(document);
					gmh.decodeHeader(xmld);

					if (device.equals("Disk") | device.equals("Email"))
					{
						path = inter.getRealPath();
						if (fio.writeToDisk(path, document, transactionRef, "_"+desp.getLocationIDTo().replace(" ", "_")+"_DespatchPreAdvice.xml") == true)
						{
							result = true;
							il.write(gmh, GenericMessageHeader.msgStatusSuccess, "Processed OK", "File Write", fio.getFilename());
							setErrorMessage("");
							if (device.equals("Email"))
							{
								ogm = new JeMailOutGoingMessage(inter,transactionRef,fio);
								ogm.sendEmail();
							}
						}
						else
						{
							result = false;
							il.write(gmh, GenericMessageHeader.msgStatusError, fio.getErrorMessage(), "File Write", fio.getFilename());
							setErrorMessage(fio.getErrorMessage());
						}
					}
				}

				catch (Exception ex)
				{
					logger.error("Error sending message. " + ex.getMessage());
					ex.printStackTrace();

				}
			}
		}

		return result;
	}

	public void setHostID(String host) {
		hostID = host;
	}

	public void setSessionID(String session) {
		sessionID = session;
	}

	public void submit(long dbTransactionRef) {
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		inter.getInterfaceProperties("Despatch Pre Advice", "Output");
		if (inter.isEnabled() == true)
		{
			JDBInterfaceRequest ir = new JDBInterfaceRequest(getHostID(), getSessionID());
			ir.write(dbTransactionRef, "Despatch Pre Advice");
		}
		else
		{
			logger.debug("Interface Despatch Despatch Pre Advice - Output is DISABLED");
		}
	}
}
