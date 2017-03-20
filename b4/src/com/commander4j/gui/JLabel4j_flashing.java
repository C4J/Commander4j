package com.commander4j.gui;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JLabel4j_flashing.java
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

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

import com.commander4j.sys.Common;

public class JLabel4j_flashing extends JLabel
{

	private static final long serialVersionUID = 1L;

	private static final int BLINKING_RATE = 500; // in ms

	private boolean blinkingOn = true;
	
	public JLabel4j_flashing(String text)
	{
		super(text);
		setFont(Common.font_title);
		Timer timer = new Timer(BLINKING_RATE, new TimerListener(this));
		timer.setInitialDelay(0);
		timer.start();
	}

	public void setBlinking(boolean flag)
	{
		this.blinkingOn = flag;
	}

	public boolean getBlinking(boolean flag)
	{
		return this.blinkingOn;
	}

	private class TimerListener implements ActionListener
	{
		private JLabel4j_flashing bl;
		private Color bg;
		private Color fg;
		private boolean isForeground = true;

		public TimerListener(JLabel4j_flashing bl)
		{
			this.bl = bl;
			fg = bl.getForeground();
			bg = bl.getBackground();
		}

		public void actionPerformed(ActionEvent e)
		{
			if (bl.blinkingOn)
			{
				if (isForeground)
				{
					bl.setForeground(fg);
				} else
				{
					bl.setForeground(bg);
				}
				isForeground = !isForeground;
			} else
			{
				// here we want to make sure that the label is visible
				// if the blinking is off.
				if (isForeground)
				{
					bl.setForeground(fg);
					isForeground = false;
				}
			}
		}
	}
}
