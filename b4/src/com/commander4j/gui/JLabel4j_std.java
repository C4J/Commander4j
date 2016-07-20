package com.commander4j.gui;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.commander4j.sys.Common;

public class JLabel4j_std extends JLabel {

	private static final long serialVersionUID = 1L;

	public JLabel4j_std() {
		setFont(Common.font_std);
	}

	public JLabel4j_std(String arg0) {
		super(arg0);
		setFont(Common.font_std);
	}

	public JLabel4j_std(Icon arg0) {
		super(arg0);
		setFont(Common.font_std);
	}

	public JLabel4j_std(String arg0, int arg1) {
		super(arg0, arg1);
		setFont(Common.font_std);
	}

	public JLabel4j_std(Icon arg0, int arg1) {
		super(arg0, arg1);
		setFont(Common.font_std);
	}

	public JLabel4j_std(String arg0, Icon arg1, int arg2) {
		super(arg0, arg1, arg2);
		setFont(Common.font_std);
	}

}
