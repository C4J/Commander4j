package com.commander4j.renderer;

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
			foreground = Common.color_listFontSelected;
			background = Common.color_listHighlighted;
		} else
		{
			if (row % 2 == 0)
			{
				foreground = Common.color_listFontStandard;
				background = Common.color_tablerow3;
			} else
			{
				foreground = Common.color_listFontStandard;
				background = Common.color_tablerow2;
			}
		}

		String s = "";

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