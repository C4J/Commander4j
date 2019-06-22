package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameModuleAdmin.java
 * 
 * Package Name : com.commander4j.sys
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBModuleJList;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameModuleAdmin class allows a user to insert/update/delete
 * records from the SYS_MODULES table.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameModuleAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBModule JDBModule
 * @see com.commander4j.sys.JInternalFrameModuleProperties
 *      JInternalFrameModuleProperties
 * @see com.commander4j.sys.JInternalFrameModuleGroups
 *      JInternalFrameModuleGroups
 */
public class JInternalFrameModuleAdmin extends javax.swing.JInternalFrame
{
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonPrint;
	private JButton4j jButtonClose;
	private JRadioButton jRadioButtonFunctions;
	private JRadioButton jRadioButtonExec;
	private JRadioButton jRadioButtonReports;
	private JRadioButton jRadioButtonUserReports;
	private JRadioButton jRadioButtonMenus;
	private JRadioButton jRadioButtonForms;
	private JRadioButton jRadioButtonAll;
	private ButtonGroup buttonGroup1;
	private JButton4j jButtonRename;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonAdd;
	private JDBModuleJList jListModules;
	private JScrollPane jScrollPane1;
	private String lModuleId;
	private String statementText = "";
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private String selectedModuleType = "ALL";
	private JButton4j jButtonAlternative;
	private JButton4j jButtonTestHelp;

	public JInternalFrameModuleAdmin()
	{
		super();
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MODULES"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		populateList("");
	}

	private void addRecord()
	{
		JDBModule m = new JDBModule(Common.selectedHostID, Common.sessionID);

		lModuleId = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Module_Create"));
		if (lModuleId != null)
		{
			if (lModuleId.equals("") == false)
			{
				lModuleId = lModuleId.toUpperCase();

				String modType = selectedModuleType;
				if (modType.equals("ALL"))
				{
					modType = "USER";
				}
				if (m.create(lModuleId, "", "Y", "N", modType, "", "") == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, m.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
				} else
				{

					JLaunchMenu.runForm("FRM_ADMIN_MODULE_EDIT", lModuleId);
				}
				populateList(lModuleId);
			}
		}
	}

