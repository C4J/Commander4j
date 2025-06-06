package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramMaterialBatchAdmin.java
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery2;
import com.commander4j.db.JDBUser;
import com.commander4j.db.JDBViewWasteReporting;
import com.commander4j.db.JDBWasteLog;
import com.commander4j.db.JDBWasteReportingGroup;
import com.commander4j.db.JDBWasteReportingIDS;
import com.commander4j.db.JDBWasteTransactionType;
import com.commander4j.db.JDBWasteTypes;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JToggleButton4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBViewWasteReportingTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWasteLogAdmin allows the user to view/edit the table
 * APP_WASTE_LOG table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWasteLogAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBWasteLog JDBWasteLog
 */
public class JInternalFrameWasteReporting extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JButton4j jButtonPrint;
	private JButton4j jButtonExcel;
	private JLabel4j_std jStatusText;
	private JLabel4j_std jLabelProcessOrder;
	private JCheckBox4j jCheckBoxTo;
	private JCheckBox4j jCheckBoxFrom;
	private JLabel4j_std jLabel5_1;
	private JLabel4j_std jLabelRecycle;
	private JButton4j jButtonLookupWasteLocation;
	private JButton4j jButtonLookupWasteContainer;
	private JButton4j jButtonLookupWasteReportingID;
	private JButton4j jButtonLookupWasteMaterial;
	private JButton4j jButtonLookupWasteReason;
	private JButton4j jButtonLookupUserID;
	private JTextField4j jTextFieldProcessOrder;
	private JTextField4j jTextFieldUserID;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JToggleButton4j jToggleButtonSequence;
	private JButton4j jButtonLookupProcessOrder;
	private JTextField4j jTextFieldWasteLocation;
	private JTextField4j jTextFieldWasteContainer;
	private JTextField4j jTextFieldWasteReportingID;
	private JComboBox4j<JDBWasteTransactionType> jComboBoxTransactionType;
	private JComboBox4j<String> jComboBoxRecycle;
	private JComboBox4j<String> jComboBoxHazard;
	private JComboBox4j<JDBWasteReportingGroup> jComboBoxReportingGroup;
	private JLabel4j_std jLabelTransaction_Type;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel3a;
	private JLabel4j_std jLabel4;
	private JTextField4j jTextFieldWasteMaterial;
	private JTextField4j jTextFieldWasteReason;
	private JLabel4j_std jLabelWasteMaterial;
	private JLabel4j_std jLabelWasteReason;
	private JLabel4j_std jLabelUser;
	private JLabel4j_std jLabelHazard;
	private JTable4j jTable1;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSearch;
	private JScrollPane jScrollPane1;
	private static boolean dlg_sort_ascending = false;
	private JDateControl expiryFrom = new JDateControl();
	private JDateControl expiryTo = new JDateControl();
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private JCalendarButton calendarButtonDateFrom;
	private JCalendarButton calendarButtonDateTo;
	private JCheckBox4j jCheckBoxLimit = new JCheckBox4j();
	private JSpinner4j jSpinnerLimit = new JSpinner4j();
	private JDBWasteTransactionType blank = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
	private JDBWasteReportingGroup blankGroup = new JDBWasteReportingGroup(Common.selectedHostID, Common.sessionID);
	private Vector<JDBWasteTransactionType> transTypeList = new Vector<JDBWasteTransactionType>();
	private JDBWasteTransactionType transactionTypes = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<JDBWasteTypes> jComboBoxMaterialType = new JComboBox4j<JDBWasteTypes>();

	private JDBWasteTypes wasteTypes = new JDBWasteTypes(Common.selectedHostID, Common.sessionID);
	private JDBWasteReportingGroup reportingGroups = new JDBWasteReportingGroup(Common.selectedHostID, Common.sessionID);

	private Vector<JDBWasteTypes> typeList = new Vector<JDBWasteTypes>();
	private Vector<JDBWasteReportingGroup> groupList = new Vector<JDBWasteReportingGroup>();

	private ComboBoxModel<JDBWasteTypes> jComboBoxTypeModel;

	private DefaultComboBoxModel<String> sortFieldsFriendly;
	private LinkedList<String> sortFieldsSQL;
	private boolean emptyresult = true;
	private JDBQuery2 qSearch = new JDBQuery2(Common.selectedHostID, Common.sessionID);
	private JDBQuery2 qPrint = new JDBQuery2(Common.selectedHostID, Common.sessionID);
	private JDBQuery2 qExcel = new JDBQuery2(Common.selectedHostID, Common.sessionID);

	private void buildSortList()
	{
		sortFieldsFriendly = new DefaultComboBoxModel<String>();
		sortFieldsFriendly.addElement("Reporting Group");
		sortFieldsFriendly.addElement("Reporting ID");
		sortFieldsFriendly.addElement("Location ID");
		sortFieldsFriendly.addElement("Container ID");
		sortFieldsFriendly.addElement("Material ID");
		sortFieldsFriendly.addElement("Waste Type");
		sortFieldsFriendly.addElement("Process Order");
		sortFieldsFriendly.addElement("Reason");
		sortFieldsFriendly.addElement("Transaction Ref");
		sortFieldsFriendly.addElement("Date & Time");
		sortFieldsFriendly.addElement("Transaction Type");
		sortFieldsFriendly.addElement("Quantity");
		sortFieldsFriendly.addElement("Weight KG");
		sortFieldsFriendly.addElement("Cost");

		sortFieldsSQL = new LinkedList<String>();
		sortFieldsSQL.add("REPORTING_GROUP,WASTE_REPORTING_ID,WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("WASTE_REPORTING_ID,WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("WASTE_CONTAINER_ID,TRANSACTION_REF");
		sortFieldsSQL.add("WASTE_MATERIAL_ID,REPORTING_GROUP,WASTE_REPORTING_ID,WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("WASTE_TYPE_ID,REPORTING_GROUP,WASTE_REPORTING_ID,WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("PROCESS_ORDER,REPORTING_GROUP,WASTE_REPORTING_ID,WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("WASTE_REASON_ID,WASTE_MATERIAL_ID,REPORTING_GROUP,WASTE_REPORTING_ID,WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("TRANSACTION_REF,WASTE_TRANSACTION_TYPE");
		sortFieldsSQL.add("REPORT_TIME,TRANSACTION_REF,WASTE_TRANSACTION_TYPE");
		sortFieldsSQL.add("WASTE_TRANSACTION_TYPE,TRANSACTION_REF,WASTE_TRANSACTION_TYPE");
		sortFieldsSQL.add("QUANTITY,TRANSACTION_REF,WASTE_TRANSACTION_TYPE");
		sortFieldsSQL.add("WEIGHT_KG,TRANSACTION_REF,WASTE_TRANSACTION_TYPE");
		sortFieldsSQL.add("COST,TRANSACTION_REF,WASTE_TRANSACTION_TYPE");

		jComboBoxSortBy.setModel(sortFieldsFriendly);
		jComboBoxSortBy.setMaximumRowCount(sortFieldsSQL.size());

	}

	public JInternalFrameWasteReporting()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		buildSortList();

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldWasteMaterial.requestFocus();
				jTextFieldWasteMaterial.setCaretPosition(jTextFieldWasteMaterial.getText().length());
			}
		});

		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_WASTE_REPORTING"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_ascending);

	}

	private void clearFilter()
	{

		jTextFieldWasteMaterial.setText("");
		jTextFieldWasteLocation.setText("");
		jTextFieldWasteContainer.setText("");
		jTextFieldWasteReportingID.setText("");
		jTextFieldWasteReason.setText("");
		jTextFieldProcessOrder.setText("");
		jTextFieldUserID.setText("");
		jComboBoxTransactionType.setSelectedItem(blank);
		expiryTo.setEnabled(false);
		expiryFrom.setEnabled(false);
		jCheckBoxFrom.setSelected(false);
		jCheckBoxTo.setSelected(false);
		calendarButtonDateFrom.setEnabled(false);
		calendarButtonDateTo.setEnabled(false);
		jComboBoxSortBy.setSelectedIndex(0);
		jComboBoxMaterialType.setSelectedIndex(0);
		jComboBoxReportingGroup.setSelectedIndex(0);
		jComboBoxRecycle.setSelectedIndex(0);
		jComboBoxHazard.setSelectedIndex(0);

		search();

	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals(lang.get("lbl_Material")) == true)
			{
				jTextFieldWasteMaterial.setText(jTable1.getValueAt(row, JDBViewWasteReportingTableModel.Material_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Location_ID")) == true)
			{
				jTextFieldWasteLocation.setText(jTable1.getValueAt(row, JDBViewWasteReportingTableModel.Location_Col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Container_ID")) == true)
			{
				jTextFieldWasteContainer.setText(jTable1.getValueAt(row, JDBViewWasteReportingTableModel.Container_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Transaction_Type")) == true)
			{
				String temp = jTable1.getValueAt(row, JDBViewWasteReportingTableModel.Transaction_Type_Col).toString();
				for (int x = 0; x < transTypeList.size(); x++)
				{
					JDBWasteTransactionType xx = transTypeList.get(x);
					if (xx.getWasteTransactionType().equals(temp))
					{
						jComboBoxTransactionType.setSelectedIndex(x);
					}
				}

				jComboBoxTransactionType.setSelectedItem(jTable1.getValueAt(row, JDBViewWasteReportingTableModel.Transaction_Type_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Transaction_Date")) == true)
			{
				String dateString = jTable1.getValueAt(row, JDBViewWasteReportingTableModel.Report_Date_Col).toString();
				Date parsedDate;
				Calendar cal = Calendar.getInstance();
				try
				{
					parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);
					cal.setTime(parsedDate);
				}
				catch (ParseException e)
				{
					parsedDate = new java.util.Date();
					e.printStackTrace();
				}
				expiryTo.setEnabled(true);
				expiryFrom.setEnabled(true);
				jCheckBoxFrom.setSelected(true);
				jCheckBoxTo.setSelected(true);
				calendarButtonDateFrom.setEnabled(true);
				calendarButtonDateTo.setEnabled(true);
				cal.set(Calendar.SECOND, 0);
				expiryFrom.setDate(cal.getTime());
				cal.set(Calendar.SECOND, 59);
				expiryTo.setDate(cal.getTime());
			}

			search();

		}
	}

	private void search()
	{

		populateList();
	}

	private void excel()
	{
		JDBViewWasteReporting materialBatch = new JDBViewWasteReporting(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("waste_reporting.xls", materialBatch.getWasteLogResultSet(buildSQL(qExcel)), Common.mainForm);
	}

	
	private void copyToClipboard(String fieldname)
	{
		StringSelection stringSelection = new StringSelection("");
		
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals("Location") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 2).toString());
			}

			if (fieldname.equals("Container") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 3).toString());
			}
			
			if (fieldname.equals("Material") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 6).toString());
			}
			
			if (fieldname.equals("Reason") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 8).toString());
			}

			if (fieldname.equals("Process Order") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 14).toString());
			}

			if (fieldname.equals("Net Weight") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 11).toString());
			}
						
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		}
	}
	
	private void sortBy(String fieldname)
	{
		jComboBoxSortBy.setSelectedItem(fieldname);
		search();
	}

	private void setSequence(boolean descending)
	{
		if (jToggleButtonSequence.isSelected() == true)
		{
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending_16x16);
		}
		else
		{
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending_16x16);
		}
	}

	private PreparedStatement buildSQL(JDBQuery2 query)
	{

		query = new JDBQuery2(Common.selectedHostID, Common.sessionID);

		PreparedStatement result;

		query.applyFrom("{schema}view_waste_reporting vwr");
		query.applyWhat(JUtility.substSchemaName(schemaName, "vwr.*,(vwr.WEIGHT_KG - vwr.TARE_WEIGHT) AS NET_WEIGHT,((vwr.WEIGHT_KG - vwr.TARE_WEIGHT) * vwr.COST_PER_KG) AS COST"));

		if (emptyresult)
		{
			query.applyWhere("1 = ", 2);
			emptyresult = false;
		}

		query.applyWhere("waste_material_id=", jTextFieldWasteMaterial.getText());

		query.applyWhere("waste_reason_id=", jTextFieldWasteReason.getText());
		query.applyWhere("waste_location_id=", jTextFieldWasteLocation.getText());
		query.applyWhere("waste_container_id=", jTextFieldWasteContainer.getText());
		query.applyWhere("process_order=", jTextFieldProcessOrder.getText());
		query.applyWhere("user_id=", jTextFieldUserID.getText());
		query.applyWhere("waste_reporting_id=", jTextFieldWasteReportingID.getText());

		JDBWasteTypes y = ((JDBWasteTypes) jComboBoxMaterialType.getSelectedItem());

		if (JUtility.replaceNullStringwithBlank(y.getWasteTypeID()).equals("") == false)
		{
			query.applyWhere("waste_type_id=", y.getWasteTypeID());
		}

		JDBWasteReportingGroup y2 = ((JDBWasteReportingGroup) jComboBoxReportingGroup.getSelectedItem());

		if ((y2.getWasteReportGroup() != -1))
		{
			query.applyWhere("reporting_group=", y2.getWasteReportGroup());
		}

		JDBWasteTransactionType x = ((JDBWasteTransactionType) jComboBoxTransactionType.getSelectedItem());

		if (JUtility.replaceNullStringwithBlank(x.getWasteTransactionType()).equals("") == false)
		{
			query.applyWhere("waste_transaction_type=", x.getWasteTransactionType());
		}

		if (jComboBoxRecycle.getSelectedIndex() > 0)
		{
			if (jComboBoxRecycle.getSelectedIndex() == 1)
			{
				query.applyWhere("recyclable=", "Y");
			}
			else
			{
				query.applyWhere("recyclable=", "N");
			}
		}

		if (jCheckBoxFrom.isSelected())
		{
			query.applyWhere("report_time>=", JUtility.getTimestampFromDate(expiryFrom.getDate()));

		}

		if (jCheckBoxTo.isSelected())
		{
			query.applyWhere("report_time<=", JUtility.getTimestampFromDate(expiryTo.getDate()));
		}

		query.applySort(sortFieldsSQL.get(jComboBoxSortBy.getSelectedIndex()), jToggleButtonSequence.isSelected());
		query.applyRestriction(jCheckBoxLimit.isSelected(), jSpinnerLimit.getValue());

		query.applySQL();
		result = query.getPreparedStatement();

		return result;
	}

	private void populateList()
	{
		JDBViewWasteReporting wasteLog = new JDBViewWasteReporting(Common.selectedHostID, Common.sessionID);
		JDBViewWasteReportingTableModel wasteLogTable = new JDBViewWasteReportingTableModel(wasteLog.getWasteLogResultSet(buildSQL(qSearch)));
		TableRowSorter<JDBViewWasteReportingTableModel> sorter = new TableRowSorter<JDBViewWasteReportingTableModel>(wasteLogTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(wasteLogTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Reporting_ID_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Reporting_Group_Col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Transaction_Ref_Col).setPreferredWidth(85);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Report_Date_Col).setPreferredWidth(122);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Location_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Transaction_Type_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Material_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Material_Type_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Process_Order_Col).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Transaction_Ref_Col).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.WeightKG_Col).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Cost_Per_Kg_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Reason_Col).setPreferredWidth(140);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Process_Order_Col).setPreferredWidth(85);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.WeightKG_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewWasteReportingTableModel.Cost_Col).setPreferredWidth(80);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), wasteLogTable.getRowCount());
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 994, 666);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Waste Log");

			typeList.add(new JDBWasteTypes(Common.selectedHostID, Common.sessionID));
			typeList.addAll(wasteTypes.getWasteTypesList());

			blank.setDisplayMode(JDBWasteTransactionType.displayModeShort);

			transTypeList.add(blank);
			transTypeList.addAll(transactionTypes.getWasteTransactionTypesList(true,JDBWasteTransactionType.displayModeShort));

			blankGroup.setWasteReportingGroup(-1);
			blankGroup.setDescription("None");
			groupList.add(blankGroup);
			groupList.addAll(reportingGroups.getWasteGroupList());

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(483, 266));
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.setLayout(null);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 229, 988, 386);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][]
						{
								{ "One", "Two" },
								{ "Three", "Four" } }, new String[]
						{ "Column 1", "Column 2" });
						jTable1 = new JTable4j();
						jTable1.setToolTipText(lang.get("lbl_Table_Hint"));
						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jTable1, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_search_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										search();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Search"));
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
								final JMenu4j sortByMenu = new JMenu4j();
								sortByMenu.setText(lang.get("lbl_Sort_By"));
								popupMenu.add(sortByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("Material ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("Location ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Location_ID"));
									sortByMenu.add(newItemMenuItem);
								}
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("Container ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Container_ID"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("Transaction Type");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Type"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("Date & Time");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Date"));
									sortByMenu.add(newItemMenuItem);
								}
							}

							{
								final JMenu4j filterByMenu = new JMenu4j();
								filterByMenu.setText(lang.get("lbl_Filter_By"));
								popupMenu.add(filterByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Location_ID"));
									filterByMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Container_ID"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Type"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Date"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									filterByMenu.addSeparator();
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											clearFilter();
										}
									});
									newItemMenuItem.setText(lang.get("btn_Clear_Filter"));
									filterByMenu.add(newItemMenuItem);
								}
							}
							
							{
								final JMenu4j clipboardByMenu = new JMenu4j();
								clipboardByMenu.setText(lang.get("lbl_Clipboard_Copy"));
								popupMenu.add(clipboardByMenu);
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Location");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Location_ID"));
									clipboardByMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Container");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Container_ID"));
									clipboardByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Material");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material"));
									clipboardByMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Reason");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Reason"));
									clipboardByMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Process Order");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Process_Order"));
									clipboardByMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Net Weight");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Net_Weight"));
									clipboardByMenu.add(newItemMenuItem);
								}

							}

							
						}
					}
				}
				{
					jButtonSearch = new JButton4j(Common.icon_search_16x16);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonSearch.setBounds(56, 188, 140, 32);
					jButtonSearch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							search();

						}
					});
				}

				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
					jButtonHelp.setBounds(616, 188, 140, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setBounds(756, 188, 140, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							qSearch = null;
							qPrint = null;
							qExcel = null;
							dispose();
						}
					});
				}
				{
					jLabelWasteMaterial = new JLabel4j_std();
					jDesktopPane1.add(jLabelWasteMaterial);
					jLabelWasteMaterial.setText(lang.get("lbl_Material"));
					jLabelWasteMaterial.setBounds(5, 48, 113, 22);
					jLabelWasteMaterial.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelWasteReason = new JLabel4j_std();
					jDesktopPane1.add(jLabelWasteReason);
					jLabelWasteReason.setText(lang.get("lbl_Reason"));
					jLabelWasteReason.setBounds(5, 118, 113, 22);
					jLabelWasteReason.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldWasteMaterial = new JTextField4j(JDBViewWasteReporting.field_MaterialID);
					jDesktopPane1.add(jTextFieldWasteMaterial);
					jTextFieldWasteMaterial.setBounds(134, 48, 125, 22);
				}
				{
					jTextFieldWasteReason = new JTextField4j(JDBViewWasteReporting.field_ReasonID);
					jDesktopPane1.add(jTextFieldWasteReason);
					jTextFieldWasteReason.setBounds(134, 118, 125, 22);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Location_ID"));
					jLabel3.setBounds(285, 48, 108, 22);
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel3a = new JLabel4j_std();
					jDesktopPane1.add(jLabel3a);
					jLabel3a.setText(lang.get("lbl_Container_ID"));
					jLabel3a.setBounds(285, 83, 108, 22);
					jLabel3a.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel4 = new JLabel4j_std();
					jDesktopPane1.add(jLabel4);
					jLabel4.setText(lang.get("lbl_Report_ID"));
					jLabel4.setBounds(285, 118, 108, 22);
					jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldWasteLocation = new JTextField4j(JDBViewWasteReporting.field_LocationID);
					jDesktopPane1.add(jTextFieldWasteLocation);
					jTextFieldWasteLocation.setBounds(401, 48, 125, 22);
				}
				{
					jTextFieldWasteContainer = new JTextField4j(JDBWasteLog.field_ContainerID);
					jDesktopPane1.add(jTextFieldWasteContainer);
					jTextFieldWasteContainer.setBounds(401, 83, 125, 22);
				}
				{
					jTextFieldWasteReportingID = new JTextField4j(JDBWasteReportingIDS.field_WasteReportingID);
					jDesktopPane1.add(jTextFieldWasteReportingID);
					jTextFieldWasteReportingID.setBounds(401, 118, 125, 22);
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Sort_By"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(280, 152, 113, 22);
				}
				{

					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setBounds(401, 152, 255, 22);

				}
				{
					jLabelTransaction_Type = new JLabel4j_std();
					jDesktopPane1.add(jLabelTransaction_Type);
					jLabelTransaction_Type.setText(lang.get("lbl_Transaction_Type"));
					jLabelTransaction_Type.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelTransaction_Type.setBounds(674, 83, 146, 22);
				}
				{
					ComboBoxModel<JDBWasteTransactionType> jComboBoxTransTypeModel = new DefaultComboBoxModel<JDBWasteTransactionType>(transTypeList);
					jComboBoxTransactionType = new JComboBox4j<JDBWasteTransactionType>();
					jDesktopPane1.add(jComboBoxTransactionType);
					jComboBoxTransactionType.setModel(jComboBoxTransTypeModel);
					jComboBoxTransactionType.setBounds(826, 83, 146, 22);
					jComboBoxTransactionType.setMaximumRowCount(transTypeList.size());
				}
				{
					ComboBoxModel<JDBWasteReportingGroup> jComboBoxReportingGroupModel = new DefaultComboBoxModel<JDBWasteReportingGroup>(groupList);
					jComboBoxReportingGroup = new JComboBox4j<JDBWasteReportingGroup>();
					jDesktopPane1.add(jComboBoxReportingGroup);
					jComboBoxReportingGroup.setModel(jComboBoxReportingGroupModel);
					jComboBoxReportingGroup.setBounds(687, 118, 285, 22);
					jComboBoxReportingGroup.setMaximumRowCount(groupList.size());
				}

				{
					jToggleButtonSequence = new JToggleButton4j();
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(654, 152, 21, 22);
					jToggleButtonSequence.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}

				{
					jButtonLookupWasteMaterial = new JButton4j(Common.icon_lookup_16x16);
					jButtonLookupWasteMaterial.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.waste_materials_for_location())
							{
								jTextFieldWasteMaterial.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					jButtonLookupWasteMaterial.setBounds(259, 48, 21, 22);
					jDesktopPane1.add(jButtonLookupWasteMaterial);
				}

				{
					jButtonLookupWasteReason = new JButton4j(Common.icon_lookup_16x16);
					jButtonLookupWasteReason.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.waste_reasons())
							{
								jTextFieldWasteReason.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					jButtonLookupWasteReason.setBounds(259, 118, 21, 22);
					jDesktopPane1.add(jButtonLookupWasteReason);
				}

				{
					jButtonLookupWasteLocation = new JButton4j(Common.icon_lookup_16x16);
					jButtonLookupWasteLocation.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgCriteriaDefault = "";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.waste_locations())
							{
								jTextFieldWasteLocation.setText(JLaunchLookup.dlgResult);
							}

						}
					});
					jButtonLookupWasteLocation.setBounds(526, 48, 21, 22);
					jDesktopPane1.add(jButtonLookupWasteLocation);
				}
				
				{
					jButtonLookupWasteContainer = new JButton4j(Common.icon_lookup_16x16);
					jButtonLookupWasteContainer.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgCriteriaDefault = "";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.waste_containers())
							{
								jTextFieldWasteContainer.setText(JLaunchLookup.dlgResult);
							}

						}
					});
					jButtonLookupWasteContainer.setBounds(526, 83, 21, 22);
					jDesktopPane1.add(jButtonLookupWasteContainer);
				}

				{
					jButtonLookupWasteReportingID = new JButton4j(Common.icon_lookup_16x16);
					jButtonLookupWasteReportingID.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgCriteriaDefault = "";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.waste_report_ids())
							{
								jTextFieldWasteReportingID.setText(JLaunchLookup.dlgResult);
							}

						}
					});
					jButtonLookupWasteReportingID.setBounds(526, 118, 21, 22);
					jDesktopPane1.add(jButtonLookupWasteReportingID);
				}

				{
					expiryFrom.setBounds(157, 13, 125, 22);
					expiryFrom.setEnabled(false);
					jDesktopPane1.add(expiryFrom);
				}

				{
					expiryTo.setBounds(346, 13, 128, 22);
					expiryTo.setEnabled(false);
					jDesktopPane1.add(expiryTo);
				}

				{
					jLabel5_1 = new JLabel4j_std();
					jLabel5_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5_1.setText(lang.get("lbl_Transaction_Date"));
					jLabel5_1.setBounds(0, 13, 126, 22);
					jDesktopPane1.add(jLabel5_1);
				}

				{
					jCheckBoxFrom = new JCheckBox4j();
					jCheckBoxFrom.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							if (jCheckBoxFrom.isSelected())
							{
								expiryFrom.setEnabled(true);
								calendarButtonDateFrom.setEnabled(true);
							}
							else
							{
								expiryFrom.setEnabled(false);
								calendarButtonDateFrom.setEnabled(false);
							}
						}
					});
					jCheckBoxFrom.setBackground(new Color(255, 255, 255));
					jCheckBoxFrom.setBounds(134, 13, 21, 22);
					jDesktopPane1.add(jCheckBoxFrom);
				}

				{
					jCheckBoxTo = new JCheckBox4j();
					jCheckBoxTo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							if (jCheckBoxTo.isSelected())
							{
								expiryTo.setEnabled(true);
								calendarButtonDateTo.setEnabled(true);
							}
							else
							{
								expiryTo.setEnabled(false);
								calendarButtonDateTo.setEnabled(false);
							}
						}
					});
					jCheckBoxTo.setBackground(new Color(255, 255, 255));
					jCheckBoxTo.setBounds(319, 13, 21, 22);
					jDesktopPane1.add(jCheckBoxTo);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 615, 988, 21);
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusText);
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							excel();
						}
					});

					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(336, 188, 140, 32);
					jDesktopPane1.add(jButtonExcel);
				}

				{
					calendarButtonDateFrom = new JCalendarButton(expiryFrom);
					calendarButtonDateFrom.setEnabled(false);
					calendarButtonDateFrom.setBounds(290, 10, 21, 25);
					jDesktopPane1.add(calendarButtonDateFrom);
				}
				{
					calendarButtonDateTo = new JCalendarButton(expiryTo);
					calendarButtonDateTo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
						}
					});
					calendarButtonDateTo.setEnabled(false);
					calendarButtonDateTo.setBounds(477, 10, 21, 25);
					jDesktopPane1.add(calendarButtonDateTo);
				}

				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Limit"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(721, 152, 147, 22);
				jDesktopPane1.add(label4j_std);

				jCheckBoxLimit = new JCheckBox4j();
				jCheckBoxLimit.setSelected(true);
				jCheckBoxLimit.setBackground(Color.WHITE);
				jCheckBoxLimit.setBounds(872, 152, 21, 22);
				jDesktopPane1.add(jCheckBoxLimit);

				JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(jSpinnerLimit);
				jSpinnerLimit.setEditor(ne);
				jSpinnerLimit.setBounds(901, 152, 68, 22);
				jSpinnerLimit.setValue(1000);
				jDesktopPane1.add(jSpinnerLimit);

				{
					jButtonClear = new JButton4j(Common.icon_clear_16x16);
					jButtonClear.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							clearFilter();

						}
					});
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jButtonClear.setBounds(196, 188, 140, 32);
					jDesktopPane1.add(jButtonClear);
				}

				{
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setBounds(476, 188, 140, 32);
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							print();

						}
					});
				}
				{
					jTextFieldProcessOrder = new JTextField4j(JDBViewWasteReporting.field_ProcessOrder);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setBounds(134, 83, 125, 22);
				}
				{
					jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookupProcessOrder);
					jButtonLookupProcessOrder.setBounds(259, 83, 21, 22);
					jButtonLookupProcessOrder.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgCriteriaDefault = "Ready";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.processOrders())
							{
								jTextFieldProcessOrder.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jLabelProcessOrder = new JLabel4j_std();
					jDesktopPane1.add(jLabelProcessOrder);
					jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
					jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelProcessOrder.setBounds(-27, 83, 146, 22);
				}

				{
					jTextFieldUserID = new JTextField4j(JDBUser.field_user_id);
					jDesktopPane1.add(jTextFieldUserID);
					jTextFieldUserID.setBounds(134, 152, 125, 22);
				}
				{
					jButtonLookupUserID = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookupUserID);
					jButtonLookupUserID.setBounds(259, 152, 21, 22);
					jButtonLookupUserID.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgCriteriaDefault = "";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.users())
							{
								jTextFieldUserID.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jLabelUser = new JLabel4j_std();
					jDesktopPane1.add(jLabelUser);
					jLabelUser.setText(lang.get("lbl_User_ID"));
					jLabelUser.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelUser.setBounds(16, 152, 108, 22);
				}

				{

					jComboBoxTypeModel = new DefaultComboBoxModel<JDBWasteTypes>(typeList);
					jComboBoxMaterialType.setModel(jComboBoxTypeModel);
					jComboBoxMaterialType.setBounds(687, 48, 285, 22);
					jDesktopPane1.add(jComboBoxMaterialType);
				}
				{
					jLabelRecycle = new JLabel4j_std();
					jDesktopPane1.add(jLabelRecycle);
					jLabelRecycle.setText(lang.get("lbl_Recycle"));
					jLabelRecycle.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelRecycle.setBounds(504, 13, 96, 22);
				}
				{
					String[] test =
					{ "", lang.get("web_Yes"), lang.get("web_No") };
					ComboBoxModel<String> jComboBoxRecycleModel = new DefaultComboBoxModel<String>(test);
					jComboBoxRecycle = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxRecycle);
					jComboBoxRecycle.setModel(jComboBoxRecycleModel);
					jComboBoxRecycle.setBounds(609, 13, 108, 22);
					jComboBoxRecycle.setMaximumRowCount(test.length);
				}

				{
					String[] test =
					{ "", lang.get("web_Yes"), lang.get("web_No") };
					ComboBoxModel<String> jComboBoxRecycleModel = new DefaultComboBoxModel<String>(test);
					jComboBoxHazard = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxHazard);
					jComboBoxHazard.setModel(jComboBoxRecycleModel);
					jComboBoxHazard.setBounds(864, 13, 108, 22);
					jComboBoxHazard.setMaximumRowCount(test.length);
				}

				{
					jLabelHazard = new JLabel4j_std();
					jDesktopPane1.add(jLabelHazard);
					jLabelHazard.setText(lang.get("lbl_Hazard"));
					jLabelHazard.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelHazard.setBounds(735, 13, 122, 22);
				}

				JLabel4j_std jLabel_WasteType = new JLabel4j_std();
				jLabel_WasteType.setText(lang.get("lbl_Material_Type"));
				jLabel_WasteType.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_WasteType.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_WasteType.setBounds(545, 48, 130, 22);
				jDesktopPane1.add(jLabel_WasteType);

				JLabel4j_std jLabel_WastereportingGroup = new JLabel4j_std();
				jLabel_WastereportingGroup.setText(lang.get("lbl_Reporting_Group"));
				jLabel_WastereportingGroup.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_WastereportingGroup.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_WastereportingGroup.setBounds(548, 118, 130, 22);
				jDesktopPane1.add(jLabel_WastereportingGroup);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void print()
	{

		JPopupMenu popup = new JPopupMenu("Reports");

		JMenuItem item1 = new JMenuItem("Summary Report 1 (By Reporting Group)");
		item1.addActionListener(new ActionListener()
		{
			public void actionPerformed(final ActionEvent e)
			{
				jComboBoxSortBy.setSelectedIndex(0);

				JLaunchReport.runReport("RPT_WASTE_REPORTING1", null, "", buildSQL(qPrint), "");
			}
		});
		item1.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_WASTE_REPORTING1"));

		JMenuItem item2 = new JMenuItem("Summary Report 2 (By Material)");
		item2.addActionListener(new ActionListener()
		{
			public void actionPerformed(final ActionEvent e)
			{
				jComboBoxSortBy.setSelectedIndex(4);

				JLaunchReport.runReport("RPT_WASTE_REPORTING2", null, "", buildSQL(qPrint), "");
			}
		});
		item2.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_WASTE_REPORTING2"));
		
		JMenuItem item3 = new JMenuItem("Summary Report 3 (By Reason)");
		item3.addActionListener(new ActionListener()
		{
			public void actionPerformed(final ActionEvent e)
			{
				jComboBoxSortBy.setSelectedIndex(7);
				
				JLaunchReport.runReport("RPT_WASTE_REPORTING3", null, "", buildSQL(qPrint), "");
			}
		});
		item3.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_WASTE_REPORTING3"));

		popup.add(item1);
		popup.add(item2);
		popup.add(item3);

		// show on the button?
		popup.show((Component) jScrollPane1, 490, 0);
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
