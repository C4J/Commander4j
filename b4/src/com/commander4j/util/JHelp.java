package com.commander4j.util;

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

//import edu.stanford.ejalbert.BrowserLauncher;

/**
 */
public class JHelp {

	private String helpURL;
	/**
	 * Field HelpAvailable. Value: {@value HelpAvailable}
	 */
	private static boolean HelpAvailable = false;
	/**
	 * Field desktop. Value: {@value desktop}
	 */
	public static Desktop desktop;

	final Logger logger = Logger.getLogger(JHelp.class);

	public JHelp() {
		init();
	}

	/**
	 * Method setHelpURL.
	 * 
	 * @param value
	 *            String
	 */
	private void setHelpURL(String value) {
		if (value == null) {
			value = "";
		}
		if (value.isEmpty()) {
			helpURL = Common.helpURL;
		} else {
			helpURL = value;
		}
	}

	private void init() {
		try {
			if (Desktop.isDesktopSupported()) {
				desktop = Desktop.getDesktop();
				HelpAvailable = true;
			} else {
				HelpAvailable = false;
			}
		} catch (Exception e) {
			HelpAvailable = false;
		}
	}

	/**
	 * Method enableHelpOnButton.
	 * 
	 * @param button
	 *            JButton
	 * @param helpsetID
	 *            String
	 */
	public void enableHelpOnButton(JButton button, String helpsetID) {
		if (HelpAvailable) {
			try {
				setHelpURL(helpsetID);
				ButtonHandler buttonhandler = new ButtonHandler();
				button.addActionListener(buttonhandler);
			} catch (Exception ex) {
				HelpAvailable = false;
			}
		}
	}

	/**
	 * Method enableHelpOnMenuItem.
	 * 
	 * @param button
	 *            JMenuItem
	 * @param helpsetID
	 *            String
	 */
	public void enableHelpOnMenuItem(JMenuItem button, String helpsetID) {
		if (HelpAvailable) {
			try {
				setHelpURL(helpsetID);
				ButtonHandler buttonhandler = new ButtonHandler();
				button.addActionListener(buttonhandler);
			} catch (Exception ex) {
				HelpAvailable = false;
			}
		}
	}

	/**
	 */
	private class ButtonHandler implements ActionListener {
		/**
		 * Method actionPerformed.
		 * 
		 * @param event
		 *            ActionEvent
		 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
		 */
		public void actionPerformed(ActionEvent event) {
			if (HelpAvailable) {

				try {
					URI uri;
					if (helpURL.contains("http")) {
						uri = new URI(helpURL);
						desktop.browse(uri);

					} else {
						File file = new File(helpURL);
						uri = file.toURI().normalize();
						desktop.browse(uri);

					}
				} catch (Exception ex) {
					JUtility.errorBeep();
					JOptionPane
							.showMessageDialog(Common.mainForm,
									ex.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
