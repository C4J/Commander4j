package com.commander4j.c4jWS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.util.JUtility;

public class JQMTraySampleDB
{
	private String sessionID = "";
	private String hostID = "";
	private JQMTraySampleEntity traySampleEntity;
	private String dbErrorMessage;
	private Logger logger = Logger.getLogger(JQMTraySampleDB.class);
	private JDBQMSample sampleDB;

	public JQMTraySampleDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		sampleDB = new JDBQMSample(host,session);
	}

	public JQMTraySampleEntity getTraySampleEntity()
	{
		return traySampleEntity;
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

	public boolean isValid(Long trayid,Long sampleid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		logger.debug("isValid :" + trayid.toString()+ "," + sampleid);
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTraySamples.isValid"));
			stmt.setLong(1, trayid);
			stmt.setLong(2, sampleid);
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

	public boolean create(JQMTraySampleEntity traySample)
	{
		boolean result = false;
		traySampleEntity = traySample;
		logger.debug("create :" + traySampleEntity.toString());
		setErrorMessage("");
		
		if (sampleDB.isValidSample(traySample.getSampleID()))
		{

			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTraySamples.create"));
	
				stmtupdate.setLong(1, traySampleEntity.getTrayID());
				stmtupdate.setLong(2, traySampleEntity.getSampleID());
				stmtupdate.setLong(3, traySampleEntity.getSequenceID());
				traySampleEntity.setCreated(JUtility.getSQLDateTime());
				stmtupdate.setTimestamp(4, traySampleEntity.getCreated());
	
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
		}
		else
		{
			setErrorMessage("Invalid Sample ID : "+(traySample.getSampleID().toString()));
		}

		return result;
	}

	public boolean update(JQMTraySampleEntity traySample)
	{
		boolean result = false;
		traySampleEntity = traySample;
		logger.debug("update :" + traySampleEntity.toString());
		setErrorMessage("");
		
		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTraySamples.update"));


			stmtupdate.setLong(1, traySampleEntity.getSequenceID());
			traySampleEntity.setUpdated(JUtility.getSQLDateTime());
			stmtupdate.setTimestamp(2, traySampleEntity.getUpdated());
			stmtupdate.setLong(3, traySampleEntity.getTrayID());
			stmtupdate.setLong(4, traySampleEntity.getSampleID());

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
	
	public boolean delete(JQMTraySampleEntity traysample)
	{
		PreparedStatement stmtupdate;
		boolean result = false;

		logger.debug("delete :" + traysample.getTrayID().toString()+" , "+traysample.getSampleID().toString());
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTraySamples.delete"));
			stmtupdate.setLong(1, traysample.getTrayID());
			stmtupdate.setLong(2, traysample.getSampleID());
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
	
	
	public JQMTraySampleEntity getProperties(Long trayid,Long sampleId)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		JQMTraySampleEntity result = new JQMTraySampleEntity();	

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTraySamples.getProperties"));
			stmt.setFetchSize(1);
			stmt.setLong(1, trayid);
			stmt.setLong(2, sampleId);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result.setTrayID(rs.getLong("tray_id"));
				result.setSampleID(rs.getLong("sample_id"));
				result.setSequenceID(rs.getLong("sequence_id"));
				result.setCreated(rs.getTimestamp("created"));
				result.setUpdated(rs.getTimestamp("updated"));
				
				
			} else
			{
				setErrorMessage("Unknown Tray ID / Sample ID[" + trayid + "]");
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
	
	public Long getNewSequenceID(Long trayid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Long result = (long) 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTraySamples.maxSequenceID"));
			stmt.setFetchSize(1);
			stmt.setLong(1, trayid);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = rs.getLong("max_sequence_id");
				result++;
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

		return result;
	}
	
	public LinkedList<JQMTraySampleEntity> getSamplesByTray(Long trayid)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		LinkedList<JQMTraySampleEntity> result = new LinkedList<JQMTraySampleEntity>();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMTraySamples.getByTrayID"));
			stmt.setFetchSize(1);
			stmt.setLong(1, trayid);
			rs = stmt.executeQuery();

			
			while (rs.next())
			{
				JQMTraySampleEntity tent = new JQMTraySampleEntity();
				
				tent.setTrayID(rs.getLong("tray_id"));
				tent.setSequenceID(rs.getLong("sequence_id"));
				tent.setSampleID(rs.getLong("sample_id"));
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
