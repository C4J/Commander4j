package com.commander4j.start;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : Init.java
 * 
 * Package Name : com.commander4j.start
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

import javax.swing.JOptionPane;

import com.commander4j.sys.Common;
import com.commander4j.thread.InterfaceThread;
import com.commander4j.util.JUtility;

public class Init
{

	public static void main(String[] args) {

		Common.base_dir = System.getProperty("user.dir");

		if (args.length == 0)
		{

			String selectedApp = "";
			Object[] applications = { "Desktop", "Interface", "Setup","Clone" };
			selectedApp = (String) JOptionPane.showInputDialog(null, "Application", "Start Commander4j " + com.commander4j.app.JVersion.getProgramVersion(), JOptionPane.PLAIN_MESSAGE, Common.icon_interface_16x16, applications, "Desktop");

			selectedApp = JUtility.replaceNullStringwithBlank(selectedApp);
			args = new String[] { selectedApp };

		}

		for (String s : args)
		{

			if (s.equals("Desktop-NoSplashScreen"))
			{
				Common.displaySplashScreen = false;
				com.commander4j.sys.Start.main(args);
				break;
			}

			if (s.equals("Desktop"))
			{
				com.commander4j.sys.Start.main(args);
				break;
			}

			if (s.equals("Interface"))
			{
				InterfaceThread interfaceThread = new InterfaceThread(args);
				interfaceThread.start();
				for (int w = 0; w <= 600; w++)
				{
					com.commander4j.util.JWait.milliSec(1000);
					System.out.print(String.valueOf(60 - w) + ",");
				}
				System.out.println("");
				interfaceThread.requestStop();
				try
				{
					while (interfaceThread.isAlive())
					{
						interfaceThread.requestStop();
						com.commander4j.util.JWait.milliSec(100);
					}
				}
				catch (Exception ex)
				{
				}

			}

			if (s.equals("Setup"))
			{
				com.commander4j.cfg.Setup.main(args);
				break;
			}
			
			if (s.equals("Clone"))
			{
				com.commander4j.clone.Clone.main(args);
				break;
			}
		}

	}

}
