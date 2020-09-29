package com.commander4j.notifier;

import java.awt.AWTException;
import java.awt.Frame;
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

import com.commander4j.autolab.AutoLab;
import com.commander4j.prodLine.ProdLine;

public class TrayIconProdLineStatus
{

	private TrayIcon trayIcon;

	private Image imageOK;
	private Image imageWarn;
	private Image imageError;
	private Image imageStartup;
	private Image imageShutdown;
	private Image imageShutdown1;
	private Image imageShutdown2;
	private Image imageShutdown3;
	private Image imageShutdown4;
	private Image imageShutdown5;

	public static final int status_OK = 0;
	public static final int status_WARN = 1;
	public static final int status_ERROR = 2;
	public static final int status_STARTUP = 3;
	public static final int status_SHUTDOWN = 4;
	public static final int status_SHUTDOWN1 = 5;
	public static final int status_SHUTDOWN2 = 6;
	public static final int status_SHUTDOWN3 = 7;
	public static final int status_SHUTDOWN4 = 8;
	public static final int status_SHUTDOWN5 = 9;

	private PopupMenu popup = new PopupMenu("c4jAutoLab");

	private MenuItem menuItemSYSINFO = new MenuItem("Information");
	private MenuItem menuItemORDERINFO = new MenuItem("View Order Info");
	private MenuItem menuItemNOTIFICATION = new MenuItem("View Log");
	
	private JDialogProdLineInfo dialogProdLineInfo;
	private JDialogOrderInfo dialogOrderInfo;


	private String uuid = "";
	private String prodLineName="";

