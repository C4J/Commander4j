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
import com.commander4j.db.JDBWTContainerCode;
import com.commander4j.db.JDBWTScale;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWTContainerCodeProperties class allows the user to edit a record in the APP_WEIGHT_CONTAINER-CODE table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTContainerCodeProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameWTContainerCodeProperties
 *
 */
public class JInternalFrameWTContainerCodeProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldContainerCode;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel_Description;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JLabel4j_std jLabel_ContainerCode;
	private JDBWTContainerCode containerCode = new JDBWTContainerCode(Common.selectedHostID, Common.sessionID);
	private String lcontainercode;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);


	public void setContainerCode(String code)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Container Code [" + code + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			} 
		}

		jButtonSave.setEnabled(false);

		
		lcontainercode = code;
		jTextFieldContainerCode.setText(lcontainercode);
		setTitle("Container Code [" + lcontainercode+"]");
		
		containerCode.setContainerCode(lcontainercode);
		containerCode.getProperties(lcontainercode);

		jTextFieldContainerCode.setText(containerCode.getContainerCode());
		jTextFieldDescription.setText(containerCode.getDescription());
	}
	
	public JInternalFrameWTContainerCodeProperties(String uomid)
	{
		
		super();
		initGUI();
		setContainerCode(uomid);
		
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_CONTAINERCODE_ADD"));

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
			this.setBounds(25, 25, 424, 173);
			setVisible(true);
			this.setTitle("Container Code Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_ContainerCode = new JLabel4j_std();
					jDesktopPane1.add(jLabel_ContainerCode);
					jLabel_ContainerCode.setText(lang.get("lbl_ContainerCode"));
					jLabel_ContainerCode.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_ContainerCode.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_ContainerCode.setBounds(0, 10, 149, 21);
				}
				{
					jTextFieldContainerCode = new JTextField4j(JDBWTContainerCode.field_ContainerCode);
					jDesktopPane1.add(jTextFieldContainerCode);
					jTextFieldContainerCode.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldContainerCode.setEditable(false);
					jTextFieldContainerCode.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldContainerCode.setBounds(155, 10, 237, 21);
					jTextFieldContainerCode.setEnabled(false);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(45, 82, 110, 32);
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
					jButtonHelp.setBounds(157, 82, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(269, 82, 110, 32);
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
					jTextFieldDescription = new JTextField4j(JDBWTScale.field_Description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(155, 43, 237, 21);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
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
		containerCode.setDescription(jTextFieldDescription.getText().toUpperCase());
		containerCode.setContainerCode(jTextFieldContainerCode.getText().toUpperCase());
		containerCode.update();
		jButtonSave.setEnabled(false);
	}
}
