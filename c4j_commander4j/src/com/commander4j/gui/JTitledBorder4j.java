package com.commander4j.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.commander4j.sys.Common;

public class JTitledBorder4j
{

	public static TitledBorder getPanelTitledBorder(String title)
	{
		TitledBorder result;

		result = BorderFactory.createTitledBorder(title);
		result.setTitleFont(Common.font_bold);
		result.setTitleColor(Common.color_panel_title);
		result.setTitleJustification(TitledBorder.LEFT);
		result.setTitlePosition(TitledBorder.DEFAULT_POSITION);

		return result;
	}

	public static Border getPanelLineBorder()
	{
		Border result;

		result = BorderFactory.createLineBorder(Color.GRAY);


		return result;
	}

}
