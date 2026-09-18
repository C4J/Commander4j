/* Copied AS-IS from c4j_web_Issue (2026-09-11, step 7 port): only the package names changed.
 * Dave's decision: move the existing issue/return logic; the transactional core rewrite is deferred. */
package com.commander4j.web.db;

import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBPallet;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.commander4j.web.entity.JQMPalletEntity;
import com.commander4j.web.entity.JQMReturnableEntity;
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

					// Reduce the remaining quantity only. Issue never moves the
					// pallet, even when it empties it (decision A, 2026-09-10) -
					// the lane lives in the history rows alone.
					try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPallet.Issue")))
					{
						BigDecimal newQuantity = originalQty.subtract(quantity);
						stmtupdate.setBigDecimal(1, newQuantity);
						stmtupdate.setString(2, userId);
						stmtupdate.setTimestamp(3, JUtility.getSQLDateTime());
						stmtupdate.setString(4, fromLocation);
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

		if (paldb.getPalletProperties(sscc) == false)
		{
			setErrorMessage(paldb.getErrorMessage());
			return result;
		}

		try
		{
			if ((quantity.compareTo(new BigDecimal(0)) > 0))
			{

				// The lane the pallet is being returned from = the (order,
				// location) row the operator picked on
				// processOrderReturnSelect.html. The page sends that row's
				// location id as locationId; it arrives here as barcode_id.
				String fromLocation = JUtility.replaceNullStringwithBlank(barcode_id);

				// Destination = where the pallet is, and stays. Issue and
				// return never move it (decision A, 2026-09-10).
				String destination = paldb.getLocationID();

				// Whether a return message is required is governed by the
				// destination (the pallet's own location, as the desktop),
				// so capture it before the history writes change paldb's
				// location.
				boolean returnMessageRequired = paldb.getLocationObj().isPalletReturnMessageRequired();

				// A return may not exceed what is still out against the picked
				// row: the net issued minus returned for this order AT this
				// location (per-row cap, as the desktop). A blank or stale
				// location matches no row and is refused.
				BigDecimal availableToReturn = returnableFor(sscc, fromOrder, fromLocation);

				if (availableToReturn.compareTo(BigDecimal.ZERO) <= 0)
				{
					setErrorMessage("Nothing to return for pallet [" + sscc + "] against order [" + fromOrder + "] at location [" + fromLocation + "]");
					return result;
				}

				if (quantity.compareTo(availableToReturn) > 0)
				{
					setErrorMessage("Return quantity [" + quantity.toPlainString() + "] exceeds quantity available to return [" + availableToReturn.toPlainString() + "]");
					return result;
				}

				// No pallet / batch status check on return (decision B,
				// 2026-09-10). A return moves nothing - the pallet already sits
				// at its destination - and held or blocked stock is exactly
				// what must be allowed back off the line. Status gates
				// movement INTO a location (issue, despatch) only.

				BigDecimal originalQty = paldb.getQuantity();
				Long txn = (long) 0;

				// Restore the quantity only - the location is rewritten
				// unchanged.
				try (PreparedStatement stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPallet.Return")))
				{
					stmtupdate.setBigDecimal(1, originalQty.add(quantity));
					stmtupdate.setString(2, userId);
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

				// RETURN / FROM = the picked lane, order returned from.
				txn = phdb.writePalletHistory_rest(paldb, txn, "RETURN", "FROM", userId, fromLocation);

				// RETURN / TO = the pallet's own location, creation order
				// (shares the same transaction ref).
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
	 * Quantity still out against one (order, location) row for an SSCC - the
	 * same figure processOrderReturnSelect.html shows the operator, from
	 * JDBPalletHistory.getReturnableBySSCC (ISSUE/TO minus RETURN/FROM,
	 * grouped by order and location). Zero when no row matches.
	 */
	private BigDecimal returnableFor(String sscc, String order, String location)
	{
		JQMReturnableDB rdb = new JQMReturnableDB(getHostID(), getSessionID());

		for (JQMReturnableEntity row : rdb.getReturnableBySSCC(sscc))
		{
			if (JUtility.replaceNullStringwithBlank(order).equals(row.getProcessOrderID()) && JUtility.replaceNullStringwithBlank(location).equals(row.getLocationID()))
			{
				return row.getQuantity();
			}
		}

		return BigDecimal.ZERO;
	}

}
