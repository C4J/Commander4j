package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameQMSelectListAdmin.java
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQMSelectList;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JExcel;

/**
 * JInternalFrameQMSelectListAdmin is used to administer the lists which appear
 * within the Quality Module results entry table. The data which appears within
 * these lists is stored within a table called APP_QM_SELECTLIST. This table can
 * contain multiple lists by use of a filter on a field called SELECT_LIST_ID
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameQMSelectListAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBQMSelectList JDBQMSelectList
 * @see com.commander4j.app.JDialogQMSelectListProperties
 *      JDialogQMSelectListProperties
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary 
 */
public class JInternalFrameQMSelectListAdmin extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JList4j<JDBQMSelectList> jListSelectLists;
	private JButton4j jButtonClose;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonEdit;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JScrollPane jScrollPane1;
	private String llistid;
	private String llistval;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JInternalFrameQMSelectListAdmin()
	{
		super();
		initGUI();
		populateList("", "");
	}

	private void editRecord()
	{
		if (jListSelectLists.isSelectionEmpty() == false)
		{
			String id = ((JDBQMSelectList) jListSelectLists.getSelectedValue()).getSelectListID();
			String val = ((JDBQMSelectList) jListSelectLists.getSelectedValue()).getValue();
			JLaunchMenu.runDialog("FRM_QM_SELECTLIST", id, val);
		}
	}

	private void addRecord()
	{
		JLaunchMenu.runDialog("FRM_QM_SELECTLIST", "", "");
	}

	private void populateList(String defaultid, String defaultval)
	{

		DefaultComboBoxModel<JDBQMSelectList> DefComboBoxMod = new DefaultComboBoxModel<JDBQMSelectList>();

		JDBQMSelectList tempSelectList = new JDBQMSelectList(Common.selectedHostID, Common.sessionID);

		tempSelectList.setDisplayModeLong(true);
		LinkedList<JDBQMSelectList> tempList = tempSelectList.getSelectLists();
		int sel = -1;
		for (int j = 0; j < tempList.size(); j++)
		{
			JDBQMSelectList t = (JDBQMSelectList) tempList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getSelectListID().equals(defaultid) & (t.getSelectListID().equals(defaultval)))
			{
				sel = j;
			}
		}

		ListModel<JDBQMSelectList> jList1Model = DefComboBoxMod;
		jListSelectLists.setModel(jList1Model);
		jListSelectLists.setSelectedIndex(sel);
		jListSelectLists.setCellRenderer(Common.renderer_list);
		jListSelectLists.ensureIndexIsVisible(sel);
	}

	private void deleteRecord()
	{
		if (jListSelectLists.isSelectionEmpty() == false)
		{
			llistid = ((JDBQMSelectList) jListSelectLists.getSelectedValue()).getSelectListID();
			llistval = ((JDBQMSelectList) jListSelectLists.getSelectedValue()).getValue();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_SelectList_Delete") + " " + llistid + "/" + llistval + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBQMSelectList u = new JDBQMSelectList(Common.selectedHostID, Common.sessionID);
				u.setSelectListID(llistid);
				u.setValue(llistval);
				u.delete();
				populateList("", "");
			}
		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(455, 518));
			this.setBounds(0, 0, 796, 541);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Select List Admin");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 0, 651, 509);
					{
						ListModel<JDBQMSelectList> jList1Model = new DefaultComboBoxModel<JDBQMSelectList>();
						jListSelectLists = new JList4j<JDBQMSelectList>();
						jScrollPane1.setViewportView(jListSelectLists);
						jListSelectLists.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									editRecord();
								}
							}
						});
						jListSelectLists.setModel(jList1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jListSelectLists, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										addRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								newItemMenuItem.setEnabled(true);
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										deleteRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Delete"));
								newItemMenuItem.setEnabled(true);
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
								newItemMenuItem.setEnabled(true);
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										populateList("", "");
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
					jButtonAdd.setBounds(656, 5, 125, 32);
					jButtonAdd.setEnabled(true);
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							addRecord();

						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete_16x16);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setBounds(656, 36, 125, 32);
					jButtonDelete.setEnabled(true);
					jButtonDelete.setFocusTraversalKeysEnabled(false);
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							deleteRecord();

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit_16x16);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setBounds(656, 67, 125, 32);
					jButtonEdit.setEnabled(true);
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}

				{
					jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.setBounds(656, 98, 125, 32);
					jButtonRefresh.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							populateList("", "");
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(656, 160, 125, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});

					JButton4j btnExcel = new JButton4j(Common.icon_XLS_16x16);
					btnExcel.setText(lang.get("btn_Excel"));
					btnExcel.setBounds(656, 127, 125, 32);
					btnExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							excel();
						}
					});
					jDesktopPane1.add(btnExcel);

				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void excel()
	{
		JDBQMSelectList slist = new JDBQMSelectList(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		PreparedStatement ps = slist.getSelectListPreparedStatement();
		export.saveAs("qm_selectlist.xls", slist.getQMSelectListResultSet(ps), Common.mainForm);
		try
		{
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		populateList("", "");
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
