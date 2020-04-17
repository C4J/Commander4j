package com.commander4j.util;

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

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import com.commander4j.sys.Common;

public class JDateControl extends JSpinner
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SpinnerDateModel sdm;

	public JDateControl()
	{
		super();
		Date today = new Date();
		sdm = new SpinnerDateModel(today, null, null, Calendar.MONTH);
		setModel(sdm);
		DateEditor de = new JSpinner.DateEditor(this, "dd/MM/yyyy HH:mm:ss");
		de.getTextField().setFont(Common.font_dates);
		setEditor(de);
	}

	public void setBounds(int x, int y, int width, int height) {
		width = 128;
		height = 25;
		super.setBounds(x, y, width, height);
	}

	public Date getDate() {
		return sdm.getDate();
	}

	public void setDate(Date date) {
		sdm.setValue(date);
	}

}
