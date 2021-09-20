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
import com.commander4j.sys.Common;

public class JDBViewWasteLogTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;

	public static final int Location_Col = 0;
	public static final int Container_Col = 1;
	public static final int Transaction_Date_Col = 2;
	public static final int Transaction_Type_Col = 3;
	public static final int Material_Col = 4;
	public static final int Material_Type_Col = 5;
	public static final int Reason_Col = 6;
	public static final int WeightKG_Col = 7;
	public static final int TareWeight_Col = 8;
	public static final int NetWeight_Col = 9;
	public static final int CostPerKg_Col = 10;
	public static final int CostTotal_Col = 11;
	public static final int Process_Order_Col = 12;
    public static final int User_id_Col = 13;
	public static final int Transaction_Ref_Col =14;
	public static final int Comment_Col =15;


	private String[] mcolNames = {  "Location","Container", "Date","Transaction",  "Material","Mat Type","Reason","Weight KG","Tare Weight","Net Weight","Cost Per Kg","Cost",  "Process Order","User ID","Txn Ref","Comment"};
	private ResultSet mResultSet;

	private int prowCount = -1;

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
				final JDBViewWasteLog prow = new JDBViewWasteLog(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case Transaction_Ref_Col:
				return cache.get(row).getTransactionRef();
			case Transaction_Type_Col:
				return cache.get(row).getTransactionType();
			case Transaction_Date_Col:
				return cache.get(row).getWasteReportTime().toString().substring(0, 16);
			case Material_Col:
				return cache.get(row).getMaterialID();
			case Material_Type_Col:
				return cache.get(row).getMaterialType();
			case Process_Order_Col:
				return cache.get(row).getProcessOrder();
			case Reason_Col:
				return cache.get(row).getReasonID();
			case Location_Col:
				return cache.get(row).getLocationID();
			case Container_Col:
				return cache.get(row).getContainerID();
			case User_id_Col:
				return cache.get(row).getUserID();
			case WeightKG_Col:
				return cache.get(row).getWeightKG();
			case TareWeight_Col:
				return cache.get(row).getTareWeight();
			case NetWeight_Col:
				return cache.get(row).getNetWeightKG();
			case CostPerKg_Col:
				return cache.get(row).getCostPerKg();
			case CostTotal_Col:
				return cache.get(row).getCostTotal();
			case Comment_Col:
				return cache.get(row).getComment();

			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}

}
