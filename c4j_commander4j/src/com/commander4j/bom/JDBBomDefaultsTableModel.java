package com.commander4j.bom;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBBomDefaultsTableModel.java
 * 
 * Package Name : com.commander4j.tablemodel
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

import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import com.commander4j.sys.Common;

/**
 */
public class JDBBomDefaultsTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;

	public static final int source_field_desc_col = 0;
	public static final int source_field_icon_col = 1;
	public static final int source_field_col = 2;
	public static final int source_value_col = 3;
	public static final int destination_field_desc_col = 4;
	public static final int destination_field_icon_col = 5;
	public static final int destination_field_col = 6;
	public static final int destination_value_col = 7;

	// Names of the columns
	private String[] mcolNames =
	{ "Source Name","", "Source Field", "Source Value", "Destination Name","", "Destination Field", "Destination Value" };
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer, JDBBomDefaults> cache = new HashMap<Integer, JDBBomDefaults>();
	private JDBBomElement elemSource;
	private JDBBomElement elemDestination;

	public JDBBomDefaultsTableModel(String hostID, String sessionID, ResultSet rs)
	{
		super();
		
		elemSource = new JDBBomElement(hostID, sessionID);
		elemDestination = new JDBBomElement(hostID, sessionID);

		prowCount = -1;
		mResultSet = rs;
	}

	public int getColumnCount()
	{
		return mcolNames.length;
	}

	public int getRowCount()
	{
		try
		{
			if (prowCount <= 0)
			{
				mResultSet.last();
				prowCount = mResultSet.getRow();
				mResultSet.beforeFirst();
			}
			return prowCount;

		}
		catch (Exception e)
		{
			return 0;
		}
	}

	public void setValueAt(Object value, int row, int col)
	{

	}

	public String getColumnName(int col)
	{
		return mcolNames[col];
	}

	public Class<?> getColumnClass(int column)
	{
		Class<?> result;

		switch (column)
		{
		case source_field_icon_col:
			result = ImageIcon.class;
			break;
		case destination_field_icon_col:
			result = ImageIcon.class;
			break;
		default:
			result = Object.class;
		}

		return result;
	}

	public Object getValueAt(int row, int col)
	{
		try
		{
			if (cache.containsKey(row) == false)
			{
				mResultSet.absolute(row + 1);
				final JDBBomDefaults prow = new JDBBomDefaults(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				
				elemSource.getProperties(prow.getSouceField());
				elemDestination.getProperties(prow.getDestinationField());
				
				cache.put(row, prow);
			}

			switch (col)
			{
			case source_field_col:
				return cache.get(row).getSouceField();
			case source_field_desc_col:
				return elemSource.getElementRecord().getDescription();
			case source_field_icon_col:
				return elemSource.getElementRecord().getIcon();
			case source_value_col:
				return cache.get(row).getSourceValue();
			case destination_field_col:
				return cache.get(row).getDestinationField();
			case destination_value_col:
				return cache.get(row).getDestinationValue();
			case destination_field_desc_col:
				return elemDestination.getElementRecord().getDescription();
			case destination_field_icon_col:
				return elemDestination.getElementRecord().getIcon();
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
