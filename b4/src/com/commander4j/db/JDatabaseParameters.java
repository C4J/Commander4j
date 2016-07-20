/*
 * Created on 10-Aug-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.commander4j.db;

/**
 * @author David Garratt
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 * @version $Revision: 1.0 $
 */
public class JDatabaseParameters
{
	/**
	 * @uml.property name="jdbcDriver"
	 */
	private String jdbcDriver;
	/**
	 * @uml.property name="jdbcUsername"
	 */
	private String jdbcUsername;
	/**
	 * @uml.property name="jdbcPassword"
	 */
	private String jdbcPassword;

	private String jdbcPasswordEncryption;
	/**
	 * @uml.property name="jdbcDatabaseDateTimeToken"
	 */
	private String jdbcDatabaseDateTimeToken;
	/**
	 * @uml.property name="jdbcDatabaseSelectLimit"
	 */
	private String jdbcDatabaseSelectLimit;
	/**
	 * @uml.property name="jdbcDatabaseSchema"
	 */
	private String jdbcDatabaseSchema;
	/**
	 * @uml.property name="jdbcServer"
	 */
	private String jdbcServer;
	/**
	 * @uml.property name="jdbcPort"
	 */
	private String jdbcPort;
	/**
	 * @uml.property name="jdbcSID"
	 */
	private String jdbcSID;
	/**
	 * @uml.property name="jdbcDatabase"
	 */
	private String jdbcDatabase;

	public JDatabaseParameters()
	{
		jdbcDriver = "";
		jdbcUsername = "";
		jdbcPassword = "";
		jdbcPasswordEncryption = "";
		jdbcDatabaseDateTimeToken = "";
		jdbcDatabaseSelectLimit = "";
		jdbcDatabaseSchema = "";
		jdbcServer = "";
		jdbcPort = "";
		jdbcSID = "";
		jdbcDatabase = "";
	}

	/**
	 * Method setjdbcDriver.
	 * 
	 * @param value
	 *            String
	 */
	public void setjdbcDriver(String value) {
		jdbcDriver = value;
	}

	/**
	 * Method setjdbcUsername.
	 * 
	 * @param value
	 *            String
	 */
	public void setjdbcUsername(String value) {
		jdbcUsername = value;
	}

	/**
	 * Method setjdbcPassword.
	 * 
	 * @param value
	 *            String
	 */
	public void setjdbcPassword(String value) {
		jdbcPassword = value;
	}
	
	public void setjdbcPasswordEncryption(String value)
	{
		jdbcPasswordEncryption = value;
	}

	/**
	 * Method setjdbcDatabaseDateTimeToken.
	 * 
	 * @param value
	 *            String
	 */
	public void setjdbcDatabaseDateTimeToken(String value) {
		jdbcDatabaseDateTimeToken = value;
	}

	/**
	 * Method setjdbcDatabaseSelectLimit.
	 * 
	 * @param value
	 *            String
	 */
	public void setjdbcDatabaseSelectLimit(String value) {
		jdbcDatabaseSelectLimit = value;
	}

	/**
	 * Method setjdbcDatabaseSchema.
	 * 
	 * @param value
	 *            String
	 */
	public void setjdbcDatabaseSchema(String value) {
		jdbcDatabaseSchema = com.commander4j.util.JUtility.replaceNullStringwithBlank(value);
	}

	/**
	 * Method setjdbcServer.
	 * 
	 * @param value
	 *            String
	 */
	public void setjdbcServer(String value) {
		jdbcServer = value;
	}

	/**
	 * Method setjdbcPort.
	 * 
	 * @param value
	 *            String
	 */
	public void setjdbcPort(String value) {
		jdbcPort = value;
	}

	/**
	 * Method setjdbcSID.
	 * 
	 * @param value
	 *            String
	 */
	public void setjdbcSID(String value) {
		jdbcSID = value;
	}

	/**
	 * Method setjdbcDatabase.
	 * 
	 * @param value
	 *            String
	 */
	public void setjdbcDatabase(String value) {
		jdbcDatabase = value;
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
	 * Method getjdbcUsername.
	 * 
	 * @return String
	 */
	public String getjdbcUsername() {
		return jdbcUsername;
	}

	/**
	 * Method getjdbcPassword.
	 * 
	 * @return String
	 */
	public String getjdbcPassword() {
		return jdbcPassword;
	}
	
	public String getjdbcPasswordEncryption()
	{
		return jdbcPasswordEncryption;
	}

	/**
	 * Method getjdbcConnectString.
	 * 
	 * @return String
	 */
	public String getjdbcConnectString() {

		String value = "";

		if (getjdbcDriver().equals("com.ibm.db2.jcc.DB2Driver"))
		{
			value = "jdbc:db2://jdbcServer:jdbcPort/jdbcDatabase";
		}

		if (getjdbcDriver().equals("com.mysql.jdbc.Driver"))
		{
			value = "jdbc:mysql://jdbcServer/jdbcDatabase";
		}

		if (getjdbcDriver().equals("oracle.jdbc.driver.OracleDriver"))
		{
			value = "jdbc:oracle:thin:@jdbcServer:jdbcPort:jdbcSID";
		}

		if (getjdbcDriver().equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
		{
			if (getjdbcSID().equals("") == true)
			{
				value = "jdbc:sqlserver://jdbcServer";
			}
			else
			{
				value = "jdbc:sqlserver://jdbcServer\\jdbcSID";
			}
			if (getjdbcPort().equals("")==false)
			{
				if (getjdbcPort().equals("1433")==false)
				{
					value = value + ":"+"jdbcPort";
				}
			}
		}

		value = value.replaceAll("jdbcServer", getjdbcServer());
		value = value.replaceAll("jdbcPort", getjdbcPort());
		value = value.replaceAll("jdbcSID", getjdbcSID());
		value = value.replaceAll("jdbcDatabase", getjdbcDatabase());

		return value;
	}

	/**
	 * Method getjdbcDatabaseDateTimeToken.
	 * 
	 * @return String
	 */
	public String getjdbcDatabaseDateTimeToken() {
		return jdbcDatabaseDateTimeToken;
	}

	/**
	 * Method getjdbcDatabaseSelectLimit.
	 * 
	 * @return String
	 */
	public String getjdbcDatabaseSelectLimit() {
		return jdbcDatabaseSelectLimit;
	}

	/**
	 * Method getjdbcDatabaseSchema.
	 * 
	 * @return String
	 */
	public String getjdbcDatabaseSchema() {
		return jdbcDatabaseSchema;
	}

	/**
	 * Method getjdbcServer.
	 * 
	 * @return String
	 */
	public String getjdbcServer() {
		return jdbcServer;
	}

	/**
	 * Method getjdbcPort.
	 * 
	 * @return String
	 */
	public String getjdbcPort() {
		return jdbcPort;
	}

	/**
	 * Method getjdbcSID.
	 * 
	 * @return String
	 */
	public String getjdbcSID() {
		return jdbcSID;
	}

	/**
	 * Method getjdbcDatabase.
	 * 
	 * @return String
	 */
	public String getjdbcDatabase() {
		return jdbcDatabase;
	}

}
