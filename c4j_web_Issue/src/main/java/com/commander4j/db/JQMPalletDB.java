package com.commander4j.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;

import com.commander4j.entity.JQMPalletEntity;
import com.commander4j.entity.JQMPalletHistoryEntity;
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

	// Bridge restapi id for the error condition behind a failed transaction (""
	// = none).
	// The controller reads this and fires the relay pulse - never called from
	// this class.
	private String bridgePulseId = "";

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

	public String getBridgePulseId()
	{
		return bridgePulseId;
	}

	public Long issueToOrder_rest(String sscc, String toOrder, BigDecimal quantity, String barcode_id, String userId)
	{
		Long result = (long) 0;
		bridgePulseId = "";
		JDBPallet paldb = new JDBPallet(getHostID(), getSessionID());
		JQMPalletHistoryDB phdb = new JQMPalletHistoryDB(getHostID(), getSessionID());

		if (paldb.getPalletProperties(sscc))
		{

			try
			{
				if ((paldb.getQuantity().compareTo(quantity) >= 0) && (quantity.compareTo(new BigDecimal(0)) > 0))
				{

					// Location the pallet is leaving, captured before any
					// mutation of paldb.
					String fromLocation = paldb.getLocationID();

					// Location the pallet is being issued to (the scanned
					// lane).
					String toLocation = "";

					if (barcode_id.equals(""))
					{
						toLocation = paldb.getLocationID();
					}
					else
					{
						JDBLocation locdb = new JDBLocation(getHostID(), getSessionID());
						toLocation = locdb.getLocationIDfromBarcodeID(barcode_id);
					}

					if (toLocation.equals(""))
					{
						toLocation = paldb.getLocationID();
					}

					// Block the issue if the pallet / batch status is not
					// permitted at the
					// destination lane, before any self-committing mutation
					// takes place.
					// The error message already says which status blocked it;
					// the bridge only
					// needs the one INVALID_STATUS flag for both conditions
					// (user decision).
					if (paldb.isStatusValidForLocation(toLocation) == false)
					{
						setErrorMessage(paldb.getErrorMessage());
						bridgePulseId = "INVALID_STATUS";
						return result;
					}

					BigDecimal originalQty = paldb.getQuantity();
					Long txn = (long) 0;

					// Reduce the remaining quantity and MOVE the pallet to the
					// selected lane.
					try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPallet.Issue")))
					{
						BigDecimal newQuantity = originalQty.subtract(quantity);
						stmtupdate.setBigDecimal(1, newQuantity);
						stmtupdate.setString(2, paldb.getUpdatedBy());
						stmtupdate.setTimestamp(3, JUtility.getSQLDateTime());

						if (newQuantity.compareTo(BigDecimal.ZERO) == 0)
						{
							stmtupdate.setString(4, toLocation);
						}
						else
						{
							stmtupdate.setString(4, fromLocation);
						}
						stmtupdate.setString(5, paldb.getSSCC());
						stmtupdate.execute();
						stmtupdate.clearParameters();
						Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					}

					// Both history rows carry the issued quantity. The FROM row
					// keeps the
					// pallet's creation order (still in paldb from
					// getPalletProperties);
					// the TO row carries the order being issued to — so a
					// history search
					// by either order finds the transaction.
					paldb.setQuantity(quantity);

					// ISSUE / FROM = the location the pallet has just left,
					// creation order.
					txn = phdb.writePalletHistory_rest(paldb, txn, "ISSUE", "FROM", userId, fromLocation);

					// ISSUE / TO = the selected lane, destination order (shares
					// the same
					// transaction ref).
					if (txn > 0)
					{
						paldb.setProcessOrder(toOrder);
						txn = phdb.writePalletHistory_rest(paldb, txn, "ISSUE", "TO", userId, toLocation);
					}

					if (txn > 0)
					{

						if (paldb.getLocationObj().isPalletIssueMessageRequired() == true)
						{
							OutgoingPalletIssue opi = new OutgoingPalletIssue(getHostID(), getSessionID());
							opi.submit(txn);
						}

					}
					else
					{
						setErrorMessage(phdb.getErrorMessage());
					}
					result = txn;
				}
				else
				{
					// Say which way the quantity was wrong - the old "No update
					// required."
					// message gave the operator nothing to correct.
					if (quantity.compareTo(new BigDecimal(0)) <= 0)
					{
						setErrorMessage("Issue quantity must be greater than zero");
					}
					else
					{
						setErrorMessage("Issue quantity [" + quantity.toPlainString() + "] exceeds pallet quantity [" + paldb.getQuantity().toPlainString() + "]");
					}
					bridgePulseId = "INVALID_QUANTITY";
				}

			}
			catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
			setErrorMessage(paldb.getErrorMessage());
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

				// The lane the pallet is being returned from (its current
				// location),
				// captured before any mutation of paldb.
				String fromLocation = paldb.getLocationID();

				// Whether a return message is required is governed by the lane
				// (current
				// location), so capture it before the history writes move
				// paldb's location.
				boolean returnMessageRequired = paldb.getLocationObj().isPalletReturnMessageRequired();

				LinkedList<JQMPalletHistoryEntity> history = phdb.getPalletHistoryBySSCC(sscc);

				// Destination = the location the pallet occupied before its
				// most recent
				// issue, read back from the pallet history (the latest ISSUE /
				// FROM row).
				String destination = findMostRecentIssueFromLocation(history);

				if (destination.equals(""))
				{
					setErrorMessage("Cannot return pallet [" + sscc + "] - no prior issue location found in history.");
					return result;
				}

				// A return may not exceed what is still out in production: the
				// net of
				// every quantity issued minus every quantity already returned.
				BigDecimal availableToReturn = netIssuedQuantity(history);

				if (quantity.compareTo(availableToReturn) > 0)
				{
					setErrorMessage("Return quantity [" + quantity.toPlainString() + "] exceeds quantity available to return [" + availableToReturn.toPlainString() + "]");
					return result;
				}

				// Block the return if the pallet / batch status is not
				// permitted at the
				// destination (pre-issue) location, before any self-committing
				// mutation.
				if (paldb.isStatusValidForLocation(destination) == false)
				{
					setErrorMessage(paldb.getErrorMessage());
					return result;
				}

				BigDecimal originalQty = paldb.getQuantity();
				Long txn = (long) 0;

				// Restore the quantity and MOVE the pallet back to its
				// pre-issue location.
				try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPallet.Return")))
				{
					stmtupdate.setBigDecimal(1, originalQty.add(quantity));
					stmtupdate.setString(2, paldb.getUpdatedBy());
					stmtupdate.setTimestamp(3, JUtility.getSQLDateTime());
					stmtupdate.setString(4, destination);
					stmtupdate.setString(5, paldb.getSSCC());
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				}

				// Mirror of the issue stamping: the FROM row carries the order
				// being
				// returned from, the TO row the pallet's creation order (still
				// in paldb
				// from getPalletProperties).
				String creationOrder = paldb.getProcessOrder();
				paldb.setProcessOrder(fromOrder);
				paldb.setQuantity(quantity);

				// RETURN / FROM = the lane the pallet has just left, order
				// returned from.
				txn = phdb.writePalletHistory_rest(paldb, txn, "RETURN", "FROM", userId, fromLocation);

				// RETURN / TO = the pre-issue location, creation order (shares
				// the same
				// transaction ref).
				if (txn > 0)
				{
					paldb.setProcessOrder(creationOrder);
					txn = phdb.writePalletHistory_rest(paldb, txn, "RETURN", "TO", userId, destination);
				}

				if (txn > 0)
				{

					if (returnMessageRequired == true)
					{
						OutgoingPalletReturn opr = new OutgoingPalletReturn(getHostID(), getSessionID());
						opr.submit(txn);
					}

				}
				else
				{
					setErrorMessage(phdb.getErrorMessage());
				}
				result = txn;
			}
			else
			{
				setErrorMessage("Return quantity must be greater than zero");
			}

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	/**
	 * Net quantity currently out in production for an SSCC: the sum of every
	 * quantity issued minus the sum of every quantity returned, from the pallet
	 * history. Issue and return transactions each write a FROM and a TO row
	 * sharing one transaction ref and quantity, so each ref/type pair is
	 * counted once (which also tolerates any legacy single-row transactions).
	 */
	private BigDecimal netIssuedQuantity(LinkedList<JQMPalletHistoryEntity> history)
	{
		BigDecimal net = new BigDecimal(0);
		HashSet<String> counted = new HashSet<String>();

		for (JQMPalletHistoryEntity row : history)
		{
			String type = row.getTransactionType();

			if ("ISSUE".equals(type) || "RETURN".equals(type))
			{
				if (counted.add(row.getTransactionRef() + "/" + type))
				{
					BigDecimal qty = row.getQuantity() == null ? new BigDecimal(0) : row.getQuantity();

					if ("ISSUE".equals(type))
					{
						net = net.add(qty);
					}
					else
					{
						net = net.subtract(qty);
					}
				}
			}
		}

		return net;
	}

	/**
	 * Return the location_id from the most recent ISSUE / FROM pallet history
	 * row for the given SSCC, or an empty string when none exists. The history
	 * is returned ordered by transaction_ref descending, so the first matching
	 * row is the latest issue.
	 */
	private String findMostRecentIssueFromLocation(LinkedList<JQMPalletHistoryEntity> history)
	{
		for (JQMPalletHistoryEntity row : history)
		{
			if ("ISSUE".equals(row.getTransactionType()) && "FROM".equals(row.getTransactionSubtype()))
			{
				return JUtility.replaceNullStringwithBlank(row.getLocationId());
			}
		}

		return "";
	}

}
