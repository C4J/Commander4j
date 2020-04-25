package com.commander4j.db;

import java.math.BigDecimal;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWasteLocations.java
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
import java.util.LinkedList;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBWasteLog class updates the table APP_WASTE_LOG. This table contains a
 * single row for each unique Waste Location as stored in the APP_WASTE_LOG
 * table.
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_LOG.jpg" >
 * 
 * @see com.commander4j.db.JDBWasteReasons JDBWasteReasons
 * @see com.commander4j.db.JDBWasteReportingIDS JDBWasteReportingIDS
 * @see com.commander4j.db.JDBWasteTransactionType JDBWasteTransactionType
 * @see com.commander4j.db.JDBWasteLocationReporting JDBWasteLocationReporting
 * @see com.commander4j.db.JDBWasteReasons JDBWasteTypes
 * @see com.commander4j.db.JDBWasteReasons JDBWasteMaterial
 * @see com.commander4j.db.JDBWasteReasons JDBWasteLog
 * 
 */

public class JDBWasteLog
{
	public static int field_TransactionTypeID_id = 25;
	public static int field_LocationID = 25;
	public static int field_MaterialID = 25;
	public static int field_ProcessOrder = 10;
	public static int field_ReasonID = 20;
	public static int field_UserID = 20;

	private String dbErrorMessage;
	private long dbTransactionRef; /* PK */
	private Timestamp dbWasteReportTime;
	private String dbTransactionType; /* PK */
	private String dbLocationID;
	private String dbMaterialID;
	private String dbProcessOrder;
	private String dbReasonID;
	private String dbUserID;
	private BigDecimal dbQuantity;
	private JDBWasteTransactionType wasteTransactionType;
	private JDBWasteLocation wasteLocation;
	private JDBWasteMaterial wasteMaterial;
	private JDBWasteTypes wasteTypes;
	private JDBWasteLocationTypes wasteLocationTypes;
	private JDBWasteReasons wasteReason;
	private JDBProcessOrder processOrder;

	private final Logger logger = Logger.getLogger(JDBWasteLog.class);
	private String hostID;
	private String sessionID;

	public JDBWasteLog(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

		wasteTransactionType = new JDBWasteTransactionType(getHostID(), getSessionID());
		wasteLocation = new JDBWasteLocation(getHostID(), getSessionID());
		wasteMaterial = new JDBWasteMaterial(getHostID(), getSessionID());
		wasteTypes = new JDBWasteTypes(getHostID(), getSessionID());
		wasteReason = new JDBWasteReasons(getHostID(), getSessionID());
		processOrder = new JDBProcessOrder(getHostID(), getSessionID());
		wasteLocationTypes = new JDBWasteLocationTypes(getHostID(), getSessionID());
	}

	public void clear()
	{
		setWasteReportTime(JUtility.getSQLDateTime());
		setLocationID("");
		setProcessOrder("");
		setReasonID("");
		setUserID("");
		setMaterialID("");
	}

