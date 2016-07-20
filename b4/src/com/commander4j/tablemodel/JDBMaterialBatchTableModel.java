package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.sys.Common;

/**
 */
public class JDBMaterialBatchTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Material_Col = 0;
	public static final int Batch_Col = 1;
	public static final int Status_Col = 2;
	public static final int Expiry_Col = 3;

	// Names of the columns
	private String[] mcolNames = { "Material", "Batch", "Status", "Expiry Date" };
	private ResultSet mResultSet;
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private int prowCount = -1;
	private String expiryMode = "";
	private String result = "";
	private HashMap<Integer,JDBMaterialBatch> cache = new HashMap<Integer,JDBMaterialBatch>();

	public JDBMaterialBatchTableModel(ResultSet rs)
	{
		super();
		expiryMode = ctrl.getKeyValue("EXPIRY DATE MODE");
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
				final JDBMaterialBatch prow = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case Material_Col:
				return cache.get(row).getMaterial();
			case Batch_Col:
				return cache.get(row).getBatch();
			case Status_Col:
				return cache.get(row).getStatus();
			case Expiry_Col:
				if (expiryMode.equals("BATCH"))
				{
					try
					{
						result = cache.get(row).getExpiryDate().toString().substring(0, 16);
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

			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
