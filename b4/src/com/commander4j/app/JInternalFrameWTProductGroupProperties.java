package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameWTMaterialGroupsProperties.java
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
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBUom;
import com.commander4j.db.JDBWTProductGroups;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;

/**
 * The JInternalFrameWTMaterialGroupsProperties class allows the user to edit a record in the APP_WEIGHT_MATERIAL_GROUP table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTMaterialGroupsProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameWTProductGroupProperties
 *
 */
public class JInternalFrameWTProductGroupProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JLabel4j_std jLabel_ProductGroup;
	private JDBWTProductGroups matgroupDB = new JDBWTProductGroups(Common.selectedHostID, Common.sessionID);
	private String lmatgroup;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JTextField4j jTextField_ProductGroup  = new JTextField4j(JDBWTProductGroups.field_product_group);
	private JQuantityInput jTextField_NominalWeight = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput jTextField_TareWeight = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j jTextField_Description = new JTextField4j(JDBWTProductGroups.field_description);
	private JTextField4j jTextField_NominalWeight_UOM = new JTextField4j(JDBUom.field_uom);
	private JTextField4j jTextField_TareWeight_UOM = new JTextField4j(JDBUom.field_uom);
	private JLabel jStatusText = new JLabel("");


	public void setMaterialGroup(String group)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Product Group [" + group.toString() + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			} 
		}

		jButtonSave.setEnabled(false);

		lmatgroup = group;

		
		setTitle("Material Group [" + lmatgroup+"]");
		
		matgroupDB.setMaterialGroup(lmatgroup);
		matgroupDB.getMaterialGroupProperties();
		
		jTextField_ProductGroup.setEditable(false);
		
		jTextField_ProductGroup.setText(matgroupDB.getMaterialGroup());


		jTextField_Description.setText(matgroupDB.getDescription());


		jTextField_NominalWeight.setText(matgroupDB.getNominalWeight().toString());


		jTextField_NominalWeight_UOM.setText(matgroupDB.getNominalUOM());


		jTextField_TareWeight.setText(matgroupDB.getTareWeight().toString());


		jTextField_TareWeight_UOM.setText(matgroupDB.getTareWeightUOM());

		jButtonSave.setEnabled(false);
		
	}
	
	public JInternalFrameWTProductGroupProperties(String value)
	{
		
		super();
		initGUI();
		setMaterialGroup(value);
		
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_PRODUCT_GROUP_ADD"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextField_Description.requestFocus();
				jTextField_Description.setCaretPosition(jTextField_Description.getText().length());
				jButtonSave.setEnabled(false);
				

			}
		});


	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 424, 259);
			setVisible(true);
			this.setTitle("Material Group");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_ProductGroup = new JLabel4j_std();
					jDesktopPane1.add(jLabel_ProductGroup);
					jLabel_ProductGroup.setText(lang.get("lbl_Product_Group"));
					jLabel_ProductGroup.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_ProductGroup.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_ProductGroup.setBounds(29, 12, 137, 21);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(49, 148, 110, 32);
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
					jButtonHelp.setBounds(161, 148, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(273, 148, 110, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				
				jTextField_ProductGroup.setVerifyInputWhenFocusTarget(false);
				jTextField_ProductGroup.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_ProductGroup.setBounds(176, 12, 108, 22);
				jDesktopPane1.add(jTextField_ProductGroup);

				
				JLabel4j_std jLabel_Description = new JLabel4j_std();
				jLabel_Description.setText(lang.get("lbl_Description"));
				jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_Description.setBounds(29, 47, 137, 21);
				
				jDesktopPane1.add(jLabel_Description);
				jTextField_Description.setText("");
				jTextField_Description.setPreferredSize(new Dimension(40, 20));
				jTextField_Description.setFocusCycleRoot(true);
				jTextField_Description.setCaretPosition(0);
				jTextField_Description.setBounds(176, 46, 204, 21);
				jDesktopPane1.add(jTextField_Description);
				jTextField_Description.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						jButtonSave.setEnabled(true);
					}
				});
				
				JLabel4j_std jLabel_NominalWeight = new JLabel4j_std();
				jLabel_NominalWeight.setText(lang.get("lbl_Nominal_Weight"));
				jLabel_NominalWeight.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_NominalWeight.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_NominalWeight.setBounds(29, 81, 137, 21);
				jDesktopPane1.add(jLabel_NominalWeight);
				
				jTextField_NominalWeight.setVerifyInputWhenFocusTarget(false);
				jTextField_NominalWeight.setHorizontalAlignment(SwingConstants.TRAILING);
				jTextField_NominalWeight.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_NominalWeight.setBounds(176, 80, 108, 22);
				jDesktopPane1.add(jTextField_NominalWeight);
				jTextField_NominalWeight.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
					}
				});
				
	
				jTextField_NominalWeight_UOM.setText("");
				jTextField_NominalWeight_UOM.setPreferredSize(new Dimension(40, 20));
				jTextField_NominalWeight_UOM.setFocusCycleRoot(true);
				jTextField_NominalWeight_UOM.setCaretPosition(0);
				jTextField_NominalWeight_UOM.setBounds(296, 82, 50, 21);
				jDesktopPane1.add(jTextField_NominalWeight_UOM);
				jTextField_NominalWeight_UOM.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						jButtonSave.setEnabled(true);
					}
				});
				
				JLabel4j_std jLabel_TareWeight = new JLabel4j_std();
				jLabel_TareWeight.setText(lang.get("lbl_Tare_Weight"));
				jLabel_TareWeight.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_TareWeight.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_TareWeight.setBounds(29, 115, 137, 21);
				jDesktopPane1.add(jLabel_TareWeight);
				
				jTextField_TareWeight.setVerifyInputWhenFocusTarget(false);
				jTextField_TareWeight.setHorizontalAlignment(SwingConstants.TRAILING);
				jTextField_TareWeight.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_TareWeight.setBounds(176, 114, 108, 22);
				jDesktopPane1.add(jTextField_TareWeight);
				jTextField_TareWeight.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						jButtonSave.setEnabled(true);
					}
				});

				
				jTextField_TareWeight_UOM.setText("");
				jTextField_TareWeight_UOM.setPreferredSize(new Dimension(40, 20));
				jTextField_TareWeight_UOM.setFocusCycleRoot(true);
				jTextField_TareWeight_UOM.setCaretPosition(0);
				jTextField_TareWeight_UOM.setBounds(296, 116, 50, 21);
				jDesktopPane1.add(jTextField_TareWeight_UOM);
				jTextField_TareWeight_UOM.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						jButtonSave.setEnabled(true);
					}
				});
				

				jStatusText.setBounds(0, 200, 414, 27);
				jStatusText.setForeground(new java.awt.Color(255, 0, 0));
				jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				jDesktopPane1.add(jStatusText);

				jButtonSave.setEnabled(false);
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void save()
	{
		
		jTextField_NominalWeight_UOM.setText(jTextField_NominalWeight_UOM.getText().toUpperCase().trim());
		jTextField_TareWeight_UOM.setText(jTextField_TareWeight_UOM.getText().toUpperCase().trim());
		
		matgroupDB.setMaterialGroup(lmatgroup);
		matgroupDB.setDescription(jTextField_Description.getText());
		matgroupDB.setNominalWeight(jTextField_NominalWeight.getQuantity());
		matgroupDB.setTareWeight(jTextField_TareWeight.getQuantity());
		
		matgroupDB.setNominalUOM(jTextField_NominalWeight_UOM.getText());
		matgroupDB.setTareUOM(jTextField_TareWeight_UOM.getText());

		if (matgroupDB.update())
		{
			jButtonSave.setEnabled(false);
			jStatusText.setText("");
		}
		else
		{
			jStatusText.setText(matgroupDB.getErrorMessage());
		}
	}
}
