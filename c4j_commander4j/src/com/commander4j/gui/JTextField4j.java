package com.commander4j.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

import com.commander4j.sys.Common;
import com.commander4j.util.JFixedSizeFilter;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JTextField4j extends JTextField {

    private static final long serialVersionUID = 1L;
    private final Color overflowForeground = Color.RED;
    private JFixedSizeFilter tsf;
    private int characterLimit = -1; // -1 means "no limit"
    private static final Border EMPTY_BORDER = new LineBorder(Color.GRAY);
    private boolean hasFocus = false;
    
	private void init()
	{
        setDisabledTextColor(Common.color_textfield_foreground_disabled);
        setBorder(EMPTY_BORDER);
		setFont(Common.font_input);
	}

    public JTextField4j() {
        super();
        init();
        initFocusBehavior();
        updateColors();
    }
    
	public JTextField4j(String text) {
		super(text);
		init();
        initFocusBehavior();
        updateColors();
	}

    public JTextField4j(int columns) {
        super(columns);
        init();
        this.characterLimit = columns;
        initFocusBehavior();
        initCharacterLimitBehavior();
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

    private void initCharacterLimitBehavior() {
        if (characterLimit > 0) {
            getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    updateTextColor();
                }

                public void removeUpdate(DocumentEvent e) {
                    updateTextColor();
                }

                public void changedUpdate(DocumentEvent e) {
                    updateTextColor();
                }

                private void updateTextColor() {
                    if (getText().length() >= characterLimit) {
                        setForeground(overflowForeground);
                    } else {
                        updateColors();
                    }
                }
            });
    
    		tsf = new JFixedSizeFilter(characterLimit);
    	    ((AbstractDocument) getDocument()).setDocumentFilter(tsf);
        }
    }

    public void setCharacterLimit(int limit) {
        if (characterLimit < 0 && limit > 0) {
            this.characterLimit = limit;
            initCharacterLimitBehavior();
        } else {
            this.characterLimit = limit;
        }
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

