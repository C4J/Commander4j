package com.commander4j.db;

import java.awt.event.MouseEvent;

import javax.swing.JList;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 */
public class JDBModuleJList extends JList<JDBListData>
{

	private static final long serialVersionUID = 1;
	private String key;
	private String tooltip;
	private JDBModule mod;
	private String hostID;
	private String sessionID;

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

	public JDBModuleJList()
	{
		setFont(Common.font_list);
		setBackground(Common.color_listBackground);
	}

	public JDBModuleJList(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		mod = new JDBModule(getHostID(), getSessionID());
		setFont(Common.font_list);
		setBackground(Common.color_listBackground);
	}

	public String getToolTipText(MouseEvent e) {

		int index = locationToIndex(e.getPoint());
		if (index > -1)
		{
			try
			{
				key = ((JDBListData) getModel().getElementAt(index)).toString();

				mod.setModuleId(key);
				if (mod.getModuleProperties() == true)
				{
					tooltip = mod.getType() + " : " + JUtility.replaceNullStringwithBlank(mod.getDescription());
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
