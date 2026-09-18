/* Copied AS-IS from c4j_web_Issue (2026-09-11, step 7 port): only the package names changed.
 * Dave's decision: move the existing issue/return logic; the transactional core rewrite is deferred.
 * The decorative @Entity / @Jsonb* annotations were removed: serialisation is Gson here as it was there. */
package com.commander4j.web.entity;

import java.math.BigDecimal;

import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;


public class JQMReturnableEntity
{
	@SerializedName("action")
	private String action;
	@SerializedName("processOrderID")
	private String processOrderID;
	@SerializedName("locationID")
	private String locationID;
	@SerializedName("quantity")
	private BigDecimal quantity = new BigDecimal(0.000);
	@SerializedName("uom")
	private String uom;
	public String getAction()
	{
		return JUtility.replaceNullStringwithBlank(action);
	}
	public String getProcessOrderID()
	{
		return JUtility.replaceNullStringwithBlank(processOrderID);
	}
	public String getLocationID()
	{
		return JUtility.replaceNullStringwithBlank(locationID);
	}
	public BigDecimal getQuantity()
	{
		return quantity;
	}
	public String getUom()
	{
		return JUtility.replaceNullStringwithBlank(uom);
	}
	public void setAction(String action)
	{
		this.action = JUtility.replaceNullStringwithBlank(action);
	}
	public void setProcessOrderID(String processOrder)
	{
		this.processOrderID = JUtility.replaceNullStringwithBlank(processOrder);
	}
	public void setLocationID(String location)
	{
		this.locationID = JUtility.replaceNullStringwithBlank(location);
	}
	public void setQuantity(BigDecimal quantity)
	{
		this.quantity  = quantity;
	}
	public void setUom(String uom)
	{
		this.uom = JUtility.replaceNullStringwithBlank(uom);
	}
		
}
