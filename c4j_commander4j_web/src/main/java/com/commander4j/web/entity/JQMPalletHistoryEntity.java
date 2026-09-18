/* Copied AS-IS from c4j_web_Issue (2026-09-11, step 7 port): only the package names changed.
 * Dave's decision: move the existing issue/return logic; the transactional core rewrite is deferred.
 * The decorative @Entity / @Jsonb* annotations were removed: serialisation is Gson here as it was there. */
package com.commander4j.web.entity;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBPalletHistory;
import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;


public class JQMPalletHistoryEntity
{

	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMPalletHistoryEntity.class);
	@SerializedName("action")
	private String action;
	@SerializedName("transaction_ref")
	private long transaction_ref;
	@SerializedName("transaction_date")
	private Timestamp transaction_date;
	@SerializedName("transaction_type")
	private String transaction_type;
	@SerializedName("transaction_subtype")
	private String transaction_subtype;
	@SerializedName("sscc")
	private String sscc;
	@SerializedName("processOrder")
	private String processOrder;
	@SerializedName("quantity")
	private BigDecimal quantity = new BigDecimal(0.000);
	@SerializedName("material")
	private String material;
	@SerializedName("batchNumber")
	private String batchNumber;
	@SerializedName("uom")
	private String uom;
	@SerializedName("userId")
	private String userId;
	@SerializedName("locationId")
	private String locationId;
	@SerializedName("commandStatus")
	private String commandStatus;
	@SerializedName("errorMessage")
	private String errorMessage;
	public String getAction()
	{
		return JUtility.replaceNullStringwithBlank(action);
	}
	public String getBatchNumber()
	{
		return JUtility.replaceNullStringwithBlank(batchNumber);
	}
	public String getMaterial()
	{
		return JUtility.replaceNullStringwithBlank(material);
	}
	public String getUserId()
	{
		return JUtility.replaceNullStringwithBlank(userId);
	}
	public String getLocationId()
	{
		return JUtility.replaceNullStringwithBlank(locationId);
	}
	public String getCommandStatus()
	{
		return JUtility.replaceNullStringwithBlank(commandStatus);
	}
	public String getErrorMessage()
	{
		return JUtility.replaceNullStringwithBlank(errorMessage);
	}
	public String getProcessOrder()
	{
		return JUtility.replaceNullStringwithBlank(processOrder);
	}
	public BigDecimal getQuantity()
	{
		return quantity;
	}
	public String getSSCC()
	{
		return JUtility.replaceNullStringwithBlank(sscc);
	}
	public String getUom()
	{
		return JUtility.replaceNullStringwithBlank(uom);
	}
	public void setAction(String val)
	{
		this.action = JUtility.replaceNullStringwithBlank(val);
	}
	public void setBatchNumber(String val)
	{
		this.batchNumber = JUtility.replaceNullStringwithBlank(val);
	}
	public void setMaterial(String val)
	{
		this.material = JUtility.replaceNullStringwithBlank(val);
	}
	public void setUserId(String val)
	{
		this.userId = JUtility.replaceNullStringwithBlank(val);
	}
	public void setLocationId(String val)
	{
		this.locationId = JUtility.replaceNullStringwithBlank(val);
	}
	public void setCommandStatus(String val)
	{
		this.commandStatus = JUtility.replaceNullStringwithBlank(val);
	}
	public void setErrorMessage(String val)
	{
		this.errorMessage = JUtility.replaceNullStringwithBlank(val);
	}
	public void setProcessOrder(String val)
	{
		this.processOrder = JUtility.replaceNullStringwithBlank(val);
	}
	public void setQuantity(BigDecimal val)
	{
		this.quantity = val;
	}
	public void setSSCC(String val)
	{
		this.sscc = JUtility.replaceNullStringwithBlank(val);
	}
	public void setUom(String val)
	{
		this.uom = JUtility.replaceNullStringwithBlank(val);
	}
	public long getTransactionRef()
	{
		return transaction_ref;
	}
	public void setTransactionRef(long val)
	{
		this.transaction_ref = val;
	}
	public Timestamp getTransactionDate()
	{
		return transaction_date;
	}
	public void setTransactionDate(Timestamp val)
	{
		this.transaction_date = val;
	}
	public String getTransactionType()
	{
		return transaction_type;
	}
	public void setTransactionType(String val)
	{
		this.transaction_type = val;
	}
	public String getTransactionSubtype()
	{
		return transaction_subtype;
	}
	public void setTransactionSubtype(String val)
	{
		this.transaction_subtype = val;
	}

	public void getPropertiesFromPalletHistory(JDBPalletHistory pal)
	{
		setTransactionRef(pal.getTransactionRef());
		setTransactionDate(pal.getTransactionDate());
		setTransactionType(pal.getTransactionType());
		setTransactionSubtype(pal.getTransactionSubtype());
		setSSCC(pal.getPallet().getSSCC());
		setProcessOrder(pal.getPallet().getProcessOrder());
		setQuantity(pal.getPallet().getQuantity());
		setMaterial(pal.getPallet().getMaterial());
		setUom(pal.getPallet().getUom());
		setLocationId(pal.getPallet().getLocationID());
		setBatchNumber(pal.getPallet().getBatchNumber());
	}
	
	public void getPropertiesFromResultSet(ResultSet rs)
	{

		try
		{
			setTransactionRef(rs.getLong("transaction_ref"));
			setTransactionDate(rs.getTimestamp("transaction_date"));
			setTransactionType(rs.getString("transaction_type"));
			setTransactionSubtype(rs.getString("transaction_subtype"));
			setSSCC(rs.getString("sscc"));
			setProcessOrder(rs.getString("process_order"));
			setQuantity(rs.getBigDecimal("quantity"));
			setMaterial(rs.getString("material"));
			setUom(rs.getString("uom"));
			setLocationId(rs.getString("location_id"));
			setBatchNumber(rs.getString("batch_number"));
		}
		catch (SQLException e)
		{
			logger.error("getPropertiesFromResultSet failed", e);
		}

	}
	

}
