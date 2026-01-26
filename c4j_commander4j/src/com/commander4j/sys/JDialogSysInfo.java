package com.commander4j.sys;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JDialogSysInfo.java
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.commander4j.app.JVersion;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JUtility;
import com.commander4j.util.RequestCurrentUser;

import java.awt.Dimension;

/**
 * The JDialogSysInfo class is a form which displays versions of libraries,
 * programs and environment variables.
 * <p>
 * <img alt="" src="./doc-files/JDialogSysInfo.jpg" >
 *
 */
public class JDialogSysInfo extends javax.swing.JDialog
{
	private JButton4j jButtonClose;
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_std jLabel_AppUser;
	private JLabel4j_std jLabel_Architecture;
	private JLabel4j_std jLabel_BaseDir;
	private JLabel4j_std jLabel_DatabaseName;
	private JLabel4j_std jLabel_DriverName;
	private JLabel4j_std jLabel_DriverVersion;
	private JLabel4j_std jLabel_FreeMemory;
	private JLabel4j_std jLabel_HostID;
	private JLabel4j_std jLabel_JavaHome;
	private JLabel4j_std jLabel_JavaVersion;
	private JLabel4j_std jLabel_OSName;
	private JLabel4j_std jLabel_OSUser;
	private JLabel4j_std jLabel_OSVer;
	private JLabel4j_std jLabel_Session;
	private JLabel4j_std jLabel_TotalMemory;
	private JLabel4j_std jLabel_Unique;
	private JLabel4j_std jLabel_UserHome;
	private JLabel4j_std jLabel_VMVersion;
	private JLabel4j_std jLabel_Version;
	private JLabel4j_std jLabel_WorkstationName;
	private JTextField4j jTextFieldClientName;
	private JTextField4j jTextFieldDatabaseProductName;
	private JTextField4j jTextFieldDatabaseProductVersion;
	private JTextField4j jTextFieldFreeMemory;
	private JTextField4j jTextFieldHosyUniqueRef;
	private JTextField4j jTextFieldJDBCDriverName;
	private JTextField4j jTextFieldJDBCDriverVersion;
	private JTextField4j jTextFieldJavaHome;
	private JTextField4j jTextFieldJavaVMVersion;
	private JTextField4j jTextFieldJavaVersion;
	private JTextField4j jTextFieldOSArchitecture;
	private JTextField4j jTextFieldOSName;
	private JTextField4j jTextFieldOSVersion;
	private JTextField4j jTextFieldTotalMemory;
	private JTextField4j jTextFieldUserDir;
	private JTextField4j jTextFieldUserHome;
	private JTextField4j jTextFieldUserHostDescription;
	private JTextField4j jTextFieldUserHostID;
	private JTextField4j jTextFieldUserName;
	private JTextField4j jTextFieldUserNameApp;
	private JTextField4j jTextFieldUserSessionID;
	private JTextField4j jTextFieldWorkstationID;
	private JTextField4j textFieldLocale;
	private JTextField4j textFieldUsedMemory;
	private static final long serialVersionUID = 1;

