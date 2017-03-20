/*
 * Created on 02-Mar-2005
 *
 */
package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBInterfaceRequestTableModel.java
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

import com.commander4j.db.JDBInterfaceRequest;
import com.commander4j.sys.Common;

public class JDBInterfaceRequestTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1;
	public static final int EventTime_Col = 0;
	public static final int Interface_RequestID_Col = 1;
	public static final int InterfaceType_Col = 2;
	public static final int WorkstationID_Col = 3;
	public static final int TransactionRef_Col = 4;
	public static final int Status_Col = 5;
	public static final int Mode_Col = 6;
	public static final int Filename_Col = 7;

	// Names of the columns
	private String[] mcolNames = { "Event Time", "Request ID", "Interface Type", "Workstation", "Transaction Ref", "Status", "Mode", "Filaneme" };
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBInterfaceRequest> cache = new HashMap<Integer,JDBInterfaceRequest>();

	public JDBInterfaceRequestTableModel()
	{

	}

	public JDBInterfaceRequestTableModel(ResultSet rs)
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
				final JDBInterfaceRequest prow = new JDBInterfaceRequest(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case EventTime_Col:
				return cache.get(row).getEventTime().toString().substring(0, 19);
			case Interface_RequestID_Col:
				return cache.get(row).getInterfaceRequestID().toString();
			case InterfaceType_Col:
				return cache.get(row).getInterfaceType();
			case WorkstationID_Col:
				return cache.get(row).getWorkstationID();
			case TransactionRef_Col:
				return cache.get(row).getTransactionRef();
			case Status_Col:
				return cache.get(row).getStatus();
			case Mode_Col:
				return cache.get(row).getMode();
			case Filename_Col:
				return cache.get(row).getFilename();
			}

		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}

}
