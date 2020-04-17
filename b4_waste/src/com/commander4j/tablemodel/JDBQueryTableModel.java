package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQueryTableModel.java
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

	public void setQuery(ResultSet rs,String[] colNames)
	{
		cache = new Vector<Object[]>();
		try
		{
			meta = rs.getMetaData();
			colCount = meta.getColumnCount();

			headers = new String[colCount];
			for (int h = 1; h <= colCount; h++)
			{
				if ((h-1)>=colNames.length)
				{
					headers[h - 1] = meta.getColumnName(h);

				}
				else
				{
					headers[h - 1] = colNames[h-1];			
				}
			}
			//headers = colNames;
			
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