package com.commander4j.tablemodel;

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
	public static final int Updated_Col = 5;

	// Names of the columns
	private String[] mcolNames = { "Journey Ref", "To Location","Status", "Despatch No","Time Slot" ,"Updated"};
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
