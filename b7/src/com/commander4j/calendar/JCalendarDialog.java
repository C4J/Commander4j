package com.commander4j.calendar;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JCalendarDialog.java
 * 
 * Package Name : com.commander4j.calendar
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import com.commander4j.sys.Common;
import com.commander4j.util.JDateControl;
/**
 * The JCalendarDialog is used to provide a popup window showing a calendar control.
 * <p>
 * <img alt="" src="./doc-files/JCalendarDialog.jpg" >
 * 
 * @see com.commander4j.calendar.JCalendarPanel JCalendarPanel
 * @see com.commander4j.calendar.JCalendarButton JCalendarButton
 */
public class JCalendarDialog extends JDialog
{

	private static final long serialVersionUID = 1L;
	private Calendar selectedDate = Calendar.getInstance();
	private Calendar backupDate;
	protected Color background;
	protected Color foreground;
	protected Color selectedBackground;
	protected Color selectedForeground;
	private JDateControl callingControl;
	private JCalendarPanel calendarPanel; 

	public Calendar getCalendarDate()
	{
		return calendarPanel.getCalendarDate();
	}
	
	public Date getDate()
	{
		return calendarPanel.getDate();
	}
	
	public JCalendarDialog(JDateControl datetimecontrol)
	{
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
			}
			public void windowLostFocus(WindowEvent e) {
				dispose();
			}
		});
	
		callingControl = datetimecontrol;
		selectedDate.setTime(callingControl.getDate());

		init(selectedDate);
	}
	
	public void init(Calendar caldate) {

		setSize(268, 258);
		setUndecorated(true);
		setResizable(false);
		setModal(false);
		setLocation(callingControl.getLocationOnScreen().x,callingControl.getLocationOnScreen().y+callingControl.getSize().height);
		getContentPane().setLayout(null);
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(UIManager.getColor("PopupMenu.background"));
		desktopPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		desktopPane.setBounds(0, 0, 286, 268);
		getContentPane().add(desktopPane);
		desktopPane.setLayout(null);
		
		calendarPanel = new JCalendarPanel(caldate);
		calendarPanel.setBackground(UIManager.getColor("PopupMenu.background"));
		calendarPanel.setBounds(5, 5, 264, 218);
		desktopPane.add(calendarPanel);
		
		JButton buttonOK = new JButton(new ImageIcon("images\\OK.gif"));
		buttonOK.setBounds(36, 224, 90, 26);
		desktopPane.add(buttonOK);
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callingControl.setDate(calendarPanel.getDate());
				dispose();
			}
		});
		buttonOK.setText("OK");
		buttonOK.setFont(Common.font_std);
		
		JButton buttonCancel = new JButton(new ImageIcon("images\\Cancel.gif"));
		buttonCancel.setBounds(132, 224, 90, 26);
		desktopPane.add(buttonCancel);
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedDate = backupDate;
				dispose();
			}
		});
		buttonCancel.setText("Cancel");
		buttonCancel.setFont(Common.font_std);
	}
}
