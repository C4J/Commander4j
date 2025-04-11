package com.commander4j.calendar;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JCalendarButton.java
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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

import com.commander4j.gui.JDateControl;
import com.commander4j.sys.Common;
/**
 * The JCalendarButton is used to provide a popup window showing a calendar control.
 * <p>
 * <img alt="" src="./doc-files/JCalendarDialog.jpg" >
 * 
 * @see com.commander4j.calendar.JCalendarPanel JCalendarPanel
 * @see com.commander4j.calendar.JCalendarDialog JCalendarDialog
 */
public class JCalendarButton extends JButton
{
	private static final long serialVersionUID = 1L;
	private Border emptyBdr  = BorderFactory.createEmptyBorder(10,10,10,10);
	private JDateControl callingControl;	

	public JCalendarButton(JDateControl datetimecontrol) {
		
		setSize(21,21);
		setBorder(emptyBdr);
		setMargin(new Insets(0,0,0,0));
		setContentAreaFilled(false);
		setFocusable(false);
		setIcon(Common.icon_calendar_16x16);
		callingControl = datetimecontrol;
		
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCalendarDialog dtc = new JCalendarDialog(callingControl);
				dtc.setVisible(true);
			}
		});
	}
}
