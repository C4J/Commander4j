package com.commander4j.interfaces;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameInterfaceProperties.java
 * 
 * Package Name : com.commander4j.interfaces
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameInterfaceProperties is used to modify an interface. Changes are stored in a table SYS_INTERFACE
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameInterfaceAdmin.jpg" >
 * 
 * @see com.commander4j.interfaces.JInternalFrameInterfaceAdmin JInternalFrameInterfaceAdmin
 * @see com.commander4j.db.JDBInterface JDBInterface
 * @see com.commander4j.db.JDBInterfaceLog JDBInterfaceLog
 */
public class JInternalFrameInterfaceProperties extends JInternalFrame
{
	private JLabel jLabel2_2;
	private JComboBox4j<String> comboBoxDevice = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxFormat = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxUOMConversion = new JComboBox4j<String>();
	private JLabel jLabel2_1;
	private JButton jButtonExecDirChooser;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton jButtonCancel;
	private JButton jButtonHelp;
	private JButton jButtonUpdate;
	private String ltype;
	private String ldirection;
	private JDBInterface interfaceConfig = new JDBInterface(Common.selectedHostID, Common.sessionID);
	private JTextField jTextFieldPath;
	private JLabel jLabel2;
	private JTextField jComboBoxInterfaceDirection;
	private JLabel jLabel3;
	private JTextField jTextFieldInterfaceType;
	private JLabel jLabel1;
	final JCheckBox4j checkBox = new JCheckBox4j();
	private JTextField textField_addresses;
	private JCheckBox4j checkBox_success = new JCheckBox4j();
	private JCheckBox4j checkBox_warning = new JCheckBox4j();
	private JCheckBox4j checkBox_error = new JCheckBox4j();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JInternalFrameInterfaceProperties()
	{
		super();
		setIconifiable(true);
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_BATCH_EDIT"));
		{
			checkBox_success.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonUpdate.setEnabled(true);
				}
			});

			checkBox_success.setBounds(234, 222, 21, 24);
			checkBox_success.setEnabled(true);
			checkBox_success.setBackground(Color.WHITE);
			jDesktopPane1.add(checkBox_success);
		}
		{
			checkBox_warning.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonUpdate.setEnabled(true);
				}
			});

			checkBox_warning.setBounds(399, 222, 21, 24);
			checkBox_warning.setEnabled(true);
			checkBox_warning.setBackground(Color.WHITE);
			jDesktopPane1.add(checkBox_warning);
		}
		{
			checkBox_error.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonUpdate.setEnabled(true);
				}
			});

			checkBox_error.setBounds(566, 222, 21, 24);
			checkBox_error.setEnabled(true);
			checkBox_error.setBackground(Color.WHITE);
			jDesktopPane1.add(checkBox_error);
		}
		{
			JLabel lblEmailSuccess = new JLabel();
			lblEmailSuccess.setBounds(136, 225, 96, 21);
			lblEmailSuccess.setText(lang.get("lbl_Email_Success"));
			lblEmailSuccess.setHorizontalAlignment(SwingConstants.TRAILING);
			lblEmailSuccess.setFont(Common.font_std);
			jDesktopPane1.add(lblEmailSuccess);
		}
		{
			JLabel lblEmailWarning = new JLabel();
			lblEmailWarning.setBounds(290, 225, 102, 21);
			lblEmailWarning.setText(lang.get("lbl_Email_Warning"));
			lblEmailWarning.setHorizontalAlignment(SwingConstants.TRAILING);
			lblEmailWarning.setFont(Common.font_std);
			jDesktopPane1.add(lblEmailWarning);
		}
		{
			JLabel lblEmailError = new JLabel();
			lblEmailError.setBounds(451, 225, 112, 21);
			lblEmailError.setText(lang.get("lbl_Email_Error"));
			lblEmailError.setHorizontalAlignment(SwingConstants.TRAILING);
			lblEmailError.setFont(Common.font_std);
			jDesktopPane1.add(lblEmailError);
		}
		{
			JLabel lblEmailNotifications = new JLabel();
			lblEmailNotifications.setBounds(12, 225, 102, 21);
			lblEmailNotifications.setText(lang.get("lbl_Email_Notifications"));

			lblEmailNotifications.setHorizontalAlignment(SwingConstants.TRAILING);
			lblEmailNotifications.setFont(Common.font_std);
			jDesktopPane1.add(lblEmailNotifications);
		}
		{
			textField_addresses = new JTextField(JDBInterface.field_email_addresses);
			textField_addresses.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					jButtonUpdate.setEnabled(true);
				}
			});
			textField_addresses.setText((String) null);
			textField_addresses.setFont(Common.font_std);
			textField_addresses.setEnabled(true);
			textField_addresses.setDisabledTextColor(Color.BLACK);
			textField_addresses.setBounds(126, 255, 557, 21);
			jDesktopPane1.add(textField_addresses);
		}
		{
			JLabel lblEmailAddresses = new JLabel();
			lblEmailAddresses.setText(lang.get("lbl_Email_Addresses"));
			lblEmailAddresses.setHorizontalAlignment(SwingConstants.TRAILING);
			lblEmailAddresses.setFont(Common.font_std);
			lblEmailAddresses.setBounds(12, 257, 102, 21);
			jDesktopPane1.add(lblEmailAddresses);
		}
		{
			comboBoxUOMConversion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonUpdate.setEnabled(true);
				}
			});

			comboBoxUOMConversion.setFont(Common.font_combo);
			comboBoxUOMConversion.setBounds(126, 162, 159, 23);
			jDesktopPane1.add(comboBoxUOMConversion);
		}
		{
			JLabel lblUomConversion = new JLabel();
			lblUomConversion.setText(lang.get("lbl_UOM_Conversion"));
			lblUomConversion.setHorizontalAlignment(SwingConstants.TRAILING);
			lblUomConversion.setFont(Common.font_std);
			lblUomConversion.setBounds(12, 163, 102, 21);
			jDesktopPane1.add(lblUomConversion);
		}

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldPath.requestFocus();
			}
		});
	}

	public JInternalFrameInterfaceProperties(String interfacetype, String interfacedirection)
	{
		this();
		ltype = interfacetype;
		ldirection = interfacedirection;
		jTextFieldInterfaceType.setText(ltype);

		interfaceConfig.setInterfaceType(ltype);
		interfaceConfig.setInterfaceDirection(ldirection);
		jTextFieldInterfaceType.setText(interfacetype);
		jComboBoxInterfaceDirection.setText(interfacedirection);
		comboBoxDevice.setModel(new DefaultComboBoxModel<String>(new String[] { "Disk", "Email", "FTP", "ActiveMQ" }));

		comboBoxFormat.setModel(new DefaultComboBoxModel<String>(new String[] { "XML", "EANCOM", "IDOC", "MPS", "CSV" }));

		comboBoxUOMConversion.setModel(new DefaultComboBoxModel<String>(new String[] { Common.UOM_Convert_Internal_to_ISO, Common.UOM_Convert_Internal_to_Local, Common.UOM_Convert_None, Common.UOM_Convert_ISO_to_INTERNAL, Common.UOM_Convert_ISO_to_Local,
				Common.UOM_Convert_Local_to_ISO, Common.UOM_Convert_Local_to_Internal }));

		if (interfaceConfig.getInterfaceProperties())
		{
			jTextFieldPath.setText(interfaceConfig.getPath());
			checkBox.setSelected(interfaceConfig.isEnabled());
			comboBoxDevice.setSelectedItem(interfaceConfig.getDevice());
			comboBoxFormat.setSelectedItem(interfaceConfig.getFormat());
			comboBoxUOMConversion.setSelectedItem(interfaceConfig.getUOMConversion());
			checkBox_success.setSelected(interfaceConfig.getEmailSuccess());
			checkBox_warning.setSelected(interfaceConfig.getEmailWarning());
			checkBox_error.setSelected(interfaceConfig.getEmailError());
			textField_addresses.setText(interfaceConfig.getEmailAddresses());
			jButtonUpdate.setEnabled(false);
		}
		else
		{
			jButtonUpdate.setEnabled(true);
		}

	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(358, 207));
			this.setBounds(0, 0, 720+Common.LFAdjustWidth, 373+Common.LFAdjustHeight);
			setVisible(true);
			this.setTitle("Interface Properties");
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(350, 182));
				jDesktopPane1.setLayout(null);
				{
					jButtonUpdate = new JButton(Common.icon_update);
					jButtonUpdate.setBounds(159, 288, 112, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setFont(Common.font_btn);
					jButtonUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

							interfaceConfig.setPath(jTextFieldPath.getText());
							interfaceConfig.setEnabled(checkBox.isSelected());
							interfaceConfig.setDevice(comboBoxDevice.getSelectedItem().toString());
							interfaceConfig.setFormat(comboBoxFormat.getSelectedItem().toString());
							interfaceConfig.setUOMConversion(comboBoxUOMConversion.getSelectedItem().toString());
							interfaceConfig.setEmailError(checkBox_error.isSelected());
							interfaceConfig.setEmailWarning(checkBox_warning.isSelected());
							interfaceConfig.setEmailSuccess(checkBox_success.isSelected());
							interfaceConfig.setEmailAddresses(textField_addresses.getText());

							if (interfaceConfig.isValidInterface())
							{
								interfaceConfig.update();
							}
							else
							{
								interfaceConfig.create();
								interfaceConfig.update();
							}
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton(Common.icon_help);
					jButtonHelp.setBounds(273, 288, 112, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setFont(Common.font_btn);
				}
				{
					jButtonCancel = new JButton(Common.icon_close);
					jButtonCancel.setBounds(387, 288, 112, 32);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setFont(Common.font_btn);
					jButtonCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jLabel1 = new JLabel();
					jLabel1.setBounds(12, 9, 102, 21);
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Interface_Type"));
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1.setFont(Common.font_std);
				}
				{
					jTextFieldInterfaceType = new JTextField(JDBInterface.field_interface_type);
					jTextFieldInterfaceType.setBounds(126, 9, 299, 21);
					jDesktopPane1.add(jTextFieldInterfaceType);
					jTextFieldInterfaceType.setFont(Common.font_std);
					jTextFieldInterfaceType.setText(ltype);
					jTextFieldInterfaceType.setEnabled(false);
					jTextFieldInterfaceType.setEditable(false);
					jTextFieldInterfaceType.setDisabledTextColor(Common.color_text_disabled);
				}
				{
					jLabel3 = new JLabel();
					jLabel3.setBounds(12, 194, 102, 21);
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Interface_Path"));
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel3.setFont(Common.font_std);
				}
				{
					jTextFieldPath = new JTextField(JDBInterface.field_path);
					jTextFieldPath.setBounds(126, 192, 544, 21);
					jTextFieldPath.addKeyListener(new KeyAdapter() {
						public void keyTyped(final KeyEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					jDesktopPane1.add(jTextFieldPath);
					jTextFieldPath.setFont(Common.font_std);
					jTextFieldPath.setText(ldirection);
					jTextFieldPath.setEnabled(true);
					jTextFieldPath.setDisabledTextColor(Common.color_text_disabled);
				}
				{
					jComboBoxInterfaceDirection = new JTextField(JDBInterface.field_interface_direction);
					jComboBoxInterfaceDirection.setBounds(126, 39, 126, 21);
					jDesktopPane1.add(jComboBoxInterfaceDirection);
					jComboBoxInterfaceDirection.setFont(Common.font_combo);
					jComboBoxInterfaceDirection.setEnabled(false);
					jComboBoxInterfaceDirection.setEditable(false);

				}
				{
					jLabel2 = new JLabel();
					jLabel2.setBounds(12, 40, 102, 21);
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Interface_Direction"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2.setFont(Common.font_std);
				}
				{
					comboBoxFormat.setBounds(126, 132, 126, 23);
					comboBoxFormat.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});

					comboBoxFormat.setFont(Common.font_combo);
					jDesktopPane1.add(comboBoxFormat);
				}
				{
					JLabel lblFormat = new JLabel();
					lblFormat.setBounds(12, 132, 102, 21);
					lblFormat.setText(lang.get("lbl_Interface_Format"));
					lblFormat.setHorizontalAlignment(SwingConstants.TRAILING);
					lblFormat.setFont(Common.font_std);
					jDesktopPane1.add(lblFormat);
				}
				{
					jButtonExecDirChooser = new JButton();
					jButtonExecDirChooser.setBounds(670, 192, 14, 21);
					jButtonExecDirChooser.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							JFileChooser loadDir = new JFileChooser();

							try
							{
								// Set the current directory
								File f = new File(new File("").getCanonicalPath());
								loadDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								loadDir.setCurrentDirectory(f);
								loadDir.setSelectedFile(new File(jTextFieldPath.getText()));

								if (loadDir.showOpenDialog(jButtonExecDirChooser) == JFileChooser.APPROVE_OPTION)
								{
									File selectedFile;
									selectedFile = loadDir.getSelectedFile();

									if (selectedFile != null)
									{
										if (jTextFieldPath.getText().compareTo(selectedFile.getCanonicalPath()) != 0)
										{
											jTextFieldPath.setText(selectedFile.getCanonicalPath());
											jButtonUpdate.setEnabled(true);
										}
									}
								}

							}
							catch (Exception ex)
							{
							}
						}
					});
					jButtonExecDirChooser.setText("..");
					jDesktopPane1.add(jButtonExecDirChooser);
				}

				{
					checkBox.setBounds(126, 69, 21, 24);

					checkBox.setBackground(Color.WHITE);
					checkBox.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					checkBox.setEnabled(true);
					jDesktopPane1.add(checkBox);
				}

				{
					jLabel2_1 = new JLabel();
					jLabel2_1.setBounds(12, 72, 102, 21);
					jLabel2_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2_1.setFont(Common.font_std);
					jLabel2_1.setText(lang.get("lbl_Interface_Enabled"));
					jDesktopPane1.add(jLabel2_1);
				}

				{
					comboBoxDevice.setBounds(126, 102, 126, 23);

					comboBoxDevice.setFont(Common.font_combo);
					comboBoxDevice.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					jDesktopPane1.add(comboBoxDevice);
				}

				{
					jLabel2_2 = new JLabel();
					jLabel2_2.setBounds(12, 102, 102, 21);
					jLabel2_2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2_2.setFont(Common.font_std);
					jLabel2_2.setText(lang.get("lbl_Interface_Device"));
					jDesktopPane1.add(jLabel2_2);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
