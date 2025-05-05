package com.commander4j.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.commander4j.db.JDBWTSampleDetail;
import com.commander4j.sys.Common;

public class WeightSampleDetailCellRenderer extends JLabel implements ListCellRenderer<Object> {


	private static final long serialVersionUID = 1L;


	public WeightSampleDetailCellRenderer() {
        setOpaque(true);
		setFont(Common.font_list_weights);
    }


	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
        setText(value.toString());
		setFont(Common.font_list_weights);
        
        JDBWTSampleDetail x =  ( (JDBWTSampleDetail) value);
        
        if (x.getSampleT1Count()==1)
        {
        	setBackground(Color.YELLOW);
        }
        else
        {
            if (x.getSampleT2Count()==1)
            {
            	setBackground(Color.RED);
            }
            else
            {
            	setBackground(Common.color_listBackground);
            }
        }

        return this;
	}



}
