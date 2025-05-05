package com.commander4j.bom;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;

import com.commander4j.util.JUtility;

public class JDBBomListRecord
{

	private String list_id = "";
	private String item = "";
	private Integer sequence = 0;
	private String enabled = "";
	private String description = "";
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBBomListRecord.class);
	
	public static int field_list_id = 25;
	public static int field_list_item = 50;
	public static int field_list_item_sequence = 5;
	public static int field_list_item_enabled = 1;

	public String getList_id()
	{
		return list_id;
	}

	public void setList_id(String list_id)
	{
		this.list_id = list_id;
	}

	public String getItem()
	{
		return item;
	}

	public void setItem(String item)
	{
		this.item = item;
	}

	public Integer getSequence()
	{
		return sequence;
	}

	public void setSequence(Integer sequence)
	{
		this.sequence = sequence;
	}

	public String getEnabled()
	{
		return enabled;
	}

	public void setEnabled(String enabled)
	{
		this.enabled = enabled;
	}
	
	public void setEnabled(boolean yesno)
	{
		if (yesno)
		{
			this.enabled = "Y";	
		}
		else
		{			
			this.enabled = "N";		
		}
	}
	
	public boolean isEnabled()
	{
		if (this.enabled.equals("Y")==true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void getPropertiesFromResultSet(ResultSet rs)
	{
		try
		{
			setList_id(rs.getString("list_id"));
			setItem(rs.getString("item"));
			setSequence(rs.getInt("sequence"));
			setEnabled(rs.getString("enabled"));

		}
		catch (SQLException e)
		{
			logger.error(e.getMessage());
		}
	}

	public String toString()
	{
		return JUtility.padString(getList_id(), true, field_list_id, " ") +" "+JUtility.padString(getItem(), true, field_list_id, " ")+" "+JUtility.padString(getSequence().toString(), false, field_list_item_sequence, " ");

	}
	
}
