package com.commander4j.xml;

import java.util.LinkedList;

import com.commander4j.db.JDBSQLStatement;
import com.commander4j.sys.Common;

/**
 */
public class JXMLSql
{
	/**
	 * Method loadSQLStatements.
	 * 
	 * @param driver
	 *            String
	 * @param filename
	 *            String
	 * @return LinkedList<JDBSQLStatement>
	 */

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
