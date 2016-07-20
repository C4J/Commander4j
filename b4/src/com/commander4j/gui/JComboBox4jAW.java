package com.commander4j.gui;

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
