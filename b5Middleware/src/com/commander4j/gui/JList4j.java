package com.commander4j.gui;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;

import com.commander4j.sys.Common;

public class JList4j<E> extends JList<E>
{

	private static final long serialVersionUID = 1L;

	public JList4j(ListModel<E> items)
	{
		super(items);
		setFont(Common.font_list);
		setBackground(Common.color_listBackground);
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	public JList4j(E[] items)
	{
		super(items);
		setFont(Common.font_list);
		setBackground(Common.color_listBackground);
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	public JList4j()
	{
		super();
		setFont(Common.font_list);
		setBackground(Common.color_listBackground);
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}

}
