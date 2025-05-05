package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import com.commander4j.entity.JQMPalletHistoryEntity;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JQMPalletHistoryDB
{
	private String sessionID = "";
	private String hostID = "";

	JDBPalletHistory ph;
	private String dbErrorMessage;

	public JQMPalletHistoryDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		
		ph = new JDBPalletHistory(getHostID(), getSessionID());
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
	
	private void setErrorMessage(String errorMsg)
	{
		dbErrorMessage = errorMsg;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}
	
	public long writePalletHistory_rest(JDBPallet pal,long txnRef, String transactionType, String transactionSubtye,String userid,String location_id)
	{
		long txn = txnRef;

		if (transactionType.length() > 0)
		{

			if (txn == 0)
			{
				txn = ph.generateNewTransactionRef();
			}

			ph.setTransactionRef(txn);
			ph.setTransactionType(transactionType);
			ph.setTransactionSubtype(transactionSubtye);
			ph.setTransactionDate(JUtility.getSQLDateTime());
			pal.setLocationID(location_id);
			ph.setPallet(pal);
			write_rest(ph,userid);
		}
		else
		{

		}

		return txn;
	}
	
	public boolean write_rest(JDBPalletHistory ph,String userid)
	{
		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletHistory.write"));
			
			stmtupdate.setLong(1, ph.getTransactionRef());
			stmtupdate.setString(2, ph.getTransactionType());
			stmtupdate.setString(3, ph.getTransactionSubtype());
			stmtupdate.setTimestamp(4, ph.getTransactionDate());
			stmtupdate.setString(5, ph.getPallet().getSSCC());
			stmtupdate.setString(6, ph.getPallet().getDespatchNo());
			stmtupdate.setString(7, ph.getPallet().getLocationID());
			stmtupdate.setString(8, ph.getPallet().getMaterial());
			stmtupdate.setString(9, ph.getPallet().getBatchNumber());
			stmtupdate.setString(10, ph.getPallet().getProcessOrder());
			stmtupdate.setBigDecimal(11, ph.getPallet().getQuantity());
			stmtupdate.setString(12, ph.getPallet().getUom());
			stmtupdate.setTimestamp(13, ph.getPallet().getDateOfManufacture());
			stmtupdate.setString(14, ph.getPallet().getStatus());
			stmtupdate.setString(15, ph.getPallet().getEAN());
			stmtupdate.setString(16, ph.getPallet().getVariant());
			stmtupdate.setString(17,userid);
			stmtupdate.setString(18, ph.getPallet().getConfirmed());
			stmtupdate.setInt(19, ph.getPallet().getLayersOnPallet());
			stmtupdate.setTimestamp(20, ph.getPallet().getBatchExpiry());
			stmtupdate.setString(21, ph.getPallet().getCustomerID());
			stmtupdate.setString(22, ph.getPallet().getMHNNumber());
			stmtupdate.setString(23, ph.getPallet().getDecision());
			stmtupdate.setString(24,ph.getPallet().getEquipmentType());
			
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			stmtupdate.close();
			result = true;
			
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public LinkedList<JQMPalletHistoryEntity> getPalletHistoryBySSCC(String status)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		LinkedList<JQMPalletHistoryEntity> result = new LinkedList<JQMPalletHistoryEntity>();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletHistory.selectWithSSCC"));
			stmt.setFetchSize(1);
			stmt.setString(1, status);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JQMPalletHistoryEntity tent = new JQMPalletHistoryEntity();
				
				tent.getPropertiesFromResultSet(rs);
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
	

}
