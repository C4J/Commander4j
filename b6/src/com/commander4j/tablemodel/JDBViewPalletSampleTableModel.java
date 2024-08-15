package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBViewPalletSampleTableModel.java
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

import com.commander4j.db.JDBViewPalletSample;
import com.commander4j.sys.Common;

public class JDBViewPalletSampleTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	
	public static final int SSCC_Col 			= 0;
	public static final int Production_Date 	= 1;
	public static final int Location_Col 	    = 2;
	public static final int SamplePoint_Col 	= 3;	
	public static final int Sample_Reason 		= 4;
	public static final int Sample_DefectType 	= 5;
	public static final int Sample_DefectID 	= 6;
	public static final int Leaking 			= 7;
	public static final int SampleQuantity_Col 	= 8;
	public static final int Material_Col 		= 9;
	public static final int Process_Order_Col 	= 10;
	public static final int MHN_number 			= 11;	
	public static final int Sample_Date 		= 12;
	public static final int Sample_Seq 			= 13;
	public static final int Operative_Seq 		= 14;
	public static final int 	SampleLane1_Col = 15;
	public static final int 	SampleLane2_Col = 16;
	public static final int 	SampleLane3_Col = 17;
	public static final int 	SampleLane4_Col = 18;
	public static final int 	SampleLane5_Col = 19;

	private String[] mcolNames = {"SSCC", 
								  "Prod Date", 
								  "Locn Plant", 
								  "Locn Filler", 
								  "Reason", 
								  "Defect Type", 
								  "Defect ID", 
								  "Leaking", 
								  "Sample Qty", 
								  "Material", 
								  "Process Order", 
								  "MHN Number",
								  "Sample Date",
								  "Seq",
								  "Operative"
								  ,"Lane 1"
								  ,"Lane 2"
								  ,"Lane 3"
								  ,"Lane 4"
								  ,"Lane 5"};
	
	private ResultSet mResultSet;

	private int prowCount = -1;

	private HashMap<Integer,JDBViewPalletSample> cache = new HashMap<Integer,JDBViewPalletSample>();

	public JDBViewPalletSampleTableModel(ResultSet rs)
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
		int count = mcolNames.length;
		return count;
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
				final JDBViewPalletSample prow = new JDBViewPalletSample(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case SSCC_Col:
				return cache.get(row).getSSCC();
			case Sample_Seq:
				return cache.get(row).getSampleSequence();
			case Production_Date:
				return cache.get(row).getProductionDate().toString().substring(0, 16);
			case Sample_Date:
				return cache.get(row).getSampleDate().toString().substring(0, 16);
			case Location_Col:
				return cache.get(row).getLocation();
			case SamplePoint_Col:
				return cache.get(row).getSamplePoint();
			case Sample_Reason:
				return cache.get(row).getSampleReasonID();
			case Sample_DefectType:
				return cache.get(row).getSampleDefectType();
			case Sample_DefectID:
				return cache.get(row).getSampleDefectID();
			case Leaking:
				return cache.get(row).isLeaking();
			case SampleQuantity_Col:
				return cache.get(row).getSampleQuantityt();	
			case Material_Col:
				return cache.get(row).getMaterialID();	
			case Process_Order_Col:
				return cache.get(row).getProcessOrder();
			case MHN_number:
				return cache.get(row).getMHNNumber();
			case Operative_Seq:
				return cache.get(row).getOperative();
				
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
