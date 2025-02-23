package com.commander4j.bom;

import com.commander4j.util.JUtility;

public class BomMaterials
{
	public String material = "";
	public String material_uuid = "";
	public String default_location = "";
	public Integer locationsRequired = 0;
	
	public Integer getSortKey()
	{
		Integer result = 0;
		
		if (getDefault_location().equals(""))
		{
			result = 99999;
		}
		else
		{
			result = 0;
		}
		result = result + locationsRequired;
		
		
		return result;
	}
	
	public Integer getLocationsRequired()
	{
		return locationsRequired;
	}

	public void setLocationsRequired(Integer locationsRequired)
	{
		this.locationsRequired = locationsRequired;
	}

	public String getDefault_location()
	{
		return default_location;
	}

	public void setDefault_location(String default_location)
	{
		this.default_location = JUtility.replaceNullStringwithBlank(default_location);
	}

	public String getMaterial()
	{
		return material;
	}

	public void setMaterial(String material)
	{
		this.material = JUtility.replaceNullStringwithBlank(material);
	}

	public String getMaterial_uuid()
	{
		return material_uuid;
	}

	public void setMaterial_uuid(String material_uuid)
	{
		this.material_uuid = JUtility.replaceNullStringwithBlank(material_uuid);
	}

	public String toString()
	{
		return "material=["+material+"] material_uuid=["+material_uuid+"] default_location=["+default_location+"] locationsRequired=["+locationsRequired+"]";
	}
}
