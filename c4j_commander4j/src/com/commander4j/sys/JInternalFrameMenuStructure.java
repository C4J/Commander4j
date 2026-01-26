package com.commander4j.sys;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameMenuStructure.java
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
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBMenus;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBModuleJList;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_title;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import javax.swing.SwingConstants;

/**
 * The JInternalFrameMenuStructure class allows you to customise the menu
 * structure within the application and updates the table SYS_MENUS
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMenuStructure.jpg" >
 *
 * @see com.commander4j.db.JDBMenus JDBMenus
 */
public class JInternalFrameMenuStructure extends javax.swing.JInternalFrame
{
	private DefaultComboBoxModel<JDBListData> assignedModel = new DefaultComboBoxModel<JDBListData>();
	private DefaultComboBoxModel<JDBListData> menuModel = new DefaultComboBoxModel<JDBListData>();
	private DefaultComboBoxModel<JDBListData> unassignedModel = new DefaultComboBoxModel<JDBListData>();
	private JButton4j jButtonAssign;
	private JButton4j jButtonClose;
	private JButton4j jButtonDown;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JButton4j jButtonUnAssign;
	private JButton4j jButtonUndo;
	private JButton4j jButtonUp;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBModuleJList jListAssigned;
	private JDBModuleJList jListMenus;
	private JDBModuleJList jListUnAssigned;
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_title jLabel_Menu;
	private JLabel4j_title jLabel_UnAssigned;
	private JLabel4j_title jLabel_Assigned;
	private JScrollPane4j jScrollPaneAssigned;
	private JScrollPane4j jScrollPaneMenus;
	private JScrollPane4j jScrollPaneUnAssigned;
	private LinkedList<JDBListData> assignedList = new LinkedList<JDBListData>();
	private LinkedList<JDBListData> menuList = new LinkedList<JDBListData>();
	private LinkedList<JDBListData> unassignedList = new LinkedList<JDBListData>();
	private static final long serialVersionUID = 1;

