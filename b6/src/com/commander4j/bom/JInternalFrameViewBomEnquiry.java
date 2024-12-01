package com.commander4j.bom;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMaterialAdmin.java
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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameViewBomEnquiry extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;

	private JButton4j jButtonClear;
	private JButton4j jButtonExcel;
	private JButton4j jButtonPrint;
	private JButton4j jButtonHelp;
	private JButton4j jButtonClose;
	private JButton4j jButtonSearch;

	private JLabel4j_std jStatusText;
	private JLabel4j_std jLabel_UOM;
	private JLabel4j_std jLabel_BOM_ID;
	private JLabel4j_std jLabel_BOM_Version;
	private JLabel4j_std jLabel10;
	private JLabel4j_std jLabel_Description;
	private JLabel4j_std jLabel1;
	private JLabel4j_std jLabel_Type;
	private JLabel4j_std jLabel_Location;
	private JLabel4j_std jLabel_Stage;

	private JTextField4j jTextFieldDescription;
	private JTextField4j jTextFieldBomID;
	private JTextField4j jTextFieldBomVersion;
	private JTextField4j jTextFieldMaterial;

	private JToggleButton jToggleButtonSequence;

	private JComboBox4j<String> jComboBoxSortBy;
	private JComboBox4j<String> jComboBoxUOM;
	private JComboBox4j<String> jComboBoxType;
	private JComboBox4j<String> jComboBoxLocation;
	private JComboBox4j<String> jComboBoxStage;

	private JTable4j jTable1;

	private JScrollPane jScrollPane1;

	private JDBBomList dbList = new JDBBomList(Common.selectedHostID, Common.sessionID);

	private Vector<String> uomList = new Vector<String>();
	private Vector<String> typeList = new Vector<String>();
	private Vector<String> locationList = new Vector<String>();
	private Vector<String> stageList = new Vector<String>();

	private boolean dlg_sort_descending = false;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;

	private PreparedStatement listStatement;

	private JCheckBox4j jCheckBoxLimit = new JCheckBox4j();
	private JSpinner jSpinnerLimit = new JSpinner();

	private void copyToClipboard(String fieldname)
	{
		StringSelection stringSelection = new StringSelection("");

		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals(lang.get("lbl_Material")) == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, JDBViewBomTableModel.bom_material_col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Location_ID")) == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, JDBViewBomTableModel.bom_location_col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Description")) == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, JDBViewBomTableModel.bom_description_col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Process_Order_Recipe")) == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, JDBViewBomTableModel.bom_id_col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Pallet_Quantity")) == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, JDBViewBomTableModel.bom_quantity_col).toString());
			}

			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		}
	}

	private void clearFilter()
	{

		jTextFieldMaterial.setText("");

		jTextFieldDescription.setText("");

		jComboBoxUOM.getModel().setSelectedItem("");

		jComboBoxType.getModel().setSelectedItem("");

		jComboBoxLocation.getModel().setSelectedItem("");
		
		jComboBoxStage.getModel().setSelectedItem("");

		buildSQL();
		populateList();

	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals(lang.get("lbl_Process_Order_Recipe")) == true)
			{
				jTextFieldBomID.setText(jTable1.getValueAt(row, JDBViewBomTableModel.bom_id_col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Version")) == true)
			{
				jTextFieldBomVersion.setText(jTable1.getValueAt(row, JDBViewBomTableModel.bom_version_col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Material")) == true)
			{
				jTextFieldMaterial.setText(jTable1.getValueAt(row, JDBViewBomTableModel.bom_material_col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Description")) == true)
			{
				jTextFieldDescription.setText(jTable1.getValueAt(row, JDBViewBomTableModel.bom_description_col).toString());
			}

			if (fieldname.equals(lang.get("lbl_Material_UOM")) == true)
			{
				jComboBoxUOM.setSelectedItem(jTable1.getValueAt(row, JDBViewBomTableModel.bom_uom_col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Material_Type")) == true)
			{
				jComboBoxType.setSelectedItem(jTable1.getValueAt(row, JDBViewBomTableModel.bom_material_type).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Location_ID")) == true)
			{
				jComboBoxLocation.setSelectedItem(jTable1.getValueAt(row, JDBViewBomTableModel.bom_location_col).toString());
			}
			
			if (fieldname.equals(lang.get("lbl_Stage")) == true)
			{
				jComboBoxStage.setSelectedItem(jTable1.getValueAt(row, JDBViewBomTableModel.bom_stage_col).toString());
			}


			buildSQL();
			populateList();
		}
	}

	private void sortBy(String fieldname)
	{
		jComboBoxSortBy.setSelectedItem(fieldname);
		search();
	}

	private void export()
	{

	}

	private void print()
	{
		PreparedStatement temp = buildSQLr();
		JLaunchReport.runReport("RPT_VIEW_BOM", null, "", temp, "");
	}

	private void search()
	{
		buildSQL();
		populateList();
	}

	private void populateList()
	{
		JDBViewBomRecord viewBom = new JDBViewBomRecord(Common.selectedHostID, Common.sessionID);

		JDBViewBomTableModel viewBomTable = new JDBViewBomTableModel(viewBom.getViewBomResultSet(listStatement));

		TableRowSorter<JDBViewBomTableModel> sorter = new TableRowSorter<JDBViewBomTableModel>(viewBomTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(viewBomTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable1.getTableHeader().setPreferredSize(new Dimension(jScrollPane1.getWidth(), 35));

		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_id_col).setPreferredWidth(110);
		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_version_col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_input_output_col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_sequence_col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_material_col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_material_type).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_quantity_col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_location_col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_stage_col).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_description_col).setPreferredWidth(250);
		jTable1.getColumnModel().getColumn(JDBViewBomTableModel.bom_uom_col).setPreferredWidth(40);
		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), viewBomTable.getRowCount());
	}

	private PreparedStatement buildSQLr()
	{

		PreparedStatement result;
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);

		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}VIEW_BOM"));

		query.addParamtoSQL("bom_id=", jTextFieldBomID.getText());

		query.addParamtoSQL("bom_version=", jTextFieldBomVersion.getText());

		query.addParamtoSQL("material=", jTextFieldMaterial.getText());

		query.addParamtoSQL("location_id=", (String) jComboBoxLocation.getSelectedItem());

		query.addParamtoSQL("uom=", ((String) jComboBoxUOM.getSelectedItem()));

		query.addParamtoSQL("type=", ((String) jComboBoxType.getSelectedItem()));
		
		query.addParamtoSQL("stage=", ((String) jComboBoxStage.getSelectedItem()));

		if (jTextFieldDescription.getText().equals("") == false)
		{
			query.addParamtoSQL("upper(description) LIKE ", "%" + jTextFieldDescription.getText().toUpperCase() + "%");
		}

		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());

		query.applyRestriction(jCheckBoxLimit.isSelected(), Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), jSpinnerLimit.getValue());

		query.bindParams();

		result = query.getPreparedStatement();
		return result;

	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);

		listStatement = buildSQLr();
	}

	public JInternalFrameViewBomEnquiry()
	{
		super();
		setIconifiable(true);

		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		if (dbList.getListItems("uom").contains("") == false)
		{
			uomList.add("");
		}
		uomList.addAll(dbList.getListItems("uom"));

		if (dbList.getListItems("material_type").contains("") == false)
		{
			typeList.add("");
		}
		typeList.addAll(dbList.getListItems("material_type"));

		if (dbList.getListItems("location_id").contains("") == false)
		{
			locationList.add("");
		}
		locationList.addAll(dbList.getListItems("location_id"));
		
		if (dbList.getListItems("stage").contains("") == false)
		{
			stageList.add("");
		}
		stageList.addAll(dbList.getListItems("stage"));

		initGUI();

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}VIEW_BOM where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_BOM_ENQUIRY"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);
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

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(757, 535));
			this.setBounds(0, 0, 984, 617);
			setVisible(true);
			this.setTitle("BOM Enquiry");
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(645, 460));
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.setLayout(null);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 156, 968, 405);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][]
						{
								{ "One", "Two" },
								{ "Three", "Four" } }, new String[]
						{ "Column 1", "Column 2" });
						jTable1 = new JTable4j();
						jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);
						jTable1.setToolTipText(lang.get("lbl_Table_Hint"));

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jTable1, popupMenu);
							
							
							
							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_bom_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										int row = jTable1.getSelectedRow();
										if (row >= 0)
										{
											String id = jTable1.getValueAt(row, JDBViewBomTableModel.bom_id_col).toString();
											String ver = jTable1.getValueAt(row, JDBViewBomTableModel.bom_version_col).toString();
											JLaunchMenu.runForm("FRM_BOM_ADMIN", id,ver);
										}
									}
								});
								newItemMenuItem.setText(lang.get("mod_MENU_BOM"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_ADMIN"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_search_16x16);
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
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										export();
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
											sortBy("MATERIAL");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("TYPE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Type"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("DESCRIPTION");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Description"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("UOM");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_UOM"));
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
									newItemMenuItem.setText(lang.get("lbl_Process_Order_Recipe"));
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
									newItemMenuItem.setText(lang.get("lbl_Version"));
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
									newItemMenuItem.setText(lang.get("lbl_Material"));
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
									newItemMenuItem.setText(lang.get("lbl_Description"));
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
									newItemMenuItem.setText(lang.get("lbl_Material_Type"));
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
									newItemMenuItem.setText(lang.get("lbl_Material_UOM"));
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
								final JMenu4j clipboardMenu = new JMenu4j();
								clipboardMenu.setText(lang.get("lbl_Clipboard_Copy"));
								popupMenu.add(clipboardMenu);

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard(lang.get("lbl_Material"));
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material"));
									clipboardMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard(lang.get("lbl_Location_ID"));
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Location_ID"));
									clipboardMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard(lang.get("lbl_Description"));
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Description"));
									clipboardMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard(lang.get("lbl_Process_Order_Recipe"));
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Process_Order_Recipe"));
									clipboardMenu.add(newItemMenuItem);
								}
								
								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard(lang.get("lbl_Pallet_Quantity"));
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_Quantity"));
									clipboardMenu.add(newItemMenuItem);
								}

							}
						}
					}
				}
				{
					jButtonSearch = new JButton4j(Common.icon_search_16x16);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.setBounds(162, 112, 109, 32);
					jButtonSearch.setMnemonic(lang.getMnemonicChar());
					jButtonSearch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							search();

						}
					});
				}
				{
					jTextFieldBomID = new JTextField4j();
					jDesktopPane1.add(jTextFieldBomID);
					jTextFieldBomID.setBounds(114, 11, 99, 22);
				}
				{
					jTextFieldBomVersion = new JTextField4j();
					jDesktopPane1.add(jTextFieldBomVersion);
					jTextFieldBomVersion.setBounds(311, 11, 68, 22);
				}
				{
					jTextFieldMaterial = new JTextField4j();
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(475, 11, 125, 22);
				}

				{
					jTextFieldDescription = new JTextField4j();
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setBounds(704, 11, 258, 22);
				}
				{
					jLabel1 = new JLabel4j_std();
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setBounds(379, 12, 84, 21);
				}
				{
					jLabel_Description = new JLabel4j_std();
					jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel_Description);
					jLabel_Description.setText(lang.get("lbl_Description"));
					jLabel_Description.setBounds(609, 12, 84, 21);
				}
				{
					jLabel_BOM_ID = new JLabel4j_std();
					jLabel_BOM_ID.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel_BOM_ID);
					jLabel_BOM_ID.setText(lang.get("lbl_Process_Order_Recipe"));
					jLabel_BOM_ID.setBounds(0, 12, 105, 21);
				}
				{
					jLabel_BOM_Version = new JLabel4j_std();
					jLabel_BOM_Version.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel_BOM_Version);
					jLabel_BOM_Version.setText(lang.get("lbl_Version"));
					jLabel_BOM_Version.setBounds(199, 12, 105, 21);
				}
				{
					ComboBoxModel<String> jComboBox2Model = new DefaultComboBoxModel<String>(uomList);
					jComboBoxUOM = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxUOM);
					jComboBoxUOM.setModel(jComboBox2Model);
					jComboBoxUOM.setBounds(762, 45, 109, 21);
					jComboBoxUOM.setMaximumRowCount(12);
				}

				{
					ComboBoxModel<String> jComboBox2Model = new DefaultComboBoxModel<String>(typeList);
					jComboBoxType = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxType);
					jComboBoxType.setModel(jComboBox2Model);
					jComboBoxType.setBounds(578, 45, 84, 21);
					jComboBoxType.setMaximumRowCount(12);
				}

				{
					ComboBoxModel<String> jComboBox2Model = new DefaultComboBoxModel<String>(locationList);
					jComboBoxLocation = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxLocation);
					jComboBoxLocation.setModel(jComboBox2Model);
					jComboBoxLocation.setBounds(344, 45, 94, 21);
					jComboBoxLocation.setMaximumRowCount(12);
				}
				
				{
					ComboBoxModel<String> jComboBox2Model = new DefaultComboBoxModel<String>(stageList);
					jComboBoxStage = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxStage);
					jComboBoxStage.setModel(jComboBox2Model);
					jComboBoxStage.setBounds(114, 45, 94, 21);
					jComboBoxStage.setMaximumRowCount(12);
				}

				{
					jLabel_UOM = new JLabel4j_std();
					jLabel_UOM.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel_UOM);
					jLabel_UOM.setText(lang.get("lbl_Material_UOM"));
					jLabel_UOM.setBounds(649, 45, 105, 21);
				}

				{
					jLabel_Type = new JLabel4j_std();
					jLabel_Type.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel_Type);
					jLabel_Type.setText(lang.get("lbl_Material_Type"));
					jLabel_Type.setBounds(455, 45, 105, 21);
				}

				{
					jLabel_Location = new JLabel4j_std();
					jLabel_Location.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel_Location);
					jLabel_Location.setText(lang.get("lbl_Location_ID"));
					jLabel_Location.setBounds(230, 45, 105, 21);
				}
				{
					jLabel_Stage = new JLabel4j_std();
					jLabel_Stage.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel_Stage);
					jLabel_Stage.setText(lang.get("lbl_Stage_Phase"));
					jLabel_Stage.setBounds(0, 45, 105, 21);
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(596, 112, 109, 32);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}

				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(704, 112, 109, 32);
					jButtonClose.setMnemonic(lang.getMnemonicChar());
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
					jButtonPrint = new JButton4j(Common.icon_report_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setBounds(379, 112, 109, 32);
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							print();
						}
					});
				}

				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Sort_By"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(78, 78, 146, 21);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(Common.viewBOMSortBy);
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					jComboBoxSortBy.setBounds(230, 78, 350, 21);
					jComboBoxSortBy.setMaximumRowCount(Common.materialSortBy.length);
				}

				{
					jToggleButtonSequence = new JToggleButton();
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(581, 78, 21, 21);
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
					jStatusText.setBounds(0, 563, 970, 20);
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusText);
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							export();
						}
					});

					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(488, 112, 109, 32);
					jDesktopPane1.add(jButtonExcel);
				}

				{
					jButtonClear = new JButton4j(Common.icon_clear_16x16);
					jButtonClear.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							clearFilter();
						}
					});
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jButtonClear.setMnemonic(lang.getMnemonicChar());
					jButtonClear.setBounds(270, 112, 109, 32);
					jDesktopPane1.add(jButtonClear);
				}

				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Limit"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(574, 78, 84, 21);
				jDesktopPane1.add(label4j_std);

				jCheckBoxLimit.setSelected(true);
				jCheckBoxLimit.setBackground(Color.WHITE);
				jCheckBoxLimit.setBounds(662, 78, 21, 21);
				jDesktopPane1.add(jCheckBoxLimit);

				JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
				ne.getTextField().setFont(Common.font_std);
				jSpinnerLimit.setEditor(ne);
				jSpinnerLimit.setValue(1000);
				jSpinnerLimit.setBounds(691, 78, 68, 21);
				jDesktopPane1.add(jSpinnerLimit);

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
