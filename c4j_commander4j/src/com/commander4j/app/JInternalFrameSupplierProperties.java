package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameSupplierProperties.java
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

import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBSuppliers;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameSupplierProperties extends JInternalFrame
{
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("");
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBSuppliers supplierDB = new JDBSuppliers(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_std jLabel_Description;
	private JLabel4j_std jLabel_Enabled;
	private JLabel4j_std jLabel_ID;
	private JLabel4j_std jLabel_Type;
	private JTextField4j jTextFieldDescription;
	private JTextField4j jTextFieldSupplierID;
	private JTextField4j jTextFieldType;
	private String ltype;
	private static final long serialVersionUID = 1;

	public JInternalFrameSupplierProperties()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_SUPPLIERS"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());

			}
		});
	}

	public JInternalFrameSupplierProperties(String reason)
	{

		this();

		jTextFieldSupplierID.setText(reason);
		setTitle(getTitle() + " - " + reason);
		ltype = reason;

		supplierDB.setSupplierID(ltype);
		supplierDB.getSupplierProperties();

		jTextFieldSupplierID.setText(supplierDB.getSupplierID());
		jTextFieldType.setText(supplierDB.getType());
		jTextFieldDescription.setText(supplierDB.getDescription());
		chckbxEnabled.setSelected(supplierDB.isEnabled());
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 399, 210);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);

			jDesktopPane1 = new JDesktopPane4j();

			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jLabel_ID = new JLabel4j_std();
			jLabel_ID.setBounds(0, 10, 98, 22);
			jDesktopPane1.add(jLabel_ID);
			jLabel_ID.setText(lang.get("lbl_Supplier_ID"));
			jLabel_ID.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_ID.setHorizontalTextPosition(SwingConstants.RIGHT);

			jLabel_Type = new JLabel4j_std();
			jLabel_Type.setBounds(0, 39, 98, 22);
			jDesktopPane1.add(jLabel_Type);
			jLabel_Type.setText(lang.get("lbl_Supplier_Type"));
			jLabel_Type.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Type.setHorizontalTextPosition(SwingConstants.RIGHT);

			jLabel_Enabled = new JLabel4j_std();
			jLabel_Enabled.setBounds(0, 97, 98, 22);
			jDesktopPane1.add(jLabel_Enabled);
			jLabel_Enabled.setText(lang.get("lbl_Enabled"));
			jLabel_Enabled.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Enabled.setHorizontalTextPosition(SwingConstants.RIGHT);

			jTextFieldSupplierID = new JTextField4j(JDBSuppliers.field_supplier_id);
			jTextFieldSupplierID.setBounds(105, 10, 80, 22);
			jDesktopPane1.add(jTextFieldSupplierID);
			jTextFieldSupplierID.setHorizontalAlignment(SwingConstants.LEFT);
			jTextFieldSupplierID.setEditable(false);
			jTextFieldSupplierID.setEnabled(false);

			jLabel_Description = new JLabel4j_std();
			jLabel_Description.setBounds(0, 68, 98, 22);
			jDesktopPane1.add(jLabel_Description);
			jLabel_Description.setText(lang.get("lbl_Description"));
			jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);

			jTextFieldType = new JTextField4j(JDBSuppliers.field_supplier_type);
			jTextFieldType.setBounds(105, 39, 125, 22);
			jDesktopPane1.add(jTextFieldType);
			jTextFieldType.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			jTextFieldType.setFocusCycleRoot(true);

			jTextFieldDescription = new JTextField4j(JDBSuppliers.field_description);
			jTextFieldDescription.setBounds(105, 68, 238, 22);
			jDesktopPane1.add(jTextFieldDescription);
			jTextFieldDescription.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			jTextFieldDescription.setFocusCycleRoot(true);

			jButtonUpdate = new JButton4j(Common.icon_update_16x16);
			jButtonUpdate.setBounds(24, 134, 112, 32);
			jDesktopPane1.add(jButtonUpdate);
			jButtonUpdate.setEnabled(false);
			jButtonUpdate.setText(lang.get("btn_Save"));
			jButtonUpdate.setMnemonic(lang.getMnemonicChar());
			jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
			jButtonUpdate.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					supplierDB.setType(jTextFieldType.getText().toUpperCase());
					jTextFieldType.setText(supplierDB.getType());
					supplierDB.setDescription(jTextFieldDescription.getText());
					supplierDB.setEnabled(chckbxEnabled.isSelected());
					supplierDB.update();
					jButtonUpdate.setEnabled(false);
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jButtonHelp.setBounds(136, 134, 112, 32);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jButtonClose.setBounds(249, 134, 112, 32);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			chckbxEnabled.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			chckbxEnabled.setBounds(102, 97, 22, 22);
			jDesktopPane1.add(chckbxEnabled);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
