package com.commander4j.util;

import java.io.File;

/**
 */
public class JFileFilterPDFTypes
{
	/**
	 * Field EXE. (value is ""xls"") Value: {@value EXE}
	 */
	public final static String PDF = "pdf";

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
