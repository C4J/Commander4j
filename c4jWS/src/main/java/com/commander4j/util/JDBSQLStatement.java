package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBSQLStatement.java
 * 
 * Package Name : com.commander4j.db
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

import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.commander4j.c4jWS.Common;


public class JDBSQLStatement implements Comparable<String>
{

	private String jdbcDriver;
	private LinkedList<JDBSQLStatement> sqlstatements = new LinkedList<JDBSQLStatement>();
	private HashMap<String, String> sqlindex = new HashMap<String, String>();
	private String statementId;

	private String statementText;
	private LinkedList<JDBSQLStatement> substitutions = new LinkedList<JDBSQLStatement>();

	private String xmlfilename;

	private final Logger logger = Logger.getLogger(JDBSQLStatement.class);
	private boolean statementsLoaded = false;


	public void setInitialised() {
		statementsLoaded = true;
	}

	public boolean IsInitialised() {
		return statementsLoaded;
	}

	public JDBSQLStatement()
	{

	}


	public JDBSQLStatement(String xmlfile, String driver)
	{
		super();
		setXMLFilename(xmlfile);
		setjdbcDriver(driver);
	}


	public String getjdbcDriver() {
		return jdbcDriver;
	}


	public String getSQL(String id) {
		String result = "";
		String before = "";
		String from = "";
		String to = "";

		int i = sqlstatements.size();

		result = sqlindex.get(id);

		if (result == null)
		{
			result = "";
			if (Common.applicationMode.equals("SwingClient"))
			{
				JOptionPane.showMessageDialog(null, "No SQL found for tag id [" + id + "]", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				logger.error("No SQL found for tag id [" + id + "]");
			}

		}
		else
		{
			i = substitutions.size();

			boolean again = true;

			if (i > 0)
			{
				do
				{

					for (int t = 0; t < i; t++)
					{
						from = "[" + substitutions.get(t).getStatementId() + "]";
						to = substitutions.get(t).getStatementText();
						before = result;
						result = result.replace(from, to);
						if (before.equals(result))
						{
							again = false;
						}
					}

				}
				while (again);
			}
		}
		result = result.replace('\t', ' ').trim();
		//logger.debug("["+id+"] " + result);
		return result;
	}


	public String getStatementId() {
		return statementId;
	}


	public String getStatementText() {
		return statementText;
	}


	public String getXMLFilename() {
		return xmlfilename;
	}

	public void loadSQLStatements(String host) {
		// JXMLDocument xmltest = new JXMLDocument(getXMLFilename());
		try
		{
			sqlstatements.clear();
			sqlstatements = JXMLSql.loadSQLStatements(host, getjdbcDriver(), getXMLFilename());
			int i = sqlstatements.size();
			int c = 0;
			sqlindex.clear();
			for (int t = 0; t < i; t++)
			{
				sqlindex.put(sqlstatements.get(t).getStatementId(), sqlstatements.get(t).getStatementText());
				c++;
			}
			logger.debug("JDBSQLStatement loadSQLStatements loaded " + String.valueOf(c) + " statements.");
		}
		catch (Exception ex)
		{
			logger.error("JDBSQLStatement error during loadSQLStatements " + ex.getMessage());
		}
	}


	public LinkedList<JDBSQLStatement> getSQLStatements() {
		return sqlstatements;
	}


	public void setjdbcDriver(String driver) {
		jdbcDriver = driver;
	}


	public void setStatementId(String id) {
		statementId = id;
	}


	public void setStatementText(String text) {
		statementText = text;
	}


	public void setSubstitutions(LinkedList<JDBSQLStatement> subs) {
		substitutions.clear();
		substitutions.addAll(subs);
	}


	public void setXMLFilename(String filename) {
		xmlfilename = filename;
	}


	public String toString() {
		return statementId;
	}


	public int compareTo(String anothersqlstatementid) {
		return statementId.toString().compareTo(anothersqlstatementid);
	}

}
