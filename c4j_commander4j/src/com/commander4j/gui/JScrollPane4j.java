package com.commander4j.gui;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTree;

import com.commander4j.sys.Common;

public class JScrollPane4j extends JScrollPane
{

	private static final long serialVersionUID = 1L;

	public static final int Table = 0;
	public static final int List = 1;
	public static final int Assigned = 2;
	public static final int UnAssigned = 3;

	private Color backgroundColor = Common.color_list_background;

	public void setBackgroundColor(Color c)
	{
		backgroundColor = c;
	}

	private void init()
	{
		getViewport().setBackground(backgroundColor);
		setBackground(backgroundColor);
		setBorder(Common.borderScrollPane);
		repaint();
	}

	public JScrollPane4j(JTree tree)
	{
		super(tree);

		init();
	}

	public JScrollPane4j(int  mode)
	{
		super();

		if (mode == Table) setBackgroundColor(Common.color_table_background1);
		if (mode == List) setBackgroundColor(Common.color_list_background);
		if (mode == Assigned) setBackgroundColor(Common.color_list_background_assigned);
		if (mode == UnAssigned) setBackgroundColor(Common.color_list_background_unassigned);

		init();
	}

	public JScrollPane4j(Color c)
	{
		super();
		setBackground(c);
		init();
	}

	public JScrollPane4j()
	{
		super();

		init();
	}

}
