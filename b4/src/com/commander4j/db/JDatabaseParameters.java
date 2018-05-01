package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDatabaseParameters.java
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

public class JDatabaseParameters
{

	private String jdbcDriver;

	private String jdbcUsername;

	private String jdbcPassword;

	private String jdbcPasswordEncryption;

	private String jdbcDatabaseDateTimeToken;

	private String jdbcDatabaseSelectLimit;

	private String jdbcDatabaseSchema;

	private String jdbcServer;

	private String jdbcPort;

	private String jdbcSID;

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


	public void setjdbcDriver(String value) {
		jdbcDriver = value;
	}


	public void setjdbcUsername(String value) {
		jdbcUsername = value;
	}


	public void setjdbcPassword(String value) {
		jdbcPassword = value;
	}
	
	public void setjdbcPasswordEncryption(String value)
	{
		jdbcPasswordEncryption = value;
	}


	public void setjdbcDatabaseDateTimeToken(String value) {
		jdbcDatabaseDateTimeToken = value;
	}


	public void setjdbcDatabaseSelectLimit(String value) {
		jdbcDatabaseSelectLimit = value;
	}


	public void setjdbcDatabaseSchema(String value) {
		jdbcDatabaseSchema = com.commander4j.util.JUtility.replaceNullStringwithBlank(value);
	}


	public void setjdbcServer(String value) {
		jdbcServer = value;
	}


	public void setjdbcPort(String value) {
		jdbcPort = value;
	}


	public void setjdbcSID(String value) {
		jdbcSID = value;
	}


	public void setjdbcDatabase(String value) {
		jdbcDatabase = value;
	}


	public String getjdbcDriver() {
		return jdbcDriver;
	}


	public String getjdbcUsername() {
		return jdbcUsername;
	}


	public String getjdbcPassword() {
		return jdbcPassword;
	}
	
	public String getjdbcPasswordEncryption()
	{
		return jdbcPasswordEncryption;
	}


	public String getjdbcConnectString() {

		String value = "";

		if (getjdbcDriver().equals("com.ibm.db2.jcc.DB2Driver"))
		{
			value = "jdbc:db2://jdbcServer:jdbcPort/jdbcDatabase";
		}

		if (getjdbcDriver().equals("com.mysql.cj.jdbc.Driver"))
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


	public String getjdbcDatabaseDateTimeToken() {
		return jdbcDatabaseDateTimeToken;
	}


	public String getjdbcDatabaseSelectLimit() {
		return jdbcDatabaseSelectLimit;
	}


	public String getjdbcDatabaseSchema() {
		return jdbcDatabaseSchema;
	}


	public String getjdbcServer() {
		return jdbcServer;
	}


	public String getjdbcPort() {
		return jdbcPort;
	}


	public String getjdbcSID() {
		return jdbcSID;
	}


	public String getjdbcDatabase() {
		return jdbcDatabase;
	}

}
