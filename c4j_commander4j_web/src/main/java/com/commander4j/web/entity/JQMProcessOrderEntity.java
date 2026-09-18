/* Copied AS-IS from c4j_web_Issue (2026-09-11, step 7 port): only the package names changed.
 * Dave's decision: move the existing issue/return logic; the transactional core rewrite is deferred.
 * The decorative @Entity / @Jsonb* annotations were removed: serialisation is Gson here as it was there. */
package com.commander4j.web.entity;

import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;


public class JQMProcessOrderEntity
{
	@SerializedName("processOrderID")
	private String processOrderID;
	@SerializedName("material")
	private String material;
	@SerializedName("description")
	private String description;
	@SerializedName("status")
	private String status;
	@SerializedName("bom_id")
	private String bom_id;
	@SerializedName("bom_version")
	private String bom_version;
	public String getProcessOrderID()
	{
		return JUtility.replaceNullStringwithBlank(processOrderID);
	}
	public void setUserProcessOrderID(String var)
	{
		this.processOrderID = JUtility.replaceNullStringwithBlank(var);
	}
	public String getMaterial()
	{
		return JUtility.replaceNullStringwithBlank(material);
	}
	public void setMaterial(String var)
	{
		this.material = JUtility.replaceNullStringwithBlank(var);
	}
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(description);
	}
	public void setDescription(String var)
	{
		this.description = JUtility.replaceNullStringwithBlank(var);
	}
	public String getStatus()
	{
		return JUtility.replaceNullStringwithBlank(status);
	}
	public void setStatus(String var)
	{
		this.status = JUtility.replaceNullStringwithBlank(var);
	}
	public String getBomID()
	{
		return JUtility.replaceNullStringwithBlank(bom_id);
	}
	public void setBomID(String var)
	{
		this.bom_id = JUtility.replaceNullStringwithBlank(var);
	}
	public String getBomVersion()
	{
		return JUtility.replaceNullStringwithBlank(bom_version);
	}
	public void setBomVersion(String var)
	{
		this.bom_version = JUtility.replaceNullStringwithBlank(var);
	}
		
	@Override
	public String toString()
	{
		return "process order="+getProcessOrderID().toString()+"material="+getMaterial().toString()+ " description="+getDescription()+ " bom_id="+getBomID()+ " bom_version="+getBomVersion();
	}
}
