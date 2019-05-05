package com.commander4j.b4Tester;

import java.util.HashMap;

public class TestOutput
{
	public String id = "";
	public String description = "";
	public String type="";
	public String outputPath="";
	public String referencePath="";
	public String compareMethod="";
	public String excludeList="";
	public HashMap<String,TestOutputFile> outputFiles = new HashMap<String,TestOutputFile>();

}
