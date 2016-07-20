package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBMaterialUom;
import com.commander4j.sys.Common;


public class JDBMaterialUomTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Material_Col = 0;
	public static final int Material_Uom_Col = 1;
	public static final int Material_Ean_Col = 2;
	public static final int Material_Variant_Col = 3;
	public static final int Material_Numerator_Col = 4;
	public static final int Material_Denominator_Col = 5;

	private String[] mcolNames = { "Material", "UOM", "EAN", "Variant", "Numerator", "Denominator" };
	private ResultSet mResultSet;

	private int prowCount = -1;
	private HashMap<Integer,JDBMaterialUom> cache = new HashMap<Integer,JDBMaterialUom>();
	
	public JDBMaterialUomTableModel()
	{

	}

	public JDBMaterialUomTableModel(ResultSet rs)
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
				final JDBMaterialUom prow = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case Material_Col:
				return cache.get(row).getMaterial();
			case Material_Uom_Col:
				return cache.get(row).getUom();
			case Material_Ean_Col:
				return cache.get(row).getEan();
			case Material_Variant_Col:
				return cache.get(row).getVariant();
			case Material_Numerator_Col:
				return cache.get(row).getNumerator();
			case Material_Denominator_Col:
				return cache.get(row).getDenominator();
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
