package com.commander4j.gui;

import java.awt.Color;

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
	private Color backgroundColor = Common.color_list_background;
	private Color foregroundColor = Common.color_list_foreground;

	public static final int Normal = 0;
	public static final int Assigned = 1;
	public static final int UnAssigned = 2;

	public void setMode(int mode)
	{

		if (mode == Normal)
		{
			setBackgroundColor(Common.color_list_background);
			setForegroundColor(Common.color_list_foreground);
		}

		if (mode == Assigned)
		{
			setBackgroundColor(Common.color_list_background_assigned);
			setForegroundColor(Common.color_list_foreground_assigned);
		}

		if (mode == UnAssigned)
		{
			setBackgroundColor(Common.color_list_background_unassigned);
			setForegroundColor(Common.color_list_foreground_unassigned);
		}

		 applyStyle();
	}

	private void setBackgroundColor(Color c)
	{
		backgroundColor = c;
	}

	private void setForegroundColor(Color c)
	{
		foregroundColor = c;
	}

	private void applyStyle()
	{
		setFont(Common.font_list);
		setBackground(backgroundColor);
		setForeground(foregroundColor);
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	public JList4j(ListModel<E> items)
	{
		super(items);
		applyStyle();
	}

	public JList4j(E[] items)
	{
		super(items);
		applyStyle();
	}

	public JList4j()
	{
		super();
		applyStyle();
	}

}
