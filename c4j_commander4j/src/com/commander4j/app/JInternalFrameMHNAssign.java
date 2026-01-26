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
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
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
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenu4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JToggleButton4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.tablemodel.JDBMHNPalletTableModelAssign;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameMHNAssign class allows the user to assign SSCC's (pallets) to a
 * Master Hold Notice in the APP_MHN table. When pallets are assigned to a MHN
 * an entry is put in the APP_PALLET_HISTORY table.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMHNAssign.jpg" >
 *
 * @see com.commander4j.app.JInternalFrameMHNProperties
 *      JInternalFrameMHNProperties
 * @see com.commander4j.app.JInternalFrameMHNAdmin JInternalFrameMHNAdmin
 * @see com.commander4j.db.JDBMHN JDBMHN
 */
public class JInternalFrameMHNAssign extends JInternalFrame
{
	private static boolean dlg_sort_descending = false;
	private static final long serialVersionUID = 1;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JButton4j jButtonClear;
	private JButton4j jButtonClose;
	private JButton4j jButtonLookupBatch;
	private JButton4j jButtonLookupCustomer;
	private JButton4j jButtonLookupLocation;
	private JButton4j jButtonLookupMaterial;
	private JButton4j jButtonLookupProcessOrder;
	private JButton4j jButtonSearch;
	private JCalendarButton button_CalendardomDateFrom;
	private JCalendarButton button_CalendardomDateTo;
	private JCalendarButton calendarButtonexpiryDateFrom;
	private JCalendarButton calendarButtonexpiryDateTo;
	private JCheckBox4j jCheckBoxDOMFrom;
	private JCheckBox4j jCheckBoxDOMTo;
	private JCheckBox4j jCheckBoxExpiryFrom;
	private JCheckBox4j jCheckBoxExpiryTo;
	private JCheckBox4j jCheckBoxLimit;
	private JCheckBox4j jCheckBoxQuantity;
	private JComboBox4j<JDBUom> jComboBoxUOM;
	private JComboBox4j<String> jComboBoxPalletStatus;
	private JComboBox4j<String> jComboBoxSortBy;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBMaterialType t = new JDBMaterialType(Common.selectedHostID, Common.sessionID);
	private JDBUom u = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDateControl domDateFrom;
	private JDateControl domDateTo;
	private JDateControl expiryDateFrom;
	private JDateControl expiryDateTo;
	private JDesktopPane4j jDesktopPane1;
	private JInternalFrameMHNProperties callingForm;
	private JLabel4j_status jStatusText;
	private JLabel4j_std jLabelLimit;
	private JLabel4j_std jLabelPalletStatus;
	private JLabel4j_std jLabelMaterial;
	private JLabel4j_std jLabelCustomer;
	private JLabel4j_std jLabelBatch;
	private JLabel4j_std jLabelLocation;
	private JLabel4j_std jLabelUOM;
	private JLabel4j_std jLabelMaterialUOM;
	private JLabel4j_std jLabel6;
	private JLabel4j_std jLabelSortBy;
	private JLabel4j_std jLabelBatchExpiry;
	private JLabel4j_std jLabelDespatchNo;
	private JLabel4j_std jLabelProcessOrder;
	private JLabel4j_std jLabelProductionDate;
	private JLabel4j_std jLabelQuantity;
	private JLabel4j_std jLabelSCC;
	private JQuantityInput jFormattedTextFieldQuantity;
	private JRadioButtonMenuItem rbascending = new JRadioButtonMenuItem();
	private JRadioButtonMenuItem rbdescending = new JRadioButtonMenuItem();
	private JScrollPane4j jScrollPane1;
	private JSpinner4j jSpinnerLimit;
	private JTable4j jTable1;
	private JTextField4j jTextFieldBatch;
	private JTextField4j jTextFieldCustomer;
	private JTextField4j jTextFieldDespatch_No;
	private JTextField4j jTextFieldEAN;
	private JTextField4j jTextFieldLocation;
	private JTextField4j jTextFieldMHN;
	private JTextField4j jTextFieldMaterial;
	private JTextField4j jTextFieldProcessOrder;
	private JTextField4j jTextFieldSSCC;
	private JTextField4j jTextFieldVariant;
	private JToggleButton4j jToggleButtonSequence;
	private PreparedStatement listStatement;
	private String mhnnumber = "";
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private Vector<JDBMaterialType> typeList = new Vector<JDBMaterialType>();
	private Vector<JDBUom> uomList = new Vector<JDBUom>();

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

