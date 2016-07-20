package com.commander4j.util;

import java.io.File;

/**
 */
public class JFileFilterExecTypes
{
	/**
	 * Field EXE. (value is ""exe"") Value: {@value EXE}
	 */
	public final static String EXE = "exe";
	/**
	 * Field COM. (value is ""com"") Value: {@value COM}
	 */
	public final static String COM = "com";
	/**
	 * Field CMD. (value is ""cmd"") Value: {@value CMD}
	 */
	public final static String CMD = "cmd";
	/**
	 * Field BAT. (value is ""bat"") Value: {@value BAT}
	 */
	public final static String BAT = "bat";
	/**
	 * Field SH. (value is ""sh"") Value: {@value SH}
	 */
	public final static String SH = "sh";
	/**
	 * Field BIN. (value is ""bin"") Value: {@value BIN}
	 */
	public final static String BIN = "bin";

	/*
	 * Get the extension of a file.
	 */
	/**
	 * Method getExtension.
	 * 
	 * @param f
	 *            File
	 * @return String
	 */
	public static String getExtension(File f) {
		String ext = null;
		String filename = f.getName();
		int i = filename.lastIndexOf('.');

		if (i > 0 && i < filename.length() - 1)
		{
			ext = filename.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}
