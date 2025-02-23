package com.commander4j.celledit;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : NumberCellEditor.java
 * 
 * Package Name : com.commander4j.celledit
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.commander4j.sys.Common;


public class NumberCellEditor extends DefaultCellEditor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NumberCellEditor()
	{
		super(new JFormattedTextField());
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		JFormattedTextField editor = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
		Color foreground, background;

		editor.setHorizontalAlignment(SwingConstants.LEFT);

		foreground = Common.color_listFontSelected;
		background = Color.white;

		editor.setForeground(foreground);
		editor.setBackground(background);
		editor.setFont(Common.font_table);

		return editor;
	}

	@Override
	public boolean stopCellEditing()
	{
		try
		{
			// try to get the value
			this.getCellEditorValue();
			return super.stopCellEditing();
		} catch (Exception ex)
		{
			return false;
		}

	}

	@Override
	public Object getCellEditorValue()
	{
		// get content of textField
		String str = (String) super.getCellEditorValue();
		if (str == null)
		{
			return "";
		}

		if (str.length() == 0)
		{
			return "";
		}

		// try to parse a number
		try
		{
			ParsePosition pos = new ParsePosition(0);
			Number n = NumberFormat.getInstance().parse(str, pos);
			if (pos.getIndex() != str.length())
			{
				throw new ParseException("parsing incomplete", pos.getIndex());
			}

			// return an instance of column class
			return Float.valueOf(n.floatValue());

		} catch (ParseException pex)
		{
			throw new RuntimeException(pex);
		}
	}
}
