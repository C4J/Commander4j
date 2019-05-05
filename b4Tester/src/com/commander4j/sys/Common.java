package com.commander4j.sys;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

import com.commander4j.b4Tester.Test;

public class Common
{
	public static String logDir = "";
	public static int verboseLevel = 0;
	
	public static String testName = "";
	public static String rootPath = "";
	public static String inputPath = "";
	public static String samplePath = "";
	public static String outputPath = "";
	public static String referencePath = "";
	public static HashMap<String,LinkedList<String>> excludeList = new HashMap<String,LinkedList<String>>(); 
	public static SortedMap<String, Test> testRig  = new TreeMap<String,Test>();
}
