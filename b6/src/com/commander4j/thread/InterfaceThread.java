package com.commander4j.thread;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : InterfaceThread.java
 * 
 * Package Name : com.commander4j.thread
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

import java.io.File;
import java.util.LinkedList;

import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.logging.log4j.Logger;

import com.commander4j.app.JVersion;
import com.commander4j.db.JDBArchive;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBSchema;
import com.commander4j.db.JDBUser;
import com.commander4j.email.JeMail;
import com.commander4j.messages.GenericMessageHeader;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JArchive;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;

public class InterfaceThread extends Thread
{

	private String currentDateTime = "";
	private String currentTime = "";
	private String currentDate = "";
	private String lastRunDate = "";
	private String hostID = "";
	private Boolean shutdown = false;
	private String sessionID = "";
	private OutboundMessageThread outboundThread;
	private AutoLabellerThread autoLabellerThread;
	private ReportingThread reportingThread;
	private InboundMessageCollectionThread fileCollectThread;
	private InboundMessageThread inboundThread;
	private boolean threadsRunning = false;
	private boolean houseKeeping = false;
	private Boolean enableEnterfaceStatusEmails = false;
	private String siteName = "";
	private String interfaceEmailAddresses = "";
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(InterfaceThread.class);
	private boolean abortThread = false;
	private int backupMessageRetention = 30;


	public String getSessionID()
	{
		return sessionID;
	}

