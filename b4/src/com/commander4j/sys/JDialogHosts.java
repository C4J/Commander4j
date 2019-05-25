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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.commander4j.app.JVersion;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class JDialogHosts extends JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JList4j<JHost> jListHosts;
	private JButton4j jButtonConnect;
	private JScrollPane jScrollPane1;
	private LinkedList<JHost> temp = new LinkedList<JHost>();

	public JDialogHosts(JFrame frame)
	{
		super(frame);
		
		Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(normalCursor);
		
		initGUI();

		setTitle("C4J Ver "+JVersion.getProgramVersion()+" ["+Common.hostVersion+"]");

		populateList("");

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		Common.selectedHostID = "Cancel";
		setResizable(false);
		setModal(true);
		setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jListHosts.requestFocus();
				jListHosts.setSelectedIndex(0);
			}
		});


	}

	private void populateList(String defaultitem) {
		DefaultComboBoxModel<JHost> defComboBoxMod = new DefaultComboBoxModel<JHost>();

		JHost hst = new JHost();
		temp = Common.hostList.getHosts();
		for (int j = 0; j < temp.size(); j++)
		{
			hst = (JHost) temp.get(j);
			if (hst.getEnabled().equals("Y"))
			{
				if (hst.getDatabaseParameters().getjdbcDriver().equals("http")==false)
				{
					defComboBoxMod.addElement(hst);
				}
			}
		}
		int sel = defComboBoxMod.getIndexOf(defaultitem);
		if (sel < 0)
		{
			sel=0;
		}
		ListModel<JHost> jList1Model = defComboBoxMod;
		jListHosts.setModel(jList1Model);
		jListHosts.setCellRenderer(Common.renderer_list);
		jListHosts.setSelectedIndex(sel);
	}

	private void selectHost() {
		if (jListHosts.isSelectionEmpty() == false)
		{
			Common.selectedHostID = ((JHost) jListHosts.getSelectedValue()).getSiteNumber();
			dispose();
		}
	}

	private void initGUI() {
		try
		{
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				jDesktopPane1.setBackground(Common.color_app_window);
				jDesktopPane1.setLayout(null);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(232, 189));
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.setBounds(5, 3, 302, 235);
					jDesktopPane1.add(jScrollPane1);
					{
						ListModel<JHost> jListHostsModel = new DefaultComboBoxModel<JHost>();
						jListHosts = new JList4j<JHost>();
						jListHosts.setSelectedIndices(new int[] {0});
						jListHosts.setBorder(new EmptyBorder(0, 0, 0, 0));
						jScrollPane1.setViewportView(jListHosts);
						jListHosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jListHosts.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
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

					jButtonConnect = new JButton4j(Common.icon_connect);
					jButtonConnect.setBounds(45, 245, 110, 32);
					jDesktopPane1.add(jButtonConnect);
					jButtonConnect.setText("Connect");
					jButtonConnect.setMnemonic(java.awt.event.KeyEvent.VK_N);
					jButtonConnect.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							selectHost();
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jButtonClose.setBounds(161, 245, 110, 32);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText("Close");
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							Common.selectedHostID = "Cancel";
							dispose();
						}
					});
				}
			}
			this.setSize(312, 315);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
