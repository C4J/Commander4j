// $codepro.audit.disable numericLiterals

package com.commander4j.renderer;

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
				this.setText(JUtility.bigDecimaltoString((BigDecimal) value));
			}
		}
		catch (Exception ex)
		{

		}

		return this;
	}
}