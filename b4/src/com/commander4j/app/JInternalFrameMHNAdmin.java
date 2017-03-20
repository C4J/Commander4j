package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMHNAdmin.java
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMHN;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBMHNTableModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameMHNAdmin extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JLabel4j_std jStatusText;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldRecorder;
	private JLabel4j_std jLabelInitiator;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonAdd;
	private JLabel4j_std jLabelReason;
	private JToggleButton jToggleButtonSequence;
	private JButton4j jButtonSearch;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel1SortBy;
	private JTextField4j jTextFieldReason;
	private JComboBox4j<String> jTextFieldStatus;
	private JLabel4j_std jLabelStatus;
	private JTextField4j jTextFieldResource;
	private JLabel4j_std jLabelResource;
	private JLabel4j_std jLabelMHN;
	private JTextField4j jTextFieldMHN;
	private JTextField4j jTextFieldInitiator;
	private JTable jTable1;
	private JScrollPane jScrollPane1;
	private JTextField4j jTextFieldComment;
	private JLabel4j_std jLabelComment;
	private JLabel4j_std jLabelRecorder;
	private JButton4j buttonReasonLookup;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private JLabel4j_std jLabelAuthorisor;
	private JTextField4j jTextFieldAuthorisor;
	
	private JCheckBox4j checkBoxCreatedFrom;
	private JCalendarButton calendarButtonCreatedFrom;
	private JDateControl dateControlCreatedFrom;
	private JCheckBox4j checkBoxCreatedTo;
	private JCalendarButton calendarButtonCreatedTo;
	private JDateControl dateControlCreatedTo;

	private JCheckBox4j checkBoxExpectedFrom;
	private JCalendarButton calendarButtonExpectedFrom;
	private JDateControl dateControlExpectedFrom;
	private JCheckBox4j checkBoxExpectedTo;
	private JCalendarButton calendarButtonExpectedTo;
	private JDateControl dateControlExpectedTo;
	
	private JCheckBox4j checkBoxResolvedFrom;
	private JCalendarButton calendarButtonResolvedFrom;
	private JDateControl dateControlResolvedFrom;
	private JCheckBox4j checkBoxResolvedTo;
	private JCalendarButton calendarButtonResolvedTo;
	private JDateControl dateControlResolvedTo;
	private JButton4j button;
	private JButton4j button_1;
	private JButton4j button_2;
	private PreparedStatement listStatement;
	
	private void clearFilter()
	{
		jTextFieldMHN.setText("");
		jTextFieldRecorder.setText("");
		jTextFieldInitiator.setText("");
		jTextFieldResource.setText("");
		jTextFieldComment.setText("");
		jTextFieldReason.setText("");
		jTextFieldStatus.setSelectedItem("");
		checkBoxCreatedFrom.setSelected(false);
		checkBoxCreatedTo.setSelected(false);
		checkBoxExpectedFrom.setSelected(false);
		checkBoxExpectedTo.setSelected(false);
		checkBoxResolvedFrom.setSelected(false);
		checkBoxResolvedTo.setSelected(false);

		search();
	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			if (fieldname.equals("MHN_NUMBER") == true)
			{
				jTextFieldMHN.setText(jTable1.getValueAt(row, JDBMHNTableModel.MHN_Number_Col).toString());
			}

			if (fieldname.equals("RECORDER") == true)
			{
				jTextFieldRecorder.setText(jTable1.getValueAt(row, JDBMHNTableModel.recorder_Col).toString());
			}

			if (fieldname.equals("INITIATOR") == true)
			{
				jTextFieldInitiator.setText(jTable1.getValueAt(row, JDBMHNTableModel.initiator_Col).toString());
			}

			if (fieldname.equals("RESOURCE") == true)
			{
				jTextFieldResource.setText(jTable1.getValueAt(row, JDBMHNTableModel.resource_Col).toString());
			}

			if (fieldname.equals("COMMENT") == true)
			{
				jTextFieldComment.setText(jTable1.getValueAt(row, JDBMHNTableModel.comment_Col).toString());
			}

			if (fieldname.equals("REASON") == true)
			{
				jTextFieldReason.setText(jTable1.getValueAt(row, JDBMHNTableModel.reason1_Col).toString());
			}

			if (fieldname.equals("STATUS") == true)
			{
				jTextFieldStatus.setSelectedItem(jTable1.getValueAt(row, JDBMHNTableModel.status_Col).toString());
			}

			search();

		}
	}

	private void sortBy(String fieldname)
	{
		jComboBoxSortBy.setSelectedItem(fieldname);
		search();
	}

	private void print()
	{
		
		PreparedStatement temp = buildSQLr();
		JLaunchReport.runReport("RPT_MHNS", null, "", temp, "");
	}

	private void search()
	{
		buildSQL();
		populateList();
	}

	private void create()
	{
		JDBMHN lmhn= new JDBMHN(Common.selectedHostID, Common.sessionID);
		int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_MHN_Create"), lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
		if (n == 0)
		{
			String newMHN = lmhn.generateNewMHNNumber().toUpperCase();

			lmhn.setMHNNumber(newMHN);
			lmhn.setStatus("Active");
			lmhn.create();
			JLaunchMenu.runForm("FRM_ADMIN_MHN_EDIT", newMHN);
			buildSQL();
			populateList();
		}
	}

	private void populateList()
	{
		JDBMHN mhn = new JDBMHN(Common.selectedHostID, Common.sessionID);
		JDBMHNTableModel mhntable = new JDBMHNTableModel(mhn.getMHNDataResultSet(listStatement));
		TableRowSorter<JDBMHNTableModel> sorter = new TableRowSorter<JDBMHNTableModel>(mhntable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(mhntable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(JDBMHNTableModel.MHN_Number_Col).setPreferredWidth(75);
		jTable1.getColumnModel().getColumn(JDBMHNTableModel.recorder_Col).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(JDBMHNTableModel.initiator_Col).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(JDBMHNTableModel.reason1_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBMHNTableModel.reason2_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBMHNTableModel.reason3_Col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBMHNTableModel.date_created_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBMHNTableModel.date_expected_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBMHNTableModel.date_resolved_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBMHNTableModel.status_Col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBMHNTableModel.comment_Col).setPreferredWidth(395);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, false, 0, mhntable.getRowCount());
	}

	private void deleteRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			String lMHN = jTable1.getValueAt(row, JDBMHNTableModel.MHN_Number_Col).toString();
			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_MHN_Delete") + " " + lMHN + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (n == 0)
			{
				JDBMHN l = new JDBMHN(Common.selectedHostID, Common.sessionID);
				l.setMHNNumber(lMHN);
				boolean result = l.delete();
				if (result == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, l.getErrorMessage(), "Delete error (" + lMHN + ")", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					buildSQL();
					populateList();
				}
			}
		}
	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			String lMHN = jTable1.getValueAt(row,JDBMHNTableModel.MHN_Number_Col).toString();
			JLaunchMenu.runForm("FRM_ADMIN_MHN_EDIT", lMHN);
		}
	}

	private void setSequence(boolean descending)
	{
		jToggleButtonSequence.setSelected(descending);
		if (jToggleButtonSequence.isSelected())
		{
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending);
		}
		else
		{
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending);
		}
	}

	private PreparedStatement buildSQLr()
	{
		
		PreparedStatement result;
		
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MHN"));

		query.addParamtoSQL("MHN_Number=", jTextFieldMHN.getText());
		query.addParamtoSQL("recorder=", jTextFieldRecorder.getText());
		query.addParamtoSQL("initiator=", jTextFieldInitiator.getText());
		query.addParamtoSQL("authorisor=",jTextFieldAuthorisor.getText());

		if (jTextFieldComment.getText().equals("") == false)
		{
			query.addParamtoSQL("upper(comment) LIKE ", "%" + jTextFieldComment.getText().toUpperCase() + "%");
		}
		
		if (jTextFieldReason.getText().equals("")==false)
		{
			String sel = "";
			if (query.getCriteriaCount()==0)
			{
				sel = " where ";
			}
			else
			{
				sel = " and ";
			}
			query.setCriterialCount(query.getCriteriaCount()+3);
			query.addText(sel+"(reason1 = ? or reason2 = ? or reason3 = ?)");
			query.addParameter(jTextFieldReason.getText());
			query.addParameter(jTextFieldReason.getText());
			query.addParameter(jTextFieldReason.getText());
		}
				
		query.addParamtoSQL("status=", jTextFieldStatus.getSelectedItem().toString());
		query.addParamtoSQL("required_resource=", jTextFieldResource.getText());
		
		if (checkBoxCreatedFrom.isSelected())
		{
		query.addParamtoSQL("date_created>=", JUtility.getTimestampFromDate(dateControlCreatedFrom.getDate()));
		}

		if (checkBoxCreatedTo.isSelected())
		{
		query.addParamtoSQL("date_created<=", JUtility.getTimestampFromDate(dateControlCreatedTo.getDate()));
		}

		if (checkBoxExpectedFrom.isSelected())
		{
			query.addParamtoSQL("date_expected>=", JUtility.getTimestampFromDate(dateControlExpectedFrom.getDate()));
		}

		if (checkBoxExpectedTo.isSelected())
		{
			query.addParamtoSQL("date_expected<=", JUtility.getTimestampFromDate(dateControlExpectedTo.getDate()));
		}

		if (checkBoxResolvedFrom.isSelected())
		{
			query.addParamtoSQL("date_resolved>=", JUtility.getTimestampFromDate(dateControlResolvedFrom.getDate()));
		}

		if (checkBoxResolvedTo.isSelected())
		{
			query.addParamtoSQL("date_resolved<=", JUtility.getTimestampFromDate(dateControlResolvedTo.getDate()));
		}		
		
		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(false,"none",0);
		query.bindParams();
		result = query.getPreparedStatement();
		
		return result;
	}
	
	
	
	private void buildSQL()
	{
		
		JDBQuery.closeStatement(listStatement);
		

		listStatement = buildSQLr();
	}

	public JInternalFrameMHNAdmin() {
		super();

		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MHN where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MHN"));

		search();
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(770, 478));
			this.setBounds(0, 0, 997+Common.LFAdjustWidth, 563+Common.LFAdjustHeight);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("MHN Administration");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabelRecorder = new JLabel4j_std();
					jDesktopPane1.add(jLabelRecorder);
					jLabelRecorder.setText(lang.get("lbl_Recorder"));
					jLabelRecorder.setBounds(242, 8, 102, 21);
					jLabelRecorder.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					JButton4j btnExcel = new JButton4j(Common.icon_XLS);
					btnExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent arg0)
						{
							export();
						}
					});
					btnExcel.setText(lang.get("btn_Excel"));
					btnExcel.setMnemonic(lang.getMnemonicChar());
					btnExcel.setBounds(642, 118, 106, 32);
					jDesktopPane1.add(btnExcel);
				}
				{
					jTextFieldRecorder = new JTextField4j(JDBMHN.field_recorder);
					jDesktopPane1.add(jTextFieldRecorder);
					jTextFieldRecorder.setBounds(350, 8, 99, 21);
				}
				{
					jLabelComment = new JLabel4j_std();
					jDesktopPane1.add(jLabelComment);
					jLabelComment.setText(lang.get("lbl_Comment"));
					jLabelComment.setBounds(2, 63, 102, 21);
					jLabelComment.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldComment = new JTextField4j(JDBMHN.field_comments);
					jDesktopPane1.add(jTextFieldComment);
					jTextFieldComment.setBounds(109, 63, 360, 21);
				}
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jScrollPane1.setToolTipText("");
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 152, 969, 343);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][] { { "One", "Two" }, { "Three", "Four" } }, new String[] { "Column 1", "Column 2" });
						jTable1 = new JTable();
						jTable1.setToolTipText("Update selected MHN by using the Edit option.");
						jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
						jScrollPane1.setViewportView(jTable1);
						jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						jTable1.getTableHeader().setFont(Common.font_table_header);
						jTable1.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_EDIT"))
									{
										editRecord();
									}
								}
							}
						});
						jTable1.setModel(jTable1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jTable1, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_find);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										search();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Search"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_ADD"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										create();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_EDIT"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										editRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Edit"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DELETE"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										deleteRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Delete"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_print);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										print();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Print"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenu4j sortByMenu = new JMenu4j();
								sortByMenu.setText(lang.get("lbl_Sort_By"));
								popupMenu.add(sortByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("MHN_NUMBER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_MHN_Number"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("INITIATOR");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Initiator"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("RECORDER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Recorder"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("REASON");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Reason"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("STATUS");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Status"));
									sortByMenu.add(newItemMenuItem);
								}


							}

							{
								final JMenu4j filterByMenu = new JMenu4j();
								filterByMenu.setText("Filter by");
								popupMenu.add(filterByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("RECORDER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Recorder"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("INITIATOR");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Initiator"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("REASON");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Reason"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("STATUS");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Status"));
									filterByMenu.add(newItemMenuItem);
								}


								{
									filterByMenu.addSeparator();
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											clearFilter();
										}
									});
									newItemMenuItem.setText(lang.get("btn_Clear_Filter"));
									filterByMenu.add(newItemMenuItem);
								}
							}
						}
					}
				}
				{
					jLabelInitiator = new JLabel4j_std();
					jDesktopPane1.add(jLabelInitiator);
					jLabelInitiator.setText(lang.get("lbl_Initiator"));
					jLabelInitiator.setBounds(480, 8, 103, 21);
					jLabelInitiator.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldInitiator = new JTextField4j(JDBMHN.field_initiator);
					jDesktopPane1.add(jTextFieldInitiator);
					jTextFieldInitiator.setBounds(592, 8, 99, 21);
				}
				{
					jTextFieldMHN = new JTextField4j(JDBMHN.field_mhn_number);
					jDesktopPane1.add(jTextFieldMHN);
					jTextFieldMHN.setBounds(109, 8, 126, 21);
				}
				{
					jLabelMHN = new JLabel4j_std();
					jDesktopPane1.add(jLabelMHN);
					jLabelMHN.setText(lang.get("lbl_MHN_Number"));
					jLabelMHN.setBounds(2, 8, 102, 21);
					jLabelMHN.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelResource = new JLabel4j_std();
					jDesktopPane1.add(jLabelResource);
					jLabelResource.setText(lang.get("lbl_Process_Order_Required_Resource"));
					jLabelResource.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelResource.setBounds(2, 90, 102, 21);
				}
				{
					jTextFieldResource = new JTextField4j(JDBProcessOrder.field_process_order);
					jDesktopPane1.add(jTextFieldResource);
					jTextFieldResource.setBounds(109, 90, 105, 21);
				}
				{
					jLabelReason = new JLabel4j_std();
					jDesktopPane1.add(jLabelReason);
					jLabelReason.setText(lang.get("lbl_Reason"));
					jLabelReason.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelReason.setBounds(2, 35, 102, 21);
				}
				{
					jTextFieldReason = new JTextField4j(JDBMHN.field_reason1);
					jDesktopPane1.add(jTextFieldReason);
					jTextFieldReason.setBounds(109, 35, 105, 21);
				}
				{
					jLabelStatus = new JLabel4j_std();
					jDesktopPane1.add(jLabelStatus);
					jLabelStatus.setText(lang.get("lbl_Status"));
					jLabelStatus.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelStatus.setBounds(242, 35, 102, 21);
				}
				{
					jTextFieldStatus = new JComboBox4j<String>();
					jTextFieldStatus.setModel(new DefaultComboBoxModel<String>(new String[] {"", "Active", "Closed"}));
					jDesktopPane1.add(jTextFieldStatus);
					jTextFieldStatus.setBounds(349, 35, 120, 23);
					jTextFieldStatus.setSelectedItem("Active");
				}
				{
					jLabel1SortBy = new JLabel4j_std();
					jDesktopPane1.add(jLabel1SortBy);
					jLabel1SortBy.setText(lang.get("lbl_Sort_By"));
					jLabel1SortBy.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1SortBy.setBounds(242, 90, 80, 21);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[] { "MHN_NUMBER", "RECORDER", "INITIATOR", "REASON", "STATUS", "DATE_CREATED", "DATE_EXPECTED",
							"DATE_RESOLVED" });
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					jComboBoxSortBy.setBounds(328, 90, 141, 23);
					jComboBoxSortBy.setSelectedItem("MHN_NUMBER");
					jComboBoxSortBy.setRequestFocusEnabled(false);
				}
				{
					jButtonSearch = new JButton4j(Common.icon_find);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.setMnemonic(lang.getMnemonicChar());
					jButtonSearch.setBounds(0, 118, 106, 32);
					jButtonSearch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							search();

						}
					});
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add);
					jButtonAdd.setToolTipText("Create new MHN");
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_ADD"));
					jButtonAdd.setBounds(214, 118, 106, 32);
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							create();
						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit);
					jButtonEdit.setToolTipText("Edit MHN (Update Pallets)");
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_EDIT"));
					jButtonEdit.setBounds(321, 118, 106, 32);
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete);
					jButtonDelete.setToolTipText("Delete MHN");
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DELETE"));
					jButtonDelete.setBounds(428, 118, 106, 32);
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							deleteRecord();
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(535, 118, 106, 32);
					jButtonPrint.setEnabled(true);
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							print();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(749, 118, 106, 32);
				}
				{

					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(856, 118, 106, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JDBQuery.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(470, 90, 21, 21);
					jToggleButtonSequence.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 495, 962, 21);
					jDesktopPane1.add(jStatusText);
				}

				{
					jButtonClear = new JButton4j(Common.icon_clear);
					jButtonClear.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							clearFilter();
						}
					});
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jButtonClear.setBounds(107, 118, 106, 32);
					jDesktopPane1.add(jButtonClear);
				}
				{
					buttonReasonLookup = new JButton4j(Common.icon_lookup);
					buttonReasonLookup.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.reasons())
							{
								jTextFieldReason.setText(JLaunchLookup.dlgResult);
							}
						}
					});

					buttonReasonLookup.setBounds(214, 35, 21, 21);
					jDesktopPane1.add(buttonReasonLookup);
				}
				{
					jLabelAuthorisor = new JLabel4j_std();
					jLabelAuthorisor.setText(lang.get("lbl_Authorisor"));
					jLabelAuthorisor.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelAuthorisor.setBounds(726, 8, 103, 21);
					jDesktopPane1.add(jLabelAuthorisor);
				}
				{
					jTextFieldAuthorisor = new JTextField4j(JDBMHN.field_authorisor);
					jTextFieldAuthorisor.setBounds(833, 8, 99, 21);
					jDesktopPane1.add(jTextFieldAuthorisor);
				}
				
				JLabel4j_std labelCreated = new JLabel4j_std();
				labelCreated.setText(lang.get("lbl_Created"));
				labelCreated.setHorizontalAlignment(SwingConstants.TRAILING);
				labelCreated.setBounds(476, 35, 126, 21);
				jDesktopPane1.add(labelCreated);
				
				dateControlCreatedFrom = new JDateControl();
				dateControlCreatedFrom.setEnabled(false);
				dateControlCreatedFrom.setBounds(634, 31, 125, 25);
				jDesktopPane1.add(dateControlCreatedFrom);
				
				calendarButtonCreatedFrom = new JCalendarButton(dateControlCreatedFrom);
				calendarButtonCreatedFrom.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				calendarButtonCreatedFrom.setEnabled(false);
				calendarButtonCreatedFrom.setBounds(763, 35, 21, 21);
				jDesktopPane1.add(calendarButtonCreatedFrom);
				
				checkBoxCreatedFrom = new JCheckBox4j();
				checkBoxCreatedFrom.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (checkBoxCreatedFrom.isSelected())
						{
							dateControlCreatedFrom.setEnabled(true);
							calendarButtonCreatedFrom.setEnabled(true);
						}
						else
						{
							dateControlCreatedFrom.setEnabled(false);
							calendarButtonCreatedFrom.setEnabled(false);
						}
					}
				});
				checkBoxCreatedFrom.setBackground(Color.WHITE);
				checkBoxCreatedFrom.setBounds(604, 31, 21, 25);
				jDesktopPane1.add(checkBoxCreatedFrom);
				
				checkBoxCreatedTo = new JCheckBox4j();
				checkBoxCreatedTo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (checkBoxCreatedTo.isSelected())
						{
							dateControlCreatedTo.setEnabled(true);
							calendarButtonCreatedTo.setEnabled(true);
						}
						else
						{
							dateControlCreatedTo.setEnabled(false);
							calendarButtonCreatedTo.setEnabled(false);
						}
					}
				});
				checkBoxCreatedTo.setBackground(Color.WHITE);
				checkBoxCreatedTo.setBounds(784, 31, 21, 25);
				jDesktopPane1.add(checkBoxCreatedTo);
				
				dateControlCreatedTo = new JDateControl();
				dateControlCreatedTo.setEnabled(false);
				dateControlCreatedTo.setBounds(810, 31, 125, 25);
				jDesktopPane1.add(dateControlCreatedTo);
				
				calendarButtonCreatedTo = new JCalendarButton(dateControlCreatedTo);
				calendarButtonCreatedTo.setEnabled(false);
				calendarButtonCreatedTo.setBounds(938, 35, 21, 21);
				jDesktopPane1.add(calendarButtonCreatedTo);
				
				JLabel4j_std labelExpected = new JLabel4j_std();
				labelExpected.setText(lang.get("lbl_Expected"));
				labelExpected.setHorizontalAlignment(SwingConstants.TRAILING);
				labelExpected.setBounds(476, 63, 126, 21);
				jDesktopPane1.add(labelExpected);
				
				checkBoxExpectedFrom = new JCheckBox4j();
				checkBoxExpectedFrom.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (checkBoxExpectedFrom.isSelected())
						{
							dateControlExpectedFrom.setEnabled(true);
							calendarButtonExpectedFrom.setEnabled(true);
						}
						else
						{
							dateControlExpectedFrom.setEnabled(false);
							calendarButtonExpectedFrom.setEnabled(false);
						}
					}
				});
				checkBoxExpectedFrom.setBackground(Color.WHITE);
				checkBoxExpectedFrom.setBounds(604, 59, 21, 25);
				jDesktopPane1.add(checkBoxExpectedFrom);
				
				dateControlExpectedFrom = new JDateControl();
				dateControlExpectedFrom.setEnabled(false);
				dateControlExpectedFrom.setBounds(634, 59, 125, 25);
				jDesktopPane1.add(dateControlExpectedFrom);
				
				calendarButtonExpectedFrom = new JCalendarButton(dateControlExpectedFrom);
				calendarButtonExpectedFrom.setEnabled(false);
				calendarButtonExpectedFrom.setBounds(763, 63, 21, 21);
				jDesktopPane1.add(calendarButtonExpectedFrom);
				
				checkBoxExpectedTo = new JCheckBox4j();
				checkBoxExpectedTo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (checkBoxExpectedTo.isSelected())
						{
							dateControlExpectedTo.setEnabled(true);
							calendarButtonExpectedTo.setEnabled(true);
						}
						else
						{
							dateControlExpectedTo.setEnabled(false);
							calendarButtonExpectedTo.setEnabled(false);
						}
					}
				});
				checkBoxExpectedTo.setBackground(Color.WHITE);
				checkBoxExpectedTo.setBounds(784, 59, 21, 25);
				jDesktopPane1.add(checkBoxExpectedTo);
				
				dateControlExpectedTo = new JDateControl();
				dateControlExpectedTo.setEnabled(false);
				dateControlExpectedTo.setBounds(810, 59, 125, 25);
				jDesktopPane1.add(dateControlExpectedTo);
				
				calendarButtonExpectedTo = new JCalendarButton(dateControlExpectedTo);
				calendarButtonExpectedTo.setEnabled(false);
				calendarButtonExpectedTo.setBounds(938, 63, 21, 21);
				jDesktopPane1.add(calendarButtonExpectedTo);
				
				JLabel4j_std labelResolved = new JLabel4j_std();
				labelResolved.setText(lang.get("lbl_Resolved"));
				labelResolved.setHorizontalAlignment(SwingConstants.TRAILING);
				labelResolved.setBounds(480, 90, 122, 21);
				jDesktopPane1.add(labelResolved);
				
				checkBoxResolvedFrom = new JCheckBox4j();
				checkBoxResolvedFrom.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (checkBoxResolvedFrom.isSelected())
						{
							dateControlResolvedFrom.setEnabled(true);
							calendarButtonResolvedFrom.setEnabled(true);
						}
						else
						{
							dateControlResolvedFrom.setEnabled(false);
							calendarButtonResolvedFrom.setEnabled(false);
						}
					}
				});
				checkBoxResolvedFrom.setBackground(Color.WHITE);
				checkBoxResolvedFrom.setBounds(604, 86, 21, 25);
				jDesktopPane1.add(checkBoxResolvedFrom);
				
				dateControlResolvedFrom = new JDateControl();
				dateControlResolvedFrom.setEnabled(false);
				dateControlResolvedFrom.setBounds(634, 86, 125, 25);
				jDesktopPane1.add(dateControlResolvedFrom);
				
				checkBoxResolvedTo = new JCheckBox4j();
				checkBoxResolvedTo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
							if (checkBoxResolvedTo.isSelected())
							{
								dateControlResolvedTo.setEnabled(true);
								calendarButtonResolvedTo.setEnabled(true);
							}
							else
							{
								dateControlResolvedTo.setEnabled(false);
								calendarButtonResolvedTo.setEnabled(false);
							}
					}
				});
				checkBoxResolvedTo.setBackground(Color.WHITE);
				checkBoxResolvedTo.setBounds(784, 86, 21, 25);
				jDesktopPane1.add(checkBoxResolvedTo);
				
				dateControlResolvedTo = new JDateControl();
				dateControlResolvedTo.setEnabled(false);
				dateControlResolvedTo.setBounds(810, 86, 125, 25);
				jDesktopPane1.add(dateControlResolvedTo);
				
				calendarButtonResolvedFrom = new JCalendarButton(dateControlResolvedFrom);
				calendarButtonResolvedFrom.setEnabled(false);
				calendarButtonResolvedFrom.setBounds(763, 90, 21, 21);
				jDesktopPane1.add(calendarButtonResolvedFrom);
				
				calendarButtonResolvedTo = new JCalendarButton(dateControlResolvedTo);
				calendarButtonResolvedTo.setEnabled(false);
				calendarButtonResolvedTo.setBounds(938, 90, 21, 21);
				jDesktopPane1.add(calendarButtonResolvedTo);
				{
					button = new JButton4j(Common.icon_lookup);
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";

							if (JLaunchLookup.users()) {
								jTextFieldAuthorisor.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					button.setBounds(932, 8, 21, 21);
					jDesktopPane1.add(button);
				}
				{
					button_1 = new JButton4j(Common.icon_lookup);
					button_1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";

							if (JLaunchLookup.users()) {
								jTextFieldInitiator.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					button_1.setBounds(691, 8, 21, 21);
					jDesktopPane1.add(button_1);
				}
				{
					button_2 = new JButton4j(Common.icon_lookup);
					button_2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";

							if (JLaunchLookup.users()) {
								jTextFieldRecorder.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					button_2.setBounds(449, 8, 21, 21);
					jDesktopPane1.add(button_2);
				}
				
				JButton4j buttonResourceLookup = new JButton4j(Common.icon_lookup);
				buttonResourceLookup.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JLaunchLookup.dlgAutoExec = false;
						JLaunchLookup.dlgCriteriaDefault = "";
						if (JLaunchLookup.resources())
						{
							jTextFieldResource.setText(JLaunchLookup.dlgResult);
						}
					}
				});
				buttonResourceLookup.setBounds(214, 90, 21, 21);
				jDesktopPane1.add(buttonResourceLookup);

				Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
				Rectangle window = getBounds();
				setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

				setSequence(true);
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void export()
	{
		JDBMHN mhn = new JDBMHN(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		PreparedStatement temp = buildSQLr();
		export.saveAs("mhn_list.xls", mhn.getMHNDataResultSet(temp), Common.mainForm);
	}

	/**
	 * WindowBuilder generated method.<br>
	 * Please don't remove this method or its invocations.<br>
	 * It used by WindowBuilder to associate the {@link javax.swing.JPopupMenu}
	 * with parent.
	 */
	private static void addPopup(Component component, final JPopupMenu popup)
	{
		component.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger())
					showMenu(e);
			}

			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e)
			{
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
