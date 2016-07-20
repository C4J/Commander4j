/*
 * Created on 02-Mar-2005
 *
 */
package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBPrinters;
import com.commander4j.sys.Common;

public class JDBPrintersTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1;
	public static final int PrinterID_Col = 0;
	public static final int Group_Col = 1;
	public static final int Description_Col = 2;
	public static final int PrinterType_Col = 3;
	public static final int Language_Col = 4;
	public static final int DPI_Col = 5;	
	public static final int IPAddress_Col = 6;
	public static final int Port_Col = 7;
	public static final int Enabled_Col = 8;
	public static final int Direct_Enable_Col = 9;
	public static final int Export_Enable_Col = 10;
	public static final int Export_Format_Col = 11;
	public static final int Export_Path_Col = 12;

	// Names of the columns
	private String[] mcolNames = { "Printer ID","Group", "Description", "Type","Language","DPI", "IP Address", "Port", "Enabled", "Direct","Export","Format","Path" };
	private ResultSet mResultSet;
	private int prowCount = -1;
	private HashMap<Integer,JDBPrinters> cache = new HashMap<Integer,JDBPrinters>();

	public JDBPrintersTableModel()
	{

	}

	public JDBPrintersTableModel(ResultSet rs)
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
				final JDBPrinters prow = new JDBPrinters(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case PrinterID_Col:
				return cache.get(row).getPrinterID();
			case PrinterType_Col:
				return cache.get(row).getPrinterType();
			case IPAddress_Col:
				return cache.get(row).getIPAddress();
			case Port_Col:
				return cache.get(row).getPort();
			case Description_Col:
				return cache.get(row).getDescription();
			case Language_Col:
				return cache.get(row).getLanguage();
			case DPI_Col:
				return cache.get(row).getDPI();				
			case Group_Col:
				return cache.get(row).getGroupID();				
			case Enabled_Col:
				return cache.get(row).isEnabled();
			case Direct_Enable_Col:
				return cache.get(row).isDirectPrintEnabled();
			case Export_Enable_Col:
				return cache.get(row).isExportEnabled();
			case Export_Format_Col:
				return cache.get(row).getExportFormat();
			case Export_Path_Col:
				return cache.get(row).getExportPath();						
			}

		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}

}
