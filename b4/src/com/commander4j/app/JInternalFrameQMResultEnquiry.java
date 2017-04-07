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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
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
import com.commander4j.db.JDBQMDictionary;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JCheckListItem;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.renderer.MultiItemCheckListRenderer;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.table.JDBQMResultTable;
import com.commander4j.tablemodel.JDBQueryTableModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameQMResultEnquiry is used for querying the APP_QM_RESULT table
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameQMResultEnquiry.jpg" >
 * 
 * @see com.commander4j.db.JDBQMResult JDBQMResult
 */
public class JInternalFrameQMResultEnquiry extends JInternalFrame
{

	private static final long serialVersionUID = 1L;
	private JTextField4j textFieldProcessOrder;
	private JButton4j btnClose;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBQMDictionary dict = new JDBQMDictionary(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std lblStatusBar;
	private JDBQMResultTable table;
	private JTextField4j textFieldMaterial;
	private JLabel4j_std lbl_inspection;
	private JTextField4j textFieldInspectionID;
	private JScrollPane scrollPaneResults;
	private JCheckBox4j checkBoxSampleFrom;
	private JDateControl dateSampleFrom;
	private JCheckBox4j checkBoxSampleTo;
	private JDateControl dateSampleTo;
	private JList4j<JCheckListItem> listDictionary;
	private JTextField4j textFieldUserData1;
	private JTextField4j textFieldUserData2;
	private JTextField4j textFieldUserData3;
	private JTextField4j textFieldUserData4;
	private JDBQueryTableModel model = new JDBQueryTableModel();
	private JCalendarButton calendarButtonsampleDateFrom;
	private JCalendarButton calendarButtonsampleDateTo;
	private JSpinner jSpinnerLimit;
	private JCheckBox4j jCheckBoxLimit;
	private JLabel4j_std jLabel10;

	private PreparedStatement buildSQL()
	{
		PreparedStatement result;
		String resultSQL = "";

		String startSQL = "SELECT  Sample_ID, Sample_Date,Material,Process_Order, User_Data_1, User_Data_2, User_Data_3, User_Data_4 ";

		String fieldsSQL = "";
		int x = listDictionary.getModel().getSize();
		if (x > 0)
		{
			JCheckListItem tempItem;
			for (int sel = 0; sel < x; sel++)
			{
				tempItem = (JCheckListItem) listDictionary.getModel().getElementAt(sel);
				if (tempItem.isSelected())
				{
					JDBQMDictionary dictItem = (JDBQMDictionary) tempItem.getValue();
					String testID = dictItem.getTestID();
					String description = dictItem.getDescription();
					String tempField = ",MAX(CASE TEST_ID WHEN '" + testID + "' THEN RESULT ELSE NULL END) AS '" + description + "'";
					if (fieldsSQL.equals(""))
					{
						fieldsSQL = tempField;
					} else
					{
						fieldsSQL = fieldsSQL + tempField;
					}
				}
			}
		}

		String joinSQL = "FROM VIEW_QM_RESULTS WHERE 1 = 1";

		String whereSQL = "";

		if (textFieldProcessOrder.getText().equals("") == false)
		{
			whereSQL = whereSQL + " AND PROCESS_ORDER = '" + textFieldProcessOrder.getText() + "'";
		}

		if (textFieldMaterial.getText().equals("") == false)
		{
			whereSQL = whereSQL + " AND MATERIAL = '" + textFieldMaterial.getText() + "'";
		}

		if (textFieldInspectionID.getText().equals("") == false)
		{
			whereSQL = whereSQL + " AND INSPECTION_ID = '" + textFieldInspectionID.getText() + "'";
		}

		if (textFieldUserData1.getText().equals("") == false)
		{
			whereSQL = whereSQL + " AND USER_DATA_1 = '" + textFieldUserData1.getText() + "'";
		}

		if (textFieldUserData2.getText().equals("") == false)
		{
			whereSQL = whereSQL + " AND USER_DATA_2 = '" + textFieldUserData2.getText() + "'";
		}

		if (textFieldUserData3.getText().equals("") == false)
		{
			whereSQL = whereSQL + " AND USER_DATA_3 = '" + textFieldUserData3.getText() + "'";
		}

		if (textFieldUserData4.getText().equals("") == false)
		{
			whereSQL = whereSQL + " AND USER_DATA_4 = '" + textFieldUserData4.getText() + "'";
		}
		
		int dateParams = 0;

		if (checkBoxSampleFrom.isSelected())
		{
			dateParams++;
		}
		if (checkBoxSampleTo.isSelected())
		{
			dateParams++;
		}

		if (dateParams > 0)
		{
			if (dateParams == 1)
			{
				if (checkBoxSampleFrom.isSelected())
				{
					whereSQL = whereSQL + " AND SAMPLE_DATE >= ? ";
				}
				if (checkBoxSampleTo.isSelected())
				{
					whereSQL = whereSQL + " AND SAMPLE_DATE <= ? ";
				}
			} else
			{
				if (dateParams == 2)
				{
					whereSQL = whereSQL + " AND SAMPLE_DATE >= ? AND SAMPLE_DATE <= ? ";
				}
			}
		}

		String groupSQL = "GROUP BY SAMPLE_ID,SAMPLE_DATE,MATERIAL,PROCESS_ORDER,USER_DATA_1,USER_DATA_2,USER_DATA_3,USER_DATA_4";

		String sqlHaving = "";

		if (x > 0)
		{
			int count = 0;
			String limit = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit();
			String fieldDelim1 = "";
			String fieldDelim2 = "";
			if (limit.equals("top"))
			{
				fieldDelim1 = "'";
				fieldDelim2 = "'";
			}
			if (limit.equals("rownum"))
			{
				fieldDelim1 = "\"";
				fieldDelim2 = "\"";
			}
			if (limit.equals("limit"))
			{
				fieldDelim1 = "`";
				fieldDelim2 = "`";
			}
			JCheckListItem tempItem;
			for (int sel = 0; sel < x; sel++)
			{
				tempItem = (JCheckListItem) listDictionary.getModel().getElementAt(sel);
				if (tempItem.isSelected())
				{
					JDBQMDictionary dictItem = (JDBQMDictionary) tempItem.getValue();
					String description = dictItem.getDescription();

					if (count == 0)
					{
						sqlHaving = " HAVING (" + fieldDelim1 + description + fieldDelim2 + " IS NOT NULL) ";
					} else
					{
						sqlHaving = sqlHaving + " OR (" + fieldDelim1 + description + fieldDelim2 + " IS NOT NULL) ";
					}
					count++;
				}
			}
		}

		resultSQL = startSQL + " " + fieldsSQL + " " + joinSQL + " " + whereSQL + " " + groupSQL + " " + sqlHaving;

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.setSqlText(resultSQL);
		query.applyRestriction(jCheckBoxLimit.isSelected(), Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), jSpinnerLimit.getValue());
		if (dateParams > 0)
		{
			if (dateParams == 1)
			{
				if (checkBoxSampleFrom.isSelected())
				{
					query.addParameter(JUtility.getTimestampFromDate(dateSampleFrom.getDate()));
				}
				if (checkBoxSampleTo.isSelected())
				{
					query.addParameter(JUtility.getTimestampFromDate(dateSampleTo.getDate()));
				}
			} else
			{
				if (dateParams == 2)
				{
					query.addParameter(JUtility.getTimestampFromDate(dateSampleFrom.getDate()));
					query.addParameter(JUtility.getTimestampFromDate(dateSampleTo.getDate()));
				}
			}
		}

		query.bindParams();
		result = query.getPreparedStatement();
		System.out.println(resultSQL);

		return result;
	}

