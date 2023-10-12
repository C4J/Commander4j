package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameArchiveAdmin.java
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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBArchive;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.tablemodel.JDBArchiveDataTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The class JInternalFrameArchiveAdmin allows you view the data in the
 * SYS_ARCHIVE table. You can then insert/update/delete records from this
 * screen. Edits to data are performed by JDialogArchiveProperties.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameArchiveAdmin.jpg" >
 * 
 * @see com.commander4j.sys.JDialogArchiveProperties JDialogArchiveProperties
 * @see com.commander4j.db.JDBArchive JDBArchive
 */
public class JInternalFrameArchiveAdmin extends javax.swing.JInternalFrame
{
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JScrollPane jScrollPane1;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JTable4j jTable1;
	private String archID;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private PreparedStatement listStatement;

	public JInternalFrameArchiveAdmin()
	{
		super();
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_ARCHIVE"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);

		query.clear();
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}SYS_ARCHIVE order by SEQUENCE,ARCHIVE_ID"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();

		listStatement = query.getPreparedStatement();
	}

	private void populateList(String defaultitem)
	{

		JDBArchive archive = new JDBArchive(Common.selectedHostID, Common.sessionID);
		buildSQL();
		JDBArchiveDataTableModel controltable = new JDBArchiveDataTableModel(archive.getArchiveDataResultSet(listStatement));
		TableRowSorter<JDBArchiveDataTableModel> sorter = new TableRowSorter<JDBArchiveDataTableModel>(controltable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(controltable);

		jScrollPane1.setViewportView(jTable1);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.ArchiveID_Col).setPreferredWidth(130);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.Description_Col).setPreferredWidth(150);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.Enabled_Col).setPreferredWidth(55);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.Background_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.SQLTable_Col).setPreferredWidth(150);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.RetentionDays_Col).setPreferredWidth(55);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.MaxDelete_Col).setPreferredWidth(75);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.RetentionExpiryDate_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.Sequence_Col).setPreferredWidth(55);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.Result_Col).setPreferredWidth(200);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.Run_Start_Col).setPreferredWidth(125);
		jTable1.getColumnModel().getColumn(JDBArchiveDataTableModel.Run_End_Col).setPreferredWidth(125);
		jScrollPane1.repaint();
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(669, 341));
			this.setBounds(0, 0, 999, 348);
			setVisible(true);
			this.setClosable(true);
			this.getContentPane().setLayout(null);
			this.setIconifiable(true);

			{
				jScrollPane1 = new JScrollPane();
				jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
				this.getContentPane().add(jScrollPane1);
				jScrollPane1.setBounds(3, 3, 986, 267);
				jScrollPane1.setFont(Common.font_std);
				{
					jTable1 = new JTable4j();
					jTable1.getTableHeader().setBounds(0, 0, 629, 16);

					jTable1.getTableHeader().setFont(Common.font_table_header);
					jTable1.getTableHeader().setForeground(Common.color_tableHeaderFont);
					jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
					jTable1.setDefaultRenderer(Integer.class, Common.renderer_table);
					jTable1.setDefaultRenderer(Boolean.class, Common.renderer_table);
					jTable1.setFont(Common.font_list);
					jTable1.setForeground(Common.color_listFontStandard);
					jTable1.addMouseListener(new MouseAdapter()
					{
						public void mouseClicked(MouseEvent evt)
						{
							if (evt.getClickCount() == 2)
							{
								if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_ARCHIVE_EDIT") == true)
								{
									editRecord();
								}
							}
						}
					});
					populateList("");

				}

			}
			{
				jButtonEdit = new JButton4j(Common.icon_edit_16x16);
				this.getContentPane().add(jButtonEdit);
				jButtonEdit.setText(lang.get("btn_Edit"));
				jButtonEdit.setBounds(124, 275, 122, 32);
				jButtonEdit.setMnemonic(lang.getMnemonicChar());
				jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_ARCHIVE_EDIT"));
				jButtonEdit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						editRecord();
					}
				});
			}
			{
				jButtonClose = new JButton4j(Common.icon_close_16x16);
				this.getContentPane().add(jButtonClose);
				jButtonClose.setText(lang.get("btn_Close"));
				jButtonClose.setBounds(862, 275, 122, 32);
				jButtonClose.setMnemonic(lang.getMnemonicChar());
				jButtonClose.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jButtonCloseActionPerformed(evt);
					}
				});
			}
			{

				jButtonAdd = new JButton4j(Common.icon_add_16x16);
				this.getContentPane().add(jButtonAdd);
				jButtonAdd.setText(lang.get("btn_Add"));
				jButtonAdd.setBounds(1, 275, 122, 32);
				jButtonAdd.setMnemonic(lang.getMnemonicChar());
				jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_ARCHIVE_ADD"));
				jButtonAdd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						JDBArchive arch = new JDBArchive(Common.selectedHostID, Common.sessionID);

						archID = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Archive_ID_Input"));
						if (archID != null)
						{
							if (archID.equals("") == false)
							{
								archID = archID.toUpperCase();
								arch.clear();
								if (arch.create(archID) == false)
								{
									JUtility.errorBeep();
									JOptionPane.showMessageDialog(Common.mainForm, arch.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
								} else
								{
									arch.update();
									populateList("");
									JLaunchMenu.runDialog("FRM_ADMIN_ARCHIVE_EDIT", archID);
									populateList("");
								}
							}
						}

					}
				});
			}
			{
				jButtonDelete = new JButton4j(Common.icon_delete_16x16);
				this.getContentPane().add(jButtonDelete);
				jButtonDelete.setText(lang.get("btn_Delete"));
				jButtonDelete.setBounds(247, 275, 122, 32);
				jButtonDelete.setMnemonic(lang.getMnemonicChar());
				jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_ARCHIVE_DELETE"));
				jButtonDelete.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						int row = jTable1.getSelectedRow();
						if (row >= 0)
						{

							archID = jTable1.getValueAt(row, 0).toString();

							int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Archive_ID_Delete") + " " + archID + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
							if (n == 0)
							{
								JDBArchive c = new JDBArchive(Common.selectedHostID, Common.sessionID);
								c.setArchiveID(archID);
								c.delete();
								populateList("");
							}
						}
					}
				});
			}
			{
				jButtonPrint = new JButton4j(Common.icon_report_16x16);
				this.getContentPane().add(jButtonPrint);
				jButtonPrint.setText(lang.get("btn_Print"));
				jButtonPrint.setBounds(616, 275, 122, 32);
				jButtonPrint.setMnemonic(lang.getMnemonicChar());
				jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_ARCHIVE"));
				jButtonPrint.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						JLaunchReport.runReport("RPT_ARCHIVE", null, "", null, "");
					}
				});
			}
			{
				jButtonHelp = new JButton4j(Common.icon_help_16x16);
				this.getContentPane().add(jButtonHelp);
				jButtonHelp.setText(lang.get("btn_Help"));
				jButtonHelp.setBounds(739, 275, 122, 32);
				jButtonHelp.setMnemonic(lang.getMnemonicChar());

			}
			{
				jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
				jButtonExcel.addActionListener(new ActionListener()
				{
					public void actionPerformed(final ActionEvent e)
					{
						JDBArchive arch = new JDBArchive(Common.selectedHostID, Common.sessionID);

						JExcel export = new JExcel();
						buildSQL();
						export.saveAs("archive.xls", arch.getArchiveDataResultSet(listStatement), Common.mainForm);
						populateList("");
					}
				});
				jButtonExcel.setText(lang.get("btn_Excel"));
				jButtonExcel.setMnemonic(lang.getMnemonicChar());
				jButtonExcel.setBounds(493, 275, 122, 32);
				getContentPane().add(jButtonExcel);
			}

			JButton4j button4jRun = new JButton4j(Common.icon_execute_16x16);
			button4jRun.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					int row = jTable1.getSelectedRow();
					if (row >= 0)
					{

						archID = jTable1.getValueAt(row, 0).toString();
						runArchive(archID);
						populateList(archID);
					}
				}
			});
			button4jRun.setText(lang.get("btn_Run"));
			button4jRun.setMnemonic('0');
			button4jRun.setEnabled(false);
			button4jRun.setBounds(370, 275, 122, 32);
			button4jRun.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_ARCHIVE_RUN"));
			getContentPane().add(button4jRun);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void runArchive(String archID)
	{
		JDBArchive c = new JDBArchive(Common.selectedHostID, Common.sessionID);
		c.runManual(this,archID);
	}
	
	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			archID = jTable1.getValueAt(row, 0).toString();

			JLaunchMenu.runDialog("FRM_ADMIN_ARCHIVE_EDIT", archID);
			populateList(archID);
		}
	}

	private void jButtonCloseActionPerformed(ActionEvent evt)
	{
		JDBQuery.closeStatement(listStatement);
		dispose();
	}
}
