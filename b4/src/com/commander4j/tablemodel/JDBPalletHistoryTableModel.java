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

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBPalletHistory;
import com.commander4j.sys.Common;

public class JDBPalletHistoryTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Transaction_Ref = 0;
	public static final int Transaction_Type = 1;
	public static final int Transaction_Subtype = 2;
	public static final int Transaction_Date = 3;
	public static final int SSCC_Col = 4;
	public static final int Material_Col = 5;
	public static final int Batch_Col = 6;
	public static final int Process_Order_Col = 7;
	public static final int Quantity_Col = 8;
	public static final int Uom_Col = 9;
	public static final int Despatch_Col = 10;
	public static final int Status_Col = 11;
	public static final int Location_Col = 12;
	public static final int User_id = 13;
	public static final int mhn_number = 14;
	public static final int decision = 15;
	public static final int sscc_expiry_date_id = 16;

	private String[] mcolNames = { "Ref", "Type", "Subtype", "Date", "SSCC", "Material", "Batch", "Process Order", "Quantity", "UOM", "Despatch No", "Status", "Location", "User ID","MHN Number","Decision", "SSCC Expiry" };
	private ResultSet mResultSet;
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private int prowCount = -1;
	private String expiryMode = "";
	private String result = "";
	private HashMap<Integer,JDBPalletHistory> cache = new HashMap<Integer,JDBPalletHistory>();

	public JDBPalletHistoryTableModel(ResultSet rs)
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
		expiryMode = ctrl.getKeyValue("EXPIRY DATE MODE");
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
				final JDBPalletHistory prow = new JDBPalletHistory(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case Transaction_Ref:
				return cache.get(row).getTransactionRef();
			case Transaction_Type:
				return cache.get(row).getTransactionType();
			case Transaction_Subtype:
				return cache.get(row).getTransactionSubtype();
			case Transaction_Date:
				return cache.get(row).getTransactionDate().toString().substring(0, 16);
			case SSCC_Col:
				return cache.get(row).getPallet().getSSCC();
			case Material_Col:
				return cache.get(row).getPallet().getMaterial();
			case Batch_Col:
				return cache.get(row).getPallet().getBatchNumber();
			case Process_Order_Col:
				return cache.get(row).getPallet().getProcessOrder();
			case Quantity_Col:
				return cache.get(row).getPallet().getQuantity();
			case Uom_Col:
				return cache.get(row).getPallet().getUom();
			case Despatch_Col:
				return cache.get(row).getPallet().getDespatchNo();
			case Status_Col:
				return cache.get(row).getPallet().getStatus();
			case Location_Col:
				return cache.get(row).getPallet().getLocationID();
			case User_id:
				return cache.get(row).getUserID();
			case sscc_expiry_date_id:
				if (expiryMode.equals("SSCC"))
				{
					try
					{
						result = cache.get(row).getPallet().getBatchExpiry().toString().substring(0, 16);
					}
					catch (Exception ex)
					{
						result = "";
					}
				}
				else
				{
					result = "";
				}
				return result;
			case mhn_number:
				return cache.get(row).getPallet().getMHNNumber();
			case decision:
				return cache.get(row).getPallet().getDecision();

			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}

}
