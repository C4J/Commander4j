package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBProcessOrderResourceTableModel.java
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

import com.commander4j.db.JDBProcessOrderResource;
import com.commander4j.sys.Common;

public class JDBProcessOrderResourceTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Resource_Col = 0;
	public static final int Description_Col = 1;
	public static final int Batch_Col = 2;
	public static final int Plant_Col = 3;
	public static final int Enabled_Col = 4;

	private String[] mcolNames = { "Resource", "Description", "Batch Suffix","Plant","Enabled"};
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBProcessOrderResource> cache = new HashMap<Integer,JDBProcessOrderResource>();

	public JDBProcessOrderResourceTableModel(ResultSet rs)
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
				final JDBProcessOrderResource prow = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case Resource_Col:
				return cache.get(row).getResource();
			case Description_Col:
				return cache.get(row).getDescription();
			case Batch_Col:
				return cache.get(row).getBatchSuffix();
			case Plant_Col:
				return cache.get(row).getPlant();
			case Enabled_Col:
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
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
