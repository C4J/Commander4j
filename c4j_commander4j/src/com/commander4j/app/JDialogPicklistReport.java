package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JDialogDMLErrors.java
 *
 * Package Name : com.commander4j.cfg
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import javax.swing.ListModel;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBUpdateRequest;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileFilterXML;

/**
 * The JDialogDMLErrors is used to display any SQL errors which are encountered
 * when a database is being created or updated by the Setup4j application.
 *
 * @see com.commander4j.cfg.JFrameHostAdmin JFrameHostAdmin
 * @see com.commander4j.cfg.JDialogSetupPassword JDialogSetupPassword
 * @see com.commander4j.cfg.Setup Setup
 */
public class JDialogPicklistReport extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	JDBUpdateRequest updateRequest;
	private JButton4j jButtonClose;
	private JDesktopPane4j jDesktopPane1;
	private JDialogPicklistReport me = this;
	private JList4j<String> jListErrors;
	private JScrollPane4j jScrollPane1;
	private LinkedList<String> picklistlLocal;

	public JDialogPicklistReport(JFrame frame, LinkedList<String> picklist)
	{
		super(frame);
		initGUI();

		picklistlLocal = picklist;

		populateList(picklist);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		setResizable(false);
		setModal(true);
		setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void initGUI()
	{
		try
		{

			this.setTitle("Pickist Import Report");

			jDesktopPane1 = new JDesktopPane4j();
			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.List);
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.setBounds(0, 0, 659, 441);
			{
				ListModel<String> jListErrorsModel = new DefaultComboBoxModel<String>(new String[]
				{ "Item One", "Item Two" });
				jListErrors = new JList4j<String>();
				jScrollPane1.setViewportView(jListErrors);
				jListErrors.setModel(jListErrorsModel);
			}

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText("Close");
			jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
			jButtonClose.setBounds(333, 453, 112, 32);

			JButton4j jButtonSave = new JButton4j(Common.icon_file_save_16x16);
			jButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					saveAs("Errors.txt", me);
				}
			});
			jButtonSave.setText("Save to File");
			jButtonSave.setMnemonic(KeyEvent.VK_S);
			jButtonSave.setBounds(219, 453, 112, 32);
			jDesktopPane1.add(jButtonSave);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			this.setSize(666, 530);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void populateList(LinkedList<String> picklist)
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		int s = picklist.size();
		if (s > 0)
		{
			for (int i = 0; i < s; i++)
			{
				if (i == (s - 3))
				{
					defComboBoxMod.addElement("\n");
				}
				defComboBoxMod.addElement(picklist.get(i));

			}
		}

		ListModel<String> jList1Model = defComboBoxMod;
		jListErrors.setModel(jList1Model);
		jListErrors.setCellRenderer(Common.renderer_list);

	}

	public Boolean saveAs(String filename, Component parent)
	{

		Boolean result = false;
		JFileChooser saveTXT = new JFileChooser();

		try
		{
			File f = new File(new File(System.getProperty("user.home")).getCanonicalPath());
			saveTXT.setCurrentDirectory(f);
			saveTXT.addChoosableFileFilter(new JFileFilterXML());
			saveTXT.setSelectedFile(new File(filename));

			int r = saveTXT.showSaveDialog(parent);

			if (r == 0)
			{
				File selectedFile;
				selectedFile = saveTXT.getSelectedFile();
				if (selectedFile != null)
				{
					String exportFilename = selectedFile.getAbsolutePath();
					try
					{
						FileWriter fw = new FileWriter(exportFilename);

						fw.write("---------------------------------------------------------------------\n");
						for (int x = 0; x < picklistlLocal.size(); x++)
						{
							fw.write(picklistlLocal.get(x) + "\n");
							fw.flush();
						}
						fw.write("---------------------------------------------------------------------\n");
						fw.close();

						result = true;

					}
					catch (Exception ex)
					{

					}
					result = true;
				}
			}
			else
			{

			}
		}
		catch (Exception ex)

		{

		}

		return result;
	}
}