	public JInternalFrameMenuStructure()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MENU"));
		populateMenuList();
	}

	public void setButtonState()
	{
		jButtonSave.setEnabled(true);
		jButtonUndo.setEnabled(true);

		jButtonUp.setEnabled(false);
		jButtonDown.setEnabled(false);
		jButtonUnAssign.setEnabled(false);
		jButtonAssign.setEnabled(false);

		if (unassignedList.size() > 0)
		{
			jButtonAssign.setEnabled(true);
		}

		if (assignedList.size() > 0)
		{
			jButtonUnAssign.setEnabled(true);

			if (assignedList.size() > 1)
			{
				jButtonUp.setEnabled(true);
				jButtonDown.setEnabled(true);
			}
		}

	}

	private void populateMenuList()
	{
		menuModel.removeAllElements();

		JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
		this.setIconifiable(true);

		menuList = mod.getMenuIds();

		for (int j = 0; j < menuList.size(); j++)
		{
			menuModel.addElement(menuList.get(j));
		}

		ListModel<JDBListData> jList1Model = menuModel;
		jListMenus.setCellRenderer(Common.renderer_list);
		jListMenus.setModel(jList1Model);
	}

	private void refreshJList(JDBModuleJList jlist, DefaultComboBoxModel<JDBListData> comboboxmodel, LinkedList<JDBListData> linkedlist)
	{
		comboboxmodel.removeAllElements();

		for (int j = 0; j < linkedlist.size(); j++)
		{
			comboboxmodel.addElement(linkedlist.get(j));
		}
		ListModel<JDBListData> jList1Model = comboboxmodel;

		jlist.setModel(jList1Model);
	}

	private void populateUnAssignedList(JDBListData i)
	{
		unassignedModel.removeAllElements();

		JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
		mod.setModuleId(i.toString());

		unassignedList = mod.getModulesUnAssignedtoMenu();

		if (unassignedList.size() > 0)
		{
			for (int j = 0; j < unassignedList.size(); j++)
			{
				unassignedModel.addElement(unassignedList.get(j));
			}
			jButtonAssign.setEnabled(true);
			jButtonUnAssign.setEnabled(true);
		}
		else
		{
			jButtonAssign.setEnabled(false);
			jButtonUnAssign.setEnabled(false);
		}

		ListModel<JDBListData> jList1Model = unassignedModel;

		jListUnAssigned.setModel(jList1Model);

		jListUnAssigned.setBackground(Common.color_list_background_unassigned);
		jListUnAssigned.setCellRenderer(Common.renderer_list_unassigned);
	}

	private void populateAssignedList(JDBListData i)
	{
		assignedModel.removeAllElements();

		JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
		mod.setModuleId(i.toString());

		assignedList = mod.getModulesAssignedtoMenu();

		if (assignedList.size() > 0)
		{
			for (int j = 0; j < assignedList.size(); j++)
			{
				assignedModel.addElement(assignedList.get(j));
			}
			jButtonUp.setEnabled(true);
			jButtonDown.setEnabled(true);
		}
		else
		{
			jButtonUp.setEnabled(false);
			jButtonDown.setEnabled(false);
		}

		ListModel<JDBListData> jList1Model = assignedModel;
		jListAssigned.setBackground(Common.color_list_background_assigned);
		jListAssigned.setCellRenderer(Common.renderer_list_assigned);
		jListAssigned.setModel(jList1Model);
	}

	public void addToList(LinkedList<JDBListData> list, JDBListData newValue, boolean sort)
	{
		list.add(newValue);
		if (sort == true)
			Collections.sort(list);
	}

	public void removeFromList(LinkedList<JDBListData> list, Object oldValue)
	{
		list.remove(list.indexOf(oldValue));
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(761, 450));
			this.setBounds(0, 0, 818, 543);
			setVisible(true);
			this.setTitle("Menu Structure");
			this.setClosable(true);
			getContentPane().setLayout(null);

			jDesktopPane1 = new JDesktopPane4j();
			jDesktopPane1.setBounds(0, 0, 829, 511);
			jDesktopPane1.setBackground(Common.color_app_window);
			this.getContentPane().add(jDesktopPane1);
			jDesktopPane1.setPreferredSize(new Dimension(792, 429));
			jDesktopPane1.setLayout(null);

			jScrollPaneMenus = new JScrollPane4j(JScrollPane4j.List);
			jDesktopPane1.add(jScrollPaneMenus);
			jScrollPaneMenus.setBounds(0, 25, 245, 434);
			jScrollPaneMenus.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));

			ListModel<JDBListData> jListMenusModel = new DefaultComboBoxModel<JDBListData>();
			jListMenus = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
			jScrollPaneMenus.setViewportView(jListMenus);
			jListMenus.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListMenus.setFont(Common.font_list);
			jListMenus.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent evt)
				{
					int sel = jListMenus.getSelectedIndex();
					if (sel > -1)
					{
						JDBListData item = (JDBListData) jListMenus.getModel().getElementAt(sel);

						populateAssignedList(item);
						populateUnAssignedList(item);
						setButtonState();
						jButtonSave.setEnabled(false);
						jButtonUndo.setEnabled(false);
					}

				}
			});
			jListMenus.setModel(jListMenusModel);

			jScrollPaneUnAssigned = new JScrollPane4j(JScrollPane4j.UnAssigned);
			jDesktopPane1.add(jScrollPaneUnAssigned);
			jScrollPaneUnAssigned.setBounds(561, 25, 245, 434);
			jScrollPaneUnAssigned.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));

			ListModel<JDBListData> jListUnAssignedModel = new DefaultComboBoxModel<JDBListData>();
			jListUnAssigned = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
			jScrollPaneUnAssigned.setViewportView(jListUnAssigned);
			jListUnAssigned.setModel(jListUnAssignedModel);
			jListUnAssigned.setFont(Common.font_list);
			jListUnAssigned.setCellRenderer(Common.renderer_list_assigned);

			jScrollPaneAssigned = new JScrollPane4j(JScrollPane4j.Assigned);
			jDesktopPane1.add(jScrollPaneAssigned);
			jScrollPaneAssigned.setBounds(267, 25, 245, 434);
			jScrollPaneAssigned.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));

			ListModel<JDBListData> jListAssignedModel = new DefaultComboBoxModel<JDBListData>();
			jListAssigned = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
			jScrollPaneAssigned.setViewportView(jListAssigned);
			jListAssigned.setModel(jListAssignedModel);
			jListAssigned.setFont(Common.font_list);
			jListAssigned.setCellRenderer(Common.renderer_list_assigned);

			jButtonAssign = new JButton4j(Common.icon_arrow_left_16x16);
			jDesktopPane1.add(jButtonAssign);
			jButtonAssign.setBounds(524, 160, 25, 25);
			jButtonAssign.setEnabled(false);
			jButtonAssign.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					if (jListUnAssigned.getSelectedIndex() > -1)
					{
						for (int j = jListUnAssigned.getMaxSelectionIndex(); j >= jListUnAssigned.getMinSelectionIndex(); j--)
						{
							if (jListUnAssigned.isSelectedIndex(j))
							{
								JDBListData item = (JDBListData) jListUnAssigned.getModel().getElementAt(j);

								addToList(assignedList, item, false);
							}
						}

						for (int j = jListUnAssigned.getMaxSelectionIndex(); j >= jListUnAssigned.getMinSelectionIndex(); j--)
						{
							if (jListUnAssigned.isSelectedIndex(j))
							{
								Object item = jListUnAssigned.getModel().getElementAt(j);

								removeFromList(unassignedList, item);
							}
						}

						refreshJList(jListAssigned, assignedModel, assignedList);
						refreshJList(jListUnAssigned, unassignedModel, unassignedList);

						setButtonState();
					}

				}
			});

			jButtonUnAssign = new JButton4j(Common.icon_arrow_right_16x16);
			jDesktopPane1.add(jButtonUnAssign);
			jButtonUnAssign.setBounds(524, 189, 25, 25);
			jButtonUnAssign.setEnabled(false);
			jButtonUnAssign.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					if (jListAssigned.getSelectedIndex() > -1)
					{
						for (int j = jListAssigned.getMaxSelectionIndex(); j >= jListAssigned.getMinSelectionIndex(); j--)
						{
							if (jListAssigned.isSelectedIndex(j))
							{
								JDBListData item = (JDBListData) jListAssigned.getModel().getElementAt(j);

								addToList(unassignedList, item, true);
							}
						}

						for (int j = jListAssigned.getMaxSelectionIndex(); j >= jListAssigned.getMinSelectionIndex(); j--)
						{
							if (jListAssigned.isSelectedIndex(j))
							{
								Object item = jListAssigned.getModel().getElementAt(j);

								removeFromList(assignedList, item);
							}
						}
						refreshJList(jListUnAssigned, unassignedModel, unassignedList);
						refreshJList(jListAssigned, assignedModel, assignedList);

						setButtonState();
					}
				}
			});

			jButtonUp = new JButton4j(Common.icon_arrow_up_16x16);
			jDesktopPane1.add(jButtonUp);
			jButtonUp.setBounds(524, 130, 25, 25);
			jButtonUp.setEnabled(false);
			jButtonUp.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					int j = jListAssigned.getSelectedIndex();
					JDBListData element = (JDBListData) jListAssigned.getModel().getElementAt(j);
					assignedList = JDBModule.moveElementUp(assignedList, element);
					refreshJList(jListAssigned, assignedModel, assignedList);
					jListAssigned.setSelectedIndex(assignedList.indexOf(element));
					jButtonSave.setEnabled(true);
					jButtonUndo.setEnabled(true);
				}
			});

			jButtonDown = new JButton4j(Common.icon_arrow_down_16x16);
			jDesktopPane1.add(jButtonDown);
			jButtonDown.setBounds(524, 220, 25, 25);
			jButtonDown.setEnabled(false);
			jButtonDown.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					int j = jListAssigned.getSelectedIndex();
					JDBListData element = (JDBListData) jListAssigned.getModel().getElementAt(j);
					assignedList = JDBModule.moveElementDown(assignedList, element);
					refreshJList(jListAssigned, assignedModel, assignedList);
					jListAssigned.setSelectedIndex(assignedList.indexOf(element));
					jButtonSave.setEnabled(true);
					jButtonUndo.setEnabled(true);
				}
			});

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setBounds(510, 468, 116, 32);
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jLabel_Menu = new JLabel4j_title();
			jLabel_Menu.setHorizontalAlignment(SwingConstants.CENTER);
			jDesktopPane1.add(jLabel_Menu);
			jLabel_Menu.setText("Menu");
			jLabel_Menu.setBounds(0, 0, 245, 22);

			jLabel_UnAssigned = new JLabel4j_title();
			jLabel_UnAssigned.setHorizontalAlignment(SwingConstants.CENTER);
			jDesktopPane1.add(jLabel_UnAssigned);
			jLabel_UnAssigned.setText("Unassigned");
			jLabel_UnAssigned.setBounds(561, 0, 245, 22);

			jLabel_Assigned = new JLabel4j_title();
			jLabel_Assigned.setHorizontalAlignment(SwingConstants.CENTER);
			jDesktopPane1.add(jLabel_Assigned);
			jLabel_Assigned.setText("Assigned");
			jLabel_Assigned.setBounds(267, 0, 241, 22);

			jButtonSave = new JButton4j(Common.icon_update_16x16);
			jDesktopPane1.add(jButtonSave);
			jButtonSave.setText(lang.get("btn_Save"));
			jButtonSave.setBounds(156, 468, 116, 32);
			jButtonSave.setMnemonic(lang.getMnemonicChar());
			jButtonSave.setEnabled(false);
			jButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JDBMenus m = new JDBMenus(Common.selectedHostID, Common.sessionID);

					int j = jListMenus.getSelectedIndex();
					JDBListData menu = (JDBListData) jListMenus.getModel().getElementAt(j);
					m.rewriteMenu(menu.toString(), assignedList);
					jButtonSave.setEnabled(false);
					jButtonUndo.setEnabled(false);
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setBounds(392, 468, 116, 32);
			jButtonHelp.setMnemonic(lang.getMnemonicChar());

			jButtonUndo = new JButton4j(Common.icon_undo_16x16);
			jDesktopPane1.add(jButtonUndo);
			jButtonUndo.setText(lang.get("btn_Undo"));
			jButtonUndo.setBounds(274, 468, 116, 32);
			jButtonUndo.setEnabled(false);
			jButtonUndo.setMnemonic(lang.getMnemonicChar());
			jButtonUndo.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					int j = jListMenus.getSelectedIndex();
					if (j > -1)
					{
						JDBListData item = (JDBListData) jListMenus.getModel().getElementAt(j);
						populateUnAssignedList(item);
						populateAssignedList(item);
						jButtonSave.setEnabled(false);
						jButtonUndo.setEnabled(false);
					}
				}
			});

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
