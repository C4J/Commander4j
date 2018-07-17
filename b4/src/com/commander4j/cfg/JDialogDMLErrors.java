package com.commander4j.cfg;

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
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBDDL;
import com.commander4j.db.JDBUpdateRequest;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileFilterXML;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;

/**
 * The JDialogDMLErrors is used to display any SQL errors which are encountered
 * when a database is being created or updated by the Setup4j application.
 * 
 * @see com.commander4j.cfg.JFrameHostAdmin JFrameHostAdmin
 * @see com.commander4j.cfg.JDialogSetupPassword JDialogSetupPassword
 * @see com.commander4j.cfg.Setup Setup
 */
public class JDialogDMLErrors extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JList4j<String> jListErrors;
	private JButton jButtonClose;
	private JScrollPane jScrollPane1;
	private LinkedList<JDBDDL> ddlLocal;
	private JDialogDMLErrors me = this;
	JDBUpdateRequest updateRequest;


	public JDialogDMLErrors(JFrame frame, LinkedList<JDBDDL> ddl,JDBUpdateRequest updrst)
	{
		super(frame);
		initGUI();

		ddlLocal = ddl;
		updateRequest = updrst;
		populateList(ddl);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		setResizable(false);
		setModal(true);
		setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void populateList(LinkedList<JDBDDL> ddl)
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		int s = ddl.size();
		if (s > 0)
		{
			for (int i = 0; i < s; i++)
			{
				if (ddl.get(i).getError().equals("Success"))
				{
					// defComboBoxMod.addElement("Description : "
					// + ddl.get(i).getDescription());
					// defComboBoxMod
					// .addElement("-----------------------------------------------------------------------------------------------------------------");
				} else
				{
					defComboBoxMod.addElement("Schema Version : " + String.valueOf(ddl.get(i).getVersion()));
					defComboBoxMod.addElement("Sequence       : " + String.valueOf(ddl.get(i).getSequence()));
					// defComboBoxMod.addElement("Description : "
					// + ddl.get(i).getDescription());
					defComboBoxMod.addElement("DDL            : " + ddl.get(i).getText());
					defComboBoxMod.addElement("Error Message  : " + ddl.get(i).getError());
					defComboBoxMod.addElement("-----------------------------------------------------------------------------------------------------------------");
				}
			}
		}

		ListModel<String> jList1Model = defComboBoxMod;
		jListErrors.setModel(jList1Model);
		jListErrors.setCellRenderer(Common.renderer_list);

	}

	private void initGUI()
	{
		try
		{
			{
				this.setTitle("DML Errors");
			}
			{
				jDesktopPane1 = new JDesktopPane();
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 0, 956, 441);
					{
						ListModel<String> jListErrorsModel = new DefaultComboBoxModel<String>(new String[]
						{ "Item One", "Item Two" });
						jListErrors = new JList4j<String>();
						jScrollPane1.setViewportView(jListErrors);
						jListErrors.setModel(jListErrorsModel);
					}
				}
				{
					jButtonClose = new JButton(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText("Close");
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setFont(Common.font_btn);
					jButtonClose.setBounds(473, 453, 112, 28);
					
					JButton jButtonSave = new JButton(Common.icon_file_save);
					jButtonSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							saveAs("Errors.txt",me);
						}
					});
					jButtonSave.setText("Save to File");
					jButtonSave.setMnemonic(KeyEvent.VK_S);
					jButtonSave.setFont(new Font("Arial", Font.PLAIN, 11));
					jButtonSave.setBounds(359, 453, 112, 28);
					jDesktopPane1.add(jButtonSave);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
			}
			this.setSize(962, 521);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
						fw.write("*********************************************************************************************************************************************************\n");
						fw.write("Host ID        : "+Common.hostList.getHost(Common.selectedHostID).getSiteNumber()+"\n");
						fw.write("Description    : "+Common.hostList.getHost(Common.selectedHostID).getSiteDescription()+"\n");
						fw.write("Database       : "+Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabase()+"\n");
						fw.write("jdbc Driver    : "+Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver()+"\n");
						fw.write("Server         : "+Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcServer()+"\n");
						fw.write("Port           : "+Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcPort()+"\n");
						fw.write("Connection     : "+Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcConnectString()+"\n");
						fw.write("Updating from Schema Version "+String.valueOf(updateRequest.schema_CURVersion+" to "+String.valueOf(updateRequest.schema_NEWVersion))+"\n");
						fw.write("*********************************************************************************************************************************************************\n\n\n");
						fw.write("---------------------------------------------------------------------------------------------------------------------------------------------------------\n");
						for (int x = 0; x < ddlLocal.size();x++)
						{            
							fw.write("Schema Version : " + ddlLocal.get(x).getVersion()+"\n");
							fw.write("Sequence       : " + String.valueOf(ddlLocal.get(x).getSequence())+"\n");
							fw.write("DDL            : " + ddlLocal.get(x).getText()+"\n");
							fw.write("Error Message  : " + ddlLocal.get(x).getError()+"\n");
							fw.write("---------------------------------------------------------------------------------------------------------------------------------------------------------\n");
							fw.flush();
						}
						fw.write("\nNo of Errors   : " +String.valueOf(ddlLocal.size())+"\n");
						fw.close();
						
						result=true;

					} catch (Exception ex)
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
