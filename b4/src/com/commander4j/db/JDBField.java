package com.commander4j.db;

/**
 * @author David
 * @version $Revision: 1.0 $
 */
public class JDBField
{
	/**
	 * @uml.property name="fieldName"
	 */
	private String fieldName = "";
	/**
	 * @uml.property name="fieldType"
	 */
	private String fieldType = "";
	/**
	 * @uml.property name="fieldSize"
	 */
	private int fieldSize = 0;

	/**
	 * Constructor for JDBField.
	 * 
	 * @param name
	 *            String
	 * @param type
	 *            String
	 * @param size
	 *            int
	 */
	public JDBField(String name, String type, int size)
	{
		fieldName = name;
		fieldType = type;
		fieldSize = size;
	}

	/**
	 * Method getfieldName.
	 * 
	 * @return String
	 */
	public String getfieldName() {
		return fieldName;
	}

	/**
	 * Method getfieldType.
	 * 
	 * @return String
	 */
	public String getfieldType() {
		return fieldType;
	}

	/**
	 * Method getfieldSize.
	 * 
	 * @return int
	 */
	public int getfieldSize() {
		return fieldSize;

	}
	
	public String toString()
	{
		return fieldName;
	}
}
