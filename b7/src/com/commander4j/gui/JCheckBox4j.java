package com.commander4j.gui;

import java.awt.Insets;

import javax.swing.JCheckBox;

import com.commander4j.sys.Common;

public class JCheckBox4j extends JCheckBox {

    private static final long serialVersionUID = 1L;

	public void init()
	{
		setFont(Common.font_std);
        setOpaque(false);
		setBackground(Common.color_app_window);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setMargin(new Insets(0, 0, 0, 0));  // Reduce padding

	}
	
	public JCheckBox4j() 
	{
		super();
		init();
	}
	
    public JCheckBox4j(String text) {
        super(text);
    	init();
    }
}
