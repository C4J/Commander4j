package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBMHN;
import com.commander4j.sys.Common;

/**
 */
public class JDBMHNTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1;

	public static final int MHN_Number_Col = 0;
	public static final int recorder_Col = 1;
	public static final int initiator_Col = 2;
	public static final int reason1_Col = 3;
	public static final int reason2_Col = 4;
	public static final int reason3_Col = 5;
	public static final int status_Col = 6;
	public static final int resource_Col = 7;
	public static final int comment_Col = 8;
	public static final int date_created_Col = 9;
	public static final int date_expected_Col = 10;
	public static final int date_resolved_Col = 11;

	private String[] mcolNames = { "MHN Number", "Recorder", "Initiator","Reason 1","Reason 2","Reason 3", "Status","Resource","Comment", "Date Created", "Date Expected","Date Resolved" };
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBMHN> cache = new HashMap<Integer,JDBMHN>();

	public JDBMHNTableModel()
	{

	}

	public JDBMHNTableModel(ResultSet rs)
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
		String result = "";
		try
		{
			if (cache.containsKey(row)==false)
			{
				mResultSet.absolute(row + 1);
				final JDBMHN prow = new JDBMHN(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case MHN_Number_Col:
				return cache.get(row).getMHNNumber();
			case recorder_Col:
				return cache.get(row).getRecorder();
			case initiator_Col:
				return cache.get(row).getInitiator();
			case reason1_Col:
				return cache.get(row).getReason1();
			case reason2_Col:
				return cache.get(row).getReason2();
			case reason3_Col:
				return cache.get(row).getReason3();
			case date_created_Col:

				try
				{
					result = cache.get(row).getDateCreated().toString().substring(0, 16);
				}
				catch (Exception ex)
				{
					result = "";
				}
				return result;
			case date_expected_Col:
				
				try
				{
					result = cache.get(row).getDateExpected().toString().substring(0, 16);
				}
				catch (Exception ex)
				{
					result = "";
				}
				return result;
			case date_resolved_Col:
				try
				{
					result = cache.get(row).getDateResolved().toString().substring(0, 16);
				}
				catch (Exception ex)
				{
					result = "";
				}
				return result;
			case status_Col:
				return cache.get(row).getStatus();
			case resource_Col:
				return cache.get(row).getResource();
			case comment_Col:
				return cache.get(row).getComments();
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
