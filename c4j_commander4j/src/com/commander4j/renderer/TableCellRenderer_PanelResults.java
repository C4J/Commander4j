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
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.commander4j.gui.JCheckBox4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 */
public class TableCellRenderer_PanelResults extends DefaultTableCellRenderer
{

	HashMap<Long, Color> test = new HashMap<Long, Color>();
	private static final long serialVersionUID = 1;
	private JCheckBox4j checkbox;
	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	private Long lastSampleID = (long) 0;

	private Color active  = Common.color_table_sample_id_background1;
	private Color result_foreground  = Common.color_table_alternate_foreground1;
	private Color result  = Common.color_table_sample_id_background2;


	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		setFont(Common.font_table);

		Color foreground, background;

		if (column == 2)
		{
			if (test.containsKey(value))
			{
				active = test.get(value);
			}
			else
			{
				if (Long.compare((Long) value, lastSampleID) != 0)
				{
					if (active == Common.color_table_sample_id_background1)
					{
						active = Common.color_table_sample_id_background2;
					}
					else
					{
						active = Common.color_table_sample_id_background1;
					}
				}

				lastSampleID = (Long) value;

				test.put(lastSampleID, active);
			}
		}

		if (column == 10)
		{
			String comment = value.toString();

			if (comment.startsWith("IN"))
			{
				result = Common.color_background_result_ok;
				result_foreground = Common.color_table_alternate_foreground1;
			}
			if (comment.startsWith("INC"))
			{
				result = Common.color_background_result_warn;
				result_foreground = Common.color_table_alternate_foreground1;
			}
			if (comment.startsWith("OUT"))
			{
				result = Common.color_background_result_error;
				result_foreground = Common.color_table_alternate_foreground2;
			}
		}

		if (isSelected)
		{
			foreground = Common.color_list_foreground_selected;
			background = Common.color_list_background_selected;
		}
		else
		{
			if (row % 2 == 0)
			{
				foreground = Common.color_list_foreground;
				background = Common.color_table_standard_row1;

				if (column == 2)
				{
					foreground = Common.color_table_alternate_foreground1;
					background = active;
				}
				if (column == 10)
				{
					foreground = result_foreground;
					background = result;
				}
			}
			else
			{
				foreground = Common.color_list_foreground;
				background = Common.color_table_standard_row2;

				if (column == 2)
				{
					foreground = Common.color_table_alternate_foreground1;
					background = active;
				}
				if (column == 10)
				{
					foreground = result_foreground;
					background = result;
				}
			}
		}

		setForeground(foreground);
		setBackground(background);

		setHorizontalAlignment(JLabel.LEFT);

		try
		{
			if (value.getClass().equals(Boolean.class))
			{
				checkbox = new JCheckBox4j();
				checkbox.setOpaque(true);
				checkbox.setSelected((Boolean) value);
				checkbox.setText("");
				checkbox.setFont(Common.font_table);
				checkbox.setSize(getWidth(), getHeight());
				checkbox.setForeground(foreground);
				checkbox.setBackground(background);
				checkbox.setFocusable(false);
				checkbox.setHorizontalAlignment(JCheckBox4j.CENTER);

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
			System.out.println(ex.getMessage());
		}

		return this;
	}
}