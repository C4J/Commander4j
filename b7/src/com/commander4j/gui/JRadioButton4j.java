package com.commander4j.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JRadioButton4j.java
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

import javax.swing.JRadioButton;

import com.commander4j.sys.Common;

public class JRadioButton4j extends JRadioButton
{

	private static final long serialVersionUID = 1L;
	private boolean hover = false;

	public void init()
	{
		setFont(Common.font_std);
		setBackground(Common.color_app_window);
        setMargin(new Insets(0, 0, 0, 0));  // Reduce padding
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });		
	}
	
	public JRadioButton4j() {
		super();
		init();
	}
	
	public JRadioButton4j(String arg0) {
		super(arg0);
		init();
	}
	
	   @Override
	    public Dimension getPreferredSize() {
	        // Increase clickable area slightly
	        Dimension d = super.getPreferredSize();
	        d.width += 6;
	        d.height = Math.max(d.height, 22); // Minimum height
	        return d;
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        Graphics2D g2 = (Graphics2D) g.create();
	        try {
	            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	            int boxSize = 16;
	            int boxX = 2;
	            int boxY = (getHeight() - boxSize) / 2;

	            // Draw checkbox outline
	            g2.setColor(hover ? new Color(150, 150, 150) : new Color(120, 120, 120));
	            //g2.drawRect(boxX, boxY, boxSize, boxSize);
	            g2.drawOval(boxX, boxY, boxSize, boxSize);

	            // Fill if selected
	            if (isSelected()) {
	                g2.setStroke(new BasicStroke(2f));
	                g2.setColor(new Color(60, 120, 200));

	                int x1 = boxX + 3;
	                int y1 = boxY + boxSize / 2;
	                int x2 = boxX + boxSize / 2 - 1;
	                int y2 = boxY + boxSize - 4;
	                int x3 = boxX + boxSize - 3;
	                int y3 = boxY + 4;
	                g2.drawLine(x1, y1, x2, y2);  // left branch
	                g2.drawLine(x2, y2, x3, y3);  // right branch
	            }

	            // Draw label text
	            g2.setColor(getForeground());
	            FontMetrics fm = g2.getFontMetrics();
	            int textX = boxX + boxSize + 6;
	            int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

	            g2.drawString(getText(), textX, textY);

	        } finally {
	            g2.dispose();
	        }
	    }

	    @Override
	    public boolean contains(int x, int y) {
	        // Increase clickable area
	        return new Rectangle(0, 0, getWidth(), getHeight()).contains(x, y);
	    }

}
