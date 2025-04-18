package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameCustomerProperties.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBModule;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameCustomerProperties class is used to update a record in the
 * APP_CUSTOMER table. This table is used to hold customer names and label
 * options. When printing labels the the system looks at the customer ref in the
 * Process Order and then retrieves the customer details from the APP_CUSTOMER
 * table. This permits customer specific titles to appear on labels. The default
 * customer ref of SELF can be used for all Process Orders if the company name
 * on the label is constant.
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameCustomerProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameCustomerAdmin
 *      JInternalFrameCustomerAdmin
 * @see com.commander4j.db.JDBCustomer JDBCustomer
 */
public class JInternalFrameCustomerProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JTextField4j jTextFieldName;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldID;
	private JLabel4j_std jLabel1;
	private JDBCustomer mt = new JDBCustomer(Common.selectedHostID, Common.sessionID);
	private String ltype;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std label;
	private JCheckBox4j checkBoxPrintOnLabel;
	private JCheckBox4j checkBoxOverridePackLabel = new JCheckBox4j();
	private JCheckBox4j checkBoxOverridePalletLabel = new JCheckBox4j();
	private JComboBox4j<JDBListData> comboBoxPackModuleID = new JComboBox4j<JDBListData>();
	private JComboBox4j<JDBListData> comboBoxPalletModuleID = new JComboBox4j<JDBListData>();
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private Vector<JDBListData> moduleList = new Vector<JDBListData>();
	private JTextField4j textField4j_Cust_Data_01 = new JTextField4j(40);
	private JTextField4j textField4j_Cust_Data_02 = new JTextField4j(40);
	private JCheckBox4j checkBox4jOverideBatchFormat = new JCheckBox4j();
	private JTextField4j textField4j_BatchFormat = new JTextField4j(80);
	private JTextField4j textField4j_BatchValidation = new JTextField4j(80);
	private JTextField4j textField4j_Cust_Data_03 = new JTextField4j(40);
	private JTextField4j textField4j_Cust_Data_04 = new JTextField4j(40);

	public JInternalFrameCustomerProperties()
	{
		super();

		moduleList.add(null);
		moduleList.addAll(mod.getModuleIdsByType("USER"));

		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_CUSTOMER"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldName.requestFocus();
				jTextFieldName.setCaretPosition(jTextFieldName.getText().length());

			}
		});
	}

	public JInternalFrameCustomerProperties(String typ)
	{

		this();

		jTextFieldID.setText(typ);
		setTitle(getTitle() + " - " + typ);
		ltype = typ;

		mt.setID(ltype);
		mt.getCustomerProperties();

		jTextFieldID.setText(mt.getID());
		jTextFieldName.setText(mt.getName());

		textField4j_Cust_Data_01.setText(mt.getCustomerData01());
		textField4j_Cust_Data_02.setText(mt.getCustomerData02());
		textField4j_Cust_Data_03.setText(mt.getCustomerData03());
		textField4j_Cust_Data_04.setText(mt.getCustomerData04());

		if (mt.getPrintOnLabel().equals("Y"))
		{
			checkBoxPrintOnLabel.setSelected(true);
		}
		else
		{
			checkBoxPrintOnLabel.setSelected(false);
		}

		checkBoxOverridePackLabel.setSelected(mt.isOverridePackLabel());
		checkBoxOverridePalletLabel.setSelected(mt.isOverridePalletLabel());

		for (int x = 1; x < moduleList.size(); x++)
		{
			if (moduleList.get(x).getmData().equals(mt.getPackLabelModuleID()))
			{
				comboBoxPackModuleID.setSelectedIndex(x);
			}
			if (moduleList.get(x).getmData().equals(mt.getPalletLabelModuleID()))
			{
				comboBoxPalletModuleID.setSelectedIndex(x);
			}
		}

		checkBox4jOverideBatchFormat.setSelected(mt.isBatchOverride());
		textField4j_BatchFormat.setText(mt.getCustomerBatchFormat());
		textField4j_BatchValidation.setText(mt.getCustomerBatchValidation());

		setButtonStates();

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 655, 532);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel1.setText(lang.get("lbl_Customer_ID"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel1.setBounds(12, 22, 185, 22);
				}
				{
					jTextFieldID = new JTextField4j(JDBCustomer.field_customer_id);
					jDesktopPane1.add(jTextFieldID, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldID.setEditable(false);
					jTextFieldID.setEnabled(false);
					jTextFieldID.setBounds(204, 22, 141, 22);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel3.setText(lang.get("lbl_Customer_Name"));
					jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel3.setBounds(12, 55, 185, 22);
				}

				{
					jTextFieldName = new JTextField4j(JDBCustomer.field_customer_name);
					jDesktopPane1.add(jTextFieldName, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldName.setBounds(204, 55, 244, 22);
					jTextFieldName.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					jTextFieldName.setFocusCycleRoot(true);
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonUpdate, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.setBounds(147, 444, 112, 32);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							mt.setName(jTextFieldName.getText());

							mt.setCustomerData01(textField4j_Cust_Data_01.getText());
							mt.setCustomerData02(textField4j_Cust_Data_02.getText());
							mt.setCustomerData03(textField4j_Cust_Data_03.getText());
							mt.setCustomerData04(textField4j_Cust_Data_04.getText());

							if (checkBoxPrintOnLabel.isSelected())
							{
								mt.setPrintOnLabel("Y");
							}
							else
							{
								mt.setPrintOnLabel("N");
							}

							if (checkBoxOverridePackLabel.isSelected())
							{
								if (comboBoxPackModuleID.getSelectedIndex() >= 0)
								{
									if (comboBoxPackModuleID.getSelectedItem().toString().equals(""))
									{
										mt.setOverridePackLabel("N");
										checkBoxOverridePackLabel.setSelected(false);
										mt.setPackLabelModuleID("");
									}
									else
									{
										mt.setOverridePackLabel("Y");
										checkBoxOverridePackLabel.setSelected(true);
										mt.setPackLabelModuleID(comboBoxPackModuleID.getSelectedItem().toString());
									}
								}
								else
								{
									mt.setOverridePackLabel("N");
									checkBoxOverridePackLabel.setSelected(false);
									mt.setPackLabelModuleID("");
								}
							}
							else
							{
								mt.setOverridePackLabel("N");
								checkBoxOverridePackLabel.setSelected(false);
								mt.setPackLabelModuleID("");
							}

							if (checkBoxOverridePalletLabel.isSelected())
							{
								if (comboBoxPalletModuleID.getSelectedIndex() >= 0)
								{
									if (comboBoxPalletModuleID.getSelectedItem().toString().equals(""))
									{
										mt.setOverridePalletLabel("N");
										checkBoxOverridePalletLabel.setSelected(false);
										mt.setPalletLabelModuleID("");
									}
									else
									{
										mt.setOverridePalletLabel("Y");
										checkBoxOverridePalletLabel.setSelected(true);
										mt.setPalletLabelModuleID(comboBoxPalletModuleID.getSelectedItem().toString());
									}
								}
								else
								{
									mt.setOverridePalletLabel("N");
									checkBoxOverridePalletLabel.setSelected(false);
									mt.setPalletLabelModuleID("");
								}
							}
							else
							{
								mt.setOverridePalletLabel("N");
								checkBoxOverridePalletLabel.setSelected(false);
								mt.setPalletLabelModuleID("");
							}

							mt.setOverrideBatchFormat(checkBox4jOverideBatchFormat.isSelected());
							mt.setCustomerBatchFormat(textField4j_BatchFormat.getText());
							mt.setCustomerBatchValidation(textField4j_BatchValidation.getText());

							mt.update();
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(280, 444, 112, 32);
				}
				{
					label = new JLabel4j_std();
					label.setText((String) null);
					label.setHorizontalTextPosition(SwingConstants.RIGHT);
					label.setHorizontalAlignment(SwingConstants.RIGHT);
					label.setBounds(12, 92, 185, 22);
					label.setText(lang.get("lbl_Print_Customer_on_Label"));
					jDesktopPane1.add(label);
				}
				{
					checkBoxPrintOnLabel = new JCheckBox4j();
					checkBoxPrintOnLabel.setSelected(true);
					checkBoxPrintOnLabel.setBackground(Color.WHITE);
					checkBoxPrintOnLabel.setBounds(201, 88, 21, 24);
					checkBoxPrintOnLabel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					jDesktopPane1.add(checkBoxPrintOnLabel);

				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(411, 444, 112, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				checkBoxOverridePackLabel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						setButtonStates();
					}
				});

				checkBoxOverridePackLabel.setBackground(Color.WHITE);
				checkBoxOverridePackLabel.setBounds(201, 126, 21, 24);
				jDesktopPane1.add(checkBoxOverridePackLabel);
				checkBoxOverridePalletLabel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setButtonStates();
					}
				});

				checkBoxOverridePalletLabel.setBackground(Color.WHITE);
				checkBoxOverridePalletLabel.setBounds(201, 162, 21, 24);
				jDesktopPane1.add(checkBoxOverridePalletLabel);

				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Override_Pack_Label"));
				label4j_std.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_std.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_std.setBounds(12, 128, 185, 22);
				jDesktopPane1.add(label4j_std);

				JLabel4j_std label4j_std_1 = new JLabel4j_std();
				label4j_std_1.setText(lang.get("lbl_Override_Pallet_Label"));
				label4j_std_1.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_std_1.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_std_1.setBounds(12, 164, 185, 22);
				jDesktopPane1.add(label4j_std_1);

				ComboBoxModel<JDBListData> jComboBox1Model = new DefaultComboBoxModel<JDBListData>(moduleList);
				comboBoxPackModuleID.setModel(jComboBox1Model);
				comboBoxPackModuleID.setEnabled(false);
				comboBoxPackModuleID.setBounds(230, 128, 219, 22);
				comboBoxPackModuleID.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jButtonUpdate.setEnabled(true);
					}
				});

				jDesktopPane1.add(comboBoxPackModuleID);

				ComboBoxModel<JDBListData> jComboBox2Model = new DefaultComboBoxModel<JDBListData>(moduleList);
				comboBoxPalletModuleID.setModel(jComboBox2Model);
				comboBoxPalletModuleID.setEnabled(false);
				comboBoxPalletModuleID.setBounds(230, 164, 219, 22);
				comboBoxPalletModuleID.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				jDesktopPane1.add(comboBoxPalletModuleID);
				textField4j_Cust_Data_01.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});

				textField4j_Cust_Data_01.setFocusCycleRoot(true);
				textField4j_Cust_Data_01.setCaretPosition(0);
				textField4j_Cust_Data_01.setBounds(204, 199, 244, 22);
				jDesktopPane1.add(textField4j_Cust_Data_01);
				textField4j_Cust_Data_02.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyPressed(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});

				textField4j_Cust_Data_02.setFocusCycleRoot(true);
				textField4j_Cust_Data_02.setCaretPosition(0);
				textField4j_Cust_Data_02.setBounds(204, 232, 244, 22);
				jDesktopPane1.add(textField4j_Cust_Data_02);

				JLabel4j_std label4j_std2 = new JLabel4j_std();
				label4j_std2.setText(lang.get("lbl_Customer_Data_01"));
				label4j_std2.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_std2.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_std2.setBounds(12, 199, 185, 22);
				jDesktopPane1.add(label4j_std2);

				JLabel4j_std label4j_std3 = new JLabel4j_std();
				label4j_std3.setText(lang.get("lbl_Customer_Data_02"));
				label4j_std3.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_std3.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_std3.setBounds(12, 232, 185, 22);
				jDesktopPane1.add(label4j_std3);

				JLabel4j_std lbl_OverrideBatchFormat = new JLabel4j_std();
				lbl_OverrideBatchFormat.setText(lang.get("lbl_Override_Batch_Format"));
				lbl_OverrideBatchFormat.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_OverrideBatchFormat.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_OverrideBatchFormat.setBounds(12, 335, 185, 22);
				jDesktopPane1.add(lbl_OverrideBatchFormat);
				checkBox4jOverideBatchFormat.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});

				checkBox4jOverideBatchFormat.setSelected(true);
				checkBox4jOverideBatchFormat.setBackground(Color.WHITE);
				checkBox4jOverideBatchFormat.setBounds(201, 331, 21, 24);
				jDesktopPane1.add(checkBox4jOverideBatchFormat);

				JLabel4j_std lbl_BatchFormat = new JLabel4j_std();
				lbl_BatchFormat.setText(lang.get("lbl_Batch_Format"));
				lbl_BatchFormat.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_BatchFormat.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_BatchFormat.setBounds(12, 364, 185, 22);
				jDesktopPane1.add(lbl_BatchFormat);

				JLabel4j_std lbl_BatchValidation = new JLabel4j_std();
				lbl_BatchValidation.setText(lang.get("lbl_Batch_Validation"));
				lbl_BatchValidation.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_BatchValidation.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_BatchValidation.setBounds(12, 397, 185, 22);
				jDesktopPane1.add(lbl_BatchValidation);

				textField4j_BatchFormat.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				textField4j_BatchFormat.setColumns(80);
				textField4j_BatchFormat.setFocusCycleRoot(true);
				textField4j_BatchFormat.setCaretPosition(0);
				textField4j_BatchFormat.setBounds(204, 364, 410, 22);
				jDesktopPane1.add(textField4j_BatchFormat);

				textField4j_BatchValidation.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				textField4j_BatchValidation.setColumns(80);
				textField4j_BatchValidation.setFocusCycleRoot(true);
				textField4j_BatchValidation.setCaretPosition(0);
				textField4j_BatchValidation.setBounds(204, 397, 410, 22);
				jDesktopPane1.add(textField4j_BatchValidation);

				JLabel4j_std label4j_std4 = new JLabel4j_std();
				label4j_std4.setText(lang.get("lbl_Customer_Data_03"));
				label4j_std4.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_std4.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_std4.setBounds(12, 265, 185, 22);
				jDesktopPane1.add(label4j_std4);
				textField4j_Cust_Data_03.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});

				textField4j_Cust_Data_03.setFocusCycleRoot(true);
				textField4j_Cust_Data_03.setCaretPosition(0);
				textField4j_Cust_Data_03.setBounds(204, 265, 244, 22);
				jDesktopPane1.add(textField4j_Cust_Data_03);

				JLabel4j_std label4j_std5 = new JLabel4j_std();
				label4j_std5.setText(lang.get("lbl_Customer_Data_04"));
				label4j_std5.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_std5.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_std5.setBounds(12, 298, 185, 22);
				jDesktopPane1.add(label4j_std5);
				textField4j_Cust_Data_04.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});

				textField4j_Cust_Data_04.setFocusCycleRoot(true);
				textField4j_Cust_Data_04.setCaretPosition(0);
				textField4j_Cust_Data_04.setBounds(204, 298, 244, 22);
				jDesktopPane1.add(textField4j_Cust_Data_04);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void setButtonStates()
	{
		jButtonUpdate.setEnabled(true);
		if (checkBoxOverridePackLabel.isSelected())
		{
			comboBoxPackModuleID.setEnabled(true);
		}
		else
		{
			comboBoxPackModuleID.setEnabled(false);
		}

		if (checkBoxOverridePalletLabel.isSelected())
		{
			comboBoxPalletModuleID.setEnabled(true);
		}
		else
		{
			comboBoxPalletModuleID.setEnabled(false);
		}
	}
}
