package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBPalletHistory.java
 * 
 * Package Name : com.commander4j.db
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBPalletHistory
{
	private long dbTransactionRef;
	private String dbTransactionType;
	private String dbTransactionSubtype;
	private Timestamp dbTransactionDate;
	private JDBPallet dbPallet;
	private String dbErrorMessage;
	private String dbUserId;
	private final Logger logger = Logger.getLogger(JDBPalletHistory.class);
	private String hostID;
	private String sessionID;
	private long transaction_ref = 1;

	public long getTransaction_ref() {
		return transaction_ref;
	}

	public JDBPalletHistory(long transactionRef, String transactionType, String transactionSubtype, Timestamp transactionDate, JDBPallet pallet, String userId)
	{
		clear();
		setTransactionRef(transactionRef);
		setTransactionType(transactionType);
		setTransactionSubtype(transactionSubtype);
		setTransactionDate(transactionDate);
		setUserID(userId);
		setPallet(pallet);
	}

	public ResultSet getInterfacingData(Long transactionRef, String transactionType, String transactionSubtype, Long maxRecords, String sortBy, String ascDesc) {

		ResultSet rs = null;
		String temp = "";

		JDBQuery query = new JDBQuery(getHostID(), getSessionID());
		query.clear();

		temp = Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletHistory.selectWithExpiry");

		query.addText(temp);

		query.addParamtoSQL("transaction_ref = ", String.valueOf(transactionRef));

		query.addParamtoSQL("transaction_type=", transactionType);

		query.addParamtoSQL("transaction_subtype=", transactionSubtype);

		query.appendSort(sortBy, ascDesc);
		
		if (maxRecords > 0)
		{
			query.applyRestriction(true, Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), maxRecords);
		}
		else
		{
			query.applyRestriction(false, "none", 0);
		}
		query.bindParams();

		try
		{
			rs = query.getPreparedStatement().executeQuery();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public JDBPalletHistory(ResultSet rs)
	{
		getPropertiesfromResultSet(rs);
	}

	public JDBPalletHistory(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		dbPallet = new JDBPallet(getHostID(), getSessionID());
	}

	public void clear() {
		dbTransactionRef = Long.valueOf("0");
		dbTransactionType = "";
		dbTransactionSubtype = "";
		dbTransactionDate = null;
		dbUserId = "";
		try
		{
			dbPallet.clear();
		}
		catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public long generateNewTransactionRef() {
		long result = 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		String transaction_ref_str = "1";

		boolean retry = true;
		@SuppressWarnings("unused")
		int counter = 0;

		if (ctrl.getProperties("TRANSACTION REF") == true)
		{
			do
			{
				if (ctrl.lockRecord("TRANSACTION REF") == true)
				{
					if (ctrl.getProperties("TRANSACTION REF") == true)
					{
						transaction_ref_str = ctrl.getKeyValue();
						transaction_ref = Long.parseLong(transaction_ref_str);
						transaction_ref++;
						temp = String.valueOf(transaction_ref);
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
		}

		result = transaction_ref;

		logger.debug("Transaction Ref :" + result);
		return result;
	}

	public String getErrorMessage() {
		return dbErrorMessage;
	}

	private String getHostID() {
		return hostID;
	}

	public JDBPallet getPallet() {
		return dbPallet;
	}

	public ResultSet getPalletHistoryDataResultSet(PreparedStatement criteria) {

		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();

		}
		catch (Exception e)
		{
			rs = null;
			setErrorMessage(e.getMessage());
		}
		return rs;
	}

	public void getPropertiesfromResultSet(ResultSet rs) {
		try
		{
			clear();

			setTransactionRef(rs.getLong("transaction_ref"));
			setTransactionType(rs.getString("transaction_type"));
			setTransactionSubtype(rs.getString("transaction_subtype"));
			setTransactionDate(rs.getTimestamp("transaction_date"));
			setUserID(JUtility.replaceNullStringwithBlank(rs.getString("user_id")));

			getPallet().getPropertiesfromResultSet(rs);

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	private String getSessionID() {
		return sessionID;
	}

	public Timestamp getTransactionDate() {
		return dbTransactionDate;
	}

	public long getTransactionRef() {
		return dbTransactionRef;
	}

	public String getTransactionSubtype() {
		return dbTransactionSubtype;
	}

	public String getTransactionType() {
		return dbTransactionType;
	}

	public String getUserID() {
		return dbUserId;
	}

	private void setErrorMessage(String ErrorMsg) {
		ErrorMsg = JUtility.replaceNullStringwithBlank(ErrorMsg);
		if (ErrorMsg.isEmpty() == false)
		{
			logger.error(ErrorMsg);
		}
		dbErrorMessage = ErrorMsg;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	public void setPallet(JDBPallet pallet) {
		dbPallet = pallet;
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		dbTransactionDate = transactionDate;
	}

	public void setTransactionRef(long transactionRef) {
		dbTransactionRef = transactionRef;
	}

	public void setTransactionSubtype(String transactionSubtype) {
		dbTransactionSubtype = transactionSubtype;
	}

	/**
	 * Method setTransactionType.
	 * 
	 * @param transactionType
	 *            String
	 */
	public void setTransactionType(String transactionType) {
		dbTransactionType = transactionType;
	}

	/**
	 * Method setUserID.
	 * 
	 * @param userid
	 *            String
	 */
	public void setUserID(String userid) {
		dbUserId = userid;
	}

	/**
	 * Method write.
	 * 
	 * @return boolean
	 */
	public boolean write() {
		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletHistory.write"));
			stmtupdate.setLong(1, getTransactionRef());
			stmtupdate.setString(2, getTransactionType());
			stmtupdate.setString(3, getTransactionSubtype());
			stmtupdate.setTimestamp(4, getTransactionDate());
			stmtupdate.setString(5, getPallet().getSSCC());
			stmtupdate.setString(6, getPallet().getDespatchNo());
			stmtupdate.setString(7, getPallet().getLocationID());
			stmtupdate.setString(8, getPallet().getMaterial());
			stmtupdate.setString(9, getPallet().getBatchNumber());
			stmtupdate.setString(10, getPallet().getProcessOrder());
			stmtupdate.setBigDecimal(11, getPallet().getQuantity());
			stmtupdate.setString(12, getPallet().getUom());
			stmtupdate.setTimestamp(13, getPallet().getDateOfManufacture());
			stmtupdate.setString(14, getPallet().getStatus());
			stmtupdate.setString(15, getPallet().getEAN());
			stmtupdate.setString(16, getPallet().getVariant());
			stmtupdate.setString(17, Common.userList.getUser(getSessionID()).getUserId());
			// stmtupdate.setString(17,
			// Common.userList.getUser(Common.sessionID).getUserId());
			stmtupdate.setString(18, getPallet().getConfirmed());
			stmtupdate.setInt(19, getPallet().getLayersOnPallet());
			stmtupdate.setTimestamp(20, getPallet().getBatchExpiry());
			stmtupdate.setString(21, getPallet().getCustomerID());
			stmtupdate.setString(22, getPallet().getMHNNumber());
			stmtupdate.setString(23, getPallet().getDecision());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
