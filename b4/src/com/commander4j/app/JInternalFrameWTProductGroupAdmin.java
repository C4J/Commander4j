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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBWTProductGroups;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWTMaterialGroups class allows a user to maintain the table
 * APP_WEIGHT_PRODUCT_GROUPS.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTTNEAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBWTProductGroups JDBWTMaterialGroups
 * @see com.commander4j.app.JInternalFrameWTProductGroupAdmin
 *      JInternalFrameWTMaterialGroups
 *
 */
public class JInternalFrameWTProductGroupAdmin extends javax.swing.JInternalFrame
{
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JList4j<JDBWTProductGroups> jListGroups;
	private JButton4j jButtonClose;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonEdit;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JScrollPane jScrollPane1;
	private String lGroupString;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	private void addrecord()
	{
		JDBWTProductGroups u = new JDBWTProductGroups(Common.selectedHostID, Common.sessionID);
		lGroupString = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Product_Group_Add"));

		if (lGroupString != null)
		{
			if (lGroupString.equals("") == false)
			{
				if (u.create(lGroupString) == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
				}
				else
				{
					JLaunchMenu.runForm("FRM_WEIGHT_PRODUCT_GROUP_EDIT", lGroupString);
				}

				populateList(lGroupString);
			}
		}
	}

	private void populateList(String defaultValue)
	{

		DefaultComboBoxModel<JDBWTProductGroups> DefComboBoxMod = new DefaultComboBoxModel<JDBWTProductGroups>();

		JDBWTProductGroups tempTNE = new JDBWTProductGroups(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBWTProductGroups> tempTNEList = tempTNE.getMaterialGroups();
		int sel = -1;
		for (int j = 0; j < tempTNEList.size(); j++)
		{
			JDBWTProductGroups t = (JDBWTProductGroups) tempTNEList.get(j);
			DefComboBoxMod.addElement(t);

			if (t.getMaterialGroup().equals(defaultValue))
			{

				sel = j;
				break;

			}
		}

		ListModel<JDBWTProductGroups> jList1Model = DefComboBoxMod;
		jListGroups.setModel(jList1Model);
		jListGroups.setSelectedIndex(sel);
		jListGroups.setCellRenderer(Common.renderer_list);
		jListGroups.ensureIndexIsVisible(sel);
	}

	private void editRecord()
	{
		if (jListGroups.isSelectionEmpty() == false)
		{
			lGroupString = ((JDBWTProductGroups) jListGroups.getSelectedValue()).getMaterialGroup().toString();

			JLaunchMenu.runForm("FRM_WEIGHT_PRODUCT_GROUP_EDIT", lGroupString);
		}
	}

	private void delete()
	{
		if (jListGroups.isSelectionEmpty() == false)
		{
			lGroupString = ((JDBWTProductGroups) jListGroups.getSelectedValue()).getMaterialGroup().toString();

			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Product_Group_Delete") + " " + lGroupString + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBWTProductGroups u = new JDBWTProductGroups(Common.selectedHostID, Common.sessionID);
				u.setMaterialGroup(lGroupString);

				u.delete();
				populateList("");
			}
		}
	}

	private void print()
	{
		JLaunchReport.runReport("RPT_PRODUCT_GROUPS", null, "", null, "");
	}

	private void excel()
	{
		JDBWTProductGroups samp = new JDBWTProductGroups(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("Product_Groups.xls", samp.getMaterialDataResultSet(), Common.mainForm);
		populateList("");
	}

	public JInternalFrameWTProductGroupAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_PRODUCT_GROUP"));

		populateList("");
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(455, 518));
			this.setBounds(0, 0, 673, 322);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Material Groups");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(6, 29, 518, 249);
					{
						ListModel<JDBWTProductGroups> jList1Model = new DefaultComboBoxModel<JDBWTProductGroups>();
						jListGroups = new JList4j<JDBWTProductGroups>();
						jScrollPane1.setViewportView(jListGroups);
						jListGroups.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_PRODUCT_GROUP_EDIT") == true)
									{
										editRecord();
									}
								}
							}
						});
						jListGroups.setModel(jList1Model);

						{
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_PRODUCT_GROUP_ADD"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_PRODUCT_GROUP_DELETE"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_PRODUCT_GROUP_EDIT"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PRODUCT_GROUPS"));
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
						}
					}
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(530, 5, 125, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_PRODUCT_GROUP_ADD"));
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
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setBounds(530, 36, 125, 32);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_PRODUCT_GROUP_DELETE"));
					jButtonDelete.setFocusTraversalKeysEnabled(false);
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							delete();

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit_16x16);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setBounds(530, 67, 125, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_PRODUCT_GROUP_EDIT"));
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_print_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(530, 98, 125, 32);
					jButtonPrint.setEnabled(true);
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							print();
						}
					});
				}
				{
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
					jButtonHelp.setBounds(530, 191, 125, 32);
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.setBounds(530, 160, 125, 32);
					jButtonRefresh.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							populateList("");
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(530, 222, 125, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(530, 129, 125, 32);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							excel();
						}
					});
					jDesktopPane1.add(jButtonExcel);
				}

				JLabel lbl_Legend = new JLabel("Product Groups");
				lbl_Legend.setFont(new Font("Monospaced", Font.BOLD, 11));
				lbl_Legend.setBounds(6, 12, 518, 15);
				jDesktopPane1.add(lbl_Legend);
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
