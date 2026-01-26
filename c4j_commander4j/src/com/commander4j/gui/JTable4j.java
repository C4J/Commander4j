package com.commander4j.gui;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.commander4j.sys.Common;

public class JTable4j extends JTable
{

	private static final long serialVersionUID = 1L;

	public JTable4j()
	{
		setDefaultRenderer(Object.class, Common.renderer_table);
		setDefaultRenderer(Object.class, Common.renderer_table);
		setDefaultRenderer(Integer.class, Common.renderer_table);
		setDefaultRenderer(Boolean.class, Common.renderer_table);

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		getTableHeader().setDefaultRenderer(Common.renderer_tableheader);
		getTableHeader().setPreferredSize(new Dimension(10000, 25));
		getTableHeader().setForeground(Common.color_table_header_foreground);
		getTableHeader().setFont(Common.font_table_header);
		getTableHeader().setReorderingAllowed(false);
	}

}
