package com.commander4j.util;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.install4j.api.context.UserCanceledException;
import com.install4j.api.launcher.ApplicationLauncher;
import com.install4j.api.update.ApplicationDisplayMode;
import com.install4j.api.update.UpdateChecker;

public class JUpdate
{

	public static void updateCheck()
	{
		final Logger logger = Logger.getLogger(JUpdate.class);

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
						int n = JOptionPane.showConfirmDialog(null, "Upgrade available - apply now ?", "Update", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
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
