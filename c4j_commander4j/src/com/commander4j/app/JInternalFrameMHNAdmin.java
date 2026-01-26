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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
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
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JToggleButton4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBMHNTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameMHNAdmin class allows the user to insert/update/delete Master
 * Hold Notices in the APP_MHN table.
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMHNAdmin.jpg" >
 *
 * @see com.commander4j.app.JInternalFrameMHNAssign JInternalFrameMHNAssign
 * @see com.commander4j.app.JInternalFrameMHNProperties
 *      JInternalFrameMHNProperties
 * @see com.commander4j.db.JDBMHN JDBMHN
 */
public class JInternalFrameMHNAdmin extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JButton4j button;
	private JButton4j buttonReasonLookup;
	private JButton4j button_1;
	private JButton4j button_2;
	private JButton4j jButtonAdd;
	private JButton4j jButtonClear;
	private JButton4j jButtonClose;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonSearch;
	private JCalendarButton calendarButtonCreatedFrom;
	private JCalendarButton calendarButtonCreatedTo;
	private JCalendarButton calendarButtonExpectedFrom;
	private JCalendarButton calendarButtonExpectedTo;
	private JCalendarButton calendarButtonResolvedFrom;
	private JCalendarButton calendarButtonResolvedTo;
	private JCheckBox4j checkBoxCreatedFrom;
	private JCheckBox4j checkBoxCreatedTo;
	private JCheckBox4j checkBoxExpectedFrom;
	private JCheckBox4j checkBoxExpectedTo;
	private JCheckBox4j checkBoxResolvedFrom;
	private JCheckBox4j checkBoxResolvedTo;
	private JComboBox4j<String> jComboBoxSortBy;
	private JComboBox4j<String> jTextFieldStatus;
	private JDBLanguage lang;
	private JDateControl dateControlCreatedFrom;
	private JDateControl dateControlCreatedTo;
	private JDateControl dateControlExpectedFrom;
	private JDateControl dateControlExpectedTo;
	private JDateControl dateControlResolvedFrom;
	private JDateControl dateControlResolvedTo;
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_status jStatusText;
	private JLabel4j_std jLabel1SortBy;
	private JLabel4j_std jLabelAuthorisor;
	private JLabel4j_std jLabelComment;
	private JLabel4j_std jLabelInitiator;
	private JLabel4j_std jLabelMHN;
	private JLabel4j_std jLabelReason;
	private JLabel4j_std jLabelRecorder;
	private JLabel4j_std jLabelResource;
	private JLabel4j_std jLabelStatus;
	private JScrollPane4j jScrollPane1;
	private JTable4j jTable1;
	private JTextField4j jTextFieldAuthorisor;
	private JTextField4j jTextFieldComment;
	private JTextField4j jTextFieldInitiator;
	private JTextField4j jTextFieldMHN;
	private JTextField4j jTextFieldReason;
	private JTextField4j jTextFieldRecorder;
	private JTextField4j jTextFieldResource;
	private JToggleButton4j jToggleButtonSequence;
	private PreparedStatement listStatement;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();

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

	public JInternalFrameMHNAdmin()
	{
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

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);

		listStatement = buildSQLr();
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
		query.addParamtoSQL("authorisor=", jTextFieldAuthorisor.getText());

		if (jTextFieldComment.getText().equals("") == false)
		{
			query.addParamtoSQL("upper(comments) LIKE ", "%" + jTextFieldComment.getText().toUpperCase() + "%");
		}

		if (jTextFieldReason.getText().equals("") == false)
		{
			String sel = "";
			if (query.getCriteriaCount() == 0)
			{
				sel = " where ";
			}
			else
			{
				sel = " and ";
			}
			query.setCriterialCount(query.getCriteriaCount() + 3);
			query.addText(sel + "(reason1 = ? or reason2 = ? or reason3 = ?)");
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
		if (jComboBoxSortBy.getSelectedItem().toString().equals("REASON"))
		{
			query.appendSort("reason1,reason2,reason3", jToggleButtonSequence.isSelected());
		}
		else
		{
			query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		}
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		result = query.getPreparedStatement();

		return result;
	}

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

	private void copyToClipboard(String fieldname)
	{
		StringSelection stringSelection = new StringSelection("");

		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals("MHN_NUMBER") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals("REASON") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 3).toString() + "," + jTable1.getValueAt(row, 4).toString() + "," + jTable1.getValueAt(row, 5).toString());
			}

			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		}
	}

	private void create()
	{
		JDBMHN lmhn = new JDBMHN(Common.selectedHostID, Common.sessionID);
		int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_MHN_Create"), lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
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

	private void deleteRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			String lMHN = jTable1.getValueAt(row, JDBMHNTableModel.MHN_Number_Col).toString();
			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_MHN_Delete") + " " + lMHN + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
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
			String lMHN = jTable1.getValueAt(row, JDBMHNTableModel.MHN_Number_Col).toString();
			JLaunchMenu.runForm("FRM_ADMIN_MHN_EDIT", lMHN);
		}
	}

	private void export()
	{
		JDBMHN mhn = new JDBMHN(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		PreparedStatement temp = buildSQLr();
		export.saveAs("mhn_list.xls", mhn.getMHNDataResultSet(temp), Common.mainForm);
		populateList();
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

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(770, 478));
			this.setBounds(0, 0, 990, 630);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("MHN Administration");

			jDesktopPane1 = new JDesktopPane4j();

			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jLabelRecorder = new JLabel4j_std();
			jDesktopPane1.add(jLabelRecorder);
			jLabelRecorder.setText(lang.get("lbl_Recorder"));
			jLabelRecorder.setBounds(242, 8, 102, 22);
			jLabelRecorder.setHorizontalAlignment(SwingConstants.TRAILING);

			JButton4j btnExcel = new JButton4j(Common.icon_XLS_16x16);
			btnExcel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					export();
				}
			});
			btnExcel.setText(lang.get("btn_Excel"));
			btnExcel.setMnemonic(lang.getMnemonicChar());
			btnExcel.setBounds(650, 140, 108, 32);
			jDesktopPane1.add(btnExcel);

			jTextFieldRecorder = new JTextField4j(JDBMHN.field_recorder);
			jDesktopPane1.add(jTextFieldRecorder);
			jTextFieldRecorder.setBounds(350, 8, 99, 22);

			jLabelComment = new JLabel4j_std();
			jDesktopPane1.add(jLabelComment);
			jLabelComment.setText(lang.get("lbl_Comment"));
			jLabelComment.setBounds(2, 74, 102, 22);
			jLabelComment.setHorizontalAlignment(SwingConstants.TRAILING);

			jTextFieldComment = new JTextField4j(JDBMHN.field_comments);
			jDesktopPane1.add(jTextFieldComment);
			jTextFieldComment.setBounds(109, 74, 360, 22);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.Table);

			jScrollPane1.setToolTipText("");
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.setBounds(0, 180, 969, 386);
			{
				TableModel jTable1Model = new DefaultTableModel(new String[][]
				{
						{ "One", "Two" },
						{ "Three", "Four" } }, new String[]
				{ "Column 1", "Column 2" });
				jTable1 = new JTable4j();
				jTable1.setToolTipText("Update selected MHN by using the Edit option.");
				jScrollPane1.setViewportView(jTable1);

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
						final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_find_16x16);
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
						final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
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
						final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit_16x16);
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
						final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete_16x16);
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
						final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_print_16x16);
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
					{
						final JMenu4j clipboardByMenu = new JMenu4j();
						clipboardByMenu.setText(lang.get("lbl_Clipboard_Copy"));
						popupMenu.add(clipboardByMenu);

						{
							final JMenuItem4j menuItemFilterBySSCC = new JMenuItem4j();
							menuItemFilterBySSCC.addActionListener(new ActionListener()
							{
								public void actionPerformed(final ActionEvent e)
								{
									copyToClipboard("MHN_NUMBER");
								}
							});
							menuItemFilterBySSCC.setText(lang.get("lbl_MHN_Number"));
							clipboardByMenu.add(menuItemFilterBySSCC);
						}

						{
							final JMenuItem4j menuItemFilterByBatch = new JMenuItem4j();
							menuItemFilterByBatch.addActionListener(new ActionListener()
							{
								public void actionPerformed(final ActionEvent e)
								{
									copyToClipboard("REASON");
								}
							});
							menuItemFilterByBatch.setText(lang.get("lbl_Reason"));
							clipboardByMenu.add(menuItemFilterByBatch);
						}

					}

				}
			}

			jLabelInitiator = new JLabel4j_std();
			jDesktopPane1.add(jLabelInitiator);
			jLabelInitiator.setText(lang.get("lbl_Initiator"));
			jLabelInitiator.setBounds(480, 8, 103, 22);
			jLabelInitiator.setHorizontalAlignment(SwingConstants.TRAILING);

			jTextFieldInitiator = new JTextField4j(JDBMHN.field_initiator);
			jDesktopPane1.add(jTextFieldInitiator);
			jTextFieldInitiator.setBounds(592, 8, 99, 22);

			jTextFieldMHN = new JTextField4j(JDBMHN.field_mhn_number);
			jDesktopPane1.add(jTextFieldMHN);
			jTextFieldMHN.setBounds(109, 8, 126, 22);

			jLabelMHN = new JLabel4j_std();
			jDesktopPane1.add(jLabelMHN);
			jLabelMHN.setText(lang.get("lbl_MHN_Number"));
			jLabelMHN.setBounds(2, 9, 102, 21);
			jLabelMHN.setHorizontalAlignment(SwingConstants.TRAILING);

			jLabelResource = new JLabel4j_std();
			jDesktopPane1.add(jLabelResource);
			jLabelResource.setText(lang.get("lbl_Process_Order_Required_Resource"));
			jLabelResource.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelResource.setBounds(2, 105, 102, 22);

			jTextFieldResource = new JTextField4j(JDBProcessOrder.field_process_order);
			jDesktopPane1.add(jTextFieldResource);
			jTextFieldResource.setBounds(109, 105, 105, 22);

			jLabelReason = new JLabel4j_std();
			jDesktopPane1.add(jLabelReason);
			jLabelReason.setText(lang.get("lbl_Reason"));
			jLabelReason.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelReason.setBounds(2, 42, 102, 22);

			jTextFieldReason = new JTextField4j(JDBMHN.field_reason1);
			jDesktopPane1.add(jTextFieldReason);
			jTextFieldReason.setBounds(109, 42, 105, 22);

			jLabelStatus = new JLabel4j_std();
			jDesktopPane1.add(jLabelStatus);
			jLabelStatus.setText(lang.get("lbl_Status"));
			jLabelStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelStatus.setBounds(242, 42, 102, 22);

			jTextFieldStatus = new JComboBox4j<String>();
			jTextFieldStatus.setModel(new DefaultComboBoxModel<String>(new String[]
			{ "", "Active", "Closed" }));
			jDesktopPane1.add(jTextFieldStatus);
			jTextFieldStatus.setBounds(349, 41, 120, 23);
			jTextFieldStatus.setSelectedItem("Active");

			jLabel1SortBy = new JLabel4j_std();
			jDesktopPane1.add(jLabel1SortBy);
			jLabel1SortBy.setText(lang.get("lbl_Sort_By"));
			jLabel1SortBy.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel1SortBy.setBounds(242, 105, 80, 22);

			ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[]
			{ "MHN_NUMBER", "RECORDER", "INITIATOR", "REASON", "STATUS", "DATE_CREATED", "DATE_EXPECTED", "DATE_RESOLVED" });
			jComboBoxSortBy = new JComboBox4j<String>();
			jDesktopPane1.add(jComboBoxSortBy);
			jComboBoxSortBy.setModel(jComboBoxSortByModel);
			jComboBoxSortBy.setBounds(328, 105, 141, 22);
			jComboBoxSortBy.setSelectedItem("MHN_NUMBER");

			jButtonSearch = new JButton4j(Common.icon_find_16x16);
			jDesktopPane1.add(jButtonSearch);
			jButtonSearch.setText(lang.get("btn_Search"));
			jButtonSearch.setMnemonic(lang.getMnemonicChar());
			jButtonSearch.setBounds(2, 140, 108, 32);
			jButtonSearch.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					search();

				}
			});

			jButtonAdd = new JButton4j(Common.icon_add_16x16);
			jButtonAdd.setToolTipText("Create new MHN");
			jDesktopPane1.add(jButtonAdd);
			jButtonAdd.setText(lang.get("btn_Add"));
			jButtonAdd.setMnemonic(lang.getMnemonicChar());
			jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_ADD"));
			jButtonAdd.setBounds(218, 140, 108, 32);
			jButtonAdd.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					create();
				}
			});

			jButtonEdit = new JButton4j(Common.icon_edit_16x16);
			jButtonEdit.setToolTipText("Edit MHN (Update Pallets)");
			jDesktopPane1.add(jButtonEdit);
			jButtonEdit.setText(lang.get("btn_Edit"));
			jButtonEdit.setMnemonic(lang.getMnemonicChar());
			jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_EDIT"));
			jButtonEdit.setBounds(326, 140, 108, 32);
			jButtonEdit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					editRecord();
				}
			});

			jButtonDelete = new JButton4j(Common.icon_delete_16x16);
			jButtonDelete.setToolTipText("Delete MHN");
			jDesktopPane1.add(jButtonDelete);
			jButtonDelete.setText(lang.get("btn_Delete"));
			jButtonDelete.setMnemonic(lang.getMnemonicChar());
			jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DELETE"));
			jButtonDelete.setBounds(434, 140, 108, 32);
			jButtonDelete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					deleteRecord();
				}
			});

			jButtonPrint = new JButton4j(Common.icon_report_16x16);
			jDesktopPane1.add(jButtonPrint);
			jButtonPrint.setText(lang.get("btn_Print"));
			jButtonPrint.setMnemonic(lang.getMnemonicChar());
			jButtonPrint.setBounds(542, 140, 108, 32);
			jButtonPrint.setEnabled(true);
			jButtonPrint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					print();
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(758, 140, 108, 32);

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(866, 140, 108, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JDBQuery.closeStatement(listStatement);
					dispose();
				}
			});

			jToggleButtonSequence = new JToggleButton4j();
			jDesktopPane1.add(jToggleButtonSequence);
			jToggleButtonSequence.setBounds(469, 105, 22, 22);
			jToggleButtonSequence.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					setSequence(jToggleButtonSequence.isSelected());
				}
			});

			jStatusText = new JLabel4j_status();
			jStatusText.setBounds(5, 572, 965, 21);
			jDesktopPane1.add(jStatusText);

			jButtonClear = new JButton4j(Common.icon_clear_16x16);
			jButtonClear.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					clearFilter();
				}
			});
			jButtonClear.setText(lang.get("btn_Clear_Filter"));
			jButtonClear.setBounds(110, 140, 108, 32);
			jDesktopPane1.add(jButtonClear);

			buttonReasonLookup = new JButton4j(Common.icon_lookup_16x16);
			buttonReasonLookup.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JLaunchLookup.dlgAutoExec = false;
					JLaunchLookup.dlgCriteriaDefault = "";
					if (JLaunchLookup.reasons())
					{
						jTextFieldReason.setText(JLaunchLookup.dlgResult);
					}
				}
			});

			buttonReasonLookup.setBounds(214, 42, 21, 22);
			jDesktopPane1.add(buttonReasonLookup);

			jLabelAuthorisor = new JLabel4j_std();
			jLabelAuthorisor.setText(lang.get("lbl_Authorisor"));
			jLabelAuthorisor.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelAuthorisor.setBounds(726, 8, 103, 22);
			jDesktopPane1.add(jLabelAuthorisor);

			jTextFieldAuthorisor = new JTextField4j(JDBMHN.field_authorisor);
			jTextFieldAuthorisor.setBounds(833, 8, 99, 22);
			jDesktopPane1.add(jTextFieldAuthorisor);

			JLabel4j_std labelCreated = new JLabel4j_std();
			labelCreated.setText(lang.get("lbl_Created"));
			labelCreated.setHorizontalAlignment(SwingConstants.TRAILING);
			labelCreated.setBounds(476, 42, 126, 22);
			jDesktopPane1.add(labelCreated);

			dateControlCreatedFrom = new JDateControl();
			dateControlCreatedFrom.setEnabled(false);
			dateControlCreatedFrom.setBounds(634, 42, 128, 22);
			jDesktopPane1.add(dateControlCreatedFrom);

			calendarButtonCreatedFrom = new JCalendarButton(dateControlCreatedFrom);
			calendarButtonCreatedFrom.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				}
			});
			calendarButtonCreatedFrom.setEnabled(false);
			calendarButtonCreatedFrom.setBounds(763, 43, 21, 21);
			jDesktopPane1.add(calendarButtonCreatedFrom);

			checkBoxCreatedFrom = new JCheckBox4j();
			checkBoxCreatedFrom.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
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

			checkBoxCreatedFrom.setBounds(608, 42, 22, 22);
			jDesktopPane1.add(checkBoxCreatedFrom);

			checkBoxCreatedTo = new JCheckBox4j();
			checkBoxCreatedTo.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
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

			checkBoxCreatedTo.setBounds(784, 42, 22, 22);
			jDesktopPane1.add(checkBoxCreatedTo);

			dateControlCreatedTo = new JDateControl();
			dateControlCreatedTo.setEnabled(false);
			dateControlCreatedTo.setBounds(810, 42, 128, 22);
			jDesktopPane1.add(dateControlCreatedTo);

			calendarButtonCreatedTo = new JCalendarButton(dateControlCreatedTo);
			calendarButtonCreatedTo.setEnabled(false);
			calendarButtonCreatedTo.setBounds(939, 43, 21, 21);
			jDesktopPane1.add(calendarButtonCreatedTo);

			JLabel4j_std labelExpected = new JLabel4j_std();
			labelExpected.setText(lang.get("lbl_Expected"));
			labelExpected.setHorizontalAlignment(SwingConstants.TRAILING);
			labelExpected.setBounds(476, 74, 126, 22);
			jDesktopPane1.add(labelExpected);

			checkBoxExpectedFrom = new JCheckBox4j();
			checkBoxExpectedFrom.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
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

			checkBoxExpectedFrom.setBounds(608, 74, 22, 22);
			jDesktopPane1.add(checkBoxExpectedFrom);

			dateControlExpectedFrom = new JDateControl();
			dateControlExpectedFrom.setEnabled(false);
			dateControlExpectedFrom.setBounds(634, 74, 128, 22);
			jDesktopPane1.add(dateControlExpectedFrom);

			calendarButtonExpectedFrom = new JCalendarButton(dateControlExpectedFrom);
			calendarButtonExpectedFrom.setEnabled(false);
			calendarButtonExpectedFrom.setBounds(763, 75, 21, 21);
			jDesktopPane1.add(calendarButtonExpectedFrom);

			checkBoxExpectedTo = new JCheckBox4j();
			checkBoxExpectedTo.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
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

			checkBoxExpectedTo.setBounds(784, 74, 22, 22);
			jDesktopPane1.add(checkBoxExpectedTo);

			dateControlExpectedTo = new JDateControl();
			dateControlExpectedTo.setEnabled(false);
			dateControlExpectedTo.setBounds(810, 74, 128, 22);
			jDesktopPane1.add(dateControlExpectedTo);

			calendarButtonExpectedTo = new JCalendarButton(dateControlExpectedTo);
			calendarButtonExpectedTo.setEnabled(false);
			calendarButtonExpectedTo.setBounds(939, 75, 21, 21);
			jDesktopPane1.add(calendarButtonExpectedTo);

			JLabel4j_std labelResolved = new JLabel4j_std();
			labelResolved.setText(lang.get("lbl_Resolved"));
			labelResolved.setHorizontalAlignment(SwingConstants.TRAILING);
			labelResolved.setBounds(480, 105, 122, 22);
			jDesktopPane1.add(labelResolved);

			checkBoxResolvedFrom = new JCheckBox4j();
			checkBoxResolvedFrom.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
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

			checkBoxResolvedFrom.setBounds(608, 105, 22, 22);
			jDesktopPane1.add(checkBoxResolvedFrom);

			dateControlResolvedFrom = new JDateControl();
			dateControlResolvedFrom.setEnabled(false);
			dateControlResolvedFrom.setBounds(634, 105, 128, 22);
			jDesktopPane1.add(dateControlResolvedFrom);

			checkBoxResolvedTo = new JCheckBox4j();
			checkBoxResolvedTo.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
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

			checkBoxResolvedTo.setBounds(784, 105, 22, 22);
			jDesktopPane1.add(checkBoxResolvedTo);

			dateControlResolvedTo = new JDateControl();
			dateControlResolvedTo.setEnabled(false);
			dateControlResolvedTo.setBounds(810, 105, 128, 22);
			jDesktopPane1.add(dateControlResolvedTo);

			calendarButtonResolvedFrom = new JCalendarButton(dateControlResolvedFrom);
			calendarButtonResolvedFrom.setEnabled(false);
			calendarButtonResolvedFrom.setBounds(763, 106, 21, 21);
			jDesktopPane1.add(calendarButtonResolvedFrom);

			calendarButtonResolvedTo = new JCalendarButton(dateControlResolvedTo);
			calendarButtonResolvedTo.setEnabled(false);
			calendarButtonResolvedTo.setBounds(939, 106, 21, 21);
			jDesktopPane1.add(calendarButtonResolvedTo);

			button = new JButton4j(Common.icon_lookup_16x16);
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.dlgAutoExec = true;
					JLaunchLookup.dlgCriteriaDefault = "";

					if (JLaunchLookup.users())
					{
						jTextFieldAuthorisor.setText(JLaunchLookup.dlgResult);
					}
				}
			});
			button.setBounds(932, 8, 21, 22);
			jDesktopPane1.add(button);

			button_1 = new JButton4j(Common.icon_lookup_16x16);
			button_1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.dlgAutoExec = true;
					JLaunchLookup.dlgCriteriaDefault = "";

					if (JLaunchLookup.users())
					{
						jTextFieldInitiator.setText(JLaunchLookup.dlgResult);
					}
				}
			});
			button_1.setBounds(691, 8, 21, 22);
			jDesktopPane1.add(button_1);

			button_2 = new JButton4j(Common.icon_lookup_16x16);
			button_2.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.dlgAutoExec = true;
					JLaunchLookup.dlgCriteriaDefault = "";

					if (JLaunchLookup.users())
					{
						jTextFieldRecorder.setText(JLaunchLookup.dlgResult);
					}
				}
			});
			button_2.setBounds(449, 8, 21, 22);
			jDesktopPane1.add(button_2);

			JButton4j buttonResourceLookup = new JButton4j(Common.icon_lookup_16x16);
			buttonResourceLookup.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.dlgAutoExec = false;
					JLaunchLookup.dlgCriteriaDefault = "";
					if (JLaunchLookup.resources())
					{
						jTextFieldResource.setText(JLaunchLookup.dlgResult);
					}
				}
			});
			buttonResourceLookup.setBounds(214, 105, 21, 22);
			jDesktopPane1.add(buttonResourceLookup);

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					jTextFieldMHN.requestFocus();
					jTextFieldMHN.setCaretPosition(jTextFieldMHN.getText().length());

				}
			});

			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle window = getBounds();
			setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

			setSequence(true);

		}
		catch (Exception e)
		{
			e.printStackTrace();
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

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

	private void setSequence(boolean descending)
	{
		jToggleButtonSequence.setSelected(descending);
		if (jToggleButtonSequence.isSelected())
		{
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending_16x16);
		}
		else
		{
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending_16x16);
		}
	}

	private void sortBy(String fieldname)
	{
		jComboBoxSortBy.setSelectedItem(fieldname);
		search();
	}
}
