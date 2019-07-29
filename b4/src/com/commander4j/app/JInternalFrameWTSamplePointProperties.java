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

import com.commander4j.db.JDBWTSamplePoint;
import com.commander4j.db.JDBWTScale;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWTSamplePointProperties class allows the user to edit a record in the APP_WEIGHT_SAMPLE_POINT table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTSamplePointProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameWTSamplePointProperties
 *
 */
public class JInternalFrameWTSamplePointProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldSamplePoint;
	private JLabel4j_std jLabel_Location;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel_Description;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JTextField4j jTextFieldLocation;
	private JLabel4j_std jLabel1;
	private JDBWTSamplePoint samppoint = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
	private String lsamplepoint;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);


	public void setSamplePointID(String sampid)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Sample Point [" + sampid + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			} 
		}

		jButtonSave.setEnabled(false);

		
		lsamplepoint = sampid;
		jTextFieldSamplePoint.setText(lsamplepoint);
		setTitle("Sample Point [" + lsamplepoint+"]");
		
		samppoint.setSamplePoint(lsamplepoint);
		samppoint.getProperties(lsamplepoint);

		jTextFieldSamplePoint.setText(samppoint.getSamplePoint());
		jTextFieldLocation.setText(samppoint.getLocation());
		jTextFieldDescription.setText(samppoint.getDescription());
	}
	
	public JInternalFrameWTSamplePointProperties(String uomid)
	{
		
		super();
		initGUI();
		setSamplePointID(uomid);
		
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_SAMPLEPOINT_ADD"));

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
			this.setBounds(25, 25, 424, 204);
			setVisible(true);
			this.setTitle("Sample Point Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_SamplePoint"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel1.setBounds(0, 10, 149, 21);
				}
				{
					jTextFieldSamplePoint = new JTextField4j(JDBWTSamplePoint.field_SamplePoint);
					jDesktopPane1.add(jTextFieldSamplePoint);
					jTextFieldSamplePoint.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldSamplePoint.setEditable(false);
					jTextFieldSamplePoint.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldSamplePoint.setBounds(155, 10, 237, 21);
					jTextFieldSamplePoint.setEnabled(false);
				}
				{
					jLabel_Location = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Location);
					jLabel_Location.setText(lang.get("lbl_Storage_Location"));
					jLabel_Location.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Location.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Location.setBounds(0, 76, 149, 21);
				}
				{
					jTextFieldLocation = new JTextField4j(JDBWTSamplePoint.field_Location);
					jDesktopPane1.add(jTextFieldLocation);
					jTextFieldLocation.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldLocation.setFocusCycleRoot(true);
					jTextFieldLocation.setBounds(155, 76, 237, 21);
					jTextFieldLocation.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(44, 116, 110, 32);
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
					jButtonHelp.setBounds(156, 116, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(268, 116, 110, 32);
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
						jTextFieldLocation.requestFocus();
						jTextFieldLocation.setCaretPosition(jTextFieldLocation.getText().length());
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
		samppoint.setLocation(jTextFieldLocation.getText().toUpperCase());
		samppoint.setDescription(jTextFieldDescription.getText().toUpperCase());
		samppoint.setSamplePoint(jTextFieldSamplePoint.getText().toUpperCase());
		samppoint.update();
		jButtonSave.setEnabled(false);
	}
}
