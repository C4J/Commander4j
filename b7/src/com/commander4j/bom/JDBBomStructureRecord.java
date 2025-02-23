package com.commander4j.bom;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;

import com.commander4j.util.JUtility;

public class JDBBomStructureRecord
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBBomStructureRecord.class);
	private String data_id = "";
	private String data_id_parent = "";

	private JDBBomElementRecord element = new JDBBomElementRecord();

	public void clear()
	{
		setDataId("");
		setDataIDParent("");
		element.clear();
	}
	
	public JDBBomElementRecord getElementRecord()
	{
		return element;
	}
			
	
	public void getPropertiesFromResultSet(ResultSet rs)
	{
		try
		{
			setDataId(rs.getString("data_id"));
			setDataIDParent(rs.getString("parent_data_id"));
			element.getPropertiesFromResultSet(rs);

		}
		catch (SQLException e)
		{
			logger.error(e.getMessage());
		}
	}
	
	public String getDataId()
	{
		return data_id;
	}

	public void setDataId(String id)
	{
		this.data_id = JUtility.replaceNullStringwithBlank(id);
	}

	public String getDataIDParent()
	{
		return data_id_parent;
	}

	public void setDataIDParent(String data)
	{
		this.data_id_parent = JUtility.replaceNullStringwithBlank(data);
	}
	
	public String toString()
	{
		return JUtility.padString(getDataIDParent(), true, JDBBomElementRecord.field_data_id, " ")  + " " + JUtility.padString(getDataId(), true, JDBBomElementRecord.field_data_id, " ") ;
	}

}
