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

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameInterfaceProperties is used to modify an interface. Changes are
 * stored in a table SYS_INTERFACE
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameInterfaceAdmin.jpg" >
 *
 * @see com.commander4j.interfaces.JInternalFrameInterfaceAdmin
 *      JInternalFrameInterfaceAdmin
 * @see com.commander4j.db.JDBInterface JDBInterface
 * @see com.commander4j.db.JDBInterfaceLog JDBInterfaceLog
 */
public class JInternalFrameInterfaceProperties extends JInternalFrame
{
	private JLabel4j_std jLabel2_2;
	private JComboBox4j<String> comboBoxDevice = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxFormat = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxUOMConversion = new JComboBox4j<String>();
	private JLabel4j_std jLabel2_1;
	private JButton4j jButtonExecDirChooser;
	private static final long serialVersionUID = 1;
	private JDesktopPane4j jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private String ltype;
	private String ldirection;
	private JDBInterface interfaceConfig = new JDBInterface(Common.selectedHostID, Common.sessionID);
	private JTextField4j jTextFieldPath;
	private JLabel4j_std jLabel2;
	private JTextField4j jComboBoxInterfaceDirection;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldInterfaceType;
	private JLabel4j_std jLabel1;
	final JCheckBox4j checkBox = new JCheckBox4j();
	private JTextField4j textField_addresses;
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

