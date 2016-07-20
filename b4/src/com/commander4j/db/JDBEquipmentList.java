package com.commander4j.db;

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
