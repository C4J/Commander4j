package com.commander4j.db;

import java.awt.Color;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBModuleJList.java
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
import com.commander4j.util.JUtility;


public class JDBWasteReportingIDSJList extends JList<JDBListData>
{

	private static final long serialVersionUID = 1;
	private String key;
	private String tooltip;
	private JDBWasteReportingIDS mod;
	private String hostID;
	private String sessionID;
	private Color backgroundColor = Common.color_list_background;

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
	
	public void setBackground(Color c)
	{
		backgroundColor = c;
	}

	public JDBWasteReportingIDSJList()
	{
		setFont(Common.font_list);
		setBackground(Common.color_list_background);
	}

	public JDBWasteReportingIDSJList(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		mod = new JDBWasteReportingIDS(getHostID(), getSessionID());
		setFont(Common.font_list);
		setBackground(backgroundColor);
	}

	public String getToolTipText(MouseEvent e) {

		int index = locationToIndex(e.getPoint());
		if (index > -1)
		{
			try
			{
				key = ((JDBListData) getModel().getElementAt(index)).toString();

				mod.setWasteReportingID(key);
				if (mod.getReportingIDProperties() == true)
				{
					tooltip = JUtility.replaceNullStringwithBlank(mod.getDescription());
				}
				else
				{
					tooltip = key;
				}
			}
			catch (Exception ex)
			{
				return null;
			}
			return tooltip;
		}
		else
		{
			return null;
		}
	}

}
