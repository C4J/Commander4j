package com.commander4j.entity;

import java.math.BigDecimal;

import com.commander4j.db.JDBPallet;
import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.Entity;

@JsonbPropertyOrder(
{ "action", "sscc", "processOrder", "quantity", "material", "palletStatus", "batchNumber", "batchStatus", "uom", "confirmed", "bomId", "bomVersion", "oldMaterial", "userId", "locationId", "description", "commandStatus", "errorMessage" })

@Entity
public class JQMPalletEntity
{

	@JsonbProperty("action")
	@SerializedName("action")
	private String action;

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

	@JsonbProperty("palletStatus")
	@SerializedName("palletStatus")
	private String palletStatus;

	@JsonbProperty("batchNumber")
	@SerializedName("batchNumber")
	private String batchNumber;

	@JsonbProperty("batchStatus")
	@SerializedName("batchStatus")
	private String batchStatus;

	@JsonbProperty("uom")
	@SerializedName("uom")
	private String uom;

	@JsonbProperty("confirmed")
	@SerializedName("confirmed")
	private String confirmed;

	@JsonbProperty("bomId")
	@SerializedName("bomId")
	private String bomId;

	@JsonbProperty("bomVersion")
	@SerializedName("bomVersion")
	private String bomVersion;

	@JsonbProperty("oldMaterial")
	@SerializedName("oldMaterial")
	private String oldMaterial;

	@JsonbProperty("userId")
	@SerializedName("userId")
	private String userId;

	@JsonbProperty("locationId")
	@SerializedName("locationId")
	private String locationId;

	@JsonbProperty("description")
	@SerializedName("description")
	private String description;

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

	@JsonbProperty("batchStatus")
	public String getBatchStatus()
	{
		return JUtility.replaceNullStringwithBlank(batchStatus);
	}

	@JsonbProperty("bomId")
	public String getBomdId()
	{
		return JUtility.replaceNullStringwithBlank(bomId);
	}

	@JsonbProperty("bomVersion")
	public String getBomVersion()
	{
		return JUtility.replaceNullStringwithBlank(bomVersion);
	}

	@JsonbProperty("confirmed")
	public String getConfirmed()
	{
		return JUtility.replaceNullStringwithBlank(confirmed);
	}

	@JsonbProperty("material")
	public String getMaterial()
	{
		return JUtility.replaceNullStringwithBlank(material);
	}

	@JsonbProperty("oldMaterial")
	public String getOldMaterial()
	{
		return JUtility.replaceNullStringwithBlank(oldMaterial);
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

	@JsonbProperty("description")
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(description);
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

	@JsonbProperty("palletStatus")
	public String getPalletStatus()
	{
		return JUtility.replaceNullStringwithBlank(palletStatus);
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

	@JsonbProperty("batchStatus")
	public void setBatchStatus(String val)
	{
		this.batchStatus = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("bomId")
	public void setBomId(String val)
	{
		this.bomId = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("bomVersion")
	public void setBomVersion(String val)
	{
		this.bomVersion = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("confirmed")
	public void setConfirmed(String val)
	{
		this.confirmed = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("material")
	public void setMaterial(String val)
	{
		this.material = JUtility.replaceNullStringwithBlank(val);
	}

	@JsonbProperty("oldMaterial")
	public void setOldMaterial(String val)
	{
		this.oldMaterial = JUtility.replaceNullStringwithBlank(val);
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

	@JsonbProperty("description")
	public void setDescription(String val)
	{
		this.description = JUtility.replaceNullStringwithBlank(val);
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

	@JsonbProperty("palletStatus")
	public void setPalletStatus(String val)
	{
		this.palletStatus = JUtility.replaceNullStringwithBlank(val);
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

	public void getPropertiesFromPallet(JDBPallet pal)
	{

		setSSCC(pal.getSSCC());
		setProcessOrder(pal.getProcessOrder());
		//setQuantity(pal.getQuantity());
		setMaterial(pal.getMaterial());
		setUom(pal.getUom());
		setBomId(pal.getProcessOrderObj(true).getRecipe());
		setBomVersion(pal.getProcessOrderObj(true).getRecipeVersion());
		setLocationId(pal.getLocationID());
		setBatchNumber(pal.getBatchNumber());
		setConfirmed(pal.getConfirmed());
		setPalletStatus(pal.getStatus());
		setBatchStatus(pal.getMaterialBatchStatus());
		setOldMaterial(pal.getMaterialObj().getOldMaterial());
		setDescription(pal.getMaterialObj().getDescription());

	}

}
