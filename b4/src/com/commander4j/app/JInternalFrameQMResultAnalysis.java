package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameQMResultEnquiry.java
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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMAnalysis;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameQMResultAnalysis is used for querying a user selectable
 * view.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameQMResultAnalysis.jpg" >
 * 
 * @see com.commander4j.db.JDBQMAnalysis JDBQMAnalysis
 */
public class JInternalFrameQMResultAnalysis extends JInternalFrame
{

	private static final long serialVersionUID = 1L;
	private JTextField4j textFieldProcessOrder;
	private JButton4j btnClose;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBQMAnalysis dict = new JDBQMAnalysis(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std lblStatusBar;
	private JTextField4j textFieldMaterial;
	private JLabel4j_std lbl_inspection;
	private JTextField4j textFieldInspectionID;
	private JCheckBox4j checkBoxSampleFrom;
	private JDateControl dateSampleFrom;
	private JCheckBox4j checkBoxSampleTo;
	private JDateControl dateSampleTo;
	private JList4j<JDBQMAnalysis> listDictionary;
	private JTextField4j textFieldUserData1;
	private JTextField4j textFieldUserData2;
	private JTextField4j textFieldUserData3;
	private JTextField4j textFieldUserData4;
	private JCalendarButton calendarButtonsampleDateFrom;
	private JCalendarButton calendarButtonsampleDateTo;
	private JSpinner jSpinnerLimit;
	private JCheckBox4j jCheckBoxLimit;
	private JLabel4j_std jLabel10;
	private PreparedStatement listStatement;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();

	private boolean buildSQL(String viewname,String sortOrder,Boolean ascending)
	{
		boolean result = false;

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}VIEW_QM_ANALYSIS_01"));
		query.addParamtoSQL("material=", textFieldMaterial.getText());

		if (checkBoxSampleFrom.isSelected())
		{
			query.addParamtoSQL("expiry_date>=", JUtility.getTimestampFromDate(dateSampleFrom.getDate()));

		}

		if (checkBoxSampleTo.isSelected())
		{
			query.addParamtoSQL("expiry_date<=", JUtility.getTimestampFromDate(dateSampleTo.getDate()));
		}

		query.appendSort(sortOrder,ascending);
		query.applyRestriction(jCheckBoxLimit.isSelected(), Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), jSpinnerLimit.getValue());

		query.bindParams();
		listStatement = query.getPreparedStatement();

		result = true;

