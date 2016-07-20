/*
 * Created on 27-May-2005
 *
 */
package com.commander4j.bar;

/**
 * @author David
 * 
 * @version $Revision: 1.0 $
 */
public class JEANdata
{
	private String applicationID;

	private String data;

	JEANdata()
	{
		applicationID = "";
		data = "";
	}

	/**
	 * Constructor for JEANdata.
	 * 
	 * @param id
	 *            String
	 * @param dat
	 *            String
	 */
	JEANdata(String id, String dat)
	{
		applicationID = id;
		data = dat;
	}

	/**
	 * Method getApplicationID.
	 * 
	 * @return String
	 */
	public String getApplicationID() {
		return applicationID;
	}

	/**
	 * Method setApplicationID.
	 * 
	 * @param value
	 *            String
	 */
	public void setApplicationID(String value) {
		applicationID = value;
	}

	/**
	 * Method getData.
	 * 
	 * @return String
	 */
	public String getData() {
		return data;
	}

	/**
	 * Method setData.
	 * 
	 * @param value
	 *            String
	 */
	public void setData(String value) {
		data = value;
	}
}
