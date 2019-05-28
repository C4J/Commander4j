package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameCustomerAdmin.java
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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBLanguage;
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
 * JInternalFrameCustomerAdmin class is used to insert/update/delete the
 * APP_CUSTOMER table. This table is used to hold customer names and label
 * options. When printing labels the the system looks at the customer ref in the
 * Process Order and then retrieves the customer details from the APP_CUSTOMER
 * table. This permits customer specific titles to appear on labels. The default
 * customer ref of SELF can be used for all Process Orders if the company name
 * on the label is constant.
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameCustomerAdmin.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameCustomerProperties JInternalFrameCustomerProperties
 * @see com.commander4j.db.JDBCustomer JDBCustomer
 */
public class JInternalFrameCustomerAdmin extends javax.swing.JInternalFrame
{
	private JButton jButtonExcel;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JList4j<JDBCustomer> jListCustomers;
	private JButton4j jButtonRename;
	private JButton4j jButtonClose;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonEdit;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JScrollPane jScrollPane1;
	private String lcustid;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	private void addrecord()
	{
		JDBCustomer u = new JDBCustomer(Common.selectedHostID, Common.sessionID);
		lcustid = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Customer_Add"), null, JOptionPane.QUESTION_MESSAGE, Common.icon_confirm, null, null);
		if (lcustid != null)
		{
			if (lcustid.equals("") == false)
			{
				lcustid = lcustid.toUpperCase();
				if (u.create(lcustid, "", "Y") == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
				} else
				{
					JLaunchMenu.runForm("FRM_ADMIN_CUSTOMER_EDIT", lcustid);
				}
				populateList(lcustid);
			}
		}
	}

	private void populateList(String defaultitem)
	{

		DefaultComboBoxModel<JDBCustomer> DefComboBoxMod = new DefaultComboBoxModel<JDBCustomer>();

		JDBCustomer tempCust = new JDBCustomer(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBCustomer> tempCustomerList = tempCust.getCustomers();
		int sel = -1;
		for (int j = 0; j < tempCustomerList.size(); j++)
		{
			JDBCustomer t = (JDBCustomer) tempCustomerList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getID().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBCustomer> jList1Model = DefComboBoxMod;
		jListCustomers.setModel(jList1Model);
		jListCustomers.setSelectedIndex(sel);
		jListCustomers.setCellRenderer(Common.renderer_list);
		jListCustomers.ensureIndexIsVisible(sel);
	}

	private void editRecord()
	{
		if (jListCustomers.isSelectionEmpty() == false)
		{
			lcustid = ((JDBCustomer) jListCustomers.getSelectedValue()).getID();
			JLaunchMenu.runForm("FRM_ADMIN_CUSTOMER_EDIT", lcustid);
		}
	}

	private void delete()
	{
		if (jListCustomers.isSelectionEmpty() == false)
		{
			lcustid = ((JDBCustomer) jListCustomers.getSelectedValue()).getID();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Customer_Delete") + " " + lcustid + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (question == 0)
			{
				JDBCustomer u = new JDBCustomer(Common.selectedHostID, Common.sessionID);
				u.setID(lcustid);
				u.delete();
				populateList("");
			}
		}
	}

	private void print()
	{
		JLaunchReport.runReport("RPT_CUSTOMER", null, "", null, "");
	}

	private void rename()
	{
		if (jListCustomers.isSelectionEmpty() == false)
		{
			String lcust_from = ((JDBCustomer) jListCustomers.getSelectedValue()).getID();
			String lcust_to = new String();
			lcust_to = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Customer_Rename"));
			if (lcust_to != null)
			{
				if (lcust_to.equals("") == false)
				{
					lcust_to = lcust_to.toUpperCase();
					JDBCustomer u = new JDBCustomer(Common.selectedHostID, Common.sessionID);
					u.setID(lcust_from);
					if (u.renameTo(lcust_to) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
					}
					populateList(lcust_to);
				}
			}
		}
	}

	private void excel()
	{
		JDBCustomer customer = new JDBCustomer(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("customers.xls", customer.getCustomerDataResultSet(), Common.mainForm);
		populateList("");
	}

	public JInternalFrameCustomerAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_CUSTOMER"));
		populateList("");
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(455, 518));
			this.setBounds(0, 0, 563, 541);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Customer Admin");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 0, 420, 510);
					{
						ListModel<JDBCustomer> jList1Model = new DefaultComboBoxModel<JDBCustomer>();
						jListCustomers = new JList4j<JDBCustomer>();
						jScrollPane1.setViewportView(jListCustomers);
						jListCustomers.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER_EDIT") == true)
									{
										editRecord();
									}
								}
							}
						});
						jListCustomers.setModel(jList1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jListCustomers, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										addrecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER_ADD"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										delete();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Delete"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER_DELETE"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										editRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Edit"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER_EDIT"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_rename);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										rename();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Rename"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER_RENAME"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_print);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										print();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Print"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_CUSTOMERS"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS);
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
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh);
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
					jButtonAdd = new JButton4j(Common.icon_add);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(423, 6, 125, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER_ADD"));
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							addrecord();

						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setBounds(423, 37, 125, 32);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER_DELETE"));
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
					jButtonEdit = new JButton4j(Common.icon_edit);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setBounds(423, 68, 125, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER_EDIT"));
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonRename = new JButton4j(Common.icon_rename);
					jDesktopPane1.add(jButtonRename);
					jButtonRename.setText(lang.get("btn_Rename"));
					jButtonRename.setMnemonic(lang.getMnemonicChar());
					jButtonRename.setBounds(423, 99, 125, 32);
					jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER_RENAME"));
					jButtonRename.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							rename();

						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_print);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(423, 130, 125, 32);
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
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(423, 223, 125, 32);
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.setBounds(423, 192, 125, 32);
					jButtonRefresh.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							populateList("");
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(423, 254, 125, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS);
					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(423, 161, 125, 32);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							excel();
						}
					});
					jDesktopPane1.add(jButtonExcel);
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
