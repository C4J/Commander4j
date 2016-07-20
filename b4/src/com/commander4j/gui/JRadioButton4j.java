package com.commander4j.gui;

import javax.swing.JRadioButton;

import com.commander4j.sys.Common;

public class JRadioButton4j extends JRadioButton
{

	private static final long serialVersionUID = 1L;

	public JRadioButton4j(String arg0) {
		super(arg0);
		setFont(Common.font_std);
		setBackground(Common.color_app_window);
	}

}
