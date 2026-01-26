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

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBUom;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameUomProperties class allows the user to edit a record in the
 * APP_UOM table.
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
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBUom uom = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_std jLabel_Description;
	private JLabel4j_std jLabel_UOM_ISO;
	private JLabel4j_std jLabel_UOM_Internal;
	private JLabel4j_std jLabel_UOM_Local;
	private JTextField4j jTextFieldDescription;
	private JTextField4j jTextFieldISO_UOM;
	private JTextField4j jTextFieldLocal_UOM;
	private JTextField4j jTextFieldUOM;
	private String luomid;
	private static final long serialVersionUID = 1;

	public void setUOMID(String uomid)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to UOM [" + uomid + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		jButtonSave.setEnabled(false);

		luomid = uomid;
		jTextFieldUOM.setText(luomid);
		setTitle("Unit of Measure [" + luomid + "]");

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

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldISO_UOM.requestFocus();
				jTextFieldISO_UOM.setCaretPosition(jTextFieldISO_UOM.getText().length());
			}
		});

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 424, 224);
			setVisible(true);
			this.setTitle("UOM Properties");

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jLabel_UOM_Internal = new JLabel4j_std();
			jDesktopPane1.add(jLabel_UOM_Internal);
			jLabel_UOM_Internal.setText(lang.get("lbl_UOM_Internal"));
			jLabel_UOM_Internal.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_UOM_Internal.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_UOM_Internal.setBounds(0, 8, 149, 22);

			jTextFieldUOM = new JTextField4j(JDBUom.field_uom);
			jDesktopPane1.add(jTextFieldUOM);
			jTextFieldUOM.setHorizontalAlignment(SwingConstants.LEFT);
			jTextFieldUOM.setEditable(false);
			jTextFieldUOM.setPreferredSize(new java.awt.Dimension(100, 20));
			jTextFieldUOM.setBounds(155, 8, 51, 22);
			jTextFieldUOM.setEnabled(false);

			jLabel_UOM_ISO = new JLabel4j_std();
			jDesktopPane1.add(jLabel_UOM_ISO);
			jLabel_UOM_ISO.setText(lang.get("lbl_UOM_ISO"));
			jLabel_UOM_ISO.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_UOM_ISO.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_UOM_ISO.setBounds(0, 40, 149, 22);

			jTextFieldISO_UOM = new JTextField4j(JDBUom.field_uom);
			jDesktopPane1.add(jTextFieldISO_UOM);
			jTextFieldISO_UOM.setPreferredSize(new java.awt.Dimension(40, 20));
			jTextFieldISO_UOM.setFocusCycleRoot(true);
			jTextFieldISO_UOM.setBounds(155, 40, 53, 22);
			jTextFieldISO_UOM.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonSave.setEnabled(true);
				}
			});

			jButtonSave = new JButton4j(Common.icon_update_16x16);
			jDesktopPane1.add(jButtonSave);
			jButtonSave.setEnabled(false);
			jButtonSave.setText(lang.get("btn_Save"));
			jButtonSave.setMnemonic(lang.getMnemonicChar());
			jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
			jButtonSave.setBounds(47, 142, 110, 32);
			jButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					save();
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(157, 142, 110, 32);

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(267, 142, 110, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jLabel_Description = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Description);
			jLabel_Description.setText(lang.get("lbl_Description"));
			jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Description.setBounds(0, 104, 149, 22);

			jTextFieldLocal_UOM = new JTextField4j(JDBUom.field_uom);
			jTextFieldLocal_UOM.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					jButtonSave.setEnabled(true);
				}
			});
			jTextFieldLocal_UOM.setPreferredSize(new Dimension(40, 20));
			jTextFieldLocal_UOM.setFocusCycleRoot(true);
			jTextFieldLocal_UOM.setCaretPosition(0);
			jTextFieldLocal_UOM.setBounds(155, 72, 53, 22);
			jDesktopPane1.add(jTextFieldLocal_UOM);

			jLabel_UOM_Local = new JLabel4j_std();
			jLabel_UOM_Local.setText(lang.get("lbl_UOM_Local"));
			jLabel_UOM_Local.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_UOM_Local.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_UOM_Local.setBounds(0, 72, 149, 22);
			jDesktopPane1.add(jLabel_UOM_Local);

			jTextFieldDescription = new JTextField4j(JDBUom.field_description);
			jDesktopPane1.add(jTextFieldDescription);
			jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
			jTextFieldDescription.setFocusCycleRoot(true);
			jTextFieldDescription.setBounds(155, 104, 237, 22);
			jTextFieldDescription.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonSave.setEnabled(true);
				}
			});

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					jTextFieldISO_UOM.requestFocus();
					jTextFieldISO_UOM.setCaretPosition(jTextFieldISO_UOM.getText().length());
				}
			});

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void save()
	{
		uom.setIsoUom(jTextFieldISO_UOM.getText().toUpperCase());
		uom.setDescription(jTextFieldDescription.getText());
		uom.setLocalUom(jTextFieldLocal_UOM.getText().toUpperCase());
		uom.update();
		jButtonSave.setEnabled(false);
	}
}
