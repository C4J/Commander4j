package com.commander4j.app;

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

import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBWasteLocation;
import com.commander4j.db.JDBWasteLocationJList;
import com.commander4j.db.JDBWasteLocationReporting;
import com.commander4j.db.JDBWasteReportingIDS;
import com.commander4j.db.JDBWasteReportingIDSJList;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_title;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameMenuStructure class allows you to customise the menu
 * structure within the application and updates the table SYS_MENUS
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMenuStructure.jpg" >
 *
 * @see com.commander4j.db.JDBMenus JDBMenus
 */
public class JInternalFrameWasteLocationReporting extends javax.swing.JInternalFrame
{
	private DefaultComboBoxModel<JDBListData> assignedModel = new DefaultComboBoxModel<JDBListData>();
	private DefaultComboBoxModel<JDBListData> menuModel = new DefaultComboBoxModel<JDBListData>();
	private DefaultComboBoxModel<JDBListData> unassignedModel = new DefaultComboBoxModel<JDBListData>();
	private JButton4j jButtonAssign;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonSave;
	private JButton4j jButtonUnAssign;
	private JButton4j jButtonUndo;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBWasteLocationJList jListMenus;
	private JDBWasteReportingIDSJList jListAssigned;
	private JDBWasteReportingIDSJList jListUnAssigned;
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_title jLabel_Waste_Location;
	private JLabel4j_title jLabel_UnAssigned;
	private JLabel4j_title jLabel_Assigned;
	private JScrollPane4j jScrollPaneAssigned;
	private JScrollPane4j jScrollPaneMenus;
	private JScrollPane4j jScrollPaneUnAssigned;
	private LinkedList<JDBListData> assignedList = new LinkedList<JDBListData>();
	private LinkedList<JDBListData> menuList = new LinkedList<JDBListData>();
	private LinkedList<JDBListData> unassignedList = new LinkedList<JDBListData>();
	private static final long serialVersionUID = 1;

