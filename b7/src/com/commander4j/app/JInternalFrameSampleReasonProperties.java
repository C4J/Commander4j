package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMHNReasonProperties.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBSampleReasons;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameMHNReasonProperties class allows the user to edit a single record in the APP_MHN_REASONS table.
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMHNReasonProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameMHNReasonAdmin JInternalFrameMHNReasonAdmin
 * @see com.commander4j.db.JDBSampleReasons JDBSampleReasons
 */
public class JInternalFrameSampleReasonProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldReason;
	private JLabel4j_std jLabel1;
	private JLabel4j_std jLabel2;
	private JDBSampleReasons reas = new JDBSampleReasons(Common.selectedHostID, Common.sessionID);
	private String ltype;
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("");
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JInternalFrameSampleReasonProperties()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_SAMPLE_REASON"));

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
				
			}
		});
	}

	public JInternalFrameSampleReasonProperties(String reason)
	{

		this();

		jTextFieldReason.setText(reason);
		setTitle(getTitle() + " - " + reason);
		ltype = reason;

		reas.setSampleReason(ltype);
		reas.getSampleReasonProperties();

		jTextFieldReason.setText(reas.getSampleReason());
		jTextFieldDescription.setText(reas.getDescription());
		chckbxEnabled.setSelected(reas.isEnabled());
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 399, 167);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel1 = new JLabel4j_std();
					jLabel1.setBounds(0, 7, 98, 21);
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Reason"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				
				{
					jLabel2 = new JLabel4j_std();
					jLabel2.setBounds(0, 57, 98, 21);
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Enabled"));
					jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel2.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				
				{
					jTextFieldReason = new JTextField4j(JDBSampleReasons.field_sample_reason);
					jTextFieldReason.setBounds(105, 7, 80, 21);
					jDesktopPane1.add(jTextFieldReason);
					jTextFieldReason.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldReason.setEditable(false);
					jTextFieldReason.setEnabled(false);
				}
				{
					jLabel3 = new JLabel4j_std();
					jLabel3.setBounds(0, 35, 98, 21);
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Description"));
					jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				
				
				{
					jTextFieldDescription = new JTextField4j(JDBSampleReasons.field_description);
					jTextFieldDescription.setBounds(105, 35, 238, 21);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonUpdate.setEnabled(true);
						}
					});
					jTextFieldDescription.setFocusCycleRoot(true);
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jButtonUpdate.setBounds(26, 90, 112, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							reas.setDescription(jTextFieldDescription.getText());
							reas.setEnabled(chckbxEnabled.isSelected());
							reas.update();
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(138, 90, 112, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(251, 90, 112, 32);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				
				{
					chckbxEnabled.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					chckbxEnabled.setBounds(102, 57, 22, 23);
					jDesktopPane1.add(chckbxEnabled);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
