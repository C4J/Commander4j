package com.commander4j.gui;

import java.awt.Color;

import javax.swing.JDesktopPane;

import com.commander4j.sys.Common;

public class JDesktopPane4j extends JDesktopPane {

	private static final long serialVersionUID = 1L;
	
	public JDesktopPane4j()
	{
		super();
		setBackground(Common.color_app_window);
	}
	
	public JDesktopPane4j(Color background)
	{
		super();
		setBackground(background);
	}



}
