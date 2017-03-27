package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameUomProperties.java
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
import com.commander4j.db.JDBUom;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameUomProperties class allows the user to edit a record in the APP_UOM table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameUomProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBMaterialType JDBMaterialType
 * @see com.commander4j.app.JInternalFrameUomAdmin JInternalFrameUomAdmin
 *
 */
public class JInternalFrameUomProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldUOM;
	private JLabel4j_std jLabel2;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel3;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JTextField4j jTextFieldISO_UOM;
	private JLabel4j_std jLabel1;
	private JDBUom uom = new JDBUom(Common.selectedHostID, Common.sessionID);
	private String luomid;
	private JTextField4j jTextFieldLocal_UOM;
	private JLabel4j_std lblLocalUom;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);


	public void setUOMID(String uomid)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to UOM [" + uomid + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (question == 0)
			{
				save();
			} 
		}

		jButtonSave.setEnabled(false);

		
		luomid = uomid;
		jTextFieldUOM.setText(luomid);
		setTitle("Unit of Measure [" + luomid+"]");
		
		uom.setInternalUom(luomid);
		uom.getInternalUomProperties();

		jTextFieldUOM.setText(uom.getInternalUom());
		jTextFieldISO_UOM.setText(uom.getIsoUom());
		jTextFieldLocal_UOM.setText(uom.getLocalUom());
		jTextFieldDescription.setText(uom.getDescription());
	}
	
	public JInternalFrameUomProperties(String uomid)
	{
		
		super();
		initGUI();
		setUOMID(uomid);
		
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_UOM_ADD"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldISO_UOM.requestFocus();
				jTextFieldISO_UOM.setCaretPosition(jTextFieldISO_UOM.getText().length());
			}
		});


	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 424, 239);
			setVisible(true);
			this.setTitle("UOM Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_UOM_Internal"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel1.setBounds(0, 10, 149, 21);
				}
				{
					jTextFieldUOM = new JTextField4j(JDBUom.field_uom);
					jDesktopPane1.add(jTextFieldUOM);
					jTextFieldUOM.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldUOM.setEditable(false);
					jTextFieldUOM.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldUOM.setBounds(155, 10, 51, 21);
					jTextFieldUOM.setEnabled(false);
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_UOM_ISO"));
					jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel2.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel2.setBounds(0, 43, 149, 21);
				}
				{
					jTextFieldISO_UOM = new JTextField4j(JDBUom.field_uom);
					jDesktopPane1.add(jTextFieldISO_UOM);
					jTextFieldISO_UOM.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldISO_UOM.setFocusCycleRoot(true);
					jTextFieldISO_UOM.setBounds(155, 43, 53, 21);
					jTextFieldISO_UOM.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jButtonSave = new JButton4j(Common.icon_update);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(45, 142, 110, 32);
					jButtonSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							save();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(157, 142, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(269, 142, 110, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Description"));
					jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel3.setBounds(0, 109, 149, 21);
				}
				{
					jTextFieldLocal_UOM = new JTextField4j(JDBUom.field_uom);
					jTextFieldLocal_UOM.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							jButtonSave.setEnabled(true);
						}
					});
					jTextFieldLocal_UOM.setPreferredSize(new Dimension(40, 20));
					jTextFieldLocal_UOM.setFocusCycleRoot(true);
					jTextFieldLocal_UOM.setCaretPosition(0);
					jTextFieldLocal_UOM.setBounds(155, 76, 53, 21);
					jDesktopPane1.add(jTextFieldLocal_UOM);
				}
				{
					lblLocalUom = new JLabel4j_std();
					lblLocalUom.setText(lang.get("lbl_UOM_Local"));
					lblLocalUom.setHorizontalTextPosition(SwingConstants.RIGHT);
					lblLocalUom.setHorizontalAlignment(SwingConstants.RIGHT);
					lblLocalUom.setBounds(0, 76, 149, 21);
					jDesktopPane1.add(lblLocalUom);
				}

				{
					jTextFieldDescription = new JTextField4j(JDBUom.field_description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(155, 109, 237, 21);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						jTextFieldISO_UOM.requestFocus();
						jTextFieldISO_UOM.setCaretPosition(jTextFieldISO_UOM.getText().length());
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
		uom.setIsoUom(jTextFieldISO_UOM.getText().toUpperCase());
		uom.setDescription(jTextFieldDescription.getText().toUpperCase());
		uom.setLocalUom(jTextFieldLocal_UOM.getText().toUpperCase());
		uom.update();
		jButtonSave.setEnabled(false);
	}
}
