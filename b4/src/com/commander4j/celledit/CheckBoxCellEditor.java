package com.commander4j.celledit;

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
			checkBox.setBackground(Common.color_listHighlighted);
		}
		else
		{
			if (row % 2 == 0)
			{
				checkBox.setBackground(Common.color_tablerow1);
			}
			else
			{
				checkBox.setBackground(Common.color_tablerow2);
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