package com.commander4j.celledit;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.AbstractCellEditor;
import javax.swing.CellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.commander4j.db.JDBQMSelectList;
import com.commander4j.gui.JComboBox4jAW;
import com.commander4j.sys.Common;

public class ComboBoxCellEditorQMResult extends AbstractCellEditor implements CellEditor, TableCellEditor, ActionListener
{

	private static final long serialVersionUID = 1L;
	private JDBQMSelectList result = new JDBQMSelectList(Common.selectedHostID,Common.sessionID);
	private LinkedList<JDBQMSelectList> listResult;
	private String keyfield="";
	private JComboBox4jAW<JDBQMSelectList> comboResult = new JComboBox4jAW<JDBQMSelectList>();
	
	public ComboBoxCellEditorQMResult(LinkedList<JDBQMSelectList> listResult) {
		this.listResult = listResult;
		
		comboResult.removeAll();
		for (JDBQMSelectList aResult : listResult) {
			comboResult.addItem(aResult);
		}
		
		int displayMax = listResult.size();
		if (displayMax > 25)
		{
			displayMax = 25;
		}
		
		comboResult.setMaximumRowCount(displayMax);
		
		comboResult.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				System.out.println(e.getSource());
				stopCellEditing();
			}
			
		});
	}
	
	@Override
	public Object getCellEditorValue()
	{
		return this.result;
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		@SuppressWarnings("unchecked")
		JComboBox4jAW<JDBQMSelectList> comboResult = (JComboBox4jAW<JDBQMSelectList>) event.getSource();
		
		this.result = (JDBQMSelectList) comboResult.getSelectedItem();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (value instanceof JDBQMSelectList) {
			this.result = (JDBQMSelectList) value;
		}
		else
		{
			this.result = new JDBQMSelectList();
		}
		
		keyfield = this.result.getValue();

			
		comboResult.removeAll();
		int sequence = 0;
		int key = 0;
		
		for (JDBQMSelectList aResult : listResult) {
			if (aResult.getValue().equals(keyfield))
			{
				key = sequence; 
			}
			sequence ++;
		}

		comboResult.setSelectedIndex(key);
		comboResult.addActionListener(this);
		
		return comboResult;
	}

}
