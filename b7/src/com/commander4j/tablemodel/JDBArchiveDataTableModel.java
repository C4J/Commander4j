package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBArchiveDataTableModel.java
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

import com.commander4j.db.JDBArchive;
import com.commander4j.sys.Common;


public class JDBArchiveDataTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int ArchiveID_Col = 0;
	public static final int Description_Col = 1;
	public static final int RetentionDays_Col = 2;
	public static final int MaxDelete_Col = 3;
	public static final int Enabled_Col = 4;
	public static final int Background_Col = 5;
	public static final int SQLTable_Col = 6;
	public static final int RetentionExpiryDate_Col = 7;	
	public static final int Sequence_Col = 8;
	public static final int Result_Col = 9;
	public static final int Run_Start_Col = 10;
	public static final int Run_End_Col = 11;

	private String[] mcolNames = { "Archive ID", "Description", "Retention","Max Delete","Enabled","Background", "Table","Archive Date","Sequence","Result","Run Start","Run End"};
	private ResultSet mResultSet;

	private int prowCount = -1;
	private HashMap<Integer,JDBArchive> cache = new HashMap<Integer,JDBArchive>();
	
	public JDBArchiveDataTableModel()
	{

	}

	public JDBArchiveDataTableModel(ResultSet rs)
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
				final JDBArchive prow = new JDBArchive(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case ArchiveID_Col:
				return cache.get(row).getArchiveID();
			case Description_Col:
				return cache.get(row).getDescription();
			case Enabled_Col:
				return cache.get(row).isEnabled();
			case Background_Col:
				return cache.get(row).isBackgroundTask();
			case SQLTable_Col:
				return cache.get(row).getSQLTable();
			case RetentionDays_Col:
				return cache.get(row).getRetentionDays();
			case MaxDelete_Col:
				return cache.get(row).getMaxDelete();
			case RetentionExpiryDate_Col:
				return cache.get(row).getSQLArchiveDate().toString().substring(0, 16);				
			case Sequence_Col:
				return cache.get(row).getSequence();
			case Result_Col:
				return cache.get(row).getSQLResult();
			case Run_Start_Col:
				try
				{
					return cache.get(row).getRunStart().toString().substring(0, 16);	
				}
				catch (Exception ex)
				{
					return "";	
				}
			case Run_End_Col:
				try
				{
					return cache.get(row).getRunEnd().toString().substring(0, 16);	
				}
				catch (Exception ex)
				{
					return "";	
				}				
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
	
  /*  @Override
    public Class<?> getColumnClass(int columnIndex) {
    	
		switch (columnIndex)
		{
		case ArchiveID_Col:
			return String.class;
		case Description_Col:
			return String.class;
		case Enabled_Col:
			return Boolean.class;
		case Background_Col:
			return Boolean.class;			
		case SQLTable_Col:
			return String.class;
		case RetentionDays_Col:
			return Integer.class;
		case MaxDelete_Col:
			return Long.class;
		case RetentionExpiryDate_Col:
			return String.class;
		case Sequence_Col:
			return Integer.class;
		case Result_Col:
			return String.class;
		case Run_Start_Col:
			return String.class;	
		case Run_End_Col:
			return String.class;					
		}
        return String.class;
    }*/
}
