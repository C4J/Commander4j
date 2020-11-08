package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JSplashScreenUtils.java
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

import com.commander4j.sys.Common;
import com.commander4j.sys.JWindowProgress;
import com.commander4j.sys.JWindowSplash;

public class JSplashScreenUtils
{

	public static void hide()
	{
		if (Common.displaySplashScreen)
		{
			if (Common.splash != null)
			{
				Common.splash.setVisible(false);
			}
			if (Common.progress != null)
			{
				Common.progress.setVisible(false);
			}
		}
	}

	public static void show()
	{
		if (Common.displaySplashScreen)
		{
			if (Common.splash != null)
			{
				Common.splash.setVisible(true);
			}
			if (Common.progress != null)
			{
				Common.progress.setVisible(true);
			}
		}
	}

	public static void updateProgress(int progress, String message)
	{
		if (Common.displaySplashScreen)
		{
			if (Common.progress != null)
			{
				JWait.milliSec(Common.splashDelay);
				Common.progress.update(progress, message);
			}
		}
	}

	public static void remove()
	{
		if (Common.displaySplashScreen)
		{
			Common.splash.dispose();
			Common.progress.dispose();
			Common.splash = null;
			Common.progress = null;
		}

	}

	public static void create()
	{
		if (Common.displaySplashScreen)
		{
			Common.splash = new JWindowSplash();
			Common.progress = new JWindowProgress(Common.splash.getImageDimensions());
		}
	}

}
