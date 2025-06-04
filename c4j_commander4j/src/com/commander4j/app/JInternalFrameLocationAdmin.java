package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameLocationAdmin.java
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

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBQuery2;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JToggleButton4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBLocationTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameMaterialLocationAdmin class allows the user to view/edit
 * the table APP_LOCATION table. This table is designed to permit complex
 * location identification methods to be addressed using a simple user friendly
 * short code. The Location ID is used predominantly with the APP_PALLET,
 * APP_PALLET_HISTORY and APP_DESPATCH tables. When messaging via the XML
 * interfaces any of the additional fields present in the APP_LOCATION table can
 * be included to satisfy the requirements of external ERP systems.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameLocationAdmin.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameLocationProperties
 *      JInternalFrameMaterialLocationProperties
 * @see com.commander4j.db.JDBLocation JDBLocation
 */
public class JInternalFrameLocationAdmin extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JLabel4j_std jStatusText;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldPlant;
	private JLabel4j_std jLabel2;
	private JLabel4j_std jLabel4;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonAdd;
	private JLabel4j_std jLabel8;
	private JToggleButton4j jToggleButtonSequence;
	private JButton4j jButtonSearch;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JTextField4j jTextFieldStorageLocation;
	private JTextField4j jTextFieldStorageType;
	private JLabel4j_std jLabel9;
	private JTextField4j jTextFieldStorageSection;
	private JLabel4j_std jLabel7;
	private JTextField4j jTextFieldGLN;
	private JLabel4j_std jLabel6;
	private JLabel4j_std jLabel5;
	private JTextField4j jTextFieldLocationID;
	private JTextField4j jTextFieldStorageBin;
	private JTextField4j jTextFieldWarehouse;
	private JTable4j jTable1;
	private JScrollPane jScrollPane1;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private String llocation;
	String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private static boolean dlg_sort_descending = false;
	private JDBLanguage lang;
	private PreparedStatement listStatement;
	private JCheckBox4j chkboxLocationEnabled;
	private JCheckBox4j jCheckBoxLimit = new JCheckBox4j();
	private JSpinner4j jSpinnerLimit = new JSpinner4j();

	private void copyToClipboard(String fieldname)
	{
		StringSelection stringSelection = new StringSelection("");
		
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals("Location ID") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals("Plant") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 2).toString());
			}

			if (fieldname.equals("Warehouse") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 3).toString());
			}

			if (fieldname.equals("GLN") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 4).toString());
			}

			if (fieldname.equals("Storage Location") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 6).toString());
			}

			if (fieldname.equals("Storage Type") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 7).toString());
			}
			
			if (fieldname.equals("Storage Section") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 8).toString());
			}
			
			if (fieldname.equals("Storage Bin") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 9).toString());
			}
			
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		}
	}
	
	
	private void clearFilter() {
		jTextFieldLocationID.setText("");
		jTextFieldPlant.setText("");
		jTextFieldWarehouse.setText("");
		jTextFieldGLN.setText("");
		jTextFieldDescription.setText("");
		jTextFieldStorageLocation.setText("");
		jTextFieldStorageType.setText("");
		jTextFieldStorageSection.setText("");
		jTextFieldStorageBin.setText("");
		search();
	}

	private void filterBy(String fieldname) {
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			if (fieldname.equals("LOCATION_ID") == true)
			{
				jTextFieldLocationID.setText(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals("PLANT") == true)
			{
				jTextFieldPlant.setText(jTable1.getValueAt(row, 1).toString());
			}

			if (fieldname.equals("WAREHOUSE") == true)
			{
				jTextFieldWarehouse.setText(jTable1.getValueAt(row, 2).toString());
			}

			if (fieldname.equals("GLN") == true)
			{
				jTextFieldGLN.setText(jTable1.getValueAt(row, 3).toString());
			}

			if (fieldname.equals("DESCRIPTION") == true)
			{
				jTextFieldDescription.setText(jTable1.getValueAt(row, 4).toString());
			}

			if (fieldname.equals("STORAGE_LOCATON") == true)
			{
				jTextFieldStorageLocation.setText(jTable1.getValueAt(row, 5).toString());
			}

			if (fieldname.equals("STORAGE_TYPE") == true)
			{
				jTextFieldStorageType.setText(jTable1.getValueAt(row, 6).toString());
			}

			if (fieldname.equals("STORAGE_SECTION") == true)
			{
				jTextFieldStorageSection.setText(jTable1.getValueAt(row, 7).toString());
			}

			if (fieldname.equals("STORAGE_BIN") == true)
			{
				jTextFieldStorageBin.setText(jTable1.getValueAt(row, 8).toString());
			}

			search();

		}
	}

	private void sortBy(String fieldname) {
		jComboBoxSortBy.setSelectedItem(fieldname);
		search();
	}

	private void print() {

		PreparedStatement temp = buildSQLr();
		JLaunchReport.runReport("RPT_LOCATIONS", null, "", temp, "");
	}

	private void search() {
		buildSQL();
		populateList();
	}

	private void addRecord() {
		JDBLocation l = new JDBLocation(Common.selectedHostID, Common.sessionID);
		llocation = (String) JOptionPane.showInputDialog(Common.mainForm, "Enter new location", null, JOptionPane.QUESTION_MESSAGE,Common.icon_confirm_16x16, null, null);
		if (llocation != null)
		{
			if (llocation.equals("") == false)
			{
				llocation = llocation.toUpperCase();
				l.setLocationID(llocation);
				if (l.isValidLocation() == false)
				{
					JLaunchMenu.runForm("FRM_ADMIN_LOCATION_EDIT", llocation);
				}
				else
				{
					JOptionPane.showMessageDialog(Common.mainForm, "Location [" + llocation + "] already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm_16x16);
				}
				buildSQL();
				populateList();
			}
		}

	}

	private void populateList() {
		JDBLocation location = new JDBLocation(Common.selectedHostID, Common.sessionID);
		JDBLocationTableModel locationtable = new JDBLocationTableModel(location.getLocationDataResultSet(listStatement));
		TableRowSorter<JDBLocationTableModel> sorter = new TableRowSorter<JDBLocationTableModel>(locationtable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(locationtable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumn col = jTable1.getColumnModel().getColumn(0);
		col.setPreferredWidth(100);
		col = jTable1.getColumnModel().getColumn(1);
		col.setPreferredWidth(50);
		col = jTable1.getColumnModel().getColumn(2);
		col.setPreferredWidth(80);
		col = jTable1.getColumnModel().getColumn(3);
		col.setPreferredWidth(90);
		col = jTable1.getColumnModel().getColumn(4);
		col.setPreferredWidth(120);
		col = jTable1.getColumnModel().getColumn(5);
		col.setPreferredWidth(150);
		col = jTable1.getColumnModel().getColumn(6);
		col.setPreferredWidth(100);
		col = jTable1.getColumnModel().getColumn(7);
		col.setPreferredWidth(100);
		col = jTable1.getColumnModel().getColumn(8);
		col.setPreferredWidth(100);
		col = jTable1.getColumnModel().getColumn(9);
		col.setPreferredWidth(100);
		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), locationtable.getRowCount());

	}

	private void deleteRecord() {
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			llocation = jTable1.getValueAt(row, 0).toString();
			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Location_Delete")+" " + llocation , lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (n == 0)
			{
				JDBLocation l = new JDBLocation(Common.selectedHostID, Common.sessionID);
				l.setLocationID(llocation);
				boolean result = l.delete();
				if (result == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, l.getErrorMessage(), "Delete error (" + llocation + ")", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					buildSQL();
					populateList();
				}
			}
		}
	}

	private void editRecord() {
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			llocation = jTable1.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_ADMIN_LOCATION_EDIT", llocation);
		}
	}

	private void setSequence(boolean descending) {
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

	private PreparedStatement buildSQLr() {
				
		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID,Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("{schema}APP_LOCATION");
		q2.applyWhere("location_id=", jTextFieldLocationID.getText());;
		q2.applyWhere("plant=", jTextFieldPlant.getText());
		q2.applyWhere("warehouse=", jTextFieldWarehouse.getText());
		if (jTextFieldDescription.getText().equals("") == false)
		{
			q2.applyWhere("upper(description) LIKE ", "%" + jTextFieldDescription.getText().toUpperCase() + "%");
		}
		q2.applyWhere("storage_location=", jTextFieldStorageLocation.getText());
		q2.applyWhere("storage_type=", jTextFieldStorageType.getText());
		q2.applyWhere("storage_section=", jTextFieldStorageSection.getText());
		q2.applyWhere("storage_bin=", jTextFieldStorageBin.getText());
		q2.applyWhere("gln=", jTextFieldGLN.getText());
		
		if (chkboxLocationEnabled.isSelected())
		{
			q2.applyWhere("enabled=", 'Y');
		}
		else
		{
			q2.applyWhere("enabled=", 'N');
		}
		
		q2.applySort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());

		q2.applyRestriction(jCheckBoxLimit.isSelected(), jSpinnerLimit.getValue());
		
		q2.applySQL();
		
		result = q2.getPreparedStatement();
		
		return result;
	}	
	
	private void buildSQL() {
		
		JDBQuery2.closeStatement(listStatement);
		
		listStatement = buildSQLr();
	}

	public JInternalFrameLocationAdmin()
	{
		super();

		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();
		
		
		////////
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID,Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("{schema}APP_LOCATION WHERE 1=2");
		q2.applyRestriction(false, 0);
		q2.applySort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		q2.applySQL();
		listStatement = q2.getPreparedStatement();
		////////
		
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_LOCATIONS"));
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(770, 478));
			this.setBounds(0, 0, 1010, 561);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Location Administration");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Storage_Plant"));
					jLabel1.setBounds(246, 11, 109, 22);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					JButton4j btnExcel = new JButton4j(Common.icon_XLS_16x16);
					btnExcel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							export();
						}
					});
					btnExcel.setText(lang.get("btn_Excel"));
					btnExcel.setMnemonic(lang.getMnemonicChar());
					btnExcel.setBounds(661, 110, 109, 32);
					jDesktopPane1.add(btnExcel);
				}
				{
					jTextFieldPlant = new JTextField4j(JDBLocation.field_plant);
					jDesktopPane1.add(jTextFieldPlant);
					jTextFieldPlant.setBounds(365, 11, 80, 22);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Description"));
					jLabel3.setBounds(12, 77, 91, 22);
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBLocation.field_description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setBounds(112, 77, 335, 22);
				}
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 150, 1010, 355);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][] { { "One", "Two" }, { "Three", "Four" } }, new String[] { "Column 1", "Column 2" });
						jTable1 = new JTable4j();
						jScrollPane1.setViewportView(jTable1);

						jTable1.setToolTipText(lang.get("lbl_Table_Hint"));
						jTable1.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_LOCATION_EDIT"))
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
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										search();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Search"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_LOCATION_ADD"));
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										addRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_LOCATION_EDIT"));
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										editRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Edit"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_LOCATION_DELETE"));
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										deleteRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Delete"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_print_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										print();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Print"));
								popupMenu.add(newItemMenuItem);
							}
							
							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_history_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										palletHistory();
									}
								});
								newItemMenuItem.setText(lang.get("mod_FRM_ADMIN_PALLET_HISTORY"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_HISTORY"));
								popupMenu.add(newItemMenuItem);
							}
							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_pallet_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										palletAdmin();
									}
								});
								newItemMenuItem.setText(lang.get("mod_FRM_ADMIN_PALLETS"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLETS"));
								popupMenu.add(newItemMenuItem);
							}
							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_process_order_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										orderAdmin();
									}
								});
								newItemMenuItem.setText(lang.get("mod_FRM_ADMIN_PROCESS_ORDER"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PROCESS_ORDER"));
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
											sortBy("LOCATION_ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Location"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("PLANT");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Plant"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("WAREHOUSE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Warehouse"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("DESCRIPTION");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Description"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("GLN");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_GLN"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("STORAGE_LOCATION");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Location"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("STORAGE_TYPE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Type"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("STORAGE_SECTION");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Section"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											sortBy("STORAGE_BIN");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Bin"));
									sortByMenu.add(newItemMenuItem);
								}
							}

							{
								final JMenu4j filterByMenu = new JMenu4j();
								filterByMenu.setText("Filter by");
								popupMenu.add(filterByMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("LOCATION_ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Location"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("PLANT");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Plant"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("WAREHOUSE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Warehouse"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("GLN");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_GLN"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("DESCRIPTION");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Description"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("STORAGE_LOCATION");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Location"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("STORAGE_TYPE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Type"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("STORAGE_SECTION");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Section"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener() {
										public void actionPerformed(final ActionEvent e) {
											filterBy("STORAGE_BIN");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Bin"));
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
							
							{
								final JMenu4j clipboardMenu = new JMenu4j();
								clipboardMenu.setText(lang.get("lbl_Clipboard_Copy"));
								popupMenu.add(clipboardMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Location ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Location"));
									clipboardMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Plant");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Plant"));
									clipboardMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Warehouse");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Warehouse"));
									clipboardMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("GLN");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_GLN"));
									clipboardMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Storage Location");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Location"));
									clipboardMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Storage Type");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Type"));
									clipboardMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Storage Section");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Section"));
									clipboardMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Storage Bin");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Storage_Bin"));
									clipboardMenu.add(newItemMenuItem);
								}
								
							}
						}
					}
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Storage_Warehouse"));
					jLabel2.setBounds(450, 11, 103, 22);
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldWarehouse = new JTextField4j(JDBLocation.field_warehouse);
					jDesktopPane1.add(jTextFieldWarehouse);
					jTextFieldWarehouse.setBounds(560, 11, 80, 22);
				}
				{
					jLabel4 = new JLabel4j_std();
					jDesktopPane1.add(jLabel4);
					jLabel4.setText(lang.get("lbl_Storage_Bin"));
					jLabel4.setBounds(650, 44, 113, 22);
					jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldStorageBin = new JTextField4j(JDBLocation.field_storage_bin);
					jDesktopPane1.add(jTextFieldStorageBin);
					jTextFieldStorageBin.setBounds(775, 44, 80, 22);
				}
				{
					jTextFieldLocationID = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(jTextFieldLocationID);
					jTextFieldLocationID.setBounds(112, 11, 126, 22);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Storage_Location"));
					jLabel5.setBounds(12, 11, 93, 22);
					jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel6 = new JLabel4j_std();
					jDesktopPane1.add(jLabel6);
					jLabel6.setText(lang.get("lbl_Storage_GLN"));
					jLabel6.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel6.setBounds(650, 11, 114, 22);
				}
				{
					jTextFieldGLN = new JTextField4j(JDBMaterialUom.field_ean);
					jDesktopPane1.add(jTextFieldGLN);
					jTextFieldGLN.setBounds(775, 11, 126, 22);
				}
				{
					jLabel7 = new JLabel4j_std();
					jDesktopPane1.add(jLabel7);
					jLabel7.setText(lang.get("lbl_Storage_Section"));
					jLabel7.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel7.setBounds(450, 44, 103, 22);
				}
				{
					jTextFieldStorageSection = new JTextField4j(JDBLocation.field_storage_section);
					jDesktopPane1.add(jTextFieldStorageSection);
					jTextFieldStorageSection.setBounds(560, 44, 80, 22);
				}
				{
					jLabel8 = new JLabel4j_std();
					jDesktopPane1.add(jLabel8);
					jLabel8.setText(lang.get("lbl_Storage_Location"));
					jLabel8.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel8.setBounds(0, 44, 105, 22);
				}
				{
					jTextFieldStorageLocation = new JTextField4j(JDBLocation.field_storage_location);
					jDesktopPane1.add(jTextFieldStorageLocation);
					jTextFieldStorageLocation.setBounds(112, 44, 126, 22);
				}
				{
					jLabel9 = new JLabel4j_std();
					jDesktopPane1.add(jLabel9);
					jLabel9.setText(lang.get("lbl_Storage_Type"));
					jLabel9.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel9.setBounds(246, 44, 109, 22);
				}
				{
					jTextFieldStorageType = new JTextField4j(JDBLocation.field_storage_type);
					jDesktopPane1.add(jTextFieldStorageType);
					jTextFieldStorageType.setBounds(365, 44, 80, 22);
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Sort_By"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(416, 77, 103, 22);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(Common.locationSortBy);
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					jComboBoxSortBy.setBounds(526, 77, 231, 22);
					jComboBoxSortBy.setSelectedItem("LOCATION_ID,PLANT,WAREHOUSE");
					jComboBoxSortBy.setMaximumRowCount(Common.locationSortBy.length);
					jComboBoxSortBy.setRequestFocusEnabled(false);
				}
				{
					jButtonSearch = new JButton4j(Common.icon_find_16x16);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.setMnemonic(lang.getMnemonicChar());
					jButtonSearch.setBounds(1, 110, 109, 32);
					jButtonSearch.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							search();

						}
					});
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_LOCATION_ADD"));
					jButtonAdd.setBounds(221, 110, 109, 32);
					jButtonAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							addRecord();
						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit_16x16);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_LOCATION_EDIT"));
					jButtonEdit.setBounds(331, 110, 109, 32);
					jButtonEdit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							editRecord();
						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete_16x16);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_LOCATION_DELETE"));
					jButtonDelete.setBounds(441, 110, 109, 32);
					jButtonDelete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							deleteRecord();
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(551, 110, 109, 32);
					jButtonPrint.setEnabled(true);
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							print();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(771, 110, 109, 32);
				}
				{

					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(881, 110, 109, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JDBQuery2.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					jToggleButtonSequence = new JToggleButton4j();
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(757, 77, 22, 22);
					jToggleButtonSequence.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 505, 1028, 21);
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusText);
				}

				{
					jButtonClear = new JButton4j(Common.icon_clear_16x16);
					jButtonClear.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							clearFilter();
						}
					});
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jButtonClear.setBounds(111, 110, 109, 32);
					jDesktopPane1.add(jButtonClear);
				}
				
				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Limit"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(785, 77, 84, 22);
				jDesktopPane1.add(label4j_std);

				jCheckBoxLimit.setSelected(true);
				jCheckBoxLimit.setBackground(Color.WHITE);
				jCheckBoxLimit.setBounds(881, 78, 21, 21);
				jDesktopPane1.add(jCheckBoxLimit);

				JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(jSpinnerLimit);
				jSpinnerLimit.setEditor(ne);
				jSpinnerLimit.setValue(1000);
				jSpinnerLimit.setBounds(910, 77, 68, 22);
				jDesktopPane1.add(jSpinnerLimit);
				
				chkboxLocationEnabled = new JCheckBox4j(lang.get("lbl_Enabled"));
				chkboxLocationEnabled.setSelected(true);
				chkboxLocationEnabled.setBounds(881, 44, 109, 22);
				jDesktopPane1.add(chkboxLocationEnabled);
				
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						jTextFieldLocationID.requestFocus();
						jTextFieldLocationID.setCaretPosition(jTextFieldLocationID.getText().length());

					}
				});
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void palletHistory()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			JLaunchMenu.runForm("FRM_ADMIN_PALLET_HISTORY","LOCATION", jTable1.getValueAt(row, 0).toString());
		}
	}
	
	private void palletAdmin()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			JLaunchMenu.runForm("FRM_ADMIN_PALLETS","LOCATION", jTable1.getValueAt(row, 0).toString());
		}
	}	

	private void orderAdmin()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			JLaunchMenu.runForm("FRM_ADMIN_PROCESS_ORDER","LOCATION", jTable1.getValueAt(row, 0).toString());
		}
	}	
	
	private void export() {
		JDBLocation location = new JDBLocation(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		PreparedStatement temp = buildSQLr();
		export.saveAs("locations.xls", location.getLocationDataResultSet(temp), Common.mainForm);
		populateList();
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
