package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JUpdate.java
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

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.install4j.api.context.UserCanceledException;
import com.install4j.api.launcher.ApplicationLauncher;
import com.install4j.api.update.ApplicationDisplayMode;
import com.install4j.api.update.UpdateChecker;

public class JUpdate
{

	public static void updateCheck()
	{
		final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JUpdate.class);

		if (new File(Common.base_dir + java.io.File.separator + ".install4j" + java.io.File.separator + "i4jparams.conf").isFile())
		{
			try
			{
				logger.debug("UpdateChecker.getUpdateDescriptor");

				ApplicationDisplayMode dispmode;

				if (Common.updateMODE.equals("AUTOMATIC"))
				{
					dispmode = ApplicationDisplayMode.UNATTENDED;
				} else
				{
					dispmode = ApplicationDisplayMode.GUI;
				}

				if (UpdateChecker.getUpdateDescriptor(Common.updateURL, dispmode).getPossibleUpdateEntry() != null)

				{

					Boolean applyupdate = true;
					if (Common.updateMODE.equals("MANUAL"))
					{
						int n = JOptionPane.showConfirmDialog(null, "Upgrade available - apply now ?", "Update", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
						if (n == 0)
						{
							applyupdate = true;
						}
						else
						{
							applyupdate = false;
						}
					}

					if (applyupdate)
					{

						String paramsAll[] =
						{ "-VC4JUpdaterUrl=" + Common.updateURL, "-VC4JUpdateMode=" + Common.updateMODE, "-VC4JUpdateDirectory=" + Common.base_dir };

						logger.debug("UpdateMODE=" + Common.updateMODE);

						int size = paramsAll.length;
						for (int x = 0; x < size; x++)
						{
							logger.debug("ParamsAll[" + String.valueOf(x) + "]=" + paramsAll[x].toString());
						}

						logger.debug("ApplicationLauncher.launchApplication");

						ApplicationLauncher.launchApplication("406", paramsAll, false, new ApplicationLauncher.Callback()
						{
							public void exited(int exitValue)
							{
								logger.debug("exited");
							}

							public void prepareShutdown()
							{
								logger.debug("prepareShutdown");
							}

						});

					}
				}

			}

			catch (UserCanceledException e)
			{
				logger.debug("UserCanceledException : " + e.getMessage());
			}

			catch (IOException e)
			{
				logger.debug("IOException : " + e.getMessage());
			}

		} else
		{
			logger.debug("Update check not performed in development enviroment");
		}

	}
}
