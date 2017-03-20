package com.commander4j.gui;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JButton4j.java
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
