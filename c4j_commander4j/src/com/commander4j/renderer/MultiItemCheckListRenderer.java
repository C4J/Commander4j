package com.commander4j.renderer;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : MultiItemCheckListRenderer.java
 * 
 * Package Name : com.commander4j.renderer
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

import java.awt.Component;

import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JCheckListItem;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;


import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MultiItemCheckListRenderer extends JCheckBox4j implements ListCellRenderer<Object>
{

	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus)
	{
		setEnabled(list.isEnabled());
		setSelected(((JCheckListItem) value).isSelected());
		setFont(Common.font_list);
		setBackground(list.getBackground());
		setForeground(list.getForeground());
		setText(value.toString());
		return this;
	}
	
	public Component getListCellRendererComponent(JList4j<?> list, Object value, int index, boolean isSelected, boolean hasFocus)
	{
		setEnabled(list.isEnabled());
		setSelected(((JCheckListItem) value).isSelected());
		setFont(Common.font_list);
		setBackground(list.getBackground());
		setForeground(list.getForeground());
		setText(value.toString());
		return this;
	}
}