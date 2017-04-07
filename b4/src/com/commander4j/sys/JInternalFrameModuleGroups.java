package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameModuleGroups.java
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBModule;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameModuleGroups class displays a list of all the groups which contain the selected module.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameModuleGroups.jpg" >
 * 
 * @see com.commander4j.db.JDBModule JDBModule
 * @see com.commander4j.sys.JInternalFrameModuleAdmin JInternalFrameModuleAdmin
 * @see com.commander4j.sys.JInternalFrameModuleProperties JInternalFrameModuleProperties
 */
public class JInternalFrameModuleGroups extends javax.swing.JInternalFrame
{
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JList4j<String> jListGroups;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonPermissions;
	private JButton4j jButtonClose;
	private JScrollPane jScrollPane1;
	private String lgroup_id;
	private String lmodule_id;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JInternalFrameModuleGroups(String lmodule_id)
	{
		super();

		initGUI();
		setModuleID(lmodule_id);
		
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MODULE_MEMBERS"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

	}

	public void setModuleID(String lmodule_id)
	{
		this.lmodule_id = lmodule_id;
		this.setTitle("Groups using ["+lmodule_id+"]");		
		populateList("");

	}
	
	private String getModuleID()
	{
		return this.lmodule_id;
	}
	
	private void permissions() {
		if (jListGroups.isSelectionEmpty() == false)
		{
			lgroup_id = jListGroups.getSelectedValue().toString();
			JLaunchMenu.runForm("FRM_ADMIN_GROUP_PERM", lgroup_id);
		}
	}


	private void populateList(String defaultitem) {
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		JDBModule tempGroup = new JDBModule(Common.selectedHostID, Common.sessionID);
		LinkedList<String> tempGroupList = tempGroup.getGroupsAssigned(lmodule_id);
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

	private void print() {
		
		PreparedStatement stmt;

		try
		{
			stmt = Common.hostList.getHost(Common.selectedHostID).getConnection(Common.sessionID).prepareStatement(Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBModule.getGroupsAssigned"));
			stmt.setString(1, lmodule_id);
			JLaunchReport.runReport("RPT_GROUPS",null,"",stmt,"");

		} catch (SQLException e)
		{

		}

	}


	private void excel() {
		JDBModule moduleMembership = new JDBModule(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("Groups_Using_Module_"+getModuleID()+".xls", moduleMembership.getGroupsAssignedDataResultSet(getModuleID()), Common.mainForm);
		populateList("");
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(273, 474));
			this.setBounds(0, 0, 368+Common.LFAdjustWidth, 574+Common.LFAdjustHeight);
			setVisible(true);
			this.setClosable(true);

			this.setIconifiable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(235, 269));
				jDesktopPane1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(10, 10, 190, 503);
					jScrollPane1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<String> jList1Model = new DefaultComboBoxModel<String>();
						jListGroups = new JList4j<String>();
						jScrollPane1.setViewportView(jListGroups);
						jListGroups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

						jListGroups.setModel(jList1Model);
						jListGroups.setFont(Common.font_list);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jListGroups, popupMenu);


							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_permissions);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										permissions();
									}
								});
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_PERM"));
								newItemMenuItem.setText(lang.get("btn_Permissions"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_USERS"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										excel();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Excel"));
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
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(212, 166, 125, 32);
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jButtonPermissions = new JButton4j(Common.icon_permissions);
					jDesktopPane1.add(jButtonPermissions);
					jButtonPermissions.setText(lang.get("btn_Permissions"));
					jButtonPermissions.setBounds(212, 10, 125, 32);
					jButtonPermissions.setMnemonic(lang.getMnemonicChar());
					jButtonPermissions.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_PERM"));
					jButtonPermissions.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							permissions();
						}
					});
				}
				{

					jButtonPrint = new JButton4j(Common.icon_report);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setBounds(212, 42, 125, 32);
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_USERS"));
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
					jButtonHelp.setBounds(212, 135, 125, 32);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setBounds(212, 73, 125, 32);
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							populateList("");
						}
					});
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS);
					jButtonExcel.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							excel();

						}
					});

					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(212, 104, 125, 32);
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
