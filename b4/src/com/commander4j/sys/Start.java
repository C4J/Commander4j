package com.commander4j.sys;

import java.util.Arrays;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : Start.java
 * 
 * Package Name : com.commander4j.sys
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

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBSchema;
import com.commander4j.db.JDBUser;
import com.commander4j.util.JPlaySound;
import com.commander4j.util.JPrint;
import com.commander4j.util.JSplashScreenUtils;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;

public class Start
{
	public static void main(String[] args)
	{
		Logger logger = Logger.getLogger(Start.class);
		JDBLanguage lang;
		@SuppressWarnings("unused")
		JDialogLogin userLogonDialog;
		JDialogChangePassword userPasswordChangeDialog;
		JDialogHosts hsts;
		JDBControl ctrl;
		JDBUser user;
		String passwordMode = "";

		Thread t = Thread.currentThread();
		t.setName("C4J Main");

		Common.base_dir = System.getProperty("user.dir");
		Common.sessionID = JUnique.getUniqueID();
		Common.sd.setData(Common.sessionID, "silentExceptions", "No", true);
		Common.applicationMode = "SwingClient";
		JPlaySound.enable();
		JUtility.setLookandFeel();
		JUtility.adjustForLookandFeel();
		JConfig.loadConfig("config.xml");
		JUtility.initLogging("");

		Common.hostList.loadHosts();
		
		if (Common.hostList.checkUpdatedHosts()==true)
		{
			logger.debug("Reloading Host File");
			Common.hostList.loadHosts();
		}

		com.commander4j.util.JUpdate.updateCheck();

		JSplashScreenUtils.create();
		JSplashScreenUtils.updateProgress(0, "Initialising");
		JSplashScreenUtils.updateProgress(5, "Starting Logger");
		JSplashScreenUtils.updateProgress(10, "Checking Printers");

		JPrint.init();

		if (JUtility.isValidJavaVersion(Common.requiredJavaVersion) == false)
		{
			JUtility.errorBeep();
			JOptionPane.showMessageDialog(null, "This application requires java version " + Common.requiredJavaVersion + " or higher.\n Detected version is " + System.getProperty("java.version"), "Information", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		JSplashScreenUtils.updateProgress(15, "Setting Look and Feel");

		logger.info("Application starting");
		JSplashScreenUtils.updateProgress(20, "Preparing for Logon");

		if (JUtility.getActiveHostCount() > 1)
		{
			JSplashScreenUtils.hide();

			hsts = new JDialogHosts(null);
			hsts.setVisible(false);
			hsts.dispose();
		}
		else
		{
			Common.selectedHostID = JUtility.getFirtActiveHost().getSiteNumber();
		}

		if (Common.selectedHostID.equals("Cancel") == false)
		{
			if (Common.displaySplashScreen)
			{
				JSplashScreenUtils.show();

				JSplashScreenUtils.updateProgress(25, "Initialising....");
				JSplashScreenUtils.updateProgress(30, "Open SQL Library....");
				JSplashScreenUtils.updateProgress(35, "Building jdbc connection string....");
				JSplashScreenUtils.updateProgress(40, "Loading database SQL statements...");
				JSplashScreenUtils.updateProgress(45, "Open Virtual Views....");
				
				Arrays.sort(Common.messageTypesexclBlank);
				Arrays.sort(Common.messageTypesincBlank);
				Arrays.sort(Common.transactionTypes);
				Arrays.sort(Common.transactionSubTypes);
				Arrays.sort(Common.auditEventActions);
				Arrays.sort(Common.auditEventTypes);
				Arrays.sort(Common.processOrderStatusincBlank);
				Arrays.sort(Common.languages);
				
				JSplashScreenUtils.updateProgress(55, "Building jdbc connection string....");
				JSplashScreenUtils.updateProgress(60, "Loading database view statements...");
				JSplashScreenUtils.updateProgress(65, "Loading database view statements...");
				JSplashScreenUtils.updateProgress(70, "Connecting to Host database...");
			}

			if (Common.hostList.getHost(Common.selectedHostID).connect(Common.sessionID, Common.selectedHostID) == true)
			{
				JSplashScreenUtils.updateProgress(75, "Validating Schema Version...");

				JDBSchema schema = new JDBSchema(Common.sessionID, Common.hostList.getHost(Common.selectedHostID));

				schema.validate(true);
				JSplashScreenUtils.updateProgress(80, "Initialising Help Subsystem...");
				JSplashScreenUtils.updateProgress(85, "Loading EAN Barcode definitions...");

				JUtility.initEANBarcode();

				JSplashScreenUtils.updateProgress(90, "Initialising Reporting system...");

				JLaunchReport.init();

				JSplashScreenUtils.updateProgress(95, "Initialising Common values...");

				Common.init();

				JSplashScreenUtils.updateProgress(100, "Loading Logon dialog...");

				JSplashScreenUtils.hide();
				JSplashScreenUtils.remove();

				ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
				passwordMode = ctrl.getKeyValue("PASSWORD MODE");

				// OS LOGON
				if (passwordMode.equals("PASSTHROUGH") || passwordMode.equals("PASSTHROUGH_FALLBACK"))
				{
					// When using OS mode to logon ignore attributes for
					// Password Change.

					user = new JDBUser(Common.selectedHostID, Common.sessionID);
					user.getUserProperties(System.getProperty("user.name").toUpperCase());

					// Try and logon using OS username.
					if (user.login())
					{
						// Add os user to list (no dialog)
						Common.userList.addUser(Common.sessionID, user);
						Common.validatedUsername = user.getUserId();
						Common.validatedPassword = user.getLoginPassword();
						Common.logonValidated = true;

					}
					else
					{
						// If OS username no goood and passthrough enabled try
						// local as fallback.
						if (passwordMode.equals("PASSTHROUGH"))
						{
							JUtility.errorBeep();
							JOptionPane.showMessageDialog(null, "Error during logon for user [" + user.getUserId() + "]\n" + user.getErrorMessage(), "Login", JOptionPane.ERROR_MESSAGE);
							System.exit(0);
						}
						else
						{
							passwordMode = "LOCAL";
						}
					}
				}

				// LOCAL LOGON
				if (passwordMode.equals("LOCAL") || (Common.logonValidated == false))
				{
					userLogonDialog = new JDialogLogin(null);

				}

				lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
				lang.preLoad("%");

				// Check logon worked
				if (Common.logonValidated)
				{
					// Do we need to prompt for new password
					if (Common.passwordChangeRequested || Common.passwordChangeRequired || Common.passwordExpired)
					{

						// if system forced password change determine message to
						// display.
						String prompt = "";

						if (Common.passwordChangeRequested)
						{
							prompt = lang.get("lbl_Password_Change_Requested");
						}

						if (Common.passwordChangeRequired)
						{
							prompt = lang.get("lbl_Password_Change_Required");
							JOptionPane.showMessageDialog(null, prompt, "Password", JOptionPane.ERROR_MESSAGE);
						}

						if (Common.passwordExpired)
						{
							prompt = lang.get("lbl_Password_Expired");
							JOptionPane.showMessageDialog(null, prompt, "Password", JOptionPane.ERROR_MESSAGE);
						}

						// show password change dialog
						userPasswordChangeDialog = new JDialogChangePassword(null, Common.validatedUsername, Common.validatedPassword);

						// was password change necessary
						if (Common.passwordChangeRequired || Common.passwordExpired)
						{
							if (userPasswordChangeDialog.passwordChanged == false)
							{
								// if user cancelled password change and is was
								// needed abort
								System.exit(0);
							}
						}
					}

					Common.mainForm = new JFrameMain();
					Common.mainForm.setIconImage(Common.imageIconloader.getImageIcon(Common.image_osx_commander4j).getImage());
					Common.mainForm.setVisible(true);

				}
				else
				{
					Common.hostList.getHost(Common.selectedHostID).disconnect(Common.sessionID);
					System.exit(0);
				}
			}
			else
			{
				System.exit(0);
			}
		}
		else
		{
			System.exit(0);
		}
	}
}