	private static String OS = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);

	ActionListener listener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{

			if (e.getSource().equals(menuItemSYSINFO))
			{
				if (dialogProdLineInfo==null)
				{
					dialogProdLineInfo = new JDialogProdLineInfo(getUuid());
					dialogProdLineInfo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				}
				
				dialogProdLineInfo.setVisible(true);
			}

			if (e.getSource().equals(menuItemORDERINFO))
			{	
				if (dialogOrderInfo==null)
				{
					dialogOrderInfo = new JDialogOrderInfo(getUuid());
					dialogOrderInfo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				}
				else
				{
					dialogOrderInfo.refresh();
				}
				dialogOrderInfo.setVisible(true);
			}
			
			if (e.getSource().equals(menuItemNOTIFICATION))
			{	
				showNotificationWindow();
			}

		}
	};
	
	private void showNotificationWindow()
	{
		((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).prodLineNotify.setState(Frame.NORMAL);
		((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).prodLineNotify.toFront();
	}

	public void appendNotification(String message)
	{
		((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).appendNotification(message);
	}
	
	public void setNotification(String message)
	{
		((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).setNotification(message);
	}
	
	public TrayIcon getTrayIcon()
	{
		return trayIcon;
	}

	public void setTrayIcon(TrayIcon trayIcon)
	{
		this.trayIcon = trayIcon;
	}
	
	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}
	
	public String getProdLineName()
	{
		return prodLineName;
	}

	public void setProdLineName(String prodLineName)
	{
		this.prodLineName = prodLineName;
	}
	
	public void init(String uuid)
	{
		
		setUuid(uuid);
		
		setProdLineName(AutoLab.getProdLine_Name(getUuid()));
		
		if (SystemTray.isSupported())
		{

			if (isWindows())
			{
				imageOK = Toolkit.getDefaultToolkit().getImage("images/windows/image_ok.gif");
				imageWarn = Toolkit.getDefaultToolkit().getImage("images/windows/image_warn.gif");
				imageError = Toolkit.getDefaultToolkit().getImage("images/windows/image_error.gif");
				imageStartup = Toolkit.getDefaultToolkit().getImage("images/windows/image_startup.gif");
				imageShutdown = Toolkit.getDefaultToolkit().getImage("images/windows/image_shutdown.gif");
				imageShutdown1 = Toolkit.getDefaultToolkit().getImage("images/windows/image_shutdown1.gif");
				imageShutdown2 = Toolkit.getDefaultToolkit().getImage("images/windows/image_shutdown2.gif");
				imageShutdown3 = Toolkit.getDefaultToolkit().getImage("images/windows/image_shutdown3.gif");
				imageShutdown4 = Toolkit.getDefaultToolkit().getImage("images/windows/image_shutdown4.gif");
				imageShutdown5 = Toolkit.getDefaultToolkit().getImage("images/windows/image_shutdown5.gif");
			}

			if (isMac())
			{
				imageOK = Toolkit.getDefaultToolkit().getImage("images/mac/image_ok.gif");
				imageWarn = Toolkit.getDefaultToolkit().getImage("images/mac/image_warn.gif");
				imageError = Toolkit.getDefaultToolkit().getImage("images/mac/image_error.gif");
				imageStartup = Toolkit.getDefaultToolkit().getImage("images/mac/image_startup.gif");
				imageShutdown = Toolkit.getDefaultToolkit().getImage("images/mac/image_shutdown.gif");
				imageShutdown1 = Toolkit.getDefaultToolkit().getImage("images/mac/image_shutdown1.gif");
				imageShutdown2 = Toolkit.getDefaultToolkit().getImage("images/mac/image_shutdown2.gif");
				imageShutdown3 = Toolkit.getDefaultToolkit().getImage("images/mac/image_shutdown3.gif");
				imageShutdown4 = Toolkit.getDefaultToolkit().getImage("images/mac/image_shutdown4.gif");
				imageShutdown5 = Toolkit.getDefaultToolkit().getImage("images/mac/image_shutdown5.gif");
			}

			if (isUnix())
			{
				imageOK = Toolkit.getDefaultToolkit().getImage("images/linux/image_ok.gif");
				imageWarn = Toolkit.getDefaultToolkit().getImage("images/linux/image_warn.gif");
				imageError = Toolkit.getDefaultToolkit().getImage("images/linux/image_error.gif");
				imageStartup = Toolkit.getDefaultToolkit().getImage("images/linux/image_startup.gif");
				imageShutdown = Toolkit.getDefaultToolkit().getImage("images/linux/image_shutdown.gif");
				imageShutdown1 = Toolkit.getDefaultToolkit().getImage("images/linux/image_shutdown1.gif");
				imageShutdown2 = Toolkit.getDefaultToolkit().getImage("images/linux/image_shutdown2.gif");
				imageShutdown3 = Toolkit.getDefaultToolkit().getImage("images/linux/image_shutdown3.gif");
				imageShutdown4 = Toolkit.getDefaultToolkit().getImage("images/linux/image_shutdown4.gif");
				imageShutdown5 = Toolkit.getDefaultToolkit().getImage("images/linux/image_shutdown5.gif");
			}

			
			menuItemSYSINFO.setLabel("Information"+" "+AutoLab.getProdLine_Name(getUuid()));
			
			popup.add(menuItemSYSINFO);
			popup.add(menuItemORDERINFO);
			popup.add(menuItemNOTIFICATION);

			menuItemSYSINFO.addActionListener(listener);
			menuItemORDERINFO.addActionListener(listener);
			menuItemNOTIFICATION.addActionListener(listener);
			
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

	public void setStatus(int status, String message)
	{
		switch (status)
		{
		case status_OK:
		{
			setOK();
			break;
		}
		case status_WARN:
		{
			setWarn();
			break;
		}
		
		case status_ERROR:
		{
			setError();
			break;
		}
		case status_STARTUP:
		{
			setStartup();
			break;
		}
		case status_SHUTDOWN:
		{
			setShutdown();
			break;
		}
		case status_SHUTDOWN1:
		{
			setShutdown1();
			break;
		}
		case status_SHUTDOWN2:
		{
			setShutdown2();
			break;
		}
		case status_SHUTDOWN3:
		{
			setShutdown3();
			break;
		}
		case status_SHUTDOWN4:
		{
			setShutdown4();
			break;
		}
		case status_SHUTDOWN5:
		{
			setShutdown5();
			break;
		}
		default:
			break;
		}

		trayIcon.setToolTip("["+getProdLineName()+ "] " +message);

	}

	private void setOK()
	{
		if (SystemTray.isSupported())
		{
			if (trayIcon != null)
			{

				trayIcon.setImage(imageOK);

			}
		}
	}

	private void setWarn()
	{
		if (SystemTray.isSupported())
		{
			if (trayIcon != null)
			{

				trayIcon.setImage(imageWarn);
			}
		}
	}

	private void setError()
	{
		if (SystemTray.isSupported())
		{
			if (trayIcon != null)
			{

				trayIcon.setImage(imageError);
			}
		}
	}

	private void setStartup()
	{
		if (SystemTray.isSupported())
		{
			if (trayIcon != null)
			{

				trayIcon.setImage(imageStartup);
			}
		}
	}

	private void setShutdown()
	{
		if (SystemTray.isSupported())
		{
			if (trayIcon != null)
			{

				trayIcon.setImage(imageShutdown);
			}
		}
	}

	private void setShutdown1()
	{
		if (SystemTray.isSupported())
		{
			if (trayIcon != null)
			{

				trayIcon.setImage(imageShutdown1);
			}
		}
	}

	private void setShutdown2()
	{
		if (SystemTray.isSupported())
		{
			if (trayIcon != null)
			{

				trayIcon.setImage(imageShutdown2);
			}
		}
	}

	private void setShutdown3()
	{
		if (SystemTray.isSupported())
		{
			if (trayIcon != null)
			{

				trayIcon.setImage(imageShutdown3);
			}
		}
	}

	private void setShutdown4()
	{
		if (SystemTray.isSupported())
		{
			if (trayIcon != null)
			{

				trayIcon.setImage(imageShutdown4);
			}
		}
	}

	private void setShutdown5()
	{
		if (SystemTray.isSupported())
		{
			if (trayIcon != null)
			{

				trayIcon.setImage(imageShutdown5);
			}
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