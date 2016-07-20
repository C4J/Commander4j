package com.commander4j.sys;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
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
import com.commander4j.gui.JLabel4j_title;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameMenuStructure extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JScrollPane jScrollPaneUnAssigned;
	private JButton4j jButtonClose;
	private JDBModuleJList jListAssigned;
	private JDBModuleJList jListUnAssigned;
	private JDBModuleJList jListMenus;
	private JButton4j jButtonDown;
	private JButton4j jButtonUp;
	private JButton4j jButtonUnAssign;
	private JButton4j jButtonAssign;
	private JScrollPane jScrollPaneAssigned;
	private JScrollPane jScrollPaneMenus;
	private DefaultComboBoxModel<JDBListData> menuModel = new DefaultComboBoxModel<JDBListData>();
	private LinkedList<JDBListData> menuList = new LinkedList<JDBListData>();
	private DefaultComboBoxModel<JDBListData> unassignedModel = new DefaultComboBoxModel<JDBListData>();
	private LinkedList<JDBListData> unassignedList = new LinkedList<JDBListData>();
	private DefaultComboBoxModel<JDBListData> assignedModel = new DefaultComboBoxModel<JDBListData>();
	private LinkedList<JDBListData> assignedList = new LinkedList<JDBListData>();
	private JButton4j jButtonUndo;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JLabel4j_title jLabel3;
	private JLabel4j_title jLabel2;
	private JLabel4j_title jLabel1;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JInternalFrameMenuStructure()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MENU"));
		populateMenuList();
	}

	public void setButtonState() {
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

	private void populateMenuList() {
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

	private void refreshJList(JDBModuleJList jlist, DefaultComboBoxModel<JDBListData> comboboxmodel, LinkedList<JDBListData> linkedlist) {
		comboboxmodel.removeAllElements();

		for (int j = 0; j < linkedlist.size(); j++)
		{
			comboboxmodel.addElement(linkedlist.get(j));
		}
		ListModel<JDBListData> jList1Model = comboboxmodel;

		jlist.setModel(jList1Model);
	}

	private void populateUnAssignedList(JDBListData i) {
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
	}

	private void populateAssignedList(JDBListData i) {
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

		jListAssigned.setModel(jList1Model);
	}

	public void addToList(LinkedList<JDBListData> list, JDBListData newValue, boolean sort) {
		list.add(newValue);
		if (sort == true)
			Collections.sort(list);
	}

	public void removeFromList(LinkedList<JDBListData> list, Object oldValue) {
		list.remove(list.indexOf(oldValue));
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(761, 450));
			this.setBounds(0, 0, 839+Common.LFAdjustWidth, 465+Common.LFAdjustHeight);
			setVisible(true);
			this.setTitle("Menu Structure");
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.NORTH);
				jDesktopPane1.setPreferredSize(new Dimension(792, 429));
				jDesktopPane1.setLayout(null);
				{
					jScrollPaneMenus = new JScrollPane();
					jDesktopPane1.add(jScrollPaneMenus);
					jScrollPaneMenus.setBounds(10, 25, 245, 345);
					jScrollPaneMenus.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<JDBListData> jListMenusModel = new DefaultComboBoxModel<JDBListData>();
						jListMenus = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
						jScrollPaneMenus.setViewportView(jListMenus);
						jListMenus.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jListMenus.setFont(Common.font_list);
						jListMenus.addListSelectionListener(new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent evt) {
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
					}
				}
				{
					jScrollPaneUnAssigned = new JScrollPane();
					jDesktopPane1.add(jScrollPaneUnAssigned);
					jScrollPaneUnAssigned.setBounds(561, 25, 245, 345);
					jScrollPaneUnAssigned.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<JDBListData> jListUnAssignedModel = new DefaultComboBoxModel<JDBListData>();
						jListUnAssigned = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
						jScrollPaneUnAssigned.setViewportView(jListUnAssigned);
						jListUnAssigned.setModel(jListUnAssignedModel);
						jListUnAssigned.setFont(Common.font_list);
						jListUnAssigned.setCellRenderer(Common.renderer_list_assigned);
						jListUnAssigned.setBackground(Common.color_list_unassigned);
					}
				}
				{
					jScrollPaneAssigned = new JScrollPane();
					jDesktopPane1.add(jScrollPaneAssigned);
					jScrollPaneAssigned.setBounds(267, 25, 245, 345);
					jScrollPaneAssigned.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<JDBListData> jListAssignedModel = new DefaultComboBoxModel<JDBListData>();
						jListAssigned = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
						jScrollPaneAssigned.setViewportView(jListAssigned);
						jListAssigned.setModel(jListAssignedModel);
						jListAssigned.setFont(Common.font_list);
						jListAssigned.setCellRenderer(Common.renderer_list_assigned);
						jListAssigned.setBackground(Common.color_list_assigned);
					}
				}
				{
					jButtonAssign = new JButton4j(Common.icon_arrow_left);
					jDesktopPane1.add(jButtonAssign);
					jButtonAssign.setBounds(524, 160, 25, 25);
					jButtonAssign.setEnabled(false);
					jButtonAssign.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
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
				}
				{
					jButtonUnAssign = new JButton4j(Common.icon_arrow_right);
					jDesktopPane1.add(jButtonUnAssign);
					jButtonUnAssign.setBounds(524, 189, 25, 25);
					jButtonUnAssign.setEnabled(false);
					jButtonUnAssign.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
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
				}
				{
					jButtonUp = new JButton4j(Common.icon_arrow_up);
					jDesktopPane1.add(jButtonUp);
					jButtonUp.setBounds(524, 130, 25, 25);
					jButtonUp.setEnabled(false);
					jButtonUp.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							int j = jListAssigned.getSelectedIndex();
							JDBListData element = (JDBListData) jListAssigned.getModel().getElementAt(j);
							assignedList = JDBModule.moveElementUp(assignedList, element);
							refreshJList(jListAssigned, assignedModel, assignedList);
							jListAssigned.setSelectedIndex(assignedList.indexOf(element));
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);
						}
					});
				}
				{
					jButtonDown = new JButton4j(Common.icon_arrow_down);
					jDesktopPane1.add(jButtonDown);
					jButtonDown.setBounds(524, 220, 25, 25);
					jButtonDown.setEnabled(false);
					jButtonDown.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							int j = jListAssigned.getSelectedIndex();
							JDBListData element = (JDBListData) jListAssigned.getModel().getElementAt(j);
							assignedList = JDBModule.moveElementDown(assignedList, element);
							refreshJList(jListAssigned, assignedModel, assignedList);
							jListAssigned.setSelectedIndex(assignedList.indexOf(element));
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(504, 377, 116, 32);
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jLabel1 = new JLabel4j_title();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText("Menu");
					jLabel1.setBounds(12, 7, 60, 16);
				}
				{
					jLabel2 = new JLabel4j_title();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText("Unassigned");
					jLabel2.setBounds(561, 5, 136, 21);
				}
				{
					jLabel3 = new JLabel4j_title();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText("Assigned");
					jLabel3.setBounds(267, 5, 71, 18);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setBounds(150, 377, 116, 32);
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setEnabled(false);
					jButtonSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JDBMenus m = new JDBMenus(Common.selectedHostID, Common.sessionID);

							int j = jListMenus.getSelectedIndex();
							JDBListData menu = (JDBListData) jListMenus.getModel().getElementAt(j);
							m.rewriteMenu(menu.toString(), assignedList);
							jButtonSave.setEnabled(false);
							jButtonUndo.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(386, 377, 116, 32);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonUndo = new JButton4j(Common.icon_undo);
					jDesktopPane1.add(jButtonUndo);
					jButtonUndo.setText(lang.get("btn_Undo"));
					jButtonUndo.setBounds(268, 377, 116, 32);
					jButtonUndo.setEnabled(false);
					jButtonUndo.setMnemonic(lang.getMnemonicChar());
					jButtonUndo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
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
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