	public JDialogSysInfo(JFrame frame)
	{
		super(frame);
		initGUI();

		jTextFieldUserDir.setText(System.getProperty("user.dir"));
		jTextFieldUserHome.setText(System.getProperty("user.home"));
		jTextFieldUserName.setText(RequestCurrentUser.getAuthoritativeUsername().toUpperCase());
		jTextFieldUserSessionID.setText(Common.sessionID);
		jTextFieldUserHostID.setText(Common.selectedHostID);
		jTextFieldUserHostDescription.setText(Common.hostList.getHost(Common.selectedHostID).getSiteDescription());
		jTextFieldHosyUniqueRef.setText(Common.hostList.getHost(Common.selectedHostID).getUniqueID());
		jTextFieldOSVersion.setText(System.getProperty("os.version"));
		jTextFieldOSArchitecture.setText(System.getProperty("os.arch"));
		jTextFieldOSName.setText(System.getProperty("os.name"));
		jTextFieldJavaHome.setText(System.getProperty("java.home"));
		jTextFieldJavaVMVersion.setText(System.getProperty("java.vm.version"));
		jTextFieldJavaVersion.setText(System.getProperty("java.version"));

		try
		{

			DatabaseMetaData dmd = Common.hostList.getHost(Common.selectedHostID).getConnection(Common.sessionID).getMetaData();
			jTextFieldDatabaseProductName.setText(dmd.getDatabaseProductName());
			jTextFieldDatabaseProductVersion.setText(dmd.getDatabaseProductVersion());
			jTextFieldJDBCDriverName.setText(dmd.getDriverName());
			jTextFieldJDBCDriverVersion.setText(dmd.getDriverVersion());
		}
		catch (SQLException E)
		{

		}
		jTextFieldTotalMemory.setText(String.valueOf(Runtime.getRuntime().totalMemory() / 1024) + "k");
		jTextFieldFreeMemory.setText(String.valueOf(Runtime.getRuntime().freeMemory() / 1024) + "k");

		JLabel4j_std jLabel_UsedMemory = new JLabel4j_std();
		jLabel_UsedMemory.setText("Used Memory :");
		jLabel_UsedMemory.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel_UsedMemory.setBounds(611, 448, 91, 22);
		jDesktopPane1.add(jLabel_UsedMemory);

		textFieldUsedMemory = new JTextField4j();
		textFieldUsedMemory.setText(String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) + "k");
		textFieldUsedMemory.setHorizontalAlignment(SwingConstants.TRAILING);
		textFieldUsedMemory.setEditable(false);
		textFieldUsedMemory.setBounds(714, 448, 119, 22);
		jDesktopPane1.add(textFieldUsedMemory);

		JLabel4j_std jLabel_Locale = new JLabel4j_std();
		jLabel_Locale.setText("Locale :");
		jLabel_Locale.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel_Locale.setBounds(647, 107, 88, 22);
		jDesktopPane1.add(jLabel_Locale);

		textFieldLocale = new JTextField4j();
		textFieldLocale.setEditable(false);
		textFieldLocale.setBounds(753, 107, 80, 22);
		jDesktopPane1.add(textFieldLocale);
		textFieldLocale.setText(Locale.getDefault().getCountry() + "," + Locale.getDefault().getLanguage());

		jTextFieldWorkstationID = new JTextField4j();
		jTextFieldWorkstationID.setText(JUtility.getClientName());
		jTextFieldWorkstationID.setEditable(false);
		jTextFieldWorkstationID.setBounds(616, 355, 217, 22);
		jDesktopPane1.add(jTextFieldWorkstationID);

		JLabel4j_std jLabel_WorkstationID = new JLabel4j_std();
		jLabel_WorkstationID.setText("Workstation ID :");
		jLabel_WorkstationID.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel_WorkstationID.setBounds(502, 355, 105, 22);
		jDesktopPane1.add(jLabel_WorkstationID);

		JLabel4j_std jLabel_AppVersion = new JLabel4j_std();
		jLabel_AppVersion.setText("App Version :");
		jLabel_AppVersion.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel_AppVersion.setBounds(25, 9, 92, 22);
		jDesktopPane1.add(jLabel_AppVersion);

		JLabel4j_std jLabel_HostsVersion = new JLabel4j_std();
		jLabel_HostsVersion.setText("Hosts FIle Version :");
		jLabel_HostsVersion.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel_HostsVersion.setBounds(232, 9, 119, 22);
		jDesktopPane1.add(jLabel_HostsVersion);

		JTextField4j jTextFieldAppVersion = new JTextField4j();
		jTextFieldAppVersion.setText(JVersion.getProgramVersion());
		jTextFieldAppVersion.setEditable(false);
		jTextFieldAppVersion.setBounds(129, 9, 100, 22);
		jDesktopPane1.add(jTextFieldAppVersion);

		JTextField4j jTextFieldHostsVersion = new JTextField4j();
		jTextFieldHostsVersion.setText(Common.hostVersion);
		jTextFieldHostsVersion.setEditable(false);
		jTextFieldHostsVersion.setBounds(360, 9, 100, 22);
		jDesktopPane1.add(jTextFieldHostsVersion);

		JLabel4j_std jLabel_UpdateURL = new JLabel4j_std();
		jLabel_UpdateURL.setText("App Update URL :");
		jLabel_UpdateURL.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel_UpdateURL.setBounds(25, 40, 92, 22);
		jDesktopPane1.add(jLabel_UpdateURL);

