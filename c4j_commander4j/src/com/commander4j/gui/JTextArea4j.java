package com.commander4j.gui;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.commander4j.sys.Common;

public class JTextArea4j extends JTextArea {

    private static final long serialVersionUID = 1L;
    private static final Border EMPTY_BORDER = new LineBorder(Color.GRAY);
    private boolean hasFocus = false;
    
	private void init()
	{
        setBorder(EMPTY_BORDER);
		setFont(Common.font_input);
	}

    public JTextArea4j() {
        super();
        init();
        initFocusBehavior();
        updateColors();
    }
    
	public JTextArea4j(String text) {
		super(text);
		init();
        initFocusBehavior();
        updateColors();
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

    private void initFocusBehavior() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            	hasFocus = true;
            	if (isEditable())
            	{
                    setBackground(Common.color_textfield_background_focus_color);
            	}

            }

            @Override
            public void focusLost(FocusEvent e) {
            	hasFocus = false;
            	if (isEditable())
            	{
                setBackground(Common.color_textfield_background_nofocus_color);
            	}
            }
        });
    }
    
    private void updateColors() {
        if (!isEnabled()) {
            setBackground(Common.color_textfield_background_disabled);
            setForeground(Common.color_textfield_foreground_disabled);
        } else if (!isEditable()) {
            setBackground(Common.color_textfield_background_disabled);
            setForeground(Common.color_textfield_foreground_disabled);
        } else if (hasFocus){
            setBackground(Common.color_textfield_background_focus_color);
            setForeground(Common.color_textfield_foreground_focus_color);
        } else
        {
            setBackground(Common.color_textfield_background_nofocus_color);
            setForeground(Common.color_textfield_forground_nofocus_color);	
        }
        
    }

}

