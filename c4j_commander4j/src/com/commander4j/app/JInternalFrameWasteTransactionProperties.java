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
import com.commander4j.db.JDBWasteTransactionType;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWasteLocationProperties class allows the user to edit a record in the APP_WASTE_LOCATION table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWasteLocationProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameWasteTransactionProperties
 *
 */
public class JInternalFrameWasteTransactionProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldTransactionType;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel_Description;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JLabel4j_std jLabel_WasteTransaction;
	private JDBWasteTransactionType wasteLocation = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
	private String ltransactionid;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j chckbx_IncludeInTotals = new JCheckBox4j("");
	private JCheckBox4j chckbx_StoreAsNegative = new JCheckBox4j("");
	private JCheckBox4j chckbx_Enabled = new JCheckBox4j("");


	public void setTransactionType(String txntype)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Transaction [" + jTextFieldTransactionType.getText() + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			} 
		}

		jButtonSave.setEnabled(false);

		
		ltransactionid = txntype;
		jTextFieldTransactionType.setText(ltransactionid);
		setTitle("Transaction [" + ltransactionid+"]");
		
		wasteLocation.setWasteTransactionType(ltransactionid);
		wasteLocation.getWasteTransactionProperties(ltransactionid);

		jTextFieldTransactionType.setText(wasteLocation.getWasteTransactionType());
		jTextFieldDescription.setText(wasteLocation.getDescription());
		
		
		chckbx_IncludeInTotals.setSelected(wasteLocation.isIncludedInTotals());
		chckbx_IncludeInTotals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSave.setEnabled(true);
			}
		});
		
		
		chckbx_StoreAsNegative.setSelected(wasteLocation.isStoreAsNegative());
		chckbx_StoreAsNegative.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSave.setEnabled(true);
			}
		});

		chckbx_Enabled.setSelected(wasteLocation.isEnabled());
		chckbx_Enabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSave.setEnabled(true);
			}
		});
		

	}
	
	public JInternalFrameWasteTransactionProperties(String uomid)
	{
		
		super();
		initGUI();
		setTransactionType(uomid);
		
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WASTE_LOCATION"));

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
			this.setBounds(25, 25, 624, 267);
			setVisible(true);
			this.setTitle("Waste Transaction Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_WasteTransaction = new JLabel4j_std();
					jDesktopPane1.add(jLabel_WasteTransaction);
					jLabel_WasteTransaction.setText(lang.get("lbl_Transaction_ID"));
					jLabel_WasteTransaction.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_WasteTransaction.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_WasteTransaction.setBounds(0, 10, 149, 22);
				}
				{
					jTextFieldTransactionType = new JTextField4j(JDBWasteLocation.field_WasteLocationID);
					jDesktopPane1.add(jTextFieldTransactionType);
					jTextFieldTransactionType.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldTransactionType.setEditable(false);
					jTextFieldTransactionType.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldTransactionType.setBounds(155, 10, 237, 22);
					jTextFieldTransactionType.setEnabled(false);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(146, 173, 110, 32);
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
					jButtonHelp.setBounds(258, 173, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(369, 173, 110, 32);
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
					jTextFieldDescription = new JTextField4j(JDBWasteLocation.field_Description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(155, 43, 433, 22);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
					
					chckbx_IncludeInTotals.setBounds(155, 75, 29, 22);
					jDesktopPane1.add(chckbx_IncludeInTotals);
					
					chckbx_StoreAsNegative.setBounds(155, 103, 29, 22);
					jDesktopPane1.add(chckbx_StoreAsNegative);
					
					chckbx_Enabled.setBounds(155, 130, 29, 22);
					jDesktopPane1.add(chckbx_Enabled);
					
					JLabel4j_std jLabel_IncludeInTotals = new JLabel4j_std();
					jLabel_IncludeInTotals.setText(lang.get("lbl_Include_In_Totals"));
					jLabel_IncludeInTotals.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_IncludeInTotals.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_IncludeInTotals.setBounds(0, 75, 149, 22);
					jDesktopPane1.add(jLabel_IncludeInTotals);
					
					JLabel4j_std jLabel_StoreAsNegative = new JLabel4j_std();
					jLabel_StoreAsNegative.setText(lang.get("lbl_Store_As_Negative"));
					jLabel_StoreAsNegative.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_StoreAsNegative.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_StoreAsNegative.setBounds(0, 103, 149, 22);
					jDesktopPane1.add(jLabel_StoreAsNegative);
					
					JLabel4j_std jLabel_Enabled = new JLabel4j_std();
					jLabel_Enabled.setText(lang.get("lbl_Enabled"));
					jLabel_Enabled.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Enabled.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Enabled.setBounds(0, 130, 149, 22);
					jDesktopPane1.add(jLabel_Enabled);
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
		wasteLocation.setWasteTransactionType(jTextFieldTransactionType.getText().toUpperCase());
		wasteLocation.setDescription(jTextFieldDescription.getText());
		wasteLocation.setIncludeInTotals(chckbx_IncludeInTotals.isSelected());
		wasteLocation.setStoreAsNegative(chckbx_StoreAsNegative.isSelected());
		wasteLocation.setEnabled(chckbx_Enabled.isSelected());
		wasteLocation.update();
		
		jButtonSave.setEnabled(false);
	}
}
