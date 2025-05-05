package com.commander4j.c4jWS;

import org.apache.xalan.xsltc.runtime.Hashtable;

import com.commander4j.util.JHostList;
import com.commander4j.util.JSessionData;

public class Common
{
	public static int sessionCount = 0;
	public static String applicationMode ="Servlet";
	public static JHostList hostList = new JHostList();
	public static String updateMODE = "";
	public static String hostVersion = "";
	public static String hostUpdatePath = "";
	public static String updateURL = "";
	public static Hashtable paths = new Hashtable();
	public static JSessionData sd = new JSessionData();
	public static String base_dir = "";
	public static String updateInstallDir = "";
	public static String setupPassword = "";
	public static boolean displaySplashScreen = false;
	public static String encryptionKey = "C0mm4nd3r4jP455w";
	public static String selectedHostID = "";
}
