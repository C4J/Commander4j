package com.commander4j.app;

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
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
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
import com.commander4j.tablemodel.JDBMaterialBatchTableModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameMaterialBatchAdmin extends JInternalFrame
{
	private JButton4j jButtonExcel;
	private JLabel4j_std jStatusText;
	private JButton4j jButtonAdd;
	private JCheckBox4j jCheckBoxTo;
	private JCheckBox4j jCheckBoxFrom;
	private JLabel4j_std jLabel5_2;
	private JLabel4j_std jLabel5_1;
	private JButton4j jButtonLookupBatch;
	private JButton4j jButtonLookupMaterial;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JToggleButton jToggleButtonSequence;
	private JTextField4j jTextFieldBatch;
	private JComboBox4j<String> jComboBoxStatus;
	private JLabel4j_std jLabel5;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabel1;
	private JTable jTable1;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSearch;
	private JScrollPane jScrollPane1;
	private String lmaterial;
	private String lbatch;
	private static boolean dlg_sort_descending = false;
	private JDateControl expiryFrom = new JDateControl();
	private JDateControl expiryTo = new JDateControl();
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private JCalendarButton calendarButtonexpiryFrom;
	private JCalendarButton calendarButtonexpiryTo;
	private PreparedStatement listStatement;

	public JInternalFrameMaterialBatchAdmin()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_BATCH"));
	
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);
	}

	private void clearFilter() {

		jTextFieldMaterial.setText("");

		jTextFieldBatch.setText("");

		jComboBoxStatus.setSelectedItem("");

		expiryTo.setEnabled(false);
		expiryFrom.setEnabled(false);
		jCheckBoxFrom.setSelected(false);
		jCheckBoxTo.setSelected(false);

		search();

	}

	private void filterBy(String fieldname) {
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals(lang.get("lbl_Material")) == true)
			{
				jTextFieldMaterial.setText(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals(lang.get("lbl_Material_Batch")) == true)
			{
				jTextFieldBatch.setText(jTable1.getValueAt(row, 1).toString());
			}

			if (fieldname.equals("lbl_Material_Batch_Status") == true)
			{
				jComboBoxStatus.setSelectedItem(jTable1.getValueAt(row, 2).toString());
			}

			if (fieldname.equals(lang.get("lbl_Material_Batch_Expiry_Date")) == true)
			{
				String dateString = jTable1.getValueAt(row, 3).toString();
				Date parsedDate;
				try
				{
					parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);
				}
				catch (ParseException e)
				{
					parsedDate = new java.util.Date();
					e.printStackTrace();
				}
				expiryTo.setEnabled(true);
				expiryFrom.setEnabled(true);
				jCheckBoxFrom.setSelected(true);
				jCheckBoxTo.setSelected(true);
				expiryFrom.setDate(parsedDate);
				expiryTo.setDate(parsedDate);
			}

			search();

		}
	}

	public JInternalFrameMaterialBatchAdmin(String material)
	{
		this();
		lmaterial = material;
		jTextFieldMaterial.setText(lmaterial);
		jTextFieldBatch.setText(lbatch);
		buildSQL();
		populateList();
	}

	private void search() {
		buildSQL();
		populateList();
	}

	private void excel() {
		JDBMaterialBatch materialBatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		buildSQL();
		export.saveAs("material_batch.xls", materialBatch.getMaterialBatchDataResultSet(listStatement), Common.mainForm);
	}

	private void addRecord() {
		String lmaterial = "";
		String lbatch = "";
		lmaterial = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Material_Input"));
		if (lmaterial != null)
		{
			if (lmaterial.equals("") == false)
			{
				JDBMaterial mat = new JDBMaterial(Common.selectedHostID, Common.sessionID);
				if (mat.isValidMaterial(lmaterial))
				{
					lbatch = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Material_Batch_Input"));
					if (lbatch != null)
					{
						if (lbatch.equals("") == false)
						{
							JDBMaterialBatch matbat = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
							if (matbat.isValidMaterialBatch(lmaterial, lbatch) == false)
							{
								JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_BATCH_EDIT", lmaterial, lbatch);
							}
							else
							{
								JOptionPane.showMessageDialog(Common.mainForm, "Material/Batch [" + lmaterial + " / " + lbatch + "] already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);
							}
						}
					}
				}
				else
				{
					JOptionPane.showMessageDialog(Common.mainForm, "Material [" + lmaterial + "] does not exist", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);
				}
			}
		}

	}

	private void sortBy(String fieldname) {
		jComboBoxSortBy.setSelectedItem(fieldname);
		search();
	}

	private void setSequence(boolean descending) {
		jToggleButtonSequence.setSelected(descending);
		if (jToggleButtonSequence.isSelected() == true)
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

	public JInternalFrameMaterialBatchAdmin(String material, String batch)
	{
		this();
		lmaterial = material;
		lbatch = batch;
		jTextFieldMaterial.setText(lmaterial);
		jTextFieldBatch.setText(lbatch);
		buildSQL();
		populateList();
	}

	private void buildSQL() {
	
		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL_BATCH"));
		query.addParamtoSQL("material=", jTextFieldMaterial.getText());
		query.addParamtoSQL("batch_number=", jTextFieldBatch.getText());
		query.addParamtoSQL("status=", jComboBoxStatus.getSelectedItem().toString());

		if (jCheckBoxFrom.isSelected())
		{
			query.addParamtoSQL("expiry_date>=", JUtility.getTimestampFromDate(expiryFrom.getDate()));

		}

		if (jCheckBoxTo.isSelected())
		{
			query.addParamtoSQL("expiry_date<=", JUtility.getTimestampFromDate(expiryTo.getDate()));
		}

		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(false,"none",0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
	}

	private void populateList() {
		JDBMaterialBatch materialBatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
		JDBMaterialBatchTableModel materialBatchTable = new JDBMaterialBatchTableModel(materialBatch.getMaterialBatchDataResultSet(listStatement));
		TableRowSorter<JDBMaterialBatchTableModel> sorter = new TableRowSorter<JDBMaterialBatchTableModel>(materialBatchTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(materialBatchTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(0).setPreferredWidth(95);
		jTable1.getColumnModel().getColumn(1).setPreferredWidth(95);
		jTable1.getColumnModel().getColumn(2).setPreferredWidth(95);
		jTable1.getColumnModel().getColumn(3).setPreferredWidth(120);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, false, 0, materialBatchTable.getRowCount());
	}

	private void editRecord() {
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lmaterial = jTable1.getValueAt(row, 0).toString();
			lbatch = jTable1.getValueAt(row, 1).toString();
			JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_BATCH_EDIT", lmaterial, lbatch);
		}

	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 448, 632);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Material Batches");
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
					jScrollPane1.setBounds(0, 210, 425, 349);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][] { { "One", "Two" }, { "Three", "Four" } }, new String[] { "Column 1", "Column 2" });
						jTable1 = new JTable();
						jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);
						jTable1.getTableHeader().setFont(Common.font_table_header);
						jTable1.getTableHeader().setForeground(Common.color_tableHeaderFont);
						jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						jTable1.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_EDIT"))
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
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										search();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Search"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_ADD"));
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										addRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_EDIT"));
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										editRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Edit"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
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
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("MATERIAL");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("BATCH_NUMBER");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("STATUS");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch_Status"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("EXPIRY_DATE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
									sortByMenu.add(newItemMenuItem);
								}
							}

							{
								final JMenu4j filterByMenu = new JMenu4j();
								filterByMenu.setText(lang.get("lbl_Filter_By"));
								popupMenu.add(filterByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch_Status"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									filterByMenu.addSeparator();
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
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
					jButtonSearch.setBounds(293, 10, 126, 32);
					jButtonSearch.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							search();

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(java.awt.event.KeyEvent.VK_E);
					jButtonEdit.setBounds(293, 68, 126, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_EDIT"));
					jButtonEdit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							editRecord();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
					jButtonHelp.setBounds(293, 126, 126, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setBounds(293, 155, 126, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JDBQuery.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setBounds(0, 11, 91, 21);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(99, 11, 141, 21);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Material_Batch"));
					jLabel3.setBounds(0, 44, 91, 21);
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jDesktopPane1.add(jTextFieldBatch);
					jTextFieldBatch.setBounds(99, 44, 141, 21);
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Sort_By"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(0, 178, 91, 21);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[] { "MATERIAL", "BATCH_NUMBER", "STATUS", "EXPIRY_DATE" });
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					jComboBoxSortBy.setBounds(99, 176, 141, 23);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Material_Batch_Status"));
					jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5.setBounds(0, 79, 91, 21);
				}
				{
					ComboBoxModel<String> jComboBoxStatusModel = new DefaultComboBoxModel<String>(Common.batchStatusIncBlank);
					jComboBoxStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxStatus);
					jComboBoxStatus.setModel(jComboBoxStatusModel);
					jComboBoxStatus.setBounds(99, 77, 141, 23);
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(246, 178, 21, 21);
					jToggleButtonSequence.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}

				{
					jButtonLookupMaterial = new JButton4j();
					jButtonLookupMaterial.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.materials())
							{
								jTextFieldMaterial.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					jButtonLookupMaterial.setBounds(246, 11, 21, 21);
					jDesktopPane1.add(jButtonLookupMaterial);
				}

				{
					jButtonLookupBatch = new JButton4j();
					jButtonLookupBatch.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JLaunchLookup.dlgCriteriaDefault = jTextFieldMaterial.getText();
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.materialBatches())
							{
								jTextFieldBatch.setText(JLaunchLookup.dlgResult);
							}

						}
					});
					jButtonLookupBatch.setBounds(246, 44, 21, 21);
					jDesktopPane1.add(jButtonLookupBatch);
				}

				{
					expiryFrom.setBounds(120, 110, 125, 25);
					expiryFrom.setEnabled(false);
					jDesktopPane1.add(expiryFrom);
				}

				{
					expiryTo.setBounds(120, 143, 125, 25);
					expiryTo.setEnabled(false);
					jDesktopPane1.add(expiryTo);
				}

				{
					jLabel5_1 = new JLabel4j_std();
					jLabel5_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5_1.setText(lang.get("lbl_Material_Batch_Expiry_From"));
					jLabel5_1.setBounds(0, 110, 91, 21);
					jDesktopPane1.add(jLabel5_1);
				}

				{
					jLabel5_2 = new JLabel4j_std();
					jLabel5_2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5_2.setText(lang.get("lbl_Material_Batch_Expiry_To"));
					jLabel5_2.setBounds(0, 147, 91, 21);
					jDesktopPane1.add(jLabel5_2);
				}

				{
					jCheckBoxFrom = new JCheckBox4j();
					jCheckBoxFrom.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (jCheckBoxFrom.isSelected())
							{
								expiryFrom.setEnabled(true);
								calendarButtonexpiryFrom.setEnabled(true);
							}
							else
							{
								expiryFrom.setEnabled(false);
								calendarButtonexpiryFrom.setEnabled(false);
							}
						}
					});
					jCheckBoxFrom.setBackground(new Color(255, 255, 255));
					jCheckBoxFrom.setBounds(99, 110, 21, 21);
					jDesktopPane1.add(jCheckBoxFrom);
				}

				{
					jCheckBoxTo = new JCheckBox4j();
					jCheckBoxTo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (jCheckBoxTo.isSelected())
							{
								expiryTo.setEnabled(true);
								calendarButtonexpiryTo.setEnabled(true);
							}
							else
							{
								expiryTo.setEnabled(false);
								calendarButtonexpiryTo.setEnabled(false);
							}
						}
					});
					jCheckBoxTo.setBackground(new Color(255, 255, 255));
					jCheckBoxTo.setBounds(99, 143, 21, 21);
					jDesktopPane1.add(jCheckBoxTo);
				}

				{
					jButtonAdd = new JButton4j(Common.icon_add);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_ADD"));
					jButtonAdd.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							addRecord();
						}
					});
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(293, 39, 126, 32);
					jDesktopPane1.add(jButtonAdd);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 559, 425, 21);
					jDesktopPane1.add(jStatusText);
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS);
					jButtonExcel.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							excel();
						}
					});

					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(293, 97, 126, 32);
					jDesktopPane1.add(jButtonExcel);
				}
				
				{
					calendarButtonexpiryFrom = new JCalendarButton(expiryFrom);
					calendarButtonexpiryFrom.setEnabled(false);
					calendarButtonexpiryFrom.setBounds(248, 110, 21, 21);
					jDesktopPane1.add(calendarButtonexpiryFrom);
				}
				{
					calendarButtonexpiryTo = new JCalendarButton(expiryTo);
					calendarButtonexpiryTo.setEnabled(false);
					calendarButtonexpiryTo.setBounds(248, 145, 21, 21);
					jDesktopPane1.add(calendarButtonexpiryTo);
				}
			}
		}
		catch (Exception e)
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
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
