package com.commander4j.celledit;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : CheckBoxCellEditor.java
 * 
 * Package Name : com.commander4j.celledit
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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

import com.commander4j.gui.JCheckBox4j;
import com.commander4j.sys.Common;

public class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JCheckBox4j checkBox;  
      
    public CheckBoxCellEditor() {  
        checkBox = new JCheckBox4j();  
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);  
        checkBox.setBackground( Color.white);  
        checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopCellEditing();
			}
		});
    }  
      
    public Component getTableCellEditorComponent(  
            JTable table,   
            Object value,   
            boolean isSelected,   
            int row,   
            int column) {  

        checkBox.setSelected(((Boolean) value).booleanValue());  
          

		if (isSelected)
		{
			checkBox.setBackground(Common.color_list_background_selected);
		}
		else
		{
			if (row % 2 == 0)
			{
				checkBox.setBackground(Common.color_table_standard_row1);
			}
			else
			{
				checkBox.setBackground(Common.color_table_standard_row2);
			}
		}
          
        return checkBox;  
    }  
    
	@Override
	public boolean stopCellEditing() {
		try {
			// try to get the value
			this.getCellEditorValue();
			return super.stopCellEditing();
		} catch (Exception ex) {
			return false;
		}

	}
    public Object getCellEditorValue() {  
        return String.valueOf(checkBox.isSelected());  
    }  
}  