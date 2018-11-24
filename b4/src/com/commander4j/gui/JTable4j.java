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
		getTableHeader().setDefaultRenderer(Common.renderer_tableheader);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setReorderingAllowed(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setPreferredSize(new Dimension(10000, 25));
	}
	
}
