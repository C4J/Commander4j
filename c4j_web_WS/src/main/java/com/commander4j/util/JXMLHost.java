package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JXMLHost.java
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

import com.commander4j.c4jWS.Common;

;

/**
 */
public class JXMLHost
{

	private static int iNumberOfHosts;

	public static boolean validateServiceHostPresent(LinkedList<JHost> hostList)
	{
		boolean result = false;
		for (int j = 0; j < hostList.size(); j++)
		{
			if (hostList.get(j).getUniqueID().trim().equals("service"))
			{
				result = true;
			}
		}

		return result;

	}



	public static int getNumberOfHostsLoaded()
	{
		return iNumberOfHosts;
	}

	public static Double checkHostVersion(String filename)
	{
		Double result = (double) 0;

		JXMLDocument xmltest = new JXMLDocument(filename);

		String hostVersion = xmltest.findXPath("//Hosts/hostVersion");

		if (hostVersion.equals(""))
		{
			hostVersion = "1";
		}

		result = Double.valueOf(hostVersion);

		return result;
	}

	public static LinkedList<JHost> loadHosts(String filename, boolean parse)
	{

		String sNumberOfSites = "";
		String splash = "Y";
		String updateURL = "";
		String updateMODE = "";
		String hostVersion = "";
		String hostUpdatePath = "";
		String updateDIR = "";
		String setupPassword = "";
		String jdbcDriver = "";
		String jdbcConnectString = "";
		String sitejdbcConnectString = "";
		String jdbcDatabaseDateTimeToken = "";
		String jdbcDatabaseSelectLimit = "";
		String jdbcDatabaseTimeZone = "";
		String jdbcDatabaseCollation= "";
		String jdbcDatabaseCharacterSet= "";
		String jdbcDatabaseTimeZoneEnable = "";
		String jdbcDatabaseSchema = "";
		String SiteNumber = "";
		String SiteDescription = "";
		String SiteURL = "";
		String jdbcUsername = "";
		String jdbcPassword = "";
		String jdbcPasswordEncryption = "";
		String jdbcServer = "";
		String jdbcPort = "";
		String jdbcSID = "";
		String jdbcDatabase = "";
		String SiteEnabled = "";
		String uniqueid = "";
		String singleInstance = "";
		String singleInstancePort = "";
		String jdbcTrustServerCertificate = "";
		String jdbcIntegratedSecurity = "";
		String jdbcLoginTimeoutEnabled = "";
		String jdbcSocketTimeoutEnabled = "";
		String jdbcLoginTimeout = "";
		String jdbcSocketTimeout = "";
		String jdbcEncrypt = "";

		LinkedList<JHost> hostList = new LinkedList<JHost>();
		hostList.clear();

		if (filename.isEmpty())
		{
			filename = "xml/hosts/hosts.xml";
		}

		JXMLDocument xmltest = new JXMLDocument(filename);

		sNumberOfSites = xmltest.findXPath("//Hosts/NumberOfSites");
		iNumberOfHosts = Integer.valueOf(sNumberOfSites).intValue();
		splash = xmltest.findXPath("//Hosts/SplashScreen");
		updateURL = xmltest.findXPath("//Hosts/UpdateURL");

		singleInstance = JUtility.replaceNullStringwithBlank(xmltest.findXPath("//Hosts/singleInstance"));

		if (singleInstance.equals(""))
		{
			singleInstance = "N";
		}

		singleInstancePort = xmltest.findXPath("//Hosts/singleInstancePort");
		singleInstancePort = JUtility.replaceNullStringwithBlank(singleInstancePort);

		if (singleInstancePort.equals(""))
		{
			singleInstancePort = String.valueOf(Common.singleInstancePort);
		}

		hostVersion = xmltest.findXPath("//Hosts/hostVersion");
		if (hostVersion.equals(""))
		{
			hostVersion = "1";
		}
		hostUpdatePath = xmltest.findXPath("//Hosts/hostUpdatePath");

		updateMODE = xmltest.findXPath("//Hosts/UpdateMODE");
		updateDIR = xmltest.findXPath("//Hosts/UpdateDIR");
		setupPassword = JEncryption.decrypt(xmltest.findXPath("//Hosts/SetupPassword"));

		if (updateDIR.equals(""))
		{
			updateDIR = Common.base_dir;
		}

		Common.updateURL = updateURL;
		Common.updateMODE = updateMODE;
		Common.updateInstallDir = updateDIR;
		Common.setupPassword = setupPassword;
		Common.hostVersion = hostVersion;
		Common.hostUpdatePath = hostUpdatePath;

		if (singleInstance.equals("N"))
		{
			Common.singleInstanceMode = false;
		}
		else
		{
			Common.singleInstanceMode = true;
		}

		Common.singleInstancePort = Integer.valueOf(singleInstancePort);

		if (splash.equals("N"))
		{
			Common.displaySplashScreen = false;
		}
		else
		{
			Common.displaySplashScreen = true;
		}

		if (iNumberOfHosts > 0)
		{
			for (int i = 1; i <= iNumberOfHosts; i++)
			{
				SiteNumber = Integer.toString(i);

				SiteDescription = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/Description").trim();
				SiteURL = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/URL").trim();
				SiteEnabled = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/Enabled").trim();
				uniqueid = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/UniqueID").trim();
				uniqueid = JUtility.replaceNullStringwithBlank(uniqueid);
				if (uniqueid.length() == 0)
				{
					uniqueid = JUnique.getUniqueID();
				}

				jdbcDriver = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDriver").trim();
				if (jdbcDriver.equals("com.mysql.jdbc.Driver"))
				{
					jdbcDriver = "com.mysql.cj.jdbc.Driver";
				}
				jdbcConnectString = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcConnectString").trim();
				jdbcDatabaseDateTimeToken = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDatabaseDateTimeToken").trim();
				jdbcDatabaseSelectLimit = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDatabaseSelectLimit").trim();
				jdbcDatabaseTimeZone = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDatabaseTimeZone").trim();
				jdbcDatabaseTimeZoneEnable = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDatabaseTimeZoneEnable").trim();
				jdbcDatabaseCollation = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDatabaseCollation").trim();
				jdbcDatabaseCharacterSet = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDatabaseCharacterSet").trim();

				jdbcDatabaseSchema = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDatabaseSchema").trim();

				jdbcUsername = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcUsername").trim();
				jdbcPassword = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcPassword").trim();
				jdbcPasswordEncryption = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcPasswordEncryption").trim();

				if (jdbcPasswordEncryption.equals("AES"))
				{
					JCipher advancedEncryptionStandard = new JCipher(Common.encryptionKey);
					jdbcPassword = advancedEncryptionStandard.decode(jdbcPassword);

				}
				else
				{
					jdbcPassword = JEncryption.decrypt(jdbcPassword);
				}

				jdbcServer = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcServer").trim();
				jdbcPort = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcPort").trim();
				jdbcSID = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcSID").trim();
				jdbcDatabase = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcDatabase").trim();

				jdbcTrustServerCertificate = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcDatabaseTrustServerCertificate").trim();
				jdbcIntegratedSecurity = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcDatabaseIntegratedSecurity").trim();
				jdbcEncrypt = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcDatabaseEncrypt").trim();
				jdbcLoginTimeoutEnabled = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcDatabaseLoginTimeoutEnabled").trim();
				jdbcSocketTimeoutEnabled = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcDatabaseSocketTimeoutEnabled").trim();
				jdbcLoginTimeout = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcDatabaseLoginTimeout").trim();
				jdbcSocketTimeout = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcDatabaseSocketTimeout").trim();

				sitejdbcConnectString = jdbcConnectString;
				if (parse)
				{
					sitejdbcConnectString = sitejdbcConnectString.replaceAll("jdbcServer", jdbcServer);
					sitejdbcConnectString = sitejdbcConnectString.replaceAll("jdbcPort", jdbcPort);
					sitejdbcConnectString = sitejdbcConnectString.replaceAll("jdbcSID", jdbcSID);
					sitejdbcConnectString = sitejdbcConnectString.replaceAll("jdbcDatabase", jdbcDatabase);
				}

				JHost host = new JHost();
				host.setSiteNumber(SiteNumber);
				host.setSiteDescription(SiteDescription);
				host.setSiteURL(SiteURL);
				host.setEnabled(SiteEnabled);
				host.setUniqueID(uniqueid);
				host.getDatabaseParameters().setjdbcDriver(jdbcDriver);
				host.getDatabaseParameters().setjdbcDatabaseDateTimeToken(jdbcDatabaseDateTimeToken);
				host.getDatabaseParameters().setjdbcDatabaseSelectLimit(jdbcDatabaseSelectLimit);
				host.getDatabaseParameters().setjdbcDatabaseTimeZone(jdbcDatabaseTimeZone);
				host.getDatabaseParameters().setjdbcCollation(jdbcDatabaseCollation);
				host.getDatabaseParameters().setjdbcCharacterSet(jdbcDatabaseCharacterSet);
				host.getDatabaseParameters().setjdbcDatabaseTimeZoneEnable(jdbcDatabaseTimeZoneEnable);
				host.getDatabaseParameters().setjdbcDatabaseSchema(jdbcDatabaseSchema);
				host.getDatabaseParameters().setjdbcUsername(jdbcUsername);
				host.getDatabaseParameters().setjdbcPassword(jdbcPassword);
				host.getDatabaseParameters().setjdbcServer(jdbcServer);
				host.getDatabaseParameters().setjdbcPort(jdbcPort);
				host.getDatabaseParameters().setjdbcSID(jdbcSID);
				host.getDatabaseParameters().setjdbcDatabase(jdbcDatabase);

				host.getDatabaseParameters().setjdbcDatabaseEncrypt(jdbcEncrypt);
				host.getDatabaseParameters().setjdbcDatabaseTrustServerCertificate(jdbcTrustServerCertificate);
				host.getDatabaseParameters().setjdbcDatabaseIntegratedSecurity(jdbcIntegratedSecurity);
				host.getDatabaseParameters().setjdbcDatabaseLoginTimeoutEnabled(jdbcLoginTimeoutEnabled);
				host.getDatabaseParameters().setjdbcDatabaseSocketTimeoutEnabled(jdbcSocketTimeoutEnabled);
				
				host.getDatabaseParameters().setjdbcDatabaseLoginTimeout(jdbcLoginTimeout);
				host.getDatabaseParameters().setjdbcDatabaseSocketTimeout(jdbcSocketTimeout);

				hostList.addLast(host);

			}
		}
		return hostList;
	}
}
