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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBQuery2;
import com.commander4j.db.JDBWTProductGroups;
import com.commander4j.db.JDBWTSampleHeader;
import com.commander4j.db.JDBWTWorkstation;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBWeightHeaderTableModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The JInternalFrameWTReports is for reporting on weight checks
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTReports.jpg" >
 * 
 */
public class JInternalFrameWTReport extends JInternalFrame
{

	private static boolean dlg_sort_descending = false;
	private static final long serialVersionUID = 1;
	private JButton4j btn_Close;
	private JButton4j btn_Help;
	private JButton4j btn_Process_Order_Lookup;
	private JButton4j btn_SamplePoint_Lookup;
	private JCalendarButton button_CalendarSampleDateFrom;
	private JCalendarButton button_CalendarSampleDateTo;
	private JCheckBox4j checkBox4jFromEnabled = new JCheckBox4j();
	private JCheckBox4j checkBox4jToEnabled = new JCheckBox4j();
	private JTextField4j fld_Container_Code = new JTextField4j(JDBQMSample.field_data_2);
	private JTextField4j fld_Material = new JTextField4j(JDBMaterial.field_material);
	private JTextField fld_Process_Order = new JTextField(JDBProcessOrder.field_process_order);
	private JTextField4j fld_Product_Group = new JTextField4j(JDBWTProductGroups.field_product_group);
	private JTextField4j fld_SamplePoint = new JTextField4j(JDBWTWorkstation.field_SamplePoint);
	private JButton4j jButtonClear;
	private JButton4j jButtonDetails;
	private JButton4j jButtonExcel;
	private JButton4j jButtonMaterialLookup;
	private JButton4j jButtonReports;
	private JCheckBox4j jCheckBoxLimit;
	private JComboBox4j<String> jComboBoxSortBy;
	private JComboBox4j<String> jComboBoxReportType;
	private JDesktopPane jDesktopPane1;
	private JSpinner jSpinnerLimit = new JSpinner();
	private JLabel4j_std jStatusText;
	private JToggleButton jToggleButtonSequence = new JToggleButton();
	private JDBLanguage lang;
	private JLabel4j_std lbl_Material;
	private JLabel4j_std lbl_Process_Order;
	private PreparedStatement listStatement;
	private JDateControl sampleDateFrom = new JDateControl();
	private JDateControl sampleDateTo = new JDateControl();
	private JScrollPane scrollPane = new JScrollPane();
	private JTable4j tableResults;
	private JCheckBox4j checkBox4j_T1 = new JCheckBox4j();
	private JCheckBox4j checkBox4j_T2 = new JCheckBox4j();
	private LinkedList<String> selectedSamplePoints = new LinkedList<String>();