	private void copyRecord()
	{

		if (jListModules.isSelectionEmpty() == false)
		{
			String fromModuleId = ((JDBListData) jListModules.getSelectedValue()).toString();

			JDBModule m = new JDBModule(Common.selectedHostID, Common.sessionID);
			JDBModule m2 = new JDBModule(Common.selectedHostID, Common.sessionID);

			if (m.getModuleProperties(fromModuleId))
			{
				m2.getModuleProperties(fromModuleId);
				
				String toModuleId = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Module_Create"));

				if (toModuleId != null)
				{
					if (toModuleId.equals("") == false)
					{
						toModuleId = toModuleId.toUpperCase();

						m.setModuleId(toModuleId);

						if (m.create(toModuleId, m2.getResourceKey(), m2.getDKActive(), m2.getRFActive(), m2.getType(), m2.getIconFilename(), m2.getHelpSetID()) == false)
						{
							JUtility.errorBeep();
							JOptionPane.showMessageDialog(Common.mainForm, m.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
						} 
						else
						{
						   lModuleId = toModuleId;
							m = m2;
							m.setModuleId(toModuleId);
							m.update();
							
							JLaunchMenu.runForm("FRM_ADMIN_MODULE_EDIT", toModuleId);
							populateList(toModuleId);
						}
					}
				}
			}
		}
	}

	private void delete()
	{
		if (jListModules.isSelectionEmpty() == false)
		{

			lModuleId = ((JDBListData) jListModules.getSelectedValue()).toString();
			if (lModuleId.equals("root") == false)
			{
				int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Module_Delete") + " " + lModuleId + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
				if (n == 0)
				{
					JDBModule m = new JDBModule(Common.selectedHostID, Common.sessionID);
					m.setModuleId(lModuleId);
					m.delete();
					populateList("");
				}
			} else
			{
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(null, "Cannot delete module " + lModuleId + " !", "Information", JOptionPane.WARNING_MESSAGE, Common.icon_confirm_16x16);
			}
		}
	}

	private void populateList(String defaultitem)
	{
		DefaultComboBoxModel<JDBListData> defComboBoxMod = new DefaultComboBoxModel<JDBListData>();

		JDBModule tempModule = new JDBModule(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBListData> tempModuleList = new LinkedList<JDBListData>();

		if (jRadioButtonAll.isSelected())
		{
			tempModuleList = tempModule.getModuleIds();
		} else
		{
			tempModuleList = tempModule.getModuleIdsByType(selectedModuleType);
		}

		int sel = -1;
		for (int j = 0; j < tempModuleList.size(); j++)
		{
			defComboBoxMod.addElement(tempModuleList.get(j));
			if (tempModuleList.get(j).toString().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBListData> jList1Model = defComboBoxMod;

		jListModules.setModel(jList1Model);
		jListModules.setSelectedIndex(sel);

		jListModules.setCellRenderer(Common.renderer_list);
	}

	private void editRecord()
	{
		if (jListModules.isSelectionEmpty() == false)
		{
			lModuleId = ((JDBListData) jListModules.getSelectedValue()).toString();
			JLaunchMenu.runForm("FRM_ADMIN_MODULE_EDIT", lModuleId);
			populateList(lModuleId);
		}
	}

	private void rename()
	{
		if (jListModules.isSelectionEmpty() == false)
		{
			String lmodule_id_from = ((JDBListData) jListModules.getSelectedValue()).toString();
			String lmodule_id_to = new String();
			lmodule_id_to = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Rename"));
			if (lmodule_id_to != null)
			{
				if (lmodule_id_to.equals("") == false)
				{
					lmodule_id_to = lmodule_id_to.toUpperCase();
					JDBModule m = new JDBModule(Common.selectedHostID, Common.sessionID);
					m.setModuleId(lmodule_id_from);
					if (m.renameTo(lmodule_id_to) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(null, m.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
					}
					populateList(lModuleId);
				}
			}
		}
	}

	private void print()
	{
		statementText = "select * from " + Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema() + "SYS_MODULES order by module_type,module_id";
		JLaunchReport.runReport("RPT_MODULES", null, statementText, null, "");
	}

	private void excel()
	{
		JDBModule module = new JDBModule(Common.selectedHostID, Common.sessionID);
		ResultSet rs = null;
		String moduleType = "";

		if (jRadioButtonAll.isSelected())
		{
			moduleType = "ALL";
			rs = module.getModuleData();
		}
		if (jRadioButtonFunctions.isSelected())
		{
			moduleType = "FUNCTION";
			rs = module.getModuleDataByType(moduleType);
		}
		if (jRadioButtonForms.isSelected())
		{
			moduleType = "FORM";
			rs = module.getModuleDataByType(moduleType);
		}
		if (jRadioButtonMenus.isSelected())
		{
			moduleType = "MENU";
			rs = module.getModuleDataByType(moduleType);
		}
		if (jRadioButtonReports.isSelected())
		{
			moduleType = "REPORT";
			rs = module.getModuleDataByType(moduleType);
		}
		if (jRadioButtonExec.isSelected())
		{
			moduleType = "EXEC";
			rs = module.getModuleDataByType(moduleType);
		}

		JExcel export = new JExcel();
		export.saveAs("modules_" + moduleType + ".xls", rs, Common.mainForm);
		populateList("");
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(518, 511));
			this.setBounds(0, 0, 563, 703);
			setVisible(true);
			this.setTitle("Module Admin");
			this.setClosable(true);
			this.setIconifiable(true);
			{
				{
					buttonGroup1 = new ButtonGroup();

				}
				{
				}
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(410, 272));
				jDesktopPane1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.setBounds(0, 0, 365, 671);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						ListModel<JDBListData> jListModulesModel = new DefaultComboBoxModel<JDBListData>();
						jListModules = new JDBModuleJList(Common.selectedHostID, Common.sessionID);
						jListModules.addListSelectionListener(new ListSelectionListener()
						{
							public void valueChanged(ListSelectionEvent e)
							{
								int sel = jListModules.getSelectedIndex();
								if (sel > -1)
								{
									JDBListData item = (JDBListData) jListModules.getModel().getElementAt(sel);
									mod.getModuleProperties(item.getmData().toString());
									if (mod.getType().equals("REPORT"))
									{
										jButtonAlternative.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_ALTERNATE"));
									} else
									{
										jButtonAlternative.setEnabled(false);
									}
									if (mod.getType().equals("FORM"))
									{
										jButtonTestHelp.setEnabled(true);
									} else
									{
										jButtonTestHelp.setEnabled(false);
									}
								}
							}
						});
						jScrollPane1.setViewportView(jListModules);
						jListModules.setModel(jListModulesModel);
						jListModules.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_EDIT") == true)
									{
										editRecord();
									}
								}
							}
						});

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jListModules, popupMenu);

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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_ADD"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_DELETE"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_EDIT"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_RENAME"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_MODULES"));
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
					jButtonAdd.setBounds(378, 10, 167, 32);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_ADD"));
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
					jButtonDelete.setBounds(378, 41, 167, 32);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setFocusTraversalKeysEnabled(false);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_DELETE"));
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
					jButtonEdit.setBounds(378, 72, 167, 32);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_EDIT"));
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jButtonPrint.setBounds(378, 196, 167, 32);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_MODULES"));
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							print();
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(378, 384, 167, 32);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jButtonRename = new JButton4j(Common.icon_rename_16x16);
					jButtonRename.setBounds(378, 103, 167, 32);
					jDesktopPane1.add(jButtonRename);
					jButtonRename.setText(lang.get("btn_Rename"));
					jButtonRename.setMnemonic(lang.getMnemonicChar());
					jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_RENAME"));
					jButtonRename.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							rename();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(378, 322, 167, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
					jButtonRefresh.setBounds(378, 353, 167, 32);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							populateList("");
						}
					});
				}
				{
					jRadioButtonAll = new JRadioButton();
					jRadioButtonAll.setBounds(378, 424, 167, 28);
					jDesktopPane1.add(jRadioButtonAll);
					jRadioButtonAll.setText(lang.get("lbl_Module_ALL"));
					jRadioButtonAll.setFont(Common.font_bold);
					buttonGroup1.add(jRadioButtonAll);
					jRadioButtonAll.setBackground(Common.color_app_window);
					jRadioButtonAll.setSelected(true);
					jRadioButtonAll.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							selectedModuleType = "ALL";
							jRadioButtonActionPerformed(evt);
						}
					});
				}
				{
					jRadioButtonForms = new JRadioButton();
					jRadioButtonForms.setBounds(378, 452, 167, 28);
					jDesktopPane1.add(jRadioButtonForms);
					jRadioButtonForms.setText(lang.get("lbl_Module_Form"));
					jRadioButtonForms.setFont(Common.font_bold);
					buttonGroup1.add(jRadioButtonForms);
					jRadioButtonForms.setBackground(Common.color_app_window);
					jRadioButtonForms.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							selectedModuleType = "FORM";
							jRadioButtonActionPerformed(evt);
						}
					});
				}
				{
					jRadioButtonFunctions = new JRadioButton();
					jRadioButtonFunctions.setBounds(378, 480, 167, 28);
					jDesktopPane1.add(jRadioButtonFunctions);
					jRadioButtonFunctions.setText(lang.get("lbl_Module_Function"));
					jRadioButtonFunctions.setFont(Common.font_bold);
					buttonGroup1.add(jRadioButtonFunctions);
					jRadioButtonFunctions.setBackground(Common.color_app_window);
					jRadioButtonFunctions.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							selectedModuleType = "FUNCTION";
							jRadioButtonActionPerformed(evt);
						}
					});
				}
				{
					jRadioButtonMenus = new JRadioButton();
					jRadioButtonMenus.setBounds(378, 508, 167, 28);
					jDesktopPane1.add(jRadioButtonMenus);
					jRadioButtonMenus.setText(lang.get("lbl_Module_Menu"));
					jRadioButtonMenus.setFont(Common.font_bold);
					buttonGroup1.add(jRadioButtonMenus);
					jRadioButtonMenus.setBackground(Common.color_app_window);
					jRadioButtonMenus.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							selectedModuleType = "MENU";
							jRadioButtonActionPerformed(evt);
						}
					});
				}
				{
					jRadioButtonReports = new JRadioButton();
					jRadioButtonReports.setBounds(378, 536, 167, 28);
					jDesktopPane1.add(jRadioButtonReports);
					jRadioButtonReports.setText(lang.get("lbl_Module_Report"));
					jRadioButtonReports.setFont(Common.font_bold);
					buttonGroup1.add(jRadioButtonReports);
					jRadioButtonReports.setBackground(Common.color_app_window);
					jRadioButtonReports.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							selectedModuleType = "REPORT";
							jRadioButtonActionPerformed(evt);
						}
					});
				}
				{
					jRadioButtonUserReports = new JRadioButton();
					jRadioButtonUserReports.setBounds(378, 592, 167, 28);
					jDesktopPane1.add(jRadioButtonUserReports);
					jRadioButtonUserReports.setText(lang.get("lbl_Module_UserReport"));
					jRadioButtonUserReports.setFont(Common.font_bold);
					buttonGroup1.add(jRadioButtonUserReports);
					jRadioButtonUserReports.setBackground(Common.color_app_window);
					jRadioButtonUserReports.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							selectedModuleType = "USER";
							jRadioButtonActionPerformed(evt);
						}
					});
				}

				{
					jRadioButtonExec = new JRadioButton();
					jRadioButtonExec.setBounds(378, 564, 167, 28);
					jDesktopPane1.add(jRadioButtonExec);
					jRadioButtonExec.setText(lang.get("lbl_Module_Executable"));
					jRadioButtonExec.setFont(Common.font_bold);
					jRadioButtonExec.setBackground(Common.color_app_window);
					buttonGroup1.add(jRadioButtonExec);
					jRadioButtonExec.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							selectedModuleType = "EXEC";
							jRadioButtonActionPerformed(evt);
						}
					});
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.setBounds(378, 290, 167, 32);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							excel();
						}
					});
					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jDesktopPane1.add(jButtonExcel);
				}
				{
					JButton4j jButtonGroups = new JButton4j(Common.icon_groups_16x16);
					jButtonGroups.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							showGroupMembership();
						}
					});
					jButtonGroups.setText(lang.get("mod_FRM_ADMIN_GROUPS"));
					jButtonGroups.setMnemonic('G');
					jButtonGroups.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_GROUPS"));
					jButtonGroups.setBounds(378, 165, 167, 32);
					jDesktopPane1.add(jButtonGroups);
				}

				{
					jButtonAlternative = new JButton4j(Common.icon_alternative_16x16);
					jButtonAlternative.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							lModuleId = ((JDBListData) jListModules.getSelectedValue()).toString();
							JLaunchMenu.runDialog("FRM_ADMIN_MODULE_ALTERNATE", lModuleId);
							populateList(lModuleId);
						}
					});
					jButtonAlternative.setText(lang.get("btn_Alternative"));
					jButtonAlternative.setMnemonic('A');
					jButtonAlternative.setEnabled(false);
					jButtonAlternative.setBounds(378, 226, 167, 32);
					jDesktopPane1.add(jButtonAlternative);

				}
				
				{
					jButtonTestHelp = new JButton4j(Common.icon_help_16x16);
					jButtonTestHelp.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							lModuleId = ((JDBListData) jListModules.getSelectedValue()).toString();
							JDBModule m = new JDBModule(Common.selectedHostID, Common.sessionID);
							m.getModuleProperties(lModuleId);
							JHelp help = new JHelp();
							help.testHelpURL(m.getHelpSetID());
						}
					});
					jButtonTestHelp.setText(lang.get("btn_TestHelp"));
					jButtonTestHelp.setMnemonic('A');
					jButtonTestHelp.setEnabled(false);
					jButtonTestHelp.setBounds(378, 257, 167, 32);
					jDesktopPane1.add(jButtonTestHelp);

				}

				{
					JButton4j jButtonCopy = new JButton4j(Common.icon_copy_16x16);
					jButtonCopy.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							copyRecord();
						}
					});
					jButtonCopy.setText(lang.get("btn_Copy"));
					jButtonCopy.setMnemonic('0');
					jButtonCopy.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_COPY"));
					jButtonCopy.setBounds(378, 134, 167, 32);
					jDesktopPane1.add(jButtonCopy);
				}

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void showGroupMembership()
	{
		if (jListModules.isSelectionEmpty() == false)
		{
			lModuleId = ((JDBListData) jListModules.getSelectedValue()).toString();
			JLaunchMenu.runForm("FRM_ADMIN_MODULE_MEMBERS", lModuleId);
		}
	}

	private void jRadioButtonActionPerformed(ActionEvent evt)
	{
		populateList("");
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