		return result;
	}
	
	public JInternalFrameQMResultAnalysis()
	{

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();

		setVisible(true);
		this.setClosable(true);
		this.setIconifiable(true);
		setBounds(100, 100, 1138, 278);
		getContentPane().setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 1128, 249);
		desktopPane.setBackground(Common.color_app_window);
		getContentPane().add(desktopPane);
		desktopPane.setLayout(null);

		setTitle("Results Enquiry");

		JLabel4j_std lblProcessOrder = new JLabel4j_std(lang.get("lbl_Process_Order"));
		lblProcessOrder.setBounds(6, 16, 111, 16);
		lblProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
		desktopPane.add(lblProcessOrder);

		textFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
		textFieldProcessOrder.setBounds(123, 14, 119, 22);

		desktopPane.add(textFieldProcessOrder);
		textFieldProcessOrder.setColumns(10);

		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.setBounds(301, 186, 117, 32);
		btnClose.setIcon(Common.icon_close);
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		desktopPane.add(btnClose);

		JButton btnProcessOrderLookup = new JButton();
		btnProcessOrderLookup.setIcon(Common.icon_lookup);
		btnProcessOrderLookup.setBounds(240, 14, 21, 22);
		btnProcessOrderLookup.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JLaunchLookup.dlgCriteriaDefault = "Ready";
				JLaunchLookup.dlgAutoExec = true;
				if (JLaunchLookup.processOrders())
				{
					textFieldProcessOrder.setText(JLaunchLookup.dlgResult);
				}
			}
		});
		desktopPane.add(btnProcessOrderLookup);

		lblStatusBar = new JLabel4j_std();
		lblStatusBar.setBounds(0, 662, 1012, 21);
		lblStatusBar.setForeground(Color.RED);
		lblStatusBar.setBackground(Color.GRAY);
		desktopPane.add(lblStatusBar);
		desktopPane.setLayout(null);

		textFieldMaterial = new JTextField4j(JDBMaterial.field_material);
		textFieldMaterial.setColumns(10);
		textFieldMaterial.setBounds(386, 14, 119, 22);
		desktopPane.add(textFieldMaterial);

		JLabel4j_std lbl_material = new JLabel4j_std(lang.get("lbl_Material"));
		lbl_material.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_material.setBounds(263, 16, 111, 16);
		desktopPane.add(lbl_material);

		textFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
		textFieldInspectionID.setColumns(10);
		textFieldInspectionID.setBounds(123, 152, 119, 22);
		desktopPane.add(textFieldInspectionID);

		lbl_inspection = new JLabel4j_std(lang.get("lbl_Inspection_ID"));
		lbl_inspection.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_inspection.setBounds(6, 152, 111, 22);
		desktopPane.add(lbl_inspection);

		JButton4j btnExcel = new JButton4j(lang.get("btn_Excel"));
		btnExcel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JExcel export = new JExcel();
				if (buildSQL("VIEW_QM_ANALYSIS_01","BATCH_SUFFIX,SAMPLE_DATE",true))
				{

					ResultSet rs;

					try
					{
						rs = listStatement.executeQuery();
						export.saveAs("qm_analysis.xls", rs, Common.mainForm);
					} catch (Exception e)
					{
						rs = null;

					}
				}
			}
		});
		btnExcel.setIcon(Common.icon_XLS);
		btnExcel.setBounds(142, 186, 117, 32);
		desktopPane.add(btnExcel);

		JLabel4j_std label4j_std = new JLabel4j_std(lang.get("lbl_Sample_Date"));
		label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std.setBounds(6, 117, 108, 22);
		desktopPane.add(label4j_std);

		checkBoxSampleFrom = new JCheckBox4j();
		checkBoxSampleFrom.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (checkBoxSampleFrom.isSelected())
				{
					dateSampleFrom.setEnabled(true);
					calendarButtonsampleDateFrom.setEnabled(true);
				} else
				{
					dateSampleFrom.setEnabled(false);
					calendarButtonsampleDateFrom.setEnabled(false);
				}
			}
		});
		checkBoxSampleFrom.setBackground(Color.WHITE);
		checkBoxSampleFrom.setBounds(120, 114, 21, 25);
		desktopPane.add(checkBoxSampleFrom);

		dateSampleFrom = new JDateControl();
		dateSampleFrom.setFont(new Font("Arial", Font.PLAIN, 11));
		dateSampleFrom.setEnabled(false);
		dateSampleFrom.setBounds(142, 114, 128, 22);
		desktopPane.add(dateSampleFrom);

		checkBoxSampleTo = new JCheckBox4j();
		checkBoxSampleTo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (checkBoxSampleTo.isSelected())
				{
					dateSampleTo.setEnabled(true);
					calendarButtonsampleDateTo.setEnabled(true);
				} else
				{
					dateSampleTo.setEnabled(false);
					calendarButtonsampleDateTo.setEnabled(false);
				}
			}
		});
		checkBoxSampleTo.setBackground(Color.WHITE);
		checkBoxSampleTo.setBounds(373, 114, 21, 25);
		desktopPane.add(checkBoxSampleTo);

		dateSampleTo = new JDateControl();
		dateSampleTo.setFont(new Font("Arial", Font.PLAIN, 11));
		dateSampleTo.setEnabled(false);
		dateSampleTo.setBounds(396, 114, 128, 22);
		desktopPane.add(dateSampleTo);

		JLabel4j_std lbl_UserData1 = new JLabel4j_std(lang.get("lbl_User_Data1"));
		lbl_UserData1.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData1.setBounds(6, 44, 111, 22);
		desktopPane.add(lbl_UserData1);

		JLabel4j_std lbl_UserData2 = new JLabel4j_std(lang.get("lbl_User_Data2"));
		lbl_UserData2.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData2.setBounds(263, 44, 111, 22);
		desktopPane.add(lbl_UserData2);

		JLabel4j_std lbl_UserData3 = new JLabel4j_std(lang.get("lbl_User_Data3"));
		lbl_UserData3.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData3.setBounds(6, 78, 111, 22);
		desktopPane.add(lbl_UserData3);

		textFieldUserData3 = new JTextField4j(20);
		textFieldUserData3.setColumns(20);
		textFieldUserData3.setBounds(123, 78, 138, 22);
		desktopPane.add(textFieldUserData3);

		JLabel4j_std lbl_UserData4 = new JLabel4j_std(lang.get("lbl_User_Data4"));
		lbl_UserData4.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData4.setBounds(263, 78, 111, 22);
		desktopPane.add(lbl_UserData4);

		textFieldUserData4 = new JTextField4j(20);
		textFieldUserData4.setColumns(20);
		textFieldUserData4.setBounds(386, 78, 138, 22);
		desktopPane.add(textFieldUserData4);

		textFieldUserData1 = new JTextField4j(JDBQMSample.field_data_1);
		textFieldUserData1.setColumns(20);
		textFieldUserData1.setBounds(123, 44, 138, 22);
		desktopPane.add(textFieldUserData1);

		textFieldUserData2 = new JTextField4j(JDBQMSample.field_data_2);
		textFieldUserData2.setColumns(20);
		textFieldUserData2.setBounds(386, 44, 138, 22);
		desktopPane.add(textFieldUserData2);

		calendarButtonsampleDateFrom = new JCalendarButton(dateSampleFrom);
		calendarButtonsampleDateFrom.setSize(21, 25);
		calendarButtonsampleDateFrom.setEnabled(false);
		calendarButtonsampleDateFrom.setLocation(273, 114);
		desktopPane.add(calendarButtonsampleDateFrom);

		calendarButtonsampleDateTo = new JCalendarButton(dateSampleTo);
		calendarButtonsampleDateTo.setSize(21, 25);
		calendarButtonsampleDateTo.setEnabled(false);
		calendarButtonsampleDateTo.setLocation(526, 114);
		desktopPane.add(calendarButtonsampleDateTo);

		SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
		jSpinnerIntModel.setMinimum(1);
		jSpinnerIntModel.setMaximum(5000);
		jSpinnerIntModel.setStepSize(1);
		jSpinnerLimit = new JSpinner();
		JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
		ne.getTextField().setFont(Common.font_std);
		jSpinnerLimit.setEditor(ne);
		jSpinnerLimit.setModel(jSpinnerIntModel);
		jSpinnerLimit.setBounds(397, 149, 68, 25);
		jSpinnerLimit.setValue(1000);
		jSpinnerLimit.getEditor().setSize(45, 21);
		desktopPane.add(jSpinnerLimit);

		jCheckBoxLimit = new JCheckBox4j();
		desktopPane.add(jCheckBoxLimit);
		jCheckBoxLimit.setBackground(new java.awt.Color(255, 255, 255));
		jCheckBoxLimit.setBounds(374, 149, 21, 25);
		jCheckBoxLimit.setSelected(true);
		jCheckBoxLimit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				if (jCheckBoxLimit.isSelected())
				{
					jSpinnerLimit.setEnabled(true);
				} else
				{
					jSpinnerLimit.setEnabled(false);
				}
			}
		});

		jLabel10 = new JLabel4j_std();
		desktopPane.add(jLabel10);
		jLabel10.setText(lang.get("lbl_Limit"));
		jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel10.setBounds(294, 152, 77, 22);

		JScrollPane scrollPaneDictionary = new JScrollPane();
		scrollPaneDictionary.setBounds(559, 32, 552, 172);

		listDictionary = new JList4j<JDBQMAnalysis>();


		ComboBoxModel<JDBQMAnalysis> model = new DefaultComboBoxModel<JDBQMAnalysis>(dict.getAnalysisData());
		listDictionary.setModel(model);
		listDictionary.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDictionary.setCellRenderer(Common.renderer_list);
	

		scrollPaneDictionary.setViewportView(listDictionary);

		desktopPane.add(scrollPaneDictionary);

		JLabel4j_std label4j_std_3 = new JLabel4j_std(lang.get("mod_FRM_QM_RESULT_ANALYSIS"));
		label4j_std_3.setBounds(559, 16, 218, 16);
		desktopPane.add(label4j_std_3);

		JButton btnInspectionIDLookup = new JButton();
		btnInspectionIDLookup.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JLaunchLookup.dlgAutoExec = true;
				JLaunchLookup.dlgCriteriaDefault = "";
				if (JLaunchLookup.qmInspections())
				{
					JLaunchLookup.dlgCriteriaDefault = "";
					textFieldInspectionID.setText(JLaunchLookup.dlgResult);
				}
			}
		});
		btnInspectionIDLookup.setIcon(Common.icon_lookup);
		btnInspectionIDLookup.setBounds(240, 152, 21, 22);
		desktopPane.add(btnInspectionIDLookup);

		JButton button = new JButton();
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JLaunchLookup.dlgAutoExec = false;
				JLaunchLookup.dlgCriteriaDefault = "";

				if (JLaunchLookup.materials())
				{
					textFieldMaterial.setText(JLaunchLookup.dlgResult);
				}
			}
		});
		button.setIcon(Common.icon_lookup);
		button.setBounds(503, 14, 21, 22);
		desktopPane.add(button);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(241, 241, 241));
		panel.setBounds(559, 205, 120, 32);
		desktopPane.add(panel);
		
		JButton4j button4jAdd = new JButton4j(Common.icon_add);
		button4jAdd.setMnemonic('0');
		button4jAdd.setFont(new Font("Arial", Font.PLAIN, 11));
		button4jAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_RESULT_ANALYSIS_ADD"));
		button4jAdd.setBounds(0, 0, 28, 28);
		panel.add(button4jAdd);
		
		JButton4j button4jDelete = new JButton4j(Common.icon_delete);
		button4jDelete.setMnemonic('0');
		button4jDelete.setFont(new Font("Arial", Font.PLAIN, 11));
		button4jDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_RESULT_ANALYSIS_DELETE"));
		button4jDelete.setBounds(58, 0, 28, 28);
		panel.add(button4jDelete);
		
		JButton4j button4jEdit = new JButton4j(Common.icon_edit);
		button4jEdit.setMnemonic('0');
		button4jEdit.setFont(new Font("Arial", Font.PLAIN, 11));
		button4jEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_RESULT_ANALYSIS_EDIT"));
		button4jEdit.setBounds(29, 0, 28, 28);
		panel.add(button4jEdit);
		
		JButton4j button4jRefresh = new JButton4j(Common.icon_refresh);
		button4jRefresh.setMnemonic('0');
		button4jRefresh.setFont(new Font("Arial", Font.PLAIN, 11));
		button4jRefresh.setBounds(87, 0, 28, 28);
		panel.add(button4jRefresh);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				textFieldProcessOrder.requestFocus();
				textFieldProcessOrder.setCaretPosition(textFieldProcessOrder.getText().length());
				

			}
		});

	}
}