	public JInternalFrameWTReport()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(btn_Help, JUtility.getHelpSetIDforModule("FRM_WEIGHT_REPORTS"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				fld_SamplePoint.setToolTipText("None");
				fld_SamplePoint.setEditable(false);
				fld_SamplePoint.setEnabled(false);
				fld_SamplePoint.requestFocus();
				fld_SamplePoint.setCaretPosition(fld_SamplePoint.getText().length());

			}
		});

	}

	private PreparedStatement buildSQLindividual()
	{

		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		q2.applyFrom("{schema}view_weight_samples");
		q2.applyWhat("*");

		if (checkBox4jFromEnabled.isSelected())
		{
			q2.applyWhere("sample_date>=", JUtility.getTimestampFromDate(sampleDateFrom.getDate()));
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("Mean above Nominal"))
		{
			q2.applyWhereLiteral(" sample_mean > nominal_weight ");
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("T1s or T2s"))
		{
			q2.applyWhereLiteral(" ((sample_t1_count > 0) or (sample_t2_count > 0)) ");
		}

		if (checkBox4jToEnabled.isSelected())
		{
			q2.applyWhere("sample_date<=", JUtility.getTimestampFromDate(sampleDateTo.getDate()));
		}

		if (fld_Material.getText().equals("") == false)
		{
			q2.applyWhere("material = ", fld_Material.getText());
		}

		if (fld_Process_Order.getText().equals("") == false)
		{
			q2.applyWhere("process_order = ", fld_Process_Order.getText());
		}

		if (fld_SamplePoint.getText().equals("") == false)
		{
			q2.applyIn("sample_point in ", selectedSamplePoints);
		}

		if (fld_Product_Group.getText().equals("") == false)
		{
			q2.applyWhere("product_group = ", fld_Product_Group.getText());
		}

		if (checkBox4j_T1.isSelected())
		{
			q2.applyWhere("sample_t1_count > ", 0);
		}

		if (checkBox4j_T2.isSelected())
		{
			q2.applyWhere("sample_t2_count > ", 0);
		}

		q2.applySort("SAMPLE_POINT,PROCESS_ORDER,SAMPLE_DATE,SAMPLE_SEQUENCE,SAMPLE_WEIGHT_DATE", false);

		q2.applySQL();
		result = q2.getPreparedStatement();

		return result;
	}

	private void buildSQL()
	{

		JDBQuery2.closeStatement(listStatement);

		listStatement = buildSQLr();
	}
	
	private PreparedStatement buildSQLquickref1()
	{

		PreparedStatement result;
		
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);

		String driver = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver();

		if (driver.equals("oracle.jdbc.driver.OracleDriver"))
		{
			q2.applyWhat("SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT, CONTAINER_CODE, PRODUCT_GROUP, MIN(SAMPLE_SIZE) AS SAMPLE_SIZE,  AVG(SAMPLE_NET_WEIGHT) AS MEAN_WEIGHT,\n" + 
					" STDDEV(SAMPLE_NET_WEIGHT) AS STANDARD_DEVIATION, SUM(SAMPLE_T1_COUNT_0) AS TOTAL_T1S, SUM(SAMPLE_T2_COUNT_0) AS TOTAL_T2S, MIN(SAMPLE_WEIGHT_DATE) AS FIRST_WEIGHT_DATE,\n" + 
					" MAX(SAMPLE_WEIGHT_DATE) AS LAST_WEIGHT_DATE, COUNT(*) AS NO_OF_SAMPLES, CAST(ROUND((((SUM(SAMPLE_T1_COUNT_0)*1.000)  / (COUNT(*)*1.000)) * 100.000),2) AS DECIMAL(10, 2)) AS PERCENTAGE_T1S\n");

		}
		
		if (driver.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
		{
			q2.applyWhat("SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT, CONTAINER_CODE, PRODUCT_GROUP, MIN(SAMPLE_SIZE) AS SAMPLE_SIZE, AVG(SAMPLE_NET_WEIGHT) AS MEAN_WEIGHT,\n" + 
					"	STDEV(SAMPLE_NET_WEIGHT) AS STANDARD_DEVIATION, SUM(SAMPLE_T1_COUNT_0) AS TOTAL_T1S, SUM(SAMPLE_T2_COUNT_0) AS TOTAL_T2S, MIN(SAMPLE_WEIGHT_DATE) AS FIRST_WEIGHT_DATE,\n" + 
					"	MAX(SAMPLE_WEIGHT_DATE) AS LAST_WEIGHT_DATE, COUNT(*) AS NO_OF_SAMPLES, (SUM(SAMPLE_T1_COUNT_0)*1.000)  / ((COUNT(*)*1.000)) AS PERCENTAGE_T1S\n");
		}
		
		if (driver.equals("com.mysql.cj.jdbc.Driver"))
		{
			q2.applyWhat("SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT, CONTAINER_CODE, PRODUCT_GROUP, MIN(SAMPLE_SIZE) AS SAMPLE_SIZE, AVG(SAMPLE_NET_WEIGHT) AS MEAN_WEIGHT,\n" + 
					"	STDDEV(SAMPLE_NET_WEIGHT) AS STANDARD_DEVIATION, SUM(SAMPLE_T1_COUNT_0) AS TOTAL_T1S, SUM(SAMPLE_T2_COUNT_0) AS TOTAL_T2S, MIN(SAMPLE_WEIGHT_DATE) AS FIRST_WEIGHT_DATE,\n" + 
					"	MAX(SAMPLE_WEIGHT_DATE) AS LAST_WEIGHT_DATE, COUNT(*) AS NO_OF_SAMPLES, CAST(ROUND((((SUM(SAMPLE_T1_COUNT_0)*1.000)  / (COUNT(*)*1.000)) * 100.000),2) AS DECIMAL(10, 2)) AS PERCENTAGE_T1S\n");
		}
		
		q2.applyFrom("view_weight_samples\n");

		q2.applyWhere("NOMINAL_WEIGHT > ", 0);
		
		if (checkBox4jFromEnabled.isSelected())
		{
			q2.applyWhere("sample_date>=", JUtility.getTimestampFromDate(sampleDateFrom.getDate()));
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("Mean above Nominal"))
		{
			q2.applyWhereLiteral(" sample_mean > nominal_weight ");
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("T1s or T2s"))
		{
			q2.applyWhereLiteral(" ((sample_t1_count > 0) or (sample_t2_count > 0)) ");
		}

		if (checkBox4jToEnabled.isSelected())
		{
			q2.applyWhere("sample_date<=", JUtility.getTimestampFromDate(sampleDateTo.getDate()));
		}

		if (fld_Material.getText().equals("") == false)
		{
			q2.applyWhere("material = ", fld_Material.getText());
		}

		if (fld_Process_Order.getText().equals("") == false)
		{
			q2.applyWhere("process_order = ", fld_Process_Order.getText());
		}

		if (fld_SamplePoint.getText().equals("") == false)
		{
			q2.applyIn("sample_point in ", selectedSamplePoints);
		}

		if (fld_Product_Group.getText().equals("") == false)
		{
			q2.applyWhere("product_group = ", fld_Product_Group.getText());
		}

		if (checkBox4j_T1.isSelected())
		{
			q2.applyWhere("sample_t1_count > ", 0);
		}

		if (checkBox4j_T2.isSelected())
		{
			q2.applyWhere("sample_t2_count > ", 0);
		}
		
		q2.setSQLFinal(" GROUP BY SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT, CONTAINER_CODE, 	PRODUCT_GROUP\n" + 
				"	ORDER BY SAMPLE_DATE_SHORT,	NOMINAL_WEIGHT,SAMPLE_POINT,CONTAINER_CODE, PRODUCT_GROUP");
		
		q2.applySQL();
		result = q2.getPreparedStatement();

		return result;
	}

	private PreparedStatement buildSQLquickref2()
	{

		PreparedStatement result;
		
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);

		String driver = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver();

		if (driver.equals("oracle.jdbc.driver.OracleDriver"))
		{
			q2.applyWhat("SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT, PROCESS_ORDER, MATERIAL,CONTAINER_CODE, MIN(SAMPLE_SIZE) AS SAMPLE_SIZE, AVG(SAMPLE_NET_WEIGHT) AS MEAN_WEIGHT,\n" + 
					" STDDEV(SAMPLE_NET_WEIGHT) AS STANDARD_DEVIATION, SUM(SAMPLE_T1_COUNT_0) AS TOTAL_T1S, SUM(SAMPLE_T2_COUNT_0) AS TOTAL_T2S, MIN(SAMPLE_WEIGHT_DATE) AS FIRST_WEIGHT_DATE,\n" + 
					" MAX(SAMPLE_WEIGHT_DATE) AS LAST_WEIGHT_DATE, COUNT(*) AS NO_OF_SAMPLES, CAST(ROUND((((SUM(SAMPLE_T1_COUNT_0)*1.000)  / (COUNT(*)*1.000)) * 100.000),2) AS DECIMAL(10, 2)) AS PERCENTAGE_T1S\n");

		}
		
		if (driver.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
		{
			q2.applyWhat("SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT, PROCESS_ORDER, MATERIAL,CONTAINER_CODE, MIN(SAMPLE_SIZE) AS SAMPLE_SIZE, AVG(SAMPLE_NET_WEIGHT) AS MEAN_WEIGHT,\n" + 
					" STDEV(SAMPLE_NET_WEIGHT) AS STANDARD_DEVIATION, SUM(SAMPLE_T1_COUNT_0) AS TOTAL_T1S, SUM(SAMPLE_T2_COUNT_0) AS TOTAL_T2S, MIN(SAMPLE_WEIGHT_DATE) AS FIRST_WEIGHT_DATE,\n" + 
					" MAX(SAMPLE_WEIGHT_DATE) AS LAST_WEIGHT_DATE, COUNT(*) AS NO_OF_SAMPLES, (SUM(SAMPLE_T1_COUNT_0)*1.000)  / ((COUNT(*)*1.000)) AS PERCENTAGE_T1S\n");
		}
		
		if (driver.equals("com.mysql.cj.jdbc.Driver"))
		{
			q2.applyWhat("SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT, PROCESS_ORDER, MATERIAL,CONTAINER_CODE, MIN(SAMPLE_SIZE) AS SAMPLE_SIZE, AVG(SAMPLE_NET_WEIGHT) AS MEAN_WEIGHT,\n" + 
					" STDDEV(SAMPLE_NET_WEIGHT) AS STANDARD_DEVIATION, SUM(SAMPLE_T1_COUNT_0) AS TOTAL_T1S, SUM(SAMPLE_T2_COUNT_0) AS TOTAL_T2S, MIN(SAMPLE_WEIGHT_DATE) AS FIRST_WEIGHT_DATE,\n" + 
					" MAX(SAMPLE_WEIGHT_DATE) AS LAST_WEIGHT_DATE, COUNT(*) AS NO_OF_SAMPLES, CAST(ROUND((((SUM(SAMPLE_T1_COUNT_0)*1.000)  / (COUNT(*)*1.000)) * 100.000),2) AS DECIMAL(10, 2)) AS PERCENTAGE_T1S\n");
		}
		
		q2.applyFrom("view_weight_samples\n");

		q2.applyWhere("NOMINAL_WEIGHT > ", 0);
		
		if (checkBox4jFromEnabled.isSelected())
		{
			q2.applyWhere("sample_date>=", JUtility.getTimestampFromDate(sampleDateFrom.getDate()));
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("Mean above Nominal"))
		{
			q2.applyWhereLiteral(" sample_mean > nominal_weight ");
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("T1s or T2s"))
		{
			q2.applyWhereLiteral(" ((sample_t1_count > 0) or (sample_t2_count > 0)) ");
		}

		if (checkBox4jToEnabled.isSelected())
		{
			q2.applyWhere("sample_date<=", JUtility.getTimestampFromDate(sampleDateTo.getDate()));
		}

		if (fld_Material.getText().equals("") == false)
		{
			q2.applyWhere("material = ", fld_Material.getText());
		}

		if (fld_Process_Order.getText().equals("") == false)
		{
			q2.applyWhere("process_order = ", fld_Process_Order.getText());
		}

		if (fld_SamplePoint.getText().equals("") == false)
		{
			q2.applyIn("sample_point in ", selectedSamplePoints);
		}

		if (fld_Product_Group.getText().equals("") == false)
		{
			q2.applyWhere("product_group = ", fld_Product_Group.getText());
		}

		if (checkBox4j_T1.isSelected())
		{
			q2.applyWhere("sample_t1_count > ", 0);
		}

		if (checkBox4j_T2.isSelected())
		{
			q2.applyWhere("sample_t2_count > ", 0);
		}
		
		q2.setSQLFinal(" GROUP BY SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT, PROCESS_ORDER, MATERIAL,CONTAINER_CODE\n" + 
				"	ORDER BY SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT, PROCESS_ORDER, MATERIAL");
		
		q2.applySQL();
		result = q2.getPreparedStatement();

		return result;
	}
	
	private PreparedStatement buildSQLquickref3()
	{

		PreparedStatement result;
		
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);

		String driver = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver();

		if (driver.equals("oracle.jdbc.driver.OracleDriver"))
		{
			q2.applyWhat("SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT,CONTAINER_CODE,PRODUCT_GROUP, PROCESS_ORDER, MATERIAL, MIN(SAMPLE_SIZE) AS SAMPLE_SIZE, AVG(SAMPLE_NET_WEIGHT) AS MEAN_WEIGHT,\n" + 
					" STDDEV(SAMPLE_NET_WEIGHT) AS STANDARD_DEVIATION, SUM(SAMPLE_T1_COUNT_0) AS TOTAL_T1S, SUM(SAMPLE_T2_COUNT_0) AS TOTAL_T2S, MIN(SAMPLE_WEIGHT_DATE) AS FIRST_WEIGHT_DATE,\n" + 
					" MAX(SAMPLE_WEIGHT_DATE) AS LAST_WEIGHT_DATE, COUNT(*) AS NO_OF_SAMPLES, CAST(ROUND((((SUM(SAMPLE_T1_COUNT_0)*1.000)  / (COUNT(*)*1.000)) * 100.000),2) AS DECIMAL(10, 2)) AS PERCENTAGE_T1S\n");

		}
		
		if (driver.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
		{
			q2.applyWhat("SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT,CONTAINER_CODE,PRODUCT_GROUP, PROCESS_ORDER, MATERIAL, MIN(SAMPLE_SIZE) AS SAMPLE_SIZE, AVG(SAMPLE_NET_WEIGHT) AS MEAN_WEIGHT,\n" + 
					" STDEV(SAMPLE_NET_WEIGHT) AS STANDARD_DEVIATION, SUM(SAMPLE_T1_COUNT_0) AS TOTAL_T1S, SUM(SAMPLE_T2_COUNT_0) AS TOTAL_T2S, MIN(SAMPLE_WEIGHT_DATE) AS FIRST_WEIGHT_DATE,\n" + 
					" MAX(SAMPLE_WEIGHT_DATE) AS LAST_WEIGHT_DATE, COUNT(*) AS NO_OF_SAMPLES, (SUM(SAMPLE_T1_COUNT_0)*1.000)  / ((COUNT(*)*1.000)) AS PERCENTAGE_T1S\n");
		}
		
		if (driver.equals("com.mysql.cj.jdbc.Driver"))
		{
			q2.applyWhat("SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT,CONTAINER_CODE,PRODUCT_GROUP, PROCESS_ORDER, MATERIAL, MIN(SAMPLE_SIZE) AS SAMPLE_SIZE, AVG(SAMPLE_NET_WEIGHT) AS MEAN_WEIGHT,\n" + 
					" STDDEV(SAMPLE_NET_WEIGHT) AS STANDARD_DEVIATION, SUM(SAMPLE_T1_COUNT_0) AS TOTAL_T1S, SUM(SAMPLE_T2_COUNT_0) AS TOTAL_T2S, MIN(SAMPLE_WEIGHT_DATE) AS FIRST_WEIGHT_DATE,\n" + 
					" MAX(SAMPLE_WEIGHT_DATE) AS LAST_WEIGHT_DATE, COUNT(*) AS NO_OF_SAMPLES, CAST(ROUND((((SUM(SAMPLE_T1_COUNT_0)*1.000)  / (COUNT(*)*1.000)) * 100.000),2) AS DECIMAL(10, 2)) AS PERCENTAGE_T1S\n");
		}
		
		q2.applyFrom("view_weight_samples\n");

		q2.applyWhere("NOMINAL_WEIGHT > ", 0);
		
		if (checkBox4jFromEnabled.isSelected())
		{
			q2.applyWhere("sample_date>=", JUtility.getTimestampFromDate(sampleDateFrom.getDate()));
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("Mean above Nominal"))
		{
			q2.applyWhereLiteral(" sample_mean > nominal_weight ");
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("T1s or T2s"))
		{
			q2.applyWhereLiteral(" ((sample_t1_count > 0) or (sample_t2_count > 0)) ");
		}

		if (checkBox4jToEnabled.isSelected())
		{
			q2.applyWhere("sample_date<=", JUtility.getTimestampFromDate(sampleDateTo.getDate()));
		}

		if (fld_Material.getText().equals("") == false)
		{
			q2.applyWhere("material = ", fld_Material.getText());
		}

		if (fld_Process_Order.getText().equals("") == false)
		{
			q2.applyWhere("process_order = ", fld_Process_Order.getText());
		}

		if (fld_SamplePoint.getText().equals("") == false)
		{
			q2.applyIn("sample_point in ", selectedSamplePoints);
		}

		if (fld_Product_Group.getText().equals("") == false)
		{
			q2.applyWhere("product_group = ", fld_Product_Group.getText());
		}

		if (checkBox4j_T1.isSelected())
		{
			q2.applyWhere("sample_t1_count > ", 0);
		}

		if (checkBox4j_T2.isSelected())
		{
			q2.applyWhere("sample_t2_count > ", 0);
		}
		
		q2.setSQLFinal(" GROUP BY SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT,CONTAINER_CODE,PRODUCT_GROUP, PROCESS_ORDER, MATERIAL\n" + 
				"	ORDER BY SAMPLE_DATE_SHORT, NOMINAL_WEIGHT, SAMPLE_POINT,CONTAINER_CODE,PRODUCT_GROUP, PROCESS_ORDER, MATERIAL");
		
		q2.applySQL();
		result = q2.getPreparedStatement();

		return result;
	}	


	private PreparedStatement buildSQLsummary_overview()
	{

		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);

		String driver = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver();

		if (driver.equals("oracle.jdbc.driver.OracleDriver"))
		{
			q2.applyWhat("	SAMPLE_POINT,	SAMPLE_DATE,	USER_ID,	WORKSTATION_ID,	SCALE_ID,	PROCESS_ORDER,	REQUIRED_RESOURCE,	CUSTOMER_ID,	MATERIAL,	PRODUCT_GROUP,	CONTAINER_CODE,\n"
					+ "	TARE_WEIGHT,	TARE_WEIGHT_UOM,	NOMINAL_WEIGHT,	NOMINAL_WEIGHT_UOM,	TNE,	NEG_T1,	NEG_T2,	SAMPLE_SIZE,	SAMPLE_COUNT,	SAMPLE_MEAN,	SAMPLE_STD_DEV,\n"
					+ "	SAMPLE_T1_COUNT,	SAMPLE_T2_COUNT,	RUNNINGTOTAL,	COUNT_TOTAL,	T1_TOTAL,	T2_TOTAL,	MEAN_TOTAL,	COUNTER,	ROUND( RUNNINGTOTAL / COUNTER, 3 ) AS RUNNINGMEAN \n" + " FROM\n" + "	(\n"
					+ "	SELECT	SAMPLE_POINT,	SAMPLE_DATE,	USER_ID,	WORKSTATION_ID,	SCALE_ID,	PROCESS_ORDER,	REQUIRED_RESOURCE,	CUSTOMER_ID,	MATERIAL,	PRODUCT_GROUP,CONTAINER_CODE,\n"
					+ "		TARE_WEIGHT,	TARE_WEIGHT_UOM,	NOMINAL_WEIGHT,	NOMINAL_WEIGHT_UOM,	TNE, NEG_T1,NEG_T2,SAMPLE_SIZE,	SAMPLE_COUNT,	SAMPLE_MEAN,	SAMPLE_STD_DEV,	SAMPLE_T1_COUNT,SAMPLE_T2_COUNT,\n"
					+ "		SUM( SAMPLE_MEAN )     OVER ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY SAMPLE_POINT,PROCESS_ORDER, SAMPLE_DATE ) AS RUNNINGTOTAL,\n"
					+ "		SUM( SAMPLE_COUNT )    OVER ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY SAMPLE_POINT,PROCESS_ORDER, SAMPLE_DATE ) AS COUNT_TOTAL,\n"
					+ "	    SUM( SAMPLE_T1_COUNT ) OVER ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY SAMPLE_POINT,PROCESS_ORDER, SAMPLE_DATE ) AS T1_TOTAL,\n"
					+ "	    SUM( SAMPLE_T2_COUNT ) OVER ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY SAMPLE_POINT,PROCESS_ORDER, SAMPLE_DATE ) AS T2_TOTAL,\n"
					+ "		AVG( SAMPLE_MEAN )     OVER ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY SAMPLE_POINT,PROCESS_ORDER, SAMPLE_DATE ) AS MEAN_TOTAL,\n"
					+ "		ROW_NUMBER ( )         OVER ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY SAMPLE_POINT,PROCESS_ORDER, SAMPLE_DATE ) AS COUNTER");

		}
		else
		{

			q2.applyWhat(" YYY.*,\n" + "	round( YYY.RUNNINGTOTAL / YYY.COUNTER, 3 ) AS RUNNINGMEAN \n" + "   FROM\n" + "	(\n" + "	SELECT\n" + "		*,\n"
					+ "		sum( sample_mean ) over ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY sample_point,process_order, sample_date ) AS RUNNINGTOTAL,\n"
					+ "		sum( sample_count ) over ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY sample_point,process_order, sample_date ) AS COUNT_TOTAL,\n"
					+ "		sum( sample_t1_count ) over ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY sample_point,process_order, sample_date ) AS T1_TOTAL,\n"
					+ "		sum( sample_t2_count ) over ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY sample_point,process_order, sample_date ) AS T2_TOTAL,\n"
					+ "		AVG( sample_mean ) over ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY sample_point,process_order, sample_date ) AS MEAN_TOTAL,\n"
					+ "		row_number () over ( PARTITION BY SAMPLE_POINT,PROCESS_ORDER ORDER BY sample_point,process_order, sample_date ) AS COUNTER ");
		}

		q2.applyFrom("APP_WEIGHT_SAMPLE_HEADER");

		if (checkBox4jFromEnabled.isSelected())
		{
			q2.applyWhere("sample_date>=", JUtility.getTimestampFromDate(sampleDateFrom.getDate()));
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("Mean above Nominal"))
		{
			q2.applyWhereLiteral(" sample_mean > nominal_weight ");
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("T1s or T2s"))
		{
			q2.applyWhereLiteral(" ((sample_t1_count > 0) or (sample_t2_count > 0)) ");
		}

		if (checkBox4jToEnabled.isSelected())
		{
			q2.applyWhere("sample_date<=", JUtility.getTimestampFromDate(sampleDateTo.getDate()));
		}

		if (fld_Material.getText().equals("") == false)
		{
			q2.applyWhere("material = ", fld_Material.getText());
		}

		if (fld_Process_Order.getText().equals("") == false)
		{
			q2.applyWhere("process_order = ", fld_Process_Order.getText());
		}

		if (fld_SamplePoint.getText().equals("") == false)
		{
			q2.applyIn("sample_point in ", selectedSamplePoints);
		}

		if (fld_Product_Group.getText().equals("") == false)
		{
			q2.applyWhere("product_group = ", fld_Product_Group.getText());
		}

		if (checkBox4j_T1.isSelected())
		{
			q2.applyWhere("sample_t1_count > ", 0);
		}

		if (checkBox4j_T2.isSelected())
		{
			q2.applyWhere("sample_t2_count > ", 0);
		}

		if (driver.equals("oracle.jdbc.driver.OracleDriver"))
		{
			q2.setSQLFinal(" ) ORDER BY SAMPLE_POINT,SAMPLE_DATE");
		}
		else
		{
			q2.setSQLFinal(" ) AS YYY  ORDER BY SAMPLE_POINT,SAMPLE_DATE");
		}

		q2.applySQL();
		result = q2.getPreparedStatement();

		return result;
	}

	private PreparedStatement buildSQLr()
	{

		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		q2.applyWhat("*");

		q2.applyFrom("{schema}APP_WEIGHT_SAMPLE_HEADER");

		if (checkBox4jFromEnabled.isSelected())
		{
			q2.applyWhere("sample_date>=", JUtility.getTimestampFromDate(sampleDateFrom.getDate()));
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("Mean above Nominal"))
		{
			q2.applyWhereLiteral(" sample_mean > nominal_weight ");
		}

		if (jComboBoxReportType.getSelectedItem().toString().equals("T1s or T2s"))
		{
			q2.applyWhereLiteral(" ((sample_t1_count > 0) or (sample_t2_count > 0)) ");
		}

		if (checkBox4jToEnabled.isSelected())
		{
			q2.applyWhere("sample_date<=", JUtility.getTimestampFromDate(sampleDateTo.getDate()));
		}

		if (fld_Material.getText().equals("") == false)
		{
			q2.applyWhere("material = ", fld_Material.getText());
		}

		if (fld_Process_Order.getText().equals("") == false)
		{
			q2.applyWhere("process_order = ", fld_Process_Order.getText());
		}

		if (fld_SamplePoint.getText().equals("") == false)
		{
			q2.applyIn("sample_point in ", selectedSamplePoints);
		}

		if (fld_Product_Group.getText().equals("") == false)
		{
			q2.applyWhere("product_group = ", fld_Product_Group.getText());
		}

		if (checkBox4j_T1.isSelected())
		{
			q2.applyWhere("sample_t1_count > ", 0);
		}

		if (checkBox4j_T2.isSelected())
		{
			q2.applyWhere("sample_t2_count > ", 0);
		}

		q2.applySort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		q2.applyRestriction(jCheckBoxLimit.isSelected(), jSpinnerLimit.getValue());
		q2.applySQL();
		result = q2.getPreparedStatement();

		return result;
	}

	private void clearFilter()
	{

		fld_Process_Order.setText("");
		fld_Material.setText("");
		fld_Product_Group.setText("");
		fld_Container_Code.setText("");
		checkBox4jFromEnabled.setSelected(false);
		checkBox4jToEnabled.setSelected(false);
		sampleDateFrom.setEnabled(false);
		button_CalendarSampleDateFrom.setEnabled(false);
		sampleDateTo.setEnabled(false);
		button_CalendarSampleDateTo.setEnabled(false);
		checkBox4j_T1.setSelected(false);
		checkBox4j_T2.setSelected(false);
		selectedSamplePoints.clear();
		fld_SamplePoint.setText("");
		fld_SamplePoint.setToolTipText("None");

		search();
	}

	private void excel()
	{
		JDBWTSampleHeader sampleHeader = new JDBWTSampleHeader(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.setExcelRowLimit(jCheckBoxLimit, jSpinnerLimit);
		buildSQL();
		export.saveAs("weight_samples.xls", sampleHeader.getSampleHeaderDataResultSet(listStatement), Common.mainForm);
		populateList();
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 1036, 671);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Weight Check Reports");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(483, 266));

				{
					btn_Help = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(btn_Help);
					btn_Help.setText(lang.get("btn_Help"));
					btn_Help.setMnemonic(java.awt.event.KeyEvent.VK_H);
					btn_Help.setBounds(718, 150, 140, 32);
				}
				{
					btn_Close = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(btn_Close);
					btn_Close.setText(lang.get("btn_Close"));
					btn_Close.setMnemonic(java.awt.event.KeyEvent.VK_C);
					btn_Close.setBounds(861, 150, 140, 32);
					btn_Close.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							JDBQuery.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					lbl_Process_Order = new JLabel4j_std();
					jDesktopPane1.add(lbl_Process_Order);
					lbl_Process_Order.setText(lang.get("lbl_Process_Order"));
					lbl_Process_Order.setBounds(0, 63, 120, 25);
					lbl_Process_Order.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					fld_Process_Order.setFont(Common.font_input);

					jDesktopPane1.add(fld_Process_Order);
					fld_Process_Order.setBounds(128, 63, 107, 25);
				}
				{
					lbl_Material = new JLabel4j_std();
					jDesktopPane1.add(lbl_Material);
					lbl_Material.setText(lang.get("lbl_Material"));
					lbl_Material.setBounds(279, 63, 94, 25);
					lbl_Material.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{

					jDesktopPane1.add(fld_Material);
					fld_Material.setBounds(379, 63, 93, 25);
				}

				{
					btn_Process_Order_Lookup = new JButton4j(Common.icon_lookup_16x16);
					btn_Process_Order_Lookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{

							JLaunchLookup.dlgCriteriaDefault = "Ready";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.processOrders())
							{
								fld_Process_Order.setText(JLaunchLookup.dlgResult);
							}
						}
					});

					btn_Process_Order_Lookup.setBounds(236, 63, 21, 25);
					jDesktopPane1.add(btn_Process_Order_Lookup);
				}

				{
					btn_SamplePoint_Lookup = new JButton4j(Common.icon_lookup_16x16);
					btn_SamplePoint_Lookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JDialogSamplePointSelect sps = new JDialogSamplePointSelect(selectedSamplePoints);
							sps.setVisible(true);
							selectedSamplePoints = sps.getSelected();
							
							fld_SamplePoint.setText(String.valueOf(selectedSamplePoints.size())+" selected.");
							
							if (selectedSamplePoints.size()>0)
							{

								String tooltiptext = "<html>";
								
								for (int x=0;x<selectedSamplePoints.size();x++)
								{
									tooltiptext = tooltiptext + "<strong>" + selectedSamplePoints.get(x)+"</strong><br>";
								}
								tooltiptext = tooltiptext + "</html>";
								
								fld_SamplePoint.setToolTipText(tooltiptext);
							}
							else
							{
								fld_SamplePoint.setToolTipText("None");
							}

						}
					});
					btn_SamplePoint_Lookup.setBounds(236, 14, 21, 25);
					jDesktopPane1.add(btn_SamplePoint_Lookup);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setFont(new Font("Arial", Font.PLAIN, 11));
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 614, 1006, 21);
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusText);
				}

				JLabel4j_std lbl_SamplePoint = new JLabel4j_std();
				lbl_SamplePoint.setText(lang.get("lbl_SamplePoint"));
				lbl_SamplePoint.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_SamplePoint.setBounds(0, 14, 122, 25);
				jDesktopPane1.add(lbl_SamplePoint);
				fld_SamplePoint.setBounds(128, 14, 107, 25);
				jDesktopPane1.add(fld_SamplePoint);

				JLabel4j_std lbl_Product_Group = new JLabel4j_std();
				lbl_Product_Group.setText(lang.get("lbl_Product_Group"));
				lbl_Product_Group.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Product_Group.setBounds(748, 14, 107, 25);
				jDesktopPane1.add(lbl_Product_Group);

				fld_Product_Group.setBounds(861, 14, 94, 25);
				jDesktopPane1.add(fld_Product_Group);

				JLabel4j_std lbl_Container_Code = new JLabel4j_std();
				lbl_Container_Code.setText(lang.get("lbl_Container_Code"));
				lbl_Container_Code.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Container_Code.setBounds(748, 63, 107, 25);
				jDesktopPane1.add(lbl_Container_Code);

				fld_Container_Code.setBounds(861, 63, 94, 25);
				jDesktopPane1.add(fld_Container_Code);

				JLabel4j_std lbl_Sample_Date = new JLabel4j_std();
				lbl_Sample_Date.setText(lang.get("lbl_Sample_Date"));
				lbl_Sample_Date.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Sample_Date.setBounds(261, 14, 120, 25);
				jDesktopPane1.add(lbl_Sample_Date);
				checkBox4jFromEnabled.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{

						if (checkBox4jFromEnabled.isSelected())
						{
							sampleDateFrom.setEnabled(true);
							button_CalendarSampleDateFrom.setEnabled(true);
						}
						else
						{
							sampleDateFrom.setEnabled(false);
							button_CalendarSampleDateFrom.setEnabled(false);
						}
					}
				});

				checkBox4jFromEnabled.setBackground(Color.WHITE);
				checkBox4jFromEnabled.setBounds(391, 14, 21, 25);
				jDesktopPane1.add(checkBox4jFromEnabled);

				sampleDateFrom.setEnabled(false);
				sampleDateFrom.setBounds(415, 14, 128, 25);
				jDesktopPane1.add(sampleDateFrom);
				checkBox4jToEnabled.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (checkBox4jToEnabled.isSelected())
						{
							sampleDateTo.setEnabled(true);
							button_CalendarSampleDateTo.setEnabled(true);
						}
						else
						{
							sampleDateTo.setEnabled(false);
							button_CalendarSampleDateTo.setEnabled(false);
						}
					}
				});

				checkBox4jToEnabled.setBackground(Color.WHITE);
				checkBox4jToEnabled.setBounds(576, 14, 21, 25);
				jDesktopPane1.add(checkBox4jToEnabled);

				sampleDateTo.setEnabled(false);
				sampleDateTo.setBounds(603, 14, 128, 25);
				jDesktopPane1.add(sampleDateTo);

				scrollPane.setBounds(0, 194, 1014, 408);
				jDesktopPane1.add(scrollPane);

				tableResults = new JTable4j();
				tableResults.setToolTipText("Double click on row to see individual samples");
				tableResults.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{

						if (tableResults.getSelectedRowCount() == 1)
						{
							if (e.getClickCount() == 2)
							{
								if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_REPORTS_DETAILS") == true)
								{
									sampleDetail();
								}
							}
						}
					}
				});
				scrollPane.setViewportView(tableResults);

				JButton4j btn_Search = new JButton4j(Common.icon_search_16x16);
				btn_Search.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						search();
					}
				});

				btn_Search.setText(lang.get("btn_Search"));
				btn_Search.setMnemonic(KeyEvent.VK_S);
				btn_Search.setBounds(3, 150, 140, 32);
				jDesktopPane1.add(btn_Search);

				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Sort_By"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(405, 115, 69, 21);
				jDesktopPane1.add(label4j_std);

				ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[]
				{ "SAMPLE_POINT,PROCESS_ORDER,SAMPLE_DATE", "SAMPLE_POINT,SAMPLE_DATE", "SAMPLE_POINT,PRODUCT_GROUP,SAMPLE_DATE" });
				jComboBoxSortBy = new JComboBox4j<String>();
				jComboBoxSortBy.setMaximumRowCount(15);
				jComboBoxSortBy.setModel(jComboBoxSortByModel);
				jComboBoxSortBy.setBounds(480, 114, 328, 22);
				jDesktopPane1.add(jComboBoxSortBy);
				jToggleButtonSequence.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setSequence(jToggleButtonSequence.isSelected());
					}
				});

				JLabel4j_std label4j_std_report_type = new JLabel4j_std();
				label4j_std_report_type.setText(lang.get("mod_FRM_WEIGHT_REPORTS"));
				label4j_std_report_type.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std_report_type.setBounds(3, 114, 117, 21);
				jDesktopPane1.add(label4j_std_report_type);

				ComboBoxModel<String> jComboBoxReportTypeModel = new DefaultComboBoxModel<String>(new String[]
				{ "Quick Reference 1","Quick Reference 2","Quick Reference 3", "Overview Weight Report by Sample Point", "Summary Weight Report by Sample Point", "Individual Weight Check Report by Sample Point", "Search Results", "Mean above Nominal", "T1s or T2s" });
				jComboBoxReportType = new JComboBox4j<String>();
				jComboBoxReportType.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (jComboBoxReportType.getSelectedItem().toString().equals("Overview Weight Report by Sample Point"))
						{
							jComboBoxSortBy.setSelectedItem("SAMPLE_POINT,PROCESS_ORDER,SAMPLE_DATE");
							jComboBoxSortBy.setEnabled(false);
						}
						if (jComboBoxReportType.getSelectedItem().toString().equals("Summary Weight Report by Sample Point"))
						{
							jComboBoxSortBy.setEnabled(false);
						}
						if (jComboBoxReportType.getSelectedItem().toString().equals("Individual Weight Check Report by Sample Point"))
						{
							jComboBoxSortBy.setEnabled(false);

						}
						if (jComboBoxReportType.getSelectedItem().toString().equals("Quick Reference 1"))
						{
							jComboBoxSortBy.setEnabled(false);

						}
						if (jComboBoxReportType.getSelectedItem().toString().equals("Quick Reference 2"))
						{
							jComboBoxSortBy.setEnabled(false);

						}
						if (jComboBoxReportType.getSelectedItem().toString().equals("Quick Reference 3"))
						{
							jComboBoxSortBy.setEnabled(false);

						}						
						if (jComboBoxReportType.getSelectedItem().toString().equals("Mean above Nominal"))
						{
							jComboBoxSortBy.setEnabled(true);

						}
						if (jComboBoxReportType.getSelectedItem().toString().equals("T1s or T2s"))
						{
							jComboBoxSortBy.setEnabled(true);

						}
						if (jComboBoxReportType.getSelectedItem().toString().equals("Search Results"))
						{
							jComboBoxSortBy.setEnabled(true);

						}
					}
				});
				jComboBoxReportType.setMaximumRowCount(15);
				jComboBoxReportType.setModel(jComboBoxReportTypeModel);
				jComboBoxReportType.setBounds(128, 113, 273, 22);
				jComboBoxReportType.setSelectedItem("Search Results");
				jDesktopPane1.add(jComboBoxReportType);
				jToggleButtonSequence.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setSequence(jToggleButtonSequence.isSelected());
					}
				});

				jToggleButtonSequence.setSelected(false);
				jToggleButtonSequence.setBounds(808, 114, 21, 22);
				jDesktopPane1.add(jToggleButtonSequence);

				JLabel4j_std label4j_std_1 = new JLabel4j_std();
				label4j_std_1.setText(lang.get("lbl_Limit"));
				label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std_1.setBounds(807, 117, 74, 21);
				jDesktopPane1.add(label4j_std_1);

				jCheckBoxLimit = new JCheckBox4j();
				jCheckBoxLimit.setSelected(true);
				jCheckBoxLimit.setBackground(Color.WHITE);
				jCheckBoxLimit.setBounds(883, 117, 21, 21);
				jDesktopPane1.add(jCheckBoxLimit);

				{
					SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
					jSpinnerIntModel.setMinimum(1);
					jSpinnerIntModel.setMaximum(5000);
					jSpinnerIntModel.setStepSize(1);
					JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
					ne.getTextField().setFont(Common.font_std);
					jSpinnerLimit.setEditor(ne);
					jSpinnerLimit.setModel(jSpinnerIntModel);
					jSpinnerLimit.setBounds(906, 116, 68, 21);
					jSpinnerLimit.setValue(1000);
					jSpinnerLimit.getEditor().setSize(45, 21);
					jDesktopPane1.add(jSpinnerLimit);
				}

				button_CalendarSampleDateFrom = new JCalendarButton(sampleDateFrom);
				button_CalendarSampleDateFrom.setEnabled(false);
				button_CalendarSampleDateFrom.setBounds(542, 18, 21, 21);
				jDesktopPane1.add(button_CalendarSampleDateFrom);

				button_CalendarSampleDateTo = new JCalendarButton(sampleDateTo);
				button_CalendarSampleDateTo.setEnabled(false);
				button_CalendarSampleDateTo.setBounds(732, 18, 21, 21);
				jDesktopPane1.add(button_CalendarSampleDateTo);

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
					jButtonClear.setBounds(146, 150, 140, 32);
					jDesktopPane1.add(jButtonClear);
				}

				{
					jButtonDetails = new JButton4j(Common.icon_details_16x16);
					jButtonDetails.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_REPORTS_DETAILS"));
					jButtonDetails.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							sampleDetail();
						}
					});
					jButtonDetails.setText(lang.get("btn_Details"));
					jButtonDetails.setBounds(432, 150, 140, 32);
					jDesktopPane1.add(jButtonDetails);
				}

				{
					jButtonReports = new JButton4j(Common.icon_report_16x16);
					jButtonReports.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							print();
						}
					});
					jButtonReports.setText(lang.get("btn_Print"));
					jButtonReports.setBounds(289, 150, 140, 32);
					jDesktopPane1.add(jButtonReports);
				}

				JButton4j btn_LookupProductGroup = new JButton4j(Common.icon_lookup_16x16);
				btn_LookupProductGroup.setBounds(954, 14, 21, 25);
				jDesktopPane1.add(btn_LookupProductGroup);
				btn_LookupProductGroup.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "";
						if (JLaunchLookup.wtProductGroups())
						{

							fld_Product_Group.setText(JLaunchLookup.dlgResult);
						}
					}
				});

				JButton4j btn_LookupContainerCode = new JButton4j(Common.icon_lookup_16x16);
				btn_LookupContainerCode.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "";
						if (JLaunchLookup.wtContainerCode())
						{

							fld_Container_Code.setText(JLaunchLookup.dlgResult);

						}
					}
				});
				btn_LookupContainerCode.setBounds(954, 63, 21, 25);
				jDesktopPane1.add(btn_LookupContainerCode);

				{
					jButtonMaterialLookup = new JButton4j(Common.icon_lookup_16x16);
					jButtonMaterialLookup.setBounds(471, 63, 21, 25);
					jDesktopPane1.add(jButtonMaterialLookup);
					jButtonMaterialLookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.materials())
							{
								fld_Material.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.setBounds(575, 150, 140, 32);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							excel();
						}
					});
					jButtonExcel.setMnemonic(KeyEvent.VK_H);
					jButtonExcel.setText(lang.get("btn_Excel"));
					jDesktopPane1.add(jButtonExcel);
				}
				checkBox4j_T1.setBackground(Color.WHITE);
				checkBox4j_T1.setBounds(610, 63, 21, 25);
				jDesktopPane1.add(checkBox4j_T1);
				checkBox4j_T2.setBackground(Color.WHITE);
				checkBox4j_T2.setBounds(670, 63, 21, 25);
				jDesktopPane1.add(checkBox4j_T2);

				JLabel4j_std label4j_T1 = new JLabel4j_std();
				label4j_T1.setText("T2");
				label4j_T1.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_T1.setBounds(645, 63, 21, 25);
				jDesktopPane1.add(label4j_T1);

				JLabel4j_std label4j_T2 = new JLabel4j_std();
				label4j_T2.setText("T1");
				label4j_T2.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_T2.setBounds(553, 63, 52, 25);
				jDesktopPane1.add(label4j_T2);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void print()
	{
		if (jComboBoxReportType.getSelectedItem().equals("Quick Reference 1"))
		{
			PreparedStatement temp = buildSQLquickref1();
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("p_title", jComboBoxReportType.getSelectedItem().toString());
			JLaunchReport.runReport("RPT_WT_QUICKREF1", parameters, "", temp, "");
		}
		else
			{
			if (jComboBoxReportType.getSelectedItem().equals("Quick Reference 2"))
			{
				PreparedStatement temp = buildSQLquickref2();
				HashMap<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("p_title", jComboBoxReportType.getSelectedItem().toString());
				JLaunchReport.runReport("RPT_WT_QUICKREF2", parameters, "", temp, "");
			}
			else
			{
				if (jComboBoxReportType.getSelectedItem().equals("Quick Reference 3"))
				{
					PreparedStatement temp = buildSQLquickref3();
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("p_title", jComboBoxReportType.getSelectedItem().toString());
					JLaunchReport.runReport("RPT_WT_QUICKREF3", parameters, "", temp, "");
				}
				else
				{
					if (jComboBoxReportType.getSelectedItem().equals("Individual Weight Check Report by Sample Point"))
					{
						PreparedStatement temp = buildSQLindividual();
						HashMap<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("p_title", jComboBoxReportType.getSelectedItem().toString());
						JLaunchReport.runReport("RPT_WT_INDIVIDUAL", parameters, "", temp, "");
					}
					else
					{
						if (jComboBoxReportType.getSelectedItem().equals("Summary Weight Report by Sample Point"))
						{
							HashMap<String, Object> parameters = new HashMap<String, Object>();
							parameters.put("p_title", jComboBoxReportType.getSelectedItem().toString());
							PreparedStatement temp = buildSQLsummary_overview();
							JLaunchReport.runReport("RPT_WT_SUMMARY", parameters, "", temp, "");
						}
						else
						{
							if (jComboBoxReportType.getSelectedItem().equals("Overview Weight Report by Sample Point"))
							{
								HashMap<String, Object> parameters = new HashMap<String, Object>();
								parameters.put("p_title", jComboBoxReportType.getSelectedItem().toString());
								PreparedStatement temp = buildSQLsummary_overview();
								JLaunchReport.runReport("RPT_WT_OVERVIEW", parameters, "", temp, "");
							}
							else
							{
								HashMap<String, Object> parameters = new HashMap<String, Object>();
								parameters.put("p_title", jComboBoxReportType.getSelectedItem().toString());
								PreparedStatement temp = buildSQLr();
								JLaunchReport.runReport("RPT_WT_OVERVIEW", parameters, "", temp, "");
							}
						}
					}
				}
			}
		}
	}

	private void populateList()
	{
		JDBWTSampleHeader sampleHeader = new JDBWTSampleHeader(Common.selectedHostID, Common.sessionID);

		JDBWeightHeaderTableModel sampleTable = new JDBWeightHeaderTableModel(sampleHeader.getSampleHeaderDataResultSet(listStatement));

		TableRowSorter<JDBWeightHeaderTableModel> sorter = new TableRowSorter<JDBWeightHeaderTableModel>(sampleTable);

		tableResults.setRowSorter(sorter);

		this.setIconifiable(true);
		tableResults.setModel(sampleTable);

		scrollPane.setViewportView(tableResults);
		JUtility.scrolltoHomePosition(scrollPane);

		tableResults.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.SamplePoint_Col).setPreferredWidth(80);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.SampleDate_Col).setPreferredWidth(145);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.ProductGroup_Col).setPreferredWidth(120);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.ContainerCode_Col).setPreferredWidth(70);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.ProcessOrder_Col).setPreferredWidth(80);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.Material_Col).setPreferredWidth(80);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.NominalWeight_Col).setPreferredWidth(80);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.NominalWeightUom_Col).setPreferredWidth(40);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.SampleMean_Col).setPreferredWidth(85);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.SampleStdDev_Col).setPreferredWidth(80);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.SampleT1Count_Col).setPreferredWidth(65);
		tableResults.getColumnModel().getColumn(JDBWeightHeaderTableModel.SampleT2Count_Col).setPreferredWidth(65);

		scrollPane.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), sampleTable.getRowCount());

	}

	private void sampleDetail()
	{
		int row = tableResults.getSelectedRow();
		if (row >= 0)
		{
			String samplePoint = tableResults.getValueAt(row, JDBWeightHeaderTableModel.SamplePoint_Col).toString();
			String sampleDate = tableResults.getValueAt(row, JDBWeightHeaderTableModel.SampleDate_Col).toString();
			JLaunchMenu.runDialog("FRM_WEIGHT_REPORTS_DETAILS", samplePoint, sampleDate);
		}

	}

	private void search()
	{
		buildSQL();
		populateList();
	}

	private void setSequence(boolean descending)
	{
		if (jToggleButtonSequence.isSelected() == true)
		{
			// rbdescending.setSelected(true);
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending_16x16);
		}
		else
		{
			// rbascending.setSelected(true);
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending_16x16);
		}
	}
}
