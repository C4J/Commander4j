/* Copied AS-IS from c4j_web_Issue (2026-09-11, step 7 port): only the package names changed (JQMResourcesDB: an unused JQMUserEntity field and a logger keyed on the uncopied JQMUserDB were dropped).
 * Dave's decision: move the existing issue/return logic; the transactional core rewrite is deferred. */
package com.commander4j.web.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.web.entity.JQMResourceEntity;
import com.commander4j.sys.Common;

public class JQMResourcesDB
{

	private String sessionID = "";
	private String hostID = "";
	private String dbErrorMessage;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMResourcesDB.class);

	public JQMResourcesDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
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
		setErrorMessage("");
		JQMResourceEntity result = new JQMResourceEntity();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.getProperties")))
		{
			stmt.setFetchSize(1);
			stmt.setString(1, resource);

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					result.setRequiredResource(rs.getString("required_resource"));
					result.setDescription(rs.getString("description"));
				} else
				{
					setErrorMessage("Unknown Resource [" + resource + "]");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			logger.error(e);
		}

		return result;
	}
	
	public LinkedList<JQMResourceEntity> getResources()
	{
		setErrorMessage("");
		LinkedList<JQMResourceEntity> result = new LinkedList<JQMResourceEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrderResource.getResources")))
		{
			stmt.setFetchSize(1);

			try (ResultSet rs = stmt.executeQuery())
			{
				while (rs.next())
				{
					JQMResourceEntity tent = new JQMResourceEntity();

					tent.setRequiredResource(rs.getString("required_resource"));
					tent.setDescription(rs.getString("description"));

					result.addLast(tent);
				}
			}

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
