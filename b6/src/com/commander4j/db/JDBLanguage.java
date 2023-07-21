package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBLanguage.java
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * JDBLanguage is used to insert/update/delete records from the SYS_LANGUAGE
 * table. The table text for all buttons, labels within the application in
 * multiple languages.
 * <p>
 * <img alt="" src="./doc-files/SYS_LANGUAGE.jpg" >
 */
public class JDBLanguage
{
	private String hostID;
	private String sessionID;
	private String dbErrorMessage;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBLanguage.class);
	private String dbKey;
	private String dbLanguage;
	private String dbText;
	private String dbMnemonic;
	public static int field_text = 150;
	public static int field_language = 2;
	public static int field_resource_key = 50;
	public static int field_mnemonic = 1;

	public JDBLanguage(String host, String session)
	{
		super();
		setHostID(host);
		setSessionID(session);
	}

	private void clear()
	{
		setKey("");
		setLanguage("");
		setText("");
		setMnemonic("");
	}

	public boolean create(String key, String lang, String text, String mnemonic)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setKey(key);
			setLanguage(lang);
			setText(text);
			setMnemonic(mnemonic);

			if (isValidKey() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLanguage.create"));
				stmtupdate.setString(1, getKey());
				stmtupdate.setString(2, getLanguage());
				stmtupdate.setString(3, getText());
				stmtupdate.setString(4, getMnemonicString());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Key already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidKey() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLanguage.delete"));
				stmtupdate.setString(1, getKey());
				stmtupdate.setString(2, getLanguage());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public String get(String key)
	{
		String result = "";
		String language = JUtility.replaceNullStringwithBlank(Common.userList.getUser(getSessionID()).getLanguage());
		try
		{
			result = get(key, language);
		} catch (Exception ex)
		{
			result = key;
		}
		return result;
	}

	public String get(String key, String language)
	{

		if (language.equals(""))
		{
			language = "EN";
		}

		if (Common.translation_Text.containsKey(getHostID() + " " + key + " " + language))
		{
			setText(Common.translation_Text.get(getHostID() + " " + key + " " + language));
			setMnemonic(Common.translation_Mnemonic.get(getHostID() + " " + key + " " + language));
		} else
		{
			if (getProperties(getHostID() + " " + key, language) == false)
			{
				setText(getHostID() + " " + key);
				setMnemonic("");
			}
			Common.translation_Text.put(getHostID() + " " + key + " " + language, getText());
			Common.translation_Mnemonic.put(getHostID() + " " + key + " " + language, getMnemonicString());
		}
		return JUtility.replaceNullStringwithBlank(getText());
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getKey()
	{
		return dbKey;
	}

	public String getLanguage()
	{
		return dbLanguage;
	}

	public ResultSet getLanguageDataResultSet(PreparedStatement criteria)
	{

		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();

		} catch (Exception e)
		{
			rs = null;
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public char getMnemonicChar()
	{
		char result;
		try
		{
			result = dbMnemonic.charAt(0);
		} catch (Exception e)
		{
			result = Integer.toString(0).charAt(0);
		}
		return result;
	}

	public String getMnemonicString()
	{
		return dbMnemonic;
	}

	public boolean getProperties(String key, String language)
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		// logger.debug("getText for ["+key+"]");

		clear();
		setKey(key);
		setLanguage(language);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLanguage.getText"));
			stmt.setFetchSize(1);
			stmt.setString(1, getKey());
			stmt.setString(2, getLanguage());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Despatch No");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();

			setKey(rs.getString("resource_key"));
			setLanguage(rs.getString("language_id"));
			setText(rs.getString("text"));
			setMnemonic(rs.getString("mnemonic"));

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getText()
	{
		return dbText;
	}

	public boolean isValidKey()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLanguage.isValidKey"));
			stmt.setString(1, getKey());
			stmt.setString(2, getLanguage());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Key [" + getKey() + "]");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public void preLoad(String mask)
	{
		String language = JUtility.replaceNullStringwithBlank(Common.userList.getUser(getSessionID()).getLanguage());
		int counter = 0;

		if (language.equals(""))
		{
			logger.debug("preLoad using default language of EN");
			language = "EN";
		}

		if (Common.translation_Text.containsKey(getHostID() + " <loaded> " + getLanguage()) == false)
		{

			PreparedStatement stmt = null;
			LinkedList<String> result = new LinkedList<String>();
			ResultSet rs;

			result.clear();
			try
			{
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLanguage.preLoad"));
				stmt.setFetchSize(250);
				stmt.setString(1, mask);
				stmt.setString(2, language);

				rs = stmt.executeQuery();

				while (rs.next())
				{
					getPropertiesfromResultSet(rs);
					if (Common.translation_Text.containsKey(getHostID() + " " + getKey() + " " + getLanguage()) == false)
					{
						Common.translation_Text.put(getHostID() + " " + getKey() + " " + getLanguage(), getText());
						Common.translation_Mnemonic.put(getHostID() + " " + getKey() + " " + getLanguage(), getMnemonicString());

						counter++;
					}

				}
				rs.close();

				stmt.close();

				Common.translation_Text.put(getHostID() + " <loaded> " + getLanguage(), "true");
				Common.translation_Mnemonic.put(getHostID() + " <loaded> " + getLanguage(), "true");

				logger.debug(String.valueOf(counter) + " messages loaded.");

			} catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		} else
		{
			logger.debug("Message preload request for host " + getHostID() + " language " + getLanguage() + " ignored.");
		}

	}

	private void setErrorMessage(String ErrorMsg)
	{

		dbErrorMessage = ErrorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setKey(String key)
	{
		dbKey = key;
	}

	public void setLanguage(String language)
	{
		dbLanguage = language;
	}

	public void setMnemonic(String mnemonic)
	{
		dbMnemonic = mnemonic;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setText(String text)
	{
		dbText = text;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidKey() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBLanguage.update"));
				stmtupdate.setString(1, getText());
				stmtupdate.setString(2, getMnemonicString());
				stmtupdate.setString(3, getKey());
				stmtupdate.setString(4, getLanguage());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