	public JInternalFrameMHNAssign(JInternalFrameMHNProperties callingForm, String mhnnumber)
	{
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
		jDesktopPane1.add(jLabelLocation);
		jDesktopPane1.add(jTextFieldLocation);
		jDesktopPane1.add(jButtonLookupLocation);
		jDesktopPane1.add(jLabelBatch);
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
		jDesktopPane1.add(jLabelPalletStatus);
		jDesktopPane1.add(jComboBoxPalletStatus);
		jDesktopPane1.add(jLabelBatchExpiry);
		jDesktopPane1.add(jCheckBoxExpiryFrom);
		jDesktopPane1.add(expiryDateFrom);
		jDesktopPane1.add(calendarButtonexpiryDateFrom);
		jDesktopPane1.add(jCheckBoxExpiryTo);
		jDesktopPane1.add(expiryDateTo);
		jDesktopPane1.add(calendarButtonexpiryDateTo);
		jDesktopPane1.add(jLabelUOM);
		jDesktopPane1.add(jComboBoxUOM);
		jDesktopPane1.add(jLabelDespatchNo);
		jDesktopPane1.add(jTextFieldDespatch_No);
		jDesktopPane1.add(jLabelSCC);
		jDesktopPane1.add(jLabelCustomer);
		jDesktopPane1.add(jTextFieldSSCC);
		jDesktopPane1.add(jTextFieldCustomer);
		jDesktopPane1.add(jButtonLookupCustomer);
		jDesktopPane1.add(jLabelQuantity);
		jDesktopPane1.add(jCheckBoxQuantity);
		jDesktopPane1.add(jFormattedTextFieldQuantity);
		jDesktopPane1.add(jLabelMaterialUOM);
		jDesktopPane1.add(jTextFieldEAN);
		jDesktopPane1.add(jLabel6);
		jDesktopPane1.add(jTextFieldVariant);
		jDesktopPane1.add(jButtonSearch);
		jDesktopPane1.add(jButtonClear);
		jDesktopPane1.add(jLabelSortBy);
		jDesktopPane1.add(jButtonClose);
		jDesktopPane1.add(jToggleButtonSequence);
		jDesktopPane1.add(jComboBoxSortBy);
		jDesktopPane1.add(jLabelLimit);
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
		getContentPane().setBackground(Common.color_app_window);
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

			int n = JOptionPane.showConfirmDialog(Common.mainForm, "Assign selected SSCC's to Master Hold Notice [" + mhnnumber + "  ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
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
						temp.updateStatus(initialStatus, true);
					}
					jStatusText.setText("SSCC " + sscc + " added to MHN " + mhnnumber);
					Rectangle progressRect = jStatusText.getBounds();
					progressRect.x = 0;
					progressRect.y = 0;
					jStatusText.paintImmediately(progressRect);

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
				query.addParamtoSQL("quantity=", JUtility.stringToBigDecimal(jFormattedTextFieldQuantity.getText().toString()));
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
		listStatement = query.getPreparedStatement();
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

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(979, 535));
			this.setBounds(0, 0, 1002, 585);
			this.setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);

			jDesktopPane1.setPreferredSize(new java.awt.Dimension(917, 504));

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.Table);

			jScrollPane1.setBounds(2, 178, 980, 348);

			TableModel jTable1Model = new DefaultTableModel(new String[][]
			{
					{ "One", "Two" },
					{ "Three", "Four" } }, new String[]
			{ "Column 1", "Column 2" });
			jTable1 = new JTable4j();

			jScrollPane1.setViewportView(jTable1);
			jTable1.setModel(jTable1Model);

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

				filterByMenu.addSeparator();

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

				final JMenu4j orderMenu = new JMenu4j();
				orderMenu.setText(lang.get("lbl_Sort_By"));
				sortByMenu.add(orderMenu);

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

				final JMenu4j clipboardByMenu = new JMenu4j();
				clipboardByMenu.setText(lang.get("lbl_Clipboard_Copy"));
				popupMenu.add(clipboardByMenu);

				{
					final JMenuItem4j menuItemCopySSCC = new JMenuItem4j();
					menuItemCopySSCC.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("SSCC");
						}
					});
					menuItemCopySSCC.setText(lang.get("lbl_Pallet_SSCC"));
					clipboardByMenu.add(menuItemCopySSCC);
				}

				{
					final JMenuItem4j menuItemCopyMaterial = new JMenuItem4j();
					menuItemCopyMaterial.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("Material");
						}
					});
					menuItemCopyMaterial.setText(lang.get("lbl_Material"));
					clipboardByMenu.add(menuItemCopyMaterial);
				}

				{
					final JMenuItem4j menuItemCopyBatch = new JMenuItem4j();
					menuItemCopyBatch.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("Batch");
						}
					});
					menuItemCopyBatch.setText(lang.get("lbl_Material_Batch"));
					clipboardByMenu.add(menuItemCopyBatch);
				}

				{
					final JMenuItem4j menuItemCopyLocation = new JMenuItem4j();
					menuItemCopyLocation.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("Location");
						}
					});
					menuItemCopyLocation.setText(lang.get("lbl_Location_ID"));
					clipboardByMenu.add(menuItemCopyLocation);
				}

				{
					final JMenuItem4j menuItemCopyPalletStatus = new JMenuItem4j();
					menuItemCopyPalletStatus.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("Pallet Status");
						}
					});
					menuItemCopyPalletStatus.setText(lang.get("lbl_Pallet_Status"));
					clipboardByMenu.add(menuItemCopyPalletStatus);
				}

				{
					final JMenuItem4j menuItemCopyProcessOrder = new JMenuItem4j();
					menuItemCopyProcessOrder.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							copyToClipboard("Process Order");
						}
					});
					menuItemCopyProcessOrder.setText(lang.get("lbl_Process_Order"));
					clipboardByMenu.add(menuItemCopyProcessOrder);
				}

			}

			jButtonSearch = new JButton4j(Common.icon_search_16x16);
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

			jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
			jTextFieldMaterial.setBounds(345, 8, 100, 22);

			jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
			jTextFieldLocation.setBounds(581, 8, 134, 22);

			jTextFieldCustomer = new JTextField4j(JDBCustomer.field_customer_id);
			jTextFieldCustomer.setBounds(345, 104, 100, 22);

			jLabelMaterial = new JLabel4j_std();
			jLabelMaterial.setBounds(265, 8, 74, 22);
			jLabelMaterial.setText(lang.get("lbl_Material"));
			jLabelMaterial.setHorizontalAlignment(SwingConstants.TRAILING);

			jLabelLocation = new JLabel4j_std();
			jLabelLocation.setBounds(483, 8, 88, 22);
			jLabelLocation.setText(lang.get("lbl_Location_ID"));

			jLabelCustomer = new JLabel4j_std();
			jLabelCustomer.setBounds(265, 104, 74, 22);
			jLabelCustomer.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelCustomer.setText(lang.get("lbl_Customer_ID"));

			ComboBoxModel<JDBUom> jComboBox2Model = new DefaultComboBoxModel<JDBUom>(uomList);
			jComboBoxUOM = new JComboBox4j<JDBUom>();
			jComboBoxUOM.setBounds(581, 72, 155, 22);
			jComboBoxUOM.setModel(jComboBox2Model);
			jComboBoxUOM.setMaximumRowCount(12);

			jLabelUOM = new JLabel4j_std();
			jLabelUOM.setBounds(483, 72, 88, 22);
			jLabelLocation.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelUOM.setText(lang.get("lbl_Material_UOM"));
			jLabelUOM.setHorizontalAlignment(SwingConstants.TRAILING);

			jButtonClose = new JButton4j(Common.icon_close_16x16);
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

			jLabelLimit = new JLabel4j_std();
			jLabelLimit.setBounds(744, 143, 126, 22);
			jLabelLimit.setText(lang.get("lbl_Limit"));
			jLabelLimit.setHorizontalAlignment(SwingConstants.TRAILING);

			ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[]
			{ "DATE_OF_MANUFACTURE", "SSCC", "MATERIAL", "PROCESS_ORDER", "BATCH_NUMBER", "DATE_OF_MANUFACTURE", "STATUS", "LOCATION_ID" });
			jComboBoxSortBy = new JComboBox4j<String>();
			jComboBoxSortBy.setBounds(571, 143, 210, 22);
			jComboBoxSortBy.setModel(jComboBoxSortByModel);

			ComboBoxModel<String> jComboBoxDefaultPalletStatusModel = new DefaultComboBoxModel<String>(Common.palletStatusIncBlank);
			jComboBoxPalletStatus = new JComboBox4j<String>();
			jComboBoxPalletStatus.setBounds(827, 40, 155, 22);
			jComboBoxPalletStatus.setModel(jComboBoxDefaultPalletStatusModel);

			jLabelPalletStatus = new JLabel4j_std();
			jLabelPalletStatus.setBounds(726, 40, 97, 22);
			jLabelPalletStatus.setText(lang.get("lbl_Pallet_Status"));
			jLabelPalletStatus.setHorizontalAlignment(SwingConstants.TRAILING);

			jToggleButtonSequence = new JToggleButton4j();
			jToggleButtonSequence.setBounds(778, 143, 21, 22);
			jToggleButtonSequence.setSelected(true);
			jToggleButtonSequence.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					setSequence(jToggleButtonSequence.isSelected());
				}
			});

			jLabelQuantity = new JLabel4j_std();
			jLabelQuantity.setBounds(483, 104, 88, 22);
			jLabelQuantity.setText(lang.get("lbl_Pallet_Quantity"));
			jLabelQuantity.setHorizontalAlignment(SwingConstants.TRAILING);

			jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
			jFormattedTextFieldQuantity.setBounds(603, 104, 91, 22);
			jFormattedTextFieldQuantity.setVerifyInputWhenFocusTarget(false);
			jFormattedTextFieldQuantity.setEnabled(false);

			jLabelBatch = new JLabel4j_std();
			jLabelBatch.setBounds(734, 8, 89, 22);
			jLabelBatch.setText(lang.get("lbl_Material_Batch"));
			jLabelBatch.setHorizontalAlignment(SwingConstants.TRAILING);

			jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
			jTextFieldBatch.setBounds(827, 8, 134, 22);

			jLabelProcessOrder = new JLabel4j_std();
			jLabelProcessOrder.setBounds(483, 40, 88, 22);
			jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
			jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);

			jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
			jTextFieldProcessOrder.setBounds(581, 40, 134, 22);

			jLabelProductionDate = new JLabel4j_std();
			jLabelProductionDate.setBounds(2, 40, 111, 22);
			jLabelProductionDate.setText(lang.get("lbl_Pallet_DOM"));
			jLabelProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);

			jTextFieldEAN = new JTextField4j(JDBMaterialUom.field_ean);
			jTextFieldEAN.setBounds(827, 104, 119, 22);
			jTextFieldEAN.setFocusCycleRoot(true);

			jLabelMaterialUOM = new JLabel4j_std();
			jLabelMaterialUOM.setBounds(726, 104, 97, 22);
			jLabelMaterialUOM.setText(lang.get("lbl_Material_UOM_EAN"));
			jLabelMaterialUOM.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabelMaterialUOM.setHorizontalTextPosition(SwingConstants.RIGHT);

			jLabel6 = new JLabel4j_std();
			jLabel6.setBounds(950, 105, 10, 21);
			jLabel6.setText("/");
			jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel6.setHorizontalTextPosition(SwingConstants.RIGHT);

			jTextFieldVariant = new JTextField4j(JDBMaterialUom.field_ean);
			jTextFieldVariant.setBounds(961, 104, 21, 22);
			jTextFieldVariant.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldVariant.setFocusCycleRoot(true);

			jCheckBoxQuantity = new JCheckBox4j();
			jCheckBoxQuantity.setBounds(579, 104, 22, 22);

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

			domDateFrom = new JDateControl();
			domDateFrom.setBounds(148, 40, 120, 22);
			domDateFrom.setEnabled(false);

			jCheckBoxDOMTo = new JCheckBox4j();
			jCheckBoxDOMTo.setBounds(321, 40, 22, 22);

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

			domDateTo = new JDateControl();
			domDateTo.setBounds(345, 40, 120, 22);
			domDateTo.setEnabled(false);

			jLabelSortBy = new JLabel4j_std();
			jLabelSortBy.setBounds(466, 143, 98, 22);
			jLabelSortBy.setText(lang.get("lbl_Sort_By"));
			jLabelSortBy.setHorizontalAlignment(SwingConstants.TRAILING);

			SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
			jSpinnerIntModel.setMinimum(1);
			jSpinnerIntModel.setMaximum(5000);
			jSpinnerIntModel.setStepSize(1);
			jSpinnerLimit = new JSpinner4j();
			JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(jSpinnerLimit);
			jSpinnerLimit.setEditor(ne);
			jSpinnerLimit.setBounds(902, 143, 71, 22);
			jSpinnerLimit.setModel(jSpinnerIntModel);
			jSpinnerLimit.setValue(1000);
			jSpinnerLimit.getEditor().setSize(45, 21);

			jCheckBoxLimit = new JCheckBox4j();
			jCheckBoxLimit.setBounds(877, 143, 21, 22);

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

			jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupProcessOrder.setBounds(715, 40, 21, 22);
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

			jButtonLookupBatch = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupBatch.setBounds(961, 8, 21, 22);
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

			jButtonLookupMaterial = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupMaterial.setBounds(444, 8, 21, 22);
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

			jButtonLookupLocation = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupLocation.setBounds(715, 8, 21, 22);
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

			jButtonLookupCustomer = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupCustomer.setBounds(444, 104, 21, 22);
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

			jCheckBoxDOMFrom = new JCheckBox4j();
			jCheckBoxDOMFrom.setBounds(125, 40, 22, 22);

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

			jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
			jTextFieldSSCC.setBounds(148, 104, 120, 22);

			jLabelSCC = new JLabel4j_std();
			jLabelSCC.setBounds(2, 104, 111, 22);
			jLabelSCC.setText(lang.get("lbl_Pallet_SSCC"));
			jLabelSCC.setHorizontalAlignment(SwingConstants.TRAILING);

			expiryDateFrom = new JDateControl();
			expiryDateFrom.setBounds(148, 72, 120, 22);
			expiryDateFrom.setEnabled(false);

			expiryDateTo = new JDateControl();
			expiryDateTo.setBounds(345, 72, 120, 22);
			expiryDateTo.setEnabled(false);

			jLabelBatchExpiry = new JLabel4j_std();
			jLabelBatchExpiry.setBounds(2, 72, 111, 22);
			jLabelBatchExpiry.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
			jLabelBatchExpiry.setHorizontalAlignment(SwingConstants.TRAILING);

			jCheckBoxExpiryFrom = new JCheckBox4j();
			jCheckBoxExpiryFrom.setBounds(125, 72, 22, 22);

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

			jCheckBoxExpiryTo = new JCheckBox4j();
			jCheckBoxExpiryTo.setBounds(321, 72, 22, 22);

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

			jLabelDespatchNo = new JLabel4j_std();
			jLabelDespatchNo.setBounds(726, 72, 97, 22);
			jLabelDespatchNo.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelDespatchNo.setText(lang.get("lbl_Despatch_No"));

			jTextFieldDespatch_No = new JTextField4j(JDBDespatch.field_despatch_no);
			jTextFieldDespatch_No.setBounds(827, 72, 155, 22);
			jTextFieldDespatch_No.setFocusCycleRoot(true);

			jStatusText = new JLabel4j_status();
			jStatusText.setBounds(5, 530, 975, 21);

			jButtonClear = new JButton4j(Common.icon_clear_16x16);
			jButtonClear.setBounds(124, 138, 120, 32);
			jButtonClear.addActionListener(new ActionListener()
			{
				public void actionPerformed(final ActionEvent e)
				{
					clearFilter();

				}
			});
			jButtonClear.setText(lang.get("btn_Clear_Filter"));

			button_CalendardomDateFrom = new JCalendarButton(domDateFrom);
			button_CalendardomDateFrom.setBounds(270, 40, 22, 22);
			button_CalendardomDateFrom.setEnabled(false);

			button_CalendardomDateTo = new JCalendarButton(domDateTo);
			button_CalendardomDateTo.setBounds(468, 40, 22, 22);
			button_CalendardomDateTo.setEnabled(false);

			calendarButtonexpiryDateFrom = new JCalendarButton(expiryDateFrom);
			calendarButtonexpiryDateFrom.setBounds(270, 72, 22, 22);
			calendarButtonexpiryDateFrom.setEnabled(false);

			calendarButtonexpiryDateTo = new JCalendarButton(expiryDateTo);
			calendarButtonexpiryDateTo.setBounds(468, 72, 22, 22);
			calendarButtonexpiryDateTo.setEnabled(false);

			jTextFieldMHN = new JTextField4j(JDBMHN.field_mhn_number);
			jTextFieldMHN.setForeground(Color.RED);
			jTextFieldMHN.setBounds(148, 8, 120, 22);
			jTextFieldMHN.setEditable(false);
			jDesktopPane1.setLayout(null);

			JLabel4j_std jLabelMHN = new JLabel4j_std();
			jLabelMHN.setBounds(2, 8, 111, 22);
			jLabelMHN.setText(lang.get("lbl_MHN_Number"));
			jLabelMHN.setHorizontalAlignment(SwingConstants.TRAILING);

			jDesktopPane1.add(jLabelMHN);
			jDesktopPane1.add(jLabelMaterial);
			jDesktopPane1.add(jTextFieldMHN);

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
			jToggleButtonSequence.setIcon(Common.icon_descending_16x16);
		}
		else
		{
			rbascending.setSelected(true);
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending_16x16);
		}
	}

	private void sortBy(String orderField)
	{
		jComboBoxSortBy.setSelectedItem(orderField);
		buildSQL();
		populateList();
	}
}
