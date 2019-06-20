package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameUserReportAdmin.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBShifts;
import com.commander4j.db.JDBUserReport;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameUserReportAdmin class allows a user manage a table called
 * SYS_USER_REPORTS which contains user defined reports.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameUserReportAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBUserReport JDBUserReport
 * @see com.commander4j.app.JInternalFrameUserReportProperties
 *      JInternalFrameUserReportProperties
 * @see com.commander4j.app.JDialogShiftProperties
 *      JDialogShiftProperties
 */
public class JInternalFrameUserReportAdmin extends JInternalFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList4j<JDBListData> jListUserReports = new JList4j<JDBListData>();
	private JList4j<JDBShifts> jListShifts = new JList4j<JDBShifts>();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDateControl domDateFrom;
	private JDateControl domDateTo;
	private JCalendarButton button_CalendardomDateFrom;
	private JCalendarButton button_CalendardomDateTo;
	private JPanel panelDateParams = new JPanel();
	private JButton4j button4j_Backwards = new JButton4j(Common.icon_arrow_left);
	private JButton4j button4j_Forwards = new JButton4j(Common.icon_arrow_right);
	private JLabel4j_std label4j_statusBar = new JLabel4j_std();
	private JButton4j btn4jAdd = new JButton4j(Common.icon_add);
	private JButton4j btn4jEdit = new JButton4j(Common.icon_edit);
	private JButton4j btn4jDelete = new JButton4j(Common.icon_delete);
	private JButton4j btn4jCopy = new JButton4j(Common.icon_copy);
	private JButton4j btn4jRefresh = new JButton4j(Common.icon_refresh_16x16);
	private JButton4j btn4jRun = new JButton4j(Common.icon_execute);
	private JButton4j btn4jHelp = new JButton4j(Common.icon_help_16x16);
	private JButton4j btn4jClose = new JButton4j(Common.icon_close_16x16);

	/**
	 * Create the frame.
	 */
	public JInternalFrameUserReportAdmin()
	{
		setVisible(true);
		this.setClosable(true);
		this.setIconifiable(true);
		setBounds(100, 100, 795, 568);
		getContentPane().setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 806, 565);
		desktopPane.setBackground(Common.color_app_window);
		getContentPane().add(desktopPane);

		JScrollPane scrollPaneReports = new JScrollPane();
		scrollPaneReports.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		scrollPaneReports.setBounds(12, 30, 569, 253);
		desktopPane.add(scrollPaneReports);
		jListUserReports.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent arg0)
			{
				if (arg0.getValueIsAdjusting() == false)
				{
					setButtonState();
				}
			}
		});
		jListUserReports.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				if (arg0.getClickCount() == 2)
				{
					editReport();
				}
			}
		});
		jListUserReports.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPaneReports.setViewportView(jListUserReports);

		JScrollPane scrollPaneShifts = new JScrollPane();
		scrollPaneShifts.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		scrollPaneShifts.setBounds(12, 312, 569, 161);
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

						domDateFrom.setDate(startCal.getTime());
						domDateTo.setDate(endCal.getTime());
					}
				}
			}
		});

		jListShifts.setSelectedIndex(-1);
		scrollPaneShifts.setViewportView(jListShifts);

		JLabel4j_std lblCriteria = new JLabel4j_std("Criteria - Quick Set");
		lblCriteria.setText(lang.get("mod_FRM_ADMIN_SHIFT"));
		lblCriteria.setBounds(12, 295, 126, 15);
		desktopPane.add(lblCriteria);

		JLabel4j_std lblReport = new JLabel4j_std(lang.get("mod_FRM_ADMIN_USER_REPORT"));
		lblReport.setBounds(12, 12, 126, 15);
		desktopPane.add(lblReport);

		label4j_statusBar.setForeground(Color.RED);
		label4j_statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		label4j_statusBar.setBounds(0, 514, 782, 21);
		desktopPane.add(label4j_statusBar);
		panelDateParams.setBackground(Common.color_app_window);

		panelDateParams.setBounds(604, 312, 165, 161);
		panelDateParams.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		desktopPane.add(panelDateParams);
		panelDateParams.setLayout(null);

		JLabel4j_std lblStart = new JLabel4j_std(lang.get("web_From"));
		lblStart.setBounds(11, 12, 145, 15);
		panelDateParams.add(lblStart);

		domDateFrom = new JDateControl();
		domDateFrom.setBounds(6, 27, 128, 25);
		panelDateParams.add(domDateFrom);
		domDateFrom.setFont(Common.font_std);

		button_CalendardomDateFrom = new JCalendarButton(domDateFrom);
		button_CalendardomDateFrom.setSize(21, 21);
		button_CalendardomDateFrom.setLocation(510, 314);
		button_CalendardomDateFrom.setBounds(135, 31, 21, 21);
		panelDateParams.add(button_CalendardomDateFrom);

		JLabel4j_std label4jEnd = new JLabel4j_std(lang.get("web_To"));
		label4jEnd.setBounds(11, 67, 149, 15);
		panelDateParams.add(label4jEnd);

		domDateTo = new JDateControl();
		domDateTo.setBounds(6, 82, 128, 25);
		panelDateParams.add(domDateTo);
		domDateTo.setFont(Common.font_std);

		button_CalendardomDateTo = new JCalendarButton(domDateTo);
		button_CalendardomDateTo.setSize(21, 21);
		button_CalendardomDateTo.setLocation(510, 369);
		button_CalendardomDateTo.setBounds(135, 86, 21, 21);
		panelDateParams.add(button_CalendardomDateTo);

		button4j_Backwards.setBounds(36, 115, 25, 25);
		panelDateParams.add(button4j_Backwards);
		button4j_Backwards.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dateAdjust(domDateFrom, -1);
				dateAdjust(domDateTo, -1);
			}
		});
		button4j_Backwards.setEnabled(true);

		button4j_Forwards.setBounds(73, 115, 25, 25);
		panelDateParams.add(button4j_Forwards);
		button4j_Forwards.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dateAdjust(domDateFrom, 1);
				dateAdjust(domDateTo, 1);
			}
		});
		button4j_Forwards.setEnabled(true);

		JPanel panelButtons = new JPanel();
		panelButtons.setBackground(Common.color_app_window);
		panelButtons.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panelButtons.setBounds(604, 30, 165, 253);
		desktopPane.add(panelButtons);
		panelButtons.setLayout(null);

		btn4jAdd.setBounds(20, 6, 126, 29);
		panelButtons.add(btn4jAdd);
		btn4jAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				addReport();
			}
		});
		btn4jAdd.setText(lang.get("btn_Add"));
		btn4jAdd.setMnemonic('0');
		btn4jEdit.setEnabled(false);

		btn4jEdit.setBounds(20, 36, 126, 29);
		panelButtons.add(btn4jEdit);
		btn4jEdit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				editReport();
			}
		});
		btn4jEdit.setText(lang.get("btn_Edit"));
		btn4jEdit.setMnemonic('0');
		btn4jEdit.setFocusTraversalKeysEnabled(false);

		btn4jDelete.setBounds(20, 66, 126, 29);
		panelButtons.add(btn4jDelete);
		btn4jDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				deleteReport();
			}
		});
		btn4jDelete.setText(lang.get("btn_Delete"));
		btn4jDelete.setMnemonic('0');

		btn4jCopy.setBounds(20, 96, 126, 29);
		panelButtons.add(btn4jCopy);
		btn4jCopy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				copyReport();
			}
		});
		btn4jCopy.setText(lang.get("btn_Copy"));
		btn4jCopy.setMnemonic('0');

		btn4jRefresh.setBounds(20, 126, 126, 29);
		panelButtons.add(btn4jRefresh);
		btn4jRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (jListUserReports.isSelectionEmpty() == false)
				{
					JDBUserReport reportid = (JDBUserReport) ((JDBListData) jListUserReports.getSelectedValue()).getObject();
					populateListUserReports(reportid.getReportID());
				} else
				{
					populateListUserReports("");
				}

			}
		});
		btn4jRefresh.setText(lang.get("btn_Refresh"));
		btn4jRefresh.setMnemonic('0');

		btn4jRun.setBounds(20, 156, 126, 29);
		panelButtons.add(btn4jRun);
		btn4jRun.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (jListUserReports.isSelectionEmpty() == false)
				{
					JDBUserReport reportid = (JDBUserReport) ((JDBListData) jListUserReports.getSelectedValue()).getObject();
					reportid.getUserReportProperties();
					
					if (reportid.getDestination().equals("SYSTEM")==false)
					{
					if (reportid.isParamDateRequired())
					{
						reportid.setParamFromDate(JUtility.getTimestampFromDate(domDateFrom.getDate()));
						reportid.setParamToDate(JUtility.getTimestampFromDate(domDateTo.getDate()));
					}
					label4j_statusBar.setText("Please wait....");
					if (reportid.runReport() == false)
					{
						JUtility.errorBeep();
						label4j_statusBar.setText(reportid.getErrorMessage());
						JOptionPane.showMessageDialog(Common.mainForm, reportid.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
					} else
					{
						label4j_statusBar.setText("Created : " + reportid.getExportFilename());
					}
					}
					else
					{
						JUtility.errorBeep();
						label4j_statusBar.setText("");
						JOptionPane.showMessageDialog(Common.mainForm, "SYSTEM reports cannot be run interactively.", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);

					}
				}
			}
		});
		btn4jRun.setText(lang.get("btn_Run"));
		btn4jRun.setMnemonic('0');

		btn4jHelp.setBounds(20, 186, 126, 29);
		panelButtons.add(btn4jHelp);
		btn4jHelp.setText(lang.get("btn_Help"));
		btn4jHelp.setMnemonic('0');

		btn4jClose.setBounds(20, 216, 126, 29);
		panelButtons.add(btn4jClose);
		btn4jClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				dispose();
			}
		});
		btn4jClose.setText(lang.get("btn_Close"));
		btn4jClose.setMnemonic('0');

		JPanel panelShiftButtons = new JPanel();
		panelShiftButtons.setBackground(Common.color_app_window);
		panelShiftButtons.setBounds(12, 475, 120, 32);
		desktopPane.add(panelShiftButtons);
		panelShiftButtons.setLayout(null);

		JButton4j button4jShiftAdd = new JButton4j(Common.icon_add);
		button4jShiftAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				addShift();
			}
		});
		button4jShiftAdd.setBounds(0, 0, 28, 28);
		panelShiftButtons.add(button4jShiftAdd);
		button4jShiftAdd.setFont(Common.font_std);
		button4jShiftAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_SHIFT_ADD"));
		button4jShiftAdd.setMnemonic('0');

		JButton4j button4jShiftDelete = new JButton4j(Common.icon_delete);
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
		button4jShiftDelete.setFont(Common.font_std);

		JButton4j button4jShiftEdit = new JButton4j(Common.icon_edit);
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
		button4jShiftEdit.setFont(Common.font_std);

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
		button4jShiftRefresh.setFont(Common.font_std);

		populateListUserReports("");
		populateListUserShifts("");
		setButtonState();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(btn4jHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_USER_REPORT"));
	}

	private void dateAdjust(JDateControl datecontrol, Integer adjustment)
	{
		Date dateNow = datecontrol.getDate();
		Calendar calendarNow = GregorianCalendar.getInstance();

		calendarNow.setTime(dateNow);

		calendarNow.add(Calendar.DATE, adjustment);

		datecontrol.setDate(calendarNow.getTime());

	}

	private void copyReport()
	{
		if (jListUserReports.isSelectionEmpty() == false)
		{
			JDBUserReport reportid = (JDBUserReport) ((JDBListData) jListUserReports.getSelectedValue()).getObject();
			String newid = JOptionPane.showInputDialog(Common.mainForm, "Copy report [" + reportid.getReportID() + "] to NEW Report ID");
			if (newid != null)
			{
				if (newid.equals("") == false)
				{
					newid = newid.toUpperCase();
					reportid.getUserReportProperties();
					reportid.setReportID(newid);
					reportid.create(newid);
					reportid.update();
					populateListUserReports(newid);
				}
			}
		}
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

	private void editReport()
	{
		if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_REPORT_EDIT"))
		{
			if (jListUserReports.isSelectionEmpty() == false)
			{
				JDBUserReport reportid = (JDBUserReport) ((JDBListData) jListUserReports.getSelectedValue()).getObject();
				JLaunchMenu.runForm("FRM_ADMIN_USER_REPORT_PROP", reportid.getReportID());
			}
		}
	}

	private void deleteReport()
	{
		if (jListUserReports.isSelectionEmpty() == false)
		{
			JDBUserReport reportid = (JDBUserReport) ((JDBListData) jListUserReports.getSelectedValue()).getObject();
			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_User_Report_Delete") + " " + reportid.getReportID() + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (n == 0)
			{

				reportid.delete();
				populateListUserReports("");
			}
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
						Common.icon_confirm);
				if (n == 0)
				{

					shiftid.delete();
					populateListUserShifts("");
				}
			}
		}
	}

	private void addReport()
	{

		JDBUserReport rpt = new JDBUserReport(Common.selectedHostID, Common.sessionID);

		String reportId = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_User_Report_Create"));
		if (reportId != null)
		{
			if (reportId.equals("") == false)
			{
				reportId = reportId.toUpperCase();
				rpt.setReportID(reportId);
				if (rpt.create(reportId) == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, rpt.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
				} else
				{

					JLaunchMenu.runForm("FRM_ADMIN_USER_REPORT_PROP", reportId);
				}
				populateListUserReports(reportId);
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
					JOptionPane.showMessageDialog(Common.mainForm, shift.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
				} else
				{

					JLaunchMenu.runDialog("FRM_ADMIN_SHIFT", shiftId);
				}
				populateListUserShifts(shiftId);
			}
		}

	}

	private void setButtonState()
	{
		if (jListUserReports.getSelectedIndex() > -1)
		{
			JDBUserReport reportid = (JDBUserReport) ((JDBListData) jListUserReports.getSelectedValue()).getObject();

			domDateFrom.setEnabled(reportid.isParamDateRequired());
			domDateTo.setEnabled(reportid.isParamDateRequired());
			button_CalendardomDateFrom.setEnabled(reportid.isParamDateRequired());
			button_CalendardomDateTo.setEnabled(reportid.isParamDateRequired());
			jListShifts.setEnabled(reportid.isParamDateRequired());
			button4j_Backwards.setEnabled(reportid.isParamDateRequired());
			button4j_Forwards.setEnabled(reportid.isParamDateRequired());

			btn4jAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_REPORT_ADD"));
			btn4jEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_REPORT_EDIT"));
			btn4jDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_REPORT_DELETE"));
			btn4jRun.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_REPORT_RUN"));
			btn4jCopy.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_REPORT_COPY"));
		} else
		{
			btn4jAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_USER_REPORT_ADD"));
			btn4jEdit.setEnabled(false);
			btn4jDelete.setEnabled(false);
			btn4jRun.setEnabled(false);
			btn4jCopy.setEnabled(false);
		}
	}

	private void populateListUserReports(String defaultitem)
	{
		DefaultComboBoxModel<JDBListData> defComboBoxMod = new DefaultComboBoxModel<JDBListData>();

		JDBUserReport tempUserReport = new JDBUserReport(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBListData> tempUserReportList = new LinkedList<JDBListData>();

		tempUserReportList = tempUserReport.getUserReportIds();

		int sel = -1;
		for (int j = 0; j < tempUserReportList.size(); j++)
		{
			defComboBoxMod.addElement(tempUserReportList.get(j));
			if (((JDBUserReport) tempUserReportList.get(j).getmData()).getReportID().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBListData> jList1Model = defComboBoxMod;

		jListUserReports.setModel(jList1Model);
		jListUserReports.setSelectedIndex(sel);

		jListUserReports.setCellRenderer(Common.renderer_list);

		if (jListUserReports.isSelectionEmpty())
		{
			if (jListUserReports.getModel().getSize() > 0)
				jListUserReports.setSelectedIndex(0);
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
}
