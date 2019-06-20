package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePalletAdmin.java
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
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.bar.JEANBarcode;
import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMHN;
import com.commander4j.db.JDBMHNDecisions;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBMaterialType;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBPallet;
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
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.messages.IncommingPalletImportXML;
import com.commander4j.messages.OutgoingPalletExportXML;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBPalletTableModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;

/**
 * The JInternalFramePalletAdmin allows you to search the APP_PALLET table which
 * contains a single record for each SSCC. Once a list of matching pallets has
 * been returned you can edit/delete or perform certain transactions on them.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFramePalletAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.app.JInternalFramePalletHistoryAdmin
 *      JInternalFramePalletHistoryAdmin
 * @see com.commander4j.db.JDBPalletHistory JDBPalletHistory
 */
public class JInternalFramePalletAdmin extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JCheckBox4j jCheckBoxConfirmed;
	private JLabel4j_std jLabel5_1;
	private JLabel4j_std jStatusText;
	private JTextField4j jTextFieldDespatch_No;
	private JLabel4j_std jLabel8_1;
	private static final long serialVersionUID = 1;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButtonMenuItem rbascending = new JRadioButtonMenuItem();
	private JRadioButtonMenuItem rbdescending = new JRadioButtonMenuItem();
	private static boolean dlg_sort_descending = false;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonSearch1;
	private JButton4j jButtonExport;
	private JButton4j jButtonImport;
	private JTextField4j jTextFieldLocation;
	private JLabel4j_std jLabel4;
	private JLabel4j_std jLabel23;
	private JButton4j jButtonLookupProcessOrder;
	private JCheckBox4j jCheckBoxLimit;
	private JSpinner jSpinnerLimit;
	private JLabel4j_std jLabel7;
	private JDateControl domDateTo;
	private JCheckBox4j jCheckBoxDOMTo;
	private JDateControl domDateFrom;
	private JCheckBox4j jCheckBoxQuantity;
	private JTextField4j jTextFieldVariant;
	private JLabel4j_std jLabel6;
	private JLabel4j_std jLabel5;
	private JTextField4j jTextFieldEAN;
	private JLabel4j_std jLabelProductionDate;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std jLabelProcessOrder;
	private JTextField4j jTextFieldBatch;
	private JLabel4j_std jLabel2;
	private JButton4j jButtonClose;
	private JButton4j jButtonLabel;
	private JButton4j jButtonLookupCustomer;
	private JCheckBox4j jCheckBoxExpiryTo;
	private JCheckBox4j jCheckBoxExpiryFrom;
	private JLabel4j_std jLabel8;
	private JDateControl expiryDateTo;
	private JDateControl expiryDateFrom;
	private JLabel4j_std jLabelSCC;
	private JTextField4j jTextFieldSSCC;
	private JTextField4j jTextFieldCustomer;
	private JButton4j jButtonLookupBatch;
	private JCheckBox4j jCheckBoxDOMFrom;
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
	private JButton4j jButtonSummary;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonAdd;
	private JComboBox4j<JDBUom> jComboBoxUOM;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private JTextField4j jTextFieldMaterial;
	private JTable4j jTable1;
	private JScrollPane jScrollPane1;
	private JDBUom u = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDBMHNDecisions d = new JDBMHNDecisions(Common.selectedHostID, Common.sessionID);
	private JDBMaterialType t = new JDBMaterialType(Common.selectedHostID, Common.sessionID);
	private Vector<JDBUom> uomList = new Vector<JDBUom>();
	private Vector<JDBMHNDecisions> decisionList = new Vector<JDBMHNDecisions>();
	private Vector<JDBMaterialType> typeList = new Vector<JDBMaterialType>();
	private String lsscc;
	private JMenuItem4j mntmEditProcessOrder;
	private JMenuItem4j mntmEditMHN;
	private JMenuItem4j mntmEditMaterial;
	private JMenuItem4j mntmEditBatch;
	private JMenuItem4j mntmEditLocation;
	private JMenu4j mnReferenceData;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JMenuItem4j menuItemSummary;
	private JCalendarButton button_CalendardomDateFrom;
	private JCalendarButton button_CalendardomDateTo;
	private JCalendarButton calendarButtonexpiryDateFrom;
	private JCalendarButton calendarButtonexpiryDateTo;
	private JTextField4j textFieldMHN;
	private JComboBox4j<JDBMHNDecisions> comboBoxDecisions = new JComboBox4j<JDBMHNDecisions>();
	private PreparedStatement listStatement;
	private TableModel jTable1Model = new DefaultTableModel(new String[][]
	{
			{ "One", "Two" },
			{ "Three", "Four" } }, new String[]
	{ "Column 1", "Column 2" });
	private JCheckBox4j jCheckBoxCreatedFrom;
	private JDateControl createdDateFrom;
	private JCalendarButton button_CalendarCreatedDateFrom;
	private JCalendarButton button_CalendarCreatedDateTo;
	private JCalendarButton button_CalendarUpdatedDateFrom;
	private JCalendarButton button_CalendarUpdatedDateTo;
	private JCheckBox4j jCheckBoxUpdatedFrom;
	private JDateControl updatedDateFrom;
	private JDateControl createdDateTo;
	private JCheckBox4j jCheckBoxCreatedTo;
	private JDateControl updatedDateTo;
	private JCheckBox4j jCheckBoxUpdatedTo;
	private JTextField4j textFieldUserCreated;
	private JTextField4j textFieldUserUpdated;
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JMenuItem4j menuItemEdit;

	private void app_Init()
	{
		getContentPane().setBackground(Color.WHITE);

		uomList.add(new JDBUom(Common.selectedHostID, Common.sessionID));
		uomList.addAll(u.getInternalUoms());

		decisionList.add(new JDBMHNDecisions(Common.selectedHostID, Common.sessionID));
		decisionList.addAll(d.getDecisions());

		typeList.add(new JDBMaterialType(Common.selectedHostID, Common.sessionID));
		typeList.addAll(t.getMaterialTypes());

		initGUI();

		////////
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("{schema}VIEW_PALLET_EXPIRY WHERE 1=2");
		q2.applyRestriction(false, 0);
		q2.applySort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		q2.applySQL();
		listStatement = q2.getPreparedStatement();
		////////

		populateList();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);
	}

	public JInternalFramePalletAdmin()
	{
		super();
		app_Init();
	}

	public JInternalFramePalletAdmin(String keyField, String keyValue)
	{
		super();
		app_Init();

		clearFilter();

		updateSearch(keyField, keyValue);

	}

	public JInternalFramePalletAdmin(String keyField, String keyValue1, String keyValue2)
	{
		super();
		app_Init();

		clearFilter();

		updateSearch(keyField, keyValue1, keyValue2);

	}

	public void updateSearch(String keyField, String keyValue)
	{
		clearFilter();

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

		buildSQL();
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

		buildSQL();
		populateList();
	}

	private PreparedStatement buildSQLr()
	{

		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("{schema}VIEW_PALLET_EXPIRY");

		if (jCheckBoxConfirmed.isSelected())
		{
			q2.applyWhere("confirmed=", 'Y');
		}
		else
		{
			q2.applyWhere("confirmed=", 'N');
		}

		if (jTextFieldSSCC.getText().equals("") == false)
		{
			q2.applyWhere("sscc = ", jTextFieldSSCC.getText());
		}

		if (textFieldMHN.getText().equals("") == false)
		{
			q2.applyWhere("mhn_number = ", textFieldMHN.getText());
		}

		if (jTextFieldMaterial.getText().equals("") == false)
		{
			q2.applyWhere("material = ", jTextFieldMaterial.getText());
		}

		if (jTextFieldCustomer.getText().equals("") == false)
		{
			q2.applyWhere("customer_id=", jTextFieldCustomer.getText());
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

		if (((JDBMHNDecisions) comboBoxDecisions.getSelectedItem()).getDecision().equals("") == false)
		{
			q2.applyWhere("decision = ", ((JDBMHNDecisions) comboBoxDecisions.getSelectedItem()).getDecision());
		}

		q2.applyWhere("uom=", ((JDBUom) jComboBoxUOM.getSelectedItem()).getInternalUom());

		q2.applyWhere("status=", ((String) jComboBoxPalletStatus.getSelectedItem()).toString());

		if (jCheckBoxQuantity.isSelected())
		{
			if (jFormattedTextFieldQuantity.getText().equals("") == false)
			{
				q2.applyWhere("quantity=", JUtility.stringToBigDecimal(jFormattedTextFieldQuantity.getText().toString()));
			}
		}

		if (jCheckBoxDOMFrom.isSelected())
		{
			q2.applyWhere("date_of_manufacture>=", JUtility.getTimestampFromDate(domDateFrom.getDate()));
		}

		if (jCheckBoxDOMTo.isSelected())
		{
			q2.applyWhere("date_of_manufacture<=", JUtility.getTimestampFromDate(domDateTo.getDate()));
		}

		if (jCheckBoxExpiryFrom.isSelected())
		{
			q2.applyWhere("expiry_date>=", JUtility.getTimestampFromDate(expiryDateFrom.getDate()));
		}

		if (jCheckBoxExpiryTo.isSelected())
		{
			q2.applyWhere("expiry_date<=", JUtility.getTimestampFromDate(expiryDateTo.getDate()));
		}

		if (jCheckBoxCreatedFrom.isSelected())
		{
			q2.applyWhere("date_created>=", JUtility.getTimestampFromDate(createdDateFrom.getDate()));
		}

		if (jCheckBoxCreatedTo.isSelected())
		{
			q2.applyWhere("date_created<=", JUtility.getTimestampFromDate(createdDateTo.getDate()));
		}

		if (jCheckBoxUpdatedFrom.isSelected())
		{
			q2.applyWhere("date_updated>=", JUtility.getTimestampFromDate(updatedDateFrom.getDate()));
		}

		if (jCheckBoxUpdatedFrom.isSelected())
		{
			q2.applyWhere("date_updated<=", JUtility.getTimestampFromDate(updatedDateTo.getDate()));
		}

		if (textFieldUserCreated.getText().equals("") == false)
		{
			q2.applyWhere("created_by_user_id = ", textFieldUserCreated.getText());
		}

		if (textFieldUserUpdated.getText().equals("") == false)
		{
			q2.applyWhere("updated_by_user_id = ", textFieldUserUpdated.getText());
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

	private void buildSQL()
	{

		JDBQuery2.closeStatement(listStatement);

		listStatement = buildSQLr();
	}

	private PreparedStatement buildSQL1Record()
	{
		PreparedStatement result;

		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("VIEW_PALLET_EXPIRY");

		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lsscc = jTable1.getValueAt(row, 0).toString();
		}
		else
		{
			lsscc = "";
		}

		if (lsscc.equals("") == false)
		{
			q2.applyWhere("sscc = ", lsscc);
		}

		q2.applyRestriction(false, 0);
		q2.applySQL();
		result = q2.getPreparedStatement();
		return result;
	}

	private void sortBy(String orderField)
	{
		jComboBoxSortBy.setSelectedItem(orderField);
		buildSQL();
		populateList();
	}

	private void splitRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lsscc = jTable1.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_PAL_SPLIT", lsscc);
		}
	}

	private void palletHistory()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lsscc = jTable1.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_ADMIN_PALLET_HISTORY", "SSCC", lsscc);
		}
	}

	private void interfaceLog()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lsscc = jTable1.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_ADMIN_INTERFACE_LOG", "SSCC", lsscc);
		}
	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lsscc = jTable1.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_ADMIN_PALLET_EDIT", lsscc);
		}
	}

	private void confirmRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lsscc = jTable1.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_PAL_PROD_CONFIRM", lsscc);
		}
	}

	private void addRecord()
	{
		String addSSCC = JUtility.replaceNullStringwithBlank((String) JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_SSCC_Add"), null, JOptionPane.QUESTION_MESSAGE, Common.icon_confirm, null, null));
		if (addSSCC.equals("") == false)
		{
			JEANBarcode bc = new JEANBarcode(Common.selectedHostID, Common.sessionID);
			if (addSSCC.toUpperCase().equals("AUTO"))
			{
				do
				{
					addSSCC = bc.generateNewSSCC();
				}
				while (addSSCC.equals(""));
			}

			if (bc.isValidSSCCformat(addSSCC))
			{
				JLaunchMenu.runForm("FRM_ADMIN_PALLET_EDIT", addSSCC);
			}
			else
			{
				JOptionPane.showMessageDialog(Common.mainForm, bc.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
			}
		}
	}

	private void deleteRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lsscc = jTable1.getValueAt(row, 0).toString();
			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_SSCC_Delete") + " " + lsscc + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (n == 0)
			{
				JDBPallet m = new JDBPallet(Common.selectedHostID, Common.sessionID);
				m.setSSCC(lsscc);
				m.getPalletProperties();

				boolean result = m.delete();
				if (result == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, m.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
				}
				else
				{
					buildSQL();
					populateList();
				}
			}
		}

	}

	private void printRecords(String mode)
	{
		if (mode.equals("multi") == true)
		{
			PreparedStatement temp = buildSQLr();
			JLaunchReport.runReport("RPT_PALLETS", null, "", temp, "");
		}
		else
		{
			int row = jTable1.getSelectedRow();
			if (row >= 0)
			{
				PreparedStatement temp = buildSQL1Record();
				JLaunchReport.runReport("RPT_PALLETS", null, "", temp, "");
			}
		}
	}

	private void print_summary()
	{
		jComboBoxSortBy.setSelectedItem("MATERIAL,PROCESS_ORDER");
		PreparedStatement temp = buildSQLr();
		JLaunchReport.runReport("RPT_PAL_SUMMARY", null, "", temp, "");
	}

	private void printLabels()
	{

		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lsscc = jTable1.getValueAt(row, 0).toString();
			// JLaunchMenu.runForm("FRM_PAL_LABEL_COPIES", lsscc);
			JLaunchMenu.runDialog("FRM_PAL_LABEL_COPIES", lsscc);
		}

	}

	private void clearFilter()
	{
		jTextFieldMaterial.setText("");
		jTextFieldBatch.setText("");
		jTextFieldSSCC.setText("");
		jTextFieldLocation.setText("");
		jTextFieldProcessOrder.setText("");
		jTextFieldDespatch_No.setText("");
		jTextFieldEAN.setText("");
		jTextFieldVariant.setText("");
		jComboBoxPalletStatus.setSelectedItem("");
		jTextFieldCustomer.setText("");
		jComboBoxUOM.setSelectedItem("");
		jCheckBoxDOMFrom.setSelected(false);
		jCheckBoxDOMTo.setSelected(false);
		jCheckBoxExpiryFrom.setSelected(false);
		jCheckBoxExpiryTo.setSelected(false);
		comboBoxDecisions.setSelectedIndex(0);
		jCheckBoxCreatedFrom.setSelected(false);
		jCheckBoxCreatedTo.setSelected(false);
		jCheckBoxUpdatedFrom.setSelected(false);
		jCheckBoxUpdatedTo.setSelected(false);
		textFieldUserCreated.setText("");
		textFieldUserUpdated.setText("");
		textFieldMHN.setText("");
		jComboBoxUOM.setSelectedIndex(0);
	}

	private void exportExcel(String mode)
	{
		JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.setExcelRowLimit(jCheckBoxLimit, jSpinnerLimit);
		if (mode.equals("multi") == true)
		{
			PreparedStatement temp = buildSQLr();
			export.saveAs("pallet.xls", pallet.getPalletDataResultSet(temp), Common.mainForm);
		}
		else
		{
			int row = jTable1.getSelectedRow();
			if (row >= 0)
			{
				PreparedStatement temp = buildSQL1Record();
				export.saveAs("pallet.xls", pallet.getPalletDataResultSet(temp), Common.mainForm);
			}
		}
		populateList();
	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			if (fieldname.equals("SSCC") == true)
			{
				jTextFieldSSCC.setText(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals("Material") == true)
			{
				jTextFieldMaterial.setText(jTable1.getValueAt(row, 1).toString());
			}

			if (fieldname.equals("Batch") == true)
			{
				jTextFieldBatch.setText(jTable1.getValueAt(row, 2).toString());
			}

			if (fieldname.equals("Process Order") == true)
			{
				jTextFieldProcessOrder.setText(jTable1.getValueAt(row, 3).toString());
			}

			if (fieldname.equals("Pallet Status") == true)
			{
				jComboBoxPalletStatus.setSelectedItem(jTable1.getValueAt(row, 7).toString());
			}

			if (fieldname.equals("Location") == true)
			{
				jTextFieldLocation.setText(jTable1.getValueAt(row, 8).toString());
			}

			if (fieldname.equals("Despatch") == true)
			{
				jTextFieldDespatch_No.setText(jTable1.getValueAt(row, 11).toString());
			}

			buildSQL();
			populateList();

		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(979, 535));
			this.setBounds(0, 0, 994, 646);
			setVisible(true);
			this.setClosable(true);
			getContentPane().setLayout(null);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBounds(0, 0, 990, 614);
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(917, 504));
				jDesktopPane1.setLayout(null);
				{
					jButtonExport = new JButton4j(Common.icon_export);
					jDesktopPane1.add(jButtonExport);
					jButtonExport.setText(lang.get("btn_Export"));
					jButtonExport.setBounds(166, 260, 162, 32);
					jButtonExport.setMnemonic(lang.getMnemonicChar());
					jButtonExport.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EXPORT"));
					jButtonExport.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							int maxrows = Integer.valueOf(ctrl.getKeyValueWithDefault("PALLET EXPORT MAX ROWS", "100", "Max rows which can be exported to XML"));
							jCheckBoxLimit.setSelected(true);
							jSpinnerLimit.setValue(maxrows);
							buildSQL();
							OutgoingPalletExportXML export = new OutgoingPalletExportXML(Common.selectedHostID, Common.sessionID);
							export.saveAs(ctrl.getKeyValue("DEFAULT_LOCATION") + "_Pallet_Export.xml", listStatement, Common.mainForm, jStatusText);
						}
					});
				}
				{
					jButtonImport = new JButton4j(Common.icon_open);
					jDesktopPane1.add(jButtonImport);
					jButtonImport.setText(lang.get("btn_Import"));
					jButtonImport.setBounds(2, 260, 162, 32);
					jButtonImport.setMnemonic(lang.getMnemonicChar());
					jButtonImport.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_IMPORT"));
					jButtonImport.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							IncommingPalletImportXML import1 = new IncommingPalletImportXML(Common.selectedHostID, Common.sessionID);
							import1.loadFrom("Pallet_Export.xml", Common.mainForm, jStatusText);
						}
					});
				}
				{
					jButtonSearch1 = new JButton4j(Common.icon_search_16x16);
					jDesktopPane1.add(jButtonSearch1);
					jButtonSearch1.setText(lang.get("btn_Search"));
					jButtonSearch1.setBounds(2, 226, 162, 32);
					jButtonSearch1.setMnemonic(lang.getMnemonicChar());
					jButtonSearch1.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							buildSQL();
							populateList();
						}
					});
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(122, 11, 121, 22);
				}
				{
					jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(jTextFieldLocation);
					jTextFieldLocation.setBounds(340, 11, 104, 22);
				}
				{
					jTextFieldCustomer = new JTextField4j(JDBCustomer.field_customer_id);
					jDesktopPane1.add(jTextFieldCustomer);
					jTextFieldCustomer.setBounds(340, 105, 104, 22);
				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setBounds(2, 11, 108, 21);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Location_ID"));
					jLabel3.setBounds(260, 11, 74, 21);
				}
				{
					jLabel23 = new JLabel4j_std();
					jLabel23.setHorizontalAlignment(SwingConstants.TRAILING);
					jDesktopPane1.add(jLabel23);
					jLabel23.setText(lang.get("lbl_Customer_ID"));
					jLabel23.setBounds(243, 105, 91, 21);
				}
				{
					ComboBoxModel<JDBUom> jComboBox2Model = new DefaultComboBoxModel<JDBUom>(uomList);
					jComboBoxUOM = new JComboBox4j<JDBUom>();
					jDesktopPane1.add(jComboBoxUOM);
					jComboBoxUOM.setModel(jComboBox2Model);
					jComboBoxUOM.setBounds(582, 75, 135, 23);
					jComboBoxUOM.setMaximumRowCount(12);
				}
				{
					jLabel4 = new JLabel4j_std();
					jDesktopPane1.add(jLabel4);
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel4.setText(lang.get("lbl_Material_UOM"));
					jLabel4.setBounds(490, 75, 88, 21);
					jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setBounds(330, 226, 162, 32);
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_ADD"));
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							addRecord();
						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setBounds(494, 226, 162, 32);
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT"));
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setBounds(658, 226, 162, 32);
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_DELETE"));
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							deleteRecord();
						}
					});
				}
				{
					jButtonSummary = new JButton4j(Common.icon_report_16x16);
					jButtonSummary.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							print_summary();
						}
					});
					jDesktopPane1.add(jButtonSummary);
					jButtonSummary.setText(lang.get("btn_Print_Summary"));
					jButtonSummary.setBounds(494, 260, 162, 32);
					jButtonSummary.setMnemonic(lang.getMnemonicChar());
					jButtonSummary.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PAL_SUMMARY"));
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(822, 260, 162, 32);
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JDBQuery2.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setBounds(658, 260, 162, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PALLETS"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							printRecords("multi");

						}
					});
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Limit"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(702, 200, 97, 21);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[]
					{ "DATE_OF_MANUFACTURE", "DATE_CREATED", "DATE_UPDATED", "SSCC", "MATERIAL,BATCH_NUMBER", "MATERIAL,PROCESS_ORDER", "BATCH_NUMBER,MATERIAL", "PROCESS_ORDER,DATE_OF_MANUFACTURE", "QUANTITY", "STATUS", "LOCATION_ID", "UOM", "EAN",
							"VARIANT" });
					jComboBoxSortBy = new JComboBox4j<String>();
					jComboBoxSortBy.setMaximumRowCount(15);
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					jComboBoxSortBy.setBounds(340, 196, 355, 22);
				}
				{
					ComboBoxModel<String> jComboBoxDefaultPalletStatusModel = new DefaultComboBoxModel<String>(Common.palletStatusIncBlank);
					jComboBoxPalletStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxPalletStatus);
					jComboBoxPalletStatus.setModel(jComboBoxDefaultPalletStatusModel);
					jComboBoxPalletStatus.setBounds(805, 11, 169, 23);
				}
				{
					jLabel15 = new JLabel4j_std();
					jDesktopPane1.add(jLabel15);
					jLabel15.setText(lang.get("lbl_Pallet_Status"));
					jLabel15.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel15.setBounds(702, 11, 97, 21);
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jToggleButtonSequence.setSelected(true);
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(696, 198, 21, 21);
					jToggleButtonSequence.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}
				{
					jLabelQuantity = new JLabel4j_std();
					jDesktopPane1.add(jLabelQuantity);
					jLabelQuantity.setText(lang.get("lbl_Pallet_Quantity"));
					jLabelQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelQuantity.setBounds(490, 105, 88, 21);
				}
				{

					jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
					jDesktopPane1.add(jFormattedTextFieldQuantity);
					jFormattedTextFieldQuantity.setFont(Common.font_std);
					jFormattedTextFieldQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
					jFormattedTextFieldQuantity.setBounds(604, 105, 108, 22);
					jFormattedTextFieldQuantity.setVerifyInputWhenFocusTarget(false);
					jFormattedTextFieldQuantity.setEnabled(false);
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Material_Batch"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2.setBounds(490, 11, 88, 21);
				}
				{
					jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jDesktopPane1.add(jTextFieldBatch);
					jTextFieldBatch.setBounds(582, 11, 115, 22);
				}
				{
					jLabelProcessOrder = new JLabel4j_std();
					jDesktopPane1.add(jLabelProcessOrder);
					jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
					jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelProcessOrder.setBounds(490, 44, 88, 21);
				}
				{
					jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setBounds(582, 44, 135, 22);
				}
				{
					jLabelProductionDate = new JLabel4j_std();
					jDesktopPane1.add(jLabelProductionDate);
					jLabelProductionDate.setText(lang.get("lbl_Pallet_DOM"));
					jLabelProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelProductionDate.setBounds(2, 42, 108, 25);
				}
				{
					jTextFieldEAN = new JTextField4j(JDBMaterialUom.field_ean);
					jDesktopPane1.add(jTextFieldEAN);
					jTextFieldEAN.setBounds(805, 75, 117, 22);
					jTextFieldEAN.setFocusCycleRoot(true);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Material_UOM_EAN"));
					jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel5.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel5.setBounds(702, 75, 97, 21);
				}
				{
					jLabel6 = new JLabel4j_std();
					jDesktopPane1.add(jLabel6);
					jLabel6.setText("/");
					jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
					jLabel6.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel6.setBounds(922, 75, 15, 21);
				}
				{
					jTextFieldVariant = new JTextField4j(JDBMaterialUom.field_variant);
					jTextFieldVariant.setHorizontalAlignment(SwingConstants.CENTER);
					jDesktopPane1.add(jTextFieldVariant);
					jTextFieldVariant.setBounds(937, 75, 25, 22);
					jTextFieldVariant.setFocusCycleRoot(true);
				}
				{
					jCheckBoxQuantity = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxQuantity);
					jCheckBoxQuantity.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxQuantity.setBounds(582, 105, 21, 21);
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
				}
				{
					domDateFrom = new JDateControl();
					jDesktopPane1.add(domDateFrom);
					domDateFrom.setEnabled(false);
					domDateFrom.setBounds(144, 40, 120, 25);

				}
				{
					jCheckBoxDOMTo = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxDOMTo);
					jCheckBoxDOMTo.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxDOMTo.setBounds(313, 40, 21, 25);
					jCheckBoxDOMTo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxDOMTo.isSelected())
							{
								button_CalendardomDateTo.setEnabled(true);
								domDateTo.setEnabled(true);
							}
							else
							{
								domDateTo.setEnabled(false);
								button_CalendardomDateTo.setEnabled(false);
							}
						}
					});
				}
				{
					domDateTo = new JDateControl();
					jDesktopPane1.add(domDateTo);
					domDateTo.setEnabled(false);
					domDateTo.setBounds(340, 40, 120, 25);
				}
				{
					jLabel7 = new JLabel4j_std();
					jDesktopPane1.add(jLabel7);
					jLabel7.setText(lang.get("lbl_Sort_By"));
					jLabel7.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel7.setBounds(265, 198, 69, 21);
				}
				{
					SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
					jSpinnerIntModel.setMinimum(1);
					jSpinnerIntModel.setMaximum(5000);
					jSpinnerIntModel.setStepSize(1);
					jSpinnerLimit = new JSpinner();
					JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
					ne.getTextField().setFont(Common.font_std);
					jSpinnerLimit.setEditor(ne);
					jSpinnerLimit.setModel(jSpinnerIntModel);
					jSpinnerLimit.setBounds(824, 200, 68, 21);
					jSpinnerLimit.setValue(1000);
					jSpinnerLimit.getEditor().setSize(45, 21);
					jDesktopPane1.add(jSpinnerLimit);
				}
				{
					jCheckBoxLimit = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxLimit);
					jCheckBoxLimit.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxLimit.setBounds(801, 200, 21, 21);
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
				}
				{
					jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupProcessOrder);
					jButtonLookupProcessOrder.setBounds(680, 44, 21, 21);
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
					jButtonLookupBatch = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupBatch);

					jButtonLookupBatch.setBounds(696, 11, 21, 21);
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
				}
				{
					jButtonLookupMaterial = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupMaterial);
					jButtonLookupMaterial.setBounds(243, 11, 21, 21);
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
				}
				{
					jButtonLookupLocation = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupLocation);
					jButtonLookupLocation.setBounds(444, 11, 21, 21);
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
				}
				{
					jButtonLookupCustomer = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupCustomer);
					jButtonLookupCustomer.setBounds(444, 105, 21, 21);
					jButtonLookupCustomer.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.customers())
							{
								jTextFieldCustomer.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jCheckBoxDOMFrom = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxDOMFrom);
					jCheckBoxDOMFrom.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxDOMFrom.setBounds(120, 40, 21, 25);
					jCheckBoxDOMFrom.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxDOMFrom.isSelected())
							{
								domDateFrom.setEnabled(true);
								button_CalendardomDateFrom.setEnabled(true);
							}
							else
							{
								domDateFrom.setEnabled(false);
								button_CalendardomDateFrom.setEnabled(false);
							}
						}
					});
				}
				{
					jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
					jDesktopPane1.add(jTextFieldSSCC);
					jTextFieldSSCC.setBounds(122, 105, 120, 22);
				}
				{
					jLabelSCC = new JLabel4j_std();
					jDesktopPane1.add(jLabelSCC);
					jLabelSCC.setText(lang.get("lbl_Pallet_SSCC"));
					jLabelSCC.setBounds(2, 105, 108, 21);
					jLabelSCC.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					expiryDateFrom = new JDateControl();
					jDesktopPane1.add(expiryDateFrom);
					expiryDateFrom.setEnabled(false);
					expiryDateFrom.setBounds(144, 71, 120, 25);
				}
				{
					expiryDateTo = new JDateControl();
					jDesktopPane1.add(expiryDateTo);
					expiryDateTo.setEnabled(false);
					expiryDateTo.setBounds(340, 71, 120, 25);
				}
				{
					jLabel8 = new JLabel4j_std();
					jDesktopPane1.add(jLabel8);
					jLabel8.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
					jLabel8.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel8.setBounds(2, 71, 108, 25);
				}
				{
					jCheckBoxExpiryFrom = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxExpiryFrom);
					jCheckBoxExpiryFrom.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxExpiryFrom.setBounds(120, 71, 21, 25);
					jCheckBoxExpiryFrom.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxExpiryFrom.isSelected())
							{
								expiryDateFrom.setEnabled(true);
								calendarButtonexpiryDateFrom.setEnabled(true);
							}
							else
							{
								expiryDateFrom.setEnabled(false);
								calendarButtonexpiryDateFrom.setEnabled(false);
							}
						}
					});
				}
				{
					jCheckBoxExpiryTo = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxExpiryTo);
					jCheckBoxExpiryTo.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxExpiryTo.setBounds(313, 71, 21, 25);
					jCheckBoxExpiryTo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxExpiryTo.isSelected())
							{
								expiryDateTo.setEnabled(true);
								calendarButtonexpiryDateTo.setEnabled(true);
							}
							else
							{
								expiryDateTo.setEnabled(false);
								calendarButtonexpiryDateTo.setEnabled(false);
							}
						}
					});
				}
				{
					jButtonLabel = new JButton4j(Common.icon_label_16x16);
					jButtonLabel.setToolTipText("Print labels for all returned rows below");
					jDesktopPane1.add(jButtonLabel);
					jButtonLabel.setText(lang.get("btn_Label"));
					jButtonLabel.setMnemonic(java.awt.event.KeyEvent.VK_L);
					jButtonLabel.setBounds(330, 260, 162, 32);
					jButtonLabel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							printLabels();
						}
					});
				}

				{
					jLabel8_1 = new JLabel4j_std();
					jLabel8_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel8_1.setText(lang.get("lbl_Despatch_No"));
					jLabel8_1.setBounds(702, 44, 97, 21);
					jDesktopPane1.add(jLabel8_1);
				}

				{
					jTextFieldDespatch_No = new JTextField4j(JDBDespatch.field_despatch_no);
					jTextFieldDespatch_No.setFocusCycleRoot(true);
					jTextFieldDespatch_No.setBounds(805, 44, 117, 22);
					jDesktopPane1.add(jTextFieldDespatch_No);
				}

				{
					final JButton4j exportXlsButton = new JButton4j(Common.icon_XLS_16x16);
					exportXlsButton.setToolTipText("Export all rows below to spreadsheet.");
					exportXlsButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							exportExcel("multi");
						}
					});
					exportXlsButton.setText(lang.get("btn_Excel"));
					exportXlsButton.setBounds(822, 226, 162, 32);
					jDesktopPane1.add(exportXlsButton);
				}

				{
					jLabel5_1 = new JLabel4j_std();
					jLabel5_1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel5_1.setText(lang.get("lbl_Confirmed"));
					jLabel5_1.setBounds(702, 167, 97, 21);
					jDesktopPane1.add(jLabel5_1);
				}

				{
					jCheckBoxConfirmed = new JCheckBox4j();
					jCheckBoxConfirmed.setSelected(true);
					jCheckBoxConfirmed.setBackground(Color.WHITE);
					jCheckBoxConfirmed.setBounds(801, 167, 21, 21);
					jDesktopPane1.add(jCheckBoxConfirmed);
				}

				{
					jButtonClear = new JButton4j(Common.icon_clear);
					jButtonClear.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							clearFilter();

						}
					});
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jButtonClear.setBounds(166, 226, 162, 32);
					jDesktopPane1.add(jButtonClear);
				}

				button_CalendardomDateFrom = new JCalendarButton(domDateFrom);
				button_CalendardomDateFrom.setSize(21, 21);
				button_CalendardomDateFrom.setEnabled(false);
				button_CalendardomDateFrom.setLocation(274, 42);
				jDesktopPane1.add(button_CalendardomDateFrom);

				button_CalendardomDateTo = new JCalendarButton(domDateTo);
				button_CalendardomDateTo.setSize(21, 21);
				button_CalendardomDateTo.setEnabled(false);
				button_CalendardomDateTo.setLocation(470, 42);
				jDesktopPane1.add(button_CalendardomDateTo);

				calendarButtonexpiryDateFrom = new JCalendarButton(expiryDateFrom);
				calendarButtonexpiryDateFrom.setSize(21, 21);
				calendarButtonexpiryDateFrom.setEnabled(false);
				calendarButtonexpiryDateFrom.setLocation(274, 75);
				jDesktopPane1.add(calendarButtonexpiryDateFrom);

				calendarButtonexpiryDateTo = new JCalendarButton(expiryDateTo);
				calendarButtonexpiryDateTo.setSize(21, 21);
				calendarButtonexpiryDateTo.setEnabled(false);
				calendarButtonexpiryDateTo.setLocation(470, 74);
				jDesktopPane1.add(calendarButtonexpiryDateTo);

				JLabel4j_std label = new JLabel4j_std();
				label.setText(lang.get("lbl_MHN_Number"));
				label.setHorizontalAlignment(SwingConstants.TRAILING);
				label.setBounds(702, 105, 97, 21);
				jDesktopPane1.add(label);

				textFieldMHN = new JTextField4j(JDBMHN.field_mhn_number);
				textFieldMHN.setBounds(805, 105, 117, 22);
				jDesktopPane1.add(textFieldMHN);

				JLabel4j_std label_1 = new JLabel4j_std();
				label_1.setText(lang.get("lbl_Decision"));
				label_1.setHorizontalAlignment(SwingConstants.TRAILING);
				label_1.setBounds(702, 134, 97, 21);
				jDesktopPane1.add(label_1);

				ComboBoxModel<JDBMHNDecisions> jComboBox3Model = new DefaultComboBoxModel<JDBMHNDecisions>(decisionList);
				comboBoxDecisions.setModel(jComboBox3Model);
				comboBoxDecisions.setMaximumRowCount(12);
				comboBoxDecisions.setBounds(805, 134, 169, 23);
				jDesktopPane1.add(comboBoxDecisions);
			}

			{
				jStatusText = new JLabel4j_std();
				jStatusText.setBounds(0, 593, 983, 21);
				jDesktopPane1.add(jStatusText);
				jStatusText.setBackground(Color.GRAY);
				jStatusText.setForeground(new Color(255, 0, 0));
				jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(0, 300, 984, 290);
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
			jTable1 = new JTable4j();

			jScrollPane1.setViewportView(jTable1);
			jTable1.setModel(jTable1Model);

			jTable1.setToolTipText(lang.get("lbl_Table_Hint"));
			jTable1.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent evt)
				{
					if (jTable1.getSelectedRowCount() == 1)
					{
						jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT"));
						menuItemEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT"));
					}
					else
					{
						jButtonEdit.setEnabled(false);
						menuItemEdit.setEnabled(false);
					}

					if (evt.getClickCount() == 2)
					{
						if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_EDIT") == true)
						{
							editRecord();
						}
					}
				}
			});

			{
				final JPopupMenu popupMenu = new JPopupMenu();
				popupMenu.setFont(Common.font_popup);
				addPopup(jTable1, popupMenu);

				{
					final JMenuItem4j menuItemAdd = new JMenuItem4j(Common.icon_add);
					menuItemAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_ADD"));
					menuItemAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							addRecord();
						}
					});
					menuItemAdd.setText(lang.get("btn_Add"));
					popupMenu.add(menuItemAdd);
				}

				{
					menuItemEdit = new JMenuItem4j(Common.icon_edit);
					menuItemEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT"));
					menuItemEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							editRecord();
						}
					});
					menuItemEdit.setText(lang.get("btn_Edit"));
					popupMenu.add(menuItemEdit);
				}

				{
					final JMenuItem4j menuItemDelete = new JMenuItem4j(Common.icon_delete);
					menuItemDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_DELETE"));
					menuItemDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							deleteRecord();
						}
					});
					menuItemDelete.setText(lang.get("btn_Delete"));
					popupMenu.add(menuItemDelete);
				}

				{
					final JMenuItem4j menuItemPrint = new JMenuItem4j(Common.icon_print_16x16);
					menuItemPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PALLETS"));
					menuItemPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							printRecords("selected");
						}
					});
					menuItemPrint.setText(lang.get("btn_Print"));
					popupMenu.add(menuItemPrint);
				}

				{
					final JMenuItem4j menuItemLabel = new JMenuItem4j(Common.icon_label_16x16);
					menuItemLabel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							printLabels();
						}
					});
					menuItemLabel.setText(lang.get("btn_Label"));
					popupMenu.add(menuItemLabel);
				}

				{
					final JMenuItem4j menuItemExcel = new JMenuItem4j(Common.icon_XLS_16x16);
					menuItemExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							exportExcel("selected");
						}
					});
					menuItemExcel.setText(lang.get("btn_Excel"));
					popupMenu.add(menuItemExcel);
				}

				{
					final JMenuItem4j menuItemConfirm = new JMenuItem4j(Common.icon_scanner_16x16);
					menuItemConfirm.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_PROD_CONFIRM"));
					menuItemConfirm.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							confirmRecord();
						}
					});

					menuItemConfirm.setText(lang.get("btn_Confirm"));
					popupMenu.add(menuItemConfirm);
				}
				{
					menuItemSummary = new JMenuItem4j(Common.icon_report_16x16);
					menuItemSummary.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							print_summary();
						}
					});
					menuItemSummary.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PAL_SUMMARY"));
					menuItemSummary.setText(lang.get("btn_Print_Summary"));
					popupMenu.add(menuItemSummary);
				}
				{
					final JMenuItem4j menuItemSplit = new JMenuItem4j(Common.icon_split);
					menuItemSplit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_SPLIT"));
					menuItemSplit.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							splitRecord();
						}
					});
					menuItemSplit.setText(lang.get("mod_FRM_PAL_SPLIT"));
					popupMenu.add(menuItemSplit);
				}
				{
					final JMenuItem4j menuItemMove = new JMenuItem4j(Common.icon_move);
					menuItemMove.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_MOVE"));
					menuItemMove.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							palletMove();
						}
					});
					menuItemMove.setText(lang.get("lbl_Pallet_Move"));
					popupMenu.add(menuItemMove);
				}
				{
					final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_history);
					newItemMenuItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							palletHistory();
						}
					});
					newItemMenuItem.setText(lang.get("mod_FRM_ADMIN_PALLET_HISTORY"));
					newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_HISTORY"));
					popupMenu.add(newItemMenuItem);
				}
				{
					final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_interface);
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
				{

					mnReferenceData = new JMenu4j(lang.get("lbl_Referenced_Data"));
					popupMenu.add(mnReferenceData);
					{
						mntmEditBatch = new JMenuItem4j(lang.get("btn_Edit_Batch"));
						mntmEditBatch.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent arg0)
							{
								int row = jTable1.getSelectedRow();
								if (row >= 0)
								{
									String lmaterial = jTable1.getValueAt(row, 1).toString();
									String lbatch = jTable1.getValueAt(row, 2).toString();
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
						mntmEditLocation.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent arg0)
							{
								int row = jTable1.getSelectedRow();
								if (row >= 0)
								{
									String llocation = jTable1.getValueAt(row, 8).toString();
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
						mntmEditMaterial.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent arg0)
							{
								int row = jTable1.getSelectedRow();
								if (row >= 0)
								{
									String lmaterial = jTable1.getValueAt(row, 1).toString();
									JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_EDIT", lmaterial);
								}
							}
						});
						mnReferenceData.add(mntmEditMaterial);
						mntmEditMaterial.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_EDIT"));
						mntmEditMaterial.setIcon(Common.icon_material);
					}
					{
						mntmEditProcessOrder = new JMenuItem4j(lang.get("btn_Edit_Process_Order"));
						mntmEditProcessOrder.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent arg0)
							{
								int row = jTable1.getSelectedRow();
								if (row >= 0)
								{
									String lprocessorder = jTable1.getValueAt(row, 3).toString();
									JLaunchMenu.runForm("FRM_ADMIN_PROCESS_ORDER_EDIT", lprocessorder);
								}
							}
						});
						mnReferenceData.add(mntmEditProcessOrder);
						mntmEditProcessOrder.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PROCESS_ORDER_EDIT"));
						mntmEditProcessOrder.setIcon(Common.icon_process_order);
					}
					{
						mntmEditMHN = new JMenuItem4j(lang.get("btn_Edit_MHN"));
						mntmEditMHN.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent arg0)
							{
								int row = jTable1.getSelectedRow();
								if (row >= 0)
								{
									String lmhn = jTable1.getValueAt(row, JDBPalletTableModel.MHN_Number_Col).toString();
									if (lmhn.equals("") == false)
									{
										JLaunchMenu.runForm("FRM_ADMIN_MHN_EDIT", lmhn);
									}
								}
							}
						});
						mnReferenceData.add(mntmEditMHN);
						mntmEditMHN.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_EDIT"));
						mntmEditMHN.setIcon(Common.icon_mhn);
					}
				}

				{
					final JMenu4j filterByMenu = new JMenu4j();
					filterByMenu.setText(lang.get("lbl_Filter_By"));
					popupMenu.add(filterByMenu);

					{
						final JMenuItem4j menuItemFilterBySSCC = new JMenuItem4j();
						menuItemFilterBySSCC.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								filterBy("SSCC");
							}
						});
						menuItemFilterBySSCC.setText(lang.get("lbl_Pallet_SSCC"));
						filterByMenu.add(menuItemFilterBySSCC);
					}

					{
						final JMenuItem4j menuItemFilterByMaterial = new JMenuItem4j();
						menuItemFilterByMaterial.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								filterBy("Material");
							}
						});
						menuItemFilterByMaterial.setText(lang.get("lbl_Material"));
						filterByMenu.add(menuItemFilterByMaterial);
					}

					{
						final JMenuItem4j menuItemFilterByBatch = new JMenuItem4j();
						menuItemFilterByBatch.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								filterBy("Batch");
							}
						});
						menuItemFilterByBatch.setText(lang.get("lbl_Material_Batch"));
						filterByMenu.add(menuItemFilterByBatch);
					}

					{
						final JMenuItem4j menuItemFilterByLocation = new JMenuItem4j();
						menuItemFilterByLocation.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								filterBy("Location");
							}
						});
						menuItemFilterByLocation.setText(lang.get("lbl_Location_ID"));
						filterByMenu.add(menuItemFilterByLocation);
					}

					{
						final JMenuItem4j menuItemFilterByDespatch = new JMenuItem4j();
						menuItemFilterByDespatch.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								filterBy("Despatch");
							}
						});
						menuItemFilterByDespatch.setText(lang.get("lbl_Despatch_No"));
						filterByMenu.add(menuItemFilterByDespatch);
					}

					{
						final JMenuItem4j menuItemFilterByPalletStatus = new JMenuItem4j();
						menuItemFilterByPalletStatus.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								filterBy("Pallet Status");
							}
						});
						menuItemFilterByPalletStatus.setText(lang.get("lbl_Pallet_Status"));
						filterByMenu.add(menuItemFilterByPalletStatus);
					}

					{
						final JMenuItem4j menuItemFilterByProcessOrder = new JMenuItem4j();
						menuItemFilterByProcessOrder.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								filterBy("Process Order");
							}
						});
						menuItemFilterByProcessOrder.setText(lang.get("lbl_Process_Order"));
						filterByMenu.add(menuItemFilterByProcessOrder);
					}

					{
						filterByMenu.addSeparator();
					}

					{
						final JMenuItem4j menuItemResetFilter = new JMenuItem4j();
						menuItemResetFilter.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								clearFilter();
							}
						});
						menuItemResetFilter.setText(lang.get("btn_Clear_Filter"));
						filterByMenu.add(menuItemResetFilter);
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
								sortBy("DATE_OF_MANUFACTURE");
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

					{
						final JMenu4j orderMenu = new JMenu4j();
						orderMenu.setText(lang.get("lbl_Sort_By"));
						sortByMenu.add(orderMenu);

						{

							rbascending.addActionListener(new ActionListener()
							{
								public void actionPerformed(final ActionEvent e)
								{
									setSequence(false);
								}
							});
							buttonGroup.add(rbascending);
							rbascending.setSelected(true);
							rbascending.setText("Ascending");
							rbascending.setFont(Common.font_popup);
							orderMenu.add(rbascending);
						}

						{

							rbdescending.addActionListener(new ActionListener()
							{
								public void actionPerformed(final ActionEvent e)
								{
									setSequence(true);
								}
							});
							buttonGroup.add(rbdescending);
							rbdescending.setText("Descending");
							rbdescending.setFont(Common.font_popup);
							orderMenu.add(rbdescending);
						}
					}
				}
			}

			JLabel4j_std jLabelCreatedDate = new JLabel4j_std();
			jLabelCreatedDate.setText(lang.get("lbl_Date_Created"));
			jLabelCreatedDate.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelCreatedDate.setBounds(2, 134, 108, 21);
			jDesktopPane1.add(jLabelCreatedDate);

			JLabel4j_std jLabelUpdatedDate = new JLabel4j_std();
			jLabelUpdatedDate.setText(lang.get("lbl_Date_Updated"));
			jLabelUpdatedDate.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelUpdatedDate.setBounds(2, 163, 108, 25);
			jDesktopPane1.add(jLabelUpdatedDate);

			jCheckBoxCreatedFrom = new JCheckBox4j();
			jCheckBoxCreatedFrom.setBackground(Color.WHITE);
			jCheckBoxCreatedFrom.setBounds(120, 134, 21, 25);
			jDesktopPane1.add(jCheckBoxCreatedFrom);
			jCheckBoxCreatedFrom.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					if (jCheckBoxCreatedFrom.isSelected())
					{
						createdDateFrom.setEnabled(true);
						button_CalendarCreatedDateFrom.setEnabled(true);
					}
					else
					{
						createdDateFrom.setEnabled(false);
						button_CalendarCreatedDateFrom.setEnabled(false);
					}
				}
			});

			jCheckBoxUpdatedFrom = new JCheckBox4j();
			jCheckBoxUpdatedFrom.setBackground(Color.WHITE);
			jCheckBoxUpdatedFrom.setBounds(120, 163, 21, 25);
			jDesktopPane1.add(jCheckBoxUpdatedFrom);
			jCheckBoxUpdatedFrom.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					if (jCheckBoxUpdatedFrom.isSelected())
					{
						updatedDateFrom.setEnabled(true);
						button_CalendarUpdatedDateFrom.setEnabled(true);
					}
					else
					{
						updatedDateFrom.setEnabled(false);
						button_CalendarUpdatedDateFrom.setEnabled(false);
					}
				}
			});

			createdDateFrom = new JDateControl();
			createdDateFrom.setEnabled(false);
			createdDateFrom.setBounds(144, 134, 128, 25);
			jDesktopPane1.add(createdDateFrom);

			updatedDateFrom = new JDateControl();
			updatedDateFrom.setEnabled(false);
			updatedDateFrom.setBounds(144, 163, 128, 25);
			jDesktopPane1.add(updatedDateFrom);

			jCheckBoxCreatedTo = new JCheckBox4j();
			jCheckBoxCreatedTo.setBackground(Color.WHITE);
			jCheckBoxCreatedTo.setBounds(313, 134, 21, 25);
			jDesktopPane1.add(jCheckBoxCreatedTo);
			jCheckBoxCreatedTo.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					if (jCheckBoxCreatedTo.isSelected())
					{
						createdDateTo.setEnabled(true);
						button_CalendarCreatedDateTo.setEnabled(true);
					}
					else
					{
						createdDateTo.setEnabled(false);
						button_CalendarCreatedDateTo.setEnabled(false);
					}
				}
			});

			jCheckBoxUpdatedTo = new JCheckBox4j();
			jCheckBoxUpdatedTo.setBackground(Color.WHITE);
			jCheckBoxUpdatedTo.setBounds(313, 163, 21, 25);
			jDesktopPane1.add(jCheckBoxUpdatedTo);
			jCheckBoxUpdatedTo.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					if (jCheckBoxUpdatedTo.isSelected())
					{
						updatedDateTo.setEnabled(true);
						button_CalendarUpdatedDateTo.setEnabled(true);
					}
					else
					{
						createdDateTo.setEnabled(false);
						button_CalendarUpdatedDateTo.setEnabled(false);
					}
				}
			});

			createdDateTo = new JDateControl();
			createdDateTo.setEnabled(false);
			createdDateTo.setBounds(340, 134, 128, 25);
			jDesktopPane1.add(createdDateTo);

			updatedDateTo = new JDateControl();
			updatedDateTo.setEnabled(false);
			updatedDateTo.setBounds(340, 163, 128, 25);
			jDesktopPane1.add(updatedDateTo);

			JLabel4j_std label4j_std_2 = new JLabel4j_std();
			label4j_std_2.setText((String) null);
			label4j_std_2.setHorizontalAlignment(SwingConstants.TRAILING);
			label4j_std_2.setBounds(2, 138, 108, 25);
			jDesktopPane1.add(label4j_std_2);

			JLabel4j_std label4j_std_3 = new JLabel4j_std();
			label4j_std_3.setText((String) null);
			label4j_std_3.setHorizontalAlignment(SwingConstants.TRAILING);
			label4j_std_3.setBounds(2, 167, 108, 25);
			jDesktopPane1.add(label4j_std_3);

			JLabel4j_std jLabelCreatedUser = new JLabel4j_std();
			jLabelCreatedUser.setText(lang.get("lbl_Created_By"));
			jLabelCreatedUser.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelCreatedUser.setBounds(501, 134, 77, 21);
			jDesktopPane1.add(jLabelCreatedUser);

			textFieldUserCreated = new JTextField4j(JDBUser.field_user_id);
			textFieldUserCreated.setBounds(582, 134, 115, 22);
			jDesktopPane1.add(textFieldUserCreated);

			JButton4j jButtonLookupUserCreated = new JButton4j(Common.icon_lookup);
			jButtonLookupUserCreated.setBounds(696, 134, 21, 21);
			jDesktopPane1.add(jButtonLookupUserCreated);
			jButtonLookupUserCreated.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.dlgAutoExec = true;
					JLaunchLookup.dlgCriteriaDefault = "";

					if (JLaunchLookup.users())
					{
						textFieldUserCreated.setText(JLaunchLookup.dlgResult);
					}
				}
			});

			JLabel4j_std jLabelUpdatedUser = new JLabel4j_std();
			jLabelUpdatedUser.setText(lang.get("lbl_Updated_By"));
			jLabelUpdatedUser.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelUpdatedUser.setBounds(501, 167, 77, 21);
			jDesktopPane1.add(jLabelUpdatedUser);

			textFieldUserUpdated = new JTextField4j(JDBUser.field_user_id);
			textFieldUserUpdated.setBounds(582, 167, 115, 22);
			jDesktopPane1.add(textFieldUserUpdated);

			JButton4j jButtonLookupUserUpdated = new JButton4j(Common.icon_lookup);
			jButtonLookupUserUpdated.setBounds(696, 167, 21, 21);
			jDesktopPane1.add(jButtonLookupUserUpdated);
			jButtonLookupUserUpdated.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.dlgAutoExec = true;
					JLaunchLookup.dlgCriteriaDefault = "";

					if (JLaunchLookup.users())
					{
						textFieldUserUpdated.setText(JLaunchLookup.dlgResult);
					}
				}
			});

			button_CalendarCreatedDateFrom = new JCalendarButton(createdDateFrom);
			button_CalendarCreatedDateFrom.setEnabled(false);
			button_CalendarCreatedDateFrom.setBounds(274, 136, 21, 21);
			jDesktopPane1.add(button_CalendarCreatedDateFrom);

			button_CalendarUpdatedDateFrom = new JCalendarButton(updatedDateFrom);
			button_CalendarUpdatedDateFrom.setEnabled(false);
			button_CalendarUpdatedDateFrom.setBounds(274, 164, 21, 21);
			jDesktopPane1.add(button_CalendarUpdatedDateFrom);

			button_CalendarCreatedDateTo = new JCalendarButton(createdDateTo);
			button_CalendarCreatedDateTo.setEnabled(false);
			button_CalendarCreatedDateTo.setBounds(470, 136, 21, 21);
			jDesktopPane1.add(button_CalendarCreatedDateTo);

			button_CalendarUpdatedDateTo = new JCalendarButton(updatedDateTo);
			button_CalendarUpdatedDateTo.setEnabled(false);
			button_CalendarUpdatedDateTo.setBounds(470, 164, 21, 21);
			jDesktopPane1.add(button_CalendarUpdatedDateTo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void populateList()
	{
		JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);

		JDBPalletTableModel palletTable = new JDBPalletTableModel(pallet.getPalletDataResultSet(listStatement));

		TableRowSorter<JDBPalletTableModel> sorter = new TableRowSorter<JDBPalletTableModel>(palletTable);

		jTable1.setRowSorter(sorter);

		this.setIconifiable(true);
		jTable1.setModel(palletTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		jTable1.getColumnModel().getColumn(JDBPalletTableModel.SSCC_Col).setPreferredWidth(135);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Material_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Batch_Col).setPreferredWidth(75);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Process_Order_Col).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Quantity_Col).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Uom_Col).setPreferredWidth(30);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Date_of_Manufacture_Col).setPreferredWidth(125);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Status_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Location_Col).setPreferredWidth(95);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Date_Created_Col).setPreferredWidth(125);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Confirmed_Col).setPreferredWidth(40);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.Despatch_Col).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(JDBPalletTableModel.MHN_Number_Col).setPreferredWidth(70);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), palletTable.getRowCount());

	}

	private void palletMove()
	{
		int row = jTable1.getSelectedRow();

		if (row >= 0)
		{
			String lsscc = jTable1.getValueAt(row, 0).toString();
			String source = jTable1.getValueAt(row, 8).toString();

			JDBLocation locn = new JDBLocation(Common.selectedHostID, Common.sessionID);
			String[] locnList = locn.getLocationListArray();

			JDBPallet pal = new JDBPallet(Common.selectedHostID, Common.sessionID);
			
			if (pal.getPalletProperties(lsscc))
			{
				String dest = (String) JOptionPane.showInputDialog(Common.mainForm, "Move To", lsscc, JOptionPane.PLAIN_MESSAGE, Common.icon_interface, locnList, "Pallet Move");

				if (dest != null)
				{
					if (dest.equals("") == false)

					{
						Long txn = (long) 0;

						txn = pal.updateLocationID(txn, source, dest);

					}
				}
			}
		}
	}

	private void setSequence(boolean descending)
	{
		if (jToggleButtonSequence.isSelected() == true)
		{
			rbdescending.setSelected(true);
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending);
		}
		else
		{
			rbascending.setSelected(true);
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
