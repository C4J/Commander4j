package com.commander4j.gui;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDateControl.java
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

import java.util.Calendar;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.commander4j.sys.Common;

public class JDateControl extends JSpinner4j
{

	public static int mode_disable_not_visible = 0;
	public static int mode_disable_visible = 1;
	
	private static final long serialVersionUID = 1L;
	private static final Border EMPTY_BORDER = new LineBorder(Color.GRAY);
	SpinnerDateModel datemodel;
	private int displayMode = mode_disable_not_visible;

	public JDateControl()
	{
		super();

		setBorder(EMPTY_BORDER);
		Date today = new Date();
		datemodel = new SpinnerDateModel(today, null, null, Calendar.MONTH);
		setModel(datemodel);
		DateEditor editor = new JSpinner4j.DateEditor(this, "dd/MM/yyyy HH:mm:ss");
		editor.getTextField().setFont(Common.font_dates);
		setEditor(editor);

		JFormattedTextField textField = editor.getTextField();

		textField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent e)
			{
				textField.setBackground(Common.color_textfield_background_focus_color);
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				textField.setBackground(Common.color_textfield_background_nofocus_color);
			}
		});

		applyStyle();
	}

	public JDateControl(SpinnerModel model)
	{
		super(model);
		applyStyle();
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		applyStyle();
	}

	@Override
	public void setEditor(JComponent editor)
	{
		super.setEditor(editor);
		applyStyle();
	}

	public void setEditable(boolean editable)
	{
		JTextField tf = getEditorTextField();
		if (tf != null)
		{
			tf.setEditable(editable);
		}
		applyStyle();
	}

	public boolean isEditable()
	{
		JTextField tf = getEditorTextField();
		return tf != null && tf.isEditable();
	}

	private JTextField getEditorTextField()
	{
		if (getEditor() instanceof DefaultEditor de)
		{
			return de.getTextField();
		}
		return null;
	}

	private void applyStyle()
	{
		JTextField tf = getEditorTextField();
		if (tf == null)
			return;

		tf.setDisabledTextColor(getDisplayModeDisabledForgegroundColor());

		if (!isEnabled())
		{
			tf.setBackground(Common.color_textfield_background_disabled);
			tf.setForeground(getDisplayModeDisabledForgegroundColor());
		}
		else if (!isEditable())
		{
			tf.setBackground(Common.color_textfield_background_disabled);
			tf.setForeground(getDisplayModeDisabledForgegroundColor());
		}
		else
		{
			tf.setBackground(Common.color_textfield_background_nofocus_color);
			tf.setForeground(Common.color_textfield_forground_nofocus_color);
		}
	}

	@Override
	public void updateUI()
	{
		super.updateUI();
		applyStyle();
	}
	
	public Date getDate()
	{
		return datemodel.getDate();
	}

	public void setDate(Date date)
	{
		datemodel.setValue(date);
	}
	
	public void setDisplayMode(int mode)
	{
		displayMode = mode;
		applyStyle();
	}
	
	public int getDisplayMode()
	{
		return displayMode;
	}
	
	private Color getDisplayModeDisabledForgegroundColor()
	{
		Color result = Color.BLACK;
		
		if (displayMode == JDateControl.mode_disable_not_visible)
			result = Common.color_textfield_background_disabled;
		
		if (displayMode == JDateControl.mode_disable_visible)
			result = Common.color_textfield_foreground_disabled;
		
		return result;
	}

}
