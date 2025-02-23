/*
 * Created on 02-Mar-2005
 *
 */
package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBControlTableModel.java
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

import com.commander4j.db.JDBControl;
import com.commander4j.sys.Common;

public class JDBControlTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Control_Key_Col = 0;
	public static final int Control_Value_Col = 1;
	public static final int Control_Description_Col = 2;

	private String[] mcolNames = { "System Key", "Value", "Description" };
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBControl> cache = new HashMap<Integer,JDBControl>();
	
	public JDBControlTableModel()
	{

	}

	public JDBControlTableModel(ResultSet rs)
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
				final JDBControl prow = new JDBControl(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case Control_Key_Col:
				return cache.get(row).getSystemKey();
			case Control_Value_Col:
				return cache.get(row).getKeyValue();
			case Control_Description_Col:
				return cache.get(row).getDescription();
			}

		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}

}