		checkBox_success.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonUpdate.setEnabled(true);
			}
		});

		checkBox_success.setBounds(234, 232, 21, 22);
		checkBox_success.setEnabled(true);
		checkBox_success.setBackground(Color.WHITE);
		jDesktopPane1.add(checkBox_success);

		checkBox_warning.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonUpdate.setEnabled(true);
			}
		});

		checkBox_warning.setBounds(399, 232, 21, 22);
		checkBox_warning.setEnabled(true);
		checkBox_warning.setBackground(Color.WHITE);
		jDesktopPane1.add(checkBox_warning);

		checkBox_error.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonUpdate.setEnabled(true);
			}
		});

		checkBox_error.setBounds(566, 232, 21, 22);
		checkBox_error.setEnabled(true);
		checkBox_error.setBackground(Color.WHITE);
		jDesktopPane1.add(checkBox_error);

		JLabel4j_std lblEmailSuccess = new JLabel4j_std();
		lblEmailSuccess.setBounds(136, 232, 96, 22);
		lblEmailSuccess.setText(lang.get("lbl_Email_Success"));
		lblEmailSuccess.setHorizontalAlignment(SwingConstants.TRAILING);
		jDesktopPane1.add(lblEmailSuccess);

		JLabel4j_std lblEmailWarning = new JLabel4j_std();
		lblEmailWarning.setBounds(290, 232, 102, 22);
		lblEmailWarning.setText(lang.get("lbl_Email_Warning"));
		lblEmailWarning.setHorizontalAlignment(SwingConstants.TRAILING);
		jDesktopPane1.add(lblEmailWarning);

		JLabel4j_std lblEmailError = new JLabel4j_std();
		lblEmailError.setBounds(451, 232, 112, 22);
		lblEmailError.setText(lang.get("lbl_Email_Error"));
		lblEmailError.setHorizontalAlignment(SwingConstants.TRAILING);
		jDesktopPane1.add(lblEmailError);

		JLabel4j_std lblEmailNotifications = new JLabel4j_std();
		lblEmailNotifications.setBounds(12, 232, 102, 22);
		lblEmailNotifications.setText(lang.get("lbl_Email_Notifications"));

		lblEmailNotifications.setHorizontalAlignment(SwingConstants.TRAILING);
		jDesktopPane1.add(lblEmailNotifications);

		textField_addresses = new JTextField4j(JDBInterface.field_email_addresses);
		textField_addresses.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				jButtonUpdate.setEnabled(true);
			}
		});
		textField_addresses.setText((String) null);
		textField_addresses.setEnabled(true);
		textField_addresses.setDisabledTextColor(Color.BLACK);
		textField_addresses.setBounds(126, 264, 557, 22);
		jDesktopPane1.add(textField_addresses);

		JLabel4j_std lblEmailAddresses = new JLabel4j_std();
		lblEmailAddresses.setText(lang.get("lbl_Email_Addresses"));
		lblEmailAddresses.setHorizontalAlignment(SwingConstants.TRAILING);
		lblEmailAddresses.setBounds(12, 264, 102, 22);
		jDesktopPane1.add(lblEmailAddresses);

		comboBoxUOMConversion.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonUpdate.setEnabled(true);
			}
		});

		comboBoxUOMConversion.setBounds(126, 168, 159, 22);
		jDesktopPane1.add(comboBoxUOMConversion);

		JLabel4j_std lblUomConversion = new JLabel4j_std();
		lblUomConversion.setText(lang.get("lbl_UOM_Conversion"));
		lblUomConversion.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUomConversion.setBounds(12, 168, 102, 22);
		jDesktopPane1.add(lblUomConversion);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
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
		comboBoxDevice.setModel(new DefaultComboBoxModel<String>(new String[]
		{ "Disk", "Email", "FTP", "ActiveMQ" }));

		comboBoxFormat.setModel(new DefaultComboBoxModel<String>(new String[]
		{ "XML", "EANCOM", "IDOC", "MPS", "CSV", "PDF" }));

		comboBoxUOMConversion.setModel(new DefaultComboBoxModel<String>(new String[]
		{ Common.UOM_Convert_Internal_to_ISO, Common.UOM_Convert_Internal_to_Local, Common.UOM_Convert_None, Common.UOM_Convert_ISO_to_INTERNAL, Common.UOM_Convert_ISO_to_Local, Common.UOM_Convert_Local_to_ISO, Common.UOM_Convert_Local_to_Internal }));

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

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(358, 207));
			this.setBounds(0, 0, 720, 370);
			setVisible(true);
			this.setTitle("Interface Properties");
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane4j();

				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(350, 182));
				jDesktopPane1.setLayout(null);
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jButtonUpdate.setBounds(160, 296, 112, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

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
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(274, 296, 112, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close_16x16);
					jButtonCancel.setBounds(388, 296, 112, 32);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jLabel1 = new JLabel4j_std();
					jLabel1.setBounds(12, 8, 102, 22);
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Interface_Type"));
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldInterfaceType = new JTextField4j(JDBInterface.field_interface_type);
					jTextFieldInterfaceType.setBounds(126, 8, 299, 22);
					jDesktopPane1.add(jTextFieldInterfaceType);
					jTextFieldInterfaceType.setText(ltype);
					jTextFieldInterfaceType.setEnabled(false);
					jTextFieldInterfaceType.setEditable(false);
					jTextFieldInterfaceType.setDisabledTextColor(Common.color_text_disabled);
				}
				{
					jLabel3 = new JLabel4j_std();
					jLabel3.setBounds(12, 200, 102, 22);
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Interface_Path"));
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldPath = new JTextField4j(JDBInterface.field_path);
					jTextFieldPath.setBounds(126, 200, 544, 22);
					jTextFieldPath.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(final KeyEvent e)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					jDesktopPane1.add(jTextFieldPath);
					jTextFieldPath.setText(ldirection);
					jTextFieldPath.setEnabled(true);
					jTextFieldPath.setDisabledTextColor(Common.color_text_disabled);
				}
				{
					jComboBoxInterfaceDirection = new JTextField4j(JDBInterface.field_interface_direction);
					jComboBoxInterfaceDirection.setBounds(126, 40, 126, 22);
					jDesktopPane1.add(jComboBoxInterfaceDirection);
					jComboBoxInterfaceDirection.setEnabled(false);
					jComboBoxInterfaceDirection.setEditable(false);

				}
				{
					jLabel2 = new JLabel4j_std();
					jLabel2.setBounds(12, 40, 102, 22);
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Interface_Direction"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					comboBoxFormat.setBounds(126, 136, 126, 22);
					comboBoxFormat.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonUpdate.setEnabled(true);
						}
					});

					jDesktopPane1.add(comboBoxFormat);
				}
				{
					JLabel4j_std lblFormat = new JLabel4j_std();
					lblFormat.setBounds(12, 136, 102, 22);
					lblFormat.setText(lang.get("lbl_Interface_Format"));
					lblFormat.setHorizontalAlignment(SwingConstants.TRAILING);
					jDesktopPane1.add(lblFormat);
				}
				{
					jButtonExecDirChooser = new JButton4j();
					jButtonExecDirChooser.setBounds(670, 201, 14, 21);
					jButtonExecDirChooser.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
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
					checkBox.setBounds(126, 72, 21, 22);

					checkBox.setBackground(Color.WHITE);
					checkBox.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					checkBox.setEnabled(true);
					jDesktopPane1.add(checkBox);
				}

				{
					jLabel2_1 = new JLabel4j_std();
					jLabel2_1.setBounds(12, 72, 102, 22);
					jLabel2_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2_1.setText(lang.get("lbl_Interface_Enabled"));
					jDesktopPane1.add(jLabel2_1);
				}

				{
					comboBoxDevice.setBounds(126, 104, 126, 22);

					comboBoxDevice.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					jDesktopPane1.add(comboBoxDevice);
				}

				{
					jLabel2_2 = new JLabel4j_std();
					jLabel2_2.setBounds(12, 104, 102, 22);
					jLabel2_2.setHorizontalAlignment(SwingConstants.TRAILING);
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
