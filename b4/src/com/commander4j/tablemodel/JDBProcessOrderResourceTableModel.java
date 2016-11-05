package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBProcessOrderResource;
import com.commander4j.sys.Common;

public class JDBProcessOrderResourceTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Resource_Col = 0;
	public static final int Description_Col = 1;
	public static final int Batch_Col = 2;
	public static final int Enabled_Col = 3;

	private String[] mcolNames = { "Resource", "Description", "Batch Suffix","Enabled"};
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBProcessOrderResource> cache = new HashMap<Integer,JDBProcessOrderResource>();

	public JDBProcessOrderResourceTableModel(ResultSet rs)
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
				final JDBProcessOrderResource prow = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case Resource_Col:
				return cache.get(row).getResource();
			case Description_Col:
				return cache.get(row).getDescription();
			case Batch_Col:
				return cache.get(row).getBatchSuffix();
			case Enabled_Col:
				Boolean cb;
				if (cache.get(row).getEnabled().equals("Y") == true)
				{
					cb = true;
				}
				else
				{
					cb = false;
				}
				return cb;			
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
