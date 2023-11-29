package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePalletSample.java
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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMHN;
import com.commander4j.db.JDBMHNDecisions;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBMaterialCustomerData;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBPalletExtension;
import com.commander4j.db.JDBPalletSamples;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQuery2;
import com.commander4j.db.JDBUom;
import com.commander4j.db.JDBWTSamplePoint;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBPalletSampleTableModel;
import com.commander4j.util.JColorPair;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JHelp;
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;
import javax.swing.ScrollPaneConstants;

/**
 * The JInternalFramePalletSample class is used to record additional information
 * in a pallet extension table for an already created SSCC.
 * 
 */
public class JInternalFramePalletSample extends javax.swing.JInternalFrame
{
	private JTextField4j jTextFieldUom;
	private JTextField4j jTextFieldBatchStatus;
	private JTextField4j jTextFieldProductGroup;
	private JLabel4j_std jLabel10;
	private JTextField4j jTextFieldLocation;
	private JLabel4j_std jLabel_Location;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JLabel4j_std jStatusText;
	private JLabel4j_std jLabelMHN;
	private JButton4j jButtonHelp;
	private JTextField4j jTextFieldSSCC;
	private JTextField4j jTextFieldBatch;
	private JTextField4j jTextFieldContainerCode;
	private JLabel4j_std jLabel_OrderDescription;
	private JTextField4j jTextFieldMaterialDescription;
	private JLabel4j_std jLabel_Decision;
	private JLabel4j_std jLabel_WeekNumber;
	private JTextField4j jTextFieldMHNDecision;
	private JFormattedTextField jFormattedTextFieldQuantity;
	private JFormattedTextField jFormattedTextFieldStartQuantity;
	private JDateControl productionDate;
	private JDateControl expiryDate;
	private JLabel4j_std jLabelQuantity;
	private JLabel4j_std jLabelStartQuantity;
	private JLabel4j_std jLabelContainerCode;
	private JLabel4j_std jLabelProductGroup;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std jLabelProcessOrder;
	private JLabel4j_std jLabel_Batch;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabel_Material;
	private JLabel4j_std jLabel_UOM;
	private JLabel4j_std jLabel_SSCC;
	private JTextField4j jTextFieldPalletStatus;
	private JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder processOrder = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private String lsscc;
	private JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBControl control = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBPalletSamples palsamp = new JDBPalletSamples(Common.selectedHostID, Common.sessionID);
	private JDBMaterialBatch materialBatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBPalletExtension palext = new JDBPalletExtension(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std jLabel_PalletStatus;
	private JLabel4j_std jLabelBatchExpiry;
	private JLabel4j_std jLabelProductionDate;
	private JTextField4j jTextFieldMHN;
	private JCheckBox4j checkBoxConfirmed;
	private JCheckBox4j checkBoxFirstCaseInput;
	private JCheckBox4j checkBoxLastCaseInput;
	private JButton4j jButtonLookup_Supplier_ID1;
	private JButton4j jButtonLookup_Shift_Names;
	private JButton4j jButtonLookup_SampleLocation;
	private JTextField4j jTextFieldProcessOrderResource;
	private JLabel4j_std jLabel_Resource;
	private JLabel4j_std jLabel_BaseQuantity;
	private JDateControl firstCaseTime;
	private JDateControl lastCaseTime;
	private JTextField4j jTextFieldSupplier_ID1;
	private JTextField4j jTextFieldSampleLocation;
	private JTextField4j jTextFieldShift;
	private JTextField4j jTextFieldIncident_Ref;
	private JCalendarButton calendarButtonFirst;
	private JCalendarButton calendarButtonLast;
	private HashMap<String, JColorPair> descisionLookup = new HashMap<String, JColorPair>();
	private JDBMHNDecisions dbDecision = new JDBMHNDecisions(Common.selectedHostID, Common.sessionID);
	private JDBMaterialCustomerData matCustData = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
	private Color foregroundDecision, backgroundDecision;
	private JButton4j jButtonSave = new JButton4j(Common.icon_update_16x16);
	private JButton4j jButtonNotifyEmail = new JButton4j(Common.icon_notifyEmail_16x16);
	private JButton4j jButtonPrint = new JButton4j(Common.icon_report_16x16);
	private JButton4j jButtonUndo = new JButton4j(Common.icon_undo_16x16);
	private JButton4j button4jSampleAdd = new JButton4j(Common.icon_add_16x16);
	private JButton4j button4jSampleDelete = new JButton4j(Common.icon_delete_16x16);
	private JButton4j button4jSampleEdit = new JButton4j(Common.icon_edit_16x16);
	private JButton4j button4jSampleRefresh = new JButton4j(Common.icon_refresh_16x16);
	private JTable4j jTable1;
	private PreparedStatement listStatement;
	private JScrollPane scrollPane1 = new JScrollPane();
	private JQuantityInput jFormattedTextFieldRequiredUOMQuantity;
	private JTextField4j jTextFieldBaseUom;
	private JTextField4j jTextFieldWeekNumber;
	private Color light_grey = new Color(238, 238, 238);
	private Color error_color = Color.RED;
	private JTextField4j jTextFieldDayOfWeek = new JTextField4j();
	private JTextField4j jTextFieldMonth = new JTextField4j();
	private JTextField4j jTextFieldYear = new JTextField4j();

	public JInternalFramePalletSample()
	{
		super();

		initGUI();

		blankfields();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PAL_SAMPLE"));

		refreshSamples();

		jTextFieldSSCC.setText(control.getKeyValue("SSCC PREFIX"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldSSCC.requestFocus();
				jTextFieldSSCC.setCaretPosition(jTextFieldSSCC.getText().length());

			}
		});
	}

	private void refreshSamples()
	{
		buildSQL();
		populateList();
	}

	private void buildSQL()
	{
		JDBQuery2.closeStatement(listStatement);
		listStatement = buildSQLr();
	}

	private PreparedStatement buildSQLr()
	{

		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("{schema}app_pallet_samples");

		if (jTextFieldSSCC.getText().equals("") == false)
		{
			q2.applyWhere("sscc = ", lsscc);
		}
		else
		{
			q2.applyWhere("sscc = ", "NA");
		}

		q2.applySort("SAMPLE_DATE", false);
		q2.applySQL();
		result = q2.getPreparedStatement();

		return result;
	}

