/* Copied AS-IS from c4j_web_WS (2026-09-12, step 8 port): only the package changed, the decorative @Entity annotation was dropped (jakarta.persistence is not in this war; serialisation is Gson) and the sample check uses core com.commander4j.db.JDBQMSample. */
package com.commander4j.web.entity;

import com.commander4j.util.JUtility;


public class JQMUserEntity
{
	private String userID;
	private String firstname;
	private String surname;
	private String enabled;

	
	public String getUserID()
	{
		return JUtility.replaceNullStringwithBlank(userID);
	}
	public void setUserID(String userID)
	{
		this.userID = JUtility.replaceNullStringwithBlank(userID);
	}
	public String getFirstName()
	{
		return JUtility.replaceNullStringwithBlank(firstname);
	}
	public void setFirstName(String firstName)
	{
		this.firstname = JUtility.replaceNullStringwithBlank(firstName);
	}
	public String getSurname()
	{
		return JUtility.replaceNullStringwithBlank(surname);
	}
	public void setSurname(String surname)
	{
		this.surname = JUtility.replaceNullStringwithBlank(surname);
	}
	public String getEnabled()
	{
		return JUtility.replaceNullStringwithBlank(enabled);
	}
	public void setEnabled(String enabled)
	{
		this.enabled = JUtility.replaceNullStringwithBlank(enabled);
	}
		
	@Override
	public String toString()
	{
		return "qm user="+getUserID().toString()+"firstname="+getFirstName().toString()+ " surname="+getSurname();
	}
}
