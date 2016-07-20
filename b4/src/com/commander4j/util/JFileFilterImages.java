package com.commander4j.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 */
public class JFileFilterImages extends FileFilter
{
	// Accept all directories and all gif, jpg, tiff, or png files.
	/**
	 * Method accept.
	 * 
	 * @param f
	 *            File
	 * @return boolean
	 */
	public boolean accept(File f) {
		if (f.isDirectory())
		{
			return true;
		}

		String extension = JFileFilterImageTypes.getExtension(f);
		if (extension != null)
		{
			if (extension.equals(JFileFilterImageTypes.TIFF) || extension.equals(JFileFilterImageTypes.TIF) || extension.equals(JFileFilterImageTypes.GIF) || extension.equals(JFileFilterImageTypes.JPEG) || extension.equals(JFileFilterImageTypes.JPG)
					|| extension.equals(JFileFilterImageTypes.PNG))
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		return false;
	}

	// The description of this filter
	/**
	 * Method getDescription.
	 * 
	 * @return String
	 */
	public String getDescription() {
		return "Images";
	}

}
