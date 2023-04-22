package com.commander4j.c4jWS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.util.JUtility;

public class JQMTrayDB
{

	private String sessionID = "";
	private String hostID = "";
	private JQMTrayEntity trayEntity;
	private String dbErrorMessage;
	private Logger logger = Logger.getLogger(JQMTrayDB.class);

	public JQMTrayDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}
	
	public JQMTrayEntity getTrayEntity()
	{
		return trayEntity;
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

	public boolean isValid(Long trayid,Long panelid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		logger.debug("isValid :" + trayid.toString());
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.isValid"));
			stmt.setLong(1, panelid);
			stmt.setLong(2, trayid);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Panel/Tray ID");
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

	public boolean create(JQMTrayEntity tray)
	{
		boolean result = false;
		trayEntity = tray;
		logger.debug("create :" + trayEntity.toString());
		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.create"));

			trayEntity.setTrayID(getNewTrayID());
			trayEntity.setCreated( JUtility.getSQLDateTime());
			
			stmtupdate.setLong(1, trayEntity.getPanelID());
			stmtupdate.setLong(2, trayEntity.getTrayID());
			stmtupdate.setString(3, trayEntity.getDescription());
			stmtupdate.setTimestamp(4, trayEntity.getCreated());

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

	public boolean update(JQMTrayEntity tray)
	{
		boolean result = false;
		trayEntity = tray;
		logger.debug("update :" + trayEntity.toString());
		setErrorMessage("");
		
		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.update"));

			trayEntity.setUpdated(JUtility.getSQLDateTime());
			stmtupdate.setString(1, trayEntity.getDescription());
			stmtupdate.setTimestamp(2,trayEntity.getUpdated());
			stmtupdate.setLong(3, trayEntity.getPanelID());
			stmtupdate.setLong(4, trayEntity.getTrayID());


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

	public long getNewTrayID()
	{
		long result = 0;
		long new_tray_id = 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		String transaction_ref_str = "1";

		boolean retry = true;
		@SuppressWarnings("unused")
		int counter = 0;

		ctrl.getKeyValueWithDefault("TRAY ID", "0", "Unique Tray Sequence");

		do
		{
			if (ctrl.lockRecord("TRAY ID") == true)
			{
				if (ctrl.getProperties("TRAY ID") == true)
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

		logger.debug("New Tray ID :" + result);
		return result;
	}
	
	public boolean delete(JQMTrayEntity tray)
	{
		PreparedStatement stmtupdate;
		boolean result = false;

		logger.debug("delete : trayID=" + tray.getTrayID().toString()+" panelID="+ tray.getPanelID().toString());
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.delete"));
			
			stmtupdate.setLong(1, tray.getPanelID());
			stmtupdate.setLong(2, tray.getTrayID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		} catch (SQLException e)
		{
			logger.debug(e.getMessage());
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	
	public JQMTrayEntity getProperties(Long panelid,Long trayid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		JQMTrayEntity result = new JQMTrayEntity();	

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.getProperties"));
			stmt.setFetchSize(1);
			stmt.setLong(1, panelid);
			stmt.setLong(2, trayid);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result.setPanelID(rs.getLong("panel_id"));
				result.setTrayID(rs.getLong("tray_id"));
				result.setDescription(JUtility.replaceNullStringwithBlank(rs.getString("description")));
				result.setCreated(rs.getTimestamp("created"));
				result.setUpdated(rs.getTimestamp("updated"));
			} else
			{
				setErrorMessage("Unknown Tray ID [" + trayid + "]");
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
	
	public LinkedList<JQMTrayEntity> getTraysByPanel(Long panel)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		LinkedList<JQMTrayEntity> result = new LinkedList<JQMTrayEntity>();
		int seq = 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.getByPanel"));
			stmt.setFetchSize(1);
			stmt.setLong(1, panel);
			rs = stmt.executeQuery();

			
			while (rs.next())
			{
				JQMTrayEntity tent = new JQMTrayEntity();
				
				tent.setPanelID(rs.getLong("panel_id"));
				tent.setTrayID(rs.getLong("tray_id"));
				seq++;
				tent.setTraySequence(seq);
				tent.setDescription(JUtility.replaceNullStringwithBlank(rs.getString("description")));
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

	private void setErrorMessage(String errorMsg)
	{
		dbErrorMessage = errorMsg;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

}
