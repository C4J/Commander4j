package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBPalletHistoryTableModel.java
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

import com.commander4j.db.JDBViewWasteLog;

public class JDBViewWasteLogTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;

	public static final int Transaction_Date = 0;
	public static final int Location_Col = 1;
	public static final int Transaction_Type = 2;
	public static final int Material_Col = 3;
	public static final int Material_Type_Col = 4;
	public static final int Reason_Col = 5;
	public static final int Process_Order_Col = 6;
	public static final int Transaction_Ref =7;
	public static final int Quantity_Col = 8;
	public static final int UOM_Col = 9;
	public static final int WeightKG_Col = 10;
	public static final int CostTotal_Col = 11;
    public static final int User_id = 12;


	private String[] mcolNames = { "Date", "Location", "Transaction",  "Material","Mat Type","Reason",  "Process Order","Txn Ref",  "Quantity","UOM","Weight KG","Cost","User ID"};
	private ResultSet mResultSet;

	private int prowCount = -1;
	private String expiryMode = "";
	private HashMap<Integer,JDBViewWasteLog> cache = new HashMap<Integer,JDBViewWasteLog>();

	public JDBViewWasteLogTableModel(ResultSet rs)
	{
		super();
		try
		{
			rs.setFetchSize(1000);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		prowCount = -1;
		mResultSet = rs;
	}

	public int getColumnCount() {
		int count = mcolNames.length;
		if (expiryMode.equals("BATCH"))
		{
			count--;
		}
		return count;
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
				final JDBViewWasteLog prow = new JDBViewWasteLog();
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case Transaction_Ref:
				return cache.get(row).getTransactionRef();
			case Transaction_Type:
				return cache.get(row).getTransactionType();
			case Transaction_Date:
				return cache.get(row).getWasteReportTime().toString().substring(0, 16);
			case Material_Col:
				return cache.get(row).getMaterialID();
			case Material_Type_Col:
				return cache.get(row).getMaterialType();
			case Process_Order_Col:
				return cache.get(row).getProcessOrder();
			case Quantity_Col:
				return cache.get(row).getQuantity();
			case Reason_Col:
				return cache.get(row).getReasonID();
			case UOM_Col:
				return cache.get(row).getUOM();
			case Location_Col:
				return cache.get(row).getLocationID();
			case User_id:
				return cache.get(row).getUserID();
			case WeightKG_Col:
				return cache.get(row).getWeightKG();
			case CostTotal_Col:
				return cache.get(row).getCostTotal();

			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}

}
