package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameMHNProperties.java
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
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMHN;
import com.commander4j.db.JDBMHNDecisions;
import com.commander4j.db.JDBMHNReasons;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JToggleButton4j;
import com.commander4j.renderer.TableCellRenderer_MHNPallet;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBMHNPalletTableModelProperties;
import com.commander4j.util.JExcel;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameMHNProperties class allows the user to edit a single Master
 * Hold Notice in the APP_MHN table.
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMHNProperties.jpg" >
 *
 * @see com.commander4j.app.JInternalFrameMHNAssign JInternalFrameMHNAssign
 * @see com.commander4j.app.JInternalFrameMHNAdmin JInternalFrameMHNAdmin
 * @see com.commander4j.db.JDBMHN JDBMHN
 */
public class JInternalFrameMHNProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JButton4j button_1;
	private JButton4j jButtonAdd;
	private JButton4j jButtonClose;
	private JButton4j jButtonDelete;
	private JButton4j jButtonPrint;
	private JButton4j jButtonSearch;
	private JComboBox4j<JDBMHNReasons> jTextFieldReason1;
	private JComboBox4j<JDBMHNReasons> textFieldReason2;
	private JComboBox4j<JDBMHNReasons> textFieldReason3;
	private JComboBox4j<String> jComboBoxSortBy;
	private JComboBox4j<String> jTextFieldStatus;
	private JDBLanguage lang;
	private JDBMHN mhn = new JDBMHN(Common.selectedHostID, Common.sessionID);
	private JDBMHNDecisions mhnDecisions = new JDBMHNDecisions(Common.selectedHostID, Common.sessionID);
	private JDBMHNReasons mhnReasons = new JDBMHNReasons(Common.selectedHostID, Common.sessionID);
	private JDateControl dateControlExpected = new JDateControl();
	private JDesktopPane4j jDesktopPane1;
	private JInternalFrameMHNProperties me;
	private JLabel4j_status jStatusText;
	private JLabel4j_std jLabel1SortBy;
	private JLabel4j_std jLabelComment;
	private JLabel4j_std jLabelInitiator;
	private JLabel4j_std jLabelMHN;
	private JLabel4j_std jLabelReason;
	private JLabel4j_std jLabelRecorder;
	private JLabel4j_std jLabelResource;
	private JLabel4j_std jLabelStatus;
	private JLabel4j_std label;
	private JList4j<String> list = new JList4j<String>();
	private JMenu4j menu;
	private JMenuItem4j menuItem;
	private JScrollPane4j jScrollPane1;
	private JTable4j jTable1;
	private JTextField4j jTextFieldAuthorisor;
	private JTextField4j jTextFieldComment;
	private JTextField4j jTextFieldGS_GSTD_REF = new JTextField4j(10);
	private JTextField4j jTextFieldInitiator;
	private JTextField4j jTextFieldMHN;
	private JTextField4j jTextFieldRecorder;
	private JTextField4j jTextFieldResource;
	private JTextField4j jTextFieldWriteOffRef = new JTextField4j(20);
	private JToggleButton4j jToggleButtonSequence;
	private PreparedStatement listStatement;
	private String masterHoldNoticeNumber = "";
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private Vector<JDBMHNDecisions> decisionList = new Vector<JDBMHNDecisions>();
	private Vector<JDBMHNReasons> reasonList = new Vector<JDBMHNReasons>();

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

	public JInternalFrameMHNProperties(String mhnNumber)
	{

		super();

		addInternalFrameListener(new InternalFrameAdapter()
		{
			@Override
			public void internalFrameClosing(InternalFrameEvent e)
			{
				save();
			}
		});

		decisionList.addAll(mhnDecisions.getDecisions());
		reasonList.add(new JDBMHNReasons(Common.selectedHostID, Common.sessionID));
		reasonList.addAll(mhnReasons.getReasons());

		masterHoldNoticeNumber = mhnNumber;

		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_PALLET where 1=2"));
		query.applyRestriction(true, "none", 1);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		jTextFieldMHN.setText(mhnNumber);

		JScrollPane4j scrollPane = new JScrollPane4j(JScrollPane4j.List);
		scrollPane.setBounds(726, 104, 275, 98);
		jDesktopPane1.add(scrollPane);

		ListModel<String> jList1Model = new DefaultComboBoxModel<String>(new String[]
		{ "Item One", "Item Two" });

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		list.setModel(jList1Model);

		JLabel4j_std jLabelAuthorisor = new JLabel4j_std();
		jLabelAuthorisor.setText(lang.get("lbl_Authorisor"));
		jLabelAuthorisor.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabelAuthorisor.setBounds(785, 8, 79, 22);
		jDesktopPane1.add(jLabelAuthorisor);

		jTextFieldAuthorisor = new JTextField4j(JDBMHN.field_authorisor);
		jTextFieldAuthorisor.setEditable(false);
		jTextFieldAuthorisor.setBounds(870, 8, 111, 22);
		jDesktopPane1.add(jTextFieldAuthorisor);

		JLabel4j_std label_1 = new JLabel4j_std();
		label_1.setText(lang.get("lbl_Reason") + " 2");
		label_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label_1.setBounds(5, 72, 87, 22);
		jDesktopPane1.add(label_1);

		ComboBoxModel<JDBMHNReasons> jComboBox2Model = new DefaultComboBoxModel<JDBMHNReasons>(reasonList);
		textFieldReason2 = new JComboBox4j<JDBMHNReasons>();
		textFieldReason2.setModel(jComboBox2Model);
		textFieldReason2.setEditable(false);
		textFieldReason2.setBounds(101, 72, 273, 22);
		jDesktopPane1.add(textFieldReason2);

		JLabel4j_std label_2 = new JLabel4j_std();
		label_2.setText(lang.get("lbl_Reason") + " 3");
		label_2.setHorizontalAlignment(SwingConstants.TRAILING);
		label_2.setBounds(5, 104, 87, 22);
		jDesktopPane1.add(label_2);

		ComboBoxModel<JDBMHNReasons> jComboBox3Model = new DefaultComboBoxModel<JDBMHNReasons>(reasonList);
		textFieldReason3 = new JComboBox4j<JDBMHNReasons>();
		textFieldReason3.setModel(jComboBox3Model);
		textFieldReason3.setEditable(false);
		textFieldReason3.setBounds(101, 104, 273, 22);
		jDesktopPane1.add(textFieldReason3);

		JButton4j button = new JButton4j(Common.icon_lookup_16x16);
		button.addActionListener(new ActionListener()
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
		button.setBounds(755, 8, 21, 22);
		jDesktopPane1.add(button);
		{
			button_1 = new JButton4j(Common.icon_lookup_16x16);
			button_1.addActionListener(new ActionListener()
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
			button_1.setBounds(980, 8, 21, 22);
			jDesktopPane1.add(button_1);
		}

		jTextFieldWriteOffRef.setBounds(834, 40, 167, 22);
		jDesktopPane1.add(jTextFieldWriteOffRef);

		JLabel4j_std jLabelWriteOffRef = new JLabel4j_std();
		jLabelWriteOffRef.setText(lang.get("lbl_Write_Off_Ref"));
		jLabelWriteOffRef.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabelWriteOffRef.setBounds(688, 40, 141, 22);
		jDesktopPane1.add(jLabelWriteOffRef);

		JLabel4j_std jlabelGS_GSTD_REF = new JLabel4j_std();
		jlabelGS_GSTD_REF.setText("GS GSTD Ref");
		jlabelGS_GSTD_REF.setHorizontalAlignment(SwingConstants.TRAILING);
		jlabelGS_GSTD_REF.setBounds(688, 72, 141, 22);
		jDesktopPane1.add(jlabelGS_GSTD_REF);

		jTextFieldGS_GSTD_REF.setBounds(834, 72, 167, 22);
		jDesktopPane1.add(jTextFieldGS_GSTD_REF);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(true);

		if (mhn.getMHNProperties(mhnNumber))
		{

			for (int x = 0; x < reasonList.size(); x++)
			{
				if (reasonList.get(x).getReason().equals(mhn.getReason1()))
				{
					jTextFieldReason1.setSelectedIndex(x);
				}
				if (reasonList.get(x).getReason().equals(mhn.getReason2()))
				{
					textFieldReason2.setSelectedIndex(x);
				}
				if (reasonList.get(x).getReason().equals(mhn.getReason3()))
				{
					textFieldReason3.setSelectedIndex(x);
				}
			}
			jTextFieldComment.setText(mhn.getComments());
			jTextFieldResource.setText(mhn.getResource());
			jTextFieldStatus.setSelectedItem(mhn.getStatus());
			jTextFieldRecorder.setText(mhn.getRecorder());
			jTextFieldInitiator.setText(mhn.getInitiator());
			jTextFieldAuthorisor.setText(mhn.getAuthorisor());
			jTextFieldWriteOffRef.setText(mhn.getWriteOffRef());
			jTextFieldGS_GSTD_REF.setText(mhn.getGS_GSTD_REF());
			jTextFieldMHN.setText(mhnNumber);
			try
			{
				dateControlExpected.setDate(mhn.getDateExpected());
			}
			catch (Exception ex)
			{

			}

			refreshData();
		}
		this.me = this;

	}

	private void addPallets()
	{
		JLaunchMenu.runForm(me, "FRM_ADMIN_MHN_ASSIGN", jTextFieldMHN.getText());
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
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_PALLET"));
		query.addParamtoSQL("MHN_Number=", masterHoldNoticeNumber);
		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		result = query.getPreparedStatement();
		return result;
	}

	private void copyToClipboard(String fieldname)
	{
		StringSelection stringSelection = new StringSelection("");

		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals("SSCC") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals("Material") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 1).toString());
			}

			if (fieldname.equals("Batch") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 2).toString());
			}

			if (fieldname.equals("Process Order") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 3).toString());
			}

			if (fieldname.equals("Pallet Status") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 7).toString());
			}

			if (fieldname.equals("Location") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 9).toString());
			}

			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		}
	}

	private void deleteRecord()
	{
		int rowCount = jTable1.getSelectedRowCount();

		if (rowCount >= 0)
		{
			int[] rows = jTable1.getSelectedRows();

			JDBPallet temp = new JDBPallet(Common.selectedHostID, Common.sessionID);

			int n = JOptionPane.showConfirmDialog(Common.mainForm, "Delete " + String.valueOf(rowCount) + " SSCC(s) from Master Hold Notice " + jTextFieldMHN.getText() + " ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (n == 0)
			{

				for (int l = 0; l < rows.length; l++)
				{
					String sscc = jTable1.getValueAt(rows[l], JDBMHNPalletTableModelProperties.SSCC_Col).toString();
					temp.getPalletProperties(sscc);
					if (temp.getStatus().equals("Unrestricted"))
					{
						temp.updateMHNDecision("");
						temp.updateMHNNumber("");
						jStatusText.setText("SSCC " + sscc + " removed from MHN " + masterHoldNoticeNumber);
						Rectangle progressRect = jStatusText.getBounds();
						progressRect.x = 0;
						progressRect.y = 0;
						jStatusText.paintImmediately(progressRect);
					}
					else
					{
						JOptionPane.showMessageDialog(Common.mainForm, "Cannot remove SSCC from MHN [" + masterHoldNoticeNumber + "] as status is not Unrestricted.", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
					}
				}
				buildSQL();
				populateList();
			}
		}
	}

	private void export()
	{
		JDBMHN mhn = new JDBMHN(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		PreparedStatement temp = buildSQLr();
		export.saveAs("mhn_" + masterHoldNoticeNumber + ".xls", mhn.getMHNDataResultSet(temp), Common.mainForm);
		populateList();
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(770, 478));
			this.setBounds(0, 0, 1016, 581);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Edit Master Hold Notice");

			jDesktopPane1 = new JDesktopPane4j();

			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jLabelRecorder = new JLabel4j_std();
			jDesktopPane1.add(jLabelRecorder);
			jLabelRecorder.setText(lang.get("lbl_Recorder"));
			jLabelRecorder.setBounds(372, 8, 71, 22);
			jLabelRecorder.setHorizontalAlignment(SwingConstants.TRAILING);

			JButton4j btnExcel = new JButton4j(Common.icon_XLS_16x16);
			btnExcel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					save();
					export();
				}
			});
			btnExcel.setText(lang.get("btn_Excel"));
			btnExcel.setMnemonic(lang.getMnemonicChar());
			btnExcel.setBounds(516, 168, 101, 32);
			jDesktopPane1.add(btnExcel);

			jTextFieldRecorder = new JTextField4j(JDBMHN.field_recorder);
			jTextFieldRecorder.setEditable(false);
			jDesktopPane1.add(jTextFieldRecorder);
			jTextFieldRecorder.setBounds(452, 8, 121, 22);

			jLabelComment = new JLabel4j_std();
			jDesktopPane1.add(jLabelComment);
			jLabelComment.setText(lang.get("lbl_Comment"));
			jLabelComment.setBounds(13, 136, 79, 22);
			jLabelComment.setHorizontalAlignment(SwingConstants.TRAILING);

			jTextFieldComment = new JTextField4j(JDBMHN.field_comments);
			jDesktopPane1.add(jTextFieldComment);
			jTextFieldComment.setBounds(101, 136, 618, 22);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.Table);
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.setBounds(0, 210, 1001, 314);
			{
				TableModel jTable1Model = new DefaultTableModel(new String[][]
				{
						{ "One", "Two" },
						{ "Three", "Four" } }, new String[]
				{ "Column 1", "Column 2" });
				jTable1 = new JTable4j();
				jTable1.setToolTipText("Update decision by selecting one or more rows and then use the mouse right click popup menu options. ");

				jTable1.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent evt)
					{

						if (evt.getClickCount() == 2)
						{
							if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_SAMPLE") == true)
							{
								int row = jTable1.getSelectedRow();
								if (row >= 0)
								{
									String lsscc = jTable1.getValueAt(row, 0).toString();
									JLaunchMenu.runForm("FRM_PAL_SAMPLE", lsscc);
								}
							}
						}
					}
				});

				jTable1.setDefaultRenderer(Object.class, new TableCellRenderer_MHNPallet());

				jScrollPane1.setViewportView(jTable1);

				jTable1.setModel(jTable1Model);

				final JPopupMenu popupMenu = new JPopupMenu();
				addPopup(jTable1, popupMenu);

				{
					final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh_16x16);
					newItemMenuItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							refreshData();
						}
					});
					newItemMenuItem.setText(lang.get("btn_Refresh"));
					popupMenu.add(newItemMenuItem);
				}

				{
					final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
					newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_ADD"));
					newItemMenuItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							addPallets();
						}
					});
					newItemMenuItem.setText(lang.get("btn_Add"));
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
					final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_pallet_sampling_16x16);
					newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_SAMPLE"));
					newItemMenuItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							int row = jTable1.getSelectedRow();
							if (row >= 0)
							{
								String lsscc = jTable1.getValueAt(row, 0).toString();
								JLaunchMenu.runForm("FRM_PAL_SAMPLE", lsscc);
							}
						}
					});
					newItemMenuItem.setText(lang.get("mod_FRM_PAL_SAMPLE"));
					popupMenu.add(newItemMenuItem);
				}

				menu = new JMenu4j(lang.get("btn_Decision_Assign"));
				popupMenu.add(menu);
				{
					for (int x = 0; x < decisionList.size(); x++)
					{
						final int xx = x;
						menuItem = new JMenuItem4j(decisionList.get(x).getDecision());
						menuItem.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{

								int rowCount = jTable1.getSelectedRowCount();

								if (rowCount >= 0)
								{
									int[] rows = jTable1.getSelectedRows();

									JDBPallet temp = new JDBPallet(Common.selectedHostID, Common.sessionID);

									int n = JOptionPane.showConfirmDialog(Common.mainForm,
											"Update Decision  of " + String.valueOf(rowCount) + " SSCC(s) from Master Hold Notice " + jTextFieldMHN.getText() + " to " + decisionList.get(xx).getDescription() + " ?", "Confirm", JOptionPane.YES_NO_OPTION, 0,
											Common.icon_confirm_16x16);
									if (n == 0)
									{
										save();
										for (int l = 0; l < rows.length; l++)
										{
											String sscc = jTable1.getValueAt(rows[l], JDBMHNPalletTableModelProperties.SSCC_Col).toString();
											temp.getPalletProperties(sscc);
											temp.updateMHNDecision(decisionList.get(xx).getDecision());
											if (decisionList.get(xx).getStatus().equals("") == false)
											{
												temp.updateStatus(decisionList.get(xx).getStatus(), true);

												jStatusText.setText("SSCC " + sscc + " updated to  " + decisionList.get(xx).getStatus());
												Rectangle progressRect = jStatusText.getBounds();
												progressRect.x = 0;
												progressRect.y = 0;
												jStatusText.paintImmediately(progressRect);
											}
										}
										buildSQL();
										populateList();

									}
								}
							}
						});
						menu.add(menuItem);
					}
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

				final JMenu4j sortByMenu = new JMenu4j();
				sortByMenu.setText(lang.get("lbl_Sort_By"));
				popupMenu.add(sortByMenu);

				{
					final JMenuItem4j newItemMenuItem = new JMenuItem4j();
					newItemMenuItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							sortBy("SSCC");
						}
					});
					newItemMenuItem.setText(lang.get("lbl_Pallet_SSCC"));
					sortByMenu.add(newItemMenuItem);
				}

				{
					final JMenuItem4j newItemMenuItem = new JMenuItem4j();
					newItemMenuItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							sortBy("BATCH_NUMBER");
						}
					});
					newItemMenuItem.setText(lang.get("lbl_Material_Batch"));
					sortByMenu.add(newItemMenuItem);
				}

				{
					final JMenuItem4j newItemMenuItem = new JMenuItem4j();
					newItemMenuItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							sortBy("PROCESS_ORDER");
						}
					});
					newItemMenuItem.setText(lang.get("lbl_Process_Order"));
					sortByMenu.add(newItemMenuItem);
				}

				{
					final JMenuItem4j newItemMenuItem = new JMenuItem4j();
					newItemMenuItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							sortBy("DATE_OF_MANUFACTURE");
						}
					});
					newItemMenuItem.setText(lang.get("lbl_Pallet_DOM"));
					sortByMenu.add(newItemMenuItem);
				}

				final JMenu4j clipboardByMenu = new JMenu4j();
				clipboardByMenu.setText(lang.get("lbl_Clipboard_Copy"));
				popupMenu.add(clipboardByMenu);

				{
					final JMenuItem4j menuItemFilterBySSCC = new JMenuItem4j();
					menuItemFilterBySSCC.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("SSCC");
						}
					});
					menuItemFilterBySSCC.setText(lang.get("lbl_Pallet_SSCC"));
					clipboardByMenu.add(menuItemFilterBySSCC);
				}

				{
					final JMenuItem4j menuItemFilterByMaterial = new JMenuItem4j();
					menuItemFilterByMaterial.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("Material");
						}
					});
					menuItemFilterByMaterial.setText(lang.get("lbl_Material"));
					clipboardByMenu.add(menuItemFilterByMaterial);
				}

				{
					final JMenuItem4j menuItemFilterByBatch = new JMenuItem4j();
					menuItemFilterByBatch.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("Batch");
						}
					});
					menuItemFilterByBatch.setText(lang.get("lbl_Material_Batch"));
					clipboardByMenu.add(menuItemFilterByBatch);
				}

				{
					final JMenuItem4j menuItemFilterByLocation = new JMenuItem4j();
					menuItemFilterByLocation.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("Location");
						}
					});
					menuItemFilterByLocation.setText(lang.get("lbl_Location_ID"));
					clipboardByMenu.add(menuItemFilterByLocation);
				}

				{
					final JMenuItem4j menuItemFilterByPalletStatus = new JMenuItem4j();
					menuItemFilterByPalletStatus.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("Pallet Status");
						}
					});
					menuItemFilterByPalletStatus.setText(lang.get("lbl_Pallet_Status"));
					clipboardByMenu.add(menuItemFilterByPalletStatus);
				}

				{
					final JMenuItem4j menuItemFilterByProcessOrder = new JMenuItem4j();
					menuItemFilterByProcessOrder.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("Process Order");
						}
					});
					menuItemFilterByProcessOrder.setText(lang.get("lbl_Process_Order"));
					clipboardByMenu.add(menuItemFilterByProcessOrder);
				}

			}

			jLabelInitiator = new JLabel4j_std();
			jDesktopPane1.add(jLabelInitiator);
			jLabelInitiator.setText(lang.get("lbl_Initiator"));
			jLabelInitiator.setBounds(575, 8, 51, 22);
			jLabelInitiator.setHorizontalAlignment(SwingConstants.TRAILING);

			jTextFieldInitiator = new JTextField4j(JDBMHN.field_initiator);
			jTextFieldInitiator.setEditable(false);
			jDesktopPane1.add(jTextFieldInitiator);
			jTextFieldInitiator.setBounds(634, 8, 121, 22);

			jTextFieldMHN = new JTextField4j(JDBMHN.field_mhn_number);
			jTextFieldMHN.setForeground(Color.RED);
			jTextFieldMHN.setEditable(false);
			jDesktopPane1.add(jTextFieldMHN);
			jTextFieldMHN.setBounds(101, 8, 87, 22);

			jLabelMHN = new JLabel4j_std();
			jDesktopPane1.add(jLabelMHN);
			jLabelMHN.setText(lang.get("lbl_MHN_Number"));
			jLabelMHN.setBounds(5, 8, 87, 22);
			jLabelMHN.setHorizontalAlignment(SwingConstants.TRAILING);

			jLabelResource = new JLabel4j_std();
			jDesktopPane1.add(jLabelResource);
			jLabelResource.setText(lang.get("lbl_Process_Order_Required_Resource"));
			jLabelResource.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelResource.setBounds(380, 40, 173, 22);

			jTextFieldResource = new JTextField4j(JDBProcessOrder.field_required_resource);
			jDesktopPane1.add(jTextFieldResource);
			jTextFieldResource.setBounds(562, 40, 128, 22);

			jLabelReason = new JLabel4j_std();
			jDesktopPane1.add(jLabelReason);
			jLabelReason.setText(lang.get("lbl_Reason") + " 1");
			jLabelReason.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelReason.setBounds(5, 40, 87, 22);

			ComboBoxModel<JDBMHNReasons> jComboBox2Model = new DefaultComboBoxModel<JDBMHNReasons>(reasonList);
			jTextFieldReason1 = new JComboBox4j<JDBMHNReasons>();
			jTextFieldReason1.setModel(jComboBox2Model);
			jTextFieldReason1.setEditable(false);
			jDesktopPane1.add(jTextFieldReason1);
			jTextFieldReason1.setBounds(101, 40, 273, 22);

			jLabelStatus = new JLabel4j_std();
			jDesktopPane1.add(jLabelStatus);
			jLabelStatus.setText(lang.get("lbl_Status"));
			jLabelStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelStatus.setBounds(189, 8, 79, 22);

			jTextFieldStatus = new JComboBox4j<String>();
			jTextFieldStatus.setModel(new DefaultComboBoxModel<String>(new String[]
			{ "Active", "Closed" }));
			jDesktopPane1.add(jTextFieldStatus);
			jTextFieldStatus.setBounds(273, 8, 101, 22);

			jLabel1SortBy = new JLabel4j_std();
			jDesktopPane1.add(jLabel1SortBy);
			jLabel1SortBy.setText(lang.get("lbl_Sort_By"));
			jLabel1SortBy.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel1SortBy.setBounds(372, 104, 71, 22);

			jComboBoxSortBy = new JComboBox4j<String>();
			jDesktopPane1.add(jComboBoxSortBy);
			jComboBoxSortBy.setModel(new DefaultComboBoxModel<String>(new String[]
			{ "DECISION", "SSCC", "DATE_OF_MANUFACTURE", "BATCH", "MATERIAL", "PROCESS_ORDER" }));
			jComboBoxSortBy.setBounds(460, 104, 239, 22);
			jComboBoxSortBy.setSelectedItem("DATE_OF_MANUFACTURE");
			jComboBoxSortBy.setRequestFocusEnabled(false);

			jButtonSearch = new JButton4j(Common.icon_refresh_16x16);
			jDesktopPane1.add(jButtonSearch);
			jButtonSearch.setText(lang.get("btn_Refresh"));
			jButtonSearch.setMnemonic(lang.getMnemonicChar());
			jButtonSearch.setBounds(5, 168, 101, 32);
			jButtonSearch.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					refreshData();
				}
			});

			jButtonAdd = new JButton4j(Common.icon_add_16x16);
			jDesktopPane1.add(jButtonAdd);
			jButtonAdd.setText(lang.get("btn_Add"));
			jButtonAdd.setMnemonic(lang.getMnemonicChar());
			jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_ASSIGN"));
			jButtonAdd.setBounds(108, 168, 101, 32);
			jButtonAdd.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					addPallets();
				}
			});

			jButtonDelete = new JButton4j(Common.icon_delete_16x16);
			jDesktopPane1.add(jButtonDelete);
			jButtonDelete.setText(lang.get("btn_Delete"));
			jButtonDelete.setMnemonic(lang.getMnemonicChar());
			jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DELETE"));
			jButtonDelete.setBounds(210, 168, 101, 32);
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
			jButtonPrint.setBounds(414, 168, 101, 32);
			jButtonPrint.setEnabled(true);
			jButtonPrint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					save();
					print();
				}
			});

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(618, 168, 101, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					save();
					JDBQuery.closeStatement(listStatement);
					dispose();
				}
			});

			jToggleButtonSequence = new JToggleButton4j();
			jDesktopPane1.add(jToggleButtonSequence);
			jToggleButtonSequence.setBounds(698, 104, 21, 22);
			jToggleButtonSequence.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					setSequence(jToggleButtonSequence.isSelected());
				}
			});

			jStatusText = new JLabel4j_status();
			jStatusText.setBounds(5, 526, 993, 21);
			jDesktopPane1.add(jStatusText);

			JButton4j button = new JButton4j(Common.icon_batch_16x16);
			button.setText(lang.get("btn_Material_Batches"));
			button.setMnemonic('0');
			button.setBounds(312, 168, 101, 32);
			button.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH"));
			jDesktopPane1.add(button);

			dateControlExpected.setBounds(563, 72, 120, 22);
			dateControlExpected.setDisplayMode(JDateControl.mode_disable_visible);
			jDesktopPane1.add(dateControlExpected);

			JCalendarButton calendarButtonExpected = new JCalendarButton(dateControlExpected);
			calendarButtonExpected.setBounds(682, 72, 22, 22);
			jDesktopPane1.add(calendarButtonExpected);
			{
				label = new JLabel4j_std();
				label.setText(lang.get("lbl_Expected"));
				label.setHorizontalAlignment(SwingConstants.TRAILING);
				label.setBounds(380, 72, 173, 22);
				jDesktopPane1.add(label);
			}
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_BATCH");
				}
			});

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void populateList()
	{
		JDBMHN mhnTemp = new JDBMHN(Common.selectedHostID, Common.sessionID);
		JDBMHNPalletTableModelProperties mhntable = new JDBMHNPalletTableModelProperties(mhnTemp.getMHNDataResultSet(listStatement));
		TableRowSorter<JDBMHNPalletTableModelProperties> sorter = new TableRowSorter<JDBMHNPalletTableModelProperties>(mhntable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(mhntable);
		jScrollPane1.setViewportView(jTable1);

		jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.SSCC_Col).setPreferredWidth(132);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.Material_Col).setPreferredWidth(68);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.Batch_Col).setPreferredWidth(75);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.Process_Order_Col).setPreferredWidth(82);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.Quantity_Col).setPreferredWidth(75);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.Uom_Col).setPreferredWidth(35);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.Date_of_Manufacture_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.SSCC_Status_Col).setPreferredWidth(132);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.Batch_Status_Col).setPreferredWidth(95);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.Location_Col).setPreferredWidth(88);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelProperties.Decision_Col).setPreferredWidth(80);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, false, 0, mhntable.getRowCount());

		populateSummary();
	}

	private void populateSummary()
	{

		JDBMHN mhnTemp = new JDBMHN(Common.selectedHostID, Common.sessionID);
		mhnTemp.getPalletDecisionSummmary(masterHoldNoticeNumber);

		DefaultComboBoxModel<String> DefComboBoxMod = new DefaultComboBoxModel<String>();

		LinkedList<String> tempTypeList = mhnTemp.getPalletDecisionSummmary(masterHoldNoticeNumber);
		int sel = -1;
		for (int j = 0; j < tempTypeList.size(); j++)
		{
			String t = tempTypeList.get(j);
			DefComboBoxMod.addElement(t);
		}

		ListModel<String> jList1Model = DefComboBoxMod;
		list.setModel(jList1Model);
		list.setSelectedIndex(sel);
		list.setCellRenderer(Common.renderer_list);
		list.ensureIndexIsVisible(sel);
	}

	private void print()
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("MHN_NUMBER", masterHoldNoticeNumber);
		JLaunchReport.runReport("RPT_MHN", parameters, "", null, "");
	}

	public void refreshData()
	{
		buildSQL();
		populateList();
	}

	private void save()
	{
		mhn.setComment(jTextFieldComment.getText());
		mhn.setReason1(((JDBMHNReasons) jTextFieldReason1.getSelectedItem()).getReason());
		mhn.setReason2(((JDBMHNReasons) textFieldReason2.getSelectedItem()).getReason());
		mhn.setReason3(((JDBMHNReasons) textFieldReason3.getSelectedItem()).getReason());
		mhn.setInitiator(jTextFieldInitiator.getText());
		mhn.setRecorder(jTextFieldRecorder.getText());
		mhn.setAuthorisor(jTextFieldAuthorisor.getText());
		mhn.setResource(jTextFieldResource.getText());
		mhn.setWriteOffRef(jTextFieldWriteOffRef.getText());
		mhn.setGS_GSTD_REF(jTextFieldGS_GSTD_REF.getText());
		mhn.setStatus(jTextFieldStatus.getSelectedItem().toString());
		Date d = dateControlExpected.getDate();
		mhn.setDateExpected(JUtility.getTimestampFromDate(d));
		mhn.update();

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
		refreshData();
	}
}
