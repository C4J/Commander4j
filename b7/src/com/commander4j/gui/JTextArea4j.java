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
    
	private void init()
	{
        setBorder(EMPTY_BORDER);
		setFont(Common.font_input);
	}

    public JTextArea4j() {
        super();
        init();
        initFocusBehavior();
    }
    
	public JTextArea4j(String text) {
		super(text);
		init();
        initFocusBehavior();
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

}

