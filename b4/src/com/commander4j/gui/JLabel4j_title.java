package com.commander4j.gui;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.commander4j.sys.Common;

public class JLabel4j_title extends JLabel {

	private static final long serialVersionUID = 1L;

	public JLabel4j_title() {
		setFont(Common.font_title);
	}

	public JLabel4j_title(String arg0) {
		super(arg0);
		setFont(Common.font_title);
	}

	public JLabel4j_title(Icon arg0) {
		super(arg0);
		setFont(Common.font_title);
	}

	public JLabel4j_title(String arg0, int arg1) {
		super(arg0, arg1);
		setFont(Common.font_title);
	}

	public JLabel4j_title(Icon arg0, int arg1) {
		super(arg0, arg1);
		setFont(Common.font_title);
	}

	public JLabel4j_title(String arg0, Icon arg1, int arg2) {
		super(arg0, arg1, arg2);
		setFont(Common.font_title);
	}

}
