package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMHNAssign.java
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
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMHN;
import com.commander4j.db.JDBMHNDecisions;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBMaterialType;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBUom;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.tablemodel.JDBMHNPalletTableModelAssign;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameMHNAssign class allows the user to assign SSCC's (pallets) to a Master Hold Notice in the APP_MHN table.
 * When pallets are assigned to a MHN an entry is put in the APP_PALLET_HISTORY table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMHNAssign.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameMHNProperties JInternalFrameMHNProperties
 * @see com.commander4j.app.JInternalFrameMHNAdmin JInternalFrameMHNAdmin
 * @see com.commander4j.db.JDBMHN JDBMHN
 */
public class JInternalFrameMHNAssign extends JInternalFrame
{
	private JButton4j jButtonClear;
	private JLabel4j_std jStatusText;
	private JTextField4j jTextFieldDespatch_No;
	private JLabel4j_std jLabel8_1;
	private static final long serialVersionUID = 1;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButtonMenuItem rbascending = new JRadioButtonMenuItem();
	private JRadioButtonMenuItem rbdescending = new JRadioButtonMenuItem();
	private static boolean dlg_sort_descending = false;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonSearch;
	private JTextField4j jTextFieldLocation;
	private JLabel4j_std jLabel4;
	private JLabel4j_std jLabel23;
	private JButton4j jButtonLookupProcessOrder;
	private JCheckBox4j jCheckBoxLimit;
	private JSpinner jSpinnerLimit;
	private JLabel4j_std jLabel7;
	private JDateControl domDateTo;
	private JCheckBox4j jCheckBoxDOMTo;
	private JDateControl domDateFrom;
	private JCheckBox4j jCheckBoxQuantity;
	private JTextField4j jTextFieldVariant;
	private JLabel4j_std jLabel6;
	private JLabel4j_std jLabel5;
	private JTextField4j jTextFieldEAN;
	private JLabel4j_std jLabelProductionDate;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std jLabelProcessOrder;
	private JTextField4j jTextFieldBatch;
	private JLabel4j_std jLabel2;
	private JButton4j jButtonClose;
	private JButton4j jButtonLookupCustomer;
	private JCheckBox4j jCheckBoxExpiryTo;
	private JCheckBox4j jCheckBoxExpiryFrom;
	private JLabel4j_std jLabel8;
	private JDateControl expiryDateTo;
	private JDateControl expiryDateFrom;
	private JLabel4j_std jLabelSCC;
	private JTextField4j jTextFieldSSCC;
	private JTextField4j jTextFieldCustomer;
	private JButton jButtonLookupBatch;
	private JCheckBox4j jCheckBoxDOMFrom;
	private JButton4j jButtonLookupLocation;
	private JButton4j jButtonLookupMaterial;
	private JQuantityInput jFormattedTextFieldQuantity;
	private JLabel4j_std jLabelQuantity;
	private JToggleButton jToggleButtonSequence;
	private JLabel4j_std jLabel15;
	private JComboBox4j<String> jComboBoxPalletStatus;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JComboBox4j<JDBUom> jComboBoxUOM;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private JTextField4j jTextFieldMaterial;
	private JTable4j jTable1;
	private JScrollPane jScrollPane1;
	private JDBUom u = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDBMaterialType t = new JDBMaterialType(Common.selectedHostID, Common.sessionID);
	private Vector<JDBUom> uomList = new Vector<JDBUom>();
	private Vector<JDBMaterialType> typeList = new Vector<JDBMaterialType>();
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCalendarButton button_CalendardomDateFrom;
	private JCalendarButton button_CalendardomDateTo;
	private JCalendarButton calendarButtonexpiryDateFrom;
	private JCalendarButton calendarButtonexpiryDateTo;
	private String mhnnumber = "";
	private JInternalFrameMHNProperties callingForm;
	private JTextField4j jTextFieldMHN;
	private PreparedStatement listStatement;

