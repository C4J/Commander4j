package com.commander4j.gui;

import java.awt.Insets;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JRadioButton4j.java
 * 
 * Package Name : com.commander4j.gui
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import javax.swing.JRadioButton;

import com.commander4j.sys.Common;

public class JRadioButton4j extends JRadioButton
{

	private static final long serialVersionUID = 1L;

	public void init()
	{
		setFont(Common.font_std);
        setMargin(new Insets(0, 0, 0, 0));  // Reduce padding
        setOpaque(false);
		setBackground(Common.color_app_window);
        	
	}
	
	public JRadioButton4j() {
		super();
		init();
	}
	
	public JRadioButton4j(String arg0) {
		super(arg0);
		init();
	}
	

}
