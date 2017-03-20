package com.commander4j.gui;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JComboBox4jAW.java
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JComboBox;

import com.commander4j.sys.Common;

public class JComboBox4jAW<T> extends JComboBox<T> {

	private static final long serialVersionUID = 1L;

	public JComboBox4jAW() {
		super();
		setFont(Common.font_combo);
	}

	public JComboBox4jAW(String[] fieldAlignment)
	{
		super();
		setFont(Common.font_combo);
	}


    private boolean layingOut = false;
    private int widestLengh = 0;
    private boolean wide = false;

    public boolean isWide() {
        return wide;
    }

    // Setting the JComboBox wide
    public void setWide(boolean wide) {
        this.wide = wide;
        widestLengh = getWidestItemWidth();

    }

    public Dimension getSize() {
        Dimension dim = super.getSize();
        if (!layingOut && isWide())
            dim.width = Math.max(widestLengh, dim.width);
        return dim;
    }

    public int getWidestItemWidth() {

        int numOfItems = this.getItemCount();
        Font font = this.getFont();
        FontMetrics metrics = this.getFontMetrics(font);
        int widest = 0;
        for (int i = 0; i < numOfItems; i++) {
            Object item = this.getItemAt(i);
            int lineWidth = metrics.stringWidth(item.toString());
            widest = Math.max(widest, lineWidth);
        }

        return widest + 5;
    }

    public void doLayout() {
        try {
            layingOut = true;
            super.doLayout();
        } finally {
            layingOut = false;
        }
    }




}
