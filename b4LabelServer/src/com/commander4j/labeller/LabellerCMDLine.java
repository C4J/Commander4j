package com.commander4j.labeller;

import org.apache.logging.log4j.Logger;

import com.commander4j.util.JUtility;

public class LabellerCMDLine
{
	public String label = "";
	public String command = "";
	public String val = "";
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((LabellerCMDLine.class));
	
	public String toString()
	{
		String result ="";
		
		String t1 = JUtility.padString(label, true, 	18, " ");
		String t2 = JUtility.padString(command, true, 30, " ");
		String t3 = JUtility.padString(val, true, 100, " ");
		
		result = t1+t2+t3;
		
		return result;
	}
}
