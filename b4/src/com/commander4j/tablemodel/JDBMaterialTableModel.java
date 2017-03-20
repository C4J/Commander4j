package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBMaterialTableModel.java
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

import com.commander4j.db.JDBMaterial;
import com.commander4j.sys.Common;

/**
 */
public class JDBMaterialTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Material_Col = 0;
	public static final int Material_Description_Col = 1;
	public static final int Material_Type_Col = 2;
	public static final int Material_Base_Uom_Col = 3;
	public static final int Material_Shelf_Life_Col = 4;
	public static final int Material_Shelf_Life_Uom_Col = 5;
	public static final int Material_Shelf_Life_Rule_Col = 6;

	// Names of the columns
	private String[] mcolNames = { "Material", "Description", "Type", "Base UOM", "Shelf Life", "Life UOM", "Rule" };

	// store the data
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBMaterial> cache = new HashMap<Integer,JDBMaterial>();

	public JDBMaterialTableModel()
	{

	}

	public JDBMaterialTableModel(ResultSet rs)
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
				final JDBMaterial prow = new JDBMaterial(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case Material_Col:
				return cache.get(row).getMaterial();
			case Material_Description_Col:
				return cache.get(row).getDescription();
			case Material_Type_Col:
				return cache.get(row).getMaterialType();
			case Material_Base_Uom_Col:
				return cache.get(row).getBaseUom();
			case Material_Shelf_Life_Col:
				return cache.get(row).getShelfLife();
			case Material_Shelf_Life_Uom_Col:
				return cache.get(row).getShelfLifeUom();
			case Material_Shelf_Life_Rule_Col:
				return cache.get(row).getShelfLifeRule();

			}

		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
