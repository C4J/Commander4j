package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameMHNDecisionAdmin.java
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

import javax.swing.DefaultComboBoxModel;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMHNDecisions;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameMHNDecisionAdmin class allows the user to insert/update/delete
 * the APP_MHN_DECISIONS table.
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMHNDecisionAdmin.jpg" >
 *
 * @see com.commander4j.app.JInternalFrameMHNDecisionProperties
 *      JInternalFrameMHNDecisionProperties
 * @see com.commander4j.db.JDBMHNDecisions JDBMHNDecisions
 */
public class JInternalFrameMHNDecisionAdmin extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JButton4j jButtonAdd;
	private JButton4j jButtonClose;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonRename;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JList4j<JDBMHNDecisions> jListDecisions;
	private JScrollPane4j jScrollPane1;
	private String ldecision;

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

	public JInternalFrameMHNDecisionAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MHN_DECISION"));
		populateList("");
	}

	private void create()
	{
		JDBMHNDecisions u = new JDBMHNDecisions(Common.selectedHostID, Common.sessionID);
		ldecision = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Decision_Add"));
		if (ldecision != null)
		{
			if (ldecision.equals("") == false)
			{

				if (u.create(ldecision, "", "Blocked") == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
				}
				else
				{
					JLaunchMenu.runForm("FRM_ADMIN_MHN_DECISION_EDIT", ldecision);
				}
				populateList(ldecision);
			}
		}
	}

	private void delete()
	{
		if (jListDecisions.isSelectionEmpty() == false)
		{
			ldecision = ((JDBMHNDecisions) jListDecisions.getSelectedValue()).getDecision();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Decision_Delete") + " " + ldecision + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBMHNDecisions u = new JDBMHNDecisions(Common.selectedHostID, Common.sessionID);
				u.setDecision(ldecision);
				u.delete();
				populateList("");
			}
		}
	}

	private void editRecord()
	{
		if (jListDecisions.isSelectionEmpty() == false)
		{
			ldecision = ((JDBMHNDecisions) jListDecisions.getSelectedValue()).getDecision();
			JLaunchMenu.runForm("FRM_ADMIN_MHN_DECISION_EDIT", ldecision);
		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(375, 402));
			this.setBounds(0, 0, 676, 419);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Decision Admin");
			{
				jDesktopPane1 = new JDesktopPane4j();

				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);

				jButtonAdd = new JButton4j(Common.icon_add_16x16);
				jDesktopPane1.add(jButtonAdd);
				jButtonAdd.setText(lang.get("btn_Add"));
				jButtonAdd.setMnemonic(lang.getMnemonicChar());
				jButtonAdd.setBounds(539, 0, 126, 32);
				jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DECISION_ADD"));
				jButtonAdd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						create();

					}
				});

				jButtonDelete = new JButton4j(Common.icon_delete_16x16);
				jDesktopPane1.add(jButtonDelete);
				jButtonDelete.setText(lang.get("btn_Delete"));
				jButtonDelete.setMnemonic(lang.getMnemonicChar());
				jButtonDelete.setBounds(539, 31, 126, 32);
				jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DECISION_DELETE"));
				jButtonDelete.setFocusTraversalKeysEnabled(false);
				jButtonDelete.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						delete();

					}
				});

				jButtonEdit = new JButton4j(Common.icon_edit_16x16);
				jDesktopPane1.add(jButtonEdit);
				jButtonEdit.setText(lang.get("btn_Edit"));
				jButtonEdit.setMnemonic(lang.getMnemonicChar());
				jButtonEdit.setBounds(539, 62, 126, 32);
				jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DECISION_EDIT"));
				jButtonEdit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						editRecord();
					}
				});

				jButtonRename = new JButton4j(Common.icon_rename_16x16);
				jDesktopPane1.add(jButtonRename);
				jButtonRename.setText(lang.get("btn_Rename"));
				jButtonRename.setMnemonic(lang.getMnemonicChar());
				jButtonRename.setBounds(539, 93, 126, 32);
				jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DECISION_RENAME"));
				jButtonRename.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						rename();

					}
				});

				jButtonPrint = new JButton4j(Common.icon_report_16x16);
				jDesktopPane1.add(jButtonPrint);
				jButtonPrint.setText(lang.get("btn_Print"));
				jButtonPrint.setMnemonic(lang.getMnemonicChar());
				jButtonPrint.setBounds(539, 124, 126, 32);
				jButtonPrint.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						print();
					}
				});

				jButtonHelp = new JButton4j(Common.icon_help_16x16);
				jDesktopPane1.add(jButtonHelp);
				jButtonHelp.setText(lang.get("btn_Help"));
				jButtonHelp.setMnemonic(lang.getMnemonicChar());
				jButtonHelp.setBounds(539, 155, 126, 32);

				jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
				jDesktopPane1.add(jButtonRefresh);
				jButtonRefresh.setText(lang.get("btn_Refresh"));
				jButtonRefresh.setMnemonic(lang.getMnemonicChar());
				jButtonRefresh.setBounds(539, 186, 126, 32);
				jButtonRefresh.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						populateList("");
					}
				});

				jButtonClose = new JButton4j(Common.icon_close_16x16);
				jDesktopPane1.add(jButtonClose);
				jButtonClose.setText(lang.get("btn_Close"));
				jButtonClose.setMnemonic(lang.getMnemonicChar());
				jButtonClose.setBounds(539, 217, 126, 32);
				jButtonClose.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						dispose();
					}
				});

				jScrollPane1 = new JScrollPane4j(JScrollPane4j.List);
				jDesktopPane1.add(jScrollPane1);
				jScrollPane1.setBounds(0, 0, 538, 387);

				ListModel<JDBMHNDecisions> jList1Model = new DefaultComboBoxModel<JDBMHNDecisions>();
				jListDecisions = new JList4j<JDBMHNDecisions>();
				jListDecisions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				jScrollPane1.setViewportView(jListDecisions);
				jListDecisions.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent evt)
					{
						if (evt.getClickCount() == 2)
						{
							if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DECISION_EDIT") == true)
							{
								editRecord();
							}
						}
					}
				});
				jListDecisions.setModel(jList1Model);

				final JPopupMenu popupMenu = new JPopupMenu();
				addPopup(jListDecisions, popupMenu);

				{
					final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
					newItemMenuItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							create();
						}
					});
					newItemMenuItem.setText(lang.get("btn_Add"));
					newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DECISION_ADD"));
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
					newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DECISION_DELETE"));
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
					newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DECISION_EDIT"));
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
					newItemMenuItem.setText(lang.get("btn_Rename"));
					newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DECISION_RENAME"));
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
					newItemMenuItem.setEnabled(true);
					popupMenu.add(newItemMenuItem);
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void populateList(String defaultitem)
	{

		DefaultComboBoxModel<JDBMHNDecisions> DefComboBoxMod = new DefaultComboBoxModel<JDBMHNDecisions>();

		JDBMHNDecisions tempType = new JDBMHNDecisions(Common.selectedHostID, Common.sessionID);
		Vector<JDBMHNDecisions> tempTypeList = tempType.getDecisions();
		int sel = -1;
		for (int j = 0; j < tempTypeList.size(); j++)
		{
			JDBMHNDecisions t = (JDBMHNDecisions) tempTypeList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getDecision().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBMHNDecisions> jList1Model = DefComboBoxMod;
		jListDecisions.setModel(jList1Model);
		jListDecisions.setSelectedIndex(sel);
		jListDecisions.setCellRenderer(Common.renderer_list);
		jListDecisions.ensureIndexIsVisible(sel);
	}

	private void print()
	{
		JLaunchReport.runReport("RPT_DECISIONS", null, "", null, "");
	}

	private void rename()
	{
		if (jListDecisions.isSelectionEmpty() == false)
		{
			String ldecision_from = ((JDBMHNDecisions) jListDecisions.getSelectedValue()).getDecision();
			String ldecision_to = new String();
			ldecision_to = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Decision_Rename"));
			if (ldecision_to != null)
			{
				if (ldecision_to.equals("") == false)
				{
					// ldecision_to = ldecision_to.toUpperCase();
					JDBMHNDecisions u = new JDBMHNDecisions(Common.selectedHostID, Common.sessionID);
					u.setDecision(ldecision_from);
					if (u.renameTo(ldecision_to) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
					}
					populateList(ldecision_to);
				}
			}
		}
	}
}