	public JInternalFrameMHNAssign(JInternalFrameMHNProperties callingForm, String mhnnumber) {
		super();

		this.mhnnumber = mhnnumber;
		this.callingForm = callingForm;

		uomList.add(new JDBUom(Common.selectedHostID, Common.sessionID));
		uomList.addAll(u.getInternalUoms());
		typeList.add(new JDBMaterialType(Common.selectedHostID, Common.sessionID));
		typeList.addAll(t.getMaterialTypes());

		initGUI();
		jTextFieldMHN.setText(mhnnumber);
		jDesktopPane1.add(jTextFieldMaterial);
		jDesktopPane1.add(jButtonLookupMaterial);
		jDesktopPane1.add(jLabel3);
		jDesktopPane1.add(jTextFieldLocation);
		jDesktopPane1.add(jButtonLookupLocation);
		jDesktopPane1.add(jLabel2);
		jDesktopPane1.add(jTextFieldBatch);
		jDesktopPane1.add(jButtonLookupBatch);
		jDesktopPane1.add(jLabelProductionDate);
		jDesktopPane1.add(jCheckBoxDOMFrom);
		jDesktopPane1.add(domDateFrom);
		jDesktopPane1.add(button_CalendardomDateFrom);
		jDesktopPane1.add(jCheckBoxDOMTo);
		jDesktopPane1.add(domDateTo);
		jDesktopPane1.add(jLabelProcessOrder);
		jDesktopPane1.add(button_CalendardomDateTo);
		jDesktopPane1.add(jTextFieldProcessOrder);
		jDesktopPane1.add(jButtonLookupProcessOrder);
		jDesktopPane1.add(jLabel15);
		jDesktopPane1.add(jComboBoxPalletStatus);
		jDesktopPane1.add(jLabel8);
		jDesktopPane1.add(jCheckBoxExpiryFrom);
		jDesktopPane1.add(expiryDateFrom);
		jDesktopPane1.add(calendarButtonexpiryDateFrom);
		jDesktopPane1.add(jCheckBoxExpiryTo);
		jDesktopPane1.add(expiryDateTo);
		jDesktopPane1.add(calendarButtonexpiryDateTo);
		jDesktopPane1.add(jLabel4);
		jDesktopPane1.add(jComboBoxUOM);
		jDesktopPane1.add(jLabel8_1);
		jDesktopPane1.add(jTextFieldDespatch_No);
		jDesktopPane1.add(jLabelSCC);
		jDesktopPane1.add(jLabel23);
		jDesktopPane1.add(jTextFieldSSCC);
		jDesktopPane1.add(jTextFieldCustomer);
		jDesktopPane1.add(jButtonLookupCustomer);
		jDesktopPane1.add(jLabelQuantity);
		jDesktopPane1.add(jCheckBoxQuantity);
		jDesktopPane1.add(jFormattedTextFieldQuantity);
		jDesktopPane1.add(jLabel5);
		jDesktopPane1.add(jTextFieldEAN);
		jDesktopPane1.add(jLabel6);
		jDesktopPane1.add(jTextFieldVariant);
		jDesktopPane1.add(jButtonSearch);
		jDesktopPane1.add(jButtonClear);
		jDesktopPane1.add(jLabel7);
		jDesktopPane1.add(jButtonClose);
		jDesktopPane1.add(jToggleButtonSequence);
		jDesktopPane1.add(jComboBoxSortBy);
		jDesktopPane1.add(jLabel10);
		jDesktopPane1.add(jCheckBoxLimit);
		jDesktopPane1.add(jSpinnerLimit);
		jDesktopPane1.add(jScrollPane1);
		jDesktopPane1.add(jStatusText);

		JButton4j jButtonAssign = new JButton4j();
		jButtonAssign.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				assign();
			}
		});
		jButtonAssign.setText(lang.get("btn_Assign_to_MHN"));
		jButtonAssign.setBounds(245, 138, 120, 32);
		jDesktopPane1.add(jButtonAssign);
		this.setTitle("[" + mhnnumber + "]");
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_PALLET where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		setSequence(dlg_sort_descending);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		getContentPane().setBackground(Color.WHITE);
	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		String temp = "";

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBPallet.selectWithExpiry");

		query.addText(temp);

		query.addParamtoSQL("confirmed = ", "Y");


		if (jTextFieldSSCC.getText().equals("") == false)
		{
			query.addParamtoSQL("sscc = ", jTextFieldSSCC.getText());
		}

		if (jTextFieldMaterial.getText().equals("") == false)
		{
			query.addParamtoSQL("material = ", jTextFieldMaterial.getText());
		}

		if (jTextFieldCustomer.getText().equals("") == false)
		{
			query.addParamtoSQL("customer_id=", jTextFieldCustomer.getText());
		}

		if (jTextFieldBatch.getText().equals("") == false)
		{
			query.addParamtoSQL("batch_number like ", jTextFieldBatch.getText());
		}

		if (jTextFieldProcessOrder.getText().equals("") == false)
		{
			query.addParamtoSQL("process_order = ", jTextFieldProcessOrder.getText());
		}

		if (jTextFieldLocation.getText().equals("") == false)
		{
			query.addParamtoSQL("location_id = ", jTextFieldLocation.getText());
		}

		if (jTextFieldEAN.getText().equals("") == false)
		{
			query.addParamtoSQL("EAN = ", jTextFieldEAN.getText());
		}

		if (jTextFieldDespatch_No.getText().equals("") == false)
		{
			query.addParamtoSQL("DESPATCH_NO = ", jTextFieldDespatch_No.getText());
		}

		if (jTextFieldVariant.getText().equals("") == false)
		{
			query.addParamtoSQL("variant = ", jTextFieldVariant.getText());
		}

		query.addParamtoSQL("uom=", ((JDBUom) jComboBoxUOM.getSelectedItem()).getInternalUom());

		query.addParamtoSQL("status=", ((String) jComboBoxPalletStatus.getSelectedItem()).toString());

		if (jCheckBoxQuantity.isSelected())
		{
			if (jFormattedTextFieldQuantity.getText().equals("") == false)
			{
				query.addParamtoSQL("quantity=",JUtility.stringToBigDecimal(jFormattedTextFieldQuantity.getText().toString()));
			}
		}

		if (jCheckBoxDOMFrom.isSelected())
		{
			query.addParamtoSQL("date_of_manufacture>=", JUtility.getTimestampFromDate(domDateFrom.getDate()));
		}

		if (jCheckBoxDOMTo.isSelected())
		{
			query.addParamtoSQL("date_of_manufacture<=", JUtility.getTimestampFromDate(domDateTo.getDate()));
		}

		if (jCheckBoxExpiryFrom.isSelected())
		{
			query.addParamtoSQL("expiry_date>=", JUtility.getTimestampFromDate(expiryDateFrom.getDate()));
		}

		if (jCheckBoxExpiryTo.isSelected())
		{
			query.addParamtoSQL("expiry_date<=", JUtility.getTimestampFromDate(expiryDateTo.getDate()));
		}

		Integer i;

		try
		{
			i = Integer.valueOf(jFormattedTextFieldQuantity.getText());
			query.addParamtoSQL("quantity=", i);
		}
		catch (Exception e)
		{
		}


		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(jCheckBoxLimit.isSelected(), Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), jSpinnerLimit.getValue());
		query.bindParams();
		listStatement =  query.getPreparedStatement();
	}

	private void sortBy(String orderField)
	{
		jComboBoxSortBy.setSelectedItem(orderField);
		buildSQL();
		populateList();
	}

	private void clearFilter()
	{
		jTextFieldMaterial.setText("");
		jTextFieldBatch.setText("");
		jTextFieldSSCC.setText("");
		jTextFieldLocation.setText("");
		jTextFieldProcessOrder.setText("");
		jTextFieldDespatch_No.setText("");
		jTextFieldEAN.setText("");
		jTextFieldVariant.setText("");
		jComboBoxPalletStatus.setSelectedItem("");
		jTextFieldCustomer.setText("");
		jComboBoxUOM.setSelectedItem("");
		jCheckBoxDOMFrom.setSelected(false);
		jCheckBoxDOMTo.setSelected(false);
		jCheckBoxExpiryFrom.setSelected(false);
		jCheckBoxExpiryTo.setSelected(false);
		buildSQL();
		populateList();
	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			if (fieldname.equals("SSCC") == true)
			{
				jTextFieldSSCC.setText(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals("Material") == true)
			{
				jTextFieldMaterial.setText(jTable1.getValueAt(row, 1).toString());
			}

			if (fieldname.equals("Batch") == true)
			{
				jTextFieldBatch.setText(jTable1.getValueAt(row, 2).toString());
			}

			if (fieldname.equals("Process Order") == true)
			{
				jTextFieldProcessOrder.setText(jTable1.getValueAt(row, 3).toString());
			}

			if (fieldname.equals("Pallet Status") == true)
			{
				jComboBoxPalletStatus.setSelectedItem(jTable1.getValueAt(row, 7).toString());
			}

			if (fieldname.equals("Location") == true)
			{
				jTextFieldLocation.setText(jTable1.getValueAt(row, 8).toString());
			}

			if (fieldname.equals("Despatch") == true)
			{
				jTextFieldDespatch_No.setText(jTable1.getValueAt(row, 10).toString());
			}
			buildSQL();
			populateList();

		}
	}

	private void assign()
	{
		int rowCount = jTable1.getSelectedRowCount();

		if (rowCount >= 0)
		{
			int[] rows = jTable1.getSelectedRows();

			JDBPallet temp = new JDBPallet(Common.selectedHostID, Common.sessionID);
			JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
			JDBMHNDecisions decis = new JDBMHNDecisions(Common.selectedHostID, Common.sessionID);

			int n = JOptionPane.showConfirmDialog(Common.mainForm, "Assign selected SSCC's to Master Hold Notice [" + mhnnumber + "  ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (n == 0)
			{

				String initalDecision = ctrl.getKeyValueWithDefault("MHN INITIAL DECISION", "Pending", "Decision for pallets added to MHN");
				String initialStatus = "";
				if (decis.getDecisionProperties(initalDecision) == true)
				{
					initialStatus = decis.getStatus();
				}
				else
				{
					initialStatus = "";
				}

				for (int l = 0; l < rows.length; l++)
				{
					String sscc = jTable1.getValueAt(rows[l], JDBMHNPalletTableModelAssign.SSCC_Col).toString();
					temp.getPalletProperties(sscc);
					temp.updateMHNNumber(mhnnumber);
					temp.updateMHNDecision(initalDecision);
					if (initialStatus.equals("") == false)
					{
						temp.updateStatus(initialStatus,true);
					}
					jStatusText.setText("SSCC "+sscc+" added to MHN "+mhnnumber);
					Rectangle progressRect = jStatusText.getBounds();  
					progressRect.x = 0;  
					progressRect.y = 0;  
					jStatusText.paintImmediately( progressRect );

				}
				try
				{
					callingForm.refreshData();
				}
				catch (Exception ex)
				{

				}
				buildSQL();
				populateList();

			}
		}

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(979, 535));
			this.setBounds(0, 0, 1002, 585);
			this.setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);

				jDesktopPane1.setPreferredSize(new java.awt.Dimension(917, 504));
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jScrollPane1.setBounds(2, 178, 980, 348);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][] { { "One", "Two" }, { "Three", "Four" } }, new String[] { "Column 1", "Column 2" });
						jTable1 = new JTable4j();

						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jTable1, popupMenu);

							JMenuItem4j menuItem = new JMenuItem4j(lang.get("lbl_Assign_to_MHN"));
							menuItem.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									assign();
								}
							});
							popupMenu.add(menuItem);

							{
								final JMenu4j filterByMenu = new JMenu4j();
								filterByMenu.setText(lang.get("lbl_Filter_By"));
								popupMenu.add(filterByMenu);

								{
									final JMenuItem4j menuItemFilterBySSCC = new JMenuItem4j();
									menuItemFilterBySSCC.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("SSCC");
										}
									});
									menuItemFilterBySSCC.setText(lang.get("lbl_Pallet_SSCC"));
									filterByMenu.add(menuItemFilterBySSCC);
								}

								{
									final JMenuItem4j menuItemFilterByMaterial = new JMenuItem4j();
									menuItemFilterByMaterial.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("Material");
										}
									});
									menuItemFilterByMaterial.setText(lang.get("lbl_Material"));
									filterByMenu.add(menuItemFilterByMaterial);
								}

								{
									final JMenuItem4j menuItemFilterByBatch = new JMenuItem4j();
									menuItemFilterByBatch.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("Batch");
										}
									});
									menuItemFilterByBatch.setText(lang.get("lbl_Material_Batch"));
									filterByMenu.add(menuItemFilterByBatch);
								}

								{
									final JMenuItem4j menuItemFilterByLocation = new JMenuItem4j();
									menuItemFilterByLocation.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("Location");
										}
									});
									menuItemFilterByLocation.setText(lang.get("lbl_Location_ID"));
									filterByMenu.add(menuItemFilterByLocation);
								}

								{
									final JMenuItem4j menuItemFilterByDespatch = new JMenuItem4j();
									menuItemFilterByDespatch.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("Despatch");
										}
									});
									menuItemFilterByDespatch.setText(lang.get("lbl_Despatch_No"));
									filterByMenu.add(menuItemFilterByDespatch);
								}

								{
									final JMenuItem4j menuItemFilterByPalletStatus = new JMenuItem4j();
									menuItemFilterByPalletStatus.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("Pallet Status");
										}
									});
									menuItemFilterByPalletStatus.setText(lang.get("lbl_Pallet_Status"));
									filterByMenu.add(menuItemFilterByPalletStatus);
								}

								{
									final JMenuItem4j menuItemFilterByProcessOrder = new JMenuItem4j();
									menuItemFilterByProcessOrder.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("Process Order");
										}
									});
									menuItemFilterByProcessOrder.setText(lang.get("lbl_Process_Order"));
									filterByMenu.add(menuItemFilterByProcessOrder);
								}

								{
									filterByMenu.addSeparator();
								}

								{
									final JMenuItem4j menuItemResetFilter = new JMenuItem4j();
									menuItemResetFilter.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											clearFilter();
										}
									});
									menuItemResetFilter.setText(lang.get("btn_Clear_Filter"));
									filterByMenu.add(menuItemResetFilter);
								}
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
											sortBy("QUANTITY");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_Quantity"));
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

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("STATUS");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_Status"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("LOCATION_ID");
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
											sortBy("UOM");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Pallet_UOM"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("EAN");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_UOM_EAN"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem4j newItemMenuItem = new JMenuItem4j();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("VARIANT");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_UOM_Variant"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenu4j orderMenu = new JMenu4j();
									orderMenu.setText(lang.get("lbl_Sort_By"));
									sortByMenu.add(orderMenu);

									{

										rbascending.addActionListener(new ActionListener()
										{
											public void actionPerformed(final ActionEvent e)
											{
												setSequence(false);
											}
										});
										buttonGroup.add(rbascending);
										rbascending.setSelected(true);
										rbascending.setText("Ascending");
										orderMenu.add(rbascending);
									}

									{

										rbdescending.addActionListener(new ActionListener()
										{
											public void actionPerformed(final ActionEvent e)
											{
												setSequence(true);
											}
										});
										buttonGroup.add(rbdescending);
										rbdescending.setText("Descending");
										orderMenu.add(rbdescending);
									}
								}
							}
						}
					}
				}
				{
					jButtonSearch = new JButton4j(Common.icon_search);
					jButtonSearch.setBounds(2, 138, 120, 32);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.setMnemonic(lang.getMnemonicChar());
					jButtonSearch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							buildSQL();
							populateList();
						}
					});
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jTextFieldMaterial.setBounds(345, 11, 98, 22);
				}
				{
					jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
					jTextFieldLocation.setBounds(571, 11, 98, 22);
				}
				{
					jTextFieldCustomer = new JTextField4j(JDBCustomer.field_customer_id);
					jTextFieldCustomer.setBounds(346, 105, 98, 22);
				}
				{
					jLabel1 = new JLabel4j_std();
					jLabel1.setBounds(251, 11, 88, 21);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel3 = new JLabel4j_std();
					jLabel3.setBounds(490, 11, 74, 21);
					jLabel3.setText(lang.get("lbl_Location_ID"));
				}
				{
					jLabel23 = new JLabel4j_std();
					jLabel23.setBounds(265, 105, 74, 21);
					jLabel23.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel23.setText(lang.get("lbl_Customer_ID"));
				}
				{
					ComboBoxModel<JDBUom> jComboBox2Model = new DefaultComboBoxModel<JDBUom>(uomList);
					jComboBoxUOM = new JComboBox4j<JDBUom>();
					jComboBoxUOM.setBounds(571, 75, 119, 23);
					jComboBoxUOM.setModel(jComboBox2Model);
					jComboBoxUOM.setMaximumRowCount(12);
				}
				{
					jLabel4 = new JLabel4j_std();
					jLabel4.setBounds(487, 75, 77, 21);
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel4.setText(lang.get("lbl_Material_UOM"));
					jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jButtonClose.setBounds(366, 138, 120, 32);
					jButtonClose.setText(lang.get("btn_Close"));
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
					jLabel10 = new JLabel4j_std();
					jLabel10.setBounds(744, 136, 126, 21);
					jLabel10.setText(lang.get("lbl_Limit"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[] { "DATE_OF_MANUFACTURE", "SSCC", "MATERIAL", "PROCESS_ORDER", "BATCH_NUMBER", "DATE_OF_MANUFACTURE",
							"STATUS", "LOCATION_ID" });
					jComboBoxSortBy = new JComboBox4j<String>();
					jComboBoxSortBy.setBounds(571, 138, 195, 23);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
				}
				{
					ComboBoxModel<String> jComboBoxDefaultPalletStatusModel = new DefaultComboBoxModel<String>(Common.palletStatusIncBlank);
					jComboBoxPalletStatus = new JComboBox4j<String>();
					jComboBoxPalletStatus.setBounds(803, 44, 155, 23);
					jComboBoxPalletStatus.setModel(jComboBoxDefaultPalletStatusModel);
				}
				{
					jLabel15 = new JLabel4j_std();
					jLabel15.setBounds(702, 44, 97, 21);
					jLabel15.setText(lang.get("lbl_Pallet_Status"));
					jLabel15.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jToggleButtonSequence.setBounds(778, 138, 21, 21);
					jToggleButtonSequence.setSelected(true);
					jToggleButtonSequence.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}
				{
					jLabelQuantity = new JLabel4j_std();
					jLabelQuantity.setBounds(476, 105, 88, 21);
					jLabelQuantity.setText(lang.get("lbl_Pallet_Quantity"));
					jLabelQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{

					jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldQuantity.setBounds(599, 105, 91, 22);
					jFormattedTextFieldQuantity.setFont(Common.font_std);
					jFormattedTextFieldQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
					jFormattedTextFieldQuantity.setVerifyInputWhenFocusTarget(false);
					jFormattedTextFieldQuantity.setEnabled(false);
				}
				{
					jLabel2 = new JLabel4j_std();
					jLabel2.setBounds(710, 11, 89, 21);
					jLabel2.setText(lang.get("lbl_Material_Batch"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jTextFieldBatch.setBounds(803, 11, 134, 22);
				}
				{
					jLabelProcessOrder = new JLabel4j_std();
					jLabelProcessOrder.setBounds(475, 44, 89, 21);
					jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
					jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jTextFieldProcessOrder.setBounds(571, 44, 98, 22);
				}
				{
					jLabelProductionDate = new JLabel4j_std();
					jLabelProductionDate.setBounds(2, 42, 119, 25);
					jLabelProductionDate.setText(lang.get("lbl_Pallet_DOM"));
					jLabelProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldEAN = new JTextField4j(JDBMaterialUom.field_ean);
					jTextFieldEAN.setBounds(803, 105, 119, 22);
					jTextFieldEAN.setFocusCycleRoot(true);
				}
				{
					jLabel5 = new JLabel4j_std();
					jLabel5.setBounds(702, 105, 97, 21);
					jLabel5.setText(lang.get("lbl_Material_UOM_EAN"));
					jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel5.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jLabel6 = new JLabel4j_std();
					jLabel6.setBounds(922, 105, 15, 21);
					jLabel6.setText("/");
					jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
					jLabel6.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jTextFieldVariant = new JTextField4j(JDBMaterialUom.field_ean);
					jTextFieldVariant.setBounds(937, 105, 30, 22);
					jTextFieldVariant.setHorizontalAlignment(SwingConstants.CENTER);
					jTextFieldVariant.setFocusCycleRoot(true);
				}
				{
					jCheckBoxQuantity = new JCheckBox4j();
					jCheckBoxQuantity.setBounds(571, 105, 21, 21);
					jCheckBoxQuantity.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxQuantity.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxQuantity.isSelected())
							{
								jFormattedTextFieldQuantity.setValue(0);
								jFormattedTextFieldQuantity.setEnabled(true);
							}
							else
							{
								jFormattedTextFieldQuantity.setValue(0);
								jFormattedTextFieldQuantity.setEnabled(false);
							}
						}
					});
				}
				{
					domDateFrom = new JDateControl();
					domDateFrom.setBounds(148, 40, 120, 25);
					domDateFrom.setEnabled(false);

				}
				{
					jCheckBoxDOMTo = new JCheckBox4j();
					jCheckBoxDOMTo.setBounds(319, 40, 21, 25);
					jCheckBoxDOMTo.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxDOMTo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxDOMTo.isSelected())
							{
								button_CalendardomDateTo.setEnabled(true);
								domDateTo.setEnabled(true);
							}
							else
							{
								domDateTo.setEnabled(false);
								button_CalendardomDateTo.setEnabled(false);
							}
						}
					});
				}
				{
					domDateTo = new JDateControl();
					domDateTo.setBounds(345, 40, 120, 25);
					domDateTo.setEnabled(false);
				}
				{
					jLabel7 = new JLabel4j_std();
					jLabel7.setBounds(466, 138, 98, 21);
					jLabel7.setText(lang.get("lbl_Sort_By"));
					jLabel7.setHorizontalAlignment(SwingConstants.TRAILING);
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
					jSpinnerLimit.setBounds(902, 136, 71, 22);
					jSpinnerLimit.setFont(Common.font_std);
					jSpinnerLimit.setModel(jSpinnerIntModel);
					jSpinnerLimit.setValue(1000);
					jSpinnerLimit.getEditor().setSize(45, 21);
				}
				{
					jCheckBoxLimit = new JCheckBox4j();
					jCheckBoxLimit.setBounds(877, 136, 21, 21);
					jCheckBoxLimit.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxLimit.setSelected(true);
					jCheckBoxLimit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxLimit.isSelected())
							{
								jSpinnerLimit.setEnabled(true);
							}
							else
							{
								jSpinnerLimit.setEnabled(false);
							}
						}
					});
				}
				{
					jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup);
					jButtonLookupProcessOrder.setBounds(669, 44, 21, 21);
					jButtonLookupProcessOrder.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgCriteriaDefault = "Ready";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.processOrders())
							{
								jTextFieldProcessOrder.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jButtonLookupBatch = new JButton4j(Common.icon_lookup);
					jButtonLookupBatch.setBounds(937, 11, 21, 21);
					jButtonLookupBatch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgCriteriaDefault = jTextFieldMaterial.getText();
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.materialBatches())
							{
								jTextFieldBatch.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jButtonLookupMaterial = new JButton4j(Common.icon_lookup);
					jButtonLookupMaterial.setBounds(444, 11, 21, 21);
					jButtonLookupMaterial.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.materials())
							{
								jTextFieldMaterial.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jButtonLookupLocation = new JButton4j(Common.icon_lookup);
					jButtonLookupLocation.setBounds(669, 11, 21, 21);
					jButtonLookupLocation.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "Y";
							if (JLaunchLookup.locations())
							{
								jTextFieldLocation.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jButtonLookupCustomer = new JButton4j(Common.icon_lookup);
					jButtonLookupCustomer.setBounds(444, 105, 21, 21);
					jButtonLookupCustomer.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.customers())
							{
								jTextFieldCustomer.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					jCheckBoxDOMFrom = new JCheckBox4j();
					jCheckBoxDOMFrom.setBounds(126, 40, 21, 25);
					jCheckBoxDOMFrom.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxDOMFrom.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxDOMFrom.isSelected())
							{
								domDateFrom.setEnabled(true);
								button_CalendardomDateFrom.setEnabled(true);
							}
							else
							{
								domDateFrom.setEnabled(false);
								button_CalendardomDateFrom.setEnabled(false);
							}
						}
					});
				}
				{
					jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
					jTextFieldSSCC.setBounds(135, 105, 133, 22);
				}
				{
					jLabelSCC = new JLabel4j_std();
					jLabelSCC.setBounds(2, 105, 119, 21);
					jLabelSCC.setText(lang.get("lbl_Pallet_SSCC"));
					jLabelSCC.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					expiryDateFrom = new JDateControl();
					expiryDateFrom.setBounds(148, 71, 120, 25);
					expiryDateFrom.setEnabled(false);
				}
				{
					expiryDateTo = new JDateControl();
					expiryDateTo.setBounds(345, 71, 120, 25);
					expiryDateTo.setEnabled(false);
				}
				{
					jLabel8 = new JLabel4j_std();
					jLabel8.setBounds(2, 71, 119, 25);
					jLabel8.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
					jLabel8.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jCheckBoxExpiryFrom = new JCheckBox4j();
					jCheckBoxExpiryFrom.setBounds(126, 72, 21, 25);
					jCheckBoxExpiryFrom.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxExpiryFrom.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxExpiryFrom.isSelected())
							{
								expiryDateFrom.setEnabled(true);
								calendarButtonexpiryDateFrom.setEnabled(true);
							}
							else
							{
								expiryDateFrom.setEnabled(false);
								calendarButtonexpiryDateFrom.setEnabled(false);
							}
						}
					});
				}
				{
					jCheckBoxExpiryTo = new JCheckBox4j();
					jCheckBoxExpiryTo.setBounds(319, 71, 21, 25);
					jCheckBoxExpiryTo.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxExpiryTo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jCheckBoxExpiryTo.isSelected())
							{
								expiryDateTo.setEnabled(true);
								calendarButtonexpiryDateTo.setEnabled(true);
							}
							else
							{
								expiryDateTo.setEnabled(false);
								calendarButtonexpiryDateTo.setEnabled(false);
							}
						}
					});
				}

				{
					jLabel8_1 = new JLabel4j_std();
					jLabel8_1.setBounds(702, 75, 97, 21);
					jLabel8_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel8_1.setText(lang.get("lbl_Despatch_No"));
				}

				{
					jTextFieldDespatch_No = new JTextField4j(JDBDespatch.field_despatch_no);
					jTextFieldDespatch_No.setBounds(803, 75, 119, 22);
					jTextFieldDespatch_No.setFocusCycleRoot(true);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setBounds(2, 530, 987, 21);
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				}

				{
					jButtonClear = new JButton4j(Common.icon_clear);
					jButtonClear.setBounds(124, 138, 120, 32);
					jButtonClear.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							clearFilter();

						}
					});
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
				}

				button_CalendardomDateFrom = new JCalendarButton(domDateFrom);
				button_CalendardomDateFrom.setBounds(273, 42, 21, 21);
				button_CalendardomDateFrom.setEnabled(false);

				button_CalendardomDateTo = new JCalendarButton(domDateTo);
				button_CalendardomDateTo.setBounds(470, 42, 21, 21);
				button_CalendardomDateTo.setEnabled(false);

				calendarButtonexpiryDateFrom = new JCalendarButton(expiryDateFrom);
				calendarButtonexpiryDateFrom.setBounds(273, 74, 21, 21);
				calendarButtonexpiryDateFrom.setEnabled(false);

				calendarButtonexpiryDateTo = new JCalendarButton(expiryDateTo);
				calendarButtonexpiryDateTo.setBounds(470, 74, 21, 21);
				calendarButtonexpiryDateTo.setEnabled(false);

				jTextFieldMHN = new JTextField4j(JDBMHN.field_mhn_number);
				jTextFieldMHN.setForeground(Color.RED);
				jTextFieldMHN.setBounds(148, 11, 120, 21);
				jTextFieldMHN.setEditable(false);
				jDesktopPane1.setLayout(null);

				JLabel4j_std label = new JLabel4j_std();
				label.setBounds(42, 11, 93, 21);
				label.setText(lang.get("lbl_MHN_Number"));
				label.setHorizontalAlignment(SwingConstants.TRAILING);

				jDesktopPane1.add(label);
				jDesktopPane1.add(jLabel1);
				jDesktopPane1.add(jTextFieldMHN);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void populateList()
	{
		JDBMHN mhn = new JDBMHN(Common.selectedHostID, Common.sessionID);

		JDBMHNPalletTableModelAssign mhntable = new JDBMHNPalletTableModelAssign(mhn.getMHNDataResultSet(listStatement));

		TableRowSorter<JDBMHNPalletTableModelAssign> sorter = new TableRowSorter<JDBMHNPalletTableModelAssign>(mhntable);

		jTable1.setRowSorter(sorter);

		jTable1.setModel(mhntable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.SSCC_Col).setPreferredWidth(132);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.Material_Col).setPreferredWidth(68);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.Batch_Col).setPreferredWidth(75);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.Process_Order_Col).setPreferredWidth(82);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.Quantity_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.Uom_Col).setPreferredWidth(35);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.Date_of_Manufacture_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.SSCC_Status_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.Batch_Status_Col).setPreferredWidth(95);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.Location_Col).setPreferredWidth(88);
		jTable1.getColumnModel().getColumn(JDBMHNPalletTableModelAssign.MHN_Col).setPreferredWidth(80);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), mhntable.getRowCount());

	}

	private void setSequence(boolean descending)
	{
		if (jToggleButtonSequence.isSelected() == true)
		{
			rbdescending.setSelected(true);
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending);
		}
		else
		{
			rbascending.setSelected(true);
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending);
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
