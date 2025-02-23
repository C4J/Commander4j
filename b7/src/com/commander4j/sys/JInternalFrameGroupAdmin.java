package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameGroupAdmin.java
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;

import com.commander4j.db.JDBGroup;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import javax.swing.ListSelectionModel;

/**
 * The JInternalFrameGroupAdmin class is a form which allows a user to manage the groups held in the table SYS_GROUPS. This table is part of the
 * security/permissions configuration within the system. A group will contain a list of modules which a user is allowed to access. A user is then 
 * allocated one or more groups.
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameGroupAdmin.jpg" >
 * 
 * @see com.commander4j.sys.JDialogeGroupProperties JDialogeGroupProperties
 * @see com.commander4j.sys.JInternalFrameGroupPermissions JInternalFrameGroupPermissions
 * @see com.commander4j.db.JDBGroup JDBGroup
 * @see com.commander4j.db.JDBGroupPermissions JDBGroupPermissions
 */
public class JInternalFrameGroupAdmin extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JButton4j jButtonRename;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonPermissions;
	private JButton4j jButtonAdd;
	private JList4j<String> jListGroups;
	private JScrollPane jScrollPane1;
	private String lGroupId;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	private void addRecord() {
		JDBGroup grp = new JDBGroup(Common.selectedHostID, Common.sessionID);

		lGroupId = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Group_Create"));
		if (lGroupId != null)
		{
			if (lGroupId.equals("") == false)
			{
				lGroupId = lGroupId.toUpperCase();
				//if (grp.create(lGroupId, "",Common.userList.getUser(Common.sessionID).getUserId()) == false)
				grp.setGroupId(lGroupId);
				if (grp.isValidGroupId() )
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, "Group Id already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
				}
				else
				{

					JLaunchMenu.runDialog("FRM_ADMIN_GROUP_EDIT", lGroupId);
				}
				populateList(lGroupId);
			}
		}
	}

	private void delete() {
		if (jListGroups.isSelectionEmpty() == false)
		{
			lGroupId = (String) jListGroups.getSelectedValue();
			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Group_Delete")+" " + lGroupId + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (n == 0)
			{
				JDBGroup qroup = new JDBGroup(Common.selectedHostID, Common.sessionID);
				qroup.setGroupId(lGroupId);
				qroup.delete(lGroupId,Common.userList.getUser(Common.sessionID).getUserId());
				populateList("");
			}
		}
	}

	private void edit() {
		if (jListGroups.isSelectionEmpty() == false)
		{
			lGroupId = (String) jListGroups.getSelectedValue();
			JLaunchMenu.runDialog("FRM_ADMIN_GROUP_EDIT", lGroupId);
		}
	}

	private void permissions() {
		if (jListGroups.isSelectionEmpty() == false)
		{
			lGroupId = (String) jListGroups.getSelectedValue();
			JLaunchMenu.runForm("FRM_ADMIN_GROUP_PERM", lGroupId);
		}
	}

	private void print() {
		JLaunchReport.runReport("RPT_GROUPS",null,"",null,"");
	}
	
	private void showGroupMembership() {
		if (jListGroups.isSelectionEmpty()==false)
		{
			lGroupId = (String) jListGroups.getSelectedValue();
			JLaunchMenu.runForm("FRM_ADMIN_GROUP_MEMBERS", lGroupId);
		}
	}

	private void rename() {
		if (jListGroups.isSelectionEmpty() == false)
		{
			String lgroup_id_from = (String) jListGroups.getSelectedValue();
			String lgroup_id_to = new String();
			lgroup_id_to = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Rename"),lgroup_id_from);
			if (lgroup_id_to != null)
			{
				if (lgroup_id_to.equals("") == false)
				{
					lgroup_id_to = lgroup_id_to.toUpperCase();
					JDBGroup group = new JDBGroup(Common.selectedHostID, Common.sessionID);
					group.setGroupId(lgroup_id_from);
					if (group.renameTo(lgroup_id_from,lgroup_id_to,Common.userList.getUser(Common.sessionID).getUserId()) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, group.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
					}
					populateList(lgroup_id_to);
				}
			}
		}
	}

	public JInternalFrameGroupAdmin()
	{
		super();
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_GROUPS"));
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		populateList("");
	}

	private void populateList(String defaultitem) {
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		JDBGroup tempGroup = new JDBGroup(Common.selectedHostID, Common.sessionID);
		LinkedList<String> tempGroupList = tempGroup.getGroupIds();
		int sel = -1;
		for (int j = 0; j < tempGroupList.size(); j++)
		{
			defComboBoxMod.addElement(tempGroupList.get(j));
			if (tempGroupList.get(j).toString().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<String> jList1Model = defComboBoxMod;
		jListGroups.setModel(jList1Model);
		jListGroups.setSelectedIndex(sel);
		jListGroups.setCellRenderer(Common.renderer_list);
		jListGroups.ensureIndexIsVisible(sel);
	}

	private void excel() {
		JDBGroup groups = new JDBGroup(Common.selectedHostID, Common.sessionID);
		ResultSet rs = null;

			rs = groups.getGroupIdResultset();

		JExcel export = new JExcel();
		export.saveAs("groups.xls", rs, Common.mainForm);
		populateList("");
	}
	
	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(270, 472));
			this.setBounds(0, 0, 371, 571);
			setVisible(true);
			this.setTitle("Group Admin");
			this.setClosable(true);
			this.setIconifiable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(260, 292));
				jDesktopPane1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 0, 225, 539);
					jScrollPane1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<String> jList1Model = new DefaultComboBoxModel<String>(new String[] { "Item One", "Item Two" });
						jListGroups = new JList4j<String>();
						jListGroups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jScrollPane1.setViewportView(jListGroups);
						jListGroups.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									jButtonPermissions.doClick();
								}
							}
						});
						jListGroups.setModel(jList1Model);

					}

					{
						final JPopupMenu popupMenu = new JPopupMenu();
						addPopup(jListGroups, popupMenu);

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									addRecord();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Add"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_ADD"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete_16x16);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									delete();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Delete"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_DELETE"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit_16x16);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									edit();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Edit"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_EDIT"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_permissions_16x16);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									permissions();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Permissions"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_PERM"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_rename_16x16);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									rename();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Rename"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_RENAME"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_print_16x16);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									print();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Print"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_GROUPS"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh_16x16);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									populateList("");
								}
							});
							newItemMenuItem.setText(lang.get("btn_Refresh"));
							popupMenu.add(newItemMenuItem);
						}
					}
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setBounds(230, 5, 125, 32);
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_ADD"));
					jButtonAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							addRecord();
						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete_16x16);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setBounds(230, 36, 125, 32);
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setFocusTraversalKeysEnabled(false);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_DELETE"));
					jButtonDelete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							delete();
						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit_16x16);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setBounds(230, 67, 125, 32);
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_EDIT"));
					jButtonEdit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							edit();
						}
					});
				}
				{
					jButtonPermissions = new JButton4j(Common.icon_permissions_16x16);
					jDesktopPane1.add(jButtonPermissions);
					jButtonPermissions.setText(lang.get("btn_Permissions"));
					jButtonPermissions.setBounds(230, 98, 125, 32);
					jButtonPermissions.setMnemonic(lang.getMnemonicChar());
					jButtonPermissions.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_PERM"));
					jButtonPermissions.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							permissions();
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(230, 315, 125, 32);
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setBounds(230, 191, 125, 32);
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_GROUPS"));
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							print();
						}
					});
				}
				{
					jButtonRename = new JButton4j(Common.icon_rename_16x16);
					jDesktopPane1.add(jButtonRename);
					jButtonRename.setText(lang.get("btn_Rename"));
					jButtonRename.setMnemonic(lang.getMnemonicChar());
					jButtonRename.setBounds(230, 129, 125, 32);
					jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_RENAME"));
					jButtonRename.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							rename();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(230, 284, 125, 32);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setBounds(230, 222, 125, 32);
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							populateList("");
						}
					});
				}
				
				{
					JButton4j jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							excel();
						}
					});
					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(230, 253, 125, 32);
					jDesktopPane1.add(jButtonExcel);
				}
				{
					JButton4j jButtonUsers = new JButton4j(Common.icon_user_16x16);
					jButtonUsers.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							showGroupMembership();
						}
					});
					jButtonUsers.setText(lang.get("mod_FRM_ADMIN_USERS"));
					jButtonUsers.setMnemonic('M');
					jButtonUsers.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_GROUP_USERS"));
					jButtonUsers.setBounds(230, 160, 125, 32);
					jDesktopPane1.add(jButtonUsers);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
