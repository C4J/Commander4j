package com.commander4j.app;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;

import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMHN;
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
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JToggleButton4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBPalletHistoryTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import com.commander4j.util.JWait;

/**
 * The JInternalFramePalletHistoryAdmin allows you to search the
 * APP_PALLET_HISTORY table which contains a multiple records for each SSCC. The
 * pallet history table is in effect an audit trail for all actions which effect
 * a pallet. Each time a transaction is performed on a SSCC the system generates
 * a new transaction number which is written to the pallet history table. This
 * transaction ref is optionally inserted into the SYS_INTERFACE_REQUEST table
 * which will trigger the generation of an interface message.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFramePalletHistoryAdmin.jpg" >
 *
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.app.JInternalFramePalletHistoryAdmin
 *      JInternalFramePalletHistoryAdmin
 * @see com.commander4j.db.JDBPalletHistory JDBPalletHistory
 * @see com.commander4j.interfaces.JInternalFrameInterfaceLog
 *      JInternalFrameInterfaceLog
 * @see com.commander4j.db.JDBInterfaceRequest JDBInterfaceRequest
 */
public class JInternalFramePalletHistoryAdmin extends JInternalFrame
{
	private static boolean dlg_sort_descending = true;
	private static final long serialVersionUID = 1;
	private JButton4j jButtonClear;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonLookupBatch;
	private JButton4j jButtonLookupLocation;
	private JButton4j jButtonLookupMaterial;
	private JButton4j jButtonLookupProcessOrder;
	private JButton4j jButtonPrint;
	private JButton4j jButtonSearch1;
	private JCalendarButton calendarButtontransactionDateFrom;
	private JCalendarButton calendarButtontransactionDateTo;
	private JCheckBox4j jCheckBoxLimit;
	private JCheckBox4j jCheckBoxQuantity;
	private JCheckBox4j jCheckBoxTransactionDate;
	private JComboBox4j<JDBUom> jComboBoxUOM;
	private JComboBox4j<String> comboBoxTransactionSubtype;
	private JComboBox4j<String> comboBoxTransactionType;
	private JComboBox4j<String> jComboBoxPalletStatus;
	private JComboBox4j<String> jComboBoxSortBy;
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBMaterialType t = new JDBMaterialType(Common.selectedHostID, Common.sessionID);
	private JDBQuery2 qExcel = new JDBQuery2(Common.selectedHostID, Common.sessionID);
	private JDBQuery2 qPrint = new JDBQuery2(Common.selectedHostID, Common.sessionID);
	private JDBQuery2 qSearch = new JDBQuery2(Common.selectedHostID, Common.sessionID);
	private JDBUom u = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDateControl transactionDateFrom;
	private JDateControl transactionDateTo;
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_status jStatusText;
	private JLabel4j_std jLabel6;
	private JLabel4j_std jLabelQuantity;
	private JLabel4j_std jLabel_Batch;
	private JLabel4j_std jLabel_DespatchNo;
	private JLabel4j_std jLabel_EAN;
	private JLabel4j_std jLabel_Limit;
	private JLabel4j_std jLabel_LocationID;
	private JLabel4j_std jLabel_Material;
	private JLabel4j_std jLabel_PalletStatus;
	private JLabel4j_std jLabel_ProcessOrder;
	private JLabel4j_std jLabel_SSCC;
	private JLabel4j_std jLabel_SortBy;
	private JLabel4j_std jLabel_TransactionDate;
	private JLabel4j_std jLabel_TransactionRef;
	private JLabel4j_std jLabel_TransactionSubtype;
	private JLabel4j_std jLabel_TransactionType;
	private JLabel4j_std jLabel_UOM;
	private JLabel4j_std jLabel_User;
	private JMenu4j mnReferenceData;
	private JMenuItem4j mntmEditBatch;
	private JMenuItem4j mntmEditLocation;
	private JMenuItem4j mntmEditMaterial;
	private JMenuItem4j mntmEditPallet;
	private JMenuItem4j mntmEditProcessOrder;
	private JQuantityInput jFormattedTextFieldQuantity;
	private JScrollPane4j jScrollPane1;
	private JSpinner4j jSpinnerLimit;
	private JTable4j jTable1;
	private JTextField4j jTextFieldBatch;
	private JTextField4j jTextFieldCustomer = new JTextField4j(20);
	private JTextField4j jTextFieldDespatch_No;
	private JTextField4j jTextFieldEAN;
	private JTextField4j jTextFieldLocation;
	private JTextField4j jTextFieldMaterial;
	private JTextField4j jTextFieldProcessOrder;
	private JTextField4j jTextFieldSSCC;
	private JTextField4j jTextFieldTransaction_Ref;
	private JTextField4j jTextFieldUser;
	private JTextField4j jTextFieldVariant;
	private JTextField4j textField4j_MHN;
	private JToggleButton4j jToggleButtonSequence;
	private String expiryMode = "";
	private Vector<JDBMaterialType> typeList = new Vector<JDBMaterialType>();
	private Vector<JDBUom> uomList = new Vector<JDBUom>();

