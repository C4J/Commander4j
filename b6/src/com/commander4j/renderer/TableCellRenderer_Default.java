package com.commander4j.renderer;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : TableCellRenderer_Default.java
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

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 */
public class TableCellRenderer_Default extends DefaultTableCellRenderer
{

	private static final long serialVersionUID = 1;
	private JCheckBox checkbox;
	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		setFont(Common.font_table);
		Color foreground, background;

		if (isSelected)
		{
			foreground = Common.color_listFontSelected;
			background = Common.color_listHighlighted;
		}
		else
		{
			if (row % 2 == 0)
			{
				foreground = Common.color_listFontStandard;
				background = Common.color_tablerow1;
			}
			else
			{
				foreground = Common.color_listFontStandard;
				background = Common.color_tablerow2;
			}
		}

		setForeground(foreground);
		setBackground(background);

		setHorizontalAlignment(JLabel.LEFT);
		
		try
		{
			if (value.getClass().equals(Boolean.class))
			{
				checkbox = new JCheckBox();
				checkbox.setSelected((Boolean) value);
				checkbox.setText("");
				checkbox.setFont(Common.font_table);
				checkbox.setSize(getWidth(), getHeight());
				checkbox.setForeground(foreground);
				checkbox.setBackground(background);
				checkbox.setFocusable(false);
				checkbox.setHorizontalAlignment(JCheckBox.CENTER);

				return checkbox;
			}

		}
		catch (Exception ex)
		{

		}

		try
		{
			if (value.getClass().equals(BigDecimal.class))
			{
				setHorizontalAlignment(JLabel.RIGHT);
				this.setText(JUtility.bigDecimaltoString((BigDecimal) value));
			}
		}
		catch (Exception ex)
		{

		}
		
		try
		{
			if (value.getClass().equals(Integer.class))
			{
				setHorizontalAlignment(JLabel.RIGHT);
				this.setText(String.valueOf(value));
			}
		}
		catch (Exception ex)
		{

		}

		try
		{
			if (value.getClass().equals(Long.class))
			{
				setHorizontalAlignment(JLabel.RIGHT);
				this.setText(String.valueOf(value));
			}
		}
		catch (Exception ex)
		{

		}
		
		return this;
	}
}