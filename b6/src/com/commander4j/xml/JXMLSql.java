package com.commander4j.xml;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JXMLSql.java
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

import java.util.LinkedList;

import com.commander4j.db.JDBSQLStatement;
import com.commander4j.sys.Common;


public class JXMLSql
{

	public static LinkedList<JDBSQLStatement> loadSQLStatements(String host, String driver, String filename) {

		String sNumber = "";
		String statementId = "";
		String statementText = "";


		LinkedList<JDBSQLStatement> sqlList = new LinkedList<JDBSQLStatement>();
		JXMLDocument xmltest = new JXMLDocument(filename);

		try
		{
			int i = 1;
			do
			{
				sNumber = Integer.toString(i);

				try
				{
					statementId = xmltest.findXPath("//SQL/jdbcDriver[@Type='" + driver + "']/statement[" + sNumber + "]/id");
					statementText = xmltest.findXPath("//SQL/jdbcDriver[@Type='" + driver + "']/statement[" + sNumber + "]/text");
					statementText = statementText.replace("{schema}", Common.hostList.getHost(host).getDatabaseParameters().getjdbcDatabaseSchema());
					statementText = statementText.replace("{database}", Common.hostList.getHost(host).getDatabaseParameters().getjdbcDatabase());

				}
				catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}

				if (statementId.equals("") == false)
				{
					JDBSQLStatement sql = new JDBSQLStatement();
					sql.setXMLFilename(filename);
					sql.setjdbcDriver(driver);
					sql.setStatementId(statementId);
					sql.setStatementText(statementText);
					sqlList.addLast(sql);
				}
				i++;
			}
			while (statementId.equals("") == false);
		}
		catch (Exception ex)
		{

		}

		return sqlList;
	}
}
