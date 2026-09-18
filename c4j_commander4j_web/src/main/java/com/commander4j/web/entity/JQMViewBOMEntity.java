/* Copied AS-IS from c4j_web_Issue (2026-09-11, step 7 port): only the package names changed.
 * Dave's decision: move the existing issue/return logic; the transactional core rewrite is deferred.
 * The decorative @Entity / @Jsonb* annotations were removed: serialisation is Gson here as it was there. */
package com.commander4j.web.entity;

import com.commander4j.util.JUtility;
import com.google.gson.annotations.SerializedName;


public class JQMViewBOMEntity
{
	@SerializedName("action")
	private String action;
	@SerializedName("bom_id")
	private String bom_id;
	@SerializedName("bom_version")
	private String bom_version;
	@SerializedName("stage")
	private String stage;
	@SerializedName("input_output")
	private String input_Output;
	@SerializedName("material")
	private String material;
	@SerializedName("description")
	private String description;
	@SerializedName("location_id")
	private String location_id;
	@SerializedName("commandStatus")
	private String commandStatus;
	public String getAction()
	{
		return this.action;
	}
	public void setAction(String val)
	{
		this.action = val;
	}
	public String getLocation_id()
	{
		return this.location_id;
	}
	public void setLocation_id(String val)
	{
		this.location_id = val;
	}
	public String getInputOutput()
	{
		return JUtility.replaceNullStringwithBlank(this.input_Output);
	}
	public void setInputOutput(String val)
	{
		this.input_Output = JUtility.replaceNullStringwithBlank(val);
	}
	public String getMaterial()
	{
		return JUtility.replaceNullStringwithBlank(this.material);
	}
	public void setMaterial(String val)
	{
		this.material = JUtility.replaceNullStringwithBlank(val);
	}
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(this.description);
	}
	public void setDescription(String val)
	{
		this.description = JUtility.replaceNullStringwithBlank(val);
	}
	public String getStage()
	{
		return JUtility.replaceNullStringwithBlank(this.stage);
	}
	public void setStage(String val)
	{
		this.stage = JUtility.replaceNullStringwithBlank(val);
	}
	public String getBomID()
	{
		return JUtility.replaceNullStringwithBlank(this.bom_id);
	}
	public void setBomID(String val)
	{
		this.bom_id = JUtility.replaceNullStringwithBlank(val);
	}
	public String getBomVersion()
	{
		return JUtility.replaceNullStringwithBlank(this.bom_version);
	}
	public void setBomVersion(String val)
	{
		this.bom_version = JUtility.replaceNullStringwithBlank(val);
	}
	public void setCommandStatus(String val)
	{
		this.commandStatus = JUtility.replaceNullStringwithBlank(val);
	}
	public String getCommandStatus()
	{
		return JUtility.replaceNullStringwithBlank(commandStatus);
	}
	
}
