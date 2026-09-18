/* Copied AS-IS from c4j_web_Issue (2026-09-11, step 7 port): only the package names changed.
 * Dave's decision: move the existing issue/return logic; the transactional core rewrite is deferred.
 * The decorative @Entity / @Jsonb* annotations were removed: serialisation is Gson here as it was there. */
package com.commander4j.web.entity;

import com.commander4j.util.JUtility;


public class JQMResourceEntity
{
	private String required_resource;
	private String description;

	public String getRequiredResource()
	{
		return JUtility.replaceNullStringwithBlank(required_resource).toUpperCase();
	}
	public void setRequiredResource(String resource)
	{
		this.required_resource = JUtility.replaceNullStringwithBlank(resource);
	}
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(description);
	}
	public void setDescription(String desc)
	{
		this.description = JUtility.replaceNullStringwithBlank(desc);
	}

	@Override
	public String toString()
	{
		return "resource="+getRequiredResource().toString()+" description="+getDescription().toString();
	}
}
