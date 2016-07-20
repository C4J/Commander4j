package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBMaterialCustomerData;
import com.commander4j.sys.Common;


public class JDBMaterialCustomerDataTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Material_Col = 0;
	public static final int Customer_Col = 1;
	public static final int Data_ID_Col = 2;
	public static final int Data_Col = 3;


	private String[] mcolNames = { "Material", "Customer ID", "Data ID", "Data"};
	private ResultSet mResultSet;

	private int prowCount = -1;
	private HashMap<Integer,JDBMaterialCustomerData> cache = new HashMap<Integer,JDBMaterialCustomerData>();
	
	public JDBMaterialCustomerDataTableModel()
	{

	}

	public JDBMaterialCustomerDataTableModel(ResultSet rs)
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
				final JDBMaterialCustomerData prow = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case Material_Col:
				return cache.get(row).getMaterial();
			case Customer_Col:
				return cache.get(row).getCustomerID();
			case Data_ID_Col:
				return cache.get(row).getDataID();
			case Data_Col:
				return cache.get(row).getData();

			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
