package com.commander4j.compare;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JCompareIgnoreEntry.java
 *
 * Package Name : com.commander4j.compare
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

/**
 * Represents a single entry in the compare ignore list.
 * The DB type pair is stored in alphabetical order so that the same pair
 * entered in either direction compares as identical (e.g. MySQL/SQL Server
 * is treated the same as SQL Server/MySQL).
 */
public class JCompareIgnoreEntry
{
	private final String dbType1;  // alphabetically first of the pair
	private final String dbType2;  // alphabetically second of the pair
	private final String tableName;
	private final String fieldName;

	public JCompareIgnoreEntry(String dbTypeA, String dbTypeB, String tableName, String fieldName)
	{
		// Always store the pair sorted so the pair is direction-independent
		if (dbTypeA.compareToIgnoreCase(dbTypeB) <= 0)
		{
			this.dbType1 = dbTypeA;
			this.dbType2 = dbTypeB;
		}
		else
		{
			this.dbType1 = dbTypeB;
			this.dbType2 = dbTypeA;
		}
		this.tableName = tableName.toUpperCase();
		this.fieldName = fieldName.toUpperCase();
	}

	public String getDbType1()   { return dbType1; }
	public String getDbType2()   { return dbType2; }
	public String getTableName() { return tableName; }
	public String getFieldName() { return fieldName; }

	/**
	 * Returns true if this entry matches the given DB type pair, table and field.
	 * The DB type pair comparison is order-independent.
	 */
	public boolean matches(String dtA, String dtB, String table, String field)
	{
		String a, b;
		if (dtA.compareToIgnoreCase(dtB) <= 0) { a = dtA; b = dtB; }
		else { a = dtB; b = dtA; }
		return dbType1.equalsIgnoreCase(a)
			&& dbType2.equalsIgnoreCase(b)
			&& tableName.equalsIgnoreCase(table)
			&& fieldName.equalsIgnoreCase(field);
	}

	@Override
	public String toString()
	{
		return dbType1 + " / " + dbType2 + " | " + tableName + " | " + fieldName;
	}
}
