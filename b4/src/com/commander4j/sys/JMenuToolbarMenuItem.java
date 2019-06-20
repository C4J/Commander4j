package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JMenuToolbarMenuItem.java
 * 
 * Package Name : com.commander4j.sys
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

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.commander4j.db.JDBModule;

public class JMenuToolbarMenuItem extends JButton
{
	private static final long serialVersionUID = 1;
	
	private JMenuOption menuoption = new JMenuOption(Common.selectedHostID, Common.sessionID);

	public void init()
	{
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setMaximumSize(new Dimension(Common.buttonToolbarSize,Common.buttonToolbarSize));
		this.setMinimumSize(new Dimension(Common.buttonToolbarSize,Common.buttonToolbarSize));
		this.setPreferredSize(new Dimension(Common.buttonToolbarSize,Common.buttonToolbarSize));
		this.setSize(Common.buttonToolbarSize,Common.buttonToolbarSize);
		this.setFont(Common.font_small);
	}
	
	public JMenuToolbarMenuItem(JMenuOption mo)
	{
		super();
		init();
		menuoption = mo;

		this.setToolTipText(menuoption.hint);
		this.setIcon(JDBModule.getModuleIcon32x32(mo.iconFilename, mo.moduleType));
		this.setText(menuoption.wrapped_description);
	}

	public String getModuleType() {
		return menuoption.moduleType;
	}

	public String toString() {
		return menuoption.moduleID;
	}
	
	
	public JMenuToolbarMenuItem(String title,String hint,Icon icon)
	{
		super();
		init();
		this.setToolTipText(hint);
		this.setIcon(icon);
		this.setText(title);
	}	
	
}
