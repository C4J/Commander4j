package com.commander4j.sys;

import java.awt.BorderLayout;

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

import java.awt.Cursor;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.commander4j.app.JVersion;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.util.JUtility;

public class JDialogHosts extends JDialog
{
	private JButton4j jButtonClose;
	private JButton4j jButtonConnect;
	private JDesktopPane4j jDesktopPane1;
	private JList4j<JHost> jListHosts;
	private JPanel4j panel;
	private JScrollPane4j scrollPane;
	private LinkedList<JHost> temp = new LinkedList<JHost>();
	private static final long serialVersionUID = 1;
	private static int heightadjustment = 0;
	private static int widthadjustment = 0;

	public JDialogHosts(JFrame frame)
	{
		super(frame);

		Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(normalCursor);

		initGUI();

		widthadjustment = JUtility.getOSWidthAdjustment();
		heightadjustment = JUtility.getOSHeightAdjustment();

		setTitle("C4J Ver " + JVersion.getProgramVersion() + " [" + Common.hostVersion + "]");

		populateList("");

		GraphicsDevice gd = JUtility.getGraphicsDevice();

		GraphicsConfiguration gc = gd.getDefaultConfiguration();

		Rectangle screenBounds = gc.getBounds();

		setBounds(screenBounds.x + ((screenBounds.width - JDialogHosts.this.getWidth()) / 2), screenBounds.y + ((screenBounds.height - JDialogHosts.this.getHeight()) / 2), JDialogHosts.this.getWidth() + widthadjustment,
				JDialogHosts.this.getHeight() + heightadjustment);
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
		DefaultComboBoxModel<JHost> defComboBoxMod = new DefaultComboBoxModel<JHost>();

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
		ListModel<JHost> jList1Model = defComboBoxMod;
		jListHosts.setModel(jList1Model);
		jListHosts.setCellRenderer(Common.renderer_list);
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
			getContentPane().setLayout(null);

			jDesktopPane1 = new JDesktopPane4j();
			jDesktopPane1.setBounds(0, 0, 340, 370);
			jDesktopPane1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

			this.getContentPane().add(jDesktopPane1);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(232, 189));
			jDesktopPane1.setLayout(null);

			jButtonConnect = new JButton4j(Common.icon_connect_16x16);
			jButtonConnect.setBounds(56, 321, 110, 32);
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

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jButtonClose.setBounds(172, 321, 110, 32);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText("Close");
			jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
			panel = new JPanel4j();
			panel.setBorder(null);
			panel.setBounds(0, 0, 325 + widthadjustment, 314 + heightadjustment);
			jDesktopPane1.add(panel);
			panel.setLayout(new BorderLayout(0, 0));
			scrollPane = new JScrollPane4j(JScrollPane4j.List);
			panel.add(scrollPane, BorderLayout.CENTER);
			jListHosts = new JList4j<JHost>();
			jListHosts.setSelectedIndices(new int[]
			{ 0 });
			jListHosts.setBorder(new EmptyBorder(0, 0, 0, 0));
			scrollPane.setViewportView(jListHosts);
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

			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					Common.selectedHostID = "Cancel";
					System.exit(0);
				}
			});

			this.setSize(340, 399);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
