package com.commander4j.sys;

import java.util.LinkedList;

import com.commander4j.util.JUtility;

public class JLicenceInfo
{
	public String description;
	public String licenceFilename;
	
	public JLicenceInfo()
	{
		
	}
	
	public JLicenceInfo(String desc,String filename)
	{
		this.description = desc;
		this.licenceFilename=filename;
	}
	
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLicenceFilename()
	{
		return licenceFilename;
	}

	public void setLicenceFilename(String licenceFilename)
	{
		this.licenceFilename = licenceFilename;
	}


	public String toString()
	{
		return JUtility.replaceNullStringwithBlank(description);
	}
	
	public LinkedList<JLicenceInfo> getLicences()
	{
		LinkedList<JLicenceInfo> typeList = new LinkedList<JLicenceInfo>();
		
		String description = "Apache Commons IO";
		String filename = "apache_commons.txt";
		
		JLicenceInfo test = new JLicenceInfo(description,filename);
		typeList.add(test);
		
		return typeList;
	}
	

}
