package com.commander4j.xml;

import java.awt.Rectangle;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JXMLSchema.java
 *
 * Package Name : com.commander4j.xml
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;

import com.commander4j.app.JVersion;
import com.commander4j.db.JDBDDL;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.sys.Common;


public class JXMLSchema
{


	public static int getSchemaVersion() {

		return JVersion.getSchemaVersion();
	}


	public static LinkedList<JDBDDL> loadDDLStatements(String driver, String directory,JLabel4j_status commandLabel) {

		// String Description = "";
		String Text = "";
		String key = "";

		LinkedList<JDBDDL> DDLList = new LinkedList<JDBDDL>();

		for (int ver = Common.hostList.getHost(Common.selectedHostID).getSchemaVersion() + 1; ver <= JVersion.getSchemaVersion(); ver++)
		{

			NumberFormat formatter = new DecimalFormat("000000");
			String filename = directory + formatter.format(ver) + ".xml";


			commandLabel.setText("Please wait, loading SQL commands from "+formatter.format(ver) + ".xml");
			Rectangle progressRect = commandLabel.getBounds();
			progressRect.x = 0;
			progressRect.y = 0;
			commandLabel.paintImmediately(progressRect);
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException ie)
			{
			}

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
					Text = Text.replace("{charset}", Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcCharacterSet());
					Text = Text.replace("{collation}", Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcCollation());

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
