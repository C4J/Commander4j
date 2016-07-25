package com.commander4j.gui;

import javax.swing.Icon;

/**
 */
public class JListData implements Comparable<JListData>
{
	/**
	 * @uml.property name="mIcon"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private Icon mIcon;
	/**
	 * @uml.property name="mIndex"
	 */
	private int mIndex;
	/**
	 * @uml.property name="mSelectable"
	 */
	private boolean mSelectable;
	/**
	 * @uml.property name="mData"
	 */
	private Object mData;

	/**
	 * Constructor for JDBListData.
	 * 
	 * @param icon
	 *            Icon
	 * @param index
	 *            int
	 * @param selectable
	 *            boolean
	 * @param data
	 *            Object
	 */
	public JListData(Icon icon, int index, boolean selectable, Object data)
	{
		mIcon = icon;
		mIndex = index;
		mSelectable = selectable;
		mData = data;
	}

	/**
	 * Method getmData.
	 * 
	 * @return Object
	 */
	public Object getmData() {
		return mData;
	}

	/**
	 * Method getIcon.
	 * 
	 * @return Icon
	 */
	public Icon getIcon() {
		return mIcon;
	}

	/**
	 * Method getIndex.
	 * 
	 * @return int
	 */
	public int getIndex() {
		return mIndex;
	}

	/**
	 * Method isSelectable.
	 * 
	 * @return boolean
	 */
	public boolean isSelectable() {
		return mSelectable;
	}

	/**
	 * Method getObject.
	 * 
	 * @return Object
	 */
	public Object getObject() {
		return mData;
	}

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	public String toString() {
		return mData.toString();
	}

	/**
	 * Method compareTo.
	 * 
	 * @param anotherModuleListData
	 *            JDBListData
	 * @return int
	 */
	public int compareTo(JListData anotherModuleListData) {
		return mData.toString().compareTo(anotherModuleListData.toString());

	}

}
