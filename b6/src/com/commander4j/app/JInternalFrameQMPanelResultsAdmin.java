package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameQMPanelResultsAdmin.java
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
import com.commander4j.db.JDBViewProductGroups;
import com.commander4j.db.JDBViewQMPanelResults;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.renderer.TableCellRenderer_PanelResults;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBViewQMPanelResultsTableModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The JInternalFrameWasteLogAdmin allows the user to view/edit the table
 * APP_WASTE_LOG table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWasteLogAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBWasteLog JDBWasteLog
 */
public class JInternalFrameQMPanelResultsAdmin extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JButton4j jButtonPrint;
	private JButton4j jButtonExcel;
	private JLabel4j_std jStatusText;
	private JLabel4j_std jLabelProcessOrder;

	private JCheckBox4j jCheckBoxPanelDateTo;
	private JCheckBox4j jCheckBoxPanelDateFrom;
	private JCheckBox4j jCheckBoxSampleDateTo;
	private JCheckBox4j jCheckBoxSampleDateFrom;
	private JLabel4j_std jLabelPanelDate;
	private JLabel4j_std jLabelSampleDate;
	private JButton4j jButtonLookupPlant;
	private JButton4j jButtonLookupFiller;

	private JButton4j jButtonLookupWasteMaterial;
	private JButton4j jButtonLookupPanelResult;
	private JButton4j jButtonLookupUserID;
	private JTextField4j jTextFieldProcessOrder;
	private JTextField4j jTextFieldUserID;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;

	private JButton4j jButtonClose;
	private JToggleButton jToggleButtonSequence;
	private JButton4j jButtonLookupProcessOrder;
	private JTextField4j jTextFieldPlant;

	private JComboBox4j<String> jComboBoxStatus;

	private JLabel4j_std jLabelStatus;

	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabelSortby;
	private JLabel4j_std jLabelPlant;

	private JTextField4j jTextFieldMaterial;
	private JTextField4j jTextFieldPanelResult;
	private JTextField4j jTextFieldPanelID;
	private JTextField4j jTextFieldTrayID;
	private JTextField4j jTextFieldSampleID;
	private JTextField4j jTextFieldFillerID;
	private JLabel4j_std jLabelMaterial;
	private JLabel4j_std jLabelPanelResult;
	private JLabel4j_std jLabelPanelID;
	private JLabel4j_std jLabelReportType;
	private JLabel4j_std jLabelTrayID;
	private JLabel4j_std jLabelSampleID;
	private JLabel4j_std jLabelFillerID;
	private JLabel4j_std jLabelUser;
	private JTable4j jTable1;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSearch;
	private JScrollPane jScrollPane1;
	private String lref;
	private String ltransaction;
	private static boolean dlg_sort_descending = true;
	private JDateControl panelDateFrom = new JDateControl();
	private JDateControl panelDateTo = new JDateControl();
	private JDateControl sampleDateFrom = new JDateControl();
	private JDateControl sampleDateTo = new JDateControl();
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private JCalendarButton calendarButtonPanelDateFrom;
	private JCalendarButton calendarButtonPanelDateTo;
	private JCalendarButton calendarButtonSampleDateFrom;
	private JCalendarButton calendarButtonSampleDateTo;
	private JCheckBox4j jCheckBoxLimit = new JCheckBox4j();
	private JDBViewProductGroups d = new JDBViewProductGroups(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<JDBViewProductGroups> comboBoxProductGroups = new JComboBox4j<JDBViewProductGroups>();
	private Vector<JDBViewProductGroups> productGroupList = new Vector<JDBViewProductGroups>();
	private JSpinner jSpinnerLimit = new JSpinner();

	private DefaultComboBoxModel<String> sortFieldsFriendly;
	private LinkedList<String> sortFieldsSQL;
	private boolean emptyresult = true;
	private JDBQuery qSearch = new JDBQuery(Common.selectedHostID, Common.sessionID);
	private JDBQuery qPrint = new JDBQuery(Common.selectedHostID, Common.sessionID);
	private JDBQuery qExcel = new JDBQuery(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<String> jComboBoxReportType;

	private void buildSortList()
	{
		sortFieldsFriendly = new DefaultComboBoxModel<String>();

		sortFieldsFriendly.addElement("Panel ID");

		sortFieldsFriendly.addElement("Panel Date");

		sortFieldsFriendly.addElement("Plant");
		
		sortFieldsFriendly.addElement("Result");
		
		sortFieldsFriendly.addElement("Process Order");
		
		sortFieldsFriendly.addElement("Material");
		
		sortFieldsFriendly.addElement("User ID");

		sortFieldsSQL = new LinkedList<String>();

		sortFieldsSQL.add("PANEL_ID,TRAY_ID,SAMPLE_ID");
		
		sortFieldsSQL.add("PANEL_DATE,TRAY_ID,SAMPLE_ID");

		sortFieldsSQL.add("PLANT,PANEL_ID,TRAY_ID,SAMPLE_ID");
		
		sortFieldsSQL.add("RESULT_DESCRIPTION,PANEL_ID,TRAY_ID,SAMPLE_ID");
		
		sortFieldsSQL.add("PROCESS_ORDER,PANEL_ID,TRAY_ID,SAMPLE_ID");
		
		sortFieldsSQL.add("MATERIAL,PANEL_ID,TRAY_ID,SAMPLE_ID");
		
		sortFieldsSQL.add("USER_ID,PANEL_ID,TRAY_ID,SAMPLE_ID");

		jComboBoxSortBy.setModel(sortFieldsFriendly);
		jComboBoxSortBy.setMaximumRowCount(sortFieldsSQL.size());
		
		productGroupList.add(new JDBViewProductGroups(Common.selectedHostID, Common.sessionID));
		productGroupList.addAll(d.getProductGroups());

	}

	public JInternalFrameQMPanelResultsAdmin()
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
				jTextFieldMaterial.requestFocus();
				jTextFieldMaterial.setCaretPosition(jTextFieldMaterial.getText().length());
			}
		});

		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_QM_PANEL_RESULTS"));
	

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);
	}

	private void clearFilter()
	{

		jTextFieldMaterial.setText("");

		jTextFieldPlant.setText("");
		
		
		jTextFieldPanelResult.setText("");
		jTextFieldPanelID.setText("");
		jTextFieldTrayID.setText("");
		jTextFieldSampleID.setText("");

		jTextFieldProcessOrder.setText("");

		jTextFieldUserID.setText("");

		panelDateTo.setEnabled(false);
		panelDateFrom.setEnabled(false);
		jCheckBoxPanelDateFrom.setSelected(false);
		jCheckBoxPanelDateTo.setSelected(false);
		calendarButtonPanelDateFrom.setEnabled(false);
		calendarButtonPanelDateTo.setEnabled(false);
		
		sampleDateTo.setEnabled(false);
		sampleDateFrom.setEnabled(false);
		jCheckBoxSampleDateFrom.setSelected(false);
		jCheckBoxSampleDateTo.setSelected(false);
		calendarButtonSampleDateFrom.setEnabled(false);
		calendarButtonSampleDateTo.setEnabled(false);

		jComboBoxStatus.setSelectedIndex(0);
		comboBoxProductGroups.setSelectedIndex(0);

		search();

	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			
			if (fieldname.equals(lang.get("lbl_Panel_ID"))== true)
			{
				jTextFieldPanelID.setText(jTable1.getValueAt(row, JDBViewQMPanelResultsTableModel.PanelID_Col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Tray_ID"))== true)
			{
				jTextFieldTrayID.setText(jTable1.getValueAt(row, JDBViewQMPanelResultsTableModel.TrayID_Col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Sample_ID"))== true)
			{
				jTextFieldSampleID.setText(jTable1.getValueAt(row, JDBViewQMPanelResultsTableModel.SampleID_Col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Process_Order")) == true)
			{
				jTextFieldProcessOrder.setText(jTable1.getValueAt(row, JDBViewQMPanelResultsTableModel.ProcessOrder_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Material")) == true)
			{
				jTextFieldMaterial.setText(jTable1.getValueAt(row, JDBViewQMPanelResultsTableModel.Material_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_User_ID")) == true)
			{
				jTextFieldUserID.setText(jTable1.getValueAt(row, JDBViewQMPanelResultsTableModel.UserID_Col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Panel_Date")) == true)
			{
				String dateString = jTable1.getValueAt(row, JDBViewQMPanelResultsTableModel.PanelDate_Col).toString();
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
				
				panelDateTo.setEnabled(true);
				panelDateFrom.setEnabled(true);
				jCheckBoxPanelDateFrom.setSelected(true);
				jCheckBoxPanelDateTo.setSelected(true);
				calendarButtonPanelDateFrom.setEnabled(true);
				calendarButtonPanelDateTo.setEnabled(true);
				cal.set(Calendar.SECOND, 0);
				panelDateFrom.setDate(cal.getTime());
				cal.set(Calendar.SECOND, 59);
				panelDateTo.setDate(cal.getTime());
				
			}
			
			if (fieldname.equals(lang.get("lbl_Sample_Date")) == true)
			{
				String dateString = jTable1.getValueAt(row, JDBViewQMPanelResultsTableModel.SampleDate_Col).toString();
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
				
				sampleDateTo.setEnabled(true);
				sampleDateFrom.setEnabled(true);
				jCheckBoxSampleDateFrom.setSelected(true);
				jCheckBoxSampleDateTo.setSelected(true);
				calendarButtonSampleDateFrom.setEnabled(true);
				calendarButtonSampleDateTo.setEnabled(true);
				cal.set(Calendar.SECOND, 0);
				sampleDateFrom.setDate(cal.getTime());
				cal.set(Calendar.SECOND, 59);
				sampleDateTo.setDate(cal.getTime());
			}
			
			if (fieldname.equals(lang.get("lbl_Plant")) == true)
			{
				jTextFieldPlant.setText(jTable1.getValueAt(row, JDBViewQMPanelResultsTableModel.Plant_Col).toString());
			}

			search();

		}
	}

	public JInternalFrameQMPanelResultsAdmin(String material)
	{
		this();
		lref = material;
		jTextFieldMaterial.setText(lref);
		jTextFieldPlant.setText(ltransaction);

		populateList();
	}

	private void search()
	{
		populateList();
	}
	
	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			String temp = jTable1.getValueAt(row, 2).toString();
			JLaunchMenu.runForm("FRM_QM_SAMPLE_EDIT", temp);
		}
	}

	private void excel()
	{
		JDBViewQMPanelResults sampleResults = new JDBViewQMPanelResults(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();

		export.saveAs("panel_results.xls", sampleResults.getPanelResultSet(buildSQL(qExcel)), Common.mainForm);
		populateList();
	}


	private void copyToClipboard(String fieldname)
	{
		StringSelection stringSelection = new StringSelection("");
		
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals("Panel ID") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 0).toString());
			}
			
			if (fieldname.equals("Tray ID") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 1).toString());
			}
			
			if (fieldname.equals("Sample ID") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 2).toString());
			}

			if (fieldname.equals("Result") == true)
			{				
				stringSelection = new StringSelection("[" + jTable1.getValueAt(row, 9).toString() + "] "+jTable1.getValueAt(row, 10).toString());
			}

			if (fieldname.equals("Process Order") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 11).toString());
			}
			
			if (fieldname.equals("Material") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 12).toString());
			}
			
			if (fieldname.equals("Sample Date") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 3).toString());
			}
			
			if (fieldname.equals("Panel Date") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 4).toString());
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

	public JInternalFrameQMPanelResultsAdmin(String material, String batch)
	{
		this();
		lref = material;
		ltransaction = batch;
		jTextFieldMaterial.setText(lref);
		jTextFieldPlant.setText(ltransaction);

		populateList();
	}

	private PreparedStatement buildSQL(JDBQuery query)
	{

		PreparedStatement result;
		
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}view_qm_panel_results"));
		
		
		if (emptyresult)
		{
			query.addParamtoSQL("1 = ", 2);
			emptyresult = false;
		}
		
		query.addParamtoSQL("material=", jTextFieldMaterial.getText());
		query.addParamtoSQL("process_order=", jTextFieldProcessOrder.getText());
		query.addParamtoSQL("user_id=", jTextFieldUserID.getText());


		if (jComboBoxStatus.getSelectedIndex() > 0)
		{
			query.addParamtoSQL("status = ",jComboBoxStatus.getSelectedItem().toString());
		}
		
		if (comboBoxProductGroups.getSelectedIndex() > 0)
		{
			query.addParamtoSQL("product_group = ",comboBoxProductGroups.getSelectedItem().toString());
		}


		if (jCheckBoxPanelDateFrom.isSelected())
		{
			query.addParamtoSQL("panel_date >=", JUtility.getTimestampFromDate(panelDateFrom.getDate()));

		}

		if (jCheckBoxPanelDateTo.isSelected())
		{
			query.addParamtoSQL("panel_date <=", JUtility.getTimestampFromDate(panelDateTo.getDate()));
		}
		
		if (jCheckBoxSampleDateFrom.isSelected())
		{
			query.addParamtoSQL("sample_date >=", JUtility.getTimestampFromDate(sampleDateFrom.getDate()));

		}

		if (jCheckBoxSampleDateTo.isSelected())
		{
			query.addParamtoSQL("sample_date <=", JUtility.getTimestampFromDate(sampleDateTo.getDate()));
		}

		if (jTextFieldPanelID.getText().isEmpty()==false)
		{
			query.addParamtoSQL("panel_id =", Long.valueOf(jTextFieldPanelID.getText()));
		}
		
		if (jTextFieldTrayID.getText().isEmpty()==false)
		{
			query.addParamtoSQL("tray_id =", Long.valueOf(jTextFieldTrayID.getText()));
		}
		
		if (jTextFieldSampleID.getText().isEmpty()==false)
		{
			query.addParamtoSQL("sample_id =", Long.valueOf(jTextFieldSampleID.getText()));
		}
		
		if (jTextFieldPlant.getText().isEmpty()==false)
		{
			query.addParamtoSQL("plant =",jTextFieldPlant.getText());
		}
		
		if (jTextFieldFillerID.getText().isEmpty()==false)
		{
			query.addParamtoSQL("filler =",jTextFieldFillerID.getText());
		}
		
		if (jTextFieldPanelResult.getText().isEmpty()==false)
		{
			String driver = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver();

			if (driver.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
			{
				query.addParamtoSQL("[value] = ", jTextFieldPanelResult.getText());
			}
			else
			{
				query.addParamtoSQL("value = ", jTextFieldPanelResult.getText());
			}
		}
		
		query.appendSort(sortFieldsSQL.get(jComboBoxSortBy.getSelectedIndex()), jToggleButtonSequence.isSelected());
		query.applyRestriction(jCheckBoxLimit.isSelected(), Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), jSpinnerLimit.getValue());

		query.bindParams();
		result = query.getPreparedStatement();
		return result;
	}

	private void populateList()
	{
		JDBViewQMPanelResults panelLog = new JDBViewQMPanelResults(Common.selectedHostID, Common.sessionID);
		JDBViewQMPanelResultsTableModel panelLogTable = new JDBViewQMPanelResultsTableModel(panelLog.getPanelResultSet(buildSQL(qSearch)));
		TableRowSorter<JDBViewQMPanelResultsTableModel> sorter = new TableRowSorter<JDBViewQMPanelResultsTableModel>(panelLogTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(panelLogTable);

		jScrollPane1.setViewportView(jTable1);
		jTable1.setDefaultRenderer(Object.class, new TableCellRenderer_PanelResults());
		JUtility.scrolltoHomePosition(jScrollPane1);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.PanelID_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.TrayID_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.SampleID_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.SampleDate_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.PanelDate_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.Status_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.TrayDescription_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.Plant_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.UserID_Col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.Result_Col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.ResultDescription_Col).setPreferredWidth(200);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.ProcessOrder_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.Material_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.UserData1_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.UserData2_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.UserData3_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.UserData4_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.FirstName_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewQMPanelResultsTableModel.Surname_Col).setPreferredWidth(80);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), panelLogTable.getRowCount());
	}


	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 994, 666);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Panel Results");

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
					jScrollPane1.setBounds(0, 239, 988, 376);
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
								final JMenuItem4j menuItemEdit = new JMenuItem4j(Common.icon_edit_16x16);
								menuItemEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_SAMPLE_EDIT"));
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
								final JMenu4j sortByMenu = new JMenu4j();
								sortByMenu.setText(lang.get("lbl_Sort_By"));
								popupMenu.add(sortByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("Panell ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Panel_ID"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("Panel Date");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Panel_Date"));
									sortByMenu.add(newItemMenuItem);
								}
								

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("Plant");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Plant"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("Result");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Result"));
									sortByMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("Process Order");
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
											sortBy("Material");
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
											sortBy("User ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_User_ID"));
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
									newItemMenuItem.setText(lang.get("lbl_Panel_ID"));
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
									newItemMenuItem.setText(lang.get("lbl_Tray_ID"));
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
									newItemMenuItem.setText(lang.get("lbl_Sample_ID"));
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
									newItemMenuItem.setText(lang.get("lbl_Process_Order"));
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
									newItemMenuItem.setText(lang.get("lbl_User_ID"));
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
									newItemMenuItem.setText(lang.get("lbl_Panel_Date"));
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
									newItemMenuItem.setText(lang.get("lbl_Sample_Date"));
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
									newItemMenuItem.setText(lang.get("lbl_Plant"));
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
											copyToClipboard("Panel ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Panel_ID"));
									clipboardByMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Tray ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Tray_ID"));
									clipboardByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Sample ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_SampleID"));
									clipboardByMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Result");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Result"));
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
											copyToClipboard("Sample Date");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Sample_Date"));
									clipboardByMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Panel Date");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Panel_Date"));
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
					jButtonSearch.setBounds(117, 195, 123, 32);
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
					jButtonHelp.setBounds(609, 195, 123, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setBounds(732, 195, 123, 32);
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
					jLabelMaterial = new JLabel4j_std();
					jDesktopPane1.add(jLabelMaterial);
					jLabelMaterial.setText(lang.get("lbl_Material"));
					jLabelMaterial.setBounds(289, 51, 126, 21);
					jLabelMaterial.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelPanelResult = new JLabel4j_std();
					jDesktopPane1.add(jLabelPanelResult);
					jLabelPanelResult.setText(lang.get("lbl_Result"));
					jLabelPanelResult.setBounds(-4, 88, 130, 21);
					jLabelPanelResult.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelPanelID = new JLabel4j_std();
					jDesktopPane1.add(jLabelPanelID);
					jLabelPanelID.setText(lang.get("lbl_Panel_ID"));
					jLabelPanelID.setBounds(0, 127, 126, 21);
					jLabelPanelID.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelReportType = new JLabel4j_std();
					jDesktopPane1.add(jLabelReportType);
					jLabelReportType.setText(lang.get("lbl_Module_Report_Type"));
					jLabelReportType.setBounds(0, 162, 126, 21);
					jLabelReportType.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelTrayID = new JLabel4j_std();
					jDesktopPane1.add(jLabelTrayID);
					jLabelTrayID.setText(lang.get("lbl_Tray_ID"));
					jLabelTrayID.setBounds(215, 125, 93, 21);
					jLabelTrayID.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelSampleID = new JLabel4j_std();
					jDesktopPane1.add(jLabelSampleID);
					jLabelSampleID.setText(lang.get("lbl_Sample_ID"));
					jLabelSampleID.setBounds(384, 127, 108, 21);
					jLabelSampleID.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelFillerID = new JLabel4j_std();
					jDesktopPane1.add(jLabelFillerID);
					jLabelFillerID.setText(lang.get("lbl_Filler_ID"));
					jLabelFillerID.setBounds(774, 51, 81, 21);
					jLabelFillerID.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBViewQMPanelResults.field_MaterialID);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(440, 50, 126, 22);
				}
				{
					jTextFieldPanelResult = new JTextField4j(JDBViewQMPanelResults.field_Result);
					jDesktopPane1.add(jTextFieldPanelResult);
					jTextFieldPanelResult.setBounds(134, 87, 126, 22);
				}
				{
					jTextFieldPanelID = new JTextField4j(JDBViewQMPanelResults.field_PanelID);
					jTextFieldPanelID.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
				             char c = e.getKeyChar();
				             if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
				                  e.consume();
				             }
						}
					});
					jDesktopPane1.add(jTextFieldPanelID);
					jTextFieldPanelID.setBounds(134, 127, 68, 22);
				}
				{
					jTextFieldTrayID = new JTextField4j(JDBViewQMPanelResults.field_TrayID);
					jDesktopPane1.add(jTextFieldTrayID);
					jTextFieldTrayID.setBounds(314, 126, 68, 22);
					jTextFieldTrayID.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
				             char c = e.getKeyChar();
				             if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
				                  e.consume();
				             }
						}
					});
				}
				{
					jTextFieldSampleID = new JTextField4j(JDBViewQMPanelResults.field_SampleID);
					jDesktopPane1.add(jTextFieldSampleID);
					jTextFieldSampleID.setBounds(498, 127, 68, 22);
					jTextFieldSampleID.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
				             char c = e.getKeyChar();
				             if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
				                  e.consume();
				             }
						}
					});
				}
				{
					jTextFieldFillerID = new JTextField4j(JDBViewQMPanelResults.field_SampleID);
					jDesktopPane1.add(jTextFieldFillerID);
					jTextFieldFillerID.setBounds(860, 50, 68, 22);
					jTextFieldFillerID.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
				             char c = e.getKeyChar();
				             if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
				                  e.consume();
				             }
						}
					});
				}
				{
					jLabelPlant = new JLabel4j_std();
					jDesktopPane1.add(jLabelPlant);
					jLabelPlant.setText(lang.get("lbl_Plant"));
					jLabelPlant.setBounds(613, 51, 51, 21);
					jLabelPlant.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jTextFieldPlant = new JTextField4j(JDBViewQMPanelResults.field_Plant);
					jDesktopPane1.add(jTextFieldPlant);
					jTextFieldPlant.setBounds(681, 50, 68, 22);
				}

				{
					jLabelSortby = new JLabel4j_std();
					jDesktopPane1.add(jLabelSortby);
					jLabelSortby.setText(lang.get("lbl_Sort_By"));
					jLabelSortby.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelSortby.setBounds(307, 160, 108, 23);
				}
				{
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setBounds(439, 160, 255, 23);
				}

				{
					jLabelStatus = new JLabel4j_std();
					jDesktopPane1.add(jLabelStatus);
					jLabelStatus.setText(lang.get("lbl_Panel_Status"));
					jLabelStatus.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelStatus.setBounds(704, 88, 126, 21);
				}


				{
					String[] test =
					{ "", "Prepare", "Ready","Complete" };
					ComboBoxModel<String> jComboBoxRecycleModel = new DefaultComboBoxModel<String>(test);
					jComboBoxStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxStatus);
					jComboBoxStatus.setModel(jComboBoxRecycleModel);
					jComboBoxStatus.setBounds(839, 86, 108, 23);
					jComboBoxStatus.setMaximumRowCount(test.length);
				}

				{
					jToggleButtonSequence = new JToggleButton();
					jToggleButtonSequence.setSelected(true);
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(692, 160, 21, 23);
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
							if (JLaunchLookup.materials())
							{
								jTextFieldMaterial.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					jButtonLookupWasteMaterial.setBounds(565, 50, 21, 22);
					jDesktopPane1.add(jButtonLookupWasteMaterial);
				}

				{
					jButtonLookupPanelResult = new JButton4j(Common.icon_lookup_16x16);
					jButtonLookupPanelResult.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.panel_ZWSIPANE())
							{
								jTextFieldPanelResult.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					jButtonLookupPanelResult.setBounds(259, 87, 21, 22);
					jDesktopPane1.add(jButtonLookupPanelResult);
				}

				{
					jButtonLookupPlant = new JButton4j(Common.icon_lookup_16x16);
					jButtonLookupPlant.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgCriteriaDefault = "";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.plants_po_resource())
							{
								jTextFieldPlant.setText(JLaunchLookup.dlgResult);
							}

						}
					});
					jButtonLookupPlant.setBounds(748, 50, 21, 22);
					jDesktopPane1.add(jButtonLookupPlant);
				}
				
				{
					jButtonLookupFiller = new JButton4j(Common.icon_lookup_16x16);
					jButtonLookupFiller.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgCriteriaDefault = "";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.panel_Filler())
							{
								jTextFieldFillerID.setText(JLaunchLookup.dlgResult);
							}

						}
					});
					jButtonLookupFiller.setBounds(926, 50, 21, 22);
					jDesktopPane1.add(jButtonLookupFiller);
				}
				

				{
					panelDateFrom.setBounds(155, 13, 125, 25);
					panelDateFrom.setEnabled(false);
					jDesktopPane1.add(panelDateFrom);
				}

				{
					panelDateTo.setBounds(328, 13, 125, 25);
					panelDateTo.setEnabled(false);
					jDesktopPane1.add(panelDateTo);
				}
				
				{
					sampleDateFrom.setBounds(645, 13, 125, 25);
					sampleDateFrom.setEnabled(false);
					jDesktopPane1.add(sampleDateFrom);
				}

				{
					sampleDateTo.setBounds(822, 13, 125, 25);
					sampleDateTo.setEnabled(false);
					jDesktopPane1.add(sampleDateTo);
				}

				{
					jLabelPanelDate = new JLabel4j_std();
					jLabelPanelDate.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelPanelDate.setText(lang.get("lbl_Panel_Date"));
					jLabelPanelDate.setBounds(0, 15, 126, 21);
					jDesktopPane1.add(jLabelPanelDate);
				}
				
				{
					jLabelSampleDate = new JLabel4j_std();
					jLabelSampleDate.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelSampleDate.setText(lang.get("lbl_Sample_Date"));
					jLabelSampleDate.setBounds(490, 15, 126, 21);
					jDesktopPane1.add(jLabelSampleDate);
				}

				{
					jCheckBoxPanelDateFrom = new JCheckBox4j();
					jCheckBoxPanelDateFrom.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							if (jCheckBoxPanelDateFrom.isSelected())
							{
								panelDateFrom.setEnabled(true);
								calendarButtonPanelDateFrom.setEnabled(true);
							}
							else
							{
								panelDateFrom.setEnabled(false);
								calendarButtonPanelDateFrom.setEnabled(false);
							}
						}
					});
					jCheckBoxPanelDateFrom.setBackground(new Color(255, 255, 255));
					jCheckBoxPanelDateFrom.setBounds(134, 11, 21, 25);
					jDesktopPane1.add(jCheckBoxPanelDateFrom);
				}

				{
					jCheckBoxPanelDateTo = new JCheckBox4j();
					jCheckBoxPanelDateTo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							if (jCheckBoxPanelDateTo.isSelected())
							{
								panelDateTo.setEnabled(true);
								calendarButtonPanelDateTo.setEnabled(true);
							}
							else
							{
								panelDateTo.setEnabled(false);
								calendarButtonPanelDateTo.setEnabled(false);
							}
						}
					});
					jCheckBoxPanelDateTo.setBackground(new Color(255, 255, 255));
					jCheckBoxPanelDateTo.setBounds(308, 13, 21, 25);
					jDesktopPane1.add(jCheckBoxPanelDateTo);
				}

				{
					jCheckBoxSampleDateFrom = new JCheckBox4j();
					jCheckBoxSampleDateFrom.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							if (jCheckBoxSampleDateFrom.isSelected())
							{
								sampleDateFrom.setEnabled(true);
								calendarButtonSampleDateFrom.setEnabled(true);
							}
							else
							{
								sampleDateFrom.setEnabled(false);
								calendarButtonSampleDateFrom.setEnabled(false);
							}
						}
					});
					jCheckBoxSampleDateFrom.setBackground(new Color(255, 255, 255));
					jCheckBoxSampleDateFrom.setBounds(624, 13, 21, 25);
					jDesktopPane1.add(jCheckBoxSampleDateFrom);
				}
				
				{
					jCheckBoxSampleDateTo = new JCheckBox4j();
					jCheckBoxSampleDateTo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							if (jCheckBoxSampleDateTo.isSelected())
							{
								sampleDateTo.setEnabled(true);
								calendarButtonSampleDateTo.setEnabled(true);
							}
							else
							{
								sampleDateTo.setEnabled(false);
								calendarButtonSampleDateTo.setEnabled(false);
							}
						}
					});
					jCheckBoxSampleDateTo.setBackground(new Color(255, 255, 255));
					jCheckBoxSampleDateTo.setBounds(800, 13, 21, 25);
					jDesktopPane1.add(jCheckBoxSampleDateTo);
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
					jButtonExcel.setBounds(363, 195, 123, 32);
					jDesktopPane1.add(jButtonExcel);
				}

				{
					calendarButtonPanelDateFrom = new JCalendarButton(panelDateFrom);
					calendarButtonPanelDateFrom.setEnabled(false);
					calendarButtonPanelDateFrom.setBounds(286, 13, 21, 25);
					jDesktopPane1.add(calendarButtonPanelDateFrom);
				}
				
				{
					calendarButtonSampleDateFrom = new JCalendarButton(sampleDateFrom);
					calendarButtonSampleDateFrom.setEnabled(false);
					calendarButtonSampleDateFrom.setBounds(775, 13, 21, 25);
					jDesktopPane1.add(calendarButtonSampleDateFrom);
				}
				
				{
					calendarButtonPanelDateTo = new JCalendarButton(panelDateTo);
					calendarButtonPanelDateTo.setEnabled(false);
					calendarButtonPanelDateTo.setBounds(457, 13, 21, 25);
					jDesktopPane1.add(calendarButtonPanelDateTo);
				}
				
				{
					calendarButtonSampleDateTo = new JCalendarButton(sampleDateTo);
					calendarButtonSampleDateTo.setEnabled(false);
					calendarButtonSampleDateTo.setBounds(952, 13, 21, 25);
					jDesktopPane1.add(calendarButtonSampleDateTo);
				}

				JLabel4j_std labelLimit = new JLabel4j_std();
				labelLimit.setText(lang.get("lbl_Limit"));
				labelLimit.setHorizontalAlignment(SwingConstants.TRAILING);
				labelLimit.setBounds(725, 162, 147, 21);
				jDesktopPane1.add(labelLimit);

				jCheckBoxLimit = new JCheckBox4j();
				jCheckBoxLimit.setSelected(true);
				jCheckBoxLimit.setBackground(Color.WHITE);
				jCheckBoxLimit.setBounds(876, 162, 21, 21);
				jDesktopPane1.add(jCheckBoxLimit);
				


				JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
				ne.getTextField().setFont(Common.font_std);
				jSpinnerLimit.setEditor(ne);
				jSpinnerLimit.setBounds(905, 162, 68, 21);
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
					jButtonClear.setBounds(240, 195, 123, 32);
					jDesktopPane1.add(jButtonClear);
				}

				{
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setBounds(486, 195, 123, 32);
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
					jTextFieldProcessOrder = new JTextField4j(JDBViewQMPanelResults.field_ProcessOrder);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setBounds(134, 51, 126, 22);
				}
				{
					jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookupProcessOrder);
					jButtonLookupProcessOrder.setBounds(259, 51, 21, 22);
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
					jLabelProcessOrder.setBounds(0, 52, 126, 21);
				}

				{
					jTextFieldUserID = new JTextField4j(JDBUser.field_user_id);
					jDesktopPane1.add(jTextFieldUserID);
					jTextFieldUserID.setBounds(440, 88, 126, 22);
				}
				{
					jButtonLookupUserID = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookupUserID);
					jButtonLookupUserID.setBounds(565, 88, 21, 22);
					jButtonLookupUserID.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgCriteriaDefault = "Y";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.panelUsers())
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
					jLabelUser.setBounds(307, 88, 108, 21);
				}

				ComboBoxModel<String> jComboBoxReportTypeModel = new DefaultComboBoxModel<String>(new String[] { "Daily Panel Summary","Detail Report","Summary Report" });
				jComboBoxReportType = new JComboBox4j<String>();
				jComboBoxReportType.setMaximumRowCount(15);
				jComboBoxReportType.setModel(jComboBoxReportTypeModel);
				jComboBoxReportType.setBounds(134, 160, 182, 22);
				jComboBoxReportType.setSelectedItem("Daily Panel Summary");
				jDesktopPane1.add(jComboBoxReportType);
				
				ComboBoxModel<JDBViewProductGroups> jComboBox3Model = new DefaultComboBoxModel<JDBViewProductGroups>(productGroupList);
				comboBoxProductGroups.setModel(jComboBox3Model);
				comboBoxProductGroups.setMaximumRowCount(12);
				comboBoxProductGroups.setBounds(778, 125, 169, 23);
				jDesktopPane1.add(comboBoxProductGroups);
				
				JLabel4j_std jLabelProductGroup = new JLabel4j_std();
				jLabelProductGroup.setText(lang.get("lbl_User_Data4"));
				jLabelProductGroup.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabelProductGroup.setBounds(644, 127, 126, 21);
				jDesktopPane1.add(jLabelProductGroup);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void print()
	{
		jComboBoxSortBy.setSelectedIndex(0);
		
		if (jComboBoxReportType.getSelectedItem().equals("Detail Report"))
		{
			JLaunchReport.runReport("RPT_PANEL_RESULTS1", null, "", buildSQL(qPrint), "");
		}
		
		if (jComboBoxReportType.getSelectedItem().equals("Summary Report"))
		{
			JLaunchReport.runReport("RPT_PANEL_RESULTS2", null, "", buildSQL(qPrint), "");
		}
		
		if (jComboBoxReportType.getSelectedItem().equals("Daily Panel Summary"))
		{
			JLaunchReport.runReport("RPT_PANEL_RESULTS3", null, "", buildSQL(qPrint), "");
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