	private boolean emptyresult = true;

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

	public JInternalFramePalletHistoryAdmin()
	{
		super();
		app_Init();

	}

	public JInternalFramePalletHistoryAdmin(String keyField, String keyValue)
	{
		super();
		app_Init();

		clearFilter();

		updateSearch(keyField, keyValue);

	}

	public JInternalFramePalletHistoryAdmin(String keyField, String keyValue1, String keyValue2)
	{
		super();
		app_Init();

		clearFilter();

		updateSearch(keyField, keyValue1, keyValue2);

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

		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_PALLET_HISTORY"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		calendarButtontransactionDateFrom = new JCalendarButton(transactionDateFrom);
		calendarButtontransactionDateFrom.setEnabled(false);
		calendarButtontransactionDateFrom.setBounds(251, 11, 22, 22);
		jDesktopPane1.add(calendarButtontransactionDateFrom);

		calendarButtontransactionDateTo = new JCalendarButton(transactionDateTo);
		calendarButtontransactionDateTo.setEnabled(false);
		calendarButtontransactionDateTo.setBounds(469, 11, 22, 22);
		jDesktopPane1.add(calendarButtontransactionDateTo);

		setSequence(dlg_sort_descending);
	}

	private PreparedStatement buildSQL(JDBQuery2 query)
	{

		query = new JDBQuery2(Common.selectedHostID, Common.sessionID);

		PreparedStatement result;

		query.applyWhat("*");
		query.applyFrom("{schema}view_pallet_history_expiry");

		if (emptyresult)
		{
			query.applyWhere("1 = ", 2);
			emptyresult = false;
		}

		if (jCheckBoxTransactionDate.isSelected())
		{
			query.applyWhere("transaction_date>=", JUtility.getTimestampFromDate(transactionDateFrom.getDate()));
			query.applyWhere("transaction_date<=", JUtility.getTimestampFromDate(transactionDateTo.getDate()));
		}

		if (jTextFieldTransaction_Ref.getText().equals("") == false)
		{
			query.applyWhere("transaction_ref = ", jTextFieldTransaction_Ref.getText());
		}

		if (jTextFieldSSCC.getText().equals("") == false)
		{
			query.applyWhere("sscc = ", jTextFieldSSCC.getText());
		}

		if (jTextFieldMaterial.getText().equals("") == false)
		{
			query.applyWhere("material = ", jTextFieldMaterial.getText());
		}

		if (textField4j_MHN.getText().equals("") == false)
		{
			query.applyWhere("mhn_number = ", textField4j_MHN.getText());
		}

		if (jTextFieldBatch.getText().equals("") == false)
		{
			query.applyWhere("batch_number like ", jTextFieldBatch.getText());
		}

		if (jTextFieldProcessOrder.getText().equals("") == false)
		{
			query.applyWhere("process_order = ", jTextFieldProcessOrder.getText());
		}

		if (jTextFieldLocation.getText().equals("") == false)
		{
			query.applyWhere("location_id = ", jTextFieldLocation.getText());
		}

		if (jTextFieldUser.getText().equals("") == false)
		{
			query.applyWhere("user_id = ", jTextFieldUser.getText());
		}

		if (jTextFieldEAN.getText().equals("") == false)
		{
			query.applyWhere("EAN = ", jTextFieldEAN.getText());
		}

		if (jTextFieldDespatch_No.getText().equals("") == false)
		{
			query.applyWhere("DESPATCH_NO = ", jTextFieldDespatch_No.getText());
		}

		if (jTextFieldVariant.getText().equals("") == false)
		{
			query.applyWhere("variant = ", jTextFieldVariant.getText());
		}

		if (jTextFieldCustomer.getText().equals("") == false)
		{
			query.applyWhere("customer_id = ", jTextFieldCustomer.getText());
		}

		query.applyWhere("transaction_type=", (String) comboBoxTransactionType.getSelectedItem());

		query.applyWhere("transaction_subtype=", (String) comboBoxTransactionSubtype.getSelectedItem());

		query.applyWhere("uom=", ((JDBUom) jComboBoxUOM.getSelectedItem()).getInternalUom());

		query.applyWhere("status=", ((String) jComboBoxPalletStatus.getSelectedItem()).toString());

		if (jCheckBoxQuantity.isSelected())
		{
			if (jFormattedTextFieldQuantity.getText().equals("") == false)
			{
				query.applyWhere("quantity=", JUtility.stringToBigDecimal(jFormattedTextFieldQuantity.getText().toString()));
			}
		}

		Integer i;

		try
		{
			i = Integer.valueOf(jFormattedTextFieldQuantity.getText());
			query.applyWhere("quantity=", i);
		}
		catch (Exception e)
		{
		}

		query.applySort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(jCheckBoxLimit.isSelected(), jSpinnerLimit.getValue());
		query.applySQL();

		result = query.getPreparedStatement();

		return result;
	}

