package com.commander4j.notifier;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.commander4j.autolab.StartStop;

public class TrayIconSystemInfo
{

	private TrayIcon trayIcon;

	private Image imageOK;

	private PopupMenu popup = new PopupMenu("c4jAutoLab");

	private MenuItem menuItemEmailConfig = new MenuItem("Email Config");
	private MenuItem menuItemEmailLogs = new MenuItem("Email Logs");
	private MenuItem menuItemSYSINFO = new MenuItem("System Information");
	private MenuItem menuItemABOUT = new MenuItem("About");
	private MenuItem menuItemQUIT = new MenuItem("Quit");

	private static String OS = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);

	ActionListener listener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{

			if (e.getSource().equals(menuItemEmailConfig))
			{
				EmailConfig emailConfig = new EmailConfig();
				emailConfig.sendConfigEmail();
			}

			if (e.getSource().equals(menuItemEmailLogs))
			{
				EmailLogs emailLog = new EmailLogs();
				emailLog.sendLogEmail();
			}
			
			if (e.getSource().equals(menuItemSYSINFO))
			{
				JDialogSysInfo dialog = new JDialogSysInfo();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}

			if (e.getSource().equals(menuItemABOUT))
			{
				JDialogAbout dialog = new JDialogAbout();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}

			if (e.getSource().equals(menuItemQUIT))
			{

				SwingUtilities.invokeLater(() -> {
					String option = JDialogQuit.showDialogAndSelectOption("Do you really want to quit?", "Please confirm", JOptionPane.QUESTION_MESSAGE, "Yes", "No");

					if (option.equals("Yes"))
					{
						StartStop.autolab.requestStop();
					}
				});

			}

		}
	};

	public TrayIcon getTrayIcon()
	{
		return trayIcon;
	}

	public void setTrayIcon(TrayIcon trayIcon)
	{
		this.trayIcon = trayIcon;
	}
		
	public void init()
	{
		
		
		if (SystemTray.isSupported())
		{

			if (isWindows())
			{
				imageOK = Toolkit.getDefaultToolkit().getImage("images/windows/image_sys_control.gif");
			}

			if (isMac())
			{
				imageOK = Toolkit.getDefaultToolkit().getImage("images/mac/image_sys_control.gif");
			}

			if (isUnix())
			{
				imageOK = Toolkit.getDefaultToolkit().getImage("images/linux/image_sys_control.gif");
			}

			popup.add(menuItemABOUT);
			popup.add(menuItemSYSINFO);
			popup.add(menuItemEmailConfig);
			popup.add(menuItemEmailLogs);
			popup.add(menuItemQUIT);

			menuItemEmailConfig.addActionListener(listener);
			menuItemEmailLogs.addActionListener(listener);
			menuItemABOUT.addActionListener(listener);
			menuItemSYSINFO.addActionListener(listener);

			menuItemQUIT.addActionListener(listener);


			
			trayIcon = new TrayIcon(imageOK, "", popup);
			trayIcon.setImageAutoSize(true);

			try
			{
				SystemTray.getSystemTray().add(trayIcon);
			}
			catch (AWTException e)
			{
				System.err.println(e);
			}
		}

	}

	public void removeStatusIcon()
	{
		if (SystemTray.isSupported())
		{
			SystemTray.getSystemTray().remove(trayIcon);
		}
	}
	
	private boolean isWindows()
	{
		return OS.contains("win");
	}

	private boolean isMac()
	{
		return OS.contains("mac");
	}

	private boolean isUnix()
	{
		return OS.contains("nux");
	}

}