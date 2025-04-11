package com.commander4j.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JToggleButton;

import com.commander4j.sys.Common;

public class JToggleButton4j extends JToggleButton
{

	private static final long serialVersionUID = 1L;

	private void init()
	{
		setFont(Common.font_btn);
		setForeground(Color.black);
		setOpaque(true);
		setBackground(Common.color_button);
        setContentAreaFilled(false);
        setFocusPainted(false);
		
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				if (isEnabled())
				{
					setBackground(Common.color_button);
					setForeground(Common.color_button_font);
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				setBackground(Common.color_button);
				setForeground(Common.color_button_font);
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if (isEnabled())
				{
					setBackground(Common.color_button);
					setForeground(Common.color_button_font);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				setBackground(Common.color_button);
				setForeground(Common.color_button_font);
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
	
	public JToggleButton4j()
	{
		init();
	}
	
	public JToggleButton4j(Icon icn)
	{
		super(icn);
		init();
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Common.color_button);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
