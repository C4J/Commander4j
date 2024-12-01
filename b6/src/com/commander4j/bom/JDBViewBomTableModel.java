package com.commander4j.bom;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBBomElementsTableModel.java
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

import com.commander4j.sys.Common;

/**
 */
public class JDBViewBomTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	
	public static final int bom_id_col = 0;
	public static final int bom_version_col = 1;
	public static final int bom_stage_col = 2;
	public static final int bom_input_output_col = 3;
	public static final int bom_sequence_col = 4;
	public static final int bom_material_col = 5;
	public static final int bom_material_type = 6;
	public static final int bom_description_col = 7;
	public static final int bom_quantity_col = 8;
	public static final int bom_uom_col = 9;
	public static final int bom_location_col = 10;

	
	private String[] mcolNames = {  "Bom ID", 
									"<html><center>Bom<br>Version</center></html>",
									"Stage",
									"<html><center>Input<br>Output</center></html>",
									"Sequence",
									"Material", 
									"Type",
									"Description",
									"Quantity",
									"UOM",
									"Location"};
	
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBViewBomRecord> cache = new HashMap<Integer,JDBViewBomRecord>();
	
	public JDBViewBomTableModel(ResultSet rs)
	{
		super();
		prowCount = -1;
		mResultSet = rs;
	}
	
	public JDBViewBomTableModel(String hostID,String sessionID,ResultSet rs)
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
				final JDBViewBomRecord prow = new JDBViewBomRecord(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesFromResultSet(mResultSet);
				cache.put(row, prow);
			}
			
			switch (col)
			{
			case bom_id_col:
				return cache.get(row).getBom_id();
			case bom_version_col:
				return cache.get(row).getBom_version();
			case bom_input_output_col:
				return cache.get(row).getInputOutput();
			case bom_stage_col:
				return cache.get(row).getStage();
			case bom_sequence_col:
				return cache.get(row).getSequence();
			case bom_material_col:
				return cache.get(row).getMaterial();
			case bom_material_type:
				return cache.get(row).getType();
			case bom_description_col:
				return cache.get(row).getDescription();
			case bom_quantity_col:
				return cache.get(row).getQuantity();
			case bom_uom_col:
				return cache.get(row).getUom();
			case bom_location_col:
				return cache.get(row).getLocation_id();

				
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
