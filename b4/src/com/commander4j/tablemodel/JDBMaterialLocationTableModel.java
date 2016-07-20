package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBMaterialLocation;
import com.commander4j.sys.Common;

public class JDBMaterialLocationTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Material_Col = 0;
	public static final int Location_Col = 1;
	public static final int Status_Col = 2;

	private String[] mcolNames = { "Material", "Location", "Status"};
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBMaterialLocation> cache = new HashMap<Integer,JDBMaterialLocation>();

	public JDBMaterialLocationTableModel(ResultSet rs)
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
				final JDBMaterialLocation prow = new JDBMaterialLocation(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case Material_Col:
				return cache.get(row).getMaterial();
			case Location_Col:
				return cache.get(row).getLocation();
			case Status_Col:
				return cache.get(row).getStatus();
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
