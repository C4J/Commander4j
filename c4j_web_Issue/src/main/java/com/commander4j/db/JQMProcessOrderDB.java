package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.entity.JQMProcessOrderEntity;
import com.commander4j.sys.Common;

public class JQMProcessOrderDB
{

	private String sessionID = "";
	private String hostID = "";
	private JQMProcessOrderEntity processOrderEntity;
	private String dbErrorMessage;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMProcessOrderDB.class);

	public JQMProcessOrderDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}
	
	public JQMProcessOrderEntity getProcessOrderEntity()
	{
		return processOrderEntity;
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

	public boolean isValid(String processOrder)
	{
		boolean result = false;

		logger.debug("isValid :" + processOrder.toString());
		setErrorMessage("");

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrder.isValidProcessOrder")))
		{
			stmt.setString(1, processOrder);
			stmt.setFetchSize(1);

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					result = true;
				}
				else
				{
					setErrorMessage("Invalid Process Order");
				}
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	
	public JQMProcessOrderEntity getProperties(String processOrder)
	{
		setErrorMessage("");
		JQMProcessOrderEntity result = new JQMProcessOrderEntity();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMProcessOrder.getProperties")))
		{
			stmt.setFetchSize(1);
			stmt.setString(1, processOrder);

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					result.setUserProcessOrderID(rs.getString("process_order"));
					result.setMaterial(rs.getString("material"));
					result.setDescription(rs.getString("description"));
					result.setStatus(rs.getString("status"));
					result.setBomID(rs.getString("recipe_id"));
					result.setBomVersion(rs.getString("recipe_version"));
				} else
				{
					setErrorMessage("Unknown Process Order [" + processOrder + "]");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			logger.error(e);
		}

		return result;
	}
	
	public LinkedList<JQMProcessOrderEntity> getProcessOrdersByStatus(String status)
	{
		setErrorMessage("");
		LinkedList<JQMProcessOrderEntity> result = new LinkedList<JQMProcessOrderEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrder.selectByStatus")))
		{
			stmt.setFetchSize(1);
			stmt.setString(1, status);

			try (ResultSet rs = stmt.executeQuery())
			{
				while (rs.next())
				{
					JQMProcessOrderEntity tent = new JQMProcessOrderEntity();

					tent.setUserProcessOrderID(rs.getString("process_order"));
					tent.setMaterial(rs.getString("material"));
					tent.setDescription(rs.getString("description"));
					tent.setStatus(rs.getString("status"));
					tent.setBomID(rs.getString("recipe_id"));
					tent.setBomVersion(rs.getString("recipe_version"));
					result.addLast(tent);

				}
			}

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public LinkedList<JQMProcessOrderEntity> getProcessOrdersByStatusByResource(String status,String resource)
	{
		setErrorMessage("");
		LinkedList<JQMProcessOrderEntity> result = new LinkedList<JQMProcessOrderEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrder.selectByStatusByResource")))
		{
			stmt.setFetchSize(1);
			stmt.setString(1, status);
			stmt.setString(2, resource);

			try (ResultSet rs = stmt.executeQuery())
			{
				while (rs.next())
				{
					JQMProcessOrderEntity tent = new JQMProcessOrderEntity();

					tent.setUserProcessOrderID(rs.getString("process_order"));
					tent.setMaterial(rs.getString("material"));
					tent.setDescription(rs.getString("description"));
					tent.setStatus(rs.getString("status"));
					tent.setBomID(rs.getString("recipe_id"));
					tent.setBomVersion(rs.getString("recipe_version"));
					result.addLast(tent);

				}
			}

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public LinkedList<JQMProcessOrderEntity> getProcessOrderByID(String order)
	{
		setErrorMessage("");
		LinkedList<JQMProcessOrderEntity> result = new LinkedList<JQMProcessOrderEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrder.getProcessOrderProperties")))
		{
			stmt.setFetchSize(1);
			stmt.setString(1, order);

			try (ResultSet rs = stmt.executeQuery())
			{
				while (rs.next())
				{
					JQMProcessOrderEntity tent = new JQMProcessOrderEntity();

					tent.setUserProcessOrderID(rs.getString("process_order"));
					tent.setMaterial(rs.getString("material"));
					tent.setDescription(rs.getString("description"));
					tent.setStatus(rs.getString("status"));
					tent.setBomID(rs.getString("recipe_id"));
					tent.setBomVersion(rs.getString("recipe_version"));
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
