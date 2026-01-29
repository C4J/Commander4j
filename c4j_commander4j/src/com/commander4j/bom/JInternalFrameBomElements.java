package com.commander4j.bom;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameBomElements.java
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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameBomElements extends javax.swing.JInternalFrame
{
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JScrollPane4j jScrollPane1;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JTable4j jTable1;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	private JDBBomElement bomElement = new JDBBomElement(Common.selectedHostID, Common.sessionID);
	private PreparedStatement listStatement;

	public JInternalFrameBomElements()
	{
		super();
		initGUI();
		refresh();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_BOM_ELEMENTS"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);

		query.clear();
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_BOM_ELEMENTS order by data_id"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();

		listStatement = query.getPreparedStatement();
	}

	public void setDataID(String id)
	{
		populateList(id);
	}

	private PreparedStatement buildSQLr()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);

		query.clear();
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_BOM_ELEMENTS order by data_id"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();

		listStatement = query.getPreparedStatement();

		return listStatement;
	}

	private void refresh()
	{
		String data_id = "";
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			data_id = jTable1.getValueAt(row, JDBBomElementsTableModel.data_id_col).toString();
		}
		refresh(data_id);
	}

	private void refresh(String id)
	{
		populateList(id);
	}

	private void populateList(String defaultitem)
	{

		JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
		buildSQL();
		JDBBomElementsTableModel bomelementstable = new JDBBomElementsTableModel(Common.selectedHostID, Common.sessionID, ctrl.getControlDataResultSet(listStatement));
		TableRowSorter<JDBBomElementsTableModel> sorter = new TableRowSorter<JDBBomElementsTableModel>(bomelementstable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(bomelementstable);

		jScrollPane1.setViewportView(jTable1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable1.getTableHeader().setPreferredSize(new Dimension(jScrollPane1.getWidth(), 35));

		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.data_id_col).setPreferredWidth(110);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.data_type_col).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.description_col).setPreferredWidth(130);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.icon_filename_col).setPreferredWidth(110);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.icon_image_col).setPreferredWidth(20);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.mac_occurs_level_col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.enable_create_col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.enable_edit_col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.enable_delete_col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.enable_duplicate_col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.enable_clipboard_col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.enable_lookup_col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.lookup_sql_col).setPreferredWidth(300);
		jTable1.getColumnModel().getColumn(JDBBomElementsTableModel.lookup_field_col).setPreferredWidth(100);

		if (defaultitem.equals("") == false)
		{
			for (int x = 0; x <= jTable1.getModel().getRowCount(); x++)
			{
				Object val = jTable1.getModel().getValueAt(x, JDBBomElementsTableModel.data_id_col);
				System.out.println(x);
				System.out.println(val);

				if (String.valueOf(val).equals(defaultitem))
				{
					jTable1.setRowSelectionInterval(x, x);
				}
			}
		}

		jScrollPane1.repaint();
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(669, 341));
			this.setBounds(0, 0, 990, 422);
			setVisible(true);
			this.setClosable(true);
			this.getContentPane().setLayout(null);
			this.setIconifiable(true);
			this.getContentPane().setBackground(Common.color_app_window);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.Table);

			this.getContentPane().add(jScrollPane1);
			jScrollPane1.setBounds(0, 0, 872, 390);
			jScrollPane1.setFont(Common.font_std);
			{
				jTable1 = new JTable4j();
				jTable1.getTableHeader().setBounds(0, 0, 629, 16);

				jTable1.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent evt)
					{
						if (evt.getClickCount() == 2)
						{
							if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_ELEMENTS_EDIT") == true)
							{
								editRecord();
							}
						}
					}
				});
			}

			jButtonEdit = new JButton4j(Common.icon_edit_16x16);
			this.getContentPane().add(jButtonEdit);
			jButtonEdit.setText(lang.get("btn_Edit"));
			jButtonEdit.setBounds(873, 31, 106, 32);
			jButtonEdit.setMnemonic(lang.getMnemonicChar());
			jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_ELEMENTS_EDIT"));
			jButtonEdit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					editRecord();
				}
			});

			jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
			this.getContentPane().add(jButtonRefresh);
			jButtonRefresh.setText(lang.get("btn_Refresh"));
			jButtonRefresh.setBounds(873, 155, 106, 32);
			jButtonRefresh.setMnemonic(lang.getMnemonicChar());
			jButtonRefresh.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					refresh();
				}
			});

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			this.getContentPane().add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setBounds(873, 217, 106, 32);
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButtonCloseActionPerformed(evt);
				}
			});

			jButtonAdd = new JButton4j(Common.icon_add_16x16);
			this.getContentPane().add(jButtonAdd);
			jButtonAdd.setText(lang.get("btn_Add"));
			jButtonAdd.setBounds(873, 0, 106, 32);
			jButtonAdd.setMnemonic(lang.getMnemonicChar());
			jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_ELEMENTS_ADD"));
			jButtonAdd.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					addRecord();
				}
			});

			jButtonDelete = new JButton4j(Common.icon_delete_16x16);
			this.getContentPane().add(jButtonDelete);
			jButtonDelete.setText(lang.get("btn_Delete"));
			jButtonDelete.setBounds(873, 62, 106, 32);
			jButtonDelete.setMnemonic(lang.getMnemonicChar());
			jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_ELEMENTS_DELETE"));
			jButtonDelete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					deleteRecord();
				}
			});

			jButtonPrint = new JButton4j(Common.icon_report_16x16);
			this.getContentPane().add(jButtonPrint);
			jButtonPrint.setText(lang.get("btn_Print"));
			jButtonPrint.setBounds(873, 124, 106, 32);
			jButtonPrint.setMnemonic(lang.getMnemonicChar());
			jButtonPrint.setEnabled(true);
			jButtonPrint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JLaunchReport.runReport("RPT_BOM_ELEMENTS", null, "", null, "");
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			this.getContentPane().add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setBounds(873, 186, 106, 32);
			jButtonHelp.setMnemonic(lang.getMnemonicChar());

			jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
			jButtonExcel.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					JExcel export = new JExcel();
					PreparedStatement temp = buildSQLr();
					export.saveAs("bom_elements.xls", bomElement.getBomElementsResultSet(temp), Common.mainForm);
					refresh();
				}
			});
			jButtonExcel.setText(lang.get("btn_Excel"));
			jButtonExcel.setMnemonic(lang.getMnemonicChar());
			jButtonExcel.setBounds(873, 93, 106, 32);
			getContentPane().add(jButtonExcel);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void addRecord()
	{
		String data_id = (String) JOptionPane.showInputDialog(Common.mainForm, "Data ID", null, JOptionPane.QUESTION_MESSAGE, Common.icon_confirm_16x16, null, null);
		if (data_id != null)
		{
			if (data_id.equals("") == false)
			{
				data_id = data_id.toLowerCase();

				if (bomElement.isValid(data_id) == false)
				{
					JLaunchMenu.runForm("FRM_BOM_ELEMENTS_EDIT", data_id);

				}
				else
				{
					JOptionPane.showMessageDialog(Common.mainForm, "Data ID [" + data_id + "] already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
				}
			}
		}
	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			String data_id = jTable1.getValueAt(row, JDBBomElementsTableModel.data_id_col).toString();

			JLaunchMenu.runForm("FRM_BOM_ELEMENTS_EDIT", data_id);

		}
	}

	private void jButtonCloseActionPerformed(ActionEvent evt)
	{
		JDBQuery.closeStatement(listStatement);
		dispose();
	}

	private void deleteRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			String data_id = jTable1.getValueAt(row, JDBBomElementsTableModel.data_id_col).toString();

			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Delete") + " " + data_id, lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (n == 0)
			{

				if (bomElement.delete(data_id) == false)

				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, bomElement.getErrorMessage(), "Delete error (" + data_id + ")", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					refresh();
				}
			}
		}
	}

}
