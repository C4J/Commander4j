package com.commander4j.util;

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
