package com.commander4j.c4jWS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.util.JUtility;

public class JQMPanelDB
{

	private String sessionID = "";
	private String hostID = "";
	private JQMPanelEntity panelEntity;
	private String dbErrorMessage;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMPanelDB.class);

	public JQMPanelDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}
	
	public JQMPanelEntity getPanelEntity()
	{
		return panelEntity;
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

	public boolean isValid(Long panelid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		logger.debug("isValid :" + panelid.toString());
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.isValid"));
			stmt.setLong(1, panelid);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Panel ID");
			}

			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(JQMPanelEntity panel)
	{
		boolean result = false;
		panelEntity = panel;
		logger.debug("create :" + panelEntity.toString());
		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.create"));

			panelEntity.setPanelID(getNewPanelID());
			stmtupdate.setLong(1, panelEntity.getPanelID());
			stmtupdate.setTimestamp(2, panelEntity.getPanelDate());
			stmtupdate.setString(3, panelEntity.getDescription());
			stmtupdate.setString(4, panelEntity.getPlant());
			stmtupdate.setString(5, panelEntity.getStatus());
			stmtupdate.setTimestamp(6, JUtility.getSQLDateTime());

			stmtupdate.execute();
			stmtupdate.clearParameters();

			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean update(JQMPanelEntity panel)
	{
		boolean result = false;
		panelEntity = panel;
		logger.debug("update :" + panelEntity.toString());
		setErrorMessage("");
		
		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.update"));

			stmtupdate.setTimestamp(1, panelEntity.getPanelDate());
			stmtupdate.setString(2, panelEntity.getDescription());
			stmtupdate.setString(3, panelEntity.getPlant());
			stmtupdate.setString(4, panelEntity.getStatus());
			stmtupdate.setTimestamp(5, JUtility.getSQLDateTime());
			stmtupdate.setLong(6, panelEntity.getPanelID());

			stmtupdate.execute();
			stmtupdate.clearParameters();

			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public long getNewPanelID()
	{
		long result = 0;
		long new_tray_id = 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		String transaction_ref_str = "1";

		boolean retry = true;
		@SuppressWarnings("unused")
		int counter = 0;

		ctrl.getKeyValueWithDefault("PANEL ID", "0", "Unique Panel Sequence");

		do
		{
			if (ctrl.lockRecord("PANEL ID") == true)
			{
				if (ctrl.getProperties("PANEL ID") == true)
				{
					transaction_ref_str = ctrl.getKeyValue();
					new_tray_id = Long.parseLong(transaction_ref_str);
					new_tray_id++;
					temp = String.valueOf(new_tray_id);
					ctrl.setKeyValue(temp);

					if (ctrl.update())
					{
						retry = false;
					}
				}
			}
			else
			{
				retry = true;
				counter++;
			}
		}
		while (retry);

		result = new_tray_id;

		logger.debug("New Panel ID :" + result);
		return result;
	}
	
	public boolean delete(JQMPanelEntity panel)
	{
		PreparedStatement stmtupdate;
		boolean result = false;

		logger.debug("delete :" + panel.getPanelID().toString());
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.delete"));
			stmtupdate.setLong(1, panel.getPanelID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	
	public JQMPanelEntity getProperties(Long panelid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		JQMPanelEntity result = new JQMPanelEntity();	

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.getProperties"));
			stmt.setFetchSize(1);
			stmt.setLong(1, panelid);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result.setPanelID(rs.getLong("panel_id"));
				result.setPanelDate(rs.getTimestamp("panel_date"));
				result.setDescription(JUtility.replaceNullStringwithBlank(rs.getString("description")));
				result.setPlant(JUtility.replaceNullStringwithBlank(rs.getString("plant")));
				result.setStatus(JUtility.replaceNullStringwithBlank(rs.getString("status")));
				result.setCreated(rs.getTimestamp("created"));
				result.setUpdated(rs.getTimestamp("updated"));
			} else
			{
				setErrorMessage("Unknown Panel ID [" + panelid + "]");
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
	
	public LinkedList<JQMPanelEntity> getPanelsByStatus(String status)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		LinkedList<JQMPanelEntity> result = new LinkedList<JQMPanelEntity>();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.getByStatus"));
			stmt.setFetchSize(1);
			stmt.setString(1, status);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JQMPanelEntity tent = new JQMPanelEntity();
				
				tent.setPanelID(rs.getLong("panel_id"));
				tent.setPanelDate(rs.getTimestamp("panel_date"));
				tent.setDescription(JUtility.replaceNullStringwithBlank(rs.getString("description")));
				tent.setPlant(JUtility.replaceNullStringwithBlank(rs.getString("plant")));
				tent.setStatus(JUtility.replaceNullStringwithBlank(rs.getString("status")));
				tent.setCreated(rs.getTimestamp("created"));
				tent.setUpdated(rs.getTimestamp("updated"));
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
	
	public LinkedList<JQMPanelEntity> getPanelsListLimit(Long maxrows)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		LinkedList<JQMPanelEntity> result = new LinkedList<JQMPanelEntity>();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.getListLimit"));
			stmt.setFetchSize(15);
			stmt.setLong(1, maxrows);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JQMPanelEntity tent = new JQMPanelEntity();
				
				tent.setPanelID(rs.getLong("panel_id"));
				tent.setPanelDate(rs.getTimestamp("panel_date"));
				tent.setDescription(JUtility.replaceNullStringwithBlank(rs.getString("description")));
				tent.setPlant(JUtility.replaceNullStringwithBlank(rs.getString("plant")));
				tent.setStatus(JUtility.replaceNullStringwithBlank(rs.getString("status")));
				tent.setCreated(rs.getTimestamp("created"));
				tent.setUpdated(rs.getTimestamp("updated"));
				result.addLast(tent);

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
