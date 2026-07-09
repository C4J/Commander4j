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
	private JQMTrayDB trayDB;


	public JQMPanelDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		trayDB = new JQMTrayDB(host,session);

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
		boolean result = false;

		logger.debug("isValid :" + panelid.toString());
		setErrorMessage("");

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.isValid")))
		{
			stmt.setLong(1, panelid);
			stmt.setFetchSize(1);

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					result = true;
				}
				else
				{
					setErrorMessage("Invalid Panel ID");
				}
			}
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

		try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.create")))
		{
			panelEntity.setPanelID(getNewPanelID());

			if (panelEntity.getPanelID() <= 0)
			{
				// getNewPanelID() failed to allocate (error message already set).
				// Skip the insert and report failure rather than writing id 0.
				if (getErrorMessage() == null || getErrorMessage().equals(""))
				{
					setErrorMessage("Could not allocate a new Panel ID");
				}
				return false;
			}

			stmtupdate.setLong(1, panelEntity.getPanelID());
			stmtupdate.setTimestamp(2, panelEntity.getPanelDate());

			if (panelEntity.getDescription().equals(""))
			{
				panelEntity.setDescription("Daily Panel");
			}

			stmtupdate.setString(3, panelEntity.getDescription());

			if (panelEntity.getPlant().equals(""))
			{
				panelEntity.setPlant("Pouch");
			}

			stmtupdate.setString(4, panelEntity.getPlant());
			stmtupdate.setString(5, panelEntity.getStatus());
			stmtupdate.setTimestamp(6, JUtility.getSQLDateTime());

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

	public boolean update(JQMPanelEntity panel)
	{
		boolean result = false;
		panelEntity = panel;
		logger.debug("update :" + panelEntity.toString());
		setErrorMessage("");

		try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.update")))
		{
			stmtupdate.setTimestamp(1, panelEntity.getPanelDate());
			stmtupdate.setString(2, panelEntity.getDescription());
			stmtupdate.setString(3, panelEntity.getPlant());
			stmtupdate.setString(4, panelEntity.getStatus());
			stmtupdate.setTimestamp(5, JUtility.getSQLDateTime());
			stmtupdate.setLong(6, panelEntity.getPanelID());

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

	public long getNewPanelID()
	{
		long result = -1;
		long new_tray_id = 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		String transaction_ref_str = "1";

		boolean success = false;
		int counter = 0;
		final int maxAttempts = 3;

		ctrl.getKeyValueWithDefault("PANEL ID", "0", "Unique Panel Sequence");

		do
		{
			counter++;
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
						success = true;
					}
				}
			}
		}
		while ((success == false) && (counter < maxAttempts));

		if (success)
		{
			result = new_tray_id;
			logger.debug("New Panel ID :" + result);
		}
		else
		{
			// Could not allocate an ID. Roll back to release any row lock taken
			// by lockRecord so it does not linger on the per-session connection,
			// and return -1 so create() skips the insert instead of writing id 0.
			try
			{
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).rollback();
			}
			catch (java.sql.SQLException e)
			{
				logger.error("getNewPanelID rollback failed : " + e.getMessage());
			}
			setErrorMessage("Could not allocate a new Panel ID (lock timeout)");
			logger.error("Could not allocate a new Panel ID after " + counter + " attempts");
		}

		return result;
	}

	public boolean delete(JQMPanelEntity panel)
	{
		boolean result = false;

		logger.debug("delete :" + panel.getPanelID().toString());
		setErrorMessage("");

		try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.delete")))
		{
			stmtupdate.setLong(1, panel.getPanelID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			result = true;

			//Get a list of Trays assigned to Panel
			LinkedList<JQMTrayEntity> trayList = trayDB.getTraysByPanel(panel.getPanelID());

			JQMTrayEntity trayEntity = new JQMTrayEntity();


			for (int x=0;x<trayList.size();x++)
			{
				//Get the Tray Data
				trayEntity = trayList.get(x);

				//Delete the Tray itself.
				trayEntity.setqueryType("TrayID");

				trayDB.delete(trayEntity);
			}


		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}




	public JQMPanelEntity getProperties(Long panelid)
	{
		setErrorMessage("");
		JQMPanelEntity result = new JQMPanelEntity();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.getProperties")))
		{
			stmt.setFetchSize(1);
			stmt.setLong(1, panelid);

			try (ResultSet rs = stmt.executeQuery())
			{
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
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			logger.error(e);
		}

		return result;
	}

	public LinkedList<JQMPanelEntity> getPanelsByStatus(String status)
	{
		setErrorMessage("");
		LinkedList<JQMPanelEntity> result = new LinkedList<JQMPanelEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.getByStatus")))
		{
			stmt.setFetchSize(1);
			stmt.setString(1, status);

			try (ResultSet rs = stmt.executeQuery())
			{
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
			}

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public LinkedList<JQMPanelEntity> getPanelsListLimit(Long maxrows)
	{
		setErrorMessage("");
		LinkedList<JQMPanelEntity> result = new LinkedList<JQMPanelEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMPanels.getListLimit")))
		{
			stmt.setFetchSize(15);
			stmt.setLong(1, maxrows);

			try (ResultSet rs = stmt.executeQuery())
			{
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
			}

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
