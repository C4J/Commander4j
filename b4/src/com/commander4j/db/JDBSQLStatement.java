package com.commander4j.db;

import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.xml.JXMLSql;

/**
 * @author David
 */
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

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	public void setInitialised() {
		statementsLoaded = true;
	}

	public boolean IsInitialised() {
		return statementsLoaded;
	}

	public JDBSQLStatement()
	{

	}

	/**
	 * Constructor for JDBSQLStatement.
	 * 
	 * @param xmlfile
	 *            String
	 * @param driver
	 *            String
	 */
	public JDBSQLStatement(String xmlfile, String driver)
	{
		super();
		setXMLFilename(xmlfile);
		setjdbcDriver(driver);
	}

	/**
	 * Method getjdbcDriver.
	 * 
	 * @return String
	 */
	public String getjdbcDriver() {
		return jdbcDriver;
	}

	/**
	 * Method getSQL.
	 * 
	 * @param id
	 *            String
	 * @return String
	 */
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

	/**
	 * Method getStatementId.
	 * 
	 * @return String
	 * @uml.property name="statementId"
	 */
	public String getStatementId() {
		return statementId;
	}

	/**
	 * Method getStatementText.
	 * 
	 * @return String
	 * @uml.property name="statementText"
	 */
	public String getStatementText() {
		return statementText;
	}

	/**
	 * Method getXMLFilename.
	 * 
	 * @return String
	 */
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

	/**
	 * Method getSQLStatements.
	 * 
	 * @return LinkedList<JDBSQLStatement>
	 */
	public LinkedList<JDBSQLStatement> getSQLStatements() {
		return sqlstatements;
	}

	/**
	 * Method setjdbcDriver.
	 * 
	 * @param driver
	 *            String
	 */
	public void setjdbcDriver(String driver) {
		jdbcDriver = driver;
	}

	/**
	 * Method setStatementId.
	 * 
	 * @param id
	 *            String
	 * @uml.property name="statementId"
	 */
	public void setStatementId(String id) {
		statementId = id;
	}

	/**
	 * Method setStatementText.
	 * 
	 * @param text
	 *            String
	 * @uml.property name="statementText"
	 */
	public void setStatementText(String text) {
		statementText = text;
	}

	/**
	 * Method setSubstitutions.
	 * 
	 * @param subs
	 *            LinkedList<JDBSQLStatement>
	 * @uml.property name="substitutions"
	 */
	public void setSubstitutions(LinkedList<JDBSQLStatement> subs) {
		substitutions.clear();
		substitutions.addAll(subs);
	}

	/**
	 * Method setXMLFilename.
	 * 
	 * @param filename
	 *            String
	 */
	public void setXMLFilename(String filename) {
		xmlfilename = filename;
	}

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	public String toString() {
		return statementId;
	}

	/**
	 * Method compareTo.
	 * 
	 * @param anothersqlstatementid
	 *            String
	 * @return int
	 */
	public int compareTo(String anothersqlstatementid) {
		return statementId.toString().compareTo(anothersqlstatementid);
	}

}
