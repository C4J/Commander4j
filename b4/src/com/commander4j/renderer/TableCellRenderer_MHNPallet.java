// $codepro.audit.disable numericLiterals

package com.commander4j.renderer;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.commander4j.db.JDBMHNDecisions;
import com.commander4j.sys.Common;
import com.commander4j.tablemodel.JDBMHNPalletTableModelProperties;
import com.commander4j.util.JColorPair;
import com.commander4j.util.JUtility;

/**
 */
public class TableCellRenderer_MHNPallet extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1;

	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	private HashMap<String,JColorPair> lookup = new HashMap<String,JColorPair>();
	private JDBMHNDecisions db = new JDBMHNDecisions(Common.selectedHostID, Common.sessionID);

	public TableCellRenderer_MHNPallet()
	{
		super();
		lookup = db.getDecisionColors();
	}
	

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

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
				background = Common.color_tablerow3;
			}
			else
			{
				foreground = Common.color_listFontStandard;
				background = Common.color_tablerow2;
			}
		}

		if (column == JDBMHNPalletTableModelProperties.Decision_Col)
		{
			if (lookup.containsKey((String) value))
			{
				background = lookup.get((String) value).background;
				foreground = lookup.get((String) value).foreground;
			}
			else
			{
				background = Color.black;
				foreground = Color.white;
			}
		}
		
		setForeground(foreground);
		setBackground(background);

		setHorizontalAlignment(JLabel.LEFT);

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

		return this;
	}
}