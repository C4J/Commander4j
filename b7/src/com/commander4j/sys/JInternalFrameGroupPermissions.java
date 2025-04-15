package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameGroupPermissions.java
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
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;

import com.commander4j.db.JDBGroup;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBModuleJList;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_title;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;


/**
 * The JInternalFrameGroupPermissions class is a form which allows a user to manage permissions by assigning modules to a group. Groups are then assigned to a user.
 * This class updates the table SYS_GROUP_PERMISSIONS
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameGroupPermissions.jpg" >
 * 
 * @see com.commander4j.sys.JDialogGroupProperties JDialogeGroupProperties
 * @see com.commander4j.sys.JInternalFrameGroupAdmin JInternalFrameGroupAdmin
 * @see com.commander4j.db.JDBGroup JDBGroup
 * @see com.commander4j.db.JDBGroupPermissions JDBGroupPermissions
 */
public class JInternalFrameGroupPermissions extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonAssign;
	private JDBModuleJList jListUnassigned;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JButton4j jButtonUndo;
	private JDBModuleJList jListAssigned;
	private JScrollPane jScrollPaneAssigned;
	private JScrollPane jScrollPaneUnassigned;
	private JButton4j jButtonUnAssign;
	private JLabel4j_title jLabelAvailable;
	private JLabel4j_title jLabelAssigned;
	private LinkedList<JDBListData> assignedGroupList = new LinkedList<JDBListData>();
	private LinkedList<JDBListData> unAssignedGroupList = new LinkedList<JDBListData>();
	private DefaultComboBoxModel<JDBListData> assignedModel = new DefaultComboBoxModel<JDBListData>();
	private DefaultComboBoxModel<JDBListData> unassignedModel = new DefaultComboBoxModel<JDBListData>();
	private String lGroupId;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public void setButtonState() {
		jButtonSave.setEnabled(true);
		jButtonUndo.setEnabled(true);

		jButtonUnAssign.setEnabled(false);
		jButtonAssign.setEnabled(false);

		if (unAssignedGroupList.size() > 0)
		{
			jButtonAssign.setEnabled(true);
		}

		if (assignedGroupList.size() > 0)
		{
			jButtonUnAssign.setEnabled(true);

		}

	}

	public void addToList(LinkedList<JDBListData> list, JDBListData newValue) {
		list.add(newValue);
		Collections.sort(list);
	}

	public void removeFromList(LinkedList<JDBListData> list, JDBListData oldValue) {
		list.remove(list.indexOf(oldValue));
		Collections.sort(list);
	}

	public JInternalFrameGroupPermissions(String groupId)
	{
		super();

		lGroupId = groupId;
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_GROUP_PERM"));

		resetLists();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

	}

	private void resetLists() {
		JDBGroup tempGroup = new JDBGroup(Common.selectedHostID, Common.sessionID);
		tempGroup.setGroupId(lGroupId);

		assignedGroupList.clear();
		assignedGroupList = tempGroup.getModulesAssigned();
		populateAssignedList();

		unAssignedGroupList.clear();
		unAssignedGroupList = tempGroup.getModulesUnAssigned();
		populateUnAssignedList();

		jButtonUndo.setEnabled(false);
		jButtonSave.setEnabled(false);
	}

	private void populateAssignedList() {
		assignedModel.removeAllElements();

		if (assignedGroupList.size() > 0)
		{
			for (int j = 0; j < assignedGroupList.size(); j++)
			{
				assignedModel.addElement(assignedGroupList.get(j));
			}
			jButtonUnAssign.setEnabled(true);
		}
		else
		{
			jButtonUnAssign.setEnabled(false);
		}

		ListModel<JDBListData> jList1Model = assignedModel;

		jListAssigned.setModel(jList1Model);
	}

	private void populateUnAssignedList() {
		unassignedModel.removeAllElements();

		if (unAssignedGroupList.size() > 0)
		{
			for (int j = 0; j < unAssignedGroupList.size(); j++)
			{
				unassignedModel.addElement(unAssignedGroupList.get(j));
			}
			jButtonAssign.setEnabled(true);
		}
		else
		{
			jButtonAssign.setEnabled(false);
		}

		ListModel<JDBListData> jList1Model = unassignedModel;

		jListUnassigned.setModel(jList1Model);
	}

	private void initGUI() {
		try
		{

			this.setPreferredSize(new java.awt.Dimension(683, 465));
			this.setBounds(0, 0, 692, 562);

			setVisible(true);
			this.setTitle("Permissions - " + lGroupId);
			this.setClosable(true);
			this.setIconifiable(true);

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(314, 295));
				jDesktopPane1.setLayout(null);
				{
					jLabelAssigned = new JLabel4j_title();
					jDesktopPane1.add(jLabelAssigned);
					jLabelAssigned.setText(lang.get("lbl_Assigned"));
					jLabelAssigned.setBounds(15, 0, 152, 22);
				}
				{
					jLabelAvailable = new JLabel4j_title();
					jDesktopPane1.add(jLabelAvailable);
					jLabelAvailable.setText(lang.get("lbl_Unassigned"));
					jLabelAvailable.setBounds(370, 0, 142, 22);
				}
				{
					jButtonAssign = new JButton4j(Common.icon_arrow_left_16x16);
					jDesktopPane1.add(jButtonAssign);
					jButtonAssign.setBounds(327, 205, 26, 24);
					jButtonAssign.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if (jListUnassigned.getSelectedIndex() > -1)
							{
								for (int j = jListUnassigned.getMaxSelectionIndex(); j >= jListUnassigned.getMinSelectionIndex(); j--)
								{
									if (jListUnassigned.isSelectedIndex(j))
									{
										JDBListData item = (JDBListData) jListUnassigned.getModel().getElementAt(j);
										addToList(assignedGroupList, item);
										removeFromList(unAssignedGroupList, item);
									}
								}
								populateAssignedList();
								populateUnAssignedList();
								setButtonState();
							}
						}
					});
				}
				{
					jButtonUnAssign = new JButton4j(Common.icon_arrow_right_16x16);
					jDesktopPane1.add(jButtonUnAssign);
					jButtonUnAssign.setBounds(327, 233, 26, 24);
					jButtonUnAssign.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if (jListAssigned.getSelectedIndex() > -1)
							{
								for (int j = jListAssigned.getMaxSelectionIndex(); j >= jListAssigned.getMinSelectionIndex(); j--)
								{
									if (jListAssigned.isSelectedIndex(j))
									{
										JDBListData item = (JDBListData) jListAssigned.getModel().getElementAt(j);
										addToList(unAssignedGroupList, item);
										removeFromList(assignedGroupList, item);
									}
								}
								populateAssignedList();
								populateUnAssignedList();
								setButtonState();
							}
						}
					});
				}
				{
					jScrollPaneUnassigned = new JScrollPane();
					jDesktopPane1.add(jScrollPaneUnassigned);
					jScrollPaneUnassigned.setBounds(370, 21, 301, 453);
					jScrollPaneUnassigned.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					jScrollPaneUnassigned.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<JDBListData> jListUnassignedModel = new DefaultComboBoxModel<JDBListData>();
						jListUnassigned = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
						jScrollPaneUnassigned.setViewportView(jListUnassigned);
						jListUnassigned.setModel(jListUnassignedModel);
						jListUnassigned.setCellRenderer(Common.renderer_list_unassigned);
						jListUnassigned.setBackground(Common.color_list_unassigned);
					}
				}
				{
					jScrollPaneAssigned = new JScrollPane();
					jDesktopPane1.add(jScrollPaneAssigned);
					jScrollPaneAssigned.setBounds(14, 21, 301, 453);
					jScrollPaneAssigned.setFocusable(false);
					jScrollPaneAssigned.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					jScrollPaneAssigned.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<JDBListData> jListAssignedModel = new DefaultComboBoxModel<JDBListData>();
						jListAssigned = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
						jScrollPaneAssigned.setViewportView(jListAssigned);
						jListAssigned.setCellRenderer(Common.renderer_list_assigned);
						jListAssigned.setBackground(Common.color_list_assigned);
						jListAssigned.setModel(jListAssignedModel);
						jListAssigned.setForeground(Common.color_listFontStandard);
					}
				}
				{
					jButtonUndo = new JButton4j(Common.icon_undo_16x16);
					jDesktopPane1.add(jButtonUndo);
					jButtonUndo.setText(lang.get("btn_Undo"));
					jButtonUndo.setBounds(116, 486, 112, 32);
					jButtonUndo.setMnemonic(lang.getMnemonicChar());
					jButtonUndo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							resetLists();
						}
					});
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setBounds(228, 486, 112, 32);
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JDBGroup group = new JDBGroup(Common.selectedHostID, Common.sessionID);
							group.setGroupId(lGroupId);
							for (int j = 0; j < unAssignedGroupList.size(); j++)
							{
								group.removeModule(unAssignedGroupList.get(j).toString(),Common.userList.getUser(Common.sessionID).getUserId());
							}
							for (int j = 0; j < assignedGroupList.size(); j++)
							{
								group.addModule(assignedGroupList.get(j).toString(),Common.userList.getUser(Common.sessionID).getUserId());
							}
							jButtonUndo.setEnabled(false);
							jButtonSave.setEnabled(false);
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(452, 486, 112, 32);
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(340, 486, 112, 32);
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
