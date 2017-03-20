
package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JMenuToolbarMenu.java
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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

public class JMenuToolbarMenu
{

	final Logger logger = Logger.getLogger(JMenuToolbarMenu.class);
	private String hostID;
	private String sessionID;

	public JMenuToolbarMenu(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private String getHostID() {
		return hostID;
	}

	private String getSessionID() {
		return sessionID;
	}

	public void buildMenu(JToolBar jtb,boolean quickMenu) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int usedWidth = Common.buttonToolbarSize + Common.spacerToolBarSize; //Home + Spacer
		
		
		if (quickMenu)
		{
			usedWidth = usedWidth+Common.spacerToolBarSize + Common.comboToolBarSize + Common.spacerToolBarSize + Common.buttonToolbarSize; // ComboBox, Spacer and Execute

		}
		
		usedWidth = usedWidth + Common.spacerToolBarSize + Common.buttonToolbarSize + Common.buttonToolbarSize + Common.buttonToolbarSize + Common.buttonToolbarSize +Common.spacerToolBarSize; //Minimise, Maximise, Restore, Help
		
		usedWidth = usedWidth + Common.buttonToolbarSize; //Close
		
		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JMenuToolbarMenu.buildMenu"));
			stmt.setString(1, Common.userList.getUser(getSessionID()).getUserId());
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while ((rs.next()) & ((usedWidth+55) < screenSize.width))
			{

				JMenuOption menuOption = new JMenuOption(getHostID(), getSessionID());
				menuOption.load(rs);

				JMenuToolbarMenuItem newmenuitem = new JMenuToolbarMenuItem(menuOption);

				newmenuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						JMenuToolbarMenuItem tbMenuItem = (JMenuToolbarMenuItem) evt.getSource();
						if (tbMenuItem.getModuleType().equals("FORM"))
						{
							JLaunchMenu.runForm(tbMenuItem.toString());
						}
						if (tbMenuItem.getModuleType().equals("REPORT"))
						{
							JLaunchReport.runReport(tbMenuItem.toString(),null,"",null,"");
						}
					}
				});

				jtb.add(newmenuitem);

				usedWidth = usedWidth + Common.buttonToolbarSize;

			}
			rs.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Error in JMenuToolbarMenu " + ex.getMessage());
		}

	}
}
