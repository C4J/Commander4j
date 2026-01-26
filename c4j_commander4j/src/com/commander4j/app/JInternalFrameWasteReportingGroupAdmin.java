package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameTNEAdmin.java
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

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JOptionPane;

import javax.swing.JPopupMenu;

import javax.swing.ListModel;
import javax.swing.border.EtchedBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBWasteReportingGroup;
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
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWasteReportingGroupAdmin class allows a user to maintain
 * the table APP_WASTE_REPORTING_GROUPS.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWasteReasonAdmin.jpg" >
 *
 * @see com.commander4j.db.JDBWasteReportingGroup
 * @see com.commander4j.app.JInternalFrameWasteReportingGroupAdmin
 *
 *
 */
public class JInternalFrameWasteReportingGroupAdmin extends javax.swing.JInternalFrame
{
	private JButton4j jButtonAdd;
	private JButton4j jButtonClose;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonExcel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonRename;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JList4j<JDBListData> jListGroups;
	private JRadioButton4j rdbtnDisabled;
	private JRadioButton4j rdbtnEnabled;
	private JScrollPane4j jScrollPane1;
	private String lGroupString;
	private static final long serialVersionUID = 1;

	private void addrecord()
	{
		JDBWasteReportingGroup u = new JDBWasteReportingGroup(Common.selectedHostID, Common.sessionID);
		lGroupString = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Waste_Reporting_Group_Add"));

		if (lGroupString != null)
		{
			if (lGroupString.equals("") == false)
			{
				boolean validNumber = false;
				try
				{
					Integer.valueOf(lGroupString);
					validNumber = true;
				}
				catch (NumberFormatException ex)
				{
					validNumber = false;
				}

				if (validNumber)
				{
					u.clear();
					if (u.create(Integer.valueOf(lGroupString)) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
					}
					else
					{
						JLaunchMenu.runForm("FRM_WASTE_REPORTING_GROUP_EDIT", lGroupString);
					}

					populateList(Integer.valueOf(lGroupString));
				}
				else
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, "Reporting Group has to be a number", "Error", JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
				}
			}
		}
	}

	private void populateList(int defaultitem)
	{
		DefaultComboBoxModel<JDBListData> DefComboBoxMod = new DefaultComboBoxModel<JDBListData>();
		JDBWasteReportingGroup x;
		JDBWasteReportingGroup tempLocation = new JDBWasteReportingGroup(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBListData> tempLocationList = tempLocation.getWasteReportingGroups(rdbtnEnabled.isSelected());

		int sel = -1;
		for (int j = 0; j < tempLocationList.size(); j++)
		{

			DefComboBoxMod.addElement(tempLocationList.get(j));

			x = ((JDBWasteReportingGroup) tempLocationList.get(j).getObject());

			if (x.getWasteReportGroup() == defaultitem)
			{
				sel = j;
			}

		}
		ListModel<JDBListData> jList1Model = DefComboBoxMod;
		jListGroups.setModel(jList1Model);
		jListGroups.setSelectedIndex(sel);
		jListGroups.setCellRenderer(Common.renderer_list);
		jListGroups.ensureIndexIsVisible(sel);

	}

	private void editRecord()
	{
		if (jListGroups.isSelectionEmpty() == false)
		{
			JDBWasteReportingGroup x;

			x = ((JDBWasteReportingGroup) jListGroups.getSelectedValue().getObject());

			lGroupString = String.valueOf(x.getWasteReportGroup());

			JLaunchMenu.runForm("FRM_WASTE_REPORTING_GROUP_EDIT", lGroupString);
		}
	}

	private void delete()
	{
		if (jListGroups.isSelectionEmpty() == false)
		{

			JDBWasteReportingGroup x;

			x = ((JDBWasteReportingGroup) jListGroups.getSelectedValue().getObject());

			lGroupString = String.valueOf(x.getWasteReportGroup());

			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Waste_Reporting_Group_Delete") + " " + lGroupString + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{

				x.setWasteReportingGroup(Integer.valueOf(lGroupString));

				x.delete();
				populateList(-1);
			}
		}
	}

	private void renameRecord()
	{
		if (jListGroups.isSelectionEmpty() == false)
		{
			JDBWasteReportingGroup x;

			x = ((JDBWasteReportingGroup) jListGroups.getSelectedValue().getObject());

			lGroupString = String.valueOf(x.getWasteReportGroup());

			String lMaterialStringgNEW = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Waste_Reporting_Group_Rename"), lGroupString);

			if (lMaterialStringgNEW != null)
			{
				if (lMaterialStringgNEW.equals("") == false)
				{
					lMaterialStringgNEW = lMaterialStringgNEW.toUpperCase();

					if (x.rename(Integer.valueOf(lGroupString), Integer.valueOf(lMaterialStringgNEW)) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, x.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
					}
					else
					{

						lGroupString = lMaterialStringgNEW;
					}

					populateList(-1);
				}
			}

		}
	}

	private void print()
	{
		JLaunchReport.runReport("RPT_WASTE_REPORTING_GROUP", null, "", null, "");
	}

	private void excel()
	{
		JDBWasteReportingGroup samp = new JDBWasteReportingGroup(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("Waste_Reporting_Group.xls", samp.getWasteReportingGroupDataResultSet(), Common.mainForm);
		populateList(-1);
	}

	public JInternalFrameWasteReportingGroupAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_WASTE_REPORTING_GROUP"));

		populateList(-1);
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(455, 518));
			this.setBounds(0, 0, 638, 543);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Waste Reporting Group Admin");

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.List);
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.setBounds(0, 0, 500, 512);

			ListModel<JDBListData> jList1Model = new DefaultComboBoxModel<JDBListData>();
			jListGroups = new JList4j<JDBListData>();
			jScrollPane1.setViewportView(jListGroups);
			jListGroups.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent evt)
				{
					if (evt.getClickCount() == 2)
					{
						if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_REPORTING_GROUP_EDIT") == true)
						{
							editRecord();
						}
					}
				}
			});
			jListGroups.setModel(jList1Model);

			final JPopupMenu popupMenu = new JPopupMenu();
			addPopup(jListGroups, popupMenu);

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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_REPORTING_GROUP_ADD"));
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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_REPORTING_GROUP_DELETE"));
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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_REPORTING_GROUP_EDIT"));
				popupMenu.add(newItemMenuItem);
			}
			{
				final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_rename_16x16);
				newItemMenuItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(final ActionEvent e)
					{
						renameRecord();
					}
				});
				newItemMenuItem.setText(lang.get("btn_Rename"));
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_REPORTING_GROUP_RENAME"));
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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_WASTE_REPORTING_GROUP"));
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
				final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh_16x16);
				newItemMenuItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(final ActionEvent e)
					{
						populateList(-1);
					}
				});
				newItemMenuItem.setText(lang.get("btn_Refresh"));
				popupMenu.add(newItemMenuItem);
			}

			jButtonAdd = new JButton4j(Common.icon_add_16x16);
			jDesktopPane1.add(jButtonAdd);
			jButtonAdd.setText(lang.get("btn_Add"));
			jButtonAdd.setMnemonic(lang.getMnemonicChar());
			jButtonAdd.setBounds(501, 0, 125, 32);
			jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_REPORTING_GROUP_ADD"));
			jButtonAdd.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					addrecord();

				}
			});

			jButtonDelete = new JButton4j(Common.icon_delete_16x16);
			jDesktopPane1.add(jButtonDelete);
			jButtonDelete.setText(lang.get("btn_Delete"));
			jButtonDelete.setMnemonic(lang.getMnemonicChar());
			jButtonDelete.setBounds(501, 31, 125, 32);
			jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_REPORTING_GROUP_DELETE"));
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
			jButtonEdit.setBounds(501, 62, 125, 32);
			jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_REPORTING_GROUP_EDIT"));
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
			jButtonRename.setBounds(501, 93, 125, 32);
			jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_REPORTING_GROUP_RENAME"));
			jButtonRename.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					renameRecord();
				}
			});

			jButtonPrint = new JButton4j(Common.icon_print_16x16);
			jDesktopPane1.add(jButtonPrint);
			jButtonPrint.setText(lang.get("btn_Print"));
			jButtonPrint.setMnemonic(lang.getMnemonicChar());
			jButtonPrint.setBounds(501, 124, 125, 32);
			jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_WASTE_REPORTING_GROUP"));
			jButtonPrint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					print();
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jButtonHelp.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				}
			});
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(501, 217, 125, 32);

			jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
			jDesktopPane1.add(jButtonRefresh);
			jButtonRefresh.setText(lang.get("btn_Refresh"));
			jButtonRefresh.setMnemonic(lang.getMnemonicChar());
			jButtonRefresh.setBounds(501, 186, 125, 32);
			jButtonRefresh.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{

					if (jListGroups.isSelectionEmpty() == false)
					{
						JDBWasteReportingGroup x;

						x = ((JDBWasteReportingGroup) jListGroups.getSelectedValue().getObject());

						lGroupString = String.valueOf(x.getWasteReportGroup());

					}
					else
					{
						lGroupString = "-1";
					}
					populateList(Integer.valueOf(lGroupString));
				}
			});

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(501, 248, 125, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
			jButtonExcel.setText(lang.get("btn_Excel"));
			jButtonExcel.setMnemonic(lang.getMnemonicChar());
			jButtonExcel.setBounds(501, 155, 125, 32);
			jButtonExcel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					excel();
				}
			});
			jDesktopPane1.add(jButtonExcel);

			ButtonGroup bgroup = new ButtonGroup();
			JPanel4j panel = new JPanel4j();
			panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			panel.setBounds(501, 291, 125, 68);
			jDesktopPane1.add(panel);
			panel.setLayout(null);

			rdbtnEnabled = new JRadioButton4j(lang.get("lbl_Enabled"));
			rdbtnEnabled.setBounds(8, 8, 102, 22);
			panel.add(rdbtnEnabled);

			rdbtnEnabled.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					populateList(-1);
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
					populateList(-1);
				}
			});
			rdbtnDisabled.setSelected(false);
			bgroup.add(rdbtnDisabled);

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
