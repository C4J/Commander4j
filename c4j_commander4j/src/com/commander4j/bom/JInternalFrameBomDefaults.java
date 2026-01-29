package com.commander4j.bom;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameControlAdmin.java
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
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
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

public class JInternalFrameBomDefaults extends javax.swing.JInternalFrame
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
	private String sourceField;
	private String sourceValue;
	private String destinationField;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBBomList bomLists = new JDBBomList(Common.selectedHostID, Common.sessionID);
	private JDBBomDefaults bomDefault = new JDBBomDefaults(Common.selectedHostID, Common.sessionID);
	private PreparedStatement listStatement;

	public JInternalFrameBomDefaults()
	{
		super();
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_BOM_DEFAULTS"));

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
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_BOM_DEFAULTS order by source_field,source_value,destination_field,destination_value"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();

		listStatement = query.getPreparedStatement();
	}

	private PreparedStatement buildSQLr()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);

		query.clear();
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_BOM_DEFAULTS order by source_field,source_value,destination_field,destination_value"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();

		listStatement = query.getPreparedStatement();

		return listStatement;
	}

	private void populateList(String defaultitem)
	{

		JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
		buildSQL();
		JDBBomDefaultsTableModel bomdefaultstable = new JDBBomDefaultsTableModel(Common.selectedHostID, Common.sessionID, ctrl.getControlDataResultSet(listStatement));
		TableRowSorter<JDBBomDefaultsTableModel> sorter = new TableRowSorter<JDBBomDefaultsTableModel>(bomdefaultstable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(bomdefaultstable);

		jScrollPane1.setViewportView(jTable1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.source_field_desc_col).setPreferredWidth(100);

		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.source_field_icon_col).setPreferredWidth(20);

		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.source_value_col).setPreferredWidth(100);

		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.destination_field_desc_col).setPreferredWidth(100);

		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.destination_field_icon_col).setPreferredWidth(20);

		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.destination_value_col).setPreferredWidth(100);

		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.source_field_col).setWidth(0);
		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.source_field_col).setMinWidth(0);
		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.source_field_col).setMaxWidth(0);

		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.destination_field_col).setWidth(0);
		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.destination_field_col).setMinWidth(0);
		jTable1.getColumnModel().getColumn(JDBBomDefaultsTableModel.destination_field_col).setMaxWidth(0);

		jScrollPane1.repaint();
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(669, 341));
			this.setBounds(0, 0, 579, 422);
			setVisible(true);
			this.setClosable(true);
			this.getContentPane().setLayout(null);
			this.setIconifiable(true);
			this.getContentPane().setBackground(Common.color_app_window);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.Table);

			this.getContentPane().add(jScrollPane1);
			jScrollPane1.setBounds(0, 0, 461, 390);
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
							if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_DEFAULTS_EDIT") == true)
							{
								editRecord();
							}
						}
					}
				});
				populateList("");

			}

			jButtonEdit = new JButton4j(Common.icon_edit_16x16);
			this.getContentPane().add(jButtonEdit);
			jButtonEdit.setText(lang.get("btn_Edit"));
			jButtonEdit.setBounds(462, 31, 106, 32);
			jButtonEdit.setMnemonic(lang.getMnemonicChar());
			jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_DEFAULTS_EDIT"));
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
			jButtonRefresh.setBounds(462, 155, 106, 32);
			jButtonRefresh.setMnemonic(lang.getMnemonicChar());
			jButtonRefresh.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					populateList("");
				}
			});

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			this.getContentPane().add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setBounds(462, 217, 106, 32);
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
			jButtonAdd.setBounds(462, 0, 106, 32);
			jButtonAdd.setMnemonic(lang.getMnemonicChar());
			jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_DEFAULTS_ADD"));
			jButtonAdd.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					// *Source Field**

					DefaultComboBoxModel<JDBBomListRecord> sourceComboBoxMod = new DefaultComboBoxModel<JDBBomListRecord>();
					LinkedList<JDBBomListRecord> listSource = bomLists.getListValues("source_field");
					sourceComboBoxMod.addAll(listSource);

					JComboBox<JDBBomListRecord> comboBoxSource = new JComboBox<JDBBomListRecord>();
					comboBoxSource.setModel(sourceComboBoxMod);

					if (listSource.size() > 0)
					{
						comboBoxSource.setSelectedIndex(0);
					}
					int result = JOptionPane.showConfirmDialog(Common.mainForm, comboBoxSource, lang.get("lbl_Source_Field"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

					if (result == JOptionPane.OK_OPTION)
					{

						String selectedSource = ((JDBBomListRecord) comboBoxSource.getSelectedItem()).getItem();

						// Source Value //
						String sourceValue = JUtility.replaceNullStringwithBlank(JOptionPane.showInputDialog(Common.mainForm, lang.get("lbl_Source_Value") + " for " + ((JDBBomListRecord) comboBoxSource.getSelectedItem()).getItem()));

						if (sourceValue.equals("") == false)
						{

							// *Destination Field**
							DefaultComboBoxModel<JDBBomListRecord> destComboBoxMod = new DefaultComboBoxModel<JDBBomListRecord>();
							LinkedList<JDBBomListRecord> listDestination = bomLists.getListValues("destination_field");
							destComboBoxMod.addAll(listDestination);

							JComboBox<JDBBomListRecord> comboBoxDestination = new JComboBox<JDBBomListRecord>();
							comboBoxDestination.setModel(destComboBoxMod);

							if (listDestination.size() > 0)
							{
								comboBoxDestination.setSelectedIndex(0);
							}

							result = JOptionPane.showConfirmDialog(Common.mainForm, comboBoxDestination, lang.get("lbl_Destination_Field"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

							if (result == JOptionPane.OK_OPTION)
							{

								String selectedDestination = ((JDBBomListRecord) comboBoxDestination.getSelectedItem()).getItem();
								JLaunchMenu.runDialog("FRM_BOM_DEFAULTS_EDIT", selectedSource, sourceValue, selectedDestination);
								populateList("");
							}
						}
					}
				}
			});

			jButtonDelete = new JButton4j(Common.icon_delete_16x16);
			this.getContentPane().add(jButtonDelete);
			jButtonDelete.setText(lang.get("btn_Delete"));
			jButtonDelete.setBounds(462, 62, 106, 32);
			jButtonDelete.setMnemonic(lang.getMnemonicChar());
			jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_DEFAULTS_DELETE"));
			jButtonDelete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Delete"), lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
					if (question == 0)
					{

						int row = jTable1.getSelectedRow();
						if (row >= 0)
						{
							sourceField = jTable1.getValueAt(row, JDBBomDefaultsTableModel.source_field_col).toString();
							sourceValue = jTable1.getValueAt(row, JDBBomDefaultsTableModel.source_value_col).toString();
							destinationField = jTable1.getValueAt(row, JDBBomDefaultsTableModel.destination_field_col).toString();
							bomDefault.delete(sourceField, sourceValue, destinationField);
							populateList("");
						}
					}
				}
			});

			jButtonPrint = new JButton4j(Common.icon_report_16x16);
			this.getContentPane().add(jButtonPrint);
			jButtonPrint.setText(lang.get("btn_Print"));
			jButtonPrint.setBounds(462, 124, 106, 32);
			jButtonPrint.setMnemonic(lang.getMnemonicChar());
			jButtonPrint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JLaunchReport.runReport("RPT_BOM_DEFAULTS", null, "", null, "");
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			this.getContentPane().add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setBounds(462, 186, 106, 32);
			jButtonHelp.setMnemonic(lang.getMnemonicChar());

			jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
			jButtonExcel.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					JExcel export = new JExcel();
					PreparedStatement temp = buildSQLr();
					export.saveAs("bom_defaults.xls", bomDefault.getBomDefaultsResultSet(temp), Common.mainForm);
					populateList("");
				}
			});
			jButtonExcel.setText(lang.get("btn_Excel"));
			jButtonExcel.setMnemonic(lang.getMnemonicChar());
			jButtonExcel.setBounds(462, 93, 106, 32);
			getContentPane().add(jButtonExcel);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			sourceField = jTable1.getValueAt(row, JDBBomDefaultsTableModel.source_field_col).toString();
			sourceValue = jTable1.getValueAt(row, JDBBomDefaultsTableModel.source_value_col).toString();
			destinationField = jTable1.getValueAt(row, JDBBomDefaultsTableModel.destination_field_col).toString();

			JLaunchMenu.runDialog("FRM_BOM_DEFAULTS_EDIT", sourceField, sourceValue, destinationField);

			populateList("");
		}
	}

	private void jButtonCloseActionPerformed(ActionEvent evt)
	{
		JDBQuery.closeStatement(listStatement);
		dispose();
	}

}
