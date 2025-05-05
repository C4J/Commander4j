package com.commander4j.bom;

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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

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
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JRadioButton4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameBomListAdmin extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JList4j<JDBBomListRecord> jBomList;
	private JScrollPane jScrollPane1;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonExcel;
	private JButton4j jButtonAdd;
	private JLabel4j_std lbl_Description;
	private String list_id;
	private String item_id;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JRadioButton4j rdbtnEnabled;
	private JRadioButton4j rdbtnDisabled;

	public void setDataID(String id,String parent_id,boolean enabled)
	{
		if (enabled)
		{
			rdbtnEnabled.setSelected(true);
		}
		else
		{
			rdbtnDisabled.setSelected(true);
		}
		
		populateList(id,parent_id);
	}
	
	private void delete()
	{
		if (jBomList.isSelectionEmpty() == false)
		{
			JDBBomListRecord rec = ((JDBBomListRecord) jBomList.getSelectedValue());
			
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Delete") + " " + rec.getList_id()+"/"+rec.getItem() + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBBomList u = new JDBBomList(Common.selectedHostID, Common.sessionID);
				
				u.delete(rec);
				populateList("","");
			}
		}
	}

	private void create()
	{
		JDBBomList u = new JDBBomList(Common.selectedHostID, Common.sessionID);
		
		list_id = JOptionPane.showInputDialog(Common.mainForm, "List ID");

		if (list_id != null)
		{
			if (list_id.equals("") == false)
			{
				list_id = list_id.toLowerCase();
				
				item_id = JOptionPane.showInputDialog(Common.mainForm, "Item ID");
				
				if (item_id != null)
				{
					if (item_id.equals("") == false)
					{
						item_id = item_id.toLowerCase();
				
						JDBBomListRecord newrec = new JDBBomListRecord();
						
						newrec.setList_id(list_id);
						newrec.setItem(item_id);
						newrec.setSequence(1);
						newrec.setEnabled("Y");
						
				
						if (u.create(newrec) == false)
						{
							JUtility.errorBeep();
							JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
						}
						else
						{
		
							JLaunchMenu.runForm("FRM_BOM_LIST_EDIT", u.getListRecord().getList_id(),u.getListRecord().getItem());
						}
				        populateList(list_id,item_id);
					}
				}
			}
		}
	}

	private void print()
	{
		JLaunchReport.runReport("RPT_BOM_LIST", null, "", null, "");
	}

	private void populateList(String defaultlist,String defaultitem)
	{

		DefaultComboBoxModel<JDBBomListRecord> DefComboBoxMod = new DefaultComboBoxModel<JDBBomListRecord>();

		JDBBomList tempCust = new JDBBomList(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBBomListRecord> tempCustomerList = tempCust.getListIds(rdbtnEnabled.isSelected());
		int sel = -1;
		for (int j = 0; j < tempCustomerList.size(); j++)
		{
			JDBBomListRecord t = (JDBBomListRecord) tempCustomerList.get(j);
			DefComboBoxMod.addElement(t);
			
			if (t.getList_id().equals(defaultlist))
			{
				if (t.getItem().equals(defaultitem))
				{
				sel = j;
				}
			}
		}

		ListModel<JDBBomListRecord> jList1Model = DefComboBoxMod;
		jBomList.setModel(jList1Model);
		jBomList.setSelectedIndex(sel);
		jBomList.setCellRenderer(Common.renderer_list);
		jBomList.ensureIndexIsVisible(sel);
	}

	public JInternalFrameBomListAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_BOM_LIST_ADMIN"));
		
		populateList("","");
	}

	private void editRecord()
	{
		if (jBomList.isSelectionEmpty() == false)
		{
			list_id = ((JDBBomListRecord) jBomList.getSelectedValue()).getList_id();
			item_id = ((JDBBomListRecord) jBomList.getSelectedValue()).getItem();
			JLaunchMenu.runForm("FRM_BOM_LIST_EDIT", list_id,item_id);
		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(375, 402));
			this.setBounds(0, 0, 573, 617);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("BOM List Admin");

			jDesktopPane1 = new JDesktopPane();
			jDesktopPane1.setBackground(Common.color_app_window);
			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jButtonAdd = new JButton4j(Common.icon_add_16x16);
			jDesktopPane1.add(jButtonAdd);
			jButtonAdd.setText(lang.get("btn_Add"));
			jButtonAdd.setMnemonic(lang.getMnemonicChar());
			jButtonAdd.setBounds(431, 6, 126, 32);
			jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_LIST_ADD"));
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
			jButtonDelete.setBounds(431, 37, 126, 32);
			jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_LIST_DELETE"));
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
			jButtonEdit.setBounds(431, 68, 126, 32);
			jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_LIST_EDIT"));
			jButtonEdit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					editRecord();
				}
			});

			jButtonPrint = new JButton4j(Common.icon_report_16x16);
			jDesktopPane1.add(jButtonPrint);
			jButtonPrint.setText(lang.get("btn_Print"));
			jButtonPrint.setMnemonic(lang.getMnemonicChar());
			jButtonPrint.setBounds(431, 99, 126, 32);
			jButtonPrint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					print();
				}
			});
			
			jButtonExcel = new JButton4j(Common.icon_export_16x16);
			jDesktopPane1.add(jButtonExcel);
			jButtonExcel.setText(lang.get("btn_Excel"));
			jButtonExcel.setMnemonic(lang.getMnemonicChar());
			jButtonExcel.setBounds(431, 130, 126, 32);
			jButtonExcel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					excel();
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(431, 161, 126, 32);

			jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
			jDesktopPane1.add(jButtonRefresh);
			jButtonRefresh.setText(lang.get("btn_Refresh"));
			jButtonRefresh.setMnemonic(lang.getMnemonicChar());
			jButtonRefresh.setBounds(431, 192, 126, 32);
			jButtonRefresh.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					populateList("","");
				}
			});

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(431, 223, 126, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			ButtonGroup bgroup = new ButtonGroup();
			JPanel panel = new JPanel();
			panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			panel.setBounds(431, 267, 125, 68);
			jDesktopPane1.add(panel);
			panel.setLayout(null);

			rdbtnEnabled = new JRadioButton4j(lang.get("lbl_Enabled"));
			rdbtnEnabled.setBounds(8, 8, 102, 22);
			panel.add(rdbtnEnabled);
			rdbtnEnabled.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					populateList("","");
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
					populateList("","");
				}
			});
			rdbtnDisabled.setSelected(false);
			bgroup.add(rdbtnDisabled);

			jScrollPane1 = new JScrollPane();
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.setBounds(5, 23, 419, 556);
			{
				ListModel<JDBBomListRecord> jList1Model = new DefaultComboBoxModel<JDBBomListRecord>();
				jBomList = new JList4j<JDBBomListRecord>();
				jBomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				jScrollPane1.setViewportView(jBomList);
				jBomList.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent evt)
					{
						if (evt.getClickCount() == 2)
						{
							if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_LIST_EDIT") == true)
							{
								editRecord();
							}
						}
					}
				});
				jBomList.setModel(jList1Model);
			}

			final JPopupMenu popupMenu = new JPopupMenu();
			addPopup(jBomList, popupMenu);

			final JMenuItem4j newItemMenuItem1 = new JMenuItem4j(Common.icon_add_16x16);
			newItemMenuItem1.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					create();
				}
			});
			newItemMenuItem1.setText(lang.get("btn_Add"));
			newItemMenuItem1.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_LIST_ADD"));
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
			newItemMenuItem2.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_LIST_DELETE"));
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
			newItemMenuItem3.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_LIST_EDIT"));
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
			newItemMenuItem4.setEnabled(true);
			popupMenu.add(newItemMenuItem4);

			final JMenuItem4j newItemMenuItem5 = new JMenuItem4j(Common.icon_refresh_16x16);
			newItemMenuItem5.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					populateList("","");
				}
			});
			newItemMenuItem5.setText(lang.get("btn_Refresh"));
			popupMenu.add(newItemMenuItem5);
			
			lbl_Description = new JLabel4j_std("List ID                   Item ID                 Sequence");
			lbl_Description.setFont(new Font("Monospaced", Font.BOLD, 11));
			lbl_Description.setBounds(6, 6, 413, 15);
			jDesktopPane1.add(lbl_Description);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void excel()
	{

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(Common.selectedHostID).getConnection(Common.sessionID).prepareStatement(Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBBomList.getList"));

			rs = stmt.executeQuery();
			JExcel export = new JExcel();
			
			export.saveAs("bom_lists.xls", rs, Common.mainForm);
			
			stmt.clearParameters();
			rs.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			
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
