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
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
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
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.renderer.TableCellRenderer_MHNPallet;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBMHNPalletTableModelProperties;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameMHNProperties class allows the user to edit a single Master Hold Notice in the APP_MHN table.
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMHNProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameMHNAssign JInternalFrameMHNAssign
 * @see com.commander4j.app.JInternalFrameMHNAdmin JInternalFrameMHNAdmin
 * @see com.commander4j.db.JDBMHN JDBMHN
 */
public class JInternalFrameMHNProperties extends JInternalFrame
{
	private JLabel4j_std jStatusText;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldRecorder;
	private JLabel4j_std jLabelInitiator;
	private JButton4j jButtonClose;
	private JButton4j jButtonPrint;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JLabel4j_std jLabelReason;
	private JToggleButton jToggleButtonSequence;
	private JButton4j jButtonSearch;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel1SortBy;
	private JComboBox4j<JDBMHNReasons> jTextFieldReason1;
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
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private JDBMHN mhn = new JDBMHN(Common.selectedHostID, Common.sessionID);
	private JDBMHNDecisions mhnDecisions = new JDBMHNDecisions(Common.selectedHostID, Common.sessionID);
	private JDBMHNReasons mhnReasons = new JDBMHNReasons(Common.selectedHostID, Common.sessionID);
	private String masterHoldNoticeNumber = "";
	private Vector<JDBMHNDecisions> decisionList = new Vector<JDBMHNDecisions>();
	private Vector<JDBMHNReasons> reasonList = new Vector<JDBMHNReasons>();
	private JMenu4j menu;
	private JMenuItem4j menuItem;
	private JDateControl dateControlExpected = new JDateControl();
	private JLabel4j_std label;
	private JInternalFrameMHNProperties me;
	private JList4j<String> list = new JList4j<String>();
	private JTextField4j jTextFieldAuthorisor;
	private JComboBox4j<JDBMHNReasons> textFieldReason2;
	private JComboBox4j<JDBMHNReasons> textFieldReason3;
	private JButton4j button_1;
	private PreparedStatement listStatement;
	private JTextField4j jTextFieldWriteOffRef = new JTextField4j(50);

