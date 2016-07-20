package com.commander4j.gui;

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
