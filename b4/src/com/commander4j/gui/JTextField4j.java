package com.commander4j.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;

import com.commander4j.sys.Common;
import com.commander4j.util.JFixedSizeFilter;

public class JTextField4j extends JTextField {

	private static final long serialVersionUID = 1L;
	AbstractDocument doc1;
	JFixedSizeFilter tsf;
	
	FocusListener fl =new FocusListener() {
	      public void focusGained(FocusEvent e) {
	    	  setForeground(Common.color_textfield_foreground_focus_color);
	    	  setBackground(Common.color_textfield_background_focus_color);
	        }

	        public void focusLost(FocusEvent e) {
	           setForeground(Common.color_textfield_forground_nofocus_color);
	           setBackground(Common.color_textfield_background_nofocus_color);
	        }
	      };
	

	public JTextField4j() {
		setFont(Common.font_input);
		setDisabledTextColor(Common.color_text_disabled);
	}

	public JTextField4j(String text) {
		super(text);
		setFont(Common.font_input);
		setDisabledTextColor(Common.color_text_disabled);
	}

	public JTextField4j(int columns) {

		super(columns);
		final int cols = columns;
		setFont(Common.font_input);
		setDisabledTextColor(Common.color_text_disabled);
		doc1 = (AbstractDocument) this.getDocument();
		tsf = new JFixedSizeFilter(columns);
		doc1.setDocumentFilter(tsf);
		
		addFocusListener(fl);

		doc1.addDocumentListener(new DocumentListener(){

			@Override
			public void insertUpdate(DocumentEvent e)
			{
		        if (cols==e.getDocument().getLength())
		        {
		        	setForeground(Common.color_text_maxsize_color);
		        }
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				setForeground(Common.color_textfield_foreground_focus_color);
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				setForeground(Common.color_textfield_foreground_focus_color);
				
			}});
	}
		

	public JTextField4j(String text, int columns) {
		super(text, columns);
		setFont(Common.font_input);
		setDisabledTextColor(Common.color_text_disabled);
	}

	public JTextField4j(Document doc, String text, int columns) {
		super(doc, text, columns);
		setFont(Common.font_input);
		setDisabledTextColor(Common.color_text_disabled);
	}

}
