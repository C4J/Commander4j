package com.commander4j.c4jWS;

import com.commander4j.util.JUtility;

import jakarta.persistence.Entity;

@Entity
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
