/*
 * Created on 05-Apr-2005
 *
 */
package com.commander4j.html;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.sys.JMenuOption;

/**
 * @author David
 * 
 */
public class JMenuRFMenu
{

	final Logger logger = Logger.getLogger(JMenuRFMenu.class);
	private String hostID;
	private String sessionID;
	private int checkedIndex;
	private String CRLF = "\n";

	public JMenuRFMenu(String host, String session)
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
	
	public int getCheckedIndex() {
		return checkedIndex;
	}

	public String getCheckedIndexString() {
		return String.valueOf(checkedIndex);
	}	

	public String buildMenu(String defaultItem) {

		String result = "";
		String selected = "";
		
		checkedIndex = 0;
		
		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JMenuRFMenu.buildMenu"));
			stmt.setString(1, Common.userList.getUser(getSessionID()).getUserId());
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();
			JMenuOption menuOption = new JMenuOption(getHostID(), getSessionID());
			int x = 0;
			while (rs.next())
			{

				menuOption.load(rs);
				
				if (defaultItem.equals("")==true)
				{
					defaultItem = menuOption.moduleID;
				}
				
				if (menuOption.moduleID.equals(defaultItem))
				{
					selected = "checked=\"checked\"";
					checkedIndex = x;
				}
				else
				{
					selected = "";
				}				
				
				logger.debug(Common.userList.getUser(getSessionID()).getUserId() + " " + menuOption.description);
				result = result + "<label><input type=\"radio\" name=\"selectedMenuOption\" value=\"" + menuOption.moduleID + "\" " + selected + "/>" + menuOption.description + "</label><br/>"+CRLF;
				x++;
			}
		}
		catch (Exception ex)
		{
			logger.debug("Error in JMenuRFMenu " + ex.getMessage());
		}
		return result;

	}

}
