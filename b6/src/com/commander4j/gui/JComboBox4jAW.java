package com.commander4j.gui;

import com.commander4j.sys.Common;

import java.awt.*; 

import javax.swing.*;


// got this workaround from the following bug: 
//      http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4618607 
public class JComboBox4jAW<T> extends JComboBox<T> {

    private static final long serialVersionUID = 1L;

	public JComboBox4jAW() { 
    		setFont(Common.font_combo);
    } 

    public JComboBox4jAW(final ComboBoxModel<T> items){ 
        super(items); 
        setFont(Common.font_combo);
    } 


    private boolean layingOut = false; 

    public void doLayout(){ 
        try{ 
            layingOut = true; 
                super.doLayout(); 
        }finally{ 
            layingOut = false; 
        } 
    } 

    public Dimension getSize(){ 
        Dimension dim = super.getSize(); 
        if(!layingOut) 
            dim.width = Math.max(dim.width, getPreferredSize().width); 
        return dim; 
    } 
}