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

import com.commander4j.db.JDBWTViewWeightSample;
import com.commander4j.sys.Common;


public class JDBViewWeightSamplModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1;
	public static final int SampleDate_Col = 0;
	public static final int SamplePoint_Col = 1;
	public static final int Material_Col = 2;
	public static final int ProcessOrder_Col = 3;
	public static final int ProductGroup_Col = 4;
	public static final int ContainerCode_Col = 5;
	public static final int NominalWeight_Col = 6;
	public static final int NominalWeightUom_Col = 7;
	public static final int SampleMean_Col = 8;
	public static final int SampleStdDev_Col = 9;
	public static final int SampleTotalT1s_Col = 10;
	public static final int SampleTotalT2s_Col = 11;

	private String[] mcolNames = { "Sample Date","Sample Point","Material","Order", "Group", "Container","Nominal", "UOM","Mean","Std Dev","T1's","T2's"};
	private ResultSet mResultSet;
	
	private int prowCount = -1;
	private HashMap<Integer,JDBWTViewWeightSample> cache = new HashMap<Integer,JDBWTViewWeightSample>();
	
	public JDBViewWeightSamplModel()
	{

	}

	public JDBViewWeightSamplModel(ResultSet rs)
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
				final JDBWTViewWeightSample prow = new JDBWTViewWeightSample(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			String result = "";
			switch (col)
			{
			
			case SampleDate_Col:
				result = "";
				try
				{
					result = cache.get(row).getSampleDate().toString().substring(0, 19);
				}
				catch (Exception ex)
				{
					result = "";
				}
				return result;
				
			case SamplePoint_Col:
				return cache.get(row).getSamplePoint();
				
			case Material_Col:
				return cache.get(row).getMaterial();
				
			case ProcessOrder_Col:
				return cache.get(row).getProcessOrder();
				
			case ProductGroup_Col:
				return cache.get(row).getProductGroup();
				
			case ContainerCode_Col:
				return cache.get(row).getContainerCode();

			case NominalWeight_Col:
				return cache.get(row).getNominalWeight();

			case NominalWeightUom_Col:
				return cache.get(row).getNominalWeightUOM();
				
			case SampleMean_Col:
				return cache.get(row).getMeanWeight();

			case SampleStdDev_Col:
				return cache.get(row).getStandardDeviation();
				
			case SampleTotalT1s_Col:
				return cache.get(row).getTotalT1s();	
				
			case SampleTotalT2s_Col:
				return cache.get(row).getTotalT2s();	
	
			}

		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
