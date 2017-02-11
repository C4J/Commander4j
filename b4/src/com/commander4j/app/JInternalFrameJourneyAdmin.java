package com.commander4j.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBJourney;
import com.commander4j.db.JDBLanguage;
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
import com.commander4j.tablemodel.JDBJourneyRefTableModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameJourneyAdmin extends JInternalFrame
{
	private JButton4j jButtonExcel;
	private JButton4j jButtonPrint;
	private JLabel4j_std jStatusText;
	private JButton4j jButtonAdd;
	private JCheckBox4j jCheckBoxTo;
	private JCheckBox4j jCheckBoxFrom;
	private JLabel4j_std jLabel5_1;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JToggleButton jToggleButtonSequence;
	private JTextField4j jTextFieldDespatchNo;
	private JComboBox4j<String> jComboBoxStatus;
	private JLabel4j_std jLabel5;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldJourneyRef;
	private JLabel4j_std jLabel1;
	private JTable jTable1;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSearch;
	private JScrollPane jScrollPane1;
	private String lref;
	private String lbatch;
	private static boolean dlg_sort_descending = false;
	private JDateControl timeslotFrom = new JDateControl();
	private JDateControl timeslotTo = new JDateControl();
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private JCalendarButton calendarButtonexpiryFrom;
	private JCalendarButton calendarButtonexpiryTo;
	private PreparedStatement listStatement;
	private JTextField4j jTextFieldLocationID = new JTextField4j(15);
	private JCheckBox4j jCheckBoxLimit;
	private JSpinner jSpinnerLimit;
	private JTextField4j jTextFieldLoadType = new JTextField4j();
	private JTextField4j jTextFieldLoadTypeDesc = new JTextField4j();
	private JTextField4j jTextFieldHaulier = new JTextField4j();

	public JInternalFrameJourneyAdmin()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_JOURNEY_REF where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_JOURNEY"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);
	}

	private void clearFilter()
	{

		jTextFieldJourneyRef.setText("");

		jTextFieldDespatchNo.setText("");

		jTextFieldLocationID.setText("");

		jComboBoxStatus.setSelectedItem("");

		timeslotTo.setEnabled(false);
		timeslotFrom.setEnabled(false);
		jCheckBoxFrom.setSelected(false);
		jCheckBoxTo.setSelected(false);
		
		jTextFieldLoadType.setText("");
		jTextFieldLoadTypeDesc.setText("");
		jTextFieldHaulier.setText("");

		search();

	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals(lang.get("lbl_Journey_Ref")) == true)
			{
				jTextFieldJourneyRef.setText(jTable1.getValueAt(row, JDBJourneyRefTableModel.Journey_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Despatch_No")) == true)
			{
				jTextFieldDespatchNo.setText(jTable1.getValueAt(row, JDBJourneyRefTableModel.Despatch_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Location_ID")) == true)
			{
				jTextFieldLocationID.setText(jTable1.getValueAt(row, JDBJourneyRefTableModel.Location_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Journey_Status")) == true)
			{
				jComboBoxStatus.setSelectedItem(jTable1.getValueAt(row, JDBJourneyRefTableModel.Status_Col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Timeslot")) == true)
			{
				String dateString = jTable1.getValueAt(row, JDBJourneyRefTableModel.Timeslot_Col).toString();
				Date parsedDate;
				try
				{
					parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);
				} catch (ParseException e)
				{
					parsedDate = new java.util.Date();
					e.printStackTrace();
				}
				timeslotTo.setEnabled(true);
				timeslotFrom.setEnabled(true);
				jCheckBoxFrom.setSelected(true);
				jCheckBoxTo.setSelected(true);
				timeslotFrom.setDate(parsedDate);
				timeslotTo.setDate(parsedDate);
			}

			search();

		}
	}

	public JInternalFrameJourneyAdmin(String material)
	{
		this();
		lref = material;
		jTextFieldJourneyRef.setText(lref);
		jTextFieldDespatchNo.setText(lbatch);
		buildSQL();
		populateList();
	}
	
	private void print()
	{
		buildSQL();
		JLaunchReport.runReport("RPT_JOURNEYS", null, "", listStatement, "");
	}
	
	private void search()
	{
		buildSQL();
		populateList();
	}

	private void excel()
	{
		JDBJourney jref = new JDBJourney(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		buildSQL();
		export.saveAs("journeys.xls", jref.getJourneyRefDataResultSet(listStatement), Common.mainForm);
	}

	private void addRecord()
	{
		String ljourneyref = "";

		ljourneyref = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Journey_Ref_Input"));

		if (ljourneyref != null)
		{
			if (ljourneyref.equals("") == false)
			{
				ljourneyref = ljourneyref.toUpperCase();
				JDBJourney jref = new JDBJourney(Common.selectedHostID, Common.sessionID);
				if (jref.isValidJourneyRef(ljourneyref) == false)
				{
					JLaunchMenu.runForm("FRM_ADMIN_JOURNEY_EDIT", ljourneyref);
				} else
				{
					JOptionPane.showMessageDialog(Common.mainForm, "Journey Ref [" + ljourneyref + "] already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
				}

			}
		}

	}

	private void sortBy(String fieldname)
	{
		jComboBoxSortBy.setSelectedItem(fieldname);
		search();
	}

	private void setSequence(boolean descending)
	{
		// jToggleButtonSequence.setSelected(true);
		if (jToggleButtonSequence.isSelected() == true)
		{
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending);
		} else
		{
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending);
		}
	}

	public JInternalFrameJourneyAdmin(String material, String batch)
	{
		this();
		lref = material;
		lbatch = batch;
		jTextFieldJourneyRef.setText(lref);
		jTextFieldDespatchNo.setText(lbatch);
		buildSQL();
		populateList();
	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_JOURNEY"));
		query.addParamtoSQL("journey_ref=", jTextFieldJourneyRef.getText());
		query.addParamtoSQL("despatch_no=", jTextFieldDespatchNo.getText());
		query.addParamtoSQL("status=", jComboBoxStatus.getSelectedItem().toString());
		query.addParamtoSQL("location_id_to=", jTextFieldLocationID.getText());
		query.addParamtoSQL("upper(load_type)=", jTextFieldLoadType.getText().toUpperCase() );
		query.addParamtoSQL("upper(load_type_desc)=", jTextFieldLoadTypeDesc.getText().toUpperCase());
		query.addParamtoSQL("haulier=", jTextFieldHaulier.getText());

		if (jCheckBoxFrom.isSelected())
		{
			query.addParamtoSQL("timeslot>=", JUtility.getTimestampFromDate(timeslotFrom.getDate()));

		}

		if (jCheckBoxTo.isSelected())
		{
			query.addParamtoSQL("timeslot<=", JUtility.getTimestampFromDate(timeslotTo.getDate()));
		}

		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(jCheckBoxLimit.isSelected(), Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), jSpinnerLimit.getValue());

		query.bindParams();
		listStatement = query.getPreparedStatement();
	}

	private void populateList()
	{
		JDBJourney journeyref = new JDBJourney(Common.selectedHostID, Common.sessionID);
		JDBJourneyRefTableModel journeyTable = new JDBJourneyRefTableModel(journeyref.getJourneyRefDataResultSet(listStatement));
		TableRowSorter<JDBJourneyRefTableModel> sorter = new TableRowSorter<JDBJourneyRefTableModel>(journeyTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(journeyTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(0).setPreferredWidth(95);
		jTable1.getColumnModel().getColumn(1).setPreferredWidth(95);
		jTable1.getColumnModel().getColumn(2).setPreferredWidth(85);
		jTable1.getColumnModel().getColumn(3).setPreferredWidth(85);
		jTable1.getColumnModel().getColumn(4).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(5).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(6).setPreferredWidth(160);
		jTable1.getColumnModel().getColumn(7).setPreferredWidth(160);
		jTable1.getColumnModel().getColumn(8).setPreferredWidth(120);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, false, 0, journeyTable.getRowCount());
	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lref = jTable1.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_ADMIN_JOURNEY_EDIT", lref);
		}

	}

	private void deleteRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lref = jTable1.getValueAt(row, 0).toString();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Journey_Ref_Delete") + " " + lref + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (question == 0)
			{
				JDBJourney journeyref = new JDBJourney(Common.selectedHostID, Common.sessionID);

				journeyref.setJourneyRef(lref);
				journeyref.delete();
				populateList();
			}
		}

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 1003, 660);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Journey References");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(483, 266));
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.setLayout(null);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 193, 990, 410);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][]
						{
								{ "One", "Two" },
								{ "Three", "Four" } }, new String[]
						{ "Column 1", "Column 2" });
						jTable1 = new JTable();
						jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);
						jTable1.getTableHeader().setFont(Common.font_table_header);
						jTable1.getTableHeader().setForeground(Common.color_tableHeaderFont);
						jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						jTable1.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_JOURNEY_EDIT"))
									{
										editRecord();
									}
								}
							}
						});

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jTable1, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_search);
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_JOURNEY_ADD"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										addRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_JOURNEY_EDIT"));
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
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										excel();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Excel"));
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
											sortBy("JOURNEY_REF");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Journey_Ref"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("DESPATCH_NO");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Despatch_No"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("LOCATION_ID_TO");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Location_ID"));
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
									newItemMenuItem.setText(lang.get("lbl_Journey_Status"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("TIMESLOT");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Timeslot"));
									sortByMenu.add(newItemMenuItem);
								}
							}

							{
								final JMenu4j filterByMenu = new JMenu4j();
								filterByMenu.setText(lang.get("lbl_Filter_By"));
								popupMenu.add(filterByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Despatch_No"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Location_ID"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Journey_Status"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Timeslot"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Journey_Ref"));
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
					jButtonSearch = new JButton4j(Common.icon_search);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonSearch.setBounds(0, 149, 110, 32);
					jButtonSearch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							search();

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(java.awt.event.KeyEvent.VK_E);
					jButtonEdit.setBounds(330, 149, 110, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_JOURNEY_EDIT"));
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
					jButtonHelp.setBounds(770, 149, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setBounds(880, 149, 110, 32);
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
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Journey_Ref"));
					jLabel1.setBounds(12, 11, 113, 21);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldJourneyRef = new JTextField4j(JDBJourney.field_journey_ref);
					jDesktopPane1.add(jTextFieldJourneyRef);
					jTextFieldJourneyRef.setBounds(132, 11, 168, 23);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Despatch_No"));
					jLabel3.setBounds(299, 11, 113, 21);
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldDespatchNo = new JTextField4j(JDBJourney.field_despatch_no);
					jDesktopPane1.add(jTextFieldDespatchNo);
					jTextFieldDespatchNo.setBounds(419, 11, 174, 23);
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Sort_By"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(220, 116, 113, 21);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[]
					{ "JOURNEY_REF", "LOCATION_ID_TO", "DESPATCH_NO", "STATUS", "TIMESLOT", "DATE_UPDATED","LOAD_TYPE" ,"LOAD_TYPE_DESC","HAULIER"});
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					jComboBoxSortBy.setBounds(342, 116, 141, 23);
					jComboBoxSortBy.setSelectedItem("TIMESLOT");
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Journey_Status"));
					jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5.setBounds(12, 45, 113, 21);
				}
				{
					ComboBoxModel<String> jComboBoxStatusModel = new DefaultComboBoxModel<String>(Common.JourneyRefStatusIncBlank);
					jComboBoxStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxStatus);
					jComboBoxStatus.setModel(jComboBoxStatusModel);
					jComboBoxStatus.setBounds(132, 44, 168, 23);
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(489, 118, 21, 21);
					jToggleButtonSequence.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
					jToggleButtonSequence.setSelected(true);
				}

				{
					timeslotFrom.setBounds(440, 79, 125, 25);
					timeslotFrom.setEnabled(false);
					jDesktopPane1.add(timeslotFrom);
				}

				{
					timeslotTo.setBounds(730, 79, 125, 25);
					timeslotTo.setEnabled(false);
					jDesktopPane1.add(timeslotTo);
				}

				{
					jLabel5_1 = new JLabel4j_std();
					jLabel5_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5_1.setText(lang.get("lbl_Timeslot"));
					jLabel5_1.setBounds(299, 83, 113, 21);
					jDesktopPane1.add(jLabel5_1);
				}

				{
					jCheckBoxFrom = new JCheckBox4j();
					jCheckBoxFrom.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							if (jCheckBoxFrom.isSelected())
							{
								timeslotFrom.setEnabled(true);
								calendarButtonexpiryFrom.setEnabled(true);
							} else
							{
								timeslotFrom.setEnabled(false);
								calendarButtonexpiryFrom.setEnabled(false);
							}
						}
					});
					jCheckBoxFrom.setBackground(new Color(255, 255, 255));
					jCheckBoxFrom.setBounds(419, 80, 21, 21);
					jDesktopPane1.add(jCheckBoxFrom);
				}

				{
					jCheckBoxTo = new JCheckBox4j();
					jCheckBoxTo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							if (jCheckBoxTo.isSelected())
							{
								timeslotTo.setEnabled(true);
								calendarButtonexpiryTo.setEnabled(true);
							} else
							{
								timeslotTo.setEnabled(false);
								calendarButtonexpiryTo.setEnabled(false);
							}
						}
					});
					jCheckBoxTo.setBackground(new Color(255, 255, 255));
					jCheckBoxTo.setBounds(709, 80, 21, 21);
					jDesktopPane1.add(jCheckBoxTo);
				}

				{
					jButtonAdd = new JButton4j(Common.icon_add);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_JOURNEY_ADD"));
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							addRecord();
						}
					});
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(220, 149, 110, 32);
					jDesktopPane1.add(jButtonAdd);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 607, 970, 21);
					jDesktopPane1.add(jStatusText);
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							excel();
						}
					});

					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(660, 149, 110, 32);
					jDesktopPane1.add(jButtonExcel);
				}
				
				{
					jButtonPrint = new JButton4j(Common.icon_print);
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							print();
						}
					});

					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(550, 149, 110, 32);
					jDesktopPane1.add(jButtonPrint);
				}

				{
					calendarButtonexpiryFrom = new JCalendarButton(timeslotFrom);
					calendarButtonexpiryFrom.setEnabled(false);
					calendarButtonexpiryFrom.setBounds(572, 81, 21, 21);
					jDesktopPane1.add(calendarButtonexpiryFrom);
				}
				{
					calendarButtonexpiryTo = new JCalendarButton(timeslotTo);
					calendarButtonexpiryTo.setEnabled(false);
					calendarButtonexpiryTo.setBounds(861, 81, 21, 21);
					jDesktopPane1.add(calendarButtonexpiryTo);
				}
				{
					JButton4j jButtonDelete = new JButton4j(Common.icon_delete);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_JOURNEY_DELETE"));
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							deleteRecord();
						}
					});
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic('0');
					jButtonDelete.setBounds(440, 149, 110, 32);
					jDesktopPane1.add(jButtonDelete);
				}

				{
					JLabel4j_std label4j_std = new JLabel4j_std();
					label4j_std.setText(lang.get("lbl_Location_ID"));
					label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
					label4j_std.setBounds(599, 11, 113, 21);
					jDesktopPane1.add(label4j_std);

					jTextFieldLocationID.setBounds(719, 11, 139, 23);
					jDesktopPane1.add(jTextFieldLocationID);

					JButton4j button4j_LocationLookup = new JButton4j(Common.icon_lookup);
					button4j_LocationLookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "Y";
							if (JLaunchLookup.locations())
							{
								jTextFieldLocationID.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					button4j_LocationLookup.setBounds(866, 11, 21, 21);
					jDesktopPane1.add(button4j_LocationLookup);
				}

				{
					JButton4j jButtonClear = new JButton4j(Common.icon_search);
					jButtonClear.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							clearFilter();
						}
					});
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jButtonClear.setMnemonic(KeyEvent.VK_S);
					jButtonClear.setBounds(110, 149, 110, 32);
					jDesktopPane1.add(jButtonClear);
				}

				{
					SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
					jSpinnerIntModel.setMinimum(1);
					jSpinnerIntModel.setMaximum(5000);
					jSpinnerIntModel.setStepSize(1);
					jSpinnerLimit = new JSpinner();
					JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
					ne.getTextField().setFont(Common.font_std);
					jSpinnerLimit.setEditor(ne);
					jSpinnerLimit.setModel(jSpinnerIntModel);
					jSpinnerLimit.setBounds(644, 118, 68, 21);
					jSpinnerLimit.setValue(1000);
					jSpinnerLimit.getEditor().setSize(45, 21);
					jDesktopPane1.add(jSpinnerLimit);
				}

				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Limit"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(522, 118, 91, 21);
				}
				{
					jCheckBoxLimit = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxLimit);
					jCheckBoxLimit.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxLimit.setBounds(616, 118, 21, 21);
					jCheckBoxLimit.setSelected(true);
					jCheckBoxLimit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxLimit.isSelected())
							{
								jSpinnerLimit.setEnabled(true);
							} else
							{
								jSpinnerLimit.setEnabled(false);
							}
						}
					});
				}
				
				JLabel4j_std label4j_stdLoadType = new JLabel4j_std();
				label4j_stdLoadType.setText(lang.get("lbl_LoadType"));
				label4j_stdLoadType.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_stdLoadType.setBounds(299, 45, 113, 21);
				jDesktopPane1.add(label4j_stdLoadType);
				
				jTextFieldLoadType.setBounds(419, 44, 179, 23);
				jDesktopPane1.add(jTextFieldLoadType);
				
				jTextFieldLoadTypeDesc.setBounds(719, 44, 262, 23);
				jDesktopPane1.add(jTextFieldLoadTypeDesc);
				
				JLabel4j_std label4j_stdLoadTypeDesc = new JLabel4j_std();
				label4j_stdLoadTypeDesc.setText(lang.get("lbl_LoadTypeDesc"));
				label4j_stdLoadTypeDesc.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_stdLoadTypeDesc.setBounds(599, 45, 113, 21);
				jDesktopPane1.add(label4j_stdLoadTypeDesc);
				
				jTextFieldHaulier.setBounds(132, 81, 168, 23);
				jDesktopPane1.add(jTextFieldHaulier);
				
				JLabel4j_std label4j_stdHaulier = new JLabel4j_std();
				label4j_stdHaulier.setText(lang.get("lbl_Haulier"));
				label4j_stdHaulier.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_stdHaulier.setBounds(12, 83, 113, 21);
				jDesktopPane1.add(label4j_stdHaulier);

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