	private void clearFilter()
	{
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
		jTextFieldCustomer.setText("");
		textField4j_MHN.setText("");
		// buildSQL();
		// populateList();
	}

	private void copyToClipboard(String fieldname)
	{
		StringSelection stringSelection = new StringSelection("");

		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals("SSCC") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 4).toString());
			}

			if (fieldname.equals("Material") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 5).toString());
			}

			if (fieldname.equals("Batch") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 6).toString());
			}

			if (fieldname.equals("Process Order") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 7).toString());
			}
			if (fieldname.equals("Quantity") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 10).toString());
			}
			if (fieldname.equals("UOM") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 9).toString());
			}
			if (fieldname.equals("Despatch No") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 10).toString());
			}
			if (fieldname.equals("Status") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 11).toString());
			}

			if (fieldname.equals("Location") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 12).toString());
			}
			if (fieldname.equals("User") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 13).toString());
			}

			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		}
	}

	private void export()
	{
		JDBPalletHistory palletHistory = new JDBPalletHistory(Common.selectedHostID, Common.sessionID);

		JExcel export = new JExcel();
		export.setExcelRowLimit(jCheckBoxLimit, jSpinnerLimit);
		export.saveAs("pallet_history.xls", palletHistory.getPalletHistoryDataResultSet(buildSQL(qExcel)), Common.mainForm);
		// populateList();
	}

	private void filterBy(String fieldname)
	{
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

			populateList();

		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(979, 535));
			this.setBounds(0, 0, 1010, 651);
			setVisible(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane4j();

				jDesktopPane1.setBounds(0, 0, 1003, 619);
				this.getContentPane().add(jDesktopPane1);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(917, 504));
				{
					jScrollPane1 = new JScrollPane4j(JScrollPane4j.Table);
					jDesktopPane1.setLayout(null);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 210, 995, 376);
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
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_report_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PALLETS"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										print();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Print"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										export();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Excel"));
								popupMenu.add(newItemMenuItem);
							}
							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_interface_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										interfaceLog();
									}
								});
								newItemMenuItem.setText(lang.get("mod_FRM_ADMIN_INTERFACE_LOG"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_INTERFACE_LOG"));
								popupMenu.add(newItemMenuItem);
							}

							mnReferenceData = new JMenu4j(lang.get("lbl_Referenced_Data"));
							popupMenu.add(mnReferenceData);

							mntmEditBatch = new JMenuItem4j(lang.get("btn_Edit_Batch"));
							mntmEditBatch.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent arg0)
								{
									int row = jTable1.getSelectedRow();
									if (row >= 0)
									{
										String lmaterial = jTable1.getValueAt(row, 5).toString();
										String lbatch = jTable1.getValueAt(row, 6).toString();
										JLaunchMenu.runDialog("FRM_ADMIN_MATERIAL_BATCH_EDIT", lmaterial, lbatch);
									}
								}
							});
							mnReferenceData.add(mntmEditBatch);
							mntmEditBatch.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_EDIT"));
							mntmEditBatch.setIcon(Common.icon_batch_16x16);

							mntmEditLocation = new JMenuItem4j(lang.get("btn_Edit_Location"));
							mntmEditLocation.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent arg0)
								{
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
							mntmEditLocation.setIcon(Common.icon_location_16x16);

							mntmEditMaterial = new JMenuItem4j(lang.get("btn_Edit_Material"));
							mntmEditMaterial.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent arg0)
								{
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
							mntmEditMaterial.setIcon(Common.icon_material_16x16);

							mntmEditPallet = new JMenuItem4j(lang.get("btn_Edit_Pallet"));
							mntmEditPallet.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent arg0)
								{
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
							mntmEditPallet.setIcon(Common.icon_pallet_16x16);

							mntmEditProcessOrder = new JMenuItem4j(lang.get("btn_Edit_Process_Order"));
							mntmEditProcessOrder.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent arg0)
								{
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
							mntmEditProcessOrder.setIcon(Common.icon_process_order_16x16);

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
											filterBy("Transaction Type");
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
											filterBy("Transaction Subtype");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Subtype"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("SSCC");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_SSCC"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("Material");
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
											filterBy("Batch");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("Location");
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
											filterBy("Process Order");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Process_Order"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("Status");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_Status"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("Despatch No");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Despatch_No"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
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
								final JMenu4j sortByMenu = new JMenu4j();
								sortByMenu.setText(lang.get("lbl_Sort_By"));
								popupMenu.add(sortByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("TRANSACTION_REF");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Ref"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("TRANSACTION_TYPE");
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
											sortBy("TRANSACTION_SUBTYPE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Subtype"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("TRANSACTION_DATE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Transaction_Date"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("SSCC");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_SSCC"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("MATERIAL");
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
											sortBy("BATCH_NUMBER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("LOCATION_ID");
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
											sortBy("PROCESS_ORDER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Process_Order"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("QUANTITY");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_Quantity"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("DATE_OF_MANUFACTURER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_DOM"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("STATUS");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_Status"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("UOM");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_UOM"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("EAN");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_UOM_EAN"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("VARIANT");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_UOM_Variant"));
									sortByMenu.add(newItemMenuItem);
								}

								jStatusText = new JLabel4j_status();
								jStatusText.setBounds(5, 592, 986, 21);
								jDesktopPane1.add(jStatusText);

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
											copyToClipboard("SSCC");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_SSCC"));
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
											copyToClipboard("Batch");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch"));
									clipboardByMenu.add(newItemMenuItem);
								}

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
											copyToClipboard("Status");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_Status"));
									clipboardByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Despatch No");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Despatch_No"));
									clipboardByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("User");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_User_ID"));
									clipboardByMenu.add(newItemMenuItem);
								}
							}

						}

					}
				}

				jButtonSearch1 = new JButton4j(Common.icon_search_16x16);
				jDesktopPane1.add(jButtonSearch1);
				jButtonSearch1.setText(lang.get("btn_Search"));
				jButtonSearch1.setBounds(2, 170, 140, 32);
				jButtonSearch1.setMnemonic(java.awt.event.KeyEvent.VK_S);
				jButtonSearch1.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{

						populateList();

					}
				});

				jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
				jDesktopPane1.add(jTextFieldMaterial);
				jTextFieldMaterial.setBounds(130, 42, 120, 22);

				jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
				jDesktopPane1.add(jTextFieldLocation);
				jTextFieldLocation.setBounds(593, 42, 99, 22);

				jLabel_Material = new JLabel4j_std();
				jDesktopPane1.add(jLabel_Material);
				jLabel_Material.setText(lang.get("lbl_Material"));
				jLabel_Material.setBounds(1, 42, 99, 22);
				jLabel_Material.setHorizontalAlignment(SwingConstants.TRAILING);

				jLabel_LocationID = new JLabel4j_std();
				jDesktopPane1.add(jLabel_LocationID);
				jLabel_LocationID.setText(lang.get("lbl_Location_ID"));
				jLabel_LocationID.setBounds(476, 42, 112, 22);

				ComboBoxModel<JDBUom> jComboBox2Model = new DefaultComboBoxModel<JDBUom>(uomList);
				jComboBoxUOM = new JComboBox4j<JDBUom>();
				jDesktopPane1.add(jComboBoxUOM);
				jComboBoxUOM.setModel(jComboBox2Model);
				jComboBoxUOM.setBounds(843, 74, 152, 22);
				jComboBoxUOM.setMaximumRowCount(12);

				jLabel_UOM = new JLabel4j_std();
				jDesktopPane1.add(jLabel_UOM);
				jLabel_LocationID.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_UOM.setText(lang.get("lbl_Pallet_UOM"));
				jLabel_UOM.setBounds(749, 74, 87, 22);
				jLabel_UOM.setHorizontalAlignment(SwingConstants.TRAILING);

				jButtonHelp = new JButton4j(Common.icon_help_16x16);
				jDesktopPane1.add(jButtonHelp);
				jButtonHelp.setText(lang.get("btn_Help"));
				jButtonHelp.setBounds(570, 170, 140, 32);
				jButtonHelp.setMnemonic(lang.getMnemonicChar());

				jButtonClose = new JButton4j(Common.icon_close_16x16);
				jDesktopPane1.add(jButtonClose);
				jButtonClose.setText(lang.get("btn_Close"));
				jButtonClose.setBounds(854, 170, 140, 32);
				jButtonClose.setMnemonic(lang.getMnemonicChar());
				jButtonClose.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						qExcel = null;
						qPrint = null;
						qSearch = null;
						dispose();
					}
				});

				jButtonPrint = new JButton4j(Common.icon_report_16x16);
				jDesktopPane1.add(jButtonPrint);
				jButtonPrint.setText(lang.get("btn_Print"));
				jButtonPrint.setBounds(286, 170, 140, 32);
				jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PALLETS"));
				jButtonPrint.setMnemonic(lang.getMnemonicChar());
				jButtonPrint.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						print();
					}
				});

				jButtonPrint = new JButton4j(Common.icon_report_16x16);
				jDesktopPane1.add(jButtonPrint);
				jButtonPrint.setText(lang.get("btn_Print_Summary"));
				jButtonPrint.setBounds(428, 170, 140, 32);
				jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_HIST_SUMMARY"));
				jButtonPrint.setMnemonic(lang.getMnemonicChar());
				jButtonPrint.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						print_summary();
					}
				});

				jLabel_Limit = new JLabel4j_std();
				jDesktopPane1.add(jLabel_Limit);
				jLabel_Limit.setText(lang.get("lbl_Limit"));
				jLabel_Limit.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_Limit.setBounds(806, 136, 87, 22);

				String[] sortlist = new String[]
						{"TRANSACTION_REF,SSCC",
					     "TRANSACTION_TYPE",
					     "TRANSACTION_SUBTYPE",
								"TRANSACTION_TYPE,TRANSACTION_SUBTYPE",
								"TRANSACTION_DATE,SSCC",
								"TRANSACTION_DATE",
								"MATERIAL",
								"BATCH_NUMBER",
								"MATERIAL,BATCH_NUMBER",
								"MATERIAL,PROCESS_ORDER",
								"BATCH_NUMBER,MATERIAL",
								"PROCESS_ORDER",
								"QUANTITY",
								"DATE_OF_MANUFACTURE",
								"STATUS",
								"SSCC",
								"LOCATION_ID",
								"UOM",
								"EAN",
								"VARIANT",
								"EQUIPMENT_TYPE" };

				Arrays.sort(sortlist);

				ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(sortlist);

				jComboBoxSortBy = new JComboBox4j<String>();
				jComboBoxSortBy.setMaximumRowCount(15);
				jDesktopPane1.add(jComboBoxSortBy);
				jComboBoxSortBy.setModel(jComboBoxSortByModel);
				jComboBoxSortBy.setSelectedItem("TRANSACTION_REF,SSCC");
				jComboBoxSortBy.setBounds(351, 136, 279, 22);

				ComboBoxModel<String> jComboBoxDefaultPalletStatusModel = new DefaultComboBoxModel<String>(Common.palletStatusIncBlank);
				jComboBoxPalletStatus = new JComboBox4j<String>();
				jDesktopPane1.add(jComboBoxPalletStatus);
				jComboBoxPalletStatus.setModel(jComboBoxDefaultPalletStatusModel);
				jComboBoxPalletStatus.setBounds(843, 42, 152, 22);

				jLabel_PalletStatus = new JLabel4j_std();
				jDesktopPane1.add(jLabel_PalletStatus);
				jLabel_PalletStatus.setText(lang.get("lbl_Pallet_Status"));
				jLabel_PalletStatus.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_PalletStatus.setBounds(706, 42, 130, 22);

				jToggleButtonSequence = new JToggleButton4j();
				jToggleButtonSequence.setSelected(true);
				jDesktopPane1.add(jToggleButtonSequence);
				jToggleButtonSequence.setBounds(630, 136, 22, 22);
				jToggleButtonSequence.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						setSequence(jToggleButtonSequence.isSelected());
					}
				});

				jLabelQuantity = new JLabel4j_std();
				jDesktopPane1.add(jLabelQuantity);
				jLabelQuantity.setText(lang.get("lbl_Pallet_Quantity"));
				jLabelQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabelQuantity.setBounds(665, 136, 70, 22);

				jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
				jDesktopPane1.add(jFormattedTextFieldQuantity);
				jFormattedTextFieldQuantity.setBounds(765, 136, 70, 22);
				jFormattedTextFieldQuantity.setVerifyInputWhenFocusTarget(false);
				jFormattedTextFieldQuantity.setEnabled(false);

				jLabel_Batch = new JLabel4j_std();
				jDesktopPane1.add(jLabel_Batch);
				jLabel_Batch.setText(lang.get("lbl_Material_Batch"));
				jLabel_Batch.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_Batch.setBounds(251, 42, 92, 22);

				jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
				jDesktopPane1.add(jTextFieldBatch);
				jTextFieldBatch.setBounds(347, 42, 108, 22);

				jLabel_ProcessOrder = new JLabel4j_std();
				jDesktopPane1.add(jLabel_ProcessOrder);
				jLabel_ProcessOrder.setText(lang.get("lbl_Process_Order"));
				jLabel_ProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_ProcessOrder.setBounds(708, 105, 128, 22);

				jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
				jDesktopPane1.add(jTextFieldProcessOrder);
				jTextFieldProcessOrder.setBounds(843, 105, 131, 22);

				jTextFieldEAN = new JTextField4j(JDBMaterialUom.field_ean);
				jDesktopPane1.add(jTextFieldEAN);
				jTextFieldEAN.setBounds(593, 74, 119, 22);
				jTextFieldEAN.setFocusCycleRoot(true);

				jLabel_EAN = new JLabel4j_std();
				jDesktopPane1.add(jLabel_EAN);
				jLabel_EAN.setText(lang.get("lbl_Material_UOM_EAN"));
				jLabel_EAN.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_EAN.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_EAN.setBounds(476, 74, 112, 22);

				jLabel6 = new JLabel4j_std();
				jDesktopPane1.add(jLabel6);
				jLabel6.setText("/");
				jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
				jLabel6.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel6.setBounds(714, 75, 10, 21);

				jTextFieldVariant = new JTextField4j(JDBMaterialUom.field_variant);
				jDesktopPane1.add(jTextFieldVariant);
				jTextFieldVariant.setBounds(725, 74, 25, 22);
				jTextFieldVariant.setFocusCycleRoot(true);

				jCheckBoxQuantity = new JCheckBox4j();
				jDesktopPane1.add(jCheckBoxQuantity);

				jCheckBoxQuantity.setBounds(735, 136, 22, 22);
				jCheckBoxQuantity.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
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

				jLabel_SortBy = new JLabel4j_std();
				jDesktopPane1.add(jLabel_SortBy);
				jLabel_SortBy.setText(lang.get("lbl_Sort_By"));
				jLabel_SortBy.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_SortBy.setBounds(261, 136, 79, 22);

				SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
				jSpinnerIntModel.setMinimum(1);
				jSpinnerIntModel.setMaximum(100000);
				jSpinnerIntModel.setStepSize(1);

				jSpinnerLimit = new JSpinner4j();
				jDesktopPane1.add(jSpinnerLimit);

				jSpinnerLimit.setModel(jSpinnerIntModel);
				JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(jSpinnerLimit);
				jSpinnerLimit.setEditor(ne);
				jSpinnerLimit.setBounds(923, 136, 73, 22);
				jSpinnerLimit.setValue(1000);
				jSpinnerLimit.getEditor().setSize(45, 21);

				jCheckBoxLimit = new JCheckBox4j();
				jDesktopPane1.add(jCheckBoxLimit);

				jCheckBoxLimit.setBounds(900, 136, 22, 22);
				jCheckBoxLimit.setSelected(true);
				jCheckBoxLimit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
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

				jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup_16x16);
				jDesktopPane1.add(jButtonLookupProcessOrder);
				jButtonLookupProcessOrder.setBounds(974, 105, 22, 22);
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

				jButtonLookupBatch = new JButton4j(Common.icon_lookup_16x16);
				jDesktopPane1.add(jButtonLookupBatch);
				jButtonLookupBatch.setBounds(454, 42, 22, 22);
				jButtonLookupBatch.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						JLaunchLookup.dlgCriteriaDefault = jTextFieldMaterial.getText();
						JLaunchLookup.dlgAutoExec = true;
						if (JLaunchLookup.materialBatches())
						{
							jTextFieldBatch.setText(JLaunchLookup.dlgResult);
						}
					}
				});

				jButtonLookupMaterial = new JButton4j(Common.icon_lookup_16x16);
				jDesktopPane1.add(jButtonLookupMaterial);
				jButtonLookupMaterial.setBounds(251, 42, 22, 22);
				jButtonLookupMaterial.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						JLaunchLookup.dlgAutoExec = false;
						JLaunchLookup.dlgCriteriaDefault = "";
						if (JLaunchLookup.materials())
						{
							jTextFieldMaterial.setText(JLaunchLookup.dlgResult);
						}
					}
				});

				jButtonLookupLocation = new JButton4j(Common.icon_lookup_16x16);
				jDesktopPane1.add(jButtonLookupLocation);
				jButtonLookupLocation.setBounds(691, 42, 22, 22);
				jButtonLookupLocation.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "Y";
						if (JLaunchLookup.locations())
						{
							jTextFieldLocation.setText(JLaunchLookup.dlgResult);
						}
					}
				});

				jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
				jDesktopPane1.add(jTextFieldSSCC);
				jTextFieldSSCC.setBounds(130, 74, 120, 22);

				jLabel_SSCC = new JLabel4j_std();
				jDesktopPane1.add(jLabel_SSCC);
				jLabel_SSCC.setText(lang.get("lbl_Pallet_SSCC"));
				jLabel_SSCC.setBounds(1, 74, 99, 22);
				jLabel_SSCC.setHorizontalAlignment(SwingConstants.TRAILING);

				jLabel_DespatchNo = new JLabel4j_std();
				jLabel_DespatchNo.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_DespatchNo.setText(lang.get("lbl_Despatch_No"));
				jLabel_DespatchNo.setBounds(243, 74, 100, 22);
				jDesktopPane1.add(jLabel_DespatchNo);

				jTextFieldDespatch_No = new JTextField4j(JDBDespatch.field_despatch_no);
				jTextFieldDespatch_No.setFocusCycleRoot(true);
				jTextFieldDespatch_No.setBounds(347, 74, 128, 22);
				jDesktopPane1.add(jTextFieldDespatch_No);

				jLabel_TransactionRef = new JLabel4j_std();
				jLabel_TransactionRef.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_TransactionRef.setText(lang.get("lbl_Transaction_Ref"));
				jLabel_TransactionRef.setBounds(476, 105, 112, 22);
				jDesktopPane1.add(jLabel_TransactionRef);

				transactionDateFrom = new JDateControl();
				transactionDateFrom.setBounds(130, 8, 120, 25);
				transactionDateFrom.setEnabled(false);
				jDesktopPane1.add(transactionDateFrom);

				jCheckBoxTransactionDate = new JCheckBox4j();
				jCheckBoxTransactionDate.addActionListener(new ActionListener()
				{
					public void actionPerformed(final ActionEvent e)
					{
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

				jCheckBoxTransactionDate.setBounds(105, 12, 21, 21);
				jDesktopPane1.add(jCheckBoxTransactionDate);

				jLabel_TransactionDate = new JLabel4j_std();
				jLabel_TransactionDate.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_TransactionDate.setText(lang.get("lbl_Transaction_Date"));
				jLabel_TransactionDate.setBounds(1, 12, 99, 21);
				jDesktopPane1.add(jLabel_TransactionDate);

				jLabel_TransactionType = new JLabel4j_std();
				jLabel_TransactionType.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_TransactionType.setText(lang.get("lbl_Transaction_Type"));
				jLabel_TransactionType.setBounds(489, 12, 99, 21);
				jDesktopPane1.add(jLabel_TransactionType);

				jLabel_TransactionSubtype = new JLabel4j_std();
				jLabel_TransactionSubtype.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_TransactionSubtype.setText(lang.get("lbl_Transaction_Subtype"));
				jLabel_TransactionSubtype.setBounds(721, 11, 115, 22);
				jDesktopPane1.add(jLabel_TransactionSubtype);

				comboBoxTransactionType = new JComboBox4j<String>();
				comboBoxTransactionType.setModel(new DefaultComboBoxModel<String>(Common.transactionTypes));
				comboBoxTransactionType.setMaximumRowCount(15);
				comboBoxTransactionType.setBounds(593, 11, 124, 22);
				jDesktopPane1.add(comboBoxTransactionType);

				comboBoxTransactionSubtype = new JComboBox4j<String>();
				comboBoxTransactionSubtype.setModel(new DefaultComboBoxModel<String>(Common.transactionSubTypes));
				comboBoxTransactionSubtype.setBounds(843, 11, 152, 22);
				comboBoxTransactionSubtype.setMaximumRowCount(15);
				jDesktopPane1.add(comboBoxTransactionSubtype);

				jTextFieldTransaction_Ref = new JTextField4j();
				jTextFieldTransaction_Ref.setBounds(593, 105, 119, 22);
				jDesktopPane1.add(jTextFieldTransaction_Ref);

				transactionDateTo = new JDateControl();
				transactionDateTo.setBounds(347, 8, 120, 25);
				transactionDateTo.setEnabled(false);
				jDesktopPane1.add(transactionDateTo);

				final JButton4j exportXlsButton = new JButton4j(Common.icon_XLS_16x16);
				exportXlsButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(final ActionEvent e)
					{
						export();
					}
				});
				exportXlsButton.setText(lang.get("btn_Excel"));
				exportXlsButton.setBounds(712, 170, 140, 32);
				jDesktopPane1.add(exportXlsButton);

				jTextFieldUser = new JTextField4j(JDBUser.field_user_id);
				jTextFieldUser.setBounds(347, 105, 128, 22);
				jDesktopPane1.add(jTextFieldUser);

				jLabel_User = new JLabel4j_std();
				jLabel_User.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel_User.setText(lang.get("lbl_User_ID"));
				jLabel_User.setBounds(261, 105, 82, 22);
				jDesktopPane1.add(jLabel_User);

				jButtonClear = new JButton4j(Common.icon_clear_16x16);
				jButtonClear.addActionListener(new ActionListener()
				{
					public void actionPerformed(final ActionEvent e)
					{
						clearFilter();
					}
				});
				jButtonClear.setText(lang.get("btn_Clear_Filter"));
				jButtonClear.setBounds(144, 170, 140, 32);
				jDesktopPane1.add(jButtonClear);

				JLabel4j_std JLabel_Customer = new JLabel4j_std();
				JLabel_Customer.setText(lang.get("lbl_Customer_ID"));
				JLabel_Customer.setHorizontalAlignment(SwingConstants.TRAILING);
				JLabel_Customer.setBounds(1, 105, 99, 22);
				jDesktopPane1.add(JLabel_Customer);

				jTextFieldCustomer.setBounds(130, 105, 120, 22);
				jDesktopPane1.add(jTextFieldCustomer);

				JButton4j button4j = new JButton4j(Common.icon_lookup_16x16);
				button4j.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "";
						if (JLaunchLookup.customers())
						{
							jTextFieldCustomer.setText(JLaunchLookup.dlgResult);
						}
					}
				});
				button4j.setBounds(251, 105, 22, 22);
				jDesktopPane1.add(button4j);

				JLabel4j_std jLabel4j_MHN = new JLabel4j_std();
				jLabel4j_MHN.setText(lang.get("lbl_MHN_Number"));
				jLabel4j_MHN.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabel4j_MHN.setBounds(1, 136, 99, 22);
				jDesktopPane1.add(jLabel4j_MHN);

				textField4j_MHN = new JTextField4j(JDBMHN.field_mhn_number);
				textField4j_MHN.setBounds(130, 136, 120, 22);
				jDesktopPane1.add(textField4j_MHN);

				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						jTextFieldMaterial.requestFocus();
						jTextFieldMaterial.setCaretPosition(jTextFieldMaterial.getText().length());

					}
				});
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void interfaceLog()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			JLaunchMenu.runForm("FRM_ADMIN_INTERFACE_LOG", "MESSAGE REF", jTable1.getValueAt(row, 0).toString());
		}
	}

	private void populateList()
	{
		JDBPalletHistory pallethistory = new JDBPalletHistory(Common.selectedHostID, Common.sessionID);

		JDBPalletHistoryTableModel palletHistoryTable = new JDBPalletHistoryTableModel(pallethistory.getPalletHistoryDataResultSet(buildSQL(qSearch)));
		TableRowSorter<JDBPalletHistoryTableModel> sorter = new TableRowSorter<JDBPalletHistoryTableModel>(palletHistoryTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(palletHistoryTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
		jTable1.getColumnModel().getColumn(16).setPreferredWidth(70);

		if (expiryMode.equals("SSCC"))
		{
			jTable1.getColumnModel().getColumn(17).setPreferredWidth(120);
		}

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), palletHistoryTable.getRowCount());

	}

	private void print()
	{
		JLaunchReport.runReport("RPT_PALLET_HISTORY", null, "", buildSQL(qPrint), "");
	}

	private void print_summary()
	{
		jComboBoxSortBy.setSelectedItem("MATERIAL,PROCESS_ORDER");
		comboBoxTransactionType.setSelectedItem("PROD DEC");
		comboBoxTransactionSubtype.setSelectedItem("CONFIRM");
		JWait.milliSec(100);
		JLaunchReport.runReport("RPT_HIST_SUMMARY", null, "", buildSQL(qPrint), "");
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

	private void sortBy(String orderField)
	{
		jComboBoxSortBy.setSelectedItem(orderField);
		populateList();
	}

	public void updateSearch(String keyField, String keyValue)
	{
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

		if (keyField.equals("LOCATION"))
		{
			jTextFieldLocation.setText(keyValue);
		}

		populateList();
	}

	public void updateSearch(String keyField, String keyValue1, String keyValue2)
	{
		clearFilter();

		if (keyField.equals("MATERIAL-BATCH"))
		{
			jTextFieldMaterial.setText(keyValue1);
			jTextFieldBatch.setText(keyValue2);
		}

		populateList();
	}
}
