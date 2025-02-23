package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBPalletTableModel.java
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
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBWTSampleDetail;
import com.commander4j.sys.Common;


public class JDBWeightDetailTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1;
	public static final int SampleSequence_Col = 0;
	public static final int SampleWeightDate_Col = 1;
	public static final int SampleGrossWeight_Col = 2;
	public static final int SampleTareWeight_Col = 3;
	public static final int SampleNetWeight_Col = 4;
	public static final int SampleWeightUOM_Col = 5;
	public static final int SampleT1Count_Col = 6;
	public static final int SampleT2Count_Col = 7;
	
	private String[] mcolNames = { "Sequence","Date", "Gross", "Tare", "Net", "UOM","T1 Count","T2 Count" };
	private ResultSet mResultSet;
	
	private int prowCount = -1;
	private HashMap<Integer,JDBWTSampleDetail> cache = new HashMap<Integer,JDBWTSampleDetail>();
	
	public JDBWeightDetailTableModel()
	{

	}

	public JDBWeightDetailTableModel(ResultSet rs)
	{
		super();
		
		try
		{
			rs.setFetchSize(1000);
		}
		catch (SQLException e)
		{

			e.printStackTrace();
		}
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
				final JDBWTSampleDetail prow = new JDBWTSampleDetail(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			String result = "";
			switch (col)
			{
			case SampleSequence_Col:
				return cache.get(row).getSampleSequence();
				
			case SampleWeightDate_Col:
				result = "";
				try
				{
					result = cache.get(row).getSampleWeightDate().toString().substring(0, 16);
				}
				catch (Exception ex)
				{
					result = "";
				}
				return result;
				
			case SampleGrossWeight_Col:
				return cache.get(row).getSampleGrossWeight();
				
			case SampleTareWeight_Col:
				return cache.get(row).getSampleTareWeight();
				
			case SampleNetWeight_Col:
				return cache.get(row).getSampleNetWeight();
				
			case SampleWeightUOM_Col:
				return cache.get(row).getSampleWeightUom();
				
			case SampleT1Count_Col:
				return cache.get(row).getSampleT1Count();	
				
			case SampleT2Count_Col:
				return cache.get(row).getSampleT2Count();	
				
			}

		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
