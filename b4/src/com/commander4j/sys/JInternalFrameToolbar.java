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
import javax.swing.border.BevelBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBModuleJList;
import com.commander4j.db.JDBToolbar;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_title;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameToolbar extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JScrollPane jScrollPaneUnAssigned;
	private JButton4j jButtonAssign;
	private JButton4j jButtonClose;
	private JButton4j jButtonUndo;
	private JDBModuleJList jListUnAssigned;
	private JDBModuleJList jListAssigned;
	private JButton4j jButtonSave;
	private JButton4j jButtonHelp;
	private JButton4j jButtonDown;
	private JButton4j jButtonUp;
	private JButton4j jButtonUnAssign;
	private JScrollPane jScrollPaneAssigned;
	private DefaultComboBoxModel<JDBListData> unassignedModel = new DefaultComboBoxModel<JDBListData>();
	private LinkedList<JDBListData> unassignedList = new LinkedList<JDBListData>();
	private DefaultComboBoxModel<JDBListData> assignedModel = new DefaultComboBoxModel<JDBListData>();
	private LinkedList<JDBListData> assignedList = new LinkedList<JDBListData>();
	private JLabel4j_title jLabel2;
	private JLabel4j_title jLabel1;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JInternalFrameToolbar()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_TOOLBAR"));
		populateAssignedList();
		populateUnAssignedList();
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

	public void addToList(LinkedList<JDBListData> list, JDBListData newValue, boolean sort) {
		list.add(newValue);
		if (sort == true)
			Collections.sort(list);
	}

	public void removeFromList(LinkedList<JDBListData> list, Object oldValue) {
		list.remove(list.indexOf(oldValue));
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

	private void populateAssignedList() {
		assignedModel.removeAllElements();

		JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);

		assignedList = mod.getModulesAssignedtoToolbar();

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

	private void populateUnAssignedList() {
		unassignedModel.removeAllElements();

		JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);

		unassignedList = mod.getModulesUnAssignedtoToolbar();

		if (unassignedList.size() > 0)
		{
			for (int j = 0; j < unassignedList.size(); j++)
			{
				unassignedModel.addElement(unassignedList.get(j));
			}
		}

		ListModel<JDBListData> jList1Model = unassignedModel;

		jListUnAssigned.setModel(jList1Model);
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(538, 440));
			this.setBounds(0, 0, 584, 496);
			setVisible(true);
			this.setTitle("Toolbar Maintenance");
			this.setClosable(true);
			this.setIconifiable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.NORTH);
				jDesktopPane1.setPreferredSize(new Dimension(536, 450));
				jDesktopPane1.setLayout(null);
				{
					jScrollPaneAssigned = new JScrollPane();
					jDesktopPane1.add(jScrollPaneAssigned);
					jScrollPaneAssigned.setBounds(10, 25, 245, 371);
					jScrollPaneAssigned.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<JDBListData> jListAssignedModel = new DefaultComboBoxModel<JDBListData>();
						jListAssigned = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
						jScrollPaneAssigned.setViewportView(jListAssigned);
						jListAssigned.setBackground(Common.color_list_assigned);
						jListAssigned.setCellRenderer(Common.renderer_list_assigned);
						jListAssigned.setModel(jListAssignedModel);
					}
				}
				{
					jScrollPaneUnAssigned = new JScrollPane();
					jDesktopPane1.add(jScrollPaneUnAssigned);
					jScrollPaneUnAssigned.setBounds(304, 25, 245, 371);
					jScrollPaneUnAssigned.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<JDBListData> jListUnAssignedModel = new DefaultComboBoxModel<JDBListData>();
						jListUnAssigned = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
						jScrollPaneUnAssigned.setViewportView(jListUnAssigned);
						jListUnAssigned.setBackground(Common.color_list_unassigned);
						jListUnAssigned.setCellRenderer(Common.renderer_list_unassigned);
						jListUnAssigned.setModel(jListUnAssignedModel);
					}
				}
				{
					jButtonAssign = new JButton4j(Common.icon_arrow_left);
					jDesktopPane1.add(jButtonAssign);
					jButtonAssign.setBounds(267, 151, 25, 25);
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
					jButtonUnAssign.setBounds(267, 186, 25, 25);
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
					jButtonUp.setBounds(267, 118, 25, 25);
					jButtonUp.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							int sel = jListAssigned.getSelectedIndex();
							JDBListData element = ((JDBListData) jListAssigned.getModel().getElementAt(sel));
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
					jButtonDown.setBounds(267, 223, 25, 25);
					jButtonDown.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							int j = jListAssigned.getSelectedIndex();
							JDBListData element = ((JDBListData) jListAssigned.getModel().getElementAt(j));
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
					jButtonClose.setBounds(387, 408, 110, 32);
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(275, 408, 110, 32);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonSave = new JButton4j(Common.icon_update);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setBounds(45, 408, 112, 32);
					jButtonSave.setEnabled(false);
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JDBToolbar t = new JDBToolbar(Common.selectedHostID, Common.sessionID);
							t.rewriteToolbar(assignedList);
							jButtonSave.setEnabled(false);
							jButtonUndo.setEnabled(false);
						}
					});
				}
				{
					jButtonUndo = new JButton4j(Common.icon_undo);
					jDesktopPane1.add(jButtonUndo);
					jButtonUndo.setText(lang.get("btn_Undo"));
					jButtonUndo.setBounds(160, 408, 112, 32);
					jButtonUndo.setEnabled(false);
					jButtonUndo.setMnemonic(lang.getMnemonicChar());
					jButtonUndo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							populateAssignedList();
							populateUnAssignedList();
							jButtonSave.setEnabled(false);
							jButtonUndo.setEnabled(false);
						}
					});
				}
				{
					jLabel1 = new JLabel4j_title();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Assigned"));
					jLabel1.setBounds(10, 5, 220, 18);
				}
				{
					jLabel2 = new JLabel4j_title();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Unassigned"));
					jLabel2.setBounds(304, 5, 245, 18);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
