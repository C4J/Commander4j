package com.commander4j.util;

import java.io.File;

/**
 */
public class JFileFilterImageTypes
{
	/**
	 * Field JPEG. (value is ""jpeg"") Value: {@value JPEG}
	 */
	public final static String JPEG = "jpeg";
	/**
	 * Field JPG. (value is ""jpg"") Value: {@value JPG}
	 */
	public final static String JPG = "jpg";
	/**
	 * Field GIF. (value is ""gif"") Value: {@value GIF}
	 */
	public final static String GIF = "gif";
	/**
	 * Field TIFF. (value is ""tiff"") Value: {@value TIFF}
	 */
	public final static String TIFF = "tiff";
	/**
	 * Field TIF. (value is ""tif"") Value: {@value TIF}
	 */
	public final static String TIF = "tif";
	/**
	 * Field PNG. (value is ""png"") Value: {@value PNG}
	 */
	public final static String PNG = "png";

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
