package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBInterfaceTableModel.java
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

import com.commander4j.db.JDBInterface;
import com.commander4j.sys.Common;


public class JDBInterfaceTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1;

	public static final int interfaceType_Col = 0;
	public static final int interfaceDirection_Col = 1;
	public static final int device_Col = 2;
	public static final int format_Col = 3;
	public static final int Enabled_Col = 4;
	public static final int Path_Col = 5;

	private String[] mcolNames = { "Interface Type", "Interface Direction", "Device", "Format", "Enabled", "Path" };
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBInterface> cache = new HashMap<Integer,JDBInterface>();

	public JDBInterfaceTableModel()
	{

	}

	public JDBInterfaceTableModel(ResultSet rs)
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
				final JDBInterface prow = new JDBInterface(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case interfaceType_Col:
				return cache.get(row).getInterfaceType();
			case interfaceDirection_Col:
				return cache.get(row).getInterfaceDirection();
			case device_Col:
				return cache.get(row).getDevice();
			case format_Col:
				return cache.get(row).getFormat();
			case Enabled_Col:
				String result;
				if (cache.get(row).isEnabled())
				{
					result = "Yes";
				}
				else
				{
					result = "No";
				}
				return result;
			case Path_Col:
				return cache.get(row).getPath();
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