		JTextField4j textField4j = new JTextField4j();
		textField4j.setText(Common.updateURL);
		textField4j.setEditable(false);
		textField4j.setBounds(129, 40, 704, 22);
		jDesktopPane1.add(textField4j);

		JLabel4j_std jLabel_UpdatePath = new JLabel4j_std();
		jLabel_UpdatePath.setText("Hosts Update Path :");
		jLabel_UpdatePath.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel_UpdatePath.setBounds(12, 74, 103, 22);
		jDesktopPane1.add(jLabel_UpdatePath);

		JTextField4j textField4j_1 = new JTextField4j();
		textField4j_1.setText(Common.hostUpdatePath);
		textField4j_1.setEditable(false);
		textField4j_1.setBounds(127, 74, 706, 22);
		jDesktopPane1.add(textField4j_1);

		JLabel4j_std jLabel_UpdateMode = new JLabel4j_std();
		jLabel_UpdateMode.setText("Update Mode :");
		jLabel_UpdateMode.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel_UpdateMode.setBounds(479, 9, 88, 22);
		jDesktopPane1.add(jLabel_UpdateMode);

		JTextField4j jTextFieldUpdateMode = new JTextField4j();
		jTextFieldUpdateMode.setText(Common.updateMODE);
		jTextFieldUpdateMode.setEditable(false);
		jTextFieldUpdateMode.setBounds(666, 9, 167, 22);
		jDesktopPane1.add(jTextFieldUpdateMode);
	}

	private void initGUI()
	{
		try
		{

			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setModal(true);
			this.setTitle("System Properties");
			this.setResizable(false);

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.NORTH);
			jDesktopPane1.setPreferredSize(new Dimension(770, 535));
			jDesktopPane1.setLayout(null);

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText("Close");
			jButtonClose.setBounds(377, 482, 105, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jLabel_Version = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Version);
			jLabel_Version.setText("Version :");
			jLabel_Version.setBounds(57, 355, 58, 22);
			jLabel_Version.setHorizontalAlignment(SwingConstants.TRAILING);

			jLabel_JavaVersion = new JLabel4j_std();
			jDesktopPane1.add(jLabel_JavaVersion);
			jLabel_JavaVersion.setText("Java Version :");
			jLabel_JavaVersion.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_JavaVersion.setBounds(23, 107, 92, 22);

			jLabel_VMVersion = new JLabel4j_std();
			jDesktopPane1.add(jLabel_VMVersion);
			jLabel_VMVersion.setText("Java VM Version :");
			jLabel_VMVersion.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_VMVersion.setBounds(358, 107, 119, 22);

			jLabel_JavaHome = new JLabel4j_std();
			jDesktopPane1.add(jLabel_JavaHome);
			jLabel_JavaHome.setText("Java Home :");
			jLabel_JavaHome.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_JavaHome.setBounds(27, 139, 88, 21);

			jLabel_OSName = new JLabel4j_std();
			jDesktopPane1.add(jLabel_OSName);
			jLabel_OSName.setText("OS Name :");
			jLabel_OSName.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_OSName.setBounds(25, 168, 90, 22);

			jLabel_Architecture = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Architecture);
			jLabel_Architecture.setText("OS Architecture :");
			jLabel_Architecture.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Architecture.setBounds(359, 168, 118, 22);

			jLabel_OSVer = new JLabel4j_std();
			jDesktopPane1.add(jLabel_OSVer);
			jLabel_OSVer.setText("OS Version :");
			jLabel_OSVer.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_OSVer.setBounds(640, 168, 95, 22);

			jLabel_OSUser = new JLabel4j_std();
			jDesktopPane1.add(jLabel_OSUser);
			jLabel_OSUser.setText("OS User Name :");
			jLabel_OSUser.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_OSUser.setBounds(22, 200, 93, 22);

			jLabel_UserHome = new JLabel4j_std();
			jDesktopPane1.add(jLabel_UserHome);
			jLabel_UserHome.setText("User Home :");
			jLabel_UserHome.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_UserHome.setBounds(25, 262, 90, 22);

			jTextFieldJavaHome = new JTextField4j();
			jDesktopPane1.add(jTextFieldJavaHome);
			jTextFieldJavaHome.setBounds(127, 138, 706, 22);
			jTextFieldJavaHome.setEditable(false);

			jTextFieldJavaVersion = new JTextField4j();
			jDesktopPane1.add(jTextFieldJavaVersion);
			jTextFieldJavaVersion.setBounds(127, 107, 231, 22);
			jTextFieldJavaVersion.setEditable(false);

			jTextFieldJavaVMVersion = new JTextField4j();
			jDesktopPane1.add(jTextFieldJavaVMVersion);
			jTextFieldJavaVMVersion.setBounds(486, 107, 100, 22);
			jTextFieldJavaVMVersion.setEditable(false);

			jTextFieldOSName = new JTextField4j();
			jDesktopPane1.add(jTextFieldOSName);
			jTextFieldOSName.setBounds(127, 168, 231, 22);
			jTextFieldOSName.setEditable(false);

			jTextFieldOSArchitecture = new JTextField4j();
			jDesktopPane1.add(jTextFieldOSArchitecture);
			jTextFieldOSArchitecture.setBounds(486, 168, 100, 22);
			jTextFieldOSArchitecture.setEditable(false);

			jTextFieldOSVersion = new JTextField4j();
			jDesktopPane1.add(jTextFieldOSVersion);
			jTextFieldOSVersion.setBounds(753, 168, 80, 22);
			jTextFieldOSVersion.setEditable(false);

			jTextFieldUserName = new JTextField4j();
			jDesktopPane1.add(jTextFieldUserName);
			jTextFieldUserName.setEditable(false);
			jTextFieldUserName.setBounds(127, 200, 306, 22);

			jTextFieldUserHome = new JTextField4j();
			jDesktopPane1.add(jTextFieldUserHome);
			jTextFieldUserHome.setEditable(false);
			jTextFieldUserHome.setBounds(127, 262, 706, 22);

			jTextFieldUserDir = new JTextField4j();
			jDesktopPane1.add(jTextFieldUserDir);
			jTextFieldUserDir.setEditable(false);
			jTextFieldUserDir.setBounds(127, 293, 706, 22);

			jTextFieldDatabaseProductName = new JTextField4j();
			jDesktopPane1.add(jTextFieldDatabaseProductName);
			jTextFieldDatabaseProductName.setEditable(false);
			jTextFieldDatabaseProductName.setBounds(127, 324, 377, 22);

			jLabel_BaseDir = new JLabel4j_std();
			jDesktopPane1.add(jLabel_BaseDir);
			jLabel_BaseDir.setText("Base Dir :");
			jLabel_BaseDir.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_BaseDir.setBounds(25, 293, 90, 22);

			jTextFieldDatabaseProductVersion = new JTextField4j();
			jDesktopPane1.add(jTextFieldDatabaseProductVersion);
			jTextFieldDatabaseProductVersion.setEditable(false);
			jTextFieldDatabaseProductVersion.setBounds(127, 355, 377, 22);

			jLabel_DriverVersion = new JLabel4j_std();
			jDesktopPane1.add(jLabel_DriverVersion);
			jLabel_DriverVersion.setText("Version :");
			jLabel_DriverVersion.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_DriverVersion.setBounds(64, 417, 51, 22);

			jLabel_DatabaseName = new JLabel4j_std();
			jDesktopPane1.add(jLabel_DatabaseName);
			jLabel_DatabaseName.setText("Database Name :");
			jLabel_DatabaseName.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_DatabaseName.setBounds(15, 324, 100, 22);

			jTextFieldJDBCDriverName = new JTextField4j();
			jDesktopPane1.add(jTextFieldJDBCDriverName);
			jTextFieldJDBCDriverName.setEditable(false);
			jTextFieldJDBCDriverName.setBounds(127, 386, 377, 22);

			jTextFieldJDBCDriverVersion = new JTextField4j();
			jDesktopPane1.add(jTextFieldJDBCDriverVersion);
			jTextFieldJDBCDriverVersion.setEditable(false);
			jTextFieldJDBCDriverVersion.setBounds(127, 417, 706, 22);

			jLabel_DriverName = new JLabel4j_std();
			jDesktopPane1.add(jLabel_DriverName);
			jLabel_DriverName.setText("Driver Name :");
			jLabel_DriverName.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_DriverName.setBounds(35, 386, 80, 22);

			jTextFieldTotalMemory = new JTextField4j();
			jDesktopPane1.add(jTextFieldTotalMemory);
			jTextFieldTotalMemory.setEditable(false);
			jTextFieldTotalMemory.setBounds(127, 448, 119, 22);
			jTextFieldTotalMemory.setHorizontalAlignment(SwingConstants.TRAILING);

			jTextFieldFreeMemory = new JTextField4j();
			jDesktopPane1.add(jTextFieldFreeMemory);
			jTextFieldFreeMemory.setEditable(false);
			jTextFieldFreeMemory.setBounds(448, 448, 119, 22);
			jTextFieldFreeMemory.setHorizontalAlignment(SwingConstants.TRAILING);

			jLabel_TotalMemory = new JLabel4j_std();
			jDesktopPane1.add(jLabel_TotalMemory);
			jLabel_TotalMemory.setText("Total Memory :");
			jLabel_TotalMemory.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_TotalMemory.setBounds(10, 448, 105, 22);

			jLabel_FreeMemory = new JLabel4j_std();
			jDesktopPane1.add(jLabel_FreeMemory);
			jLabel_FreeMemory.setText("Free Memory :");
			jLabel_FreeMemory.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_FreeMemory.setBounds(343, 448, 91, 22);

			jLabel_WorkstationName = new JLabel4j_std();
			jLabel_WorkstationName.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_WorkstationName.setText("Client Name :");
			jLabel_WorkstationName.setBounds(22, 231, 93, 22);
			jDesktopPane1.add(jLabel_WorkstationName);

			jTextFieldClientName = new JTextField4j();
			jTextFieldClientName.setEditable(false);
			jTextFieldClientName.setBounds(127, 231, 350, 22);
			jTextFieldClientName.setText(JUtility.getClientName());
			jDesktopPane1.add(jTextFieldClientName);

			jLabel_AppUser = new JLabel4j_std();
			jLabel_AppUser.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_AppUser.setText("Application User Name :");
			jLabel_AppUser.setBounds(440, 200, 146, 22);
			jDesktopPane1.add(jLabel_AppUser);

			jTextFieldUserNameApp = new JTextField4j();
			jTextFieldUserNameApp.setEditable(false);
			jTextFieldUserNameApp.setBounds(602, 200, 231, 22);
			jTextFieldUserNameApp.setText(Common.userList.getUser(Common.sessionID).getUserId());
			jDesktopPane1.add(jTextFieldUserNameApp);

			jLabel_Session = new JLabel4j_std();
			jLabel_Session.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Session.setText("Session ID :");
			jLabel_Session.setBounds(468, 231, 118, 22);
			jDesktopPane1.add(jLabel_Session);

			jTextFieldUserSessionID = new JTextField4j();
			jTextFieldUserSessionID.setEditable(false);
			jTextFieldUserSessionID.setBounds(602, 231, 231, 22);
			jDesktopPane1.add(jTextFieldUserSessionID);

			jTextFieldUserHostID = new JTextField4j();
			jTextFieldUserHostID.setEditable(false);
			jTextFieldUserHostID.setBounds(575, 324, 32, 22);
			jDesktopPane1.add(jTextFieldUserHostID);

			jLabel_HostID = new JLabel4j_std();
			jLabel_HostID.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_HostID.setText("Host ID :");
			jLabel_HostID.setBounds(512, 324, 51, 22);
			jDesktopPane1.add(jLabel_HostID);

			jTextFieldUserHostDescription = new JTextField4j();
			jTextFieldUserHostDescription.setBounds(616, 324, 217, 22);
			jTextFieldUserHostDescription.setEditable(false);
			jDesktopPane1.add(jTextFieldUserHostDescription);

			jLabel_Unique = new JLabel4j_std();
			jLabel_Unique.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Unique.setText("Unique Host Ref :");
			jLabel_Unique.setBounds(512, 386, 95, 22);
			jDesktopPane1.add(jLabel_Unique);

			jTextFieldHosyUniqueRef = new JTextField4j();
			jTextFieldHosyUniqueRef.setEditable(false);
			jTextFieldHosyUniqueRef.setBounds(616, 386, 217, 22);
			jDesktopPane1.add(jTextFieldHosyUniqueRef);

			this.setSize(859, 562);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
