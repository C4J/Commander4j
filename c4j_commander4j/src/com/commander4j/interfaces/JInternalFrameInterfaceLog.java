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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.messages.OutgoingDespatchConfirmation;
import com.commander4j.messages.OutgoingDespatchEmail;
import com.commander4j.messages.OutgoingDespatchPreAdvice;
import com.commander4j.messages.OutgoingEquipmentTracking;
import com.commander4j.messages.OutgoingPalletIssue;
import com.commander4j.messages.OutgoingPalletReturn;
import com.commander4j.messages.OutgoingProductionDeclarationConfirmation;
import com.commander4j.messages.OutgoingProductionUnConfirm;
import com.commander4j.sys.Common;
import com.commander4j.tablemodel.JDBInterfaceLogTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JUtility;

public class JInternalFrameInterfaceLog extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane4j jDesktopPane1;
	private JButton4j jButtonClose;
	private JTable4j jTable1;
	private JButton4j jButtonRefresh;
	private JScrollPane4j jScrollPane1;
	private SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
	private JSpinner4j spinner = new JSpinner4j();
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
	private JLabel4j_status jStatusBar = new JLabel4j_status();
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
	}

	public JInternalFrameInterfaceLog(String keyField, String keyValue)
	{
		super();
		setIconifiable(true);
		initGUI();
		clearFilter();

		updateSearch(keyField, keyValue);

	}

	public void updateSearch(String keyField, String keyValue)
	{
		clearFilter();

		if (keyField.equals("PROCESS_ORDER"))
		{
			textFieldMessageInfo.setText(keyValue);
		}

		if (keyField.equals("MATERIAL"))
		{
			textFieldMessageInfo.setText(keyValue);
		}

		if (keyField.equals("SSCC"))
		{
			textFieldMessageInfo.setText(keyValue);
		}

		if (keyField.equals("MATERIAL BATCH"))
		{
			textFieldMessageInfo.setText(keyValue);
		}

		if (keyField.equals("MESSAGE REF"))
		{
			textFieldMessageRef.setText(keyValue);
		}

		buildSQL();
		populateList();
	}

	private void reSubmit()
	{

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
							// opdc.processMessage(Long.valueOf(messageRef));
							opdc.submit(Long.valueOf(messageRef));
							errorMessage.setText(opdc.getErrorMessage());
							opdc = null;
						}

						if (transactionType.endsWith("Pallet Issue"))
						{
							OutgoingPalletIssue opi = new OutgoingPalletIssue(Common.selectedHostID, Common.sessionID);
							// opdc.processMessage(Long.valueOf(messageRef));
							opi.submit(Long.valueOf(messageRef));
							errorMessage.setText(opi.getErrorMessage());
							opi = null;
						}

						if (transactionType.endsWith("Pallet Return"))
						{
							OutgoingPalletReturn opr = new OutgoingPalletReturn(Common.selectedHostID, Common.sessionID);
							// opdc.processMessage(Long.valueOf(messageRef));
							opr.submit(Long.valueOf(messageRef));
							errorMessage.setText(opr.getErrorMessage());
							opr = null;
						}

						if (transactionType.endsWith("Production Unconfirm"))
						{
							OutgoingProductionUnConfirm opuc = new OutgoingProductionUnConfirm(Common.selectedHostID, Common.sessionID);
							// opdc.processMessage(Long.valueOf(messageRef));
							opuc.submit(Long.valueOf(messageRef));
							errorMessage.setText(opuc.getErrorMessage());
							opuc = null;
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

						if (transactionType.equals("Despatch Email"))
						{
							OutgoingDespatchEmail ode = new OutgoingDespatchEmail(Common.selectedHostID, Common.sessionID);
							ode.submit(Long.valueOf(messageRef));
							errorMessage.setText(ode.getErrorMessage());
							ode = null;
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

	private void archive()
	{
		int n = JOptionPane.showConfirmDialog(Common.mainForm, "Archive Successful/Resolved Log Records ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
		if (n == 0)
		{
			JDBInterfaceLog interfaceLog = new JDBInterfaceLog(Common.selectedHostID, Common.sessionID);
			interfaceLog.archive();
			buildSQL();
			populateList();
		}
	}

	private void delete()
	{
		int n = JOptionPane.showConfirmDialog(Common.mainForm, "Delete selected Log record(s) ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
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

	private void filterBy(String fieldname)
	{
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

	private void clearFilter()
	{
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

	private void refresh()
	{

		buildSQL();
		populateList();
	}

	private void excel()
	{
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

	private void buildSQL()
	{

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

	private void populateList()
	{
		JDBInterfaceLog interfaceLog = new JDBInterfaceLog(Common.selectedHostID, Common.sessionID);
		JDBInterfaceLogTableModel interfaceLogTable = new JDBInterfaceLogTableModel(interfaceLog.getInterfaceDataResultSet(listStatement));
		TableRowSorter<JDBInterfaceLogTableModel> sorter = new TableRowSorter<JDBInterfaceLogTableModel>(interfaceLogTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(interfaceLogTable);

		jScrollPane1.setViewportView(jTable1);

		jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

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

		public void valueChanged(ListSelectionEvent e)
		{
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

	public void displayRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			errorMessage.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.message_error_Col).toString());
			workstation.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.workstation_id_Col).toString());
			messageDate.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.message_date_Col).toString().substring(0, 16));
			textFieldFilename.setText(jTable1.getModel().getValueAt(row, JDBInterfaceLogTableModel.filename_id_Col).toString());
		}

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 1000, 638);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Interface Log");

			jDesktopPane1 = new JDesktopPane4j();

			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(483, 266));
			jDesktopPane1.setLayout(null);

			jButtonRefresh = new JButton4j(Common.icon_search_16x16);
			jDesktopPane1.add(jButtonRefresh);
			jButtonRefresh.setText(lang.get("btn_Search"));
			jButtonRefresh.setMnemonic(KeyEvent.VK_S);
			jButtonRefresh.setBounds(3, 136, 139, 32);
			jButtonRefresh.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					refresh();

				}
			});

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.Table);
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.setBounds(-1, 179, 983, 290);
			jTable1 = new JTable4j();
			jScrollPane1.setViewportView(jTable1);

			jTable1.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent evt)
				{
					if (evt.getClickCount() == 1)
					{

						displayRecord();

					}
				}
			});

			final JPopupMenu popupMenu = new JPopupMenu();
			addPopup(jTable1, popupMenu);

			mntmResubmit = new JMenuItem4j("Re-Submit");
			mntmResubmit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					reSubmit();
				}
			});
			mntmResubmit.setIcon(Common.icon_release_16x16);
			popupMenu.add(mntmResubmit);

			mntmExport = new JMenuItem4j("Export");
			mntmExport.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					excel();
				}
			});
			mntmExport.setIcon(Common.icon_XLS_16x16);
			popupMenu.add(mntmExport);

			mnFilterBy = new JMenu4j("Filter By");
			popupMenu.add(mnFilterBy);

			mntmDirection = new JMenuItem4j("Direction");
			mntmDirection.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					filterBy("Direction");
				}
			});
			mnFilterBy.add(mntmDirection);

			mntmMessageRef = new JMenuItem4j("Message Ref");
			mntmMessageRef.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					filterBy("Message Ref");
				}
			});
			mnFilterBy.add(mntmMessageRef);

			mntmMessageInfo = new JMenuItem4j("Message Info");
			mntmMessageInfo.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					filterBy("Message Info");
				}
			});
			mnFilterBy.add(mntmMessageInfo);

			mntmStatus = new JMenuItem4j("Status");
			mntmStatus.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					filterBy("Status");
				}
			});
			mnFilterBy.add(mntmStatus);

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
			jButtonClose.setBounds(843, 136, 139, 32);

			jSpinnerIntModel.setValue(1000);
			jSpinnerIntModel.setMinimum(1000);
			jSpinnerIntModel.setMaximum(5000);
			jSpinnerIntModel.setStepSize(100);
			spinner.setModel(jSpinnerIntModel);
			JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(spinner);
			spinner.setEditor(ne);
			spinner.setBounds(896, 104, 80, 22);
			jDesktopPane1.add(spinner);

			JLabel4j_std lblErrorMessage = new JLabel4j_std(lang.get("lbl_Message_Error"));
			lblErrorMessage.setBounds(9, 512, 96, 22);
			lblErrorMessage.setHorizontalAlignment(SwingConstants.TRAILING);
			jDesktopPane1.add(lblErrorMessage);

			errorMessage = new JTextField4j();
			errorMessage.setEditable(false);
			errorMessage.setBounds(110, 512, 860, 22);
			jDesktopPane1.add(errorMessage);
			errorMessage.setColumns(10);

			JLabel4j_std lblRowsToDisplay = new JLabel4j_std(lang.get("lbl_Limit"));
			lblRowsToDisplay.setHorizontalAlignment(SwingConstants.TRAILING);
			lblRowsToDisplay.setBounds(733, 104, 127, 22);
			jDesktopPane1.add(lblRowsToDisplay);

			lblMessageDate = new JLabel4j_std(lang.get("lbl_Message_Date"));
			lblMessageDate.setHorizontalAlignment(SwingConstants.TRAILING);
			lblMessageDate.setBounds(19, 480, 84, 22);
			jDesktopPane1.add(lblMessageDate);

			messageDate = new JTextField4j();
			messageDate.setEditable(false);
			messageDate.setColumns(10);
			messageDate.setBounds(110, 480, 146, 22);
			jDesktopPane1.add(messageDate);

			workstation = new JTextField4j();
			workstation.setEditable(false);
			workstation.setColumns(10);
			workstation.setBounds(368, 480, 376, 22);
			jDesktopPane1.add(workstation);

			JLabel4j_std lblWorkstation = new JLabel4j_std(lang.get("lbl_Workstation"));
			lblWorkstation.setHorizontalAlignment(SwingConstants.TRAILING);
			lblWorkstation.setBounds(280, 480, 81, 22);
			jDesktopPane1.add(lblWorkstation);

			comboBoxInterfaceType.setMaximumRowCount(20);

			comboBoxInterfaceType.setModel(new DefaultComboBoxModel<String>(Common.messageTypesincBlank));
			comboBoxInterfaceType.setBounds(766, 8, 210, 22);
			jDesktopPane1.add(comboBoxInterfaceType);

			comboBoxDirection.setModel(new DefaultComboBoxModel<String>(new String[]
			{ "", "Input", "Output" }));
			comboBoxDirection.setBounds(130, 8, 136, 22);
			jDesktopPane1.add(comboBoxDirection);

			comboBoxStatus.setModel(new DefaultComboBoxModel<String>(new String[]
			{ "", "Error", "Success", "Warning" }));
			comboBoxStatus.setBounds(492, 8, 113, 22);
			jDesktopPane1.add(comboBoxStatus);

			comboBoxAction.setModel(new DefaultComboBoxModel<String>(new String[]
			{ "", "DB Update", "File Write" }));
			comboBoxAction.setBounds(130, 72, 136, 22);
			jDesktopPane1.add(comboBoxAction);

			JLabel4j_std lblMessageType = new JLabel4j_std(lang.get("lbl_Message_Type"));
			lblMessageType.setHorizontalAlignment(SwingConstants.TRAILING);
			lblMessageType.setBounds(588, 8, 172, 22);
			jDesktopPane1.add(lblMessageType);

			JLabel4j_std lblDirection = new JLabel4j_std(lang.get("lbl_Interface_Direction"));
			lblDirection.setHorizontalAlignment(SwingConstants.TRAILING);
			lblDirection.setBounds(5, 8, 115, 22);
			jDesktopPane1.add(lblDirection);

			JLabel4j_std lblStatus = new JLabel4j_std(lang.get("lbl_Message_Status"));
			lblStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			lblStatus.setBounds(320, 8, 163, 22);
			jDesktopPane1.add(lblStatus);

			textFieldMessageRef = new JTextField4j(JDBInterfaceLog.field_message_ref);
			textFieldMessageRef.setBounds(130, 40, 213, 22);
			jDesktopPane1.add(textFieldMessageRef);
			textFieldMessageRef.setColumns(10);

			textFieldMessageInfo = new JTextField4j(JDBInterfaceLog.field_message_information);
			textFieldMessageInfo.setColumns(10);
			textFieldMessageInfo.setBounds(492, 40, 484, 22);
			jDesktopPane1.add(textFieldMessageInfo);

			JLabel4j_std lblMessageRef = new JLabel4j_std(lang.get("lbl_Message_Reference"));
			lblMessageRef.setHorizontalAlignment(SwingConstants.TRAILING);
			lblMessageRef.setBounds(5, 40, 115, 22);
			jDesktopPane1.add(lblMessageRef);

			JLabel4j_std lblMessageInfo = new JLabel4j_std(lang.get("lbl_Message_Information"));
			lblMessageInfo.setHorizontalAlignment(SwingConstants.TRAILING);
			lblMessageInfo.setBounds(356, 40, 127, 22);
			jDesktopPane1.add(lblMessageInfo);

			JLabel4j_std lblEventDate = new JLabel4j_std();
			lblEventDate.setText(lang.get("lbl_Message_Event_Date"));
			lblEventDate.setHorizontalAlignment(SwingConstants.TRAILING);
			lblEventDate.setBounds(5, 104, 115, 22);
			jDesktopPane1.add(lblEventDate);

			dateControlfrom.setBounds(156, 104, 120, 22);
			jDesktopPane1.add(dateControlfrom);

			dateControlTo.setEnabled(false);
			dateControlTo.setBounds(318, 104, 120, 22);
			jDesktopPane1.add(dateControlTo);

			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JDBQuery.closeStatement(listStatement);
					dispose();
				}
			});

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

			checkBoxEventDate.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
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

			checkBoxEventDate.setBounds(130, 104, 21, 22);
			jDesktopPane1.add(checkBoxEventDate);

			checkBoxLimit.setSelected(true);

			checkBoxLimit.setBounds(868, 104, 21, 22);
			jDesktopPane1.add(checkBoxLimit);

			JLabel4j_std label = new JLabel4j_std(lang.get("lbl_Message_Error"));
			label.setHorizontalAlignment(SwingConstants.TRAILING);
			label.setBounds(356, 72, 127, 22);
			jDesktopPane1.add(label);

			textFielderrorMessage = new JTextField4j(JDBInterfaceLog.field_message_error);
			textFielderrorMessage.setColumns(10);
			textFielderrorMessage.setBounds(492, 72, 484, 22);
			jDesktopPane1.add(textFielderrorMessage);

			JButton4j button = new JButton4j(Common.icon_XLS_16x16);

			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					excel();
				}
			});
			button.setText(lang.get("btn_Excel"));
			button.setMnemonic(lang.getMnemonicChar());
			button.setBounds(423, 136, 139, 32);
			jDesktopPane1.add(button);

			button_1 = new JButton4j(Common.icon_clear_16x16);
			button_1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					clearFilter();
				}
			});
			button_1.setText(lang.get("btn_Clear_Filter"));
			button_1.setMnemonic(lang.getMnemonicChar());
			button_1.setBounds(143, 136, 139, 32);
			jDesktopPane1.add(button_1);

			jStatusBar.setBounds(5, 578, 980, 24);
			jDesktopPane1.add(jStatusBar);

			lblFilename = new JLabel4j_std(lang.get("lbl_Interface_Filename"));
			lblFilename.setHorizontalAlignment(SwingConstants.TRAILING);
			lblFilename.setBounds(9, 544, 96, 22);
			jDesktopPane1.add(lblFilename);

			textFieldFilename = new JTextField4j();
			textFieldFilename.setEditable(false);
			textFieldFilename.setColumns(10);
			textFieldFilename.setBounds(110, 544, 860, 22);
			jDesktopPane1.add(textFieldFilename);

			JButton4j btnResubmit = new JButton4j(Common.icon_release_16x16);

			btnResubmit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					reSubmit();
				}
			});
			btnResubmit.setText(lang.get("btn_Resubmit"));
			btnResubmit.setMnemonic(lang.getMnemonicChar());
			btnResubmit.setBounds(283, 136, 139, 32);
			jDesktopPane1.add(btnResubmit);

			JButton4j btnDelete = new JButton4j(Common.icon_delete_16x16);
			btnDelete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					delete();
				}
			});
			btnDelete.setToolTipText("Export all rows below to spreadsheet.");
			btnDelete.setText(lang.get("btn_Delete"));
			btnDelete.setMnemonic(lang.getMnemonicChar());
			btnDelete.setBounds(563, 136, 139, 32);
			jDesktopPane1.add(btnDelete);

			JButton4j btnArchiveResolved = new JButton4j(Common.icon_delete_16x16);
			btnArchiveResolved.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					archive();
				}
			});
			btnArchiveResolved.setToolTipText("Export all rows below to spreadsheet.");
			btnArchiveResolved.setText(lang.get("btn_Archive"));
			btnArchiveResolved.setMnemonic(lang.getMnemonicChar());
			btnArchiveResolved.setBounds(703, 136, 139, 32);
			jDesktopPane1.add(btnArchiveResolved);

			calendarButtonFrom = new JCalendarButton(dateControlfrom);
			calendarButtonFrom.setEnabled(false);
			calendarButtonFrom.setBounds(278, 104, 21, 22);
			jDesktopPane1.add(calendarButtonFrom);

			calendarButtonTo = new JCalendarButton(dateControlTo);
			calendarButtonTo.setEnabled(false);
			calendarButtonTo.setBounds(440, 104, 21, 22);
			jDesktopPane1.add(calendarButtonTo);

			JLabel4j_std label4j_std = new JLabel4j_std(lang.get("lbl_Message_Action"));
			label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
			label4j_std.setBounds(5, 72, 115, 22);
			jDesktopPane1.add(label4j_std);

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					textFieldMessageRef.requestFocus();
					textFieldMessageRef.setCaretPosition(textFieldMessageRef.getText().length());
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void addPopup(Component component, final JPopupMenu popup)
	{
		component.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e)
			{
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
