package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JMenuPulldownMenuItem.java
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

import javax.swing.JMenuItem;

public class JMenuPulldownMenuItem extends JMenuItem
{
	private static final long serialVersionUID = 1;
	private JMenuOption menuoption = new JMenuOption(Common.selectedHostID, Common.sessionID);

	public JMenuPulldownMenuItem(JMenuOption mo)
	{
		super(mo.description);
		setMnemonic(mo.mnemonic);
		setToolTipText(mo.hint);
		setFont(Common.font_menu);
		menuoption = mo;
	}

	public String getModuleType() {
		return menuoption.moduleType;
	}

	public String toString() {
		return menuoption.moduleID;
	}

}
