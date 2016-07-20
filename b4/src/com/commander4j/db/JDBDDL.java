package com.commander4j.db;

/**
 * @author David
 * @version $Revision: 1.0 $
 */
public class JDBDDL
{
	/**
	 * @uml.property name="version"
	 */
	private int version = -1;
	/**
	 * @uml.property name="sequence"
	 */
	private int sequence = -1;
	/**
	 * @uml.property name="description"
	 */
	// private String description = "";
	/**
	 * @uml.property name="text"
	 */
	private String text = "";
	/**
	 * @uml.property name="error"
	 */
	private String error = "";
	
	private int errorCount = 0;

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public JDBDDL()
	{

	}

	/**
	 * Constructor for JDBDDL.
	 * 
	 * @param ver
	 *            int
	 * @param seq
	 *            int
	 * @param desc
	 *            String
	 * @param txt
	 *            String
	 * @param err
	 *            String
	 */
	public JDBDDL(int ver, int seq, String desc, String txt, String err)
	{
		version = ver;
		sequence = seq;
		// description = desc;
		text = txt;
		error = err;
	}

	/**
	 * Method getVersion.
	 * 
	 * @return int
	 * @uml.property name="version"
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Method getSequence.
	 * 
	 * @return int
	 * @uml.property name="sequence"
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * Method getDescription.
	 * 
	 * @return String
	 * @uml.property name="description"
	 */

	/**
	 * Method getText.
	 * 
	 * @return String
	 * @uml.property name="text"
	 */
	public String getText() {
		return text;

	}

	/**
	 * Method getError.
	 * 
	 * @return String
	 * @uml.property name="error"
	 */
	public String getError() {
		return error;

	}

	/**
	 * Method setVersion.
	 * 
	 * @param value
	 *            int
	 * @uml.property name="version"
	 */
	public void setVersion(int value) {
		version = value;
	}

	/**
	 * Method setSequence.
	 * 
	 * @param value
	 *            int
	 * @uml.property name="sequence"
	 */
	public void setSequence(int value) {
		sequence = value;
	}

	/**
	 * Method setDescription.
	 * 
	 * @param value
	 *            String
	 * @uml.property name="description"
	 */

	/**
	 * Method setText.
	 * 
	 * @param value
	 *            String
	 * @uml.property name="text"
	 */
	public void setText(String value) {
		text = value;

	}

	/**
	 * Method setError.
	 * 
	 * @param value
	 *            String
	 * @uml.property name="error"
	 */
	public void setError(String value) {
		error = value;

	}

}
