package com.commander4j.gui;

import javax.swing.JCheckBox;

import com.commander4j.sys.Common;

public class JCheckBox4j extends JCheckBox {

	private static final long serialVersionUID = 1L;
	
	public JCheckBox4j() {
		super();
		setFont(Common.font_combo);
		setOpaque(false);
		setBackground(Common.color_app_window);
	}
	
	public JCheckBox4j(String label) {
		super(label);
		setFont(Common.font_combo);
		setOpaque(false);
		setBackground(Common.color_app_window);
	}

}
