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

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBWasteContainer;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWasteLocationProperties class allows the user to edit a
 * record in the APP_WASTE_CONTAINER table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWasteContainerProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameWasteContainerProperties
 *
 */
public class JInternalFrameWasteContainerProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldContainerID;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel_TareWeight;
	private JLabel4j_std jLabel_TareWeightUOM;
	private JLabel4j_std jLabel_Description;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JLabel4j_std jLabel_WasteContainer;
	private JDBWasteContainer wasteContainer = new JDBWasteContainer(Common.selectedHostID, Common.sessionID);
	private String lcontainerid;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j chckbx_Enabled = new JCheckBox4j("");
	private JQuantityInput jFormattedTextFieldTareWeight;


	public void setContainerID(String location)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Container [" + jTextFieldContainerID.getText() + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		jButtonSave.setEnabled(false);

		lcontainerid = location;
		jTextFieldContainerID.setText(lcontainerid);
		setTitle("Container [" + lcontainerid + "]");

		wasteContainer.setWasteContainerID(lcontainerid);
		wasteContainer.getWasteContainerProperties(lcontainerid);

		jTextFieldContainerID.setText(wasteContainer.getWasteContainerID());
		jTextFieldDescription.setText(wasteContainer.getDescription());
		
		jFormattedTextFieldTareWeight.setValue(wasteContainer.getTareWeight());

		chckbx_Enabled.setSelected(wasteContainer.isEnabled());
		
	}

	public JInternalFrameWasteContainerProperties(String container)
	{

		super();

		lcontainerid = container;
		
		initGUI();
		
		setContainerID(container);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WASTE_CONTAINER"));

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
			this.setTitle("Waste Container Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_WasteContainer = new JLabel4j_std();
					jDesktopPane1.add(jLabel_WasteContainer);
					jLabel_WasteContainer.setText(lang.get("lbl_Container_ID"));
					jLabel_WasteContainer.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_WasteContainer.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_WasteContainer.setBounds(0, 10, 149, 22);
				}
				{
					jTextFieldContainerID = new JTextField4j(JDBWasteContainer.field_WasteContainerID);
					jDesktopPane1.add(jTextFieldContainerID);
					jTextFieldContainerID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldContainerID.setEditable(false);
					jTextFieldContainerID.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldContainerID.setBounds(155, 10, 237, 22);
					jTextFieldContainerID.setEnabled(false);
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
					jLabel_Description.setBounds(0, 43, 149, 22);
				}
				
				{
					jLabel_TareWeight = new JLabel4j_std();
					jDesktopPane1.add(jLabel_TareWeight);
					jLabel_TareWeight.setText(lang.get("lbl_Tare_Weight"));
					jLabel_TareWeight.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_TareWeight.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_TareWeight.setBounds(0, 76, 149, 22);
				}
				
				{
					jLabel_TareWeightUOM = new JLabel4j_std();
					jDesktopPane1.add(jLabel_TareWeightUOM);
					jLabel_TareWeightUOM.setText("KG");
					jLabel_TareWeightUOM.setHorizontalAlignment(SwingConstants.LEFT);
					jLabel_TareWeightUOM.setHorizontalTextPosition(SwingConstants.LEFT);
					jLabel_TareWeightUOM.setBounds(219, 76, 60, 22);
				}

				{
					jTextFieldDescription = new JTextField4j(JDBWasteContainer.field_Description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(155, 43, 433, 22);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
					
					chckbx_Enabled.setBounds(155, 104, 29, 22);
					jDesktopPane1.add(chckbx_Enabled);
					
					JLabel4j_std jLabel_Enabled = new JLabel4j_std();
					jLabel_Enabled.setText(lang.get("lbl_Enabled"));
					jLabel_Enabled.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Enabled.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Enabled.setBounds(0, 104, 149, 22);
					jDesktopPane1.add(jLabel_Enabled);
					chckbx_Enabled.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonSave.setEnabled(true);
						}
					});
					
					
					{
						jFormattedTextFieldTareWeight = new JQuantityInput(new BigDecimal("0"));
						jDesktopPane1.add(jFormattedTextFieldTareWeight);
						jFormattedTextFieldTareWeight.setHorizontalAlignment(SwingConstants.TRAILING);
						jFormattedTextFieldTareWeight.setBounds(155, 76, 60, 22);
						jFormattedTextFieldTareWeight.addKeyListener(new KeyAdapter() {
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
		wasteContainer.setWasteContainerID(jTextFieldContainerID.getText().toUpperCase());
		wasteContainer.setDescription(jTextFieldDescription.getText());
		wasteContainer.setTareWeight(jFormattedTextFieldTareWeight.getQuantity());
		wasteContainer.setEnabled(chckbx_Enabled.isSelected());
		wasteContainer.update();
				
		jButtonSave.setEnabled(false);
	}
}
