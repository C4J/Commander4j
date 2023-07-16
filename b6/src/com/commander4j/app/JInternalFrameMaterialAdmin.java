package com.commander4j.app;

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
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBEquipmentType;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialType;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBUom;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBMaterialTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import com.commander4j.gui.JCheckBox4j;
import javax.swing.JSpinner;

/**
 * The JInternalFrameMaterialAdmin is the starting point maintaining the
 * APP_MATERIAL table. There are a several other tables which also contain
 * material related data and are linked to the APP_MATERIAL table via the
 * "MATERIAL" key field. The associated tables can also be maintained by using
 * the menu options on this screen.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMaterialAdmin.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameMaterialProperties
 *      JInternalFrameMaterialProperties
 * @see com.commander4j.app.JInternalFrameMaterialUomAdmin
 *      JInternalFrameMaterialUomAdmin
 * @see com.commander4j.app.JInternalFrameMaterialBatchAdmin
 *      JInternalFrameMaterialBatchAdmin
 * @see com.commander4j.app.JInternalFrameMaterialCustomerDataAdmin
 *      JInternalFrameMaterialCustomerDataAdmin
 * @see com.commander4j.app.JInternalFrameMaterialTypeAdmin
 *      JInternalFrameMaterialTypeAdmin
 * @see com.commander4j.db.JDBMaterial JDBMaterial
 * @see com.commander4j.db.JDBMaterialUom JDBMaterialUom
 * @see com.commander4j.db.JDBMaterialLocation JDBMaterialLocation
 * @see com.commander4j.db.JDBMaterialType JDBMaterialType
 * @see com.commander4j.db.JDBMaterialCustomerData JDBMaterialCustomerData
 * @see com.commander4j.db.JDBMaterialBatch JDBMaterialBatch
 */
