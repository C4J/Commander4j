package com.commander4j.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 */
public class JFileFilterExecs extends FileFilter
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

		String extension = JFileFilterExecTypes.getExtension(f);
		if (extension != null)
		{
			if (extension.equals(JFileFilterExecTypes.BAT) || extension.equals(JFileFilterExecTypes.BIN) || extension.equals(JFileFilterExecTypes.CMD) || extension.equals(JFileFilterExecTypes.COM) || extension.equals(JFileFilterExecTypes.EXE)
					|| extension.equals(JFileFilterExecTypes.SH))
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
		return "Executables";
	}

}
