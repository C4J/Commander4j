package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFramePalletSplit.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.bar.JLabelPrint;
import com.commander4j.bom.JDBBomList;
import com.commander4j.bom.JDBViewBom;
import com.commander4j.bom.JDBViewBomIssueTableModel;
import com.commander4j.bom.JDBViewBomRecord;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBProcessOrderResource;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBQuery2;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JComboBoxPODevices4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.print.JPrintDevice;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFramePalletSplit allows a user to split a pallet into 2 pallets. You
 * are prompted to enter a quantity of cases to remove from the current SSCC and
 * this is automatically added to a new SSCC. The program also allows you to
 * reprint labels for the old and new SSCC's
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFramePalletSplit.jpg" >
 *
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.db.JDBPalletHistory JDBPalletHistory
 */
public class JInternalFramePalletIssue extends javax.swing.JInternalFrame
{

	private static final long serialVersionUID = 1;

	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonIssue;

	private JPanel4j panelOrder = new JPanel4j();
	private JPanel4j panelIssue = new JPanel4j();
	private JPanel4j panelBOM = new JPanel4j();
	private JPanel4j panelHistory = new JPanel4j();

	private JCheckBox4j checkBoxIncHeaderText = new JCheckBox4j();
	private JCheckBox4j checkBoxRePrint = new JCheckBox4j("");
	private JCheckBox4j jCheckBoxAutoPreview;

	private JComboBoxPODevices4j comboBoxPrintQueue;
	private JComboBox4j<String> comboBoxStage;

	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDBLocation locn = new JDBLocation(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder po = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrderResource res = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
	private JDBPallet pal = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JDBBomList bomListsDB = new JDBBomList(Common.selectedHostID, Common.sessionID);
	private JDBViewBom viewBOM = new JDBViewBom(Common.selectedHostID, Common.sessionID);

	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_status jStatusBar = new JLabel4j_status();
	private JLabel4j_std labelCopies = new JLabel4j_std();
	private JLabel4j_std labelNewSSCCQuantity;
	private JLabel4j_std lbl_BatchExpiry;
	private JLabel4j_std labelUOM;
	private JLabel4j_std labelPreview;
	private JLabel4j_std labelSSCC;
	private JLabel4j_std labelMaterial;
	private JLabel4j_std labelBatch;
	private JLabel4j_std labelPalletStatus;
	private JLabel4j_std labelBatchStatus;
	private JLabel4j_std labelSSCCLocation;

	private JLabelPrint lab = new JLabelPrint(Common.selectedHostID, Common.sessionID);
	private JQuantityInput jFormattedTextFieldIssueQuantity;
	private JSpinner4j jSpinnerCopies = new JSpinner4j();
	private SpinnerNumberModel copiesnumbermodel;

	private JTextField4j jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
	private JTextField4j jTextFieldProcessOrderDescription = new JTextField4j();
	private JTextField4j jTextFieldRecipe = new JTextField4j();
	private JTextField4j jTextFieldRecipeVersion = new JTextField4j();
	private JTextField4j jTextFieldOrderStatus = new JTextField4j();
	private JTextField4j jTextFieldProcessOrderResourceDescription = new JTextField4j();
	private JTextField4j jTextFieldRequiredResource = new JTextField4j(JDBProcessOrder.field_required_resource);
	private JTextField4j jTextFieldSSCC;
	private JTextField4j jTextFieldMaterial;
	private JTextField4j jTextFieldBatch;
	private JTextField4j jTextFieldUOM;
	private JTextField4j jTextFieldBatchStatus;
	private JTextField4j jTextFieldSSCCLocation;
	private JTextField4j jTextFieldLocationBarcode = new JTextField4j(JDBLocation.field_barcode_id);
	private JTextField4j jTextFieldLocationID = new JTextField4j(JDBLocation.field_location_id);
	private JTextField4j jTextFieldPalletStatus;

	private JTable4j jTable1;

	private JScrollPane4j jScrollPane1;

	private JDateControl fld_BatchExpiry;

	private String defaultlabel = "";
	private String lsscc;

	private BigDecimal zero = new BigDecimal(0);

	private PreparedStatement listStatement;
	private JScrollPane4j scrollPane;

	private DefaultListModel<IssueHistoryEntry> historyModel = new DefaultListModel<IssueHistoryEntry>();
	private JList4j<IssueHistoryEntry> listHistory;

	public JInternalFramePalletIssue()
	{
		super();

		addInternalFrameListener(new InternalFrameAdapter()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				closeListStatement();
			}
		});

		this.setTitle("Issue Pallet");

		int copies = Integer.valueOf(ctrl.getKeyValueWithDefault("DEFAULT_LABELS_TO_PRINT", "2", "Default No of Labels to print"));
		copiesnumbermodel = new SpinnerNumberModel(copies, 1, 100, 1);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PAL_ISSUE"));

		lsscc = "";
		jTextFieldSSCC.setText(lsscc);

