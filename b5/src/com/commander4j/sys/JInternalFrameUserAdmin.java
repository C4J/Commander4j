package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameUserAdmin.java
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
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBUser;
import com.commander4j.db.JDBUserJList;
import com.commander4j.db.JDBUserReport;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameUserAdmin extends javax.swing.JInternalFrame {
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JDBUserJList jListUsers;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonRename;
	private JButton4j jButtonPrint;
	private JButton4j jButtonExport;	
	private JButton4j jButtonPermissions;
	private JButton4j jButtonAdd;
	private JButton4j jButtonDelete;
	private JButton4j jButtonClose;
	private JButton4j jButtonUpdate;
	private JScrollPane jScrollPane1;
	private String luser_id;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JInternalFrameUserAdmin()
	{
		super();

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_USERS"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		populateList("");
	}

	private void addrecord()
	{
		JDBUser u = new JDBUser(Common.selectedHostID, Common.sessionID);

		luser_id = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_User_Create"));
		if (luser_id != null)
		{
			if (luser_id.equals("") == false)
			{
				luser_id = luser_id.toUpperCase();

				u.setUserId(luser_id);
				// if (u.create(luser_id, "password", "", true, true, false,
				// "EN", false, "") == false)
				if (u.isValidUserId())
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, "UserId already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
				} else
				{
					JLaunchMenu.runDialog("FRM_ADMIN_USER_EDIT", luser_id);
				}
				populateList(luser_id);
			}
		}
	}

	private void copyrecord()
	{

		if (jListUsers.isSelectionEmpty() == false)
		{
			// String luser_id = new String();
			String oldUserID = ((JDBUser) jListUsers.getSelectedValue().getObject()).getUserId();
			//String oldUserID = ((JDBListData) jListUsers.getSelectedValue()).toString();
			JDBUser u = new JDBUser(Common.selectedHostID, Common.sessionID);

			String newUserID = JUtility.replaceNullStringwithBlank(JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_User_Create")));

			if (newUserID.equals("") == false)
			{
				newUserID = newUserID.toUpperCase();

				if (u.copyTo(oldUserID, newUserID, Common.userList.getUser(Common.sessionID).getUserId()) == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
				} else
				{
					JLaunchMenu.runDialog("FRM_ADMIN_USER_EDIT", newUserID);
				}
				populateList(newUserID);
			}

		}
	}

	private void delete()
	{
		if (jListUsers.isSelectionEmpty() == false)
		{
			// String luser_id = new String();
			luser_id = ((JDBUser) jListUsers.getSelectedValue().getObject()).getUserId();
			//luser_id = ((JDBListData) jListUsers.getSelectedValue()).toString();
			if ((luser_id.equals(Common.userList.getUser(Common.sessionID).getUserId()) == false) && (luser_id.toLowerCase().equals("interface")==false))
			{
				int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_User_Delete") + " " + luser_id, lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
				if (n == 0)
				{
					JDBUser u = new JDBUser(Common.selectedHostID, Common.sessionID);
					u.delete(luser_id, Common.userList.getUser(Common.sessionID).getUserId());
					populateList("");
				}
			} else
			{
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(null, "Cannot delete user " + luser_id + " !", "Information", JOptionPane.WARNING_MESSAGE, Common.icon_confirm_16x16);
			}
		}
	}

	private void permissions()
	{
		if (jListUsers.isSelectionEmpty() == false)
		{
			luser_id = ((JDBUser) jListUsers.getSelectedValue().getObject()).getUserId();
			JLaunchMenu.runForm("FRM_ADMIN_USER_PERM", luser_id);
		}
	}

	private void rename()
	{
		if (jListUsers.isSelectionEmpty() == false)
		{
			String luser_id_from = ((JDBUser) jListUsers.getSelectedValue().getObject()).getUserId();
			//String luser_id_from = ((JDBListData) jListUsers.getSelectedValue()).toString();
			String luser_id_to = new String();
			luser_id_to = JOptionPane.showInputDialog(Common.mainForm, "Rename to user id",luser_id_from);
			if (luser_id_to != null)
			{
				if (luser_id_to.equals("") == false)
				{
					luser_id_to = luser_id_to.toUpperCase();
					JDBUser u = new JDBUser(Common.selectedHostID, Common.sessionID);
					u.setUserId(luser_id_from);
					if (u.renameTo(luser_id_to) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
					} else
					{
						JDBUserReport userReport = new JDBUserReport(Common.selectedHostID, Common.sessionID);
						userReport.updateUserID(luser_id_from, luser_id_to);
						userReport = null;
					}
					populateList(luser_id_to);
				}
			}
		}
	}

	private void populateList(String defaultitem)
	{
		DefaultComboBoxModel<JDBListData> DefComboBoxMod = new DefaultComboBoxModel<JDBListData>();

		JDBUser tempUser = new JDBUser(Common.selectedHostID, Common.sessionID);
		LinkedList<JDBListData> tempUserList = tempUser.getUserIds();

		int sel = -1;
		for (int j = 0; j < tempUserList.size(); j++)
		{
			DefComboBoxMod.addElement(tempUserList.get(j));
			if (((JDBUser) tempUserList.get(j).getObject()).getUserId().equals(defaultitem))
			{
				sel = j;
			}
/*			if (tempUserList.get(j).toString().equals(defaultitem))
			{
				sel = j;
			}*/
		}
		ListModel<JDBListData> jList1Model = DefComboBoxMod;
		jListUsers.setModel(jList1Model);
		jListUsers.setSelectedIndex(sel);
		jListUsers.setCellRenderer(Common.renderer_list);
		jListUsers.ensureIndexIsVisible(sel);
	}

	private void print()
	{
		JLaunchReport.runReport("RPT_USERS", null, "", null, "");
	}

	private void lock()
	{
		luser_id = ((JDBUser) jListUsers.getSelectedValue().getObject()).getUserId();
		JDBUser user = new JDBUser(Common.selectedHostID, Common.sessionID);
		user.getUserProperties(luser_id);
		user.lock(luser_id, Common.userList.getUser(Common.sessionID).getUserId());
		user.update();
		populateList(luser_id);
	}

	private void unlock()
	{
		luser_id = ((JDBUser) jListUsers.getSelectedValue().getObject()).getUserId();
		JDBUser user = new JDBUser(Common.selectedHostID, Common.sessionID);
		user.getUserProperties(luser_id);
		user.unlock(Common.userList.getUser(Common.sessionID).getUserId());
		user.update();
		populateList(luser_id);
	}

	private void editRecord()
	{
		if (jListUsers.isSelectionEmpty() == false)
		{
			luser_id = ((JDBUser) jListUsers.getSelectedValue().getObject()).getUserId();
			JLaunchMenu.runDialog("FRM_ADMIN_USER_EDIT", luser_id);
			populateList(luser_id);
		}
	}

	private void excel()
	{
		JDBUser user = new JDBUser(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("users.xls", user.getUserDataResultSet(), Common.mainForm);
		populateList("");
	}

	private void export()
	{
		JDBUser user = new JDBUser(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("user_permissions.xls", user.getUserPermissionsResultSet(), Common.mainForm);
		populateList("");
	}
	
	
	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(273, 474));
			this.setBounds(0, 0, 570, 574);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("User Admin");
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
					jScrollPane1.setBounds(0, 0, 425, 542);
					jScrollPane1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<JDBListData> jList1Model = new DefaultComboBoxModel<JDBListData>();
						jListUsers = new JDBUserJList(Common.selectedHostID, Common.sessionID);
						jScrollPane1.setViewportView(jListUsers);
						jListUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jListUsers.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_EDIT") == true)
									{
										permissions();
									}
								}
							}
						});
						jListUsers.setModel(jList1Model);
						jListUsers.setFont(Common.font_list);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jListUsers, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										addrecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_ADD"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										delete();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Delete"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_DELETE"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										editRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Edit"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_EDIT"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_permissions_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										permissions();
									}
								});
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_PERM"));
								newItemMenuItem.setText(lang.get("btn_Permissions"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_rename_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										rename();
									}
								});
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_RENAME"));
								newItemMenuItem.setText(lang.get("btn_Rename"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_print_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										print();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Print"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_USERS"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										excel();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Excel"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenu4j accountMenu = new JMenu4j();
								accountMenu.setText(lang.get("lbl_User_Account"));
								accountMenu.setIcon(Common.icon_user_16x16);
								popupMenu.add(accountMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											lock();
										}
									});
									newItemMenuItem.setText(lang.get("lbl_User_Account_Lock"));
									accountMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											unlock();
										}
									});
									newItemMenuItem.setText(lang.get("lbl_User_Account_Unlock"));
									accountMenu.add(newItemMenuItem);
								}
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
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
					jButtonAdd.setBounds(431, 5, 125, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_ADD"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							addrecord();
						}
					});
				}
				{

					jButtonDelete = new JButton4j(Common.icon_delete_16x16);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setBounds(431, 36, 125, 32);
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_DELETE"));
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							delete();

						}
					});
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_edit_16x16);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setText(lang.get("btn_Edit"));
					jButtonUpdate.setBounds(431, 67, 125, 32);
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_EDIT"));
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(431, 346, 125, 32);
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jButtonPermissions = new JButton4j(Common.icon_permissions_16x16);
					jDesktopPane1.add(jButtonPermissions);
					jButtonPermissions.setText(lang.get("btn_Permissions"));
					jButtonPermissions.setBounds(431, 98, 125, 32);
					jButtonPermissions.setMnemonic(lang.getMnemonicChar());
					jButtonPermissions.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_PERM"));
					jButtonPermissions.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							permissions();
						}
					});
				}
				{

					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setBounds(431, 191, 125, 32);
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_USERS"));
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							print();
						}
					});
				}
				{

					jButtonExport = new JButton4j(Common.icon_export_16x16);
					jDesktopPane1.add(jButtonExport);
					jButtonExport.setText(lang.get("btn_Export"));
					jButtonExport.setBounds(431, 222, 125, 32);
					jButtonExport.setMnemonic(lang.getMnemonicChar());
					jButtonExport.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_PERM_EXPORT"));
					jButtonExport.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							export();
						}
					});
				}
				
				{
					jButtonRename = new JButton4j(Common.icon_rename_16x16);
					jDesktopPane1.add(jButtonRename);
					jButtonRename.setText(lang.get("btn_Rename"));
					jButtonRename.setMnemonic(lang.getMnemonicChar());
					jButtonRename.setBounds(431, 129, 125, 32);
					jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_RENAME"));
					jButtonRename.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							rename();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(431, 315, 125, 32);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setBounds(431, 253, 125, 32);
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							populateList("");
						}
					});
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							excel();

						}
					});

					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(431, 284, 125, 32);
					jDesktopPane1.add(jButtonExcel);
				}

				{

					JButton4j jButtonCopy = new JButton4j(Common.icon_copy_16x16);
					jButtonCopy.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							copyrecord();
						}
					});
					jButtonCopy.setText(lang.get("btn_Copy"));
					jButtonCopy.setMnemonic(lang.getMnemonicChar());
					jButtonCopy.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_COPY"));
					jButtonCopy.setBounds(431, 160, 125, 32);
					jDesktopPane1.add(jButtonCopy);
				}

				
				{
					JLabel4j_std lblUserDisabled = new JLabel4j_std(lang.get("lbl_Account_Disabled"));
					lblUserDisabled.setBounds(432, 391, 125, 32);
					lblUserDisabled.setIcon(Common.icon_user_disabled_16x16);
					jDesktopPane1.add(lblUserDisabled);
				}
				
				{
					
					JLabel4j_std lblUserLocked = new JLabel4j_std(lang.get("lbl_Account_Locked"));
					lblUserLocked.setBounds(432, 426, 125, 32);
					lblUserLocked.setIcon(Common.icon_user_locked_16x16);
					jDesktopPane1.add(lblUserLocked);
				}
					
				{
					JLabel4j_std lblUserExpired = new JLabel4j_std(lang.get("lbl_Account_Expired"));
					lblUserExpired.setBounds(432, 461, 125, 32);
					lblUserExpired.setIcon(Common.icon_user_expired_16x16);
					jDesktopPane1.add(lblUserExpired);
				}

			}
		} catch (Exception e)
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
	private static void addPopup(Component component, final JPopupMenu popup)
	{
		component.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger())
					showMenu(e);
			}

			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e)
			{
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
