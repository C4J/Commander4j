package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogHosts.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.commander4j.app.JVersion;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JImagePanel4j;
import com.commander4j.gui.JList4j;
import javax.swing.border.EtchedBorder;

public class JDialogHosts2 extends JDialog
{
	private static final long serialVersionUID = 1;
	private final JImagePanel4j jDesktopPane1 = new JImagePanel4j(System.getProperty("user.dir")+File.separator+"images"+File.separator+"16x16"+File.separator+"connection.jpg");
	private JButton4j jButtonClose;
	private JList4j<JHost> jListHosts;
	private JButton4j jButtonConnect;
	private JScrollPane jScrollPane1;
	private LinkedList<JHost> temp = new LinkedList<JHost>();
	DefaultComboBoxModel<JHost> defComboBoxMod = new DefaultComboBoxModel<JHost>();
	ListModel<JHost> jList1Model = defComboBoxMod;

	public JDialogHosts2(JFrame frame)
	{
		super(frame);

		initGUI();

		populateList("");

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		Common.selectedHostID = "Cancel";

		setResizable(false);
		setModal(true);
		setVisible(true);

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jListHosts.requestFocus();
				jListHosts.setSelectedIndex(0);
			}
		});

	}

	private void populateList(String defaultitem)
	{

		jListHosts.setModel(jList1Model);
		jListHosts.setCellRenderer(Common.renderer_list);
		jListHosts.setOpaque(false);
		jListHosts.setBackground(new Color(0, 0, 0, 0));
		jListHosts.setForeground(Color.BLACK);

		JHost hst = new JHost();
		temp = Common.hostList.getHosts();
		for (int j = 0; j < temp.size(); j++)
		{
			hst = (JHost) temp.get(j);
			if (hst.getEnabled().equals("Y"))
			{
				if (hst.getDatabaseParameters().getjdbcDriver().equals("http") == false)
				{
					defComboBoxMod.addElement(hst);
				}
			}
		}

		int sel = defComboBoxMod.getIndexOf(defaultitem);

		if (sel < 0)
		{
			sel = 0;
		}

		jListHosts.setSelectedIndex(sel);

	}

	private void selectHost()
	{
		if (jListHosts.isSelectionEmpty() == false)
		{
			Common.selectedHostID = ((JHost) jListHosts.getSelectedValue()).getSiteNumber();
			dispose();
		}
	}

	private void initGUI()
	{
		try
		{

			setTitle("C4J Ver " + JVersion.getProgramVersion() + " [" + Common.hostVersion + "]");

			Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(normalCursor);

			jDesktopPane1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			jDesktopPane1.setBackground(Common.color_app_window);
			jDesktopPane1.setLayout(null);
			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(232, 189));
			{
				jScrollPane1 = new JScrollPane();
				jScrollPane1.setOpaque(false);
				jScrollPane1.getViewport().setOpaque(false);
				jScrollPane1.setBounds(0, 0, 318, 270);
				jDesktopPane1.add(jScrollPane1);
				{
					ListModel<JHost> jListHostsModel = new DefaultComboBoxModel<JHost>();
					jListHosts = new JList4j<JHost>();

					jScrollPane1.setViewportView(jListHosts);
					jListHosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					jListHosts.addMouseListener(new MouseAdapter()
					{
						public void mouseClicked(MouseEvent evt)
						{
							if (evt.getClickCount() == 2)
							{
								selectHost();
							}
						}
					});
					jListHosts.setModel(jListHostsModel);
				}
			}
			{

				jButtonConnect = new JButton4j(Common.icon_connect_16x16);
				jButtonConnect.setBounds(48, 281, 110, 32);
				jDesktopPane1.add(jButtonConnect);
				jButtonConnect.setText("Connect");
				jButtonConnect.setMnemonic(java.awt.event.KeyEvent.VK_N);
				jButtonConnect.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						selectHost();
					}
				});
			}
			{
				jButtonClose = new JButton4j(Common.icon_close_16x16);
				jButtonClose.setBounds(164, 281, 110, 32);
				jDesktopPane1.add(jButtonClose);
				jButtonClose.setText("Close");
				jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
				jButtonClose.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						Common.selectedHostID = "Cancel";
						dispose();
					}
				});
			}

			this.setSize(318, 353);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
