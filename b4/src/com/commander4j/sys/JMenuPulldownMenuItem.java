/*
 * Created on 19-Mar-2005
 *
 */
package com.commander4j.sys;

import javax.swing.JMenuItem;

/**
 * @author David
 * 
 */
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
