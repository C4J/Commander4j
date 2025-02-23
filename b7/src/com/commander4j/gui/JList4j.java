package com.commander4j.gui;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JList4j.java
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

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;

import com.commander4j.sys.Common;


public class JList4j<E> extends JList<E>
{

	private static final long serialVersionUID = 1L;

	public JList4j(ListModel<E> items)
	{
		super(items);
		setFont(Common.font_list);
		setBackground(Common.color_listBackground);
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	public JList4j(E[] items)
	{
		super(items);
		setFont(Common.font_list);
		setBackground(Common.color_listBackground);
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	public JList4j()
	{
		super();
		setFont(Common.font_list);
		setBackground(Common.color_listBackground);
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}
	
		
}
