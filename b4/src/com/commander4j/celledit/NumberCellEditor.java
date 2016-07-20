package com.commander4j.celledit;

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
			return new Float(n.floatValue());

		} catch (ParseException pex)
		{
			throw new RuntimeException(pex);
		}
	}
}
