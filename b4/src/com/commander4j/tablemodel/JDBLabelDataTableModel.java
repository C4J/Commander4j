package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBLabelDataTableModel.java
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
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBLabelData;
import com.commander4j.sys.Common;

/**
 */
public class JDBLabelDataTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1;
	public static final int Line_Col = 0;
	public static final int LabelType_Col = 1;
	public static final int Print_Date_Col = 2;
	public static final int UserID_Col = 3;
	public static final int WorkstationID_Col = 4;
	public static final int ProcessOrder_Col = 5;
	public static final int Material_Col = 6;
	public static final int BatchNumber_Col = 7;
	public static final int Expiry_Col = 8;	

	
	private String[] mcolNames = { "Line","Group","Print Date",  "User", "Workstation","Process Order","Material", "Batch","Expiry Date" };
	private ResultSet mResultSet;

	private int prowCount = -1;
	private HashMap<Integer,JDBLabelData> cache = new HashMap<Integer,JDBLabelData>();
	
	public JDBLabelDataTableModel()
	{
	}
	
	public JDBLabelDataTableModel(ResultSet rs)
	{
		super();
		prowCount = -1;
		mResultSet = rs;
	}
	
	public void setResultSet(ResultSet rs)
	{
		
		try
		{
			cache.clear();
			rs.setFetchSize(20);
		}
		catch (SQLException e)
		{

			e.printStackTrace();
		}
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
				final JDBLabelData prow = new JDBLabelData(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case Print_Date_Col:
				return cache.get(row).getPrintDate().toString().substring(0, 16);
			case Expiry_Col:
				return cache.get(row).getExpirtDate().toString().substring(0, 16);				
			case UserID_Col:
				return cache.get(row).getUserID();
			case WorkstationID_Col:
				return cache.get(row).getWorkstationID();	
			case ProcessOrder_Col:
				return cache.get(row).getProcessOrder();	
			case Material_Col:
				return cache.get(row).getMaterial();			
			case Line_Col:
				return cache.get(row).getLine();	
			case BatchNumber_Col:
				return cache.get(row).getBatchNumber();
			case LabelType_Col:
				return cache.get(row).getLabelType();
			}

		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
