package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameSampleDefectAdmin.java
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

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBSampleDefectIDs;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JRadioButton4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;


public class JInternalFrameSampleDefectIDAdmin extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonRename;
	private JButton4j jButtonClose;
	private JList4j<JDBSampleDefectIDs> jListDefects;
	private JScrollPane jScrollPane1;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonAdd;
	private String ldefect;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JRadioButton4j rdbtnEnabled;
	private JRadioButton4j rdbtnDisabled;

	private void delete() {
		if (jListDefects.isSelectionEmpty() == false)
		{
			ldefect = ((JDBSampleDefectIDs) jListDefects.getSelectedValue()).getSampleDefectID();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Defect_ID_Delete") + " " + ldefect + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBSampleDefectIDs u = new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID);
				u.setSampleDefectID(ldefect);
				u.delete();
				populateList("");
			}
		}
	}

	private void rename() {
		if (jListDefects.isSelectionEmpty() == false)
		{
			String ldefect_from = ((JDBSampleDefectIDs) jListDefects.getSelectedValue()).getSampleDefectID();
			String ldefect_to = new String();
			ldefect_to = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Defect_ID_Rename"));
			if (ldefect_to != null)
			{
				if (ldefect_to.equals("") == false)
				{
					ldefect_to = ldefect_to.toUpperCase();
					JDBSampleDefectIDs u = new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID);
					u.setSampleDefectID(ldefect_from);
					if (u.renameTo(ldefect_to) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm_16x16);
					}
					populateList(ldefect_to);
				}
			}
		}
	}

	private void create() {
		JDBSampleDefectIDs u = new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID);
		ldefect = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Defect_ID_Add"));
		if (ldefect != null)
		{
			if (ldefect.equals("") == false)
			{
				ldefect = ldefect.toUpperCase();
				if (u.create(ldefect, "") == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm_16x16);
				}
				else
				{
					JLaunchMenu.runForm("FRM_ADMIN_SAMPLE_DEFECT_ID_EDIT", ldefect);
				}
				populateList(ldefect);
			}
		}
	}

	private void print() {
		JLaunchReport.runReport("RPT_SAMPLE_DEFECT_ID",null,"",null,"");
	}

	private void populateList(String defaultitem) {

		DefaultComboBoxModel<JDBSampleDefectIDs> DefComboBoxMod = new DefaultComboBoxModel<JDBSampleDefectIDs>();

		JDBSampleDefectIDs tempType = new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID);
		Vector<JDBSampleDefectIDs> tempTypeList = tempType.getSampleDefects(rdbtnEnabled.isSelected());
		int sel = -1;
		for (int j = 0; j < tempTypeList.size(); j++)
		{
			JDBSampleDefectIDs t = (JDBSampleDefectIDs) tempTypeList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getSampleDefectID().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBSampleDefectIDs> jList1Model = DefComboBoxMod;
		jListDefects.setModel(jList1Model);
		jListDefects.setSelectedIndex(sel);
		jListDefects.setCellRenderer(Common.renderer_list);
		jListDefects.ensureIndexIsVisible(sel);
	}

	public JInternalFrameSampleDefectIDAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_SAMPLE_DEFECT"));
		populateList("");
	}

	private void editRecord() {
		if (jListDefects.isSelectionEmpty() == false)
		{
			ldefect = ((JDBSampleDefectIDs) jListDefects.getSelectedValue()).getSampleDefectID();
			JLaunchMenu.runForm("FRM_ADMIN_SAMPLE_DEFECT_ID_EDIT", ldefect);
		}
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(375, 402));
			this.setBounds(0, 0, 624, 506);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Defect Mode Admin");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(476, 0, 126, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SAMPLE_DEFECT_ID_ADD"));
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
					jButtonDelete.setBounds(476, 31, 126, 32);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SAMPLE_DEFECT_ID_DELETE"));
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
					jButtonEdit.setBounds(476, 62, 126, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SAMPLE_DEFECT_ID_EDIT"));
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
					jButtonRename.setBounds(476, 93, 126, 32);
					jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SAMPLE_DEFECT_ID_RENAME"));
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
					jButtonPrint.setBounds(476, 124, 126, 32);
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
					jButtonHelp.setBounds(476, 155, 126, 32);
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.setBounds(476, 186, 126, 32);
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
					jButtonClose.setBounds(476, 218, 126, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 0, 464, 474);
					{
						ListModel<JDBSampleDefectIDs> jList1Model = new DefaultComboBoxModel<JDBSampleDefectIDs>();
						jListDefects = new JList4j<JDBSampleDefectIDs>();
						jListDefects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jScrollPane1.setViewportView(jListDefects);
						jListDefects.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SAMPLE_DEFECT_ID_EDIT") == true)
									{
										editRecord();
									}
								}
							}
						});
						jListDefects.setModel(jList1Model);
					}

					{
						final JPopupMenu popupMenu = new JPopupMenu();
						addPopup(jListDefects, popupMenu);

						{
							final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
							newItemMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									create();
								}
							});
							newItemMenuItem.setText(lang.get("btn_Add"));
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SAMPLE_DEFECT_ID_ADD"));
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
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SAMPLE_DEFECT_ID_DELETE"));
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
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SAMPLE_DEFECT_ID_EDIT"));
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
							newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SAMPLE_DEFECT_ID_RENAME"));
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
					}
					
					ButtonGroup bgroup = new ButtonGroup();
					JPanel panel = new JPanel();
					panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
					panel.setBounds(476, 258, 125, 68);
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
