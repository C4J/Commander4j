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

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.commander4j.app.JVersion;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JUtility;
import java.awt.Dimension;


/**
 * The JDialogSysInfo class is a form which displays versions of libraries, programs and environment variables.
 * <p>
 * <img alt="" src="./doc-files/JDialogSysInfo.jpg" >
 * 
 */
public class JDialogSysInfo extends javax.swing.JDialog
{
	private JTextField4j jTextFieldHosyUniqueRef;
	private JLabel4j_std jLabel102_3;
	private JTextField4j jTextFieldUserHostDescription;
	private JLabel4j_std jLabel102_2;
	private JTextField4j jTextFieldUserHostID;
	private JTextField4j jTextFieldUserSessionID;
	private JLabel4j_std jLabel102_1;
	private JTextField4j jTextFieldUserNameApp;
	private JLabel4j_std jLabel102;
	private JTextField4j jTextFieldClientName;
	private JLabel4j_std jLabel101;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JLabel4j_std jLabel2;
	private JLabel4j_std jLabel4;
	private JTextField4j jTextFieldJDBCDriverName;
	private JTextField4j jTextFieldUserName;
	private JLabel4j_std jLabel11;
	private JLabel4j_std jLabel10;
	private JLabel4j_std jLabel8;
	private JLabel4j_std jLabel15;
	private JLabel4j_std jLabel14;
	private JTextField4j jTextFieldFreeMemory;
	private JTextField4j jTextFieldTotalMemory;
	private JLabel4j_std jLabel13;
	private JTextField4j jTextFieldJDBCDriverVersion;
	private JLabel4j_std jLabel12;
	private JLabel4j_std jLabel6;
	private JTextField4j jTextFieldDatabaseProductVersion;
	private JLabel4j_std jLabel5;
	private JTextField4j jTextFieldDatabaseProductName;
	private JTextField4j jTextFieldUserDir;
	private JTextField4j jTextFieldUserHome;
	private JTextField4j jTextFieldOSVersion;
	private JTextField4j jTextFieldOSArchitecture;
	private JTextField4j jTextFieldOSName;
	private JTextField4j jTextFieldJavaVMVersion;
	private JTextField4j jTextFieldJavaVersion;
	private JTextField4j jTextFieldJavaHome;
	private JLabel4j_std jLabel9;
	private JLabel4j_std jLabel7;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private JTextField4j textFieldUsedMemory;
	private JTextField4j textFieldLocale;
	private JTextField4j jTextFieldWorkstationID;

