package com.commander4j.xml;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;

import com.commander4j.app.JVersion;
import com.commander4j.db.JDBDDL;
import com.commander4j.sys.Common;

//import com.commander4j.util.JUtility;

/**
 */
public class JXMLSchema
{
	/**
	 * 
	 * Retrive the Schema Version number from the schema.xml file. This version
	 * number is the correct number for the application and is compared to the
	 * value held in the SYS_CONTROL table "SCHEMA VERSION" to determine if an
	 * update is required during application startup.
	 * 
	 * @return int
	 */

	public static int getSchemaVersion() {

		return JVersion.getSchemaVersion();
	}

	/**
	 * 
	 * @param driver
	 *            - detemines the jdbc driver being used and is used to retrive
	 *            the correct sql DDL commands from the file schema.xml
	 * 
	 * @param filename
	 *            - determines the name of the xml file holding the schema DDL
	 *            sql commands. Normally this should be xml/schema.xml
	 * 
	 * @return - returns a linked list of type DDL which is used by
	 *         JFrameHostAdmin to build or upgrade the applications database
	 *         schema. *
	 */
	public static LinkedList<JDBDDL> loadDDLStatements(String driver, String directory) {

		// String Description = "";
		String Text = "";
		String key = "";

		LinkedList<JDBDDL> DDLList = new LinkedList<JDBDDL>();

		for (int ver = Common.hostList.getHost(Common.selectedHostID).getSchemaVersion() + 1; ver <= JVersion.getSchemaVersion(); ver++)
		{

			NumberFormat formatter = new DecimalFormat("000000");
			String filename = directory + formatter.format(ver) + ".xml";
			JXMLDocument xmltest = new JXMLDocument(filename);
			@SuppressWarnings("unused")
			int counter = 0;
			int seq = 1;
			do
			{

				key = "//DDL/statement[" + String.valueOf(seq) + "]";
				Text = xmltest.findXPath(key);

				if (Text.equals("") == false)
				{
					Text = Text.replace("{schema}", Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema());
					Text = Text.replace("{database}", Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabase());
					JDBDDL ddl = new JDBDDL();
					ddl.setVersion(ver);
					ddl.setSequence(seq);
					ddl.setText(Text);
					DDLList.addLast(ddl);
					counter++;
				}

				seq++;

			}
			while (Text.equals("") == false);

		}

		return DDLList;
	}

}
