package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePalletSampleAdmin.java
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
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMHN;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBPalletExtension;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQuery2;
import com.commander4j.db.JDBSampleDefectIDs;
import com.commander4j.db.JDBSampleDefectTypes;
import com.commander4j.db.JDBSampleReasons;
import com.commander4j.db.JDBViewPalletSample;
import com.commander4j.db.JDBWTSamplePoint;
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
import com.commander4j.tablemodel.JDBViewPalletSampleTableModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFramePalletSampleAdmin extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JLabel4j_std jLabel5_1;
	private JLabel4j_std jStatusText;
	private JDateControl productionDateTo;
	private JLabel4j_std lbl_Defect_ID;
	private JLabel4j_std lbl_Defect_Type;
	private JLabel4j_std lbl_Reason;
	private JLabel4j_std jLabel_Sample_Date;
	private JLabel4j_std jLabel_Pallet_DOM;
	private JCheckBox4j jCheckBoxProductionDate;
	private JCheckBox4j jCheckBoxSampleDate;
	private JDateControl productionDateFrom;
	private JDateControl sampleDateFrom;
	private JDateControl sampleDateTo;
	private static final long serialVersionUID = 1;
	private static boolean dlg_sort_descending = true;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonSearch1;
	private JButton4j jButtonQuantityChanges;
	private JTextField4j jTextFieldLocationID;
	private JButton4j jButtonLookupProcessOrder;
	private JButton4j jButtonLookupMHN;
	private JCheckBox4j jCheckBoxLimit;
	private JCheckBox4j jCheckBoxYear;
	private JCheckBox4j jCheckBoxWeekNumber;
	private JSpinner jSpinnerLimit;
	private JSpinner jSpinnerYear;
	private JSpinner jSpinnerWeekNumber;
	private JLabel4j_std jLabel7;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std jLabelProcessOrder;
	private JTextField4j jTextFieldBatch;
	private JLabel4j_std jLabel2;
	private JButton4j jButtonClose;
	private JLabel4j_std jLabelSCC;
	private JTextField4j jTextFieldSSCC;
	private JButton4j jButtonLookupBatch;
	private JButton4j jButtonLookupLocationID;
	private JButton4j jButtonLookupMaterial;
	private JToggleButton jToggleButtonSequence;
	private JLabel4j_std jLabel15;
	private JComboBox4j<String> jComboBoxPalletStatus;
	private JComboBox4j<String> jComboBoxMonth;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JButton4j jButtonPrint;
	private JButton4j jButtonHelp;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private JTextField4j jTextFieldMaterial;
	private JTable4j jTable1;
	private JScrollPane jScrollPane1;
	private JMenuItem4j mntmEditProcessOrder;
	private JMenuItem4j mntmEditMHN;
	private JMenuItem4j mntmEditPallet;
	private JMenu4j mnReferenceData;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std jLabel_WeekNumber;

	private JCalendarButton calendarButtonproductionDateFrom;
	private JCalendarButton calendarButtontProductionDateTo;
	private JCalendarButton calendarButtonsampleDateFrom;
	private JCalendarButton calendarButtonsampleDateTo;
	private JTextField4j jTextFieldSample_Location_Plant = new JTextField4j(20);
	private JTextField4j jTextFieldSample_LocationFiller = new JTextField4j(JDBWTSamplePoint.field_SamplePoint);
	private JTextField4j jTextFieldIncidentRef = new JTextField4j(JDBPalletExtension.field_incident_ref);
	private JTextField4j jTextFieldMHN;
	private boolean emptyresult = true;
	private JDBQuery2 qSearch = new JDBQuery2(Common.selectedHostID, Common.sessionID);
	private JDBQuery2 qPrint = new JDBQuery2(Common.selectedHostID, Common.sessionID);
	private JDBQuery2 qExcel = new JDBQuery2(Common.selectedHostID, Common.sessionID);

	private JComboBox4j<JDBSampleReasons> jComboBoxSampleReason;
	private JComboBox4j<JDBSampleDefectIDs> jComboBoxDefectID;
	private JComboBox4j<JDBSampleDefectTypes> jComboBoxDefectType;
	private Vector<JDBSampleReasons> reasonList = new Vector<JDBSampleReasons>();
	private Vector<JDBSampleDefectIDs> defectIDList = new Vector<JDBSampleDefectIDs>();
	private Vector<JDBSampleDefectTypes> defectTypeList = new Vector<JDBSampleDefectTypes>();
	private Vector<JDBSampleDefectTypes> defectTypeColumns = new Vector<JDBSampleDefectTypes>();
	private JDBSampleReasons sampleReasons = new JDBSampleReasons(Common.selectedHostID, Common.sessionID);
	private JDBSampleDefectIDs sampleDefectIDs = new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID);
	private JDBSampleDefectTypes sampleDefectTypes = new JDBSampleDefectTypes(Common.selectedHostID, Common.sessionID);
	private Vector<JDBSampleDefectIDs> tempIDList = new Vector<JDBSampleDefectIDs>();
	private JCheckBox4j checkBoxLeaking;

	private void print()
	{
		JLaunchReport.runReport("RPT_PALLET_SAMPLES", null, "", buildSQL(qPrint, "*", "",true), "");
	}

	private void refreshDefectIDList(String typeFilter, String dflt)
	{

		tempIDList.clear();
		tempIDList.addAll(sampleDefectIDs.getSampleDefects(true));
		defectIDList.clear();
		defectIDList.add(new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID));

		for (int x = 0; x < tempIDList.size(); x++)
		{
			if ((typeFilter.equals("")) || (typeFilter.equals(tempIDList.get(x).getSampleDefectType())))
			{
				defectIDList.add(tempIDList.get(x));
			}
		}

		for (int x = 0; x < defectIDList.size(); x++)
		{
			if (defectIDList.get(x).getSampleDefectID().equals(sampleDefectIDs.getSampleDefectID()))
			{
				jComboBoxDefectID.setSelectedIndex(x);
			}
		}

	}

	private void print_summary()
	{

		String colname = "";
		String expression = "";

		String header = "";
		String driver = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver();
		JDBViewPalletSample viewPalletSample = new JDBViewPalletSample(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();

		defectTypeColumns.clear();
		defectTypeColumns.addAll(sampleDefectTypes.getSampleDefectsByOutputColumn(true));

		// ***********************************************************************
		// ***************************** MYSQL
		// ***********************************
		// ***********************************************************************

		if (driver.equals("com.mysql.cj.jdbc.Driver"))
		{
			header = "DISTINCT SSCC AS 'SSCC',\n" + "	SAMPLE_REASON AS 'Sample Reason',\n" + "	DATE_OF_MANUFACTURE AS 'Date of Manufacture',\n" + "	DATE_FORMAT(FIRST_CASE_TIME,'%H:%i:%s') AS 'Top of Pallet Time',\n"
					+ "	DATE_FORMAT(LAST_CASE_TIME,'%H:%i:%s') AS 'Bottom of Pallet Time',\n" + "	PDAY AS 'Day',\n" + "	PWEEK AS 'Week No',\n" + "	PMONTH AS 'PMONTH',\n" + "	PYEAR AS 'PYEAR',\n" + "	LOCATION AS 'Plant',\n"
					+ "	SAMPLE_POINT AS 'Filler',\n" + "	PROCESS_ORDER AS 'Process Order',\n" + "	MATERIAL AS 'Material',\n" + "	BATCH_NUMBER AS 'Batch Number',\n" + "	CONTAINER_CODE AS 'Container Code',\n" + "	SHIFT_ID AS 'Shift',\n"
					+ "	START_QUANTITY as 'Starting Quantity', \n";

			// Non Leaking

			for (int x = 0; x < defectTypeColumns.size(); x++)
			{

				if (((JDBSampleDefectTypes) defectTypeColumns.get(x)).isNonLeakingData())
				{
					colname = ((JDBSampleDefectTypes) defectTypeColumns.get(x)).getSampleDefectType();
					expression = " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='N' AND DEFECT_TYPE = '" + colname + "' AND NON_LEAKING_DATA = 'Y'),0.0) AS '" + colname + " (No Leak)',\n";
					header = header + expression;
				}

			}

			// Total Leaking

			header = header + " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='N' AND NON_LEAKING_DATA = 'Y'),0.0) AS 'TOTAL (No Leak)',\n";

			// Leaking

			for (int x = 0; x < defectTypeColumns.size(); x++)
			{
				if (((JDBSampleDefectTypes) defectTypeColumns.get(x)).isLeakingData())
				{
					colname = ((JDBSampleDefectTypes) defectTypeColumns.get(x)).getSampleDefectType();
					expression = " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='Y' AND DEFECT_TYPE = '" + colname + "' AND LEAKING_DATA = 'Y'),0.0) AS '" + colname + " (Leaking)',\n";
					header = header + expression;
				}
			}

			// Total Leaking

			header = header + " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='Y' AND LEAKING_DATA = 'Y'),0.0) AS 'TOTAL (Leaking)',\n";

			header = header + " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND (LEAKING_DATA = 'Y' OR NON_LEAKING_DATA = 'Y')),0.0) AS 'TOTAL (All)',\n";

			header = header + " SUPPLIER_DESCRIPTION AS 'SUPPLIER_DESCRIPTION'";

			export.saveAndView("sample_summary.xls", viewPalletSample.getPalletSampleResultSet(buildSQL(qExcel, header, " AS MMM",false)), Common.mainForm);
		}

		// ***********************************************************************
		// ***************************** ORACLE
		// **********************************
		// ***********************************************************************

		if (driver.equals("oracle.jdbc.driver.OracleDriver"))
		{
			header = "DISTINCT SSCC AS \"SSCC\",\n" + "  SAMPLE_REASON AS \"Sample Reason\",\n" + "	DATE_OF_MANUFACTURE AS \"Date of Manufacture\",\n" + "	TO_CHAR(FIRST_CASE_TIME, 'HH24:MI:SS') AS \"Top of Pallet Time\",\n"
					+ "	TO_CHAR(LAST_CASE_TIME, 'HH24:MI:SS') AS \"Bottom of Pallet Time\",\n" + "	PDAY AS \"Day\",\n" + "	PWEEK AS \"Week No\",\n" + "	PMONTH AS \"PMONTH\",\n" + "	PYEAR AS \"PYEAR\",\n" + "	LOCATION AS \"Plant\",\n"
					+ "	SAMPLE_POINT AS \"Filler\",\n" + "	PROCESS_ORDER AS \"Process Order\",\n" + "	MATERIAL AS \"Material\",\n" + "	BATCH_NUMBER AS \"Batch Number\",\n" + "	CONTAINER_CODE AS \"Container Code\",\n"
					+ "	SHIFT_ID AS \"Shift\",\n" + "	START_QUANTITY as \"Starting Quantity\",";

			// Non Leaking

			for (int x = 0; x < defectTypeColumns.size(); x++)
			{

				if (((JDBSampleDefectTypes) defectTypeColumns.get(x)).isNonLeakingData())
				{
					colname = ((JDBSampleDefectTypes) defectTypeColumns.get(x)).getSampleDefectType();
					expression = " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='N' AND DEFECT_TYPE = '" + colname + "' AND NON_LEAKING_DATA = 'Y'),0.0) AS \"BSF (No Leak)\",";
					header = header + expression;
				}

			}

			// Total Leaking

			header = header + " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='N' AND NON_LEAKING_DATA = 'Y'),0.0) AS \"TOTAL (No Leak)\",";

			// Leaking

			for (int x = 0; x < defectTypeColumns.size(); x++)
			{
				if (((JDBSampleDefectTypes) defectTypeColumns.get(x)).isLeakingData())
				{
					colname = ((JDBSampleDefectTypes) defectTypeColumns.get(x)).getSampleDefectType();
					expression = " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='Y' AND DEFECT_TYPE = '" + colname + "' AND NON_LEAKING_DATA = 'Y'),0.0) AS \"BSF (Leaking)\",";
					header = header + expression;
				}
			}

			// Total Leaking

			header = header + " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='Y' AND LEAKING_DATA = 'Y'),0.0) AS \"TOTAL (Leaking)\",";

			header = header + " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND (LEAKING_DATA = 'Y' OR NON_LEAKING_DATA = 'Y')),0.0) AS \"TOTAL (All)\",";

			header = header + " SUPPLIER_DESCRIPTION AS 'SUPPLIER_DESCRIPTION'";

			export.saveAndView("sample_summary.xls", viewPalletSample.getPalletSampleResultSet(buildSQL(qExcel, header, " MMM",false)), Common.mainForm);

		}

		// ***********************************************************************
		// ***************************** SQL SERVER
		// ******************************
		// ***********************************************************************

		if (driver.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
		{

			header = "DISTINCT SSCC AS 'SSCC',\n" + "	SAMPLE_REASON AS 'Sample Reason',\n" + "	DATE_OF_MANUFACTURE AS 'Date of Manufacture',\n" + "	CAST(FIRST_CASE_TIME AS TIME) AS 'Top of Pallet Time',\n"
					+ "	CAST(LAST_CASE_TIME AS TIME) AS 'Bottom of Pallet Time',\n" + "	PDAY AS 'Day',\n" + "	PWEEK AS 'Week No',\n" + "	PMONTH AS 'PMONTH',\n" + "	PYEAR AS 'PYEAR',\n" + "	LOCATION AS 'Plant',\n" + "	SAMPLE_POINT AS 'Filler',\n"
					+ "	PROCESS_ORDER AS 'Process Order',\n" + "	MATERIAL AS 'Material',\n" + "	BATCH_NUMBER AS 'Batch Number',\n" + "	CONTAINER_CODE AS 'Container Code',\n" + "	SHIFT_ID AS 'Shift',\n" + "	START_QUANTITY as 'Starting Quantity', ";

			// Non Leaking

			for (int x = 0; x < defectTypeColumns.size(); x++)
			{
				colname = ((JDBSampleDefectTypes) defectTypeColumns.get(x)).getSampleDefectType();
				expression = " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='N' AND DEFECT_TYPE = '" + colname + "' AND NON_LEAKING_DATA = 'Y'),0.0) AS '" + colname + " (No Leak)',";
				header = header + expression;
			}

			// Total Leaking

			header = header + " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='N' AND NON_LEAKING_DATA = 'Y'),0.0) AS 'TOTAL (No Leak)',";

			// Leaking

			for (int x = 0; x < defectTypeColumns.size(); x++)
			{
				colname = ((JDBSampleDefectTypes) defectTypeColumns.get(x)).getSampleDefectType();
				expression = " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='Y' AND DEFECT_TYPE = '" + colname + "' AND LEAKING_DATA = 'Y'),0.0) AS '" + colname + " (Leaking)',";
				header = header + expression;
			}

			// Total Leaking

			header = header + " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND LEAKING ='Y' AND LEAKING_DATA = 'Y'),0.0) AS 'TOTAL (Leaking)',";

			header = header + " COALESCE((SELECT SUM(SAMPLE_QUANTITY) FROM APP_PALLET_SAMPLES WHERE SSCC = MMM.SSCC AND (LEAKING_DATA = 'Y' OR NON_LEAKING_DATA = 'Y')),0.0) AS 'TOTAL (All)',";

			header = header + " SUPPLIER_DESCRIPTION AS 'SUPPLIER_DESCRIPTION'";

			export.saveAndView("sample_summary.xls", viewPalletSample.getPalletSampleResultSet(buildSQL(qExcel, header, " AS MMM",false)), Common.mainForm);
		}

	}

	private void export()
	{
		JDBViewPalletSample viewPalletSample = new JDBViewPalletSample(Common.selectedHostID, Common.sessionID);

		JExcel export = new JExcel();
		export.setExcelRowLimit(jCheckBoxLimit, jSpinnerLimit);
		export.saveAs("pallet_samples.xls", viewPalletSample.getPalletSampleResultSet(buildSQL(qExcel, "*", "",true)), Common.mainForm);
		// populateList();
	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals(lang.get("lbl_Pallet_SSCC")) == true)
			{
				jTextFieldSSCC.setText(jTable1.getValueAt(row, JDBViewPalletSampleTableModel.SSCC_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Material")) == true)
			{
				jTextFieldMaterial.setText(jTable1.getValueAt(row, JDBViewPalletSampleTableModel.Material_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Process_Order")) == true)
			{
				jTextFieldProcessOrder.setText(jTable1.getValueAt(row, JDBViewPalletSampleTableModel.Process_Order_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Sample_Location")) == true)
			{
				jTextFieldSample_Location_Plant.setText(jTable1.getValueAt(row, JDBViewPalletSampleTableModel.Location_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Sample_SubLocation")) == true)
			{
				jTextFieldSample_LocationFiller.setText(jTable1.getValueAt(row, JDBViewPalletSampleTableModel.SamplePoint_Col).toString());
			}
			populateList();

		}
	}

	private void copyToClipboard(String fieldname)
	{
		StringSelection stringSelection = new StringSelection("");

		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals("SSCC") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, JDBViewPalletSampleTableModel.SSCC_Col).toString());
			}

			if (fieldname.equals("Material") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, JDBViewPalletSampleTableModel.Material_Col).toString());
			}

			if (fieldname.equals("Process Order") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, JDBViewPalletSampleTableModel.Process_Order_Col).toString());
			}

			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		}
	}

	private void sortBy(String orderField)
	{
		jComboBoxSortBy.setSelectedItem(orderField);
		populateList();
	}

	private void app_Init()
	{
		setIconifiable(true);
		getContentPane().setLayout(null);

		initGUI();

		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_PALLET_SAMPLE"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);
	}

	public JInternalFramePalletSampleAdmin()
	{
		super();
		app_Init();

	}

	public JInternalFramePalletSampleAdmin(String keyField, String keyValue)
	{
		super();
		app_Init();

		clearFilter();

		updateSearch(keyField, keyValue);

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
			jTextFieldLocationID.setText(keyValue);
		}

		populateList();
	}

	private PreparedStatement buildSQL(JDBQuery2 query, String columns, String tableAlias, boolean processLimit)
	{

		query = new JDBQuery2(Common.selectedHostID, Common.sessionID);

		PreparedStatement result;

		query.applyWhat(columns);

		query.applyFrom("{schema}view_pallet_samples" + tableAlias);

		if (emptyresult)
		{
			query.applyWhere("1 = ", 2);
			emptyresult = false;
		}

		if (jCheckBoxProductionDate.isSelected())
		{
			query.applyWhere("date_of_manufacture>=", JUtility.getTimestampFromDate(productionDateFrom.getDate()));
			query.applyWhere("date_of_manufacture<=", JUtility.getTimestampFromDate(productionDateTo.getDate()));
		}

		if (jCheckBoxSampleDate.isSelected())
		{
			query.applyWhere("sample_date>=", JUtility.getTimestampFromDate(sampleDateFrom.getDate()));
			query.applyWhere("sample_date<=", JUtility.getTimestampFromDate(sampleDateTo.getDate()));
		}

		if (jTextFieldSSCC.getText().equals("") == false)
		{
			query.applyWhere("sscc = ", jTextFieldSSCC.getText());
		}

		if (jTextFieldMaterial.getText().equals("") == false)
		{
			query.applyWhere("material = ", jTextFieldMaterial.getText());
		}

		if (jTextFieldMHN.getText().equals("") == false)
		{
			query.applyWhere("mhn_number = ", jTextFieldMHN.getText());
		}

		if (jTextFieldBatch.getText().equals("") == false)
		{
			query.applyWhere("batch_number like ", jTextFieldBatch.getText());
		}

		if (jTextFieldProcessOrder.getText().equals("") == false)
		{
			query.applyWhere("process_order = ", jTextFieldProcessOrder.getText());
		}

		if (jTextFieldLocationID.getText().equals("") == false)
		{
			query.applyWhere("location_id = ", jTextFieldLocationID.getText());
		}

		if (jTextFieldIncidentRef.getText().equals("") == false)
		{
			query.applyWhere("incident_reference = ", jTextFieldIncidentRef.getText());
		}

		if (jTextFieldSample_Location_Plant.getText().equals("") == false)
		{
			query.applyWhere("location = ", jTextFieldSample_Location_Plant.getText());
		}

		if (jTextFieldSample_LocationFiller.getText().equals("") == false)
		{
			query.applyWhere("sample_point = ", jTextFieldSample_LocationFiller.getText());
		}

		if (((JDBSampleReasons) jComboBoxSampleReason.getSelectedItem()).getSampleReason().equals("") == false)
		{
			query.applyWhere("sample_reason =", ((JDBSampleReasons) jComboBoxSampleReason.getSelectedItem()).getSampleReason());
		}

		if (((JDBSampleDefectTypes) jComboBoxDefectType.getSelectedItem()).getSampleDefectType().equals("") == false)
		{
			query.applyWhere("defect_type=", ((JDBSampleDefectTypes) jComboBoxDefectType.getSelectedItem()).getSampleDefectType());
		}

		if (((JDBSampleDefectIDs) jComboBoxDefectID.getSelectedItem()).getSampleDefectID().equals("") == false)
		{
			query.applyWhere("defect_id=", ((JDBSampleDefectIDs) jComboBoxDefectID.getSelectedItem()).getSampleDefectID());
		}

		if (jComboBoxPalletStatus.getSelectedItem().toString().equals("") == false)
		{
			query.applyWhere("pallet_status=", ((String) jComboBoxPalletStatus.getSelectedItem()).toString());
		}

		if (jComboBoxMonth.getSelectedItem().toString().equals("") == false)
		{
			query.applyWhere("pmonth=", ((String) jComboBoxMonth.getSelectedItem()).toString());
		}

		if (checkBoxLeaking.isSelected())
		{
			query.applyWhere("leaking = ", 'Y');
		}

		if (jCheckBoxWeekNumber.isSelected())
		{
			query.applyWhere("pweek = ", jSpinnerWeekNumber.getValue().toString());
		}

		if (jCheckBoxYear.isSelected())
		{
			query.applyWhere("pyear = ", jSpinnerYear.getValue().toString());
		}

		query.applySort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());

		if (processLimit)
		{

			query.applyRestriction(jCheckBoxLimit.isSelected(), jSpinnerLimit.getValue());

		}

		query.applySQL();

		result = query.getPreparedStatement();

		return result;
	}

	private void editAllSamples()
	{
		if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_SAMPLE"))
		{
			int row = jTable1.getSelectedRow();
			if (row >= 0)
			{
				String lsscc = jTable1.getValueAt(row, JDBViewPalletSampleTableModel.SSCC_Col).toString();
				JLaunchMenu.runForm("FRM_PAL_SAMPLE", lsscc);
			}
		}
	}

	private void editSample()
	{
		if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_SAMPLE_EDIT"))
		{
			int row = jTable1.getSelectedRow();
			if (row >= 0)
			{
				String lsscc = jTable1.getValueAt(row, JDBViewPalletSampleTableModel.SSCC_Col).toString();
				Long seq = Long.valueOf(jTable1.getValueAt(row, JDBViewPalletSampleTableModel.Sample_Seq).toString());
				JLaunchMenu.runForm("FRM_PAL_SAMPLE_EDIT", lsscc, seq);
			}
		}
	}

	private void setSampleDateRangeStatus(boolean enabled)
	{
		if (enabled)
		{
			sampleDateFrom.setEnabled(true);
			sampleDateTo.setEnabled(true);
			calendarButtonsampleDateFrom.setEnabled(true);
			calendarButtonsampleDateTo.setEnabled(true);
		}
		else
		{
			sampleDateFrom.setEnabled(false);
			sampleDateTo.setEnabled(false);
			calendarButtonsampleDateFrom.setEnabled(false);
			calendarButtonsampleDateTo.setEnabled(false);
		}
	}

	private void setProductionDateRangeStatus(boolean enabled)
	{
		if (enabled)
		{

			productionDateFrom.setEnabled(true);
			productionDateTo.setEnabled(true);
			calendarButtonproductionDateFrom.setEnabled(true);
			calendarButtontProductionDateTo.setEnabled(true);
		}
		else
		{
			productionDateFrom.setEnabled(false);
			productionDateTo.setEnabled(false);
			calendarButtonproductionDateFrom.setEnabled(false);
			calendarButtontProductionDateTo.setEnabled(false);
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
					jScrollPane1.setBounds(0, 290, 995, 300);
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
									editSample();
								}
							}
						});

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
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_pallet_sampling_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_SAMPLE_EDIT"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										editSample();
									}
								});
								newItemMenuItem.setText(lang.get("mod_FRM_PAL_SAMPLE_EDIT"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_pallet_sampling_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_SAMPLE"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										editAllSamples();
									}
								});
								newItemMenuItem.setText(lang.get("mod_FRM_PAL_SAMPLE"));
								popupMenu.add(newItemMenuItem);
							}

							{
								mnReferenceData = new JMenu4j(lang.get("lbl_Referenced_Data"));
								popupMenu.add(mnReferenceData);

								{
									mntmEditPallet = new JMenuItem4j(lang.get("btn_Edit_Pallet"));
									mntmEditPallet.addActionListener(new ActionListener()
									{
										public void actionPerformed(ActionEvent arg0)
										{
											int row = jTable1.getSelectedRow();
											if (row >= 0)
											{
												String lpallet = jTable1.getValueAt(row, JDBViewPalletSampleTableModel.SSCC_Col).toString();
												JLaunchMenu.runForm("FRM_ADMIN_PALLET_EDIT", lpallet);
											}
										}
									});
									mnReferenceData.add(mntmEditPallet);
									mntmEditPallet.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT"));
									mntmEditPallet.setIcon(Common.icon_pallet_16x16);
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
												String lprocessorder = jTable1.getValueAt(row, JDBViewPalletSampleTableModel.Process_Order_Col).toString();
												JLaunchMenu.runForm("FRM_ADMIN_PROCESS_ORDER_EDIT", lprocessorder);
											}
										}
									});
									mnReferenceData.add(mntmEditProcessOrder);
									mntmEditProcessOrder.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PROCESS_ORDER_EDIT"));
									mntmEditProcessOrder.setIcon(Common.icon_process_order_16x16);
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
												String lmhn = jTable1.getValueAt(row, JDBViewPalletSampleTableModel.MHN_number).toString();
												if (lmhn.equals("") == false)
												{
													JLaunchMenu.runForm("FRM_ADMIN_MHN_EDIT", lmhn);
												}
											}
										}
									});
									mnReferenceData.add(mntmEditMHN);
									mntmEditMHN.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_EDIT"));
									mntmEditMHN.setIcon(Common.icon_mhn_16x16);
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
											filterBy(lang.get("lbl_Pallet_SSCC"));
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
											filterBy(lang.get("lbl_Material"));
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
											filterBy(lang.get("lbl_Process_Order"));
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
											filterBy(lang.get("lbl_Sample_Location"));
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Sample_Location"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(lang.get("lbl_Sample_SubLocation"));
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Sample_SubLocation"));
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
											populateList();
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
											sortBy("DATE_OF_MANUFACTURE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Sample_Date"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("SAMPLE_DATE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Sample_Date"));
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
											sortBy("PROCESS_ORDER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Process_Order"));
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
											copyToClipboard("Process Order");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Process_Order"));
									clipboardByMenu.add(newItemMenuItem);
								}

							}

						}

					}
				}
				{
					jButtonSearch1 = new JButton4j(Common.icon_search_16x16);
					jDesktopPane1.add(jButtonSearch1);
					jButtonSearch1.setText(lang.get("btn_Search"));
					jButtonSearch1.setBounds(3, 257, 140, 32);
					jButtonSearch1.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonSearch1.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							populateList();

						}
					});
				}
				{
					jButtonQuantityChanges = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonQuantityChanges);
					jButtonQuantityChanges.setText(lang.get("btn_Sorting_Qty"));
					jButtonQuantityChanges.setBounds(78, 213, 176, 32);
					jButtonQuantityChanges.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonQuantityChanges.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_SORTING_QTY"));
					jButtonQuantityChanges.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							populateList();
							JLaunchReport.runReport("RPT_SORTING_QTY", null, true, null, 1, false);

						}
					});
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(824, 43, 130, 22);
				}
				{
					jTextFieldLocationID = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(jTextFieldLocationID);
					jTextFieldLocationID.setBounds(114, 43, 92, 22);
				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setBounds(695, 43, 121, 21);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Location_ID"));
					jLabel3.setBounds(0, 43, 108, 21);
				}

				{
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(571, 257, 140, 32);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(855, 257, 140, 32);
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
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setBounds(287, 257, 140, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_PALLETS"));
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
					jButtonPrint = new JButton4j(Common.icon_XLS_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print_Summary"));
					jButtonPrint.setBounds(429, 257, 140, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_HIST_SUMMARY"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							print_summary();
						}
					});
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Limit"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(695, 218, 121, 21);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[]
					{ "SSCC", "DATE_OF_MANUFACTURE", "SAMPLE_DATE", "REASON", "DEFECT_ID", "LEAKING", "MATERIAL", "PROCESS_ORDER" });
					jComboBoxSortBy = new JComboBox4j<String>();
					jComboBoxSortBy.setMaximumRowCount(15);
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					// jComboBoxSortBy.setSelectedItem("TRANSACTION_DATE,SSCC");
					jComboBoxSortBy.setBounds(373, 218, 300, 22);
				}
				{
					ComboBoxModel<String> jComboBoxDefaultPalletStatusModel = new DefaultComboBoxModel<String>(Common.palletStatusIncBlank);
					jComboBoxPalletStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxPalletStatus);
					jComboBoxPalletStatus.setModel(jComboBoxDefaultPalletStatusModel);
					jComboBoxPalletStatus.setBounds(373, 148, 176, 22);
				}

				{
					ComboBoxModel<String> jComboBoxDefaultMonthModel = new DefaultComboBoxModel<String>(JUtility.getMonthNames());
					jComboBoxMonth = new JComboBox4j<String>();
					jComboBoxMonth.setMaximumRowCount(13);
					jDesktopPane1.add(jComboBoxMonth);
					jComboBoxMonth.setModel(jComboBoxDefaultMonthModel);
					jComboBoxMonth.setBounds(683, 186, 92, 22);
				}
				{
					jLabel15 = new JLabel4j_std();
					jDesktopPane1.add(jLabel15);
					jLabel15.setText(lang.get("lbl_Pallet_Status"));
					jLabel15.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel15.setBounds(236, 148, 130, 21);
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jToggleButtonSequence.setSelected(true);
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(674, 218, 21, 22);
					jToggleButtonSequence.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Material_Batch"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2.setBounds(695, 78, 121, 21);
				}
				{
					jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jDesktopPane1.add(jTextFieldBatch);
					jTextFieldBatch.setBounds(824, 78, 130, 22);
				}
				{
					jLabelProcessOrder = new JLabel4j_std();
					jDesktopPane1.add(jLabelProcessOrder);
					jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
					jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelProcessOrder.setBounds(695, 113, 121, 21);
				}
				{
					jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setBounds(824, 113, 130, 22);
				}
				{
					jLabel7 = new JLabel4j_std();
					jDesktopPane1.add(jLabel7);
					jLabel7.setText(lang.get("lbl_Sort_By"));
					jLabel7.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel7.setBounds(236, 218, 130, 21);
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
					jSpinnerLimit.setBounds(844, 218, 73, 21);
					jSpinnerLimit.setValue(1000);
					jSpinnerLimit.getEditor().setSize(45, 21);
				}
				{
					SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
					jSpinnerIntModel.setMinimum(1980);
					jSpinnerIntModel.setMaximum(2500);
					jSpinnerIntModel.setStepSize(1);

					jSpinnerYear = new JSpinner();
					jSpinnerYear.setEnabled(false);
					jDesktopPane1.add(jSpinnerYear);

					jSpinnerYear.setModel(jSpinnerIntModel);
					JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerYear, "#");
					ne.getTextField().setFont(Common.font_std);
					jSpinnerYear.setEditor(ne);
					jSpinnerYear.setBounds(329, 186, 63, 21);
					// jSpinnerYear.setValue(2020);

					jSpinnerYear.setValue(Integer.valueOf(JUtility.getYear(JUtility.getSQLDateTime())));
					jSpinnerYear.getEditor().setSize(45, 21);
				}
				{
					SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
					jSpinnerIntModel.setMinimum(1);
					jSpinnerIntModel.setMaximum(53);
					jSpinnerIntModel.setStepSize(1);

					jSpinnerWeekNumber = new JSpinner();
					jSpinnerWeekNumber.setEnabled(false);
					jDesktopPane1.add(jSpinnerWeekNumber);

					jSpinnerWeekNumber.setModel(jSpinnerIntModel);
					JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerWeekNumber, "#");
					ne.getTextField().setFont(Common.font_std);
					jSpinnerWeekNumber.setEditor(ne);
					jSpinnerWeekNumber.setBounds(541, 186, 45, 21);

					jSpinnerWeekNumber.setValue(Integer.valueOf(JUtility.getWeekOfYear(JUtility.getSQLDateTime())));
					jSpinnerWeekNumber.getEditor().setSize(45, 21);
				}
				{
					jCheckBoxLimit = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxLimit);
					jCheckBoxLimit.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxLimit.setBounds(823, 218, 21, 21);
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
					jCheckBoxYear = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxYear);
					jCheckBoxYear.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxYear.setBounds(300, 186, 21, 21);
					jCheckBoxYear.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxYear.isSelected())
							{
								jSpinnerYear.setEnabled(true);
							}
							else
							{
								jSpinnerYear.setEnabled(false);
							}
						}
					});
				}
				{
					jCheckBoxWeekNumber = new JCheckBox4j();
					jCheckBoxWeekNumber.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							if (jCheckBoxWeekNumber.isSelected())
							{
								jSpinnerWeekNumber.setEnabled(true);
							}
							else
							{
								jSpinnerWeekNumber.setEnabled(false);
							}
						}
					});
					jDesktopPane1.add(jCheckBoxWeekNumber);
					jCheckBoxWeekNumber.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxWeekNumber.setBounds(512, 186, 21, 21);
					jCheckBoxYear.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxWeekNumber.isSelected())
							{
								jSpinnerWeekNumber.setEnabled(true);
							}
							else
							{
								jSpinnerWeekNumber.setEnabled(false);
							}
						}
					});
				}
				{
					jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookupProcessOrder);
					jButtonLookupProcessOrder.setBounds(954, 113, 21, 21);
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
					jButtonLookupMHN = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookupMHN);
					jButtonLookupMHN.setBounds(206, 148, 21, 21);
					jButtonLookupMHN.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgCriteriaDefault = "Active";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.masterHoldNotices())
							{
								jTextFieldMHN.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jButtonLookupBatch = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookupBatch);
					jButtonLookupBatch.setBounds(954, 78, 21, 21);
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
					jButtonLookupMaterial = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookupMaterial);
					jButtonLookupMaterial.setBounds(954, 43, 21, 21);
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
					jButtonLookupLocationID = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookupLocationID);
					jButtonLookupLocationID.setBounds(206, 43, 21, 21);
					jButtonLookupLocationID.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "Y";
							if (JLaunchLookup.locations())
							{
								jTextFieldLocationID.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
					jDesktopPane1.add(jTextFieldSSCC);
					jTextFieldSSCC.setBounds(824, 148, 152, 22);
				}
				{
					jLabelSCC = new JLabel4j_std();
					jDesktopPane1.add(jLabelSCC);
					jLabelSCC.setText(lang.get("lbl_Pallet_SSCC"));
					jLabelSCC.setBounds(695, 148, 121, 21);
					jLabelSCC.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					productionDateFrom = new JDateControl();
					productionDateFrom.setBounds(189, 7, 128, 25);
					productionDateFrom.setEnabled(false);
					jDesktopPane1.add(productionDateFrom);
				}

				{
					sampleDateFrom = new JDateControl();
					sampleDateFrom.setBounds(647, 7, 128, 25);
					sampleDateFrom.setEnabled(false);
					jDesktopPane1.add(sampleDateFrom);
				}

				{
					jCheckBoxProductionDate = new JCheckBox4j();
					jCheckBoxProductionDate.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							setProductionDateRangeStatus(jCheckBoxProductionDate.isSelected());
						}
					});
					jCheckBoxProductionDate.setBackground(new Color(255, 255, 255));
					jCheckBoxProductionDate.setBounds(165, 7, 21, 21);
					jDesktopPane1.add(jCheckBoxProductionDate);
				}

				{
					jCheckBoxSampleDate = new JCheckBox4j();
					jCheckBoxSampleDate.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							setSampleDateRangeStatus(jCheckBoxSampleDate.isSelected());
						}
					});
					jCheckBoxSampleDate.setBackground(new Color(255, 255, 255));
					jCheckBoxSampleDate.setBounds(625, 7, 21, 21);
					jDesktopPane1.add(jCheckBoxSampleDate);
				}

				{
					jLabel_Sample_Date = new JLabel4j_std();
					jLabel_Sample_Date.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel_Sample_Date.setText(lang.get("lbl_Sample_Date"));
					jLabel_Sample_Date.setBounds(486, 9, 128, 21);
					jDesktopPane1.add(jLabel_Sample_Date);
				}

				{
					jLabel_Pallet_DOM = new JLabel4j_std();
					jLabel_Pallet_DOM.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel_Pallet_DOM.setText(lang.get("lbl_Pallet_DOM"));
					jLabel_Pallet_DOM.setBounds(0, 9, 152, 21);
					jDesktopPane1.add(jLabel_Pallet_DOM);
				}

				{
					lbl_Reason = new JLabel4j_std();
					lbl_Reason.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_Reason.setText(lang.get("lbl_Reason"));
					lbl_Reason.setBounds(236, 43, 130, 21);
					jDesktopPane1.add(lbl_Reason);
				}

				{
					lbl_Defect_ID = new JLabel4j_std();
					lbl_Defect_ID.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_Defect_ID.setText(lang.get("lbl_Defect_ID"));
					lbl_Defect_ID.setBounds(236, 113, 130, 21);
					jDesktopPane1.add(lbl_Defect_ID);
				}

				{
					lbl_Defect_Type = new JLabel4j_std();
					lbl_Defect_Type.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_Defect_Type.setText(lang.get("lbl_Defect_Type"));
					lbl_Defect_Type.setBounds(236, 78, 130, 21);
					jDesktopPane1.add(lbl_Defect_Type);
				}

				{
					reasonList.add(new JDBSampleReasons(Common.selectedHostID, Common.sessionID));
					reasonList.addAll(sampleReasons.getSampleReasons(true));
					ComboBoxModel<JDBSampleReasons> jComboBox2Model = new DefaultComboBoxModel<JDBSampleReasons>(reasonList);
					jComboBoxSampleReason = new JComboBox4j<JDBSampleReasons>();
					jComboBoxSampleReason.setModel(jComboBox2Model);
					jComboBoxSampleReason.setEditable(false);
					jComboBoxSampleReason.setBounds(373, 43, 300, 23);
					jDesktopPane1.add(jComboBoxSampleReason);
				}

				{
					defectIDList.add(new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID));
					defectIDList.addAll(sampleDefectIDs.getSampleDefects(true));
					ComboBoxModel<JDBSampleDefectIDs> jComboBox3Model = new DefaultComboBoxModel<JDBSampleDefectIDs>(defectIDList);
					jComboBoxDefectID = new JComboBox4j<JDBSampleDefectIDs>();
					jComboBoxDefectID.setModel(jComboBox3Model);
					jComboBoxDefectID.setEditable(false);
					jComboBoxDefectID.setBounds(373, 113, 300, 23);
					jComboBoxDefectID.setMaximumRowCount(20);
					jDesktopPane1.add(jComboBoxDefectID);
				}

				{
					defectTypeList.add(new JDBSampleDefectTypes(Common.selectedHostID, Common.sessionID));
					defectTypeList.addAll(sampleDefectTypes.getSampleDefects(true));
					ComboBoxModel<JDBSampleDefectTypes> jComboBox3Model = new DefaultComboBoxModel<JDBSampleDefectTypes>(defectTypeList);
					jComboBoxDefectType = new JComboBox4j<JDBSampleDefectTypes>();
					jComboBoxDefectType.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							refreshDefectIDList(((JDBSampleDefectTypes) jComboBoxDefectType.getSelectedItem()).getSampleDefectType(), ((JDBSampleDefectIDs) jComboBoxDefectID.getSelectedItem()).getSampleDefectID());
						}
					});
					jComboBoxDefectType.setModel(jComboBox3Model);
					jComboBoxDefectType.setEditable(false);
					jComboBoxDefectType.setBounds(373, 78, 300, 23);
					jComboBoxDefectType.setMaximumRowCount(20);
					jDesktopPane1.add(jComboBoxDefectType);
				}

				{
					productionDateTo = new JDateControl();
					productionDateTo.setBounds(349, 7, 128, 25);
					productionDateTo.setEnabled(false);
					jDesktopPane1.add(productionDateTo);
				}

				{
					sampleDateTo = new JDateControl();
					sampleDateTo.setBounds(824, 7, 128, 25);
					sampleDateTo.setEnabled(false);
					jDesktopPane1.add(sampleDateTo);
				}

				{
					final JButton4j exportXlsButton = new JButton4j(Common.icon_XLS_16x16);
					exportXlsButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							export();
						}
					});
					exportXlsButton.setText(lang.get("btn_Excel"));
					exportXlsButton.setBounds(713, 257, 140, 32);
					jDesktopPane1.add(exportXlsButton);
				}

				{

					jTextFieldIncidentRef.setBounds(114, 183, 92, 22);
					jDesktopPane1.add(jTextFieldIncidentRef);
				}

				{
					jLabel5_1 = new JLabel4j_std();
					jLabel5_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5_1.setText(lang.get("lbl_Incident_Ref"));
					jLabel5_1.setBounds(0, 186, 108, 21);
					jDesktopPane1.add(jLabel5_1);
				}

				{
					JLabel4j_std lblLeaking = new JLabel4j_std();
					lblLeaking.setText(lang.get("lbl_Leaking"));
					lblLeaking.setHorizontalAlignment(SwingConstants.TRAILING);
					lblLeaking.setBounds(817, 186, 130, 21);
					jDesktopPane1.add(lblLeaking);
				}

				{
					JLabel4j_std lblMonth = new JLabel4j_std();
					lblMonth.setText(lang.get("lbl_Month"));
					lblMonth.setHorizontalAlignment(SwingConstants.TRAILING);
					lblMonth.setBounds(591, 186, 82, 21);
					jDesktopPane1.add(lblMonth);
				}

				{
					checkBoxLeaking = new JCheckBox4j();
					checkBoxLeaking.setOpaque(true);
					checkBoxLeaking.setText("");
					checkBoxLeaking.setSelected(false);
					checkBoxLeaking.setBounds(954, 186, 21, 24);
					jDesktopPane1.add(checkBoxLeaking);
				}

				JLabel4j_std jLabel_Year = new JLabel4j_std();
				jLabel_Year.setBounds(216, 186, 78, 21);
				jDesktopPane1.add(jLabel_Year);
				jLabel_Year.setText(lang.get("lbl_Year"));
				jLabel_Year.setHorizontalAlignment(SwingConstants.TRAILING);

				{
					jLabel_WeekNumber = new JLabel4j_std();
					jLabel_WeekNumber.setBounds(399, 186, 101, 21);
					jDesktopPane1.add(jLabel_WeekNumber);
					jLabel_WeekNumber.setText(lang.get("lbl_Week_Number"));
					jLabel_WeekNumber.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jButtonClear = new JButton4j(Common.icon_clear_16x16);
					jButtonClear.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							clearFilter();
							populateList();
						}
					});
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jButtonClear.setBounds(145, 257, 140, 32);
					jDesktopPane1.add(jButtonClear);
				}

				JLabel4j_std lbl_Sample_Location_Main = new JLabel4j_std();
				lbl_Sample_Location_Main.setText(lang.get("lbl_Sample_Location"));
				lbl_Sample_Location_Main.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Sample_Location_Main.setBounds(0, 78, 108, 21);
				jDesktopPane1.add(lbl_Sample_Location_Main);

				{
					JLabel4j_std jLabel_Sample_Point = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Sample_Point);
					jLabel_Sample_Point.setText(lang.get("lbl_Sample_SubLocation"));
					jLabel_Sample_Point.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel_Sample_Point.setBounds(0, 113, 108, 21);
				}

				jTextFieldSample_Location_Plant.setBounds(114, 78, 92, 22);
				jDesktopPane1.add(jTextFieldSample_Location_Plant);

				JButton4j button4j_sublocation = new JButton4j(Common.icon_lookup_16x16);
				button4j_sublocation.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "";
						if (JLaunchLookup.samplePointLocations())
						{
							jTextFieldSample_Location_Plant.setText(JLaunchLookup.dlgResult);
						}
					}
				});
				button4j_sublocation.setBounds(206, 78, 21, 21);
				jDesktopPane1.add(button4j_sublocation);

				jTextFieldSample_LocationFiller.setBounds(114, 113, 92, 22);
				jDesktopPane1.add(jTextFieldSample_LocationFiller);

				JButton4j button4j_samplepoint = new JButton4j(Common.icon_lookup_16x16);
				button4j_samplepoint.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = jTextFieldSample_Location_Plant.getText();
						if (JLaunchLookup.weightSamplePoint())
						{
							jTextFieldSample_LocationFiller.setText(JLaunchLookup.dlgResult);
						}
					}
				});
				button4j_samplepoint.setBounds(206, 113, 21, 21);
				jDesktopPane1.add(button4j_samplepoint);

				JLabel4j_std label4j_MHN = new JLabel4j_std();
				label4j_MHN.setText(lang.get("lbl_MHN_Number"));
				label4j_MHN.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_MHN.setBounds(0, 148, 108, 21);
				jDesktopPane1.add(label4j_MHN);

				jTextFieldMHN = new JTextField4j(JDBMHN.field_mhn_number);
				jTextFieldMHN.setBounds(114, 148, 92, 22);
				jDesktopPane1.add(jTextFieldMHN);

				calendarButtonproductionDateFrom = new JCalendarButton(productionDateFrom);
				calendarButtonproductionDateFrom.setEnabled(false);
				calendarButtonproductionDateFrom.setBounds(317, 9, 21, 21);
				jDesktopPane1.add(calendarButtonproductionDateFrom);

				calendarButtontProductionDateTo = new JCalendarButton(productionDateTo);
				calendarButtontProductionDateTo.setEnabled(false);
				calendarButtontProductionDateTo.setBounds(478, 9, 21, 21);
				jDesktopPane1.add(calendarButtontProductionDateTo);

				calendarButtonsampleDateFrom = new JCalendarButton(sampleDateFrom);
				calendarButtonsampleDateFrom.setEnabled(false);
				calendarButtonsampleDateFrom.setBounds(779, 9, 21, 21);
				jDesktopPane1.add(calendarButtonsampleDateFrom);

				calendarButtonsampleDateTo = new JCalendarButton(sampleDateTo);
				calendarButtonsampleDateTo.setEnabled(false);
				calendarButtonsampleDateTo.setBounds(954, 9, 21, 21);
				jDesktopPane1.add(calendarButtonsampleDateTo);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void clearFilter()
	{

		jTextFieldMaterial.setText("");
		jTextFieldBatch.setText("");
		jTextFieldSSCC.setText("");
		jTextFieldLocationID.setText("");
		jTextFieldProcessOrder.setText("");
		jComboBoxPalletStatus.setSelectedIndex(0);
		jComboBoxSampleReason.setSelectedIndex(0);
		jComboBoxDefectID.setSelectedIndex(0);
		jComboBoxDefectType.setSelectedIndex(0);
		jCheckBoxProductionDate.setSelected(false);
		jCheckBoxSampleDate.setSelected(false);
		setSampleDateRangeStatus(false);
		jCheckBoxProductionDate.setSelected(false);
		setProductionDateRangeStatus(false);
		jTextFieldIncidentRef.setText("");
		jTextFieldLocationID.setText("");
		jTextFieldSample_Location_Plant.setText("");
		jTextFieldSample_LocationFiller.setText("");
		jTextFieldMHN.setText("");
		jComboBoxMonth.setSelectedIndex(0);

	}

	private void populateList()
	{
		JDBViewPalletSample pallethistory = new JDBViewPalletSample(Common.selectedHostID, Common.sessionID);

		JDBViewPalletSampleTableModel palletHistoryTable = new JDBViewPalletSampleTableModel(pallethistory.getPalletSampleResultSet(buildSQL(qSearch, "*", "",true)));
		TableRowSorter<JDBViewPalletSampleTableModel> sorter = new TableRowSorter<JDBViewPalletSampleTableModel>(palletHistoryTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(palletHistoryTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.SSCC_Col).setPreferredWidth(140);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.Sample_Seq).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.Production_Date).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.Sample_Date).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.Location_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.SamplePoint_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.Sample_Reason).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.Sample_DefectType).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.Sample_DefectID).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.Leaking).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.SampleQuantity_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.Material_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.Process_Order_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewPalletSampleTableModel.MHN_number).setPreferredWidth(90);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), palletHistoryTable.getRowCount());

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
