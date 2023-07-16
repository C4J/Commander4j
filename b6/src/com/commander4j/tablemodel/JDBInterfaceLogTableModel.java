package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBInterfaceLogTableModel.java
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

import com.commander4j.db.JDBInterfaceLog;
import com.commander4j.sys.Common;

/**
 */
public class JDBInterfaceLogTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1;

	public static final int interface_log_id_Col = 0;
	public static final int event_time_Col = 1;
	public static final int interface_type_Col = 2;
	public static final int message_ref_Col = 3;
	public static final int message_info_Col = 4;
	public static final int interface_direction_Col = 5;
	public static final int action_Col = 6;
	public static final int message_status_Col = 7;
	public static final int workstation_id_Col = 8;
	public static final int message_error_Col = 9;
	public static final int filename_id_Col = 10;
	public static final int message_date_Col = 11;

	private String[] mcolNames = { "Log ID", "Event Time", "Interface Type", "Message Ref", "Message Info", "Direction", "Action", "Status","Workstation","Result", "Filename"};
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBInterfaceLog> cache = new HashMap<Integer,JDBInterfaceLog>();

	public JDBInterfaceLogTableModel()
	{

	}

	public JDBInterfaceLogTableModel(ResultSet rs)
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
				final JDBInterfaceLog prow = new JDBInterfaceLog(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case event_time_Col:
				return cache.get(row).getEventTime().toString().substring(0, 16);
			case interface_log_id_Col:
				return cache.get(row).getInterfaceLogID();
			case message_ref_Col:
				return cache.get(row).getMessageRef();
			case interface_type_Col:
				return cache.get(row).getInterfaceType();
			case message_info_Col:
				return cache.get(row).getMessageInformation();
			case interface_direction_Col:
				return cache.get(row).getInterfaceDirection();
			case action_Col:
				return cache.get(row).getAction();
			case message_date_Col:
				return cache.get(row).getMessageDate().toString().substring(0, 16);
			case message_status_Col:
				return cache.get(row).getMessageStatus();
			case message_error_Col:
				return cache.get(row).getErrorMessage();
			case workstation_id_Col:
				return cache.get(row).getWorkstationID();
			case filename_id_Col:
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
