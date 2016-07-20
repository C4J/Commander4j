package com.commander4j.util;

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
