package com.commander4j.calendar;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

import com.commander4j.sys.Common;
import com.commander4j.util.JDateControl;

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
		setIcon(Common.icon_calendar);
		callingControl = datetimecontrol;
		
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCalendarDialog dtc = new JCalendarDialog(callingControl);
				dtc.setVisible(true);
			}
		});
	}
}
