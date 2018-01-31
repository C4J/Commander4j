package com.commander4j.idoc;

import java.util.ArrayList;
import java.util.HashMap;

public class OutputData {
	
	private ArrayList<DataSegment> DataSegments;
	private String msgId;
	
	public OutputData()
	{
		DataSegments = new ArrayList<DataSegment>();
	}
	
	private int GetSegmentIndexByName(String name) throws NullPointerException
	{
		if (name.equals(""))
			throw new NullPointerException("Name cannot be blank");
		
		for (DataSegment d : DataSegments)
		{
			if (d.SegmentName.equals(name))
				return DataSegments.indexOf(d);
		}
		
		throw new NullPointerException("Segment not found: Segment " + name);
	}
	
	private int GetSegmentIndexByKeyAndValue(String segment, String key, String value) throws NullPointerException
	{
		if (DataSegments == null || DataSegments.size() == 0)
			throw new NullPointerException("Contains no data segments");
		
		for (DataSegment d : DataSegments)
		{
			if (d.HasKeyAndValue(segment, key, value))
				return DataSegments.indexOf(d);
		}
		
		throw new NullPointerException("Data segment not found: Segment " + segment + ",Key " + key + ",Value " + value);
	}
	
	private int GetSegmentIndexByKeys(String segment, HashMap<String,String> values) throws NullPointerException
	{
		if (DataSegments == null || DataSegments.size() == 0)
			throw new NullPointerException("Contains no data segments");
		
		
		int segmentPos = 0;
		for (DataSegment d : DataSegments)
		{
			int keyCount = 0;
			for (String k : values.keySet())
			{
				
				if (d.HasKeyAndValue(segment, k, values.get(k)))
				{	
					keyCount++;
					segmentPos = DataSegments.indexOf(d);
				}
				
			}
			if (keyCount == values.size())
				return segmentPos;
		}
		
		
		
		throw new NullPointerException("Data segment not found: Segment " + segment + ",Multiple Keys");
	}
	
	public void AddDataSegment(String name)
	{
		boolean foundSegment = false;
		if (DataSegments.size() > 0)
		{
			for (DataSegment d : DataSegments)
		
			{
				if (d.SegmentName.equals(name))
				{	
					foundSegment = true;
					break;
				}
			}
		}
		if (!foundSegment)
			DataSegments.add(new DataSegment(name));
	}
	
	public void AddDataSegmentWithKey(String name, String key, String value)
	{
		DataSegment s = new DataSegment(name);
		s.Properties.put(key, value);
		DataSegments.add(s);
	}
	
	public void AddDataSegmentWithKeys(String name, HashMap<String,String> keys)
	{
		DataSegment s = new DataSegment(name);
		s.Properties.putAll(keys);
		DataSegments.add(s);
	}
	
	public void AddValueToSegment(String segment, String key, String value)
	{
		int segPos = GetSegmentIndexByName(segment);
		DataSegments.get(segPos).Properties.put(key, value);
		
	}
	

	public void AddValueToSegment(String segment, String key, String value, String segmentKey, String segmentValue)
	{
		int segPos = GetSegmentIndexByKeyAndValue(segment, segmentKey, segmentValue);
		DataSegments.get(segPos).Properties.put(key, value);
	}
	
	public void AddValueToSegment(String segment, String key, String value, HashMap<String,String> subKeys)
	{
		int segPos = GetSegmentIndexByKeys(segment,subKeys);
		DataSegments.get(segPos).Properties.put(key,value);
	}
	
	public String GetValueFromSegment(String segment, String key)
	{
		String output = "";
		int segPos = GetSegmentIndexByName(segment);
		if (DataSegments.get(segPos).Properties.containsKey(key))
			output = DataSegments.get(segPos).Properties.get(key).toString();
		
		return output;
	}
	
	public String GetValueFromSegment(String segment, String key, String segmentKey, String segmentValue)
	{
		String output = "";
		int segPos = GetSegmentIndexByKeyAndValue(segment,segmentKey,segmentValue);
		if (DataSegments.get(segPos).Properties.containsKey(key))
			output = DataSegments.get(segPos).Properties.get(key).toString();
		
		return output;
	}
	
	public ArrayList<DataSegment> GetDataSegments()
	{
		return DataSegments;
	}
	
	public ArrayList<DataSegment> GetDataSegmentsByName(String segment)
	{
		ArrayList<DataSegment> results = new ArrayList<DataSegment>();
		
		for(DataSegment d: DataSegments)
		{
			if (d.SegmentName.equals(segment))
				results.add(d);
		}
		
		return results;
	}
	
	public String getMsgId()
	{
		return msgId;
	}
	
	public void setMsgId(String value)
	{
		msgId = value;
	}
			
}
