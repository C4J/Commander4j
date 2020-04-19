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
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.EtchedBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBWasteTransactionType;
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
 * The JInternalFrameWasteTransactionAdmin class allows a user to maintain the
 * table APP_WASTE_TRANSACTION_TYPE.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWasteTransactionAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBWasteTransactionType
 * @see com.commander4j.app.JInternalFrameWasteTransactionAdmin
 *
 *
 */
public class JInternalFrameWasteTransactionAdmin extends javax.swing.JInternalFrame
{
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JList4j<JDBListData> jListTransactions;
	private JButton4j jButtonClose;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonEdit;
	private JButton4j jButtonRename;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JScrollPane jScrollPane1;
	private String lTransactionString;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JRadioButton rdbtnEnabled;
	private JRadioButton rdbtnDisabled;

	private void addrecord()
	{
		JDBWasteTransactionType u = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
		lTransactionString = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Waste_Transaction_Add"));
		
		if (lTransactionString != null)
		{
			if (lTransactionString.equals("") == false)
			{
				lTransactionString = lTransactionString.toUpperCase();
				
				if (u.create(lTransactionString) == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
				}
				else
				{
					JLaunchMenu.runForm("FRM_WASTE_TRANSACTION_EDIT", lTransactionString);
				}

				populateList(lTransactionString);
			}
		}
	}

	private void populateList(String defaultitem)
	{
		DefaultComboBoxModel<JDBListData> DefComboBoxMod = new DefaultComboBoxModel<JDBListData>();

		JDBWasteTransactionType tempType = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBListData> tempTypeList = tempType.getWasteTransactionTypes(rdbtnEnabled.isSelected());

		int sel = -1;
		for (int j = 0; j < tempTypeList.size(); j++)
		{
			DefComboBoxMod.addElement(tempTypeList.get(j));
			if (((JDBWasteTransactionType) tempTypeList.get(j).getObject()).getWasteTransactionType().equals(defaultitem))
			{
				sel = j;
			}

		}
		ListModel<JDBListData> jList1Model = DefComboBoxMod;
		jListTransactions.setModel(jList1Model);
		jListTransactions.setSelectedIndex(sel);
		jListTransactions.setCellRenderer(Common.renderer_list);
		jListTransactions.ensureIndexIsVisible(sel);

	}

	private void editRecord()
	{
		if (jListTransactions.isSelectionEmpty() == false)
		{
			lTransactionString = ((JDBWasteTransactionType) jListTransactions.getSelectedValue().getObject()).getWasteTransactionType();

			JLaunchMenu.runForm("FRM_WASTE_TRANSACTION_EDIT", lTransactionString);
		}
	}

	private void delete()
	{
		if (jListTransactions.isSelectionEmpty() == false)
		{
			lTransactionString = ((JDBWasteTransactionType) jListTransactions.getSelectedValue().getObject()).getWasteTransactionType();

			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Waste_Transaction_Delete") + " " + lTransactionString + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBWasteTransactionType u = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
				u.setWasteTransactionType(lTransactionString);

				u.delete();
				populateList("");
			}
		}
	}
	
	private void renameRecord()
	{
		if (jListTransactions.isSelectionEmpty() == false)
		{
			lTransactionString = ((JDBWasteTransactionType) jListTransactions.getSelectedValue().getObject()).getWasteTransactionType();
			JDBWasteTransactionType u = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
			
			
			String lMaterialStringgNEW = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Waste_Transaction_Rename"),lTransactionString);
			
			if (lMaterialStringgNEW != null)
			{
				if (lMaterialStringgNEW.equals("") == false)
				{
					lMaterialStringgNEW = lMaterialStringgNEW.toUpperCase();
					
					if (u.rename(lTransactionString,lMaterialStringgNEW) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
					}
					else
					{
					
						lTransactionString = lMaterialStringgNEW;
					}

					populateList(lTransactionString);
				}
			}	


		}	
	}

	private void print()
	{
		JLaunchReport.runReport("RPT_WASTE_TRANSACTIONS", null, "", null, "");
	}

	private void excel()
	{
		JDBWasteTransactionType samp = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("Waste_Transaction_Types.xls", samp.getWasteTransactionTypesDataResultSet(), Common.mainForm);
		populateList("");
	}

	public JInternalFrameWasteTransactionAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WASTE_TRANSACTION"));

		populateList("");
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
			this.setTitle("Waste Transaction Admin");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 0, 500, 512);
					{
						ListModel<JDBListData> jList1Model = new DefaultComboBoxModel<JDBListData>();
						jListTransactions = new JList4j<JDBListData>();
						jScrollPane1.setViewportView(jListTransactions);
						jListTransactions.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_TRANSACTION_EDIT") == true)
									{
										editRecord();
									}
								}
							}
						});
						jListTransactions.setModel(jList1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jListTransactions, popupMenu);

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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_TRANSACTION_ADD"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_TRANSACTION_DELETE"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_TRANSACTION_EDIT"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_TRANSACTION_RENAME"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_WASTE_TRANSACTIONS"));
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
					jButtonAdd.setBounds(501, 0, 125, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_TRANSACTION_ADD"));
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
					jButtonDelete.setBounds(501, 31, 125, 32);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_TRANSACTION_DELETE"));
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
					jButtonEdit.setBounds(501, 62, 125, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_TRANSACTION_EDIT"));
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonRename = new JButton4j(Common.icon_rename_16x16);
					jDesktopPane1.add(jButtonRename);
					jButtonRename.setText(lang.get("btn_Rename"));
					jButtonRename.setMnemonic(lang.getMnemonicChar());
					jButtonRename.setBounds(501, 93, 125, 32);
					jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WASTE_TRANSACTION_RENAME"));
					jButtonRename.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							renameRecord();
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_print_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(501, 124, 125, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_WASTE_TRANSACTIONS"));
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
					jButtonHelp.setBounds(501, 217, 125, 32);
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.setBounds(501, 186, 125, 32);
					jButtonRefresh.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							if (jListTransactions.isSelectionEmpty() == false)
							{
								lTransactionString = ((JDBWasteTransactionType) jListTransactions.getSelectedValue().getObject()).getWasteTransactionType();

							}
							else
							{
								lTransactionString = "";
							}
							populateList(lTransactionString);
						}
					});
				}
				{
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
				}

				{
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
				}

				ButtonGroup bgroup = new ButtonGroup();
				JPanel panel = new JPanel();
				panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				panel.setBounds(501, 291, 125, 68);
				jDesktopPane1.add(panel);
				panel.setLayout(null);

				rdbtnEnabled = new JRadioButton(lang.get("lbl_Enabled"));
				rdbtnEnabled.setBounds(8, 8, 102, 23);
				panel.add(rdbtnEnabled);
				rdbtnEnabled.setFont(Common.font_std);
				rdbtnEnabled.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						populateList("");
					}
				});
				rdbtnEnabled.setSelected(true);
				bgroup.add(rdbtnEnabled);

				rdbtnDisabled = new JRadioButton(lang.get("lbl_Disabled"));
				rdbtnDisabled.setBounds(8, 35, 102, 23);
				panel.add(rdbtnDisabled);
				rdbtnDisabled.setFont(Common.font_std);
				rdbtnDisabled.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						populateList("");
					}
				});
				rdbtnDisabled.setSelected(false);
				bgroup.add(rdbtnDisabled);
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
