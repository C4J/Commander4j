package com.commander4j.xml;

//import java.io.File;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
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
public class JXMLHost {


	private static int iNumberOfHosts;
	
	public static boolean validateServiceHostPresent(LinkedList<JHost> hostList)
	{
		boolean result = false;
		for (int j = 0; j < hostList.size(); j++)
		{
			if (hostList.get(j).getUniqueID().trim().equals("service"))
			{
				result=true;
			}
		}
		
		return result;
		
	}

	public static void writeHosts(LinkedList<JHost> hostList, String splash, String updatePath, String updateMode, String installDir, String setupPassword)
	{
		final Logger logger = Logger.getLogger(JXMLHost.class);
		final JFileIO fio = new JFileIO();

		Common.updateURL = updatePath;
		Common.updateMODE = updateMode;
		Common.updateInstallDir = installDir;

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

			Element configPassword = (Element) document.createElement("SetupPassword");
			text = document.createTextNode(JEncryption.encrypt(setupPassword));
			configPassword.appendChild(text);
			hosts.appendChild(configPassword);

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
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabaseSelectLimit().trim());
				jdbcDatabaseSelectLimit.appendChild(text);

				Element jdbcDatabaseSchema = (Element) document.createElement("jdbcDatabaseSchema");
				text = document.createTextNode(hostList.get(j).getDatabaseParameters().getjdbcDatabaseSchema().trim());
				jdbcDatabaseSchema.appendChild(text);

				databasedriver.appendChild(jdbcDriver);
				databasedriver.appendChild(jdbcConnectString);
				databasedriver.appendChild(jdbcDatabaseDateTimeToken);
				databasedriver.appendChild(jdbcDatabaseSelectLimit);
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

				DatabaseParameters.appendChild(jdbcUsername);
				DatabaseParameters.appendChild(jdbcPassword);
				DatabaseParameters.appendChild(jdbcPasswordEncryption);
				DatabaseParameters.appendChild(jdbcServer);
				DatabaseParameters.appendChild(jdbcPort);
				DatabaseParameters.appendChild(jdbcSID);
				DatabaseParameters.appendChild(jdbcDatabase);

				site.appendChild(enabled);
				site.appendChild(uniqueid);
				site.appendChild(description);
				site.appendChild(url);
				site.appendChild(databasedriver);
				site.appendChild(DatabaseParameters);

				hosts.appendChild(site);
			}

			document.appendChild(hosts);

			fio.writeToDisk("xml/hosts/", document, -1, "hosts.xml");

		} catch (ParserConfigurationException pce)
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

	/**
	 * Method loadHosts.
	 * 
	 * @param filename
	 *            String
	 * @param parse
	 *            boolean
	 * @return LinkedList<JHost>
	 */
	public static LinkedList<JHost> loadHosts(String filename, boolean parse)
	{

		String sNumberOfSites = "";
		String splash = "Y";
		String updateURL = "";
		String updateMODE = "";
		String updateDIR = "";
		String setupPassword = "";
		String jdbcDriver = "";
		String jdbcConnectString = "";
		String sitejdbcConnectString = "";
		String jdbcDatabaseDateTimeToken = "";
		String jdbcDatabaseSelectLimit = "";
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

		if (splash.equals("N"))
		{
			Common.displaySplashScreen = false;
		} else
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
				jdbcConnectString = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcConnectString").trim();
				jdbcDatabaseDateTimeToken = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDatabaseDateTimeToken").trim();
				jdbcDatabaseSelectLimit = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDatabaseSelectLimit").trim();
				jdbcDatabaseSchema = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseDriver/jdbcDatabaseSchema").trim();

				jdbcUsername = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcUsername").trim();
				jdbcPassword = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcPassword").trim();
				jdbcPasswordEncryption = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcPasswordEncryption").trim();

				if (jdbcPasswordEncryption.equals("AES"))
				{
					JCipher advancedEncryptionStandard = new JCipher(Common.encryptionKey);
					jdbcPassword = advancedEncryptionStandard.decode(jdbcPassword);

				} else
				{
					jdbcPassword = JEncryption.decrypt(jdbcPassword);
				}

				jdbcServer = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcServer").trim();
				jdbcPort = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcPort").trim();
				jdbcSID = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcSID").trim();
				jdbcDatabase = xmltest.findXPath("//Hosts/Site[@Number='" + SiteNumber + "']/DatabaseParameters/jdbcDatabase").trim();

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
				host.getDatabaseParameters().setjdbcDatabaseSchema(jdbcDatabaseSchema);
				host.getDatabaseParameters().setjdbcUsername(jdbcUsername);
				host.getDatabaseParameters().setjdbcPassword(jdbcPassword);
				host.getDatabaseParameters().setjdbcServer(jdbcServer);
				host.getDatabaseParameters().setjdbcPort(jdbcPort);
				host.getDatabaseParameters().setjdbcSID(jdbcSID);
				host.getDatabaseParameters().setjdbcDatabase(jdbcDatabase);

				hostList.addLast(host);

			}
		}
		return hostList;
	}
}
