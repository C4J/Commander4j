package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBAuditPermissions;
import com.commander4j.sys.Common;


public class JDBAuditPermissionsTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	
	public static final int AuditLogID_Col = 0;
	public static final int EventTime_Col = 1;
	public static final int UserID_Col = 2;
	public static final int EventType_Col = 3;
	public static final int Target_Col = 4;
	public static final int EventAction_Col = 5;
	public static final int Data_Col = 6;
	public static final int Workstation_Col = 7;
	
	// Names of the columns
	private String[] mcolNames = { "Audit Log ID", "Event Time", "User ID", "Event Type" , "Target", "Event Action", "Data", "Workstation"};
	private ResultSet mResultSet;
	private int prowCount = -1;

	private HashMap<Integer,JDBAuditPermissions> cache = new HashMap<Integer,JDBAuditPermissions>();

	public JDBAuditPermissionsTableModel(ResultSet rs)
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
				final JDBAuditPermissions prow = new JDBAuditPermissions(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case AuditLogID_Col:
				return cache.get(row).getAuditLogID();
			case EventTime_Col:
				return cache.get(row).getEventTime().toString().substring(0,16);
			case UserID_Col:
				return cache.get(row).getUserID();
			case EventType_Col:
				return cache.get(row).getTargetType();
			case EventAction_Col:
				return cache.get(row).getTargetAction();
			case Target_Col:
				return cache.get(row).getTargetID();
			case Data_Col:
				return cache.get(row).getTargetData();
			case Workstation_Col:
				return cache.get(row).getTargetWorkstationID();
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
