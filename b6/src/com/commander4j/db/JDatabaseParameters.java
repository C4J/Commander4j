package com.commander4j.db;

import org.apache.commons.lang3.StringUtils;

import com.commander4j.util.JUtility;

/**
 * @author David Garratt
 * 
 *         Project Name : Commander4j
 * 
 *         Filename : JDatabaseParameters.java
 * 
 *         Package Name : com.commander4j.db
 * 
 *         License : GNU General Public License
 * 
 *         This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 *         This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License along with this program. If not, see http://www.commander4j.com/website/license.html.
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
	
	private String jdbcDatabaseTimeZone;
	
	private String jdbcDatabaseTimeZoneEnable;
	
	private String jdbcDatabaseEncrypt;
	
	private String jdbcDatabaseIntegratedSecurity;
	
	private String jdbcDatabaseTrustServerCertificate;
	
	private String jdbcDatabaseCollation;
	
	private String jdbcDatabaseCharacterSet;

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
		jdbcDatabaseTimeZone = "";
		jdbcDatabaseTimeZoneEnable = "";
		setjdbcDatabaseEncrypt("");
		setjdbcDatabaseIntegratedSecurity("");
		setjdbcDatabaseTrustServerCertificate("");
		jdbcDatabaseSchema = "";
		jdbcServer = "";
		jdbcPort = "";
		jdbcSID = "";
		jdbcDatabase = "";
		jdbcDatabaseCollation = "";
		jdbcDatabaseCharacterSet = "";
	}

	public void setjdbcDriver(String value)
	{
		jdbcDriver = value;
	}

	public void setjdbcUsername(String value)
	{
		jdbcUsername = value;
	}

	public void setjdbcPassword(String value)
	{
		jdbcPassword = value;
	}

	public void setjdbcPasswordEncryption(String value)
	{
		jdbcPasswordEncryption = value;
	}

	public void setjdbcDatabaseDateTimeToken(String value)
	{
		jdbcDatabaseDateTimeToken = value;
	}

	public void setjdbcDatabaseSelectLimit(String value)
	{
		jdbcDatabaseSelectLimit = value;
	}
	
	public void setjdbcDatabaseTimeZone(String value)
	{
		jdbcDatabaseTimeZone = value;
	}
	
	public void setjdbcCollation(String value)
	{
		jdbcDatabaseCollation = value;
	}
	
	public void setjdbcCharacterSet(String value)
	{
		jdbcDatabaseCharacterSet = value;
	}
	
	public void setjdbcDatabaseTimeZoneEnable(String value)
	{
		jdbcDatabaseTimeZoneEnable = value;
	}
	
	public void setjdbcDatabaseEncrypt(String value)
	{
		if (value.equals("")) value = "N";
		jdbcDatabaseEncrypt = value;
	}
	
	public void setjdbcDatabaseIntegratedSecurity(String value)
	{
		if (value.equals("")) value = "N";
		jdbcDatabaseIntegratedSecurity = value;
	}

	public void setjdbcDatabaseTrustServerCertificate(String value)
	{
		if (value.equals("")) value = "N";
		jdbcDatabaseTrustServerCertificate = value;
	}


	public void setjdbcDatabaseSchema(String value)
	{
		jdbcDatabaseSchema = com.commander4j.util.JUtility.replaceNullStringwithBlank(value);
	}

	public void setjdbcServer(String value)
	{
		jdbcServer = value;
	}

	public void setjdbcPort(String value)
	{
		jdbcPort = value;
	}

	public void setjdbcSID(String value)
	{
		jdbcSID = value;
	}

	public void setjdbcDatabase(String value)
	{
		jdbcDatabase = value;
	}

	public String getjdbcDriver()
	{
		return jdbcDriver;
	}

	public String getjdbcUsername()
	{
		return jdbcUsername;
	}

	public String getjdbcPassword()
	{
		return jdbcPassword;
	}

	public String getjdbcPasswordEncryption()
	{
		return jdbcPasswordEncryption;
	}

	public String getjdbcConnectString()
	{

		String value = "";
		String timezone = "";

		if (getjdbcDriver().equals("com.mysql.cj.jdbc.Driver"))
		{	
			if	(isjdbcDatabaseTimeZoneEnable())
			{
				timezone = "serverTimezone="+getjdbcDatabaseTimeZone()+"#";
			}
						
			if (getjdbcPort().equals(""))
			{
				if	(isjdbcDatabaseTimeZoneEnable())
				{
					value = "jdbc:mysql://jdbcServer/jdbcDatabase?timezone?connectTimeout=5&socketTimeout=0&autoReconnect=true";
				}
				else
				{
					value = "jdbc:mysql://jdbcServer/jdbcDatabase?connectTimeout=5&socketTimeout=0&autoReconnect=true";
				}

			}
			else
			{
				if	(isjdbcDatabaseTimeZoneEnable())
				{
					value = "jdbc:mysql://jdbcServer:jdbcPort/jdbcDatabase?timezone?connectTimeout=5&socketTimeout=0&autoReconnect=true";
				}
				else
				{
					value = "jdbc:mysql://jdbcServer:jdbcPort/jdbcDatabase?connectTimeout=5&socketTimeout=0&autoReconnect=true";
				}
			}
		}

		if (getjdbcDriver().equals("oracle.jdbc.driver.OracleDriver"))
		{
			value = "jdbc:oracle:thin:@//jdbcServer:jdbcPort/jdbcSID";
		}

		if (getjdbcDriver().equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
		{

			value = "jdbc:sqlserver://jdbcServer\\jdbcSID:jdbcPort;databaseName=jdbcDatabase;selectMethod=direct;"+getjdbcDatabaseEncryptParam()+getjdbcDatabaseTrustServerCertificateParam()+getjdbcDatabaseIntegratedSecurityParam();

		}

		value = value.replaceAll("jdbcServer", getjdbcServer());
		
		if	(isjdbcDatabaseTimeZoneEnable())
		{
			value = value.replaceAll("timezone", timezone);
		}

		if (getjdbcPort().equals(""))
		{
			value = value.replaceAll(":jdbcPort", "");
		}
		else
		{
			value = value.replaceAll("jdbcPort", getjdbcPort());
		}

		if (getjdbcSID().equals(""))
		{
			value = StringUtils.remove(value, "\\jdbcSID");
		}
		else
		{
			value = value.replaceAll("jdbcSID", getjdbcSID());
		}

		value = value.replaceAll("jdbcDatabase", getjdbcDatabase());

		return value;
	}

	public String getjdbcDatabaseDateTimeToken()
	{
		return jdbcDatabaseDateTimeToken;
	}

	public String getjdbcDatabaseSelectLimit()
	{
		return jdbcDatabaseSelectLimit;
	}

	public String getjdbcDatabaseTimeZone()
	{
		return jdbcDatabaseTimeZone;
	}
	
	public String getjdbcCollation()
	{
		if (JUtility.replaceNullStringwithBlank(getjdbcDriver()).equals("com.mysql.cj.jdbc.Driver"))
		{
			if (JUtility.replaceNullStringwithBlank(jdbcDatabaseCollation).equals(""))
			{
				setjdbcCollation("utf8mb4_0900_ai_ci");
			}
		}
		
		return jdbcDatabaseCollation;
	}
	
	public String getjdbcCharacterSet()
	{
		if (JUtility.replaceNullStringwithBlank(getjdbcDriver()).equals("com.mysql.cj.jdbc.Driver"))
		{
			if (JUtility.replaceNullStringwithBlank(jdbcDatabaseCharacterSet).equals(""))
			{
				setjdbcCharacterSet("utf8mb4");
			}
		}
		
		return jdbcDatabaseCharacterSet;
	}
	
	public String getjdbcDatabaseTimeZoneEnable()
	{
		return jdbcDatabaseTimeZoneEnable;
	}
	
	public String getjdbcDatabaseEncryptEnable()
	{
		if (jdbcDatabaseEncrypt.equals("")) jdbcDatabaseEncrypt = "N";
		return jdbcDatabaseEncrypt;
	}
	
	public String getjdbcDatabaseEncryptParam()
	{
		String result = "encrypt=";
		
		if (isjdbcDatabaseEncrypt())
		{
			result = result + "true;";
		}
		else
		{
			result = result + "false;";
		}
		
		return result;
	}
	
	public String getjdbcDatabaseIntegratedSecurityParam()
	{
		String result = "integratedSecurity=";
		
		if (isjdbcDatabaseIntegratedSecurity())
		{
			result = result + "true;";
		}
		else
		{
			result = result + "false;";
		}
		
		return result;
	}
	
	public String getjdbcDatabaseTrustServerCertificateParam()
	{
		String result = "trustServerCertificate=";
		
		if (isjdbcDatabaseTrustServerCertificate())
		{
			result = result + "true;";
		}
		else
		{
			result = result + "false;";
		}
		
		return result;
	}
	
	public String getjdbcDatabaseIntegratedSecurityEnable()
	{
		if (jdbcDatabaseIntegratedSecurity.equals("")) jdbcDatabaseIntegratedSecurity = "N";
		return jdbcDatabaseIntegratedSecurity;
	}
	
	public String getjdbcDatabaseTrustServerCertificateEnable()
	{
		if (jdbcDatabaseTrustServerCertificate.equals("")) jdbcDatabaseTrustServerCertificate = "N"; 
		return jdbcDatabaseTrustServerCertificate;
	}
	
	
	public boolean isjdbcDatabaseTimeZoneEnable()
	{
		if (JUtility.replaceNullStringwithBlank(jdbcDatabaseTimeZoneEnable).equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public void setjdbcDatabaseTimeZoneEnable(boolean value)
	{
		if (value)
		{
			jdbcDatabaseTimeZoneEnable ="Y";
		}
		else
		{
			jdbcDatabaseTimeZoneEnable ="";
		}
	}
	
	
	public boolean isjdbcDatabaseEncrypt()
	{
		if (JUtility.replaceNullStringwithBlank(jdbcDatabaseEncrypt).equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public void setjdbcDatabaseEncrypt(boolean value)
	{
		if (value)
		{
			jdbcDatabaseEncrypt ="Y";
		}
		else
		{
			jdbcDatabaseEncrypt ="";
		}
	}	
	
	public boolean getjdbcDatabaseIntegratedSecurity()
	{
		if (JUtility.replaceNullStringwithBlank(jdbcDatabaseIntegratedSecurity).equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public void setjdbcDatabaseIntegratedSecurity(boolean value)
	{
		if (value)
		{
			jdbcDatabaseIntegratedSecurity ="Y";
		}
		else
		{
			jdbcDatabaseIntegratedSecurity ="";
		}
	}
	
	public boolean isjdbcDatabaseIntegratedSecurity()
	{
		if (JUtility.replaceNullStringwithBlank(jdbcDatabaseIntegratedSecurity).equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isjdbcDatabaseTrustServerCertificate()
	{
		if (JUtility.replaceNullStringwithBlank(jdbcDatabaseTrustServerCertificate).equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public void setjdbcDatabaseTrustServerCertificate(boolean value)
	{
		if (value)
		{
			jdbcDatabaseTrustServerCertificate ="Y";
		}
		else
		{
			jdbcDatabaseTrustServerCertificate ="";
		}
	}
	
	public String getjdbcDatabaseSchema()
	{
		return jdbcDatabaseSchema;
	}

	public String getjdbcServer()
	{
		String result = jdbcServer;
		result = StringUtils.removeIgnoreCase(result, ";selectMethod=direct");
		result = StringUtils.removeIgnoreCase(result, ";autoReconnect=true");
		return result;
	}

	public String getjdbcPort()
	{
		return jdbcPort;
	}

	public String getjdbcSID()
	{
		return jdbcSID;
	}

	public String getjdbcDatabase()
	{
		String result = jdbcDatabase;
		result = StringUtils.removeIgnoreCase(result, ";selectMethod=direct");
		result = StringUtils.removeIgnoreCase(result, ";autoReconnect=true");
		return result;
	}

}
