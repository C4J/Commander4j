package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBUserJList.java
 * 
 * Package Name : com.commander4j.db
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

import java.awt.event.MouseEvent;

import javax.swing.JList;

import com.commander4j.sys.Common;

public class JDBUserJList extends JList<JDBListData>
{

	private static final long serialVersionUID = 1;
	private String key;
	private String tooltip;
	private JDBUser usr;
	private String hostID;
	private String sessionID;
	private JDBLanguage lang;

	private void setSessionID(String session) {
		sessionID = session;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private String getSessionID() {
		return sessionID;
	}

	private String getHostID() {
		return hostID;
	}

	public JDBUserJList()
	{
		setFont(Common.font_list);
		setBackground(Common.color_list_background);		
	}

	public JDBUserJList(String host, String session)
	{

		try
		{
			setHostID(host);
			setSessionID(session);
			usr = new JDBUser(getHostID(), getSessionID());
			lang = new JDBLanguage(getHostID(),getSessionID());
		}
		catch (Exception ex)
		{

		}
		setFont(Common.font_list);
		setBackground(Common.color_list_background);
	}

	public String getToolTipText(MouseEvent e) {

		try
		{
			int index = locationToIndex(e.getPoint());
			if (index > -1)
			{
				
				key = ((JDBUser) getModel().getElementAt(index).getObject()).getUserId();

				usr.setUserId(key);
				if (usr.getUserProperties() == true)
				{
					tooltip = "";
					if (usr.isAccountEnabled() == false)
					{
						tooltip = lang.get("lbl_Account_Disabled");
					}
					if (usr.getAccountLocked().equals("Y") == true)
					{
						tooltip = lang.get("lbl_Account_Locked");
					}
					if (usr.isAccountExpired() == true)
					{
						tooltip = lang.get("lbl_Account_Expired") + " " + usr.getAccountExpiryDate();
					}
					if (tooltip.equals("") == true)
					{
						if (usr.getLastLoginTimestamp() != null)
						{
							tooltip = "Last logon " + usr.getLastLoginTimestamp();
						}
						else
						{
							tooltip = "Account never used";
						}
					}
				}
				else
				{
					tooltip = key;
				}

				return tooltip;
			}
			else
			{
				return null;
			}
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}
