package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class JDBQueryTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1L;
	private Vector<Object[]> cache; 
	private int colCount;
	private String[] headers;
	private ResultSetMetaData meta;

	public String getColumnName(int i)
	{
		return headers[i];
	}

	public int getColumnCount()
	{
		return colCount;
	}

	public int getRowCount()
	{
		return cache.size();
	}

	public Object getValueAt(int row, int col)
	{
		return ((Object[]) cache.elementAt(row))[col];
	}

	public void setQuery(ResultSet rs)
	{
		cache = new Vector<Object[]>();
		try
		{
			meta = rs.getMetaData();
			colCount = meta.getColumnCount();

			headers = new String[colCount];
			for (int h = 1; h <= colCount; h++)
			{
				headers[h - 1] = meta.getColumnName(h);
			}

			while (rs.next())
			{
				Object[] record = new Object[colCount];
				for (int i = 0; i < colCount; i++)
				{
					record[i] = rs.getObject(i + 1);
				}
				cache.addElement(record);
			}
			fireTableChanged(null); // notify everyone that we have a new table.
		} catch (Exception e)
		{
			cache = new Vector<Object[]>(); // blank it out and keep going.
			e.printStackTrace();
		}
	}
}