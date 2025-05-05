
package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JStatusBar.java
 * 
 * Package Name : com.commander4j.sys
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import com.commander4j.gui.JLabel4j_std;
import com.commander4j.util.JWait;

class JStatusBar extends JPanel
{
	private static final long serialVersionUID = 1;
	private Font titleFont = new Font("Dialog", Font.BOLD, 12);
	private Font dataFont = new Font("Dialog", Font.PLAIN, 12);
	// private Font timeFont = new Font("Dialog", Font.ITALIC, 12);

	private titlePane statusTitle = new titlePane("Status :");
	public dataPane statusData = new dataPane("");

	private titlePane userTitle = new titlePane("User :");
	private dataPane userData = new dataPane(Common.userList.getUser(Common.sessionID).getUserId());

	private titlePane hostTitle = new titlePane("Host :");
	private dataPane hostData = new dataPane(Common.hostList.getHost(Common.selectedHostID).getSiteDescription());

	private titlePane jdbcServerTitle = new titlePane("Database :");
	// private dataPane jdbcServerData = new
	// dataPane(Common.hosts.get(Common.selectedHost).databaseParameters.jdbcServer+":"+Common.hosts.get(Common.selectedHost).databaseParameters.jdbcPort+":"+Common.hosts.get(Common.selectedHost).databaseParameters.jdbcSID);
	private dataPane jdbcServerData = new dataPane(Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcConnectString());

	private dataPane clock = new dataPane("");
	Calendar rightNow = Calendar.getInstance();
	Date date = rightNow.getTime();
	DateFormat df1 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

	public JStatusBar()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEtchedBorder());

		add(statusTitle);
		statusData.setPreferredSize(new Dimension(200, 20));
		statusData.setHorizontalAlignment(JLabel4j_std.LEFT);
		add(statusData);
		setStatusReady();
		add(new Separator());

		add(userTitle);
		add(userData);
		add(new Separator());

		add(hostTitle);
		add(hostData);
		add(new Separator());

		add(jdbcServerTitle);
		add(jdbcServerData);
		add(new Separator());

		clock.setFont(dataFont);
		add(clock);
		add(new Separator());

		ClockListener clocklistener = new ClockListener();
		new Timer(1000, clocklistener).start();

	}

	public void setStatus(String status) {
		statusData.setText(status);
		JWait.milliSec(1000);
	}

	public void setStatusReady() {
		statusData.setText("Ready");
		JWait.milliSec(1000);
	}

	class Separator extends JLabel4j_std
	{
		private static final long serialVersionUID = 1;

		public Separator()
		{
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			setPreferredSize(new Dimension(1, 23));
		}
	}

	class titlePane extends JLabel4j_std
	{
		private static final long serialVersionUID = 1;

		public titlePane(String text)
		{
			setBackground(Color.lightGray); // Set background color
			setForeground(Color.black);

			setHorizontalAlignment(CENTER); // Center the pane text
			setBorder(BorderFactory.createCompoundBorder());
			setText(text); // Set the text in the pane
			setFont(titleFont); // Set the fixed font
		}
	}

	class dataPane extends JLabel4j_std
	{
		private static final long serialVersionUID = 1;

		public dataPane(String text)
		{
			setBackground(Color.lightGray); // Set background color
			setForeground(Color.black);

			setHorizontalAlignment(CENTER); // Center the pane text
			setBorder(BorderFactory.createCompoundBorder());
			setText(text); // Set the text in the pane
			setFont(dataFont); // Set the fixed font
		}
	}

	public class ClockListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event) {
			rightNow = Calendar.getInstance();
			date = rightNow.getTime();
			clock.setText(df1.format(date));

		}
	}

}
