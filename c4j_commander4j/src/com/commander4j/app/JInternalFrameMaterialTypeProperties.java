package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMaterialTypeProperties.java
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

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBMaterialType;
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
 * JInternalFrameMaterialTypeProperties allows you edit a record held in the
 * APP_MATERIAL_TYPE table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMaterialTypeProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBMaterialType JDBMaterialType
 * @see com.commander4j.app.JInternalFrameMaterialTypeProperties
 *      JInternalFrameMaterialTypeProperties
 *
 */
public class JInternalFrameMaterialTypeProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldType;
	private JLabel4j_std jLabel1;
	private JDBMaterialType mt = new JDBMaterialType(Common.selectedHostID, Common.sessionID);
	private String ltype;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j checkBoxOverridePackLabel = new JCheckBox4j();
	private JCheckBox4j checkBoxOverridePalletLabel = new JCheckBox4j();
	private JComboBox4j<JDBListData> comboBoxPackModuleID = new JComboBox4j<JDBListData>();
	private JComboBox4j<JDBListData> comboBoxPalletModuleID = new JComboBox4j<JDBListData>();
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private Vector<JDBListData> moduleList = new Vector<JDBListData>();

	public JInternalFrameMaterialTypeProperties()
	{
		super();

		moduleList.add(null);
		moduleList.addAll(mod.getModuleIdsByType("USER"));

		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_TYPE"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
			}
		});
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

	public JInternalFrameMaterialTypeProperties(String typ)
	{

		this();

		jTextFieldType.setText(typ);
		setTitle(getTitle() + " - " + typ);
		ltype = typ;

		mt.setType(ltype);
		mt.getMaterialTypeProperties();

		jTextFieldType.setText(mt.getType());
		jTextFieldDescription.setText(mt.getDescription());

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

		setButtonStates();
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 636, 245);
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
					jLabel1.setText(lang.get("lbl_Material_Type"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel1.setBounds(6, 17, 175, 22);
				}
				{
					jTextFieldType = new JTextField4j(JDBMaterialType.field_material_type);
					jDesktopPane1.add(jTextFieldType, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldType.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldType.setEditable(false);
					jTextFieldType.setEnabled(false);
					jTextFieldType.setBounds(185, 17, 104, 22);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel3.setText(lang.get("lbl_Description"));
					jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel3.setBounds(6, 49, 175, 22);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBMaterialType.field_description);
					jDesktopPane1.add(jTextFieldDescription, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldDescription.setBounds(185, 49, 397, 22);
					jTextFieldDescription.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					jTextFieldDescription.setFocusCycleRoot(true);
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonUpdate, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.setBounds(147, 154, 112, 32);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							mt.setDescription(jTextFieldDescription.getText());

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
										mt.setPalletLabelModuleID("");
										checkBoxOverridePalletLabel.setSelected(false);
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
					jButtonHelp.setBounds(266, 154, 112, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(385, 154, 112, 32);
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
				checkBoxOverridePackLabel.setBounds(185, 79, 21, 22);
				jDesktopPane1.add(checkBoxOverridePackLabel);
				checkBoxOverridePalletLabel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setButtonStates();
					}
				});

				checkBoxOverridePalletLabel.setBackground(Color.WHITE);
				checkBoxOverridePalletLabel.setBounds(185, 115, 21, 22);
				jDesktopPane1.add(checkBoxOverridePalletLabel);

				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Override_Pack_Label"));
				label4j_std.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_std.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_std.setBounds(6, 79, 175, 22);
				jDesktopPane1.add(label4j_std);

				JLabel4j_std label4j_std_1 = new JLabel4j_std();
				label4j_std_1.setText(lang.get("lbl_Override_Pallet_Label"));
				label4j_std_1.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_std_1.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_std_1.setBounds(6, 115, 175, 22);
				jDesktopPane1.add(label4j_std_1);

				ComboBoxModel<JDBListData> jComboBox1Model = new DefaultComboBoxModel<JDBListData>(moduleList);
				comboBoxPackModuleID.setModel(jComboBox1Model);
				comboBoxPackModuleID.setEnabled(false);
				comboBoxPackModuleID.setBounds(218, 79, 208, 22);
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
				comboBoxPalletModuleID.setBounds(218, 115, 208, 22);
				comboBoxPalletModuleID.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				jDesktopPane1.add(comboBoxPalletModuleID);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
