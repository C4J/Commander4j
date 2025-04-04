package com.commander4j.xml;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.sys.Common;
import com.commander4j.sys.JHost;
import com.commander4j.util.JCipher;
import com.commander4j.util.JEncryption;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;

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

	public static void writeHosts(String filename, LinkedList<JHost> hostList, String splash, String updatePath, String updateMode, String installDir, String setupPassword, String hostVersion, String hostUpdatePath, Boolean singleInstance,
			Integer singleInstancePort)
	{
		final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JXMLHost.class);
		final JFileIO fio = new JFileIO();

		Common.updateURL = updatePath;
		Common.updateMODE = updateMode;
		Common.updateInstallDir = installDir;
		Common.singleInstanceMode = singleInstance;
		Common.singleInstancePort = singleInstancePort;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.newDocument();

			// Create the root element - hosts //
			Element hosts = (Element) document.createElement("Hosts");

			// Number of Hosts //
			Element numberofhosts = (Element) document.createElement("NumberOfSites");
			Text text = document.createTextNode(Integer.toString(hostList.size()));
			numberofhosts.appendChild(text);
			hosts.appendChild(numberofhosts);

			// Splash Screen Enabled //
			Element splashScreen = (Element) document.createElement("SplashScreen");
			text = document.createTextNode(splash);
			splashScreen.appendChild(text);
			hosts.appendChild(splashScreen);

			Element updateURL = (Element) document.createElement("UpdateURL");
			text = document.createTextNode(updatePath);
			updateURL.appendChild(text);
			hosts.appendChild(updateURL);

			Element updateMODE = (Element) document.createElement("UpdateMODE");
			text = document.createTextNode(updateMode);
			updateMODE.appendChild(text);
			hosts.appendChild(updateMODE);

			Element updateDIR = (Element) document.createElement("UpdateDIR");
			text = document.createTextNode(installDir);
			updateDIR.appendChild(text);
			hosts.appendChild(updateDIR);

			Element hostVer = (Element) document.createElement("hostVersion");
			text = document.createTextNode(hostVersion);
			hostVer.appendChild(text);
			hosts.appendChild(hostVer);

			Element hostUpdate = (Element) document.createElement("hostUpdatePath");
			text = document.createTextNode(hostUpdatePath);
			hostUpdate.appendChild(text);
			hosts.appendChild(hostUpdate);

			Element configPassword = (Element) document.createElement("SetupPassword");
			text = document.createTextNode(JEncryption.encrypt(setupPassword));
			configPassword.appendChild(text);
			hosts.appendChild(configPassword);

			Element singleInstanceElement = (Element) document.createElement("singleInstance");
			if (singleInstance)
			{
				text = document.createTextNode("Y");
			}
			else
			{
				text = document.createTextNode("N");
			}
			singleInstanceElement.appendChild(text);
			hosts.appendChild(singleInstanceElement);

			Element singleInstancePortElement = (Element) document.createElement("singleInstancePort");
			text = document.createTextNode(String.valueOf(singleInstancePort));
			singleInstancePortElement.appendChild(text);
			hosts.appendChild(singleInstancePortElement);

			for (int j = 0; j < hostList.size(); j++)
			{
				// Create the 1st level - site //
				Element site = (Element) document.createElement("Site");
				site.setAttribute("Number", hostList.get(j).getSiteNumber());

				Element enabled = (Element) document.createElement("Enabled");
				text = document.createTextNode(hostList.get(j).getEnabled().trim());
				enabled.appendChild(text);

				Element uniqueid = (Element) document.createElement("UniqueID");
				text = document.createTextNode(hostList.get(j).getUniqueID().trim());
				uniqueid.appendChild(text);

				Element description = (Element) document.createElement("Description");
				text = document.createTextNode(hostList.get(j).getSiteDescription().trim());
				description.appendChild(text);

				Element url = (Element) document.createElement("URL");
				text = document.createTextNode(hostList.get(j).getSiteURL().trim());
				url.appendChild(text);

				Element databasedriver = (Element) document.createElement("DatabaseDriver");

				Element jdbcDriver = (Element) document.createElement("jdbcDriver");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDriver().trim());
				jdbcDriver.appendChild(text);

				Element jdbcConnectString = (Element) document.createElement("jdbcConnectString");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcConnectString().trim());
				jdbcConnectString.appendChild(text);

				Element jdbcDatabaseDateTimeToken = (Element) document.createElement("jdbcDatabaseDateTimeToken");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabaseDateTimeToken().trim());
				jdbcDatabaseDateTimeToken.appendChild(text);

				Element jdbcDatabaseSelectLimit = (Element) document.createElement("jdbcDatabaseSelectLimit");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabaseSelectLimit().toLowerCase().trim());
				jdbcDatabaseSelectLimit.appendChild(text);

				if ((hostList.get(j).getDatabaseParameters().getjdbcDatabaseTimeZone().trim().equals("") == false) && (hostList.get(j).getDatabaseParameters().isjdbcDatabaseTimeZoneEnable() == false))
				{
					hostList.get(j).getDatabaseParameters().setjdbcDatabaseTimeZone("");
				}

				Element jdbcDatabaseTimeZone = (Element) document.createElement("jdbcDatabaseTimeZone");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabaseTimeZone().trim());
				jdbcDatabaseTimeZone.appendChild(text);

				Element jdbcDatabaseTimeZoneEnable = (Element) document.createElement("jdbcDatabaseTimeZoneEnable");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabaseTimeZoneEnable().trim());
				jdbcDatabaseTimeZoneEnable.appendChild(text);

				Element jdbcDatabaseCollation = (Element) document.createElement("jdbcDatabaseCollation");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcCollation().trim());
				jdbcDatabaseCollation.appendChild(text);

				Element jdbcDatabaseCharacterSet = (Element) document.createElement("jdbcDatabaseCharacterSet");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcCharacterSet().trim());
				jdbcDatabaseCharacterSet.appendChild(text);

				Element jdbcDatabaseSchema = (Element) document.createElement("jdbcDatabaseSchema");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabaseSchema().trim());
				jdbcDatabaseSchema.appendChild(text);

				databasedriver.appendChild(jdbcDriver);
				databasedriver.appendChild(jdbcConnectString);
				databasedriver.appendChild(jdbcDatabaseDateTimeToken);
				databasedriver.appendChild(jdbcDatabaseSelectLimit);
				databasedriver.appendChild(jdbcDatabaseTimeZone);
				databasedriver.appendChild(jdbcDatabaseTimeZoneEnable);
				databasedriver.appendChild(jdbcDatabaseCollation);
				databasedriver.appendChild(jdbcDatabaseCharacterSet);
				databasedriver.appendChild(jdbcDatabaseSchema);

				Element DatabaseParameters = (Element) document.createElement("DatabaseParameters");

				Element jdbcUsername = (Element) document.createElement("jdbcUsername");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcUsername().trim());
				jdbcUsername.appendChild(text);

				String cipherText = hostList.get(j).getDatabaseParameters().getjdbcPassword().trim();

				JCipher advancedEncryptionStandard = new JCipher(Common.encryptionKey);
				cipherText = advancedEncryptionStandard.encode(cipherText);

				Element jdbcPassword = (Element) document.createElement("jdbcPassword");
				text = document.createTextNode(cipherText);
				jdbcPassword.appendChild(text);

				Element jdbcPasswordEncryption = (Element) document.createElement("jdbcPasswordEncryption");
				text = document.createTextNode("AES");
				jdbcPasswordEncryption.appendChild(text);

				Element jdbcServer = (Element) document.createElement("jdbcServer");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcServer().trim());
				jdbcServer.appendChild(text);

				Element jdbcPort = (Element) document.createElement("jdbcPort");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcPort().trim());
				jdbcPort.appendChild(text);

				Element jdbcSID = (Element) document.createElement("jdbcSID");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcSID().trim());
				jdbcSID.appendChild(text);

				Element jdbcDatabase = (Element) document.createElement("jdbcDatabase");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabase().trim());
				jdbcDatabase.appendChild(text);

				Element jdbcDatabaseEncrypt = (Element) document.createElement("jdbcDatabaseEncrypt");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabaseEncryptEnable().trim());
				jdbcDatabaseEncrypt.appendChild(text);

				Element jdbcDatabaseIntegratedSecurity = (Element) document.createElement("jdbcDatabaseIntegratedSecurity");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabaseIntegratedSecurityEnable().trim());
				jdbcDatabaseIntegratedSecurity.appendChild(text);

				Element jdbcDatabaseTrustServerCertificate = (Element) document.createElement("jdbcDatabaseTrustServerCertificate");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabaseTrustServerCertificateEnable().trim());
				jdbcDatabaseTrustServerCertificate.appendChild(text);

				DatabaseParameters.appendChild(jdbcUsername);
				DatabaseParameters.appendChild(jdbcPassword);
				DatabaseParameters.appendChild(jdbcPasswordEncryption);
				DatabaseParameters.appendChild(jdbcServer);
				DatabaseParameters.appendChild(jdbcPort);
				DatabaseParameters.appendChild(jdbcSID);
				DatabaseParameters.appendChild(jdbcDatabase);
				DatabaseParameters.appendChild(jdbcDatabaseEncrypt);
				DatabaseParameters.appendChild(jdbcDatabaseIntegratedSecurity);
				DatabaseParameters.appendChild(jdbcDatabaseTrustServerCertificate);

				site.appendChild(enabled);
				site.appendChild(uniqueid);
				site.appendChild(description);
				site.appendChild(url);
				site.appendChild(databasedriver);
				site.appendChild(DatabaseParameters);

				hosts.appendChild(site);
			}

			document.appendChild(hosts);

			fio.writeToDisk(filename, document);
			
		}
		catch (ParserConfigurationException pce)
		{
			// Parser with specified options can't be built
			pce.printStackTrace();
		}

		catch (Exception ex)
		{
			logger.error("writeHosts : cannot write to file");
		}

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
		String jdbcDatabaseCollation = "";
		String jdbcDatabaseCharacterSet = "";
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

				hostList.addLast(host);

			}
		}
		return hostList;
	}
}
