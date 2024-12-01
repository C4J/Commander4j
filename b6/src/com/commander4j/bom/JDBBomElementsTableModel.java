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

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import com.commander4j.sys.Common;

/**
 */
public class JDBBomElementsTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	
	public static final int data_id_col = 0;
	public static final int data_type_col = 1;
	public static final int description_col = 2;
	public static final int icon_filename_col = 3;
	public static final int icon_image_col = 4;
	public static final int mac_occurs_level_col = 5;
	public static final int enable_create_col = 6;
	public static final int enable_edit_col = 7;
	public static final int enable_delete_col = 8;
	public static final int enable_duplicate_col = 9;
	public static final int enable_clipboard_col = 10;
	public static final int enable_lookup_col = 11;
	public static final int lookup_sql_col = 12;
	public static final int lookup_field_col = 13;
	
	private String[] mcolNames = {  "Data ID", 
									"Data Type",
									"Display Name", 
									"Icon Filename",
									"",
									"<html><center>Max<br>Occurs</center></html>",
									"<html><center>Enable<br>Create</center></html>", 
									"<html><center>Enable<br>Edit</center></html>", 
									"<html><center>Enable<br>Delete</center></html>", 
									"<html><center>Enable<br>Duplicate</center></html>", 
									"<html><center>Enable<br>Clipboard</center></html>", 
									"<html><center>Enable<br>Lookup</center></html>", 
									"Lookup SQL", 
									"Lookup Field"};
	
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBBomElement> cache = new HashMap<Integer,JDBBomElement>();
	
	public JDBBomElementsTableModel(String hostID,String sessionID,ResultSet rs)
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

	public Class<?> getColumnClass(int column) 
	{
        if (column == icon_image_col) {
        	return ImageIcon.class;
        }
        else
        {
        	return Object.class;
        }

     }
	
	public Object getValueAt(int row, int col) {
		try
		{
			if (cache.containsKey(row)==false)
			{
				mResultSet.absolute(row + 1);
				final JDBBomElement prow = new JDBBomElement(Common.selectedHostID, Common.sessionID);
				prow.getElementRecord().getPropertiesFromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case data_id_col:
				return cache.get(row).getElementRecord().getDataId();
			case data_type_col:
				return cache.get(row).getElementRecord().getDataType();
			case description_col:
				return cache.get(row).getElementRecord().getDescription();
			case icon_filename_col:
				return cache.get(row).getElementRecord().getIcon_filename();
			case icon_image_col:
				return cache.get(row).getElementRecord().getIcon();
			case mac_occurs_level_col:
				return cache.get(row).getElementRecord().getMax_occur_level();
			case enable_create_col:
				return cache.get(row).getElementRecord().isEnable_create();
			case enable_edit_col:
				return cache.get(row).getElementRecord().isEnable_edit();
			case enable_delete_col:
				return cache.get(row).getElementRecord().isEnable_delete();
			case enable_duplicate_col:
				return cache.get(row).getElementRecord().isEnable_duplicate();
			case enable_clipboard_col:
				return cache.get(row).getElementRecord().isEnable_clipboard();
			case enable_lookup_col:
				return cache.get(row).getElementRecord().isEnable_lookup();
			case lookup_sql_col:
				return cache.get(row).getElementRecord().getLookup_sql();
			case lookup_field_col:
				return cache.get(row).getElementRecord().getLookup_field();
				
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
