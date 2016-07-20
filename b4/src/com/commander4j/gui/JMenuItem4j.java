package com.commander4j.gui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

import com.commander4j.sys.Common;

public class JMenuItem4j extends JMenuItem {

	private static final long serialVersionUID = 1L;

	public JMenuItem4j() {
		setFont(Common.font_popup);
	}

	public JMenuItem4j(Icon icon) {
		super(icon);
		setFont(Common.font_popup);
	}

	public JMenuItem4j(String text) {
		super(text);
		setFont(Common.font_popup);
	}

	public JMenuItem4j(Action a) {
		super(a);
		setFont(Common.font_popup);
	}

	public JMenuItem4j(String text, Icon icon) {
		super(text, icon);
		setFont(Common.font_popup);
	}

	public JMenuItem4j(String text, int mnemonic) {
		super(text, mnemonic);
		setFont(Common.font_popup);
	}

}
