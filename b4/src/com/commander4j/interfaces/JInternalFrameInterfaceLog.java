package com.commander4j.interfaces;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameInterfaceLog.java
 * 
 * Package Name : com.commander4j.interfaces
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.*;
import com.commander4j.messages.OutgoingDespatchConfirmation;
import com.commander4j.messages.OutgoingDespatchPreAdvice;
import com.commander4j.messages.OutgoingEquipmentTracking;
import com.commander4j.messages.OutgoingProductionDeclarationConfirmation;
import com.commander4j.sys.Common;
import com.commander4j.tablemodel.JDBInterfaceLogTableModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JUtility;

public class JInternalFrameInterfaceLog extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JTable jTable1;
	private JButton4j jButtonRefresh;
	private JScrollPane jScrollPane1;
	private SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
	private JSpinner spinner = new JSpinner();
	private int row;
	private SelectionListener listener = new SelectionListener();
	private JDateControl dateControlfrom = new JDateControl();
	private JDateControl dateControlTo = new JDateControl();
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JTextField4j errorMessage;
	private JLabel4j_std lblMessageDate;
	private JTextField4j messageDate;
	private JTextField4j workstation;
	private JTextField4j textFieldMessageRef;
	private JTextField4j textFieldMessageInfo;
	private JCheckBox4j checkBoxLimit = new JCheckBox4j();
	private JCheckBox4j checkBoxEventDate = new JCheckBox4j();
	private JComboBox4j<String> comboBoxInterfaceType = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxDirection = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxStatus = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxAction = new JComboBox4j<String>();
	private JTextField4j textFielderrorMessage;
	private JButton4j button_1;
	private JLabel4j_std jStatusBar = new JLabel4j_std();
	private JLabel4j_std lblFilename;
	private JTextField4j textFieldFilename;
	private JMenuItem4j mntmResubmit;
	private JMenuItem4j mntmExport;
	private JMenu4j mnFilterBy;
	private JMenuItem4j mntmMessageRef;
	private JMenuItem4j mntmMessageInfo;
	private JMenuItem4j mntmStatus;
	private JMenuItem4j mntmDirection;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCalendarButton calendarButtonFrom;
	private JCalendarButton calendarButtonTo;
	private PreparedStatement listStatement;

	public JInternalFrameInterfaceLog()
	{
		super();
		setIconifiable(true);
		initGUI();
		jTable1.getSelectionModel().addListSelectionListener(listener);
		jTable1.getColumnModel().getSelectionModel().addListSelectionListener(listener);

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}SYS_INTERFACE_LOG where 1=2"));
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		Date start = dateControlfrom.getDate();
		Calendar calstart = Calendar.getInstance();
		calstart.setTime(start);
		calstart.add(Calendar.HOUR, -1);
		dateControlfrom.setEnabled(false);
		dateControlfrom.setDate(calstart.getTime());
		{
			checkBoxEventDate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (checkBoxEventDate.isSelected())
					{
						dateControlfrom.setEnabled(true);
						dateControlTo.setEnabled(true);
						calendarButtonFrom.setEnabled(true);
						calendarButtonTo.setEnabled(true);
					}
					else
					{
						dateControlfrom.setEnabled(false);
						dateControlTo.setEnabled(false);
						calendarButtonFrom.setEnabled(false);
						calendarButtonTo.setEnabled(false);
					}
				}
			});

			checkBoxEventDate.setBackground(Color.WHITE);
			checkBoxEventDate.setBounds(124, 110, 21, 21);
			jDesktopPane1.add(checkBoxEventDate);
		}
		{

			checkBoxLimit.setSelected(true);
			checkBoxLimit.setBackground(Color.WHITE);
			checkBoxLimit.setBounds(863, 109, 21, 21);
			jDesktopPane1.add(checkBoxLimit);
		}
		{
			JLabel4j_std label = new JLabel4j_std(lang.get("lbl_Message_Error"));
			label.setHorizontalAlignment(SwingConstants.TRAILING);
			label.setBounds(356, 72, 127, 25);
			jDesktopPane1.add(label);
		}
		{
			textFielderrorMessage = new JTextField4j(JDBInterfaceLog.field_message_error);
			textFielderrorMessage.setColumns(10);
			textFielderrorMessage.setBounds(492, 75, 484, 22);
			jDesktopPane1.add(textFielderrorMessage);
		}
		{
			JButton4j button = new JButton4j(Common.icon_XLS);

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					excel();
				}
			});
			button.setText(lang.get("btn_Excel"));
			button.setMnemonic(lang.getMnemonicChar());
			button.setBounds(423, 135, 139, 32);
			jDesktopPane1.add(button);
		}

		button_1 = new JButton4j(Common.icon_clear);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFilter();
			}
		});
		button_1.setText(lang.get("btn_Clear_Filter"));
		button_1.setMnemonic(lang.getMnemonicChar());
		button_1.setBounds(143, 135, 139, 32);
		jDesktopPane1.add(button_1);
		{

			jStatusBar.setForeground(Color.BLACK);
			jStatusBar.setBackground(Color.GRAY);
			jStatusBar.setBounds(1, 569, 1004, 24);
			jStatusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			jDesktopPane1.add(jStatusBar);
		}
		{
			lblFilename = new JLabel4j_std(lang.get("lbl_Interface_Filename"));
			lblFilename.setHorizontalAlignment(SwingConstants.TRAILING);
			lblFilename.setBounds(11, 530, 96, 21);
			jDesktopPane1.add(lblFilename);
		}

		textFieldFilename = new JTextField4j();
		textFieldFilename.setEditable(false);
		textFieldFilename.setColumns(10);
		textFieldFilename.setBounds(112, 531, 860, 20);
		jDesktopPane1.add(textFieldFilename);

		JButton4j btnResubmit = new JButton4j(Common.icon_release);

		btnResubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reSubmit();
			}
		});
		btnResubmit.setText(lang.get("btn_Resubmit"));
		btnResubmit.setMnemonic(lang.getMnemonicChar());
		btnResubmit.setBounds(283, 135, 139, 32);
		jDesktopPane1.add(btnResubmit);

		JButton4j btnDelete = new JButton4j(Common.icon_delete);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				delete();
			}
		});
		btnDelete.setToolTipText("Export all rows below to spreadsheet.");
		btnDelete.setText(lang.get("btn_Delete"));
		btnDelete.setMnemonic(lang.getMnemonicChar());
		btnDelete.setBounds(563, 135, 139, 32);
		jDesktopPane1.add(btnDelete);

		JButton4j btnArchiveResolved = new JButton4j(Common.icon_delete);
		btnArchiveResolved.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				archive();
			}
		});
		btnArchiveResolved.setToolTipText("Export all rows below to spreadsheet.");
		btnArchiveResolved.setText(lang.get("btn_Archive"));
		btnArchiveResolved.setMnemonic(lang.getMnemonicChar());
		btnArchiveResolved.setBounds(703, 135, 139, 32);
		jDesktopPane1.add(btnArchiveResolved);
		
		calendarButtonFrom = new JCalendarButton(dateControlfrom);
		calendarButtonFrom.setEnabled(false);
		calendarButtonFrom.setBounds(284, 110, 21, 21);
		jDesktopPane1.add(calendarButtonFrom);
		
		calendarButtonTo = new JCalendarButton(dateControlTo);
		calendarButtonTo.setEnabled(false);
		calendarButtonTo.setBounds(439, 110, 21, 21);
		jDesktopPane1.add(calendarButtonTo);
		
		JLabel4j_std label4j_std = new JLabel4j_std(lang.get("lbl_Message_Action"));
		label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std.setBounds(5, 72, 115, 25);
		jDesktopPane1.add(label4j_std);
	}

	private void reSubmit() {

		if (jTable1.getSelectedRowCount() > 0)
		{
			int first = jTable1.getSelectionModel().getMinSelectionIndex();
			int last = jTable1.getSelectionModel().getMaxSelectionIndex();
			for (int x = first; x <= last; x++)
			{
				if (jTable1.getSelectionModel().isSelectedIndex(x))
				{
					jTable1.getModel().getValueAt(x, 2);
					String transactionType = (String) jTable1.getModel().getValueAt(x, 2);
					String direction = (String) jTable1.getModel().getValueAt(x, 5);
					String messageRef = (String) jTable1.getModel().getValueAt(x, 3);
					String status = (String) jTable1.getModel().getValueAt(x, 7);
					String filename = (String) jTable1.getModel().getValueAt(x, 10);

					if (direction.equals("Output"))
					{
						if (transactionType.endsWith("Production Declaration"))
						{
							OutgoingProductionDeclarationConfirmation opdc = new OutgoingProductionDeclarationConfirmation(Common.selectedHostID, Common.sessionID);
							//opdc.processMessage(Long.valueOf(messageRef));
							opdc.submit(Long.valueOf(messageRef));
							errorMessage.setText(opdc.getErrorMessage());
							opdc = null;
						}

						if (transactionType.equals("Despatch Confirmation"))
						{
							OutgoingDespatchConfirmation odc = new OutgoingDespatchConfirmation(Common.selectedHostID, Common.sessionID);
							odc.submit(Long.valueOf(messageRef));
							errorMessage.setText(odc.getErrorMessage());
							odc = null;
						}

						if (transactionType.equals("Despatch Pre Advice"))
						{
							OutgoingDespatchPreAdvice opa = new OutgoingDespatchPreAdvice(Common.selectedHostID, Common.sessionID);
							opa.submit(Long.valueOf(messageRef));
							errorMessage.setText(opa.getErrorMessage());
							opa = null;
						}

						if (transactionType.equals("Equipment Tracking"))
						{
							OutgoingEquipmentTracking oet = new OutgoingEquipmentTracking(Common.selectedHostID, Common.sessionID);
							oet.submit(Long.valueOf(messageRef));
							errorMessage.setText(oet.getErrorMessage());
							oet = null;
						}
					}

					if (direction.equals("Input"))
					{
						if (status.equals("Error"))
						{

							if (filename.equals("") == false)
							{
								JDBInterfaceRequest ir = new JDBInterfaceRequest(Common.selectedHostID, Common.sessionID);
								ir.write(filename, transactionType, "Inbound File Re-Submit");
							}
						}
					}
				}
			}
		}
	}

	private void archive() {
		int n = JOptionPane.showConfirmDialog(Common.mainForm, "Archive Successful/Resolved Log Records ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
		if (n == 0)
		{
			JDBInterfaceLog interfaceLog = new JDBInterfaceLog(Common.selectedHostID, Common.sessionID);
			interfaceLog.archive();
			buildSQL();
			populateList();
		}
	}

	private void delete() {
		int n = JOptionPane.showConfirmDialog(Common.mainForm, "Delete selected Log record(s) ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
		if (n == 0)
		{

			JDBInterfaceLog interfaceLog = new JDBInterfaceLog(Common.selectedHostID, Common.sessionID);

			if (jTable1.getSelectedRowCount() > 0)
			{
				int first = jTable1.getSelectionModel().getMinSelectionIndex();
				int last = jTable1.getSelectionModel().getMaxSelectionIndex();
				for (int x = first; x <= last; x++)
				{
					if (jTable1.getSelectionModel().isSelectedIndex(x))
					{
						jTable1.getModel().getValueAt(x, 2);
						Long logID = (Long) jTable1.getModel().getValueAt(x, 0);
						interfaceLog.setInterfaceLogID(logID);
						interfaceLog.delete();
					}
				}
			}
			buildSQL();
			populateList();
		}
	}

	private void filterBy(String fieldname) {
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			if (fieldname.equals("Message Ref") == true)
			{
				textFieldMessageRef.setText(jTable1.getValueAt(row, JDBInterfaceLogTableModel.message_ref_Col).toString());
			}
			if (fieldname.equals("Message Info") == true)
			{
				textFieldMessageInfo.setText(jTable1.getValueAt(row, JDBInterfaceLogTableModel.message_info_Col).toString());
			}
			if (fieldname.equals("Direction") == true)
			{
				comboBoxDirection.setSelectedItem(jTable1.getValueAt(row, JDBInterfaceLogTableModel.interface_direction_Col).toString());
			}
			if (fieldname.equals("Status") == true)
			{
				comboBoxStatus.setSelectedItem(jTable1.getValueAt(row, JDBInterfaceLogTableModel.message_status_Col).toString());
			}
			
			buildSQL();
			populateList();

		}
	}

	private void clearFilter() {
		checkBoxEventDate.setSelected(false);
		dateControlfrom.setEnabled(false);
		dateControlTo.setEnabled(false);
		comboBoxInterfaceType.setSelectedItem("");
		comboBoxDirection.setSelectedItem("");
		comboBoxStatus.setSelectedItem("");
		comboBoxAction.setSelectedItem("");
		textFieldMessageRef.setText("");
		textFieldMessageInfo.setText("");
		textFielderrorMessage.setText("");
		textFieldFilename.setText("");
		buildSQL();
		populateList();
	}

	private void refresh() {

		buildSQL();
		populateList();
	}

	private void excel() {
		JExcel export = new JExcel();

		ResultSet rs = null;
		try
		{
			rs = listStatement.executeQuery();

		}
		catch (SQLException e)
		{

		}
		export.saveAs("interface_log.xls", rs, Common.mainForm);
		populateList();
	}

	private void buildSQL() {

		JDBQuery.closeStatement(listStatement);
		
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		String temp = JUtility.substSchemaName(schemaName, "select * from {schema}SYS_INTERFACE_LOG");
		
		query.addText(temp);

		if (textFieldMessageRef.getText().equals("") == false)
		{
			query.addParamtoSQL("message_ref like ", "%" + textFieldMessageRef.getText() + "%");
		}

		if (textFieldMessageInfo.getText().equals("") == false)
		{
			query.addParamtoSQL("message_information like ", "%" + textFieldMessageInfo.getText() + "%");
		}

		if (textFielderrorMessage.getText().equals("") == false)
		{
			query.addParamtoSQL("message_error like ", "%" + textFielderrorMessage.getText() + "%");
		}

		if (checkBoxEventDate.isSelected())
		{
			query.addParamtoSQL("event_time>=", JUtility.getTimestampFromDate(dateControlfrom.getDate()));
		}

		if (checkBoxEventDate.isSelected())
		{
			query.addParamtoSQL("event_time<=", JUtility.getTimestampFromDate(dateControlTo.getDate()));
		}

		if (comboBoxInterfaceType.getSelectedItem().toString().equals("") == false)
		{
			query.addParamtoSQL("interface_type=", comboBoxInterfaceType.getSelectedItem().toString());
		}

		if (comboBoxDirection.getSelectedItem().toString().equals("") == false)
		{
			query.addParamtoSQL("interface_direction=", comboBoxDirection.getSelectedItem().toString());
		}

		if (comboBoxStatus.getSelectedItem().toString().equals("") == false)
		{
			query.addParamtoSQL("message_status=", comboBoxStatus.getSelectedItem().toString());
		}

		if (comboBoxAction.getSelectedItem().toString().equals("") == false)
		{
			query.addParamtoSQL("action=", comboBoxAction.getSelectedItem().toString());
		}

		query.appendSort("interface_log_id", "desc");
		query.applyRestriction(checkBoxLimit.isSelected(), Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), spinner.getValue());
		query.bindParams();

		listStatement = query.getPreparedStatement();	

	}

	private void populateList() {
		JDBInterfaceLog interfaceLog = new JDBInterfaceLog(Common.selectedHostID, Common.sessionID);
		JDBInterfaceLogTableModel interfaceLogTable = new JDBInterfaceLogTableModel(interfaceLog.getInterfaceDataResultSet(listStatement));
		TableRowSorter<JDBInterfaceLogTableModel> sorter = new TableRowSorter<JDBInterfaceLogTableModel>(interfaceLogTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(interfaceLogTable);

		jScrollPane1.setViewportView(jTable1);

		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.event_time_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.interface_log_id_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.interface_type_Col).setPreferredWidth(190);
		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.message_ref_Col).setPreferredWidth(160);

		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.message_info_Col).setPreferredWidth(180);
		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.interface_direction_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.action_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.message_status_Col).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.filename_id_Col).setPreferredWidth(600);
		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.message_error_Col).setPreferredWidth(600);
		jTable1.getColumnModel().getColumn(JDBInterfaceLogTableModel.workstation_id_Col).setPreferredWidth(80);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusBar, checkBoxLimit.isSelected(), Integer.valueOf(spinner.getValue().toString()), jTable1.getRowCount());
		
	}

	private class SelectionListener implements ListSelectionListener
	{

		public void valueChanged(ListSelectionEvent e) {
			row = jTable1.getSelectedRow();
			if (row >= 0)
			{
				errorMessage.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.message_error_Col).toString());
				workstation.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.workstation_id_Col).toString());
				messageDate.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.message_date_Col).toString().substring(0, 16));
				textFieldFilename.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.filename_id_Col).toString());
			}

		}

	}

	public void displayRecord() {
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			errorMessage.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.message_error_Col).toString());
			workstation.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.workstation_id_Col).toString());
			messageDate.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.message_date_Col).toString().substring(0, 16));
			textFieldFilename.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.filename_id_Col).toString());
		}

	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 1015+Common.LFAdjustWidth, 628+Common.LFAdjustHeight);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Interface Log");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(483, 266));
				jDesktopPane1.setLayout(null);

				{
					jButtonRefresh = new JButton4j(Common.icon_search);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Search"));
					jButtonRefresh.setMnemonic(KeyEvent.VK_S);
					jButtonRefresh.setBounds(3, 135, 139, 32);
					jButtonRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							refresh();

						}
					});
				}
				jScrollPane1 = new JScrollPane();
				jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
				jDesktopPane1.add(jScrollPane1);
				jScrollPane1.setBounds(1, 172, 983, 290);
				jTable1 = new JTable();
				jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
				jScrollPane1.setViewportView(jTable1);

				jTable1.getTableHeader().setFont(Common.font_table_header);
				jTable1.getTableHeader().setForeground(Common.color_tableHeaderFont);
				jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				jTable1.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						if (evt.getClickCount() == 1)
						{

							displayRecord();

						}
					}
				});
				{
					final JPopupMenu popupMenu = new JPopupMenu();
					addPopup(jTable1, popupMenu);
					{
						mntmResubmit = new JMenuItem4j("Re-Submit");
						mntmResubmit.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								reSubmit();
							}
						});
						mntmResubmit.setIcon(Common.icon_release);
						popupMenu.add(mntmResubmit);
					}
					{
						mntmExport = new JMenuItem4j("Export");
						mntmExport.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								excel();
							}
						});
						mntmExport.setIcon(Common.icon_XLS);
						popupMenu.add(mntmExport);
					}
					{
						mnFilterBy = new JMenu4j("Filter By");
						popupMenu.add(mnFilterBy);
						{
							mntmDirection = new JMenuItem4j("Direction");
							mntmDirection.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									filterBy("Direction");
								}
							});
							mnFilterBy.add(mntmDirection);
						}
						{
							mntmMessageRef = new JMenuItem4j("Message Ref");
							mntmMessageRef.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									filterBy("Message Ref");
								}
							});
							mnFilterBy.add(mntmMessageRef);
						}
						{
							mntmMessageInfo = new JMenuItem4j("Message Info");
							mntmMessageInfo.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									filterBy("Message Info");
								}
							});
							mnFilterBy.add(mntmMessageInfo);
						}
						{
							mntmStatus = new JMenuItem4j("Status");
							mntmStatus.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									filterBy("Status");
								}
							});
							mnFilterBy.add(mntmStatus);
						}
					}
				}

				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setBounds(843, 135, 139, 32);
					{

						jSpinnerIntModel.setValue(1000);
						jSpinnerIntModel.setMinimum(1000);
						jSpinnerIntModel.setMaximum(5000);
						jSpinnerIntModel.setStepSize(100);
						spinner.setModel(jSpinnerIntModel);
						JSpinner.NumberEditor ne = new JSpinner.NumberEditor(spinner);
						ne.getTextField().setFont(Common.font_std); 
						spinner.setEditor(ne);
						spinner.setFont(Common.font_std);
						spinner.setBounds(896, 109, 80, 22);
						jDesktopPane1.add(spinner);
					}
					{
						JLabel4j_std lblErrorMessage = new JLabel4j_std(lang.get("lbl_Message_Error"));
						lblErrorMessage.setBounds(11, 499, 96, 21);
						lblErrorMessage.setHorizontalAlignment(SwingConstants.TRAILING);
						jDesktopPane1.add(lblErrorMessage);
					}
					{
						errorMessage = new JTextField4j();
						errorMessage.setEditable(false);
						errorMessage.setBounds(112, 500, 860, 20);
						jDesktopPane1.add(errorMessage);
						errorMessage.setColumns(10);
					}
					{
						JLabel4j_std lblRowsToDisplay = new JLabel4j_std(lang.get("lbl_Limit"));
						lblRowsToDisplay.setHorizontalAlignment(SwingConstants.TRAILING);
						lblRowsToDisplay.setBounds(779, 109, 81, 21);
						jDesktopPane1.add(lblRowsToDisplay);
					}

					lblMessageDate = new JLabel4j_std(lang.get("lbl_Message_Date"));
					lblMessageDate.setHorizontalAlignment(SwingConstants.TRAILING);
					lblMessageDate.setBounds(21, 470, 84, 21);
					jDesktopPane1.add(lblMessageDate);
					{
						messageDate = new JTextField4j();
						messageDate.setEditable(false);
						messageDate.setColumns(10);
						messageDate.setBounds(112, 470, 146, 20);
						jDesktopPane1.add(messageDate);
					}
					{
						workstation = new JTextField4j();
						workstation.setEditable(false);
						workstation.setColumns(10);
						workstation.setBounds(370, 470, 376, 20);
						jDesktopPane1.add(workstation);
					}
					{
						JLabel4j_std lblWorkstation = new JLabel4j_std(lang.get("lbl_Workstation"));
						lblWorkstation.setHorizontalAlignment(SwingConstants.TRAILING);
						lblWorkstation.setBounds(282, 470, 81, 21);
						jDesktopPane1.add(lblWorkstation);
					}
					{
						comboBoxInterfaceType.setMaximumRowCount(20);

						comboBoxInterfaceType.setModel(new DefaultComboBoxModel<String>(Common.messageTypesincBlank));
						comboBoxInterfaceType.setBounds(766, 12, 210, 23);
						jDesktopPane1.add(comboBoxInterfaceType);
					}
					{

						comboBoxDirection.setModel(new DefaultComboBoxModel<String>(new String[] { "", "Input", "Output" }));
						comboBoxDirection.setBounds(130, 12, 136, 23);
						jDesktopPane1.add(comboBoxDirection);
					}
					{

						comboBoxStatus.setModel(new DefaultComboBoxModel<String>(new String[] { "", "Error", "Success", "Warning" }));
						comboBoxStatus.setBounds(457, 12, 113, 23);
						jDesktopPane1.add(comboBoxStatus);
					}
					{

						comboBoxAction.setModel(new DefaultComboBoxModel<String>(new String[] { "", "DB Update", "File Write" }));
						comboBoxAction.setBounds(132, 74, 136, 23);
						jDesktopPane1.add(comboBoxAction);
					}
					{
						JLabel4j_std lblMessageType = new JLabel4j_std(lang.get("lbl_Message_Type"));
						lblMessageType.setHorizontalAlignment(SwingConstants.TRAILING);
						lblMessageType.setBounds(588, 12, 172, 25);
						jDesktopPane1.add(lblMessageType);
					}
					{
						JLabel4j_std lblDirection = new JLabel4j_std(lang.get("lbl_Interface_Direction"));
						lblDirection.setHorizontalAlignment(SwingConstants.TRAILING);
						lblDirection.setBounds(5, 12, 115, 25);
						jDesktopPane1.add(lblDirection);
					}
					{
						JLabel4j_std lblStatus = new JLabel4j_std(lang.get("lbl_Message_Status"));
						lblStatus.setHorizontalAlignment(SwingConstants.TRAILING);
						lblStatus.setBounds(287, 12, 163, 25);
						jDesktopPane1.add(lblStatus);
					}
					{
						textFieldMessageRef = new JTextField4j(JDBInterfaceLog.field_message_ref);
						textFieldMessageRef.setBounds(130, 45, 213, 22);
						jDesktopPane1.add(textFieldMessageRef);
						textFieldMessageRef.setColumns(10);
					}
					{
						textFieldMessageInfo = new JTextField4j(JDBInterfaceLog.field_message_information);
						textFieldMessageInfo.setColumns(10);
						textFieldMessageInfo.setBounds(492, 45, 484, 22);
						jDesktopPane1.add(textFieldMessageInfo);
					}
					{
						JLabel4j_std lblMessageRef = new JLabel4j_std(lang.get("lbl_Message_Reference"));
						lblMessageRef.setHorizontalAlignment(SwingConstants.TRAILING);
						lblMessageRef.setBounds(5, 42, 113, 25);
						jDesktopPane1.add(lblMessageRef);
					}
					{
						JLabel4j_std lblMessageInfo = new JLabel4j_std(lang.get("lbl_Message_Information"));
						lblMessageInfo.setHorizontalAlignment(SwingConstants.TRAILING);
						lblMessageInfo.setBounds(356, 46, 127, 21);
						jDesktopPane1.add(lblMessageInfo);
					}
					{
						JLabel4j_std lblEventDate = new JLabel4j_std();
						lblEventDate.setText(lang.get("lbl_Message_Event_Date"));
						lblEventDate.setHorizontalAlignment(SwingConstants.TRAILING);
						lblEventDate.setBounds(3, 110, 113, 21);
						jDesktopPane1.add(lblEventDate);
					}
					{

						dateControlfrom.setBounds(157, 106, 125, 25);
						jDesktopPane1.add(dateControlfrom);
					}
					{
						dateControlTo.setEnabled(false);
						dateControlTo.setBounds(312, 106, 125, 25);
						jDesktopPane1.add(dateControlTo);
					}
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JDBQuery.closeStatement(listStatement);
							dispose();
						}
					});
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
				{
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
