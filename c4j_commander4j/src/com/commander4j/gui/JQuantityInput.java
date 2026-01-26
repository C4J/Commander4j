package com.commander4j.gui;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JQuantityInput.java
 * 
 * Package Name : com.commander4j.util
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

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JQuantityInput extends JFormattedTextField
{

	private static final long serialVersionUID = 1L;
    private static final Border EMPTY_BORDER = new LineBorder(Color.GRAY);
	
	private void init()
	{
        setDisabledTextColor(Common.color_textfield_foreground_disabled);
		setOpaque(true);
        setBorder(EMPTY_BORDER);
		setFont(Common.font_input);
		setHorizontalAlignment(SwingConstants.TRAILING);
		
	}
	
	public JQuantityInput(BigDecimal arg0)
	{
		super(new DecimalFormat("###,##0.000"));
		this.setValue(arg0);
        init();
        initFocusBehavior();
        updateColors();
	}
	
	public BigDecimal getQuantity()
	{
		BigDecimal result;
		
		try
		{
			result = JUtility.stringToBigDecimal(getText());
		}
		catch (Exception ex)
		{
			result = new BigDecimal("0");
		}

		return result;
	}
	
	public void setQuantity(BigDecimal qty)
	{
		setText(qty.toString());
	}
	
    private void initFocusBehavior() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            	if (isEditable())
            	{
                    setBackground(Common.color_textfield_background_focus_color);
            	}

            }

            @Override
            public void focusLost(FocusEvent e) {
            	if (isEditable())
            	{
                setBackground(Common.color_textfield_background_nofocus_color);
            	}
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateColors();
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        updateColors();
    }
    
    @Override
    public void updateUI() {
        super.updateUI();
        setDisabledTextColor(Common.color_textfield_foreground_disabled);
        updateColors();
    }

    private void updateColors() {
        if (!isEnabled()) {
            setBackground(Common.color_textfield_background_disabled);
            setForeground(Common.color_textfield_foreground_disabled);
        } else if (!isEditable()) {
            setBackground(Common.color_textfield_background_disabled);
            setForeground(Common.color_textfield_foreground_disabled);
        } else {
            setBackground(Common.color_textfield_background_nofocus_color);
            setForeground(Common.color_textfield_foreground_nofocus_color);
        }
    }
}
