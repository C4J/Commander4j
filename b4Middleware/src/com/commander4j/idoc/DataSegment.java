package com.commander4j.idoc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author James Farrer
 *
 * Stores an IDOC segment and all its associated values
 *
 */
public class DataSegment {
	public String SegmentName;
	public Map<String,String> Properties;
	
	public DataSegment(String name)
	{
		SegmentName = name;
		Properties = new HashMap<String,String>();
	}
	
	
	
	public boolean HasKeyAndValue(String segmentName, String key, String value)
	{
		
		if (!SegmentName.equals(segmentName))
			return false;
		
		if (!Properties.containsKey(key))
			return false;
		
		if (!Properties.get(key).toString().equals(value))
			return false;
		
		return true;
	}
	
	
}
