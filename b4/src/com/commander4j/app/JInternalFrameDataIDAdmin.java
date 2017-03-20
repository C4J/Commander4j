package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameDataIDAdmin.java
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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.commander4j.db.JDBDataIDs;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameDataIDAdmin extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JList4j<JDBDataIDs> jListDataIDs;
	private JScrollPane jScrollPane1;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonAdd;
	private String lmattype;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	private void delete() {
		if (jListDataIDs.isSelectionEmpty() == false)
		{
			lmattype = ((JDBDataIDs) jListDataIDs.getSelectedValue()).getID();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Data_ID_Delete") + " " + lmattype + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (question == 0)
			{
				JDBDataIDs u = new JDBDataIDs(Common.selectedHostID, Common.sessionID);
				u.setID(lmattype);
				u.delete();
				populateList("");
			}
		}
	}

	private void addrecord() {
		JDBDataIDs u = new JDBDataIDs(Common.selectedHostID, Common.sessionID);
		lmattype = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Data_ID_Add"));
		if (lmattype != null)
		{
			if (lmattype.equals("") == false)
			{
				lmattype = lmattype.toUpperCase();
				if (u.create(lmattype) == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);
				}
				else
				{
					JLaunchMenu.runForm("FRM_ADMIN_DATA_IDS_EDIT", lmattype);
				}
				populateList(lmattype);
			}
		}
	}

	private void print() {
		JLaunchReport.runReport("RPT_DATA_IDS",null,"",null,"");
	}

	private void populateList(String defaultitem) {

		DefaultComboBoxModel<JDBDataIDs> DefComboBoxMod = new DefaultComboBoxModel<JDBDataIDs>();

		JDBDataIDs tempType = new JDBDataIDs(Common.selectedHostID, Common.sessionID);
		LinkedList<JDBDataIDs> tempTypeList = tempType.getDataIDs();
		int sel = -1;
		for (int j = 0; j < tempTypeList.size(); j++)
		{
			JDBDataIDs t = (JDBDataIDs) tempTypeList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getID().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBDataIDs> jList1Model = DefComboBoxMod;
		jListDataIDs.setModel(jList1Model);
		jListDataIDs.setSelectedIndex(sel);
		jListDataIDs.setCellRenderer(Common.renderer_list);
		jListDataIDs.ensureIndexIsVisible(sel);
	}

	public JInternalFrameDataIDAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_DATA_IDS"));
		populateList("");
	}

	private void editRecord() {
		if (jListDataIDs.isSelectionEmpty() == false)
		{
			lmattype = ((JDBDataIDs) jListDataIDs.getSelectedValue()).getID();
			JLaunchMenu.runDialog("FRM_ADMIN_DATA_IDS_EDIT", lmattype);
			populateList(lmattype);
		}
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(375, 402));
			this.setBounds(0, 0, 541, 415);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Customer Moterial Data IDS");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					jButtonAdd = new JButton4j(Common.icon_add);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(385, 7, 126, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_DATA_IDS_ADD"));
					jButtonAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							addrecord();

						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setBounds(385, 36, 126, 32);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_DATA_IDS_DELETE"));
					jButtonDelete.setFocusTraversalKeysEnabled(false);
					jButtonDelete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							delete();

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setBounds(385, 65, 126, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_DATA_IDS_EDIT"));
					jButtonEdit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							editRecord();
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());;
					jButtonPrint.setBounds(385, 93, 126, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_DATA_IDS"));
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							print();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(385, 122, 126, 32);
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.setBounds(385, 151, 126, 32);
					jButtonRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							populateList("");
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(385, 180, 126, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(7, 7, 366, 357);
					{
						ListModel<JDBDataIDs> jList1Model = new DefaultComboBoxModel<JDBDataIDs>();
						jListDataIDs = new JList4j<JDBDataIDs>();
						jListDataIDs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jScrollPane1.setViewportView(jListDataIDs);
						jListDataIDs.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_DATA_IDS_EDIT") == true)
									{
										editRecord();
									}
								}
							}
						});
						jListDataIDs.setModel(jList1Model);
					}

					{
						final JPopupMenu popupMenu = new JPopupMenu();
						addPopup(jListDataIDs, popupMenu);

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									addrecord();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Add"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_DATA_IDS_ADD"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									delete();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Delete"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_DATA_IDS_DELETE"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									editRecord();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Edit"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_DATA_IDS_EDIT"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_print);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									print();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Print"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_DATA_IDS"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh);
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
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * WindowBuilder generated method.<br>
	 * Please don't remove this method or its invocations.<br>
	 * It used by WindowBuilder to associate the {@link javax.swing.JPopupMenu}
	 * with parent.
	 */
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
