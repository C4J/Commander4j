/* Copied AS-IS from c4j_web_WS (2026-09-12, step 8 port): only the package changed, the decorative @Entity annotation was dropped (jakarta.persistence is not in this war; serialisation is Gson) and the sample check uses core com.commander4j.db.JDBQMSample. */
package com.commander4j.web.db;

import com.commander4j.web.entity.JQMTrayEntity;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBControl;
import com.commander4j.sys.Common;
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

		boolean result = false;

		logger.debug("isValid :" + trayIDorSeq.toString());
		setErrorMessage("");

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.isValid"+queryType)))
		{
			stmt.setLong(1, panelid);
			stmt.setLong(2, trayIDorSeq);
			stmt.setFetchSize(1);

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					result = true;
				}
				else
				{
					setErrorMessage("Invalid Panel/Tray ID");
				}
			}
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

		try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.create")))
		{
			if (trayEntity.getTrayID()== -1)
			{
				trayEntity.setTrayID(getNewTrayID());
			}

			if (trayEntity.getTrayID() <= 0)
			{
				// getNewTrayID() failed to allocate (error message already set).
				// Skip the insert and report failure rather than writing id 0.
				if (getErrorMessage() == null || getErrorMessage().equals(""))
				{
					setErrorMessage("Could not allocate a new Tray ID");
				}
				return false;
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

		try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.updateByTrayID")))
		{
			trayEntity.setUpdated(JUtility.getSQLDateTime());
			stmtupdate.setString(1, trayEntity.getDescription());
			stmtupdate.setTimestamp(2,trayEntity.getUpdated());
			stmtupdate.setLong(3, trayEntity.getPanelID());
			stmtupdate.setLong(4, trayEntity.getTrayID());


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

	public long getNewTrayID()
	{
		long result = -1;
		long new_tray_id = 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		String transaction_ref_str = "1";

		boolean success = false;
		int counter = 0;
		final int maxAttempts = 3;

		ctrl.getKeyValueWithDefault("TRAY ID", "0", "Unique Tray Sequence");

		do
		{
			counter++;
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
						success = true;
					}
				}
			}
		}
		while ((success == false) && (counter < maxAttempts));

		if (success)
		{
			result = new_tray_id;
			logger.debug("New Tray ID :" + result);
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
				logger.error("getNewTrayID rollback failed : " + e.getMessage());
			}
			setErrorMessage("Could not allocate a new Tray ID (lock timeout)");
			logger.error("Could not allocate a new Tray ID after " + counter + " attempts");
		}

		return result;
	}

	public boolean delete(JQMTrayEntity tray)
	{
		boolean result = false;

		logger.debug("delete : trayID=" + tray.getTrayID().toString()+" panelID="+ tray.getPanelID().toString());
		setErrorMessage("");

		try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.deleteby"+tray.getqueryType())))
		{
			stmtupdate.setLong(1, tray.getPanelID());
			stmtupdate.setLong(2, tray.getTrayID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
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
		setErrorMessage("");
		JQMTrayEntity result = new JQMTrayEntity();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.getProperties"+queryType)))
		{
			stmt.setFetchSize(1);
			stmt.setLong(1, panelid);
			stmt.setLong(2, trayIDorSeq);

			try (ResultSet rs = stmt.executeQuery())
			{
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
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			logger.error(e);
		}

		return result;
	}

	public LinkedList<JQMTrayEntity> getTraysByPanel(Long panel)
	{
		setErrorMessage("");
		LinkedList<JQMTrayEntity> result = new LinkedList<JQMTrayEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.getByPanel")))
		{
			stmt.setFetchSize(1);
			stmt.setLong(1, panel);

			try (ResultSet rs = stmt.executeQuery())
			{
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

	public Long getNextSequenceID(Long panelid)
	{
		setErrorMessage("");
		Long result = (long) 1;

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrays.nextSequenceID")))
		{
			stmt.setFetchSize(1);
			stmt.setLong(1, panelid);

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					result = rs.getLong("next_sequence");
				} else
				{
					result = (long) 1;
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			logger.error(e);
			result = (long) -1;
		}

		logger.debug("Next Tray Sequence is " + result + " for Panel " + panelid);

		return result;
	}

}
