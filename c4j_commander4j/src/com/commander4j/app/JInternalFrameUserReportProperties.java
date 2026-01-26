package com.commander4j.app;

import java.awt.Font;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameUserReportProperties.java
 *
 * Package Name : com.commander4j.app
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


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBUserGroupMembership;
import com.commander4j.db.JDBUserReport;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextArea4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameUserReportProperties class allows a user to create simple
 * reports by entering a SQL SELECT statement which can include a date range
 * criteria.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameUserReportProperties.jpg" >
 *
 * @see com.commander4j.db.JDBUserReport JDBUserReport
 * @see com.commander4j.app.JInternalFrameUserReportAdmin
 *      JInternalFrameUserReportAdmin
 * @see com.commander4j.app.JDialogShiftProperties JDialogShiftProperties
 */
public class JInternalFrameUserReportProperties extends JInternalFrame
{

	private JButton4j button4j_GroupLookup = new JButton4j(Common.icon_lookup_16x16);
	private JButton4j button4j_ModuleID = new JButton4j((Icon) null);
	private JButton4j button4j_UserLookup = new JButton4j(Common.icon_lookup_16x16);
	private JDateControl domDateFrom = new JDateControl();
	private JDateControl domDateTo = new JDateControl();
	private JCalendarButton button_CalendardomDateFrom = new JCalendarButton(domDateFrom);
	private JCalendarButton button_CalendardomDateTo = new JCalendarButton(domDateTo);
	private JCheckBox4j chckbxEmailEnabled = new JCheckBox4j();
	private JCheckBox4j chckbxEmailPrompt = new JCheckBox4j();
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("Enabled");
	private JCheckBox4j checkBoxDateParameters = new JCheckBox4j("Date Parameters Required");
	private JCheckBox4j checkBoxPreview = new JCheckBox4j("Preview");
	private JCheckBox4j checkBoxSaveAs = new JCheckBox4j("Save As");
	private JCheckBox4j checkBox_Private = new JCheckBox4j("");
	private JComboBox4j<String> comboBox4j_Destination = new JComboBox4j<String>();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDBUserReport ur = new JDBUserReport(Common.selectedHostID, Common.sessionID);
	private JLabel4j_status jStatusBar = new JLabel4j_status();
	private JLabel4j_std label4jEnd;
	private JLabel4j_std label4j_std_GroupID = new JLabel4j_std();
	private JLabel4j_std label4j_std_Report_Path = new JLabel4j_std();
	private JLabel4j_std label4j_std_UserID = new JLabel4j_std();
	private JLabel4j_std lblStart;
	private JSpinner4j spinner_Sequence = new JSpinner4j();
	private JTextArea4j textArea_SQL = new JTextArea4j();
	private JTextField4j textField4j_Description = new JTextField4j();
	private JTextField4j textField4j_EmailAddresses = new JTextField4j();
	private JTextField4j textField4j_GroupID = new JTextField4j();
	private JTextField4j textField4j_ModuleID = new JTextField4j();
	private JTextField4j textField4j_ReportID = new JTextField4j();
	private JTextField4j textField4j_SavePath = new JTextField4j();
	private JTextField4j textField4j_UserID = new JTextField4j();
	private static final long serialVersionUID = 1L;

