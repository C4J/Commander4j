package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMaterialUomProperties.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBUom;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import javax.swing.JCheckBox;

/**
 * The JInternalFrameMaterialUomProperties is the screen for editing a single unit of  measure record for a Material in the APP_MATERIAL_UOM table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMaterialUomProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameMaterialUomAdmin JInternalFrameMaterialUomAdmin
 * @see com.commander4j.db.JDBMaterialUom JDBMaterialUom
 */
public class JInternalFrameMaterialUomProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JLabel4j_std jLabel2;
	private JLabel4j_std jLabel4;
	private JSpinner jSpinnerNumerator;
	private JSpinner jSpinnerDenominator;
	private JTextField4j jTextFieldVariant;
	private JTextField4j jTextFieldEAN;
	private JTextField4j jTextFieldUOM;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabel6;
	private JLabel4j_std jLabel5;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private SpinnerNumberModel numeratornumbermodel = new SpinnerNumberModel();
	private SpinnerNumberModel denominatornumbermodel = new SpinnerNumberModel();
	private String lmaterial;
	private String luom;
	private JDBMaterialUom materialuom = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std status = new JLabel4j_std();
	private JCheckBox chckbxOverride = new JCheckBox("");

	public JInternalFrameMaterialUomProperties()
	{
		super();
		initGUI();
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldEAN.requestFocus();
				jTextFieldEAN.setCaretPosition(jTextFieldEAN.getText().length());

				status.setForeground(Color.RED);
				status.setBackground(Color.GRAY);
				status.setBounds(0, 196, 385, 21);
				jDesktopPane1.add(status);

			}
		});
	}

	public JInternalFrameMaterialUomProperties(String material, String uom)
	{
		this();
		lmaterial = material;
		luom = uom;
		jTextFieldMaterial.setText(lmaterial);
		jTextFieldUOM.setText(luom);
		materialuom.setMaterial(lmaterial);
		materialuom.setUom(luom);
		materialuom.getMaterialUomProperties();
		jTextFieldEAN.setText(materialuom.getEan());
		jTextFieldVariant.setText(materialuom.getVariant());
		jSpinnerNumerator.setValue(materialuom.getNumerator());
		jSpinnerDenominator.setValue(materialuom.getDenominator());
		if (materialuom.getOverride().equals("X"))
		{
			chckbxOverride.setSelected(true);
		}
		else
		{
			chckbxOverride.setSelected(false);
		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(376, 234));
			this.setBounds(25, 25, 410, 261);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(390, 208));
				jDesktopPane1.setLayout(null);
				{
					jButtonUpdate = new JButton4j(Common.icon_update);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setPreferredSize(new java.awt.Dimension(90, 30));
					jButtonUpdate.setBounds(11, 181, 120, 32);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							boolean result = true;

							materialuom.setMaterial(lmaterial);
							materialuom.setUom(luom);
							materialuom.setEan(jTextFieldEAN.getText());
							jTextFieldEAN.setText(materialuom.getEan());
							materialuom.setVariant(jTextFieldVariant.getText());
							jTextFieldVariant.setText(materialuom.getVariant());
							materialuom.setNumerator((Integer) jSpinnerNumerator.getValue());
							materialuom.setDenominator((Integer) jSpinnerDenominator.getValue());
							
							if (chckbxOverride.isSelected())
							{
								materialuom.setOverride("X");
							}
							else
							{
								materialuom.setOverride("");
							}

							if (materialuom.isValidMaterialUom() == false)
							{
								result = materialuom.create();
							}
							else
							{
								result = materialuom.update();
							}
							
							if (result == false)
							{
								JOptionPane.showMessageDialog(Common.mainForm, materialuom.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);
							}
							else
							{
								jButtonUpdate.setEnabled(false);
							}

						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(141, 181, 120, 32);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setPreferredSize(new java.awt.Dimension(90, 30));
					jButtonCancel.setBounds(271, 181, 120, 32);
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
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel1.setBounds(11, 9, 147, 21);
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Material_UOM_EAN"));
					jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel2.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel2.setBounds(11, 59, 147, 21);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Material_UOM_Variant"));
					jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel3.setBounds(11, 83, 147, 21);
				}
				{
					jLabel4 = new JLabel4j_std();
					jDesktopPane1.add(jLabel4);
					jLabel4.setText(lang.get("lbl_Material_UOM_Numerator"));
					jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel4.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel4.setBounds(11, 107, 147, 21);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Material_UOM_Denominator"));
					jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel5.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel5.setBounds(11, 129, 147, 21);
				}
				{
					jLabel6 = new JLabel4j_std();
					jDesktopPane1.add(jLabel6);
					jLabel6.setText(lang.get("lbl_Material_UOM"));
					jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel6.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel6.setBounds(11, 34, 147, 21);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldMaterial.setEditable(false);
					jTextFieldMaterial.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldMaterial.setBounds(165, 9, 113, 21);
					jTextFieldMaterial.setEnabled(false);
				}
				{
					jTextFieldUOM = new JTextField4j(JDBUom.field_uom);
					jDesktopPane1.add(jTextFieldUOM);
					jTextFieldUOM.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldUOM.setEditable(false);
					jTextFieldUOM.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldUOM.setBounds(165, 34, 60, 21);
					jTextFieldUOM.setEnabled(false);
				}
				{
					jTextFieldEAN = new JTextField4j(JDBMaterialUom.field_ean);
					jDesktopPane1.add(jTextFieldEAN);
					jTextFieldEAN.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldEAN.setFocusCycleRoot(true);
					jTextFieldEAN.setBounds(165, 59, 175, 21);
					jTextFieldEAN.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{

							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jTextFieldVariant = new JTextField4j(JDBMaterialUom.field_variant);;
					jDesktopPane1.add(jTextFieldVariant);
					jTextFieldVariant.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldVariant.setFocusCycleRoot(true);
					jTextFieldVariant.setBounds(165, 83, 34, 21);
					jTextFieldVariant.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{

					jSpinnerDenominator = new JSpinner();
					JSpinner.NumberEditor ne2 = new JSpinner.NumberEditor(jSpinnerDenominator);
					ne2.getTextField().setFont(Common.font_std); 
					jSpinnerDenominator.setEditor(ne2);
					jDesktopPane1.add(jSpinnerDenominator);
					jSpinnerDenominator.setModel(denominatornumbermodel);
					jSpinnerDenominator.setBounds(165, 129, 75, 21);
					jSpinnerDenominator.addChangeListener(new ChangeListener()
					{
						public void stateChanged(ChangeEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{

					jSpinnerNumerator = new JSpinner();
					JSpinner.NumberEditor ne1 = new JSpinner.NumberEditor(jSpinnerNumerator);
					ne1.getTextField().setFont(Common.font_std); 
					jSpinnerNumerator.setEditor(ne1);
					jDesktopPane1.add(jSpinnerNumerator);
					jSpinnerNumerator.setModel(numeratornumbermodel);
					jSpinnerNumerator.setBounds(165, 107, 75, 21);
					jSpinnerNumerator.addChangeListener(new ChangeListener()
					{
						public void stateChanged(ChangeEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				
				{
					JLabel4j_std lbl_Override = new JLabel4j_std();
					lbl_Override.setText(lang.get("lbl_Override"));
					lbl_Override.setHorizontalTextPosition(SwingConstants.RIGHT);
					lbl_Override.setHorizontalAlignment(SwingConstants.RIGHT);
					lbl_Override.setBounds(11, 152, 147, 21);
					jDesktopPane1.add(lbl_Override);
				}
				chckbxOverride.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonUpdate.setEnabled(true);
					}
				});
				

				chckbxOverride.setBounds(161, 150, 25, 23);
				jDesktopPane1.add(chckbxOverride);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
