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
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMAnalysis;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JUtility;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	private JDBQMAnalysis analdb = new JDBQMAnalysis(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std lblStatusBar;
	private JTextField4j textFieldMaterial;
	private JDateControl dateSampleFrom;
	private JDateControl dateSampleTo;
	private JList4j<JDBQMAnalysis> listDictionary;
	private JTextField4j textFieldUserData1;
	private JTextField4j textFieldUserData2;
	private JTextField4j textFieldUserData3;
	private JTextField4j textFieldUserData4;
	private JCalendarButton calendarButtonsampleDateFrom;
	private JCalendarButton calendarButtonsampleDateTo;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JTextField4j textFieldBatchSuffix = new JTextField4j();
	private JTextField4j textFieldResource = new JTextField4j();
	private JButton btnMaterialLookup = new JButton();
	private JButton btnProcessOrderResourceLookup = new JButton();

	public JInternalFrameQMResultAnalysis()
	{

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();

		setVisible(true);
		this.setClosable(true);
		this.setIconifiable(true);
		setBounds(100, 100, 602, 491);
		getContentPane().setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 598, 465);
		desktopPane.setBackground(Common.color_app_window);
		getContentPane().add(desktopPane);
		desktopPane.setLayout(null);

		JLabel4j_std lblProcessOrder = new JLabel4j_std(lang.get("lbl_Process_Order"));
		lblProcessOrder.setBounds(288, 14, 119, 16);
		lblProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
		desktopPane.add(lblProcessOrder);

		textFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
		textFieldProcessOrder.setEnabled(false);
		textFieldProcessOrder.setBounds(413, 12, 119, 22);

		desktopPane.add(textFieldProcessOrder);
		textFieldProcessOrder.setColumns(10);

		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.setBounds(323, 414, 117, 32);
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
		btnProcessOrderLookup.setEnabled(false);
		btnProcessOrderLookup.setIcon(Common.icon_lookup);
		btnProcessOrderLookup.setBounds(530, 12, 21, 22);
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
		textFieldMaterial.setEnabled(false);
		textFieldMaterial.setColumns(10);
		textFieldMaterial.setBounds(413, 46, 119, 22);
		desktopPane.add(textFieldMaterial);

		JLabel4j_std lbl_material = new JLabel4j_std(lang.get("lbl_Material"));
		lbl_material.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_material.setBounds(288, 48, 119, 16);
		desktopPane.add(lbl_material);

		JButton4j btnReport = new JButton4j(lang.get("btn_Print"));
		btnReport.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (listDictionary.isSelectionEmpty() == false)
				{

					JDBQMAnalysis selectedReport = ((JDBQMAnalysis) listDictionary.getSelectedValue());

					HashMap<String, Object> parameters = new HashMap<String, Object>();

					if (selectedReport.getBatchSuffixReqd().equals("Y"))
					{
						parameters.put(selectedReport.getBatchSuffixParam(), textFieldBatchSuffix.getText());
					}

					if (selectedReport.getSampleDateStartReqd().equals("Y"))
					{
						parameters.put(selectedReport.getSampleDateStartParam(), JUtility.getTimestampFromDate(dateSampleFrom.getDate()));
					}

					if (selectedReport.getSampleDateEndReqd().equals("Y"))
					{
						parameters.put(selectedReport.getSampleDateEndParam(), JUtility.getTimestampFromDate(dateSampleTo.getDate()));
					}

					JLaunchReport.runReport(selectedReport.getModuleID(), parameters, "", null, "");
				}
			}
		});
		btnReport.setIcon(Common.icon_report);
		btnReport.setBounds(164, 414, 117, 32);
		desktopPane.add(btnReport);

		JLabel4j_std label4j_std = new JLabel4j_std(lang.get("lbl_Sample_Date"));
		label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std.setBounds(21, 148, 121, 22);
		desktopPane.add(label4j_std);

		dateSampleFrom = new JDateControl();
		dateSampleFrom.setEnabled(false);
		dateSampleFrom.setFont(new Font("Arial", Font.PLAIN, 11));
		dateSampleFrom.setBounds(150, 145, 136, 25);
		desktopPane.add(dateSampleFrom);

		dateSampleTo = new JDateControl();
		dateSampleTo.setEnabled(false);
		dateSampleTo.setFont(new Font("Arial", Font.PLAIN, 11));
		dateSampleTo.setBounds(413, 145, 128, 22);
		desktopPane.add(dateSampleTo);

		JLabel4j_std lbl_UserData1 = new JLabel4j_std(lang.get("lbl_User_Data1"));
		lbl_UserData1.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData1.setBounds(21, 78, 121, 22);
		desktopPane.add(lbl_UserData1);

		JLabel4j_std lbl_UserData2 = new JLabel4j_std(lang.get("lbl_User_Data2"));
		lbl_UserData2.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData2.setBounds(288, 78, 119, 22);
		desktopPane.add(lbl_UserData2);

		JLabel4j_std lbl_UserData3 = new JLabel4j_std(lang.get("lbl_User_Data3"));
		lbl_UserData3.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData3.setBounds(21, 110, 121, 22);
		desktopPane.add(lbl_UserData3);

		textFieldUserData3 = new JTextField4j(20);
		textFieldUserData3.setEnabled(false);
		textFieldUserData3.setColumns(20);
		textFieldUserData3.setBounds(150, 110, 138, 22);
		desktopPane.add(textFieldUserData3);

		JLabel4j_std lbl_UserData4 = new JLabel4j_std(lang.get("lbl_User_Data4"));
		lbl_UserData4.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData4.setBounds(288, 110, 119, 22);
		desktopPane.add(lbl_UserData4);

		textFieldUserData4 = new JTextField4j(20);
		textFieldUserData4.setEnabled(false);
		textFieldUserData4.setColumns(20);
		textFieldUserData4.setBounds(413, 110, 138, 22);
		desktopPane.add(textFieldUserData4);

		textFieldUserData1 = new JTextField4j(JDBQMSample.field_data_1);
		textFieldUserData1.setEnabled(false);
		textFieldUserData1.setColumns(20);
		textFieldUserData1.setBounds(150, 78, 138, 22);
		desktopPane.add(textFieldUserData1);

		textFieldUserData2 = new JTextField4j(JDBQMSample.field_data_2);
		textFieldUserData2.setEnabled(false);
		textFieldUserData2.setColumns(20);
		textFieldUserData2.setBounds(413, 78, 138, 22);
		desktopPane.add(textFieldUserData2);

		calendarButtonsampleDateFrom = new JCalendarButton(dateSampleFrom);
		calendarButtonsampleDateFrom.setEnabled(false);
		calendarButtonsampleDateFrom.setSize(21, 25);
		calendarButtonsampleDateFrom.setLocation(281, 145);
		desktopPane.add(calendarButtonsampleDateFrom);

		calendarButtonsampleDateTo = new JCalendarButton(dateSampleTo);
		calendarButtonsampleDateTo.setEnabled(false);
		calendarButtonsampleDateTo.setSize(21, 25);
		calendarButtonsampleDateTo.setLocation(543, 145);
		desktopPane.add(calendarButtonsampleDateTo);

		JScrollPane scrollPaneDictionary = new JScrollPane();
		scrollPaneDictionary.setBounds(12, 206, 552, 172);

		listDictionary = new JList4j<JDBQMAnalysis>();
		listDictionary.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_RESULT_ANALYSIS_EDIT") == true)
					{
						editRecord();
					}
				}
			}
		});
		listDictionary.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if (listDictionary.isSelectionEmpty() == false)
				{

					JDBQMAnalysis selectedReport = ((JDBQMAnalysis) listDictionary.getSelectedValue());

					if (selectedReport.getBatchSuffixReqd().equals("Y"))
					{
						textFieldBatchSuffix.setBackground(Color.YELLOW);
						textFieldBatchSuffix.setEnabled(true);
						textFieldBatchSuffix.requestFocus();
						textFieldBatchSuffix.setCaretPosition(textFieldBatchSuffix.getText().length());
					} else
					{
						textFieldBatchSuffix.setBackground(Color.WHITE);
						textFieldBatchSuffix.setEnabled(false);
					}

					if (selectedReport.getProcessOrderReqd().equals("Y"))
					{
						btnProcessOrderLookup.setEnabled(true);
						textFieldProcessOrder.setBackground(Color.YELLOW);
						textFieldProcessOrder.setEnabled(true);
						btnProcessOrderResourceLookup.setEnabled(true);
					} else
					{
						btnProcessOrderLookup.setEnabled(false);
						textFieldProcessOrder.setBackground(Color.WHITE);
						textFieldProcessOrder.setEnabled(false);
						btnProcessOrderResourceLookup.setEnabled(false);
					}

					if (selectedReport.getMaterialReqd().equals("Y"))
					{
						btnMaterialLookup.setEnabled(true);
						textFieldMaterial.setBackground(Color.YELLOW);
						textFieldMaterial.setEnabled(true);
						btnMaterialLookup.setEnabled(true);
					} else
					{
						btnMaterialLookup.setEnabled(false);
						textFieldMaterial.setBackground(Color.WHITE);
						textFieldMaterial.setEnabled(false);
						btnMaterialLookup.setEnabled(false);
					}

					if (selectedReport.getUserData1Reqd().equals("Y"))
					{
						textFieldUserData1.setBackground(Color.YELLOW);
						textFieldUserData1.setEnabled(true);
					} else
					{
						textFieldUserData1.setBackground(Color.WHITE);
						textFieldUserData1.setEnabled(false);
					}

					if (selectedReport.getUserData2Reqd().equals("Y"))
					{
						textFieldUserData2.setBackground(Color.YELLOW);
						textFieldUserData2.setEnabled(true);
					} else
					{
						textFieldUserData2.setBackground(Color.WHITE);
						textFieldUserData2.setEnabled(false);
					}
					if (selectedReport.getUserData3Reqd().equals("Y"))
					{
						textFieldUserData3.setBackground(Color.YELLOW);
						textFieldUserData3.setEnabled(true);
					} else
					{
						textFieldUserData3.setBackground(Color.WHITE);
						textFieldUserData3.setEnabled(false);
					}

					if (selectedReport.getUserData4Reqd().equals("Y"))
					{
						textFieldUserData4.setBackground(Color.YELLOW);
						textFieldUserData4.setEnabled(true);
					} else
					{
						textFieldUserData4.setBackground(Color.WHITE);
						textFieldUserData4.setEnabled(false);
					}

					if (selectedReport.getSampleDateStartReqd().equals("Y"))
					{
						calendarButtonsampleDateFrom.setEnabled(true);
						dateSampleFrom.setBackground(Color.YELLOW);
						dateSampleFrom.setEnabled(true);
						calendarButtonsampleDateFrom.setEnabled(true);
					} else
					{
						calendarButtonsampleDateFrom.setEnabled(false);
						dateSampleFrom.setBackground(Color.WHITE);
						dateSampleFrom.setEnabled(false);
						calendarButtonsampleDateFrom.setEnabled(false);
					}

					if (selectedReport.getSampleDateEndReqd().equals("Y"))
					{
						calendarButtonsampleDateTo.setEnabled(true);
						dateSampleTo.setBackground(Color.YELLOW);
						dateSampleTo.setEnabled(true);
						calendarButtonsampleDateTo.setEnabled(true);
					} else
					{
						calendarButtonsampleDateTo.setEnabled(false);
						dateSampleTo.setBackground(Color.WHITE);
						dateSampleTo.setEnabled(false);
						calendarButtonsampleDateTo.setEnabled(false);
					}

					if (selectedReport.getResourceReqd().equals("Y"))
					{
						btnProcessOrderResourceLookup.setEnabled(true);
						textFieldResource.setBackground(Color.YELLOW);
						textFieldResource.setEnabled(true);
						btnProcessOrderResourceLookup.setEnabled(true);
					} else
					{
						btnProcessOrderResourceLookup.setEnabled(false);
						textFieldResource.setBackground(Color.WHITE);
						textFieldResource.setEnabled(false);
						btnProcessOrderResourceLookup.setEnabled(false);
					}
				}
			}
		});

		/*
		 * Vector<JDBQMAnalysis> vect= analdb.getAnalysisData();
		 * 
		 * ComboBoxModel<JDBQMAnalysis> model = new
		 * DefaultComboBoxModel<JDBQMAnalysis>(vect);
		 * listDictionary.setModel(model);
		 * listDictionary.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 * listDictionary.setCellRenderer(Common.renderer_list);
		 */

		populateList("");

		scrollPaneDictionary.setViewportView(listDictionary);

		desktopPane.add(scrollPaneDictionary);

		/*
		 * if (vect.size()>0) { listDictionary.setSelectedIndex(0); }
		 */

		JLabel4j_std label4j_std_3 = new JLabel4j_std(lang.get("mod_FRM_QM_RESULT_ANALYSIS"));
		label4j_std_3.setBounds(12, 190, 218, 16);
		desktopPane.add(label4j_std_3);
		btnMaterialLookup.setEnabled(false);

		btnMaterialLookup.addActionListener(new ActionListener()
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
		btnMaterialLookup.setIcon(Common.icon_lookup);
		btnMaterialLookup.setBounds(530, 46, 21, 22);
		desktopPane.add(btnMaterialLookup);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(241, 241, 241));
		panel.setBounds(12, 380, 116, 30);
		desktopPane.add(panel);

		JButton4j button4jAdd = new JButton4j(Common.icon_add);
		button4jAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addRecord();
			}
		});
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
		button4jEdit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				editRecord();
			}
		});
		button4jEdit.setMnemonic('0');
		button4jEdit.setFont(new Font("Arial", Font.PLAIN, 11));
		button4jEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_RESULT_ANALYSIS_EDIT"));
		button4jEdit.setBounds(29, 0, 28, 28);
		panel.add(button4jEdit);

		JButton4j button4jRefresh = new JButton4j(Common.icon_refresh);
		button4jRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (listDictionary.isSelectionEmpty() == false)
				{

					JDBQMAnalysis selectedReport = ((JDBQMAnalysis) listDictionary.getSelectedValue());
					populateList(selectedReport.getAnalysisID());
				} else
				{
					populateList("");
				}
			}
		});
		button4jRefresh.setMnemonic('0');
		button4jRefresh.setFont(new Font("Arial", Font.PLAIN, 11));
		button4jRefresh.setBounds(87, 0, 28, 28);
		panel.add(button4jRefresh);

		JLabel4j_std label4j_std_1 = new JLabel4j_std(lang.get("lbl_Batch_Suffix"));
		label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_1.setBounds(21, 14, 121, 22);
		desktopPane.add(label4j_std_1);
		textFieldBatchSuffix.setEnabled(false);

		textFieldBatchSuffix.setBounds(150, 14, 138, 22);
		desktopPane.add(textFieldBatchSuffix);

		JLabel4j_std label4j_std_2 = new JLabel4j_std(lang.get("lbl_Process_Order_Required_Resource"));
		label4j_std_2.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_2.setBounds(21, 48, 121, 16);
		desktopPane.add(label4j_std_2);
		textFieldResource.setEnabled(false);

		textFieldResource.setColumns(10);
		textFieldResource.setCaretPosition(0);
		textFieldResource.setBounds(150, 46, 119, 22);
		desktopPane.add(textFieldResource);
		btnProcessOrderResourceLookup.setEnabled(false);

		btnProcessOrderResourceLookup.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JLaunchLookup.dlgCriteriaDefault = "Y";
				JLaunchLookup.dlgAutoExec = true;
				if (JLaunchLookup.resources())
				{
					textFieldResource.setText(JLaunchLookup.dlgResult);
				}
			}
		});
		btnProcessOrderResourceLookup.setIcon(Common.icon_lookup);
		btnProcessOrderResourceLookup.setBounds(265, 46, 21, 22);
		desktopPane.add(btnProcessOrderResourceLookup);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				textFieldBatchSuffix.requestFocus();
				textFieldBatchSuffix.setCaretPosition(textFieldBatchSuffix.getText().length());

			}
		});

	}

	private void populateList(String defaultitem)
	{
		Vector<JDBQMAnalysis> vect = analdb.getAnalysisData();

		ComboBoxModel<JDBQMAnalysis> model = new DefaultComboBoxModel<JDBQMAnalysis>(vect);
		listDictionary.setModel(model);
		listDictionary.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDictionary.setCellRenderer(Common.renderer_list);

		int sel = -1;
		for (int j = 0; j < vect.size(); j++)
		{
			if (((JDBQMAnalysis) vect.get(j)).getAnalysisID().equals(defaultitem))
			{
				sel = j;
			}
		}

		listDictionary.setSelectedIndex(sel);

	}

	private void addRecord()
	{

		String analid;

		analid = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Analysis_Add"));

		if (analid != null)
		{
			if (analid.equals("") == false)
			{
				JDBQMAnalysis newAnal = new JDBQMAnalysis(Common.selectedHostID, Common.sessionID);
				if (newAnal.isValidAnalysis(analid) == false)
				{
					JLaunchMenu.runForm("FRM_QM_RESULT_ANALYSIS_EDIT", analid);
				} else
				{
					JOptionPane.showMessageDialog(Common.mainForm, "Analysis [" + analid + "] already exists", lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
				}
			}
		}
	}

	private void editRecord()
	{

		if (listDictionary.isSelectionEmpty() == false)
		{

			JDBQMAnalysis selectedReport = ((JDBQMAnalysis) listDictionary.getSelectedValue());

			String analid = selectedReport.getAnalysisID();

			JLaunchMenu.runForm("FRM_QM_RESULT_ANALYSIS_EDIT", analid);
		}
	}

}
