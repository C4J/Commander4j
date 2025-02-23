package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameWTSamplePointProperties.java
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBUom;
import com.commander4j.db.JDBWasteMaterial;
import com.commander4j.db.JDBWasteReportingIDS;
import com.commander4j.db.JDBWasteTypes;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;

/**
 * The JInternalFrameWasteMaterialProperties class allows the user to edit a
 * record in the APP_WASTE_MATERIAL table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWasteMaterialProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameWasteMaterialProperties
 *
 */
public class JInternalFrameWasteMaterialProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldMaterialID;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JComboBox4j<JDBWasteTypes> jComboBoxMaterialType = new JComboBox4j<JDBWasteTypes>();
	private JLabel4j_std jLabel_Description;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JLabel4j_std jLabel_WasteMaterialID;
	private JDBWasteMaterial wasteMaterials = new JDBWasteMaterial(Common.selectedHostID, Common.sessionID);
	private JDBWasteTypes wasteTypes = new JDBWasteTypes(Common.selectedHostID, Common.sessionID);
	private String lmaterialid;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox chckbx_Enabled = new JCheckBox("");
	
	private Vector<JDBWasteTypes> typeList = new Vector<JDBWasteTypes>();
	private Vector<JDBUom> uomList = new Vector<JDBUom>();
	
	private ComboBoxModel<JDBWasteTypes> jComboBoxTypeModel;
	
	private JQuantityInput jFormattedTextFieldCostPerUOM;
	private JDBUom uom = new JDBUom(Common.selectedHostID, Common.sessionID);

	public void setMaterialID(String mat)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Material ID [" + jTextFieldMaterialID.getText() + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		jButtonSave.setEnabled(false);

		lmaterialid = mat;
		jTextFieldMaterialID.setText(lmaterialid);
		setTitle("Material ID [" + lmaterialid + "]");

		wasteMaterials.setWasteMaterialID(lmaterialid);
		wasteMaterials.getWasteMaterialProperties(lmaterialid);

		jTextFieldMaterialID.setText(wasteMaterials.getWasteMaterialID());
		jTextFieldDescription.setText(wasteMaterials.getDescription());

		chckbx_Enabled.setSelected(wasteMaterials.isEnabled());
		
		wasteTypes.setWasteTypeID(wasteMaterials.getWasteTypeID());
		wasteTypes.getWasteTypeProperties();
		
		jComboBoxTypeModel.setSelectedItem(wasteTypes);
		
		jFormattedTextFieldCostPerUOM.setValue(wasteMaterials.getCostPerKG());

		jButtonSave.setEnabled(false);
	}

	public JInternalFrameWasteMaterialProperties(String uomid)
	{

		super();
		initGUI();
		setMaterialID(uomid);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WASTE_MATERIAL"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());


			}
		});

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 624, 259);
			setVisible(true);
			this.setTitle("Waste Material ID Properties");
			{
				typeList.add(new JDBWasteTypes(Common.selectedHostID, Common.sessionID));
				typeList.addAll(wasteTypes.getWasteTypesList());
				
				uomList.add(null);
				uomList.addAll(uom.getInternalUoms());
				
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_WasteMaterialID = new JLabel4j_std();
					jDesktopPane1.add(jLabel_WasteMaterialID);
					jLabel_WasteMaterialID.setText(lang.get("lbl_Material"));
					jLabel_WasteMaterialID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_WasteMaterialID.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_WasteMaterialID.setBounds(0, 10, 149, 21);
				}
				{
					jTextFieldMaterialID = new JTextField4j(JDBWasteReportingIDS.field_WasteReportingID);
					jDesktopPane1.add(jTextFieldMaterialID);
					jTextFieldMaterialID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldMaterialID.setEditable(false);
					jTextFieldMaterialID.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldMaterialID.setBounds(155, 10, 237, 21);
					jTextFieldMaterialID.setEnabled(false);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(140, 174, 110, 32);
					jButtonSave.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							save();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(252, 174, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(363, 174, 110, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jLabel_Description = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Description);
					jLabel_Description.setText(lang.get("lbl_Description"));
					jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Description.setBounds(0, 45, 149, 21);
				}

				{
					jTextFieldDescription = new JTextField4j(JDBWasteReportingIDS.field_Description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(155, 45, 433, 21);
					jTextFieldDescription.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});

					chckbx_Enabled.setBounds(155, 143, 29, 23);
					jDesktopPane1.add(chckbx_Enabled);

					JLabel4j_std jLabel_Enabled = new JLabel4j_std();
					jLabel_Enabled.setText(lang.get("lbl_Enabled"));
					jLabel_Enabled.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Enabled.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Enabled.setBounds(0, 145, 149, 21);
					chckbx_Enabled.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jDesktopPane1.add(jLabel_Enabled);

					{

						jComboBoxMaterialType.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								jButtonSave.setEnabled(true);
							}
						});

						jComboBoxTypeModel = new DefaultComboBoxModel<JDBWasteTypes>(typeList);
						jComboBoxMaterialType.setModel(jComboBoxTypeModel);
						jComboBoxMaterialType.setBounds(155, 80, 433, 21);
						jComboBoxMaterialType.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								jButtonSave.setEnabled(true);
							}
						});
						jDesktopPane1.add(jComboBoxMaterialType);
					}
					
					JLabel4j_std jLabel_WasteType = new JLabel4j_std();
					jLabel_WasteType.setText(lang.get("lbl_Type_ID"));
					jLabel_WasteType.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_WasteType.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_WasteType.setBounds(0, 80, 149, 21);
					jDesktopPane1.add(jLabel_WasteType);

				}
								
				JLabel4j_std jLabel_CostPerKG = new JLabel4j_std();
				jLabel_CostPerKG.setText(lang.get("lbl_Cost_Per_KG"));
				jLabel_CostPerKG.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_CostPerKG.setBounds(0, 113, 149, 21);
				jDesktopPane1.add(jLabel_CostPerKG);
				
				{
					jFormattedTextFieldCostPerUOM = new JQuantityInput(new BigDecimal("0.000"));
					jDesktopPane1.add(jFormattedTextFieldCostPerUOM);
					jFormattedTextFieldCostPerUOM.setFont(Common.font_std);
					jFormattedTextFieldCostPerUOM.setHorizontalAlignment(SwingConstants.TRAILING);
					jFormattedTextFieldCostPerUOM.setBounds(155, 113, 60, 21);
					jFormattedTextFieldCostPerUOM.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}

				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
					}
				});
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void save()
	{
		wasteMaterials.setWasteMaterialID(jTextFieldMaterialID.getText().toUpperCase());
		wasteMaterials.setDescription(jTextFieldDescription.getText());
		wasteMaterials.setEnabled(chckbx_Enabled.isSelected());
			
		JDBWasteTypes t = ((JDBWasteTypes) jComboBoxMaterialType.getSelectedItem());
				
		wasteMaterials.setWasteTypeID(t.getWasteTypeID());
		wasteMaterials.setCostPerKG(jFormattedTextFieldCostPerUOM.getQuantity());
		wasteMaterials.update();

		jButtonSave.setEnabled(false);
	}
}
