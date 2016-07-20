package com.commander4j.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 */
public class JFileFilterLabels extends FileFilter
{

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

		String extension = JFileFilterReportTypes.getExtension(f);
		if (extension != null)
		{
			if (extension.equals(JFileFilterLabelTypes.LBL))
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

	/**
	 * Method getDescription.
	 * 
	 * @return String
	 */
	public String getDescription() {
		return "Label Layouts";
	}
}
