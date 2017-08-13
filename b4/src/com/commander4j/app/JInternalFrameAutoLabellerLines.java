package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameAutoLabellerLines.java
 * 
 * Package Name : com.commander4j.app
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBAutoLabeller;
import com.commander4j.db.JDBAutoLabellerResources;
import com.commander4j.db.JDBLabelData;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBPrinterLineMembership;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.tablemodel.JDBAutoLabellerTableModel;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameAutoLabellerLines class allows the user to manage the table
 * APP_AUTO_LABELLER. The Production Line defined within the Auto Labeller table
 * allows the operator to send data to a logical production line which may
 * consist of one or more physical labelling machines. The actual labellers
 * which are assigned to the Production line are defined using the form
 * JDialogAutoLabellerProperties
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameAutoLabellerLines.jpg" >
 * 
 * @see com.commander4j.app.JDialogAutoLabellerProperties
 *      JDialogAutoLabellerProperties
 * @see com.commander4j.db.JDBAutoLabeller JDBAutoLabeller
 *
 */
public class JInternalFrameAutoLabellerLines extends JInternalFrame
{

	private static final long serialVersionUID = 1;
	private JButton4j jButtonClose;
	private JDesktopPane jDesktopPane1;
	private JDBAutoLabeller autolab = new JDBAutoLabeller(Common.selectedHostID, Common.sessionID);
	private JDBPrinterLineMembership plm = new JDBPrinterLineMembership(Common.selectedHostID, Common.sessionID);
	private JDBAutoLabellerResources lres = new JDBAutoLabellerResources(Common.selectedHostID, Common.sessionID);
	private JDBLabelData ldat = new JDBLabelData(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JTable jTable1;
	private JScrollPane scrollPane = new JScrollPane();
	private PreparedStatement listStatement;
	private JDBAutoLabellerTableModel autolabeltable = new JDBAutoLabellerTableModel(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std jStatusText;

	public JInternalFrameAutoLabellerLines()
	{
		initGUI();
		refresh();
	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);

		query.clear();
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_AUTO_LABELLER order by line"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();

		listStatement = query.getPreparedStatement();
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(674, 474));
			this.setBounds(0, 0, 998, 437);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			jDesktopPane1 = new JDesktopPane();
			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(665, 490));
			jDesktopPane1.setBackground(Common.color_app_window);
			jDesktopPane1.setLayout(null);

