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

import com.commander4j.db.JDBEquipmentType;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
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
 * The JInternalFrameEquipmentAdmin class allows a user to maintain the table
 * APP_EQUIPMENT_TYPE.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameEquipmentAdmin.jpg" >
 *
 * @see com.commander4j.db.JDBEquipmentType
 * @see com.commander4j.app.JInternalFrameEquipmentAdmin
 *
 *
 */
public class JInternalFrameEquipmentAdmin extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JButton4j jButtonExcel;
	private JDesktopPane4j jDesktopPane1;
	private JList4j<JDBListData> jListEquipmentTypes;
	private JButton4j jButtonClose;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonEdit;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JScrollPane4j jScrollPane1;
	private String lEquipmentTypeString;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JRadioButton4j rdbtnEnabled;

	private JRadioButton4j rdbtnDisabled;
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


	public JInternalFrameEquipmentAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_EQUIPMENT_TYPE"));

		populateList("");
	}

	private void addrecord()
	{
		JDBEquipmentType u = new JDBEquipmentType(Common.selectedHostID, Common.sessionID);
		lEquipmentTypeString = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Equipment_Type_Add"));

		if (lEquipmentTypeString != null)
		{
			if (lEquipmentTypeString.equals("") == false)
			{
				lEquipmentTypeString = lEquipmentTypeString.toUpperCase();
				lEquipmentTypeString = JUtility.removeNonGS1BarcodeFriendlyChars(lEquipmentTypeString);

				if (u.create(lEquipmentTypeString) == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
				}
				else
				{
					JLaunchMenu.runForm("FRM_EQUIPMENT_TYPE_EDIT", lEquipmentTypeString);
				}

				populateList(lEquipmentTypeString);
			}
		}
	}

	private void delete()
	{
		if (jListEquipmentTypes.isSelectionEmpty() == false)
		{
			lEquipmentTypeString = ((JDBEquipmentType) jListEquipmentTypes.getSelectedValue().getObject()).getEquipmentType();

			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Equipment_Type_Delete") + " " + lEquipmentTypeString + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBEquipmentType u1 = new JDBEquipmentType(Common.selectedHostID, Common.sessionID);
				u1.setEquipmentType(lEquipmentTypeString);

				u1.delete();

				u1 = null;

				populateList("");
			}
		}
	}

	private void editRecord()
	{
		if (jListEquipmentTypes.isSelectionEmpty() == false)
		{
			lEquipmentTypeString = ((JDBEquipmentType) jListEquipmentTypes.getSelectedValue().getObject()).getEquipmentType();

			JLaunchMenu.runForm("FRM_EQUIPMENT_TYPE_EDIT", lEquipmentTypeString);
		}
	}

	private void excel()
	{
		JDBEquipmentType samp = new JDBEquipmentType(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("Equipment_Types.xls", samp.getEquipmentTypeDataResultSet(), Common.mainForm);
		populateList("");
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(455, 518));
			this.setBounds(0, 0, 667, 543);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Equipment Type Admin");

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.List);
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.setBounds(0, 0, 500, 512);

			ListModel<JDBListData> jList1Model = new DefaultComboBoxModel<JDBListData>();
			jListEquipmentTypes = new JList4j<JDBListData>();
			jScrollPane1.setViewportView(jListEquipmentTypes);
			jListEquipmentTypes.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent evt)
				{
					if (evt.getClickCount() == 2)
					{
						if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_EQUIPMENT_TYPE_EDIT") == true)
						{
							editRecord();
						}
					}
				}
			});
			jListEquipmentTypes.setModel(jList1Model);

			final JPopupMenu popupMenu = new JPopupMenu();
			addPopup(jListEquipmentTypes, popupMenu);

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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_EQUIPMENT_TYPE_ADD"));
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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_EQUIPMENT_TYPE_DELETE"));
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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_EQUIPMENT_TYPE_EDIT"));
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
				newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_EQUIPMENT_TYPE"));
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
						populateList("");
					}
				});
				newItemMenuItem.setText(lang.get("btn_Refresh"));
				popupMenu.add(newItemMenuItem);
			}

			jButtonAdd = new JButton4j(Common.icon_add_16x16);
			jDesktopPane1.add(jButtonAdd);
			jButtonAdd.setText(lang.get("btn_Add"));
			jButtonAdd.setMnemonic(lang.getMnemonicChar());
			jButtonAdd.setBounds(501, 0, 153, 32);
			jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_EQUIPMENT_TYPE_ADD"));
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
			jButtonDelete.setBounds(501, 31, 153, 32);
			jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_EQUIPMENT_TYPE_DELETE"));
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
			jButtonEdit.setBounds(501, 62, 153, 32);
			jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_EQUIPMENT_TYPE_EDIT"));
			jButtonEdit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					editRecord();
				}
			});

			jButtonPrint = new JButton4j(Common.icon_print_16x16);
			jDesktopPane1.add(jButtonPrint);
			jButtonPrint.setText(lang.get("btn_Print"));
			jButtonPrint.setMnemonic(lang.getMnemonicChar());
			jButtonPrint.setBounds(501, 93, 153, 32);
			jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_EQUIPMENT_TYPE"));
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
			jButtonHelp.setBounds(501, 217, 153, 32);

			jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
			jDesktopPane1.add(jButtonRefresh);
			jButtonRefresh.setText(lang.get("btn_Refresh"));
			jButtonRefresh.setMnemonic(lang.getMnemonicChar());
			jButtonRefresh.setBounds(501, 186, 153, 32);
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
			jButtonClose.setBounds(501, 248, 153, 32);
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
			jButtonExcel.setBounds(501, 155, 153, 32);
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
			panel.setBounds(505, 292, 145, 68);
			jDesktopPane1.add(panel);
			panel.setLayout(null);

			rdbtnEnabled = new JRadioButton4j(lang.get("lbl_Enabled"));
			rdbtnEnabled.setBounds(8, 8, 122, 22);
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
			rdbtnDisabled.setBounds(8, 35, 122, 22);
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

			JButton4j jButtonUpdatePallets = new JButton4j(Common.icon_pallet_16x16);
			jButtonUpdatePallets.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JDBEquipmentType u = new JDBEquipmentType(Common.selectedHostID, Common.sessionID);
					u.updatePallets();
				}
			});
			jButtonUpdatePallets.setMnemonic('0');
			jButtonUpdatePallets.setBounds(501, 124, 153, 32);
			jButtonUpdatePallets.setText(lang.get("btn_Update_Pallets"));
			jButtonUpdatePallets.setToolTipText("Update pallets with missing Equipment Types");
			jDesktopPane1.add(jButtonUpdatePallets);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void populateList(String defaultitem)
	{
		DefaultComboBoxModel<JDBListData> DefComboBoxMod = new DefaultComboBoxModel<JDBListData>();

		JDBEquipmentType tempEquipment = new JDBEquipmentType(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBListData> tempEquipmentList = tempEquipment.getEquipmentTypes(rdbtnEnabled.isSelected(), JDBEquipmentType.displayModeFull);

		int sel = -1;
		for (int j = 0; j < tempEquipmentList.size(); j++)
		{

			DefComboBoxMod.addElement(tempEquipmentList.get(j));
			if (((JDBEquipmentType) tempEquipmentList.get(j).getObject()).getEquipmentType().equals(defaultitem))
			{
				sel = j;
			}

		}
		ListModel<JDBListData> jList1Model = DefComboBoxMod;
		jListEquipmentTypes.setModel(jList1Model);
		jListEquipmentTypes.setSelectedIndex(sel);
		jListEquipmentTypes.setCellRenderer(Common.renderer_list);
		jListEquipmentTypes.ensureIndexIsVisible(sel);

	}

	private void print()
	{
		JLaunchReport.runReport("RPT_EQUIPMENT_TYPE", null, "", null, "");
	}
}
