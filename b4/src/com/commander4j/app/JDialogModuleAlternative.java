package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogAutoLabellerProperties.java
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBModuleAlternative;
import com.commander4j.db.JDBQuery2;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * This Dialog box allow you to assign an alternative report module based on
 * workstation name.
 *
 * <p>
 * <img alt="" src="./doc-files/JDialogModuleAlternative.jpg" >
 * 
 */

public class JDialogModuleAlternative extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonAdd;
	private JButton4j jButtonExcel;
	private JButton4j jButtonUpdate;
	private JButton4j jButtonDelete;
	private JLabel4j_std jLabelLineID;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JList4j<String> listWorkstations = new JList4j<String>();
	private JComboBox4j<String> comboBox4jAlternativeModule = new JComboBox4j<String>();
	private String selectedModule = "";
	private String selectedAlternativeModule = "";

	public JDialogModuleAlternative(JFrame frame, String module)
	{
		super(frame, "Alternatives for Module ", ModalityType.DOCUMENT_MODAL);

		selectedModule = module;

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Alternatives for Module ");
		this.setResizable(false);
		this.setSize(433, 492);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		jDesktopPane1 = new JDesktopPane();
		jDesktopPane1.setBounds(0, 200, 671, -200);
		jDesktopPane1.setBackground(Common.color_edit_properties);
		this.getContentPane().add(jDesktopPane1);
		jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));
		jDesktopPane1.setLayout(null);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MODULE_ALTERNATE"));

		initGUI();

		setTitle(getTitle() + " - " + selectedModule);

		populateListWorkstations("");

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{

			}
		});

	}

	/**
	 * Method used to build GUI
	 */
	private void initGUI()
	{
		try
		{

			{

				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					jLabelLineID = new JLabel4j_std();
					jDesktopPane1.add(jLabelLineID, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelLineID.setText(lang.get("btn_Alternative"));
					jLabelLineID.setHorizontalAlignment(SwingConstants.LEFT);
					jLabelLineID.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelLineID.setBounds(29, 393, 139, 21);
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							update();
						}
					});
					jDesktopPane1.add(jButtonUpdate, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.setBounds(298, 87, 112, 32);

				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(298, 175, 112, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(298, 204, 112, 32);
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
					jButtonExcel.setBounds(298, 116, 112, 32);
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

				jButtonUpdate.setEnabled(false);

				JButton4j jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
				jButtonRefresh.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						refreshWorkstationList();
					}
				});
				jButtonRefresh.setText(lang.get("btn_Refresh"));
				jButtonRefresh.setBounds(298, 145, 112, 32);
				jDesktopPane1.add(jButtonRefresh);

				JScrollPane scrollPaneWorkstations = new JScrollPane();
				scrollPaneWorkstations.setBounds(42, 50, 216, 286);
				jDesktopPane1.add(scrollPaneWorkstations);
				listWorkstations.addListSelectionListener(new ListSelectionListener()
				{
					public void valueChanged(ListSelectionEvent e)
					{

						JDBModuleAlternative po = new JDBModuleAlternative(Common.selectedHostID, Common.sessionID);

						String selectedWorkstation = "";
						if (listWorkstations.getSelectedIndex() != -1)
						{
							selectedWorkstation = listWorkstations.getSelectedValue().toString();
						}

						po.getProperties(selectedModule, selectedWorkstation);

						String altModule = po.getAlternativeModuleId();

						populateListModules(altModule);

						jButtonUpdate.setEnabled(false);
					}
				});

				listWorkstations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPaneWorkstations.setViewportView(listWorkstations);

				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Workstation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));
				panel_1.setBounds(29, 21, 257, 360);
				jDesktopPane1.add(panel_1);
				panel_1.setLayout(null);

				JButton4j jButtonAddWorkstation = new JButton4j(Common.icon_add);
				jButtonAddWorkstation.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						addWorkstationRecord();
					}
				});
				jButtonAddWorkstation.setBounds(16, 320, 25, 25);
				panel_1.add(jButtonAddWorkstation);

				JButton4j jButtonDeleteWorkstation = new JButton4j(Common.icon_delete);
				jButtonDeleteWorkstation.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						deleteWorkstationRecord();
					}
				});
				jButtonDeleteWorkstation.setBounds(45, 320, 25, 25);
				panel_1.add(jButtonDeleteWorkstation);

			}
			comboBox4jAlternativeModule.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			comboBox4jAlternativeModule.setBounds(29, 416, 386, 21);
			jDesktopPane1.add(comboBox4jAlternativeModule);

			{
				jButtonDelete = new JButton4j(Common.icon_delete);
				jButtonDelete.setBounds(298, 57, 112, 32);
				jDesktopPane1.add(jButtonDelete);
				jButtonDelete.setText(lang.get("btn_Delete"));
				jButtonDelete.setMnemonic(lang.getMnemonicChar());
				jButtonDelete.setFocusTraversalKeysEnabled(false);
				jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_DELETE"));
				jButtonDelete.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						deleteWorkstationRecord();
					}
				});
			}

			{
				jButtonAdd = new JButton4j(Common.icon_add);
				jButtonAdd.setBounds(298, 28, 112, 32);
				jDesktopPane1.add(jButtonAdd);
				jButtonAdd.setText(lang.get("btn_Add"));
				jButtonAdd.setMnemonic(lang.getMnemonicChar());
				jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_ADD"));
				jButtonAdd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						addWorkstationRecord();
					}
				});
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void excel()
	{
		JDBModuleAlternative material = new JDBModuleAlternative(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		PreparedStatement temp = buildSQLr();
		export.saveAs("alternative_modules.xls", material.getDataResultSet(temp), this);
		refreshWorkstationList();
	}

	private PreparedStatement buildSQLr()
	{

		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("{schema}SYS_MODULES_ALTERNATIVE");
		q2.applyWhere("module_id=", selectedModule);

		q2.applySort("module_id,workstation_id", false);
		q2.applyRestriction(false, 0);
		q2.applySQL();
		result = q2.getPreparedStatement();

		return result;

	}

	private void refreshWorkstationList()
	{
		String defaultItem = "";

		if (listWorkstations.isSelectionEmpty() == false)
		{
			defaultItem = ((String) listWorkstations.getSelectedValue()).toString();
		}
		populateListWorkstations(defaultItem);
	}

	private void deleteWorkstationRecord()
	{
		if (listWorkstations.isSelectionEmpty() == false)
		{
			String item = ((String) listWorkstations.getSelectedValue()).toString();
			int n = JOptionPane.showConfirmDialog(this, lang.get("btn_Delete") + " " + lang.get("lbl_Workstation") + " [" + item + "]", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (n == 0)
			{
				JDBModuleAlternative po = new JDBModuleAlternative(Common.selectedHostID, Common.sessionID);
				po.delete(selectedModule, item);
				populateListWorkstations("");
			}
		}
	}

	private void addWorkstationRecord()
	{
		JDBModuleAlternative u = new JDBModuleAlternative(Common.selectedHostID, Common.sessionID);
		String lworkstation_id = "";
		lworkstation_id = JOptionPane.showInputDialog(this, lang.get("btn_Add") + " " + lang.get("lbl_Workstation"));
		if (lworkstation_id != null)
		{
			if (lworkstation_id.equals("") == false)
			{
				lworkstation_id = lworkstation_id.toUpperCase();
				try
				{
					selectedAlternativeModule = comboBox4jAlternativeModule.getSelectedItem().toString();
				} catch (Exception ex)
				{
					selectedAlternativeModule = "";
				}

				if (selectedAlternativeModule.equals(""))
				{
					selectedAlternativeModule = selectedModule;
					comboBox4jAlternativeModule.setSelectedItem(selectedAlternativeModule);
				}

				if (u.create(selectedModule, lworkstation_id, selectedAlternativeModule))
				{
					populateListWorkstations(lworkstation_id);
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Workstation [" + lworkstation_id + "] already defined", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
				}
			}
		}
	}

	private void update()
	{
		if (listWorkstations.isSelectionEmpty() == false)
		{
			String workstation = ((String) listWorkstations.getSelectedValue()).toString();
			JDBModuleAlternative po = new JDBModuleAlternative(Common.selectedHostID, Common.sessionID);
			po.setModuleId(selectedModule);
			po.setWorkstationId(workstation);
			po.setAternativeModuleId(comboBox4jAlternativeModule.getSelectedItem().toString());
			po.update();
			populateListWorkstations(workstation);
		}
	}

	private void populateListWorkstations(String defaultWorkstation)
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		JDBModuleAlternative altMod = new JDBModuleAlternative(Common.selectedHostID, Common.sessionID);
		LinkedList<String> tempWorkstationList = altMod.getWorkstations(selectedModule);

		int sel = -1;
		for (int j = 0; j < tempWorkstationList.size(); j++)
		{
			defComboBoxMod.addElement(tempWorkstationList.get(j));
			if (tempWorkstationList.get(j).toString().equals(defaultWorkstation))
			{
				sel = j;
			}
		}

		ListModel<String> jList1Model = defComboBoxMod;
		listWorkstations.setModel(jList1Model);
		listWorkstations.setSelectedIndex(sel);
	}

	private void populateListModules(String defaultitem)
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		JDBModule tempModule = new JDBModule(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBListData> tempModuleList = new LinkedList<JDBListData>();

		tempModuleList = tempModule.getModuleIdsByType("REPORT");

		int sel = -1;
		for (int j = 0; j < tempModuleList.size(); j++)
		{

			defComboBoxMod.addElement(tempModuleList.get(j).toString());
			if (tempModuleList.get(j).toString().equals(defaultitem))
			{
				sel = j;
			}
		}

		comboBox4jAlternativeModule.setModel(defComboBoxMod);

		comboBox4jAlternativeModule.setSelectedIndex(sel);

	}

}
