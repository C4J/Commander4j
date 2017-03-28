package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogeGroupProperties.java
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBGroup;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JDialogeGroupProperties class is a form which allows a user edit a record within the table SYS_GROUPS. 
 * 
 * <p>
 * <img alt="" src="./doc-files/JDialogeGroupProperties.jpg" >
 * 
 * @see com.commander4j.sys.JInternalFrameGroupAdmin JInternalFrameGroupAdmin
 * @see com.commander4j.sys.JInternalFrameGroupPermissions JInternalFrameGroupPermissions
 * @see com.commander4j.db.JDBGroup JDBGroup
 * @see com.commander4j.db.JDBGroupPermissions JDBGroupPermissions
 */
public class JDialogeGroupProperties extends JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldGroupId;
	private JLabel4j_std jLabelDescription;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JTextField4j jTextFieldDescription;
	private JButton4j jButtonUpdate;
	private JLabel4j_std jLabelGroupID;
	private String lgroupid;
	private JDBGroup group = new JDBGroup(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);


	public JDialogeGroupProperties(JFrame parent,String groupid)
	{
		
		super(parent);
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_GROUP_EDIT"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
			}
		});

		setTitle(getTitle() + " - " + groupid);
		lgroupid = groupid;

		group.setGroupId(lgroupid);
		group.getGroupProperties();
		jTextFieldGroupId.setText(lgroupid);
		jTextFieldDescription.setText(group.getDescription());

	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(366, 145));
			this.setBounds(0, 0, 435, 140);
			setModal(true);
			this.setTitle("Group Properties");

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(361, 104));
				jDesktopPane1.setLayout(null);
				{
					jTextFieldGroupId = new JTextField4j(JDBGroup.field_group_id);
					jTextFieldGroupId.setBounds(101, 10, 170, 21);
					jDesktopPane1.add(jTextFieldGroupId);
					jTextFieldGroupId.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldGroupId.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldGroupId.setEditable(false);
					jTextFieldGroupId.setEnabled(false);
					jTextFieldGroupId.setDisabledTextColor(Common.color_text_disabled);

				}
				{
					jLabelGroupID = new JLabel4j_std();
					jLabelGroupID.setBounds(5, 10, 83, 21);
					jDesktopPane1.add(jLabelGroupID);
					jLabelGroupID.setText(lang.get("lbl_Group_ID"));
					jLabelGroupID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelGroupID.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jLabelDescription = new JLabel4j_std();
					jLabelDescription.setBounds(5, 37, 86, 21);
					jDesktopPane1.add(jLabelDescription);
					jLabelDescription.setText(lang.get("lbl_Description"));
					jLabelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{

					jButtonUpdate = new JButton4j(Common.icon_update);
					jButtonUpdate.setBounds(50, 70, 110, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							
							if (group.isValidGroupId()==false)
							{
								group.create(lgroupid, jTextFieldDescription.getText(), Common.userList.getUser(Common.sessionID).getUserId());
							}
							else
							{
								group.setDescription(jTextFieldDescription.getText());
								group.update();								
							}

							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{

					jButtonClose = new JButton4j(Common.icon_close);
					jButtonClose.setBounds(284, 70, 110, 32);
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
					jTextFieldDescription = new JTextField4j(JDBGroup.field_description);
					jTextFieldDescription.setBounds(101, 37, 314, 21);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jButtonHelp.setBounds(166, 70, 110, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