	private void populateTable()
	{

		try
		{
			PreparedStatement ps = buildSQL();
			ResultSet rs = ps.executeQuery();

			String[] columnNames = {lang.get("lbl_SampleID"),lang.get("lbl_Sample_Date"),lang.get("lbl_Material"),lang.get("lbl_Process_Order"),lang.get("lbl_User_Data1"),lang.get("lbl_User_Data2"),lang.get("lbl_User_Data3"),lang.get("lbl_User_Data4")};
			
			model.setQuery(rs,columnNames);

			table.setModel(model);
			table.setCellRenderers("", "", "", "result");
			table.setColumnWidths();

		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		JUtility.setResultRecordCountColour(lblStatusBar, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), table.getRowCount());
	}

	private void editRecord()
	{
		int row = table.getSelectedRow();
		if (row >= 0)
		{
			String temp = table.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_QM_SAMPLE_EDIT", temp);
		}
	}

	public JInternalFrameQMResultEnquiry()
	{

		setVisible(true);
		this.setClosable(true);
		this.setIconifiable(true);
		setBounds(100, 100, 1028, 721);
		getContentPane().setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 1018, 689);
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
		btnClose.setBounds(397, 194, 117, 32);
		btnClose.setIcon(Common.icon_close);
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		desktopPane.add(btnClose);

