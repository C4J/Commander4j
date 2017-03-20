package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBJourneyRefTableModel.java
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

import com.commander4j.db.JDBJourney;
import com.commander4j.sys.Common;

public class JDBJourneyRefTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int Journey_Col = 0;
	public static final int Location_Col = 1;
	public static final int Status_Col = 2;
	public static final int Despatch_Col = 3;
	public static final int Timeslot_Col = 4;
	public static final int LoadType_Col = 5;
	public static final int LoadTypeDesc_Col = 6;
	public static final int Haulier_Col = 7;
	public static final int Updated_Col = 8;

	private String[] mcolNames = { "Journey Ref", "To Location","Status", "Despatch No","Time Slot" ,"Load Type","Type Description","Haulier","Updated"};
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBJourney> cache = new HashMap<Integer,JDBJourney>();

	public JDBJourneyRefTableModel(ResultSet rs)
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
				final JDBJourney prow = new JDBJourney(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case Journey_Col:
				return cache.get(row).getJourneyRef();
			case Status_Col:
				return cache.get(row).getStatus();
			case LoadType_Col:
				return cache.get(row).getLoadType();
			case LoadTypeDesc_Col:
				return cache.get(row).getLoadTypeDesc();
			case Haulier_Col:
				return cache.get(row).getHaulier();
			case Location_Col:
				return cache.get(row).getLocationTo();				
			case Despatch_Col:
				return cache.get(row).getDespatchNo();
			case Timeslot_Col:
				String result2 = "";
				try
				{
					result2 = cache.get(row).getTimeslot().toString().substring(0, 16);
				}
				catch (Exception ex)
				{
					result2 = "";
				}
				return result2;
			case Updated_Col:
				String result3 = "";
				try
				{
					result3 = cache.get(row).getDateUpdated().toString().substring(0, 16);
				}
				catch (Exception ex)
				{
					result3 = "";
				}
				return result3;				
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
