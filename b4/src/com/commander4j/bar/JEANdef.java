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
public class JEANdef
{
	private String applicationID;

	private String description;

	private String dataType;

	private String decimalIndicator;

	private int maxLength;

	private String fixedVariable;

	private String checkDigit;

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	public String toString() {
		return this.applicationID;
	}

	/**
	 * Constructor for JEANdef.
	 * 
	 * @param appid
	 *            String
	 * @param desc
	 *            String
	 * @param type
	 *            String
	 * @param decInd
	 *            String
	 * @param maxlen
	 *            int
	 * @param fixedvar
	 *            String
	 * @param cd
	 *            String
	 */
	JEANdef(String appid, String desc, String type, String decInd, int maxlen, String fixedvar, String cd)
	{
		applicationID = appid;
		description = desc;
		dataType = type;
		decimalIndicator = decInd;
		maxLength = maxlen;
		fixedVariable = fixedvar;
		checkDigit = cd;
	}

	/**
	 * @return the applicationID
	 */
	public String getApplicationID() {
		return applicationID;
	}

	/**
	 * @param value
	 *            String
	 */
	public void setApplicationID(String value) {
		applicationID = value;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param value
	 *            String
	 */
	public void setDescription(String value) {
		description = value;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param value
	 *            String
	 */
	public void setDataType(String value) {
		dataType = value;
	}

	/**
	 * @return the decimalIndicator
	 */
	public String getDecimalIndicator() {
		return decimalIndicator;
	}

	/**
	 * @param value
	 *            String
	 */
	public void setDecimalIndicator(String value) {
		decimalIndicator = value;
	}

	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * @param value
	 *            int
	 */
	public void setMaxLength(int value) {
		maxLength = value;
	}

	/**
	 * @return the fixedVariable
	 */
	public String getFixedVariable() {
		return fixedVariable;
	}

	/**
	 * @param value
	 *            String
	 */
	public void setFixedVariable(String value) {
		fixedVariable = value;
	}

	/**
	 * @return the checkDigit
	 */
	public String getCheckDigit() {
		return checkDigit;
	}

	/**
	 * @param value
	 *            String
	 */
	public void setCheckDigit(String value) {
		checkDigit = value;
	}

}
