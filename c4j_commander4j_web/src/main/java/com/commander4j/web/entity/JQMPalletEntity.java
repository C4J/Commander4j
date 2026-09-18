/* Copied AS-IS from c4j_web_Issue (2026-09-11, step 7 port): only the package names changed.
 * Dave's decision: move the existing issue/return logic; the transactional core rewrite is deferred.
 * The decorative @Entity / @Jsonb* annotations were removed: serialisation is Gson here as it was there. */
package com.commander4j.web.entity;

import java.math.BigDecimal;

import com.commander4j.db.JDBPallet;
import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;


public class JQMPalletEntity
{
	@SerializedName("action")
	private String action;
	@SerializedName("sscc")
	private String sscc;
	@SerializedName("processOrder")
	private String processOrder;
	@SerializedName("quantity")
	private BigDecimal quantity = new BigDecimal(0.000);
	@SerializedName("material")
	private String material;
	@SerializedName("palletStatus")
	private String palletStatus;
	@SerializedName("batchNumber")
	private String batchNumber;
	@SerializedName("batchStatus")
	private String batchStatus;
	@SerializedName("uom")
	private String uom;
	@SerializedName("confirmed")
	private String confirmed;
	@SerializedName("bomId")
	private String bomId;
	@SerializedName("bomVersion")
	private String bomVersion;
	@SerializedName("oldMaterial")
	private String oldMaterial;
	@SerializedName("userId")
	private String userId;
	@SerializedName("locationId")
	private String locationId;
	@SerializedName("description")
	private String description;
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
	public String getBatchStatus()
	{
		return JUtility.replaceNullStringwithBlank(batchStatus);
	}
	public String getBomdId()
	{
		return JUtility.replaceNullStringwithBlank(bomId);
	}
	public String getBomVersion()
	{
		return JUtility.replaceNullStringwithBlank(bomVersion);
	}
	public String getConfirmed()
	{
		return JUtility.replaceNullStringwithBlank(confirmed);
	}
	public String getMaterial()
	{
		return JUtility.replaceNullStringwithBlank(material);
	}
	public String getOldMaterial()
	{
		return JUtility.replaceNullStringwithBlank(oldMaterial);
	}
	public String getUserId()
	{
		return JUtility.replaceNullStringwithBlank(userId);
	}
	public String getLocationId()
	{
		return JUtility.replaceNullStringwithBlank(locationId);
	}
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(description);
	}
	public String getCommandStatus()
	{
		return JUtility.replaceNullStringwithBlank(commandStatus);
	}
	public String getErrorMessage()
	{
		return JUtility.replaceNullStringwithBlank(errorMessage);
	}
	public String getPalletStatus()
	{
		return JUtility.replaceNullStringwithBlank(palletStatus);
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
	public void setBatchStatus(String val)
	{
		this.batchStatus = JUtility.replaceNullStringwithBlank(val);
	}
	public void setBomId(String val)
	{
		this.bomId = JUtility.replaceNullStringwithBlank(val);
	}
	public void setBomVersion(String val)
	{
		this.bomVersion = JUtility.replaceNullStringwithBlank(val);
	}
	public void setConfirmed(String val)
	{
		this.confirmed = JUtility.replaceNullStringwithBlank(val);
	}
	public void setMaterial(String val)
	{
		this.material = JUtility.replaceNullStringwithBlank(val);
	}
	public void setOldMaterial(String val)
	{
		this.oldMaterial = JUtility.replaceNullStringwithBlank(val);
	}
	public void setUserId(String val)
	{
		this.userId = JUtility.replaceNullStringwithBlank(val);
	}
	public void setLocationId(String val)
	{
		this.locationId = JUtility.replaceNullStringwithBlank(val);
	}
	public void setDescription(String val)
	{
		this.description = JUtility.replaceNullStringwithBlank(val);
	}
	public void setCommandStatus(String val)
	{
		this.commandStatus = JUtility.replaceNullStringwithBlank(val);
	}
	public void setErrorMessage(String val)
	{
		this.errorMessage = JUtility.replaceNullStringwithBlank(val);
	}
	public void setPalletStatus(String val)
	{
		this.palletStatus = JUtility.replaceNullStringwithBlank(val);
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
