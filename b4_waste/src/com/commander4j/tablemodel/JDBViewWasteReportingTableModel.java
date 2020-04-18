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

import com.commander4j.db.JDBViewWasteReporting;

public class JDBViewWasteReportingTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;

	public static final int Reporting_Group_Col 		= 0;
	public static final int Reporting_ID_Col 			= 1;
	public static final int Location_Col 				= 2;
	public static final int Report_Date_Col 			= 3;
	public static final int Transaction_Type_Col 		= 4;
	public static final int Material_Col 				= 5;
	public static final int Material_Type_Col 			= 6;
	public static final int Reason_Col 					= 7;	
	public static final int Quantity_Col 				= 8;
	public static final int UOM_Col 					= 9;
	public static final int Cost_Per_Uom_Col 			= 10;
	public static final int Conversion_To_Kg_Col 		= 11;
	public static final int WeightKG_Col 				= 12;
	public static final int Cost_Col 					= 13;
	public static final int Process_Order_Col 			= 14;
	public static final int Transaction_Ref_Col 		= 15;



	private String[] mcolNames = {"Group", "Report ID",  "Location","Date Time", "Transaction",   "Material", "Mat Type", "Reason", "Quantity","UOM", "Cost Per UOM","Conv To KG", "Weight KG",  "Cost","Process Order","Txn Ref",};
	private ResultSet mResultSet;
	private String grp = "";

	private int prowCount = -1;

	private HashMap<Integer,JDBViewWasteReporting> cache = new HashMap<Integer,JDBViewWasteReporting>();

	public JDBViewWasteReportingTableModel(ResultSet rs)
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
				final JDBViewWasteReporting prow = new JDBViewWasteReporting();
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			
//			public static final int Reporting_ID_Col 			= 0;
//			public static final long Reporting_Group_Col 		= 1;
//			public static final int Transaction_Ref_Col 		= 2;
//			public static final int Report_Date_Col 			= 3;
//			public static final int Transaction_Type_Col 		= 4;
//			public static final int Location_Col 				= 5;
//			public static final int Material_Col 				= 6;
//			public static final int Material_Tye_Col 			= 7;
//			public static final int Reason_Col 					= 8;	
//			public static final int Process_Order_Col 			= 9;	
//			public static final int Quantity_Col 				= 10;
//			public static final int UOM_Col 					= 11;
//			public static final int Cost_Per_Uom_Col 			= 12;
//			public static final int Conversion_To_Kg_Col 		= 13;
//			public static final int WeightKG_Col 				= 14;
//			public static final int Cost_Col 					= 15;

			switch (col)
			{
			case Reporting_ID_Col:
				return cache.get(row).getReportingID();
			case Reporting_Group_Col:
				grp = String.valueOf(cache.get(row).getReportingGroup());
				return grp;
			case Transaction_Ref_Col:
				return cache.get(row).getTransactionRef();
			case Report_Date_Col:
				return cache.get(row).getWasteReportTime().toString().substring(0, 16);
			case Transaction_Type_Col:
				return cache.get(row).getTransactionType();
			case Location_Col:
				return cache.get(row).getLocationID();
			case Material_Col:
				return cache.get(row).getMaterialID();
			case Material_Type_Col:
				return cache.get(row).getMaterialType();
			case Reason_Col:
				return cache.get(row).getReasonID();
			case Process_Order_Col:
				return cache.get(row).getProcessOrder();
			case Quantity_Col:
				return cache.get(row).getQuantity();
			case UOM_Col:
				return cache.get(row).getUOM();
			case Cost_Per_Uom_Col:
				return cache.get(row).getCostPerUom();
			case Conversion_To_Kg_Col:
				return cache.get(row).getConversionToKg();
			case WeightKG_Col:
				return cache.get(row).getWeightKG();
			case Cost_Col:
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
