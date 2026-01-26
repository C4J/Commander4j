package com.commander4j.renderer;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JDatabaseParameters.java
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
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.commander4j.sys.Common;

public class QMIndexRenderer implements TableCellRenderer
{
	private TableCellRenderer delegate;

	public QMIndexRenderer(TableCellRenderer defaultRenderer)
	{
		this.delegate = defaultRenderer;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		Color foreground, background;

		if (isSelected)
		{
			foreground = Common.color_list_foreground_selected;
			background = Common.color_list_background_selected;
		}
		else
		{
			if (row % 2 == 0)
			{
				foreground = Common.color_table_alternate_foreground1;
				background = Common.color_table_alternate_row1;
			}
			else
			{
				foreground = Common.color_list_foreground;
				background = Common.color_table_standard_row2;
			}
		}

		String s = "";

		if (value == null)
		{
			s="";
		}
		else
		{
			if (value.getClass().equals(Integer.class))
			{
				s = String.valueOf(value);
			}

			if (value.getClass().equals(Timestamp.class))
			{
				s = ((Timestamp) value).toString().substring(0, 16);
			}

			if (value.getClass().equals(String.class))
			{
				s = value.toString();
			}

		}

		if (c instanceof JLabel)
		{
			((JLabel) c).setText(s);
			((JLabel) c).setHorizontalAlignment(JLabel.LEFT);

			((JLabel) c).setForeground(foreground);
			((JLabel) c).setBackground(background);
		}

		return c;
	}
}