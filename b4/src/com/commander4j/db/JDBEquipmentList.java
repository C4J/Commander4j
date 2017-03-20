package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBEquipmentList.java
 * 
 * Package Name : com.commander4j.db
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

import com.commander4j.util.JUtility;

public class JDBEquipmentList
{
	private String equipmentID = "";
	private int count = 0;

	JDBEquipmentList(String equip, int qty)
	{
		setEquipmentID(equip);
		setCount(qty);

	}

	public int getCount() {
		return count;
	}

	public String getEquipmentID() {
		return equipmentID;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setEquipmentID(String equipmentID) {
		this.equipmentID = JUtility.replaceNullStringwithBlank(equipmentID);
	}

}
