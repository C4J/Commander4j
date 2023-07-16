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
	private boolean cacheImages16x16 = true;
	private HashMap<String, ImageIcon> imageIndex16x16 = new HashMap<String, ImageIcon>();
	private boolean cacheImages24x24 = true;
	private HashMap<String, ImageIcon> imageIndex24x24 = new HashMap<String, ImageIcon>();
	private boolean cacheImages32x32 = true;
	private HashMap<String, ImageIcon> imageIndex32x32 = new HashMap<String, ImageIcon>();

	public JImageIconLoader()
	{
		enableCaches();
	}

	public void clearCaches() {
		imageIndex16x16.clear();
		imageIndex24x24.clear();
		imageIndex32x32.clear();
	}

	public void disableCaches() {
		cacheImages16x16 = false;
		cacheImages24x24 = false;
		cacheImages32x32 = false;
	}

	public void enableCaches() {
		cacheImages16x16 = true;
		cacheImages24x24 = true;
		cacheImages32x32 = true;
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
	public ImageIcon getImageIcon16x16(String filename) {
		ImageIcon result = null;

		// $hide>>$
		boolean readfromfile = false;

		if (isCacheEnabled16x16())
		{
			if (imageIndex16x16.containsKey(filename))
			{
				result = imageIndex16x16.get(filename);
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

				result = new ImageIcon(Common.image_path_16x16 + filename);
				if (isCacheEnabled16x16())
				{
					imageIndex16x16.put(filename, result);
				}
			}
			catch (Exception e)
			{
				result = new ImageIcon(Common.image_path_16x16 + Common.image_error);
			}
		}
		// $hide<<$

		return result;

	}
	
	public ImageIcon getImageIcon24x24(String filename) {
		ImageIcon result = null;

		// $hide>>$
		boolean readfromfile = false;

		if (isCacheEnabled24x24())
		{
			if (imageIndex24x24.containsKey(filename))
			{
				result = imageIndex24x24.get(filename);
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

				result = new ImageIcon(Common.image_path_24x24 + filename);
				if (isCacheEnabled24x24())
				{
					imageIndex24x24.put(filename, result);
				}
			}
			catch (Exception e)
			{
				result = new ImageIcon(Common.image_path_24x24 + Common.image_error);
			}
		}
		// $hide<<$

		return result;

	}

	public ImageIcon getImageIcon32x32(String filename) {
		ImageIcon result = null;

		// $hide>>$
		boolean readfromfile = false;

		if (isCacheEnabled32x32())
		{
			if (imageIndex32x32.containsKey(filename))
			{
				result = imageIndex32x32.get(filename);
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

				result = new ImageIcon(Common.image_path_32x32 + filename);
				if (isCacheEnabled32x32())
				{
					imageIndex32x32.put(filename, result);
				}
			}
			catch (Exception e)
			{
				result = new ImageIcon(Common.image_path_32x32 + Common.image_error);
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
	public boolean isCacheEnabled16x16() {
		return cacheImages16x16;
	}
	
	public boolean isCacheEnabled24x24() {
		return cacheImages24x24;
	}
	
	public boolean isCacheEnabled32x32() {
		return cacheImages32x32;
	}

}
