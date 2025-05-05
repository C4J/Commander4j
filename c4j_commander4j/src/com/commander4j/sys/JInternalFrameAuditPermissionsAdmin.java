package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameAuditPermissionsAdmin.java
 * 
 * Package Name : com.commander4j.sys
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
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBAuditPermissions;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBUser;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JToggleButton4j;
import com.commander4j.tablemodel.JDBAuditPermissionsTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JUtility;

public class JInternalFrameAuditPermissionsAdmin extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JComboBox4j<String> jComboBoxEventType;
	private JLabel4j_std lbl_EventType;
	private JButton4j jButtonExcel;
	private JLabel4j_std jStatusBar;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonPrint;
	private JButton4j jButtonClose;
	private JLabel4j_std lbl_UserID;
	private JLabel4j_std lbl_Workstation;
	private JSpinner4j jSpinnerLimit;
	private JCheckBox4j jCheckBoxLimit;
	private JLabel4j_std lbl_Limit;
	private JButton4j jButtonUserIDLookup;
	private JToggleButton4j jToggleButtonSequence;
	private JComboBox4j<String> jComboBoxSortBy;
	private JComboBox4j<String> jComboBoxEventAction;
	private JDateControl eventsTo;
	private JCheckBox4j jCheckBoxEventsTo;
	private JCheckBox4j jCheckBoxEventsFrom;
	private JLabel4j_std lbl_EventDate;
	private JDateControl eventsFrom;
	private JLabel4j_std lbl_SortBy;
	private JTextField4j jTextFieldWorkstation;
	private JLabel4j_std lbl_EventAction;
	private JTextField4j jTextFieldUserID;	
	private JTextField4j jTextFieldData;
	private JLabel4j_std lbl_Data;
	private JTable4j jTable1;
	private JButton4j jButtonSearch;
	private JScrollPane jScrollPane1;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private static boolean dlg_sort_descending = false;
	String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JTextField4j jTextFieldTarget;
	private JCalendarButton calendarButtonEventFrom;
	private JCalendarButton calendarButtonEventTo;
	private PreparedStatement listStatement;

	public JInternalFrameAuditPermissionsAdmin()
	{
		super();
		getContentPane().setLayout(null);

		initGUI();

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}SYS_AUDIT_PERMISSIONS where 1=2"));
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		JLabel4j_std lbl_Target = new JLabel4j_std();
		lbl_Target.setText(lang.get("lbl_Target"));
		lbl_Target.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_Target.setBounds(676, 11, 125, 22);
		jDesktopPane1.add(lbl_Target);

		jTextFieldTarget = new JTextField4j(JDBAuditPermissions.field_target);
		jTextFieldTarget.setBounds(815, 11, 168, 22);
		jDesktopPane1.add(jTextFieldTarget);
		
		calendarButtonEventFrom = new JCalendarButton(eventsFrom);
		calendarButtonEventFrom.setEnabled(false);
		calendarButtonEventFrom.setBounds(274, 78, 22, 22);
		jDesktopPane1.add(calendarButtonEventFrom);
		
		calendarButtonEventTo = new JCalendarButton(eventsTo);
		calendarButtonEventTo.setEnabled(false);
		calendarButtonEventTo.setBounds(458, 78, 22, 22);
		jDesktopPane1.add(calendarButtonEventTo);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		dlg_sort_descending = true;
		setSequence(dlg_sort_descending);
	}

	private void filterBy(String fieldname) {
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals("User ID") == true)
			{
				jTextFieldUserID.setText(jTable1.getValueAt(row, 2).toString());
			}

			if (fieldname.equals("Event Type") == true)
			{
				jComboBoxEventType.setSelectedItem(jTable1.getValueAt(row, 3).toString());
			}

			
			if (fieldname.equals("Event Action") == true)
			{
				jComboBoxEventAction.setSelectedItem(jTable1.getValueAt(row, 4).toString());
			}

			if (fieldname.equals("Target") == true)
			{
				jTextFieldTarget.setText(jTable1.getValueAt(row, 5).toString());
			}
			
			if (fieldname.equals("Data") == true)
			{
				jTextFieldData.setText(jTable1.getValueAt(row, 6).toString());
			}
			
			if (fieldname.equals("Workstation") == true)
			{
				jTextFieldWorkstation.setText(jTable1.getValueAt(row, 7).toString());
			}
			buildSQL();
			populateList();

		}
	}

	private void clearFilter() {
		jTextFieldUserID.setText("");
		jTextFieldData.setText("");
		jComboBoxEventAction.setSelectedItem("");
		jTextFieldWorkstation.setText("");
		search();
	}

	private void sortBy(String orderField) {
		jComboBoxSortBy.setSelectedItem(orderField);
		buildSQL();
		populateList();
	}

	private void excel() {
		JDBAuditPermissions auditData = new JDBAuditPermissions(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.setExcelRowLimit(jCheckBoxLimit, jSpinnerLimit);
		buildSQL();
		export.saveAs("audit_activity.xls", auditData.getAuditDataResultSet(listStatement), Common.mainForm);
		populateList();
	}

	private void print() {
		PreparedStatement temp = buildSQLr();
		JLaunchReport.runReport("RPT_AUDIT_PERMISSIONS",null,"", temp,"");
	}


	private void populateList() {
		JDBAuditPermissions auditData = new JDBAuditPermissions(Common.selectedHostID, Common.sessionID);

		JDBAuditPermissionsTableModel audittable = new JDBAuditPermissionsTableModel(auditData.getAuditDataResultSet(listStatement));
		TableRowSorter<JDBAuditPermissionsTableModel> sorter = new TableRowSorter<JDBAuditPermissionsTableModel>(audittable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(audittable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(JDBAuditPermissionsTableModel.AuditLogID_Col).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(JDBAuditPermissionsTableModel.EventTime_Col).setPreferredWidth(125);
		jTable1.getColumnModel().getColumn(JDBAuditPermissionsTableModel.UserID_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBAuditPermissionsTableModel.EventType_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBAuditPermissionsTableModel.EventAction_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBAuditPermissionsTableModel.Target_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBAuditPermissionsTableModel.Data_Col).setPreferredWidth(200);
		jTable1.getColumnModel().getColumn(JDBAuditPermissionsTableModel.Workstation_Col).setPreferredWidth(200);

		
		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusBar, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), audittable.getRowCount());
	}

	private void search() {
		buildSQL();
		populateList();
	}


	private void setSequence(boolean descending) {
		jToggleButtonSequence.setSelected(descending);
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

	
	private PreparedStatement buildSQLr() {

		PreparedStatement result;
		
		String temp = "";

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBAuditPermissions.selectWithLimit");

		query.addText(temp);

		query.addParamtoSQL("user_id=", jTextFieldUserID.getText());
		query.addParamtoSQL("event_action=", jComboBoxEventAction.getSelectedItem());
		query.addParamtoSQL("workstation_id like ",  "%"+jTextFieldWorkstation.getText()+ "%");
		query.addParamtoSQL("data like ", "%" + jTextFieldData.getText() + "%");
		query.addParamtoSQL("target like ","%" +  jTextFieldTarget.getText() + "%");

		
		query.addParamtoSQL("event_type=", ((String) jComboBoxEventType.getSelectedItem()));

		if (jCheckBoxEventsFrom.isSelected())
		{
			query.addParamtoSQL("event_time>=", JUtility.getTimestampFromDate(eventsFrom.getDate()));
		}
		if (jCheckBoxEventsTo.isSelected())
		{
			query.addParamtoSQL("event_time<=", JUtility.getTimestampFromDate(eventsTo.getDate()));
		}

		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(jCheckBoxLimit.isSelected(),Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), jSpinnerLimit.getValue());
		query.bindParams();
		result = query.getPreparedStatement();
		return result;
	}
	
	private void buildSQL() {

		JDBQuery.closeStatement(listStatement);
		
		listStatement = buildSQLr();
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(750, 498));
			this.setBounds(0, 0, 1011, 600);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				jDesktopPane1.setBounds(0, 0, 1011, 576);
				jDesktopPane1.setLayout(null);
				this.getContentPane().add(jDesktopPane1);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(757, 468));
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jScrollPane1.setBounds(0, 157, 991, 382);
					jDesktopPane1.add(jScrollPane1);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][] { { "One", "Two" }, { "Three", "Four" } }, new String[] { "Column 1", "Column 2" });
						jTable1 = new JTable4j();

						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jTable1, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_search_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										search();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Search"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_print_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										print();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Print"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_AUDIT_ACTIVITY"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										excel();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Excel"));

								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenu4j sortByMenu = new JMenu4j();
								sortByMenu.setText(lang.get("lbl_Sort_By"));
								popupMenu.add(sortByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("USER_ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_User_ID"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("EVENT_TYPE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Event_Type"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("EVENT_ACTION");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Event_Action"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("TARGET");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Target"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("DATA");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Data"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("EVENT_DATE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Message_Event_Date"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("WORKSTATION");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Workstation"));
									sortByMenu.add(newItemMenuItem);
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
											filterBy("User ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_User_ID"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Event Type");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Event_Type"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Event Action");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Event_Action"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Target");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Target"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Data");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Data"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("Workstation");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Workstation"));
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
						}
					}
				}
				{
					jButtonSearch = new JButton4j(Common.icon_search_16x16);
					jButtonSearch.setBounds(217, 113, 109, 32);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.setMnemonic(lang.getMnemonicChar());
					jButtonSearch.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							search();

						}
					});
				}

				{
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jButtonPrint.setBounds(435, 113, 109, 32);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_AUDIT_PERMISSIONS"));
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

							print();
						}
					});
				}

				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(654, 113, 109, 32);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JDBQuery.closeStatement(listStatement);
							dispose();
						}
					});
				}

				{
					lbl_Data = new JLabel4j_std();
					lbl_Data.setBounds(676, 45, 125, 22);
					jDesktopPane1.add(lbl_Data);
					lbl_Data.setText(lang.get("lbl_Data"));
					lbl_Data.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldData = new JTextField4j(JDBAuditPermissions.field_data);
					jTextFieldData.setBounds(815, 45, 168, 22);
					jDesktopPane1.add(jTextFieldData);
				}
				{
					jTextFieldUserID = new JTextField4j(JDBUser.field_user_id);
					jTextFieldUserID.setBounds(124, 11, 151, 22);
					jDesktopPane1.add(jTextFieldUserID);
				}
				{
					lbl_UserID = new JLabel4j_std();
					lbl_UserID.setBounds(0, 11, 110, 22);
					jDesktopPane1.add(lbl_UserID);
					lbl_UserID.setText(lang.get("lbl_User_ID"));
					lbl_UserID.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					lbl_EventAction = new JLabel4j_std();
					lbl_EventAction.setBounds(318, 45, 140, 22);
					jDesktopPane1.add(lbl_EventAction);
					lbl_EventAction.setText(lang.get("lbl_Event_Action"));
					lbl_EventAction.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldWorkstation = new JTextField4j(JDBAuditPermissions.field_workstation_id);
					jTextFieldWorkstation.setBounds(124, 45, 175, 22);
					jDesktopPane1.add(jTextFieldWorkstation);
				}
				{
					lbl_SortBy = new JLabel4j_std();
					lbl_SortBy.setBounds(480, 78, 135, 22);
					jDesktopPane1.add(lbl_SortBy);
					lbl_SortBy.setText(lang.get("lbl_Sort_By"));
					lbl_SortBy.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					eventsFrom = new JDateControl();
					eventsFrom.setBounds(146, 78, 128, 22);
					jDesktopPane1.add(eventsFrom);
					eventsFrom.setEnabled(false);
					eventsFrom.getEditor().setPreferredSize(new java.awt.Dimension(86, 32));
				}
				{
					lbl_EventDate = new JLabel4j_std();
					lbl_EventDate.setBounds(0, 78, 110, 22);
					jDesktopPane1.add(lbl_EventDate);
					lbl_EventDate.setText(lang.get("lbl_Message_Event_Date"));
					lbl_EventDate.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jCheckBoxEventsFrom = new JCheckBox4j();
					jCheckBoxEventsFrom.setBounds(120, 78, 22, 22);
					jDesktopPane1.add(jCheckBoxEventsFrom);
					jCheckBoxEventsFrom.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxEventsFrom.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							System.out.println("jCheckBoxDueDateFrom.actionPerformed, event=" + evt);
							if (jCheckBoxEventsFrom.isSelected())
							{
								eventsFrom.setEnabled(true);
								calendarButtonEventFrom.setEnabled(true);
							}
							else
							{
								eventsFrom.setEnabled(false);
								calendarButtonEventFrom.setEnabled(false);
							}
						}
					});
				}
				{
					jCheckBoxEventsTo = new JCheckBox4j();
					jCheckBoxEventsTo.setBounds(303, 78, 22, 22);
					jDesktopPane1.add(jCheckBoxEventsTo);
					jCheckBoxEventsTo.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxEventsTo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							System.out.println("jCheckBoxDueDateFrom.actionPerformed, event=" + evt);
							if (jCheckBoxEventsTo.isSelected())
							{
								eventsTo.setEnabled(true);
								calendarButtonEventTo.setEnabled(true);
							}
							else
							{
								eventsTo.setEnabled(false);
								calendarButtonEventTo.setEnabled(false);
							}
						}
					});
				}
				{
					eventsTo = new JDateControl();
					eventsTo.setBounds(330, 78, 128, 22);
					jDesktopPane1.add(eventsTo);
					eventsTo.setEnabled(false);
				}
				{
					ComboBoxModel<String> jComboBoxStatusModel = new DefaultComboBoxModel<String>(Common.auditEventActions);
					jComboBoxEventAction = new JComboBox4j<String>();
					jComboBoxEventAction.setBounds(470, 45, 194, 22);
					jDesktopPane1.add(jComboBoxEventAction);
					jComboBoxStatusModel.setSelectedItem("");
					jComboBoxEventAction.setModel(jComboBoxStatusModel);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[] { "SYS_AUDIT_LOG_ID", "EVENT_TIME", "USER_ID", "EVENT_TYPE", "EVENT_ACTION", "TARGET", "DATA", "WORKSTATION" });
					jComboBoxSortBy = new JComboBox4j<String>();
					jComboBoxSortBy.setBounds(625, 78, 168, 22);
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					jComboBoxSortBy.getModel().setSelectedItem("SYS_AUDIT_LOG_ID");
				}
				{
					lbl_Workstation = new JLabel4j_std();
					lbl_Workstation.setBounds(0, 45, 110, 22);
					jDesktopPane1.add(lbl_Workstation);
					lbl_Workstation.setText(lang.get("lbl_Workstation"));
					lbl_Workstation.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jToggleButtonSequence = new JToggleButton4j();
					jToggleButtonSequence.setBounds(791, 78, 22, 22);
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							setSequence(jToggleButtonSequence.isSelected());
						}
					});

				}
				
				{
					jButtonUserIDLookup = new JButton4j(Common.icon_lookup_16x16);
					jButtonUserIDLookup.setBounds(274, 11, 21, 22);
					jDesktopPane1.add(jButtonUserIDLookup);
					jButtonUserIDLookup.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchLookup.dlgCriteriaDefault = "";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.users())
							{
								jTextFieldUserID.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}

				{
					lbl_Limit = new JLabel4j_std();
					lbl_Limit.setBounds(791, 78, 100, 22);
					jDesktopPane1.add(lbl_Limit);
					lbl_Limit.setText(lang.get("lbl_Limit"));
					lbl_Limit.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jCheckBoxLimit = new JCheckBox4j();
					jCheckBoxLimit.setBounds(896, 76, 22, 22);
					jDesktopPane1.add(jCheckBoxLimit);
					jCheckBoxLimit.setSelected(true);
					jCheckBoxLimit.setBackground(new java.awt.Color(255, 255, 255));
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
					
					SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
					jSpinnerIntModel.setMinimum(1);
					jSpinnerIntModel.setMaximum(5000);
					jSpinnerIntModel.setStepSize(1);					
					
					jSpinnerLimit = new JSpinner4j();
					jDesktopPane1.add(jSpinnerLimit);
					
					jSpinnerLimit.setModel(jSpinnerIntModel);
					JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(jSpinnerLimit);
					jSpinnerLimit.setEditor(ne);
					jSpinnerLimit.setBounds(917, 78, 63, 22);
					jSpinnerLimit.setValue(1000);
					jSpinnerLimit.getEditor().setSize(45, 21);
					

				}

				{
					jStatusBar = new JLabel4j_std();
					jStatusBar.setBounds(0, 545, 1000, 21);
					jStatusBar.setForeground(new Color(255, 0, 0));
					jStatusBar.setBackground(Color.GRAY);
					jStatusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusBar);
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.setBounds(546, 113, 109, 32);
					jButtonExcel.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							excel();
						}
					});
					jButtonExcel.setMnemonic(KeyEvent.VK_H);
					jButtonExcel.setText(lang.get("btn_Excel"));
					jDesktopPane1.add(jButtonExcel);
				}

				{
					lbl_EventType = new JLabel4j_std();
					lbl_EventType.setBounds(318, 11, 140, 22);
					lbl_EventType.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_EventType.setText(lang.get("lbl_Event_Type"));
					jDesktopPane1.add(lbl_EventType);
				}

				{
					ComboBoxModel<String> jComboBox2Model = new DefaultComboBoxModel<String>(Common.auditEventTypes);
					jComboBoxEventType = new JComboBox4j<String>();
					jComboBoxEventType.setBounds(470, 11, 194, 22);
					jComboBoxEventType.setModel(jComboBox2Model);
					jComboBoxEventType.setMaximumRowCount(12);
					jDesktopPane1.add(jComboBoxEventType);
				}

				{
					jButtonClear = new JButton4j(Common.icon_clear_16x16);
					jButtonClear.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							clearFilter();
						}
					});
					jButtonClear.setBounds(327, 113, 109, 32);
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jDesktopPane1.add(jButtonClear);
				}
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

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
