package com.commander4j.renderer;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBListRenderer.java
 * 
 * Package Name : com.commander4j.dbrenderer
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

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;

import com.commander4j.db.JDBListData;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;

/**
 */
public class JDBListRenderer extends JLabel implements ListCellRenderer<Object>
{

	private static final long serialVersionUID = 1;
	private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private Color background = Common.color_listBackground;
	private JSeparator separator;

	public JDBListRenderer()
	{
		separator = new JSeparator(JSeparator.HORIZONTAL);
		setFont(Common.font_list);
	}


	public JDBListRenderer(Color newBackgroundColor)
	{
		separator = new JSeparator(JSeparator.HORIZONTAL);
		background = newBackgroundColor;
	}


	public Component getListCellRendererComponent(JList4j<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		{
			Icon theIcon = null;
			String theText = null;

			String str = (value == null) ? "" : value.toString();
			if (str.equals("SEPARATOR"))
			{
				return separator;
			}

			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			renderer.setFont(Common.font_list);

			if (value instanceof JDBListData)
			{
				JDBListData ldata = (JDBListData) value;
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
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		{
			Icon theIcon = null;
			String theText = null;

			String str = (value == null) ? "" : value.toString();
			if (str.equals("SEPARATOR"))
			{
				return separator;
			}

			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			renderer.setFont(Common.font_list);

			if (value instanceof JDBListData)
			{
				JDBListData ldata = (JDBListData) value;
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
