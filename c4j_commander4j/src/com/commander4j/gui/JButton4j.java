package com.commander4j.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JButton4j.java
 * 
 * Package Name : com.commander4j.gui
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

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import com.commander4j.sys.Common;

/**
 * Standard JButton4j which is used throughout the application permitting global
 * changes to be applied to look feel etc.
 *
 */
public class JButton4j extends JButton
{

	private static final long serialVersionUID = 1L;

	private void init()
	{
		setFont(Common.font_btn);
		setForeground(Common.color_button_font);
		setOpaque(true);
		setBackground(Common.color_button);
		setContentAreaFilled(true);

		// Hover listener
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				if (isEnabled())
				{
					setBackground(Common.color_button_hover);
					setForeground(Common.color_button_font_hover);
					setFont(Common.font_bold);
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				setBackground(Common.color_button);
				setForeground(Common.color_button_font);
				setFont(Common.font_btn);
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if (isEnabled())
				{
					setBackground(Common.color_button_hover);
					setForeground(Common.color_button_font_hover);
					setFont(Common.font_bold);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				setBackground(Common.color_button);
				setForeground(Common.color_button_font);
				setFont(Common.font_btn);
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (isEnabled())
				{
					setBackground(Common.color_button);
					setForeground(Common.color_button_font);
				}
			}
		});
	}

	public JButton4j()
	{
		super();
		init();
	}

	public JButton4j(Icon icon)
	{
		super(icon);
		init();
	}

	public JButton4j(String text)
	{
		super(text);
		setToolTipText(text);
		init();
	}

	public JButton4j(Action a)
	{
		super(a);
		init();
	}

	public JButton4j(String text, Icon icon)
	{
		super(text, icon);
		setToolTipText(text);
		init();
	}

	@Override
	public void setText(String text)
	{
		super.setText(text);
		setToolTipText(text);
	}

}