public class JInternalFrameMaterialAdmin extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JButton4j jButtonExcel;
	private JLabel4j_std jStatusText;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonSearch;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel2;
	private JLabel4j_std jLabel4;
	private JButton4j jButtonClose;
	private JLabel4j_std jLabel8;
	private JToggleButton jToggleButtonSequence;
	private JComboBox4j<String> jComboBoxDefaultBatchStatus;
	private JLabel4j_std jLabel12;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JLabel4j_std jLabel9;
	private JComboBox4j<JShelfLifeRoundingRule> jComboBoxRoundingRule;
	private JButton4j jButtonPrint;
	private JComboBox4j<JShelfLifeUom> jComboBoxShelfLifeUOM;
	private JTextField4j jTextFieldShelfLife;
	private JLabel4j_std jLabel7;
	private JButton4j jButtonHelp;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JButton4j jButtonAdd;
	private JComboBox4j<JDBUom> jComboBoxBaseUOM;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private JComboBox4j<JDBMaterialType> jComboBoxMaterialType;
	private JTextField4j jTextFieldMaterial;
	private JTable4j jTable1;
	private JScrollPane jScrollPane1;
	private JDBUom uom = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDBUom baseUom = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDBMaterialType materialType = new JDBMaterialType(Common.selectedHostID, Common.sessionID);
	private Vector<JDBUom> uomList = new Vector<JDBUom>();
	private Vector<JShelfLifeUom> shelfLifeUomList = new Vector<JShelfLifeUom>();
	private Vector<JShelfLifeRoundingRule> shelfLifeRule = new Vector<JShelfLifeRoundingRule>();
	private Vector<JDBMaterialType> typeList = new Vector<JDBMaterialType>();
	private JShelfLifeUom sluom = new JShelfLifeUom();
	private JShelfLifeRoundingRule slrr = new JShelfLifeRoundingRule();
	private String lmaterial;
	private boolean dlg_sort_descending = false;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private PreparedStatement listStatement;
	private JTextField4j textFieldInspectionID;
	private JCheckBox4j jCheckBoxLimit = new JCheckBox4j();
	private JSpinner jSpinnerLimit = new JSpinner();
	private JCheckBox4j checkBox4j_Enabled = new JCheckBox4j();
	private JTextField4j textFieldEquipmentType;

	private void copyToClipboard(String fieldname)
	{
		StringSelection stringSelection = new StringSelection("");
		
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals("Material") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals("Description") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 1).toString());
			}

			if (fieldname.equals("Material Type") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 2).toString());
			}

			if (fieldname.equals("Equipment Type") == true)
			{
				stringSelection = new StringSelection(jTable1.getValueAt(row, 10).toString());
			}
			
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		}
	}
	
	
	private void clearFilter()
	{

		jTextFieldMaterial.setText("");

		jTextFieldDescription.setText("");

		materialType.setType("");
		materialType.getMaterialTypeProperties();
		jComboBoxMaterialType.getModel().setSelectedItem(materialType);

		baseUom.clear();
		jComboBoxBaseUOM.getModel().setSelectedItem(baseUom);

		jTextFieldShelfLife.setText("");

		sluom.getShelfLifeUomProperties("");
		jComboBoxShelfLifeUOM.getModel().setSelectedItem(sluom);

		slrr.getShelfLifeRuleProperties("");
		jComboBoxRoundingRule.getModel().setSelectedItem(slrr);
		textFieldEquipmentType.setText("");

		buildSQL();
		populateList();

	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals(lang.get("lbl_Material")) == true)
			{
				jTextFieldMaterial.setText(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals(lang.get("lbl_Description")) == true)
			{
				jTextFieldDescription.setText(jTable1.getValueAt(row, 1).toString());
			}

			if (fieldname.equals(lang.get("lbl_Material_Type")) == true)
			{
				materialType.setType(jTable1.getValueAt(row, 2).toString());
				materialType.getMaterialTypeProperties();
				jComboBoxMaterialType.getModel().setSelectedItem(materialType);
			}

			if (fieldname.equals(lang.get("lbl_Material_Base_UOM")) == true)
			{
				baseUom.setInternalUom(jTable1.getValueAt(row, 3).toString());
				baseUom.getInternalUomProperties();
				jComboBoxBaseUOM.getModel().setSelectedItem(baseUom);
			}

			if (fieldname.equals(lang.get("lbl_Material_Shelf_Life")) == true)
			{
				jTextFieldShelfLife.setText(jTable1.getValueAt(row, 4).toString());
			}

			if (fieldname.equals(lang.get("lbl_Material_Shelf_Life_UOM")) == true)
			{
				sluom.getShelfLifeUomProperties(jTable1.getValueAt(row, 5).toString());
				jComboBoxShelfLifeUOM.getModel().setSelectedItem(sluom);
			}

			if (fieldname.equals(lang.get("lbl_Material_Shelf_Life_Rounding_Rule")) == true)
			{
				slrr.getShelfLifeRuleProperties(jTable1.getValueAt(row, 6).toString());
				jComboBoxRoundingRule.getModel().setSelectedItem(slrr);
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
		JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		PreparedStatement temp = buildSQLr();
		export.saveAs("materials.xls", material.getMaterialDataResultSet(temp), Common.mainForm);
		populateList();
	}

	private void print()
	{
		PreparedStatement temp = buildSQLr();
		JLaunchReport.runReport("RPT_MATERIALS", null, "", temp, "");
	}

	private void search()
	{
		buildSQL();
		populateList();
	}

	private void deleteRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lmaterial = jTable1.getValueAt(row, 0).toString();
			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Material_Delete") + " " + lmaterial, lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (n == 0)
			{
				JDBMaterial m = new JDBMaterial(Common.selectedHostID, Common.sessionID);
				m.setMaterial(lmaterial);
				boolean result = m.delete();
				if (result == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, m.getErrorMessage(), "Delete error (" + lmaterial + ")", JOptionPane.WARNING_MESSAGE);
				} else
				{
					buildSQL();
					populateList();
				}
			}
		}
	}

	private void addRecord()
	{
		JDBMaterial m = new JDBMaterial(Common.selectedHostID, Common.sessionID);
		lmaterial = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Material_Create"), null, JOptionPane.QUESTION_MESSAGE, Common.icon_confirm_16x16, null, null);
		if (lmaterial != null)
		{
			if (lmaterial.equals("") == false)
			{
				lmaterial = lmaterial.toUpperCase();
				m.setMaterial(lmaterial);
				if (m.isValidMaterial() == false)
				{
					JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_EDIT", lmaterial);
				} else
				{
					JOptionPane.showMessageDialog(Common.mainForm, "Material [" + lmaterial + "] already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
				}
				buildSQL();
				populateList();
			}
		}

	}

	private void populateList()
	{
		JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);

		JDBMaterialTableModel materialtable = new JDBMaterialTableModel(material.getMaterialDataResultSet(listStatement));
		TableRowSorter<JDBMaterialTableModel> sorter = new TableRowSorter<JDBMaterialTableModel>(materialtable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(materialtable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumn col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Col);
		col.setPreferredWidth(80);
		col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Description_Col);
		col.setPreferredWidth(300);
		col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Type_Col);
		col.setPreferredWidth(50);
		col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Base_Uom_Col);
		col.setPreferredWidth(60);
		col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Shelf_Life_Col);
		col.setPreferredWidth(60);
		col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Shelf_Life_Uom_Col);
		col.setPreferredWidth(60);
		col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Shelf_Life_Rule_Col);
		col.setPreferredWidth(50);
	
		col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Gross_Weight_Col);
		col.setPreferredWidth(80);
		col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Net_Weight_Col);
		col.setPreferredWidth(80);
		col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Weight_UOM_Col);
		col.setPreferredWidth(60);	
		col = jTable1.getColumnModel().getColumn(JDBMaterialTableModel.Material_Equipment_Col);
		col.setPreferredWidth(70);	
		jScrollPane1.repaint();
		
		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), materialtable.getRowCount());


	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lmaterial = jTable1.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_EDIT", lmaterial);
		}
	}

	private PreparedStatement buildSQLr()
	{

		PreparedStatement result;
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL"));
		query.addParamtoSQL("material=", jTextFieldMaterial.getText());
		query.addParamtoSQL("inspection_id=", textFieldInspectionID.getText());

		if (jTextFieldDescription.getText().equals("") == false)
		{
			query.addParamtoSQL("upper(description) LIKE ", "%" + jTextFieldDescription.getText().toUpperCase() + "%");
		}
		query.addParamtoSQL("base_uom=", ((JDBUom) jComboBoxBaseUOM.getSelectedItem()).getInternalUom());
		query.addParamtoSQL("material_type=", ((JDBMaterialType) jComboBoxMaterialType.getSelectedItem()).getType());
		query.addParamtoSQL("shelf_life_uom=", ((JShelfLifeUom) jComboBoxShelfLifeUOM.getSelectedItem()).getUom());
		query.addParamtoSQL("shelf_life_rule=", ((JShelfLifeRoundingRule) jComboBoxRoundingRule.getSelectedItem()).getRule());
		query.addParamtoSQL("default_batch_status=", ((String) jComboBoxDefaultBatchStatus.getSelectedItem()).toString());
		query.addParamtoSQL("equipment_type=", textFieldEquipmentType.getText());

		if (checkBox4j_Enabled.isSelected())
		{
			query.addParamtoSQL("enabled=", "Y");
		}
		else
		{
			query.addParamtoSQL("enabled=", "N");
		}
		
		Integer i;

		try
		{
			i = Integer.valueOf(jTextFieldShelfLife.getText());
			query.addParamtoSQL("shelf_life=", i);
		} catch (Exception e)
		{
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

	public JInternalFrameMaterialAdmin()
	{
		super();
		setIconifiable(true);

		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		uomList.add(new JDBUom(Common.selectedHostID, Common.sessionID));
		uomList.addAll(uom.getInternalUoms());
		typeList.add(new JDBMaterialType(Common.selectedHostID, Common.sessionID));
		typeList.addAll(materialType.getMaterialTypes());

		initGUI();

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIALS"));

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
		} else
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
			this.setTitle("Material Admin");
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
					jScrollPane1.setBounds(0, 183, 968, 378);
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
						jTable1.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_EDIT"))
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
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_ADD"));
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
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_EDIT"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_DELETE"));
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
											sortBy("MATERIAL_TYPE");
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
											sortBy("BASE_UOM");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Base_UOM"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("SHELF_LIFE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Shelf_Life"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("SHELF_LIFE_RULE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Shelf_Life_Rounding_Rule"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("DEFAULT_BATCH_STATUS");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Default_Batch_Status"));
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
									newItemMenuItem.setText(lang.get("lbl_Material_Base_UOM"));
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
									newItemMenuItem.setText(lang.get("lbl_Material_Shelf_Life"));
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
									newItemMenuItem.setText(lang.get("lbl_Material_Shelf_Life_UOM"));
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
									newItemMenuItem.setText(lang.get("lbl_Material_Shelf_Life_Rounding_Rule"));
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
											copyToClipboard("Material");
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
											copyToClipboard("Description");
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
											copyToClipboard("Material Type");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Type"));
									clipboardMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											copyToClipboard("Equipment Type");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Equipment_Type"));
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
					jButtonSearch.setBounds(-1, 143, 109, 32);
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
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(113, 11, 125, 22);
				}
				{
					JLabel4j_std lblInspectionID = new JLabel4j_std(lang.get("lbl_Inspection_ID"));
					lblInspectionID.setBounds(0, 110, 105, 21);
					jDesktopPane1.add(lblInspectionID);
					lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					textFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
					textFieldInspectionID.setBounds(114, 110, 117, 22);
					jDesktopPane1.add(textFieldInspectionID);
					textFieldInspectionID.setColumns(10);
				}
				{
					ComboBoxModel<JDBMaterialType> jComboBox1Model = new DefaultComboBoxModel<JDBMaterialType>(typeList);
					jComboBoxMaterialType = new JComboBox4j<JDBMaterialType>();
					jDesktopPane1.add(jComboBoxMaterialType);
					jComboBoxMaterialType.setModel(jComboBox1Model);
					jComboBoxMaterialType.setBounds(372, 11, 248, 21);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBMaterial.field_description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setBounds(114, 44, 264, 22);
				}
				{
					JLabel4j_std jLabel_Equipment = new JLabel4j_std();
					jLabel_Equipment.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel_Equipment.setBounds(405, 44, 135, 21);
					jLabel_Equipment.setText(lang.get("lbl_Material_Equipment_Type"));
					jDesktopPane1.add(jLabel_Equipment);
				}
				{
					textFieldEquipmentType = new JTextField4j(JDBEquipmentType.field_EquipmentType);
					textFieldEquipmentType.setBounds(549, 44, 91, 22);
					jDesktopPane1.add(textFieldEquipmentType);
				}
				{
					JButton4j jButtonLookupEquipment = new JButton4j(Common.icon_lookup_16x16);
					jButtonLookupEquipment.setBounds(640, 44, 21, 21);
					jButtonLookupEquipment.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "Y";
							if (JLaunchLookup.equipmentType()) {
								textFieldEquipmentType.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					jDesktopPane1.add(jButtonLookupEquipment);
				}
				{
					jLabel1 = new JLabel4j_std();
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setBounds(0, 11, 105, 21);
				}
				{
					jLabel2 = new JLabel4j_std();
					jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Material_Type"));
					jLabel2.setBounds(250, 11, 115, 21);
				}
				{
					jLabel3 = new JLabel4j_std();
					jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Description"));
					jLabel3.setBounds(0, 44, 105, 21);
				}
				{
					ComboBoxModel<JDBUom> jComboBox2Model = new DefaultComboBoxModel<JDBUom>(uomList);
					jComboBoxBaseUOM = new JComboBox4j<JDBUom>();
					jDesktopPane1.add(jComboBoxBaseUOM);
					jComboBoxBaseUOM.setModel(jComboBox2Model);
					jComboBoxBaseUOM.setBounds(114, 77, 154, 21);
					jComboBoxBaseUOM.setMaximumRowCount(12);
				}
				{
					jLabel4 = new JLabel4j_std();
					jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
					jDesktopPane1.add(jLabel4);
					jLabel4.setText(lang.get("lbl_Base_UOM"));
					jLabel4.setBounds(0, 77, 105, 21);
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setBounds(215, 143, 109, 32);
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_ADD"));
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							addRecord();
						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit_16x16);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setBounds(323, 143, 109, 32);
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_EDIT"));
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete_16x16);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setBounds(431, 143, 109, 32);
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_DELETE"));
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							deleteRecord();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(755, 143, 109, 32);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jLabel7 = new JLabel4j_std();
					jDesktopPane1.add(jLabel7);
					jLabel7.setText(lang.get("lbl_Material_Shelf_Life"));
					jLabel7.setBounds(665, 11, 162, 21);
					jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
				}
				{
					jTextFieldShelfLife = new JTextField4j();
					jDesktopPane1.add(jTextFieldShelfLife);
					jTextFieldShelfLife.setBounds(835, 11, 125, 22);
				}
				{

					JShelfLifeUom slu = new JShelfLifeUom();
					slu.setUom("");
					slu.setDescription("");
					shelfLifeUomList.add(slu);
					shelfLifeUomList.addAll(slu.getShelfLifeUOMs());
					ComboBoxModel<JShelfLifeUom> jComboBox5Model = new DefaultComboBoxModel<JShelfLifeUom>(shelfLifeUomList);
					jComboBoxShelfLifeUOM = new JComboBox4j<JShelfLifeUom>();
					jDesktopPane1.add(jComboBoxShelfLifeUOM);
					jComboBoxShelfLifeUOM.setModel(jComboBox5Model);
					jComboBoxShelfLifeUOM.setBounds(836, 44, 125, 21);
					jComboBoxShelfLifeUOM.setMaximumRowCount(12);
				}
				{
					jLabel8 = new JLabel4j_std();
					jDesktopPane1.add(jLabel8);
					jLabel8.setText(lang.get("lbl_Material_Shelf_Life_UOM"));
					jLabel8.setBounds(665, 44, 162, 21);
					jLabel8.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(863, 143, 109, 32);
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
					jButtonPrint.setBounds(539, 143, 109, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_MATERIALS"));
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
					JShelfLifeRoundingRule slrr = new JShelfLifeRoundingRule();
					slrr.setRule("");
					slrr.setDescription("");
					shelfLifeRule.add(slrr);
					shelfLifeRule.addAll(slrr.getShelfLifeRoundingRules());
					ComboBoxModel<JShelfLifeRoundingRule> jComboBoxRoundingRuleModel = new DefaultComboBoxModel<JShelfLifeRoundingRule>(shelfLifeRule);
					jComboBoxRoundingRule = new JComboBox4j<JShelfLifeRoundingRule>();
					jDesktopPane1.add(jComboBoxRoundingRule);
					jComboBoxRoundingRule.setModel(jComboBoxRoundingRuleModel);
					jComboBoxRoundingRule.setBounds(837, 77, 125, 21);
				}
				{
					jLabel9 = new JLabel4j_std();
					jDesktopPane1.add(jLabel9);
					jLabel9.setText(lang.get("lbl_Material_Shelf_Life_Rounding_Rule"));
					jLabel9.setBounds(634, 78, 193, 21);
					jLabel9.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Sort_By"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(286, 110, 146, 21);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(Common.materialSortBy);
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					jComboBoxSortBy.setBounds(447, 110, 168, 21);
					jComboBoxSortBy.setMaximumRowCount(Common.materialSortBy.length);
				}
				{
					jLabel12 = new JLabel4j_std();
					jDesktopPane1.add(jLabel12);
					jLabel12.setText(lang.get("lbl_Material_Default_Batch_Status"));
					jLabel12.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel12.setBounds(286, 78, 146, 21);
				}
				{
					ComboBoxModel<String> jComboBoxDefaultBatchStatusModel = new DefaultComboBoxModel<String>(Common.batchStatusIncBlank);
					jComboBoxDefaultBatchStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxDefaultBatchStatus);
					jComboBoxDefaultBatchStatus.setModel(jComboBoxDefaultBatchStatusModel);
					jComboBoxDefaultBatchStatus.setBounds(447, 78, 168, 21);
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(616, 110, 21, 21);
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
					jButtonExcel.setBounds(647, 143, 109, 32);
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
					jButtonClear.setBounds(107, 143, 109, 32);
					jDesktopPane1.add(jButtonClear);
				}
				{
					JButton4j btnLookupInspection = new JButton4j("");
					btnLookupInspection.setIcon(Common.icon_lookup_16x16);
					btnLookupInspection.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgCriteriaDefault = "";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.qmInspections())
							{
								textFieldInspectionID.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					btnLookupInspection.setBounds(230, 110, 21, 21);
					jDesktopPane1.add(btnLookupInspection);
				}
				
				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Limit"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(775, 110, 84, 21);
				jDesktopPane1.add(label4j_std);

				jCheckBoxLimit.setSelected(true);
				jCheckBoxLimit.setBackground(Color.WHITE);
				jCheckBoxLimit.setBounds(863, 110, 21, 21);
				jDesktopPane1.add(jCheckBoxLimit);

				JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
				ne.getTextField().setFont(Common.font_std);
				jSpinnerLimit.setEditor(ne);
				jSpinnerLimit.setValue(1000);
				jSpinnerLimit.setBounds(892, 110, 68, 21);
				jDesktopPane1.add(jSpinnerLimit);
				
				JLabel4j_std label4j_Enabled = new JLabel4j_std();
				label4j_Enabled.setText(lang.get("lbl_Enabled"));
				label4j_Enabled.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_Enabled.setBounds(644, 110, 84, 21);
				jDesktopPane1.add(label4j_Enabled);
				
				checkBox4j_Enabled.setSelected(true);
				checkBox4j_Enabled.setBackground(Color.WHITE);
				checkBox4j_Enabled.setBounds(733, 110, 21, 21);
				jDesktopPane1.add(checkBox4j_Enabled);

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void palletHistory()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			JLaunchMenu.runForm("FRM_ADMIN_PALLET_HISTORY","MATERIAL", jTable1.getValueAt(row, 0).toString());
		}
	}
	
	private void palletAdmin()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			JLaunchMenu.runForm("FRM_ADMIN_PALLETS","MATERIAL", jTable1.getValueAt(row, 0).toString());
		}
	}	

	private void orderAdmin()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			JLaunchMenu.runForm("FRM_ADMIN_PROCESS_ORDER","MATERIAL", jTable1.getValueAt(row, 0).toString());
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
