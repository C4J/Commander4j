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


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
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
import com.commander4j.db.JDBShifts;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JTitledBorder4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
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

	private JButton4j btnClose;
	private JButton4j btnMaterialLookup = new JButton4j();
	private JButton4j btnProcessOrderResourceLookup = new JButton4j();
	private JButton4j button4j_Backwards = new JButton4j(Common.icon_arrow_left_16x16);
	private JButton4j button4j_Forwards = new JButton4j(Common.icon_arrow_right_16x16);
	private JCalendarButton button_CalendardomDateFrom;
	private JCalendarButton button_CalendardomDateTo;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBQMAnalysis analdb = new JDBQMAnalysis(Common.selectedHostID, Common.sessionID);
	private JDateControl dateSampleFrom = new JDateControl();
	private JDateControl dateSampleTo = new JDateControl();
	private JLabel4j_status label4j_statusBar = new JLabel4j_status();
	private JList4j<JDBQMAnalysis> listDictionary;
	private JList4j<JDBShifts> jListShifts = new JList4j<JDBShifts>();
	private JPanel4j panelDateParams = new JPanel4j();
	private JTextField4j textFieldBatchSuffix = new JTextField4j();
	private JTextField4j textFieldMaterial;
	private JTextField4j textFieldProcessOrder;
	private JTextField4j textFieldResource = new JTextField4j();
	private JTextField4j textFieldUserData1;
	private JTextField4j textFieldUserData2;
	private JTextField4j textFieldUserData3;
	private JTextField4j textFieldUserData4;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private static final long serialVersionUID = 1L;

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
		setBounds(100, 100, 590, 597);
		getContentPane().setLayout(null);

		JDesktopPane4j desktopPane = new JDesktopPane4j();
		desktopPane.setBounds(0, 0, 589, 671);

		getContentPane().add(desktopPane);
		desktopPane.setLayout(null);

		JLabel4j_std lblProcessOrder = new JLabel4j_std(lang.get("lbl_Process_Order"));
		lblProcessOrder.setBounds(288, 8, 119, 22);
		lblProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
		desktopPane.add(lblProcessOrder);

		textFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
		textFieldProcessOrder.setEnabled(false);
		textFieldProcessOrder.setBounds(413, 8, 120, 22);

		desktopPane.add(textFieldProcessOrder);
		textFieldProcessOrder.setColumns(10);

		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.setBounds(305, 495, 117, 32);
		btnClose.setIcon(Common.icon_close_16x16);
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		desktopPane.add(btnClose);

		JButton4j btnProcessOrderLookup = new JButton4j();
		btnProcessOrderLookup.setEnabled(false);
		btnProcessOrderLookup.setIcon(Common.icon_lookup_16x16);
		btnProcessOrderLookup.setBounds(533, 8, 21, 22);
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
		desktopPane.setLayout(null);

		textFieldMaterial = new JTextField4j(JDBMaterial.field_material);
		textFieldMaterial.setEnabled(false);
		textFieldMaterial.setColumns(10);
		textFieldMaterial.setBounds(413, 40, 120, 22);
		desktopPane.add(textFieldMaterial);

		JLabel4j_std lbl_material = new JLabel4j_std(lang.get("lbl_Material"));
		lbl_material.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_material.setBounds(288, 40, 119, 22);
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
		btnReport.setIcon(Common.icon_report_16x16);
		btnReport.setBounds(187, 495, 117, 32);
		desktopPane.add(btnReport);


		JLabel4j_std lbl_UserData1 = new JLabel4j_std(lang.get("lbl_User_Data1"));
		lbl_UserData1.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData1.setBounds(0, 72, 142, 22);
		desktopPane.add(lbl_UserData1);

		JLabel4j_std lbl_UserData2 = new JLabel4j_std(lang.get("lbl_User_Data2"));
		lbl_UserData2.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData2.setBounds(288, 72, 119, 22);
		desktopPane.add(lbl_UserData2);

		JLabel4j_std lbl_UserData3 = new JLabel4j_std(lang.get("lbl_User_Data3"));
		lbl_UserData3.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData3.setBounds(0, 104, 142, 22);
		desktopPane.add(lbl_UserData3);

		textFieldUserData3 = new JTextField4j(20);
		textFieldUserData3.setEnabled(false);
		textFieldUserData3.setColumns(20);
		textFieldUserData3.setBounds(150, 104, 138, 22);
		desktopPane.add(textFieldUserData3);

		JLabel4j_std lbl_UserData4 = new JLabel4j_std(lang.get("lbl_User_Data4"));
		lbl_UserData4.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_UserData4.setBounds(288, 104, 119, 22);
		desktopPane.add(lbl_UserData4);

		textFieldUserData4 = new JTextField4j(20);
		textFieldUserData4.setEnabled(false);
		textFieldUserData4.setColumns(20);
		textFieldUserData4.setBounds(413, 104, 138, 22);
		desktopPane.add(textFieldUserData4);

		textFieldUserData1 = new JTextField4j(JDBQMSample.field_data_1);
		textFieldUserData1.setEnabled(false);
		textFieldUserData1.setColumns(20);
		textFieldUserData1.setBounds(150, 72, 138, 22);
		desktopPane.add(textFieldUserData1);

		textFieldUserData2 = new JTextField4j(JDBQMSample.field_data_2);
		textFieldUserData2.setEnabled(false);
		textFieldUserData2.setColumns(20);
		textFieldUserData2.setBounds(413, 72, 138, 22);
		desktopPane.add(textFieldUserData2);


		JLabel4j_std label4j_std_3 = new JLabel4j_std(lang.get("mod_FRM_QM_RESULT_ANALYSIS"));
		label4j_std_3.setBounds(16, 331, 218, 22);
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
		btnMaterialLookup.setIcon(Common.icon_lookup_16x16);
		btnMaterialLookup.setBounds(533, 40, 21, 22);
		desktopPane.add(btnMaterialLookup);

		JPanel4j panel = new JPanel4j();
		panel.setLayout(null);

		panel.setBounds(16, 486, 116, 32);
		desktopPane.add(panel);

		JButton4j button4jAdd = new JButton4j(Common.icon_add_16x16);
		button4jAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addRecord();
			}
		});
		button4jAdd.setMnemonic('0');
		button4jAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_RESULT_ANALYSIS_ADD"));
		button4jAdd.setBounds(0, 0, 28, 28);
		panel.add(button4jAdd);

		JButton4j button4jDelete = new JButton4j(Common.icon_delete_16x16);
		button4jDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				deleteRecord();
			}
		});
		button4jDelete.setMnemonic('0');
		button4jDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_RESULT_ANALYSIS_DELETE"));
		button4jDelete.setBounds(58, 0, 28, 28);
		panel.add(button4jDelete);

		JButton4j button4jEdit = new JButton4j(Common.icon_edit_16x16);
		button4jEdit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				editRecord();
			}
		});
		button4jEdit.setMnemonic('0');
		button4jEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_RESULT_ANALYSIS_EDIT"));
		button4jEdit.setBounds(29, 0, 28, 28);
		panel.add(button4jEdit);

		JButton4j button4jRefresh = new JButton4j(Common.icon_refresh_16x16);
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
		button4jRefresh.setBounds(87, 0, 28, 28);
		panel.add(button4jRefresh);

		JLabel4j_std label4j_std_1 = new JLabel4j_std(lang.get("lbl_Batch_Suffix"));
		label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_1.setBounds(0, 8, 142, 22);
		desktopPane.add(label4j_std_1);
		textFieldBatchSuffix.setEnabled(false);

		textFieldBatchSuffix.setBounds(150, 8, 138, 22);
		desktopPane.add(textFieldBatchSuffix);

		JLabel4j_std label4j_std_2 = new JLabel4j_std(lang.get("lbl_Process_Order_Required_Resource"));
		label4j_std_2.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_2.setBounds(0, 40, 142, 22);
		desktopPane.add(label4j_std_2);
		textFieldResource.setEnabled(false);

		textFieldResource.setColumns(10);
		textFieldResource.setCaretPosition(0);
		textFieldResource.setBounds(150, 40, 119, 22);
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
		btnProcessOrderResourceLookup.setIcon(Common.icon_lookup_16x16);
		btnProcessOrderResourceLookup.setBounds(269, 40, 21, 22);
		desktopPane.add(btnProcessOrderResourceLookup);


		/*=========*/
		JScrollPane4j scrollPaneShifts = new JScrollPane4j(JScrollPane4j.List);
		scrollPaneShifts.setBounds(16, 160, 375, 133);
		desktopPane.add(scrollPaneShifts);
		jListShifts.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				if (arg0.getClickCount() == 2)
				{
					editShift();
				}
			}
		});
		jListShifts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jListShifts.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent arg0)
			{
				if (arg0.getValueIsAdjusting() == false)
				{

					if (jListShifts.getSelectedIndex() > -1)
					{

						Date dateNow = new Date();
						Calendar calendarNow = GregorianCalendar.getInstance();

						calendarNow.setTime(dateNow);

						JDBShifts selectedShift = (JDBShifts) jListShifts.getSelectedValue();

						Calendar startCal = GregorianCalendar.getInstance();
						startCal.setTime(dateNow);
						startCal.set(Calendar.HOUR_OF_DAY, selectedShift.getStartTimeHours());
						startCal.set(Calendar.MINUTE, selectedShift.getStartTimeMins());
						startCal.set(Calendar.SECOND, selectedShift.getStartTimeSecs());

						Calendar endCal = GregorianCalendar.getInstance();
						endCal.setTime(dateNow);
						endCal.set(Calendar.HOUR_OF_DAY, selectedShift.getEndTimeHours());
						endCal.set(Calendar.MINUTE, selectedShift.getEndTimeMins());
						endCal.set(Calendar.SECOND, selectedShift.getEndTimeSecs());

						if (startCal.compareTo(endCal) > 0)
						{
							endCal.add(Calendar.DATE, 1);
						}

						if (startCal.compareTo(calendarNow) > 0)
						{
							endCal.add(Calendar.DATE, -1);
							startCal.add(Calendar.DATE, -1);
						}

						dateSampleFrom.setDate(startCal.getTime());
						dateSampleTo.setDate(endCal.getTime());
					}
				}
			}
		});

		jListShifts.setSelectedIndex(-1);
		scrollPaneShifts.setViewportView(jListShifts);

		JLabel4j_std lblCriteria = new JLabel4j_std("Criteria - Quick Set");
		lblCriteria.setText(lang.get("mod_FRM_ADMIN_SHIFT"));
		lblCriteria.setBounds(20, 136, 226, 22);
		desktopPane.add(lblCriteria);
		panelDateParams.setBorder(JTitledBorder4j.getPanelLineBorder());

		panelDateParams.setBounds(399, 160, 165, 133);

		desktopPane.add(panelDateParams);
		panelDateParams.setLayout(null);

		JLabel4j_std lblStart = new JLabel4j_std(lang.get("web_From"));
		lblStart.setBounds(12, 0, 142, 22);
		panelDateParams.add(lblStart);

		dateSampleFrom.setBounds(12, 21, 120, 22);
		panelDateParams.add(dateSampleFrom);

		button_CalendardomDateFrom = new JCalendarButton(dateSampleFrom);
		button_CalendardomDateFrom.setSize(21, 21);
		button_CalendardomDateFrom.setLocation(510, 314);
		button_CalendardomDateFrom.setBounds(136, 21, 21, 21);
		panelDateParams.add(button_CalendardomDateFrom);

		JLabel4j_std label4jEnd = new JLabel4j_std(lang.get("web_To"));
		label4jEnd.setBounds(12, 44, 149, 22);
		panelDateParams.add(label4jEnd);

		dateSampleTo.setBounds(12, 65, 120, 22);
		panelDateParams.add(dateSampleTo);

		button_CalendardomDateTo = new JCalendarButton(dateSampleTo);
		button_CalendardomDateTo.setSize(21, 21);
		button_CalendardomDateTo.setLocation(510, 369);
		button_CalendardomDateTo.setBounds(136, 66, 21, 21);
		panelDateParams.add(button_CalendardomDateTo);

		button4j_Backwards.setBounds(56, 94, 25, 25);
		panelDateParams.add(button4j_Backwards);
		button4j_Backwards.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dateAdjust(dateSampleFrom, -1);
				dateAdjust(dateSampleTo, -1);
			}
		});
		button4j_Backwards.setEnabled(true);

		button4j_Forwards.setBounds(93, 94, 25, 25);
		panelDateParams.add(button4j_Forwards);
		button4j_Forwards.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dateAdjust(dateSampleFrom, 1);
				dateAdjust(dateSampleTo, 1);
			}
		});
		button4j_Forwards.setEnabled(true);

		label4j_statusBar.setBounds(0, 539, 589, 25);
		desktopPane.add(label4j_statusBar);

		populateListUserShifts("");

		/*===========	*/

		JPanel4j panelShiftButtons = new JPanel4j();

		panelShiftButtons.setBounds(16, 294, 120, 32);
		desktopPane.add(panelShiftButtons);
		panelShiftButtons.setLayout(null);

		JButton4j button4jShiftAdd = new JButton4j(Common.icon_add_16x16);
		button4jShiftAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				addShift();
			}
		});
		button4jShiftAdd.setBounds(0, 0, 28, 28);
		panelShiftButtons.add(button4jShiftAdd);
		button4jShiftAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_ADD"));
		button4jShiftAdd.setMnemonic('0');

		JButton4j button4jShiftDelete = new JButton4j(Common.icon_delete_16x16);
		button4jShiftDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				deleteShift();
			}
		});
		button4jShiftDelete.setBounds(58, 0, 28, 28);
		panelShiftButtons.add(button4jShiftDelete);
		button4jShiftDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_DELETE"));
		button4jShiftDelete.setMnemonic('0');

		JButton4j button4jShiftEdit = new JButton4j(Common.icon_edit_16x16);
		button4jShiftEdit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				editShift();
			}
		});
		button4jShiftEdit.setBounds(29, 0, 28, 28);
		panelShiftButtons.add(button4jShiftEdit);
		button4jShiftEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_EDIT"));
		button4jShiftEdit.setMnemonic('0');

		JButton4j button4jShiftRefresh = new JButton4j(Common.icon_refresh_16x16);
		button4jShiftRefresh.setBounds(87, 0, 28, 28);
		panelShiftButtons.add(button4jShiftRefresh);
		button4jShiftRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				populateListUserShifts("");
			}
		});
		button4jShiftRefresh.setMnemonic('0');

		JScrollPane4j scrollPaneDictionary = new JScrollPane4j(JScrollPane4j.List);
		scrollPaneDictionary.setBounds(16, 352, 552, 133);

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
				if (e.getValueIsAdjusting() == false)
				{

				if (listDictionary.isSelectionEmpty() == false)
				{

					JDBQMAnalysis selectedReport = ((JDBQMAnalysis) listDictionary.getSelectedValue());

					if (selectedReport.getBatchSuffixReqd().equals("Y"))
					{
						textFieldBatchSuffix.setEnabled(true);
						textFieldBatchSuffix.requestFocus();
						textFieldBatchSuffix.setCaretPosition(textFieldBatchSuffix.getText().length());
					} else
					{

						textFieldBatchSuffix.setEnabled(false);
					}

					if (selectedReport.getProcessOrderReqd().equals("Y"))
					{
						btnProcessOrderLookup.setEnabled(true);

						textFieldProcessOrder.setEnabled(true);
						btnProcessOrderResourceLookup.setEnabled(true);
					} else
					{
						btnProcessOrderLookup.setEnabled(false);

						textFieldProcessOrder.setEnabled(false);
						btnProcessOrderResourceLookup.setEnabled(false);
					}

					if (selectedReport.getMaterialReqd().equals("Y"))
					{
						btnMaterialLookup.setEnabled(true);

						textFieldMaterial.setEnabled(true);
						btnMaterialLookup.setEnabled(true);
					} else
					{
						btnMaterialLookup.setEnabled(false);

						textFieldMaterial.setEnabled(false);
						btnMaterialLookup.setEnabled(false);
					}

					if (selectedReport.getUserData1Reqd().equals("Y"))
					{

						textFieldUserData1.setEnabled(true);
					} else
					{

						textFieldUserData1.setEnabled(false);
					}

					if (selectedReport.getUserData2Reqd().equals("Y"))
					{

						textFieldUserData2.setEnabled(true);
					} else
					{

						textFieldUserData2.setEnabled(false);
					}
					if (selectedReport.getUserData3Reqd().equals("Y"))
					{

						textFieldUserData3.setEnabled(true);
					} else
					{

						textFieldUserData3.setEnabled(false);
					}

					if (selectedReport.getUserData4Reqd().equals("Y"))
					{

						textFieldUserData4.setEnabled(true);
					} else
					{

						textFieldUserData4.setEnabled(false);
					}

					if (selectedReport.getSampleDateStartReqd().equals("Y"))
					{
						button_CalendardomDateFrom.setEnabled(true);

						dateSampleFrom.setEnabled(true);
						button_CalendardomDateFrom.setEnabled(true);
					} else
					{
						button_CalendardomDateFrom.setEnabled(false);

						dateSampleFrom.setEnabled(false);
						button_CalendardomDateFrom.setEnabled(false);
					}

					if (selectedReport.getSampleDateEndReqd().equals("Y"))
					{
						button_CalendardomDateTo.setEnabled(true);

						dateSampleTo.setEnabled(true);
						button_CalendardomDateTo.setEnabled(true);
					} else
					{
						button_CalendardomDateTo.setEnabled(false);

						dateSampleTo.setEnabled(false);
						button_CalendardomDateTo.setEnabled(false);
					}

					if (selectedReport.getResourceReqd().equals("Y"))
					{
						btnProcessOrderResourceLookup.setEnabled(true);

						textFieldResource.setEnabled(true);
						btnProcessOrderResourceLookup.setEnabled(true);
					} else
					{
						btnProcessOrderResourceLookup.setEnabled(false);

						textFieldResource.setEnabled(false);
						btnProcessOrderResourceLookup.setEnabled(false);
					}
				}
				}
			}
		});

		populateList("");

		scrollPaneDictionary.setViewportView(listDictionary);

		desktopPane.add(scrollPaneDictionary);

		if (listDictionary.getModel().getSize() > 0)
		{
			listDictionary.setSelectedIndex(0);
		}




		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				textFieldBatchSuffix.requestFocus();
				textFieldBatchSuffix.setCaretPosition(textFieldBatchSuffix.getText().length());
}
		});

	}

	private void dateAdjust(JDateControl datecontrol, Integer adjustment)
	{
		Date dateNow = datecontrol.getDate();
		Calendar calendarNow = GregorianCalendar.getInstance();

		calendarNow.setTime(dateNow);

		calendarNow.add(Calendar.DATE, adjustment);

		datecontrol.setDate(calendarNow.getTime());

	}

	private void editShift()
	{
		if (jListShifts.isSelectionEmpty() == false)
		{
			if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_EDIT"))
			{
				JDBShifts shiftid = (JDBShifts) (jListShifts.getSelectedValue());
				JLaunchMenu.runDialog("FRM_ADMIN_SHIFT", shiftid.getShiftID());
				populateListUserShifts(shiftid.getShiftID());
			}
		}
	}

	private void populateListUserShifts(String defaultitem)
	{
		DefaultComboBoxModel<JDBShifts> defComboBoxMod = new DefaultComboBoxModel<JDBShifts>();

		JDBShifts tempShifts = new JDBShifts(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBShifts> tempShiftList = new LinkedList<JDBShifts>();

		tempShiftList = tempShifts.getShifts();

		int sel = -1;
		for (int j = 0; j < tempShiftList.size(); j++)
		{
			defComboBoxMod.addElement(tempShiftList.get(j));
			if (tempShiftList.get(j).toString().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBShifts> jList1Model = defComboBoxMod;

		jListShifts.setModel(jList1Model);
		jListShifts.setSelectedIndex(sel);

		jListShifts.setCellRenderer(Common.renderer_list);

		if (jListShifts.isSelectionEmpty())
		{
			if (jListShifts.getModel().getSize() > 0)
				jListShifts.setSelectedIndex(0);
		}
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

	private void deleteRecord()
	{

		if (listDictionary.isSelectionEmpty() == false)
		{

			JDBQMAnalysis selectedReport = ((JDBQMAnalysis) listDictionary.getSelectedValue());

			String analid = selectedReport.getAnalysisID();

			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Analysis_Delete") + " " + analid + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (n == 0)
			{
				JDBQMAnalysis newAnal = new JDBQMAnalysis(Common.selectedHostID, Common.sessionID);
				newAnal.setAnalysisID(analid);
				newAnal.delete();
				populateList("");
			}
		}
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
					JOptionPane.showMessageDialog(Common.mainForm, "Analysis [" + analid + "] already exists", lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
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

	private void deleteShift()
	{
		if (jListShifts.isSelectionEmpty() == false)
		{
			if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_DELETE"))
			{
				JDBShifts shiftid = (JDBShifts) (jListShifts.getSelectedValue());

				int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Shift_Delete") + " " + shiftid.getShiftID() + " - [" + shiftid.getDescription() + "]" + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0,
						Common.icon_confirm_16x16);
				if (n == 0)
				{

					shiftid.delete();
					populateListUserShifts("");
				}
			}
		}
	}

	private void addShift()
	{

		JDBShifts shift = new JDBShifts(Common.selectedHostID, Common.sessionID);

		String shiftId = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Shift_Create"));
		if (shiftId != null)
		{
			if (shiftId.equals("") == false)
			{
				shiftId = shiftId.toUpperCase();
				shift.setShiftID(shiftId);
				if (shift.isValid(shiftId))
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, shift.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
				} else
				{

					JLaunchMenu.runDialog("FRM_ADMIN_SHIFT", shiftId);
				}
				populateListUserShifts(shiftId);
			}
		}

	}
}
