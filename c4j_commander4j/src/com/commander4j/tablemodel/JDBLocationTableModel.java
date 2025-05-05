package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBLocationTableModel.java
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

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBLocation;
import com.commander4j.sys.Common;

/**
 */
public class JDBLocationTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Location_ID_Col = 0;
	public static final int Location_Enabled_Col = 1;
	public static final int Location_Plant_Col = 2;
	public static final int Location_Warehouse_Col = 3;
	public static final int Location_GLN_Col = 4;
	public static final int Location_Description_Col = 5;
	public static final int Location_Storage_Location_Col = 6;
	public static final int Location_Storage_Type_Col = 7;
	public static final int Location_Storage_Section_Col = 8;
	public static final int Location_Storage_Bin_Col = 9;

	// Names of the columns
	private String[] mcolNames = { "Location ID", "Enabled","Plant", "Warehouse", "GLN", "Description", "Storage Location", "Storage Type", "Storage Section", "Storage Bin" };
	// Types of the columns.
	private ResultSet mResultSet;

	private int prowCount = -1;
	private HashMap<Integer,JDBLocation> cache = new HashMap<Integer,JDBLocation>();

	public JDBLocationTableModel()
	{

	}

	public JDBLocationTableModel(ResultSet rs)
	{
		super();
		prowCount = -1;
		mResultSet = rs;
	}

	public int getColumnCount() {
		return mcolNames.length;
	}

	public int getRowCount() {
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

	public void setValueAt(Object value, int row, int col) {

	}

	public String getColumnName(int col) {
		return mcolNames[col];
	}

	public Object getValueAt(int row, int col) {
		try
		{
			if (cache.containsKey(row)==false)
			{
				mResultSet.absolute(row + 1);
				final JDBLocation prow = new JDBLocation(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case Location_ID_Col:
				return cache.get(row).getLocationID();		
			case Location_Enabled_Col:

				Boolean cb;
				if (cache.get(row).getEnabled().equals("Y") == true)
				{
					cb = true;
				}
				else
				{
					cb = false;
				}
				return cb;
			case Location_Plant_Col:
				return cache.get(row).getPlant();
			case Location_Warehouse_Col:
				return cache.get(row).getWarehouse();
			case Location_GLN_Col:
				return cache.get(row).getGLN();
			case Location_Description_Col:
				return cache.get(row).getDescription();
			case Location_Storage_Location_Col:
				return cache.get(row).getStorageLocation();
			case Location_Storage_Type_Col:
				return cache.get(row).getStorageType();
			case Location_Storage_Section_Col:
				return cache.get(row).getStorageSection();
			case Location_Storage_Bin_Col:
				return cache.get(row).getStorageBin();
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
