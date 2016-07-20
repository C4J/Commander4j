package com.commander4j.renderer;

import java.awt.Component;

import com.commander4j.gui.JCheckListItem;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MultiItemCheckListRenderer extends JCheckBox implements ListCellRenderer<Object>
{

	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus)
	{
		setEnabled(list.isEnabled());
		setSelected(((JCheckListItem) value).isSelected());
		setFont(Common.font_list);
		setBackground(list.getBackground());
		setForeground(list.getForeground());
		setText(value.toString());
		return this;
	}
	
	public Component getListCellRendererComponent(JList4j<?> list, Object value, int index, boolean isSelected, boolean hasFocus)
	{
		setEnabled(list.isEnabled());
		setSelected(((JCheckListItem) value).isSelected());
		setFont(Common.font_list);
		setBackground(list.getBackground());
		setForeground(list.getForeground());
		setText(value.toString());
		return this;
	}
}