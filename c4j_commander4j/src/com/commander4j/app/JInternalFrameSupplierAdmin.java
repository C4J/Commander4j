package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameSampleReasonAdmin.java
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
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBSuppliers;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JRadioButton4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;


public class JInternalFrameSupplierAdmin extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonRename;
	private JButton4j jButtonClose;
	private JList4j<JDBSuppliers> jListSuppliers;
	private JScrollPane jScrollPane1;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonAdd;
	private String lSupplierID;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBControl control = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JRadioButton4j rdbtnEnabled;
	private JRadioButton4j rdbtnDisabled;

	private void delete() {
		if (jListSuppliers.isSelectionEmpty() == false)
		{
			lSupplierID = ((JDBSuppliers) jListSuppliers.getSelectedValue()).getSupplierID();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Supplier_Delete") + " " + lSupplierID + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBSuppliers u = new JDBSuppliers(Common.selectedHostID, Common.sessionID);
				u.setSupplierID(lSupplierID);
				u.delete();
				populateList("");
			}
		}
	}

	private void rename() {
		if (jListSuppliers.isSelectionEmpty() == false)
		{
			String lreason_from = ((JDBSuppliers) jListSuppliers.getSelectedValue()).getSupplierID();
			String lreason_to = new String();
			lreason_to = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Supplier_Rename"));
			if (lreason_to != null)
			{
				if (lreason_to.equals("") == false)
				{
					lreason_to = lreason_to.toUpperCase();
					JDBSuppliers u = new JDBSuppliers(Common.selectedHostID, Common.sessionID);
					u.setSupplierID(lreason_from);
					if (u.renameTo(lreason_to) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm_16x16);
					}
					populateList(lreason_to);
				}
			}
		}
	}

	private void create() {
		JDBSuppliers u = new JDBSuppliers(Common.selectedHostID, Common.sessionID);
		lSupplierID = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Supplier_Add"));
		if (lSupplierID != null)
		{
			if (lSupplierID.equals("") == false)
			{
				lSupplierID = lSupplierID.toUpperCase();
				if (u.create(lSupplierID,control.getKeyValueWithDefault("DEFAULT SUPPLIER TYPE", "LAMINATE", "New Default Supplier Type"), "") == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm_16x16);
				}
				else
				{
					JLaunchMenu.runForm("FRM_ADMIN_SUPPLIER_EDIT", lSupplierID);
				}
				populateList(lSupplierID);
			}
		}
	}

	private void print() {
		JLaunchReport.runReport("RPT_SUPPLIERS",null,"",null,"");
	}

	private void populateList(String defaultitem) {

		DefaultComboBoxModel<JDBSuppliers> DefComboBoxMod = new DefaultComboBoxModel<JDBSuppliers>();

		JDBSuppliers tempType = new JDBSuppliers(Common.selectedHostID, Common.sessionID);
		Vector<JDBSuppliers> tempTypeList = tempType.getSuppliers(rdbtnEnabled.isSelected());
		int sel = -1;
		for (int j = 0; j < tempTypeList.size(); j++)
		{
			JDBSuppliers t = (JDBSuppliers) tempTypeList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getSupplierID().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBSuppliers> jList1Model = DefComboBoxMod;
		jListSuppliers.setModel(jList1Model);
		jListSuppliers.setSelectedIndex(sel);
		jListSuppliers.setCellRenderer(Common.renderer_list);
		jListSuppliers.ensureIndexIsVisible(sel);
	}

	public JInternalFrameSupplierAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_SUPPLIER"));
		populateList("");
	}

	private void editRecord() {
		if (jListSuppliers.isSelectionEmpty() == false)
		{
			lSupplierID = ((JDBSuppliers) jListSuppliers.getSelectedValue()).getSupplierID();
			JLaunchMenu.runForm("FRM_ADMIN_SUPPLIER_EDIT", lSupplierID);
		}
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(375, 402));
			this.setBounds(0, 0, 522, 494);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Supplier Admin");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(380, 5, 126, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SUPPLIER_ADD"));
					jButtonAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							create();

						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete_16x16);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setBounds(380, 36, 126, 32);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SUPPLIER_DELETE"));
					jButtonDelete.setFocusTraversalKeysEnabled(false);
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
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setBounds(380, 67, 126, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SUPPLIER_EDIT"));
					jButtonEdit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							editRecord();
						}
					});
				}
				{
					jButtonRename = new JButton4j(Common.icon_rename_16x16);
					jDesktopPane1.add(jButtonRename);
					jButtonRename.setText(lang.get("btn_Rename"));
					jButtonRename.setMnemonic(lang.getMnemonicChar());
					jButtonRename.setBounds(380, 98, 126, 32);
					jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SUPPLIER_RENAME"));
					jButtonRename.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							rename();

						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(380, 129, 126, 32);
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							print();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(380, 160, 126, 32);
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.setBounds(380, 191, 126, 32);
					jButtonRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							populateList("");
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(380, 223, 126, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 0, 374, 462);
					{
						ListModel<JDBSuppliers> jList1Model = new DefaultComboBoxModel<JDBSuppliers>();
						jListSuppliers = new JList4j<JDBSuppliers>();
						jListSuppliers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jScrollPane1.setViewportView(jListSuppliers);
						jListSuppliers.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SUPPLIER_EDIT") == true)
									{
										editRecord();
									}
								}
							}
						});
						jListSuppliers.setModel(jList1Model);
					}

					{
						final JPopupMenu popupMenu = new JPopupMenu();
						addPopup(jListSuppliers, popupMenu);

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									create();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Add"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SUPPLIER_ADD"));
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
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SUPPLIER_DELETE"));
							popupMenu.add(newItemMenuItem);
						}

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit_16x16);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									editRecord();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Edit"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SUPPLIER_EDIT"));
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
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SUPPLIER_RENAME"));
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
							newItemMenuItem.setEnabled(true);
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
						
						ButtonGroup bgroup = new ButtonGroup();
						JPanel panel = new JPanel();
						panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
						panel.setBounds(380, 267, 125, 68);
						jDesktopPane1.add(panel);
						panel.setLayout(null);

						rdbtnEnabled = new JRadioButton4j(lang.get("lbl_Enabled"));
						rdbtnEnabled.setBounds(8, 8, 102, 22);
						panel.add(rdbtnEnabled);
						rdbtnEnabled.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								populateList("");
							}
						});
						rdbtnEnabled.setSelected(true);
						bgroup.add(rdbtnEnabled);

						rdbtnDisabled = new JRadioButton4j(lang.get("lbl_Disabled"));
						rdbtnDisabled.setBounds(8, 35, 102, 22);
						panel.add(rdbtnDisabled);
						rdbtnDisabled.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								populateList("");
							}
						});
						rdbtnDisabled.setSelected(false);
						bgroup.add(rdbtnDisabled);						
						
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
