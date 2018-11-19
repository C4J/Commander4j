package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePalletHistoryAdmin.java
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


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBMaterialType;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBPalletHistory;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQuery2;
import com.commander4j.db.JDBUom;
import com.commander4j.db.JDBUser;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBPalletHistoryTableModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;
import com.commander4j.util.JWait;

/**
 * The JInternalFramePalletHistoryAdmin allows you to search the APP_PALLET_HISTORY table which
 * contains a multiple records for each SSCC. The pallet history table is in effect an audit trail
 * for all actions which effect a pallet. Each time a transaction is performed on a SSCC the system
 * generates a new transaction number which is written to the pallet history table. This transaction 
 * ref is optionally inserted into the SYS_INTERFACE_REQUEST table which will trigger the generation 
 * of an interface message.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFramePalletHistoryAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.app.JInternalFramePalletHistoryAdmin JInternalFramePalletHistoryAdmin
 * @see com.commander4j.db.JDBPalletHistory JDBPalletHistory
 * @see com.commander4j.interfaces.JInternalFrameInterfaceLog JInternalFrameInterfaceLog
 * @see com.commander4j.db.JDBInterfaceRequest JDBInterfaceRequest
 */
public class JInternalFramePalletHistoryAdmin extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JLabel4j_std jLabel5_1;
	private JTextField4j jTextFieldUser;
	private JLabel4j_std jStatusText;
	private JDateControl transactionDateTo;
	private JTextField4j jTextFieldTransaction_Ref;
	private JComboBox4j<String> comboBoxTransactionSubtype;
	private JComboBox4j<String> comboBoxTransactionType;
	private JLabel4j_std jLabel1_4;
	private JLabel4j_std jLabel1_3;
	private JLabel4j_std jLabel1_2;
	private JCheckBox4j jCheckBoxTransactionDate;
	private JDateControl transactionDateFrom;
	private JLabel4j_std jLabel1_1;
	private JTextField4j jTextFieldDespatch_No;
	private JLabel4j_std jLabel8_1;
	private static final long serialVersionUID = 1;
	private static boolean dlg_sort_descending = true;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonSearch1;
	private JTextField4j jTextFieldLocation;
	private JLabel4j_std jLabel4;
	private JButton4j jButtonLookupProcessOrder;
	private JCheckBox4j jCheckBoxLimit;
	private JSpinner jSpinnerLimit;
	private JLabel4j_std jLabel7;
	private JCheckBox4j jCheckBoxQuantity;
	private JTextField4j jTextFieldVariant;
	private JLabel4j_std jLabel6;
	private JLabel4j_std jLabel5;
	private JTextField4j jTextFieldEAN;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std jLabelProcessOrder;
	private JTextField4j jTextFieldBatch;
	private JLabel4j_std jLabel2;
	private JButton4j jButtonClose;
	private JLabel4j_std jLabelSCC;
	private JTextField4j jTextFieldSSCC;
	private JButton4j jButtonLookupBatch;
	private JButton4j jButtonLookupLocation;
	private JButton4j jButtonLookupMaterial;
	private JQuantityInput jFormattedTextFieldQuantity;
	private JLabel4j_std jLabelQuantity;
	private JToggleButton jToggleButtonSequence;
	private JLabel4j_std jLabel15;
	private JComboBox4j<String> jComboBoxPalletStatus;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JButton4j jButtonPrint;
	private JButton4j jButtonHelp;
	private JComboBox4j<JDBUom> jComboBoxUOM;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private JTextField4j jTextFieldMaterial;
	private JTable jTable1;
	private JScrollPane jScrollPane1;
	private JDBUom u = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBMaterialType t = new JDBMaterialType(Common.selectedHostID, Common.sessionID);
	private Vector<JDBUom> uomList = new Vector<JDBUom>();
	private Vector<JDBMaterialType> typeList = new Vector<JDBMaterialType>();
	private JMenuItem4j mntmEditProcessOrder;
	private JMenuItem4j mntmEditMaterial;
	private JMenuItem4j mntmEditBatch;
	private JMenuItem4j mntmEditLocation;
	private JMenuItem4j mntmEditPallet;
	private JMenu4j mnReferenceData;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private String expiryMode = "";
	private JCalendarButton calendarButtontransactionDateFrom;
	private JCalendarButton calendarButtontransactionDateTo;
	private PreparedStatement listStatement;
	private JTextField4j jTextFieldCustomer = new JTextField4j(20);

	private void print() {
		buildSQL();
		JLaunchReport.runReport("RPT_PALLET_HISTORY",null,"", listStatement,"");
	}

	private void print_summary() {
		jComboBoxSortBy.setSelectedItem("MATERIAL,PROCESS_ORDER");
		comboBoxTransactionType.setSelectedItem("PROD DEC");
		comboBoxTransactionSubtype.setSelectedItem("CONFIRM");
		JWait.milliSec(100);
		PreparedStatement temp = buildSQLr();
		JLaunchReport.runReport("RPT_HIST_SUMMARY",null,"", temp,"");
	}

	private void export() {
		JDBPalletHistory palletHistory = new JDBPalletHistory(Common.selectedHostID, Common.sessionID);

		JExcel export = new JExcel();
		export.setExcelRowLimit(jCheckBoxLimit, jSpinnerLimit);
		buildSQL();
		export.saveAs("pallet_history.xls", palletHistory.getPalletHistoryDataResultSet(listStatement), Common.mainForm);
		populateList();
	}

	private void filterBy(String fieldname) {
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			if (fieldname.equals("Transaction Ref") == true)
			{
				jTextFieldTransaction_Ref.setText(jTable1.getValueAt(row, 0).toString());
			}
			if (fieldname.equals("Transaction Type") == true)
			{
				comboBoxTransactionType.setSelectedItem(jTable1.getValueAt(row, 1).toString());
			}
			if (fieldname.equals("Transaction Subtype") == true)
			{
				comboBoxTransactionSubtype.setSelectedItem(jTable1.getValueAt(row, 2).toString());
			}
			if (fieldname.equals("SSCC") == true)
			{
				jTextFieldSSCC.setText(jTable1.getValueAt(row, 4).toString());
			}

			if (fieldname.equals("Material") == true)
			{
				jTextFieldMaterial.setText(jTable1.getValueAt(row, 5).toString());
			}

			if (fieldname.equals("Batch") == true)
			{
				jTextFieldBatch.setText(jTable1.getValueAt(row, 6).toString());
			}

			if (fieldname.equals("Process Order") == true)
			{
				jTextFieldProcessOrder.setText(jTable1.getValueAt(row, 7).toString());
			}
			if (fieldname.equals("Quantity") == true)
			{
				jFormattedTextFieldQuantity.setValue(new BigDecimal(jTable1.getValueAt(row, 10).toString()));
			}
			if (fieldname.equals("UOM") == true)
			{
				jComboBoxUOM.setSelectedItem(jTable1.getValueAt(row, 9).toString());
			}
			if (fieldname.equals("Despatch No") == true)
			{
				jTextFieldDespatch_No.setText(jTable1.getValueAt(row, 10).toString());
			}
			if (fieldname.equals("Status") == true)
			{
				jComboBoxPalletStatus.setSelectedItem(jTable1.getValueAt(row, 11).toString());
			}

			if (fieldname.equals("Location") == true)
			{
				jTextFieldLocation.setText(jTable1.getValueAt(row, 12).toString());
			}
			if (fieldname.equals("User") == true)
			{
				jTextFieldUser.setText(jTable1.getValueAt(row, 13).toString());
			}

			buildSQL();
			populateList();

		}
	}

	private void sortBy(String orderField) {
		jComboBoxSortBy.setSelectedItem(orderField);
		buildSQL();
		populateList();
	}
	
	private void app_Init()
	{
		setIconifiable(true);
		getContentPane().setLayout(null);

		uomList.add(new JDBUom(Common.selectedHostID, Common.sessionID));
		uomList.addAll(u.getInternalUoms());

		typeList.add(new JDBMaterialType(Common.selectedHostID, Common.sessionID));
		typeList.addAll(t.getMaterialTypes());

		expiryMode = ctrl.getKeyValue("EXPIRY DATE MODE");

		initGUI();

		////////
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID,Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("{schema}VIEW_PALLET_HISTORY_EXPIRY WHERE 1=2");
		q2.applyRestriction(false, 0);
		q2.applySort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		q2.applySQL();
		listStatement = q2.getPreparedStatement();
		////////
		
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_PALLET_HISTORY"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		
		calendarButtontransactionDateFrom = new JCalendarButton(transactionDateFrom);
		calendarButtontransactionDateFrom.setEnabled(false);
		calendarButtontransactionDateFrom.setBounds(282, 9, 21, 21);
		jDesktopPane1.add(calendarButtontransactionDateFrom);
		
		calendarButtontransactionDateTo = new JCalendarButton(transactionDateTo);
		calendarButtontransactionDateTo.setEnabled(false);
		calendarButtontransactionDateTo.setBounds(476, 9, 21, 21);
		jDesktopPane1.add(calendarButtontransactionDateTo);
		

		setSequence(dlg_sort_descending);
	}

	public JInternalFramePalletHistoryAdmin()
	{
		super();
		app_Init();

	}
	
	public JInternalFramePalletHistoryAdmin(String keyField,String keyValue)
	{
		super();
		app_Init();
		
		clearFilter();
		
		if (keyField.equals("SSCC"))
		{
			jTextFieldSSCC.setText(keyValue);
		}
		
		if (keyField.equals("PROCESS_ORDER"))
		{
			jTextFieldProcessOrder.setText(keyValue);
		}
		
		if (keyField.equals("MATERIAL"))
		{
			jTextFieldMaterial.setText(keyValue);
		}
		
		buildSQL();
		populateList();

	}

	private PreparedStatement buildSQLr() {
		
		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID,Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("{schema}VIEW_PALLET_HISTORY_EXPIRY");
		

		if (jCheckBoxTransactionDate.isSelected())
		{
			q2.applyWhere("transaction_date>=", JUtility.getTimestampFromDate(transactionDateFrom.getDate()));
			q2.applyWhere("transaction_date<=", JUtility.getTimestampFromDate(transactionDateTo.getDate()));
		}


		if (jTextFieldTransaction_Ref.getText().equals("") == false)
		{
			q2.applyWhere("transaction_ref = ", jTextFieldTransaction_Ref.getText());
		}

		if (jTextFieldSSCC.getText().equals("") == false)
		{
			q2.applyWhere("sscc = ", jTextFieldSSCC.getText());
		}

		if (jTextFieldMaterial.getText().equals("") == false)
		{
			q2.applyWhere("material = ", jTextFieldMaterial.getText());
		}

		if (jTextFieldBatch.getText().equals("") == false)
		{
			q2.applyWhere("batch_number like ", jTextFieldBatch.getText());
		}

		if (jTextFieldProcessOrder.getText().equals("") == false)
		{
			q2.applyWhere("process_order = ", jTextFieldProcessOrder.getText());
		}

		if (jTextFieldLocation.getText().equals("") == false)
		{
			q2.applyWhere("location_id = ", jTextFieldLocation.getText());
		}

		if (jTextFieldUser.getText().equals("") == false)
		{
			q2.applyWhere("user_id = ", jTextFieldUser.getText());
		}

		if (jTextFieldEAN.getText().equals("") == false)
		{
			q2.applyWhere("EAN = ", jTextFieldEAN.getText());
		}

		if (jTextFieldDespatch_No.getText().equals("") == false)
		{
			q2.applyWhere("DESPATCH_NO = ", jTextFieldDespatch_No.getText());
		}

		if (jTextFieldVariant.getText().equals("") == false)
		{
			q2.applyWhere("variant = ", jTextFieldVariant.getText());
		}

		q2.applyWhere("transaction_type=", (String) comboBoxTransactionType.getSelectedItem());

		q2.applyWhere("transaction_subtype=", (String) comboBoxTransactionSubtype.getSelectedItem());

		q2.applyWhere("uom=", ((JDBUom) jComboBoxUOM.getSelectedItem()).getInternalUom());

		q2.applyWhere("status=", ((String) jComboBoxPalletStatus.getSelectedItem()).toString());

		if (jCheckBoxQuantity.isSelected())
		{
			if (jFormattedTextFieldQuantity.getText().equals("") == false)
			{
				q2.applyWhere("quantity=",JUtility.stringToBigDecimal(jFormattedTextFieldQuantity.getText().toString()));
			}
		}

		Integer i;

		try
		{
			i = Integer.valueOf(jFormattedTextFieldQuantity.getText());
			q2.applyWhere("quantity=", i);
		}
		catch (Exception e)
		{
		}

		q2.applySort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		q2.applyRestriction(jCheckBoxLimit.isSelected(), jSpinnerLimit.getValue());
		q2.applySQL();
		
		result = q2.getPreparedStatement();
		
		return result;
	}
	
	private void buildSQL() {
		
		JDBQuery2.closeStatement(listStatement);

		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("{schema}VIEW_PALLET_HISTORY_EXPIRY");

		if (jCheckBoxTransactionDate.isSelected())
		{
			q2.applyWhere("transaction_date>=", JUtility.getTimestampFromDate(transactionDateFrom.getDate()));
			q2.applyWhere("transaction_date<=", JUtility.getTimestampFromDate(transactionDateTo.getDate()));
		}


		if (jTextFieldTransaction_Ref.getText().equals("") == false)
		{
			q2.applyWhere("transaction_ref = ", jTextFieldTransaction_Ref.getText());
		}

		if (jTextFieldSSCC.getText().equals("") == false)
		{
			q2.applyWhere("sscc = ", jTextFieldSSCC.getText());
		}

		if (jTextFieldMaterial.getText().equals("") == false)
		{
			q2.applyWhere("material = ", jTextFieldMaterial.getText());
		}

		if (jTextFieldBatch.getText().equals("") == false)
		{
			q2.applyWhere("batch_number like ", jTextFieldBatch.getText());
		}

		if (jTextFieldProcessOrder.getText().equals("") == false)
		{
			q2.applyWhere("process_order = ", jTextFieldProcessOrder.getText());
		}

		if (jTextFieldLocation.getText().equals("") == false)
		{
			q2.applyWhere("location_id = ", jTextFieldLocation.getText());
		}

		if (jTextFieldUser.getText().equals("") == false)
		{
			q2.applyWhere("user_id = ", jTextFieldUser.getText());
		}

		if (jTextFieldEAN.getText().equals("") == false)
		{
			q2.applyWhere("EAN = ", jTextFieldEAN.getText());
		}

		if (jTextFieldDespatch_No.getText().equals("") == false)
		{
			q2.applyWhere("DESPATCH_NO = ", jTextFieldDespatch_No.getText());
		}

		if (jTextFieldVariant.getText().equals("") == false)
		{
			q2.applyWhere("variant = ", jTextFieldVariant.getText());
		}
		
		if (jTextFieldCustomer.getText().equals("") == false)
		{
			q2.applyWhere("customer_id = ", jTextFieldCustomer.getText());
		}

		q2.applyWhere("transaction_type=", (String) comboBoxTransactionType.getSelectedItem());

		q2.applyWhere("transaction_subtype=", (String) comboBoxTransactionSubtype.getSelectedItem());

		q2.applyWhere("uom=", ((JDBUom) jComboBoxUOM.getSelectedItem()).getInternalUom());

		q2.applyWhere("status=", ((String) jComboBoxPalletStatus.getSelectedItem()).toString());

		if (jCheckBoxQuantity.isSelected())
		{
			if (jFormattedTextFieldQuantity.getText().equals("") == false)
			{
				q2.applyWhere("quantity=",JUtility.stringToBigDecimal(jFormattedTextFieldQuantity.getText().toString()));
			}
		}

		Integer i;

		try
		{
			i = Integer.valueOf(jFormattedTextFieldQuantity.getText());
			q2.applyWhere("quantity=", i);
		}
		catch (Exception e)
		{
		}

		q2.applySort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		q2.applyRestriction(jCheckBoxLimit.isSelected(), jSpinnerLimit.getValue());
		q2.applySQL();
		listStatement = q2.getPreparedStatement();
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(979, 535));
			this.setBounds(0, 0, 1010, 651);
			setVisible(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				jDesktopPane1.setBounds(0, 0, 1003, 619);
				this.getContentPane().add(jDesktopPane1);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(917, 504));
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.setLayout(null);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 216, 995, 370);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][] { { "One", "Two" }, { "Three", "Four" } }, new String[] { "Column 1", "Column 2" });
						jTable1 = new JTable();
						jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);
						jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						jTable1.getTableHeader().setFont(Common.font_table_header);
						jTable1.getTableHeader().setForeground(Common.color_tableHeaderFont);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jTable1, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_report);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PALLETS"));
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										print();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Print"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										export();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Excel"));
								popupMenu.add(newItemMenuItem);
							}

							{
								mnReferenceData = new JMenu4j(lang.get("lbl_Referenced_Data"));
								popupMenu.add(mnReferenceData);
								{
									mntmEditBatch = new JMenuItem4j(lang.get("btn_Edit_Batch"));
									mntmEditBatch.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent arg0) {
											int row = jTable1.getSelectedRow();
											if (row >= 0)
											{
												String lmaterial = jTable1.getValueAt(row, 5).toString();
												String lbatch = jTable1.getValueAt(row, 6).toString();
												JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_BATCH_EDIT", lmaterial, lbatch);
											}
										}
									});
									mnReferenceData.add(mntmEditBatch);
									mntmEditBatch.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_EDIT"));
									mntmEditBatch.setIcon(Common.icon_batch);
								}
								{
									mntmEditLocation = new JMenuItem4j(lang.get("btn_Edit_Location"));
									mntmEditLocation.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent arg0) {
											int row = jTable1.getSelectedRow();
											if (row >= 0)
											{
												String llocation = jTable1.getValueAt(row, 12).toString();
												JLaunchMenu.runForm("FRM_ADMIN_LOCATION_EDIT", llocation);
											}
										}
									});
									mnReferenceData.add(mntmEditLocation);
									mntmEditLocation.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_LOCATION_EDIT"));
									mntmEditLocation.setIcon(Common.icon_location);
								}
								{
									mntmEditMaterial = new JMenuItem4j(lang.get("btn_Edit_Material"));
									mntmEditMaterial.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent arg0) {
											int row = jTable1.getSelectedRow();
											if (row >= 0)
											{
												String lmaterial = jTable1.getValueAt(row, 5).toString();
												JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_EDIT", lmaterial);
											}
										}
									});
									mnReferenceData.add(mntmEditMaterial);
									mntmEditMaterial.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_EDIT"));
									mntmEditMaterial.setIcon(Common.icon_material);
								}
								{
									mntmEditPallet = new JMenuItem4j(lang.get("btn_Edit_Pallet"));
									mntmEditPallet.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent arg0) {
											int row = jTable1.getSelectedRow();
											if (row >= 0)
											{
												String lpallet = jTable1.getValueAt(row, 4).toString();
												JLaunchMenu.runForm("FRM_ADMIN_PALLET_EDIT", lpallet);
											}
										}
									});
									mnReferenceData.add(mntmEditPallet);
									mntmEditPallet.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT"));
									mntmEditPallet.setIcon(Common.icon_pallet);
								}
								{
									mntmEditProcessOrder = new JMenuItem4j(lang.get("btn_Edit_Process_Order"));
									mntmEditProcessOrder.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent arg0) {
											int row = jTable1.getSelectedRow();
											if (row >= 0)
											{
												String lprocessorder = jTable1.getValueAt(row, 7).toString();
												JLaunchMenu.runForm("FRM_ADMIN_PROCESS_ORDER_EDIT", lprocessorder);
											}
										}
									});
									mnReferenceData.add(mntmEditProcessOrder);
									mntmEditProcessOrder.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PROCESS_ORDER_EDIT"));
									mntmEditProcessOrder.setIcon(Common.icon_process_order);
								}
							}

							{
								final JMenu4j filterByMenu = new JMenu4j();
								filterByMenu.setText(lang.get("lbl_Filter_By"));
								popupMenu.add(filterByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Transaction Type");
										}

									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Type"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Transaction Subtype");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Subtype"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("SSCC");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_SSCC"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Material");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Batch");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Location");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Location_ID"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Process Order");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Process_Order"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Status");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_Status"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Despatch No");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Despatch_No"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("User");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_User_ID"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									filterByMenu.addSeparator();
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											clearFilter();
										}
									});
									newItemMenuItem.setText(lang.get("btn_Clear_Filter"));
									filterByMenu.add(newItemMenuItem);
								}
							}

							{
								final JMenu4j sortByMenu = new JMenu4j();
								sortByMenu.setText(lang.get("lbl_Sort_By"));
								popupMenu.add(sortByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("TRANSACTION_REF");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Ref"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("TRANSACTION_TYPE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Type"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("TRANSACTION_SUBTYPE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Subtype"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("TRANSACTION_DATE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Date"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("SSCC");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_SSCC"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("MATERIAL");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("BATCH_NUMBER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("LOCATION_ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Location_ID"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("PROCESS_ORDER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Process_Order"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("QUANTITY");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_Quantity"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("DATE_OF_MANUFACTURER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_DOM"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("STATUS");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_Status"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("UOM");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_UOM"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("EAN");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_UOM_EAN"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("VARIANT");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_UOM_Variant"));
									sortByMenu.add(newItemMenuItem);
								}
								{
									jStatusText = new JLabel4j_std();
									jStatusText.setBounds(0, 595, 1000, 21);
									jDesktopPane1.add(jStatusText);
									jStatusText.setForeground(new Color(255, 0, 0));
									jStatusText.setBackground(Color.GRAY);
									jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
								}
							}
						}

					}
				}
				{
					jButtonSearch1 = new JButton4j(Common.icon_search);
					jDesktopPane1.add(jButtonSearch1);
					jButtonSearch1.setText(lang.get("btn_Search"));
					jButtonSearch1.setBounds(2, 176, 140, 32);
					jButtonSearch1.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonSearch1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							buildSQL();
							populateList();

						}
					});
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(134, 44, 127, 22);
				}
				{
					jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(jTextFieldLocation);
					jTextFieldLocation.setBounds(593, 44, 98, 22);
				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setBounds(1, 44, 121, 21);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Location_ID"));
					jLabel3.setBounds(485, 44, 103, 21);
				}
				{
					ComboBoxModel<JDBUom> jComboBox2Model = new DefaultComboBoxModel<JDBUom>(uomList);
					jComboBoxUOM = new JComboBox4j<JDBUom>();
					jDesktopPane1.add(jComboBoxUOM);
					jComboBoxUOM.setModel(jComboBox2Model);
					jComboBoxUOM.setBounds(865, 77, 130, 22);
					jComboBoxUOM.setMaximumRowCount(12);
				}
				{
					jLabel4 = new JLabel4j_std();
					jDesktopPane1.add(jLabel4);
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel4.setText(lang.get("lbl_Pallet_UOM"));
					jLabel4.setBounds(764, 77, 96, 21);
					jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(570, 176, 140, 32);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(854, 176, 140, 32);
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JDBQuery2.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setBounds(286, 176, 140, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PALLETS"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							print();
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print_Summary"));
					jButtonPrint.setBounds(428, 176, 140, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_HIST_SUMMARY"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							print_summary();
						}
					});
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Limit"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(796, 143, 87, 21);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[] { "TRANSACTION_REF,SSCC", "TRANSACTION_TYPE,TRANSACTION_SUBTYPE", "TRANSACTION_DATE,SSCC", "MATERIAL,BATCH_NUMBER", "MATERIAL,PROCESS_ORDER", "BATCH_NUMBER,MATERIAL", "PROCESS_ORDER", "QUANTITY",
							"DATE_OF_MANUFACTURE", "STATUS", "LOCATION_ID", "UOM", "EAN", "VARIANT" });
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					//jComboBoxSortBy.setSelectedItem("TRANSACTION_DATE,SSCC");
					jComboBoxSortBy.setBounds(411, 141, 299, 23);
				}
				{
					ComboBoxModel<String> jComboBoxDefaultPalletStatusModel = new DefaultComboBoxModel<String>(Common.palletStatusIncBlank);
					jComboBoxPalletStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxPalletStatus);
					jComboBoxPalletStatus.setModel(jComboBoxDefaultPalletStatusModel);
					jComboBoxPalletStatus.setBounds(844, 44, 150, 22);
				}
				{
					jLabel15 = new JLabel4j_std();
					jDesktopPane1.add(jLabel15);
					jLabel15.setText(lang.get("lbl_Pallet_Status"));
					jLabel15.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel15.setBounds(731, 44, 108, 21);
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jToggleButtonSequence.setSelected(true);
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(718, 143, 21, 21);
					jToggleButtonSequence.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}
				{
					jLabelQuantity = new JLabel4j_std();
					jDesktopPane1.add(jLabelQuantity);
					jLabelQuantity.setText(lang.get("lbl_Pallet_Quantity"));
					jLabelQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelQuantity.setBounds(0, 143, 121, 21);
				}
				{

					jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
					jDesktopPane1.add(jFormattedTextFieldQuantity);
					jFormattedTextFieldQuantity.setFont(Common.font_std);
					jFormattedTextFieldQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
					jFormattedTextFieldQuantity.setBounds(160, 143, 70, 21);
					jFormattedTextFieldQuantity.setVerifyInputWhenFocusTarget(false);
					jFormattedTextFieldQuantity.setEnabled(false);
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Material_Batch"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2.setBounds(266, 44, 77, 21);
				}
				{
					jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jDesktopPane1.add(jTextFieldBatch);
					jTextFieldBatch.setBounds(347, 44, 108, 22);
				}
				{
					jLabelProcessOrder = new JLabel4j_std();
					jDesktopPane1.add(jLabelProcessOrder);
					jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
					jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelProcessOrder.setBounds(691, 110, 144, 21);
				}
				{
					jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setBounds(854, 110, 120, 22);
				}
				{
					jTextFieldEAN = new JTextField4j(JDBMaterialUom.field_ean);
					jDesktopPane1.add(jTextFieldEAN);
					jTextFieldEAN.setBounds(593, 77, 119, 22);
					jTextFieldEAN.setFocusCycleRoot(true);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Material_UOM_EAN"));
					jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel5.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel5.setBounds(485, 77, 103, 21);
				}
				{
					jLabel6 = new JLabel4j_std();
					jDesktopPane1.add(jLabel6);
					jLabel6.setText("/");
					jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
					jLabel6.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel6.setBounds(714, 77, 10, 21);
				}
				{
					jTextFieldVariant = new JTextField4j(JDBMaterialUom.field_variant);
					jDesktopPane1.add(jTextFieldVariant);
					jTextFieldVariant.setBounds(725, 77, 21, 22);
					jTextFieldVariant.setFocusCycleRoot(true);
				}
				{
					jCheckBoxQuantity = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxQuantity);
					jCheckBoxQuantity.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxQuantity.setBounds(128, 143, 21, 21);
					jCheckBoxQuantity.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if (jCheckBoxQuantity.isSelected())
							{
								jFormattedTextFieldQuantity.setValue(0);
								jFormattedTextFieldQuantity.setEnabled(true);
							}
							else
							{
								jFormattedTextFieldQuantity.setValue(0);
								jFormattedTextFieldQuantity.setEnabled(false);
							}
						}
					});
				}
				{
					jLabel7 = new JLabel4j_std();
					jDesktopPane1.add(jLabel7);
					jLabel7.setText(lang.get("lbl_Sort_By"));
					jLabel7.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel7.setBounds(242, 143, 158, 21);
				}
				{
					SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
					jSpinnerIntModel.setMinimum(1);
					jSpinnerIntModel.setMaximum(100000);
					jSpinnerIntModel.setStepSize(1);

					jSpinnerLimit = new JSpinner();
					jDesktopPane1.add(jSpinnerLimit);
					
					jSpinnerLimit.setModel(jSpinnerIntModel);
					JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
					ne.getTextField().setFont(Common.font_std); 
					jSpinnerLimit.setEditor(ne);
					jSpinnerLimit.setBounds(911, 143, 73, 21);
					jSpinnerLimit.setValue(1000);
					jSpinnerLimit.getEditor().setSize(45, 21);
				}
				{
					jCheckBoxLimit = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxLimit);
					jCheckBoxLimit.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxLimit.setBounds(890, 143, 21, 21);
					jCheckBoxLimit.setSelected(true);
					jCheckBoxLimit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if (jCheckBoxLimit.isSelected())
							{
								jSpinnerLimit.setEnabled(true);
							}
							else
							{
								jSpinnerLimit.setEnabled(false);
							}
						}
					});
				}
				{
					jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupProcessOrder);
					jButtonLookupProcessOrder.setBounds(974, 111, 21, 21);
					jButtonLookupProcessOrder.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
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
					jButtonLookupBatch = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupBatch);
					jButtonLookupBatch.setBounds(454, 44, 21, 21);
					jButtonLookupBatch.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchLookup.dlgCriteriaDefault = jTextFieldMaterial.getText();
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.materialBatches())
							{
								jTextFieldBatch.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jButtonLookupMaterial = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupMaterial);
					jButtonLookupMaterial.setBounds(261, 44, 21, 21);
					jButtonLookupMaterial.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.materials())
							{
								jTextFieldMaterial.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jButtonLookupLocation = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupLocation);
					jButtonLookupLocation.setBounds(691, 44, 21, 21);
					jButtonLookupLocation.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "Y";
							if (JLaunchLookup.locations())
							{
								jTextFieldLocation.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
					jDesktopPane1.add(jTextFieldSSCC);
					jTextFieldSSCC.setBounds(134, 77, 140, 22);
				}
				{
					jLabelSCC = new JLabel4j_std();
					jDesktopPane1.add(jLabelSCC);
					jLabelSCC.setText(lang.get("lbl_Pallet_SSCC"));
					jLabelSCC.setBounds(1, 77, 121, 21);
					jLabelSCC.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jLabel8_1 = new JLabel4j_std();
					jLabel8_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel8_1.setText(lang.get("lbl_Despatch_No"));
					jLabel8_1.setBounds(266, 77, 77, 21);
					jDesktopPane1.add(jLabel8_1);
				}

				{
					jTextFieldDespatch_No = new JTextField4j(JDBDespatch.field_despatch_no);
					jTextFieldDespatch_No.setFocusCycleRoot(true);
					jTextFieldDespatch_No.setBounds(347, 77, 128, 22);
					jDesktopPane1.add(jTextFieldDespatch_No);
				}

				{
					jLabel1_1 = new JLabel4j_std();
					jLabel1_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1_1.setText(lang.get("lbl_Transaction_Ref"));
					jLabel1_1.setBounds(485, 110, 103, 21);
					jDesktopPane1.add(jLabel1_1);
				}

				{
					transactionDateFrom = new JDateControl();
					transactionDateFrom.setBounds(154, 7, 128, 25);
					transactionDateFrom.setEnabled(false);
					jDesktopPane1.add(transactionDateFrom);
				}

				{
					jCheckBoxTransactionDate = new JCheckBox4j();
					jCheckBoxTransactionDate.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							if (jCheckBoxTransactionDate.isSelected())
							{
								transactionDateFrom.setEnabled(true);
								transactionDateTo.setEnabled(true);
								calendarButtontransactionDateFrom.setEnabled(true);
								calendarButtontransactionDateTo.setEnabled(true);
							}
							else
							{
								transactionDateFrom.setEnabled(false);
								transactionDateTo.setEnabled(false);
								calendarButtontransactionDateFrom.setEnabled(false);
								calendarButtontransactionDateTo.setEnabled(false);
							}
						}
					});
					jCheckBoxTransactionDate.setBackground(new Color(255, 255, 255));
					jCheckBoxTransactionDate.setBounds(129, 9, 21, 21);
					jDesktopPane1.add(jCheckBoxTransactionDate);
				}

				{
					jLabel1_2 = new JLabel4j_std();
					jLabel1_2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1_2.setText(lang.get("lbl_Transaction_Date"));
					jLabel1_2.setBounds(1, 12, 121, 21);
					jDesktopPane1.add(jLabel1_2);
				}

				{
					jLabel1_3 = new JLabel4j_std();
					jLabel1_3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1_3.setText(lang.get("lbl_Transaction_Type"));
					jLabel1_3.setBounds(495, 11, 93, 21);
					jDesktopPane1.add(jLabel1_3);
				}

				{
					jLabel1_4 = new JLabel4j_std();
					jLabel1_4.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1_4.setText(lang.get("lbl_Transaction_Subtype"));
					jLabel1_4.setBounds(721, 11, 140, 23);
					jDesktopPane1.add(jLabel1_4);
				}

				{
					comboBoxTransactionType = new JComboBox4j<String>();
					comboBoxTransactionType.setModel(new DefaultComboBoxModel<String>(Common.transactionTypes));
					comboBoxTransactionType.setBounds(593, 10, 124, 22);
					jDesktopPane1.add(comboBoxTransactionType);
				}

				{
					comboBoxTransactionSubtype = new JComboBox4j<String>();
					comboBoxTransactionSubtype.setModel(new DefaultComboBoxModel<String>(Common.transactionSubTypes));
					comboBoxTransactionSubtype.setBounds(865, 10, 130, 22);
					jDesktopPane1.add(comboBoxTransactionSubtype);
				}

				{
					jTextFieldTransaction_Ref = new JTextField4j();
					jTextFieldTransaction_Ref.setBounds(593, 110, 119, 22);
					jDesktopPane1.add(jTextFieldTransaction_Ref);
				}

				{
					transactionDateTo = new JDateControl();
					transactionDateTo.setBounds(347, 7, 128, 25);
					transactionDateTo.setEnabled(false);
					jDesktopPane1.add(transactionDateTo);
				}

				{
					final JButton4j exportXlsButton = new JButton4j(Common.icon_XLS);
					exportXlsButton.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							export();
						}
					});
					exportXlsButton.setText(lang.get("btn_Excel"));
					exportXlsButton.setBounds(712, 176, 140, 32);
					jDesktopPane1.add(exportXlsButton);
				}

				{
					jTextFieldUser = new JTextField4j(JDBUser.field_user_id);
					jTextFieldUser.setBounds(347, 110, 128, 22);
					jDesktopPane1.add(jTextFieldUser);
				}

				{
					jLabel5_1 = new JLabel4j_std();
					jLabel5_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5_1.setText(lang.get("lbl_User_ID"));
					jLabel5_1.setBounds(266, 110, 77, 21);
					jDesktopPane1.add(jLabel5_1);
				}

				{
					jButtonClear = new JButton4j(Common.icon_clear);
					jButtonClear.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							clearFilter();
						}
					});
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jButtonClear.setBounds(144, 176, 140, 32);
					jDesktopPane1.add(jButtonClear);
				}
				
				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Customer_ID"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(1, 111, 121, 21);
				jDesktopPane1.add(label4j_std);
				

				jTextFieldCustomer.setBounds(134, 110, 127, 22);
				jDesktopPane1.add(jTextFieldCustomer);
				
				JButton4j button4j = new JButton4j(Common.icon_lookup);
				button4j.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "";
						if (JLaunchLookup.customers())
						{
							jTextFieldCustomer.setText(JLaunchLookup.dlgResult);
						}
					}
				});
				button4j.setBounds(261, 111, 21, 21);
				jDesktopPane1.add(button4j);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void clearFilter() {
		jTextFieldTransaction_Ref.setText("");
		jTextFieldMaterial.setText("");
		jTextFieldBatch.setText("");
		jTextFieldSSCC.setText("");
		jTextFieldLocation.setText("");
		jTextFieldProcessOrder.setText("");
		jTextFieldDespatch_No.setText("");
		jTextFieldEAN.setText("");
		jTextFieldVariant.setText("");
		jComboBoxPalletStatus.setSelectedItem("");
		jComboBoxUOM.setSelectedIndex(0);
		comboBoxTransactionType.setSelectedItem("");
		comboBoxTransactionSubtype.setSelectedItem("");
		jCheckBoxQuantity.setSelected(false);
		jFormattedTextFieldQuantity.setValue(0);
		jCheckBoxTransactionDate.setSelected(false);
		jTextFieldUser.setText("");
		//buildSQL();
		//populateList();
	}

	private void populateList() {
		JDBPalletHistory pallethistory = new JDBPalletHistory(Common.selectedHostID, Common.sessionID);

		JDBPalletHistoryTableModel palletHistoryTable = new JDBPalletHistoryTableModel(pallethistory.getPalletHistoryDataResultSet(listStatement));
		TableRowSorter<JDBPalletHistoryTableModel> sorter = new TableRowSorter<JDBPalletHistoryTableModel>(palletHistoryTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(palletHistoryTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(0).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(3).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(4).setPreferredWidth(140);
		jTable1.getColumnModel().getColumn(5).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(6).setPreferredWidth(75);
		jTable1.getColumnModel().getColumn(7).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(8).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(9).setPreferredWidth(40);
		jTable1.getColumnModel().getColumn(10).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(11).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(12).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(13).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(14).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(15).setPreferredWidth(100);
		if (expiryMode.equals("SSCC"))
		{
			jTable1.getColumnModel().getColumn(16).setPreferredWidth(120);
		}
		
		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), palletHistoryTable.getRowCount());

	}

	private void setSequence(boolean descending) {

		if (jToggleButtonSequence.isSelected() == true)
		{
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending);
		} else
		{
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending);
		}
	}

	/**
	 * WindowBuilder generated method.<br>
	 * Please don't remove this method or its invocations.<br>
	 * It used by WindowBuilder to associate the {@link javax.swing.JPopupMenu}
	 * with parent.
	 */
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