	public JDialogSysInfo(JFrame frame)
	{
		super(frame);
		initGUI();

		jTextFieldUserDir.setText(System.getProperty("user.dir"));
		jTextFieldUserHome.setText(System.getProperty("user.home"));
		jTextFieldUserName.setText(System.getProperty("user.name"));
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
		jTextFieldTotalMemory.setText(String.valueOf(Runtime.getRuntime().totalMemory()/1024) + "k");
		jTextFieldFreeMemory.setText(String.valueOf(Runtime.getRuntime().freeMemory()/1024) + "k");
		
		JLabel4j_std lblUsedMemory = new JLabel4j_std();
		lblUsedMemory.setText("Used Memory :");
		lblUsedMemory.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsedMemory.setBounds(530, 384, 91, 21);
		jDesktopPane1.add(lblUsedMemory);
		
		textFieldUsedMemory = new JTextField4j();
		textFieldUsedMemory.setText(String.valueOf((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024) + "k");
		textFieldUsedMemory.setHorizontalAlignment(SwingConstants.TRAILING);
		textFieldUsedMemory.setEnabled(false);
		textFieldUsedMemory.setEditable(false);
		textFieldUsedMemory.setBounds(632, 385, 119, 21);
		jDesktopPane1.add(textFieldUsedMemory);
		
		JLabel4j_std lblLocale = new JLabel4j_std();
		lblLocale.setText("Locale :");
		lblLocale.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLocale.setBounds(476, 44, 58, 21);
		jDesktopPane1.add(lblLocale);
		
		textFieldLocale = new JTextField4j();
		textFieldLocale.setEnabled(false);
		textFieldLocale.setEditable(false);
		textFieldLocale.setBounds(539, 43, 58, 20);
		jDesktopPane1.add(textFieldLocale);
		textFieldLocale.setText(Locale.getDefault().getCountry()+","+Locale.getDefault().getLanguage());
		
		jTextFieldWorkstationID = new JTextField4j();
		jTextFieldWorkstationID.setText(JUtility.getClientName());
		jTextFieldWorkstationID.setEditable(false);
		jTextFieldWorkstationID.setBounds(481, 288, 270, 20);
		jDesktopPane1.add(jTextFieldWorkstationID);
		
		JLabel4j_std lbl_WorkstationID = new JLabel4j_std();
		lbl_WorkstationID.setText("Workstation ID :");
		lbl_WorkstationID.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_WorkstationID.setBounds(362, 290, 105, 21);
		jDesktopPane1.add(lbl_WorkstationID);
		
		JLabel4j_std label4j_std = new JLabel4j_std();
		label4j_std.setText("App Version :");
		label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std.setBounds(25, 9, 92, 21);
		jDesktopPane1.add(label4j_std);
		
		JLabel4j_std label4j_std_1 = new JLabel4j_std();
		label4j_std_1.setText("Hosts FIle Version :");
		label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_1.setBounds(232, 12, 119, 21);
		jDesktopPane1.add(label4j_std_1);
		
		JTextField4j jTextFieldAppVersion = new JTextField4j();
		jTextFieldAppVersion.setText(JVersion.getProgramVersion());
		jTextFieldAppVersion.setEnabled(false);
		jTextFieldAppVersion.setEditable(false);
		jTextFieldAppVersion.setBounds(129, 11, 100, 20);
		jDesktopPane1.add(jTextFieldAppVersion);
		
		JTextField4j jTextFieldHostsVersion = new JTextField4j();
		jTextFieldHostsVersion.setText(Common.hostVersion);
		jTextFieldHostsVersion.setEnabled(false);
		jTextFieldHostsVersion.setEditable(false);
		jTextFieldHostsVersion.setBounds(360, 11, 100, 20);
		jDesktopPane1.add(jTextFieldHostsVersion);
	}

	private void initGUI() {
		try
		{
			{
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				this.setModal(true);
				this.setTitle("System Properties");
				this.setResizable(false);
			}
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.NORTH);
				jDesktopPane1.setPreferredSize(new Dimension(770, 461));
				jDesktopPane1.setLayout(null);
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText("Close");
					jButtonClose.setBounds(330, 417, 105, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText("Version :");
					jLabel1.setBounds(59, 290, 58, 21);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText("Java Version :");
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2.setBounds(25, 42, 92, 21);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText("Java VM Version :");
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel3.setBounds(232, 42, 119, 21);
				}
				{
					jLabel4 = new JLabel4j_std();
					jDesktopPane1.add(jLabel4);
					jLabel4.setText("Java Home :");
					jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel4.setBounds(29, 72, 88, 21);
				}
				{
					jLabel7 = new JLabel4j_std();
					jDesktopPane1.add(jLabel7);
					jLabel7.setText("OS Name :");
					jLabel7.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel7.setBounds(27, 104, 90, 21);
				}
				{
					jLabel8 = new JLabel4j_std();
					jDesktopPane1.add(jLabel8);
					jLabel8.setText("OS Architecture :");
					jLabel8.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel8.setBounds(232, 104, 118, 21);
				}
				{
					jLabel9 = new JLabel4j_std();
					jDesktopPane1.add(jLabel9);
					jLabel9.setText("OS Version :");
					jLabel9.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel9.setBounds(481, 104, 95, 21);
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText("OS User Name :");
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(24, 135, 93, 21);
				}
				{
					jLabel11 = new JLabel4j_std();
					jDesktopPane1.add(jLabel11);
					jLabel11.setText("User Home :");
					jLabel11.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel11.setBounds(27, 197, 90, 21);
				}
				{
					jTextFieldJavaHome = new JTextField4j();
					jDesktopPane1.add(jTextFieldJavaHome);
					jTextFieldJavaHome.setBounds(129, 74, 622, 20);
					jTextFieldJavaHome.setEditable(false);
					jTextFieldJavaHome.setEnabled(false);
				}
				{
					jTextFieldJavaVersion = new JTextField4j();
					jDesktopPane1.add(jTextFieldJavaVersion);
					jTextFieldJavaVersion.setBounds(129, 43, 100, 20);
					jTextFieldJavaVersion.setEditable(false);
					jTextFieldJavaVersion.setEnabled(false);
				}
				{
					jTextFieldJavaVMVersion = new JTextField4j();
					jDesktopPane1.add(jTextFieldJavaVMVersion);
					jTextFieldJavaVMVersion.setBounds(360, 43, 100, 20);
					jTextFieldJavaVMVersion.setEditable(false);
					jTextFieldJavaVMVersion.setEnabled(false);
				}
				{
					jTextFieldOSName = new JTextField4j();
					jDesktopPane1.add(jTextFieldOSName);
					jTextFieldOSName.setBounds(129, 105, 100, 20);
					jTextFieldOSName.setEditable(false);
					jTextFieldOSName.setEnabled(false);
				}
				{
					jTextFieldOSArchitecture = new JTextField4j();
					jDesktopPane1.add(jTextFieldOSArchitecture);
					jTextFieldOSArchitecture.setBounds(360, 105, 100, 20);
					jTextFieldOSArchitecture.setEditable(false);
					jTextFieldOSArchitecture.setEnabled(false);
				}
				{
					jTextFieldOSVersion = new JTextField4j();
					jDesktopPane1.add(jTextFieldOSVersion);
					jTextFieldOSVersion.setBounds(582, 105, 169, 20);
					jTextFieldOSVersion.setEditable(false);
					jTextFieldOSVersion.setEnabled(false);
				}
				{
					jTextFieldUserName = new JTextField4j();
					jDesktopPane1.add(jTextFieldUserName);
					jTextFieldUserName.setEditable(false);
					jTextFieldUserName.setBounds(129, 136, 231, 20);
				}
				{
					jTextFieldUserHome = new JTextField4j();
					jDesktopPane1.add(jTextFieldUserHome);
					jTextFieldUserHome.setEditable(false);
					jTextFieldUserHome.setBounds(129, 198, 624, 20);
					jTextFieldUserHome.setEnabled(false);
				}
				{
					jTextFieldUserDir = new JTextField4j();
					jDesktopPane1.add(jTextFieldUserDir);
					jTextFieldUserDir.setEditable(false);
					jTextFieldUserDir.setBounds(129, 229, 624, 20);
					jTextFieldUserDir.setEnabled(false);
				}
				{
					jTextFieldDatabaseProductName = new JTextField4j();
					jDesktopPane1.add(jTextFieldDatabaseProductName);
					jTextFieldDatabaseProductName.setEditable(false);
					jTextFieldDatabaseProductName.setBounds(129, 260, 231, 20);
					jTextFieldDatabaseProductName.setEnabled(false);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText("Base Dir :");
					jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5.setBounds(27, 228, 90, 21);
				}
				{
					jTextFieldDatabaseProductVersion = new JTextField4j();
					jDesktopPane1.add(jTextFieldDatabaseProductVersion);
					jTextFieldDatabaseProductVersion.setEditable(false);
					jTextFieldDatabaseProductVersion.setBounds(129, 291, 231, 20);
					jTextFieldDatabaseProductVersion.setEnabled(false);
				}
				{
					jLabel6 = new JLabel4j_std();
					jDesktopPane1.add(jLabel6);
					jLabel6.setText("Version :");
					jLabel6.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel6.setBounds(66, 352, 51, 21);
				}
				{
					jLabel12 = new JLabel4j_std();
					jDesktopPane1.add(jLabel12);
					jLabel12.setText("Database Name :");
					jLabel12.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel12.setBounds(17, 259, 100, 21);
				}
				{
					jTextFieldJDBCDriverName = new JTextField4j();
					jDesktopPane1.add(jTextFieldJDBCDriverName);
					jTextFieldJDBCDriverName.setEditable(false);
					jTextFieldJDBCDriverName.setBounds(129, 322, 231, 20);
					jTextFieldJDBCDriverName.setEnabled(false);
				}
				{
					jTextFieldJDBCDriverVersion = new JTextField4j();
					jDesktopPane1.add(jTextFieldJDBCDriverVersion);
					jTextFieldJDBCDriverVersion.setEditable(false);
					jTextFieldJDBCDriverVersion.setBounds(129, 353, 622, 20);
					jTextFieldJDBCDriverVersion.setEnabled(false);
				}
				{
					jLabel13 = new JLabel4j_std();
					jDesktopPane1.add(jLabel13);
					jLabel13.setText("Driver Name :");
					jLabel13.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel13.setBounds(37, 321, 80, 21);
				}
				{
					jTextFieldTotalMemory = new JTextField4j();
					jDesktopPane1.add(jTextFieldTotalMemory);
					jTextFieldTotalMemory.setEditable(false);
					jTextFieldTotalMemory.setEnabled(false);
					jTextFieldTotalMemory.setBounds(129, 384, 119, 21);
					jTextFieldTotalMemory.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldFreeMemory = new JTextField4j();
					jDesktopPane1.add(jTextFieldFreeMemory);
					jTextFieldFreeMemory.setEditable(false);
					jTextFieldFreeMemory.setEnabled(false);
					jTextFieldFreeMemory.setBounds(359, 384, 119, 21);
					jTextFieldFreeMemory.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel14 = new JLabel4j_std();
					jDesktopPane1.add(jLabel14);
					jLabel14.setText("Total Memory :");
					jLabel14.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel14.setBounds(12, 384, 105, 21);
				}
				{
					jLabel15 = new JLabel4j_std();
					jDesktopPane1.add(jLabel15);
					jLabel15.setText("Free Memory :");
					jLabel15.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel15.setBounds(254, 384, 91, 21);
				}

				{
					jLabel101 = new JLabel4j_std();
					jLabel101.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel101.setText("Client Name :");
					jLabel101.setBounds(24, 166, 93, 21);
					jDesktopPane1.add(jLabel101);
				}

				{
					jTextFieldClientName = new JTextField4j();
					jTextFieldClientName.setEditable(false);
					jTextFieldClientName.setBounds(129, 167, 231, 20);
					jTextFieldClientName.setText(JUtility.getClientName());
					jDesktopPane1.add(jTextFieldClientName);
				}

				{
					jLabel102 = new JLabel4j_std();
					jLabel102.setHorizontalAlignment(SwingConstants.TRAILING);
						jLabel102.setText("Application User Name :");
					jLabel102.setBounds(355, 135, 146, 21);
					jDesktopPane1.add(jLabel102);
				}

				{
					jTextFieldUserNameApp = new JTextField4j();
					jTextFieldUserNameApp.setEditable(false);
					jTextFieldUserNameApp.setBounds(520, 136, 231, 20);
					jTextFieldUserNameApp.setText(Common.userList.getUser(Common.sessionID).getUserId());
					jDesktopPane1.add(jTextFieldUserNameApp);
				}

				{
					jLabel102_1 = new JLabel4j_std();
					jLabel102_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel102_1.setText("Session ID :");
					jLabel102_1.setBounds(383, 166, 118, 21);
					jDesktopPane1.add(jLabel102_1);
				}

				{
					jTextFieldUserSessionID = new JTextField4j();
					jTextFieldUserSessionID.setEditable(false);
					jTextFieldUserSessionID.setBounds(520, 166, 231, 20);
					jDesktopPane1.add(jTextFieldUserSessionID);
				}

				{
					jTextFieldUserHostID = new JTextField4j();
					jTextFieldUserHostID.setEditable(false);
					jTextFieldUserHostID.setBounds(435, 260, 32, 20);
					jDesktopPane1.add(jTextFieldUserHostID);
				}

				{
					jLabel102_2 = new JLabel4j_std();
					jLabel102_2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel102_2.setText("Host ID :");
					jLabel102_2.setBounds(383, 259, 51, 21);
					jDesktopPane1.add(jLabel102_2);
				}

				{
					jTextFieldUserHostDescription = new JTextField4j();
					jTextFieldUserHostDescription.setBounds(481, 260, 270, 20);
					jTextFieldUserHostDescription.setEditable(false);
					jDesktopPane1.add(jTextFieldUserHostDescription);
				}

				{
					jLabel102_3 = new JLabel4j_std();
					jLabel102_3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel102_3.setText("Unique Host Ref :");
					jLabel102_3.setBounds(383, 321, 95, 21);
					jDesktopPane1.add(jLabel102_3);
				}

				{
					jTextFieldHosyUniqueRef = new JTextField4j();
					jTextFieldHosyUniqueRef.setEditable(false);
					jTextFieldHosyUniqueRef.setBounds(481, 321, 270, 20);
					jDesktopPane1.add(jTextFieldHosyUniqueRef);
				}
			}
			this.setSize(771, 488);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