	public boolean write()
	{
		boolean result = false;
		setErrorMessage("");

		if (isValidData() == true)
		{
			try
			{
				//Generate new number and save it 
				setTransactionRef(generateNewTransactionRef());

				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.write"));
				stmtupdate.setLong(1, getTransactionRef());
				stmtupdate.setString(2, getTransactionType());
				stmtupdate.setTimestamp(3, getWasteReportTime());
				stmtupdate.setString(4, getLocationID());
				stmtupdate.setString(5, getMaterialID());
				stmtupdate.setString(6, getProcessOrder());
				stmtupdate.setString(7, getReasonID());
				stmtupdate.setString(8, Common.userList.getUser(getSessionID()).getUserId());
				stmtupdate.setBigDecimal(9, getQuantity());
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

		return result;
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteLog() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.delete"));
				stmtupdate.setLong(1, getTransactionRef());
				stmtupdate.setString(2, getTransactionType());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public long getTransactionRef()
	{
		return dbTransactionRef;
	}

	public void setTransactionRef(long transactionRef)
	{
		dbTransactionRef = transactionRef;
	}

	public BigDecimal getQuantity()
	{
		return dbQuantity;
	}

	public void setQuantity(BigDecimal dbQuantity)
	{
		this.dbQuantity = dbQuantity;
	}

	public String getLocationID()
	{
		return JUtility.replaceNullStringwithBlank(dbLocationID).trim();
	}

	public String getMaterialID()
	{
		return JUtility.replaceNullStringwithBlank(dbMaterialID).trim();
	}

	public String getProcessOrder()
	{
		return JUtility.replaceNullStringwithBlank(dbProcessOrder).trim();
	}

	public String getReasonID()
	{
		return JUtility.replaceNullStringwithBlank(dbReasonID).trim();
	}

	public void setReasonID(String user)
	{
		dbReasonID = JUtility.replaceNullStringwithBlank(user).trim();
	}

	public void setUserID(String user)
	{
		dbUserID = JUtility.replaceNullStringwithBlank(user).trim();
	}

	public String getTransactionType()
	{
		return JUtility.replaceNullStringwithBlank(dbTransactionType);
	}

	public String getUserID()
	{
		return JUtility.replaceNullStringwithBlank(dbUserID);
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public ResultSet getWasteLogResultSet(PreparedStatement criteria)
	{
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

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();

			setTransactionRef(rs.getLong("transaction_ref"));
			setWasteReportTime(rs.getTimestamp("report_time"));
			setTransactionType(rs.getString("waste_transaction_type"));
			setLocationID(rs.getString("waste_location_id"));
			setMaterialID(rs.getString("waste_material_id"));
			setProcessOrder(rs.getString("process_order"));
			setReasonID(rs.getString("waste_reason_id"));
			setUserID(rs.getString("user_id"));
			setQuantity(rs.getBigDecimal("quantity"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public Timestamp getWasteReportTime()
	{
		return dbWasteReportTime;
	}

	public boolean getWasteLogProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.getProperties"));
			stmt.setLong(1, getTransactionRef());
			stmt.setString(2, getTransactionType());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Waste Log - Transaction Ref [" + getTransactionRef() + "] Type [" + getTransactionType() + "]");
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

	public boolean getWasteLogProperties(Long res, String type)
	{
		setTransactionRef(res);
		setTransactionType(type);
		return getWasteLogProperties();
	}

	public LinkedList<JDBWasteLog> getWasteLogs()
	{
		LinkedList<JDBWasteLog> wasteLogList = new LinkedList<JDBWasteLog>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.getWasteLogs"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteLog samp = new JDBWasteLog(getHostID(), getSessionID());
				samp.getPropertiesfromResultSet(rs);
				wasteLogList.add(samp);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteLogList;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public boolean isValidWasteLog()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.isValidWasteLog"));
			stmt.setFetchSize(1);
			stmt.setLong(1, getTransactionRef());
			stmt.setString(2, getTransactionType());
			
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Waste Log " + getTransactionRef() + " / " + getTransactionType() + " not found.");
			}
			stmt.close();
			rs.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidWasteLog(Long res)
	{
		setTransactionRef(res);

		return isValidWasteLog();
	}

	public void setLocationID(String str)
	{
		dbLocationID = str;
	}

	public void setMaterialID(String str)
	{
		dbMaterialID = str;
	}

	public void setProcessOrder(String str)
	{
		dbProcessOrder = str;
	}

	public void setTransactionType(String desc)
	{
		dbTransactionType = desc;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setWasteReportTime(Timestamp res)
	{
		dbWasteReportTime = res;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		return String.valueOf(getTransactionRef());
	}

	public boolean isValidData()
	{
		boolean result = false;
		boolean reasonReqd = false;
		boolean poReqd = false;
		boolean storeAsNegative = false;
		boolean isNegativeValue = false;

		if (Objects.isNull(dbTransactionRef) == false)
		{
			if (wasteTransactionType.getWasteTransactionProperties(getTransactionType()))
			{
				storeAsNegative = wasteTransactionType.isStoreAsNegative();
				if (wasteLocation.getWasteLocationProperties(getLocationID()))
				{
					reasonReqd = wasteLocation.isReasonIDRequired();
					poReqd = wasteLocation.isProcessOrderRequired();

					if (wasteMaterial.getWasteMaterialProperties(getMaterialID()))
					{
						if (wasteTypes.getWasteTypeProperties(wasteMaterial.getWasteTypeID()))
						{
							if (wasteLocationTypes.getWasteLocationTypeProperties(getLocationID(), wasteMaterial.getWasteTypeID()))
							{

								if ((reasonReqd == false) || ((reasonReqd == true) && wasteReason.getWasteReasonProperties(getReasonID())))
								{
									if ((poReqd == false) || ((poReqd == true) && processOrder.getProcessOrderProperties(getProcessOrder())))
									{
										isNegativeValue = (getQuantity().compareTo(new BigDecimal("0")) < 0);

										if (storeAsNegative == isNegativeValue)
										{
											if (getQuantity().compareTo(new BigDecimal("0")) == 0)
											{
												setErrorMessage("Quantity cannot be zero");
											}
											else
											{
												result = true;
												setErrorMessage("");
											}
										}
										else
										{
											if (storeAsNegative)
											{
												setErrorMessage("Quantity needs to be negative (-ve)");
											}
											else
											{
												setErrorMessage("Quantity needs to be positive (+ve)");
											}
										}
									}
									else
									{
										setErrorMessage(processOrder.getErrorMessage());
									}
								}
								else
								{
									setErrorMessage(wasteReason.getErrorMessage());
								}
							}
							else
							{
								setErrorMessage(wasteLocationTypes.getErrorMessage());
							}
						}
						else
						{
							setErrorMessage(wasteTypes.getErrorMessage());
						}
					}
					else
					{
						setErrorMessage(wasteMaterial.getErrorMessage());
					}
				}
				else
				{
					setErrorMessage(wasteLocation.getErrorMessage());
				}
			}
			else
			{
				setErrorMessage(wasteTransactionType.getErrorMessage());
			}
		}
		else
		{
			setErrorMessage("Transaction Ref must be defined.");
		}

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteLog() == true)
			{
				if (isValidData() == true)
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.update"));

					stmtupdate.setTimestamp(1, getWasteReportTime());
					stmtupdate.setString(2, getLocationID());
					stmtupdate.setString(3, getMaterialID());
					stmtupdate.setString(4, getProcessOrder());
					stmtupdate.setString(5, getReasonID());
					stmtupdate.setString(6, Common.userList.getUser(getSessionID()).getUserId());
					stmtupdate.setBigDecimal(7, getQuantity());
					stmtupdate.setLong(8, getTransactionRef());
					stmtupdate.setString(9, getTransactionType());

					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					result = true;
				}
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public long generateNewTransactionRef()
	{
		long result = 0;
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		long transaction_ref = 1;
		String transaction_ref_str = "1";

		boolean retry = true;

		if (ctrl.getProperties("WASTE TXN REF") == true)
		{
			do
			{
				if (ctrl.lockRecord("WASTE TXN REF") == true)
				{
					if (ctrl.getProperties("WASTE TXN REF") == true)
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

				}
			}
			while (retry);
		}

		result = transaction_ref;

		logger.debug("Waste Transaction Ref :" + result);
		return result;
	}

}
