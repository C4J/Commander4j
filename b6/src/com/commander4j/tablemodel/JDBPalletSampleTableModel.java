package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBPalletSampleTableModel.java
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

import com.commander4j.db.JDBPalletSamples;
import com.commander4j.sys.Common;

/**
 */
public class JDBPalletSampleTableModel extends AbstractTableModel
{

	private static final long 	serialVersionUID = 1;
	//public static final int 	SSCC_Col = 0;
	public static final int 	SampleSequence_Col =0;
	public static final int 	SampleDate_Col = 1;
	public static final int 	SamplePoint_Col = 2;	
	public static final int 	SampleReason_Col = 3;
	public static final int 	SampleDefectType_Col = 4;
	public static final int 	SampleDefectID_Col = 5;
	public static final int 	SampleLeaking_Col = 6;
	public static final int 	SampleQuantity = 7;
	public static final int 	SampleComment_Col = 8;
	public static final int 	SampleUserID_Col = 9;
	public static final int 	SampleLane1_Col = 10;
	public static final int 	SampleLane2_Col = 11;
	public static final int 	SampleLane3_Col = 12;
	public static final int 	SampleLane4_Col = 13;
	public static final int 	SampleLane5_Col = 14;

	private String[] mcolNames = { "Unique ID", "Sample Date", "Sample Point","Reason","Defect Type", "Defect ID", "Leaking", "Sample Qty","Comment", "User ID","Lane 1","Lane 2","Lane 3","Lane 4","Lane 5" };
	private ResultSet mResultSet;
	
	private int prowCount = -1;
	private HashMap<Integer,JDBPalletSamples> cache = new HashMap<Integer,JDBPalletSamples>();
	
	public JDBPalletSampleTableModel()
	{

	}

	public JDBPalletSampleTableModel(ResultSet rs)
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
				final JDBPalletSamples prow = new JDBPalletSamples(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
				
			case SampleSequence_Col:
				return cache.get(row).getSampleSequence();
				
			case SampleDate_Col:
				String result = "";
				try
				{
					result = cache.get(row).getSampleDateTime().toString().substring(0, 16);
				}
				catch (Exception ex)
				{
					result = "";
				}
				return result;
				
			case SamplePoint_Col:
				return cache.get(row).getSamplePoint();
				
			case SampleReason_Col:
				return cache.get(row).getSampleReason();
				
			case SampleDefectType_Col:
				return cache.get(row).getSampleDefectType();
				
			case SampleDefectID_Col:
				return cache.get(row).getSampleDefectID();
				
			case SampleLeaking_Col:

				Boolean cb;
				if (cache.get(row).getSampleLeaking().equals("Y") == true)
				{
					cb = true;
				}
				else
				{
					cb = false;
				}
				return cb;
				
			case SampleComment_Col:
				return cache.get(row).getSampleComment();
				
			case SampleQuantity:
				return cache.get(row).getSampleQuantity();
				
			case SampleUserID_Col:
				return cache.get(row).getUserID();
				
			case SampleLane1_Col:
				return cache.get(row).getLane1Quantity();	
				
			case SampleLane2_Col:
				return cache.get(row).getLane2Quantity();
				
			case SampleLane3_Col:
				return cache.get(row).getLane3Quantity();	
				
			case SampleLane4_Col:
				return cache.get(row).getLane4Quantity();	
				
			case SampleLane5_Col:
				return cache.get(row).getLane5Quantity();	
				
			}

		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
