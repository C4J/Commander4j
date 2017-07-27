package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JHelp.java
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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;

public class JHelp
{

	private String helpURL;

	private static boolean HelpAvailable = false;

	public static Desktop desktop;

	final Logger logger = Logger.getLogger(JHelp.class);

	public JHelp()
	{
		init();
	}

	private void setHelpURL(String value)
	{
		if (value == null)
		{
			value = "";
		}
		if (value.isEmpty())
		{
			helpURL = Common.helpURL;
		} else
		{
			helpURL = value;
		}
	}

	private void init()
	{
		try
		{
			if (Desktop.isDesktopSupported())
			{
				desktop = Desktop.getDesktop();
				HelpAvailable = true;
			} else
			{
				HelpAvailable = false;
			}
		} catch (Exception e)
		{
			HelpAvailable = false;
		}
	}

	public void enableHelpOnButton(JButton button, String helpsetID)
	{
		if (HelpAvailable)
		{
			try
			{
				setHelpURL(helpsetID);
				ButtonHandler buttonhandler = new ButtonHandler();
				button.addActionListener(buttonhandler);
			} catch (Exception ex)
			{
				HelpAvailable = false;
			}
		}
	}

	public void enableHelpOnMenuItem(JMenuItem button, String helpsetID)
	{
		if (HelpAvailable)
		{
			try
			{
				setHelpURL(helpsetID);
				ButtonHandler buttonhandler = new ButtonHandler();
				button.addActionListener(buttonhandler);
			} catch (Exception ex)
			{
				HelpAvailable = false;
			}
		}
	}

	private class ButtonHandler implements ActionListener
	{

		public void actionPerformed(ActionEvent event)
		{
			if (HelpAvailable)
			{

				try
				{
					URI uri;
					if (helpURL.contains("http"))
					{
						uri = new URI(helpURL);
						desktop.browse(uri);

					} else
					{
						File file = new File(helpURL);
						uri = file.toURI().normalize();
						desktop.browse(uri);

					}
				} catch (Exception ex)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public void testHelpURL(String url)
	{
		if (url.isEmpty() == false)
		{

			try
			{
				URI uri;
				if (url.contains("http"))
				{
					uri = new URI(url);
					desktop.browse(uri);

				} else
				{
					File file = new File(url);
					uri = file.toURI().normalize();
					desktop.browse(uri);

				}
			} catch (Exception ex)
			{
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(Common.mainForm, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
