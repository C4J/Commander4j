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

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import javax.swing.JPopupMenu;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBShiftNames;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JRadioButton4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameShiftNameAdmin extends JInternalFrame
{
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
	private JList4j<JDBShiftNames> jListShifts;
	private JRadioButton4j rdbtnDisabled;
	private JRadioButton4j rdbtnEnabled;
	private JScrollPane4j jScrollPane1;
	private String lshift;
	private static final long serialVersionUID = 1;

	private void delete()
	{
		if (jListShifts.isSelectionEmpty() == false)
		{
			lshift = ((JDBShiftNames) jListShifts.getSelectedValue()).getShiftID();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Shift_Delete") + " " + lshift + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBShiftNames u = new JDBShiftNames(Common.selectedHostID, Common.sessionID);
				u.setShiftID(lshift);
				u.delete();
				populateList("");
			}
		}
	}

	private void rename()
	{
		if (jListShifts.isSelectionEmpty() == false)
		{
			String ldecision_from = ((JDBShiftNames) jListShifts.getSelectedValue()).getShiftID();
			String lshiftID_to = new String();
			lshiftID_to = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Shift_Rename"));
			if (lshiftID_to != null)
			{
				if (lshiftID_to.equals("") == false)
				{
					JDBShiftNames u = new JDBShiftNames(Common.selectedHostID, Common.sessionID);
					u.setShiftID(ldecision_from);
					if (u.renameTo(lshiftID_to) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
					}
					populateList(lshiftID_to);
				}
			}
		}
	}

	private void create()
	{
		JDBShiftNames u = new JDBShiftNames(Common.selectedHostID, Common.sessionID);
		lshift = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Shift_Add"));
		if (lshift != null)
		{
			if (lshift.equals("") == false)
			{
				if (u.create(lshift, "", "Y") == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
				}
				else
				{

					JLaunchMenu.runForm("FRM_ADMIN_SHIFT_NAMES_EDIT", lshift);
				}
				populateList(lshift);
			}
		}
	}

	private void print()
	{
		JLaunchReport.runReport("RPT_SHIFT_NAMES", null, "", null, "");
	}

	private void populateList(String defaultitem)
	{

		DefaultComboBoxModel<JDBShiftNames> DefComboBoxMod = new DefaultComboBoxModel<JDBShiftNames>();

		JDBShiftNames tempType = new JDBShiftNames(Common.selectedHostID, Common.sessionID);
		Vector<JDBShiftNames> tempTypeList = tempType.getShiftNames(rdbtnEnabled.isSelected());
		int sel = -1;
		for (int j = 0; j < tempTypeList.size(); j++)
		{
			JDBShiftNames t = (JDBShiftNames) tempTypeList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getShiftID().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBShiftNames> jList1Model = DefComboBoxMod;
		jListShifts.setModel(jList1Model);
		jListShifts.setSelectedIndex(sel);
		jListShifts.setCellRenderer(Common.renderer_list);
		jListShifts.ensureIndexIsVisible(sel);
	}

	public JInternalFrameShiftNameAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_SHIFT_NAMES"));
		populateList("");
	}

	private void editRecord()
	{
		if (jListShifts.isSelectionEmpty() == false)
		{
			lshift = ((JDBShiftNames) jListShifts.getSelectedValue()).getShiftID();
			JLaunchMenu.runForm("FRM_ADMIN_SHIFT_NAMES_EDIT", lshift);
		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(375, 402));
			this.setBounds(0, 0, 411, 419);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Shift Name Admin");

			jDesktopPane1 = new JDesktopPane4j();

			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jButtonAdd = new JButton4j(Common.icon_add_16x16);
			jDesktopPane1.add(jButtonAdd);
			jButtonAdd.setText(lang.get("btn_Add"));
			jButtonAdd.setMnemonic(lang.getMnemonicChar());
			jButtonAdd.setBounds(274, 0, 126, 32);
			jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_NAMES_ADD"));
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
			jButtonDelete.setBounds(274, 31, 126, 32);
			jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_NAMES_DELETE"));
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
			jButtonEdit.setBounds(274, 62, 126, 32);
			jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_NAMES_EDIT"));
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
			jButtonRename.setBounds(274, 93, 126, 32);
			jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_NAMES_RENAME"));
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
			jButtonPrint.setBounds(274, 124, 126, 32);
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
			jButtonHelp.setBounds(274, 155, 126, 32);

			jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
			jDesktopPane1.add(jButtonRefresh);
			jButtonRefresh.setText(lang.get("btn_Refresh"));
			jButtonRefresh.setMnemonic(lang.getMnemonicChar());
			jButtonRefresh.setBounds(274, 186, 126, 32);
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
			jButtonClose.setBounds(274, 217, 126, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			ButtonGroup bgroup = new ButtonGroup();
			JPanel4j panel = new JPanel4j();
			panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			panel.setBounds(274, 261, 125, 68);
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

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.List);
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.setBounds(0, 0, 273, 387);

			ListModel<JDBShiftNames> jList1Model = new DefaultComboBoxModel<JDBShiftNames>();
			jListShifts = new JList4j<JDBShiftNames>();
			jListShifts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jScrollPane1.setViewportView(jListShifts);
			jListShifts.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent evt)
				{
					if (evt.getClickCount() == 2)
					{
						if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_NAMES_EDIT") == true)
						{
							editRecord();
						}
					}
				}
			});
			jListShifts.setModel(jList1Model);

			final JPopupMenu popupMenu = new JPopupMenu();
			addPopup(jListShifts, popupMenu);

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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_NAMES_ADD"));
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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_NAMES_DELETE"));
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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_NAMES_EDIT"));
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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_NAMES_RENAME"));
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
