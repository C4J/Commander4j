package com.commander4j.gui;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JLabel4j_std.java
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
