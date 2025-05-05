package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBListData.java
 * 
 * Package Name : com.commander4j.db
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import javax.swing.Icon;

/**
 * JDBListData is a class which is used when displaying lists of data on screen
 * via a JList with an optional graphical icon associated with each item. An
 * example would be the list of users when some users have their accounts locked
 * or disabled.
 *
 */
public class JDBListData implements Comparable<JDBListData>
{

	private Icon mIcon;

	private int mIndex;

	private boolean mSelectable;

	private Object mData;

	public JDBListData(Icon icon, int index, boolean selectable, Object data)
	{
		mIcon = icon;
		mIndex = index;
		mSelectable = selectable;
		mData = data;
	}

	public Object getmData()
	{
		return mData;
	}

	public Icon getIcon()
	{
		return mIcon;
	}

	public int getIndex()
	{
		return mIndex;
	}

	public boolean isSelectable()
	{
		return mSelectable;
	}

	public Object getObject()
	{
		return mData;
	}

	public String toString()
	{
		return mData.toString();
	}

	public int compareTo(JDBListData anotherModuleListData)
	{
		return mData.toString().compareTo(anotherModuleListData.toString());

	}

}