	public JInternalFrameUserReportProperties(String id)
	{
		setVisible(true);
		this.setClosable(true);
		this.setIconifiable(true);
		setBounds(100, 100, 752, 685);
		getContentPane().setLayout(null);

		JDesktopPane4j desktopPane = new JDesktopPane4j();
		desktopPane.setBounds(0, 0, 746, 663);

		getContentPane().add(desktopPane);

		JButton4j btn4j_Save = new JButton4j(Common.icon_update_16x16);
		btn4j_Save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				saveReportProperties(textField4j_ReportID.getText(), true);
			}
		});

		btn4j_Save.setText(lang.get("btn_Save"));
		btn4j_Save.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_REPORT_EDIT"));
		btn4j_Save.setMnemonic('0');
		btn4j_Save.setBounds(20, 584, 126, 32);
		desktopPane.add(btn4j_Save);

		JButton4j btn4j_Help = new JButton4j(Common.icon_help_16x16);
		btn4j_Help.setText(lang.get("btn_Help"));
		btn4j_Help.setMnemonic('0');
		btn4j_Help.setBounds(458, 584, 126, 32);
		desktopPane.add(btn4j_Help);

		JButton4j btn4j_Close = new JButton4j(Common.icon_close_16x16);
		btn4j_Close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				dispose();
			}
		});
		btn4j_Close.setText(lang.get("btn_Close"));
		btn4j_Close.setMnemonic('0');
		btn4j_Close.setBounds(604, 584, 126, 32);
		desktopPane.add(btn4j_Close);

		JLabel4j_std label4j_std_ReportID = new JLabel4j_std();
		label4j_std_ReportID.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_ReportID.setText(lang.get("lbl_Report_ID"));
		label4j_std_ReportID.setBounds(6, 8, 97, 22);
		desktopPane.add(label4j_std_ReportID);

		JLabel4j_std label4j_std_Description = new JLabel4j_std();
		label4j_std_Description.setText(lang.get("lbl_Description"));
		label4j_std_Description.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_Description.setBounds(6, 72, 97, 22);
		desktopPane.add(label4j_std_Description);
		textField4j_ReportID.setEnabled(false);
		textField4j_ReportID.setCaretPosition(0);
		textField4j_ReportID.setBounds(117, 8, 119, 22);
		desktopPane.add(textField4j_ReportID);
		textField4j_Description.setCaretPosition(0);
		textField4j_Description.setBounds(117, 72, 286, 22);
		desktopPane.add(textField4j_Description);

		JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(spinner_Sequence);
		spinner_Sequence.setEditor(ne);
		spinner_Sequence.setBounds(354, 40, 49, 22);

		desktopPane.add(spinner_Sequence);

		JLabel4j_std label4j_std_Sequence = new JLabel4j_std();
		label4j_std_Sequence.setText(lang.get("lbl_Sequence_ID"));
		label4j_std_Sequence.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_Sequence.setBounds(243, 40, 97, 22);
		desktopPane.add(label4j_std_Sequence);

		JScrollPane4j scrollPane = new JScrollPane4j();
		scrollPane.setBounds(117, 175, 613, 170);
		desktopPane.add(scrollPane);
		textArea_SQL.setLocation(117, 0);
		textArea_SQL.setFont(new Font("Monospaced", Font.PLAIN, 12));
		scrollPane.setViewportView(textArea_SQL);

		JLabel4j_std label4j_std_SQL = new JLabel4j_std();
		label4j_std_SQL.setText(lang.get("lbl_SQL"));
		label4j_std_SQL.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_SQL.setBounds(6, 175, 97, 22);
		desktopPane.add(label4j_std_SQL);

		comboBox4j_Destination.setModel(new DefaultComboBoxModel<String>(new String[]
		{ "ACCESS", "CSV", "EXCEL", "JASPER_REPORTS", "PDF", "SYSTEM" }));
		comboBox4j_Destination.setBounds(117, 362, 198, 22);
		desktopPane.add(comboBox4j_Destination);
		comboBox4j_Destination.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent arg0)
			{
				if (arg0.getStateChange() == ItemEvent.SELECTED)
				{
					setDestinationButtons();
				}
			}
		});

		JLabel4j_std label4j_std_Destination = new JLabel4j_std();
		label4j_std_Destination.setText(lang.get("lbl_Output"));
		label4j_std_Destination.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_Destination.setBounds(6, 362, 97, 22);
		desktopPane.add(label4j_std_Destination);

		chckbxEnabled.setBounds(112, 40, 128, 22);
		chckbxEnabled.setText(lang.get("lbl_Interface_Enabled"));
		desktopPane.add(chckbxEnabled);

		label4j_std_Report_Path.setText(lang.get("lbl_Module_ID"));
		label4j_std_Report_Path.setHorizontalAlignment(SwingConstants.RIGHT);
		label4j_std_Report_Path.setBounds(319, 362, 119, 22);
		desktopPane.add(label4j_std_Report_Path);
		textField4j_ModuleID.setCaretPosition(0);
		textField4j_ModuleID.setBounds(452, 362, 254, 22);
		desktopPane.add(textField4j_ModuleID);

		button4j_ModuleID.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{

				JLaunchLookup.dlgAutoExec = false;
				JLaunchLookup.dlgCriteriaDefault = "REPORT";
				if (JLaunchLookup.modules())
				{
					textField4j_ModuleID.setText(JLaunchLookup.dlgResult);
				}

			}
		});

		button4j_ModuleID.setText("..");
		button4j_ModuleID.setBounds(705, 362, 21, 22);
		desktopPane.add(button4j_ModuleID);

		jStatusBar.setBounds(8, 625, 721, 21);
		desktopPane.add(jStatusBar);

		JButton4j button4j_Run = new JButton4j(Common.icon_execute_16x16);
		button4j_Run.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				runReport(textField4j_ReportID.getText());
			}
		});
		button4j_Run.setText(lang.get("btn_Run"));
		button4j_Run.setMnemonic('0');
		button4j_Run.setBounds(166, 584, 126, 32);
		desktopPane.add(button4j_Run);

		JButton4j button4j_ViewSchema = new JButton4j(Common.icon_help_16x16);
		button4j_ViewSchema.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JLaunchMenu.runForm("FRM_ADMIN_SCHEMA_BROWSE");
			}
		});
		button4j_ViewSchema.setText(lang.get("btn_Schema"));
		button4j_ViewSchema.setMnemonic('0');
		button4j_ViewSchema.setBounds(312, 584, 126, 32);
		desktopPane.add(button4j_ViewSchema);

		JPanel4j panel = new JPanel4j();
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.setBounds(415, 10, 315, 104);
		desktopPane.add(panel);
		panel.setLayout(null);
		checkBox_Private.setBounds(95, 5, 186, 22);
		checkBox_Private.setText(lang.get("lbl_Private"));
		panel.add(checkBox_Private);
		label4j_std_UserID.setBounds(6, 37, 84, 22);
		panel.add(label4j_std_UserID);

		label4j_std_UserID.setText(lang.get("lbl_User_ID"));
		label4j_std_UserID.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_GroupID.setBounds(6, 69, 84, 22);
		panel.add(label4j_std_GroupID);

		label4j_std_GroupID.setText(lang.get("lbl_Group_ID"));
		label4j_std_GroupID.setHorizontalAlignment(SwingConstants.TRAILING);
		textField4j_GroupID.setBounds(95, 69, 186, 22);
		panel.add(textField4j_GroupID);
		textField4j_GroupID.setCaretPosition(0);
		textField4j_UserID.setBounds(95, 37, 186, 22);
		panel.add(textField4j_UserID);
		textField4j_UserID.setCaretPosition(0);
		button4j_UserLookup.setBounds(279, 37, 21, 22);
		panel.add(button4j_UserLookup);
		button4j_GroupLookup.setBounds(279, 69, 21, 22);
		panel.add(button4j_GroupLookup);
		checkBoxDateParameters.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				setDateRangeButtons();
			}
		});

		checkBoxDateParameters.setSelected(true);
		checkBoxDateParameters.setText(lang.get("lbl_Date_Params_Reqd"));

		checkBoxDateParameters.setBounds(112, 104, 210, 22);
		desktopPane.add(checkBoxDateParameters);

		checkBoxPreview.setText(lang.get("lbl_View_Output"));
		checkBoxPreview.setSelected(true);
		checkBoxPreview.setBounds(117, 396, 248, 22);
		desktopPane.add(checkBoxPreview);

		checkBoxSaveAs.setText(lang.get("lbl_Save_As"));
		checkBoxSaveAs.setSelected(true);
		checkBoxSaveAs.setBounds(458, 396, 272, 22);
		desktopPane.add(checkBoxSaveAs);

		JLabel4j_std label4j_std_Save_Path = new JLabel4j_std();
		label4j_std_Save_Path.setText(lang.get("lbl_Save_Path"));
		label4j_std_Save_Path.setBounds(120, 428, 166, 22);
		desktopPane.add(label4j_std_Save_Path);

		textField4j_SavePath.setText("");
		textField4j_SavePath.setCaretPosition(0);
		textField4j_SavePath.setBounds(117, 450, 589, 22);
		desktopPane.add(textField4j_SavePath);

		final JButton4j button4j_SavePath = new JButton4j((Icon) null);
		button4j_SavePath.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser loadDir = new JFileChooser();

				try
				{
					// Set the current directory
					File f = new File(new File("").getCanonicalPath());
					loadDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					loadDir.setCurrentDirectory(f);
					loadDir.setSelectedFile(new File(textField4j_SavePath.getText()));

					if (loadDir.showOpenDialog(button4j_SavePath) == JFileChooser.APPROVE_OPTION)
					{
						File selectedFile;
						selectedFile = loadDir.getSelectedFile();

						if (selectedFile != null)
						{
							if (textField4j_SavePath.getText().compareTo(selectedFile.getCanonicalPath()) != 0)
							{
								textField4j_SavePath.setText(selectedFile.getCanonicalPath());
							}
						}
					}

				} catch (Exception ex)
				{
				}
			}
		});
		button4j_SavePath.setText("..");
		button4j_SavePath.setBounds(705, 450, 21, 22);
		desktopPane.add(button4j_SavePath);

		domDateFrom.setBounds(117, 136, 120, 22);
		desktopPane.add(domDateFrom);

		button_CalendardomDateFrom.setBounds(238, 137, 21, 21);
		desktopPane.add(button_CalendardomDateFrom);

		domDateTo.setBounds(354, 136, 120, 22);
		desktopPane.add(domDateTo);

		button_CalendardomDateTo.setBounds(475, 137, 21, 21);
		desktopPane.add(button_CalendardomDateTo);

		lblStart = new JLabel4j_std(lang.get("web_From"));
		lblStart.setHorizontalAlignment(SwingConstants.TRAILING);
		lblStart.setBounds(6, 136, 97, 22);
		desktopPane.add(lblStart);

		label4jEnd = new JLabel4j_std(lang.get("web_To"));
		label4jEnd.setHorizontalAlignment(SwingConstants.TRAILING);
		label4jEnd.setBounds(277, 136, 63, 22);
		desktopPane.add(label4jEnd);
		chckbxEmailEnabled.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				setEmailButtons();
				if (chckbxEmailEnabled.isSelected())
				{
					if (textField4j_EmailAddresses.getText().equals(""))
					{
						textField4j_EmailAddresses.setText(Common.userList.getUser(Common.sessionID).getEmailAddress());
					}
				}
			}
		});

		chckbxEmailEnabled.setText(lang.get("lbl_Email_Output"));
		chckbxEmailEnabled.setSelected(true);
		chckbxEmailEnabled.setBounds(117, 482, 248, 22);
		desktopPane.add(chckbxEmailEnabled);

		chckbxEmailPrompt.setText(lang.get("lbl_Email_Prompt"));
		chckbxEmailPrompt.setSelected(true);
		chckbxEmailPrompt.setBounds(458, 482, 248, 22);
		desktopPane.add(chckbxEmailPrompt);

		textField4j_EmailAddresses.setText("");
		textField4j_EmailAddresses.setCaretPosition(0);
		textField4j_EmailAddresses.setBounds(117, 537, 589, 22);
		desktopPane.add(textField4j_EmailAddresses);

		JLabel4j_std label4j_std = new JLabel4j_std();
		label4j_std.setText(lang.get("lbl_Email_Addresses"));
		label4j_std.setBounds(120, 514, 166, 22);
		desktopPane.add(label4j_std);

		button4j_GroupLookup.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JLaunchLookup.dlgAutoExec = true;
				JLaunchLookup.dlgCriteriaDefault = "";

				if (JLaunchLookup.groups())
				{
					textField4j_GroupID.setText(JLaunchLookup.dlgResult);
				}
			}
		});

		button4j_UserLookup.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JLaunchLookup.dlgAutoExec = true;
				JLaunchLookup.dlgCriteriaDefault = "";

				if (JLaunchLookup.users())
				{
					textField4j_UserID.setText(JLaunchLookup.dlgResult);
				}
			}
		});
		checkBox_Private.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{

				setPrivateButtonState();
			}
		});

		loadReportProperties(id);
		setPrivateButtonState();
		setDestinationButtons();
		setDateRangeButtons();
		setEmailButtons();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(btn4j_Help, JUtility.getHelpSetIDforModule("FRM_ADMIN_USER_REPORT_PROP"));
	}

	private void setDestinationButtons()
	{
		if (comboBox4j_Destination.getSelectedItem().toString().equals("JASPER_REPORTS") | comboBox4j_Destination.getSelectedItem().toString().equals("SYSTEM"))
		{
			label4j_std_Report_Path.setEnabled(true);
			textField4j_ModuleID.setEnabled(true);
			button4j_ModuleID.setEnabled(true);
			checkBoxPreview.setSelected(false);
			checkBoxPreview.setEnabled(false);
			checkBoxSaveAs.setSelected(false);
			checkBoxSaveAs.setEnabled(false);
			chckbxEmailEnabled.setEnabled(false);
			if (chckbxEmailEnabled.isSelected())
			{
				chckbxEmailEnabled.setSelected(false);
				setEmailButtons();
			}
			if (comboBox4j_Destination.getSelectedItem().toString().equals("SYSTEM"))
			{
				checkBoxDateParameters.setSelected(false);
				setDateRangeButtons();
			}
		} else
		{
			chckbxEmailEnabled.setEnabled(true);
			label4j_std_Report_Path.setEnabled(false);
			textField4j_ModuleID.setEnabled(false);
			button4j_ModuleID.setEnabled(false);
			checkBoxPreview.setEnabled(true);
			checkBoxSaveAs.setEnabled(true);
		}
	}

	private void setDateRangeButtons()
	{
		domDateFrom.setEnabled(checkBoxDateParameters.isSelected());
		domDateTo.setEnabled(checkBoxDateParameters.isSelected());
		button_CalendardomDateFrom.setEnabled(checkBoxDateParameters.isSelected());
		button_CalendardomDateTo.setEnabled(checkBoxDateParameters.isSelected());
		lblStart.setEnabled(checkBoxDateParameters.isSelected());
		label4jEnd.setEnabled(checkBoxDateParameters.isSelected());
	}

	private void setPrivateButtonState()
	{
		textField4j_UserID.setEnabled(checkBox_Private.isSelected());
		button4j_UserLookup.setEnabled(checkBox_Private.isSelected());
		textField4j_GroupID.setEnabled(checkBox_Private.isSelected());
		button4j_GroupLookup.setEnabled(checkBox_Private.isSelected());
	}

	private void setEmailButtons()
	{
		chckbxEmailPrompt.setEnabled(chckbxEmailEnabled.isSelected());
		textField4j_EmailAddresses.setEnabled(chckbxEmailEnabled.isSelected());
	}

	private boolean isValidReport(String id)
	{
		boolean result = true;
		String reasonInvalid = "";
		String illegalSQL[] =
		{ "DELETE", "INSERT", "DROP", "TRUNCATE", "GRANT ", "REVOKE", "ALTER", "DISABLE", "ENABLE", "CALL", "MERGE", "RENAME", "COMMIT", "ROLLBACK", "TRANSACTION " };

		if (result)
		{
			String examine = textArea_SQL.getText().toUpperCase();
			for (int x = 0; x < illegalSQL.length; x++)
			{
				if (examine.contains(illegalSQL[x]))
				{
					result = false;
					reasonInvalid = "Illegal DDL command " + illegalSQL[x] + " found in query";
					break;
				}
			}
		}

		if (result)
		{
			if (textArea_SQL.getText().toUpperCase().contains("SYS_USERS"))
			{
				result = false;
				reasonInvalid = "reference to SYS_USERS found in query";
			}
		}

		if (result)
		{
			if (textArea_SQL.getText().toUpperCase().contains("SYS_GROUP"))
			{
				result = false;
				reasonInvalid = "reference to SYS_GROUP found in query";
			}
		}

		if (result)
		{
			if (textArea_SQL.getText().toLowerCase().contains("view_permissions"))
			{
				result = false;
				reasonInvalid = "reference to view_permissions found in query";
			}
		}

		if (result)
		{
			if (checkBox_Private.isSelected())
			{
				if (textField4j_UserID.getText().equals("") == false)
				{
					if (textField4j_UserID.getText().equals(Common.userList.getUser(Common.sessionID).getUserId()) == false)
					{
						result = false;
						reasonInvalid = "private User ID must be [" + Common.userList.getUser(Common.sessionID).getUserId() + "] or blank.";
					}
				}
			}
		}

		if (result)
		{
			if (checkBox_Private.isSelected())
			{
				if (textField4j_GroupID.getText().equals("") == false)
				{
					JDBUserGroupMembership ugm = new JDBUserGroupMembership(Common.selectedHostID, Common.sessionID);
					ugm.setUserId(Common.userList.getUser(Common.sessionID).getUserId());
					ugm.setGroupId(textField4j_GroupID.getText());
					if (ugm.isValidUserGroupMembership() == false)
					{
						result = false;
						reasonInvalid = "user [" + Common.userList.getUser(Common.sessionID).getUserId() + "] must be a member of the specified group [" + textField4j_GroupID.getText() + "]";
					}
				}
			}
		}

		if (result)
		{
			if (checkBox_Private.isSelected())
			{
				if (textField4j_UserID.getText().equals("") && textField4j_GroupID.getText().equals(""))
				{
					result = false;
					reasonInvalid = "private reports require user or group to be defined";
				}
			}
		}
		if (comboBox4j_Destination.getSelectedItem().toString().equals("SYSTEM") == false)
		{
			if (result)
			{
				if (checkBoxDateParameters.isSelected())
				{
					if (textArea_SQL.getText().contains("?") == false)
					{
						result = false;
						reasonInvalid = "date params checkbox selected but no placeholders found in SQL";
					}
				}
			}

			if (result)
			{

				if (textArea_SQL.getText().contains("?") == true)
				{
					if (checkBoxDateParameters.isSelected() == false)
					{
						result = false;
						reasonInvalid = "parameter placeholders found in SQL but date params checkbox not checked";
					}
				}
			}
		}

		if (result)
		{
			if (comboBox4j_Destination.getSelectedItem().toString().equals("JASPER_REPORTS") | comboBox4j_Destination.getSelectedItem().toString().equals("PDF"))
			{
				if (textField4j_ModuleID.getText().equals(""))
				{
					result = false;
					reasonInvalid = "output is JASPER_REPORTS/PDF but no report module ID specified.";
				} else
				{
					mod.setModuleId(textField4j_ModuleID.getText());
					if (mod.getModuleProperties())
					{
						if (mod.getType().equals("REPORT") == false)
						{
							result = false;
							reasonInvalid = "module id [" + textField4j_ModuleID.getText() + "] is not a report.";
						}
					} else
					{
						result = false;
						reasonInvalid = "invalid module id [" + textField4j_ModuleID.getText() + "]";
					}
				}
			}
		}

		if (result == false)
		{
			jStatusBar.setText("Invalid Report - [" + reasonInvalid + "]");
		}

		return result;
	}

	private void runReport(String id)
	{
		saveReportProperties(id, false);
		ur.setReportID(id);
		ur.setDescription(textField4j_Description.getText());
		ur.setSequence(Integer.valueOf(spinner_Sequence.getValue().toString()));
		ur.setSQL(textArea_SQL.getText());
		ur.setModuleID(textField4j_ModuleID.getText());
		ur.setUserID(textField4j_UserID.getText().toUpperCase());
		ur.setGroupID(textField4j_GroupID.getText().toUpperCase());
		ur.setEnabled(chckbxEnabled.isSelected());
		ur.setPrivate(checkBox_Private.isSelected());
		ur.setDestination(comboBox4j_Destination.getSelectedItem().toString());
		ur.setPromptSaveAs(checkBoxSaveAs.isSelected());
		ur.setPreview(checkBoxPreview.isSelected());
		ur.setSavePath(textField4j_SavePath.getText());

		ur.setParamFromDate(JUtility.getTimestampFromDate(domDateFrom.getDate()));
		ur.setParamToDate(JUtility.getTimestampFromDate(domDateTo.getDate()));

		if (comboBox4j_Destination.getSelectedItem().toString().equals("SYSTEM") == false)
		{
			jStatusBar.setText("Please wait....");
			if (ur.runReport() == false)
			{
				JUtility.errorBeep();
				jStatusBar.setText(ur.getErrorMessage());
				JOptionPane.showMessageDialog(Common.mainForm, ur.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
			} else
			{
				jStatusBar.setText("Created : " + ur.getExportFilename());
			}
		} else
		{
			JUtility.errorBeep();
			jStatusBar.setText("");
			JOptionPane.showMessageDialog(Common.mainForm, "SYSTEM reports cannot be run interactively.", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
		}

	}

	private void loadReportProperties(String id)
	{
		ur.getUserReportProperties(id);
		ur.setReportID(id);
		textField4j_ReportID.setText(ur.getReportID());
		textField4j_Description.setText(ur.getDescription());
		spinner_Sequence.setValue(ur.getSequence());
		textArea_SQL.setText(ur.getSQL());
		textArea_SQL.setCaretPosition(0);
		textField4j_ModuleID.setText(ur.getModuleID());
		textField4j_UserID.setText(ur.getUserID());
		textField4j_GroupID.setText(ur.getGroupID());
		chckbxEnabled.setSelected(ur.isEnabled());
		checkBoxDateParameters.setSelected(ur.isParamDateRequired());
		checkBox_Private.setSelected(ur.isPrivate());
		comboBox4j_Destination.setSelectedItem(ur.getDestination());
		checkBoxPreview.setSelected(ur.isPreviewRequired());
		checkBoxSaveAs.setSelected(ur.isPromptSaveAsRequired());
		textField4j_SavePath.setText(ur.getSavePath());
		jStatusBar.setText("Report [" + id + "] loaded.");
		chckbxEmailEnabled.setSelected(ur.isEmailEnabled());
		chckbxEmailPrompt.setSelected(ur.isEmailPromptEnabled());
		textField4j_EmailAddresses.setText(ur.getEmailAddresses());
	}

	private void saveReportProperties(String id, boolean realSave)
	{
		if (isValidReport(id))
		{
			ur.setReportID(id);
			ur.setDescription(textField4j_Description.getText());
			ur.setSequence(Integer.valueOf(spinner_Sequence.getValue().toString()));
			ur.setSQL(textArea_SQL.getText());
			ur.setModuleID(textField4j_ModuleID.getText());
			ur.setUserID(textField4j_UserID.getText().toUpperCase());
			ur.setGroupID(textField4j_GroupID.getText().toUpperCase());
			ur.setEnabled(chckbxEnabled.isSelected());
			ur.setParamDateRangeRequired(checkBoxDateParameters.isSelected());
			ur.setPrivate(checkBox_Private.isSelected());
			ur.setDestination(comboBox4j_Destination.getSelectedItem().toString());
			ur.setPreview(checkBoxPreview.isSelected());
			ur.setPromptSaveAs(checkBoxSaveAs.isSelected());
			ur.setSavePath(textField4j_SavePath.getText());
			ur.setEmailEnabled(chckbxEmailEnabled.isSelected());
			ur.setEmailPromptEnabled(chckbxEmailPrompt.isSelected());
			ur.setEmailAddresses(textField4j_EmailAddresses.getText());
			if (realSave)
			{
				if (ur.isValidUserReport(id) == false)
				{
					ur.create();
				}
				ur.update();
				jStatusBar.setText("Report [" + id + "] saved.");
			}

		}
	}
}
