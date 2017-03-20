package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePrinterSelect.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.util.JPrint;

public class JInternalFramePrinterSelect extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonSelect;
	private JList4j<String> jListPrinters;
	private JScrollPane jScrollPanePrinters;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	/**
	 * Auto-generated main method to display this JInternalFrame inside a new
	 * JFrame.
	 */

	public JInternalFramePrinterSelect()
	{
		super();
		initGUI();
		populateList(JPrint.getPreferredPrinterQueueName());
	}

	private void populateList(String defaultitem) {
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		LinkedList<String> tempPrinterList = JPrint.getPrinterNames();
		for (int j = 0; j < tempPrinterList.size(); j++)
		{
			defComboBoxMod.addElement(tempPrinterList.get(j));
		}
		int sel = defComboBoxMod.getIndexOf(defaultitem);
		ListModel<String> jList1Model = defComboBoxMod;
		jListPrinters.setModel(jList1Model);
		jListPrinters.setSelectedIndex(sel);
		jListPrinters.setCellRenderer(Common.renderer_list);
		if (JPrint.getNumberofPrinters() == 0)
		{
			jButtonSelect.setEnabled(false);
		}
		else
		{
			jButtonSelect.setEnabled(true);
		}
	}

	private void selectQueue() {
		String printerqueuename = ((String) jListPrinters.getSelectedValue()).toString();
		JPrint.setPreferredPrinterQueueName(printerqueuename);
		JPrint.refresh();
		populateList(JPrint.getPreferredPrinterQueueName());
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(637, 311));
			this.setBounds(25, 25, 637, 334);
			setVisible(true);
			this.setTitle("Printer Selection");
			this.setClosable(true);
			this.setIconifiable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jScrollPanePrinters = new JScrollPane();
					jDesktopPane1.add(jScrollPanePrinters);
					jScrollPanePrinters.setBounds(3, 3, 620, 231);
					{
						ListModel<String> jList1Model = new DefaultComboBoxModel<String>(new String[] { "Item One", "Item Two" });
						jListPrinters = new JList4j<String>();
						jScrollPanePrinters.setViewportView(jListPrinters);
						jListPrinters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jListPrinters.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									selectQueue();
								}
							}
						});
						jListPrinters.setModel(jList1Model);
					}
				}
				{
					jButtonSelect = new JButton4j(Common.icon_select);
					jDesktopPane1.add(jButtonSelect);
					jButtonSelect.setText(lang.get("btn_Select"));
					jButtonSelect.setBounds(141, 241, 110, 32);
					jButtonSelect.setMnemonic(lang.getMnemonicChar());
					jButtonSelect.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							selectQueue();
						}
					});
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setFont(Common.font_btn);
					jButtonCancel.setBounds(375, 241, 110, 32);
					jButtonCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setBounds(258, 241, 110, 32);
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JPrint.refresh();
							populateList(JPrint.getPreferredPrinterQueueName());
						}
					});
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
