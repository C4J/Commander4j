package com.commander4j.sys;

import java.awt.Color;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePrinterAdmin.java
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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBPrinterLineMembership;
import com.commander4j.db.JDBPrinters;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JRadioButton4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.tablemodel.JDBPrintersTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class JInternalFramePrinterAdmin extends javax.swing.JInternalFrame {
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JScrollPane jScrollPane1;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JTable4j jTable1;
	private String printerID;
	private String groupID;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private PreparedStatement listStatement;
	private JLabel4j_std jStatusText = new JLabel4j_std();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton4j rdbtnAll = new JRadioButton4j("All");
	private JRadioButton4j rdbtnPack = new JRadioButton4j("Pack");
	private JRadioButton4j rdbtnPallet = new JRadioButton4j("Pallet");
	private JComboBox4j<String> comboBoxPrinterTypes = new JComboBox4j<String>();
	
	public JInternalFramePrinterAdmin()
	{
		super();
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_PRINTERS"));
				
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

	}
	
	private String getFilterGroupString()
	{
		String result = "%";
		
		if (rdbtnPack.isSelected())
		{
			result = "Pack";
		}
		
		if (rdbtnPallet.isSelected())
		{
			result = "Pallet";
		}		
		
		return result;
	}
	
	private String getFilterPrinterTypeString()
	{
		String result = "%";
		
		try
		{
		if (comboBoxPrinterTypes.getSelectedItem().toString().equals("") == false)
		{
			result = comboBoxPrinterTypes.getSelectedItem().toString();
		}
		}
		catch(Exception ex)
		{
			result = "%";
		}

		
		return result;
	}
	
	private void refresh()
	{
		String CurrentPrinter = "";
		String currentGroupID = "";
		int row = jTable1.getSelectedRow();
		if (row > -1)
		{
			CurrentPrinter = jTable1.getValueAt(row, JDBPrintersTableModel.PrinterID_Col).toString();
			currentGroupID = jTable1.getValueAt(row, JDBPrintersTableModel.Group_Col).toString();

		}
		populateList(CurrentPrinter, currentGroupID);

	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);

		query.clear();
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}SYS_PRINTERS  WHERE GROUP_ID LIKE ? AND PRINTER_TYPE LIKE ? order by printer_id"));
		query.addParameter(getFilterGroupString());
		query.addParameter(getFilterPrinterTypeString());
		query.applyRestriction(false, "none", 0);
		query.bindParams();

		listStatement = query.getPreparedStatement();
	}

	private void populateList(String printerid, String groupid)
	{

		JDBPrinters prn = new JDBPrinters(Common.selectedHostID, Common.sessionID);
		buildSQL();
		JDBPrintersTableModel printertable = new JDBPrintersTableModel(prn.getPrintersResultSet(listStatement));
		TableRowSorter<JDBPrintersTableModel> sorter = new TableRowSorter<JDBPrintersTableModel>(printertable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(printertable);

		jScrollPane1.setViewportView(jTable1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.PrinterID_Col).setPreferredWidth(210);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.Group_Col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.PrinterType_Col).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.IPAddress_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.Port_Col).setPreferredWidth(40);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.Description_Col).setPreferredWidth(255);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.Language_Col).setPreferredWidth(55);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.DPI_Col).setPreferredWidth(30);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.PaperSize_Col).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.Enabled_Col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.Direct_Enable_Col).setPreferredWidth(45);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.Export_Enable_Col).setPreferredWidth(45);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.Export_Format_Col).setPreferredWidth(45);
		jTable1.getColumnModel().getColumn(JDBPrintersTableModel.Export_Path_Col).setPreferredWidth(250);

		jScrollPane1.repaint();

		int rowCount = printertable.getRowCount();
		int selectedRow = -1;
		String prnID = "";
		for (int currentRow = 0; currentRow < rowCount; currentRow++)
		{
			prnID = (String) printertable.getValueAt(currentRow, JDBPrintersTableModel.PrinterID_Col);
			if (prnID.equals(printerid))
			{
				selectedRow = currentRow;
				break;
			}
		}
		if ((selectedRow == -1) && (rowCount > 0))
		{
			selectedRow = 0;
		}

		if (rowCount > 0)
		{
			jTable1.setRowSelectionInterval(selectedRow, selectedRow);
		}
		JUtility.setResultRecordCountColour(jStatusText, false, 1000, jTable1.getRowCount());
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(669, 341));
			this.setBounds(0, 0, 1025, 491);
			setVisible(true);
			this.setClosable(true);
			this.getContentPane().setLayout(null);
			this.setIconifiable(true);

			{
				jScrollPane1 = new JScrollPane();
				jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
				this.getContentPane().add(jScrollPane1);
				jScrollPane1.setBounds(0, 43, 1010, 349);
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
								if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PRINTER_EDIT") == true)
								{
									editRecord();
								}
							}
						}
					});
					populateList("", "");

				}

			}
			{
				jButtonEdit = new JButton4j(Common.icon_edit_16x16);
				this.getContentPane().add(jButtonEdit);
				jButtonEdit.setText(lang.get("btn_Edit"));
				jButtonEdit.setBounds(144, 400, 146, 32);
				jButtonEdit.setMnemonic(lang.getMnemonicChar());
				jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PRINTER_EDIT"));
				jButtonEdit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						editRecord();
					}
				});
			}
			{
				jButtonClose = new JButton4j(Common.icon_close_16x16);
				this.getContentPane().add(jButtonClose);
				jButtonClose.setText(lang.get("btn_Close"));
				jButtonClose.setBounds(864, 400, 146, 32);
				jButtonClose.setMnemonic(lang.getMnemonicChar());
				jButtonClose.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jButtonCloseActionPerformed(evt);
					}
				});
			}
			{

				jButtonAdd = new JButton4j(Common.icon_add_16x16);
				this.getContentPane().add(jButtonAdd);
				jButtonAdd.setText(lang.get("btn_Add"));
				jButtonAdd.setBounds(0, 400, 146, 32);
				jButtonAdd.setMnemonic(lang.getMnemonicChar());
				jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PRINTER_ADD"));
				jButtonAdd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						JDBPrinters prn = new JDBPrinters(Common.selectedHostID, Common.sessionID);

						printerID = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Printer_Create"));
						if (printerID != null)
						{
							if (printerID.equals("") == false)
							{
								
								int len = printerID.length();
								
								if (len > JDBPrinters.field_printer_id)
								{
									printerID = printerID.substring(1, JDBPrinters.field_printer_id);
								}
								
								Object[] printerGroups = Common.printerGroup;
								String groupId = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("lbl_Group_ID"), lang.get("lbl_Group_ID"), JOptionPane.PLAIN_MESSAGE, Common.icon_confirm_16x16, printerGroups, "Pack");

								// If a string was returned, say so.
								if ((groupId != null) && (groupId.length() > 0))
								{

									printerID = printerID.toUpperCase();
									if (prn.create(printerID, groupId) == false)
									{
										JUtility.errorBeep();
										JOptionPane.showMessageDialog(Common.mainForm, prn.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
									} else
									{

										populateList("", "");
										JLaunchMenu.runDialog("FRM_ADMIN_PRINTER_EDIT", printerID, groupId);
										populateList("", "");
									}
								}
							}
						}

					}
				});
			}
			{
				jButtonDelete = new JButton4j(Common.icon_delete_16x16);
				this.getContentPane().add(jButtonDelete);
				jButtonDelete.setText(lang.get("btn_Delete"));
				jButtonDelete.setBounds(288, 400, 146, 32);
				jButtonDelete.setMnemonic(lang.getMnemonicChar());
				jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PRINTER_DELETE"));
				jButtonDelete.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						int row = jTable1.getSelectedRow();
						if (row >= 0)
						{
							printerID = jTable1.getValueAt(row, JDBPrintersTableModel.PrinterID_Col).toString();
							groupID = jTable1.getValueAt(row, JDBPrintersTableModel.Group_Col).toString();

							int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Printer_Delete") + " " + printerID + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
							if (n == 0)
							{
								JDBPrinters c = new JDBPrinters(Common.selectedHostID, Common.sessionID);
								c.setPrinterID(printerID);
								c.setGroupID(groupID);
								if (c.delete())
								{
									JDBPrinterLineMembership plm = new JDBPrinterLineMembership(Common.selectedHostID, Common.sessionID);
									plm.removePrinterfromAllLines(printerID,groupID);
								}

								populateList("", "");
							}
						}
					}
				});
			}
			{
				jButtonPrint = new JButton4j(Common.icon_report_16x16);
				this.getContentPane().add(jButtonPrint);
				jButtonPrint.setText(lang.get("btn_Print"));
				jButtonPrint.setBounds(576, 400, 146, 32);
				jButtonPrint.setMnemonic(lang.getMnemonicChar());
				jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PRINTERS"));
				jButtonPrint.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						JLaunchReport.runReport("RPT_PRINTERS", null, "", null, "");
					}
				});
			}
			{
				jButtonHelp = new JButton4j(Common.icon_help_16x16);
				this.getContentPane().add(jButtonHelp);
				jButtonHelp.setText(lang.get("btn_Help"));
				jButtonHelp.setBounds(720, 400, 146, 32);
				jButtonHelp.setMnemonic(lang.getMnemonicChar());

			}

			{
				jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
				jButtonExcel.addActionListener(new ActionListener()
				{
					public void actionPerformed(final ActionEvent e)
					{
						JDBPrinters printers = new JDBPrinters(Common.selectedHostID, Common.sessionID);

						JExcel export = new JExcel();
						buildSQL();
						export.saveAs("printers.xls", printers.getPrintersResultSet(listStatement), Common.mainForm);
						populateList("","");
					}
				});
				jButtonExcel.setText(lang.get("btn_Excel"));
				jButtonExcel.setMnemonic(lang.getMnemonicChar());
				jButtonExcel.setBounds(432, 400, 146, 32);
				getContentPane().add(jButtonExcel);
			}
			
			buttonGroup.add(rdbtnAll);
			rdbtnAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refresh();
				}
			});
			rdbtnAll.setSelected(true);
			rdbtnAll.setBackground(Common.color_app_window);
			rdbtnAll.setBounds(120, 9, 54, 22);
			getContentPane().add(rdbtnAll);
			
			buttonGroup.add(rdbtnPack);
			rdbtnPack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refresh();
				}
			});
			rdbtnPack.setBackground(Common.color_app_window);
			rdbtnPack.setBounds(193, 9, 62, 22);
			getContentPane().add(rdbtnPack);
			
			buttonGroup.add(rdbtnPallet);
			rdbtnPallet.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refresh();
				}
			});
			rdbtnPallet.setBackground(Common.color_app_window);;
			rdbtnPallet.setBounds(268, 9, 67, 22);
			getContentPane().add(rdbtnPallet);

			jStatusText.setForeground(Color.BLACK);
			jStatusText.setBackground(Color.GRAY);
			jStatusText.setBounds(0, 435, 1010, 21);
			jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			getContentPane().add(jStatusText);
			comboBoxPrinterTypes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refresh();
				}
			});

			comboBoxPrinterTypes.setModel(new DefaultComboBoxModel<String>(Common.printerTypesAll));
			comboBoxPrinterTypes.setBounds(473, 9, 155, 22);
			getContentPane().add(comboBoxPrinterTypes);
			
			JLabel4j_std label4j_Type = new JLabel4j_std();
			label4j_Type.setText("Type");
			label4j_Type.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_Type.setBounds(387, 9, 75, 22);
			getContentPane().add(label4j_Type);
			
			JLabel4j_std label4j_Group = new JLabel4j_std();
			label4j_Group.setText("Group");
			label4j_Group.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_Group.setBounds(12, 9, 94, 22);
			getContentPane().add(label4j_Group);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			printerID = jTable1.getValueAt(row, JDBPrintersTableModel.PrinterID_Col).toString();
			groupID = jTable1.getValueAt(row, JDBPrintersTableModel.Group_Col).toString();

			JLaunchMenu.runDialog("FRM_ADMIN_PRINTER_EDIT", printerID, groupID);
			populateList(printerID, groupID);
		}
	}

	private void jButtonCloseActionPerformed(ActionEvent evt)
	{
		JDBQuery.closeStatement(listStatement);
		dispose();
	}
}
