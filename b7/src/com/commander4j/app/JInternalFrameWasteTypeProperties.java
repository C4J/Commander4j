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

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBWasteLocation;
import com.commander4j.db.JDBWasteTypes;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWasteLocationProperties class allows the user to edit a record in the APP_WASTE_TYPES table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWasteTypeProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameWasteTypeProperties
 *
 */
public class JInternalFrameWasteTypeProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldTypeID;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel_Description;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JLabel4j_std jLabel_WasteType;
	private JDBWasteTypes wasteType = new JDBWasteTypes(Common.selectedHostID, Common.sessionID);
	private String ltypeid;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j chckbx_Recycle = new JCheckBox4j("");
	private JCheckBox4j chckbx_Hazard = new JCheckBox4j("");
	private JCheckBox4j chckbx_Enabled = new JCheckBox4j("");
	private JCheckBox4j chckbx_PPEReqd = new JCheckBox4j("");


	public void setType(String location)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Type [" + jTextFieldTypeID.getText() + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			} 
		}

		jButtonSave.setEnabled(false);

		
		ltypeid = location;
		jTextFieldTypeID.setText(ltypeid);
		setTitle("Type [" + ltypeid+"]");
		
		wasteType.setWasteTypeID(ltypeid);
		wasteType.getWasteTypeProperties(ltypeid);

		jTextFieldTypeID.setText(wasteType.getWasteTypeID());
		jTextFieldDescription.setText(wasteType.getDescription());
		
		chckbx_Recycle.setSelected(wasteType.isRecyclable());
		chckbx_Recycle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSave.setEnabled(true);
			}
		});
		
		chckbx_Hazard.setSelected(wasteType.isHazardous());
		chckbx_Hazard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSave.setEnabled(true);
			}
		});
		
		chckbx_PPEReqd.setSelected(wasteType.isPPEReqd());
		chckbx_PPEReqd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSave.setEnabled(true);
			}
		});
		
		chckbx_Enabled.setSelected(wasteType.isEnabled());
		chckbx_Enabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSave.setEnabled(true);
			}
		});
	}
	
	public JInternalFrameWasteTypeProperties(String uomid)
	{
		
		super();
		initGUI();
		setType(uomid);
		
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WASTE_TYPE"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
				

				
			}
		});


	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 624, 290);
			setVisible(true);
			this.setTitle("Waste Type Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_WasteType = new JLabel4j_std();
					jDesktopPane1.add(jLabel_WasteType);
					jLabel_WasteType.setText(lang.get("lbl_Type_ID"));
					jLabel_WasteType.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_WasteType.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_WasteType.setBounds(0, 10, 149, 22);
				}
				{
					jTextFieldTypeID = new JTextField4j(JDBWasteLocation.field_WasteLocationID);
					jDesktopPane1.add(jTextFieldTypeID);
					jTextFieldTypeID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldTypeID.setEditable(false);
					jTextFieldTypeID.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldTypeID.setBounds(155, 10, 237, 22);
					jTextFieldTypeID.setEnabled(false);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(140, 214, 110, 32);
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
					jButtonHelp.setBounds(252, 214, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(363, 214, 110, 32);
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
					jLabel_Description.setBounds(0, 45, 149, 22);
				}

				{
					jTextFieldDescription = new JTextField4j(JDBWasteLocation.field_Description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(155, 45, 433, 22);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
					
					chckbx_Recycle.setBounds(155, 75, 29, 22);
					jDesktopPane1.add(chckbx_Recycle);
					
					chckbx_Hazard.setBounds(155, 105, 29, 22);
					jDesktopPane1.add(chckbx_Hazard);
					
					chckbx_Enabled.setBounds(155, 165, 29, 22);
					jDesktopPane1.add(chckbx_Enabled);
					
					JLabel4j_std jLabel_Recycle = new JLabel4j_std();
					jLabel_Recycle.setText(lang.get("lbl_Recycle"));
					jLabel_Recycle.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Recycle.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Recycle.setBounds(0, 75, 149, 22);
					jDesktopPane1.add(jLabel_Recycle);
					
					JLabel4j_std jLabel_Hazard = new JLabel4j_std();
					jLabel_Hazard.setText(lang.get("lbl_Hazard"));
					jLabel_Hazard.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Hazard.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Hazard.setBounds(0, 105, 149, 22);
					jDesktopPane1.add(jLabel_Hazard);
					
					JLabel4j_std jLabel_Enabled = new JLabel4j_std();
					jLabel_Enabled.setText(lang.get("lbl_Enabled"));
					jLabel_Enabled.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Enabled.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Enabled.setBounds(0, 165, 149, 22);
					jDesktopPane1.add(jLabel_Enabled);
					

					chckbx_PPEReqd.setSelected(false);
					chckbx_PPEReqd.setBounds(155, 135, 29, 22);
					jDesktopPane1.add(chckbx_PPEReqd);
					
					JLabel4j_std jLabel_PPEReqd = new JLabel4j_std();
					jLabel_PPEReqd.setText(lang.get("lbl_PPERequired"));
					jLabel_PPEReqd.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_PPEReqd.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_PPEReqd.setBounds(0, 135, 149, 22);
					jDesktopPane1.add(jLabel_PPEReqd);
				}
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
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
		wasteType.setWasteTypeID(jTextFieldTypeID.getText().toUpperCase());
		wasteType.setDescription(jTextFieldDescription.getText());
		wasteType.setRecylable(chckbx_Recycle.isSelected());
		wasteType.setPPERequired(chckbx_PPEReqd.isSelected());
		wasteType.setHazardous(chckbx_Hazard.isSelected()); 
		wasteType.setEnabled(chckbx_Enabled.isSelected());
		wasteType.update();
		
		jButtonSave.setEnabled(false);
	}
}
