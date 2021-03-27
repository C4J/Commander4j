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

import com.commander4j.autolab.AutoLab;
import com.commander4j.autolab.StartStop;
import com.commander4j.resources.JRes;

public class TrayIconSystemInfo
{

	private TrayIcon trayIcon;

	private Image imageOK;

	private PopupMenu popup = new PopupMenu("c4jAutoLab");

	private MenuItem menuItemEmailConfig = new MenuItem(JRes.getText("email_config_to_support"));
	private MenuItem menuItemEmailLogs = new MenuItem(JRes.getText("email_logs_to_support"));
	private MenuItem menuItemSYSINFO = new MenuItem(JRes.getText("system_information"));
	private MenuItem menuItemMINIMIZE = new MenuItem(JRes.getText("minimize_all"));
	private MenuItem menuItemRESTORE = new MenuItem(JRes.getText("restore_all"));
	private MenuItem menuItemSaveLayout = new MenuItem(JRes.getText("save_layout"));
	private MenuItem menuItemABOUT = new MenuItem(JRes.getText("about"));
	private MenuItem menuItemQUIT = new MenuItem(JRes.getText("quit"));

	private JDialogSysInfo dialogSysInfo;
	private JDialogAbout dialogAbout;

	private static String OS = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);

	ActionListener listener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{

			if (e.getSource().equals(menuItemEmailConfig))
			{
				SwingUtilities.invokeLater(() -> {
					String option = JDialogQuestion.showDialogAndSelectOption(JRes.getText("email_config_to_support") + " ?", JRes.getText("please_confirm"), JOptionPane.QUESTION_MESSAGE, JRes.getText("yes"), JRes.getText("no"));

					if (option.equals(JRes.getText("yes")))
					{
						EmailConfig emailConfig = new EmailConfig();
						emailConfig.sendConfigEmail();
					}
				});

			}

			if (e.getSource().equals(menuItemEmailLogs))
			{

				SwingUtilities.invokeLater(() -> {
					String option = JDialogQuestion.showDialogAndSelectOption(JRes.getText("email_logs_to_support") + " ?", JRes.getText("please_confirm"), JOptionPane.QUESTION_MESSAGE, JRes.getText("yes"), JRes.getText("no"));

					if (option.equals(JRes.getText("yes")))
					{
						EmailLogs emailLog = new EmailLogs();
						emailLog.sendLogEmail();
					}
				});

			}

			if (e.getSource().equals(menuItemSaveLayout))
			{
				AutoLab.saveWindowLayouts();
			}
			
			if (e.getSource().equals(menuItemMINIMIZE))
			{
				AutoLab.minimiseAll();
			}

			if (e.getSource().equals(menuItemRESTORE))
			{
				AutoLab.restoreAll();
			}

			if (e.getSource().equals(menuItemSYSINFO))
			{

				if (dialogSysInfo == null)
				{
					dialogSysInfo = new JDialogSysInfo();
					dialogSysInfo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				}

				dialogSysInfo.setVisible(true);
			}

			if (e.getSource().equals(menuItemABOUT))
			{
				if (dialogAbout == null)
				{
					dialogAbout = new JDialogAbout();
					dialogAbout.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				}
				
				dialogAbout.setVisible(true);
			}

			if (e.getSource().equals(menuItemQUIT))
			{

				SwingUtilities.invokeLater(() -> {
					String option = JDialogQuestion.showDialogAndSelectOption(JRes.getText("do_you_really_want_to_quit"), JRes.getText("please_confirm"), JOptionPane.QUESTION_MESSAGE, JRes.getText("yes"), JRes.getText("no"));

					if (option.equals(JRes.getText("yes")))
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
			popup.add(menuItemMINIMIZE);
			popup.add(menuItemRESTORE);
			popup.add(menuItemSaveLayout);
			popup.add(menuItemQUIT);

			menuItemEmailConfig.addActionListener(listener);
			menuItemEmailLogs.addActionListener(listener);
			menuItemABOUT.addActionListener(listener);
			menuItemMINIMIZE.addActionListener(listener);
			menuItemRESTORE.addActionListener(listener);
			menuItemSaveLayout.addActionListener(listener);
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