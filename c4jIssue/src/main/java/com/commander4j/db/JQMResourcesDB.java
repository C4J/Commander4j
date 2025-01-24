package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.entity.JQMResourceEntity;
import com.commander4j.entity.JQMUserEntity;
import com.commander4j.sys.Common;

public class JQMResourcesDB
{

	private String sessionID = "";
	private String hostID = "";
	private JQMUserEntity userEntity;
	private String dbErrorMessage;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMUserDB.class);

	public JQMResourcesDB(String host, String session)
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
	

	public JQMResourceEntity getProperties(String resource)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		JQMResourceEntity result = new JQMResourceEntity();	

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, resource);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result.setRequiredResource(rs.getString("required_resource"));
				result.setDescription(rs.getString("description"));
			} else
			{
				setErrorMessage("Unknown Resource [" + resource + "]");
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
	
	public LinkedList<JQMResourceEntity> getResources()
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		LinkedList<JQMResourceEntity> result = new LinkedList<JQMResourceEntity>();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.getResources"));
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JQMResourceEntity tent = new JQMResourceEntity();
				
				tent.setRequiredResource(rs.getString("required_resource"));
				tent.setDescription(rs.getString("description"));

				result.addLast(tent);
			}
			rs.close();
			stmt.close();
			
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
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
