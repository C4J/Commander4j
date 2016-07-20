package com.commander4j.start;

import javax.swing.JOptionPane;

import com.commander4j.sys.Common;
import com.commander4j.thread.InterfaceThread;
import com.commander4j.util.JUtility;

public class Init
{

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Common.base_dir = System.getProperty("user.dir");

		if (args.length == 0)
		{

			String selectedApp = "";
			Object[] applications = { "Desktop", "Interface", "Setup","Clone" };
			selectedApp = (String) JOptionPane.showInputDialog(null, "Application", "Start Commander4j " + com.commander4j.app.JVersion.getProgramVersion(), JOptionPane.PLAIN_MESSAGE, Common.icon_interface, applications, "Desktop");

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
