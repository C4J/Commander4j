/*
 * Created on 05-Apr-2005
 *
 */
package com.commander4j.sys;

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.commander4j.db.JDBModule;

// import javax.swing.*;

/**
 * @author David
 * 
 */
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
		this.setIcon(JDBModule.getModuleIcon(mo.iconFilename, mo.moduleType));
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
