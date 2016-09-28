package com.commander4j.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;

import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;

/**
 */
public class JListRenderer extends JLabel implements ListCellRenderer<Object>
{

	private static final long serialVersionUID = 1;
	private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private Color background = Common.color_listBackground;
	private JSeparator separator;

	public JListRenderer()
	{
		separator = new JSeparator(JSeparator.HORIZONTAL);
		setFont(Common.font_list);
	}

	public JListRenderer(Color newBackgroundColor)
	{
		separator = new JSeparator(JSeparator.HORIZONTAL);
		background = newBackgroundColor;
	}

	public Component getListCellRendererComponent(JList4j<? extends Object> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus)
	{
		{
			Icon theIcon = null;
			String theText = null;

			String str = (value == null) ? "" : value.toString();
			if (str.equals("SEPARATOR"))
			{
				return separator;
			}

			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			renderer.setFont(Common.font_list);

			if (value instanceof JListData)
			{
				JListData ldata = (JListData) value;
				theIcon = ldata.getIcon();
				theText = ldata.getmData().toString();
			} else
			{
				try
				{
					theText = value.toString();
				} catch (Exception ex)
				{
					theText = "";
				}
			}

			if (!isSelected)
			{
				renderer.setBackground(background);
				renderer.setForeground(Common.color_listFontSelected);
			} else
			{
				renderer.setBackground(Common.color_listHighlighted);
				renderer.setForeground(Common.color_listFontStandard);
			}

			if (theIcon != null)
			{
				renderer.setIcon(theIcon);
			}

			renderer.setText(theText);

			return renderer;
		}
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus)
	{
		{
			Icon theIcon = null;
			String theText = null;

			String str = (value == null) ? "" : value.toString();
			if (str.equals("SEPARATOR"))
			{
				return separator;
			}

			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			renderer.setFont(Common.font_list);

			if (value instanceof JListData)
			{
				JListData ldata = (JListData) value;
				theIcon = ldata.getIcon();
				theText = ldata.getmData().toString();
			} else
			{
				try
				{
					theText = value.toString();
				} catch (Exception ex)
				{
					theText = "";
				}
			}

			if (!isSelected)
			{
				renderer.setBackground(background);
				renderer.setForeground(Common.color_listFontSelected);
			} else
			{
				renderer.setBackground(Common.color_listHighlighted);
				renderer.setForeground(Common.color_listFontStandard);
			}

			if (theIcon != null)
			{
				renderer.setIcon(theIcon);
			}

			renderer.setText(theText);

			return renderer;
		}
	}
}
