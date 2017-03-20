package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JImageIconLoader.java
 * 
 * Package Name : com.commander4j.util
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import java.util.HashMap;

import javax.swing.ImageIcon;

import com.commander4j.sys.Common;

/**
 */
public class JImageIconLoader
{
	private boolean cacheImages = true;
	private HashMap<String, ImageIcon> imageIndex = new HashMap<String, ImageIcon>();

	public JImageIconLoader()
	{
		enableCache();
	}

	public void clearCache() {
		imageIndex.clear();
	}

	public void disableCache() {
		cacheImages = false;
	}

	public void enableCache() {
		cacheImages = true;
	}

	/**
	 * Method getImageIcon.
	 * 
	 * @return ImageIcon
	 */
	public ImageIcon getImageIcon() {
		ImageIcon result = null;
		return result;

	}

	/**
	 * Method getImageIcon.
	 * 
	 * @param filename
	 *            String
	 * @return ImageIcon
	 */
	public ImageIcon getImageIcon(String filename) {
		ImageIcon result = null;

		// $hide>>$
		boolean readfromfile = false;

		if (isCacheEnabled())
		{
			if (imageIndex.containsKey(filename))
			{
				result = imageIndex.get(filename);
			}
			else
			{
				readfromfile = true;
			}
		}
		else
		{
			readfromfile = true;
		}

		if (readfromfile)
		{
			try
			{

				result = new ImageIcon(Common.image_path + filename);
				if (isCacheEnabled())
				{
					imageIndex.put(filename, result);
				}
			}
			catch (Exception e)
			{
				result = new ImageIcon(Common.image_path + Common.image_error);
			}
		}
		// $hide<<$

		return result;

	}

	/**
	 * Method isCacheEnabled.
	 * 
	 * @return boolean
	 */
	public boolean isCacheEnabled() {
		return cacheImages;
	}

}
