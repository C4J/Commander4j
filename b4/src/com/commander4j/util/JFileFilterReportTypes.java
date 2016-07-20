package com.commander4j.util;

import java.io.File;

/**
 */
public class JFileFilterReportTypes
{

	/**
	 * Field JRXML. (value is ""jrxml"") Value: {@value JRXML}
	 */
	public final static String JRXML = "jrxml";

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
