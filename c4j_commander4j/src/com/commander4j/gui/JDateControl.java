package com.commander4j.gui;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDateControl.java
 * 
 * Package Name : com.commander4j.util
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

import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField;

import javax.swing.SpinnerDateModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.commander4j.sys.Common;

public class JDateControl extends JSpinner4j
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private static final Border EMPTY_BORDER = new LineBorder(Color.GRAY);
	SpinnerDateModel datemodel;

	public JDateControl()
	{
		super();
		
		setBorder(EMPTY_BORDER);
		Date today = new Date();
		datemodel = new SpinnerDateModel(today, null, null, Calendar.MONTH);
		setModel(datemodel);
		DateEditor editor = new JSpinner4j.DateEditor(this, "dd/MM/yyyy HH:mm:ss");
		editor.getTextField().setFont(Common.font_dates);
		setEditor(editor);

        JFormattedTextField textField = editor.getTextField();

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
               textField.setBackground(Common.color_textfield_background_focus_color);
            }

            @Override
            public void focusLost(FocusEvent e) {
            	 textField.setBackground(Common.color_textfield_background_nofocus_color);
            }
        });
	}

	public void setBounds(int x, int y, int width, int height)
	{
		width = 128;
		height = 22;
		super.setBounds(x, y, width, height);
	}

	public Date getDate()
	{
		return datemodel.getDate();
	}

	public void setDate(Date date)
	{
		datemodel.setValue(date);
	}

}
