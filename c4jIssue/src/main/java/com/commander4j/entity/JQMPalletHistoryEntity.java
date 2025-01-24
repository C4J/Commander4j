package com.commander4j.entity;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.commander4j.db.JDBPalletHistory;
import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.Entity;

@JsonbPropertyOrder(
{ "action","transaction_ref","transaction_date","transaction_type","transaction_subtype", "sscc", "processOrder", "quantity", "material", "batchNumber", "uom", "userId", "locationId", "commandStatus", "errorMessage" })

@Entity
public class JQMPalletHistoryEntity
{

	@JsonbProperty("action")
	@SerializedName("action")
	private String action;
	
	@JsonbProperty("transaction_ref")
	@SerializedName("transaction_ref")
	private long transaction_ref;
	
	@JsonbProperty("transaction_date")
	@SerializedName("transaction_date")
	private Timestamp transaction_date;
	
	@JsonbProperty("transaction_type")
	@SerializedName("transaction_type")
	private String transaction_type;
	
	@JsonbProperty("transaction_subtype")
	@SerializedName("transaction_subtype")
	private String transaction_subtype;

	@JsonbProperty("sscc")
	@SerializedName("sscc")
	private String sscc;

	@JsonbProperty("processOrder")
	@SerializedName("processOrder")
	private String processOrder;

	@JsonbProperty("quantity")
	@SerializedName("quantity")
	private BigDecimal quantity = new BigDecimal(0.000);

	@JsonbProperty("material")
	@SerializedName("material")
	private String material;

	@JsonbProperty("batchNumber")
	@SerializedName("batchNumber")
	private String batchNumber;

	@JsonbProperty("uom")
	@SerializedName("uom")
	private String uom;

	@JsonbProperty("userId")
	@SerializedName("userId")
	private String userId;

	@JsonbProperty("locationId")
	@SerializedName("locationId")
	private String locationId;

	@JsonbProperty("commandStatus")
	@SerializedName("commandStatus")
	private String commandStatus;

	@JsonbProperty("errorMessage")
	@SerializedName("errorMessage")
	private String errorMessage;

	@JsonbProperty("action")
	public String getAction()
	{
		return JUtility.replaceNullStringwithBlank(action);
	}

	@JsonbProperty("batchNumber")
	public String getBatchNumber()
	{
		return JUtility.replaceNullStringwithBlank(batchNumber);
	}

	@JsonbProperty("material")
	public String getMaterial()
	{
		return JUtility.replaceNullStringwithBlank(material);
	}

	@JsonbProperty("userId")
	public String getUserId()
	{
		return JUtility.replaceNullStringwithBlank(userId);
	}

	@JsonbProperty("locationId")
	public String getLocationId()
	{
		return JUtility.replaceNullStringwithBlank(locationId);
	}

	@JsonbProperty("commandStatus")
	public String getCommandStatus()
	{
		return JUtility.replaceNullStringwithBlank(commandStatus);
	}

	@JsonbProperty("errorMessage")
	public String getErrorMessage()
	{
		return JUtility.replaceNullStringwithBlank(errorMessage);
	}

	@JsonbProperty("processOrder")
	public String getProcessOrder()
	{
		return JUtility.replaceNullStringwithBlank(processOrder);
	}

	@JsonbProperty("quantity")
	public BigDecimal getQuantity()
	{
		return quantity;
	}

	@JsonbProperty("sscc")
	public String getSSCC()
	{
		return JUtility.replaceNullStringwithBlank(sscc);
	}

	@JsonbProperty("uom")
	public String getUom()
	{
		return JUtility.replaceNullStringwithBlank(uom);
	}

	@JsonbProperty("action")
	public void setAction(String val)
	{
		this.action = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("batchNumber")
	public void setBatchNumber(String val)
	{
		this.batchNumber = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("material")
	public void setMaterial(String val)
	{
		this.material = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("userId")
	public void setUserId(String val)
	{
		this.userId = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("locationId")
	public void setLocationId(String val)
	{
		this.locationId = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("commandStatus")
	public void setCommandStatus(String val)
	{
		this.commandStatus = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("errorMessage")
	public void setErrorMessage(String val)
	{
		this.errorMessage = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("processOrder")
	public void setProcessOrder(String val)
	{
		this.processOrder = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("quantity")
	public void setQuantity(BigDecimal val)
	{
		this.quantity = val;
	}

	@JsonbProperty("sscc")
	public void setSSCC(String val)
	{
		this.sscc = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("uom")
	public void setUom(String val)
	{
		this.uom = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("transaction_ref")
	public long getTransactionRef()
	{
		return transaction_ref;
	}

	@JsonbProperty("transaction_ref")
	public void setTransactionRef(long val)
	{
		this.transaction_ref = val;
	}

	@JsonbProperty("transaction_date")
	public Timestamp getTransactionDate()
	{
		return transaction_date;
	}

	@JsonbProperty("transaction_date")
	public void setTransactionDate(Timestamp val)
	{
		this.transaction_date = val;
	}

	@JsonbProperty("transaction_type")
	public String getTransactionType()
	{
		return transaction_type;
	}

	@JsonbProperty("transaction_type")
	public void setTransactionType(String val)
	{
		this.transaction_type = val;
	}

	@JsonbProperty("transaction_subtype")
	public String getTransactionSubtype()
	{
		return transaction_subtype;
	}

	@JsonbProperty("transaction_subtype")
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

			e.printStackTrace();
		}

	}
	

}
