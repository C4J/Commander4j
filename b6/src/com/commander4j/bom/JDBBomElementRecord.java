package com.commander4j.bom;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.Logger;

import com.commander4j.util.JUtility;

public class JDBBomElementRecord
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBBomElementRecord.class);
	
	private String data_id = "";
	private String data_type = "";
	private String enable_edit = "";
	private String enable_delete = "";
	private String enable_create = "";
	private String enable_duplicate = "";
	private String enable_clipboard = "";
	private String enable_lookup = "";
	private String lookup_sql = "";
	private String lookup_field = "";
	private String icon_filename = "";
	private int 	max_occur_level = 0;
	private String description = "";
	private String imagePath16 = System.getProperty("user.dir") + File.separator + "images" + File.separator + "16x16" + File.separator;
	private ImageIcon icon;
	public static int field_data_id = 20;
	
	public void clear()
	{

		setDataId("");
		setDataType("");
		setEnable_edit("");
		setEnable_delete("");
		setEnable_create("");
		setEnable_duplicate("");
		setEnable_clipboard("");
		setEnable_lookup("");
		setLookup_sql("");
		setLookup_field("");
		setIcon_filename("");
		setMax_occur_level(0);
		setDescription("");
	}
	
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getMax_occur_level()
	{
		return max_occur_level;
	}

	public void setMax_occur_level(int max_occur_level)
	{
		this.max_occur_level = max_occur_level;
	}

	public String getIcon_filename()
	{
		return JUtility.replaceNullStringwithBlank(icon_filename);
	}
	
	public ImageIcon getIcon()
	{

		String filename = getIcon_filename();
		
		if (filename.equals(""))
		{
			filename = "default.png";
		}
		
		icon = new ImageIcon(imagePath16 + filename);
		
		return icon;
	}

	public void setIcon_filename(String icon_filename)
	{
		this.icon_filename = icon_filename;
	}

	public String getLookup_sql()
	{
		return lookup_sql;
	}

	public void setLookup_sql(String lookup_sql)
	{
		this.lookup_sql = lookup_sql;
	}

	public String getLookup_field()
	{
		return lookup_field;
	}

	public void setLookup_field(String lookup_field)
	{
		this.lookup_field = lookup_field;
	}

	public void getPropertiesFromResultSet(ResultSet rs)
	{
		try
		{
			setDataId(rs.getString("data_id"));
			setDataType(rs.getString("data_type"));
			setEnable_edit(rs.getString("enable_edit"));
			setEnable_delete(rs.getString("enable_delete"));
			setEnable_create(rs.getString("enable_create"));
			setEnable_duplicate(rs.getString("enable_duplicate"));
			setEnable_clipboard(rs.getString("enable_clipboard"));
			setEnable_lookup(rs.getString("enable_lookup"));
			setLookup_sql(rs.getString("lookup_sql"));
			setLookup_field(rs.getString("lookup_field"));
			setIcon_filename(rs.getString("icon_filename"));
			setMax_occur_level(rs.getInt("max_occur_level"));
			setDescription(rs.getString("description"));
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage());
		}

	}
	
	public String getEnable_lookup()
	{
		return enable_lookup;
	}
	
	public boolean isEnable_lookup()
	{
		boolean result;
		
		if (enable_lookup.toUpperCase().equals("Y"))
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
	
	public String getEnable_edit()
	{
		return enable_edit;
	}
	
	public boolean isEnable_edit()
	{
		boolean result;
		
		if (enable_edit.toUpperCase().equals("Y"))
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
	
	public boolean isEnable_delete()
	{
		boolean result;
		
		if (enable_delete.toUpperCase().equals("Y"))
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
	
	public boolean isEnable_create()
	{
		boolean result;
		
		if (enable_create.toUpperCase().equals("Y"))
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
	
	public boolean isEnable_duplicate()
	{
		boolean result;
		
		if (enable_duplicate.toUpperCase().equals("Y"))
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
	
	public boolean isEnable_clipboard()
	{
		boolean result;
		
		if (enable_clipboard.toUpperCase().equals("Y"))
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}

	public void setEnable_edit(String enable_edit)
	{
		this.enable_edit = enable_edit;
	}
	
	public void setEnable_edit(boolean selected)
	{
		if (selected)
		{
			setEnable_edit("Y");
		}
		else
		{
			setEnable_edit("N");
		}
	}
	
	public void setEnable_lookup(String enable_lookup)
	{
		this.enable_lookup = enable_lookup;
	}

	public void setEnable_lookup(boolean selected)
	{
		if (selected)
		{
			setEnable_lookup("Y");
		}
		else
		{
			setEnable_lookup("N");
		}
	}
	
	public String getEnable_delete()
	{
		return enable_delete;
	}

	public void setEnable_delete(String enable_delete)
	{
		this.enable_delete = enable_delete;
	}
	
	public void setEnable_delete(boolean selected)
	{
		if (selected)
		{
			setEnable_delete("Y");
		}
		else
		{
			setEnable_delete("N");
		}
	}

	public String getEnable_create()
	{
		return enable_create;
	}

	public void setEnable_create(String enable_create)
	{
		this.enable_create = enable_create;
	}
	
	public void setEnable_create(boolean selected)
	{
		if (selected)
		{
			setEnable_create("Y");
		}
		else
		{
			setEnable_create("N");
		}
	}

	public String getEnable_duplicate()
	{
		return enable_duplicate;
	}

	public void setEnable_duplicate(String enable_duplicate)
	{
		this.enable_duplicate = enable_duplicate;
	}
	
	public void setEnable_duplicate(boolean selected)
	{
		if (selected)
		{
			setEnable_duplicate("Y");
		}
		else
		{
			setEnable_duplicate("N");
		}
	}

	public String getEnable_clipboard()
	{
		return enable_clipboard;
	}

	public void setEnable_clipboard(String enable_clipboard)
	{
		this.enable_clipboard = enable_clipboard;
	}
	
	public void setEnable_clipboard(boolean selected)
	{
		if (selected)
		{
			setEnable_clipboard("Y");
		}
		else
		{
			setEnable_clipboard("N");
		}
	}
	
	public String getDataType()
	{
		return data_type;
	}

	public void setDataType(String data)
	{
		this.data_type = JUtility.replaceNullStringwithBlank(data);
	}

	public String getDataId()
	{
		return data_id;
	}

	public void setDataId(String id)
	{
		this.data_id = JUtility.replaceNullStringwithBlank(id);
	}
	
	public String toString()
	{
		return getDataId();
	}

}