	public JInternalFrameWasteLocationReporting()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WASTE_LOCATION_REPORTING"));
		populateLocationList();
	}

	public void setButtonState()
	{
		jButtonSave.setEnabled(true);
		jButtonUndo.setEnabled(true);

		jButtonUnAssign.setEnabled(false);
		jButtonAssign.setEnabled(false);

		if (unassignedList.size() > 0)
		{
			jButtonAssign.setEnabled(true);
		}

		if (assignedList.size() > 0)
		{
			jButtonUnAssign.setEnabled(true);

		}

	}

	private void populateLocationList()
	{
		menuModel.removeAllElements();

		JDBWasteLocation mod = new JDBWasteLocation(Common.selectedHostID, Common.sessionID);

		this.setIconifiable(true);

		menuList = mod.getWasteLocations(true, JDBWasteLocation.displayModeShort);

		for (int j = 0; j < menuList.size(); j++)
		{
			if (((JDBWasteLocation) menuList.get(j).getObject()).isEnabled())
			{
				menuModel.addElement(menuList.get(j));
			}
		}

		ListModel<JDBListData> jList1Model = menuModel;

		jListMenus.setCellRenderer(Common.renderer_list);
		jListMenus.setModel(jList1Model);
	}

	private void refreshJList(JDBWasteReportingIDSJList jlist, DefaultComboBoxModel<JDBListData> comboboxmodel, LinkedList<JDBListData> linkedlist)
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

		JDBWasteReportingIDS repids = new JDBWasteReportingIDS(Common.selectedHostID, Common.sessionID);
		repids.setDisplayMode(JDBWasteReportingIDS.displayModeShort);

		unassignedList = repids.getReportingIDsUnassignedtoLocation(i.toString());

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
		jListUnAssigned.setCellRenderer(Common.renderer_list_unassigned);
		jListUnAssigned.setBackground(Common.color_list_background_unassigned);
		jListUnAssigned.setModel(jList1Model);
	}

	private void populateAssignedList(JDBListData i)
	{
		assignedModel.removeAllElements();

		JDBWasteReportingIDS repids = new JDBWasteReportingIDS(Common.selectedHostID, Common.sessionID);
		repids.setDisplayMode(JDBWasteReportingIDS.displayModeShort);

		assignedList = repids.getWasteReportingIDsAssignedtoLocation(i.toString());

		if (assignedList.size() > 0)
		{
			for (int j = 0; j < assignedList.size(); j++)
			{
				assignedModel.addElement(assignedList.get(j));
			}
		}

		ListModel<JDBListData> jList1Model = assignedModel;
		jListAssigned.setCellRenderer(Common.renderer_list_assigned);
		jListAssigned.setBackground(Common.color_list_background_assigned);
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
			this.setBounds(0, 0, 825, 551);
			setVisible(true);
			this.setTitle("Location Reporting Groups");
			this.setClosable(true);
			getContentPane().setLayout(null);

			jDesktopPane1 = new JDesktopPane4j();
			jDesktopPane1.setBounds(0, 0, 829, 532);

			this.getContentPane().add(jDesktopPane1);
			jDesktopPane1.setPreferredSize(new Dimension(792, 429));
			jDesktopPane1.setLayout(null);

			jScrollPaneMenus = new JScrollPane4j(JScrollPane4j.List);
			jDesktopPane1.add(jScrollPaneMenus);
			jScrollPaneMenus.setBounds(10, 30, 245, 434);

			ListModel<JDBListData> jListMenusModel = new DefaultComboBoxModel<JDBListData>();
			jListMenus = new JDBWasteLocationJList(Common.selectedHostID, Common.sessionID);
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
						populateUnAssignedList(item);
						populateAssignedList(item);
						setButtonState();
						jButtonSave.setEnabled(false);
						jButtonUndo.setEnabled(false);
					}
				}
			});
			jListMenus.setModel(jListMenusModel);

			jScrollPaneUnAssigned = new JScrollPane4j(JScrollPane4j.UnAssigned);
			jDesktopPane1.add(jScrollPaneUnAssigned);
			jScrollPaneUnAssigned.setBounds(561, 30, 245, 434);

			ListModel<JDBListData> jListUnAssignedModel = new DefaultComboBoxModel<JDBListData>();
			jListUnAssigned = new JDBWasteReportingIDSJList(Common.selectedHostID, Common.sessionID);
			jListUnAssigned.setBackground(Common.color_list_background_unassigned);
			jScrollPaneUnAssigned.setViewportView(jListUnAssigned);
			jListUnAssigned.setModel(jListUnAssignedModel);
			jListUnAssigned.setCellRenderer(Common.renderer_list_assigned);

			jScrollPaneAssigned = new JScrollPane4j(JScrollPane4j.Assigned);
			jDesktopPane1.add(jScrollPaneAssigned);
			jScrollPaneAssigned.setBounds(267, 30, 245, 434);

			ListModel<JDBListData> jListAssignedModel = new DefaultComboBoxModel<JDBListData>();

			jListAssigned = new JDBWasteReportingIDSJList(Common.selectedHostID, Common.sessionID);
			jListAssigned.setBackground(Common.color_list_background_assigned);
			jScrollPaneAssigned.setViewportView(jListAssigned);
			jListAssigned.setModel(jListAssignedModel);
			jListAssigned.setCellRenderer(Common.renderer_list_assigned);

			jButtonAssign = new JButton4j(Common.icon_arrow_left_16x16);
			jDesktopPane1.add(jButtonAssign);
			jButtonAssign.setBounds(523, 195, 25, 25);
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
			jButtonUnAssign.setBounds(523, 224, 25, 25);
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

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setBounds(592, 476, 116, 32);
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
			jDesktopPane1.add(jButtonRefresh);
			jButtonRefresh.setText(lang.get("btn_Refresh"));
			jButtonRefresh.setBounds(356, 476, 116, 32);
			jButtonRefresh.setMnemonic(lang.getMnemonicChar());
			jButtonRefresh.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					populateLocationList();
					assignedModel.removeAllElements();
					unassignedModel.removeAllElements();
				}
			});

			jLabel_Waste_Location = new JLabel4j_title();
			jDesktopPane1.add(jLabel_Waste_Location);
			jLabel_Waste_Location.setText("Waste Location");
			jLabel_Waste_Location.setBounds(10, 7, 226, 22);

			jLabel_UnAssigned = new JLabel4j_title();
			jDesktopPane1.add(jLabel_UnAssigned);
			jLabel_UnAssigned.setText("Unassigned Reporting ID");
			jLabel_UnAssigned.setBounds(561, 7, 245, 22);

			jLabel_Assigned = new JLabel4j_title();
			jDesktopPane1.add(jLabel_Assigned);
			jLabel_Assigned.setText("Assigned Reporting ID");
			jLabel_Assigned.setBounds(267, 7, 241, 22);

			jButtonSave = new JButton4j(Common.icon_update_16x16);
			jDesktopPane1.add(jButtonSave);
			jButtonSave.setText(lang.get("btn_Save"));
			jButtonSave.setBounds(120, 476, 116, 32);
			jButtonSave.setMnemonic(lang.getMnemonicChar());
			jButtonSave.setEnabled(false);
			jButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JDBWasteLocationReporting m = new JDBWasteLocationReporting(Common.selectedHostID, Common.sessionID);

					String temp = ((JDBWasteLocation) jListMenus.getSelectedValue().getObject()).getWasteLocationID();
					m.rewriteReportIDSAssignedToLocation(temp.toString(), assignedList);
					jButtonSave.setEnabled(false);
					jButtonUndo.setEnabled(false);
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setBounds(474, 476, 116, 32);
			jButtonHelp.setMnemonic(lang.getMnemonicChar());

			jButtonUndo = new JButton4j(Common.icon_undo_16x16);
			jDesktopPane1.add(jButtonUndo);
			jButtonUndo.setText(lang.get("btn_Undo"));
			jButtonUndo.setBounds(238, 476, 116, 32);
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
