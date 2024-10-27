package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBProcessOrderTableModel.java
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

import com.commander4j.db.JDBProcessOrder;
import com.commander4j.sys.Common;

public class JDBProcessOrderTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Process_Order_Col = 0;
	public static final int Process_Order_Material_Col = 1;
	public static final int Process_Order_Description_Col = 2;
	public static final int Process_Order_Status_Col = 3;
	public static final int Process_Order_Location_Col = 4;
	public static final int Process_Order_Due_Date_Col = 5;
	public static final int Process_Order_Required_Quantity_Col = 6;
	public static final int Process_Order_Required_Uom_Col = 7;
	public static final int Process_Order_Recipe_Col = 8;
	public static final int Process_Order_Recipe_Version_Col = 9;
	public static final int Process_Order_DefaultBatchStatus_Col = 10;
	public static final int Process_Order_Required_Resource_Col = 11;
	public static final int Process_Order_Customer_Col = 12;
	
	private String[] mcolNames = { "Process Order", "Material", "Description", "Status", "Location ID", "Due Date", "Quantity", "Uom", "Recipe ID","Version", "Default Pallet Status", "Resource","Customer" };
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBProcessOrder> cache = new HashMap<Integer,JDBProcessOrder>();

	public JDBProcessOrderTableModel()
	{

	}

	public JDBProcessOrderTableModel(ResultSet rs)
	{
		super();
		prowCount = -1;
		mResultSet = rs;
	}

	public int getColumnCount() {
		return mcolNames.length;
	}

	public String getColumnName(int col) {
		return mcolNames[col];
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

	public Object getValueAt(int row, int col) {

		try
		{
			if (cache.containsKey(row)==false)
			{
				mResultSet.absolute(row + 1);
				final JDBProcessOrder prow = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			
			switch (col)
			{
			case Process_Order_Col:
				return cache.get(row).getProcessOrder();
			case Process_Order_Material_Col:
				return cache.get(row).getMaterial();
			case Process_Order_Description_Col:
				return cache.get(row).getDescription();
			case Process_Order_Status_Col:
				return cache.get(row).getStatus();
			case Process_Order_Location_Col:
				return cache.get(row).getLocation();
			case Process_Order_Due_Date_Col:
				return cache.get(row).getDueDate().toString().substring(0, 16);
			case Process_Order_Required_Quantity_Col:
				return cache.get(row).getRequiredQuantity();
			case Process_Order_Required_Uom_Col:
				return cache.get(row).getRequiredUom();
			case Process_Order_Recipe_Col:
				return cache.get(row).getRecipe();
			case Process_Order_Recipe_Version_Col:
				return cache.get(row).getRecipeVersion();
			case Process_Order_DefaultBatchStatus_Col:
				return cache.get(row).getDefaultPalletStatus();
			case Process_Order_Required_Resource_Col:
				return cache.get(row).getRequiredResource();
			case Process_Order_Customer_Col:
				return cache.get(row).getCustomerID();				
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}

	public void setValueAt(Object value, int row, int col) {

	}
}
