package com.commander4j.bom;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

public class BomImport
{

	Map<String, String> inputOutputMap = new HashMap<>();
	private JXMLDocument xmlMessage;
	UUID uuid = UUID.randomUUID();
	JDBBom bomdb;

	
	private String hostID;
	private String sessionID;
	private String errorMessage;

	public String getErrorMessage()
	{
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getHostID()
	{
		return hostID;
	}

	public void setHostID(String hostID)
	{
		this.hostID = hostID;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}
	
	public BomImport(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
		setErrorMessage("");
		bomdb = new JDBBom(getHostID(),getSessionID());
	}


	public void parse(String bom_id, String bom_version, JXMLDocument xmlMessage, String xpath, String parent_uuid)
	{
		int sequence = 1;
		String valueString = "";
		Timestamp valueTimestamp = JUtility.getSQLDateTime();
		BigDecimal valueBigDecimal = new BigDecimal("0.000");

		while (JUtility.replaceNullStringwithBlank(xmlMessage.findXPath(xpath + "[" + sequence + "]" + "/@data_id").trim()).equals("") == false)
		{

			String newPath = xpath + "[" + sequence + "]";
			String data_id = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath(newPath + "/@data_id").trim());
			String type = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath(newPath + "/@type").trim());

			uuid = UUID.randomUUID();
			String uuidString = uuid.toString();
			
			System.out.print("bom_id="+bom_id+" bom_version="+bom_version+" newPath=" + newPath + " data_id=" + data_id + " type=" + type);
			
			switch (type)
			{
			case "string":
				valueString = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath(newPath + "/@value").trim());
				System.out.print(" value=" + valueString);
				bomdb.write(bom_id, bom_version, data_id, type, valueString, uuidString, parent_uuid);
				break;
			case "timestamp":
				valueTimestamp = JUtility.getTimeStampFromISOString(JUtility.replaceNullStringwithBlank(xmlMessage.findXPath(newPath + "/@value").trim()));
				System.out.print(" value=" + valueTimestamp);
				bomdb.write(bom_id, bom_version, data_id, type, valueTimestamp, uuidString, parent_uuid);
				break;
			case "decimal":
				valueBigDecimal = new BigDecimal(JUtility.replaceNullStringwithBlank(xmlMessage.findXPath(newPath + "/@value").trim()));
				System.out.print(" value=" + valueBigDecimal);
				bomdb.write(bom_id, bom_version, data_id, type, valueBigDecimal, uuidString, parent_uuid);
				break;
			default:
				break;
			}
			System.out.println(" uuid=" + uuidString + " parent_uuid=" + parent_uuid);


			// check if element has children

			parse(bom_id, bom_version, xmlMessage, newPath + "/element", uuidString);

			sequence++;
		}
	}

	public JDBBomRecord importBom(String filename)
	{

		JDBBomRecord result = new JDBBomRecord(Common.selectedHostID,Common.sessionID);
		JDBBom bomdb = new JDBBom(Common.selectedHostID,Common.sessionID);

		xmlMessage = new JXMLDocument();
		xmlMessage.setDocument(filename);

		String recipeID = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath("/message/messageData[1]/bom[1]/@id").trim());
		String recipeVersion = JUtility.replaceNullStringwithBlank(xmlMessage.findXPath("/message/messageData[1]/bom[1]/@version").trim());

		result.setBOMId(recipeID);
		result.setBOMVersion(recipeVersion);

		bomdb.deleteBOM(recipeID, recipeVersion);
		bomdb.init();

		uuid = UUID.randomUUID();
		String uuidRecipe = uuid.toString();

		bomdb.write(recipeID, recipeVersion, "root", "string", "", uuidRecipe, "");

		parse(recipeID, recipeVersion, xmlMessage, "/message/messageData/bom/element", uuidRecipe);

		return result;
	}
}
