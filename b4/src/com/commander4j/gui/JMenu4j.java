package com.commander4j.gui;

import javax.swing.Action;
import javax.swing.JMenu;

import com.commander4j.sys.Common;

public class JMenu4j extends JMenu {

	private static final long serialVersionUID = 1L;

	public JMenu4j() {
		setFont(Common.font_popup);
	}

	public JMenu4j(String arg0) {
		super(arg0);
		setFont(Common.font_popup);
	}

	public JMenu4j(Action arg0) {
		super(arg0);
		setFont(Common.font_popup);
	}

	public JMenu4j(String arg0, boolean arg1) {
		super(arg0, arg1);
		setFont(Common.font_popup);
	}

}