		getPallet();

		setFocusPosition(jTextFieldRequiredResource);
	}

	private void getProcessOrder(String order)
	{
		if (po.getProcessOrderProperties(order))
		{
			jTextFieldProcessOrderDescription.setText(po.getDescription());
			jTextFieldRecipe.setText(po.getRecipe());
			jTextFieldRecipeVersion.setText(po.getRecipeVersion());
			jTextFieldOrderStatus.setText(po.getStatus());
			setFocusPosition(jTextFieldSSCC);
			displayBOM(po.getRecipe(), po.getRecipeVersion(), comboBoxStage.getSelectedItem().toString());
		}
		else
		{
			clearIssueFields();
			jTextFieldRecipe.setText("");
			jTextFieldRecipeVersion.setText("");
			jTextFieldOrderStatus.setText("");
			jTextFieldProcessOrderDescription.setText("");
			displayBOM("-1", "-1", "-1");
			clearIssueFields();
		}

	}

	private void getProcessOrderResource(String resource)
	{
		if (res.getResourceProperties(resource))
		{
			jTextFieldProcessOrderResourceDescription.setText(res.getDescription());
			setFocusPosition(jTextFieldProcessOrder);
		}
		else
		{
			jTextFieldProcessOrderResourceDescription.setText("");
		}
	}

	private void setFocusPosition(Component field)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				field.requestFocus();

				if (field.getClass().getName().equals("com.commander4j.gui.JQuantityInput"))
					((JQuantityInput) field).setCaretPosition(((JQuantityInput) field).getText().length());

				if (field.getClass().getName().equals("com.commander4j.gui.JTextField4j"))
					((JTextField4j) field).setCaretPosition(((JTextField4j) field).getText().length());
			}
		});
	}

	private void getPallet()
	{
		String sscc = JUtility.replaceNullStringwithBlank(jTextFieldSSCC.getText());
		String order = "";

		boolean valid = false;

		jStatusBar.setText("");

		if (sscc.length() == 18)
		{
			if (pal.getPalletProperties(sscc))
			{
				jFormattedTextFieldIssueQuantity.setValue(pal.getQuantity());
				jTextFieldMaterial.setText(pal.getMaterial());
				jTextFieldBatch.setText(pal.getBatchNumber());
				jTextFieldPalletStatus.setText(pal.getStatus());
				jTextFieldBatchStatus.setText(pal.getMaterialBatchStatus());
				jTextFieldUOM.setText(pal.getUom());
				jTextFieldSSCCLocation.setText(pal.getLocationID());
				fld_BatchExpiry.setDate(pal.getBatchExpiry());

				jStatusBar.setText(sscc + " retrieved.");

				order = pal.getProcessOrder();

				valid = validateSSCC();

				comboBoxPrintQueue.refreshData("RPT_PALLET_LABEL", pal.getProcessOrder());

			}
			else
			{
				if (sscc.equals("") == false)
				{
					jStatusBar.setText(pal.getErrorMessage());
				}
			}
		}
		else
		{
			jStatusBar.setText("");
			jFormattedTextFieldIssueQuantity.setValue(0);
			jTextFieldMaterial.setText("");
			jTextFieldBatch.setText("");
			jTextFieldPalletStatus.setText("");
			jTextFieldBatchStatus.setText("");
			jTextFieldUOM.setText("");
			jTextFieldSSCCLocation.setText("");
			clearIssueFields();
		}

		if (valid)
		{
			defaultlabel = lab.getPalletLabelReportName(order);

			if (mod.getModuleProperties(defaultlabel))
			{
				if (mod.getReportType().equals("Label"))
				{
					jCheckBoxAutoPreview.setSelected(false);
					jCheckBoxAutoPreview.setEnabled(false);

					jSpinnerCopies.setVisible(true);
					labelCopies.setVisible(true);
				}
				else
				{
					jCheckBoxAutoPreview.setSelected(true);
					jCheckBoxAutoPreview.setEnabled(true);
					jSpinnerCopies.setVisible(false);
					labelCopies.setVisible(false);
				}
			}
		}

	}

	private boolean validateSSCC()
	{
		boolean result = false;

		// Is it a valid SSCC
		boolean ssccValid = true;
		boolean materialforBomValid = true;
		boolean barcodeForMaterialValid = true;
		boolean locationValid = true;
		boolean quantityValid = true;
		boolean palletStatusValid = true;
		boolean batchStatusValid = true;

		int ssccLen = jTextFieldSSCC.getText().length();

		if (ssccLen == 18)
		{
			ssccValid = pal.getPalletProperties(jTextFieldSSCC.getText());

			if (ssccValid)
			{
				materialforBomValid = viewBOM.isMaterialValidForBOM(jTextFieldRecipe.getText(), jTextFieldRecipeVersion.getText(), comboBoxStage.getSelectedItem().toString(), "input", jTextFieldMaterial.getText());

				if (materialforBomValid)
				{
					barcodeForMaterialValid = viewBOM.isValidMaterialForLocation(jTextFieldRecipe.getText(), jTextFieldRecipeVersion.getText(), comboBoxStage.getSelectedItem().toString(), "input", jTextFieldMaterial.getText(),
							jTextFieldLocationBarcode.getText());

					if (barcodeForMaterialValid)
					{
						String locationid = locn.getLocationIDfromBarcodeID(jTextFieldLocationBarcode.getText());

						locationValid = locn.getLocationProperties(locationid);

						if (locationValid)
						{
							palletStatusValid = locn.isPalletStatusValidforLocation(jTextFieldPalletStatus.getText());

							if (palletStatusValid)
							{
								batchStatusValid = locn.isBatchStatusValidforLocation(jTextFieldBatchStatus.getText());

								if (batchStatusValid)
								{
									quantityValid = !(jFormattedTextFieldIssueQuantity.getQuantity().compareTo(zero) <= 0);

									if (quantityValid)
									{
										quantityValid = !(jFormattedTextFieldIssueQuantity.getQuantity().compareTo(pal.getQuantity()) == 1);
									}
								}
							}
						}
					}
				}
			}
		}

		jTextFieldSSCC.inError(!ssccValid);
		jTextFieldMaterial.inError(!materialforBomValid);
		jTextFieldLocationID.inError(!locationValid);
		jTextFieldLocationBarcode.inError(!barcodeForMaterialValid);
		jTextFieldPalletStatus.inError(!palletStatusValid);
		jTextFieldBatchStatus.inError(!batchStatusValid);
		jFormattedTextFieldIssueQuantity.inError(!quantityValid);

		result = (ssccLen == 18) && ssccValid && materialforBomValid && barcodeForMaterialValid && locationValid && palletStatusValid && batchStatusValid && quantityValid;

		// The Issue button is only live once every check above has passed - the
		// failing fields are flagged with inError() so the operator can see
		// why.

		jButtonIssue.setEnabled(result);

		return result;
	}

	private void clearIssueFields()
	{
		jStatusBar.setText("");
		jFormattedTextFieldIssueQuantity.setValue(0);
		jTextFieldMaterial.setText("");
		jTextFieldBatch.setText("");
		jTextFieldPalletStatus.setText("");
		jTextFieldBatchStatus.setText("");
		jTextFieldUOM.setText("");
		jTextFieldSSCCLocation.setText("");
		jTextFieldLocationBarcode.setText("");
		jTextFieldLocationID.setText("");
	}

	private void initGUI()
	{
		try
		{
			// ===== Frame =====

			this.setPreferredSize(new java.awt.Dimension(1003, 536));
			this.setBounds(0, 0, 1003, 646);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);

			jDesktopPane1 = new JDesktopPane4j();
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));
			jDesktopPane1.setLayout(null);
			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);

			panelHistory.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "History", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			panelHistory.setBounds(800, 12, 183, 566);
			jDesktopPane1.add(panelHistory);
			panelHistory.setLayout(new BorderLayout(0, 0));

			scrollPane = new JScrollPane4j(JScrollPane4j.List);
			panelHistory.add(scrollPane, BorderLayout.CENTER);

			listHistory = new JList4j<IssueHistoryEntry>(historyModel);
			listHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listHistory.setCellRenderer(new IssueHistoryRenderer());
			scrollPane.setViewportView(listHistory);

			// ===== Panel : Issue To =====

			panelOrder.setLayout(null);
			panelOrder.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Order", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			panelOrder.setBounds(12, 12, 782, 142);
			jDesktopPane1.add(panelOrder);

			// Issue To - Row 1 : Required Resource

			JLabel4j_std jLabel_IssueToResource = new JLabel4j_std();
			jLabel_IssueToResource.setText(lang.get("lbl_Issue_Resource"));
			jLabel_IssueToResource.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_IssueToResource.setBounds(12, 20, 137, 22);
			panelOrder.add(jLabel_IssueToResource);

			jTextFieldRequiredResource.setBounds(160, 20, 114, 22);
			panelOrder.add(jTextFieldRequiredResource);
			jTextFieldRequiredResource.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					getProcessOrderResource(jTextFieldRequiredResource.getText());
				}
			});

			JButton4j btnLookupResource = new JButton4j(Common.icon_lookup_16x16);
			btnLookupResource.setBounds(274, 20, 22, 22);
			panelOrder.add(btnLookupResource);
			btnLookupResource.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (JLaunchLookup.resources())
					{
						jTextFieldRequiredResource.setText(JLaunchLookup.dlgResult);
						getProcessOrderResource(JLaunchLookup.dlgResult);
					}
				}
			});

			jTextFieldProcessOrderResourceDescription.setEditable(false);
			jTextFieldProcessOrderResourceDescription.setBounds(309, 20, 461, 22);
			panelOrder.add(jTextFieldProcessOrderResourceDescription);

			// Issue To - Row 2 : Process Order

			JLabel4j_std jLabel_IssueToProcessOrder = new JLabel4j_std();
			jLabel_IssueToProcessOrder.setText(lang.get("lbl_Issue_Order"));
			jLabel_IssueToProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_IssueToProcessOrder.setBounds(12, 50, 137, 22);
			panelOrder.add(jLabel_IssueToProcessOrder);

			jTextFieldProcessOrder.setBounds(160, 50, 114, 22);
			panelOrder.add(jTextFieldProcessOrder);
			jTextFieldProcessOrder.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					getProcessOrder(jTextFieldProcessOrder.getText());
				}
			});

			JButton4j btnLookupProcessOrder = new JButton4j(Common.icon_lookup_16x16);
			btnLookupProcessOrder.setBounds(274, 50, 22, 22);
			panelOrder.add(btnLookupProcessOrder);
			btnLookupProcessOrder.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.setSearchValue("status", "Ready");
					JLaunchLookup.setSearchValue("required_resource", jTextFieldRequiredResource.getText());

					if (JLaunchLookup.processOrders())
					{
						jTextFieldProcessOrder.setText(JLaunchLookup.dlgResult);
						getProcessOrder(JLaunchLookup.dlgResult);
					}
				}
			});

			jTextFieldProcessOrderDescription.setEditable(false);
			jTextFieldProcessOrderDescription.setBounds(309, 50, 461, 22);
			panelOrder.add(jTextFieldProcessOrderDescription);

			// Issue To - Row 3 : Recipe / Version

			JLabel4j_std jLabel_Recipe = new JLabel4j_std();
			jLabel_Recipe.setText(lang.get("lbl_Process_Order_Recipe"));
			jLabel_Recipe.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Recipe.setBounds(12, 80, 137, 22);
			panelOrder.add(jLabel_Recipe);

			JLabel4j_std jLabel_OrderStatus = new JLabel4j_std();
			jLabel_OrderStatus.setText(lang.get("lbl_Process_Order_Status"));
			jLabel_OrderStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_OrderStatus.setBounds(372, 80, 77, 22);
			panelOrder.add(jLabel_OrderStatus);

			JLabel4j_std jLabel_Stage = new JLabel4j_std();
			jLabel_Stage.setText(lang.get("lbl_Stage_Phase"));
			jLabel_Stage.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Stage.setBounds(12, 108, 137, 22);
			panelOrder.add(jLabel_Stage);

			LinkedList<String> listValues = bomListsDB.getListItems("stage");
			String[] valarray = listValues.toArray(new String[listValues.size()]);
			DefaultComboBoxModel<String> stages = new DefaultComboBoxModel<String>(valarray);

			comboBoxStage = new JComboBox4j<String>();
			comboBoxStage.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					getProcessOrder(jTextFieldProcessOrder.getText());
				}
			});
			comboBoxStage.setBounds(160, 110, 70, 20);
			comboBoxStage.setMaximumSize(new Dimension(70, 20));
			comboBoxStage.setModel(stages);
			comboBoxStage.setFont(Common.font_input);
			panelOrder.add(comboBoxStage);

			jTextFieldRecipe.setEnabled(false);
			jTextFieldRecipe.setBounds(160, 80, 114, 22);
			panelOrder.add(jTextFieldRecipe);

			JLabel4j_std jLabel_BOM_Version = new JLabel4j_std();
			jLabel_BOM_Version.setText("/");
			jLabel_BOM_Version.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel_BOM_Version.setBounds(275, 80, 35, 22);
			panelOrder.add(jLabel_BOM_Version);

			jTextFieldRecipeVersion.setEnabled(false);
			jTextFieldRecipeVersion.setBounds(309, 80, 54, 22);
			panelOrder.add(jTextFieldRecipeVersion);

			jTextFieldOrderStatus.setEnabled(false);
			jTextFieldOrderStatus.setBounds(460, 80, 92, 22);
			panelOrder.add(jTextFieldOrderStatus);

			// ===== Panel : Order BOM =====

			panelBOM.setLayout(new BorderLayout(0, 0));
			panelBOM.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Bill of Materials / Recipe", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			panelBOM.setBounds(12, 155, 782, 193);
			jDesktopPane1.add(panelBOM);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.Table);
			jScrollPane1.setBounds(0, 0, 100, 100);
			panelBOM.add(jScrollPane1);

			TableModel jTable1Model = new DefaultTableModel(new String[][]
			{
					{ "One", "Two" },
					{ "Three", "Four" } }, new String[]
			{ "Column 1", "Column 2" });

			jTable1 = new JTable4j();
			jTable1.setModel(jTable1Model);
			jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
			jTable1.setToolTipText(lang.get("lbl_Table_Hint"));
			jScrollPane1.setViewportView(jTable1);

			// The selection model survives setModel() so this only needs adding
			// once.

			jTable1.getSelectionModel().addListSelectionListener(e -> {
				if (e.getValueIsAdjusting())
					return; // ignore the intermediate events
				int row = jTable1.getSelectedRow();
				clearIssueFields();
				jTextFieldSSCC.setText("");
				if (row != -1)
				{
					// convert if the table is sorted/filtered
					defaultValuesFromTable();
					setFocusPosition(jTextFieldSSCC);
					validateSSCC();
				}

			});

			// ===== Panel : Issue SSCC =====

			panelIssue.setLayout(null);
			panelIssue.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Issue", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			panelIssue.setBounds(12, 353, 782, 121);
			jDesktopPane1.add(panelIssue);

			labelSSCC = new JLabel4j_std();
			labelSSCC.setText(lang.get("lbl_Pallet_SSCC"));
			labelSSCC.setBounds(10, 15, 134, 22);
			panelIssue.add(labelSSCC);

			labelMaterial = new JLabel4j_std();
			labelMaterial.setText(lang.get("lbl_Material"));
			labelMaterial.setBounds(180, 15, 134, 22);
			panelIssue.add(labelMaterial);

			labelBatch = new JLabel4j_std();
			labelBatch.setText(lang.get("lbl_Batch"));
			labelBatch.setBounds(485, 15, 55, 22);
			panelIssue.add(labelBatch);

			labelPalletStatus = new JLabel4j_std();
			labelPalletStatus.setText(lang.get("lbl_Pallet_Status"));
			labelPalletStatus.setBounds(330, 15, 134, 22);
			panelIssue.add(labelPalletStatus);

			labelBatchStatus = new JLabel4j_std();
			labelBatchStatus.setText(lang.get("lbl_Batch_Status"));
			labelBatchStatus.setBounds(635, 15, 134, 22);
			panelIssue.add(labelBatchStatus);

			labelSSCCLocation = new JLabel4j_std();
			labelSSCCLocation.setText(lang.get("lbl_Location_ID"));
			labelSSCCLocation.setBounds(635, 64, 134, 22);
			panelIssue.add(labelSSCCLocation);

			jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
			jTextFieldSSCC.setBounds(10, 37, 134, 22);
			panelIssue.add(jTextFieldSSCC);
			jTextFieldSSCC.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent arg0)
				{
					getPallet();
					validateSSCC();
				}
			});

			JButton4j jButtonLookupSSCC = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupSSCC.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.setSearchValue("material", jTextFieldMaterial.getText());

					if (JLaunchLookup.pallets())
					{
						jTextFieldSSCC.setText(JLaunchLookup.dlgResult);

						getPallet();
						if (validateSSCC())
						{
							setFocusPosition(jFormattedTextFieldIssueQuantity);
						}

					}
				}
			});
			jButtonLookupSSCC.setBounds(143, 37, 22, 22);
			panelIssue.add(jButtonLookupSSCC);

			jTextFieldMaterial = new JTextField4j();
			jTextFieldMaterial.setEnabled(false);
			jTextFieldMaterial.setBounds(180, 37, 134, 22);
			panelIssue.add(jTextFieldMaterial);

			jTextFieldBatch = new JTextField4j();
			jTextFieldBatch.setEnabled(false);
			jTextFieldBatch.setBounds(485, 37, 134, 22);
			panelIssue.add(jTextFieldBatch);

			jTextFieldUOM = new JTextField4j();
			jTextFieldUOM.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldUOM.setEnabled(false);
			jTextFieldUOM.setBounds(425, 87, 42, 22);
			panelIssue.add(jTextFieldUOM);

			fld_BatchExpiry = new JDateControl();
			fld_BatchExpiry.setEnabled(false);
			fld_BatchExpiry.setDisplayMode(JDateControl.mode_disable_visible);
			fld_BatchExpiry.setBounds(485, 87, 120, 22);
			panelIssue.add(fld_BatchExpiry);

			jTextFieldPalletStatus = new JTextField4j();
			jTextFieldPalletStatus.setEnabled(false);
			jTextFieldPalletStatus.setBounds(330, 37, 134, 22);
			panelIssue.add(jTextFieldPalletStatus);

			jTextFieldBatchStatus = new JTextField4j();
			jTextFieldBatchStatus.setEnabled(false);
			jTextFieldBatchStatus.setBounds(635, 37, 134, 22);
			panelIssue.add(jTextFieldBatchStatus);

			jTextFieldSSCCLocation = new JTextField4j();
			jTextFieldSSCCLocation.setEnabled(false);
			jTextFieldSSCCLocation.setBounds(635, 87, 134, 22);
			panelIssue.add(jTextFieldSSCCLocation);

			labelNewSSCCQuantity = new JLabel4j_std();
			labelNewSSCCQuantity.setText(lang.get("lbl_Issue_Quantity"));
			labelNewSSCCQuantity.setBounds(330, 64, 102, 22);
			panelIssue.add(labelNewSSCCQuantity);

			lbl_BatchExpiry = new JLabel4j_std();
			lbl_BatchExpiry.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
			lbl_BatchExpiry.setBounds(485, 64, 133, 22);
			panelIssue.add(lbl_BatchExpiry);

			labelUOM = new JLabel4j_std();
			labelUOM.setText(lang.get("lbl_Pallet_UOM"));
			labelUOM.setBounds(425, 64, 42, 22);
			panelIssue.add(labelUOM);

			jFormattedTextFieldIssueQuantity = new JQuantityInput(new BigDecimal("0"));
			jFormattedTextFieldIssueQuantity.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					validateSSCC();
				}
			});
			jFormattedTextFieldIssueQuantity.setVerifyInputWhenFocusTarget(false);
			jFormattedTextFieldIssueQuantity.setBounds(330, 87, 91, 22);
			panelIssue.add(jFormattedTextFieldIssueQuantity);

			JLabel4j_std jLabel_LocationBarcode = new JLabel4j_std();
			jLabel_LocationBarcode.setText(lang.get("lbl_Barcode_ID"));
			jLabel_LocationBarcode.setBounds(180, 64, 91, 22);
			panelIssue.add(jLabel_LocationBarcode);

			JLabel4j_std jLabel_LocationID = new JLabel4j_std();
			jLabel_LocationID.setText(lang.get("lbl_Issue_Location"));
			jLabel_LocationID.setBounds(10, 64, 114, 22);
			panelIssue.add(jLabel_LocationID);

			jTextFieldLocationBarcode.setBounds(180, 87, 114, 22);
			panelIssue.add(jTextFieldLocationBarcode);

			JButton4j jButtonLookupLocationBarcode = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupLocationBarcode.setBounds(293, 87, 22, 22);
			panelIssue.add(jButtonLookupLocationBarcode);
			jButtonLookupLocationBarcode.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.setSearchValue("msg_pallet_issue", "Y");

					if (JLaunchLookup.locations_barcode())
					{
						jTextFieldLocationBarcode.setText(JLaunchLookup.dlgResult);
						jTextFieldLocationID.setText(getLocationIDfromBarcodeID(JLaunchLookup.dlgResult));
						validateSSCC();
					}
				}
			});

			jTextFieldLocationID.setBounds(10, 87, 114, 22);
			panelIssue.add(jTextFieldLocationID);

			JButton4j jButtonLookupLocationID = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupLocationID.setBounds(123, 87, 22, 22);
			panelIssue.add(jButtonLookupLocationID);
			jButtonLookupLocationID.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.setSearchValue("msg_pallet_issue", "Y");

					if (JLaunchLookup.locations_issue())
					{
						jTextFieldLocationID.setText(JLaunchLookup.dlgResult);
						jTextFieldLocationBarcode.setText(getLocationBarcodeFromLocationID(JLaunchLookup.dlgResult));
						validateSSCC();
					}
				}
			});
			checkBoxRePrint.setBounds(132, 482, 28, 22);
			jDesktopPane1.add(checkBoxRePrint);

			JLabel4j_std labelReprint = new JLabel4j_std();
			labelReprint.setText(lang.get("btn_Re_Print"));
			labelReprint.setHorizontalAlignment(SwingConstants.TRAILING);
			labelReprint.setBounds(14, 482, 111, 22);
			jDesktopPane1.add(labelReprint);

			JLabel4j_std labelHeader = new JLabel4j_std();
			labelHeader.setText(lang.get("lbl_Label_Header_Text"));
			labelHeader.setHorizontalAlignment(SwingConstants.TRAILING);
			labelHeader.setBounds(166, 482, 137, 22);
			jDesktopPane1.add(labelHeader);

			checkBoxIncHeaderText.setSelected(true);
			checkBoxIncHeaderText.setBackground(Color.WHITE);
			checkBoxIncHeaderText.setBounds(314, 482, 21, 22);
			jDesktopPane1.add(checkBoxIncHeaderText);

			labelPreview = new JLabel4j_std();
			labelPreview.setText(lang.get("lbl_Preview"));
			labelPreview.setHorizontalTextPosition(SwingConstants.CENTER);
			labelPreview.setHorizontalAlignment(SwingConstants.TRAILING);
			labelPreview.setBounds(343, 484, 98, 22);
			jDesktopPane1.add(labelPreview);

			jCheckBoxAutoPreview = new JCheckBox4j();
			jCheckBoxAutoPreview.setToolTipText("Auto SSCC");
			jCheckBoxAutoPreview.setSelected(true);

			jCheckBoxAutoPreview.setBackground(Color.WHITE);
			jCheckBoxAutoPreview.setBounds(446, 484, 21, 22);
			jDesktopPane1.add(jCheckBoxAutoPreview);

			labelCopies.setText(lang.get("lbl_Labels_Per_SSCC"));
			labelCopies.setHorizontalAlignment(SwingConstants.RIGHT);
			labelCopies.setBounds(465, 482, 150, 22);
			jDesktopPane1.add(labelCopies);

			jSpinnerCopies.setInputVerifier(null);
			jSpinnerCopies.setModel(copiesnumbermodel);
			JSpinner4j.NumberEditor nec2 = new JSpinner4j.NumberEditor(jSpinnerCopies);
			jSpinnerCopies.setEditor(nec2);
			jSpinnerCopies.setBounds(622, 482, 39, 22);
			jDesktopPane1.add(jSpinnerCopies);

			// ===== Print Queue row =====

			JLabel4j_std label_3 = new JLabel4j_std(lang.get("lbl_Print_Queue"));
			label_3.setHorizontalAlignment(SwingConstants.TRAILING);
			label_3.setBounds(6, 516, 115, 22);
			jDesktopPane1.add(label_3);

			comboBoxPrintQueue = new JComboBoxPODevices4j(Common.selectedHostID, Common.sessionID, "RPT_PALLET_LABEL", "");
			comboBoxPrintQueue.setBounds(132, 516, 634, 22);
			jDesktopPane1.add(comboBoxPrintQueue);

			// ===== Buttons =====

			jButtonIssue = new JButton4j(Common.icon_issue_16x16);
			jButtonIssue.setEnabled(false);
			jButtonIssue.setText(lang.get("btn_Issue"));
			jButtonIssue.setMnemonic(lang.getMnemonicChar());
			jButtonIssue.setBounds(240, 550, 111, 32);
			jDesktopPane1.add(jButtonIssue);
			jButtonIssue.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String sscc = jTextFieldSSCC.getText();

					if (validateSSCC())
					{
						if (pal.issuePallet(sscc, jTextFieldProcessOrder.getText(), jTextFieldLocationID.getText(), jFormattedTextFieldIssueQuantity.getQuantity()))
						{
							String message = "Issued " + sscc + " to " + jTextFieldProcessOrder.getText();

							jStatusBar.setText(message);
							jButtonIssue.setEnabled(false);
							addHistory(sscc, true, message);
							jTextFieldSSCC.setText("");
							clearIssueFields();
							defaultValuesFromTable();

							if (pal.getQuantity().compareTo(BigDecimal.ZERO) == 1)
							{

								JPrintDevice pq = (JPrintDevice) comboBoxPrintQueue.getSelectedItem();

								if (checkBoxRePrint.isSelected())
								{
									buildSQL1Record(sscc);
									JLaunchReport.runReport(defaultlabel, listStatement, jCheckBoxAutoPreview.isSelected(), pq, Integer.valueOf(jSpinnerCopies.getValue().toString()), checkBoxIncHeaderText.isSelected());
								}

							}

						}
						else
						{
							String reason = JUtility.replaceNullStringwithBlank(pal.getErrorMessage());

							if (reason.equals(""))
							{
								reason = "Issue of " + sscc + " failed.";
							}

							jStatusBar.setText(reason);
							addHistory(sscc, false, reason);
						}
					}
					else
					{
						// Only reachable if the pallet changed between the
						// button being enabled and the press - not logged.
						jStatusBar.setText(sscc + " is no longer valid for issue.");
					}

				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(354, 550, 111, 32);
			jDesktopPane1.add(jButtonHelp);

			jButtonCancel = new JButton4j(Common.icon_close_16x16);
			jButtonCancel.setText(lang.get("btn_Close"));
			jButtonCancel.setMnemonic(lang.getMnemonicChar());
			jButtonCancel.setBounds(467, 550, 111, 32);
			jDesktopPane1.add(jButtonCancel);
			jButtonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					closeListStatement();
					dispose();
				}
			});

			// ===== Status Bar =====

			jStatusBar.setBounds(0, 590, 983, 21);
			jDesktopPane1.add(jStatusBar);

			populateList();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void buildSQL1Record(String lsscc)
	{

		String temp = "";

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBPallet.selectWithExpiry");

		query.addText(temp);

		if (lsscc.equals("") == false)
		{
			query.addParamtoSQL("sscc = ", lsscc);
		}

		query.bindParams();
		query.applyRestriction(false, "none", 0);
		listStatement = query.getPreparedStatement();
	}

	private void displayBOM(String bom, String version, String stage)
	{
		buildSQL(bom, version, stage);
		populateList();
	}

	private void buildSQL(String bom, String version, String stage)
	{

		closeListStatement();

		listStatement = buildSQLr(bom, version, stage);
	}

	/**
	 * Release the BOM list statement. Safe to call more than once - the frame
	 * can be closed either with the Close button or the window close icon.
	 */
	private void closeListStatement()
	{
		if (listStatement != null)
		{
			JDBQuery2.closeStatement(listStatement);
			listStatement = null;
		}
	}

	private void defaultValuesFromTable()
	{
		int row = jTable1.getSelectedRow();
		if (row != -1)
		{
			// convert if the table is sorted/filtered:
			int modelRow = jTable1.convertRowIndexToModel(row);
			// ... use modelRow
			String loc = (String) jTable1.getModel().getValueAt(modelRow, JDBViewBomIssueTableModel.bom_location_col);
			String mat = (String) jTable1.getModel().getValueAt(modelRow, JDBViewBomIssueTableModel.bom_material_col);
			jTextFieldLocationBarcode.setText(loc);
			jTextFieldLocationID.setText(getLocationIDfromBarcodeID(loc));
			jTextFieldMaterial.setText(mat);
			jTextFieldSSCC.setText("");
		}
		else
		{
			clearIssueFields();
		}
	}

	private PreparedStatement buildSQLr(String bom, String version, String stage)
	{

		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);

		q2.applyWhat("*");

		q2.applyFrom("{schema}VIEW_BOM");

		q2.applyWhere("bom_id=", bom);

		q2.applyWhere("bom_version=", version);

		q2.applyWhere("input_output=", "input");

		q2.applyWhere("stage=", stage);

		q2.applySort("bom_id,bom_version,material", false);

		q2.applyRestriction(false, 0);

		q2.applySQL();

		result = q2.getPreparedStatement();
		return result;

	}

	private void populateList()
	{
		JDBViewBomRecord viewBom = new JDBViewBomRecord(Common.selectedHostID, Common.sessionID);

		JDBViewBomIssueTableModel viewBomTable = new JDBViewBomIssueTableModel(viewBom.getViewBomResultSet(listStatement));

		TableRowSorter<JDBViewBomIssueTableModel> sorter = new TableRowSorter<JDBViewBomIssueTableModel>(viewBomTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(viewBomTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable1.getTableHeader().setPreferredSize(new Dimension(jScrollPane1.getWidth(), 25));

		jTable1.getColumnModel().getColumn(JDBViewBomIssueTableModel.bom_material_col).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(JDBViewBomIssueTableModel.bom_material_type).setPreferredWidth(50);
		jTable1.getColumnModel().getColumn(JDBViewBomIssueTableModel.bom_quantity_col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBViewBomIssueTableModel.bom_location_col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBViewBomIssueTableModel.bom_description_col).setPreferredWidth(325);
		jTable1.getColumnModel().getColumn(JDBViewBomIssueTableModel.bom_uom_col).setPreferredWidth(80);
		jScrollPane1.repaint();
	}

	private String getLocationBarcodeFromLocationID(String locationID)
	{
		String result = "";

		if (locn.getLocationProperties(locationID))
		{
			result = locn.getBarcodeId();
		}
		else
		{
			result = "";
		}

		return result;
	}

	private String getLocationIDfromBarcodeID(String barcodeID)
	{
		String result = locn.getLocationIDfromBarcodeID(barcodeID);

		return result;
	}

	/**
	 * Add an SSCC to the History panel - green if the pallet was issued, red if
	 * the issue itself failed. Newest entry first. The detail is shown as the
	 * row tooltip alongside the time.
	 *
	 * Only the Issue button writes to the History panel, so every entry is the
	 * result of a deliberate key press - repeated attempts on the same SSCC are
	 * each recorded.
	 */
	private void addHistory(String sscc, boolean success, String detail)
	{
		if (sscc.length() != 18)
		{
			return;
		}

		historyModel.insertElementAt(new IssueHistoryEntry(sscc, success, detail), 0);

		listHistory.ensureIndexIsVisible(0);
	}

	/**
	 * A single row in the History panel.
	 */
	private class IssueHistoryEntry
	{
		private String sscc;
		private boolean success;
		private String detail;
		private String timestamp;

		private IssueHistoryEntry(String sscc, boolean success, String detail)
		{
			this.sscc = sscc;
			this.success = success;
			this.detail = JUtility.replaceNullStringwithBlank(detail);
			this.timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		}

		private boolean isSuccess()
		{
			return success;
		}

		private String getToolTip()
		{
			if (detail.equals(""))
			{
				return timestamp;
			}

			return timestamp + "  " + detail;
		}

		public String toString()
		{
			return sscc;
		}
	}

	/**
	 * Colours the History panel entries - the colour is kept when the row is
	 * selected, only the background changes.
	 */
	private class IssueHistoryRenderer extends DefaultListCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			setFont(Common.font_list);

			if (isSelected)
			{
				setBackground(Common.color_list_background_selected);
			}
			else
			{
				setBackground(Common.color_list_background);
			}

			if (value instanceof IssueHistoryEntry)
			{
				IssueHistoryEntry entry = (IssueHistoryEntry) value;

				if (entry.isSuccess())
				{
					setForeground(Common.color_checkbox_tick);
				}
				else
				{
					setForeground(Color.RED);
				}

				setToolTipText(entry.getToolTip());
			}

			return this;
		}
	}
}
