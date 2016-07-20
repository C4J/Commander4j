package com.commander4j.app;

public class JVersion
{

	public static String getProgramVersion() {
		return "5.25";
	}

	public static Double getProgramVersionValue() {
		return Double.valueOf(getProgramVersion());
	}

	public static int getSchemaVersion() {
		return 96;
	}

}