	public void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}	
	
	public InterfaceThread(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		abortThread = false;
	}

	public InterfaceThread(String[] args)
	{
		JUtility.initLogging("");

		Common.base_dir = System.getProperty("user.dir");
		String uniqueID = "";

		uniqueID = "service";

		logger.debug("Checking if parameter [" + uniqueID + "] is a Unique Host ID");
		Common.hostList.loadHosts();
		hostID = Common.hostList.getHostIDforUniqueId(uniqueID);
		if (hostID.length() > 0)
		{
			logger.debug("Host ID identified for service is  [" + hostID + "]");
		}
		else
		{
			logger.debug("No Host has been identified to be target for service - check hosts file unique id.");

			logger.debug("Host with unique id  [" + uniqueID + "] not found - Interface thread aborting.");
			abortThread = true;
			requestStop();

		}
	}

	public Boolean isThreadAbortingStartup()
	{
		return abortThread;
	}

	public void requestStop()
	{
		shutdown = true;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private String getHostID()
	{
		return hostID;
	}

	public void run()
	{
		startupInterface();
	}

	public void shutdownInterface()
	{

	}

	public void startupInterface()
	{
		setSessionID(JUnique.getUniqueID());
		Common.sessionID = getSessionID();

		Common.sd.setData(getSessionID(), "silentExceptions", "Yes", true);
		Common.applicationMode = "SwingClient";

		JUtility.initLogging("");
		JPrint.init();

		if (JUtility.isValidJavaVersion(Common.requiredJavaVersion) == true)
		{

			Common.hostList.loadHosts();

			Common.selectedHostID = getHostID();
			houseKeeping = false;

			while (shutdown == false)
			{

				logger.debug("Connecting to database.");
				while ((Common.hostList.getHost(getHostID()).isConnected(getSessionID()) == false) && (shutdown == false))
				{
					Common.hostList.getHost(getHostID()).connect(getSessionID(), getHostID());
				}

				if ((Common.hostList.getHost(getHostID()).isConnected(getSessionID()) == true) && (shutdown == false))
				{
					JDBSchema schema = new JDBSchema(getSessionID(), Common.hostList.getHost(getHostID()));

					schema.validate(false);

					JUtility.initEANBarcode();
					JLaunchReport.init();
					Common.init();

					JDBUser user = new JDBUser(getHostID(), getSessionID());
					JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
					JeMail mail = new JeMail(getHostID(), getSessionID());

					user.setUserId("interface");
					user.setPassword("interface");
					user.setLoginPassword("interface");
					Common.userList.addUser(getSessionID(), user);

					enableEnterfaceStatusEmails = Boolean.parseBoolean(ctrl.getKeyValueWithDefault("INTERFACE EMAIL NOTIFY", "false", "Email startup and shutdown events :- true or false"));

					interfaceEmailAddresses = ctrl.getKeyValueWithDefault("INTERFACE ADMIN EMAIL", "someone@somewhere.com", "Email address for startup and shutdown events.");

					StringConverter stringConverter = new StringConverter();
					ArrayConverter arrayConverter = new ArrayConverter(String[].class, stringConverter);
					arrayConverter.setDelimiter(';');
					arrayConverter.setAllowedChars(new char[]
					{ '@', '_' });
					String[] emailList = (String[]) arrayConverter.convert(String[].class, interfaceEmailAddresses);
					siteName = Common.hostList.getHost(getHostID()).getSiteDescription();

					if (user.login())
					{

						if (enableEnterfaceStatusEmails == true)
						{
							try
							{
								String subject = "";
								if (houseKeeping == true)
								{

								}
								else
								{
									subject = "Commander4j " + JVersion.getProgramVersion() + " Interface startup for [" + siteName + "] on " + JUtility.getClientName();
									mail.postMail(emailList, subject, "Interface service has started.", "", "");
								}
							}
							catch (Exception ex)
							{
								logger.error("InterfaceThread Unable to send emails");
							}
						}

						houseKeeping = false;

						logger.debug("Interface Logged on successfully");

						logger.debug("Starting Threads....");

						startupThreads();

						while ((shutdown == false) & (houseKeeping == false))
						{
							com.commander4j.util.JWait.milliSec(1000);
							currentDateTime = JUtility.getDateTimeString("yyyy-MM-dd HH:mm:ss");
							currentDate = currentDateTime.substring(0, 10);
							currentTime = currentDateTime.substring(11, 19);
							
							if (currentTime.substring(0, 5).equals(Common.statusReportTime.substring(0, 5)))
							{
								if (currentDate.equals(lastRunDate) == false)
								{
									lastRunDate = currentDate;
									houseKeeping = true;
								}
							}
							
						}

						logger.debug("Stopping Threads....");
						shutdownThreads();

						user.logout();
						logger.debug("Interface Logged out successfully");

						if (enableEnterfaceStatusEmails == true)
						{
							try
							{
								String subject = "";
								if (houseKeeping == true)
								{

								}
								else
								{
									subject = "Commander4j " + JVersion.getProgramVersion() + " Interface shutdown for [" + siteName + "] on " + JUtility.getClientName();
									mail.postMail(emailList, subject, "Interface service has stopped.", "", "");
								}
							}
							catch (Exception ex)
							{
								logger.error("InterfaceThread Unable to send emails");
							}
						}

					}
					else
					{
						logger.debug("Interface routine failed to logon to application using account INTERFACE");

					}

					try
					{
						backupMessageRetention = Integer.valueOf(ctrl.getKeyValueWithDefault("INTERFACE BACKUP RETENTION", "30", "NUMBER OF DAYS TO KEEP BACKUP MESSAGES"));
					}
					catch (Exception ex)
					{
						backupMessageRetention = 30;
					}

					String archiveReportString = "";
					if (shutdown == false)
					{
						logger.debug("Initiating data archiving....");
						JDBArchive c = new JDBArchive(getHostID(), getSessionID());
						c.runSQLJobList();
						archiveReportString = c.reportData();
						c = null;
						logger.debug("Data archiving complete....");

						logger.debug("Disconnecting from database.");
						Common.hostList.getHost(getHostID()).disconnectAll();
					}

					if (houseKeeping == true)
					{
						logger.debug("HOUSEKEEPING START");

						// Archive old backup files goes here
						String archivedFiles = "Backup message files removed by auto archive = ";
						if (backupMessageRetention > 0)
						{
							archivedFiles = archivedFiles + String.valueOf(JArchive.archiveBackupFiles(System.getProperty("user.dir") + File.separator + Common.interface_backup_path, backupMessageRetention));
						}
						else
						{
							archivedFiles = "Auto archive of messages disabled";
						}

						String freeSpace = JUtility.diskFree();

						String memoryBefore = "Memory used before garbage collection = " + String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) + "k";
						System.gc();
						String memoryAfter = "Memory used after garbage collection  = " + String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) + "k";
						String stats = GenericMessageHeader.getStats();
						GenericMessageHeader.clearStats();

						if (enableEnterfaceStatusEmails == true)
						{
							try
							{
								mail.postMail(emailList, "Commander4j " + JVersion.getProgramVersion() + " Interface maintenance for [" + siteName + "] on " + JUtility.getClientName(), memoryBefore + "\n\n" + memoryAfter + "\n\n" + archivedFiles
										+ "\n\n" + freeSpace + "\n\n" + "Maintenance is scheduled to occur at " + Common.statusReportTime + " each day.\n\n\n\n" + stats + "\n\n\n" + archiveReportString, "", "");
							}
							catch (Exception ex)
							{
								logger.error("InterfaceThread Unable to send emails");
							}
						}
						logger.debug("Interface Garbage Collection.");
						logger.debug("HOUSEKEEPING END");

					}
				}
			}
		}
	}

	public void startupThreads()
	{

		logger.debug("Starting Inbound Interface Thread.....");
		com.commander4j.util.JWait.milliSec(250);
		inboundThread = new InboundMessageThread(getHostID(), Common.interface_recovery_path, Common.interface_error_path, Common.interface_backup_path);
		inboundThread.setName("C4J Inbound Interface");
		inboundThread.start();

		logger.debug("Starting Outbound Interface Thread.....");
		com.commander4j.util.JWait.milliSec(250);
		outboundThread = new OutboundMessageThread(getHostID());
		outboundThread.setName("C4J Outbound Interface");
		outboundThread.start();

		com.commander4j.util.JWait.milliSec(250);
		LinkedList<String> test1 = new LinkedList<String>();
		JDBInterface interfaces = new JDBInterface(getHostID(), getSessionID());
		test1 = interfaces.getInputPaths();

		logger.debug("Starting File Collection Thread.....");
		fileCollectThread = new InboundMessageCollectionThread(test1, Common.interface_recovery_path);
		fileCollectThread.setName("C4J Recovery Thread");
		fileCollectThread.start();

		logger.debug("Starting Reporting Thread.....");
		com.commander4j.util.JWait.milliSec(250);
		reportingThread = new ReportingThread(getHostID());
		reportingThread.setName("C4J Reporting Thread");
		reportingThread.start();

		logger.debug("Starting Auto Labeller Thread.....");
		com.commander4j.util.JWait.milliSec(250);
		autoLabellerThread = new AutoLabellerThread(getHostID());
		autoLabellerThread.setName("C4J Auto Labeller");
		autoLabellerThread.start();

		com.commander4j.util.JWait.milliSec(250);
		threadsRunning = true;
	}

	public void shutdownThreads()
	{

		if (threadsRunning)
		{

			logger.debug("Stopping Reporting Thread.....");
			com.commander4j.util.JWait.milliSec(250);
			reportingThread.allDone = true;
			try
			{
				while (reportingThread.isAlive())
				{
					reportingThread.allDone = true;
					com.commander4j.util.JWait.milliSec(250);
				}
			}
			catch (Exception ex1)
			{

			}
			com.commander4j.util.JWait.milliSec(5000);
			reportingThread = null;
			logger.debug("Reporting Thread Stopped.");

			com.commander4j.util.JWait.milliSec(250);
			threadsRunning = false;

			logger.debug("Stopping Inbound Interface Thread.....");
			com.commander4j.util.JWait.milliSec(250);
			inboundThread.allDone = true;
			try
			{
				while (inboundThread.isAlive())
				{
					inboundThread.allDone = true;
					com.commander4j.util.JWait.milliSec(250);
				}
			}
			catch (Exception ex)
			{
			}
			inboundThread = null;
			logger.debug("Inbound Interface Thread Stopped.");

			logger.debug("Stopping Outbound Interface Thread.....");
			com.commander4j.util.JWait.milliSec(250);
			outboundThread.allDone = true;
			try
			{
				while (outboundThread.isAlive())
				{
					outboundThread.allDone = true;
					com.commander4j.util.JWait.milliSec(250);
				}
			}
			catch (Exception ex1)
			{

			}
			outboundThread = null;
			logger.debug("Outbound Interface Thread Stopped.");

			logger.debug("Stopping File Collection Thread.....");
			com.commander4j.util.JWait.milliSec(250);
			fileCollectThread.allDone = true;
			try
			{
				while (fileCollectThread.isAlive())
				{
					fileCollectThread.allDone = true;
					com.commander4j.util.JWait.milliSec(250);
				}
			}
			catch (Exception ex2)
			{

			}
			fileCollectThread = null;
			logger.debug("File Collection Thread Stopped.");

			logger.debug("Stopping Auto Labeller Thread.....");
			com.commander4j.util.JWait.milliSec(250);
			autoLabellerThread.allDone = true;
			try
			{
				while (autoLabellerThread.isAlive())
				{
					autoLabellerThread.allDone = true;
					com.commander4j.util.JWait.milliSec(250);
				}
			}
			catch (Exception ex1)
			{

			}
			autoLabellerThread = null;
			logger.debug("Auto Labeller Thread Stopped.");

		}
	}

}
