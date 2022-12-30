package com.commander4j.c4jWS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.util.JUtility;

public class JQMTrayResultDB
{
	private String sessionID = "";
	private String hostID = "";
	private JQMTrayResultEntity trayResultEntity;
	private String dbErrorMessage;
	private Logger logger = Logger.getLogger(JQMTrayResultDB.class);

	public JQMTrayResultDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JQMTrayResultEntity getTrayEntity()
	{
		return trayResultEntity;
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

	public boolean isValid(Long trayid, Long sampleid, String userid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		logger.debug("isValid :" + trayid.toString() + "," + sampleid);
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrayResults.isValid"));
			stmt.setLong(1, trayid);
			stmt.setLong(2, sampleid);
			stmt.setString(3, userid);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Tray/Sample ID");
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

	public boolean create(JQMTrayResultEntity trayResult)
	{
		boolean result = false;
		trayResultEntity = trayResult;
		logger.debug("create :" + trayResultEntity.toString());
		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrayResults.create"));

			stmtupdate.setLong(1, trayResultEntity.getTrayID());
			stmtupdate.setLong(2, trayResultEntity.getSampleID());
			stmtupdate.setString(3, trayResultEntity.getUserID());
			stmtupdate.setString(4, trayResultEntity.getTestID());
			stmtupdate.setString(5, trayResultEntity.getValue());
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

	public boolean creates(JQMTrayResultEntity[] trayResult)
	{
		boolean result = true;
		int qty = trayResult.length;

		for (int x = 0; x < qty; x++)
		{

			trayResultEntity = trayResult[x];
			logger.debug("create :" + trayResultEntity.toString());
			setErrorMessage("");

			if (create(trayResultEntity)==false)
			{
				update(trayResultEntity);
			}

		}

		return result;
	}

	public boolean update(JQMTrayResultEntity trayResult)
	{
		boolean result = false;
		trayResultEntity = trayResult;
		logger.debug("update :" + trayResultEntity.toString());
		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrayResults.update"));

			stmtupdate.setString(1, trayResultEntity.getTestID());
			stmtupdate.setString(2, trayResultEntity.getValue());
			stmtupdate.setTimestamp(3, JUtility.getSQLDateTime());
			stmtupdate.setLong(4, trayResultEntity.getTrayID());
			stmtupdate.setLong(5, trayResultEntity.getSampleID());
			stmtupdate.setString(6, trayResultEntity.getUserID());

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

	public boolean delete(JQMTrayResultEntity trayResult)
	{
		PreparedStatement stmtupdate;
		boolean result = false;

		logger.debug("delete :" + trayResult.getTrayID().toString() + " , " + trayResult.getSampleID().toString());
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrayResults.delete"));
			stmtupdate.setLong(1, trayResult.getTrayID());
			stmtupdate.setLong(2, trayResult.getSampleID());
			stmtupdate.setString(3, trayResult.getUserID());
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

	public JQMTrayResultEntity getProperties(Long trayid, Long sampleId, String userId)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		JQMTrayResultEntity result = new JQMTrayResultEntity();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrayResults.getProperties"));
			stmt.setFetchSize(1);
			stmt.setLong(1, trayid);
			stmt.setLong(2, sampleId);
			stmt.setString(3, userId);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result.setTrayID(rs.getLong("tray_id"));
				result.setSampleID(rs.getLong("sample_id"));
				result.setUserID(rs.getString("user_id"));
				result.setTestID(rs.getString("test_id"));
				result.setValue(rs.getString("value"));
				result.setCreated(rs.getTimestamp("created"));
				result.setUpdated(rs.getTimestamp("updated"));
			}
			else
			{
				setErrorMessage("Unknown Result ID [" + trayid + "]");
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			logger.error(e);
		}

		return result;
	}

	public LinkedList<JQMTrayResultEntity> getResultsByTrayUser(Long trayid, String userid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		LinkedList<JQMTrayResultEntity> result = new LinkedList<JQMTrayResultEntity>();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTrayResults.getByTrayID"));
			stmt.setFetchSize(1);
			stmt.setString(1, userid);
			stmt.setString(2, "PANEL");
			stmt.setString(3, userid);
			stmt.setString(4, "PANEL");
			stmt.setLong(5, trayid);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JQMTrayResultEntity tent = new JQMTrayResultEntity();

				tent.setTrayID(rs.getLong("tray_id"));
				tent.setSampleID(rs.getLong("sample_id"));
				tent.setSequenceID(rs.getLong("sequence_id"));
				tent.setUserID(JUtility.replaceNullStringwithBlank(rs.getString("user_id")));
				tent.setTestID(JUtility.replaceNullStringwithBlank(rs.getString("test_id")));
				tent.setValue(JUtility.replaceNullStringwithBlank(rs.getString("value")));
				tent.setCreated(rs.getTimestamp("created"));
				tent.setUpdated(rs.getTimestamp("updated"));
				result.addLast(tent);
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

	private void setErrorMessage(String errorMsg)
	{
		dbErrorMessage = errorMsg;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

}
