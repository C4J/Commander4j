package com.commander4j.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.commander4j.entity.JQMPalletEntity;
import com.commander4j.messages.OutgoingPalletIssue;
import com.commander4j.messages.OutgoingPalletReturn;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JQMPalletDB
{
	private String sessionID = "";
	private String hostID = "";
	private JQMPalletEntity palletEntity;
	private JDBPallet palletDB;
	private String dbErrorMessage;

	public JQMPalletDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

		palletDB = new JDBPallet(getHostID(), getSessionID());
	}

	public JQMPalletEntity getPalletEntity()
	{
		return palletEntity;
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

	public boolean isValid(String sscc)
	{
		boolean result = false;

		result = palletDB.getPalletProperties(sscc);

		if (result)
		{
			setErrorMessage("");
		}
		else
		{
			setErrorMessage(palletDB.getErrorMessage());
		}

		return result;
	}

	public JQMPalletEntity getProperties(String sscc)
	{

		JQMPalletEntity result = new JQMPalletEntity();

		if (palletDB.getPalletProperties(sscc))
		{
			result.setSSCC(palletDB.getSSCC());
			result.setMaterial(palletDB.getMaterial());
			result.setProcessOrder(palletDB.getProcessOrder());
			result.setPalletStatus(palletDB.getStatus());
			result.setBatchStatus(palletDB.getMaterialBatchStatus());
			result.setBomId(palletDB.getProcessOrderObj(true).getRecipe());
			result.setBomVersion(palletDB.getProcessOrderObj(false).getRecipeVersion());
			result.setConfirmed(palletDB.getConfirmed());
			result.setUom(palletDB.getUom());
			result.setQuantity(palletDB.getQuantity());
			result.setBatchNumber(palletDB.getBatchNumber());
			result.setOldMaterial(palletDB.getMaterialObj().getOldMaterial());
			result.setLocationId(palletDB.getLocationID());
			result.setDescription(palletDB.getMaterialObj().getDescription());

			setErrorMessage("");
		}
		else
		{
			setErrorMessage(palletDB.getErrorMessage());
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

	public Long issueToOrder_rest(String sscc, String toOrder, BigDecimal quantity, String barcode_id, String userId)
	{
		Long result = (long) 0;
		JDBPallet paldb = new JDBPallet(getHostID(), getSessionID());
		JQMPalletHistoryDB phdb = new JQMPalletHistoryDB(getHostID(), getSessionID());

		paldb.getPalletProperties(sscc);

		try
		{
			if ((paldb.getQuantity().compareTo(quantity) >= 0) && (quantity.compareTo(new BigDecimal(0)) > 0))
			{

				String location_id = "";

				if (barcode_id.equals(""))
				{
					location_id = paldb.getLocationID();
				}
				else
				{
					JDBLocation locdb = new JDBLocation(getHostID(), getSessionID());
					location_id = locdb.getLocationIDfromBarcodeID(barcode_id);
				}

				if (location_id.equals(""))
				{
					location_id = paldb.getLocationID();
				}

				Long txn = (long) 0;

				PreparedStatement stmtupdate;

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPallet.Issue"));
				stmtupdate.setBigDecimal(1, paldb.getQuantity().subtract(quantity));
				stmtupdate.setString(2, paldb.getUpdatedBy());
				stmtupdate.setTimestamp(3, JUtility.getSQLDateTime());
				stmtupdate.setString(4, paldb.getLocationID());
				stmtupdate.setString(5, paldb.getSSCC());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();

				paldb.setProcessOrder(toOrder);
				paldb.setQuantity(quantity);
				paldb.setLocationID(location_id);

				txn = phdb.writePalletHistory_rest(paldb, txn, "ISSUE", "TO", userId, location_id);

				if (txn > 0)
				{

					if (paldb.getLocationObj().isPalletIssueMessageRequired() == true)
					{
						OutgoingPalletIssue opi = new OutgoingPalletIssue(getHostID(), getSessionID());
						opi.submit(txn);
					}

				}
				result = txn;
			}
			else
			{
				setErrorMessage("No update required.");
			}

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public Long returnFromOrder_rest(String sscc, String fromOrder, BigDecimal quantity, String barcode_id, String userId)
	{
		Long result = (long) 0;

		JDBPallet paldb = new JDBPallet(getHostID(), getSessionID());
		JQMPalletHistoryDB phdb = new JQMPalletHistoryDB(getHostID(), getSessionID());

		paldb.getPalletProperties(sscc);

		try
		{
			if ((quantity.compareTo(new BigDecimal(0)) > 0))
			{

				String location_id = "";

				if (barcode_id.equals(""))
				{
					location_id = paldb.getLocationID();
				}
				else
				{
					JDBLocation locdb = new JDBLocation(getHostID(), getSessionID());
					location_id = locdb.getLocationIDfromBarcodeID(barcode_id);
				}

				if (location_id.equals(""))
				{
					location_id = paldb.getLocationID();
				}

				Long txn = (long) 0;

				PreparedStatement stmtupdate;

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPallet.Return"));
				stmtupdate.setBigDecimal(1, paldb.getQuantity().add(quantity));
				stmtupdate.setString(2, paldb.getUpdatedBy());
				stmtupdate.setTimestamp(3, JUtility.getSQLDateTime());
				stmtupdate.setString(4, paldb.getLocationID());
				stmtupdate.setString(5, paldb.getSSCC());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();

				paldb.setProcessOrder(fromOrder);
				paldb.setQuantity(quantity);
				paldb.setLocationID(location_id);

				txn = phdb.writePalletHistory_rest(paldb, txn, "RETURN", "FROM", userId, location_id);

				if (txn > 0)
				{

					if (paldb.getLocationObj().isPalletReturnMessageRequired() == true)
					{
						OutgoingPalletReturn opr = new OutgoingPalletReturn(getHostID(), getSessionID());
						opr.submit(txn);
					}

				}
				result = txn;
			}
			else
			{
				setErrorMessage("No update required.");
			}

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
