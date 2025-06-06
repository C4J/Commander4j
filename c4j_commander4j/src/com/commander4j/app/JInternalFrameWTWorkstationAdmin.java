package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameWTWorkstationAdmin.java
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
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBWTWorkstation;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

import java.awt.Font;

/**
 * The JInternalFrameWTWorkstationAdmin class allows a user to maintain the table APP_WEIGHT_WORKSTATION.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTWorkstationAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBWTWorkstation JDBWTWorkstation
 * @see com.commander4j.app.JInternalFrameWTWorkstationAdmin JInternalFrameWTWorkstationAdmin
 *
 */
public class JInternalFrameWTWorkstationAdmin extends javax.swing.JInternalFrame
{
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JList4j<JDBWTWorkstation> jListWorkstations;
	private JButton4j jButtonRename;
	private JButton4j jButtonClose;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonEdit;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JScrollPane jScrollPane1;
	private String lWorkstation;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std label;

	private void addrecord() {
		JDBWTWorkstation u = new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);
		
		String me = JUtility.getClientName().toUpperCase();

		
		lWorkstation = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Workstation_Add"),me);
		if (lWorkstation != null)
		{
			if (lWorkstation.equals("") == false)
			{
				lWorkstation = lWorkstation.toUpperCase();
				if (u.create(lWorkstation) == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm_16x16);
				}
				else
				{
					JLaunchMenu.runForm("FRM_WEIGHT_WORKSTATION_EDIT", lWorkstation);
				}
				populateList(lWorkstation);
			}
		}
	}

	private void populateList(String defaultitem) {

		DefaultComboBoxModel<JDBWTWorkstation> DefComboBoxMod = new DefaultComboBoxModel<JDBWTWorkstation>();

		JDBWTWorkstation tempWorkstation = new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBWTWorkstation> tempWorkstationList = tempWorkstation.getWorkstations();
		int sel = -1;
		for (int j = 0; j < tempWorkstationList.size(); j++)
		{
			JDBWTWorkstation t = (JDBWTWorkstation) tempWorkstationList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getWorkstationID().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBWTWorkstation> jList1Model = DefComboBoxMod;
		jListWorkstations.setModel(jList1Model);
		jListWorkstations.setSelectedIndex(sel);
		jListWorkstations.setCellRenderer(Common.renderer_list);
		jListWorkstations.ensureIndexIsVisible(sel);
	}

	private void editRecord() {
		if (jListWorkstations.isSelectionEmpty() == false)
		{
			lWorkstation = ((JDBWTWorkstation) jListWorkstations.getSelectedValue()).getWorkstationID();
			JLaunchMenu.runForm("FRM_WEIGHT_WORKSTATION_EDIT", lWorkstation);
		}
	}

	private void delete() {
		if (jListWorkstations.isSelectionEmpty() == false)
		{
			lWorkstation = ((JDBWTWorkstation) jListWorkstations.getSelectedValue()).getWorkstationID();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Workstation_Delete") + " " + lWorkstation + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBWTWorkstation u = new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);
				u.setWorkstationID(lWorkstation);
				u.delete();
				populateList("");
			}
		}
	}

	private void print() {
		JLaunchReport.runReport("RPT_WORKSTATIONS",null,"",null,"");
	}

	private void rename() {
		if (jListWorkstations.isSelectionEmpty() == false)
		{
			String lworkstation_from = ((JDBWTWorkstation) jListWorkstations.getSelectedValue()).getWorkstationID();
			String lworkstation_to = new String();
			lworkstation_to = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Workstation_Rename"));
			if (lworkstation_to != null)
			{
				if (lworkstation_to.equals("") == false)
				{
					lworkstation_to = lworkstation_to.toUpperCase();
					JDBWTWorkstation u = new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);
					u.setWorkstationID(lworkstation_from);
					if (u.renameTo(lworkstation_to) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm_16x16);
					}
					populateList(lworkstation_to);
				}
			}
		}
	}

	private void excel() {
		JDBWTWorkstation work = new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("workstations.xls", work.getWorkstationDataResultSet(), Common.mainForm);
		populateList("");
	}

	public JInternalFrameWTWorkstationAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_WORKSTATION"));
		{
			label = new JLabel4j_std("Workstation ID           Sample Point             Scale ID       Location");
			label.setFont(new Font("Monospaced", Font.BOLD, 11));
			label.setBounds(8, 12, 518, 15);
			jDesktopPane1.add(label);
		}
		populateList("");
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(455, 518));
			this.setBounds(0, 0, 732, 322);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Workstation Admin");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(6, 27, 576, 257);
					{
						ListModel<JDBWTWorkstation> jList1Model = new DefaultComboBoxModel<JDBWTWorkstation>();
						jListWorkstations = new JList4j<JDBWTWorkstation>();
						jScrollPane1.setViewportView(jListWorkstations);
						jListWorkstations.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_WORKSTATION_EDIT") == true)
									{
										editRecord();
									}
								}
							}
						});
						jListWorkstations.setModel(jList1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jListWorkstations, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										addrecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_WORKSTATION_ADD"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_WORKSTATION_DELETE"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_WORKSTATION_EDIT"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_WORKSTATION_RENAME"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_WORKSTATION"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										excel();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Excel"));
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
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(594, 4, 125, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_WORKSTATION_ADD"));
					jButtonAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							addrecord();

						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete_16x16);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setBounds(594, 35, 125, 32);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_WORKSTATION_DELETE"));
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
					jButtonEdit.setBounds(594, 66, 125, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_WORKSTATION_EDIT"));
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
					jButtonRename.setBounds(594, 97, 125, 32);
					jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_WORKSTATION_RENAME"));
					jButtonRename.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							rename();

						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_print_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(594, 128, 125, 32);
					jButtonPrint.setEnabled(true);
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
					jButtonHelp.setBounds(594, 221, 125, 32);
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.setBounds(594, 190, 125, 32);
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
					jButtonClose.setBounds(594, 252, 125, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(594, 159, 125, 32);
					jButtonExcel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							excel();
						}
					});
					jDesktopPane1.add(jButtonExcel);
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
