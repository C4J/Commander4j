package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBPallet;
import com.commander4j.sys.Common;

/**
 */
public class JDBPalletTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1;
	public static final int SSCC_Col = 0;
	public static final int Material_Col = 1;
	public static final int Batch_Col = 2;
	public static final int Process_Order_Col = 3;
	public static final int Quantity_Col = 4;
	public static final int Uom_Col = 5;
	public static final int Date_of_Manufacture_Col = 6;
	public static final int Status_Col = 7;
	public static final int Location_Col = 8;
	public static final int Date_Created_Col = 9;
	public static final int Confirmed_Col = 10;
	public static final int Despatch_Col = 11;
	public static final int MHN_Number_Col = 12;
	private String[] mcolNames = { "SSCC", "Material", "Batch", "Process Order", "Quantity", "UOM", "Date of Manufacture", "Status", "Location","Date Created", "Conf", "Despatch","MHN Number" };
	private ResultSet mResultSet;
	
	private int prowCount = -1;
	private HashMap<Integer,JDBPallet> cache = new HashMap<Integer,JDBPallet>();
	
	public JDBPalletTableModel()
	{

	}

	public JDBPalletTableModel(ResultSet rs)
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
				final JDBPallet prow = new JDBPallet(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case SSCC_Col:
				return cache.get(row).getSSCC();
			case Material_Col:
				return cache.get(row).getMaterial();
			case Batch_Col:
				return cache.get(row).getBatchNumber();
			case Process_Order_Col:
				return cache.get(row).getProcessOrder();
			case Quantity_Col:
				return cache.get(row).getQuantity();
			case Uom_Col:
				return cache.get(row).getUom();
			case Date_of_Manufacture_Col:
				String result = "";
				try
				{
					result = cache.get(row).getDateOfManufacture().toString().substring(0, 16);
				}
				catch (Exception ex)
				{
					result = "";
				}
				return result;
			case Status_Col:
				return cache.get(row).getStatus();
			case Location_Col:
				return cache.get(row).getLocationID();
			case Date_Created_Col:
				String result2 = "";
				try
				{
					result2 = cache.get(row).getDateCreated().toString().substring(0, 16);
				}
				catch (Exception ex)
				{
					result2 = "";
				}
				return result2;
			case Confirmed_Col:

				Boolean cb;
				if (cache.get(row).getConfirmed().equals("Y") == true)
				{
					cb = true;
				}
				else
				{
					cb = false;
				}
				return cb;
			case Despatch_Col:
				return cache.get(row).getDespatchNo();
			case MHN_Number_Col:
				return cache.get(row).getMHNNumber();				
			}

		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