		JButton4j btnSearch = new JButton4j(lang.get("btn_Search"));
		btnSearch.setBounds(45, 194, 117, 32);
		btnSearch.setIcon(Common.icon_search);
		btnSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				populateTable();
			}
		});
		desktopPane.add(btnSearch);

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

		scrollPaneResults = new JScrollPane();
		scrollPaneResults.setBounds(6, 240, 993, 410);
		scrollPaneResults.getViewport().setBackground(Common.color_tablebackground);
		desktopPane.setLayout(null);
		scrollPaneResults.setViewportView(table);
		desktopPane.add(scrollPaneResults);

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
				PreparedStatement temp = buildSQL();

				ResultSet rs;

				try
				{
					rs = temp.executeQuery();
					export.saveAs("qm_results.xls", rs, Common.mainForm);
				} catch (Exception e)
				{
					rs = null;

				}
				populateTable();
			}
		});
		btnExcel.setIcon(Common.icon_XLS);
		btnExcel.setBounds(279, 194, 117, 32);
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
		jSpinnerLimit.setBounds(456, 152, 68, 25);
		jSpinnerLimit.setValue(1000);
		jSpinnerLimit.getEditor().setSize(45, 21);
		desktopPane.add(jSpinnerLimit);

		jCheckBoxLimit = new JCheckBox4j();
		desktopPane.add(jCheckBoxLimit);
		jCheckBoxLimit.setBackground(new java.awt.Color(255, 255, 255));
		jCheckBoxLimit.setBounds(433, 152, 21, 25);
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
		jLabel10.setBounds(353, 152, 77, 22);

		JScrollPane scrollPaneDictionary = new JScrollPane();
		scrollPaneDictionary.setBounds(559, 32, 440, 194);

		listDictionary = new JList4j<JCheckListItem>();

		ComboBoxModel<JCheckListItem> model = new DefaultComboBoxModel<JCheckListItem>(dict.getTestCheckListList());
		listDictionary.setModel(model);
		listDictionary.setCellRenderer(new MultiItemCheckListRenderer());
		listDictionary.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent event)
			{
				JList4j<?> list = (JList4j<?>) event.getSource();

				// Get index of item clicked

				int index = list.locationToIndex(event.getPoint());
				JCheckListItem item = (JCheckListItem) list.getModel().getElementAt(index);

				// Toggle selected state

				item.setSelected(!item.isSelected());

				// Repaint cell

				list.repaint(list.getCellBounds(index, index));
			}
		});

		scrollPaneDictionary.setViewportView(listDictionary);

		desktopPane.add(scrollPaneDictionary);

		JLabel4j_std label4j_std_3 = new JLabel4j_std(lang.get("lbl_Test_ID"));
		label4j_std_3.setBounds(559, 16, 111, 16);
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

		table = new JDBQMResultTable(Common.selectedHostID, Common.sessionID, "", "", "result");
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPaneResults.setViewportView(table);

		JButton4j btnEdit = new JButton4j(lang.get("btn_Edit"));
		btnEdit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				editRecord();
			}
		});
		btnEdit.setIcon(Common.icon_edit);
		btnEdit.setBounds(162, 194, 117, 32);
		desktopPane.add(btnEdit);

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
