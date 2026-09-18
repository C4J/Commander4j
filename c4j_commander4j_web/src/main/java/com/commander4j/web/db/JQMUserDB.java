/* Copied AS-IS from c4j_web_WS (2026-09-12, step 8 port): only the package changed, the decorative @Entity annotation was dropped (jakarta.persistence is not in this war; serialisation is Gson) and the sample check uses core com.commander4j.db.JDBQMSample. */
package com.commander4j.web.db;

import com.commander4j.sys.Common;

import com.commander4j.web.entity.JQMUserEntity;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

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

	public boolean isValid(String userid)
	{
		boolean result = false;

		logger.debug("isValid :" + userid.toString());
		setErrorMessage("");

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMUsers.isValid")))
		{
			stmt.setString(1, userid);
			stmt.setFetchSize(1);

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					result = true;
				}
				else
				{
					setErrorMessage("Invalid User ID");
				}
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(JQMUserEntity user)
	{
		boolean result = false;
		userEntity = user;
		logger.debug("create :" + userEntity.toString());
		setErrorMessage("");

		try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMUsers.create")))
		{
			stmtupdate.setString(1, userEntity.getUserID());
			stmtupdate.setString(2, userEntity.getFirstName());
			stmtupdate.setString(3, userEntity.getSurname());
			stmtupdate.setString(4, userEntity.getEnabled());

			stmtupdate.execute();
			stmtupdate.clearParameters();

			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			result = true;
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean update(JQMUserEntity user)
	{
		boolean result = false;
		userEntity = user;
		logger.debug("update :" + userEntity.toString());
		setErrorMessage("");
		
		try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMUsers.update")))
		{
			stmtupdate.setString(1, userEntity.getFirstName());
			stmtupdate.setString(2, userEntity.getSurname());
			stmtupdate.setString(3, userEntity.getEnabled());
			stmtupdate.setString(4, userEntity.getUserID());

			stmtupdate.execute();
			stmtupdate.clearParameters();

			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			result = true;
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	
	public JQMUserEntity getProperties(String userid)
	{
		setErrorMessage("");
		JQMUserEntity result = new JQMUserEntity();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMUsers.getProperties")))
		{
			stmt.setFetchSize(1);
			stmt.setString(1, userid);

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					result.setUserID(rs.getString("user_id"));
					result.setFirstName(rs.getString("first_name"));
					result.setSurname(rs.getString("surname"));
					result.setEnabled(rs.getString("enabled"));
				} else
				{
					setErrorMessage("Unknown User ID [" + userid + "]");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			logger.error(e);
		}

		return result;
	}
	
	public LinkedList<JQMUserEntity> getUsersByEnabled(String enabled)
	{
		setErrorMessage("");
		LinkedList<JQMUserEntity> result = new LinkedList<JQMUserEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMUsers.getByEnabled")))
		{
			stmt.setFetchSize(1);
			stmt.setString(1, enabled);

			try (ResultSet rs = stmt.executeQuery())
			{
				while (rs.next())
				{
					JQMUserEntity tent = new JQMUserEntity();

					tent.setUserID(rs.getString("user_id"));
					tent.setFirstName(rs.getString("first_name"));
					tent.setSurname(rs.getString("surname"));
					tent.setEnabled(rs.getString("enabled"));
					result.addLast(tent);
				}
			}

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public LinkedList<JQMUserEntity> getUserByUserID(String userID)
	{
		setErrorMessage("");
		LinkedList<JQMUserEntity> result = new LinkedList<JQMUserEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMUsers.getByUserID")))
		{
			stmt.setFetchSize(1);
			stmt.setString(1, userID);

			try (ResultSet rs = stmt.executeQuery())
			{
				while (rs.next())
				{
					JQMUserEntity tent = new JQMUserEntity();

					tent.setUserID(rs.getString("user_id"));
					tent.setFirstName(rs.getString("first_name"));
					tent.setSurname(rs.getString("surname"));
					tent.setEnabled(rs.getString("enabled"));
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
