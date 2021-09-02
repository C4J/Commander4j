package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameEquipmentTypeProperties.java
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

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBEquipmentType;

import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWasteLocationProperties class allows the user to edit a
 * record in the APP_EQUIPMENT_TYPE table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameEquipmentTypeProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameEquipmentProperties
 *
 */
public class JInternalFrameEquipmentProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldEquipmentType;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel_WeightKG;
	private JLabel4j_std jLabel_WeightKGUOM;
	private JLabel4j_std jLabel_Description;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JLabel4j_std jLabel_EquipmentType;
	private JDBEquipmentType equipmentType = new JDBEquipmentType(Common.selectedHostID, Common.sessionID);
	private String lequipmentType;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j chckbx_Enabled = new JCheckBox4j("");
	private JQuantityInput jFormattedTextFieldWeightKG;


	public void setEquipmentType(String location)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Equipment Type [" + jTextFieldEquipmentType.getText() + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		jButtonSave.setEnabled(false);

		lequipmentType = location;
		jTextFieldEquipmentType.setText(lequipmentType);
		setTitle("Equipment Type [" + lequipmentType + "]");

		equipmentType.setEquipmentType(lequipmentType);
		equipmentType.getEquipmentTypeProperties(lequipmentType);

		jTextFieldEquipmentType.setText(equipmentType.getEquipmentType());
		jTextFieldDescription.setText(equipmentType.getDescription());
		
		jFormattedTextFieldWeightKG.setValue(equipmentType.getWeightKG());

		chckbx_Enabled.setSelected(equipmentType.isEnabled());
		
	}

	public JInternalFrameEquipmentProperties(String container)
	{

		super();

		lequipmentType = container;
		
		initGUI();
		
		setEquipmentType(container);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_EQUIPMENT_TYPE_EDIT"));

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

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 624, 217);
			setVisible(true);
			this.setTitle("Equipment Type Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_EquipmentType = new JLabel4j_std();
					jDesktopPane1.add(jLabel_EquipmentType);
					jLabel_EquipmentType.setText(lang.get("lbl_Material_Equipment_Type"));
					jLabel_EquipmentType.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_EquipmentType.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_EquipmentType.setBounds(0, 10, 149, 21);
				}
				{
					jTextFieldEquipmentType = new JTextField4j(JDBEquipmentType.field_EquipmentType);
					jDesktopPane1.add(jTextFieldEquipmentType);
					jTextFieldEquipmentType.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldEquipmentType.setEditable(false);
					jTextFieldEquipmentType.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldEquipmentType.setBounds(155, 10, 110, 21);
					jTextFieldEquipmentType.setEnabled(false);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(131, 141, 110, 32);
					jButtonSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							save();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(243, 141, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(354, 141, 110, 32);
					jButtonClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
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
					jLabel_Description.setBounds(0, 43, 149, 21);
				}
				
				{
					jLabel_WeightKG = new JLabel4j_std();
					jDesktopPane1.add(jLabel_WeightKG);
					jLabel_WeightKG.setText(lang.get("lbl_Weight"));
					jLabel_WeightKG.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_WeightKG.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_WeightKG.setBounds(0, 76, 149, 21);
				}
				
				{
					jLabel_WeightKGUOM = new JLabel4j_std();
					jDesktopPane1.add(jLabel_WeightKGUOM);
					jLabel_WeightKGUOM.setText("KG");
					jLabel_WeightKGUOM.setHorizontalAlignment(SwingConstants.LEFT);
					jLabel_WeightKGUOM.setHorizontalTextPosition(SwingConstants.LEFT);
					jLabel_WeightKGUOM.setBounds(219, 76, 60, 21);
				}

				{
					jTextFieldDescription = new JTextField4j(JDBEquipmentType.field_Description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(155, 43, 433, 21);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
					
					chckbx_Enabled.setBounds(155, 104, 29, 23);
					jDesktopPane1.add(chckbx_Enabled);
					
					JLabel4j_std jLabel_Enabled = new JLabel4j_std();
					jLabel_Enabled.setText(lang.get("lbl_Enabled"));
					jLabel_Enabled.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Enabled.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Enabled.setBounds(0, 106, 149, 21);
					jDesktopPane1.add(jLabel_Enabled);
					chckbx_Enabled.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonSave.setEnabled(true);
						}
					});
					
					
					{
						jFormattedTextFieldWeightKG = new JQuantityInput(new BigDecimal("0"));
						jDesktopPane1.add(jFormattedTextFieldWeightKG);
						jFormattedTextFieldWeightKG.setFont(Common.font_std);
						jFormattedTextFieldWeightKG.setHorizontalAlignment(SwingConstants.TRAILING);
						jFormattedTextFieldWeightKG.setBounds(155, 76, 60, 21);
						jFormattedTextFieldWeightKG.addKeyListener(new KeyAdapter() {
							public void keyReleased(KeyEvent evt) {
								jButtonSave.setEnabled(true);
							}
						});
					}
				}

	}}catch(

	Exception e)
	{
			e.printStackTrace();
		}
	}

	private void save()
	{
		equipmentType.setEquipmentType(jTextFieldEquipmentType.getText().toUpperCase());
		equipmentType.setDescription(jTextFieldDescription.getText());
		equipmentType.setWeightKG(jFormattedTextFieldWeightKG.getQuantity());
		equipmentType.setEnabled(chckbx_Enabled.isSelected());
		equipmentType.update();
				
		jButtonSave.setEnabled(false);
	}
}
