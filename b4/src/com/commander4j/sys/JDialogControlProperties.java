package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogControlProperties.java
 * 
 * Package Name : com.commander4j.sys
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBAuditPermissions;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The class JInternalFrameControlAdmin allows you to edit a single control
 * record. Any changes made to an entry is record in the audit table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JDialogControlProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBControl JDBControl
 * @see com.commander4j.sys.JInternalFrameControlAdmin
 *      JInternalFrameControlAdmin
 * @see com.commander4j.db.JDBAuditPermissions JDBAuditPermissions
 */
public class JDialogControlProperties extends JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jLabelDescription;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JTextField4j jTextFieldDescription;
	private JTextField4j jTextFieldKeyValue;
	private JTextField4j jTextFieldSystemKey;
	private JButton4j jButtonUpdate;
	private JLabel4j_std jLabelKeyValue;
	private JLabel4j_std jLabelSystemKey;
	private String lsystemKey;
	private JDBControl control = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBAuditPermissions audPerm = new JDBAuditPermissions(Common.selectedHostID, Common.sessionID);

	public JDialogControlProperties(JFrame parent, String systemKey)
	{

		super(parent);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_CONTROL_EDIT"));

		jTextFieldSystemKey.setText(systemKey);
		setTitle(getTitle() + " - " + systemKey);
		lsystemKey = systemKey;

		control.setSystemKey(lsystemKey);
		control.getProperties();

		jTextFieldKeyValue.setText(control.getKeyValue());
		jTextFieldDescription.setText(control.getDescription());

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldKeyValue.requestFocus();
				jTextFieldKeyValue.setCaretPosition(jTextFieldKeyValue.getText().length());
			}
		});
	}

	private void initGUI()
	{
		try
		{
			// setDefaultLookAndFeelDecorated(true);
			setPreferredSize(new java.awt.Dimension(460, 163));
			this.setBounds(25, 25, 736, 183);
			setModal(true);
			this.setTitle("Control Properties");
			getContentPane().setLayout(null);

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBounds(0, 0, 736, 161);
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1);
				jDesktopPane1.setPreferredSize(new Dimension(452, 140));
				jDesktopPane1.setLayout(null);
				{
					jLabelSystemKey = new JLabel4j_std();
					jLabelSystemKey.setBounds(9, 67, 87, 19);
					jDesktopPane1.add(jLabelSystemKey);
					jLabelSystemKey.setText(lang.get("lbl_Description"));
					jLabelSystemKey.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSystemKey.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jLabelKeyValue = new JLabel4j_std();
					jLabelKeyValue.setBounds(5, 12, 93, 19);
					jDesktopPane1.add(jLabelKeyValue);
					jLabelKeyValue.setText(lang.get("lbl_System_Key"));
					jLabelKeyValue.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelKeyValue.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jLabelDescription = new JLabel4j_std();
					jLabelDescription.setBounds(10, 41, 88, 19);
					jDesktopPane1.add(jLabelDescription);
					jLabelDescription.setText(lang.get("lbl_Value"));
					jLabelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{

					jButtonUpdate = new JButton4j(Common.icon_update);
					jButtonUpdate.setBounds(217, 98, 110, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							control.setDescription(jTextFieldDescription.getText());
							control.setKeyValue(jTextFieldKeyValue.getText());
							control.update();
							audPerm.generateNewAuditLogID();
							audPerm.write(Common.userList.getUser(Common.sessionID).getUserId(), "CONTROL", "UPDATE", control.getSystemKey(), control.getKeyValue());
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jButtonClose.setBounds(438, 98, 110, 32);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jTextFieldSystemKey = new JTextField4j(JDBControl.field_system_key);
					jTextFieldSystemKey.setBounds(106, 10, 252, 21);
					jTextFieldSystemKey.setFocusable(false);
					jDesktopPane1.add(jTextFieldSystemKey);
					jTextFieldSystemKey.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldSystemKey.setEditable(false);
					jTextFieldSystemKey.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldSystemKey.setEnabled(false);
				}
				{
					jTextFieldKeyValue = new JTextField4j(JDBControl.field_key_value);
					jTextFieldKeyValue.setBounds(106, 39, 604, 21);
					jDesktopPane1.add(jTextFieldKeyValue);
					jTextFieldKeyValue.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldKeyValue.setFocusCycleRoot(true);
					jTextFieldKeyValue.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});

				}
				{
					jTextFieldDescription = new JTextField4j(JDBControl.field_description);
					jTextFieldDescription.setBounds(106, 65, 604, 21);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jButtonHelp.setBounds(332, 98, 100, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
