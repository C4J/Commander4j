package com.commander4j.c4jWS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.util.JUtility;

public class JQMTrayDB
{

	private String sessionID = "";
	private String hostID = "";
	private JQMTrayEntity trayEntity;
	private String dbErrorMessage;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMTrayDB.class);
	JQMTraySampleDB traySampleDB;
	JQMTrayResultDB trayResultDB;

	public JQMTrayDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		traySampleDB = new JQMTraySampleDB(host, session);
		trayResultDB = new JQMTrayResultDB(host, session);
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

	public boolean isValid(Long trayIDorSeq,Long panelid,String queryType)
	{
		// queryType should be TrayID or TraySequence

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		logger.debug("isValid :" + trayIDorSeq.toString());
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.isValid"+queryType));
			stmt.setLong(1, panelid);
			stmt.setLong(2, trayIDorSeq);
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

			if (trayEntity.getTrayID()== -1)
			{
				trayEntity.setTrayID(getNewTrayID());
			}
			if (trayEntity.getTraySequence()== -1)
			{
				trayEntity.setTraySequence(getNextSequenceID(trayEntity.getPanelID()));
			}

			if (trayEntity.getDescription().equals(""))
			{
				trayEntity.setDescription("Tray "+trayEntity.getTraySequence());
			}

			trayEntity.setCreated( JUtility.getSQLDateTime());

			stmtupdate.setLong(1, trayEntity.getPanelID());
			stmtupdate.setLong(2, trayEntity.getTrayID());
			stmtupdate.setLong(3, trayEntity.getTraySequence());
			stmtupdate.setString(4, trayEntity.getDescription());
			stmtupdate.setTimestamp(5, trayEntity.getCreated());

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
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.updateByTrayID"));

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
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.deleteby"+tray.getqueryType()));

			stmtupdate.setLong(1, tray.getPanelID());
			stmtupdate.setLong(2, tray.getTrayID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;


			traySampleDB.deleteByTrayID(tray.getTrayID());
			trayResultDB.deleteByTrayID(tray.getTrayID());

		} catch (SQLException e)
		{
			logger.debug(e.getMessage());
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	public JQMTrayEntity getProperties(Long panelid,Long trayIDorSeq,String queryType)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		JQMTrayEntity result = new JQMTrayEntity();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.getProperties"+queryType));
			stmt.setFetchSize(1);
			stmt.setLong(1, panelid);
			stmt.setLong(2, trayIDorSeq);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result.setPanelID(rs.getLong("panel_id"));
				result.setTrayID(rs.getLong("tray_id"));
				result.setTraySequence(rs.getLong("tray_sequence"));
				result.setDescription(JUtility.replaceNullStringwithBlank(rs.getString("description")));
				result.setCreated(rs.getTimestamp("created"));
				result.setUpdated(rs.getTimestamp("updated"));
			} else
			{
				result.setTrayID((long) -1);
				setErrorMessage("Unknown Tray ID [" + trayIDorSeq + "]");
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
				tent.setTraySequence(rs.getLong("tray_sequence"));
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

	public Long getNextSequenceID(Long panelid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Long result = (long) 1;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.nextSequenceID"));
			stmt.setFetchSize(1);
			stmt.setLong(1, panelid);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = rs.getLong("next_sequence");
			} else
			{
				result = (long) 1;
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			logger.error(e);
			result = (long) -1;
		}

		System.out.println("***************");
		System.out.println("NEXT TRAY SEQ IS "+result+ "FOR PANEL "+panelid);

		return result;
	}

}
