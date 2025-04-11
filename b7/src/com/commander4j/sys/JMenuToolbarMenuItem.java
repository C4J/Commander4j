package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JMenuToolbarMenuItem.java
 * 
 * Package Name : com.commander4j.sys
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

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.commander4j.db.JDBModule;

public class JMenuToolbarMenuItem extends JButton
{
	private static final long serialVersionUID = 1;
	
	private JMenuOption menuoption = new JMenuOption(Common.selectedHostID, Common.sessionID);

	public void init()
	{
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setMaximumSize(new Dimension(Common.buttonToolbarSize,Common.buttonToolbarSize));
		this.setMinimumSize(new Dimension(Common.buttonToolbarSize,Common.buttonToolbarSize));
		this.setPreferredSize(new Dimension(Common.buttonToolbarSize,Common.buttonToolbarSize));
		this.setSize(Common.buttonToolbarSize,Common.buttonToolbarSize);
		this.setFont(Common.font_btn_small);

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
					setFont(Common.font_btn_small_bold);
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				setBackground(Common.color_button);
				setForeground(Common.color_button_font);
				setFont(Common.font_btn_small);
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if (isEnabled())
				{
					setBackground(Common.color_button_hover);
					setForeground(Common.color_button_font_hover);
					setFont(Common.font_btn_small_bold);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				setBackground(Common.color_button);
				setForeground(Common.color_button_font);
				setFont(Common.font_btn_small);
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
	
	public JMenuToolbarMenuItem(JMenuOption mo)
	{
		super();
		init();
		menuoption = mo;

		this.setToolTipText(menuoption.hint);
		this.setIcon(JDBModule.getModuleIcon32x32(mo.iconFilename, mo.moduleType));
		this.setText(menuoption.wrapped_description);
	}

	public String getModuleType() {
		return menuoption.moduleType;
	}

	public String toString() {
		return menuoption.moduleID;
	}
	
	
	public JMenuToolbarMenuItem(String title,String hint,Icon icon)
	{
		super();
		init();
		this.setToolTipText(hint);
		this.setIcon(icon);
		this.setText(title);
	}	
	
}
