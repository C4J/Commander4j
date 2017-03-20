package com.commander4j.interfaces;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameInterfaceControl.java
 * 
 * Package Name : com.commander4j.interfaces
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;
import com.commander4j.thread.InterfaceThread;
import com.commander4j.util.JUtility;

public class JInternalFrameInterfaceControl extends javax.swing.JInternalFrame
{

	private JList4j<String> Status;

	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;

	final JToggleButton startToggleButton = new JToggleButton(Common.icon_ok);
	private boolean threadsRunning = false;
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	InterfaceThread interfaceThread;

	public JInternalFrameInterfaceControl()
	{
		super();
		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(final InternalFrameEvent e) {
				if (ConfirmExit() == true)
				{
					dispose();
				}
			}
		});
		initGUI();
		interfaceThread = new InterfaceThread(Common.selectedHostID,Common.sessionID);
	}

	private boolean ConfirmExit() {
		boolean result = false;
		int question = JOptionPane.showConfirmDialog(Common.mainForm, "Close Interface ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
		if (question == 0)
		{
			if (threadsRunning)
			{
				shutdownThreads();
				result = true;

			}
			else
			{
				result = true;
			}
		}
		return result;
	}

	private void updateStatusLog(String text) {
		String time = JUtility.now();

		int pos = Status.getModel().getSize();
		int lastIndex = pos - 1;
		model.add(pos, time + " " + text);
		Status.setSelectedIndex(pos);
		if (lastIndex >= 0)
		{
			Status.ensureIndexIsVisible(pos);
		}
		com.commander4j.util.JWait.milliSec(100);
	}

	private void startupThreads() {

		interfaceThread.startupThreads();
		threadsRunning = true;
	}

	private void shutdownThreads() {

		if (threadsRunning)
		{

			interfaceThread.shutdownThreads();
			threadsRunning = false;
		}
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(637, 311));
			this.setBounds(25, 25, 592+Common.LFAdjustWidth, 305+Common.LFAdjustHeight);
			setVisible(true);
			this.setTitle("Message Processing");
			this.setClosable(true);
			this.setIconifiable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jButtonCancel = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setBounds(307, 220, 195, 32);
					jButtonCancel.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if (ConfirmExit() == true)
							{
								shutdownThreads();
								dispose();
							}
						}
					});
				}

				{

					startToggleButton.setSelectedIcon(Common.icon_cancel);
					startToggleButton.setFont(Common.font_btn);
					startToggleButton.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {

							if (startToggleButton.isSelected())
							{
								startToggleButton.setText(lang.get("btn_Interface_Stop"));
								updateStatusLog("Starting threads.");
								startupThreads();
								updateStatusLog("Threads started.");
							}
							else
							{
								startToggleButton.setText(lang.get("btn_Interface_Start"));
								updateStatusLog("Stopping threads.");
								shutdownThreads();
								updateStatusLog("Threads stopped.");
							}
						}
					});
					startToggleButton.setText(lang.get("btn_Interface_Start"));
					startToggleButton.setBounds(100, 220, 195, 32);
					jDesktopPane1.add(startToggleButton);
				}

				{
					final JScrollPane scrollPane = new JScrollPane();
					scrollPane.setBounds(20, 12, 531, 198);
					jDesktopPane1.add(scrollPane);

					{
						Status = new JList4j<String>(model);
						scrollPane.setViewportView(Status);
						Status.setFocusable(false);
						Status.setEnabled(false);
					}
				}

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
