package com.commander4j.util;

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
