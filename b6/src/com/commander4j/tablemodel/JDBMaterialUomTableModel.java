package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBMaterialUomTableModel.java
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
	public static final int Material_Override_Col = 6;
	
	private String[] mcolNames = { "Material", "UOM", "EAN", "Variant", "Numerator", "Denominator","Override" };
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
			case Material_Override_Col:

				Boolean cb;
				if (cache.get(row).getOverride().equals("X") == true)
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