	private void populateList()
	{
		JDBPalletSamples ps = new JDBPalletSamples(Common.selectedHostID, Common.sessionID);

		JDBPalletSampleTableModel pstm = new JDBPalletSampleTableModel(ps.getPalletDataResultSet(listStatement));

		TableRowSorter<JDBPalletSampleTableModel> sorter = new TableRowSorter<JDBPalletSampleTableModel>(pstm);

		jTable1.setRowSorter(sorter);

		this.setIconifiable(true);
		jTable1.setModel(pstm);
		scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(scrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		// jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SSCC_Col).setPreferredWidth(135);
		jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SampleSequence_Col).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SampleDate_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SamplePoint_Col).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SampleReason_Col).setPreferredWidth(65);
		jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SampleDefectType_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SampleDefectID_Col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SampleLeaking_Col).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SampleQuantity).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SampleComment_Col).setPreferredWidth(240);
		jTable1.getColumnModel().getColumn(JDBPalletSampleTableModel.SampleUserID_Col).setPreferredWidth(100);

		scrollPane1.repaint();

	}

	public JInternalFramePalletSample(String sscc)
	{
		this();
		lsscc = sscc;
		setPalletSSCC(sscc);
	}

	public void setPalletSSCC(String sscc)
	{
		promptToSaveChanges();

		lsscc = JUtility.replaceNullStringwithBlank(sscc);

		jTextFieldSSCC.setText(lsscc);

		if (lsscc.length() == 18)
		{
			refresh();
		}
		else
		{
			blankfields();
		}
		refreshSamples();
	}

	private void palletExtensionModified(boolean yesno)
	{
		jButtonSave.setEnabled(yesno);
		jButtonUndo.setEnabled(yesno);
	}

	private void setFirstTimeStatus(boolean yes)
	{
		palletExtensionModified(true);
		if (yes)
		{
			firstCaseTime.setEnabled(true);
			calendarButtonFirst.setEnabled(true);
			if ((firstCaseTime.getDate().compareTo(productionDate.getDate()) > 0))
			{
				firstCaseTime.setDate(productionDate.getDate());
			}
		}
		else
		{
			firstCaseTime.setEnabled(false);
			calendarButtonFirst.setEnabled(false);
		}
	}

	private void setLastTimeStatus(boolean yes)
	{
		palletExtensionModified(true);
		if (yes)
		{
			lastCaseTime.setEnabled(true);
			calendarButtonLast.setEnabled(true);
			if ((lastCaseTime.getDate().compareTo(productionDate.getDate()) > 0))
			{
				lastCaseTime.setDate(productionDate.getDate());
			}
		}
		else
		{
			lastCaseTime.setEnabled(false);
			calendarButtonLast.setEnabled(false);
		}
	}

	private void refresh()
	{
		pallet.setSSCC(lsscc);

		if (pallet.isValidPallet())
		{
			pallet.getPalletProperties(lsscc);

			if (pallet.isConfirmed())
			{

				jTextFieldProcessOrder.setText(pallet.getProcessOrder());
				jTextFieldMaterial.setText(pallet.getMaterial());
				jTextFieldBatch.setText(pallet.getBatchNumber());

				matCustData.setMaterial(pallet.getMaterial());
				matCustData.setCustomerID(pallet.getProcessOrderObj(true).getCustomerID());
				matCustData.setDataID("PRODUCT_GROUP");

				if (matCustData.getMaterialCustomerDataProperties())
				{
					jTextFieldProductGroup.setText(matCustData.getData());
				}

				matCustData.setDataID("CONTAINER_CODE");

				if (matCustData.getMaterialCustomerDataProperties())
				{
					jTextFieldContainerCode.setText(matCustData.getData());
				}

				jFormattedTextFieldQuantity.setValue(pallet.getQuantity());
				jTextFieldLocation.setText(pallet.getLocationID());
				jTextFieldMHN.setText(pallet.getMHNNumber());
				jTextFieldMHNDecision.setText(pallet.getDecision());

				if (descisionLookup.containsKey(pallet.getDecision()))
				{
					backgroundDecision = descisionLookup.get(pallet.getDecision()).background;
					foregroundDecision = descisionLookup.get(pallet.getDecision()).foreground;
				}
				else
				{
					backgroundDecision = Color.white;
					foregroundDecision = Color.black;
				}

				jTextFieldMHNDecision.setForeground(foregroundDecision);
				jTextFieldMHNDecision.setBackground(backgroundDecision);

				jTextFieldPalletStatus.setText(pallet.getStatus());
				checkBoxConfirmed.setSelected(pallet.isConfirmed());
				jTextFieldUom.setText(pallet.getUom());

				processOrder.getProcessOrderProperties(pallet.getProcessOrder());
				jTextFieldProcessOrderResource.setText(processOrder.getRequiredResource());

				material.getMaterialProperties(pallet.getMaterial());
				jTextFieldMaterialDescription.setText(material.getDescription());
				jTextFieldBaseUom.setText(material.getBaseUom());

				materialBatch.getMaterialBatchProperties(pallet.getMaterial(), pallet.getBatchNumber());
				jTextFieldBatchStatus.setText(materialBatch.getStatus());
				expiryDate.setValue(pallet.getBatchExpiry());

				productionDate.setValue(pallet.getDateOfManufacture());

				jTextFieldWeekNumber.setText(JUtility.getWeekOfYear(pallet.getDateOfManufacture()));
				jTextFieldDayOfWeek.setText(JUtility.getDay(pallet.getDateOfManufacture()));
				jTextFieldMonth.setText(JUtility.getMonth(pallet.getDateOfManufacture()));
				jTextFieldYear.setText(JUtility.getYear(pallet.getDateOfManufacture()));

				jFormattedTextFieldRequiredUOMQuantity.setValue(JUtility.stringToBigDecimal(processOrder.getRequiredUOMQuantity()));

				checkBoxFirstCaseInput.setEnabled(true);
				checkBoxLastCaseInput.setEnabled(true);
				jTextFieldShift.setEnabled(true);
				jTextFieldIncident_Ref.setEnabled(true);
				jTextFieldSupplier_ID1.setEnabled(true);
				jTextFieldSampleLocation.setEnabled(true);
				jButtonLookup_Supplier_ID1.setEnabled(true);
				jButtonLookup_Shift_Names.setEnabled(true);
				jButtonLookup_SampleLocation.setEnabled(true);
				calendarButtonFirst.setEnabled(true);
				calendarButtonLast.setEnabled(true);
				button4jSampleAdd.setEnabled(true);
				button4jSampleEdit.setEnabled(true);
				button4jSampleDelete.setEnabled(true);
				button4jSampleRefresh.setEnabled(true);

				if (palext.isValid(lsscc))
				{
					palext.getSamplePalletExtensionProperties(lsscc);

					checkBoxFirstCaseInput.setSelected(palext.getFirstCaseInput());
					if (palext.getFirstCaseInput())
					{
						firstCaseTime.setDate(palext.getFirstCaseTime());
						calendarButtonFirst.setEnabled(true);
						firstCaseTime.setEnabled(true);
					}
					else
					{
						calendarButtonFirst.setEnabled(false);
						firstCaseTime.setEnabled(false);
					}

					checkBoxLastCaseInput.setSelected(palext.getLastCaseInput());
					if (palext.getLastCaseInput())
					{
						lastCaseTime.setDate(palext.getLastCaseTime());
						calendarButtonLast.setEnabled(true);
						lastCaseTime.setEnabled(true);
					}
					else
					{
						calendarButtonLast.setEnabled(false);
						lastCaseTime.setEnabled(false);
					}

					jTextFieldSupplier_ID1.setText(palext.getSupplierID1());
					jTextFieldShift.setText(palext.getShiftID());
					jTextFieldIncident_Ref.setText(palext.getIncidentRef());
					jTextFieldSampleLocation.setText(palext.getLocation());
					palext.setWeekOfYear(pallet.getDateOfManufacture());
					jTextFieldWeekNumber.setText(palext.getPWeek().toString());

					if (palext.getPalletQuantity() == null)
					{
						jFormattedTextFieldStartQuantity.setValue(pallet.getQuantity());
						palletExtensionModified(true);
					}
					else
					{
						jFormattedTextFieldStartQuantity.setValue(palext.getPalletQuantity());
					}
					jButtonNotifyEmail.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_NOTIFY_SORTING"));
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_NOTIFY_SORTING"));

				}
				else
				{
					jButtonNotifyEmail.setEnabled(false);
					jButtonPrint.setEnabled(false);
					firstCaseTime.setEnabled(false);
					lastCaseTime.setEnabled(false);
					calendarButtonFirst.setEnabled(false);
					calendarButtonLast.setEnabled(false);
					checkBoxFirstCaseInput.setSelected(false);
					checkBoxLastCaseInput.setSelected(false);
					jTextFieldSupplier_ID1.setText("");
					jTextFieldSampleLocation.setText("");
					jTextFieldShift.setText("");
					jTextFieldIncident_Ref.setText("");

					jFormattedTextFieldStartQuantity.setValue(pallet.getQuantity());
				}

				jButtonSave.setEnabled(false);

				jStatusText.setText("SSCC " + pallet.getSSCC() + " retrieved.");
			}
			else
			{
				jStatusText.setText("SSCC " + pallet.getSSCC() + " unconfirmed.");
				blankfields();
			}
		}
		else
		{
			jStatusText.setText("SSCC " + pallet.getSSCC() + " does not exist.");
			blankfields();
		}

	}

	private void blankfields()
	{

		jTextFieldProcessOrder.setText("");
		jTextFieldMaterial.setText("");
		jTextFieldBatch.setText("");
		jFormattedTextFieldQuantity.setValue(0);
		jTextFieldLocation.setText("");
		jTextFieldMHN.setText("");
		jTextFieldMHNDecision.setText("");
		jTextFieldWeekNumber.setText("");
		backgroundDecision = Color.white;
		foregroundDecision = Color.black;
		jTextFieldMHNDecision.setForeground(foregroundDecision);
		jTextFieldMHNDecision.setBackground(backgroundDecision);
		jTextFieldPalletStatus.setText("");
		checkBoxConfirmed.setSelected(false);
		jTextFieldUom.setText("");
		jTextFieldBaseUom.setText("");
		jTextFieldProcessOrderResource.setText("");
		jFormattedTextFieldRequiredUOMQuantity.setValue(0);
		jTextFieldMaterialDescription.setText("");
		jTextFieldBatchStatus.setText("");
		jTextFieldContainerCode.setText("");
		jTextFieldProductGroup.setText("");
		jTextFieldSupplier_ID1.setText("");
		jTextFieldSampleLocation.setText("");
		jTextFieldShift.setText("");
		jTextFieldIncident_Ref.setText("");
		checkBoxFirstCaseInput.setSelected(false);
		checkBoxLastCaseInput.setSelected(false);
		checkBoxFirstCaseInput.setEnabled(false);
		checkBoxLastCaseInput.setEnabled(false);
		jTextFieldIncident_Ref.setEnabled(false);
		jButtonLookup_Supplier_ID1.setEnabled(false);
		jButtonLookup_Shift_Names.setEnabled(false);
		jButtonLookup_SampleLocation.setEnabled(false);
		firstCaseTime.setEnabled(false);
		lastCaseTime.setEnabled(false);
		calendarButtonFirst.setEnabled(false);
		calendarButtonLast.setEnabled(false);
		palletExtensionModified(false);
		button4jSampleAdd.setEnabled(false);
		button4jSampleEdit.setEnabled(false);
		button4jSampleDelete.setEnabled(false);
		button4jSampleRefresh.setEnabled(false);
		jTextFieldWeekNumber.setText("");
		jTextFieldDayOfWeek.setEditable(false);
		jTextFieldDayOfWeek.setText("");
		jTextFieldMonth.setEditable(false);
		jTextFieldMonth.setText("");
		jTextFieldYear.setEditable(false);
		jTextFieldYear.setText("");
		jFormattedTextFieldStartQuantity.setValue(new BigDecimal("0.000"));
		jButtonNotifyEmail.setEnabled(false);
		jButtonPrint.setEnabled(false);
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(471, 531));
			this.setBounds(0, 0, 1000, 624);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);

			descisionLookup = dbDecision.getDecisionColors();

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));
				jDesktopPane1.setLayout(null);

				JPanel panel_Pallet = new JPanel();
				panel_Pallet.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Pallet Information", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
				panel_Pallet.setBounds(5, 5, 978, 181);
				jDesktopPane1.add(panel_Pallet);
				panel_Pallet.setLayout(null);

				{
					jLabel_SSCC = new JLabel4j_std();
					jLabel_SSCC.setBounds(2, 28, 101, 21);
					panel_Pallet.add(jLabel_SSCC);
					jLabel_SSCC.setText(lang.get("lbl_Pallet_SSCC"));
					jLabel_SSCC.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
					jTextFieldSSCC.setEditable(true);
					jTextFieldSSCC.setEnabled(true);
					jTextFieldSSCC.setBounds(108, 28, 133, 21);
					panel_Pallet.add(jTextFieldSSCC);
					jTextFieldSSCC.addKeyListener(new KeyAdapter()
					{
						public void keyReleased(final KeyEvent e)
						{

							setPalletSSCC(jTextFieldSSCC.getText());
						}

						@Override
						public void keyPressed(KeyEvent e)
						{
							promptToSaveChanges();
						}
					});

				}

				{
					jLabelProcessOrder = new JLabel4j_std();
					jLabelProcessOrder.setBounds(249, 28, 100, 21);
					panel_Pallet.add(jLabelProcessOrder);
					jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
					jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jTextFieldProcessOrder.setBounds(357, 28, 100, 21);
					panel_Pallet.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setEnabled(false);
				}

				{
					jLabel_Material = new JLabel4j_std();
					jLabel_Material.setBounds(463, 28, 110, 21);
					panel_Pallet.add(jLabel_Material);
					jLabel_Material.setText(lang.get("lbl_Material"));
					jLabel_Material.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jTextFieldMaterial.setBounds(579, 28, 128, 21);
					panel_Pallet.add(jTextFieldMaterial);
					jTextFieldMaterial.setEnabled(false);
				}

				{
					jLabel_Location = new JLabel4j_std();
					jLabel_Location.setBounds(709, 28, 118, 21);
					panel_Pallet.add(jLabel_Location);
					jLabel_Location.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel_Location.setText(lang.get("lbl_Location_ID"));
				}

				{
					jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
					jTextFieldLocation.setBounds(835, 28, 128, 21);
					panel_Pallet.add(jTextFieldLocation);
					jTextFieldLocation.setEnabled(false);
				}

				{
					jLabelBatchExpiry = new JLabel4j_std();
					jLabelBatchExpiry.setBounds(709, 59, 118, 21);
					panel_Pallet.add(jLabelBatchExpiry);
					jLabelBatchExpiry.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
					jLabelBatchExpiry.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					expiryDate = new JDateControl();
					expiryDate.setBounds(835, 55, 128, 25);
					panel_Pallet.add(expiryDate);
					expiryDate.setEnabled(false);
				}

				{
					jLabelProductionDate = new JLabel4j_std();
					jLabelProductionDate.setBounds(463, 59, 110, 21);
					panel_Pallet.add(jLabelProductionDate);
					jLabelProductionDate.setText(lang.get("lbl_Pallet_DOM"));
					jLabelProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					productionDate = new JDateControl();
					productionDate.setEnabled(false);
					productionDate.setBounds(579, 55, 128, 25);
					panel_Pallet.add(productionDate);
				}

				{
					jLabel_OrderDescription = new JLabel4j_std();
					jLabel_OrderDescription.setBounds(2, 59, 101, 21);
					panel_Pallet.add(jLabel_OrderDescription);
					jLabel_OrderDescription.setText(lang.get("lbl_Description"));
					jLabel_OrderDescription.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jTextFieldMaterialDescription = new JTextField4j(JDBMaterial.field_description);
					jTextFieldMaterialDescription.setBounds(108, 59, 349, 21);
					panel_Pallet.add(jTextFieldMaterialDescription);
					jTextFieldMaterialDescription.setEnabled(false);
				}

				{
					jLabel_Batch = new JLabel4j_std();
					jLabel_Batch.setBounds(2, 88, 101, 21);
					panel_Pallet.add(jLabel_Batch);
					jLabel_Batch.setText(lang.get("lbl_Material_Batch"));
					jLabel_Batch.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jTextFieldBatch.setBounds(108, 88, 110, 21);
					panel_Pallet.add(jTextFieldBatch);
					jTextFieldBatch.setEnabled(false);
				}

				{
					jTextFieldContainerCode = new JTextField4j();
					jTextFieldContainerCode.setBounds(108, 146, 110, 21);
					panel_Pallet.add(jTextFieldContainerCode);
					jTextFieldContainerCode.setEnabled(false);
				}

				{
					jLabel10 = new JLabel4j_std();
					jLabel10.setBounds(239, 88, 100, 21);
					panel_Pallet.add(jLabel10);
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setText(lang.get("lbl_Material_Batch_Status"));
				}

				{
					jTextFieldBatchStatus = new JTextField4j(JDBMaterialBatch.field_batch_status);
					jTextFieldBatchStatus.setBounds(347, 88, 110, 21);
					panel_Pallet.add(jTextFieldBatchStatus);
					jTextFieldBatchStatus.setEnabled(false);
				}

				{
					jTextFieldProductGroup = new JTextField4j();
					jTextFieldProductGroup.setBounds(347, 146, 110, 21);
					panel_Pallet.add(jTextFieldProductGroup);
					jTextFieldProductGroup.setEnabled(false);
				}
				{

					jFormattedTextFieldRequiredUOMQuantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldRequiredUOMQuantity.setEditable(false);
					panel_Pallet.add(jFormattedTextFieldRequiredUOMQuantity);
					jFormattedTextFieldRequiredUOMQuantity.setFont(Common.font_std);
					jFormattedTextFieldRequiredUOMQuantity.setBounds(579, 146, 94, 21);
					jFormattedTextFieldRequiredUOMQuantity.setValue(0);
					jFormattedTextFieldRequiredUOMQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldBaseUom = new JTextField4j();
					jTextFieldBaseUom.setHorizontalAlignment(SwingConstants.CENTER);
					panel_Pallet.add(jTextFieldBaseUom);
					jTextFieldBaseUom.setBounds(685, 146, 56, 21);
					jTextFieldBaseUom.setEnabled(false);
				}
				{
					jLabel_PalletStatus = new JLabel4j_std();
					jLabel_PalletStatus.setBounds(463, 88, 110, 21);
					panel_Pallet.add(jLabel_PalletStatus);
					jLabel_PalletStatus.setText(lang.get("lbl_Pallet_Status"));
					jLabel_PalletStatus.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jLabel_Resource = new JLabel4j_std();
					jLabel_Resource.setBounds(463, 117, 110, 21);
					panel_Pallet.add(jLabel_Resource);
					jLabel_Resource.setText(lang.get("lbl_Process_Order_Required_Resource"));
					jLabel_Resource.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel_BaseQuantity = new JLabel4j_std();
					jLabel_BaseQuantity.setBounds(463, 146, 110, 21);
					panel_Pallet.add(jLabel_BaseQuantity);
					jLabel_BaseQuantity.setText(lang.get("lbl_Pallet_Base_Quantity"));
					jLabel_BaseQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldPalletStatus = new JTextField4j();
					jTextFieldPalletStatus.setEnabled(false);
					jTextFieldPalletStatus.setBounds(579, 88, 128, 21);
					panel_Pallet.add(jTextFieldPalletStatus);
				}

				{
					jTextFieldProcessOrderResource = new JTextField4j();
					jTextFieldProcessOrderResource.setEnabled(false);
					jTextFieldProcessOrderResource.setBounds(579, 117, 128, 21);
					panel_Pallet.add(jTextFieldProcessOrderResource);
				}

				{
					jLabelMHN = new JLabel4j_std();
					jLabelMHN.setBounds(709, 88, 118, 21);
					panel_Pallet.add(jLabelMHN);
					jLabelMHN.setText(lang.get("lbl_MHN_Number"));
					jLabelMHN.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jTextFieldMHN = new JTextField4j(JDBMHN.field_mhn_number);
					jTextFieldMHN.setFont(new Font("Arial", Font.BOLD, 11));
					jTextFieldMHN.setForeground(Color.RED);
					jTextFieldMHN.setEnabled(false);
					jTextFieldMHN.setBounds(835, 88, 87, 21);
					panel_Pallet.add(jTextFieldMHN);
				}

				{
					jLabelQuantity = new JLabel4j_std();
					jLabelQuantity.setBounds(2, 117, 101, 21);
					panel_Pallet.add(jLabelQuantity);
					jLabelQuantity.setText(lang.get("lbl_Pallet_Quantity"));
					jLabelQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jLabelContainerCode = new JLabel4j_std();
					jLabelContainerCode.setBounds(2, 146, 101, 21);
					panel_Pallet.add(jLabelContainerCode);
					jLabelContainerCode.setText(lang.get("lbl_Container_Code"));
					jLabelContainerCode.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jLabelProductGroup = new JLabel4j_std();
					jLabelProductGroup.setBounds(239, 146, 100, 21);
					panel_Pallet.add(jLabelProductGroup);
					jLabelProductGroup.setText(lang.get("lbl_Product_Group"));
					jLabelProductGroup.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldQuantity.setEditable(false);
					jFormattedTextFieldQuantity.setBounds(108, 117, 91, 21);
					panel_Pallet.add(jFormattedTextFieldQuantity);
					jFormattedTextFieldQuantity.setFont(Common.font_std);
					jFormattedTextFieldQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jLabel_UOM = new JLabel4j_std();
					jLabel_UOM.setBounds(207, 117, 80, 21);
					panel_Pallet.add(jLabel_UOM);
					jLabel_UOM.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel_UOM.setText(lang.get("lbl_Material_UOM"));
				}

				{
					jStatusText = new JLabel4j_std();
					jDesktopPane1.add(jStatusText);
					jStatusText.setForeground(new java.awt.Color(255, 0, 0));
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jStatusText.setBounds(0, 570, 990, 21);
				}

				{
					jTextFieldUom = new JTextField4j(JDBUom.field_uom);
					jTextFieldUom.setBounds(295, 117, 45, 21);
					panel_Pallet.add(jTextFieldUom);
					jTextFieldUom.setEnabled(false);
				}

				{
					jLabel_Decision = new JLabel4j_std();
					jLabel_Decision.setBounds(709, 117, 118, 21);
					panel_Pallet.add(jLabel_Decision);
					jLabel_Decision.setText(lang.get("lbl_Decision"));
					jLabel_Decision.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					jTextFieldMHNDecision = new JTextField4j(JDBProcessOrder.field_description);
					jTextFieldMHNDecision.setBounds(835, 117, 131, 21);
					panel_Pallet.add(jTextFieldMHNDecision);
					jTextFieldMHNDecision.setEnabled(false);
				}

				{
					checkBoxConfirmed = new JCheckBox4j();
					checkBoxConfirmed.setText("");
					checkBoxConfirmed.setSelected(false);
					checkBoxConfirmed.setEnabled(false);
					checkBoxConfirmed.setBackground(Color.WHITE);
					checkBoxConfirmed.setBounds(436, 114, 21, 24);
					panel_Pallet.add(checkBoxConfirmed);
				}

				{
					JLabel4j_std jLabelProductionDate_1 = new JLabel4j_std();
					jLabelProductionDate_1.setText(lang.get("lbl_Confirmed"));
					jLabelProductionDate_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelProductionDate_1.setBounds(348, 117, 80, 21);
					panel_Pallet.add(jLabelProductionDate_1);
				}

				JPanel panel_Pallet_Extension = new JPanel();
				panel_Pallet_Extension.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Additional Data", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
				panel_Pallet_Extension.setBounds(5, 190, 978, 169);
				jDesktopPane1.add(panel_Pallet_Extension);
				panel_Pallet_Extension.setLayout(null);

				JLabel4j_std lbl_First_Case_Time = new JLabel4j_std();
				lbl_First_Case_Time.setText(lang.get("lbl_First_Case_Time"));
				lbl_First_Case_Time.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_First_Case_Time.setBounds(2, 25, 117, 21);
				panel_Pallet_Extension.add(lbl_First_Case_Time);

				firstCaseTime = new JDateControl();
				firstCaseTime.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						palletExtensionModified(true);
					}
				});
				firstCaseTime.setEnabled(false);
				firstCaseTime.setBounds(145, 25, 128, 25);
				panel_Pallet_Extension.add(firstCaseTime);

				calendarButtonFirst = new JCalendarButton((JDateControl) firstCaseTime);
				calendarButtonFirst.setBounds(274, 27, 21, 21);
				panel_Pallet_Extension.add(calendarButtonFirst);

				JLabel4j_std lbl_Last_Case_Time = new JLabel4j_std();
				lbl_Last_Case_Time.setText(lang.get("lbl_Last_Case_Time"));
				lbl_Last_Case_Time.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Last_Case_Time.setBounds(274, 27, 117, 21);
				panel_Pallet_Extension.add(lbl_Last_Case_Time);

				lastCaseTime = new JDateControl();
				lastCaseTime.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						palletExtensionModified(true);
					}
				});
				lastCaseTime.setEnabled(false);
				lastCaseTime.setBounds(420, 25, 128, 25);
				panel_Pallet_Extension.add(lastCaseTime);

				calendarButtonLast = new JCalendarButton((JDateControl) lastCaseTime);
				calendarButtonLast.setBounds(549, 27, 21, 21);
				panel_Pallet_Extension.add(calendarButtonLast);

				JLabel4j_std lbl_Supplier_ID1 = new JLabel4j_std();
				lbl_Supplier_ID1.setText(lang.get("lbl_Supplier_ID1"));
				lbl_Supplier_ID1.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Supplier_ID1.setBounds(727, 29, 100, 21);
				panel_Pallet_Extension.add(lbl_Supplier_ID1);

				jTextFieldSupplier_ID1 = new JTextField4j(JDBPalletExtension.field_supplier_id);
				jTextFieldSupplier_ID1.setEditable(false);
				jTextFieldSupplier_ID1.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						palletExtensionModified(true);
					}
				});
				jTextFieldSupplier_ID1.setBounds(835, 29, 110, 21);
				panel_Pallet_Extension.add(jTextFieldSupplier_ID1);

				JLabel4j_std lbl_Location = new JLabel4j_std();
				lbl_Location.setText(lang.get("lbl_Sample_Location"));
				lbl_Location.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Location.setBounds(12, 65, 124, 21);
				panel_Pallet_Extension.add(lbl_Location);

				jTextFieldSampleLocation = new JTextField4j(JDBWTSamplePoint.field_SamplePoint);
				jTextFieldSampleLocation.setEditable(false);
				jTextFieldSampleLocation.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						palletExtensionModified(true);
					}
				});
				jTextFieldSampleLocation.setBounds(145, 65, 106, 21);
				panel_Pallet_Extension.add(jTextFieldSampleLocation);

				jTextFieldShift = new JTextField4j(JDBPalletExtension.field_shift);
				jTextFieldShift.setEditable(false);
				jTextFieldShift.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						palletExtensionModified(true);
					}
				});
				jTextFieldShift.setBounds(636, 29, 64, 21);
				panel_Pallet_Extension.add(jTextFieldShift);

				jTextFieldIncident_Ref = new JTextField4j(JDBPalletExtension.field_incident_ref);
				jTextFieldIncident_Ref.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						palletExtensionModified(true);
					}
				});
				jTextFieldIncident_Ref.setEnabled(false);
				jTextFieldIncident_Ref.setBounds(420, 65, 128, 21);
				panel_Pallet_Extension.add(jTextFieldIncident_Ref);

				{
					checkBoxFirstCaseInput = new JCheckBox4j();
					checkBoxFirstCaseInput.setOpaque(true);
					checkBoxFirstCaseInput.setBackground(light_grey);
					checkBoxFirstCaseInput.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{

							setFirstTimeStatus(checkBoxFirstCaseInput.isSelected());

						}
					});
					checkBoxFirstCaseInput.setText("");
					checkBoxFirstCaseInput.setBackground(light_grey);
					checkBoxFirstCaseInput.setBounds(122, 23, 21, 24);
					panel_Pallet_Extension.add(checkBoxFirstCaseInput);
				}

				{
					jFormattedTextFieldStartQuantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldStartQuantity.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyTyped(KeyEvent e)
						{
							palletExtensionModified(true);
						}
					});
					jFormattedTextFieldStartQuantity.setBounds(682, 65, 91, 21);
					panel_Pallet_Extension.add(jFormattedTextFieldStartQuantity);
					jFormattedTextFieldStartQuantity.setFont(Common.font_std);
					jFormattedTextFieldStartQuantity.setHorizontalAlignment(SwingConstants.TRAILING);

				}

				{
					jLabelStartQuantity = new JLabel4j_std();
					jLabelStartQuantity.setBounds(566, 65, 101, 21);
					panel_Pallet_Extension.add(jLabelStartQuantity);
					jLabelStartQuantity.setText(lang.get("lbl_Starting_Quantity"));
					jLabelStartQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				}

				{
					checkBoxLastCaseInput = new JCheckBox4j();
					checkBoxLastCaseInput.setOpaque(true);
					checkBoxLastCaseInput.setBackground(light_grey);
					checkBoxLastCaseInput.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							setLastTimeStatus(checkBoxLastCaseInput.isSelected());
						}
					});
					checkBoxLastCaseInput.setText("");
					checkBoxLastCaseInput.setBounds(395, 25, 21, 24);
					panel_Pallet_Extension.add(checkBoxLastCaseInput);
				}

				JLabel4j_std lbl_Shift_ID = new JLabel4j_std();
				lbl_Shift_ID.setText(lang.get("lbl_Shift_ID"));
				lbl_Shift_ID.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Shift_ID.setBounds(549, 29, 80, 21);
				panel_Pallet_Extension.add(lbl_Shift_ID);

				JLabel4j_std lbl_Incident_Ref = new JLabel4j_std();
				lbl_Incident_Ref.setText(lang.get("lbl_Incident_Ref"));
				lbl_Incident_Ref.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Incident_Ref.setBounds(284, 65, 127, 21);
				panel_Pallet_Extension.add(lbl_Incident_Ref);

				{
					jButtonLookup_Supplier_ID1 = new JButton4j(Common.icon_lookup_16x16);
					panel_Pallet_Extension.add(jButtonLookup_Supplier_ID1);
					jButtonLookup_Supplier_ID1.setBounds(945, 29, 21, 21);
					jButtonLookup_Supplier_ID1.setEnabled(true);
					jButtonLookup_Supplier_ID1.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "LAMINATE";
							if (JLaunchLookup.suppliers())
							{
								jTextFieldSupplier_ID1.setText(JLaunchLookup.dlgResult);
								palletExtensionModified(true);
							}
						}
					});
				}

				{
					jButtonLookup_Shift_Names = new JButton4j(Common.icon_lookup_16x16);
					panel_Pallet_Extension.add(jButtonLookup_Shift_Names);
					jButtonLookup_Shift_Names.setBounds(702, 29, 21, 21);
					jButtonLookup_Shift_Names.setEnabled(true);
					jButtonLookup_Shift_Names.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.shiftNames())
							{
								jTextFieldShift.setText(JLaunchLookup.dlgResult);
								palletExtensionModified(true);
							}
						}
					});
				}

				{
					jButtonLookup_SampleLocation = new JButton4j(Common.icon_lookup_16x16);
					panel_Pallet_Extension.add(jButtonLookup_SampleLocation);
					jButtonLookup_SampleLocation.setBounds(252, 65, 21, 21);
					jButtonLookup_SampleLocation.setEnabled(true);
					jButtonLookup_SampleLocation.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.samplePointLocations())
							{
								jTextFieldSampleLocation.setText(JLaunchLookup.dlgResult);
								palletExtensionModified(true);
							}
						}
					});
				}

				{
					jButtonSave.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							save();

						}
					});
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic('0');
					jButtonSave.setEnabled(true);
					jButtonSave.setBounds(373, 125, 100, 32);
					panel_Pallet_Extension.add(jButtonSave);
				}

				{
					jButtonNotifyEmail.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							notifyEmail();

						}
					});
					jButtonNotifyEmail.setText(lang.get("btn_Notify"));
					jButtonNotifyEmail.setMnemonic('0');
					jButtonNotifyEmail.setEnabled(false);
					jButtonNotifyEmail.setBounds(273, 125, 100, 32);
					panel_Pallet_Extension.add(jButtonNotifyEmail);
				}
				
				{
					jButtonPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							print();

						}
					});
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic('0');
					jButtonPrint.setEnabled(false);
					jButtonPrint.setBounds(173, 125, 100, 32);
					panel_Pallet_Extension.add(jButtonPrint);
				}

				{
					jButtonUndo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							refresh();
							palletExtensionModified(false);
						}
					});
					jButtonUndo.setText(lang.get("btn_Undo"));
					jButtonUndo.setMnemonic('0');
					jButtonUndo.setEnabled(true);
					jButtonUndo.setBounds(473, 125, 100, 32);
					panel_Pallet_Extension.add(jButtonUndo);
				}

				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(573, 125, 100, 32);
					panel_Pallet_Extension.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}

				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(673, 125, 100, 32);
					panel_Pallet_Extension.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());

					{
						jLabel_WeekNumber = new JLabel4j_std();
						jLabel_WeekNumber.setBounds(205, 92, 101, 21);
						panel_Pallet_Extension.add(jLabel_WeekNumber);
						jLabel_WeekNumber.setText(lang.get("lbl_Week_Number"));
						jLabel_WeekNumber.setHorizontalAlignment(SwingConstants.TRAILING);
					}
					{
						jTextFieldWeekNumber = new JTextField4j();
						jTextFieldWeekNumber.setEditable(false);
						jTextFieldWeekNumber.setBounds(315, 92, 56, 21);
						panel_Pallet_Extension.add(jTextFieldWeekNumber);
						jTextFieldWeekNumber.setHorizontalAlignment(SwingConstants.CENTER);
					}

					JLabel4j_std jLabel_DayOfWeek = new JLabel4j_std();
					jLabel_DayOfWeek.setBounds(294, 92, 119, 21);
					panel_Pallet_Extension.add(jLabel_DayOfWeek);
					jLabel_DayOfWeek.setText(lang.get("lbl_Day"));
					jLabel_DayOfWeek.setHorizontalAlignment(SwingConstants.TRAILING);

					jTextFieldDayOfWeek.setBounds(427, 92, 80, 21);
					panel_Pallet_Extension.add(jTextFieldDayOfWeek);
					jTextFieldDayOfWeek.setText("");
					jTextFieldDayOfWeek.setHorizontalAlignment(SwingConstants.CENTER);

					JLabel4j_std jLabel_Month = new JLabel4j_std();
					jLabel_Month.setBounds(517, 92, 56, 21);
					panel_Pallet_Extension.add(jLabel_Month);
					jLabel_Month.setText(lang.get("lbl_Month"));
					jLabel_Month.setHorizontalAlignment(SwingConstants.TRAILING);

					jTextFieldMonth.setBounds(579, 92, 80, 21);
					panel_Pallet_Extension.add(jTextFieldMonth);
					jTextFieldMonth.setText("");
					jTextFieldMonth.setHorizontalAlignment(SwingConstants.CENTER);

					JLabel4j_std jLabel_Year = new JLabel4j_std();
					jLabel_Year.setBounds(666, 92, 46, 21);
					panel_Pallet_Extension.add(jLabel_Year);
					jLabel_Year.setText(lang.get("lbl_Year"));
					jLabel_Year.setHorizontalAlignment(SwingConstants.TRAILING);

					jTextFieldYear.setBounds(721, 92, 46, 21);
					panel_Pallet_Extension.add(jTextFieldYear);
					jTextFieldYear.setText("");
					jTextFieldYear.setHorizontalAlignment(SwingConstants.CENTER);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}

				JPanel panel_SampleResults = new JPanel();
				panel_SampleResults.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Results", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
				panel_SampleResults.setBounds(5, 368, 978, 162);
				jDesktopPane1.add(panel_SampleResults);
				panel_SampleResults.setLayout(null);

				scrollPane1.setBounds(0, 20, 975, 142);
				scrollPane1.getViewport().setBackground(Common.color_tablebackground);
				panel_SampleResults.add(scrollPane1);

				jTable1 = new JTable4j();
				scrollPane1.setColumnHeaderView(jTable1);

				jTable1.setToolTipText(lang.get("lbl_Table_Hint"));
				jTable1.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent evt)
					{

						if (evt.getClickCount() == 2)
						{
							if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_EDIT") == true)
							{
								editSample();
							}
						}
					}
				});

				{
					final JPopupMenu popupMenu = new JPopupMenu();
					addPopup(jTable1, popupMenu);

					{
						final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
						newItemMenuItem.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								addSample();
							}
						});
						newItemMenuItem.setText(lang.get("btn_Add"));
						popupMenu.add(newItemMenuItem);
					}

					{
						final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit_16x16);
						newItemMenuItem.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								editSample();
							}
						});
						newItemMenuItem.setText(lang.get("btn_Edit"));
						popupMenu.add(newItemMenuItem);
					}

					{
						final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete_16x16);
						newItemMenuItem.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								deleteSample();
							}
						});
						newItemMenuItem.setText(lang.get("btn_Delete"));
						popupMenu.add(newItemMenuItem);
					}

					{
						final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh_16x16);
						newItemMenuItem.addActionListener(new ActionListener()
						{
							public void actionPerformed(final ActionEvent e)
							{
								refreshSamples();
							}
						});
						newItemMenuItem.setText(lang.get("btn_Refresh"));
						popupMenu.add(newItemMenuItem);
					}

				}

				jTable1.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent evt)
					{
						if (evt.getClickCount() == 2)
						{
							editSample();
						}
					}
				});

				JPanel panelShiftButtons = new JPanel();
				panelShiftButtons.setBackground(Common.color_app_window);
				panelShiftButtons.setBounds(5, 533, 118, 30);
				jDesktopPane1.add(panelShiftButtons);
				panelShiftButtons.setLayout(null);

				button4jSampleAdd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						addSample();
					}
				});
				button4jSampleAdd.setBounds(0, 0, 28, 28);
				panelShiftButtons.add(button4jSampleAdd);
				button4jSampleAdd.setFont(Common.font_std);
				button4jSampleAdd.setMnemonic('0');

				button4jSampleDelete.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						promptToSaveChanges();
						deleteSample();
					}
				});
				button4jSampleDelete.setBounds(58, 0, 28, 28);
				panelShiftButtons.add(button4jSampleDelete);
				button4jSampleDelete.setMnemonic('0');
				button4jSampleDelete.setFont(Common.font_std);

				button4jSampleEdit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						editSample();
					}
				});
				button4jSampleEdit.setBounds(29, 0, 28, 28);
				panelShiftButtons.add(button4jSampleEdit);
				button4jSampleEdit.setMnemonic('0');
				button4jSampleEdit.setFont(Common.font_std);

				button4jSampleRefresh.setBounds(87, 0, 28, 28);
				panelShiftButtons.add(button4jSampleRefresh);
				button4jSampleRefresh.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						refreshSamples();
					}
				});
				button4jSampleRefresh.setMnemonic('0');
				button4jSampleRefresh.setFont(Common.font_std);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void addSample()
	{
		if (validateInput())
		{
			promptToSaveChanges();
			JLaunchMenu.runForm("FRM_PAL_SAMPLE_EDIT", lsscc, Long.valueOf("-1"));
		}

	}

	private void editSample()
	{
		if (validateInput())
		{
			int row = jTable1.getSelectedRow();
			if (row >= 0)
			{
				Long seq = Long.valueOf(jTable1.getValueAt(row, JDBPalletSampleTableModel.SampleSequence_Col).toString());

				promptToSaveChanges();
				JLaunchMenu.runForm("FRM_PAL_SAMPLE_EDIT", lsscc, seq);
			}
		}
	}

	private void deleteSample()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			promptToSaveChanges();

			Long seq = Long.valueOf(jTable1.getValueAt(row, JDBPalletSampleTableModel.SampleSequence_Col).toString());

			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Sample_Delete") + " [" + seq.toString() + "]", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				palsamp.setSSCC(lsscc);
				palsamp.setSampleSequence(seq);
				palsamp.delete();
				refreshSamples();
			}
		}
	}

	private void notifyEmail()
	{
		int question = JOptionPane.showConfirmDialog(Common.mainForm, "Send Email ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
		if (question == 0)
		{
			JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
			pallet.SortNotification(lsscc);
			JUtility.errorBeep();
			JOptionPane.showMessageDialog(this,"Notification Requested.");
		}

	}
	
	private void print()
	{
		HashMap<String, Object> selectedSSCC = new HashMap<String, Object>();
		selectedSSCC.put("P_SSCC", lsscc);
		JLaunchReport.runReport("RPT_NOTIFY_SORTING", selectedSSCC, "", null, "");
	}

	private void save()
	{
		jStatusText.setText("");
		if (validateInput())
		{
			palext.setSSCC(lsscc);

			if (checkBoxFirstCaseInput.isSelected() && checkBoxLastCaseInput.isSelected())
			{
				if (firstCaseTime.getDate().compareTo(lastCaseTime.getDate()) > 0)
				{
					Date temp1 = firstCaseTime.getDate();
					Date temp2 = lastCaseTime.getDate();

					firstCaseTime.setDate(temp2);
					lastCaseTime.setDate(temp1);

					jStatusText.setText("First and Last timestamps were in the wrong order and have been corrected.");
				}
			}

			palext.setFirstCaseInput(checkBoxFirstCaseInput.isSelected());
			palext.setFirstCaseTime(JUtility.getTimestampFromDate(firstCaseTime.getDate()));
			palext.setLastCaseInput(checkBoxLastCaseInput.isSelected());
			palext.setLastCaseTime(JUtility.getTimestampFromDate(lastCaseTime.getDate()));
			palext.setShiftID(jTextFieldShift.getText());
			palext.setIncident_Ref(jTextFieldIncident_Ref.getText());
			palext.setSupplierID1(jTextFieldSupplier_ID1.getText());
			palext.setLocation(jTextFieldSampleLocation.getText());
			palext.setProductGroup(jTextFieldProductGroup.getText());
			palext.setContainerCode(jTextFieldContainerCode.getText());
			palext.setWeekOfYear(JUtility.getTimestampFromDate(productionDate.getDate()));
			palext.setPDay(JUtility.getDay(JUtility.getTimestampFromDate(productionDate.getDate())));
			palext.setPMonth(JUtility.getMonth(JUtility.getTimestampFromDate(productionDate.getDate())));
			palext.setPMonth(JUtility.getMonth(JUtility.getTimestampFromDate(productionDate.getDate())));
			palext.setPYear(JUtility.getYear(JUtility.getTimestampFromDate(productionDate.getDate())));
			palext.setPalletQuantity(new BigDecimal(jFormattedTextFieldStartQuantity.getValue().toString()));

			if (palext.isValid() == false)
			{
				palext.create();

			}
			palext.update();

			palletExtensionModified(false);
		}
	}

	private void promptToSaveChanges()
	{
		if (jButtonSave.isEnabled())
		{
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Confirm_Save"), lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)

			{
				save();
			}
		}
	}

	private boolean validateInput()
	{
		boolean valid = true;

		if (checkBoxFirstCaseInput.isSelected() == false)
		{
			valid = false;
			checkBoxFirstCaseInput.setBackground(error_color);
		}
		else
		{
			checkBoxFirstCaseInput.setBackground(light_grey);

			if (checkBoxLastCaseInput.isSelected() == false)
			{
				valid = false;
				checkBoxLastCaseInput.setBackground(error_color);
			}
			else
			{
				checkBoxLastCaseInput.setBackground(light_grey);
				if (jTextFieldShift.getText().equals(""))
				{
					valid = false;
					jTextFieldShift.setBackground(error_color);
				}
				else
				{
					jTextFieldShift.setBackground(null);
					if (jTextFieldSupplier_ID1.getText().equals(""))
					{
						valid = false;
						jTextFieldSupplier_ID1.setBackground(error_color);
					}
					else
					{
						jTextFieldSupplier_ID1.setBackground(null);
						if (jTextFieldSampleLocation.getText().equals(""))
						{
							valid = false;
							jTextFieldSampleLocation.setBackground(error_color);
						}
						else
						{
							jTextFieldSampleLocation.setBackground(null);
						}
					}
				}
			}
		}

		if (valid == false)
		{
			JUtility.errorBeep();
			jButtonNotifyEmail.setEnabled(false);
			jButtonPrint.setEnabled(false);
		}
		else
		{
			jButtonNotifyEmail.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_NOTIFY_SORTING"));
			jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_NOTIFY_SORTING"));
		}

		return valid;
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
