package com.commander4j.gui;

import javax.swing.JComboBox;

import com.commander4j.sys.Common;

public class JComboBox4j<T> extends JComboBox<T> {

	private static final long serialVersionUID = 1L;

	public JComboBox4j() {
		super();
		setFont(Common.font_combo);
	}

	public JComboBox4j(String[] fieldAlignment)
	{
		super();
		setFont(Common.font_combo);
	}


}
