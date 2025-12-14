package com.commander4j.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.commander4j.print.JPrintDevice;
import com.commander4j.sys.Common;

public class JPrintDeviceRenderer extends JLabel implements ListCellRenderer<JPrintDevice>
{

	private static final long serialVersionUID = 1L;

	public JPrintDeviceRenderer()
	{
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(

			JList<? extends JPrintDevice> list, JPrintDevice value, int index, boolean isSelected, boolean cellHasFocus)
	{

		if (value != null)
		{

			setText(value.toString());
			setToolTipText(value.getType());

			if (value.getType().equals("queue"))
			{
				setIcon(Common.icon_printer_queue_16x16);
			}
			else
			{
				setIcon(Common.icon_printer_direct_16x16);
			}
		}
		else
		{
			setText("");
			setIcon(null);
			setToolTipText(null);
		}

		if (isSelected)
		{
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;
	}
}