	private void sortBy(String fieldname)
	{
		jComboBoxSortBy.setSelectedItem(fieldname);
		refreshData();
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

	private void addPallets()
	{
		JLaunchMenu.runForm(me, "FRM_ADMIN_MHN_ASSIGN", jTextFieldMHN.getText());
	}

	private void populateList()
	{
		JDBMHN mhnTemp = new JDBMHN(Common.selectedHostID, Common.sessionID);
		JDBMHNPalletTableModelProperties mhntable = new JDBMHNPalletTableModelProperties(mhnTemp.getMHNDataResultSet(listStatement));
		TableRowSorter<JDBMHNPalletTableModelProperties> sorter = new TableRowSorter<JDBMHNPalletTableModelProperties>(mhntable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(mhntable);
		jScrollPane1.setViewportView(jTable1);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jTable1.setFont(Common.font_list);
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

	private void deleteRecord()
	{
		int rowCount = jTable1.getSelectedRowCount();

		if (rowCount >= 0)
		{
			int[] rows = jTable1.getSelectedRows();

			JDBPallet temp = new JDBPallet(Common.selectedHostID, Common.sessionID);

			int n = JOptionPane.showConfirmDialog(Common.mainForm, "Delete " + String.valueOf(rowCount) + " SSCC(s) from Master Hold Notice " + jTextFieldMHN.getText() + " ?", "Confirm",
					JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
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
						jStatusText.setText("SSCC "+sscc+" removed from MHN "+masterHoldNoticeNumber);
						Rectangle progressRect = jStatusText.getBounds();  
						progressRect.x = 0;  
						progressRect.y = 0;  
						jStatusText.paintImmediately( progressRect );
					}
					else
					{
						JOptionPane.showMessageDialog(Common.mainForm, "Cannot remove SSCC from MHN ["+masterHoldNoticeNumber+"] as status is not Unrestricted.",lang.get("err_Error"),JOptionPane.ERROR_MESSAGE,Common.icon_confirm);
					}
				}
				buildSQL();
				populateList();
			}
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
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_PALLET"));
		query.addParamtoSQL("MHN_Number=", masterHoldNoticeNumber);
		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		result = query.getPreparedStatement();
		return result;
	}

	private void buildSQL()
	{
		JDBQuery.closeStatement(listStatement);

		listStatement = buildSQLr();
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
		mhn.setStatus(jTextFieldStatus.getSelectedItem().toString());
		Date d = dateControlExpected.getDate();
		mhn.setDateExpected(JUtility.getTimestampFromDate(d));
		mhn.update();

	}

	public JInternalFrameMHNProperties(String mhnNumber) {

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

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(726, 61, 258, 124);
		jDesktopPane1.add(scrollPane);

		ListModel<String> jList1Model = new DefaultComboBoxModel<String>(new String[] { "Item One", "Item Two" });
		list.setEnabled(false);
		list.setBackground(new Color(255, 255, 255));
		list.setFont(Common.font_std);
		list.setForeground(Color.BLACK);

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		list.setModel(jList1Model);

		JLabel4j_std jLabelAuthorisor = new JLabel4j_std();
		jLabelAuthorisor.setText(lang.get("lbl_Authorisor"));
		jLabelAuthorisor.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabelAuthorisor.setBounds(785, 7, 79, 21);
		jDesktopPane1.add(jLabelAuthorisor);

		jTextFieldAuthorisor = new JTextField4j(JDBMHN.field_authorisor);
		jTextFieldAuthorisor.setEditable(false);
		jTextFieldAuthorisor.setBounds(875, 7, 89, 21);
		jDesktopPane1.add(jTextFieldAuthorisor);

		JLabel4j_std label_1 = new JLabel4j_std();
		label_1.setText(lang.get("lbl_Reason") + " 2");
		label_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label_1.setBounds(5, 65, 87, 21);
		jDesktopPane1.add(label_1);

		ComboBoxModel<JDBMHNReasons> jComboBox2Model = new DefaultComboBoxModel<JDBMHNReasons>(reasonList);
		textFieldReason2 = new JComboBox4j<JDBMHNReasons>();
		textFieldReason2.setModel(jComboBox2Model);
		textFieldReason2.setEditable(false);
		textFieldReason2.setBounds(101, 65, 273, 23);
		jDesktopPane1.add(textFieldReason2);

		JLabel4j_std label_2 = new JLabel4j_std();
		label_2.setText(lang.get("lbl_Reason") + " 3");
		label_2.setHorizontalAlignment(SwingConstants.TRAILING);
		label_2.setBounds(5, 93, 87, 21);
		jDesktopPane1.add(label_2);

		ComboBoxModel<JDBMHNReasons> jComboBox3Model = new DefaultComboBoxModel<JDBMHNReasons>(reasonList);
		textFieldReason3 = new JComboBox4j<JDBMHNReasons>();
		textFieldReason3.setModel(jComboBox3Model);
		textFieldReason3.setEditable(false);
		textFieldReason3.setBounds(101, 93, 273, 23);
		jDesktopPane1.add(textFieldReason3);
		
		JButton4j button = new JButton4j(Common.icon_lookup);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JLaunchLookup.dlgAutoExec = true;
				JLaunchLookup.dlgCriteriaDefault = "";

				if (JLaunchLookup.users()) {
					jTextFieldInitiator.setText(JLaunchLookup.dlgResult);
				}
			}
		});
		button.setBounds(755, 7, 21, 21);
		jDesktopPane1.add(button);
		{
			button_1 = new JButton4j(Common.icon_lookup);
			button_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JLaunchLookup.dlgAutoExec = true;
					JLaunchLookup.dlgCriteriaDefault = "";

					if (JLaunchLookup.users()) {
						jTextFieldAuthorisor.setText(JLaunchLookup.dlgResult);
					}
				}
			});
			button_1.setBounds(964, 7, 21, 21);
			jDesktopPane1.add(button_1);
		}
		
		jTextFieldWriteOffRef.setBounds(834, 37, 149, 21);
		jDesktopPane1.add(jTextFieldWriteOffRef);
		
		JLabel4j_std jLabelWriteOffRef = new JLabel4j_std();
		jLabelWriteOffRef.setText(lang.get("lbl_Write_Off_Ref"));
		jLabelWriteOffRef.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabelWriteOffRef.setBounds(688, 37, 141, 21);
		jDesktopPane1.add(jLabelWriteOffRef);

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

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(770, 478));
			this.setBounds(0, 0, 1031, 593);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Edit Master Hold Notice");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabelRecorder = new JLabel4j_std();
					jDesktopPane1.add(jLabelRecorder);
					jLabelRecorder.setText(lang.get("lbl_Recorder"));
					jLabelRecorder.setBounds(372, 7, 71, 21);
					jLabelRecorder.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					JButton4j btnExcel = new JButton4j(Common.icon_XLS);
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
					btnExcel.setBounds(516, 153, 101, 32);
					jDesktopPane1.add(btnExcel);
				}
				{
					jTextFieldRecorder = new JTextField4j(JDBMHN.field_recorder);
					jTextFieldRecorder.setEditable(false);
					jDesktopPane1.add(jTextFieldRecorder);
					jTextFieldRecorder.setBounds(452, 7, 121, 21);
				}
				{
					jLabelComment = new JLabel4j_std();
					jDesktopPane1.add(jLabelComment);
					jLabelComment.setText(lang.get("lbl_Comment"));
					jLabelComment.setBounds(13, 123, 79, 21);
					jLabelComment.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldComment = new JTextField4j(JDBMHN.field_comments);
					jDesktopPane1.add(jTextFieldComment);
					jTextFieldComment.setBounds(101, 123, 610, 21);
				}
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 190, 1001, 334);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][] { { "One", "Two" }, { "Three", "Four" } }, new String[] { "Column 1", "Column 2" });
						jTable1 = new JTable();
						jTable1.setToolTipText("Update decision by selecting one or more rows and then use the mouse right click popup menu options. ");
						jTable1.setDefaultRenderer(Object.class, new TableCellRenderer_MHNPallet());
						jScrollPane1.setViewportView(jTable1);

						jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						jTable1.getTableHeader().setFont(Common.font_table_header);
						jTable1.setModel(jTable1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jTable1, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh);
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
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add);
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

													int n = JOptionPane.showConfirmDialog(Common.mainForm, "Update Decision  of " + String.valueOf(rowCount) + " SSCC(s) from Master Hold Notice "
															+ jTextFieldMHN.getText() + " to " + decisionList.get(xx).getDescription() + " ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
													if (n == 0)
													{
														save();
														for (int l = 0; l < rows.length; l++)
														{
															String sscc = jTable1.getValueAt(rows[l], JDBMHNPalletTableModelProperties.SSCC_Col).toString();
															temp.getPalletProperties(sscc);
															temp.updateMHNDecision(decisionList.get(xx).getDecision());
															if (decisionList.get(xx).getStatus().equals("")==false)
															{
																temp.updateStatus(decisionList.get(xx).getStatus(),true);

																jStatusText.setText("SSCC "+sscc+" updated to  "+decisionList.get(xx).getStatus());
																Rectangle progressRect = jStatusText.getBounds();  
																progressRect.x = 0;  
																progressRect.y = 0;  
																jStatusText.paintImmediately( progressRect );
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

						}
					}
				}
				{
					jLabelInitiator = new JLabel4j_std();
					jDesktopPane1.add(jLabelInitiator);
					jLabelInitiator.setText(lang.get("lbl_Initiator"));
					jLabelInitiator.setBounds(575, 7, 79, 21);
					jLabelInitiator.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldInitiator = new JTextField4j(JDBMHN.field_initiator);
					jTextFieldInitiator.setEditable(false);
					jDesktopPane1.add(jTextFieldInitiator);
					jTextFieldInitiator.setBounds(661, 7, 94, 21);
				}
				{
					jTextFieldMHN = new JTextField4j(JDBMHN.field_mhn_number);
					jTextFieldMHN.setForeground(Color.RED);
					jTextFieldMHN.setEditable(false);
					jDesktopPane1.add(jTextFieldMHN);
					jTextFieldMHN.setBounds(101, 7, 87, 21);
				}
				{
					jLabelMHN = new JLabel4j_std();
					jDesktopPane1.add(jLabelMHN);
					jLabelMHN.setText(lang.get("lbl_MHN_Number"));
					jLabelMHN.setBounds(5, 5, 87, 21);
					jLabelMHN.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelResource = new JLabel4j_std();
					jDesktopPane1.add(jLabelResource);
					jLabelResource.setText(lang.get("lbl_Process_Order_Required_Resource"));
					jLabelResource.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelResource.setBounds(372, 37, 173, 21);
				}
				{
					jTextFieldResource = new JTextField4j(JDBProcessOrder.field_required_resource);
					jDesktopPane1.add(jTextFieldResource);
					jTextFieldResource.setBounds(562, 37, 121, 21);
				}
				{
					jLabelReason = new JLabel4j_std();
					jDesktopPane1.add(jLabelReason);
					jLabelReason.setText(lang.get("lbl_Reason") + " 1");
					jLabelReason.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelReason.setBounds(5, 37, 87, 21);
				}
				{
					ComboBoxModel<JDBMHNReasons> jComboBox2Model = new DefaultComboBoxModel<JDBMHNReasons>(reasonList);
					jTextFieldReason1 = new JComboBox4j<JDBMHNReasons>();
					jTextFieldReason1.setModel(jComboBox2Model);
					jTextFieldReason1.setEditable(false);
					jDesktopPane1.add(jTextFieldReason1);
					jTextFieldReason1.setBounds(101, 37, 273, 23);

				}
				{
					jLabelStatus = new JLabel4j_std();
					jDesktopPane1.add(jLabelStatus);
					jLabelStatus.setText(lang.get("lbl_Status"));
					jLabelStatus.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelStatus.setBounds(189, 7, 79, 21);
				}
				{
					jTextFieldStatus = new JComboBox4j<String>();
					jTextFieldStatus.setModel(new DefaultComboBoxModel<String>(new String[] { "Active", "Closed" }));
					jDesktopPane1.add(jTextFieldStatus);
					jTextFieldStatus.setBounds(273, 8, 101, 23);
				}
				{
					jLabel1SortBy = new JLabel4j_std();
					jDesktopPane1.add(jLabel1SortBy);
					jLabel1SortBy.setText(lang.get("lbl_Sort_By"));
					jLabel1SortBy.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1SortBy.setBounds(372, 93, 71, 21);
				}
				{
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(new DefaultComboBoxModel<String>(new String[] { "DECISION", "SSCC", "DATE_OF_MANUFACTURE", "BATCH", "MATERIAL", "PROCESS_ORDER" }));
					jComboBoxSortBy.setBounds(452, 93, 231, 23);
					jComboBoxSortBy.setSelectedItem("DATE_OF_MANUFACTURE");
					jComboBoxSortBy.setRequestFocusEnabled(false);
				}
				{
					jButtonSearch = new JButton4j(Common.icon_refresh);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Refresh"));
					jButtonSearch.setMnemonic(lang.getMnemonicChar());
					jButtonSearch.setBounds(5, 153, 101, 32);
					jButtonSearch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							refreshData();
						}
					});
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_ASSIGN"));
					jButtonAdd.setBounds(108, 153, 101, 32);
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							addPallets();
						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MHN_DELETE"));
					jButtonDelete.setBounds(210, 153, 101, 32);
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
					jButtonPrint.setBounds(414, 153, 101, 32);
					jButtonPrint.setEnabled(true);
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							save();
							print();
						}
					});
				}
				{

					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(618, 153, 101, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							save();
							JDBQuery.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(690, 93, 21, 21);
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
					jStatusText.setBounds(0, 526, 985, 21);
					jDesktopPane1.add(jStatusText);
				}

				JButton4j button = new JButton4j(Common.icon_batch);
				button.setText(lang.get("btn_Material_Batches"));
				button.setMnemonic('0');
				button.setBounds(312, 153, 101, 32);
				button.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH"));
				jDesktopPane1.add(button);

				dateControlExpected.setBounds(563, 61, 125, 25);
				jDesktopPane1.add(dateControlExpected);

				JCalendarButton calendarButtonExpected = new JCalendarButton(dateControlExpected);
				calendarButtonExpected.setBounds(690, 65, 21, 21);
				jDesktopPane1.add(calendarButtonExpected);
				{
					label = new JLabel4j_std();
					label.setText(lang.get("lbl_Expected"));
					label.setHorizontalAlignment(SwingConstants.TRAILING);
					label.setBounds(372, 65, 173, 21);
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
		export.saveAs("mhn_" + masterHoldNoticeNumber + ".xls", mhn.getMHNDataResultSet(temp), Common.mainForm);
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
