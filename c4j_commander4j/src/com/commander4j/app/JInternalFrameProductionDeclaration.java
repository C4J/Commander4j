package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameProductionDeclaration.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.logging.log4j.Logger;

import com.commander4j.bar.JEANBarcode;
import com.commander4j.bar.JLabelPrint;
import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLabelData;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBProcessOrderResource;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBoxPODevices4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JTitledBorder4j;
import com.commander4j.print.JPrintDevice;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameProductionDeclaration class is the main class for creating
 * and printing SSCC's. The form is driven by the operator selecting a Process
 * Order and optionally amending the quantity and batch number. This class
 * allows the operator to check an option which means that any SSCC's created
 * will automatically be confirmed and interfaced to external systems. If the
 * SSCC is not confirmed then this can be done via the form
 * JInternalFrameProductionConfirmation or via the Servlet
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameProductionDeclaration.jpg" >
 *
 * @see com.commander4j.app.JInternalFrameProductionConfirmation
 *      JInternalFrameProductionConfirmation
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.db.JDBPalletHistory JDBPalletHistory
 * @see com.commander4j.servlet.Process
 */

public class JInternalFrameProductionDeclaration extends JInternalFrame
{
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JInternalFrameProductionDeclaration.class);
	private BigDecimal caseDefaultQuantity;
	private BigDecimal fullPalletDefaultQuantity;
	private BigDecimal maxOverMakePercentage = new BigDecimal("0");
	private ClockListener clocklistener = new ClockListener();
	private JButton4j jButtonAssign;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPOLookup;
	private JButton4j jButtonReprint;
	private JButton4j jButtonSave;
	private JCalendarButton calendarButtonjSpinnerExpiryDate;
	private JCalendarButton calendarButtonjSpinnerProductionDate;
	private JCheckBox4j checkBoxIncHeaderText;
	private JCheckBox4j jCheckBoxAutoConfirm;
	private JCheckBox4j jCheckBoxAutoPreview;
	private JCheckBox4j jCheckBoxAutoPrint;
	private JCheckBox4j jCheckBoxAutoSSCC;
	private JCheckBox4j jCheckBoxBatch;
	private JCheckBox4j jCheckBoxExpiry;
	private JCheckBox4j jCheckBoxProductionDate;
	private JComboBoxPODevices4j comboBoxPrintQueue;
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBMaterialBatch materialbatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
	private JDBMaterialUom materialuom = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder processorder = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrderResource processorderResource = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
	private JDateControl jSpinnerDueDate;
	private JDateControl jSpinnerExpiryDate;
	private JDateControl jSpinnerProductionDate;
	private JDesktopPane4j jDesktopPane1;
	private JEANBarcode bc = new JEANBarcode(Common.selectedHostID, Common.sessionID);
	private JLabel4j_status jStatusText;
	private JLabel4j_std jLabel_BaseQuantity;
	private JLabel4j_std jLabel_BaseUOM;
	private JLabel4j_std jLabel_Batch;
	private JLabel4j_std jLabel_BatchExpiry;
	private JLabel4j_std jLabel_Confirmed;
	private JLabel4j_std jLabel_Description;
	private JLabel4j_std jLabel_DueDate;
	private JLabel4j_std jLabel_EAN;
	private JLabel4j_std jLabel_Header;
	private JLabel4j_std jLabel_LabelsPerSSCC = new JLabel4j_std();
	private JLabel4j_std jLabel_LegacyCode;
	private JLabel4j_std jLabel_Location;
	private JLabel4j_std jLabel_Material;
	private JLabel4j_std jLabel_NoOfSSCC;
	private JLabel4j_std jLabel_OrderDescription;
	private JLabel4j_std jLabel_OrderStatus;
	private JLabel4j_std jLabel_Preview;
	private JLabel4j_std jLabel_PrintLabel;
	private JLabel4j_std jLabel_PrintQueue;
	private JLabel4j_std jLabel_ProcessOrder;
	private JLabel4j_std jLabel_ProductionDate;
	private JLabel4j_std jLabel_ProductionEAN;
	private JLabel4j_std jLabel_Recipe;
	private JLabel4j_std jLabel_Rounding;
	private JLabel4j_std jLabel_SSCC;
	private JLabel4j_std jLabel_ShelfLife;
	private JLabel4j_std jLabel_ShelfLifeUOM;
	private JLabel4j_std jLabel_Variant;
	private JLabelPrint labelPrint = new JLabelPrint(Common.selectedHostID, Common.sessionID);
	private JPanel4j jPanelBatch;
	private JPanel4j jPanelMaterial;
	private JPanel4j jPanelPallet;
	private JPanel4j jPanelProcessOrder;
	private JQuantityInput jFormattedTextFieldBaseQuantity;
	private JQuantityInput jFormattedTextFieldProdQuantity;
	private JShelfLifeRoundingRule shelfliferoundingrule = new JShelfLifeRoundingRule();
	private JShelfLifeUom shelflifeuom = new JShelfLifeUom();
	private JSpinner4j jSpinnerCopies;
	private JSpinner4j jSpinnerQuantity;
	private JSpinner4j jSpinnerShelfLife;
	private JTextField4j jTextFieldBaseUOM;
	private JTextField4j jTextFieldBatch;
	private JTextField4j jTextFieldEAN;
	private JTextField4j jTextFieldLegacyCode;
	private JTextField4j jTextFieldLocation;
	private JTextField4j jTextFieldMaterial;
	private JTextField4j jTextFieldMaterialDescription;
	private JTextField4j jTextFieldProcessOrder;
	private JTextField4j jTextFieldProcessOrderDescription;
	private JTextField4j jTextFieldProcessOrderStatus;
	private JTextField4j jTextFieldProductionUom;
	private JTextField4j jTextFieldRecipe;
	private JTextField4j jTextFieldRecipeVersion;
	private JTextField4j jTextFieldSSCC;
	private JTextField4j jTextFieldShelfLifeRoundingRule;
	private JTextField4j jTextFieldShelfLifeUOM;
	private JTextField4j jTextFieldVariant;
	private JTextField4j textField4jCustomer = new JTextField4j();
	private JTextField4j textField4jResource = new JTextField4j();
	private JTextField4j textFieldBatchExtension;
	private LinkedList<String> ssccItems = new LinkedList<String>();
	private PreparedStatement listStatement;
	private SpinnerNumberModel copiesnumbermodel;
	private SpinnerNumberModel quantitynumbermodel = new SpinnerNumberModel();
	private SpinnerNumberModel shelflifenumbermodel = new SpinnerNumberModel();
	private String batchFormat = "";
	private String batchValidate = "";
	private String expiryMode = "";
	private String jTextFieldBaseEAN;
	private String jTextFieldBaseVariant;
	private String ssccList = "";
	private Timer timer = new Timer(1000, clocklistener);
	private boolean DOMEditable = true;
	private boolean ExpiryEditable = true;
	private boolean confirmStatus = false;
	private static final long serialVersionUID = 1;

	public JInternalFrameProductionDeclaration(String procOrder)
	{
		addInternalFrameListener(new InternalFrameAdapter()
		{

			public void internalFrameClosing(InternalFrameEvent e)
			{
				timer.stop();

				while (timer.isRunning())
				{
				}

				timer = null;
			}
		});

		maxOverMakePercentage = new BigDecimal((ctrl.getKeyValueWithDefault("MAX_OVERMAKE_PERCENTAGE", "50", "Max Permitted Percentage over Default Full Qty")));

		int copies = Integer.valueOf(ctrl.getKeyValueWithDefault("DEFAULT_LABELS_TO_PRINT", "2", "Default No of Labels to print"));
		confirmStatus = Boolean.valueOf(ctrl.getKeyValueWithDefault("PRODDEC CONFIRM CHECKBOX STATUS", "false", "Default Auto Confirm Checkbox Status"));

		copiesnumbermodel = new SpinnerNumberModel(copies, 1, 100, 1);

		DOMEditable = Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_PROD_DEC_MANUAL_EDIT_DOM");
		ExpiryEditable = Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_PROD_DEC_MANUAL_EDIT_EXPIRY");

		initGUI();
		clearFields();

		timer.start();

		jTextFieldBatch.setText("");
		textFieldBatchExtension.setText("");

		expiryMode = ctrl.getKeyValue("EXPIRY DATE MODE");

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PAL_PROD_DEC"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldProcessOrder.requestFocus();
				jTextFieldProcessOrder.setCaretPosition(jTextFieldProcessOrder.getText().length());

			}
		});

		procOrder = JUtility.replaceNullStringwithBlank(procOrder);

		processOrderChanged(procOrder);
	}

	private void buildSQL(String mList, LinkedList<String> mItems)
	{
		JDBQuery.closeStatement(listStatement);

		String temp = "";

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBPallet.selectWithExpiry");

		query.addText(temp);

		query.addText(" where sscc in (" + mList + ")");

		for (int t = 0; t <= (mItems.size() - 1); t++)
		{
			query.addParameter(mItems.get(t));
		}

		query.appendSort("sscc", "asc");
		query.applyRestriction(false, "none", 0);
		query.bindParams();

		listStatement = query.getPreparedStatement();
	}

	public void processOrderChanged(String po)
	{
		clearFields();
		checkSSCC();
		jButtonReprint.setEnabled(false);
		jTextFieldProcessOrder.setText(po);
		processorder.setProcessOrder(po);

		mod.setModuleId(labelPrint.getPalletLabelReportName(po));
		mod.getModuleProperties();

		if (mod.getReportType().equals("Label"))
		{
			jCheckBoxAutoPreview.setSelected(false);
			jCheckBoxAutoPreview.setEnabled(false);
			jSpinnerCopies.setVisible(true);
			jLabel_LabelsPerSSCC.setVisible(true);
		}
		else
		{
			jCheckBoxAutoPreview.setSelected(true);
			jCheckBoxAutoPreview.setEnabled(true);
			jSpinnerCopies.setVisible(false);
			jLabel_LabelsPerSSCC.setVisible(false);
		}

		if (processorder.getProcessOrderProperties() == true)
		{

			pallet.setProcessOrder(po);
			pallet.populateFromProcessOrder();
			jButtonAssign.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABEL_DATA_ASSIGN_TO_AUTOLAB"));
			jTextFieldProcessOrderDescription.setText(processorder.getDescription());
			jTextFieldMaterial.setText(processorder.getMaterial());

			jTextFieldProcessOrderStatus.setText(processorder.getStatus());

			if (jTextFieldProcessOrderStatus.getText().equals("Ready") || (jTextFieldProcessOrderStatus.getText().equals("Running")))
			{
				jTextFieldProcessOrderStatus.setBackground(Common.color_app_window);
			}
			else
			{
				jTextFieldProcessOrderStatus.setBackground(Color.RED);
			}

			jTextFieldRecipe.setText(processorder.getRecipe());
			jTextFieldRecipeVersion.setText(processorder.getRecipeVersion());
			jTextFieldLocation.setText(processorder.getLocation());

			try
			{
				jSpinnerDueDate.setDate(processorder.getDueDate());
			}
			catch (Exception e)
			{
			}

			material.getMaterialProperties(processorder.getMaterial());
			jTextFieldMaterialDescription.setText(material.getDescription());
			jSpinnerShelfLife.setValue((Number) material.getShelfLife());
			shelflifeuom.getShelfLifeUomProperties(material.getShelfLifeUom());
			jTextFieldShelfLifeUOM.setText(shelflifeuom.toString());
			shelfliferoundingrule.getShelfLifeRuleProperties(material.getShelfLifeRule());
			jTextFieldShelfLifeRoundingRule.setText(shelfliferoundingrule.toString());
			jFormattedTextFieldProdQuantity.setValue(JUtility.stringToBigDecimal(processorder.getFullPalletQuantity()));
			fullPalletDefaultQuantity = JUtility.stringToBigDecimal(processorder.getFullPalletQuantity());
			caseDefaultQuantity = JUtility.stringToBigDecimal(processorder.getFullPalletQuantity());
			jTextFieldLegacyCode.setText(material.getOldMaterial());
			jTextFieldProductionUom.setText(processorder.getRequiredUom());
			jTextFieldBaseUOM.setText(material.getBaseUom());
			materialuom.getMaterialUomProperties(material.getMaterial(), processorder.getRequiredUom());

			jTextFieldEAN.setText(materialuom.getEan());
			jTextFieldVariant.setText(materialuom.getVariant());

			pallet.setBatchNumber(jTextFieldBatch.getText() + textFieldBatchExtension.getText());
			pallet.setQuantity(JUtility.stringToBigDecimal(jFormattedTextFieldProdQuantity.getText().toString()));

			jFormattedTextFieldBaseQuantity.setText(pallet.getBaseQuantityAsString());

			textFieldBatchExtension.setText(processorderResource.getBatchSuffixForResource(processorder.getRequiredResource()));
			materialuom.getMaterialUomProperties(material.getMaterial(), material.getBaseUom());
			jTextFieldBaseEAN = materialuom.getEan();
			jTextFieldBaseVariant = materialuom.getVariant();
			textField4jResource.setText(processorder.getRequiredResource());
			textField4jCustomer.setText(processorder.getCustomerID());

			batchFormat = materialbatch.getBatchFormatString(processorder);
			batchValidate = materialbatch.getBatchValidationString(processorder);
			jSpinnerProductionDate.setDisplayMode(JDateControl.mode_disable_visible);
			jSpinnerExpiryDate.setDisplayMode(JDateControl.mode_disable_visible);
			jSpinnerDueDate.setDisplayMode(JDateControl.mode_disable_visible);

			calcBBEBatch();

			comboBoxPrintQueue.refreshData("RPT_PALLET_LABEL", po);
		}
		else
		{
			jSpinnerProductionDate.setDisplayMode(JDateControl.mode_disable_not_visible);
			jSpinnerExpiryDate.setDisplayMode(JDateControl.mode_disable_not_visible);
			jSpinnerDueDate.setDisplayMode(JDateControl.mode_disable_not_visible);
			jButtonAssign.setEnabled(false);
			jButtonSave.setEnabled(false);
		}
	}

	private void enableField(JComponent field, Boolean allowed)
	{
		if (allowed == true)
		{
			field.setEnabled(true);
		}
		else
		{
			field.setEnabled(false);
		}
	}

	private void clearFields()
	{

		jTextFieldProcessOrderDescription.setText("");
		jTextFieldProcessOrderStatus.setText("");
		jTextFieldRecipe.setText("");
		jTextFieldRecipeVersion.setText("");
		jTextFieldLocation.setText("");
		jTextFieldMaterial.setText("");
		jTextFieldMaterialDescription.setText("");
		jTextFieldShelfLifeUOM.setText("");
		jTextFieldShelfLifeRoundingRule.setText("");
		jTextFieldLegacyCode.setText("");
		jTextFieldBatch.setText("");
		textFieldBatchExtension.setText("");
		jTextFieldProductionUom.setText("");
		jTextFieldBaseUOM.setText("");
		jTextFieldEAN.setText("");
		jTextFieldVariant.setText("");
		jTextFieldSSCC.setText("");
		textField4jResource.setText("");
		textField4jCustomer.setText("");

		jCheckBoxProductionDate.setSelected(false);
		enableField(jSpinnerProductionDate, jCheckBoxProductionDate.isSelected());

		jCheckBoxExpiry.setSelected(false);
		enableField(jSpinnerExpiryDate, getExpiryDateManualEditStatus(jCheckBoxExpiry.isSelected()));

		jCheckBoxBatch.setSelected(false);
		enableField(jTextFieldBatch, jCheckBoxBatch.isSelected());

		jFormattedTextFieldProdQuantity.setValue(0);
		jFormattedTextFieldBaseQuantity.setValue(0);
	}

	private void checkSSCC()
	{
		boolean valid = false;
		jStatusText.setText("");
		jButtonReprint.setEnabled(false);

		if (jCheckBoxAutoSSCC.isSelected() == true)
		{
			jTextFieldSSCC.setEnabled(false);
			valid = true;
		}
		else
		{
			jTextFieldSSCC.setEnabled(true);

			if (jTextFieldSSCC.getText().length() == 18)
			{
				if (Common.barcode.validateCheckdigit(jTextFieldSSCC.getText()))
				{
					valid = true;
				}
				else
				{
					jStatusText.setText("Invalid SSCC check digit");
				}
			}
		}

		if (valid)
		{
			jTextFieldSSCC.setForeground(new java.awt.Color(51, 51, 51));
			jButtonSave.setEnabled(true);

			jFormattedTextFieldProdQuantity.setEnabled(true);

			jCheckBoxProductionDate.setEnabled(true);
			jCheckBoxExpiry.setEnabled(true);
			jCheckBoxBatch.setEnabled(true);
			enableField(jSpinnerProductionDate, getProductionDateManualEditStatus(jCheckBoxProductionDate.isSelected()));
			enableField(jSpinnerExpiryDate, getExpiryDateManualEditStatus(jCheckBoxExpiry.isSelected()));
			enableField(jTextFieldBatch, jCheckBoxBatch.isSelected());
			textFieldBatchExtension.setEnabled(true);
		}
		else
		{
			jTextFieldSSCC.setForeground(new java.awt.Color(255, 0, 0));
			jButtonSave.setEnabled(false);

			jFormattedTextFieldProdQuantity.setEnabled(false);

			jSpinnerProductionDate.setEnabled(getProductionDateManualEditStatus(false));
			jCheckBoxProductionDate.setSelected(false);
			jCheckBoxProductionDate.setEnabled(false);

			jSpinnerExpiryDate.setEnabled(getExpiryDateManualEditStatus(false));
			jCheckBoxExpiry.setSelected(false);
			jCheckBoxExpiry.setEnabled(false);

			jTextFieldBatch.setEnabled(false);
			textFieldBatchExtension.setEnabled(false);
			jCheckBoxBatch.setSelected(false);
			jCheckBoxBatch.setEnabled(false);
		}
	}

	private void calcBBEBatch()
	{
		try
		{
			String temp = "";
			Date d = jSpinnerProductionDate.getDate();
			Calendar caldate = Calendar.getInstance();
			caldate.setTime(d);

			JDBMaterialBatch mb = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
			temp = mb.getDefaultBatchNumber(batchFormat, caldate, processorder);

			if (temp.equalsIgnoreCase(jTextFieldBatch.getText()) == false)
			{
				if (jCheckBoxBatch.isSelected() == false)
				{
					jTextFieldBatch.setText(mb.getDefaultBatchNumber(batchFormat, caldate, processorder));
				}
			}

			if (jCheckBoxExpiry.isSelected() == false)
			{
				jSpinnerExpiryDate.setEnabled(getExpiryDateManualEditStatus(false));

				if (material.getMaterial().length() > 0)
				{
					if (expiryMode.equals("BATCH"))
					{
						if (materialbatch.getMaterialBatchProperties(material.getMaterial(), jTextFieldBatch.getText() + textFieldBatchExtension.getText()) == true)
						{
							try
							{
								jSpinnerExpiryDate.setDate(materialbatch.getExpiryDate());
							}
							catch (Exception e)
							{
							}
						}
						else
						{
							Date de = jSpinnerProductionDate.getDate();

							try
							{
								jSpinnerExpiryDate.setDate(material.calcBBE(de, material.getShelfLife(), material.getShelfLifeUom(), material.getShelfLifeRule()));
							}
							catch (Exception e)
							{
							}
						}
					}
					else
					{
						Date de = jSpinnerProductionDate.getDate();

						try
						{
							jSpinnerExpiryDate.setDate(material.calcBBE(de, material.getShelfLife(), material.getShelfLifeUom(), material.getShelfLifeRule()));
						}
						catch (Exception e)
						{
						}
					}
				}
			}

			jSpinnerExpiryDate.setDate(material.getRoundedExpiryDate(jSpinnerExpiryDate.getDate()));
		}
		catch (Exception e)
		{
			// logger.debug(e.getMessage());
		}
	}

	private String createLabelData(int labels)
	{
		String result = "";

		JDBLabelData labelData = new JDBLabelData(Common.selectedHostID, Common.sessionID);

		labelData.generateUniqueID();
		labelData.setPrintDate(JUtility.getSQLDateTime());
		labelData.setUserID(Common.userList.getUser(Common.sessionID).getUserId());
		labelData.setWorkstationID(JUtility.getClientName());
		labelData.setMaterial(processorder.getMaterial());
		labelData.setMaterialType(material.getMaterialType());
		labelData.setBatchNumber(jTextFieldBatch.getText() + textFieldBatchExtension.getText());
		labelData.setProcessOrder(processorder.getProcessOrder());
		labelData.setRequiredResource(processorder.getRequiredResource());
		labelData.setLocationID(processorder.getLocation());
		labelData.setProdQuantity(jFormattedTextFieldProdQuantity.getQuantity());
		labelData.setProdUom(jTextFieldProductionUom.getText());
		labelData.setBaseQuantity(jFormattedTextFieldBaseQuantity.getQuantity());
		labelData.setBaseUom(jTextFieldBaseUOM.getText());
		labelData.setDateofManufacture(JUtility.getTimestampFromDate(jSpinnerProductionDate.getDate()));
		labelData.setExpiryDate(JUtility.getTimestampFromDate(jSpinnerExpiryDate.getDate()));
		labelData.setExpiryMode(expiryMode);
		labelData.setProdEAN(jTextFieldEAN.getText());
		labelData.setProdVariant(jTextFieldVariant.getText());
		labelData.setBaseEAN(jTextFieldBaseEAN);
		labelData.setBaseVariant(jTextFieldBaseVariant);
		labelData.setCustomer(processorder.getCustomerID());
		labelData.setPrintCopies((long) labels);
		labelData.setPrintQueue(comboBoxPrintQueue.getSelectedItem().toString());
		labelData.setModuleID(labelPrint.getPalletLabelReportName(labelData.getProcessOrder()));
		labelData.setBatchPrefix(jTextFieldBatch.getText());
		labelData.setBatchSuffix(textFieldBatchExtension.getText());
		labelData.setOverrideBatchPrefix(jCheckBoxBatch.isSelected());
		labelData.setOverrideDateofManufacture(jCheckBoxProductionDate.isSelected());
		labelData.setOverrideExpiryDate(jCheckBoxExpiry.isSelected());
		labelData.setLabelType("Pallet");

		labelData.create();
		result = labelData.getUniqueID();

		return result;
	}

	private void printOrAssign(String mode)
	{
		int noOfLabels = Integer.valueOf(jSpinnerQuantity.getValue().toString());

		Boolean confirmQuantity = true;
		BigDecimal a = jFormattedTextFieldProdQuantity.getQuantity();

		if (caseDefaultQuantity.compareTo(new BigDecimal("0")) > 0)
		{
			if (a.compareTo(caseDefaultQuantity) > 0)
			{
				if (JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Quantity_Confirm"), lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16) == JOptionPane.YES_OPTION)
				{
					confirmQuantity = true;
				}
				else
				{
					confirmQuantity = false;
				}
			}
		}

		if (confirmQuantity == true)
		{

			String processOrder = processorder.getProcessOrder();
			String material = processorder.getMaterial();
			String batchNumber = jTextFieldBatch.getText() + textFieldBatchExtension.getText();
			Timestamp expiryDate = JUtility.getTimestampFromDate(jSpinnerExpiryDate.getDate());

			if (materialbatch.autoCreateMaterialBatch(material, batchNumber, batchValidate, expiryDate, ""))
			{

				String key = createLabelData(noOfLabels);

				if (mode.equals("Print"))
				{

					JPrintDevice pq = (JPrintDevice) comboBoxPrintQueue.getSelectedItem();
					buildSQL(key);

					JLaunchReport.runReport(labelPrint.getPackLabelReportName(processOrder), listStatement, jCheckBoxAutoPreview.isSelected(), pq, noOfLabels, checkBoxIncHeaderText.isSelected());

				}

				if (mode.equals("Assign"))
				{
					JLaunchMenu.runDialog("FRM_LABEL_DATA_ASSIGN", key);
				}

			}
			else
			{
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(Common.mainForm, materialbatch.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
			}
		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(674, 474));
			this.setBounds(0, 0, 773, 582);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			jDesktopPane1 = new JDesktopPane4j();
			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(665, 490));

			jDesktopPane1.setLayout(null);

			jButtonAssign = new JButton4j(Common.icon_production_line_16x16);
			jButtonAssign.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					printOrAssign("Assign");
				}
			});
			jButtonAssign.setText(lang.get("btn_Assign_to_Labeller"));
			jButtonAssign.setMnemonic('0');
			jButtonAssign.setEnabled(false);
			jButtonAssign.setBounds(156, 486, 148, 32);
			jButtonAssign.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABEL_DATA_ASSIGN_TO_AUTOLAB"));
			jDesktopPane1.add(jButtonAssign);

			jButtonSave = new JButton4j(Common.icon_label_16x16);
			jDesktopPane1.add(jButtonSave);
			jButtonSave.setEnabled(false);
			jButtonSave.setText(lang.get("btn_Label"));
			jButtonSave.setMnemonic(lang.getMnemonicChar());
			jButtonSave.setBounds(7, 486, 148, 32);
			jButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					int noOfSSCCs = 0;
					int ssccCount = 0;
					int printCount = 0;
					int confirmCount = 0;
					int createCount = 0;

					ssccList = "";
					ssccItems.clear();

					if (jCheckBoxAutoSSCC.isSelected() == false)
					{
						// If manual SSCC mode selected we can only print 1
						// label for the SSCC input
						jSpinnerQuantity.setValue(1);
					}

					noOfSSCCs = Integer.valueOf(jSpinnerQuantity.getValue().toString());

					if (jCheckBoxAutoSSCC.isSelected() == false)
					{
						ssccItems.clear();
						ssccItems.add(jTextFieldSSCC.getText());
					}
					else
					{
						ssccItems = bc.generateNewSSCCs(noOfSSCCs);
					}

					Boolean confirmQuantity = true;
					BigDecimal a = jFormattedTextFieldProdQuantity.getQuantity();

					if (fullPalletDefaultQuantity.compareTo(new BigDecimal("0")) > 0)
					{
						if (a.compareTo(fullPalletDefaultQuantity) > 0)
						{
							if (JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Quantity_Confirm"), lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16) == JOptionPane.YES_OPTION)
							{
								BigDecimal maxOverMake = fullPalletDefaultQuantity;
								maxOverMake = maxOverMake.divide(new BigDecimal(100)).multiply((new BigDecimal(100).add(maxOverMakePercentage)));

								if (a.compareTo(maxOverMake) <= 0)
								{
									confirmQuantity = true;
								}
								else
								{
									confirmQuantity = false;
									JUtility.errorBeep();
									JOptionPane.showMessageDialog(Common.mainForm, lang.get("lbl_Max_Permitted_Quantity") + " " + maxOverMake.toString() + "\n\n( " + fullPalletDefaultQuantity.toString() + " +" + maxOverMakePercentage.toString() + " % )",
											lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);

								}
							}
							else
							{
								confirmQuantity = false;
							}
						}
					}

					if (confirmQuantity == true)
					{

						for (int loop = 1; loop <= noOfSSCCs; loop++)
						{
							// Get or Generate the SSCC
							String tempSSCC = jTextFieldSSCC.getText();

							if (jCheckBoxAutoSSCC.isSelected() == true)
							{
								tempSSCC = ssccItems.get(loop - 1);
								jTextFieldSSCC.setText(tempSSCC);
								jTextFieldSSCC.repaint();
							}
							else
							{
								tempSSCC = jTextFieldSSCC.getText();
							}

							// Assign SSCC to Pallet
							pallet.clear();
							pallet.setSSCC(tempSSCC);

							// Validate that the SSCC is well formed.
							if (pallet.isValidPallet() == false)
							{
								// Assign critical fields.
								pallet.setProcessOrder(jTextFieldProcessOrder.getText());
								// Get data from the Process Order
								pallet.populateFromProcessOrder();

								pallet.setBatchNumber(jTextFieldBatch.getText() + textFieldBatchExtension.getText());
								pallet.setQuantity(jFormattedTextFieldProdQuantity.getQuantity());
								pallet.setBatchExpiry(JUtility.getTimestampFromDate(jSpinnerExpiryDate.getDate()));

								jFormattedTextFieldBaseQuantity.setText(pallet.getBaseQuantityAsString());

								pallet.setTransactionRef(0);

								if (pallet.create("PROD DEC", "CREATE") == false)
								{
									JUtility.errorBeep();
									JOptionPane.showMessageDialog(Common.mainForm, pallet.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);

									break;
								}
								else
								{
									createCount++;
									jStatusText.setText("SSCC " + tempSSCC + " created.");

									if (ssccList.isEmpty())
									{
										ssccList = ssccList + '?';
									}
									else
									{
										ssccList = ssccList + ",?";
									}

									ssccCount++;

									if (jCheckBoxAutoPrint.isSelected() == true)
									{
										pallet.writePalletHistory(pallet.getTransactionRef(), "PROD DEC", "PRINT");
										printCount++;
									}

									if (jCheckBoxAutoConfirm.isSelected() == true)
									{
										Date dom = jSpinnerProductionDate.getDate();
										pallet.setDateOfManufacture(JUtility.getTimestampFromDate(dom));

										if (pallet.confirm() == true)
										{
											jStatusText.setText("SSCC " + tempSSCC + " created and confirmed.");
											confirmCount++;
										}
										else
										{
											jStatusText.setText(pallet.getErrorMessage());
										}
									}
								}
							}
							else
							{
								// SSCC already exists
								JUtility.errorBeep();
								JOptionPane.showMessageDialog(Common.mainForm, "SSCC [" + pallet.getSSCC() + "] already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
							}
						}

						if (ssccCount > 0)
						{
							jButtonReprint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PRODDEC_REPRINT"));

							if (jCheckBoxAutoPrint.isSelected())
							{
								JPrintDevice pq = (JPrintDevice) comboBoxPrintQueue.getSelectedItem();
								buildSQL(ssccList, ssccItems);
								JLaunchReport.runReport(labelPrint.getPalletLabelReportName(pallet.getProcessOrder()), listStatement, jCheckBoxAutoPreview.isSelected(), pq, Integer.valueOf(jSpinnerCopies.getValue().toString()),
										checkBoxIncHeaderText.isSelected());
							}
							JOptionPane.showMessageDialog(Common.mainForm, "SSCC's created : " + String.valueOf(createCount) + "\nSSCC's printed : " + String.valueOf(printCount) + "\nSSCC's confirmed : " + String.valueOf(confirmCount), "Information",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			});
			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(603, 486, 148, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					timer.stop();

					while (timer.isRunning())
					{
					}

					timer = null;
					dispose();
				}
			});
			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(454, 486, 148, 32);
			jPanelProcessOrder = new JPanel4j();
			jPanelProcessOrder.setBackground(Common.color_app_window);
			jPanelProcessOrder.setFont(Common.font_title);

			jDesktopPane1.add(jPanelProcessOrder);
			jPanelProcessOrder.setBounds(7, 7, 748, 112);
			jPanelProcessOrder.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Process_Order")));
			jPanelProcessOrder.setLayout(null);

			jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
			jPanelProcessOrder.add(jTextFieldProcessOrder);
			jTextFieldProcessOrder.setBounds(161, 21, 119, 22);
			jTextFieldProcessOrder.addKeyListener(new KeyAdapter()
			{
				public void keyReleased(KeyEvent evt)
				{
					processOrderChanged(jTextFieldProcessOrder.getText());
				}
			});

			jLabel_ProcessOrder = new JLabel4j_std();
			jPanelProcessOrder.add(jLabel_ProcessOrder);
			jLabel_ProcessOrder.setText(lang.get("lbl_Process_Order"));
			jLabel_ProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_ProcessOrder.setBounds(12, 21, 142, 22);
			jTextFieldProcessOrderDescription = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldProcessOrderDescription);
			jTextFieldProcessOrderDescription.setBounds(449, 21, 287, 22);
			jTextFieldProcessOrderDescription.setEditable(false);
			jTextFieldProcessOrderDescription.setEnabled(false);
			jLabel_OrderDescription = new JLabel4j_std();
			jPanelProcessOrder.add(jLabel_OrderDescription);
			jLabel_OrderDescription.setText(lang.get("lbl_Description"));
			jLabel_OrderDescription.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_OrderDescription.setBounds(306, 21, 136, 22);
			jLabel_OrderStatus = new JLabel4j_std();
			jPanelProcessOrder.add(jLabel_OrderStatus);
			jLabel_OrderStatus.setText(lang.get("lbl_Process_Order_Status"));
			jLabel_OrderStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_OrderStatus.setBounds(290, 49, 152, 22);
			jSpinnerDueDate = new JDateControl();
			jPanelProcessOrder.add(jSpinnerDueDate);
			jSpinnerDueDate.setEnabled(false);
			jSpinnerDueDate.setBounds(161, 49, 120, 22);
			jSpinnerDueDate.getEditor().setOpaque(true);

			jLabel_DueDate = new JLabel4j_std();
			jPanelProcessOrder.add(jLabel_DueDate);
			jLabel_DueDate.setText(lang.get("lbl_Process_Order_Due_Date"));
			jLabel_DueDate.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_DueDate.setBounds(12, 49, 142, 22);
			jTextFieldProcessOrderStatus = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldProcessOrderStatus);
			jTextFieldProcessOrderStatus.setBounds(449, 49, 126, 22);
			jTextFieldProcessOrderStatus.setEditable(false);
			jTextFieldProcessOrderStatus.setEnabled(false);
			jTextFieldRecipe = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldRecipe);
			jTextFieldRecipe.setBounds(161, 77, 119, 22);
			jTextFieldRecipe.setEditable(false);
			jTextFieldRecipe.setEnabled(false);
			jTextFieldRecipeVersion = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldRecipeVersion);
			jTextFieldRecipeVersion.setBounds(300, 77, 58, 21);
			jTextFieldRecipeVersion.setEditable(false);
			jTextFieldRecipeVersion.setEnabled(false);
			jLabel_Recipe = new JLabel4j_std();
			jPanelProcessOrder.add(jLabel_Recipe);
			jLabel_Recipe.setText(lang.get("lbl_Process_Order_Recipe"));
			jLabel_Recipe.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Recipe.setBounds(12, 77, 142, 22);
			jTextFieldLocation = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldLocation);
			jTextFieldLocation.setBounds(449, 77, 126, 22);
			jTextFieldLocation.setEditable(false);
			jTextFieldLocation.setEnabled(false);
			jLabel_Location = new JLabel4j_std();
			jPanelProcessOrder.add(jLabel_Location);
			jLabel_Location.setText(lang.get("lbl_Location_ID"));
			jLabel_Location.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Location.setBounds(290, 77, 152, 22);
			jButtonPOLookup = new JButton4j(Common.icon_lookup_16x16);
			jPanelProcessOrder.add(jButtonPOLookup);
			jButtonPOLookup.setBounds(280, 21, 20, 22);
			jButtonPOLookup.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JLaunchLookup.dlgCriteriaDefault = "Ready";
					JLaunchLookup.dlgAutoExec = true;

					if (JLaunchLookup.processOrders())
					{
						String po = JLaunchLookup.dlgResult;
						processOrderChanged(po);
					}
				}
			});
			jPanelMaterial = new JPanel4j();
			jDesktopPane1.add(jPanelMaterial);
			jPanelMaterial.setBounds(7, 119, 748, 112);
			jPanelMaterial.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Material")));
			jPanelMaterial.setLayout(null);
			jPanelMaterial.setFont(Common.font_title);
			jPanelMaterial.setBackground(Common.color_app_window);
			jTextFieldMaterial = new JTextField4j();
			jPanelMaterial.add(jTextFieldMaterial);
			jTextFieldMaterial.setBounds(161, 21, 128, 22);
			jTextFieldMaterial.setEditable(false);
			jTextFieldMaterial.setEnabled(false);

			jLabel_Material = new JLabel4j_std();
			jPanelMaterial.add(jLabel_Material);
			jLabel_Material.setText(lang.get("lbl_Material"));
			jLabel_Material.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Material.setBounds(12, 21, 142, 22);
			jTextFieldMaterialDescription = new JTextField4j();
			jPanelMaterial.add(jTextFieldMaterialDescription);
			jTextFieldMaterialDescription.setBounds(449, 21, 287, 22);
			jTextFieldMaterialDescription.setEditable(false);
			jTextFieldMaterialDescription.setEnabled(false);
			jLabel_Description = new JLabel4j_std();
			jPanelMaterial.add(jLabel_Description);
			jLabel_Description.setText(lang.get("lbl_Description"));
			jLabel_Description.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Description.setBounds(291, 21, 151, 22);
			jSpinnerShelfLife = new JSpinner4j();
			jPanelMaterial.add(jSpinnerShelfLife);
			jSpinnerShelfLife.setModel(shelflifenumbermodel);
			JSpinner4j.NumberEditor nec = new JSpinner4j.NumberEditor(jSpinnerShelfLife);

			jSpinnerShelfLife.setEditor(nec);
			jSpinnerShelfLife.setBounds(161, 49, 63, 22);
			jSpinnerShelfLife.setEnabled(false);
			jTextFieldShelfLifeRoundingRule = new JTextField4j();
			jPanelMaterial.add(jTextFieldShelfLifeRoundingRule);
			jTextFieldShelfLifeRoundingRule.setBounds(638, 49, 98, 22);
			jTextFieldShelfLifeRoundingRule.setEditable(false);
			jTextFieldShelfLifeRoundingRule.setEnabled(false);
			jLabel_Rounding = new JLabel4j_std();
			jPanelMaterial.add(jLabel_Rounding);
			jLabel_Rounding.setText(lang.get("lbl_Material_Shelf_Life_Rounding_Rule"));
			jLabel_Rounding.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Rounding.setBounds(546, 49, 85, 22);
			jTextFieldShelfLifeUOM = new JTextField4j();
			jPanelMaterial.add(jTextFieldShelfLifeUOM);
			jTextFieldShelfLifeUOM.setBounds(449, 49, 91, 22);
			jTextFieldShelfLifeUOM.setEditable(false);
			jTextFieldShelfLifeUOM.setEnabled(false);
			jLabel_ShelfLifeUOM = new JLabel4j_std();
			jPanelMaterial.add(jLabel_ShelfLifeUOM);
			jLabel_ShelfLifeUOM.setText(lang.get("lbl_Material_Shelf_Life_UOM"));
			jLabel_ShelfLifeUOM.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_ShelfLifeUOM.setBounds(281, 49, 161, 22);
			jLabel_ShelfLife = new JLabel4j_std();
			jPanelMaterial.add(jLabel_ShelfLife);
			jLabel_ShelfLife.setText(lang.get("lbl_Material_Shelf_Life"));
			jLabel_ShelfLife.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_ShelfLife.setBounds(12, 49, 142, 22);
			jTextFieldLegacyCode = new JTextField4j();
			jPanelMaterial.add(jTextFieldLegacyCode);
			jTextFieldLegacyCode.setBounds(161, 77, 128, 22);
			jTextFieldLegacyCode.setEditable(false);
			jTextFieldLegacyCode.setEnabled(false);
			jLabel_LegacyCode = new JLabel4j_std();
			jPanelMaterial.add(jLabel_LegacyCode);
			jLabel_LegacyCode.setText(lang.get("lbl_Material_Legacy_Code"));
			jLabel_LegacyCode.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_LegacyCode.setBounds(12, 77, 142, 22);
			jLabel_EAN = new JLabel4j_std();
			jPanelMaterial.add(jLabel_EAN);
			jLabel_EAN.setText(lang.get("lbl_Material_UOM_EAN"));
			jLabel_EAN.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_EAN.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_EAN.setBounds(291, 77, 151, 22);
			jTextFieldEAN = new JTextField4j();
			jPanelMaterial.add(jTextFieldEAN);
			jTextFieldEAN.setBounds(449, 77, 126, 22);
			jTextFieldEAN.setFocusCycleRoot(true);
			jTextFieldEAN.setEditable(false);
			jTextFieldEAN.setEnabled(false);
			jLabel_Variant = new JLabel4j_std();
			jPanelMaterial.add(jLabel_Variant);
			jLabel_Variant.setText(lang.get("lbl_Material_UOM_Variant"));
			jLabel_Variant.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Variant.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Variant.setBounds(579, 77, 114, 22);
			jTextFieldVariant = new JTextField4j();
			jPanelMaterial.add(jTextFieldVariant);
			jTextFieldVariant.setBounds(701, 77, 35, 22);
			jTextFieldVariant.setFocusCycleRoot(true);
			jTextFieldVariant.setEditable(false);
			jTextFieldVariant.setEnabled(false);
			jPanelPallet = new JPanel4j();
			jDesktopPane1.add(jPanelPallet);
			jPanelPallet.setBounds(7, 231, 748, 84);
			jPanelPallet.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Pallet")));
			jPanelPallet.setLayout(null);
			jPanelPallet.setFont(Common.font_std);
			jPanelPallet.setBackground(Common.color_app_window);
			JLabel4j_std jLabel_Quantity = new JLabel4j_std();
			jPanelPallet.add(jLabel_Quantity);
			jLabel_Quantity.setText(lang.get("lbl_Pallet_Quantity"));
			jLabel_Quantity.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Quantity.setBounds(323, 18, 119, 22);

			jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
			jPanelPallet.add(jTextFieldSSCC);
			jTextFieldSSCC.setBounds(161, 18, 128, 22);
			jTextFieldSSCC.setEnabled(false);
			jTextFieldSSCC.addKeyListener(new KeyAdapter()
			{
				public void keyReleased(KeyEvent evt)
				{
					checkSSCC();
				}
			});

			jSpinnerProductionDate = new JDateControl();
			jPanelPallet.add(jSpinnerProductionDate);

			jSpinnerProductionDate.setBounds(161, 46, 120, 22);
			jSpinnerProductionDate.getEditor().addKeyListener(new KeyAdapter()
			{
				public void keyPressed(KeyEvent e)
				{
					calcBBEBatch();
				}
			});
			jSpinnerProductionDate.addChangeListener(new ChangeListener()
			{
				public void stateChanged(final ChangeEvent e)
				{
					calcBBEBatch();
				}
			});

			try
			{
				jSpinnerProductionDate.setDate(JUtility.getSQLDate());
				jSpinnerProductionDate.setEnabled(getProductionDateManualEditStatus(false));
			}
			catch (Exception e)
			{
			}

			jLabel_ProductionDate = new JLabel4j_std();
			jPanelPallet.add(jLabel_ProductionDate);
			jLabel_ProductionDate.setText(lang.get("lbl_Pallet_DOM"));
			jLabel_ProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_ProductionDate.setBounds(12, 46, 122, 22);
			jFormattedTextFieldProdQuantity = new JQuantityInput(new BigDecimal("0"));

			jPanelPallet.add(jFormattedTextFieldProdQuantity);
			jFormattedTextFieldProdQuantity.setBounds(449, 18, 91, 22);
			jFormattedTextFieldProdQuantity.setValue(0);
			jFormattedTextFieldProdQuantity.setEnabled(false);
			jFormattedTextFieldProdQuantity.addKeyListener(new KeyAdapter()
			{
				public void keyReleased(KeyEvent evt)
				{
					pallet.setUom(jTextFieldProductionUom.getText());
					pallet.setQuantity(JUtility.stringToBigDecimal(jFormattedTextFieldProdQuantity.getText().toString()));
					jFormattedTextFieldBaseQuantity.setText(pallet.getBaseQuantityAsString());
				}
			});
			jLabel_SSCC = new JLabel4j_std();
			jPanelPallet.add(jLabel_SSCC);
			jLabel_SSCC.setText(lang.get("lbl_Pallet_SSCC"));
			jLabel_SSCC.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_SSCC.setBounds(12, 18, 122, 22);
			jTextFieldProductionUom = new JTextField4j();
			jPanelPallet.add(jTextFieldProductionUom);
			jTextFieldProductionUom.setBounds(666, 18, 70, 22);
			jTextFieldProductionUom.setEditable(false);
			jTextFieldProductionUom.setEnabled(false);
			jLabel_ProductionEAN = new JLabel4j_std();
			jPanelPallet.add(jLabel_ProductionEAN);
			jLabel_ProductionEAN.setText(lang.get("lbl_Pallet_UOM"));
			jLabel_ProductionEAN.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_ProductionEAN.setBounds(546, 18, 113, 22);
			jLabel_BaseQuantity = new JLabel4j_std();
			jPanelPallet.add(jLabel_BaseQuantity);
			jLabel_BaseQuantity.setText(lang.get("lbl_Pallet_Base_Quantity"));
			jLabel_BaseQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_BaseQuantity.setBounds(323, 46, 119, 22);
			jLabel_BaseUOM = new JLabel4j_std();
			jPanelPallet.add(jLabel_BaseUOM);
			jLabel_BaseUOM.setText(lang.get("lbl_Pallet_Base_UOM"));
			jLabel_BaseUOM.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_BaseUOM.setBounds(546, 46, 113, 22);
			jTextFieldBaseUOM = new JTextField4j();
			jPanelPallet.add(jTextFieldBaseUOM);
			jTextFieldBaseUOM.setEditable(false);
			jTextFieldBaseUOM.setEnabled(false);
			jTextFieldBaseUOM.setBounds(666, 46, 70, 22);
			jFormattedTextFieldBaseQuantity = new JQuantityInput(new BigDecimal("0"));
			jPanelPallet.add(jFormattedTextFieldBaseQuantity);
			jFormattedTextFieldBaseQuantity.setText("0.000");
			jFormattedTextFieldBaseQuantity.setEnabled(false);
			jFormattedTextFieldBaseQuantity.setBounds(449, 46, 91, 22);
			jCheckBoxProductionDate = new JCheckBox4j();
			jPanelPallet.add(jCheckBoxProductionDate);
			jCheckBoxProductionDate.setBounds(139, 46, 21, 22);
			jCheckBoxProductionDate.setEnabled(false);
			jCheckBoxProductionDate.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					enableField(jSpinnerProductionDate, getProductionDateManualEditStatus(jCheckBoxProductionDate.isSelected()));
					enableField(calendarButtonjSpinnerProductionDate, jCheckBoxProductionDate.isSelected());
				}
			});
			jCheckBoxAutoSSCC = new JCheckBox4j();
			jCheckBoxAutoSSCC.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					checkSSCC();
				}
			});
			jCheckBoxAutoSSCC.setSelected(true);
			jCheckBoxAutoSSCC.setToolTipText("Auto SSCC");
			jCheckBoxAutoSSCC.setBackground(new Color(255, 255, 255));
			jCheckBoxAutoSSCC.setBounds(139, 18, 21, 22);
			jPanelPallet.add(jCheckBoxAutoSSCC);

			calendarButtonjSpinnerProductionDate = new JCalendarButton(jSpinnerProductionDate);
			calendarButtonjSpinnerProductionDate.setEnabled(false);
			calendarButtonjSpinnerProductionDate.setBounds(280, 47, 21, 21);
			jPanelPallet.add(calendarButtonjSpinnerProductionDate);
			jPanelBatch = new JPanel4j();
			jDesktopPane1.add(jPanelBatch);
			jPanelBatch.setBounds(7, 315, 748, 56);
			jPanelBatch.setLayout(null);
			jPanelBatch.setFont(Common.font_std);
			jPanelBatch.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Material_Batch")));
			jPanelBatch.setBackground(Common.color_app_window);
			jLabel_Batch = new JLabel4j_std();
			jPanelBatch.add(jLabel_Batch);
			jLabel_Batch.setText(lang.get("lbl_Material_Batch"));
			jLabel_Batch.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Batch.setBounds(320, 21, 96, 22);

			jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
			jPanelBatch.add(jTextFieldBatch);
			jTextFieldBatch.setBounds(449, 21, 108, 22);
			jTextFieldBatch.setEnabled(false);

			jSpinnerExpiryDate = new JDateControl();
			jSpinnerExpiryDate.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(FocusEvent arg0)
				{
					jSpinnerExpiryDate.setDate(material.getRoundedExpiryDate(jSpinnerExpiryDate.getDate()));
				}
			});
			jPanelBatch.add(jSpinnerExpiryDate);

			jSpinnerExpiryDate.setBounds(161, 21, 120, 22);
			jSpinnerExpiryDate.getEditor().setPreferredSize(new java.awt.Dimension(87, 19));
			jSpinnerExpiryDate.getEditor().setSize(87, 21);
			jSpinnerExpiryDate.setEnabled(getExpiryDateManualEditStatus(false));
			jLabel_BatchExpiry = new JLabel4j_std();
			jPanelBatch.add(jLabel_BatchExpiry);
			jLabel_BatchExpiry.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
			jLabel_BatchExpiry.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_BatchExpiry.setBounds(12, 21, 122, 22);
			jCheckBoxExpiry = new JCheckBox4j();
			jPanelBatch.add(jCheckBoxExpiry);
			jCheckBoxExpiry.setBounds(139, 21, 21, 22);
			jCheckBoxExpiry.setEnabled(false);
			jCheckBoxExpiry.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					enableField(jSpinnerExpiryDate, getExpiryDateManualEditStatus(jCheckBoxExpiry.isSelected()));
					enableField(calendarButtonjSpinnerExpiryDate, jCheckBoxExpiry.isSelected());
					calcBBEBatch();
				}
			});
			jCheckBoxBatch = new JCheckBox4j();
			jPanelBatch.add(jCheckBoxBatch);
			jCheckBoxBatch.setEnabled(false);
			jCheckBoxBatch.setBounds(422, 21, 21, 22);
			jCheckBoxBatch.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					enableField(jTextFieldBatch, jCheckBoxBatch.isSelected());
					calcBBEBatch();
				}
			});
			jStatusText = new JLabel4j_status();
			jDesktopPane1.add(jStatusText);
			jStatusText.setBounds(7, 526, 744, 21);

			jButtonReprint = new JButton4j(Common.icon_report_16x16);
			jButtonReprint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buildSQL(ssccList, ssccItems);
					JPrintDevice pq = (JPrintDevice) comboBoxPrintQueue.getSelectedItem();

					JLaunchReport.runReport(labelPrint.getPalletLabelReportName(pallet.getProcessOrder()), listStatement, jCheckBoxAutoPreview.isSelected(), pq, Integer.valueOf(jSpinnerCopies.getValue().toString()), checkBoxIncHeaderText.isSelected());

				}
			});
			jButtonReprint.setMnemonic(KeyEvent.VK_C);
			jButtonReprint.setText(lang.get("btn_Re_Print"));
			jButtonReprint.setBounds(305, 486, 148, 32);
			jButtonReprint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PRODDEC_REPRINT"));
			jDesktopPane1.add(jButtonReprint);

			final JPanel4j panel = new JPanel4j();
			panel.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Options")));
			panel.setBackground(Common.color_app_window);
			panel.setFont(Common.font_title);
			panel.setLayout(null);
			panel.setBounds(7, 374, 748, 73);
			jDesktopPane1.add(panel);
			jLabel_PrintLabel = new JLabel4j_std();
			jLabel_PrintLabel.setBounds(0, 15, 129, 22);
			panel.add(jLabel_PrintLabel);
			jLabel_PrintLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_PrintLabel.setText(lang.get("lbl_Print"));
			jCheckBoxAutoPrint = new JCheckBox4j();
			jCheckBoxAutoPrint.setBounds(139, 15, 21, 22);
			panel.add(jCheckBoxAutoPrint);
			jCheckBoxAutoPrint.setToolTipText("Auto SSCC");
			jCheckBoxAutoPrint.setSelected(true);
			jCheckBoxAutoPrint.setBackground(new Color(255, 255, 255));
			jCheckBoxAutoPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PRODDEC_PRINT"));
			jLabel_Preview = new JLabel4j_std();
			jLabel_Preview.setBounds(282, 15, 138, 22);
			panel.add(jLabel_Preview);
			jLabel_Preview.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabel_Preview.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Preview.setText(lang.get("lbl_Preview"));
			jCheckBoxAutoPreview = new JCheckBox4j();
			jCheckBoxAutoPreview.setBounds(424, 15, 21, 22);
			panel.add(jCheckBoxAutoPreview);
			jCheckBoxAutoPreview.setToolTipText("Auto SSCC");
			jCheckBoxAutoPreview.setSelected(true);
			jCheckBoxAutoPreview.setBackground(new Color(255, 255, 255));
			jCheckBoxAutoPreview.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PRODDEC_PREVIEW"));
			jLabel_Header = new JLabel4j_std();
			jLabel_Header.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Header.setText(lang.get("lbl_Label_Header_Text"));
			jLabel_Header.setBounds(10, 44, 119, 22);
			panel.add(jLabel_Header);
			jCheckBoxAutoConfirm = new JCheckBox4j();
			jCheckBoxAutoConfirm.setSelected(confirmStatus);
			jCheckBoxAutoConfirm.setBackground(Color.WHITE);
			jCheckBoxAutoConfirm.setBounds(424, 44, 21, 22);
			panel.add(jCheckBoxAutoConfirm);
			jCheckBoxAutoConfirm.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PRODDEC_CONFIRM"));
			jLabel_NoOfSSCC = new JLabel4j_std();
			jLabel_NoOfSSCC.setBounds(474, 15, 201, 22);
			panel.add(jLabel_NoOfSSCC);
			jLabel_NoOfSSCC.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_NoOfSSCC.setText(lang.get("lbl_Number_of_SSCCs"));

			jSpinnerQuantity = new JSpinner4j();
			jSpinnerQuantity.addChangeListener(new ChangeListener()
			{
				public void stateChanged(final ChangeEvent e)
				{
					int t = Integer.valueOf(jSpinnerQuantity.getValue().toString());

					if (t <= 0)
					{
						jSpinnerQuantity.setValue(1);
					}

					if (t > 50)
					{
						jSpinnerQuantity.setValue(50);
					}
				}
			});
			jSpinnerQuantity.setBounds(687, 15, 49, 22);
			jSpinnerQuantity.setInputVerifier(null);
			jSpinnerQuantity.setModel(quantitynumbermodel);
			jSpinnerQuantity.setValue(1);
			JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(jSpinnerQuantity);

			jSpinnerQuantity.setEditor(ne);
			panel.add(jSpinnerQuantity);

			jSpinnerCopies = new JSpinner4j();
			jSpinnerCopies.setBounds(687, 44, 49, 22);
			jSpinnerCopies.setInputVerifier(null);
			jSpinnerCopies.setModel(copiesnumbermodel);
			JSpinner4j.NumberEditor nec2 = new JSpinner4j.NumberEditor(jSpinnerCopies);

			jSpinnerCopies.setEditor(nec2);
			panel.add(jSpinnerCopies);

			jLabel_LabelsPerSSCC.setText((String) null);
			jLabel_LabelsPerSSCC.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_LabelsPerSSCC.setBounds(474, 44, 201, 22);
			jLabel_LabelsPerSSCC.setText(lang.get("lbl_Labels_Per_SSCC"));
			panel.add(jLabel_LabelsPerSSCC);

			jLabel_Confirmed = new JLabel4j_std();
			jLabel_Confirmed.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Confirmed.setText(lang.get("lbl_Confirmed"));
			jLabel_Confirmed.setBounds(292, 44, 129, 22);
			panel.add(jLabel_Confirmed);

			checkBoxIncHeaderText = new JCheckBox4j();
			checkBoxIncHeaderText.setSelected(true);
			checkBoxIncHeaderText.setBackground(Color.WHITE);
			checkBoxIncHeaderText.setBounds(139, 44, 21, 22);
			panel.add(checkBoxIncHeaderText);

			textFieldBatchExtension = new JTextField4j();
			textFieldBatchExtension.setText("");
			textFieldBatchExtension.setBounds(561, 21, 39, 22);
			jPanelBatch.add(textFieldBatchExtension);

			calendarButtonjSpinnerExpiryDate = new JCalendarButton(jSpinnerExpiryDate);
			calendarButtonjSpinnerExpiryDate.setEnabled(false);
			calendarButtonjSpinnerExpiryDate.setBounds(280, 18, 21, 21);
			jPanelBatch.add(calendarButtonjSpinnerExpiryDate);

			jLabel_PrintQueue = new JLabel4j_std(lang.get("lbl_Print_Queue"));
			jLabel_PrintQueue.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_PrintQueue.setBounds(7, 455, 91, 22);
			jDesktopPane1.add(jLabel_PrintQueue);

			textField4jResource.setText("");
			textField4jResource.setEnabled(false);
			textField4jResource.setEditable(false);
			textField4jResource.setBounds(588, 49, 148, 22);
			jPanelProcessOrder.add(textField4jResource);

			textField4jCustomer.setText("");
			textField4jCustomer.setEnabled(false);
			textField4jCustomer.setEditable(false);
			textField4jCustomer.setBounds(587, 77, 148, 22);
			jPanelProcessOrder.add(textField4jCustomer);

			JLabel4j_std jLabelRecipeVersion = new JLabel4j_std();
			jLabelRecipeVersion.setText("/");
			jLabelRecipeVersion.setHorizontalAlignment(SwingConstants.CENTER);
			jLabelRecipeVersion.setBounds(280, 77, 21, 21);
			jPanelProcessOrder.add(jLabelRecipeVersion);

			comboBoxPrintQueue = new JComboBoxPODevices4j(Common.selectedHostID, Common.sessionID, "RPT_PALLET_LABEL", "");
			comboBoxPrintQueue.setBounds(131, 455, 622, 22);
			jDesktopPane1.add(comboBoxPrintQueue);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public class ClockListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Calendar rightNow = Calendar.getInstance();
			Date d = rightNow.getTime();

			try
			{
				if (jCheckBoxProductionDate.isSelected() == false)
				{
					jSpinnerProductionDate.setDate(d);
				}
				else
				{
					calcBBEBatch();
				}

			}
			catch (Exception e)
			{
			}
		}
	}

	private boolean getProductionDateManualEditStatus(boolean requestedStatus)
	{
		boolean result = DOMEditable;

		if (DOMEditable)
		{
			result = requestedStatus;
		}

		return result;
	}

	private boolean getExpiryDateManualEditStatus(boolean requestedStatus)
	{
		boolean result = ExpiryEditable;

		if (ExpiryEditable)
		{
			result = requestedStatus;
		}

		return result;
	}

	private void buildSQL(String key)
	{
		JDBQuery.closeStatement(listStatement);

		String temp = "";

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBLabelData.select");

		query.addText(temp);

		query.addParameter(key);

		query.applyRestriction(false, "none", 0);
		query.bindParams();

		listStatement = query.getPreparedStatement();
	}
}
