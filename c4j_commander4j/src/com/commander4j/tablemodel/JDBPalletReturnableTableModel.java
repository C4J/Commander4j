package com.commander4j.tablemodel;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JDBPalletReturnableTableModel.java
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

import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBPallet;

/**
 * Lists the quantities which are still available to be returned for a given
 * SSCC, one row per process order / location combination. The data comes from
 * JDBPallet.getReturnableBySSCC which nets the ISSUE/TO rows against the
 * RETURN/FROM rows in APP_PALLET_HISTORY.
 *
 * @see com.commander4j.db.JDBPallet JDBPallet
 */
public class JDBPalletReturnableTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1;

	public static final int returnable_order_col = 0;
	public static final int returnable_location_col = 1;
	public static final int returnable_quantity_col = 2;
	public static final int returnable_uom_col = 3;

	private LinkedList<JDBPallet.Returnable> returnableList = new LinkedList<JDBPallet.Returnable>();

	private String[] colnames =
	{ "Order", "Location", "Returnable", "UOM" };

	public JDBPalletReturnableTableModel(String hostid, String sessionid, LinkedList<JDBPallet.Returnable> list)
	{
		super();

		JDBLanguage lang = new JDBLanguage(hostid, sessionid);

		colnames[returnable_order_col] = lang.get("lbl_Process_Order");
		colnames[returnable_location_col] = lang.get("lbl_Location_ID");
		colnames[returnable_quantity_col] = lang.get("lbl_Pallet_Quantity");
		colnames[returnable_uom_col] = lang.get("lbl_Pallet_UOM");

		if (list != null)
		{
			returnableList = list;
		}
	}

	/**
	 * @param row
	 *            Expects the tables row number - the caller is responsible for
	 *            converting a sorted / filtered view row to a model row.
	 *
	 * @return    Returns the whole record for the specified row so the caller
	 *            does not have to read the individual cells back.
	 */
	public JDBPallet.Returnable getReturnable(int row)
	{
		JDBPallet.Returnable result = null;

		if ((row >= 0) && (row < returnableList.size()))
		{
			result = returnableList.get(row);
		}

		return result;
	}

	public Class<?> getColumnClass(int columnIndex)
	{
		return String.class;
	}

	public int getColumnCount()
	{
		return colnames.length;
	}

	public String getColumnName(int col)
	{
		return colnames[col];
	}

	public int getRowCount()
	{
		return returnableList.size();
	}

	public void setValueAt(Object value, int row, int col)
	{

	}

	public Object getValueAt(int row, int col)
	{
		JDBPallet.Returnable entry = getReturnable(row);

		if (entry == null)
		{
			return new String();
		}

		switch (col)
		{
		case returnable_order_col:
			return entry.processOrderID();
		case returnable_location_col:
			return entry.locationID();
		case returnable_quantity_col:
			return entry.quantity().toPlainString();
		case returnable_uom_col:
			return entry.uom();
		}

		return new String();
	}
}
