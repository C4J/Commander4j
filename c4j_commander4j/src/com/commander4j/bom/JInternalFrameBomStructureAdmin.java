package com.commander4j.bom;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameBomStructureAdmin.java
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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import javax.swing.ListModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;

import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameBomStructureAdmin extends javax.swing.JInternalFrame
{
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JDesktopPane4j jDesktopPane1;
	private JList4j<JDBListData> jListBomStructure;

	private JButton4j jButtonClose;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonEdit;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JScrollPane4j jScrollPane1;

	private JLabel4j_std lbl_Description;

	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public void setDataID(String id, String parent_id)
	{
		populateList(id, parent_id);
	}

	private void populateList(String id, String parent_id)
	{

		DefaultComboBoxModel<JDBListData> DefComboBoxMod = new DefaultComboBoxModel<JDBListData>();

		JDBBomStructure tempStruct = new JDBBomStructure(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBListData> tempStructList = tempStruct.getStructureIds();
		;

		int sel = -1;
		for (int j = 0; j < tempStructList.size(); j++)
		{
			DefComboBoxMod.addElement(tempStructList.get(j));

			JDBBomStructureRecord item = ((JDBBomStructureRecord) tempStructList.get(j).getmData());
			if (item.getDataId().equals(id))
			{
				if (item.getDataIDParent().equals(parent_id))
				{

					sel = j;

				}
			}

		}

		ListModel<JDBListData> jList1Model = DefComboBoxMod;

		jListBomStructure.setModel(jList1Model);
		jListBomStructure.setSelectedIndex(sel);
		jListBomStructure.setCellRenderer(Common.renderer_list);
		jListBomStructure.ensureIndexIsVisible(sel);

	}

	private void addRecord()
	{
		JLaunchMenu.runForm("FRM_BOM_STRUCTURE_EDIT", "", "");
	}

	private void editRecord()
	{
		if (jListBomStructure.isSelectionEmpty() == false)
		{
			JDBListData ddd = jListBomStructure.getSelectedValue();
			JDBBomStructureRecord rrr = ((JDBBomStructureRecord) ddd.getmData());

			String dataID = rrr.getDataId();
			String parentDataID = rrr.getDataIDParent();

			JLaunchMenu.runForm("FRM_BOM_STRUCTURE_EDIT", dataID, parentDataID);
		}
	}

	private void delete()
	{
		if (jListBomStructure.isSelectionEmpty() == false)
		{
			JDBBomStructureRecord strucrec = ((JDBBomStructureRecord) jListBomStructure.getSelectedValue().getObject());
			String dataID = ((JDBBomStructureRecord) jListBomStructure.getSelectedValue().getObject()).getDataId();
			String parentDataID = ((JDBBomStructureRecord) jListBomStructure.getSelectedValue().getObject()).getDataIDParent();

			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Delete") + " [" + dataID + "] - [" + parentDataID + "]", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (n == 0)
			{
				JDBBomStructure u = new JDBBomStructure(Common.selectedHostID, Common.sessionID);
				u.delete(strucrec);
				populateList(strucrec.getDataId(), strucrec.getDataIDParent());
			}
		}
	}

	private void print()
	{
		JLaunchReport.runReport("RPT_BOM_STRUCTURE", null, "", null, "");
	}

	private void excel()
	{

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(Common.selectedHostID).getConnection(Common.sessionID).prepareStatement(Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBBomStructure.getList"));

			rs = stmt.executeQuery();
			JExcel export = new JExcel();

			export.saveAs("bom_structure.xls", rs, Common.mainForm);

			stmt.clearParameters();
			rs.close();
			stmt.close();
		}
		catch (Exception ex)
		{

		}

	}

	public JInternalFrameBomStructureAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_BOM_STRUCTURE"));
		populateList("", "");
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(455, 518));
			this.setBounds(0, 0, 463, 523);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Bom Structure");

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.List);
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.setBounds(0, 16, 326, 479);

			ListModel<JDBListData> jList1Model = new DefaultComboBoxModel<JDBListData>();
			jListBomStructure = new JList4j<JDBListData>();
			jScrollPane1.setViewportView(jListBomStructure);
			jListBomStructure.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent evt)
				{
					if (evt.getClickCount() == 2)
					{
						if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_STRUCTURE_EDIT") == true)
						{
							editRecord();
						}
					}
				}
			});
			jListBomStructure.setModel(jList1Model);

			final JPopupMenu popupMenu = new JPopupMenu();
			addPopup(jListBomStructure, popupMenu);

			final JMenuItem4j newItemMenuItem1 = new JMenuItem4j(Common.icon_add_16x16);
			newItemMenuItem1.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					editRecord();
				}
			});
			newItemMenuItem1.setText(lang.get("btn_Add"));
			newItemMenuItem1.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_STRUCTURE_ADD"));
			popupMenu.add(newItemMenuItem1);

			final JMenuItem4j newItemMenuItem2 = new JMenuItem4j(Common.icon_delete_16x16);
			newItemMenuItem2.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					delete();
				}
			});
			newItemMenuItem2.setText(lang.get("btn_Delete"));
			newItemMenuItem2.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_STRUCTURE_DELETE"));
			popupMenu.add(newItemMenuItem2);

			final JMenuItem4j newItemMenuItem3 = new JMenuItem4j(Common.icon_edit_16x16);
			newItemMenuItem3.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					editRecord();
				}
			});
			newItemMenuItem3.setText(lang.get("btn_Edit"));
			newItemMenuItem3.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_STRUCTURE_EDIT"));
			popupMenu.add(newItemMenuItem3);

			final JMenuItem4j newItemMenuItem4 = new JMenuItem4j(Common.icon_print_16x16);
			newItemMenuItem4.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					print();
				}
			});
			newItemMenuItem4.setText(lang.get("btn_Print"));
			newItemMenuItem4.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_BOM_STRUCTURE"));
			popupMenu.add(newItemMenuItem4);

			final JMenuItem4j newItemMenuItem5 = new JMenuItem4j(Common.icon_XLS_16x16);
			newItemMenuItem5.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					excel();
				}
			});
			newItemMenuItem5.setText(lang.get("btn_Excel"));
			popupMenu.add(newItemMenuItem5);

			final JMenuItem4j newItemMenuItem6 = new JMenuItem4j(Common.icon_refresh_16x16);
			newItemMenuItem6.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					populateList("", "");
				}
			});
			newItemMenuItem6.setText(lang.get("btn_Refresh"));
			popupMenu.add(newItemMenuItem6);

			jButtonAdd = new JButton4j(Common.icon_add_16x16);
			jDesktopPane1.add(jButtonAdd);
			jButtonAdd.setText(lang.get("btn_Add"));
			jButtonAdd.setMnemonic(lang.getMnemonicChar());
			jButtonAdd.setBounds(327, 16, 125, 32);
			jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_STRUCTURE_ADD"));
			jButtonAdd.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					addRecord();
				}
			});

			jButtonDelete = new JButton4j(Common.icon_delete_16x16);
			jDesktopPane1.add(jButtonDelete);
			jButtonDelete.setText(lang.get("btn_Delete"));
			jButtonDelete.setMnemonic(lang.getMnemonicChar());
			jButtonDelete.setBounds(327, 47, 125, 32);
			jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_STRUCTURE_DELETE"));
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
			jButtonEdit.setBounds(327, 78, 125, 32);
			jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_STRUCTURE_EDIT"));
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
			jButtonPrint.setBounds(327, 109, 125, 32);
			jButtonPrint.setEnabled(true);
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
			jButtonHelp.setBounds(327, 202, 125, 32);

			jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
			jDesktopPane1.add(jButtonRefresh);
			jButtonRefresh.setText(lang.get("btn_Refresh"));
			jButtonRefresh.setMnemonic(lang.getMnemonicChar());
			jButtonRefresh.setBounds(327, 171, 125, 32);
			jButtonRefresh.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					populateList("", "");
				}
			});

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(327, 233, 125, 32);
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
			jButtonExcel.setBounds(327, 140, 125, 32);
			jButtonExcel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					excel();
				}
			});
			jDesktopPane1.add(jButtonExcel);

			lbl_Description = new JLabel4j_std("Parent ID               Data ID");
			lbl_Description.setFont(new Font("Monospaced", Font.BOLD, 11));
			lbl_Description.setBounds(0, 0, 326, 15);
			jDesktopPane1.add(lbl_Description);

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
