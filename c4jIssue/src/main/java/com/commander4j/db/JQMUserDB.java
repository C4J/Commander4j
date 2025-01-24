package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;

import com.commander4j.entity.JQMUserEntity;
import com.commander4j.sys.Common;

public class JQMUserDB
{

	private String sessionID = "";
	private String hostID = "";
	private JQMUserEntity userEntity;
	private String dbErrorMessage;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMUserDB.class);

	public JQMUserDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}
	
	public JQMUserEntity getUserEntity()
	{
		return userEntity;
	}
	
	private String getSessionID()
	{
		return sessionID;
	}

	private String getHostID()
	{
		return hostID;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}
	

	public JQMUserEntity getProperties(String userid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		JQMUserEntity result = new JQMUserEntity();	

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMUsers.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, userid);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result.setUserID(rs.getString("user_id"));
				result.setUserPassword(rs.getString("password"));
			} else
			{
				setErrorMessage("Unknown User ID [" + userid + "]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			logger.error(e);
		}

		return result;
	}
	
	
	private void setErrorMessage(String errorMsg)
	{
		dbErrorMessage = errorMsg;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

}
