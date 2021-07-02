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
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBUser;
import com.commander4j.db.JDBWasteLog;
import com.commander4j.db.JDBWasteTransactionType;
import com.commander4j.db.JDBWasteTypes;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBViewWasteLogTableModel;
import com.commander4j.util.JDateControl;
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
public class JInternalFrameWasteLogAdmin extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JButton4j jButtonPrint;
	private JButton4j jButtonExcel;
	private JLabel4j_std jStatusText;
	private JLabel4j_std jLabelProcessOrder;
	private JButton4j jButtonAdd;
	private JCheckBox4j jCheckBoxTo;
	private JCheckBox4j jCheckBoxFrom;
	private JLabel4j_std jLabel5_1;
	private JButton4j jButtonLookupWasteLocation;
	private JButton4j jButtonLookupWasteContainer;
	private JButton4j jButtonLookupWasteMaterial;
	private JButton4j jButtonLookupWasteReason;
	private JButton4j jButtonLookupUserID;
	private JTextField4j jTextFieldProcessOrder;
	private JTextField4j jTextFieldUserID;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JToggleButton jToggleButtonSequence;
	private JButton4j jButtonLookupProcessOrder;
	private JTextField4j jTextFieldWasteLocation;
	private JTextField4j jTextFieldWasteContainer;
	private JComboBox4j<JDBWasteTransactionType> jComboBoxTransactionType;
	private JComboBox4j<String> jComboBoxRecycle;
	private JComboBox4j<String> jComboBoxHazard;
	private JLabel4j_std jLabelTransaction_Type;
	private JLabel4j_std jLabelRecycle;
	private JLabel4j_std jLabelHazard;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel3a;
	private JTextField4j jTextFieldWasteMaterial;
	private JTextField4j jTextFieldWasteReason;
	private JLabel4j_std jLabelWasteMaterial;
	private JLabel4j_std jLabelWasteReason;
	private JLabel4j_std jLabelUser;
	private JTable4j jTable1;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSearch;
	private JScrollPane jScrollPane1;
	private String lref;
	private String ltransaction;
	private static boolean dlg_sort_descending = true;
	private JDateControl expiryFrom = new JDateControl();
	private JDateControl expiryTo = new JDateControl();
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private JCalendarButton calendarButtonDateFrom;
	private JCalendarButton calendarButtonDateTo;
	private JCheckBox4j jCheckBoxLimit = new JCheckBox4j();
	private JSpinner jSpinnerLimit = new JSpinner();
	private JDBWasteTransactionType blank = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
	private Vector<JDBWasteTransactionType> transTypeList = new Vector<JDBWasteTransactionType>();
	private JDBWasteTransactionType transactionTypes = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<JDBWasteTypes> jComboBoxMaterialType = new JComboBox4j<JDBWasteTypes>();
	private JDBWasteTypes wasteTypes = new JDBWasteTypes(Common.selectedHostID, Common.sessionID);
	private Vector<JDBWasteTypes> typeList = new Vector<JDBWasteTypes>();
	private ComboBoxModel<JDBWasteTypes> jComboBoxTypeModel;
	private DefaultComboBoxModel<String> sortFieldsFriendly;
	private LinkedList<String> sortFieldsSQL;
	private boolean emptyresult = true;
	private JDBQuery qSearch = new JDBQuery(Common.selectedHostID, Common.sessionID);
	private JDBQuery qPrint = new JDBQuery(Common.selectedHostID, Common.sessionID);
	private JDBQuery qExcel = new JDBQuery(Common.selectedHostID, Common.sessionID);

	private void buildSortList()
	{
		sortFieldsFriendly = new DefaultComboBoxModel<String>();

		sortFieldsFriendly.addElement("Transaction Ref");

		sortFieldsFriendly.addElement("Location ID");
		sortFieldsFriendly.addElement("Container ID");
		sortFieldsFriendly.addElement("Material ID");
		sortFieldsFriendly.addElement("Waste Type");
		sortFieldsFriendly.addElement("Process Order");
		sortFieldsFriendly.addElement("Reason");

		sortFieldsFriendly.addElement("Date & Time");
		sortFieldsFriendly.addElement("Transaction Type");
		sortFieldsFriendly.addElement("Quantity");
		sortFieldsFriendly.addElement("Weight KG");
		sortFieldsFriendly.addElement("Cost");

		sortFieldsSQL = new LinkedList<String>();

		sortFieldsSQL.add("TRANSACTION_REF,WASTE_TRANSACTION_TYPE");
		
		sortFieldsSQL.add("WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("WASTE_CONTAINER_ID,TRANSACTION_REF");
		sortFieldsSQL.add("WASTE_MATERIAL_ID,WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("WASTE_TYPE_ID,WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("PROCESS_ORDER,WASTE_LOCATION_ID,TRANSACTION_REF");
		sortFieldsSQL.add("WASTE_REASON_ID,WASTE_LOCATION_ID,TRANSACTION_REF");

		sortFieldsSQL.add("REPORT_TIME,TRANSACTION_REF,WASTE_TRANSACTION_TYPE");
		sortFieldsSQL.add("WASTE_TRANSACTION_TYPE,TRANSACTION_REF,WASTE_TRANSACTION_TYPE");
		sortFieldsSQL.add("WEIGHT_KG,TRANSACTION_REF,WASTE_TRANSACTION_TYPE");
		sortFieldsSQL.add("WEIGHT_KG,TRANSACTION_REF,WASTE_TRANSACTION_TYPE");
		sortFieldsSQL.add("COST,TRANSACTION_REF,WASTE_TRANSACTION_TYPE");

		jComboBoxSortBy.setModel(sortFieldsFriendly);
		jComboBoxSortBy.setMaximumRowCount(sortFieldsSQL.size());

	}

	public JInternalFrameWasteLogAdmin()
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
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_WASTE_LOG"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);
	}

	private void clearFilter()
	{

		jTextFieldWasteMaterial.setText("");

		jTextFieldWasteLocation.setText("");
		
		jTextFieldWasteContainer.setText("");
		
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

		jComboBoxMaterialType.setSelectedIndex(0);
		jComboBoxRecycle.setSelectedIndex(0);

		search();

	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals(lang.get("lbl_Material")) == true)
			{
				jTextFieldWasteMaterial.setText(jTable1.getValueAt(row, JDBViewWasteLogTableModel.Material_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Location_ID")) == true)
			{
				jTextFieldWasteLocation.setText(jTable1.getValueAt(row, JDBViewWasteLogTableModel.Location_Col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Container_ID")) == true)
			{
				jTextFieldWasteContainer.setText(jTable1.getValueAt(row, JDBViewWasteLogTableModel.Container_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Transaction_Type")) == true)
			{
				String temp = jTable1.getValueAt(row, JDBViewWasteLogTableModel.Transaction_Type_Col).toString();
				for (int x = 0; x < transTypeList.size(); x++)
				{
					JDBWasteTransactionType xx = transTypeList.get(x);
					if (xx.getWasteTransactionType().equals(temp))
					{
						jComboBoxTransactionType.setSelectedIndex(x);
					}
				}

				jComboBoxTransactionType.setSelectedItem(jTable1.getValueAt(row, JDBViewWasteLogTableModel.Transaction_Type_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Transaction_Date")) == true)
			{
				String dateString = jTable1.getValueAt(row, JDBViewWasteLogTableModel.Transaction_Date_Col).toString();
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
				// cal.set(Calendar.MILLISECOND, 0);
				cal.set(Calendar.SECOND, 0);
				// cal.set(Calendar.MINUTE, 0);
				// cal.set(Calendar.HOUR_OF_DAY, 0);
				expiryFrom.setDate(cal.getTime());
				// cal.set(Calendar.MILLISECOND, 0);
				cal.set(Calendar.SECOND, 59);
				// cal.set(Calendar.MINUTE, 59);
				// cal.set(Calendar.HOUR_OF_DAY, 23);
				expiryTo.setDate(cal.getTime());
			}

			search();

		}
	}

	public JInternalFrameWasteLogAdmin(String material)
	{
		this();
		lref = material;
		jTextFieldWasteMaterial.setText(lref);
		jTextFieldWasteLocation.setText(ltransaction);

		populateList();
	}

	private void search()
	{
		populateList();
	}

	private void excel()
	{
		JDBWasteLog materialBatch = new JDBWasteLog(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();

		export.saveAs("waste_log.xls", materialBatch.getWasteLogResultSet(buildSQL(qExcel)), Common.mainForm);
		populateList();
	}

	private void addRecord()
	{
		JDBWasteTransactionType u = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
		LinkedList<JDBWasteTransactionType> transList = u.getWasteTransactionTypesList(true,JDBWasteTransactionType.displayModeShort);
		String[] transactionList = new String[transList.size()];

		if (transactionList.length > 0)
		{
			for (int x = 0; x < transList.size(); x++)
			{
				transactionList[x] = transList.get(x).getWasteTransactionType();
			}

			String transType = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_TransactionType_Select"), lang.get("btn_Select"), JOptionPane.PLAIN_MESSAGE, Common.icon_confirm_16x16, transactionList, transactionList[0]);

			if (transType != null)
			{

				JLaunchMenu.runForm("FRM_ADMIN_WASTE_LOG_EDIT", "-1", transType);

			}
		}
		else
		{
			JUtility.errorBeep();
			JOptionPane.showMessageDialog(Common.mainForm, "No Waste Transactions defined", lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
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

	public JInternalFrameWasteLogAdmin(String material, String batch)
	{
		this();
		lref = material;
		ltransaction = batch;
		jTextFieldWasteMaterial.setText(lref);
		jTextFieldWasteLocation.setText(ltransaction);

		populateList();
	}

	private PreparedStatement buildSQL(JDBQuery query)
	{

		PreparedStatement result;
		
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}view_waste_log"));
		
		
		if (emptyresult)
		{
			query.addParamtoSQL("1 = ", 2);
			emptyresult = false;
		}
		
		query.addParamtoSQL("waste_material_id=", jTextFieldWasteMaterial.getText());
		query.addParamtoSQL("waste_reason_id=", jTextFieldWasteReason.getText());
		query.addParamtoSQL("waste_location_id=", jTextFieldWasteLocation.getText());
		query.addParamtoSQL("waste_container_id=", jTextFieldWasteContainer.getText());
		query.addParamtoSQL("process_order=", jTextFieldProcessOrder.getText());
		query.addParamtoSQL("user_id=", jTextFieldUserID.getText());

		JDBWasteTypes y = ((JDBWasteTypes) jComboBoxMaterialType.getSelectedItem());

		if (JUtility.replaceNullStringwithBlank(y.getWasteTypeID()).equals("") == false)
		{
			query.addParamtoSQL("waste_type_id=", y.getWasteTypeID());
		}

		JDBWasteTransactionType x = ((JDBWasteTransactionType) jComboBoxTransactionType.getSelectedItem());

		if (JUtility.replaceNullStringwithBlank(x.getWasteTransactionType()).equals("") == false)
		{
			query.addParamtoSQL("waste_transaction_type=", x.getWasteTransactionType());
		}

		if (jComboBoxRecycle.getSelectedIndex() > 0)
		{
			if (jComboBoxRecycle.getSelectedIndex() == 1)
			{
				query.addParamtoSQL("recyclable=", "Y");
			}
			else
			{
				query.addParamtoSQL("recyclable=", "N");
			}
		}

		if (jComboBoxHazard.getSelectedIndex() > 0)
		{
			if (jComboBoxHazard.getSelectedIndex() == 1)
			{
				query.addParamtoSQL("hazardous=", "Y");
			}
			else
			{
				query.addParamtoSQL("hazardous=", "N");
			}
		}

		if (jCheckBoxFrom.isSelected())
		{
			query.addParamtoSQL("report_time>=", JUtility.getTimestampFromDate(expiryFrom.getDate()));

		}

		if (jCheckBoxTo.isSelected())
		{
			query.addParamtoSQL("report_time<=", JUtility.getTimestampFromDate(expiryTo.getDate()));
		}

		query.appendSort(sortFieldsSQL.get(jComboBoxSortBy.getSelectedIndex()), jToggleButtonSequence.isSelected());
		query.applyRestriction(jCheckBoxLimit.isSelected(), Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), jSpinnerLimit.getValue());

		query.bindParams();
		result = query.getPreparedStatement();
		return result;
	}

	private void populateList()
	{
		JDBWasteLog wasteLog = new JDBWasteLog(Common.selectedHostID, Common.sessionID);
		JDBViewWasteLogTableModel wasteLogTable = new JDBViewWasteLogTableModel(wasteLog.getWasteLogResultSet(buildSQL(qSearch)));
		TableRowSorter<JDBViewWasteLogTableModel> sorter = new TableRowSorter<JDBViewWasteLogTableModel>(wasteLogTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(wasteLogTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.Transaction_Date_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.Transaction_Type_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.Material_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.Material_Type_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.Process_Order_Col).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.Transaction_Ref_Col).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.Location_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.Container_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.Reason_Col).setPreferredWidth(150);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.User_id_Col).setPreferredWidth(110);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.WeightKG_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.TareWeight_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.NetWeight_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.CostPerKg_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewWasteLogTableModel.CostTotal_Col).setPreferredWidth(80);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), wasteLogTable.getRowCount());
	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lref = jTable1.getValueAt(row, JDBViewWasteLogTableModel.Transaction_Ref_Col).toString();
			ltransaction = jTable1.getValueAt(row, JDBViewWasteLogTableModel.Transaction_Type_Col).toString();
			JLaunchMenu.runForm("FRM_ADMIN_WASTE_LOG_EDIT", lref, ltransaction);
		}

	}

	private void deleteRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lref = jTable1.getValueAt(row, JDBViewWasteLogTableModel.Transaction_Ref_Col).toString();
			ltransaction = jTable1.getValueAt(row, JDBViewWasteLogTableModel.Transaction_Type_Col).toString();

			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Waste_Log_Delete"), lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBWasteLog materialBatch = new JDBWasteLog(Common.selectedHostID, Common.sessionID);
				materialBatch.setTransactionRef(Long.valueOf(lref));
				materialBatch.setTransactionType(ltransaction);
				materialBatch.delete();
				search();
			}
		}

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

						jTable1.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_WASTE_LOG_EDIT"))
									{
										editRecord();
									}
								}
							}
						});

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
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_WASTE_LOG_ADD"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										addRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_WASTE_LOG_DELETE"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										deleteRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Delete"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_WASTE_LOG_EDIT"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										editRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Edit"));
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
						}
					}
				}
				{
					jButtonSearch = new JButton4j(Common.icon_search_16x16);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonSearch.setBounds(0, 188, 123, 32);
					jButtonSearch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							search();

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit_16x16);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(java.awt.event.KeyEvent.VK_E);
					jButtonEdit.setBounds(372, 188, 123, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_WASTE_LOG_EDIT"));
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
					jButtonHelp.setBounds(738, 188, 123, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setBounds(861, 188, 123, 32);
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
					jLabelWasteMaterial.setBounds(0, 49, 126, 21);
					jLabelWasteMaterial.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelWasteReason = new JLabel4j_std();
					jDesktopPane1.add(jLabelWasteReason);
					jLabelWasteReason.setText(lang.get("lbl_Reason"));
					jLabelWasteReason.setBounds(-4, 119, 130, 21);
					jLabelWasteReason.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldWasteMaterial = new JTextField4j(JDBWasteLog.field_MaterialID);
					jDesktopPane1.add(jTextFieldWasteMaterial);
					jTextFieldWasteMaterial.setBounds(134, 48, 126, 22);
				}
				{
					jTextFieldWasteReason = new JTextField4j(JDBWasteLog.field_ReasonID);
					jDesktopPane1.add(jTextFieldWasteReason);
					jTextFieldWasteReason.setBounds(134, 118, 126, 22);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Location_ID"));
					jLabel3.setBounds(285, 49, 108, 21);
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel3a = new JLabel4j_std();
					jDesktopPane1.add(jLabel3a);
					jLabel3a.setText(lang.get("lbl_Container_ID"));
					jLabel3a.setBounds(285, 84, 108, 21);
					jLabel3a.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldWasteLocation = new JTextField4j(JDBWasteLog.field_LocationID);
					jDesktopPane1.add(jTextFieldWasteLocation);
					jTextFieldWasteLocation.setBounds(401, 48, 126, 22);
				}
				{
					jTextFieldWasteContainer = new JTextField4j(JDBWasteLog.field_ContainerID);
					jDesktopPane1.add(jTextFieldWasteContainer);
					jTextFieldWasteContainer.setBounds(401, 83, 126, 22);
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Sort_By"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(285, 153, 108, 23);
				}
				{
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setBounds(401, 153, 255, 23);
				}
				{
					jLabelTransaction_Type = new JLabel4j_std();
					jDesktopPane1.add(jLabelTransaction_Type);
					jLabelTransaction_Type.setText(lang.get("lbl_Transaction_Type"));
					jLabelTransaction_Type.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelTransaction_Type.setBounds(674, 84, 146, 21);
				}
				{
					jLabelRecycle = new JLabel4j_std();
					jDesktopPane1.add(jLabelRecycle);
					jLabelRecycle.setText(lang.get("lbl_Recycle"));
					jLabelRecycle.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelRecycle.setBounds(474, 17, 126, 21);
				}
				{
					jLabelHazard = new JLabel4j_std();
					jDesktopPane1.add(jLabelHazard);
					jLabelHazard.setText(lang.get("lbl_Hazard"));
					jLabelHazard.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelHazard.setBounds(735, 17, 122, 21);
				}
				{
					ComboBoxModel<JDBWasteTransactionType> jComboBoxStatusModel = new DefaultComboBoxModel<JDBWasteTransactionType>(transTypeList);
					jComboBoxTransactionType = new JComboBox4j<JDBWasteTransactionType>();
					jDesktopPane1.add(jComboBoxTransactionType);
					jComboBoxTransactionType.setModel(jComboBoxStatusModel);
					jComboBoxTransactionType.setBounds(826, 83, 146, 23);
					jComboBoxTransactionType.setMaximumRowCount(transTypeList.size());
				}
				{
					String[] test =
					{ "", lang.get("web_Yes"), lang.get("web_No") };
					ComboBoxModel<String> jComboBoxRecycleModel = new DefaultComboBoxModel<String>(test);
					jComboBoxRecycle = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxRecycle);
					jComboBoxRecycle.setModel(jComboBoxRecycleModel);
					jComboBoxRecycle.setBounds(609, 15, 108, 23);
					jComboBoxRecycle.setMaximumRowCount(test.length);
				}
				{
					String[] test =
					{ "", lang.get("web_Yes"), lang.get("web_No") };
					ComboBoxModel<String> jComboBoxRecycleModel = new DefaultComboBoxModel<String>(test);
					jComboBoxHazard = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxHazard);
					jComboBoxHazard.setModel(jComboBoxRecycleModel);
					jComboBoxHazard.setBounds(864, 15, 108, 23);
					jComboBoxHazard.setMaximumRowCount(test.length);
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jToggleButtonSequence.setSelected(true);
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(654, 153, 21, 23);
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
							if (JLaunchLookup.waste_materials_all())
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
					jButtonLookupWasteLocation.setBounds(528, 48, 21, 22);
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
					jButtonLookupWasteContainer.setBounds(528, 83, 21, 22);
					jDesktopPane1.add(jButtonLookupWasteContainer);
				}

				{
					expiryFrom.setBounds(155, 13, 125, 25);
					expiryFrom.setEnabled(false);
					jDesktopPane1.add(expiryFrom);
				}

				{
					expiryTo.setBounds(328, 13, 125, 25);
					expiryTo.setEnabled(false);
					jDesktopPane1.add(expiryTo);
				}

				{
					jLabel5_1 = new JLabel4j_std();
					jLabel5_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5_1.setText(lang.get("lbl_Transaction_Date"));
					jLabel5_1.setBounds(0, 15, 126, 21);
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
					jCheckBoxFrom.setBounds(134, 11, 21, 25);
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
					jCheckBoxTo.setBounds(308, 13, 21, 25);
					jDesktopPane1.add(jCheckBoxTo);
				}

				{
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_WASTE_LOG_ADD"));
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							addRecord();
						}
					});
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(246, 188, 126, 32);
					jDesktopPane1.add(jButtonAdd);
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
					jButtonExcel.setBounds(492, 188, 123, 32);
					jDesktopPane1.add(jButtonExcel);
				}

				{
					calendarButtonDateFrom = new JCalendarButton(expiryFrom);
					calendarButtonDateFrom.setEnabled(false);
					calendarButtonDateFrom.setBounds(286, 13, 21, 25);
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
					calendarButtonDateTo.setBounds(473, 13, 21, 25);
					jDesktopPane1.add(calendarButtonDateTo);
				}

				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Limit"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(721, 155, 147, 21);
				jDesktopPane1.add(label4j_std);

				jCheckBoxLimit = new JCheckBox4j();
				jCheckBoxLimit.setSelected(true);
				jCheckBoxLimit.setBackground(Color.WHITE);
				jCheckBoxLimit.setBounds(872, 155, 21, 21);
				jDesktopPane1.add(jCheckBoxLimit);

				JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
				ne.getTextField().setFont(Common.font_std);
				jSpinnerLimit.setEditor(ne);
				jSpinnerLimit.setBounds(901, 155, 68, 21);
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
					jButtonClear.setBounds(123, 188, 123, 32);
					jDesktopPane1.add(jButtonClear);
				}

				{
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setBounds(615, 188, 123, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_WASTE_LOG"));
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
					jTextFieldProcessOrder = new JTextField4j(JDBWasteLog.field_ProcessOrder);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setBounds(134, 83, 126, 22);
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
					jLabelProcessOrder.setBounds(0, 84, 126, 21);
				}

				{
					jTextFieldUserID = new JTextField4j(JDBUser.field_user_id);
					jDesktopPane1.add(jTextFieldUserID);
					jTextFieldUserID.setBounds(401, 118, 126, 22);
				}
				{
					jButtonLookupUserID = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookupUserID);
					jButtonLookupUserID.setBounds(528, 118, 21, 22);
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
					jLabelUser.setBounds(285, 119, 108, 21);
				}

				{

					jComboBoxTypeModel = new DefaultComboBoxModel<JDBWasteTypes>(typeList);
					jComboBoxMaterialType.setModel(jComboBoxTypeModel);
					jComboBoxMaterialType.setBounds(687, 48, 285, 22);
					jDesktopPane1.add(jComboBoxMaterialType);
				}

				JLabel4j_std jLabel_WasteType = new JLabel4j_std();
				jLabel_WasteType.setText(lang.get("lbl_Material_Type"));
				jLabel_WasteType.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_WasteType.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_WasteType.setBounds(545, 49, 130, 21);
				jDesktopPane1.add(jLabel_WasteType);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void print()
	{
		JLaunchReport.runReport("RPT_WASTE_LOG", null, "", buildSQL(qPrint), "");
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
