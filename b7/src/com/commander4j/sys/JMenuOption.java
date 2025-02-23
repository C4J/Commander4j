
package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JMenuOption.java
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

import java.sql.ResultSet;

import com.commander4j.db.JDBLanguage;

public class JMenuOption
{
	public String menuID;
	public String moduleID;
	public int sequenceID;
	public String description;
	public String wrapped_description;
	public String moduleType;
	public String desktop;
	public String scanner;
	public String hint;
	public char mnemonic;
	public String iconFilename;
	private String hostID;
	private String sessionID;
	private JDBLanguage lang;

	// public JMenuOption()
	// {
	// clear();
	// }

	public JMenuOption(String host, String session)
	{
		super();
		setHostID(host);
		setSessionID(session);
		lang = new JDBLanguage(getHostID(), getSessionID());
	}

	public void clear() {
		menuID = "";
		moduleID = "";
		sequenceID = 0;
		description = "";
		wrapped_description = "";
		moduleType = "";
		desktop = "";
		scanner = "";
		hint = "";
		mnemonic = 0;
		iconFilename = "";
	}

	private String getHostID() {
		return hostID;
	}

	private String getSessionID() {
		return sessionID;
	}

	public void load(ResultSet rs) {
		try
		{
			moduleID = rs.getString("module_id");
			try
			{
				menuID = rs.getString("menu_id");
			}
			catch (Exception ex)
			{
			}
			description = lang.get(rs.getString("resource_key"));

			wrapped_description = "";
			String[] words = description.split(" ");
			String word="";
			for (int wrd=0;wrd<words.length;wrd++)
			{
				if (wrd < 2)
				{
					word = words[wrd];
					if (word.length()>10)
					{
						word = word.substring(0, 8)+".";
					}
					wrapped_description = wrapped_description + word +" ";
				}

			}
			wrapped_description = "<html><center>"  + wrapped_description.trim() + "</center></html>";
			
			mnemonic = lang.getMnemonicChar();
			moduleType = rs.getString("module_type");
			sequenceID = rs.getInt("sequence_id");
			desktop = rs.getString("dk_active");
			scanner = rs.getString("rf_active");
			hint = description;
			iconFilename = rs.getString("icon_filename");
		}
		catch (Exception ex)
		{

		}
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	public String toString() {
		return this.description;
	}
}
