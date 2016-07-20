package com.commander4j.gui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import com.commander4j.sys.Common;

public class JButton4j extends JButton {

	private static final long serialVersionUID = 1L;

	public JButton4j() {
		setFont(Common.font_btn);
	}

	public JButton4j(Icon icon) {
		super(icon);
		setFont(Common.font_btn);
	}

	public JButton4j(String text) {
		super(text);
		setToolTipText(text);
		setFont(Common.font_btn);
	}

	public JButton4j(Action a) {
		super(a);
		setFont(Common.font_btn);
	}

	public JButton4j(String text, Icon icon) {
		super(text, icon);
		setToolTipText(text);
		setFont(Common.font_btn);
	}
	
	@Override
	public void setText(String text)
	{
		super.setText(text);
		setToolTipText(text);
	}

}