			jButtonClose = new JButton4j(Common.icon_close);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(861, 350, 123, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			JButton4j jButtonRefresh = new JButton4j();
			jButtonRefresh.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					refresh();

				}
			});
			jButtonRefresh.setIcon(Common.icon_refresh);
			jButtonRefresh.setText(lang.get("btn_Refresh"));
			jButtonRefresh.setMnemonic('0');
			jButtonRefresh.setBounds(615, 350, 123, 32);
			jDesktopPane1.add(jButtonRefresh);

			JButton4j jButtonAdd = new JButton4j(Common.icon_add);
			jButtonAdd.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addRecord();
				}
			});
			jButtonAdd.setText(lang.get("btn_Add"));
			jButtonAdd.setMnemonic('A');
			jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_AUTO_LAB_ADD"));
			jButtonAdd.setBounds(0, 350, 123, 32);
			jDesktopPane1.add(jButtonAdd);

			JButton4j JButtonEdit = new JButton4j(Common.icon_edit);
			JButtonEdit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					editRecord();
				}
			});
			JButtonEdit.setText(lang.get("btn_Edit"));
			JButtonEdit.setMnemonic('E');
			JButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_AUTO_LAB_EDIT"));
			JButtonEdit.setBounds(123, 350, 123, 32);
			jDesktopPane1.add(JButtonEdit);

			JButton4j jButtonDelete = new JButton4j(Common.icon_delete);
			jButtonDelete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					deleteRecord();
				}
			});
			jButtonDelete.setText(lang.get("btn_Delete"));
			jButtonDelete.setMnemonic('D');
			jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_AUTO_LAB_DELETE"));
			jButtonDelete.setBounds(246, 350, 123, 32);
			jDesktopPane1.add(jButtonDelete);

			JButton4j jButtonRename = new JButton4j(Common.icon_rename);
			jButtonRename.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					renameRecord();
				}
			});
			jButtonRename.setText(lang.get("btn_Rename"));
			jButtonRename.setMnemonic('D');
			jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_AUTO_LAB_RENAME"));
			jButtonRename.setBounds(369, 350, 123, 32);
			jDesktopPane1.add(jButtonRename);

			scrollPane.setBounds(0, 0, 979, 345);

			jTable1 = new JTable();
			jTable1.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() == 2)
					{
						editRecord();
					}
				}
			});
			jTable1.getTableHeader().setBounds(0, 0, 629, 16);

			jTable1.getTableHeader().setFont(Common.font_table_header);
			jTable1.getTableHeader().setForeground(Common.color_tableHeaderFont);
			jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
			jTable1.setFont(Common.font_list);
			jTable1.setForeground(Common.color_listFontStandard);

			jTable1.getSelectionModel();

			jDesktopPane1.add(scrollPane);

			JButton4j button4jResend = new JButton4j(Common.icon_resend);
			button4jResend.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String editLabeller = "";
					String editGroup = "";
					int row = jTable1.getSelectedRow();
					if (row >= 0)
					{
						editLabeller = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Line_Col).toString();
						editGroup = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Group_Col).toString();
						autolab.resend(editLabeller, editGroup, "Y");
						refresh();
					}
				}
			});
			button4jResend.setText(lang.get("btn_Resend"));
			button4jResend.setBounds(492, 350, 123, 32);
			jDesktopPane1.add(button4jResend);

			JButton4j button4jHistory = new JButton4j(Common.icon_history);
			button4jHistory.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABELLER_HISTORY"));
			button4jHistory.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String line = "";
					String group = "";
					int row = jTable1.getSelectedRow();
					if (row >= 0)
					{
						line = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Line_Col).toString();
						group = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Group_Col).toString();
						JLaunchMenu.runForm("FRM_LABELLER_HISTORY", line, group);
						refresh();
					}

				}
			});
			button4jHistory.setText(lang.get("btn_History"));
			button4jHistory.setBounds(738, 350, 123, 32);
			jDesktopPane1.add(button4jHistory);

			jStatusText = new JLabel4j_std();
			jStatusText.setForeground(Color.RED);
			jStatusText.setBackground(Color.GRAY);
			jStatusText.setBounds(-1, 384, 985, 21);
			jDesktopPane1.add(jStatusText);

			mod.setModuleId("FRM_ADMIN_PRINTERS");
			mod.getModuleProperties();

			populateList("","");

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void refresh()
	{
		String currentLINE = "";
		String currentGroupID = "";
		int row = jTable1.getSelectedRow();
		if (row > -1)
		{
			currentLINE = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Line_Col).toString();
			currentGroupID = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Group_Col).toString();

		}
		populateList(currentLINE, currentGroupID);

	}

	private void populateList(String defaultLine, String defaultGroup)
	{

		JDBAutoLabeller al = new JDBAutoLabeller(Common.selectedHostID, Common.sessionID);
		buildSQL();
		autolabeltable.setResultSet(al.getAutoLabellerDataResultSet(listStatement));
		TableRowSorter<JDBAutoLabellerTableModel> sorter = new TableRowSorter<JDBAutoLabellerTableModel>(autolabeltable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(autolabeltable);
		scrollPane.getViewport().setBackground(Common.color_tablebackground);
		scrollPane.setViewportView(jTable1);
		jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.Line_Col).setPreferredWidth(110);
		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.Description_Col).setPreferredWidth(200);
		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.ProcessOrder_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.Material_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.Batch_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.SSCC_Prefix_Col).setWidth(150);
		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.Use_SSCC_Range_Col).setWidth(50);
		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.SSCC_Range_Col).setWidth(150);
		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.Expiry_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.Modified_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBAutoLabellerTableModel.Group_Col).setPreferredWidth(60);

		scrollPane.repaint();

		int rowCount = autolabeltable.getRowCount();
		int selectedRow = -1;
		String tableLine = "";
		String tableGroup = "";
		for (int currentRow = 0; currentRow < rowCount; currentRow++)
		{
			tableLine = (String) autolabeltable.getValueAt(currentRow, JDBAutoLabellerTableModel.Line_Col);
			tableGroup = (String) autolabeltable.getValueAt(currentRow, JDBAutoLabellerTableModel.Group_Col);

			if (tableLine.equals(defaultLine))
			{
				if (tableGroup.equals(defaultGroup))
				{
					selectedRow = currentRow;
					break;
				}
			}
		}
		if ((selectedRow == -1) && (rowCount > 0))
		{
			selectedRow = 0;
		}

		if (rowCount > 0)
		{
			jTable1.setRowSelectionInterval(selectedRow, selectedRow);
		}

		JUtility.setResultRecordCountColour(jStatusText, false, 1000, jTable1.getRowCount());
	}

	private void addRecord()
	{
		String lineId = "";
		lineId = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Line_Create"), null, JOptionPane.QUESTION_MESSAGE, Common.icon_confirm, null, null);
		if (lineId != null)
		{
			if (lineId.equals("") == false)
			{

				Object[] printerGroups = Common.printerGroup;
				String groupId = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("lbl_Group_ID"), lang.get("lbl_Group_ID"), JOptionPane.PLAIN_MESSAGE, Common.icon_confirm, printerGroups, "Pack");

				// If a string was returned, say so.
				if ((groupId != null) && (groupId.length() > 0))
				{
					lineId = lineId.toUpperCase();
					autolab.setLine(lineId);
					autolab.setGroup(groupId);
					if (autolab.isValidLineGroup() == false)
					{
						JLaunchMenu.runDialog("FRM_ADMIN_AUTO_LAB_EDIT", lineId, groupId);
					} else
					{
						JOptionPane.showMessageDialog(Common.mainForm, "Line ID [" + lineId + "] already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
					}
					buildSQL();
					populateList(lineId,groupId);
				}

			}
		}

	}

	private void editRecord()
	{
		String editLabeller = "";
		String editGroup = "";
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			editLabeller = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Line_Col).toString();
			editGroup = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Group_Col).toString();
			JLaunchMenu.runDialog("FRM_ADMIN_AUTO_LAB_EDIT", editLabeller, editGroup);
			refresh();
		}
	}

	private void renameRecord()
	{
		String oldLabeller = "";
		String oldGroup = "";
		String newLabeller = "";
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			oldLabeller = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Line_Col).toString();
			oldGroup = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Group_Col).toString();
			newLabeller = JOptionPane.showInputDialog(Common.mainForm, "Rename to (" + oldGroup + ")");
			if (newLabeller != null)
			{
				if (newLabeller.equals("") == false)
				{
					newLabeller = newLabeller.toUpperCase();

					if (autolab.rename(oldLabeller, newLabeller, oldGroup))
					{
						if (plm.renameLine(oldLabeller, newLabeller))
						{
							if (lres.renameLine(oldLabeller, newLabeller))
							{
								ldat.renameLine(oldLabeller, newLabeller);
							}
						}
					} else
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, autolab.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
					}

				}
			}
			refresh();
		}
	}

	private void deleteRecord()
	{
		String deleteLine = "";
		String deleteGroup = "";
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			deleteLine = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Line_Col).toString();
			deleteGroup = jTable1.getValueAt(row, JDBAutoLabellerTableModel.Group_Col).toString();
			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Line_Delete") + " " + deleteLine + " - " + deleteGroup + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (n == 0)
			{
				autolab.setLine(deleteLine);
				autolab.setGroup(deleteGroup);
				boolean result = autolab.delete();
				if (result == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, autolab.getErrorMessage(), "Delete error (" + deleteLine + " - " + deleteGroup + ")", JOptionPane.WARNING_MESSAGE, Common.icon_confirm);
				} else
				{
					JDBPrinterLineMembership plm = new JDBPrinterLineMembership(Common.selectedHostID, Common.sessionID);
					plm.removeAllPrintersfromLine(deleteLine, deleteGroup);
					buildSQL();
					populateList(deleteLine,deleteGroup);
				}
			}
		}
	}
}
